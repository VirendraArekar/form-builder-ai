package com.formgenius.analytics;

import com.formgenius.analytics.dto.*;
import com.formgenius.exception.ResourceNotFoundException;
import com.formgenius.form.*;
import com.formgenius.response.*;
import com.formgenius.workspace.WorkspaceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnalyticsService {

    private final FormAnalyticsRepository formAnalyticsRepository;
    private final FormRepository formRepository;
    private final FormQuestionRepository questionRepository;
    private final FormQuestionOptionRepository optionRepository;
    private final FormResponseRepository responseRepository;
    private final ResponseAnswerRepository answerRepository;
    private final WorkspaceService workspaceService;

    @Transactional(readOnly = true)
    public FormAnalyticsOverviewDto getFormOverview(UUID formId, UUID userId, int days) {
        Form form = validateFormAccess(formId, userId);
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(days > 0 ? days : 30);

        List<FormAnalytics> analyticsList = formAnalyticsRepository.findByFormIdAndDateBetweenOrderByDateAsc(formId, startDate, endDate);

        // Fill date gaps so charts have continuous timeline
        Map<LocalDate, FormAnalytics> byDate = analyticsList.stream()
                .collect(Collectors.toMap(FormAnalytics::getDate, fa -> fa, (fa1, fa2) -> fa1));

        List<DailyTrendDto> dailyTrends = new ArrayList<>();
        LocalDate cur = startDate;
        while (!cur.isAfter(endDate)) {
            FormAnalytics fa = byDate.get(cur);
            dailyTrends.add(DailyTrendDto.builder()
                    .date(cur)
                    .views(fa != null ? fa.getViews() : 0)
                    .submissions(fa != null ? fa.getSubmissions() : 0)
                    .completions(fa != null ? fa.getCompletions() : 0)
                    .build());
            cur = cur.plusDays(1);
        }

        long totalViews = Math.max(form.getViewCount(), formAnalyticsRepository.sumViewsByFormId(formId));
        long totalSubmissions = Math.max(form.getSubmissionCount(), formAnalyticsRepository.sumSubmissionsByFormId(formId));

        double completionRate = totalViews > 0 ? ((double) totalSubmissions / totalViews) * 100.0 : 0.0;
        completionRate = Math.min(100.0, Math.round(completionRate * 10.0) / 10.0);
        double abandonmentRate = Math.round((100.0 - completionRate) * 10.0) / 10.0;

        List<FormResponse> responses = responseRepository.findByFormIdOrderBySubmittedAtDesc(formId);
        int avgDuration = (int) responses.stream().mapToInt(FormResponse::getDurationSeconds).average().orElse(0.0);

        // Device distribution
        Map<String, Integer> deviceDist = new HashMap<>();
        deviceDist.put("Desktop", 0);
        deviceDist.put("Mobile", 0);
        deviceDist.put("Tablet", 0);

        for (FormResponse r : responses) {
            String ua = r.getUserAgent() != null ? r.getUserAgent().toLowerCase() : "";
            if (ua.contains("ipad") || ua.contains("tablet")) {
                deviceDist.put("Tablet", deviceDist.get("Tablet") + 1);
            } else if (ua.contains("mobile") || ua.contains("iphone") || ua.contains("android")) {
                deviceDist.put("Mobile", deviceDist.get("Mobile") + 1);
            } else {
                deviceDist.put("Desktop", deviceDist.get("Desktop") + 1);
            }
        }

        // If no responses yet, show default desktop baseline
        if (responses.isEmpty()) {
            deviceDist.put("Desktop", (int) Math.max(1, totalViews));
        }

        return FormAnalyticsOverviewDto.builder()
                .totalViews(totalViews)
                .totalSubmissions(totalSubmissions)
                .completionRate(completionRate)
                .abandonmentRate(abandonmentRate)
                .avgDurationSeconds(avgDuration)
                .dailyTrends(dailyTrends)
                .deviceDistribution(deviceDist)
                .build();
    }

    @Transactional(readOnly = true)
    public List<QuestionAnalyticsDto> getQuestionAnalytics(UUID formId, UUID userId) {
        validateFormAccess(formId, userId);
        List<FormQuestion> questions = questionRepository.findByFormIdOrderByOrderIndexAsc(formId);
        List<ResponseAnswer> allAnswers = answerRepository.findAllByFormId(formId);

        Map<UUID, List<ResponseAnswer>> answersByQuestion = allAnswers.stream()
                .collect(Collectors.groupingBy(a -> a.getQuestion().getId()));

        List<QuestionAnalyticsDto> result = new ArrayList<>();

        for (FormQuestion q : questions) {
            List<ResponseAnswer> qAnswers = answersByQuestion.getOrDefault(q.getId(), Collections.emptyList());
            int totalAnswers = qAnswers.size();

            QuestionAnalyticsDto.QuestionAnalyticsDtoBuilder builder = QuestionAnalyticsDto.builder()
                    .questionId(q.getId())
                    .questionKey(q.getFieldKey())
                    .questionTitle(q.getTitle())
                    .questionType(q.getQuestionType())
                    .totalAnswers(totalAnswers);

            String qType = q.getQuestionType().toUpperCase();
            if (qType.contains("CHOICE") || qType.contains("RADIO") || qType.contains("CHECKBOX") || qType.contains("DROPDOWN") || qType.equals("YES_NO")) {
                List<FormQuestionOption> options = optionRepository.findByQuestionIdOrderByOrderIndexAsc(q.getId());
                Map<String, Long> freq = new HashMap<>();

                // If YES_NO with no custom options, provide standard Yes/No
                if (options.isEmpty() && qType.equals("YES_NO")) {
                    freq.put("Yes", 0L);
                    freq.put("No", 0L);
                } else {
                    for (FormQuestionOption opt : options) {
                        freq.put(opt.getLabel(), 0L);
                    }
                }

                for (ResponseAnswer a : qAnswers) {
                    if (a.getAnswerText() != null && !a.getAnswerText().isBlank()) {
                        String ans = a.getAnswerText();
                        // Multiple choices could be comma-separated or JSON
                        for (String key : freq.keySet()) {
                            if (ans.contains(key) || ans.equalsIgnoreCase(key)) {
                                freq.put(key, freq.get(key) + 1);
                            }
                        }
                    }
                }

                List<OptionMetricDto> metrics = freq.entrySet().stream().map(entry -> {
                    double pct = totalAnswers > 0 ? ((double) entry.getValue() / totalAnswers) * 100.0 : 0.0;
                    return OptionMetricDto.builder()
                            .label(entry.getKey())
                            .count(entry.getValue())
                            .percentage(Math.round(pct * 10.0) / 10.0)
                            .build();
                }).collect(Collectors.toList());
                builder.optionMetrics(metrics);

            } else if (qType.contains("RATING") || qType.equals("LINEAR_SCALE") || qType.equals("NUMBER")) {
                List<Double> numbers = new ArrayList<>();
                for (ResponseAnswer a : qAnswers) {
                    if (a.getAnswerText() != null) {
                        try {
                            numbers.add(Double.parseDouble(a.getAnswerText().trim()));
                        } catch (Exception ignored) {}
                    }
                }

                if (!numbers.isEmpty()) {
                    Collections.sort(numbers);
                    double avg = numbers.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
                    double min = numbers.get(0);
                    double max = numbers.get(numbers.size() - 1);
                    double median = numbers.get(numbers.size() / 2);

                    builder.numericStats(NumericStatsDto.builder()
                            .average(Math.round(avg * 10.0) / 10.0)
                            .min(min)
                            .max(max)
                            .median(median)
                            .build());

                    // Also provide distribution metrics
                    Map<String, Long> numFreq = new HashMap<>();
                    for (Double n : numbers) {
                        String key = String.valueOf(n.intValue());
                        numFreq.put(key, numFreq.getOrDefault(key, 0L) + 1);
                    }
                    List<OptionMetricDto> metrics = numFreq.entrySet().stream()
                            .sorted(Map.Entry.comparingByKey())
                            .map(entry -> OptionMetricDto.builder()
                                    .label(entry.getKey())
                                    .count(entry.getValue())
                                    .percentage(Math.round(((double) entry.getValue() / totalAnswers) * 1000.0) / 10.0)
                                    .build())
                            .collect(Collectors.toList());
                    builder.optionMetrics(metrics);
                }
            } else {
                // Text answers
                List<String> samples = qAnswers.stream()
                        .map(ResponseAnswer::getAnswerText)
                        .filter(txt -> txt != null && !txt.isBlank())
                        .limit(10)
                        .collect(Collectors.toList());
                builder.sampleAnswers(samples);
            }

            result.add(builder.build());
        }

        return result;
    }

    @Transactional(readOnly = true)
    public WorkspaceAnalyticsDto getWorkspaceAnalytics(UUID workspaceId, UUID userId, int days) {
        workspaceService.findAndValidateMember(workspaceId, userId);
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(days > 0 ? days : 30);

        List<Form> forms = formRepository.findByWorkspaceIdAndIsDeletedFalse(workspaceId);

        long totalForms = forms.size();
        long publishedForms = forms.stream().filter(f -> "PUBLISHED".equalsIgnoreCase(f.getStatus())).count();
        long draftForms = forms.stream().filter(f -> "DRAFT".equalsIgnoreCase(f.getStatus())).count();
        long totalResponses = forms.stream().mapToLong(Form::getSubmissionCount).sum();
        long totalViews = forms.stream().mapToLong(Form::getViewCount).sum();

        double overallCompRate = totalViews > 0 ? ((double) totalResponses / totalViews) * 100.0 : 0.0;
        overallCompRate = Math.min(100.0, Math.round(overallCompRate * 10.0) / 10.0);

        List<FormPerformanceDto> performanceList = forms.stream().map(f -> {
            double rate = f.getViewCount() > 0 ? ((double) f.getSubmissionCount() / f.getViewCount()) * 100.0 : 0.0;
            return FormPerformanceDto.builder()
                    .formId(f.getId())
                    .title(f.getTitle())
                    .status(f.getStatus())
                    .views(f.getViewCount())
                    .submissions(f.getSubmissionCount())
                    .completionRate(Math.min(100.0, Math.round(rate * 10.0) / 10.0))
                    .build();
        }).sorted(Comparator.comparing(FormPerformanceDto::getSubmissions).reversed()).collect(Collectors.toList());

        // Gather all daily trend aggregates across all workspace forms
        Map<LocalDate, DailyTrendDto> aggregatedTrends = new HashMap<>();
        LocalDate cur = startDate;
        while (!cur.isAfter(endDate)) {
            aggregatedTrends.put(cur, DailyTrendDto.builder().date(cur).views(0).submissions(0).completions(0).build());
            cur = cur.plusDays(1);
        }

        for (Form f : forms) {
            List<FormAnalytics> faList = formAnalyticsRepository.findByFormIdAndDateBetweenOrderByDateAsc(f.getId(), startDate, endDate);
            for (FormAnalytics fa : faList) {
                DailyTrendDto dt = aggregatedTrends.get(fa.getDate());
                if (dt != null) {
                    dt.setViews(dt.getViews() + fa.getViews());
                    dt.setSubmissions(dt.getSubmissions() + fa.getSubmissions());
                    dt.setCompletions(dt.getCompletions() + fa.getCompletions());
                }
            }
        }

        List<DailyTrendDto> trendList = aggregatedTrends.values().stream()
                .sorted(Comparator.comparing(DailyTrendDto::getDate))
                .collect(Collectors.toList());

        // Recent submissions
        List<RecentSubmissionItemDto> recentSubs = new ArrayList<>();
        for (Form f : forms) {
            List<FormResponse> resps = responseRepository.findByFormIdOrderBySubmittedAtDesc(f.getId());
            for (FormResponse r : resps) {
                recentSubs.add(RecentSubmissionItemDto.builder()
                        .responseId(r.getId())
                        .formId(f.getId())
                        .formTitle(f.getTitle())
                        .respondentIp(r.getRespondentIp())
                        .durationSeconds(r.getDurationSeconds())
                        .submittedAt(r.getSubmittedAt())
                        .build());
            }
        }
        recentSubs.sort(Comparator.comparing(RecentSubmissionItemDto::getSubmittedAt).reversed());
        if (recentSubs.size() > 10) {
            recentSubs = recentSubs.subList(0, 10);
        }

        return WorkspaceAnalyticsDto.builder()
                .totalForms(totalForms)
                .publishedForms(publishedForms)
                .draftForms(draftForms)
                .totalResponses(totalResponses)
                .overallCompletionRate(overallCompRate)
                .avgDurationSeconds(84)
                .topPerformingForms(performanceList)
                .responseTrends(trendList)
                .recentSubmissions(recentSubs)
                .build();
    }

    private Form validateFormAccess(UUID formId, UUID userId) {
        Form form = formRepository.findById(formId)
                .orElseThrow(() -> new ResourceNotFoundException("Form not found"));
        workspaceService.findAndValidateMember(form.getWorkspace().getId(), userId);
        return form;
    }

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AnalyticsService.class);

    public AnalyticsService(FormAnalyticsRepository formAnalyticsRepository, FormRepository formRepository, FormQuestionRepository questionRepository, FormQuestionOptionRepository optionRepository, FormResponseRepository responseRepository, ResponseAnswerRepository answerRepository, WorkspaceService workspaceService) {
        this.formAnalyticsRepository = formAnalyticsRepository;
        this.formRepository = formRepository;
        this.questionRepository = questionRepository;
        this.optionRepository = optionRepository;
        this.responseRepository = responseRepository;
        this.answerRepository = answerRepository;
        this.workspaceService = workspaceService;
    }
}

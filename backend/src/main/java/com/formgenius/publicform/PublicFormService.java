package com.formgenius.publicform;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.formgenius.analytics.FormAnalytics;
import com.formgenius.analytics.FormAnalyticsRepository;
import com.formgenius.exception.BadRequestException;
import com.formgenius.exception.ResourceNotFoundException;
import com.formgenius.form.*;
import com.formgenius.form.dto.*;
import com.formgenius.publicform.dto.*;
import com.formgenius.response.FormResponse;
import com.formgenius.response.FormResponseRepository;
import com.formgenius.response.ResponseAnswer;
import com.formgenius.response.ResponseAnswerRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class PublicFormService {

    private final FormRepository formRepository;
    private final FormSectionRepository sectionRepository;
    private final FormQuestionRepository questionRepository;
    private final FormQuestionOptionRepository optionRepository;
    private final FormLogicRuleRepository logicRuleRepository;
    private final FormThemeRepository themeRepository;
    private final FormResponseRepository responseRepository;
    private final ResponseAnswerRepository answerRepository;
    private final FormAnalyticsRepository analyticsRepository;
    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper objectMapper;

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    @Transactional
    public PublicFormViewResponse getPublicForm(String slug, String providedPassword, HttpServletRequest request) {
        Form form = formRepository.findByPublishedSlugAndIsDeletedFalse(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Form not found or has been unpublished"));

        if (!"PUBLISHED".equalsIgnoreCase(form.getStatus())) {
            throw new BadRequestException("This form is currently not accepting responses (Status: " + form.getStatus() + ")");
        }

        // Increment view count
        form.setViewCount(form.getViewCount() + 1);
        formRepository.save(form);

        // Update daily analytics
        recordAnalyticsView(form);

        Instant now = Instant.now();
        boolean isExpired = form.getExpiresAt() != null && now.isAfter(form.getExpiresAt());
        boolean startsInFuture = form.getStartsAt() != null && now.isBefore(form.getStartsAt());
        boolean isLimitReached = form.getSubmissionLimit() != null && form.getSubmissionCount() >= form.getSubmissionLimit();

        // Check password protection
        if (form.isPasswordProtected()) {
            boolean passwordMatch = false;
            if (providedPassword != null && form.getAccessPasswordHash() != null) {
                passwordMatch = passwordEncoder.matches(providedPassword, form.getAccessPasswordHash());
            }

            if (!passwordMatch) {
                // Return restricted view indicating password required
                return PublicFormViewResponse.builder()
                        .id(form.getId())
                        .title(form.getTitle())
                        .description(form.getDescription())
                        .slug(form.getPublishedSlug())
                        .isPasswordProtected(true)
                        .isExpired(isExpired)
                        .isLimitReached(isLimitReached)
                        .startsInFuture(startsInFuture)
                        .startsAt(form.getStartsAt())
                        .expiresAt(form.getExpiresAt())
                        .build();
            }
        }

        // Build full public schema
        List<FormSection> sections = sectionRepository.findByFormIdOrderByOrderIndexAsc(form.getId());
        List<FormQuestion> questions = questionRepository.findByFormIdOrderByOrderIndexAsc(form.getId());
        List<FormLogicRule> rules = logicRuleRepository.findByFormId(form.getId());
        FormTheme theme = themeRepository.findByFormId(form.getId()).orElse(null);

        List<FormSectionDto> sectionDtos = sections.stream().map(s -> FormSectionDto.builder()
                .id(s.getId())
                .title(s.getTitle())
                .description(s.getDescription())
                .orderIndex(s.getOrderIndex())
                .build()).collect(Collectors.toList());

        List<FormQuestionDto> questionDtos = questions.stream().map(q -> {
            List<FormQuestionOption> options = optionRepository.findByQuestionIdOrderByOrderIndexAsc(q.getId());
            List<FormOptionDto> optionDtos = options.stream().map(opt -> FormOptionDto.builder()
                    .id(opt.getId())
                    .label(opt.getLabel())
                    .value(opt.getValue())
                    .orderIndex(opt.getOrderIndex())
                    .isDefault(opt.isDefault())
                    .build()).collect(Collectors.toList());

            return FormQuestionDto.builder()
                    .id(q.getId())
                    .sectionId(q.getSection() != null ? q.getSection().getId() : null)
                    .fieldKey(q.getFieldKey())
                    .title(q.getTitle())
                    .description(q.getDescription())
                    .questionType(q.getQuestionType())
                    .placeholder(q.getPlaceholder())
                    .isRequired(q.isRequired())
                    .defaultValue(q.getDefaultValue())
                    .minLength(q.getMinLength())
                    .maxLength(q.getMaxLength())
                    .minValue(q.getMinValue())
                    .maxValue(q.getMaxValue())
                    .regexPattern(q.getRegexPattern())
                    .allowedFileTypes(q.getAllowedFileTypes())
                    .maxFileSizeMb(q.getMaxFileSizeMb())
                    .orderIndex(q.getOrderIndex())
                    .configJson(q.getConfigJson())
                    .options(optionDtos)
                    .build();
        }).collect(Collectors.toList());

        List<FormLogicRuleDto> ruleDtos = rules.stream().map(r -> FormLogicRuleDto.builder()
                .id(r.getId())
                .sourceQuestionId(r.getSourceQuestion().getId())
                .targetQuestionId(r.getTargetQuestion() != null ? r.getTargetQuestion().getId() : null)
                .targetSectionId(r.getTargetSection() != null ? r.getTargetSection().getId() : null)
                .conditionOperator(r.getConditionOperator())
                .conditionValue(r.getConditionValue())
                .actionType(r.getActionType())
                .logicalCombinator(r.getLogicalCombinator())
                .ruleGroup(r.getRuleGroup())
                .build()).collect(Collectors.toList());

        FormThemeDto themeDto = theme != null ? FormThemeDto.builder()
                .primaryColor(theme.getPrimaryColor())
                .backgroundColor(theme.getBackgroundColor())
                .textColor(theme.getTextColor())
                .fontFamily(theme.getFontFamily())
                .borderRadius(theme.getBorderRadius())
                .headerImageUrl(theme.getHeaderImageUrl())
                .logoUrl(theme.getLogoUrl())
                .buttonStyle(theme.getButtonStyle())
                .layoutMode(theme.getLayoutMode())
                .showProgressBar(theme.isShowProgressBar())
                .build() : new FormThemeDto();

        return PublicFormViewResponse.builder()
                .id(form.getId())
                .title(form.getTitle())
                .description(form.getDescription())
                .slug(form.getPublishedSlug())
                .isPasswordProtected(form.isPasswordProtected())
                .isExpired(isExpired)
                .isLimitReached(isLimitReached)
                .startsInFuture(startsInFuture)
                .startsAt(form.getStartsAt())
                .expiresAt(form.getExpiresAt())
                .theme(themeDto)
                .sections(sectionDtos)
                .questions(questionDtos)
                .logicRules(ruleDtos)
                .build();
    }

    @Transactional
    public PublicSubmissionResultResponse submitPublicForm(String slug, SubmitPublicFormRequest request, HttpServletRequest servletRequest) {
        Form form = formRepository.findByPublishedSlugAndIsDeletedFalse(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Form not found or has been unpublished"));

        if (!"PUBLISHED".equalsIgnoreCase(form.getStatus())) {
            throw new BadRequestException("This form is not active or accepting submissions.");
        }

        Instant now = Instant.now();
        if (form.getStartsAt() != null && now.isBefore(form.getStartsAt())) {
            throw new BadRequestException("This form is not yet open for submissions.");
        }
        if (form.getExpiresAt() != null && now.isAfter(form.getExpiresAt())) {
            throw new BadRequestException("This form has expired and is no longer accepting submissions.");
        }
        if (form.getSubmissionLimit() != null && form.getSubmissionCount() >= form.getSubmissionLimit()) {
            throw new BadRequestException("This form has reached its maximum response capacity.");
        }

        if (form.isPasswordProtected()) {
            if (request.getAccessPassword() == null ||
                    !passwordEncoder.matches(request.getAccessPassword(), form.getAccessPasswordHash())) {
                throw new BadRequestException("Invalid form access password");
            }
        }

        // Load all questions & options
        List<FormQuestion> questions = questionRepository.findByFormIdOrderByOrderIndexAsc(form.getId());
        List<FormLogicRule> rules = logicRuleRepository.findByFormId(form.getId());
        Map<String, Object> submittedAnswers = request.getAnswers() != null ? request.getAnswers() : new HashMap<>();

        // Validate hidden fields according to logic rules
        Set<UUID> hiddenQuestionIds = evaluateHiddenQuestions(questions, rules, submittedAnswers);

        // Perform Server-Side Validation
        Map<String, String> errors = new HashMap<>();

        for (FormQuestion question : questions) {
            if (hiddenQuestionIds.contains(question.getId())) {
                continue; // Skipped / hidden by conditional logic
            }

            Object rawValue = findAnswerValue(submittedAnswers, question);
            String strValue = rawValue != null ? String.valueOf(rawValue).trim() : null;

            // Required field check
            if (question.isRequired()) {
                if (rawValue == null || strValue.isEmpty() || "null".equalsIgnoreCase(strValue)) {
                    errors.put(question.getFieldKey(), question.getTitle() + " is required.");
                    continue;
                }
            }

            if (rawValue != null && !strValue.isEmpty() && !"null".equalsIgnoreCase(strValue)) {
                // Type-specific server-side verification
                switch (question.getQuestionType().toUpperCase()) {
                    case "EMAIL":
                        if (!EMAIL_PATTERN.matcher(strValue).matches()) {
                            errors.put(question.getFieldKey(), "Invalid email format.");
                        }
                        break;
                    case "NUMBER":
                        try {
                            double num = Double.parseDouble(strValue);
                            if (question.getMinValue() != null && num < question.getMinValue()) {
                                errors.put(question.getFieldKey(), "Value cannot be less than " + question.getMinValue());
                            }
                            if (question.getMaxValue() != null && num > question.getMaxValue()) {
                                errors.put(question.getFieldKey(), "Value cannot be greater than " + question.getMaxValue());
                            }
                        } catch (NumberFormatException e) {
                            errors.put(question.getFieldKey(), "Please enter a valid numeric value.");
                        }
                        break;
                    case "SHORT_TEXT":
                    case "LONG_TEXT":
                        if (question.getMinLength() != null && strValue.length() < question.getMinLength()) {
                            errors.put(question.getFieldKey(), "Minimum length is " + question.getMinLength() + " characters.");
                        }
                        if (question.getMaxLength() != null && strValue.length() > question.getMaxLength()) {
                            errors.put(question.getFieldKey(), "Maximum length is " + question.getMaxLength() + " characters.");
                        }
                        if (question.getRegexPattern() != null && !question.getRegexPattern().isBlank()) {
                            try {
                                if (!Pattern.compile(question.getRegexPattern()).matcher(strValue).matches()) {
                                    errors.put(question.getFieldKey(), "Input does not match required format.");
                                }
                            } catch (Exception ex) {
                                log.warn("Invalid regex pattern on question: {}", question.getId());
                            }
                        }
                        break;
                    case "RATING":
                    case "STAR_RATING":
                        try {
                            int rating = Integer.parseInt(strValue);
                            if (rating < 1 || rating > 5) {
                                errors.put(question.getFieldKey(), "Rating must be between 1 and 5.");
                            }
                        } catch (NumberFormatException e) {
                            errors.put(question.getFieldKey(), "Invalid rating value.");
                        }
                        break;
                    case "LINEAR_SCALE":
                        try {
                            int score = Integer.parseInt(strValue);
                            if (score < 0 || score > 10) {
                                errors.put(question.getFieldKey(), "Score must be between 0 and 10.");
                            }
                        } catch (NumberFormatException e) {
                            errors.put(question.getFieldKey(), "Invalid score.");
                        }
                        break;
                }
            }
        }

        if (!errors.isEmpty()) {
            throw new BadRequestException("Validation failed: " + errors.values().iterator().next());
        }

        // Build & Save Response
        String ipAddress = servletRequest != null ? servletRequest.getRemoteAddr() : "127.0.0.1";
        String userAgent = servletRequest != null ? servletRequest.getHeader("User-Agent") : "Unknown";
        String referrer = servletRequest != null ? servletRequest.getHeader("Referer") : null;

        FormResponse response = FormResponse.builder()
                .form(form)
                .respondentIp(ipAddress)
                .userAgent(userAgent != null && userAgent.length() > 500 ? userAgent.substring(0, 500) : userAgent)
                .referrer(referrer != null && referrer.length() > 500 ? referrer.substring(0, 500) : referrer)
                .durationSeconds(request.getDurationSeconds() > 0 ? request.getDurationSeconds() : 60)
                .status("COMPLETED")
                .isReviewed(false)
                .build();
        response = responseRepository.save(response);

        // Save Answers
        for (FormQuestion question : questions) {
            Object value = findAnswerValue(submittedAnswers, question);
            if (value != null) {
                String answerText = String.valueOf(value);
                String answerJson = null;
                if (value instanceof Collection || value instanceof Map) {
                    try {
                        answerJson = objectMapper.writeValueAsString(value);
                    } catch (Exception ignored) {}
                }

                ResponseAnswer answer = ResponseAnswer.builder()
                        .response(response)
                        .question(question)
                        .questionKey(question.getFieldKey())
                        .answerText(answerText)
                        .answerJson(answerJson)
                        .build();
                answerRepository.save(answer);
            }
        }

        // Increment submission count
        form.setSubmissionCount(form.getSubmissionCount() + 1);
        formRepository.save(form);

        // Record submission in daily analytics
        recordAnalyticsSubmission(form, response.getDurationSeconds());

        return PublicSubmissionResultResponse.builder()
                .submissionId(response.getId())
                .submittedAt(response.getSubmittedAt())
                .thankYouTitle(form.getCustomThankYouTitle() != null ? form.getCustomThankYouTitle() : "Thank You!")
                .thankYouMessage(form.getCustomThankYouMessage() != null ? form.getCustomThankYouMessage() : "Your response has been submitted successfully.")
                .redirectUrl(form.getRedirectUrl())
                .build();
    }

    private Object findAnswerValue(Map<String, Object> submittedAnswers, FormQuestion question) {
        if (submittedAnswers.containsKey(question.getFieldKey())) {
            return submittedAnswers.get(question.getFieldKey());
        }
        if (submittedAnswers.containsKey(question.getId().toString())) {
            return submittedAnswers.get(question.getId().toString());
        }
        return null;
    }

    private Set<UUID> evaluateHiddenQuestions(List<FormQuestion> questions, List<FormLogicRule> rules, Map<String, Object> submittedAnswers) {
        Set<UUID> hidden = new HashSet<>();
        for (FormLogicRule rule : rules) {
            if ("SHOW".equalsIgnoreCase(rule.getActionType()) && rule.getTargetQuestion() != null) {
                // If rule says SHOW, by default target question is hidden unless condition is satisfied
                FormQuestion srcQ = rule.getSourceQuestion();
                Object srcVal = findAnswerValue(submittedAnswers, srcQ);
                String srcStr = srcVal != null ? String.valueOf(srcVal) : "";
                boolean conditionMet = checkCondition(rule.getConditionOperator(), rule.getConditionValue(), srcStr);
                if (!conditionMet) {
                    hidden.add(rule.getTargetQuestion().getId());
                }
            } else if ("HIDE".equalsIgnoreCase(rule.getActionType()) && rule.getTargetQuestion() != null) {
                FormQuestion srcQ = rule.getSourceQuestion();
                Object srcVal = findAnswerValue(submittedAnswers, srcQ);
                String srcStr = srcVal != null ? String.valueOf(srcVal) : "";
                boolean conditionMet = checkCondition(rule.getConditionOperator(), rule.getConditionValue(), srcStr);
                if (conditionMet) {
                    hidden.add(rule.getTargetQuestion().getId());
                }
            }
        }
        return hidden;
    }

    private boolean checkCondition(String operator, String targetVal, String actualVal) {
        if (operator == null) return false;
        String t = targetVal != null ? targetVal.trim() : "";
        String a = actualVal != null ? actualVal.trim() : "";

        switch (operator.toUpperCase()) {
            case "EQUALS":
                return a.equalsIgnoreCase(t);
            case "NOT_EQUALS":
                return !a.equalsIgnoreCase(t);
            case "CONTAINS":
                return a.toLowerCase().contains(t.toLowerCase());
            case "IS_EMPTY":
                return a.isEmpty();
            case "IS_NOT_EMPTY":
                return !a.isEmpty();
            case "GREATER_THAN":
                try {
                    return Double.parseDouble(a) > Double.parseDouble(t);
                } catch (Exception e) { return false; }
            case "LESS_THAN":
                try {
                    return Double.parseDouble(a) < Double.parseDouble(t);
                } catch (Exception e) { return false; }
            default:
                return false;
        }
    }

    private void recordAnalyticsView(Form form) {
        LocalDate today = LocalDate.now();
        FormAnalytics fa = analyticsRepository.findByFormIdAndDate(form.getId(), today)
                .orElseGet(() -> FormAnalytics.builder()
                        .form(form)
                        .date(today)
                        .views(0)
                        .starts(0)
                        .submissions(0)
                        .completions(0)
                        .averageDurationSeconds(0)
                        .build());
        fa.setViews(fa.getViews() + 1);
        analyticsRepository.save(fa);
    }

    private void recordAnalyticsSubmission(Form form, int durationSeconds) {
        LocalDate today = LocalDate.now();
        FormAnalytics fa = analyticsRepository.findByFormIdAndDate(form.getId(), today)
                .orElseGet(() -> FormAnalytics.builder()
                        .form(form)
                        .date(today)
                        .views(1)
                        .starts(1)
                        .submissions(0)
                        .completions(0)
                        .averageDurationSeconds(0)
                        .build());

        int prevCount = fa.getSubmissions();
        int newCount = prevCount + 1;
        int newAvgDuration = (fa.getAverageDurationSeconds() * prevCount + durationSeconds) / newCount;

        fa.setSubmissions(newCount);
        fa.setCompletions(fa.getCompletions() + 1);
        fa.setAverageDurationSeconds(newAvgDuration);
        analyticsRepository.save(fa);
    }

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(PublicFormService.class);

    public PublicFormService(FormRepository formRepository, FormSectionRepository sectionRepository, FormQuestionRepository questionRepository, FormQuestionOptionRepository optionRepository, FormLogicRuleRepository logicRuleRepository, FormThemeRepository themeRepository, FormResponseRepository responseRepository, ResponseAnswerRepository answerRepository, FormAnalyticsRepository analyticsRepository, PasswordEncoder passwordEncoder, ObjectMapper objectMapper) {
        this.formRepository = formRepository;
        this.sectionRepository = sectionRepository;
        this.questionRepository = questionRepository;
        this.optionRepository = optionRepository;
        this.logicRuleRepository = logicRuleRepository;
        this.themeRepository = themeRepository;
        this.responseRepository = responseRepository;
        this.answerRepository = answerRepository;
        this.analyticsRepository = analyticsRepository;
        this.passwordEncoder = passwordEncoder;
        this.objectMapper = objectMapper;
    }
}

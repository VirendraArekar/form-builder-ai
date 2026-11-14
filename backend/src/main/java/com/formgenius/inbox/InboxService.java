package com.formgenius.inbox;

import com.formgenius.form.Form;
import com.formgenius.form.FormRepository;
import com.formgenius.form.FormTheme;
import com.formgenius.form.FormThemeRepository;
import com.formgenius.inbox.dto.InboxItemDto;
import com.formgenius.response.FormResponse;
import com.formgenius.response.FormResponseRepository;
import com.formgenius.response.ResponseAnswer;
import com.formgenius.response.ResponseAnswerRepository;
import com.formgenius.workspace.WorkspaceService;
import com.formgenius.workspace.dto.WorkspaceResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class InboxService {

    private final FormRepository formRepository;
    private final FormResponseRepository responseRepository;
    private final ResponseAnswerRepository answerRepository;
    private final FormThemeRepository themeRepository;
    private final WorkspaceService workspaceService;

    public InboxService(FormRepository formRepository, FormResponseRepository responseRepository, ResponseAnswerRepository answerRepository, FormThemeRepository themeRepository, WorkspaceService workspaceService) {
        this.formRepository = formRepository;
        this.responseRepository = responseRepository;
        this.answerRepository = answerRepository;
        this.themeRepository = themeRepository;
        this.workspaceService = workspaceService;
    }

    @Transactional(readOnly = true)
    public Page<InboxItemDto> getInboxSubmissions(UUID userId, UUID formIdFilter, String search, Pageable pageable) {
        List<WorkspaceResponse> workspaces = workspaceService.listUserWorkspaces(userId);
        if (workspaces.isEmpty()) {
            return Page.empty(pageable);
        }

        UUID primaryWsId = workspaces.get(0).getId();
        List<Form> userForms = formRepository.findByWorkspaceIdAndIsDeletedFalse(primaryWsId);
        if (userForms.isEmpty()) {
            return Page.empty(pageable);
        }

        Map<UUID, Form> formMap = userForms.stream().collect(Collectors.toMap(Form::getId, f -> f));
        Map<UUID, String> themeColorMap = new HashMap<>();
        for (Form f : userForms) {
            String color = themeRepository.findByFormId(f.getId())
                    .map(FormTheme::getPrimaryColor)
                    .orElse("#10B981"); // Default Emerald
            themeColorMap.put(f.getId(), color);
        }

        List<Form> targetForms = userForms;
        if (formIdFilter != null) {
            targetForms = userForms.stream().filter(f -> f.getId().equals(formIdFilter)).collect(Collectors.toList());
        }

        List<InboxItemDto> allItems = new ArrayList<>();
        for (Form f : targetForms) {
            List<FormResponse> responses = responseRepository.findByFormIdOrderBySubmittedAtDesc(f.getId());
            for (FormResponse r : responses) {
                List<ResponseAnswer> answers = answerRepository.findByResponseId(r.getId());

                Map<String, String> answerMap = new LinkedHashMap<>();
                String respondentName = null;
                String respondentEmail = null;
                String firstThought = null;

                for (ResponseAnswer a : answers) {
                    String val = a.getAnswerText();
                    if (val == null) val = "";
                    answerMap.put(a.getQuestionKey(), val);

                    String keyLower = a.getQuestionKey().toLowerCase();
                    if (keyLower.contains("name") && respondentName == null && !val.isBlank()) {
                        respondentName = val;
                    }
                    if (keyLower.contains("email") && respondentEmail == null && !val.isBlank()) {
                        respondentEmail = val;
                    }
                    if (firstThought == null && val.length() > 5 && !keyLower.contains("email") && !keyLower.contains("name")) {
                        firstThought = val;
                    }
                }

                if (respondentName == null) respondentName = "Anonymous Respondent";
                if (respondentEmail == null) respondentEmail = "Not provided";

                String preview = firstThought != null ? firstThought : (!answerMap.isEmpty() ? answerMap.values().iterator().next() : "Submission completed");
                if (preview.length() > 100) preview = preview.substring(0, 97) + "...";

                String ua = r.getUserAgent() != null ? r.getUserAgent().toLowerCase() : "";
                String device = "Desktop";
                if (ua.contains("ipad") || ua.contains("tablet")) device = "Tablet";
                else if (ua.contains("mobi") || ua.contains("android") || ua.contains("iphone")) device = "Mobile";

                InboxItemDto item = new InboxItemDto();
                item.setResponseId(r.getId());
                item.setFormId(f.getId());
                item.setFormTitle(f.getTitle());
                item.setFormThemeColor(themeColorMap.getOrDefault(f.getId(), "#10B981"));
                item.setRespondentName(respondentName);
                item.setRespondentEmail(respondentEmail);
                item.setResponsePreview(preview);
                item.setSubmittedAt(r.getSubmittedAt());
                item.setDurationSeconds(r.getDurationSeconds());
                item.setDeviceType(device);
                item.setReviewed(r.isReviewed());
                item.setAnswers(answerMap);

                // Check search filter
                if (search != null && !search.trim().isEmpty()) {
                    String s = search.trim().toLowerCase();
                    boolean match = respondentName.toLowerCase().contains(s) ||
                            respondentEmail.toLowerCase().contains(s) ||
                            f.getTitle().toLowerCase().contains(s) ||
                            preview.toLowerCase().contains(s) ||
                            answerMap.values().stream().anyMatch(v -> v.toLowerCase().contains(s));
                    if (!match) continue;
                }

                allItems.add(item);
            }
        }

        // Sort newest first
        allItems.sort((a, b) -> {
            if (a.getSubmittedAt() == null && b.getSubmittedAt() == null) return 0;
            if (a.getSubmittedAt() == null) return 1;
            if (b.getSubmittedAt() == null) return -1;
            return b.getSubmittedAt().compareTo(a.getSubmittedAt());
        });

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), allItems.size());
        List<InboxItemDto> pagedList = (start <= allItems.size()) ? allItems.subList(start, end) : Collections.emptyList();

        return new PageImpl<>(pagedList, pageable, allItems.size());
    }
}

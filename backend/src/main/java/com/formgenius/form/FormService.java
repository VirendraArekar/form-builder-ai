package com.formgenius.form;

import com.formgenius.exception.BadRequestException;
import com.formgenius.exception.ResourceNotFoundException;
import com.formgenius.form.dto.*;
import com.formgenius.user.User;
import com.formgenius.user.UserRepository;
import com.formgenius.workspace.Workspace;
import com.formgenius.workspace.WorkspaceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FormService {

    private final FormRepository formRepository;
    private final FormSectionRepository sectionRepository;
    private final FormQuestionRepository questionRepository;
    private final FormQuestionOptionRepository optionRepository;
    private final FormLogicRuleRepository logicRuleRepository;
    private final FormThemeRepository themeRepository;
    private final FormPublicationRepository publicationRepository;
    private final WorkspaceService workspaceService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public Page<FormSummaryResponse> listForms(UUID workspaceId, UUID userId, String status, Boolean favorite, String search, Pageable pageable) {
        workspaceService.findAndValidateMember(workspaceId, userId);
        String filterStatus = (status == null || status.equalsIgnoreCase("ALL") || status.equalsIgnoreCase("FAVORITES")) ? null : status.toUpperCase();
        Boolean filterFavorite = (favorite != null && favorite) || (status != null && status.equalsIgnoreCase("FAVORITES")) ? true : null;
        String searchPattern = (search == null || search.trim().isEmpty()) ? null : "%" + search.trim().toLowerCase() + "%";

        Page<Form> forms = formRepository.findAllByWorkspaceWithFilters(workspaceId, filterStatus, filterFavorite, searchPattern, pageable);
        return forms.map(this::mapToSummary);
    }

    @Transactional(readOnly = true)
    public FormDetailResponse getFormDetails(UUID formId, UUID userId) {
        Form form = formRepository.findById(formId)
                .orElseThrow(() -> new ResourceNotFoundException("Form not found"));
        workspaceService.findAndValidateMember(form.getWorkspace().getId(), userId);

        return buildFullDetailResponse(form);
    }

    @Transactional
    public FormDetailResponse createForm(UUID userId, CreateFormRequest request) {
        Workspace workspace = workspaceService.findAndValidateMember(request.getWorkspaceId(), userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Form form = Form.builder()
                .workspace(workspace)
                .createdBy(user)
                .title(request.getTitle().trim())
                .description(request.getDescription())
                .status("DRAFT")
                .allowMultipleSubmissions(true)
                .build();
        form = formRepository.save(form);

        // Create default theme
        FormTheme theme = FormTheme.builder()
                .form(form)
                .primaryColor("#4F46E5")
                .backgroundColor("#FFFFFF")
                .textColor("#111827")
                .fontFamily("Inter")
                .borderRadius("md")
                .layoutMode("single_page")
                .showProgressBar(true)
                .build();
        themeRepository.save(theme);

        // Create a default initial section
        FormSection section = FormSection.builder()
                .form(form)
                .title("General Information")
                .orderIndex(0)
                .build();
        sectionRepository.save(section);

        return buildFullDetailResponse(form);
    }

    @Transactional
    public FormDetailResponse updateForm(UUID formId, UUID userId, UpdateFormRequest request) {
        Form form = formRepository.findById(formId)
                .orElseThrow(() -> new ResourceNotFoundException("Form not found"));
        workspaceService.findAndValidateMember(form.getWorkspace().getId(), userId);

        if (request.getTitle() != null) form.setTitle(request.getTitle().trim());
        if (request.getDescription() != null) form.setDescription(request.getDescription());
        if (request.getSubmissionLimit() != null) form.setSubmissionLimit(request.getSubmissionLimit());
        if (request.getStartsAt() != null) form.setStartsAt(request.getStartsAt());
        if (request.getExpiresAt() != null) form.setExpiresAt(request.getExpiresAt());
        if (request.getIsPasswordProtected() != null) {
            form.setPasswordProtected(request.getIsPasswordProtected());
            if (Boolean.TRUE.equals(request.getIsPasswordProtected()) && request.getAccessPassword() != null && !request.getAccessPassword().isBlank()) {
                form.setAccessPasswordHash(passwordEncoder.encode(request.getAccessPassword()));
            }
        }
        if (request.getRedirectUrl() != null) form.setRedirectUrl(request.getRedirectUrl());
        if (request.getCustomThankYouTitle() != null) form.setCustomThankYouTitle(request.getCustomThankYouTitle());
        if (request.getCustomThankYouMessage() != null) form.setCustomThankYouMessage(request.getCustomThankYouMessage());
        if (request.getAllowMultipleSubmissions() != null) form.setAllowMultipleSubmissions(request.getAllowMultipleSubmissions());
        if (request.getSendEmailNotification() != null) form.setSendEmailNotification(request.getSendEmailNotification());
        if (request.getNotificationEmail() != null) form.setNotificationEmail(request.getNotificationEmail());

        form = formRepository.save(form);
        return buildFullDetailResponse(form);
    }

    @Transactional
    public FormDetailResponse saveFormSchema(UUID formId, UUID userId, SaveFormSchemaRequest request) {
        Form form = formRepository.findById(formId)
                .orElseThrow(() -> new ResourceNotFoundException("Form not found"));
        workspaceService.findAndValidateMember(form.getWorkspace().getId(), userId);

        if (request.getTitle() != null && !request.getTitle().isBlank()) {
            form.setTitle(request.getTitle().trim());
        }
        if (request.getDescription() != null) {
            form.setDescription(request.getDescription());
        }
        final Form savedForm = formRepository.save(form);

        // Synchronize Theme
        if (request.getTheme() != null) {
            FormTheme theme = themeRepository.findByFormId(formId)
                    .orElseGet(() -> FormTheme.builder().form(savedForm).build());
            theme.setPrimaryColor(request.getTheme().getPrimaryColor());
            theme.setBackgroundColor(request.getTheme().getBackgroundColor());
            theme.setTextColor(request.getTheme().getTextColor());
            theme.setFontFamily(request.getTheme().getFontFamily());
            theme.setBorderRadius(request.getTheme().getBorderRadius());
            theme.setHeaderImageUrl(request.getTheme().getHeaderImageUrl());
            theme.setLogoUrl(request.getTheme().getLogoUrl());
            theme.setButtonStyle(request.getTheme().getButtonStyle());
            theme.setLayoutMode(request.getTheme().getLayoutMode());
            theme.setShowProgressBar(request.getTheme().isShowProgressBar());
            themeRepository.save(theme);
        }

        // Clean slate replace for form structure
        logicRuleRepository.deleteByFormId(formId);
        List<FormQuestion> existingQuestions = questionRepository.findByFormIdOrderByOrderIndexAsc(formId);
        for (FormQuestion q : existingQuestions) {
            optionRepository.deleteByQuestionId(q.getId());
        }
        questionRepository.deleteByFormId(formId);
        sectionRepository.deleteByFormId(formId);

        // Track mapped section IDs and question IDs (frontend temp ID -> persisted UUID)
        Map<String, FormSection> sectionMap = new HashMap<>();
        if (request.getSections() != null) {
            for (int i = 0; i < request.getSections().size(); i++) {
                FormSectionDto sDto = request.getSections().get(i);
                FormSection section = FormSection.builder()
                        .form(form)
                        .title(sDto.getTitle())
                        .description(sDto.getDescription())
                        .orderIndex(i)
                        .build();
                section = sectionRepository.save(section);
                if (sDto.getId() != null) {
                    sectionMap.put(sDto.getId().toString(), section);
                }
            }
        }

        Map<String, FormQuestion> questionMap = new HashMap<>();
        if (request.getQuestions() != null) {
            for (int i = 0; i < request.getQuestions().size(); i++) {
                FormQuestionDto qDto = request.getQuestions().get(i);
                FormSection assignedSection = null;
                if (qDto.getSectionId() != null) {
                    assignedSection = sectionMap.get(qDto.getSectionId().toString());
                }

                FormQuestion question = FormQuestion.builder()
                        .form(form)
                        .section(assignedSection)
                        .fieldKey(qDto.getFieldKey() != null ? qDto.getFieldKey() : "field_" + i)
                        .title(qDto.getTitle() != null ? qDto.getTitle() : "Untitled Question")
                        .description(qDto.getDescription())
                        .questionType(qDto.getQuestionType() != null ? qDto.getQuestionType() : "SHORT_TEXT")
                        .placeholder(qDto.getPlaceholder())
                        .isRequired(qDto.isRequired())
                        .defaultValue(qDto.getDefaultValue())
                        .minLength(qDto.getMinLength())
                        .maxLength(qDto.getMaxLength())
                        .minValue(qDto.getMinValue())
                        .maxValue(qDto.getMaxValue())
                        .regexPattern(qDto.getRegexPattern())
                        .allowedFileTypes(qDto.getAllowedFileTypes())
                        .maxFileSizeMb(qDto.getMaxFileSizeMb())
                        .orderIndex(i)
                        .configJson(qDto.getConfigJson())
                        .build();
                question = questionRepository.save(question);
                if (qDto.getId() != null) {
                    questionMap.put(qDto.getId().toString(), question);
                }

                if (qDto.getOptions() != null) {
                    for (int optIdx = 0; optIdx < qDto.getOptions().size(); optIdx++) {
                        FormOptionDto optDto = qDto.getOptions().get(optIdx);
                        FormQuestionOption option = FormQuestionOption.builder()
                                .question(question)
                                .label(optDto.getLabel())
                                .value(optDto.getValue())
                                .orderIndex(optIdx)
                                .isDefault(optDto.isDefault())
                                .build();
                        optionRepository.save(option);
                    }
                }
            }
        }

        // Logic Rules
        if (request.getLogicRules() != null) {
            for (FormLogicRuleDto rDto : request.getLogicRules()) {
                if (rDto.getSourceQuestionId() != null) {
                    FormQuestion srcQ = questionMap.get(rDto.getSourceQuestionId().toString());
                    if (srcQ != null) {
                        FormQuestion tgtQ = rDto.getTargetQuestionId() != null ? questionMap.get(rDto.getTargetQuestionId().toString()) : null;
                        FormSection tgtS = rDto.getTargetSectionId() != null ? sectionMap.get(rDto.getTargetSectionId().toString()) : null;

                        FormLogicRule rule = FormLogicRule.builder()
                                .form(form)
                                .sourceQuestion(srcQ)
                                .targetQuestion(tgtQ)
                                .targetSection(tgtS)
                                .conditionOperator(rDto.getConditionOperator() != null ? rDto.getConditionOperator() : "EQUALS")
                                .conditionValue(rDto.getConditionValue())
                                .actionType(rDto.getActionType() != null ? rDto.getActionType() : "SHOW")
                                .logicalCombinator(rDto.getLogicalCombinator() != null ? rDto.getLogicalCombinator() : "AND")
                                .ruleGroup(rDto.getRuleGroup())
                                .build();
                        logicRuleRepository.save(rule);
                    }
                }
            }
        }

        return buildFullDetailResponse(form);
    }

    @Transactional
    public FormDetailResponse duplicateForm(UUID formId, UUID userId) {
        FormDetailResponse source = getFormDetails(formId, userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Workspace workspace = workspaceService.findAndValidateMember(source.getWorkspaceId(), userId);

        CreateFormRequest createReq = new CreateFormRequest();
        createReq.setWorkspaceId(workspace.getId());
        createReq.setTitle(source.getTitle() + " (Copy)");
        createReq.setDescription(source.getDescription());
        FormDetailResponse newForm = createForm(userId, createReq);

        SaveFormSchemaRequest schemaReq = new SaveFormSchemaRequest();
        schemaReq.setTitle(newForm.getTitle());
        schemaReq.setDescription(newForm.getDescription());
        schemaReq.setSections(source.getSections());
        schemaReq.setQuestions(source.getQuestions());
        schemaReq.setLogicRules(source.getLogicRules());
        schemaReq.setTheme(source.getTheme());

        return saveFormSchema(newForm.getId(), userId, schemaReq);
    }

    @Transactional
    public FormDetailResponse publishForm(UUID formId, UUID userId, PublishFormRequest request) {
        Form form = formRepository.findById(formId)
                .orElseThrow(() -> new ResourceNotFoundException("Form not found"));
        workspaceService.findAndValidateMember(form.getWorkspace().getId(), userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String slug;
        if (request != null && request.getCustomSlug() != null && !request.getCustomSlug().isBlank()) {
            slug = slugify(request.getCustomSlug());
            if (formRepository.existsByPublishedSlug(slug) && !slug.equals(form.getPublishedSlug())) {
                throw new BadRequestException("Custom URL slug is already taken: " + slug);
            }
        } else if (form.getPublishedSlug() != null && !form.getPublishedSlug().isBlank()) {
            slug = form.getPublishedSlug();
        } else {
            slug = slugify(form.getTitle()) + "-" + UUID.randomUUID().toString().substring(0, 6);
        }

        form.setStatus("PUBLISHED");
        form.setPublishedSlug(slug);
        form = formRepository.save(form);

        FormPublication publication = FormPublication.builder()
                .form(form)
                .publishedBy(user)
                .publishedSlug(slug)
                .isActive(true)
                .build();
        publicationRepository.save(publication);

        return buildFullDetailResponse(form);
    }

    @Transactional
    public FormDetailResponse unpublishForm(UUID formId, UUID userId) {
        Form form = formRepository.findById(formId)
                .orElseThrow(() -> new ResourceNotFoundException("Form not found"));
        workspaceService.findAndValidateMember(form.getWorkspace().getId(), userId);

        form.setStatus("DRAFT");
        form = formRepository.save(form);

        return buildFullDetailResponse(form);
    }

    @Transactional
    public void archiveForm(UUID formId, UUID userId) {
        Form form = formRepository.findById(formId)
                .orElseThrow(() -> new ResourceNotFoundException("Form not found"));
        workspaceService.findAndValidateMember(form.getWorkspace().getId(), userId);

        form.setStatus("ARCHIVED");
        formRepository.save(form);
    }

    @Transactional
    public void restoreForm(UUID formId, UUID userId) {
        Form form = formRepository.findById(formId)
                .orElseThrow(() -> new ResourceNotFoundException("Form not found"));
        workspaceService.findAndValidateMember(form.getWorkspace().getId(), userId);

        form.setStatus("DRAFT");
        formRepository.save(form);
    }

    @Transactional
    public FormDetailResponse toggleFavorite(UUID formId, UUID userId) {
        Form form = formRepository.findById(formId)
                .orElseThrow(() -> new ResourceNotFoundException("Form not found"));
        workspaceService.findAndValidateMember(form.getWorkspace().getId(), userId);

        form.setFavorite(!form.isFavorite());
        form = formRepository.save(form);
        return buildFullDetailResponse(form);
    }

    @Transactional
    public FormDetailResponse toggleArchive(UUID formId, UUID userId) {
        Form form = formRepository.findById(formId)
                .orElseThrow(() -> new ResourceNotFoundException("Form not found"));
        workspaceService.findAndValidateMember(form.getWorkspace().getId(), userId);

        if ("ARCHIVED".equalsIgnoreCase(form.getStatus())) {
            form.setStatus("DRAFT");
        } else {
            form.setStatus("ARCHIVED");
        }
        form = formRepository.save(form);
        return buildFullDetailResponse(form);
    }

    @Transactional
    public void deleteForm(UUID formId, UUID userId) {
        Form form = formRepository.findById(formId)
                .orElseThrow(() -> new ResourceNotFoundException("Form not found"));
        workspaceService.findAndValidateMember(form.getWorkspace().getId(), userId);

        form.setDeleted(true);
        formRepository.save(form);
    }

    public FormDetailResponse buildFullDetailResponse(Form form) {
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

        return FormDetailResponse.builder()
                .id(form.getId())
                .workspaceId(form.getWorkspace().getId())
                .workspaceName(form.getWorkspace().getName())
                .title(form.getTitle())
                .description(form.getDescription())
                .status(form.getStatus())
                .publishedSlug(form.getPublishedSlug())
                .isPasswordProtected(form.isPasswordProtected())
                .submissionLimit(form.getSubmissionLimit())
                .submissionCount(form.getSubmissionCount())
                .viewCount(form.getViewCount())
                .startsAt(form.getStartsAt())
                .expiresAt(form.getExpiresAt())
                .redirectUrl(form.getRedirectUrl())
                .customThankYouTitle(form.getCustomThankYouTitle())
                .customThankYouMessage(form.getCustomThankYouMessage())
                .allowMultipleSubmissions(form.isAllowMultipleSubmissions())
                .sendEmailNotification(form.isSendEmailNotification())
                .notificationEmail(form.getNotificationEmail())
                .createdAt(form.getCreatedAt())
                .updatedAt(form.getUpdatedAt())
                .isFavorite(form.isFavorite())
                .sections(sectionDtos)
                .questions(questionDtos)
                .logicRules(ruleDtos)
                .theme(themeDto)
                .build();
    }

    private FormSummaryResponse mapToSummary(Form form) {
        return FormSummaryResponse.builder()
                .id(form.getId())
                .workspaceId(form.getWorkspace().getId())
                .title(form.getTitle())
                .description(form.getDescription())
                .status(form.getStatus())
                .publishedSlug(form.getPublishedSlug())
                .isPasswordProtected(form.isPasswordProtected())
                .submissionCount(form.getSubmissionCount())
                .viewCount(form.getViewCount())
                .submissionLimit(form.getSubmissionLimit())
                .startsAt(form.getStartsAt())
                .expiresAt(form.getExpiresAt())
                .createdAt(form.getCreatedAt())
                .updatedAt(form.getUpdatedAt())
                .isFavorite(form.isFavorite())
                .build();
    }

    private String slugify(String input) {
        if (input == null || input.isBlank()) return "form";
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        String slug = normalized.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "")
                .toLowerCase(Locale.ENGLISH)
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("[\\s-]+", "-")
                .replaceAll("^-+|-+$", "");
        return slug.isEmpty() ? "form" : slug;
    }

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(FormService.class);

    public FormService(FormRepository formRepository, FormSectionRepository sectionRepository, FormQuestionRepository questionRepository, FormQuestionOptionRepository optionRepository, FormLogicRuleRepository logicRuleRepository, FormThemeRepository themeRepository, FormPublicationRepository publicationRepository, WorkspaceService workspaceService, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.formRepository = formRepository;
        this.sectionRepository = sectionRepository;
        this.questionRepository = questionRepository;
        this.optionRepository = optionRepository;
        this.logicRuleRepository = logicRuleRepository;
        this.themeRepository = themeRepository;
        this.publicationRepository = publicationRepository;
        this.workspaceService = workspaceService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
}

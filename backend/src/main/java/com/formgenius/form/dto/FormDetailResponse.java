package com.formgenius.form.dto;


import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FormDetailResponse {
    private UUID id;
    private UUID workspaceId;
    private String workspaceName;
    private String title;
    private String description;
    private String status;
    private String publishedSlug;
    private boolean isPasswordProtected;
    private Integer submissionLimit;
    private int submissionCount;
    private int viewCount;
    private Instant startsAt;
    private Instant expiresAt;
    private String redirectUrl;
    private String customThankYouTitle;
    private String customThankYouMessage;
    private boolean allowMultipleSubmissions;
    private boolean sendEmailNotification;
    private String notificationEmail;
    private Instant createdAt;
    private Instant updatedAt;
    private boolean isFavorite;
    private List<FormSectionDto> sections = new ArrayList<>();
    private List<FormQuestionDto> questions = new ArrayList<>();
    private List<FormLogicRuleDto> logicRules = new ArrayList<>();

    private FormThemeDto theme;

    public FormDetailResponse() {}

    public FormDetailResponse(UUID id, UUID workspaceId, String workspaceName, String title, String description, String status, String publishedSlug, boolean isPasswordProtected, Integer submissionLimit, int submissionCount, int viewCount, Instant startsAt, Instant expiresAt, String redirectUrl, String customThankYouTitle, String customThankYouMessage, boolean allowMultipleSubmissions, boolean sendEmailNotification, String notificationEmail, Instant createdAt, Instant updatedAt, List<FormSectionDto> sections, List<FormQuestionDto> questions, List<FormLogicRuleDto> logicRules, FormThemeDto theme) {
        this.id = id;
        this.workspaceId = workspaceId;
        this.workspaceName = workspaceName;
        this.title = title;
        this.description = description;
        this.status = status;
        this.publishedSlug = publishedSlug;
        this.isPasswordProtected = isPasswordProtected;
        this.submissionLimit = submissionLimit;
        this.submissionCount = submissionCount;
        this.viewCount = viewCount;
        this.startsAt = startsAt;
        this.expiresAt = expiresAt;
        this.redirectUrl = redirectUrl;
        this.customThankYouTitle = customThankYouTitle;
        this.customThankYouMessage = customThankYouMessage;
        this.allowMultipleSubmissions = allowMultipleSubmissions;
        this.sendEmailNotification = sendEmailNotification;
        this.notificationEmail = notificationEmail;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.sections = sections;
        this.questions = questions;
        this.logicRules = logicRules;
        this.theme = theme;
    }

    public UUID getId() {
        return id;
    }

    public UUID getWorkspaceId() {
        return workspaceId;
    }

    public String getWorkspaceName() {
        return workspaceName;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public String getPublishedSlug() {
        return publishedSlug;
    }

    public boolean isPasswordProtected() {
        return isPasswordProtected;
    }

    public Integer getSubmissionLimit() {
        return submissionLimit;
    }

    public int getSubmissionCount() {
        return submissionCount;
    }

    public int getViewCount() {
        return viewCount;
    }

    public Instant getStartsAt() {
        return startsAt;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public String getCustomThankYouTitle() {
        return customThankYouTitle;
    }

    public String getCustomThankYouMessage() {
        return customThankYouMessage;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    public boolean isAllowMultipleSubmissions() {
        return allowMultipleSubmissions;
    }

    public boolean isSendEmailNotification() {
        return sendEmailNotification;
    }

    public String getNotificationEmail() {
        return notificationEmail;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public List<FormSectionDto> getSections() {
        return sections;
    }

    public List<FormQuestionDto> getQuestions() {
        return questions;
    }

    public List<FormLogicRuleDto> getLogicRules() {
        return logicRules;
    }

    public FormThemeDto getTheme() {
        return theme;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setWorkspaceId(UUID workspaceId) {
        this.workspaceId = workspaceId;
    }

    public void setWorkspaceName(String workspaceName) {
        this.workspaceName = workspaceName;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setPublishedSlug(String publishedSlug) {
        this.publishedSlug = publishedSlug;
    }

    public void setPasswordProtected(boolean isPasswordProtected) {
        this.isPasswordProtected = isPasswordProtected;
    }

    public void setSubmissionLimit(Integer submissionLimit) {
        this.submissionLimit = submissionLimit;
    }

    public void setSubmissionCount(int submissionCount) {
        this.submissionCount = submissionCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public void setStartsAt(Instant startsAt) {
        this.startsAt = startsAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public void setCustomThankYouTitle(String customThankYouTitle) {
        this.customThankYouTitle = customThankYouTitle;
    }

    public void setCustomThankYouMessage(String customThankYouMessage) {
        this.customThankYouMessage = customThankYouMessage;
    }

    public void setAllowMultipleSubmissions(boolean allowMultipleSubmissions) {
        this.allowMultipleSubmissions = allowMultipleSubmissions;
    }

    public void setSendEmailNotification(boolean sendEmailNotification) {
        this.sendEmailNotification = sendEmailNotification;
    }

    public void setNotificationEmail(String notificationEmail) {
        this.notificationEmail = notificationEmail;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setSections(List<FormSectionDto> sections) {
        this.sections = sections;
    }

    public void setQuestions(List<FormQuestionDto> questions) {
        this.questions = questions;
    }

    public void setLogicRules(List<FormLogicRuleDto> logicRules) {
        this.logicRules = logicRules;
    }

    public void setTheme(FormThemeDto theme) {
        this.theme = theme;
    }

    public static FormDetailResponseBuilder builder() {
        return new FormDetailResponseBuilder();
    }
    public static class FormDetailResponseBuilder {
        private UUID id;
        private UUID workspaceId;
        private String workspaceName;
        private String title;
        private String description;
        private String status;
        private String publishedSlug;
        private boolean isPasswordProtected;
        private Integer submissionLimit;
        private int submissionCount;
        private int viewCount;
        private Instant startsAt;
        private Instant expiresAt;
        private String redirectUrl;
        private String customThankYouTitle;
        private String customThankYouMessage;
        private boolean allowMultipleSubmissions;
        private boolean sendEmailNotification;
        private String notificationEmail;
        private Instant createdAt;
        private Instant updatedAt;
        private boolean isFavorite;
        private List<FormSectionDto> sections = new ArrayList<>();
        private List<FormQuestionDto> questions = new ArrayList<>();
        private List<FormLogicRuleDto> logicRules = new ArrayList<>();
        private FormThemeDto theme;

        public FormDetailResponseBuilder id(UUID id) {
            this.id = id;
            return this;
        }
        public FormDetailResponseBuilder workspaceId(UUID workspaceId) {
            this.workspaceId = workspaceId;
            return this;
        }
        public FormDetailResponseBuilder workspaceName(String workspaceName) {
            this.workspaceName = workspaceName;
            return this;
        }
        public FormDetailResponseBuilder title(String title) {
            this.title = title;
            return this;
        }
        public FormDetailResponseBuilder description(String description) {
            this.description = description;
            return this;
        }
        public FormDetailResponseBuilder status(String status) {
            this.status = status;
            return this;
        }
        public FormDetailResponseBuilder publishedSlug(String publishedSlug) {
            this.publishedSlug = publishedSlug;
            return this;
        }
        public FormDetailResponseBuilder isPasswordProtected(boolean isPasswordProtected) {
            this.isPasswordProtected = isPasswordProtected;
            return this;
        }
        public FormDetailResponseBuilder submissionLimit(Integer submissionLimit) {
            this.submissionLimit = submissionLimit;
            return this;
        }
        public FormDetailResponseBuilder submissionCount(int submissionCount) {
            this.submissionCount = submissionCount;
            return this;
        }
        public FormDetailResponseBuilder viewCount(int viewCount) {
            this.viewCount = viewCount;
            return this;
        }
        public FormDetailResponseBuilder startsAt(Instant startsAt) {
            this.startsAt = startsAt;
            return this;
        }
        public FormDetailResponseBuilder expiresAt(Instant expiresAt) {
            this.expiresAt = expiresAt;
            return this;
        }
        public FormDetailResponseBuilder redirectUrl(String redirectUrl) {
            this.redirectUrl = redirectUrl;
            return this;
        }
        public FormDetailResponseBuilder customThankYouTitle(String customThankYouTitle) {
            this.customThankYouTitle = customThankYouTitle;
            return this;
        }
        public FormDetailResponseBuilder customThankYouMessage(String customThankYouMessage) {
            this.customThankYouMessage = customThankYouMessage;
            return this;
        }
        public FormDetailResponseBuilder allowMultipleSubmissions(boolean allowMultipleSubmissions) {
            this.allowMultipleSubmissions = allowMultipleSubmissions;
            return this;
        }
        public FormDetailResponseBuilder sendEmailNotification(boolean sendEmailNotification) {
            this.sendEmailNotification = sendEmailNotification;
            return this;
        }
        public FormDetailResponseBuilder notificationEmail(String notificationEmail) {
            this.notificationEmail = notificationEmail;
            return this;
        }
        public FormDetailResponseBuilder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }
        public FormDetailResponseBuilder updatedAt(Instant updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }
        public FormDetailResponseBuilder isFavorite(boolean isFavorite) {
            this.isFavorite = isFavorite;
            return this;
        }
        public FormDetailResponseBuilder sections(List<FormSectionDto> sections) {
            this.sections = sections;
            return this;
        }
        public FormDetailResponseBuilder questions(List<FormQuestionDto> questions) {
            this.questions = questions;
            return this;
        }
        public FormDetailResponseBuilder logicRules(List<FormLogicRuleDto> logicRules) {
            this.logicRules = logicRules;
            return this;
        }
        public FormDetailResponseBuilder theme(FormThemeDto theme) {
            this.theme = theme;
            return this;
        }

        public FormDetailResponse build() {
            FormDetailResponse instance = new FormDetailResponse();
            instance.id = this.id;
            instance.workspaceId = this.workspaceId;
            instance.workspaceName = this.workspaceName;
            instance.title = this.title;
            instance.description = this.description;
            instance.status = this.status;
            instance.publishedSlug = this.publishedSlug;
            instance.isPasswordProtected = this.isPasswordProtected;
            instance.submissionLimit = this.submissionLimit;
            instance.submissionCount = this.submissionCount;
            instance.viewCount = this.viewCount;
            instance.startsAt = this.startsAt;
            instance.expiresAt = this.expiresAt;
            instance.redirectUrl = this.redirectUrl;
            instance.customThankYouTitle = this.customThankYouTitle;
            instance.customThankYouMessage = this.customThankYouMessage;
            instance.allowMultipleSubmissions = this.allowMultipleSubmissions;
            instance.sendEmailNotification = this.sendEmailNotification;
            instance.notificationEmail = this.notificationEmail;
            instance.createdAt = this.createdAt;
            instance.updatedAt = this.updatedAt;
            instance.isFavorite = this.isFavorite;
            instance.sections = this.sections;
            instance.questions = this.questions;
            instance.logicRules = this.logicRules;
            instance.theme = this.theme;
            return instance;
        }
    }
}

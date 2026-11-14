package com.formgenius.analytics.dto;


import java.time.Instant;
import java.util.UUID;

public class RecentSubmissionItemDto {
    private UUID responseId;
    private UUID formId;
    private String formTitle;
    private String respondentIp;
    private int durationSeconds;
    private Instant submittedAt;

    public RecentSubmissionItemDto() {}

    public RecentSubmissionItemDto(UUID responseId, UUID formId, String formTitle, String respondentIp, int durationSeconds, Instant submittedAt) {
        this.responseId = responseId;
        this.formId = formId;
        this.formTitle = formTitle;
        this.respondentIp = respondentIp;
        this.durationSeconds = durationSeconds;
        this.submittedAt = submittedAt;
    }

    public UUID getResponseId() {
        return responseId;
    }

    public UUID getFormId() {
        return formId;
    }

    public String getFormTitle() {
        return formTitle;
    }

    public String getRespondentIp() {
        return respondentIp;
    }

    public int getDurationSeconds() {
        return durationSeconds;
    }

    public Instant getSubmittedAt() {
        return submittedAt;
    }

    public void setResponseId(UUID responseId) {
        this.responseId = responseId;
    }

    public void setFormId(UUID formId) {
        this.formId = formId;
    }

    public void setFormTitle(String formTitle) {
        this.formTitle = formTitle;
    }

    public void setRespondentIp(String respondentIp) {
        this.respondentIp = respondentIp;
    }

    public void setDurationSeconds(int durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    public void setSubmittedAt(Instant submittedAt) {
        this.submittedAt = submittedAt;
    }

    public static RecentSubmissionItemDtoBuilder builder() {
        return new RecentSubmissionItemDtoBuilder();
    }
    public static class RecentSubmissionItemDtoBuilder {
        private UUID responseId;
        private UUID formId;
        private String formTitle;
        private String respondentIp;
        private int durationSeconds;
        private Instant submittedAt;

        public RecentSubmissionItemDtoBuilder responseId(UUID responseId) {
            this.responseId = responseId;
            return this;
        }
        public RecentSubmissionItemDtoBuilder formId(UUID formId) {
            this.formId = formId;
            return this;
        }
        public RecentSubmissionItemDtoBuilder formTitle(String formTitle) {
            this.formTitle = formTitle;
            return this;
        }
        public RecentSubmissionItemDtoBuilder respondentIp(String respondentIp) {
            this.respondentIp = respondentIp;
            return this;
        }
        public RecentSubmissionItemDtoBuilder durationSeconds(int durationSeconds) {
            this.durationSeconds = durationSeconds;
            return this;
        }
        public RecentSubmissionItemDtoBuilder submittedAt(Instant submittedAt) {
            this.submittedAt = submittedAt;
            return this;
        }

        public RecentSubmissionItemDto build() {
            RecentSubmissionItemDto instance = new RecentSubmissionItemDto();
            instance.responseId = this.responseId;
            instance.formId = this.formId;
            instance.formTitle = this.formTitle;
            instance.respondentIp = this.respondentIp;
            instance.durationSeconds = this.durationSeconds;
            instance.submittedAt = this.submittedAt;
            return instance;
        }
    }
}

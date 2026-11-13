package com.formgenius.publicform.dto;


import java.time.Instant;
import java.util.UUID;

public class PublicSubmissionResultResponse {
    private UUID submissionId;
    private Instant submittedAt;
    private String thankYouTitle;
    private String thankYouMessage;
    private String redirectUrl;

    public PublicSubmissionResultResponse() {}

    public PublicSubmissionResultResponse(UUID submissionId, Instant submittedAt, String thankYouTitle, String thankYouMessage, String redirectUrl) {
        this.submissionId = submissionId;
        this.submittedAt = submittedAt;
        this.thankYouTitle = thankYouTitle;
        this.thankYouMessage = thankYouMessage;
        this.redirectUrl = redirectUrl;
    }

    public UUID getSubmissionId() {
        return submissionId;
    }

    public Instant getSubmittedAt() {
        return submittedAt;
    }

    public String getThankYouTitle() {
        return thankYouTitle;
    }

    public String getThankYouMessage() {
        return thankYouMessage;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setSubmissionId(UUID submissionId) {
        this.submissionId = submissionId;
    }

    public void setSubmittedAt(Instant submittedAt) {
        this.submittedAt = submittedAt;
    }

    public void setThankYouTitle(String thankYouTitle) {
        this.thankYouTitle = thankYouTitle;
    }

    public void setThankYouMessage(String thankYouMessage) {
        this.thankYouMessage = thankYouMessage;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public static PublicSubmissionResultResponseBuilder builder() {
        return new PublicSubmissionResultResponseBuilder();
    }
    public static class PublicSubmissionResultResponseBuilder {
        private UUID submissionId;
        private Instant submittedAt;
        private String thankYouTitle;
        private String thankYouMessage;
        private String redirectUrl;

        public PublicSubmissionResultResponseBuilder submissionId(UUID submissionId) {
            this.submissionId = submissionId;
            return this;
        }
        public PublicSubmissionResultResponseBuilder submittedAt(Instant submittedAt) {
            this.submittedAt = submittedAt;
            return this;
        }
        public PublicSubmissionResultResponseBuilder thankYouTitle(String thankYouTitle) {
            this.thankYouTitle = thankYouTitle;
            return this;
        }
        public PublicSubmissionResultResponseBuilder thankYouMessage(String thankYouMessage) {
            this.thankYouMessage = thankYouMessage;
            return this;
        }
        public PublicSubmissionResultResponseBuilder redirectUrl(String redirectUrl) {
            this.redirectUrl = redirectUrl;
            return this;
        }

        public PublicSubmissionResultResponse build() {
            PublicSubmissionResultResponse instance = new PublicSubmissionResultResponse();
            instance.submissionId = this.submissionId;
            instance.submittedAt = this.submittedAt;
            instance.thankYouTitle = this.thankYouTitle;
            instance.thankYouMessage = this.thankYouMessage;
            instance.redirectUrl = this.redirectUrl;
            return instance;
        }
    }
}

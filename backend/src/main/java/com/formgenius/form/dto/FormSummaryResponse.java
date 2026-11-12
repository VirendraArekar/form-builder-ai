package com.formgenius.form.dto;


import java.time.Instant;
import java.util.UUID;

public class FormSummaryResponse {
    private UUID id;
    private UUID workspaceId;
    private String title;
    private String description;
    private String status;
    private String publishedSlug;
    private boolean isPasswordProtected;
    private int submissionCount;
    private int viewCount;
    private Integer submissionLimit;
    private Instant startsAt;
    private Instant expiresAt;
    private Instant createdAt;
    private Instant updatedAt;
    private boolean isFavorite;

    public FormSummaryResponse() {}

    public FormSummaryResponse(UUID id, UUID workspaceId, String title, String description, String status, String publishedSlug, boolean isPasswordProtected, int submissionCount, int viewCount, Integer submissionLimit, Instant startsAt, Instant expiresAt, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.workspaceId = workspaceId;
        this.title = title;
        this.description = description;
        this.status = status;
        this.publishedSlug = publishedSlug;
        this.isPasswordProtected = isPasswordProtected;
        this.submissionCount = submissionCount;
        this.viewCount = viewCount;
        this.submissionLimit = submissionLimit;
        this.startsAt = startsAt;
        this.expiresAt = expiresAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public UUID getWorkspaceId() {
        return workspaceId;
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

    public int getSubmissionCount() {
        return submissionCount;
    }

    public int getViewCount() {
        return viewCount;
    }

    public Integer getSubmissionLimit() {
        return submissionLimit;
    }

    public Instant getStartsAt() {
        return startsAt;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setWorkspaceId(UUID workspaceId) {
        this.workspaceId = workspaceId;
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

    public void setSubmissionCount(int submissionCount) {
        this.submissionCount = submissionCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public void setSubmissionLimit(Integer submissionLimit) {
        this.submissionLimit = submissionLimit;
    }

    public void setStartsAt(Instant startsAt) {
        this.startsAt = startsAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public static FormSummaryResponseBuilder builder() {
        return new FormSummaryResponseBuilder();
    }
    public static class FormSummaryResponseBuilder {
        private UUID id;
        private UUID workspaceId;
        private String title;
        private String description;
        private String status;
        private String publishedSlug;
        private boolean isPasswordProtected;
        private int submissionCount;
        private int viewCount;
        private Integer submissionLimit;
        private Instant startsAt;
        private Instant expiresAt;
        private Instant createdAt;
        private Instant updatedAt;
        private boolean isFavorite;

        public FormSummaryResponseBuilder id(UUID id) {
            this.id = id;
            return this;
        }
        public FormSummaryResponseBuilder workspaceId(UUID workspaceId) {
            this.workspaceId = workspaceId;
            return this;
        }
        public FormSummaryResponseBuilder title(String title) {
            this.title = title;
            return this;
        }
        public FormSummaryResponseBuilder description(String description) {
            this.description = description;
            return this;
        }
        public FormSummaryResponseBuilder status(String status) {
            this.status = status;
            return this;
        }
        public FormSummaryResponseBuilder publishedSlug(String publishedSlug) {
            this.publishedSlug = publishedSlug;
            return this;
        }
        public FormSummaryResponseBuilder isPasswordProtected(boolean isPasswordProtected) {
            this.isPasswordProtected = isPasswordProtected;
            return this;
        }
        public FormSummaryResponseBuilder submissionCount(int submissionCount) {
            this.submissionCount = submissionCount;
            return this;
        }
        public FormSummaryResponseBuilder viewCount(int viewCount) {
            this.viewCount = viewCount;
            return this;
        }
        public FormSummaryResponseBuilder submissionLimit(Integer submissionLimit) {
            this.submissionLimit = submissionLimit;
            return this;
        }
        public FormSummaryResponseBuilder startsAt(Instant startsAt) {
            this.startsAt = startsAt;
            return this;
        }
        public FormSummaryResponseBuilder expiresAt(Instant expiresAt) {
            this.expiresAt = expiresAt;
            return this;
        }
        public FormSummaryResponseBuilder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }
        public FormSummaryResponseBuilder isFavorite(boolean isFavorite) {
            this.isFavorite = isFavorite;
            return this;
        }
        public FormSummaryResponseBuilder updatedAt(Instant updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public FormSummaryResponse build() {
            FormSummaryResponse instance = new FormSummaryResponse();
            instance.id = this.id;
            instance.workspaceId = this.workspaceId;
            instance.title = this.title;
            instance.description = this.description;
            instance.status = this.status;
            instance.publishedSlug = this.publishedSlug;
            instance.isPasswordProtected = this.isPasswordProtected;
            instance.submissionCount = this.submissionCount;
            instance.viewCount = this.viewCount;
            instance.submissionLimit = this.submissionLimit;
            instance.startsAt = this.startsAt;
            instance.expiresAt = this.expiresAt;
            instance.createdAt = this.createdAt;
            instance.updatedAt = this.updatedAt;
            instance.isFavorite = this.isFavorite;
            return instance;
        }
    }
}

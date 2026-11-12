package com.formgenius.form;

import com.formgenius.user.User;
import com.formgenius.workspace.Workspace;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "forms")
public class Form {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id", nullable = false)
    private Workspace workspace;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String status = "DRAFT"; // DRAFT, PUBLISHED, ARCHIVED

    @Column(name = "published_slug", unique = true)
    private String publishedSlug;

    @Column(name = "is_password_protected", nullable = false)
    private boolean isPasswordProtected = false;

    @Column(name = "access_password_hash")
    private String accessPasswordHash;

    @Column(name = "submission_limit")
    private Integer submissionLimit;

    @Column(name = "submission_count", nullable = false)
    private int submissionCount = 0;

    @Column(name = "view_count", nullable = false)
    private int viewCount = 0;

    @Column(name = "starts_at")
    private Instant startsAt;

    @Column(name = "expires_at")
    private Instant expiresAt;

    @Column(name = "redirect_url")
    private String redirectUrl;

    @Column(name = "custom_thank_you_title")
    private String customThankYouTitle;

    @Column(name = "custom_thank_you_message", columnDefinition = "TEXT")
    private String customThankYouMessage;

    @Column(name = "allow_multiple_submissions", nullable = false)
    private boolean allowMultipleSubmissions = true;

    @Column(name = "send_email_notification", nullable = false)
    private boolean sendEmailNotification = false;

    @Column(name = "notification_email")
    private String notificationEmail;

    @Column(name = "is_favorite", nullable = false)
    private boolean isFavorite = false;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public Form() {}

    public Form(UUID id, Workspace workspace, User createdBy, String title, String description, String status, String publishedSlug, boolean isPasswordProtected, String accessPasswordHash, Integer submissionLimit, int submissionCount, int viewCount, Instant startsAt, Instant expiresAt, String redirectUrl, String customThankYouTitle, String customThankYouMessage, boolean allowMultipleSubmissions, boolean sendEmailNotification, String notificationEmail, boolean isDeleted, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.workspace = workspace;
        this.createdBy = createdBy;
        this.title = title;
        this.description = description;
        this.status = status;
        this.publishedSlug = publishedSlug;
        this.isPasswordProtected = isPasswordProtected;
        this.accessPasswordHash = accessPasswordHash;
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
        this.isDeleted = isDeleted;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public Workspace getWorkspace() {
        return workspace;
    }

    public User getCreatedBy() {
        return createdBy;
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

    public String getAccessPasswordHash() {
        return accessPasswordHash;
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

    public boolean isAllowMultipleSubmissions() {
        return allowMultipleSubmissions;
    }

    public boolean isSendEmailNotification() {
        return sendEmailNotification;
    }

    public String getNotificationEmail() {
        return notificationEmail;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        this.isFavorite = favorite;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setWorkspace(Workspace workspace) {
        this.workspace = workspace;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
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

    public void setAccessPasswordHash(String accessPasswordHash) {
        this.accessPasswordHash = accessPasswordHash;
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

    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public static FormBuilder builder() {
        return new FormBuilder();
    }
    public static class FormBuilder {
        private UUID id;
        private Workspace workspace;
        private User createdBy;
        private String title;
        private String description;
        private String status = "DRAFT";
        private String publishedSlug;
        private boolean isPasswordProtected = false;
        private String accessPasswordHash;
        private Integer submissionLimit;
        private int submissionCount = 0;
        private int viewCount = 0;
        private Instant startsAt;
        private Instant expiresAt;
        private String redirectUrl;
        private String customThankYouTitle;
        private String customThankYouMessage;
        private boolean allowMultipleSubmissions = true;
        private boolean sendEmailNotification = false;
        private String notificationEmail;
        private boolean isFavorite = false;
        private boolean isDeleted = false;
        private Instant createdAt;
        private Instant updatedAt;

        public FormBuilder id(UUID id) {
            this.id = id;
            return this;
        }
        public FormBuilder workspace(Workspace workspace) {
            this.workspace = workspace;
            return this;
        }
        public FormBuilder createdBy(User createdBy) {
            this.createdBy = createdBy;
            return this;
        }
        public FormBuilder title(String title) {
            this.title = title;
            return this;
        }
        public FormBuilder description(String description) {
            this.description = description;
            return this;
        }
        public FormBuilder status(String status) {
            this.status = status;
            return this;
        }
        public FormBuilder publishedSlug(String publishedSlug) {
            this.publishedSlug = publishedSlug;
            return this;
        }
        public FormBuilder isPasswordProtected(boolean isPasswordProtected) {
            this.isPasswordProtected = isPasswordProtected;
            return this;
        }
        public FormBuilder accessPasswordHash(String accessPasswordHash) {
            this.accessPasswordHash = accessPasswordHash;
            return this;
        }
        public FormBuilder submissionLimit(Integer submissionLimit) {
            this.submissionLimit = submissionLimit;
            return this;
        }
        public FormBuilder submissionCount(int submissionCount) {
            this.submissionCount = submissionCount;
            return this;
        }
        public FormBuilder viewCount(int viewCount) {
            this.viewCount = viewCount;
            return this;
        }
        public FormBuilder startsAt(Instant startsAt) {
            this.startsAt = startsAt;
            return this;
        }
        public FormBuilder expiresAt(Instant expiresAt) {
            this.expiresAt = expiresAt;
            return this;
        }
        public FormBuilder redirectUrl(String redirectUrl) {
            this.redirectUrl = redirectUrl;
            return this;
        }
        public FormBuilder customThankYouTitle(String customThankYouTitle) {
            this.customThankYouTitle = customThankYouTitle;
            return this;
        }
        public FormBuilder customThankYouMessage(String customThankYouMessage) {
            this.customThankYouMessage = customThankYouMessage;
            return this;
        }
        public FormBuilder allowMultipleSubmissions(boolean allowMultipleSubmissions) {
            this.allowMultipleSubmissions = allowMultipleSubmissions;
            return this;
        }
        public FormBuilder sendEmailNotification(boolean sendEmailNotification) {
            this.sendEmailNotification = sendEmailNotification;
            return this;
        }
        public FormBuilder notificationEmail(String notificationEmail) {
            this.notificationEmail = notificationEmail;
            return this;
        }
        public FormBuilder isFavorite(boolean isFavorite) {
            this.isFavorite = isFavorite;
            return this;
        }
        public FormBuilder isDeleted(boolean isDeleted) {
            this.isDeleted = isDeleted;
            return this;
        }
        public FormBuilder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }
        public FormBuilder updatedAt(Instant updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Form build() {
            Form instance = new Form();
            instance.id = this.id;
            instance.workspace = this.workspace;
            instance.createdBy = this.createdBy;
            instance.title = this.title;
            instance.description = this.description;
            instance.status = this.status;
            instance.publishedSlug = this.publishedSlug;
            instance.isPasswordProtected = this.isPasswordProtected;
            instance.accessPasswordHash = this.accessPasswordHash;
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
            instance.isFavorite = this.isFavorite;
            instance.isDeleted = this.isDeleted;
            instance.createdAt = this.createdAt;
            instance.updatedAt = this.updatedAt;
            return instance;
        }
    }
}

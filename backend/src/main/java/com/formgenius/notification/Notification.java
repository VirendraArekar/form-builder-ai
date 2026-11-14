package com.formgenius.notification;

import com.formgenius.user.User;
import com.formgenius.workspace.Workspace;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id")
    private Workspace workspace;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(nullable = false)
    private String type = "INFO"; // INFO, SUBMISSION, ALERT, SYSTEM

    @Column(name = "link_url", length = 500)
    private String linkUrl;

    @Column(name = "is_read", nullable = false)
    private boolean isRead = false;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    public Notification() {}

    public Notification(UUID id, User user, Workspace workspace, String title, String message, String type, String linkUrl, boolean isRead, Instant createdAt) {
        this.id = id;
        this.user = user;
        this.workspace = workspace;
        this.title = title;
        this.message = message;
        this.type = type;
        this.linkUrl = linkUrl;
        this.isRead = isRead;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Workspace getWorkspace() {
        return workspace;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public String getType() {
        return type;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public boolean isRead() {
        return isRead;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setWorkspace(Workspace workspace) {
        this.workspace = workspace;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public void setRead(boolean isRead) {
        this.isRead = isRead;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public static NotificationBuilder builder() {
        return new NotificationBuilder();
    }
    public static class NotificationBuilder {
        private UUID id;
        private User user;
        private Workspace workspace;
        private String title;
        private String message;
        private String type = "INFO";
        private String linkUrl;
        private boolean isRead = false;
        private Instant createdAt;

        public NotificationBuilder id(UUID id) {
            this.id = id;
            return this;
        }
        public NotificationBuilder user(User user) {
            this.user = user;
            return this;
        }
        public NotificationBuilder workspace(Workspace workspace) {
            this.workspace = workspace;
            return this;
        }
        public NotificationBuilder title(String title) {
            this.title = title;
            return this;
        }
        public NotificationBuilder message(String message) {
            this.message = message;
            return this;
        }
        public NotificationBuilder type(String type) {
            this.type = type;
            return this;
        }
        public NotificationBuilder linkUrl(String linkUrl) {
            this.linkUrl = linkUrl;
            return this;
        }
        public NotificationBuilder isRead(boolean isRead) {
            this.isRead = isRead;
            return this;
        }
        public NotificationBuilder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Notification build() {
            Notification instance = new Notification();
            instance.id = this.id;
            instance.user = this.user;
            instance.workspace = this.workspace;
            instance.title = this.title;
            instance.message = this.message;
            instance.type = this.type;
            instance.linkUrl = this.linkUrl;
            instance.isRead = this.isRead;
            instance.createdAt = this.createdAt;
            return instance;
        }
    }
}

package com.formgenius.webhook;

import com.formgenius.form.Form;
import com.formgenius.workspace.Workspace;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "webhooks")
public class Webhook {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id", nullable = false)
    private Workspace workspace;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "form_id")
    private Form form;

    @Column(name = "target_url", nullable = false, length = 500)
    private String targetUrl;

    @Column(name = "secret_key", nullable = false)
    private String secretKey;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @Column(name = "subscribed_events", nullable = false, length = 500)
    private String subscribedEvents; // form.published,response.created,etc.

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public Webhook() {}

    public Webhook(UUID id, Workspace workspace, Form form, String targetUrl, String secretKey, boolean isActive, String subscribedEvents, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.workspace = workspace;
        this.form = form;
        this.targetUrl = targetUrl;
        this.secretKey = secretKey;
        this.isActive = isActive;
        this.subscribedEvents = subscribedEvents;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public Workspace getWorkspace() {
        return workspace;
    }

    public Form getForm() {
        return form;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public boolean isActive() {
        return isActive;
    }

    public String getSubscribedEvents() {
        return subscribedEvents;
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

    public void setForm(Form form) {
        this.form = form;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public void setSubscribedEvents(String subscribedEvents) {
        this.subscribedEvents = subscribedEvents;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public static WebhookBuilder builder() {
        return new WebhookBuilder();
    }
    public static class WebhookBuilder {
        private UUID id;
        private Workspace workspace;
        private Form form;
        private String targetUrl;
        private String secretKey;
        private boolean isActive = true;
        private String subscribedEvents;
        private Instant createdAt;
        private Instant updatedAt;

        public WebhookBuilder id(UUID id) {
            this.id = id;
            return this;
        }
        public WebhookBuilder workspace(Workspace workspace) {
            this.workspace = workspace;
            return this;
        }
        public WebhookBuilder form(Form form) {
            this.form = form;
            return this;
        }
        public WebhookBuilder targetUrl(String targetUrl) {
            this.targetUrl = targetUrl;
            return this;
        }
        public WebhookBuilder secretKey(String secretKey) {
            this.secretKey = secretKey;
            return this;
        }
        public WebhookBuilder isActive(boolean isActive) {
            this.isActive = isActive;
            return this;
        }
        public WebhookBuilder subscribedEvents(String subscribedEvents) {
            this.subscribedEvents = subscribedEvents;
            return this;
        }
        public WebhookBuilder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }
        public WebhookBuilder updatedAt(Instant updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Webhook build() {
            Webhook instance = new Webhook();
            instance.id = this.id;
            instance.workspace = this.workspace;
            instance.form = this.form;
            instance.targetUrl = this.targetUrl;
            instance.secretKey = this.secretKey;
            instance.isActive = this.isActive;
            instance.subscribedEvents = this.subscribedEvents;
            instance.createdAt = this.createdAt;
            instance.updatedAt = this.updatedAt;
            return instance;
        }
    }
}

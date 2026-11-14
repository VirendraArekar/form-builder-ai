package com.formgenius.webhook;


import java.time.Instant;
import java.util.UUID;

public class WebhookDto {
    private UUID id;
    private UUID workspaceId;
    private UUID formId;
    private String targetUrl;
    private String secretKey;
    private boolean isActive;
    private String subscribedEvents;
    private Instant createdAt;

    public WebhookDto() {}

    public WebhookDto(UUID id, UUID workspaceId, UUID formId, String targetUrl, String secretKey, boolean isActive, String subscribedEvents, Instant createdAt) {
        this.id = id;
        this.workspaceId = workspaceId;
        this.formId = formId;
        this.targetUrl = targetUrl;
        this.secretKey = secretKey;
        this.isActive = isActive;
        this.subscribedEvents = subscribedEvents;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public UUID getWorkspaceId() {
        return workspaceId;
    }

    public UUID getFormId() {
        return formId;
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

    public void setId(UUID id) {
        this.id = id;
    }

    public void setWorkspaceId(UUID workspaceId) {
        this.workspaceId = workspaceId;
    }

    public void setFormId(UUID formId) {
        this.formId = formId;
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

    public static WebhookDtoBuilder builder() {
        return new WebhookDtoBuilder();
    }
    public static class WebhookDtoBuilder {
        private UUID id;
        private UUID workspaceId;
        private UUID formId;
        private String targetUrl;
        private String secretKey;
        private boolean isActive;
        private String subscribedEvents;
        private Instant createdAt;

        public WebhookDtoBuilder id(UUID id) {
            this.id = id;
            return this;
        }
        public WebhookDtoBuilder workspaceId(UUID workspaceId) {
            this.workspaceId = workspaceId;
            return this;
        }
        public WebhookDtoBuilder formId(UUID formId) {
            this.formId = formId;
            return this;
        }
        public WebhookDtoBuilder targetUrl(String targetUrl) {
            this.targetUrl = targetUrl;
            return this;
        }
        public WebhookDtoBuilder secretKey(String secretKey) {
            this.secretKey = secretKey;
            return this;
        }
        public WebhookDtoBuilder isActive(boolean isActive) {
            this.isActive = isActive;
            return this;
        }
        public WebhookDtoBuilder subscribedEvents(String subscribedEvents) {
            this.subscribedEvents = subscribedEvents;
            return this;
        }
        public WebhookDtoBuilder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public WebhookDto build() {
            WebhookDto instance = new WebhookDto();
            instance.id = this.id;
            instance.workspaceId = this.workspaceId;
            instance.formId = this.formId;
            instance.targetUrl = this.targetUrl;
            instance.secretKey = this.secretKey;
            instance.isActive = this.isActive;
            instance.subscribedEvents = this.subscribedEvents;
            instance.createdAt = this.createdAt;
            return instance;
        }
    }
}

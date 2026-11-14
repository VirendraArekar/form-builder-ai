package com.formgenius.webhook;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public class CreateWebhookRequest {
    @NotBlank(message = "Target URL is required")
    private String targetUrl;
    private UUID formId;
    @NotBlank(message = "Subscribed events required")
    private String subscribedEvents;

    public CreateWebhookRequest() {}

    public CreateWebhookRequest(String targetUrl, UUID formId, String subscribedEvents) {
        this.targetUrl = targetUrl;
        this.formId = formId;
        this.subscribedEvents = subscribedEvents;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public UUID getFormId() {
        return formId;
    }

    public String getSubscribedEvents() {
        return subscribedEvents;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public void setFormId(UUID formId) {
        this.formId = formId;
    }

    public void setSubscribedEvents(String subscribedEvents) {
        this.subscribedEvents = subscribedEvents;
    }
}

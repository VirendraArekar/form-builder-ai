package com.formgenius.webhook;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "webhook_deliveries")
public class WebhookDelivery {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "webhook_id", nullable = false)
    private Webhook webhook;

    @Column(name = "event_type", nullable = false)
    private String eventType;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false)
    private String payload;

    @Column(name = "response_status")
    private Integer responseStatus;

    @Column(name = "response_body", columnDefinition = "TEXT")
    private String responseBody;

    @Column(name = "duration_ms")
    private Integer durationMs;

    @Column(name = "is_successful", nullable = false)
    private boolean isSuccessful = false;

    @CreationTimestamp
    @Column(name = "delivered_at", nullable = false, updatable = false)
    private Instant deliveredAt;

    public WebhookDelivery() {}

    public WebhookDelivery(UUID id, Webhook webhook, String eventType, String payload, Integer responseStatus, String responseBody, Integer durationMs, boolean isSuccessful, Instant deliveredAt) {
        this.id = id;
        this.webhook = webhook;
        this.eventType = eventType;
        this.payload = payload;
        this.responseStatus = responseStatus;
        this.responseBody = responseBody;
        this.durationMs = durationMs;
        this.isSuccessful = isSuccessful;
        this.deliveredAt = deliveredAt;
    }

    public UUID getId() {
        return id;
    }

    public Webhook getWebhook() {
        return webhook;
    }

    public String getEventType() {
        return eventType;
    }

    public String getPayload() {
        return payload;
    }

    public Integer getResponseStatus() {
        return responseStatus;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public Integer getDurationMs() {
        return durationMs;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public Instant getDeliveredAt() {
        return deliveredAt;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setWebhook(Webhook webhook) {
        this.webhook = webhook;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public void setResponseStatus(Integer responseStatus) {
        this.responseStatus = responseStatus;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public void setDurationMs(Integer durationMs) {
        this.durationMs = durationMs;
    }

    public void setSuccessful(boolean isSuccessful) {
        this.isSuccessful = isSuccessful;
    }

    public void setDeliveredAt(Instant deliveredAt) {
        this.deliveredAt = deliveredAt;
    }

    public static WebhookDeliveryBuilder builder() {
        return new WebhookDeliveryBuilder();
    }
    public static class WebhookDeliveryBuilder {
        private UUID id;
        private Webhook webhook;
        private String eventType;
        private String payload;
        private Integer responseStatus;
        private String responseBody;
        private Integer durationMs;
        private boolean isSuccessful = false;
        private Instant deliveredAt;

        public WebhookDeliveryBuilder id(UUID id) {
            this.id = id;
            return this;
        }
        public WebhookDeliveryBuilder webhook(Webhook webhook) {
            this.webhook = webhook;
            return this;
        }
        public WebhookDeliveryBuilder eventType(String eventType) {
            this.eventType = eventType;
            return this;
        }
        public WebhookDeliveryBuilder payload(String payload) {
            this.payload = payload;
            return this;
        }
        public WebhookDeliveryBuilder responseStatus(Integer responseStatus) {
            this.responseStatus = responseStatus;
            return this;
        }
        public WebhookDeliveryBuilder responseBody(String responseBody) {
            this.responseBody = responseBody;
            return this;
        }
        public WebhookDeliveryBuilder durationMs(Integer durationMs) {
            this.durationMs = durationMs;
            return this;
        }
        public WebhookDeliveryBuilder isSuccessful(boolean isSuccessful) {
            this.isSuccessful = isSuccessful;
            return this;
        }
        public WebhookDeliveryBuilder deliveredAt(Instant deliveredAt) {
            this.deliveredAt = deliveredAt;
            return this;
        }

        public WebhookDelivery build() {
            WebhookDelivery instance = new WebhookDelivery();
            instance.id = this.id;
            instance.webhook = this.webhook;
            instance.eventType = this.eventType;
            instance.payload = this.payload;
            instance.responseStatus = this.responseStatus;
            instance.responseBody = this.responseBody;
            instance.durationMs = this.durationMs;
            instance.isSuccessful = this.isSuccessful;
            instance.deliveredAt = this.deliveredAt;
            return instance;
        }
    }
}

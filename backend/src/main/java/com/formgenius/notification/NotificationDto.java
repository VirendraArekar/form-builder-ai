package com.formgenius.notification;


import java.time.Instant;
import java.util.UUID;

public class NotificationDto {
    private UUID id;
    private String title;
    private String message;
    private String type;
    private String linkUrl;
    private boolean isRead;
    private Instant createdAt;

    public NotificationDto() {}

    public NotificationDto(UUID id, String title, String message, String type, String linkUrl, boolean isRead, Instant createdAt) {
        this.id = id;
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

    public static NotificationDtoBuilder builder() {
        return new NotificationDtoBuilder();
    }
    public static class NotificationDtoBuilder {
        private UUID id;
        private String title;
        private String message;
        private String type;
        private String linkUrl;
        private boolean isRead;
        private Instant createdAt;

        public NotificationDtoBuilder id(UUID id) {
            this.id = id;
            return this;
        }
        public NotificationDtoBuilder title(String title) {
            this.title = title;
            return this;
        }
        public NotificationDtoBuilder message(String message) {
            this.message = message;
            return this;
        }
        public NotificationDtoBuilder type(String type) {
            this.type = type;
            return this;
        }
        public NotificationDtoBuilder linkUrl(String linkUrl) {
            this.linkUrl = linkUrl;
            return this;
        }
        public NotificationDtoBuilder isRead(boolean isRead) {
            this.isRead = isRead;
            return this;
        }
        public NotificationDtoBuilder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public NotificationDto build() {
            NotificationDto instance = new NotificationDto();
            instance.id = this.id;
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

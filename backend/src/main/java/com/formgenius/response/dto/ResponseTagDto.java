package com.formgenius.response.dto;


import java.time.Instant;
import java.util.UUID;

public class ResponseTagDto {
    private UUID id;
    private String tagName;
    private String color;
    private Instant createdAt;

    public ResponseTagDto() {}

    public ResponseTagDto(UUID id, String tagName, String color, Instant createdAt) {
        this.id = id;
        this.tagName = tagName;
        this.color = color;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public String getTagName() {
        return tagName;
    }

    public String getColor() {
        return color;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public static ResponseTagDtoBuilder builder() {
        return new ResponseTagDtoBuilder();
    }
    public static class ResponseTagDtoBuilder {
        private UUID id;
        private String tagName;
        private String color;
        private Instant createdAt;

        public ResponseTagDtoBuilder id(UUID id) {
            this.id = id;
            return this;
        }
        public ResponseTagDtoBuilder tagName(String tagName) {
            this.tagName = tagName;
            return this;
        }
        public ResponseTagDtoBuilder color(String color) {
            this.color = color;
            return this;
        }
        public ResponseTagDtoBuilder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public ResponseTagDto build() {
            ResponseTagDto instance = new ResponseTagDto();
            instance.id = this.id;
            instance.tagName = this.tagName;
            instance.color = this.color;
            instance.createdAt = this.createdAt;
            return instance;
        }
    }
}

package com.formgenius.response;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "response_tags")
public class ResponseTag {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "response_id", nullable = false)
    private FormResponse response;

    @Column(name = "tag_name", nullable = false)
    private String tagName;

    @Column
    private String color = "#6366F1";

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    public ResponseTag() {}

    public ResponseTag(UUID id, FormResponse response, String tagName, String color, Instant createdAt) {
        this.id = id;
        this.response = response;
        this.tagName = tagName;
        this.color = color;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public FormResponse getResponse() {
        return response;
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

    public void setResponse(FormResponse response) {
        this.response = response;
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

    public static ResponseTagBuilder builder() {
        return new ResponseTagBuilder();
    }
    public static class ResponseTagBuilder {
        private UUID id;
        private FormResponse response;
        private String tagName;
        private String color = "#6366F1";
        private Instant createdAt;

        public ResponseTagBuilder id(UUID id) {
            this.id = id;
            return this;
        }
        public ResponseTagBuilder response(FormResponse response) {
            this.response = response;
            return this;
        }
        public ResponseTagBuilder tagName(String tagName) {
            this.tagName = tagName;
            return this;
        }
        public ResponseTagBuilder color(String color) {
            this.color = color;
            return this;
        }
        public ResponseTagBuilder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public ResponseTag build() {
            ResponseTag instance = new ResponseTag();
            instance.id = this.id;
            instance.response = this.response;
            instance.tagName = this.tagName;
            instance.color = this.color;
            instance.createdAt = this.createdAt;
            return instance;
        }
    }
}

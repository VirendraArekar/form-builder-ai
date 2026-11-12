package com.formgenius.form;

import com.formgenius.user.User;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "form_publications")
public class FormPublication {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "form_id", nullable = false)
    private Form form;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "published_by", nullable = false)
    private User publishedBy;

    @Column(name = "published_slug", nullable = false)
    private String publishedSlug;

    @CreationTimestamp
    @Column(name = "published_at", nullable = false, updatable = false)
    private Instant publishedAt;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    public FormPublication() {}

    public FormPublication(UUID id, Form form, User publishedBy, String publishedSlug, Instant publishedAt, boolean isActive) {
        this.id = id;
        this.form = form;
        this.publishedBy = publishedBy;
        this.publishedSlug = publishedSlug;
        this.publishedAt = publishedAt;
        this.isActive = isActive;
    }

    public UUID getId() {
        return id;
    }

    public Form getForm() {
        return form;
    }

    public User getPublishedBy() {
        return publishedBy;
    }

    public String getPublishedSlug() {
        return publishedSlug;
    }

    public Instant getPublishedAt() {
        return publishedAt;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setForm(Form form) {
        this.form = form;
    }

    public void setPublishedBy(User publishedBy) {
        this.publishedBy = publishedBy;
    }

    public void setPublishedSlug(String publishedSlug) {
        this.publishedSlug = publishedSlug;
    }

    public void setPublishedAt(Instant publishedAt) {
        this.publishedAt = publishedAt;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public static FormPublicationBuilder builder() {
        return new FormPublicationBuilder();
    }
    public static class FormPublicationBuilder {
        private UUID id;
        private Form form;
        private User publishedBy;
        private String publishedSlug;
        private Instant publishedAt;
        private boolean isActive = true;

        public FormPublicationBuilder id(UUID id) {
            this.id = id;
            return this;
        }
        public FormPublicationBuilder form(Form form) {
            this.form = form;
            return this;
        }
        public FormPublicationBuilder publishedBy(User publishedBy) {
            this.publishedBy = publishedBy;
            return this;
        }
        public FormPublicationBuilder publishedSlug(String publishedSlug) {
            this.publishedSlug = publishedSlug;
            return this;
        }
        public FormPublicationBuilder publishedAt(Instant publishedAt) {
            this.publishedAt = publishedAt;
            return this;
        }
        public FormPublicationBuilder isActive(boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public FormPublication build() {
            FormPublication instance = new FormPublication();
            instance.id = this.id;
            instance.form = this.form;
            instance.publishedBy = this.publishedBy;
            instance.publishedSlug = this.publishedSlug;
            instance.publishedAt = this.publishedAt;
            instance.isActive = this.isActive;
            return instance;
        }
    }
}

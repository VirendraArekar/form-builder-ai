package com.formgenius.form;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "form_sections")
public class FormSection {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "form_id", nullable = false)
    private Form form;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "order_index", nullable = false)
    private int orderIndex = 0;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public FormSection() {}

    public FormSection(UUID id, Form form, String title, String description, int orderIndex, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.form = form;
        this.title = title;
        this.description = description;
        this.orderIndex = orderIndex;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public Form getForm() {
        return form;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getOrderIndex() {
        return orderIndex;
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

    public void setForm(Form form) {
        this.form = form;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public static FormSectionBuilder builder() {
        return new FormSectionBuilder();
    }
    public static class FormSectionBuilder {
        private UUID id;
        private Form form;
        private String title;
        private String description;
        private int orderIndex = 0;
        private Instant createdAt;
        private Instant updatedAt;

        public FormSectionBuilder id(UUID id) {
            this.id = id;
            return this;
        }
        public FormSectionBuilder form(Form form) {
            this.form = form;
            return this;
        }
        public FormSectionBuilder title(String title) {
            this.title = title;
            return this;
        }
        public FormSectionBuilder description(String description) {
            this.description = description;
            return this;
        }
        public FormSectionBuilder orderIndex(int orderIndex) {
            this.orderIndex = orderIndex;
            return this;
        }
        public FormSectionBuilder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }
        public FormSectionBuilder updatedAt(Instant updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public FormSection build() {
            FormSection instance = new FormSection();
            instance.id = this.id;
            instance.form = this.form;
            instance.title = this.title;
            instance.description = this.description;
            instance.orderIndex = this.orderIndex;
            instance.createdAt = this.createdAt;
            instance.updatedAt = this.updatedAt;
            return instance;
        }
    }
}

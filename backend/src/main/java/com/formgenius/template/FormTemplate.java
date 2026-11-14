package com.formgenius.template;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "form_templates")
public class FormTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String category;

    @Column
    private String icon = "FileText";

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "template_schema", nullable = false)
    private String templateSchema;

    @Column(name = "is_featured", nullable = false)
    private boolean isFeatured = false;

    @Column(name = "usage_count", nullable = false)
    private int usageCount = 0;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    public FormTemplate() {}

    public FormTemplate(UUID id, String title, String description, String category, String icon, String templateSchema, boolean isFeatured, int usageCount, Instant createdAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.icon = icon;
        this.templateSchema = templateSchema;
        this.isFeatured = isFeatured;
        this.usageCount = usageCount;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public String getIcon() {
        return icon;
    }

    public String getTemplateSchema() {
        return templateSchema;
    }

    public boolean isFeatured() {
        return isFeatured;
    }

    public int getUsageCount() {
        return usageCount;
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

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setTemplateSchema(String templateSchema) {
        this.templateSchema = templateSchema;
    }

    public void setFeatured(boolean isFeatured) {
        this.isFeatured = isFeatured;
    }

    public void setUsageCount(int usageCount) {
        this.usageCount = usageCount;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public static FormTemplateBuilder builder() {
        return new FormTemplateBuilder();
    }
    public static class FormTemplateBuilder {
        private UUID id;
        private String title;
        private String description;
        private String category;
        private String icon = "FileText";
        private String templateSchema;
        private boolean isFeatured = false;
        private int usageCount = 0;
        private Instant createdAt;

        public FormTemplateBuilder id(UUID id) {
            this.id = id;
            return this;
        }
        public FormTemplateBuilder title(String title) {
            this.title = title;
            return this;
        }
        public FormTemplateBuilder description(String description) {
            this.description = description;
            return this;
        }
        public FormTemplateBuilder category(String category) {
            this.category = category;
            return this;
        }
        public FormTemplateBuilder icon(String icon) {
            this.icon = icon;
            return this;
        }
        public FormTemplateBuilder templateSchema(String templateSchema) {
            this.templateSchema = templateSchema;
            return this;
        }
        public FormTemplateBuilder isFeatured(boolean isFeatured) {
            this.isFeatured = isFeatured;
            return this;
        }
        public FormTemplateBuilder usageCount(int usageCount) {
            this.usageCount = usageCount;
            return this;
        }
        public FormTemplateBuilder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public FormTemplate build() {
            FormTemplate instance = new FormTemplate();
            instance.id = this.id;
            instance.title = this.title;
            instance.description = this.description;
            instance.category = this.category;
            instance.icon = this.icon;
            instance.templateSchema = this.templateSchema;
            instance.isFeatured = this.isFeatured;
            instance.usageCount = this.usageCount;
            instance.createdAt = this.createdAt;
            return instance;
        }
    }
}

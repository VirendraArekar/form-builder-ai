package com.formgenius.form;

import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "form_questions")
public class FormQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "form_id", nullable = false)
    private Form form;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id")
    private FormSection section;

    @Column(name = "field_key", nullable = false)
    private String fieldKey;

    @Column(nullable = false, length = 500)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "question_type", nullable = false)
    private String questionType;

    @Column
    private String placeholder;

    @Column(name = "is_required", nullable = false)
    private boolean isRequired = false;

    @Column(name = "default_value", columnDefinition = "TEXT")
    private String defaultValue;

    @Column(name = "min_length")
    private Integer minLength;

    @Column(name = "max_length")
    private Integer maxLength;

    @Column(name = "min_value")
    private Double minValue;

    @Column(name = "max_value")
    private Double maxValue;

    @Column(name = "regex_pattern", length = 500)
    private String regexPattern;

    @Column(name = "allowed_file_types", length = 500)
    private String allowedFileTypes;

    @Column(name = "max_file_size_mb")
    private Integer maxFileSizeMb;

    @Column(name = "order_index", nullable = false)
    private int orderIndex = 0;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "config_json")
    private String configJson;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public FormQuestion() {}

    public FormQuestion(UUID id, Form form, FormSection section, String fieldKey, String title, String description, String questionType, String placeholder, boolean isRequired, String defaultValue, Integer minLength, Integer maxLength, Double minValue, Double maxValue, String regexPattern, String allowedFileTypes, Integer maxFileSizeMb, int orderIndex, String configJson, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.form = form;
        this.section = section;
        this.fieldKey = fieldKey;
        this.title = title;
        this.description = description;
        this.questionType = questionType;
        this.placeholder = placeholder;
        this.isRequired = isRequired;
        this.defaultValue = defaultValue;
        this.minLength = minLength;
        this.maxLength = maxLength;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.regexPattern = regexPattern;
        this.allowedFileTypes = allowedFileTypes;
        this.maxFileSizeMb = maxFileSizeMb;
        this.orderIndex = orderIndex;
        this.configJson = configJson;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public Form getForm() {
        return form;
    }

    public FormSection getSection() {
        return section;
    }

    public String getFieldKey() {
        return fieldKey;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getQuestionType() {
        return questionType;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public boolean isRequired() {
        return isRequired;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public Integer getMinLength() {
        return minLength;
    }

    public Integer getMaxLength() {
        return maxLength;
    }

    public Double getMinValue() {
        return minValue;
    }

    public Double getMaxValue() {
        return maxValue;
    }

    public String getRegexPattern() {
        return regexPattern;
    }

    public String getAllowedFileTypes() {
        return allowedFileTypes;
    }

    public Integer getMaxFileSizeMb() {
        return maxFileSizeMb;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public String getConfigJson() {
        return configJson;
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

    public void setSection(FormSection section) {
        this.section = section;
    }

    public void setFieldKey(String fieldKey) {
        this.fieldKey = fieldKey;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    public void setRequired(boolean isRequired) {
        this.isRequired = isRequired;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public void setMinLength(Integer minLength) {
        this.minLength = minLength;
    }

    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }

    public void setMinValue(Double minValue) {
        this.minValue = minValue;
    }

    public void setMaxValue(Double maxValue) {
        this.maxValue = maxValue;
    }

    public void setRegexPattern(String regexPattern) {
        this.regexPattern = regexPattern;
    }

    public void setAllowedFileTypes(String allowedFileTypes) {
        this.allowedFileTypes = allowedFileTypes;
    }

    public void setMaxFileSizeMb(Integer maxFileSizeMb) {
        this.maxFileSizeMb = maxFileSizeMb;
    }

    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }

    public void setConfigJson(String configJson) {
        this.configJson = configJson;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public static FormQuestionBuilder builder() {
        return new FormQuestionBuilder();
    }
    public static class FormQuestionBuilder {
        private UUID id;
        private Form form;
        private FormSection section;
        private String fieldKey;
        private String title;
        private String description;
        private String questionType;
        private String placeholder;
        private boolean isRequired = false;
        private String defaultValue;
        private Integer minLength;
        private Integer maxLength;
        private Double minValue;
        private Double maxValue;
        private String regexPattern;
        private String allowedFileTypes;
        private Integer maxFileSizeMb;
        private int orderIndex = 0;
        private String configJson;
        private Instant createdAt;
        private Instant updatedAt;

        public FormQuestionBuilder id(UUID id) {
            this.id = id;
            return this;
        }
        public FormQuestionBuilder form(Form form) {
            this.form = form;
            return this;
        }
        public FormQuestionBuilder section(FormSection section) {
            this.section = section;
            return this;
        }
        public FormQuestionBuilder fieldKey(String fieldKey) {
            this.fieldKey = fieldKey;
            return this;
        }
        public FormQuestionBuilder title(String title) {
            this.title = title;
            return this;
        }
        public FormQuestionBuilder description(String description) {
            this.description = description;
            return this;
        }
        public FormQuestionBuilder questionType(String questionType) {
            this.questionType = questionType;
            return this;
        }
        public FormQuestionBuilder placeholder(String placeholder) {
            this.placeholder = placeholder;
            return this;
        }
        public FormQuestionBuilder isRequired(boolean isRequired) {
            this.isRequired = isRequired;
            return this;
        }
        public FormQuestionBuilder defaultValue(String defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }
        public FormQuestionBuilder minLength(Integer minLength) {
            this.minLength = minLength;
            return this;
        }
        public FormQuestionBuilder maxLength(Integer maxLength) {
            this.maxLength = maxLength;
            return this;
        }
        public FormQuestionBuilder minValue(Double minValue) {
            this.minValue = minValue;
            return this;
        }
        public FormQuestionBuilder maxValue(Double maxValue) {
            this.maxValue = maxValue;
            return this;
        }
        public FormQuestionBuilder regexPattern(String regexPattern) {
            this.regexPattern = regexPattern;
            return this;
        }
        public FormQuestionBuilder allowedFileTypes(String allowedFileTypes) {
            this.allowedFileTypes = allowedFileTypes;
            return this;
        }
        public FormQuestionBuilder maxFileSizeMb(Integer maxFileSizeMb) {
            this.maxFileSizeMb = maxFileSizeMb;
            return this;
        }
        public FormQuestionBuilder orderIndex(int orderIndex) {
            this.orderIndex = orderIndex;
            return this;
        }
        public FormQuestionBuilder configJson(String configJson) {
            this.configJson = configJson;
            return this;
        }
        public FormQuestionBuilder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }
        public FormQuestionBuilder updatedAt(Instant updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public FormQuestion build() {
            FormQuestion instance = new FormQuestion();
            instance.id = this.id;
            instance.form = this.form;
            instance.section = this.section;
            instance.fieldKey = this.fieldKey;
            instance.title = this.title;
            instance.description = this.description;
            instance.questionType = this.questionType;
            instance.placeholder = this.placeholder;
            instance.isRequired = this.isRequired;
            instance.defaultValue = this.defaultValue;
            instance.minLength = this.minLength;
            instance.maxLength = this.maxLength;
            instance.minValue = this.minValue;
            instance.maxValue = this.maxValue;
            instance.regexPattern = this.regexPattern;
            instance.allowedFileTypes = this.allowedFileTypes;
            instance.maxFileSizeMb = this.maxFileSizeMb;
            instance.orderIndex = this.orderIndex;
            instance.configJson = this.configJson;
            instance.createdAt = this.createdAt;
            instance.updatedAt = this.updatedAt;
            return instance;
        }
    }
}

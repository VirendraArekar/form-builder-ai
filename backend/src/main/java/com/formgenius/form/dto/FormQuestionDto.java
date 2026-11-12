package com.formgenius.form.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FormQuestionDto {
    private String id;
    private String sectionId;
    private String fieldKey;
    private String title;
    private String description;
    private String questionType;
    private String placeholder;
    private boolean isRequired;
    private String defaultValue;
    private Integer minLength;
    private Integer maxLength;
    private Double minValue;
    private Double maxValue;
    private String regexPattern;
    private String allowedFileTypes;
    private Integer maxFileSizeMb;
    private int orderIndex;
    private String configJson;
    private List<FormOptionDto> options = new ArrayList<>();

    public FormQuestionDto() {}

    public FormQuestionDto(String id, String sectionId, String fieldKey, String title, String description, String questionType, String placeholder, boolean isRequired, String defaultValue, Integer minLength, Integer maxLength, Double minValue, Double maxValue, String regexPattern, String allowedFileTypes, Integer maxFileSizeMb, int orderIndex, String configJson, List<FormOptionDto> options) {
        this.id = id;
        this.sectionId = sectionId;
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
        this.options = options != null ? options : new ArrayList<>();
    }

    public FormQuestionDto(UUID id, UUID sectionId, String fieldKey, String title, String description, String questionType, String placeholder, boolean isRequired, String defaultValue, Integer minLength, Integer maxLength, Double minValue, Double maxValue, String regexPattern, String allowedFileTypes, Integer maxFileSizeMb, int orderIndex, String configJson, List<FormOptionDto> options) {
        this(id != null ? id.toString() : null, sectionId != null ? sectionId.toString() : null, fieldKey, title, description, questionType, placeholder, isRequired, defaultValue, minLength, maxLength, minValue, maxValue, regexPattern, allowedFileTypes, maxFileSizeMb, orderIndex, configJson, options);
    }

    public String getId() {
        return id;
    }

    public String getSectionId() {
        return sectionId;
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

    public String getType() {
        return questionType;
    }

    public void setType(String type) {
        this.questionType = type;
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

    public List<FormOptionDto> getOptions() {
        return options;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setId(UUID id) {
        this.id = id != null ? id.toString() : null;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public void setSectionId(UUID sectionId) {
        this.sectionId = sectionId != null ? sectionId.toString() : null;
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

    public void setOptions(List<FormOptionDto> options) {
        this.options = options;
    }

    public static FormQuestionDtoBuilder builder() {
        return new FormQuestionDtoBuilder();
    }

    public static class FormQuestionDtoBuilder {
        private String id;
        private String sectionId;
        private String fieldKey;
        private String title;
        private String description;
        private String questionType;
        private String placeholder;
        private boolean isRequired;
        private String defaultValue;
        private Integer minLength;
        private Integer maxLength;
        private Double minValue;
        private Double maxValue;
        private String regexPattern;
        private String allowedFileTypes;
        private Integer maxFileSizeMb;
        private int orderIndex;
        private String configJson;
        private List<FormOptionDto> options = new ArrayList<>();

        public FormQuestionDtoBuilder id(String id) {
            this.id = id;
            return this;
        }

        public FormQuestionDtoBuilder id(UUID id) {
            this.id = id != null ? id.toString() : null;
            return this;
        }

        public FormQuestionDtoBuilder sectionId(String sectionId) {
            this.sectionId = sectionId;
            return this;
        }

        public FormQuestionDtoBuilder sectionId(UUID sectionId) {
            this.sectionId = sectionId != null ? sectionId.toString() : null;
            return this;
        }

        public FormQuestionDtoBuilder fieldKey(String fieldKey) {
            this.fieldKey = fieldKey;
            return this;
        }

        public FormQuestionDtoBuilder title(String title) {
            this.title = title;
            return this;
        }

        public FormQuestionDtoBuilder description(String description) {
            this.description = description;
            return this;
        }

        public FormQuestionDtoBuilder questionType(String questionType) {
            this.questionType = questionType;
            return this;
        }

        public FormQuestionDtoBuilder placeholder(String placeholder) {
            this.placeholder = placeholder;
            return this;
        }

        public FormQuestionDtoBuilder isRequired(boolean isRequired) {
            this.isRequired = isRequired;
            return this;
        }

        public FormQuestionDtoBuilder defaultValue(String defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }

        public FormQuestionDtoBuilder minLength(Integer minLength) {
            this.minLength = minLength;
            return this;
        }

        public FormQuestionDtoBuilder maxLength(Integer maxLength) {
            this.maxLength = maxLength;
            return this;
        }

        public FormQuestionDtoBuilder minValue(Double minValue) {
            this.minValue = minValue;
            return this;
        }

        public FormQuestionDtoBuilder maxValue(Double maxValue) {
            this.maxValue = maxValue;
            return this;
        }

        public FormQuestionDtoBuilder regexPattern(String regexPattern) {
            this.regexPattern = regexPattern;
            return this;
        }

        public FormQuestionDtoBuilder allowedFileTypes(String allowedFileTypes) {
            this.allowedFileTypes = allowedFileTypes;
            return this;
        }

        public FormQuestionDtoBuilder maxFileSizeMb(Integer maxFileSizeMb) {
            this.maxFileSizeMb = maxFileSizeMb;
            return this;
        }

        public FormQuestionDtoBuilder orderIndex(int orderIndex) {
            this.orderIndex = orderIndex;
            return this;
        }

        public FormQuestionDtoBuilder configJson(String configJson) {
            this.configJson = configJson;
            return this;
        }

        public FormQuestionDtoBuilder options(List<FormOptionDto> options) {
            this.options = options;
            return this;
        }

        public FormQuestionDto build() {
            FormQuestionDto instance = new FormQuestionDto();
            instance.id = this.id;
            instance.sectionId = this.sectionId;
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
            instance.options = this.options != null ? this.options : new ArrayList<>();
            return instance;
        }
    }
}

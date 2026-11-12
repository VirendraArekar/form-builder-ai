package com.formgenius.form.dto;

import java.util.UUID;

public class FormOptionDto {
    private String id;
    private String label;
    private String value;
    private int orderIndex;
    private boolean isDefault;

    public FormOptionDto() {}

    public FormOptionDto(String id, String label, String value, int orderIndex, boolean isDefault) {
        this.id = id;
        this.label = label;
        this.value = value;
        this.orderIndex = orderIndex;
        this.isDefault = isDefault;
    }

    public FormOptionDto(UUID id, String label, String value, int orderIndex, boolean isDefault) {
        this(id != null ? id.toString() : null, label, value, orderIndex, isDefault);
    }

    public String getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public String getValue() {
        return value;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setId(UUID id) {
        this.id = id != null ? id.toString() : null;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    public static FormOptionDtoBuilder builder() {
        return new FormOptionDtoBuilder();
    }

    public static class FormOptionDtoBuilder {
        private String id;
        private String label;
        private String value;
        private int orderIndex;
        private boolean isDefault;

        public FormOptionDtoBuilder id(String id) {
            this.id = id;
            return this;
        }

        public FormOptionDtoBuilder id(UUID id) {
            this.id = id != null ? id.toString() : null;
            return this;
        }

        public FormOptionDtoBuilder label(String label) {
            this.label = label;
            return this;
        }

        public FormOptionDtoBuilder value(String value) {
            this.value = value;
            return this;
        }

        public FormOptionDtoBuilder orderIndex(int orderIndex) {
            this.orderIndex = orderIndex;
            return this;
        }

        public FormOptionDtoBuilder isDefault(boolean isDefault) {
            this.isDefault = isDefault;
            return this;
        }

        public FormOptionDto build() {
            FormOptionDto instance = new FormOptionDto();
            instance.id = this.id;
            instance.label = this.label;
            instance.value = this.value;
            instance.orderIndex = this.orderIndex;
            instance.isDefault = this.isDefault;
            return instance;
        }
    }
}

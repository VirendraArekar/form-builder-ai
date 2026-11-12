package com.formgenius.form.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FormSectionDto {
    private String id;
    private String title;
    private String description;
    private int orderIndex;
    private List<FormQuestionDto> questions = new ArrayList<>();

    public FormSectionDto() {}

    public FormSectionDto(String id, String title, String description, int orderIndex) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.orderIndex = orderIndex;
        this.questions = new ArrayList<>();
    }

    public FormSectionDto(UUID id, String title, String description, int orderIndex) {
        this(id != null ? id.toString() : null, title, description, orderIndex);
    }

    public FormSectionDto(String id, String title, String description, int orderIndex, List<FormQuestionDto> questions) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.orderIndex = orderIndex;
        this.questions = questions != null ? questions : new ArrayList<>();
    }

    public FormSectionDto(UUID id, String title, String description, int orderIndex, List<FormQuestionDto> questions) {
        this(id != null ? id.toString() : null, title, description, orderIndex, questions);
    }

    public String getId() {
        return id;
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

    public void setId(String id) {
        this.id = id;
    }

    public void setId(UUID id) {
        this.id = id != null ? id.toString() : null;
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

    public List<FormQuestionDto> getQuestions() {
        return questions;
    }

    public void setQuestions(List<FormQuestionDto> questions) {
        this.questions = questions;
    }

    public static FormSectionDtoBuilder builder() {
        return new FormSectionDtoBuilder();
    }

    public static class FormSectionDtoBuilder {
        private String id;
        private String title;
        private String description;
        private int orderIndex;
        private List<FormQuestionDto> questions = new ArrayList<>();

        public FormSectionDtoBuilder id(String id) {
            this.id = id;
            return this;
        }

        public FormSectionDtoBuilder id(UUID id) {
            this.id = id != null ? id.toString() : null;
            return this;
        }

        public FormSectionDtoBuilder title(String title) {
            this.title = title;
            return this;
        }

        public FormSectionDtoBuilder description(String description) {
            this.description = description;
            return this;
        }

        public FormSectionDtoBuilder orderIndex(int orderIndex) {
            this.orderIndex = orderIndex;
            return this;
        }

        public FormSectionDtoBuilder questions(List<FormQuestionDto> questions) {
            this.questions = questions;
            return this;
        }

        public FormSectionDto build() {
            FormSectionDto instance = new FormSectionDto();
            instance.id = this.id;
            instance.title = this.title;
            instance.description = this.description;
            instance.orderIndex = this.orderIndex;
            instance.questions = this.questions != null ? this.questions : new ArrayList<>();
            return instance;
        }
    }
}

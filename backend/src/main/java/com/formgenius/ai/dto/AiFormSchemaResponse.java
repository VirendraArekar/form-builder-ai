package com.formgenius.ai.dto;

import com.formgenius.form.dto.FormLogicRuleDto;
import com.formgenius.form.dto.FormQuestionDto;
import com.formgenius.form.dto.FormSectionDto;
import com.formgenius.form.dto.FormThemeDto;

import java.util.ArrayList;
import java.util.List;

public class AiFormSchemaResponse {
    private String title;
    private String description;
    private List<FormSectionDto> sections = new ArrayList<>();
    private List<FormQuestionDto> questions = new ArrayList<>();
    private List<FormLogicRuleDto> logicRules = new ArrayList<>();
    private FormThemeDto theme;
    private String customThankYouTitle;
    private String customThankYouMessage;
    private int estimatedCompletionTimeMinutes;

    public AiFormSchemaResponse() {}

    public AiFormSchemaResponse(String title, String description, List<FormSectionDto> sections, List<FormQuestionDto> questions, List<FormLogicRuleDto> logicRules, FormThemeDto theme, String customThankYouTitle, String customThankYouMessage, int estimatedCompletionTimeMinutes) {
        this.title = title;
        this.description = description;
        this.sections = sections;
        this.questions = questions;
        this.logicRules = logicRules;
        this.theme = theme;
        this.customThankYouTitle = customThankYouTitle;
        this.customThankYouMessage = customThankYouMessage;
        this.estimatedCompletionTimeMinutes = estimatedCompletionTimeMinutes;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<FormSectionDto> getSections() {
        return sections;
    }

    public List<FormQuestionDto> getQuestions() {
        return questions;
    }

    public List<FormLogicRuleDto> getLogicRules() {
        return logicRules;
    }

    public FormThemeDto getTheme() {
        return theme;
    }

    public String getCustomThankYouTitle() {
        return customThankYouTitle;
    }

    public String getCustomThankYouMessage() {
        return customThankYouMessage;
    }

    public int getEstimatedCompletionTimeMinutes() {
        return estimatedCompletionTimeMinutes;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSections(List<FormSectionDto> sections) {
        this.sections = sections;
    }

    public void setQuestions(List<FormQuestionDto> questions) {
        this.questions = questions;
    }

    public void setLogicRules(List<FormLogicRuleDto> logicRules) {
        this.logicRules = logicRules;
    }

    public void setTheme(FormThemeDto theme) {
        this.theme = theme;
    }

    public void setCustomThankYouTitle(String customThankYouTitle) {
        this.customThankYouTitle = customThankYouTitle;
    }

    public void setCustomThankYouMessage(String customThankYouMessage) {
        this.customThankYouMessage = customThankYouMessage;
    }

    public void setEstimatedCompletionTimeMinutes(int estimatedCompletionTimeMinutes) {
        this.estimatedCompletionTimeMinutes = estimatedCompletionTimeMinutes;
    }

    public static AiFormSchemaResponseBuilder builder() {
        return new AiFormSchemaResponseBuilder();
    }
    public static class AiFormSchemaResponseBuilder {
        private String title;
        private String description;
        private List<FormSectionDto> sections = new ArrayList<>();
        private List<FormQuestionDto> questions = new ArrayList<>();
        private List<FormLogicRuleDto> logicRules = new ArrayList<>();
        private FormThemeDto theme;
        private String customThankYouTitle;
        private String customThankYouMessage;
        private int estimatedCompletionTimeMinutes;

        public AiFormSchemaResponseBuilder title(String title) {
            this.title = title;
            return this;
        }
        public AiFormSchemaResponseBuilder description(String description) {
            this.description = description;
            return this;
        }
        public AiFormSchemaResponseBuilder sections(List<FormSectionDto> sections) {
            this.sections = sections;
            return this;
        }
        public AiFormSchemaResponseBuilder questions(List<FormQuestionDto> questions) {
            this.questions = questions;
            return this;
        }
        public AiFormSchemaResponseBuilder logicRules(List<FormLogicRuleDto> logicRules) {
            this.logicRules = logicRules;
            return this;
        }
        public AiFormSchemaResponseBuilder theme(FormThemeDto theme) {
            this.theme = theme;
            return this;
        }
        public AiFormSchemaResponseBuilder customThankYouTitle(String customThankYouTitle) {
            this.customThankYouTitle = customThankYouTitle;
            return this;
        }
        public AiFormSchemaResponseBuilder customThankYouMessage(String customThankYouMessage) {
            this.customThankYouMessage = customThankYouMessage;
            return this;
        }
        public AiFormSchemaResponseBuilder estimatedCompletionTimeMinutes(int estimatedCompletionTimeMinutes) {
            this.estimatedCompletionTimeMinutes = estimatedCompletionTimeMinutes;
            return this;
        }

        public AiFormSchemaResponse build() {
            AiFormSchemaResponse instance = new AiFormSchemaResponse();
            instance.title = this.title;
            instance.description = this.description;
            instance.sections = this.sections;
            instance.questions = this.questions;
            instance.logicRules = this.logicRules;
            instance.theme = this.theme;
            instance.customThankYouTitle = this.customThankYouTitle;
            instance.customThankYouMessage = this.customThankYouMessage;
            instance.estimatedCompletionTimeMinutes = this.estimatedCompletionTimeMinutes;
            return instance;
        }
    }
}

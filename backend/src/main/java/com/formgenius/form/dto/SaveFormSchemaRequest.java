package com.formgenius.form.dto;


import java.util.List;

public class SaveFormSchemaRequest {
    private String title;
    private String description;
    private List<FormSectionDto> sections;
    private List<FormQuestionDto> questions;
    private List<FormLogicRuleDto> logicRules;
    private FormThemeDto theme;

    public SaveFormSchemaRequest() {}

    public SaveFormSchemaRequest(String title, String description, List<FormSectionDto> sections, List<FormQuestionDto> questions, List<FormLogicRuleDto> logicRules, FormThemeDto theme) {
        this.title = title;
        this.description = description;
        this.sections = sections;
        this.questions = questions;
        this.logicRules = logicRules;
        this.theme = theme;
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
}

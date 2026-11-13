package com.formgenius.ai.dto;

import java.util.List;
import java.util.UUID;

public class AiFormAnalysisRequest {
    private UUID formId;
    private String title;
    private String description;
    private List<String> questions;

    public AiFormAnalysisRequest() {}

    public UUID getFormId() {
        return formId;
    }

    public void setFormId(UUID formId) {
        this.formId = formId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getQuestions() {
        return questions;
    }

    public void setQuestions(List<String> questions) {
        this.questions = questions;
    }
}

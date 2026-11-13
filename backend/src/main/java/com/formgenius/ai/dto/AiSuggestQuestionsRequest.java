package com.formgenius.ai.dto;

import java.util.List;
import java.util.UUID;

public class AiSuggestQuestionsRequest {
    private UUID formId;
    private String formTitle;
    private List<String> existingQuestions;
    private int count = 3;

    public AiSuggestQuestionsRequest() {}

    public UUID getFormId() {
        return formId;
    }

    public void setFormId(UUID formId) {
        this.formId = formId;
    }

    public String getFormTitle() {
        return formTitle;
    }

    public void setFormTitle(String formTitle) {
        this.formTitle = formTitle;
    }

    public List<String> getExistingQuestions() {
        return existingQuestions;
    }

    public void setExistingQuestions(List<String> existingQuestions) {
        this.existingQuestions = existingQuestions;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}

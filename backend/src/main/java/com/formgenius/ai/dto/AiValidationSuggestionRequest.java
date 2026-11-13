package com.formgenius.ai.dto;

public class AiValidationSuggestionRequest {
    private String questionTitle;
    private String questionType;
    private String context;

    public AiValidationSuggestionRequest() {}

    public String getQuestionTitle() {
        return questionTitle;
    }

    public void setQuestionTitle(String questionTitle) {
        this.questionTitle = questionTitle;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }
}

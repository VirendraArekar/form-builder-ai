package com.formgenius.ai.dto;

import jakarta.validation.constraints.NotBlank;

public class AiImproveQuestionRequest {
    @NotBlank(message = "Question title is required")
    private String questionTitle;
    private String questionType;
    private String goal; // "more_professional", "clearer", "concise"

    public AiImproveQuestionRequest() {}

    public AiImproveQuestionRequest(String questionTitle, String questionType, String goal) {
        this.questionTitle = questionTitle;
        this.questionType = questionType;
        this.goal = goal;
    }

    public String getQuestionTitle() {
        return questionTitle;
    }

    public String getQuestionType() {
        return questionType;
    }

    public String getGoal() {
        return goal;
    }

    public void setQuestionTitle(String questionTitle) {
        this.questionTitle = questionTitle;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }
}

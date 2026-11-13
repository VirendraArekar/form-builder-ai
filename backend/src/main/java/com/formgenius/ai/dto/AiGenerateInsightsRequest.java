package com.formgenius.ai.dto;


import java.util.UUID;

public class AiGenerateInsightsRequest {
    private UUID formId;
    private UUID workspaceId;
    private String question;

    public AiGenerateInsightsRequest() {}

    public AiGenerateInsightsRequest(UUID formId, UUID workspaceId, String question) {
        this.formId = formId;
        this.workspaceId = workspaceId;
        this.question = question;
    }

    public UUID getFormId() {
        return formId;
    }

    public UUID getWorkspaceId() {
        return workspaceId;
    }

    public String getQuestion() {
        return question;
    }

    public void setFormId(UUID formId) {
        this.formId = formId;
    }

    public void setWorkspaceId(UUID workspaceId) {
        this.workspaceId = workspaceId;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}

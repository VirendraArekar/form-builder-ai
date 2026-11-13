package com.formgenius.ai.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

public class AiGenerateFormRequest {
    private UUID workspaceId;

    @NotBlank(message = "Prompt is required")
    private String prompt;

    public AiGenerateFormRequest() {}

    public AiGenerateFormRequest(UUID workspaceId, String prompt) {
        this.workspaceId = workspaceId;
        this.prompt = prompt;
    }

    public UUID getWorkspaceId() {
        return workspaceId;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setWorkspaceId(UUID workspaceId) {
        this.workspaceId = workspaceId;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }
}

package com.formgenius.form.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class CreateFormRequest {
    @NotNull(message = "Workspace ID is required")
    private UUID workspaceId;

    @NotBlank(message = "Title is required")
    private String title;

    private String description;
    private UUID templateId;

    public CreateFormRequest() {}

    public CreateFormRequest(UUID workspaceId, String title, String description, UUID templateId) {
        this.workspaceId = workspaceId;
        this.title = title;
        this.description = description;
        this.templateId = templateId;
    }

    public UUID getWorkspaceId() {
        return workspaceId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public UUID getTemplateId() {
        return templateId;
    }

    public void setWorkspaceId(UUID workspaceId) {
        this.workspaceId = workspaceId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTemplateId(UUID templateId) {
        this.templateId = templateId;
    }
}

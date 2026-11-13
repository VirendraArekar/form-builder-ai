package com.formgenius.ai.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class AiTranslateFormRequest {
    @NotNull(message = "Form ID is required")
    private UUID formId;

    @NotBlank(message = "Target language is required")
    private String targetLanguage;

    public AiTranslateFormRequest() {}

    public AiTranslateFormRequest(UUID formId, String targetLanguage) {
        this.formId = formId;
        this.targetLanguage = targetLanguage;
    }

    public UUID getFormId() {
        return formId;
    }

    public String getTargetLanguage() {
        return targetLanguage;
    }

    public void setFormId(UUID formId) {
        this.formId = formId;
    }

    public void setTargetLanguage(String targetLanguage) {
        this.targetLanguage = targetLanguage;
    }
}

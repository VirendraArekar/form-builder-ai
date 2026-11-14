package com.formgenius.template;

import jakarta.validation.constraints.NotBlank;

public class CreateTemplateRequest {
    @NotBlank(message = "Title is required")
    private String title;
    private String description;
    private String category;
    private String icon;
    private String templateSchema;

    public CreateTemplateRequest() {}

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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTemplateSchema() {
        return templateSchema;
    }

    public void setTemplateSchema(String templateSchema) {
        this.templateSchema = templateSchema;
    }
}

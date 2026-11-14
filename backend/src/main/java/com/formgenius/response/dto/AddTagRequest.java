package com.formgenius.response.dto;

import jakarta.validation.constraints.NotBlank;

public class AddTagRequest {
    @NotBlank(message = "Tag name is required")
    private String tagName;
    private String color;

    public AddTagRequest() {}

    public AddTagRequest(String tagName, String color) {
        this.tagName = tagName;
        this.color = color;
    }

    public String getTagName() {
        return tagName;
    }

    public String getColor() {
        return color;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public void setColor(String color) {
        this.color = color;
    }
}

package com.formgenius.form.dto;


public class PublishFormRequest {
    private String customSlug;

    public PublishFormRequest() {}

    public PublishFormRequest(String customSlug) {
        this.customSlug = customSlug;
    }

    public String getCustomSlug() {
        return customSlug;
    }

    public void setCustomSlug(String customSlug) {
        this.customSlug = customSlug;
    }
}

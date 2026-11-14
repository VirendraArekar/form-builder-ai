package com.formgenius.inbox.dto;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public class InboxItemDto {
    private UUID responseId;
    private UUID formId;
    private String formTitle;
    private String formThemeColor;
    private String respondentName;
    private String respondentEmail;
    private String responsePreview;
    private Instant submittedAt;
    private int durationSeconds;
    private String deviceType;
    private boolean isReviewed;
    private Map<String, String> answers;

    public InboxItemDto() {}

    public UUID getResponseId() {
        return responseId;
    }

    public void setResponseId(UUID responseId) {
        this.responseId = responseId;
    }

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

    public String getFormThemeColor() {
        return formThemeColor;
    }

    public void setFormThemeColor(String formThemeColor) {
        this.formThemeColor = formThemeColor;
    }

    public String getRespondentName() {
        return respondentName;
    }

    public void setRespondentName(String respondentName) {
        this.respondentName = respondentName;
    }

    public String getRespondentEmail() {
        return respondentEmail;
    }

    public void setRespondentEmail(String respondentEmail) {
        this.respondentEmail = respondentEmail;
    }

    public String getResponsePreview() {
        return responsePreview;
    }

    public void setResponsePreview(String responsePreview) {
        this.responsePreview = responsePreview;
    }

    public Instant getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(Instant submittedAt) {
        this.submittedAt = submittedAt;
    }

    public int getDurationSeconds() {
        return durationSeconds;
    }

    public void setDurationSeconds(int durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public boolean isReviewed() {
        return isReviewed;
    }

    public void setReviewed(boolean reviewed) {
        isReviewed = reviewed;
    }

    public Map<String, String> getAnswers() {
        return answers;
    }

    public void setAnswers(Map<String, String> answers) {
        this.answers = answers;
    }
}

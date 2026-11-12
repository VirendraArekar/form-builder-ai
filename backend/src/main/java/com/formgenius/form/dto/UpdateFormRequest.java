package com.formgenius.form.dto;


import java.time.Instant;

public class UpdateFormRequest {
    private String title;
    private String description;
    private Integer submissionLimit;
    private Instant startsAt;
    private Instant expiresAt;
    private Boolean isPasswordProtected;
    private String accessPassword;
    private String redirectUrl;
    private String customThankYouTitle;
    private String customThankYouMessage;
    private Boolean allowMultipleSubmissions;
    private Boolean sendEmailNotification;
    private String notificationEmail;

    public UpdateFormRequest() {}

    public UpdateFormRequest(String title, String description, Integer submissionLimit, Instant startsAt, Instant expiresAt, Boolean isPasswordProtected, String accessPassword, String redirectUrl, String customThankYouTitle, String customThankYouMessage, Boolean allowMultipleSubmissions, Boolean sendEmailNotification, String notificationEmail) {
        this.title = title;
        this.description = description;
        this.submissionLimit = submissionLimit;
        this.startsAt = startsAt;
        this.expiresAt = expiresAt;
        this.isPasswordProtected = isPasswordProtected;
        this.accessPassword = accessPassword;
        this.redirectUrl = redirectUrl;
        this.customThankYouTitle = customThankYouTitle;
        this.customThankYouMessage = customThankYouMessage;
        this.allowMultipleSubmissions = allowMultipleSubmissions;
        this.sendEmailNotification = sendEmailNotification;
        this.notificationEmail = notificationEmail;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Integer getSubmissionLimit() {
        return submissionLimit;
    }

    public Instant getStartsAt() {
        return startsAt;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public Boolean isPasswordProtected() {
        return isPasswordProtected;
    }

    public Boolean getIsPasswordProtected() {
        return isPasswordProtected;
    }

    public String getAccessPassword() {
        return accessPassword;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public String getCustomThankYouTitle() {
        return customThankYouTitle;
    }

    public String getCustomThankYouMessage() {
        return customThankYouMessage;
    }

    public Boolean isAllowMultipleSubmissions() {
        return allowMultipleSubmissions;
    }

    public Boolean getAllowMultipleSubmissions() {
        return allowMultipleSubmissions;
    }

    public Boolean isSendEmailNotification() {
        return sendEmailNotification;
    }

    public Boolean getSendEmailNotification() {
        return sendEmailNotification;
    }

    public String getNotificationEmail() {
        return notificationEmail;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSubmissionLimit(Integer submissionLimit) {
        this.submissionLimit = submissionLimit;
    }

    public void setStartsAt(Instant startsAt) {
        this.startsAt = startsAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    public void setPasswordProtected(Boolean isPasswordProtected) {
        this.isPasswordProtected = isPasswordProtected;
    }

    public void setAccessPassword(String accessPassword) {
        this.accessPassword = accessPassword;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public void setCustomThankYouTitle(String customThankYouTitle) {
        this.customThankYouTitle = customThankYouTitle;
    }

    public void setCustomThankYouMessage(String customThankYouMessage) {
        this.customThankYouMessage = customThankYouMessage;
    }

    public void setAllowMultipleSubmissions(Boolean allowMultipleSubmissions) {
        this.allowMultipleSubmissions = allowMultipleSubmissions;
    }

    public void setSendEmailNotification(Boolean sendEmailNotification) {
        this.sendEmailNotification = sendEmailNotification;
    }

    public void setNotificationEmail(String notificationEmail) {
        this.notificationEmail = notificationEmail;
    }
}

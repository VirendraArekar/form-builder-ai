package com.formgenius.user.dto;

import jakarta.validation.constraints.NotBlank;

public class UpdateProfileRequest {
    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    private String avatarUrl;
    private String jobTitle;
    private String companyName;
    private String phoneNumber;
    private String timezone;
    private String preferredTheme;

    public UpdateProfileRequest() {}

    public UpdateProfileRequest(String firstName, String lastName, String avatarUrl, String jobTitle, String companyName, String phoneNumber, String timezone, String preferredTheme) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.avatarUrl = avatarUrl;
        this.jobTitle = jobTitle;
        this.companyName = companyName;
        this.phoneNumber = phoneNumber;
        this.timezone = timezone;
        this.preferredTheme = preferredTheme;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getTimezone() {
        return timezone;
    }

    public String getPreferredTheme() {
        return preferredTheme;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public void setPreferredTheme(String preferredTheme) {
        this.preferredTheme = preferredTheme;
    }
}

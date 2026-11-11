package com.formgenius.user.dto;


import java.util.UUID;

public class UserProfileResponse {
    private UUID id;
    private String email;
    private String firstName;
    private String lastName;
    private String systemRole;
    private String avatarUrl;
    private String jobTitle;
    private String companyName;
    private String phoneNumber;
    private String timezone;
    private String preferredTheme;

    public UserProfileResponse() {}

    public UserProfileResponse(UUID id, String email, String firstName, String lastName, String systemRole, String avatarUrl, String jobTitle, String companyName, String phoneNumber, String timezone, String preferredTheme) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.systemRole = systemRole;
        this.avatarUrl = avatarUrl;
        this.jobTitle = jobTitle;
        this.companyName = companyName;
        this.phoneNumber = phoneNumber;
        this.timezone = timezone;
        this.preferredTheme = preferredTheme;
    }

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getSystemRole() {
        return systemRole;
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

    public void setId(UUID id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setSystemRole(String systemRole) {
        this.systemRole = systemRole;
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

    public static UserProfileResponseBuilder builder() {
        return new UserProfileResponseBuilder();
    }
    public static class UserProfileResponseBuilder {
        private UUID id;
        private String email;
        private String firstName;
        private String lastName;
        private String systemRole;
        private String avatarUrl;
        private String jobTitle;
        private String companyName;
        private String phoneNumber;
        private String timezone;
        private String preferredTheme;

        public UserProfileResponseBuilder id(UUID id) {
            this.id = id;
            return this;
        }
        public UserProfileResponseBuilder email(String email) {
            this.email = email;
            return this;
        }
        public UserProfileResponseBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }
        public UserProfileResponseBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }
        public UserProfileResponseBuilder systemRole(String systemRole) {
            this.systemRole = systemRole;
            return this;
        }
        public UserProfileResponseBuilder avatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
            return this;
        }
        public UserProfileResponseBuilder jobTitle(String jobTitle) {
            this.jobTitle = jobTitle;
            return this;
        }
        public UserProfileResponseBuilder companyName(String companyName) {
            this.companyName = companyName;
            return this;
        }
        public UserProfileResponseBuilder phoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }
        public UserProfileResponseBuilder timezone(String timezone) {
            this.timezone = timezone;
            return this;
        }
        public UserProfileResponseBuilder preferredTheme(String preferredTheme) {
            this.preferredTheme = preferredTheme;
            return this;
        }

        public UserProfileResponse build() {
            UserProfileResponse instance = new UserProfileResponse();
            instance.id = this.id;
            instance.email = this.email;
            instance.firstName = this.firstName;
            instance.lastName = this.lastName;
            instance.systemRole = this.systemRole;
            instance.avatarUrl = this.avatarUrl;
            instance.jobTitle = this.jobTitle;
            instance.companyName = this.companyName;
            instance.phoneNumber = this.phoneNumber;
            instance.timezone = this.timezone;
            instance.preferredTheme = this.preferredTheme;
            return instance;
        }
    }
}

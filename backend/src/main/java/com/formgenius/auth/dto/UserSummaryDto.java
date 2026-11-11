package com.formgenius.auth.dto;


import java.util.UUID;

public class UserSummaryDto {
    private UUID id;
    private String email;
    private String firstName;
    private String lastName;
    private String systemRole;
    private String avatarUrl;

    public UserSummaryDto() {}

    public UserSummaryDto(UUID id, String email, String firstName, String lastName, String systemRole, String avatarUrl) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.systemRole = systemRole;
        this.avatarUrl = avatarUrl;
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

    public String getName() {
        return ((firstName != null ? firstName : "") + (lastName != null && !lastName.isBlank() ? " " + lastName : "")).trim();
    }

    public String getAvatar() {
        return avatarUrl;
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

    public static UserSummaryDtoBuilder builder() {
        return new UserSummaryDtoBuilder();
    }
    public static class UserSummaryDtoBuilder {
        private UUID id;
        private String email;
        private String firstName;
        private String lastName;
        private String systemRole;
        private String avatarUrl;

        public UserSummaryDtoBuilder id(UUID id) {
            this.id = id;
            return this;
        }
        public UserSummaryDtoBuilder email(String email) {
            this.email = email;
            return this;
        }
        public UserSummaryDtoBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }
        public UserSummaryDtoBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }
        public UserSummaryDtoBuilder systemRole(String systemRole) {
            this.systemRole = systemRole;
            return this;
        }
        public UserSummaryDtoBuilder avatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
            return this;
        }

        public UserSummaryDto build() {
            UserSummaryDto instance = new UserSummaryDto();
            instance.id = this.id;
            instance.email = this.email;
            instance.firstName = this.firstName;
            instance.lastName = this.lastName;
            instance.systemRole = this.systemRole;
            instance.avatarUrl = this.avatarUrl;
            return instance;
        }
    }
}

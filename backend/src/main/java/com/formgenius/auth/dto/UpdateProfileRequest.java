package com.formgenius.auth.dto;

public class UpdateProfileRequest {
    private String name;
    private String firstName;
    private String lastName;
    private String email;
    private String avatar;
    private String avatarUrl;

    public UpdateProfileRequest() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar != null ? avatar : avatarUrl;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
        this.avatarUrl = avatar;
    }

    public String getAvatarUrl() {
        return avatarUrl != null ? avatarUrl : avatar;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
        this.avatar = avatarUrl;
    }
}

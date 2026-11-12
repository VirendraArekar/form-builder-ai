package com.formgenius.workspace.dto;


import java.time.Instant;
import java.util.UUID;

public class WorkspaceMemberResponse {
    private UUID id;
    private UUID userId;
    private String email;
    private String firstName;
    private String lastName;
    private String avatarUrl;
    private String role;
    private Instant joinedAt;

    public WorkspaceMemberResponse() {}

    public WorkspaceMemberResponse(UUID id, UUID userId, String email, String firstName, String lastName, String avatarUrl, String role, Instant joinedAt) {
        this.id = id;
        this.userId = userId;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.avatarUrl = avatarUrl;
        this.role = role;
        this.joinedAt = joinedAt;
    }

    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
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

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getRole() {
        return role;
    }

    public Instant getJoinedAt() {
        return joinedAt;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
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

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setJoinedAt(Instant joinedAt) {
        this.joinedAt = joinedAt;
    }

    public static WorkspaceMemberResponseBuilder builder() {
        return new WorkspaceMemberResponseBuilder();
    }
    public static class WorkspaceMemberResponseBuilder {
        private UUID id;
        private UUID userId;
        private String email;
        private String firstName;
        private String lastName;
        private String avatarUrl;
        private String role;
        private Instant joinedAt;

        public WorkspaceMemberResponseBuilder id(UUID id) {
            this.id = id;
            return this;
        }
        public WorkspaceMemberResponseBuilder userId(UUID userId) {
            this.userId = userId;
            return this;
        }
        public WorkspaceMemberResponseBuilder email(String email) {
            this.email = email;
            return this;
        }
        public WorkspaceMemberResponseBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }
        public WorkspaceMemberResponseBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }
        public WorkspaceMemberResponseBuilder avatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
            return this;
        }
        public WorkspaceMemberResponseBuilder role(String role) {
            this.role = role;
            return this;
        }
        public WorkspaceMemberResponseBuilder joinedAt(Instant joinedAt) {
            this.joinedAt = joinedAt;
            return this;
        }

        public WorkspaceMemberResponse build() {
            WorkspaceMemberResponse instance = new WorkspaceMemberResponse();
            instance.id = this.id;
            instance.userId = this.userId;
            instance.email = this.email;
            instance.firstName = this.firstName;
            instance.lastName = this.lastName;
            instance.avatarUrl = this.avatarUrl;
            instance.role = this.role;
            instance.joinedAt = this.joinedAt;
            return instance;
        }
    }
}

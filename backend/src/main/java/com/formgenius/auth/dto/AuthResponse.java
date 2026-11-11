package com.formgenius.auth.dto;


public class AuthResponse {
    private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";
    private UserSummaryDto user;
    private WorkspaceSummaryDto activeWorkspace;

    public AuthResponse() {}

    public AuthResponse(String accessToken, String refreshToken, String tokenType, UserSummaryDto user, WorkspaceSummaryDto activeWorkspace) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.tokenType = tokenType;
        this.user = user;
        this.activeWorkspace = activeWorkspace;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public UserSummaryDto getUser() {
        return user;
    }

    public WorkspaceSummaryDto getActiveWorkspace() {
        return activeWorkspace;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public void setUser(UserSummaryDto user) {
        this.user = user;
    }

    public void setActiveWorkspace(WorkspaceSummaryDto activeWorkspace) {
        this.activeWorkspace = activeWorkspace;
    }

    public static AuthResponseBuilder builder() {
        return new AuthResponseBuilder();
    }
    public static class AuthResponseBuilder {
        private String accessToken;
        private String refreshToken;
        private String tokenType = "Bearer";
        private UserSummaryDto user;
        private WorkspaceSummaryDto activeWorkspace;

        public AuthResponseBuilder accessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }
        public AuthResponseBuilder refreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
            return this;
        }
        public AuthResponseBuilder tokenType(String tokenType) {
            this.tokenType = tokenType;
            return this;
        }
        public AuthResponseBuilder user(UserSummaryDto user) {
            this.user = user;
            return this;
        }
        public AuthResponseBuilder activeWorkspace(WorkspaceSummaryDto activeWorkspace) {
            this.activeWorkspace = activeWorkspace;
            return this;
        }

        public AuthResponse build() {
            AuthResponse instance = new AuthResponse();
            instance.accessToken = this.accessToken;
            instance.refreshToken = this.refreshToken;
            instance.tokenType = this.tokenType;
            instance.user = this.user;
            instance.activeWorkspace = this.activeWorkspace;
            return instance;
        }
    }
}

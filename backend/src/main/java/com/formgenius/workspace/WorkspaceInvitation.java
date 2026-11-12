package com.formgenius.workspace;

import com.formgenius.user.User;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "workspace_invitations")
public class WorkspaceInvitation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id", nullable = false)
    private Workspace workspace;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String role = "EDITOR";

    @Column(nullable = false, unique = true)
    private String token;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invited_by", nullable = false)
    private User invitedBy;

    @Column(nullable = false)
    private String status = "PENDING"; // PENDING, ACCEPTED, DECLINED, EXPIRED

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    public WorkspaceInvitation() {}

    public WorkspaceInvitation(UUID id, Workspace workspace, String email, String role, String token, User invitedBy, String status, Instant expiresAt, Instant createdAt) {
        this.id = id;
        this.workspace = workspace;
        this.email = email;
        this.role = role;
        this.token = token;
        this.invitedBy = invitedBy;
        this.status = status;
        this.expiresAt = expiresAt;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public Workspace getWorkspace() {
        return workspace;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public String getToken() {
        return token;
    }

    public User getInvitedBy() {
        return invitedBy;
    }

    public String getStatus() {
        return status;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setWorkspace(Workspace workspace) {
        this.workspace = workspace;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setInvitedBy(User invitedBy) {
        this.invitedBy = invitedBy;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public static WorkspaceInvitationBuilder builder() {
        return new WorkspaceInvitationBuilder();
    }
    public static class WorkspaceInvitationBuilder {
        private UUID id;
        private Workspace workspace;
        private String email;
        private String role = "EDITOR";
        private String token;
        private User invitedBy;
        private String status = "PENDING";
        private Instant expiresAt;
        private Instant createdAt;

        public WorkspaceInvitationBuilder id(UUID id) {
            this.id = id;
            return this;
        }
        public WorkspaceInvitationBuilder workspace(Workspace workspace) {
            this.workspace = workspace;
            return this;
        }
        public WorkspaceInvitationBuilder email(String email) {
            this.email = email;
            return this;
        }
        public WorkspaceInvitationBuilder role(String role) {
            this.role = role;
            return this;
        }
        public WorkspaceInvitationBuilder token(String token) {
            this.token = token;
            return this;
        }
        public WorkspaceInvitationBuilder invitedBy(User invitedBy) {
            this.invitedBy = invitedBy;
            return this;
        }
        public WorkspaceInvitationBuilder status(String status) {
            this.status = status;
            return this;
        }
        public WorkspaceInvitationBuilder expiresAt(Instant expiresAt) {
            this.expiresAt = expiresAt;
            return this;
        }
        public WorkspaceInvitationBuilder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public WorkspaceInvitation build() {
            WorkspaceInvitation instance = new WorkspaceInvitation();
            instance.id = this.id;
            instance.workspace = this.workspace;
            instance.email = this.email;
            instance.role = this.role;
            instance.token = this.token;
            instance.invitedBy = this.invitedBy;
            instance.status = this.status;
            instance.expiresAt = this.expiresAt;
            instance.createdAt = this.createdAt;
            return instance;
        }
    }
}

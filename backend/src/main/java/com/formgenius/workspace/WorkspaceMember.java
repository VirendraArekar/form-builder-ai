package com.formgenius.workspace;

import com.formgenius.user.User;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "workspace_members", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"workspace_id", "user_id"})
})
public class WorkspaceMember {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id", nullable = false)
    private Workspace workspace;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String role = "EDITOR"; // SUPER_ADMIN, WORKSPACE_OWNER, ADMIN, EDITOR, VIEWER

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public WorkspaceMember() {}

    public WorkspaceMember(UUID id, Workspace workspace, User user, String role, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.workspace = workspace;
        this.user = user;
        this.role = role;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public Workspace getWorkspace() {
        return workspace;
    }

    public User getUser() {
        return user;
    }

    public String getRole() {
        return role;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setWorkspace(Workspace workspace) {
        this.workspace = workspace;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public static WorkspaceMemberBuilder builder() {
        return new WorkspaceMemberBuilder();
    }
    public static class WorkspaceMemberBuilder {
        private UUID id;
        private Workspace workspace;
        private User user;
        private String role = "EDITOR";
        private Instant createdAt;
        private Instant updatedAt;

        public WorkspaceMemberBuilder id(UUID id) {
            this.id = id;
            return this;
        }
        public WorkspaceMemberBuilder workspace(Workspace workspace) {
            this.workspace = workspace;
            return this;
        }
        public WorkspaceMemberBuilder user(User user) {
            this.user = user;
            return this;
        }
        public WorkspaceMemberBuilder role(String role) {
            this.role = role;
            return this;
        }
        public WorkspaceMemberBuilder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }
        public WorkspaceMemberBuilder updatedAt(Instant updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public WorkspaceMember build() {
            WorkspaceMember instance = new WorkspaceMember();
            instance.id = this.id;
            instance.workspace = this.workspace;
            instance.user = this.user;
            instance.role = this.role;
            instance.createdAt = this.createdAt;
            instance.updatedAt = this.updatedAt;
            return instance;
        }
    }
}

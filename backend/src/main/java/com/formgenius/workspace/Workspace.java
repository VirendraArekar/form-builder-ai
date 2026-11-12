package com.formgenius.workspace;

import com.formgenius.user.User;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "workspaces")
public class Workspace {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String slug;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "logo_url")
    private String logoUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Column(name = "plan_tier", nullable = false)
    private String planTier = "FREE"; // FREE, PRO, BUSINESS

    @Column(name = "max_forms", nullable = false)
    private int maxForms = 5;

    @Column(name = "max_responses_per_month", nullable = false)
    private int maxResponsesPerMonth = 100;

    @Column(name = "ai_generations_quota", nullable = false)
    private int aiGenerationsQuota = 20;

    @Column(name = "ai_generations_used", nullable = false)
    private int aiGenerationsUsed = 0;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public Workspace() {}

    public Workspace(UUID id, String name, String slug, String description, String logoUrl, User owner, String planTier, int maxForms, int maxResponsesPerMonth, int aiGenerationsQuota, int aiGenerationsUsed, boolean isDeleted, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.description = description;
        this.logoUrl = logoUrl;
        this.owner = owner;
        this.planTier = planTier;
        this.maxForms = maxForms;
        this.maxResponsesPerMonth = maxResponsesPerMonth;
        this.aiGenerationsQuota = aiGenerationsQuota;
        this.aiGenerationsUsed = aiGenerationsUsed;
        this.isDeleted = isDeleted;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSlug() {
        return slug;
    }

    public String getDescription() {
        return description;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public User getOwner() {
        return owner;
    }

    public String getPlanTier() {
        return planTier;
    }

    public int getMaxForms() {
        return maxForms;
    }

    public int getMaxResponsesPerMonth() {
        return maxResponsesPerMonth;
    }

    public int getAiGenerationsQuota() {
        return aiGenerationsQuota;
    }

    public int getAiGenerationsUsed() {
        return aiGenerationsUsed;
    }

    public boolean isDeleted() {
        return isDeleted;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public void setPlanTier(String planTier) {
        this.planTier = planTier;
    }

    public void setMaxForms(int maxForms) {
        this.maxForms = maxForms;
    }

    public void setMaxResponsesPerMonth(int maxResponsesPerMonth) {
        this.maxResponsesPerMonth = maxResponsesPerMonth;
    }

    public void setAiGenerationsQuota(int aiGenerationsQuota) {
        this.aiGenerationsQuota = aiGenerationsQuota;
    }

    public void setAiGenerationsUsed(int aiGenerationsUsed) {
        this.aiGenerationsUsed = aiGenerationsUsed;
    }

    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public static WorkspaceBuilder builder() {
        return new WorkspaceBuilder();
    }
    public static class WorkspaceBuilder {
        private UUID id;
        private String name;
        private String slug;
        private String description;
        private String logoUrl;
        private User owner;
        private String planTier = "FREE";
        private int maxForms = 5;
        private int maxResponsesPerMonth = 100;
        private int aiGenerationsQuota = 20;
        private int aiGenerationsUsed = 0;
        private boolean isDeleted = false;
        private Instant createdAt;
        private Instant updatedAt;

        public WorkspaceBuilder id(UUID id) {
            this.id = id;
            return this;
        }
        public WorkspaceBuilder name(String name) {
            this.name = name;
            return this;
        }
        public WorkspaceBuilder slug(String slug) {
            this.slug = slug;
            return this;
        }
        public WorkspaceBuilder description(String description) {
            this.description = description;
            return this;
        }
        public WorkspaceBuilder logoUrl(String logoUrl) {
            this.logoUrl = logoUrl;
            return this;
        }
        public WorkspaceBuilder owner(User owner) {
            this.owner = owner;
            return this;
        }
        public WorkspaceBuilder planTier(String planTier) {
            this.planTier = planTier;
            return this;
        }
        public WorkspaceBuilder maxForms(int maxForms) {
            this.maxForms = maxForms;
            return this;
        }
        public WorkspaceBuilder maxResponsesPerMonth(int maxResponsesPerMonth) {
            this.maxResponsesPerMonth = maxResponsesPerMonth;
            return this;
        }
        public WorkspaceBuilder aiGenerationsQuota(int aiGenerationsQuota) {
            this.aiGenerationsQuota = aiGenerationsQuota;
            return this;
        }
        public WorkspaceBuilder aiGenerationsUsed(int aiGenerationsUsed) {
            this.aiGenerationsUsed = aiGenerationsUsed;
            return this;
        }
        public WorkspaceBuilder isDeleted(boolean isDeleted) {
            this.isDeleted = isDeleted;
            return this;
        }
        public WorkspaceBuilder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }
        public WorkspaceBuilder updatedAt(Instant updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Workspace build() {
            Workspace instance = new Workspace();
            instance.id = this.id;
            instance.name = this.name;
            instance.slug = this.slug;
            instance.description = this.description;
            instance.logoUrl = this.logoUrl;
            instance.owner = this.owner;
            instance.planTier = this.planTier;
            instance.maxForms = this.maxForms;
            instance.maxResponsesPerMonth = this.maxResponsesPerMonth;
            instance.aiGenerationsQuota = this.aiGenerationsQuota;
            instance.aiGenerationsUsed = this.aiGenerationsUsed;
            instance.isDeleted = this.isDeleted;
            instance.createdAt = this.createdAt;
            instance.updatedAt = this.updatedAt;
            return instance;
        }
    }
}

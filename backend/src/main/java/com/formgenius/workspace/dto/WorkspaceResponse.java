package com.formgenius.workspace.dto;


import java.time.Instant;
import java.util.UUID;

public class WorkspaceResponse {
    private UUID id;
    private String name;
    private String slug;
    private String description;
    private String logoUrl;
    private UUID ownerId;
    private String ownerName;
    private String planTier;
    private int maxForms;
    private int maxResponsesPerMonth;
    private int aiGenerationsQuota;
    private int aiGenerationsUsed;
    private int memberCount;
    private int formCount;
    private String userRole;
    private Instant createdAt;

    public WorkspaceResponse() {}

    public WorkspaceResponse(UUID id, String name, String slug, String description, String logoUrl, UUID ownerId, String ownerName, String planTier, int maxForms, int maxResponsesPerMonth, int aiGenerationsQuota, int aiGenerationsUsed, int memberCount, int formCount, String userRole, Instant createdAt) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.description = description;
        this.logoUrl = logoUrl;
        this.ownerId = ownerId;
        this.ownerName = ownerName;
        this.planTier = planTier;
        this.maxForms = maxForms;
        this.maxResponsesPerMonth = maxResponsesPerMonth;
        this.aiGenerationsQuota = aiGenerationsQuota;
        this.aiGenerationsUsed = aiGenerationsUsed;
        this.memberCount = memberCount;
        this.formCount = formCount;
        this.userRole = userRole;
        this.createdAt = createdAt;
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

    public UUID getOwnerId() {
        return ownerId;
    }

    public String getOwnerName() {
        return ownerName;
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

    public int getMemberCount() {
        return memberCount;
    }

    public int getFormCount() {
        return formCount;
    }

    public String getUserRole() {
        return userRole;
    }

    public Instant getCreatedAt() {
        return createdAt;
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

    public void setOwnerId(UUID ownerId) {
        this.ownerId = ownerId;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
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

    public void setMemberCount(int memberCount) {
        this.memberCount = memberCount;
    }

    public void setFormCount(int formCount) {
        this.formCount = formCount;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public static WorkspaceResponseBuilder builder() {
        return new WorkspaceResponseBuilder();
    }
    public static class WorkspaceResponseBuilder {
        private UUID id;
        private String name;
        private String slug;
        private String description;
        private String logoUrl;
        private UUID ownerId;
        private String ownerName;
        private String planTier;
        private int maxForms;
        private int maxResponsesPerMonth;
        private int aiGenerationsQuota;
        private int aiGenerationsUsed;
        private int memberCount;
        private int formCount;
        private String userRole;
        private Instant createdAt;

        public WorkspaceResponseBuilder id(UUID id) {
            this.id = id;
            return this;
        }
        public WorkspaceResponseBuilder name(String name) {
            this.name = name;
            return this;
        }
        public WorkspaceResponseBuilder slug(String slug) {
            this.slug = slug;
            return this;
        }
        public WorkspaceResponseBuilder description(String description) {
            this.description = description;
            return this;
        }
        public WorkspaceResponseBuilder logoUrl(String logoUrl) {
            this.logoUrl = logoUrl;
            return this;
        }
        public WorkspaceResponseBuilder ownerId(UUID ownerId) {
            this.ownerId = ownerId;
            return this;
        }
        public WorkspaceResponseBuilder ownerName(String ownerName) {
            this.ownerName = ownerName;
            return this;
        }
        public WorkspaceResponseBuilder planTier(String planTier) {
            this.planTier = planTier;
            return this;
        }
        public WorkspaceResponseBuilder maxForms(int maxForms) {
            this.maxForms = maxForms;
            return this;
        }
        public WorkspaceResponseBuilder maxResponsesPerMonth(int maxResponsesPerMonth) {
            this.maxResponsesPerMonth = maxResponsesPerMonth;
            return this;
        }
        public WorkspaceResponseBuilder aiGenerationsQuota(int aiGenerationsQuota) {
            this.aiGenerationsQuota = aiGenerationsQuota;
            return this;
        }
        public WorkspaceResponseBuilder aiGenerationsUsed(int aiGenerationsUsed) {
            this.aiGenerationsUsed = aiGenerationsUsed;
            return this;
        }
        public WorkspaceResponseBuilder memberCount(int memberCount) {
            this.memberCount = memberCount;
            return this;
        }
        public WorkspaceResponseBuilder formCount(int formCount) {
            this.formCount = formCount;
            return this;
        }
        public WorkspaceResponseBuilder userRole(String userRole) {
            this.userRole = userRole;
            return this;
        }
        public WorkspaceResponseBuilder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public WorkspaceResponse build() {
            WorkspaceResponse instance = new WorkspaceResponse();
            instance.id = this.id;
            instance.name = this.name;
            instance.slug = this.slug;
            instance.description = this.description;
            instance.logoUrl = this.logoUrl;
            instance.ownerId = this.ownerId;
            instance.ownerName = this.ownerName;
            instance.planTier = this.planTier;
            instance.maxForms = this.maxForms;
            instance.maxResponsesPerMonth = this.maxResponsesPerMonth;
            instance.aiGenerationsQuota = this.aiGenerationsQuota;
            instance.aiGenerationsUsed = this.aiGenerationsUsed;
            instance.memberCount = this.memberCount;
            instance.formCount = this.formCount;
            instance.userRole = this.userRole;
            instance.createdAt = this.createdAt;
            return instance;
        }
    }
}

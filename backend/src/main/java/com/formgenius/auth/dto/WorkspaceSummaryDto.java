package com.formgenius.auth.dto;


import java.util.UUID;

public class WorkspaceSummaryDto {
    private UUID id;
    private String name;
    private String slug;
    private String role;
    private String planTier;

    public WorkspaceSummaryDto() {}

    public WorkspaceSummaryDto(UUID id, String name, String slug, String role, String planTier) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.role = role;
        this.planTier = planTier;
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

    public String getRole() {
        return role;
    }

    public String getPlanTier() {
        return planTier;
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

    public void setRole(String role) {
        this.role = role;
    }

    public void setPlanTier(String planTier) {
        this.planTier = planTier;
    }

    public static WorkspaceSummaryDtoBuilder builder() {
        return new WorkspaceSummaryDtoBuilder();
    }
    public static class WorkspaceSummaryDtoBuilder {
        private UUID id;
        private String name;
        private String slug;
        private String role;
        private String planTier;

        public WorkspaceSummaryDtoBuilder id(UUID id) {
            this.id = id;
            return this;
        }
        public WorkspaceSummaryDtoBuilder name(String name) {
            this.name = name;
            return this;
        }
        public WorkspaceSummaryDtoBuilder slug(String slug) {
            this.slug = slug;
            return this;
        }
        public WorkspaceSummaryDtoBuilder role(String role) {
            this.role = role;
            return this;
        }
        public WorkspaceSummaryDtoBuilder planTier(String planTier) {
            this.planTier = planTier;
            return this;
        }

        public WorkspaceSummaryDto build() {
            WorkspaceSummaryDto instance = new WorkspaceSummaryDto();
            instance.id = this.id;
            instance.name = this.name;
            instance.slug = this.slug;
            instance.role = this.role;
            instance.planTier = this.planTier;
            return instance;
        }
    }
}

package com.formgenius.template;


import java.util.UUID;

public class TemplateDto {
    private UUID id;
    private String title;
    private String description;
    private String category;
    private String icon;
    private String templateSchema;
    private boolean isFeatured;
    private int usageCount;

    public TemplateDto() {}

    public TemplateDto(UUID id, String title, String description, String category, String icon, String templateSchema, boolean isFeatured, int usageCount) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.icon = icon;
        this.templateSchema = templateSchema;
        this.isFeatured = isFeatured;
        this.usageCount = usageCount;
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public String getIcon() {
        return icon;
    }

    public String getTemplateSchema() {
        return templateSchema;
    }

    public boolean isFeatured() {
        return isFeatured;
    }

    public int getUsageCount() {
        return usageCount;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setTemplateSchema(String templateSchema) {
        this.templateSchema = templateSchema;
    }

    public void setFeatured(boolean isFeatured) {
        this.isFeatured = isFeatured;
    }

    public void setUsageCount(int usageCount) {
        this.usageCount = usageCount;
    }

    public static TemplateDtoBuilder builder() {
        return new TemplateDtoBuilder();
    }
    public static class TemplateDtoBuilder {
        private UUID id;
        private String title;
        private String description;
        private String category;
        private String icon;
        private String templateSchema;
        private boolean isFeatured;
        private int usageCount;

        public TemplateDtoBuilder id(UUID id) {
            this.id = id;
            return this;
        }
        public TemplateDtoBuilder title(String title) {
            this.title = title;
            return this;
        }
        public TemplateDtoBuilder description(String description) {
            this.description = description;
            return this;
        }
        public TemplateDtoBuilder category(String category) {
            this.category = category;
            return this;
        }
        public TemplateDtoBuilder icon(String icon) {
            this.icon = icon;
            return this;
        }
        public TemplateDtoBuilder templateSchema(String templateSchema) {
            this.templateSchema = templateSchema;
            return this;
        }
        public TemplateDtoBuilder isFeatured(boolean isFeatured) {
            this.isFeatured = isFeatured;
            return this;
        }
        public TemplateDtoBuilder usageCount(int usageCount) {
            this.usageCount = usageCount;
            return this;
        }

        public TemplateDto build() {
            TemplateDto instance = new TemplateDto();
            instance.id = this.id;
            instance.title = this.title;
            instance.description = this.description;
            instance.category = this.category;
            instance.icon = this.icon;
            instance.templateSchema = this.templateSchema;
            instance.isFeatured = this.isFeatured;
            instance.usageCount = this.usageCount;
            return instance;
        }
    }
}

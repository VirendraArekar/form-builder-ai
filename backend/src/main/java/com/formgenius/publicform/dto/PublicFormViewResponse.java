package com.formgenius.publicform.dto;

import com.formgenius.form.dto.FormLogicRuleDto;
import com.formgenius.form.dto.FormQuestionDto;
import com.formgenius.form.dto.FormSectionDto;
import com.formgenius.form.dto.FormThemeDto;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PublicFormViewResponse {
    private UUID id;
    private String title;
    private String description;
    private String slug;
    private boolean isPasswordProtected;
    private boolean isExpired;
    private boolean isLimitReached;
    private boolean startsInFuture;
    private Instant startsAt;
    private Instant expiresAt;

    private FormThemeDto theme;
    private List<FormSectionDto> sections = new ArrayList<>();
    private List<FormQuestionDto> questions = new ArrayList<>();
    private List<FormLogicRuleDto> logicRules = new ArrayList<>();

    public PublicFormViewResponse() {}

    public PublicFormViewResponse(UUID id, String title, String description, String slug, boolean isPasswordProtected, boolean isExpired, boolean isLimitReached, boolean startsInFuture, Instant startsAt, Instant expiresAt, FormThemeDto theme, List<FormSectionDto> sections, List<FormQuestionDto> questions, List<FormLogicRuleDto> logicRules) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.slug = slug;
        this.isPasswordProtected = isPasswordProtected;
        this.isExpired = isExpired;
        this.isLimitReached = isLimitReached;
        this.startsInFuture = startsInFuture;
        this.startsAt = startsAt;
        this.expiresAt = expiresAt;
        this.theme = theme;
        this.sections = sections;
        this.questions = questions;
        this.logicRules = logicRules;
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

    public String getSlug() {
        return slug;
    }

    public boolean isPasswordProtected() {
        return isPasswordProtected;
    }

    public boolean isExpired() {
        return isExpired;
    }

    public boolean isLimitReached() {
        return isLimitReached;
    }

    public boolean isStartsInFuture() {
        return startsInFuture;
    }

    public Instant getStartsAt() {
        return startsAt;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public FormThemeDto getTheme() {
        return theme;
    }

    public List<FormSectionDto> getSections() {
        return sections;
    }

    public List<FormQuestionDto> getQuestions() {
        return questions;
    }

    public List<FormLogicRuleDto> getLogicRules() {
        return logicRules;
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

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public void setPasswordProtected(boolean isPasswordProtected) {
        this.isPasswordProtected = isPasswordProtected;
    }

    public void setExpired(boolean isExpired) {
        this.isExpired = isExpired;
    }

    public void setLimitReached(boolean isLimitReached) {
        this.isLimitReached = isLimitReached;
    }

    public void setStartsInFuture(boolean startsInFuture) {
        this.startsInFuture = startsInFuture;
    }

    public void setStartsAt(Instant startsAt) {
        this.startsAt = startsAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    public void setTheme(FormThemeDto theme) {
        this.theme = theme;
    }

    public void setSections(List<FormSectionDto> sections) {
        this.sections = sections;
    }

    public void setQuestions(List<FormQuestionDto> questions) {
        this.questions = questions;
    }

    public void setLogicRules(List<FormLogicRuleDto> logicRules) {
        this.logicRules = logicRules;
    }

    public static PublicFormViewResponseBuilder builder() {
        return new PublicFormViewResponseBuilder();
    }
    public static class PublicFormViewResponseBuilder {
        private UUID id;
        private String title;
        private String description;
        private String slug;
        private boolean isPasswordProtected;
        private boolean isExpired;
        private boolean isLimitReached;
        private boolean startsInFuture;
        private Instant startsAt;
        private Instant expiresAt;
        private FormThemeDto theme;
        private List<FormSectionDto> sections = new ArrayList<>();
        private List<FormQuestionDto> questions = new ArrayList<>();
        private List<FormLogicRuleDto> logicRules = new ArrayList<>();

        public PublicFormViewResponseBuilder id(UUID id) {
            this.id = id;
            return this;
        }
        public PublicFormViewResponseBuilder title(String title) {
            this.title = title;
            return this;
        }
        public PublicFormViewResponseBuilder description(String description) {
            this.description = description;
            return this;
        }
        public PublicFormViewResponseBuilder slug(String slug) {
            this.slug = slug;
            return this;
        }
        public PublicFormViewResponseBuilder isPasswordProtected(boolean isPasswordProtected) {
            this.isPasswordProtected = isPasswordProtected;
            return this;
        }
        public PublicFormViewResponseBuilder isExpired(boolean isExpired) {
            this.isExpired = isExpired;
            return this;
        }
        public PublicFormViewResponseBuilder isLimitReached(boolean isLimitReached) {
            this.isLimitReached = isLimitReached;
            return this;
        }
        public PublicFormViewResponseBuilder startsInFuture(boolean startsInFuture) {
            this.startsInFuture = startsInFuture;
            return this;
        }
        public PublicFormViewResponseBuilder startsAt(Instant startsAt) {
            this.startsAt = startsAt;
            return this;
        }
        public PublicFormViewResponseBuilder expiresAt(Instant expiresAt) {
            this.expiresAt = expiresAt;
            return this;
        }
        public PublicFormViewResponseBuilder theme(FormThemeDto theme) {
            this.theme = theme;
            return this;
        }
        public PublicFormViewResponseBuilder sections(List<FormSectionDto> sections) {
            this.sections = sections;
            return this;
        }
        public PublicFormViewResponseBuilder questions(List<FormQuestionDto> questions) {
            this.questions = questions;
            return this;
        }
        public PublicFormViewResponseBuilder logicRules(List<FormLogicRuleDto> logicRules) {
            this.logicRules = logicRules;
            return this;
        }

        public PublicFormViewResponse build() {
            PublicFormViewResponse instance = new PublicFormViewResponse();
            instance.id = this.id;
            instance.title = this.title;
            instance.description = this.description;
            instance.slug = this.slug;
            instance.isPasswordProtected = this.isPasswordProtected;
            instance.isExpired = this.isExpired;
            instance.isLimitReached = this.isLimitReached;
            instance.startsInFuture = this.startsInFuture;
            instance.startsAt = this.startsAt;
            instance.expiresAt = this.expiresAt;
            instance.theme = this.theme;
            instance.sections = this.sections;
            instance.questions = this.questions;
            instance.logicRules = this.logicRules;
            return instance;
        }
    }
}

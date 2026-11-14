package com.formgenius.analytics.dto;


import java.util.UUID;

public class FormPerformanceDto {
    private UUID formId;
    private String title;
    private String status;
    private int views;
    private int submissions;
    private double completionRate;

    public FormPerformanceDto() {}

    public FormPerformanceDto(UUID formId, String title, String status, int views, int submissions, double completionRate) {
        this.formId = formId;
        this.title = title;
        this.status = status;
        this.views = views;
        this.submissions = submissions;
        this.completionRate = completionRate;
    }

    public UUID getFormId() {
        return formId;
    }

    public String getTitle() {
        return title;
    }

    public String getStatus() {
        return status;
    }

    public int getViews() {
        return views;
    }

    public int getSubmissions() {
        return submissions;
    }

    public double getCompletionRate() {
        return completionRate;
    }

    public void setFormId(UUID formId) {
        this.formId = formId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public void setSubmissions(int submissions) {
        this.submissions = submissions;
    }

    public void setCompletionRate(double completionRate) {
        this.completionRate = completionRate;
    }

    public static FormPerformanceDtoBuilder builder() {
        return new FormPerformanceDtoBuilder();
    }
    public static class FormPerformanceDtoBuilder {
        private UUID formId;
        private String title;
        private String status;
        private int views;
        private int submissions;
        private double completionRate;

        public FormPerformanceDtoBuilder formId(UUID formId) {
            this.formId = formId;
            return this;
        }
        public FormPerformanceDtoBuilder title(String title) {
            this.title = title;
            return this;
        }
        public FormPerformanceDtoBuilder status(String status) {
            this.status = status;
            return this;
        }
        public FormPerformanceDtoBuilder views(int views) {
            this.views = views;
            return this;
        }
        public FormPerformanceDtoBuilder submissions(int submissions) {
            this.submissions = submissions;
            return this;
        }
        public FormPerformanceDtoBuilder completionRate(double completionRate) {
            this.completionRate = completionRate;
            return this;
        }

        public FormPerformanceDto build() {
            FormPerformanceDto instance = new FormPerformanceDto();
            instance.formId = this.formId;
            instance.title = this.title;
            instance.status = this.status;
            instance.views = this.views;
            instance.submissions = this.submissions;
            instance.completionRate = this.completionRate;
            return instance;
        }
    }
}

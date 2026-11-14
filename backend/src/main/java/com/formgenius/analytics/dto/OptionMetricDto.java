package com.formgenius.analytics.dto;


public class OptionMetricDto {
    private String label;
    private long count;
    private double percentage;

    public OptionMetricDto() {}

    public OptionMetricDto(String label, long count, double percentage) {
        this.label = label;
        this.count = count;
        this.percentage = percentage;
    }

    public String getLabel() {
        return label;
    }

    public long getCount() {
        return count;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public static OptionMetricDtoBuilder builder() {
        return new OptionMetricDtoBuilder();
    }
    public static class OptionMetricDtoBuilder {
        private String label;
        private long count;
        private double percentage;

        public OptionMetricDtoBuilder label(String label) {
            this.label = label;
            return this;
        }
        public OptionMetricDtoBuilder count(long count) {
            this.count = count;
            return this;
        }
        public OptionMetricDtoBuilder percentage(double percentage) {
            this.percentage = percentage;
            return this;
        }

        public OptionMetricDto build() {
            OptionMetricDto instance = new OptionMetricDto();
            instance.label = this.label;
            instance.count = this.count;
            instance.percentage = this.percentage;
            return instance;
        }
    }
}

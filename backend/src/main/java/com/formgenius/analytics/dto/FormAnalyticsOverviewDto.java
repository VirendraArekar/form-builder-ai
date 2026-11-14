package com.formgenius.analytics.dto;


import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class FormAnalyticsOverviewDto {
    private long totalViews;
    private long totalSubmissions;
    private double completionRate;
    private double abandonmentRate;
    private int avgDurationSeconds;
    private List<DailyTrendDto> dailyTrends;
    private Map<String, Integer> deviceDistribution;

    public FormAnalyticsOverviewDto() {}

    public FormAnalyticsOverviewDto(long totalViews, long totalSubmissions, double completionRate, double abandonmentRate, int avgDurationSeconds, List<DailyTrendDto> dailyTrends, Map<String, Integer> deviceDistribution) {
        this.totalViews = totalViews;
        this.totalSubmissions = totalSubmissions;
        this.completionRate = completionRate;
        this.abandonmentRate = abandonmentRate;
        this.avgDurationSeconds = avgDurationSeconds;
        this.dailyTrends = dailyTrends;
        this.deviceDistribution = deviceDistribution;
    }

    public long getTotalViews() {
        return totalViews;
    }

    public long getTotalSubmissions() {
        return totalSubmissions;
    }

    public double getCompletionRate() {
        return completionRate;
    }

    public double getAbandonmentRate() {
        return abandonmentRate;
    }

    public int getAvgDurationSeconds() {
        return avgDurationSeconds;
    }

    public List<DailyTrendDto> getDailyTrends() {
        return dailyTrends;
    }

    public Map<String, Integer> getDeviceDistribution() {
        return deviceDistribution;
    }

    public void setTotalViews(long totalViews) {
        this.totalViews = totalViews;
    }

    public void setTotalSubmissions(long totalSubmissions) {
        this.totalSubmissions = totalSubmissions;
    }

    public void setCompletionRate(double completionRate) {
        this.completionRate = completionRate;
    }

    public void setAbandonmentRate(double abandonmentRate) {
        this.abandonmentRate = abandonmentRate;
    }

    public void setAvgDurationSeconds(int avgDurationSeconds) {
        this.avgDurationSeconds = avgDurationSeconds;
    }

    public void setDailyTrends(List<DailyTrendDto> dailyTrends) {
        this.dailyTrends = dailyTrends;
    }

    public void setDeviceDistribution(Map<String, Integer> deviceDistribution) {
        this.deviceDistribution = deviceDistribution;
    }

    public static FormAnalyticsOverviewDtoBuilder builder() {
        return new FormAnalyticsOverviewDtoBuilder();
    }
    public static class FormAnalyticsOverviewDtoBuilder {
        private long totalViews;
        private long totalSubmissions;
        private double completionRate;
        private double abandonmentRate;
        private int avgDurationSeconds;
        private List<DailyTrendDto> dailyTrends;
        private Map<String, Integer> deviceDistribution;

        public FormAnalyticsOverviewDtoBuilder totalViews(long totalViews) {
            this.totalViews = totalViews;
            return this;
        }
        public FormAnalyticsOverviewDtoBuilder totalSubmissions(long totalSubmissions) {
            this.totalSubmissions = totalSubmissions;
            return this;
        }
        public FormAnalyticsOverviewDtoBuilder completionRate(double completionRate) {
            this.completionRate = completionRate;
            return this;
        }
        public FormAnalyticsOverviewDtoBuilder abandonmentRate(double abandonmentRate) {
            this.abandonmentRate = abandonmentRate;
            return this;
        }
        public FormAnalyticsOverviewDtoBuilder avgDurationSeconds(int avgDurationSeconds) {
            this.avgDurationSeconds = avgDurationSeconds;
            return this;
        }
        public FormAnalyticsOverviewDtoBuilder dailyTrends(List<DailyTrendDto> dailyTrends) {
            this.dailyTrends = dailyTrends;
            return this;
        }
        public FormAnalyticsOverviewDtoBuilder deviceDistribution(Map<String, Integer> deviceDistribution) {
            this.deviceDistribution = deviceDistribution;
            return this;
        }

        public FormAnalyticsOverviewDto build() {
            FormAnalyticsOverviewDto instance = new FormAnalyticsOverviewDto();
            instance.totalViews = this.totalViews;
            instance.totalSubmissions = this.totalSubmissions;
            instance.completionRate = this.completionRate;
            instance.abandonmentRate = this.abandonmentRate;
            instance.avgDurationSeconds = this.avgDurationSeconds;
            instance.dailyTrends = this.dailyTrends;
            instance.deviceDistribution = this.deviceDistribution;
            return instance;
        }
    }
}

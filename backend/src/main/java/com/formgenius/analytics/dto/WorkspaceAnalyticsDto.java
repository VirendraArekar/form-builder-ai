package com.formgenius.analytics.dto;


import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WorkspaceAnalyticsDto {
    private long totalForms;
    private long publishedForms;
    private long draftForms;
    private long totalResponses;
    private double overallCompletionRate;
    private int avgDurationSeconds;
    private List<DailyTrendDto> responseTrends = new ArrayList<>();
    private List<FormPerformanceDto> topPerformingForms = new ArrayList<>();
    private List<RecentSubmissionItemDto> recentSubmissions = new ArrayList<>();

    public WorkspaceAnalyticsDto() {}

    public WorkspaceAnalyticsDto(long totalForms, long publishedForms, long draftForms, long totalResponses, double overallCompletionRate, int avgDurationSeconds, List<DailyTrendDto> responseTrends, List<FormPerformanceDto> topPerformingForms, List<RecentSubmissionItemDto> recentSubmissions) {
        this.totalForms = totalForms;
        this.publishedForms = publishedForms;
        this.draftForms = draftForms;
        this.totalResponses = totalResponses;
        this.overallCompletionRate = overallCompletionRate;
        this.avgDurationSeconds = avgDurationSeconds;
        this.responseTrends = responseTrends;
        this.topPerformingForms = topPerformingForms;
        this.recentSubmissions = recentSubmissions;
    }

    public long getTotalForms() {
        return totalForms;
    }

    public long getPublishedForms() {
        return publishedForms;
    }

    public long getDraftForms() {
        return draftForms;
    }

    public long getTotalResponses() {
        return totalResponses;
    }

    public double getOverallCompletionRate() {
        return overallCompletionRate;
    }

    public int getAvgDurationSeconds() {
        return avgDurationSeconds;
    }

    public List<DailyTrendDto> getResponseTrends() {
        return responseTrends;
    }

    public List<FormPerformanceDto> getTopPerformingForms() {
        return topPerformingForms;
    }

    public List<RecentSubmissionItemDto> getRecentSubmissions() {
        return recentSubmissions;
    }

    public void setTotalForms(long totalForms) {
        this.totalForms = totalForms;
    }

    public void setPublishedForms(long publishedForms) {
        this.publishedForms = publishedForms;
    }

    public void setDraftForms(long draftForms) {
        this.draftForms = draftForms;
    }

    public void setTotalResponses(long totalResponses) {
        this.totalResponses = totalResponses;
    }

    public void setOverallCompletionRate(double overallCompletionRate) {
        this.overallCompletionRate = overallCompletionRate;
    }

    public void setAvgDurationSeconds(int avgDurationSeconds) {
        this.avgDurationSeconds = avgDurationSeconds;
    }

    public void setResponseTrends(List<DailyTrendDto> responseTrends) {
        this.responseTrends = responseTrends;
    }

    public void setTopPerformingForms(List<FormPerformanceDto> topPerformingForms) {
        this.topPerformingForms = topPerformingForms;
    }

    public void setRecentSubmissions(List<RecentSubmissionItemDto> recentSubmissions) {
        this.recentSubmissions = recentSubmissions;
    }

    public static WorkspaceAnalyticsDtoBuilder builder() {
        return new WorkspaceAnalyticsDtoBuilder();
    }
    public static class WorkspaceAnalyticsDtoBuilder {
        private long totalForms;
        private long publishedForms;
        private long draftForms;
        private long totalResponses;
        private double overallCompletionRate;
        private int avgDurationSeconds;
        private List<DailyTrendDto> responseTrends = new ArrayList<>();
        private List<FormPerformanceDto> topPerformingForms = new ArrayList<>();
        private List<RecentSubmissionItemDto> recentSubmissions = new ArrayList<>();

        public WorkspaceAnalyticsDtoBuilder totalForms(long totalForms) {
            this.totalForms = totalForms;
            return this;
        }
        public WorkspaceAnalyticsDtoBuilder publishedForms(long publishedForms) {
            this.publishedForms = publishedForms;
            return this;
        }
        public WorkspaceAnalyticsDtoBuilder draftForms(long draftForms) {
            this.draftForms = draftForms;
            return this;
        }
        public WorkspaceAnalyticsDtoBuilder totalResponses(long totalResponses) {
            this.totalResponses = totalResponses;
            return this;
        }
        public WorkspaceAnalyticsDtoBuilder overallCompletionRate(double overallCompletionRate) {
            this.overallCompletionRate = overallCompletionRate;
            return this;
        }
        public WorkspaceAnalyticsDtoBuilder avgDurationSeconds(int avgDurationSeconds) {
            this.avgDurationSeconds = avgDurationSeconds;
            return this;
        }
        public WorkspaceAnalyticsDtoBuilder responseTrends(List<DailyTrendDto> responseTrends) {
            this.responseTrends = responseTrends;
            return this;
        }
        public WorkspaceAnalyticsDtoBuilder topPerformingForms(List<FormPerformanceDto> topPerformingForms) {
            this.topPerformingForms = topPerformingForms;
            return this;
        }
        public WorkspaceAnalyticsDtoBuilder recentSubmissions(List<RecentSubmissionItemDto> recentSubmissions) {
            this.recentSubmissions = recentSubmissions;
            return this;
        }

        public WorkspaceAnalyticsDto build() {
            WorkspaceAnalyticsDto instance = new WorkspaceAnalyticsDto();
            instance.totalForms = this.totalForms;
            instance.publishedForms = this.publishedForms;
            instance.draftForms = this.draftForms;
            instance.totalResponses = this.totalResponses;
            instance.overallCompletionRate = this.overallCompletionRate;
            instance.avgDurationSeconds = this.avgDurationSeconds;
            instance.responseTrends = this.responseTrends;
            instance.topPerformingForms = this.topPerformingForms;
            instance.recentSubmissions = this.recentSubmissions;
            return instance;
        }
    }
}

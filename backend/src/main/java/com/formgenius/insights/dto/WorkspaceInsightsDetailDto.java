package com.formgenius.insights.dto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class WorkspaceInsightsDetailDto {
    private long totalForms;
    private long publishedForms;
    private long totalResponses;
    private long totalViews;
    private double overallConversionRate;
    private int avgCompletionTimeSeconds;

    private List<Map<String, Object>> responseTimeline;
    private List<Map<String, Object>> dailyActivity;
    private List<Map<String, Object>> weeklyActivity;
    private List<Map<String, Object>> hourlyHeatmap; // 0-23 hours
    private Map<String, Integer> deviceDistribution;
    private List<LeaderboardFormDto> leaderboard; // Top 6 forms

    public WorkspaceInsightsDetailDto() {}

    public static class LeaderboardFormDto {
        private UUID formId;
        private String title;
        private String status;
        private int responseCount;
        private int viewCount;
        private double conversionRate;

        public LeaderboardFormDto() {}

        public LeaderboardFormDto(UUID formId, String title, String status, int responseCount, int viewCount, double conversionRate) {
            this.formId = formId;
            this.title = title;
            this.status = status;
            this.responseCount = responseCount;
            this.viewCount = viewCount;
            this.conversionRate = conversionRate;
        }

        public UUID getFormId() {
            return formId;
        }

        public void setFormId(UUID formId) {
            this.formId = formId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public int getResponseCount() {
            return responseCount;
        }

        public void setResponseCount(int responseCount) {
            this.responseCount = responseCount;
        }

        public int getViewCount() {
            return viewCount;
        }

        public void setViewCount(int viewCount) {
            this.viewCount = viewCount;
        }

        public double getConversionRate() {
            return conversionRate;
        }

        public void setConversionRate(double conversionRate) {
            this.conversionRate = conversionRate;
        }
    }

    public long getTotalForms() {
        return totalForms;
    }

    public void setTotalForms(long totalForms) {
        this.totalForms = totalForms;
    }

    public long getPublishedForms() {
        return publishedForms;
    }

    public void setPublishedForms(long publishedForms) {
        this.publishedForms = publishedForms;
    }

    public long getTotalResponses() {
        return totalResponses;
    }

    public void setTotalResponses(long totalResponses) {
        this.totalResponses = totalResponses;
    }

    public long getTotalViews() {
        return totalViews;
    }

    public void setTotalViews(long totalViews) {
        this.totalViews = totalViews;
    }

    public double getOverallConversionRate() {
        return overallConversionRate;
    }

    public void setOverallConversionRate(double overallConversionRate) {
        this.overallConversionRate = overallConversionRate;
    }

    public int getAvgCompletionTimeSeconds() {
        return avgCompletionTimeSeconds;
    }

    public void setAvgCompletionTimeSeconds(int avgCompletionTimeSeconds) {
        this.avgCompletionTimeSeconds = avgCompletionTimeSeconds;
    }

    public List<Map<String, Object>> getResponseTimeline() {
        return responseTimeline;
    }

    public void setResponseTimeline(List<Map<String, Object>> responseTimeline) {
        this.responseTimeline = responseTimeline;
    }

    public List<Map<String, Object>> getDailyActivity() {
        return dailyActivity;
    }

    public void setDailyActivity(List<Map<String, Object>> dailyActivity) {
        this.dailyActivity = dailyActivity;
    }

    public List<Map<String, Object>> getWeeklyActivity() {
        return weeklyActivity;
    }

    public void setWeeklyActivity(List<Map<String, Object>> weeklyActivity) {
        this.weeklyActivity = weeklyActivity;
    }

    public List<Map<String, Object>> getHourlyHeatmap() {
        return hourlyHeatmap;
    }

    public void setHourlyHeatmap(List<Map<String, Object>> hourlyHeatmap) {
        this.hourlyHeatmap = hourlyHeatmap;
    }

    public Map<String, Integer> getDeviceDistribution() {
        return deviceDistribution;
    }

    public void setDeviceDistribution(Map<String, Integer> deviceDistribution) {
        this.deviceDistribution = deviceDistribution;
    }

    public List<LeaderboardFormDto> getLeaderboard() {
        return leaderboard;
    }

    public void setLeaderboard(List<LeaderboardFormDto> leaderboard) {
        this.leaderboard = leaderboard;
    }
}

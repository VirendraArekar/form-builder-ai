package com.formgenius.ai.dto;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AiInsightsResponse {
    private String summary;
    private double overallSentimentScore; // -1.0 to 1.0
    private Map<String, Integer> sentimentBreakdown; // Positive, Neutral, Negative counts/pct
    private List<String> positiveHighlights = new ArrayList<>();
    private List<String> commonComplaints = new ArrayList<>();
    private List<String> emergingThemes = new ArrayList<>();
    private List<String> actionableRecommendations = new ArrayList<>();
    private String answerToQuery;

    public AiInsightsResponse() {}

    public AiInsightsResponse(String summary, double overallSentimentScore, Map<String, Integer> sentimentBreakdown, List<String> positiveHighlights, List<String> commonComplaints, List<String> emergingThemes, List<String> actionableRecommendations, String answerToQuery) {
        this.summary = summary;
        this.overallSentimentScore = overallSentimentScore;
        this.sentimentBreakdown = sentimentBreakdown;
        this.positiveHighlights = positiveHighlights;
        this.commonComplaints = commonComplaints;
        this.emergingThemes = emergingThemes;
        this.actionableRecommendations = actionableRecommendations;
        this.answerToQuery = answerToQuery;
    }

    public String getSummary() {
        return summary;
    }

    public double getOverallSentimentScore() {
        return overallSentimentScore;
    }

    public Map<String, Integer> getSentimentBreakdown() {
        return sentimentBreakdown;
    }

    public List<String> getPositiveHighlights() {
        return positiveHighlights;
    }

    public List<String> getCommonComplaints() {
        return commonComplaints;
    }

    public List<String> getEmergingThemes() {
        return emergingThemes;
    }

    public List<String> getActionableRecommendations() {
        return actionableRecommendations;
    }

    public String getAnswerToQuery() {
        return answerToQuery;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setOverallSentimentScore(double overallSentimentScore) {
        this.overallSentimentScore = overallSentimentScore;
    }

    public void setSentimentBreakdown(Map<String, Integer> sentimentBreakdown) {
        this.sentimentBreakdown = sentimentBreakdown;
    }

    public void setPositiveHighlights(List<String> positiveHighlights) {
        this.positiveHighlights = positiveHighlights;
    }

    public void setCommonComplaints(List<String> commonComplaints) {
        this.commonComplaints = commonComplaints;
    }

    public void setEmergingThemes(List<String> emergingThemes) {
        this.emergingThemes = emergingThemes;
    }

    public void setActionableRecommendations(List<String> actionableRecommendations) {
        this.actionableRecommendations = actionableRecommendations;
    }

    public void setAnswerToQuery(String answerToQuery) {
        this.answerToQuery = answerToQuery;
    }

    public static AiInsightsResponseBuilder builder() {
        return new AiInsightsResponseBuilder();
    }
    public static class AiInsightsResponseBuilder {
        private String summary;
        private double overallSentimentScore;
        private Map<String, Integer> sentimentBreakdown;
        private List<String> positiveHighlights = new ArrayList<>();
        private List<String> commonComplaints = new ArrayList<>();
        private List<String> emergingThemes = new ArrayList<>();
        private List<String> actionableRecommendations = new ArrayList<>();
        private String answerToQuery;

        public AiInsightsResponseBuilder summary(String summary) {
            this.summary = summary;
            return this;
        }
        public AiInsightsResponseBuilder overallSentimentScore(double overallSentimentScore) {
            this.overallSentimentScore = overallSentimentScore;
            return this;
        }
        public AiInsightsResponseBuilder sentimentBreakdown(Map<String, Integer> sentimentBreakdown) {
            this.sentimentBreakdown = sentimentBreakdown;
            return this;
        }
        public AiInsightsResponseBuilder positiveHighlights(List<String> positiveHighlights) {
            this.positiveHighlights = positiveHighlights;
            return this;
        }
        public AiInsightsResponseBuilder commonComplaints(List<String> commonComplaints) {
            this.commonComplaints = commonComplaints;
            return this;
        }
        public AiInsightsResponseBuilder emergingThemes(List<String> emergingThemes) {
            this.emergingThemes = emergingThemes;
            return this;
        }
        public AiInsightsResponseBuilder actionableRecommendations(List<String> actionableRecommendations) {
            this.actionableRecommendations = actionableRecommendations;
            return this;
        }
        public AiInsightsResponseBuilder answerToQuery(String answerToQuery) {
            this.answerToQuery = answerToQuery;
            return this;
        }

        public AiInsightsResponse build() {
            AiInsightsResponse instance = new AiInsightsResponse();
            instance.summary = this.summary;
            instance.overallSentimentScore = this.overallSentimentScore;
            instance.sentimentBreakdown = this.sentimentBreakdown;
            instance.positiveHighlights = this.positiveHighlights;
            instance.commonComplaints = this.commonComplaints;
            instance.emergingThemes = this.emergingThemes;
            instance.actionableRecommendations = this.actionableRecommendations;
            instance.answerToQuery = this.answerToQuery;
            return instance;
        }
    }
}

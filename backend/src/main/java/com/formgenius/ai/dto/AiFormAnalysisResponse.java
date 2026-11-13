package com.formgenius.ai.dto;

import java.util.List;

public class AiFormAnalysisResponse {
    private String formPurpose;
    private String targetAudience;
    private int estimatedCompletionTimeMinutes;
    private int questionQualityScore; // 0-100
    private List<String> missingQuestions;
    private List<String> improvementSuggestions;
    private List<String> recommendations;

    public AiFormAnalysisResponse() {}

    public AiFormAnalysisResponse(String formPurpose, String targetAudience, int estimatedCompletionTimeMinutes, int questionQualityScore, List<String> missingQuestions, List<String> improvementSuggestions, List<String> recommendations) {
        this.formPurpose = formPurpose;
        this.targetAudience = targetAudience;
        this.estimatedCompletionTimeMinutes = estimatedCompletionTimeMinutes;
        this.questionQualityScore = questionQualityScore;
        this.missingQuestions = missingQuestions;
        this.improvementSuggestions = improvementSuggestions;
        this.recommendations = recommendations;
    }

    public String getFormPurpose() {
        return formPurpose;
    }

    public void setFormPurpose(String formPurpose) {
        this.formPurpose = formPurpose;
    }

    public String getTargetAudience() {
        return targetAudience;
    }

    public void setTargetAudience(String targetAudience) {
        this.targetAudience = targetAudience;
    }

    public int getEstimatedCompletionTimeMinutes() {
        return estimatedCompletionTimeMinutes;
    }

    public void setEstimatedCompletionTimeMinutes(int estimatedCompletionTimeMinutes) {
        this.estimatedCompletionTimeMinutes = estimatedCompletionTimeMinutes;
    }

    public int getQuestionQualityScore() {
        return questionQualityScore;
    }

    public void setQuestionQualityScore(int questionQualityScore) {
        this.questionQualityScore = questionQualityScore;
    }

    public List<String> getMissingQuestions() {
        return missingQuestions;
    }

    public void setMissingQuestions(List<String> missingQuestions) {
        this.missingQuestions = missingQuestions;
    }

    public List<String> getImprovementSuggestions() {
        return improvementSuggestions;
    }

    public void setImprovementSuggestions(List<String> improvementSuggestions) {
        this.improvementSuggestions = improvementSuggestions;
    }

    public List<String> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(List<String> recommendations) {
        this.recommendations = recommendations;
    }
}

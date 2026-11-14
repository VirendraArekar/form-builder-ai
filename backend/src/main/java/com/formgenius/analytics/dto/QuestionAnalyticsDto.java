package com.formgenius.analytics.dto;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class QuestionAnalyticsDto {
    private UUID questionId;
    private String questionKey;
    private String questionTitle;
    private String questionType;
    private int totalAnswers;
    private List<OptionMetricDto> optionMetrics = new ArrayList<>();
    private NumericStatsDto numericStats;
    private List<String> sampleAnswers = new ArrayList<>();

    public QuestionAnalyticsDto() {}

    public QuestionAnalyticsDto(UUID questionId, String questionKey, String questionTitle, String questionType, int totalAnswers, List<OptionMetricDto> optionMetrics, NumericStatsDto numericStats, List<String> sampleAnswers) {
        this.questionId = questionId;
        this.questionKey = questionKey;
        this.questionTitle = questionTitle;
        this.questionType = questionType;
        this.totalAnswers = totalAnswers;
        this.optionMetrics = optionMetrics;
        this.numericStats = numericStats;
        this.sampleAnswers = sampleAnswers;
    }

    public UUID getQuestionId() {
        return questionId;
    }

    public String getQuestionKey() {
        return questionKey;
    }

    public String getQuestionTitle() {
        return questionTitle;
    }

    public String getQuestionType() {
        return questionType;
    }

    public int getTotalAnswers() {
        return totalAnswers;
    }

    public List<OptionMetricDto> getOptionMetrics() {
        return optionMetrics;
    }

    public NumericStatsDto getNumericStats() {
        return numericStats;
    }

    public List<String> getSampleAnswers() {
        return sampleAnswers;
    }

    public void setQuestionId(UUID questionId) {
        this.questionId = questionId;
    }

    public void setQuestionKey(String questionKey) {
        this.questionKey = questionKey;
    }

    public void setQuestionTitle(String questionTitle) {
        this.questionTitle = questionTitle;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public void setTotalAnswers(int totalAnswers) {
        this.totalAnswers = totalAnswers;
    }

    public void setOptionMetrics(List<OptionMetricDto> optionMetrics) {
        this.optionMetrics = optionMetrics;
    }

    public void setNumericStats(NumericStatsDto numericStats) {
        this.numericStats = numericStats;
    }

    public void setSampleAnswers(List<String> sampleAnswers) {
        this.sampleAnswers = sampleAnswers;
    }

    public static QuestionAnalyticsDtoBuilder builder() {
        return new QuestionAnalyticsDtoBuilder();
    }
    public static class QuestionAnalyticsDtoBuilder {
        private UUID questionId;
        private String questionKey;
        private String questionTitle;
        private String questionType;
        private int totalAnswers;
        private List<OptionMetricDto> optionMetrics = new ArrayList<>();
        private NumericStatsDto numericStats;
        private List<String> sampleAnswers = new ArrayList<>();

        public QuestionAnalyticsDtoBuilder questionId(UUID questionId) {
            this.questionId = questionId;
            return this;
        }
        public QuestionAnalyticsDtoBuilder questionKey(String questionKey) {
            this.questionKey = questionKey;
            return this;
        }
        public QuestionAnalyticsDtoBuilder questionTitle(String questionTitle) {
            this.questionTitle = questionTitle;
            return this;
        }
        public QuestionAnalyticsDtoBuilder questionType(String questionType) {
            this.questionType = questionType;
            return this;
        }
        public QuestionAnalyticsDtoBuilder totalAnswers(int totalAnswers) {
            this.totalAnswers = totalAnswers;
            return this;
        }
        public QuestionAnalyticsDtoBuilder optionMetrics(List<OptionMetricDto> optionMetrics) {
            this.optionMetrics = optionMetrics;
            return this;
        }
        public QuestionAnalyticsDtoBuilder numericStats(NumericStatsDto numericStats) {
            this.numericStats = numericStats;
            return this;
        }
        public QuestionAnalyticsDtoBuilder sampleAnswers(List<String> sampleAnswers) {
            this.sampleAnswers = sampleAnswers;
            return this;
        }

        public QuestionAnalyticsDto build() {
            QuestionAnalyticsDto instance = new QuestionAnalyticsDto();
            instance.questionId = this.questionId;
            instance.questionKey = this.questionKey;
            instance.questionTitle = this.questionTitle;
            instance.questionType = this.questionType;
            instance.totalAnswers = this.totalAnswers;
            instance.optionMetrics = this.optionMetrics;
            instance.numericStats = this.numericStats;
            instance.sampleAnswers = this.sampleAnswers;
            return instance;
        }
    }
}

package com.formgenius.ai.dto;

public class AiValidationSuggestionResponse {
    private Integer minLength;
    private Integer maxLength;
    private Double minValue;
    private Double maxValue;
    private String regexPattern;
    private String errorMessage;
    private boolean isRequired;

    public AiValidationSuggestionResponse() {}

    public AiValidationSuggestionResponse(Integer minLength, Integer maxLength, Double minValue, Double maxValue, String regexPattern, String errorMessage, boolean isRequired) {
        this.minLength = minLength;
        this.maxLength = maxLength;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.regexPattern = regexPattern;
        this.errorMessage = errorMessage;
        this.isRequired = isRequired;
    }

    public Integer getMinLength() {
        return minLength;
    }

    public void setMinLength(Integer minLength) {
        this.minLength = minLength;
    }

    public Integer getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }

    public Double getMinValue() {
        return minValue;
    }

    public void setMinValue(Double minValue) {
        this.minValue = minValue;
    }

    public Double getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(Double maxValue) {
        this.maxValue = maxValue;
    }

    public String getRegexPattern() {
        return regexPattern;
    }

    public void setRegexPattern(String regexPattern) {
        this.regexPattern = regexPattern;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean isRequired() {
        return isRequired;
    }

    public void setRequired(boolean required) {
        isRequired = required;
    }
}

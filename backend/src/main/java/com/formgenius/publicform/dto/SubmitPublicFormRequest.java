package com.formgenius.publicform.dto;


import java.util.HashMap;
import java.util.Map;

public class SubmitPublicFormRequest {
    private String accessPassword;
    private int durationSeconds;
    private Map<String, Object> answers = new HashMap<>();

    public SubmitPublicFormRequest() {}

    public SubmitPublicFormRequest(String accessPassword, int durationSeconds, Map<String, Object> answers) {
        this.accessPassword = accessPassword;
        this.durationSeconds = durationSeconds;
        this.answers = answers;
    }

    public String getAccessPassword() {
        return accessPassword;
    }

    public int getDurationSeconds() {
        return durationSeconds;
    }

    public Map<String, Object> getAnswers() {
        return answers;
    }

    public void setAccessPassword(String accessPassword) {
        this.accessPassword = accessPassword;
    }

    public void setDurationSeconds(int durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    public void setAnswers(Map<String, Object> answers) {
        this.answers = answers;
    }

    public static SubmitPublicFormRequestBuilder builder() {
        return new SubmitPublicFormRequestBuilder();
    }
    public static class SubmitPublicFormRequestBuilder {
        private String accessPassword;
        private int durationSeconds;
        private Map<String, Object> answers = new HashMap<>();

        public SubmitPublicFormRequestBuilder accessPassword(String accessPassword) {
            this.accessPassword = accessPassword;
            return this;
        }
        public SubmitPublicFormRequestBuilder durationSeconds(int durationSeconds) {
            this.durationSeconds = durationSeconds;
            return this;
        }
        public SubmitPublicFormRequestBuilder answers(Map<String, Object> answers) {
            this.answers = answers;
            return this;
        }

        public SubmitPublicFormRequest build() {
            SubmitPublicFormRequest instance = new SubmitPublicFormRequest();
            instance.accessPassword = this.accessPassword;
            instance.durationSeconds = this.durationSeconds;
            instance.answers = this.answers;
            return instance;
        }
    }
}

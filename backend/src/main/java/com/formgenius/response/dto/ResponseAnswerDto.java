package com.formgenius.response.dto;


import java.util.UUID;

public class ResponseAnswerDto {
    private UUID id;
    private UUID questionId;
    private String questionKey;
    private String questionTitle;
    private String questionType;
    private String answerText;
    private String answerJson;

    public ResponseAnswerDto() {}

    public ResponseAnswerDto(UUID id, UUID questionId, String questionKey, String questionTitle, String questionType, String answerText, String answerJson) {
        this.id = id;
        this.questionId = questionId;
        this.questionKey = questionKey;
        this.questionTitle = questionTitle;
        this.questionType = questionType;
        this.answerText = answerText;
        this.answerJson = answerJson;
    }

    public UUID getId() {
        return id;
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

    public String getAnswerText() {
        return answerText;
    }

    public String getAnswerJson() {
        return answerJson;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    public void setAnswerJson(String answerJson) {
        this.answerJson = answerJson;
    }

    public static ResponseAnswerDtoBuilder builder() {
        return new ResponseAnswerDtoBuilder();
    }
    public static class ResponseAnswerDtoBuilder {
        private UUID id;
        private UUID questionId;
        private String questionKey;
        private String questionTitle;
        private String questionType;
        private String answerText;
        private String answerJson;

        public ResponseAnswerDtoBuilder id(UUID id) {
            this.id = id;
            return this;
        }
        public ResponseAnswerDtoBuilder questionId(UUID questionId) {
            this.questionId = questionId;
            return this;
        }
        public ResponseAnswerDtoBuilder questionKey(String questionKey) {
            this.questionKey = questionKey;
            return this;
        }
        public ResponseAnswerDtoBuilder questionTitle(String questionTitle) {
            this.questionTitle = questionTitle;
            return this;
        }
        public ResponseAnswerDtoBuilder questionType(String questionType) {
            this.questionType = questionType;
            return this;
        }
        public ResponseAnswerDtoBuilder answerText(String answerText) {
            this.answerText = answerText;
            return this;
        }
        public ResponseAnswerDtoBuilder answerJson(String answerJson) {
            this.answerJson = answerJson;
            return this;
        }

        public ResponseAnswerDto build() {
            ResponseAnswerDto instance = new ResponseAnswerDto();
            instance.id = this.id;
            instance.questionId = this.questionId;
            instance.questionKey = this.questionKey;
            instance.questionTitle = this.questionTitle;
            instance.questionType = this.questionType;
            instance.answerText = this.answerText;
            instance.answerJson = this.answerJson;
            return instance;
        }
    }
}

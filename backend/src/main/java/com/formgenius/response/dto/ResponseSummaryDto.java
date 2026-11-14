package com.formgenius.response.dto;


import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ResponseSummaryDto {
    private UUID id;
    private UUID formId;
    private String respondentIp;
    private String userAgent;
    private int durationSeconds;
    private String status;
    private boolean isReviewed;
    private Instant submittedAt;
    private int notesCount;
    private List<ResponseTagDto> tags;
    private Map<String, String> answers = new HashMap<>();

    public ResponseSummaryDto() {}

    public ResponseSummaryDto(UUID id, UUID formId, String respondentIp, String userAgent, int durationSeconds, String status, boolean isReviewed, Instant submittedAt, int notesCount, List<ResponseTagDto> tags, Map<String, String> answers) {
        this.id = id;
        this.formId = formId;
        this.respondentIp = respondentIp;
        this.userAgent = userAgent;
        this.durationSeconds = durationSeconds;
        this.status = status;
        this.isReviewed = isReviewed;
        this.submittedAt = submittedAt;
        this.notesCount = notesCount;
        this.tags = tags;
        this.answers = answers;
    }

    public UUID getId() {
        return id;
    }

    public UUID getFormId() {
        return formId;
    }

    public String getRespondentIp() {
        return respondentIp;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public int getDurationSeconds() {
        return durationSeconds;
    }

    public String getStatus() {
        return status;
    }

    public boolean isReviewed() {
        return isReviewed;
    }

    public Instant getSubmittedAt() {
        return submittedAt;
    }

    public int getNotesCount() {
        return notesCount;
    }

    public List<ResponseTagDto> getTags() {
        return tags;
    }

    public Map<String, String> getAnswers() {
        return answers;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setFormId(UUID formId) {
        this.formId = formId;
    }

    public void setRespondentIp(String respondentIp) {
        this.respondentIp = respondentIp;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public void setDurationSeconds(int durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setReviewed(boolean isReviewed) {
        this.isReviewed = isReviewed;
    }

    public void setSubmittedAt(Instant submittedAt) {
        this.submittedAt = submittedAt;
    }

    public void setNotesCount(int notesCount) {
        this.notesCount = notesCount;
    }

    public void setTags(List<ResponseTagDto> tags) {
        this.tags = tags;
    }

    public void setAnswers(Map<String, String> answers) {
        this.answers = answers;
    }

    public static ResponseSummaryDtoBuilder builder() {
        return new ResponseSummaryDtoBuilder();
    }
    public static class ResponseSummaryDtoBuilder {
        private UUID id;
        private UUID formId;
        private String respondentIp;
        private String userAgent;
        private int durationSeconds;
        private String status;
        private boolean isReviewed;
        private Instant submittedAt;
        private int notesCount;
        private List<ResponseTagDto> tags;
        private Map<String, String> answers = new HashMap<>();

        public ResponseSummaryDtoBuilder id(UUID id) {
            this.id = id;
            return this;
        }
        public ResponseSummaryDtoBuilder formId(UUID formId) {
            this.formId = formId;
            return this;
        }
        public ResponseSummaryDtoBuilder respondentIp(String respondentIp) {
            this.respondentIp = respondentIp;
            return this;
        }
        public ResponseSummaryDtoBuilder userAgent(String userAgent) {
            this.userAgent = userAgent;
            return this;
        }
        public ResponseSummaryDtoBuilder durationSeconds(int durationSeconds) {
            this.durationSeconds = durationSeconds;
            return this;
        }
        public ResponseSummaryDtoBuilder status(String status) {
            this.status = status;
            return this;
        }
        public ResponseSummaryDtoBuilder isReviewed(boolean isReviewed) {
            this.isReviewed = isReviewed;
            return this;
        }
        public ResponseSummaryDtoBuilder submittedAt(Instant submittedAt) {
            this.submittedAt = submittedAt;
            return this;
        }
        public ResponseSummaryDtoBuilder notesCount(int notesCount) {
            this.notesCount = notesCount;
            return this;
        }
        public ResponseSummaryDtoBuilder tags(List<ResponseTagDto> tags) {
            this.tags = tags;
            return this;
        }
        public ResponseSummaryDtoBuilder answers(Map<String, String> answers) {
            this.answers = answers;
            return this;
        }

        public ResponseSummaryDto build() {
            ResponseSummaryDto instance = new ResponseSummaryDto();
            instance.id = this.id;
            instance.formId = this.formId;
            instance.respondentIp = this.respondentIp;
            instance.userAgent = this.userAgent;
            instance.durationSeconds = this.durationSeconds;
            instance.status = this.status;
            instance.isReviewed = this.isReviewed;
            instance.submittedAt = this.submittedAt;
            instance.notesCount = this.notesCount;
            instance.tags = this.tags;
            instance.answers = this.answers;
            return instance;
        }
    }
}

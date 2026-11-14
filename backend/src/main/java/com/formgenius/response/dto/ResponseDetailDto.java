package com.formgenius.response.dto;


import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ResponseDetailDto {
    private UUID id;
    private UUID formId;
    private String formTitle;
    private String respondentIp;
    private String userAgent;
    private String referrer;
    private int durationSeconds;
    private String status;
    private boolean isReviewed;
    private Instant submittedAt;
    private List<ResponseAnswerDto> answers = new ArrayList<>();
    private List<ResponseNoteDto> notes = new ArrayList<>();
    private List<ResponseTagDto> tags = new ArrayList<>();

    public ResponseDetailDto() {}

    public ResponseDetailDto(UUID id, UUID formId, String formTitle, String respondentIp, String userAgent, String referrer, int durationSeconds, String status, boolean isReviewed, Instant submittedAt, List<ResponseAnswerDto> answers, List<ResponseNoteDto> notes, List<ResponseTagDto> tags) {
        this.id = id;
        this.formId = formId;
        this.formTitle = formTitle;
        this.respondentIp = respondentIp;
        this.userAgent = userAgent;
        this.referrer = referrer;
        this.durationSeconds = durationSeconds;
        this.status = status;
        this.isReviewed = isReviewed;
        this.submittedAt = submittedAt;
        this.answers = answers;
        this.notes = notes;
        this.tags = tags;
    }

    public UUID getId() {
        return id;
    }

    public UUID getFormId() {
        return formId;
    }

    public String getFormTitle() {
        return formTitle;
    }

    public String getRespondentIp() {
        return respondentIp;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public String getReferrer() {
        return referrer;
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

    public List<ResponseAnswerDto> getAnswers() {
        return answers;
    }

    public List<ResponseNoteDto> getNotes() {
        return notes;
    }

    public List<ResponseTagDto> getTags() {
        return tags;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setFormId(UUID formId) {
        this.formId = formId;
    }

    public void setFormTitle(String formTitle) {
        this.formTitle = formTitle;
    }

    public void setRespondentIp(String respondentIp) {
        this.respondentIp = respondentIp;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public void setReferrer(String referrer) {
        this.referrer = referrer;
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

    public void setAnswers(List<ResponseAnswerDto> answers) {
        this.answers = answers;
    }

    public void setNotes(List<ResponseNoteDto> notes) {
        this.notes = notes;
    }

    public void setTags(List<ResponseTagDto> tags) {
        this.tags = tags;
    }

    public static ResponseDetailDtoBuilder builder() {
        return new ResponseDetailDtoBuilder();
    }
    public static class ResponseDetailDtoBuilder {
        private UUID id;
        private UUID formId;
        private String formTitle;
        private String respondentIp;
        private String userAgent;
        private String referrer;
        private int durationSeconds;
        private String status;
        private boolean isReviewed;
        private Instant submittedAt;
        private List<ResponseAnswerDto> answers = new ArrayList<>();
        private List<ResponseNoteDto> notes = new ArrayList<>();
        private List<ResponseTagDto> tags = new ArrayList<>();

        public ResponseDetailDtoBuilder id(UUID id) {
            this.id = id;
            return this;
        }
        public ResponseDetailDtoBuilder formId(UUID formId) {
            this.formId = formId;
            return this;
        }
        public ResponseDetailDtoBuilder formTitle(String formTitle) {
            this.formTitle = formTitle;
            return this;
        }
        public ResponseDetailDtoBuilder respondentIp(String respondentIp) {
            this.respondentIp = respondentIp;
            return this;
        }
        public ResponseDetailDtoBuilder userAgent(String userAgent) {
            this.userAgent = userAgent;
            return this;
        }
        public ResponseDetailDtoBuilder referrer(String referrer) {
            this.referrer = referrer;
            return this;
        }
        public ResponseDetailDtoBuilder durationSeconds(int durationSeconds) {
            this.durationSeconds = durationSeconds;
            return this;
        }
        public ResponseDetailDtoBuilder status(String status) {
            this.status = status;
            return this;
        }
        public ResponseDetailDtoBuilder isReviewed(boolean isReviewed) {
            this.isReviewed = isReviewed;
            return this;
        }
        public ResponseDetailDtoBuilder submittedAt(Instant submittedAt) {
            this.submittedAt = submittedAt;
            return this;
        }
        public ResponseDetailDtoBuilder answers(List<ResponseAnswerDto> answers) {
            this.answers = answers;
            return this;
        }
        public ResponseDetailDtoBuilder notes(List<ResponseNoteDto> notes) {
            this.notes = notes;
            return this;
        }
        public ResponseDetailDtoBuilder tags(List<ResponseTagDto> tags) {
            this.tags = tags;
            return this;
        }

        public ResponseDetailDto build() {
            ResponseDetailDto instance = new ResponseDetailDto();
            instance.id = this.id;
            instance.formId = this.formId;
            instance.formTitle = this.formTitle;
            instance.respondentIp = this.respondentIp;
            instance.userAgent = this.userAgent;
            instance.referrer = this.referrer;
            instance.durationSeconds = this.durationSeconds;
            instance.status = this.status;
            instance.isReviewed = this.isReviewed;
            instance.submittedAt = this.submittedAt;
            instance.answers = this.answers;
            instance.notes = this.notes;
            instance.tags = this.tags;
            return instance;
        }
    }
}

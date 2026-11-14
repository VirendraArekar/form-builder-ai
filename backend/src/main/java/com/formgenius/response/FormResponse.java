package com.formgenius.response;

import com.formgenius.form.Form;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "form_responses")
public class FormResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "form_id", nullable = false)
    private Form form;

    @Column(name = "respondent_ip")
    private String respondentIp;

    @Column(name = "user_agent", length = 500)
    private String userAgent;

    @Column
    private String referrer;

    @Column(name = "duration_seconds")
    private int durationSeconds = 0;

    @Column(nullable = false)
    private String status = "COMPLETED"; // COMPLETED, PARTIAL

    @Column(name = "is_reviewed", nullable = false)
    private boolean isReviewed = false;

    @CreationTimestamp
    @Column(name = "submitted_at", nullable = false, updatable = false)
    private Instant submittedAt;

    public FormResponse() {}

    public FormResponse(UUID id, Form form, String respondentIp, String userAgent, String referrer, int durationSeconds, String status, boolean isReviewed, Instant submittedAt) {
        this.id = id;
        this.form = form;
        this.respondentIp = respondentIp;
        this.userAgent = userAgent;
        this.referrer = referrer;
        this.durationSeconds = durationSeconds;
        this.status = status;
        this.isReviewed = isReviewed;
        this.submittedAt = submittedAt;
    }

    public UUID getId() {
        return id;
    }

    public Form getForm() {
        return form;
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

    public void setId(UUID id) {
        this.id = id;
    }

    public void setForm(Form form) {
        this.form = form;
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

    public static FormResponseBuilder builder() {
        return new FormResponseBuilder();
    }
    public static class FormResponseBuilder {
        private UUID id;
        private Form form;
        private String respondentIp;
        private String userAgent;
        private String referrer;
        private int durationSeconds = 0;
        private String status = "COMPLETED";
        private boolean isReviewed = false;
        private Instant submittedAt;

        public FormResponseBuilder id(UUID id) {
            this.id = id;
            return this;
        }
        public FormResponseBuilder form(Form form) {
            this.form = form;
            return this;
        }
        public FormResponseBuilder respondentIp(String respondentIp) {
            this.respondentIp = respondentIp;
            return this;
        }
        public FormResponseBuilder userAgent(String userAgent) {
            this.userAgent = userAgent;
            return this;
        }
        public FormResponseBuilder referrer(String referrer) {
            this.referrer = referrer;
            return this;
        }
        public FormResponseBuilder durationSeconds(int durationSeconds) {
            this.durationSeconds = durationSeconds;
            return this;
        }
        public FormResponseBuilder status(String status) {
            this.status = status;
            return this;
        }
        public FormResponseBuilder isReviewed(boolean isReviewed) {
            this.isReviewed = isReviewed;
            return this;
        }
        public FormResponseBuilder submittedAt(Instant submittedAt) {
            this.submittedAt = submittedAt;
            return this;
        }

        public FormResponse build() {
            FormResponse instance = new FormResponse();
            instance.id = this.id;
            instance.form = this.form;
            instance.respondentIp = this.respondentIp;
            instance.userAgent = this.userAgent;
            instance.referrer = this.referrer;
            instance.durationSeconds = this.durationSeconds;
            instance.status = this.status;
            instance.isReviewed = this.isReviewed;
            instance.submittedAt = this.submittedAt;
            return instance;
        }
    }
}

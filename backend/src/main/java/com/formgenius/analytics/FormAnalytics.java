package com.formgenius.analytics;

import com.formgenius.form.Form;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "form_analytics", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"form_id", "date"})
})
public class FormAnalytics {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "form_id", nullable = false)
    private Form form;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private int views = 0;

    @Column(nullable = false)
    private int starts = 0;

    @Column(nullable = false)
    private int submissions = 0;

    @Column(nullable = false)
    private int completions = 0;

    @Column(name = "average_duration_seconds", nullable = false)
    private int averageDurationSeconds = 0;

    public FormAnalytics() {}

    public FormAnalytics(UUID id, Form form, LocalDate date, int views, int starts, int submissions, int completions, int averageDurationSeconds) {
        this.id = id;
        this.form = form;
        this.date = date;
        this.views = views;
        this.starts = starts;
        this.submissions = submissions;
        this.completions = completions;
        this.averageDurationSeconds = averageDurationSeconds;
    }

    public UUID getId() {
        return id;
    }

    public Form getForm() {
        return form;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getViews() {
        return views;
    }

    public int getStarts() {
        return starts;
    }

    public int getSubmissions() {
        return submissions;
    }

    public int getCompletions() {
        return completions;
    }

    public int getAverageDurationSeconds() {
        return averageDurationSeconds;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setForm(Form form) {
        this.form = form;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public void setStarts(int starts) {
        this.starts = starts;
    }

    public void setSubmissions(int submissions) {
        this.submissions = submissions;
    }

    public void setCompletions(int completions) {
        this.completions = completions;
    }

    public void setAverageDurationSeconds(int averageDurationSeconds) {
        this.averageDurationSeconds = averageDurationSeconds;
    }

    public static FormAnalyticsBuilder builder() {
        return new FormAnalyticsBuilder();
    }
    public static class FormAnalyticsBuilder {
        private UUID id;
        private Form form;
        private LocalDate date;
        private int views = 0;
        private int starts = 0;
        private int submissions = 0;
        private int completions = 0;
        private int averageDurationSeconds = 0;

        public FormAnalyticsBuilder id(UUID id) {
            this.id = id;
            return this;
        }
        public FormAnalyticsBuilder form(Form form) {
            this.form = form;
            return this;
        }
        public FormAnalyticsBuilder date(LocalDate date) {
            this.date = date;
            return this;
        }
        public FormAnalyticsBuilder views(int views) {
            this.views = views;
            return this;
        }
        public FormAnalyticsBuilder starts(int starts) {
            this.starts = starts;
            return this;
        }
        public FormAnalyticsBuilder submissions(int submissions) {
            this.submissions = submissions;
            return this;
        }
        public FormAnalyticsBuilder completions(int completions) {
            this.completions = completions;
            return this;
        }
        public FormAnalyticsBuilder averageDurationSeconds(int averageDurationSeconds) {
            this.averageDurationSeconds = averageDurationSeconds;
            return this;
        }

        public FormAnalytics build() {
            FormAnalytics instance = new FormAnalytics();
            instance.id = this.id;
            instance.form = this.form;
            instance.date = this.date;
            instance.views = this.views;
            instance.starts = this.starts;
            instance.submissions = this.submissions;
            instance.completions = this.completions;
            instance.averageDurationSeconds = this.averageDurationSeconds;
            return instance;
        }
    }
}

package com.formgenius.analytics.dto;


import java.time.LocalDate;

public class DailyTrendDto {
    private LocalDate date;
    private int views;
    private int submissions;
    private int completions;

    public DailyTrendDto() {}

    public DailyTrendDto(LocalDate date, int views, int submissions, int completions) {
        this.date = date;
        this.views = views;
        this.submissions = submissions;
        this.completions = completions;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getViews() {
        return views;
    }

    public int getSubmissions() {
        return submissions;
    }

    public int getCompletions() {
        return completions;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public void setSubmissions(int submissions) {
        this.submissions = submissions;
    }

    public void setCompletions(int completions) {
        this.completions = completions;
    }

    public static DailyTrendDtoBuilder builder() {
        return new DailyTrendDtoBuilder();
    }
    public static class DailyTrendDtoBuilder {
        private LocalDate date;
        private int views;
        private int submissions;
        private int completions;

        public DailyTrendDtoBuilder date(LocalDate date) {
            this.date = date;
            return this;
        }
        public DailyTrendDtoBuilder views(int views) {
            this.views = views;
            return this;
        }
        public DailyTrendDtoBuilder submissions(int submissions) {
            this.submissions = submissions;
            return this;
        }
        public DailyTrendDtoBuilder completions(int completions) {
            this.completions = completions;
            return this;
        }

        public DailyTrendDto build() {
            DailyTrendDto instance = new DailyTrendDto();
            instance.date = this.date;
            instance.views = this.views;
            instance.submissions = this.submissions;
            instance.completions = this.completions;
            return instance;
        }
    }
}

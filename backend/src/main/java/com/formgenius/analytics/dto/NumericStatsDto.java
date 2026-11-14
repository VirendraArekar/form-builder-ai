package com.formgenius.analytics.dto;


public class NumericStatsDto {
    private double average;
    private double min;
    private double max;
    private double median;

    public NumericStatsDto() {}

    public NumericStatsDto(double average, double min, double max, double median) {
        this.average = average;
        this.min = min;
        this.max = max;
        this.median = median;
    }

    public double getAverage() {
        return average;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public double getMedian() {
        return median;
    }

    public void setAverage(double average) {
        this.average = average;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public void setMedian(double median) {
        this.median = median;
    }

    public static NumericStatsDtoBuilder builder() {
        return new NumericStatsDtoBuilder();
    }
    public static class NumericStatsDtoBuilder {
        private double average;
        private double min;
        private double max;
        private double median;

        public NumericStatsDtoBuilder average(double average) {
            this.average = average;
            return this;
        }
        public NumericStatsDtoBuilder min(double min) {
            this.min = min;
            return this;
        }
        public NumericStatsDtoBuilder max(double max) {
            this.max = max;
            return this;
        }
        public NumericStatsDtoBuilder median(double median) {
            this.median = median;
            return this;
        }

        public NumericStatsDto build() {
            NumericStatsDto instance = new NumericStatsDto();
            instance.average = this.average;
            instance.min = this.min;
            instance.max = this.max;
            instance.median = this.median;
            return instance;
        }
    }
}

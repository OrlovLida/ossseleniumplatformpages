package com.oss.pages.bpm.forecasts;

import java.util.Optional;

/**
 * @author Paweł Rother
 */
public class Forecast {
    private final String name;
    private final Long startPlusDays;
    private final Long endPlusDaysShortWay;
    private final Long endPlusDaysLongWay;
    private final Boolean longWorkWeakShortWay;
    private final Boolean longWorkWeakLongWay;

    Forecast(ForecastBuilder builder) {
        this.name = builder.name;
        this.startPlusDays = builder.startPlusDays;
        this.endPlusDaysShortWay = builder.endPlusDaysShortWay;
        this.endPlusDaysLongWay = builder.endPlusDaysLongWay;
        this.longWorkWeakShortWay = builder.longWorkWeakShortWay;
        this.longWorkWeakLongWay = builder.longWorkWeakLongWay;
    }

    public static ForecastBuilder builder() {
        return new ForecastBuilder();
    }

    public Optional<String> getName() {
        return Optional.ofNullable(this.name);
    }

    public Optional<Long> getStartPlusDays() {
        return Optional.ofNullable(this.startPlusDays);
    }

    public Optional<Long> getEndPlusDaysShortWay() {
        return Optional.ofNullable(this.endPlusDaysShortWay);
    }

    public Optional<Long> getEndPlusDaysLongWay() {
        return Optional.ofNullable(this.endPlusDaysLongWay);
    }

    public Optional<Boolean> getLongWorkWeakShortWay() {
        return Optional.ofNullable(this.longWorkWeakShortWay);
    }

    public Optional<Boolean> getLongWorkWeakLongWay() {
        return Optional.ofNullable(this.longWorkWeakLongWay);
    }

    public static class ForecastBuilder {
        private String name;
        private Long startPlusDays;
        private Long endPlusDaysShortWay;
        private Long endPlusDaysLongWay;
        private Boolean longWorkWeakShortWay;
        private Boolean longWorkWeakLongWay;

        ForecastBuilder() {
        }

        public ForecastBuilder name(String name) {
            this.name = name;
            return this;
        }

        public ForecastBuilder startPlusDays(Long startPlusDays) {
            this.startPlusDays = startPlusDays;
            return this;
        }

        public ForecastBuilder endPlusDaysShortWay(Long endPlusDaysShortWay) {
            this.endPlusDaysShortWay = endPlusDaysShortWay;
            return this;
        }

        public ForecastBuilder endPlusDaysLongWay(Long endPlusDaysLongWay) {
            this.endPlusDaysLongWay = endPlusDaysLongWay;
            return this;
        }

        public ForecastBuilder longWorkWeakShortWay(Boolean longWorkWeakShortWay) {
            this.longWorkWeakShortWay = longWorkWeakShortWay;
            return this;
        }

        public ForecastBuilder longWorkWeakLongWay(Boolean longWorkWeakLongWay) {
            this.longWorkWeakLongWay = longWorkWeakLongWay;
            return this;
        }

        public Forecast build() {
            return new Forecast(this);
        }

        public String toString() {
            return "Forecast.ForecastBuilder(name=" + this.name + ", startPlusDays=" + this.startPlusDays + ", endPlusDaysShortWay=" + this.endPlusDaysShortWay + ", endPlusDaysLongWay=" + this.endPlusDaysLongWay + ", longWorkWeakShortWay=" + this.longWorkWeakShortWay + ", longWorkWeakLongWay=" + this.longWorkWeakLongWay + ")";
        }
    }
}

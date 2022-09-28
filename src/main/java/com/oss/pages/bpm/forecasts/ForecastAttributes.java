package com.oss.pages.bpm.forecasts;

import lombok.Builder;
import lombok.Getter;

import java.util.Optional;

@Builder
@Getter
public class ForecastAttributes {
    private final Optional<String> name;
    private final Optional<String> activityType;
    private final Optional<String> startDate;
    private final Optional<String> startWeek;
    private final Optional<String> endDateShortWay;
    private final Optional<String> endDateLongWay;
    private final Optional<String> endWeekShortWay;
    private final Optional<String> endWeekLongWay;
    private final Optional<String> leadTimeDaysShortWay;
    private final Optional<String> leadTimeWeeksShortWay;
    private final Optional<String> leadTimeDaysLongWay;
    private final Optional<String> leadTimeWeeksLongWay;
    private final Optional<String> workDaysShortWay;
    private final Optional<String> workDaysLongWay;
}

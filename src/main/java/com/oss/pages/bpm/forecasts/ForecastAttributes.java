package com.oss.pages.bpm.forecasts;

import lombok.Builder;
import lombok.Getter;

/**
 * @author Paweł Rother
 */
@Builder
@Getter
public class ForecastAttributes {
    private final String name;
    private final String activityType;
    private final String startDate;
    private final String startWeek;
    private final String endDateShortWay;
    private final String endDateLongWay;
    private final String endWeekShortWay;
    private final String endWeekLongWay;
    private final String leadTimeDaysShortWay;
    private final String leadTimeWeeksShortWay;
    private final String leadTimeDaysLongWay;
    private final String leadTimeWeeksLongWay;
    private final String workDaysShortWay;
    private final String workDaysLongWay;
}

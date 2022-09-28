package com.oss.pages.bpm.processinstances;

import lombok.Builder;
import lombok.Getter;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.Month;
import java.util.Optional;
import java.util.Set;

@Builder
@Getter
public class ScheduleProperties {
    private final String scheduleType;
    private final LocalTime time;
    private final Optional<Long> plusDays;
    private final Optional<Integer> repeatDaysCycle;
    private final Optional<Set<DayOfWeek>> repeatDayOfWeek;
    private final Optional<Integer> repeatDay;
    private final Optional<Integer> repeatMonthsCycle;
    private final Optional<Integer> repeatYearsCycle;
    private final Optional<Month> repeatMonth;
}

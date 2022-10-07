package com.oss.pages.bpm.processinstances;

import lombok.Getter;

import java.time.DayOfWeek;
import java.time.Month;
import java.util.Optional;
import java.util.Set;

/**
 * @author Paweł Rother
 */
@Getter
public class ScheduleProperties {
    protected static final String SINGLE_SCHEDULE = "Single";
    protected static final String DAILY_SCHEDULE = "Daily";
    protected static final String WEEKLY_SCHEDULE = "Weekly";
    protected static final String MONTHLY_SCHEDULE = "Monthly";
    protected static final String YEARLY_SCHEDULE = "Yearly";

    private final String scheduleType;
    private final int plusMinutes;
    private final Optional<Long> plusDays;
    private final Optional<Integer> repeatDaysCycle;
    private final Optional<Set<DayOfWeek>> repeatDayOfWeek;
    private final Optional<Integer> repeatDay;
    private final Optional<Integer> repeatMonthsCycle;
    private final Optional<Integer> repeatYearsCycle;
    private final Optional<Month> repeatMonth;
    private final int settingsAmount;

    ScheduleProperties(SchedulePropertiesBuilder builder) {
        this.scheduleType = builder.scheduleType;
        this.plusMinutes = builder.plusMinutes;
        this.plusDays = builder.plusDays;
        this.repeatDaysCycle = builder.repeatDaysCycle;
        this.repeatDayOfWeek = builder.repeatDayOfWeek;
        this.repeatDay = builder.repeatDay;
        this.repeatMonthsCycle = builder.repeatMonthsCycle;
        this.repeatYearsCycle = builder.repeatYearsCycle;
        this.repeatMonth = builder.repeatMonth;
        this.settingsAmount = builder.settingsAmount;
    }

    public static SchedulePropertiesBuilder builder() {
        return new SchedulePropertiesBuilder();
    }

    public static class SchedulePropertiesBuilder {

        private String scheduleType;
        private int plusMinutes;
        private Optional<Long> plusDays;
        private Optional<Integer> repeatDaysCycle;
        private Optional<Set<DayOfWeek>> repeatDayOfWeek;
        private Optional<Integer> repeatDay;
        private Optional<Integer> repeatMonthsCycle;
        private Optional<Integer> repeatYearsCycle;
        private Optional<Month> repeatMonth;
        private int settingsAmount = 0;

        SchedulePropertiesBuilder() {
        }

        public SchedulePropertiesBuilder setSingleSchedule(Long plusDaysForDate, int plusMinutes) {
            this.settingsAmount++;
            this.scheduleType = SINGLE_SCHEDULE;
            this.plusDays = Optional.ofNullable(plusDaysForDate);
            this.plusMinutes = plusMinutes;
            return this;
        }

        public SchedulePropertiesBuilder setDailySchedule(int repeatDaysCycle, int plusMinutes) {
            this.settingsAmount++;
            this.scheduleType = DAILY_SCHEDULE;
            this.repeatDaysCycle = Optional.of(repeatDaysCycle);
            this.plusMinutes = plusMinutes;
            return this;
        }

        public SchedulePropertiesBuilder setWeeklySchedule(Set<DayOfWeek> repeatDayOfWeek, int plusMinutes) {
            this.settingsAmount++;
            this.scheduleType = WEEKLY_SCHEDULE;
            this.repeatDayOfWeek = Optional.ofNullable(repeatDayOfWeek);
            this.plusMinutes = plusMinutes;
            return this;
        }

        public SchedulePropertiesBuilder setMonthlySchedule(int repeatMonthsCycle, int repeatDay, int plusMinutes) {
            this.settingsAmount++;
            this.scheduleType = MONTHLY_SCHEDULE;
            this.repeatMonthsCycle = Optional.of(repeatMonthsCycle);
            this.repeatDay = Optional.of(repeatDay);
            this.plusMinutes = plusMinutes;
            return this;
        }

        public SchedulePropertiesBuilder setYearlySchedule(int repeatYearsCycle, Month repeatMonth, int repeatDay, int plusMinutes) {
            this.settingsAmount++;
            this.scheduleType = YEARLY_SCHEDULE;
            this.repeatYearsCycle = Optional.of(repeatYearsCycle);
            this.repeatMonth = Optional.ofNullable(repeatMonth);
            this.repeatDay = Optional.of(repeatDay);
            this.plusMinutes = plusMinutes;
            return this;
        }

        public ScheduleProperties build() {
            return new ScheduleProperties(this);
        }
    }

}

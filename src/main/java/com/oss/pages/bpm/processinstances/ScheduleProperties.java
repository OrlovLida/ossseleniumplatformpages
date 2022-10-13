package com.oss.pages.bpm.processinstances;

import java.time.DayOfWeek;
import java.time.Month;
import java.util.Optional;
import java.util.Set;

/**
 * @author Paweł Rother
 */
public class ScheduleProperties {
    protected static final String SINGLE_SCHEDULE = "Single";
    protected static final String DAILY_SCHEDULE = "Daily";
    protected static final String WEEKLY_SCHEDULE = "Weekly";
    protected static final String MONTHLY_SCHEDULE = "Monthly";
    protected static final String YEARLY_SCHEDULE = "Yearly";

    private final String scheduleType;
    private final int plusMinutes;
    private final long plusDays;
    private final int repeatDaysCycle;
    private final Set<DayOfWeek> repeatDayOfWeek;
    private final int repeatDay;
    private final int repeatMonthsCycle;
    private final int repeatYearsCycle;
    private final Month repeatMonth;
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

    public String getScheduleType() {
        return this.scheduleType;
    }

    public int getPlusMinutes() {
        return this.plusMinutes;
    }

    public Optional<Long> getPlusDays() {
        return Optional.of(this.plusDays);
    }

    public Optional<Integer> getRepeatDaysCycle() {
        return Optional.of(this.repeatDaysCycle);
    }

    public Optional<Set<DayOfWeek>> getRepeatDayOfWeek() {
        return Optional.ofNullable(this.repeatDayOfWeek);
    }

    public Optional<Integer> getRepeatDay() {
        return Optional.of(this.repeatDay);
    }

    public Optional<Integer> getRepeatMonthsCycle() {
        return Optional.of(this.repeatMonthsCycle);
    }

    public Optional<Integer> getRepeatYearsCycle() {
        return Optional.of(this.repeatYearsCycle);
    }

    public Optional<Month> getRepeatMonth() {
        return Optional.ofNullable(this.repeatMonth);
    }

    public int getSettingsAmount() {
        return this.settingsAmount;
    }

    public static class SchedulePropertiesBuilder {

        private String scheduleType;
        private int plusMinutes;
        private long plusDays;
        private int repeatDaysCycle;
        private Set<DayOfWeek> repeatDayOfWeek;
        private int repeatDay;
        private int repeatMonthsCycle;
        private int repeatYearsCycle;
        private Month repeatMonth;
        private int settingsAmount = 0;

        SchedulePropertiesBuilder() {
        }

        public SchedulePropertiesBuilder setSingleSchedule(Long plusDaysForDate, int plusMinutes) {
            this.settingsAmount++;
            this.scheduleType = SINGLE_SCHEDULE;
            this.plusDays = plusDaysForDate;
            this.plusMinutes = plusMinutes;
            return this;
        }

        public SchedulePropertiesBuilder setDailySchedule(int repeatDaysCycle, int plusMinutes) {
            this.settingsAmount++;
            this.scheduleType = DAILY_SCHEDULE;
            this.repeatDaysCycle = repeatDaysCycle;
            this.plusMinutes = plusMinutes;
            return this;
        }

        public SchedulePropertiesBuilder setWeeklySchedule(Set<DayOfWeek> repeatDayOfWeek, int plusMinutes) {
            this.settingsAmount++;
            this.scheduleType = WEEKLY_SCHEDULE;
            this.repeatDayOfWeek = repeatDayOfWeek;
            this.plusMinutes = plusMinutes;
            return this;
        }

        public SchedulePropertiesBuilder setMonthlySchedule(int repeatMonthsCycle, int repeatDay, int plusMinutes) {
            this.settingsAmount++;
            this.scheduleType = MONTHLY_SCHEDULE;
            this.repeatMonthsCycle = repeatMonthsCycle;
            this.repeatDay = repeatDay;
            this.plusMinutes = plusMinutes;
            return this;
        }

        public SchedulePropertiesBuilder setYearlySchedule(int repeatYearsCycle, Month repeatMonth, int repeatDay, int plusMinutes) {
            this.settingsAmount++;
            this.scheduleType = YEARLY_SCHEDULE;
            this.repeatYearsCycle = repeatYearsCycle;
            this.repeatMonth = repeatMonth;
            this.repeatDay = repeatDay;
            this.plusMinutes = plusMinutes;
            return this;
        }

        public ScheduleProperties build() {
            return new ScheduleProperties(this);
        }
    }

}

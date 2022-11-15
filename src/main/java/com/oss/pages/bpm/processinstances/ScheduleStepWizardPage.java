package com.oss.pages.bpm.processinstances;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.wizard.Wizard;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;

import static com.oss.pages.bpm.processinstances.ScheduleProperties.*;

/**
 * @author Paweł Rother
 */
public class ScheduleStepWizardPage extends ProcessWizardPage {
    private static final String TIME_PATTERN = "HH:mm";
    private static final String CRON_EXPRESSION_INPUT = "sheduleCronExpressionFieldId";
    private static final String CRON_EXPRESSION_DESCRIPTION_INPUT_ID = "cronExpressionDescriptionId";
    private static final String NEXT_EXECUTIONS_INPUT_ID = "nextExecutionsId";
    private static final String SCHEDULE_TYPE_INPUT_ID = "scheduleTypesComboboxFieldId_scheduleType";

    private static final String SCHEDULE_SINGLE_DATE_INPUT_ID = "dateSingle";
    private static final String SCHEDULE_SINGLE_TIME_INPUT_ID = "timeSingle";

    private static final String SCHEDULE_DAILY_REPEAT_DAYS_CYCLE_INPUT_ID = "occurrenceInputDaily";
    private static final String SCHEDULE_DAILY_TIME_INPUT_ID = "timeInput";

    private static final String SCHEDULE_WEEKLY_TIME_INPUT_ID = "timeInputWeekly";
    private static final String SCHEDULE_WEEKLY_MONDAY_BUTTON_ID = "schedulerWeekly-Mon";
    private static final String SCHEDULE_WEEKLY_TUESDAY_BUTTON_ID = "schedulerWeekly-Tue";
    private static final String SCHEDULE_WEEKLY_WEDNESDAY_BUTTON_ID = "schedulerWeekly-Wed";
    private static final String SCHEDULE_WEEKLY_THURSDAY_BUTTON_ID = "schedulerWeekly-Thu";
    private static final String SCHEDULE_WEEKLY_FRIDAY_BUTTON_ID = "schedulerWeekly-Fri";
    private static final String SCHEDULE_WEEKLY_SATURDAY_BUTTON_ID = "schedulerWeekly-Sat";
    private static final String SCHEDULE_WEEKLY_SUNDAY_BUTTON_ID = "schedulerWeekly-Sun";

    private static final String SCHEDULE_MONTHLY_TIME_INPUT_ID = "timeInput";
    private static final String SCHEDULE_MONTHLY_REPEAT_MONTHS_CYCLE_INPUT_ID = "occurrenceInputMonthly";
    private static final String SCHEDULE_MONTHLY_REPEAT_DAY_INPUT_ID = "comboDayMonthly";

    private static final String SCHEDULE_YEARLY_TIME_INPUT_ID = "timeInput";
    private static final String SCHEDULE_YEARLY_REPEAT_DAY_INPUT_ID = "comboDayYearly";
    private static final String SCHEDULE_YEARLY_REPEAT_MONTH_INPUT_ID = "comboMonthYearly";
    private static final String SCHEDULE_YEARLY_REPEAT_YEARS_CYCLE_INPUT_ID = "occurrenceInput";

    private static final String NO_ELEMENT_EXCEPTION = "There is no element with id = {}";
    private static final String WRONG_DAY_OF_THE_WEEK_ARGUMENT = "Provided day of the week is not valid.";
    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleStepWizardPage.class);
    private static final String ADDING_SCHEDULE_INFO = "Adding Schedule";

    private final Wizard wizard = Wizard.createByComponentId(driver, wait, PROCESS_WIZARD_STEP_2);

    public ScheduleStepWizardPage(WebDriver driver) {
        super(driver);
    }

    public ScheduleStepWizardPage setCRONExpression(String cronExpression) {
        LOGGER.info(ADDING_SCHEDULE_INFO);
        setInputValue(CRON_EXPRESSION_INPUT, cronExpression);
        return this;
    }

    public String getCronExpression() {
        return getInputValue(CRON_EXPRESSION_INPUT);
    }

    public String getCronExpressionDescription() {
        return getInputValue(CRON_EXPRESSION_DESCRIPTION_INPUT_ID);
    }

    public List<String> getNextExecutions() {
        Input component = getInputComponent(NEXT_EXECUTIONS_INPUT_ID);
        return component.getStringValues();
    }

    public ScheduleStepWizardPage setSchedule(ScheduleProperties scheduleProperties) {
        LOGGER.info(ADDING_SCHEDULE_INFO);

        setInputValue(SCHEDULE_TYPE_INPUT_ID, scheduleProperties.getScheduleType());
        if (scheduleProperties.getScheduleType().equals(SINGLE_SCHEDULE)) {

            scheduleProperties.getPlusDays().ifPresent(plusDays ->
                    setInputValue(SCHEDULE_SINGLE_DATE_INPUT_ID, LocalDate.now().plusDays(plusDays).toString()));

            LocalTime time = LocalTime.now().plusMinutes(scheduleProperties.getPlusMinutes());
            setInputValue(SCHEDULE_SINGLE_TIME_INPUT_ID, time.format(DateTimeFormatter.ofPattern(TIME_PATTERN)));

        } else if (scheduleProperties.getScheduleType().equals(DAILY_SCHEDULE)) {

            scheduleProperties.getRepeatDaysCycle().ifPresent(repeatDaysCycle ->
                    setInputValue(SCHEDULE_DAILY_REPEAT_DAYS_CYCLE_INPUT_ID, String.valueOf(repeatDaysCycle)));

            LocalTime time = LocalTime.now().plusMinutes(scheduleProperties.getPlusMinutes());
            setInputValue(SCHEDULE_DAILY_TIME_INPUT_ID, time.format(DateTimeFormatter.ofPattern(TIME_PATTERN)));

        } else if (scheduleProperties.getScheduleType().equals(WEEKLY_SCHEDULE)) {

            scheduleProperties.getRepeatDayOfWeek().ifPresent(repeatDayOfWeek -> {
                for (DayOfWeek dayOfTheWeek : repeatDayOfWeek) {
                    chooseDayOfTheWeek(dayOfTheWeek);
                }
            });

            LocalTime time = LocalTime.now().plusMinutes(scheduleProperties.getPlusMinutes());
            setInputValue(SCHEDULE_WEEKLY_TIME_INPUT_ID, time.format(DateTimeFormatter.ofPattern(TIME_PATTERN)));

        } else if (scheduleProperties.getScheduleType().equals(MONTHLY_SCHEDULE)) {

            scheduleProperties.getRepeatDay().ifPresent(repeatDay ->
                    setInputValue(SCHEDULE_MONTHLY_REPEAT_DAY_INPUT_ID, String.valueOf(repeatDay)));

            scheduleProperties.getRepeatMonthsCycle().ifPresent(repeatMonthsCycle ->
                    setInputValue(SCHEDULE_MONTHLY_REPEAT_MONTHS_CYCLE_INPUT_ID, String.valueOf(repeatMonthsCycle)));

            LocalTime time = LocalTime.now().plusMinutes(scheduleProperties.getPlusMinutes());
            setInputValue(SCHEDULE_MONTHLY_TIME_INPUT_ID, time.format(DateTimeFormatter.ofPattern(TIME_PATTERN)));

        } else if (scheduleProperties.getScheduleType().equals(YEARLY_SCHEDULE)) {

            scheduleProperties.getRepeatYearsCycle().ifPresent(repeatYearsCycle ->
                    setInputValue(SCHEDULE_YEARLY_REPEAT_YEARS_CYCLE_INPUT_ID, String.valueOf(repeatYearsCycle)));

            scheduleProperties.getRepeatMonth().ifPresent(repeatMonth ->
                    setInputValue(SCHEDULE_YEARLY_REPEAT_MONTH_INPUT_ID, String.valueOf(repeatMonth)));

            scheduleProperties.getRepeatDay().ifPresent(repeatDay ->
                    setInputValue(SCHEDULE_YEARLY_REPEAT_DAY_INPUT_ID, String.valueOf(repeatDay)));

            LocalTime time = LocalTime.now().plusMinutes(scheduleProperties.getPlusMinutes());
            setInputValue(SCHEDULE_YEARLY_TIME_INPUT_ID, time.format(DateTimeFormatter.ofPattern(TIME_PATTERN)));
        }
        return this;
    }


    private void chooseDayOfTheWeek(DayOfWeek dayOfTheWeek) {
        if (dayOfTheWeek.equals(DayOfWeek.MONDAY))
            wizard.clickButtonById(SCHEDULE_WEEKLY_MONDAY_BUTTON_ID);
        else if (dayOfTheWeek.equals(DayOfWeek.TUESDAY))
            wizard.clickButtonById(SCHEDULE_WEEKLY_TUESDAY_BUTTON_ID);
        else if (dayOfTheWeek.equals(DayOfWeek.WEDNESDAY))
            wizard.clickButtonById(SCHEDULE_WEEKLY_WEDNESDAY_BUTTON_ID);
        else if (dayOfTheWeek.equals(DayOfWeek.THURSDAY))
            wizard.clickButtonById(SCHEDULE_WEEKLY_THURSDAY_BUTTON_ID);
        else if (dayOfTheWeek.equals(DayOfWeek.FRIDAY))
            wizard.clickButtonById(SCHEDULE_WEEKLY_FRIDAY_BUTTON_ID);
        else if (dayOfTheWeek.equals(DayOfWeek.SATURDAY))
            wizard.clickButtonById(SCHEDULE_WEEKLY_SATURDAY_BUTTON_ID);
        else if (dayOfTheWeek.equals(DayOfWeek.SUNDAY))
            wizard.clickButtonById(SCHEDULE_WEEKLY_SUNDAY_BUTTON_ID);
        else
            throw new IllegalArgumentException(WRONG_DAY_OF_THE_WEEK_ARGUMENT);
        waitForPageToLoad();
    }

    private String getInputValue(String inputComponentId) {
        Input component = getInputComponent(inputComponentId);
        return component.getStringValue();
    }

    private void setInputValue(String inputComponentId, String value) {
        Input component = getInputComponent(inputComponentId);
        component.setSingleStringValue(value);
        waitForPageToLoad();
    }

    private Input getInputComponent(String componentId) {
        Input component;
        try {
            component = wizard.getComponent(componentId);
        } catch (NoSuchElementException e) {
            LOGGER.error(NO_ELEMENT_EXCEPTION, componentId);
            throw e;
        }
        return component;
    }

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, wait);
    }
}

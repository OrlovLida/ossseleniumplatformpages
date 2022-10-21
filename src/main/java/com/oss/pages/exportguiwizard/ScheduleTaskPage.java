package com.oss.pages.exportguiwizard;

import java.time.LocalDate;
import java.time.LocalTime;

import org.openqa.selenium.WebDriver;

import com.oss.framework.utils.DelayUtils;

import io.qameta.allure.Step;

public class ScheduleTaskPage extends ExportGuiWizardPage {

    public ScheduleTaskPage(WebDriver driver) {
        super(driver);
    }

    private static final String TASK_NAME_ID = "exportgui-components-scheduledtasknametxt";
    private static final String TIME_SINGLE_ID = "timeSingle";
    private static final String COMBO_MONTH_ID = "comboMonthYearly";
    private static final String COMBO_DAY_MONTH_ID = "comboDayMonthly";
    private static final String COMBO_DAY_YEAR_ID = "comboDayYearly";

    @Step("Choose XLS File Type")
    public ScheduleTaskPage typeTaskName(String taskName) {
        setTextValue(TASK_NAME_ID, taskName);
        return this;
    }

    @Step("Choose Single Schedule")
    public ScheduleTaskPage chooseSingleSchedule() {
        setTypeOfSchedule("Single");
        return this;
    }

    @Step("Choose Daily Schedule")
    public ScheduleTaskPage chooseDailySchedule() {
        setTypeOfSchedule("Daily");
        return this;
    }

    @Step("Choose Weekly Schedule")
    public ScheduleTaskPage chooseWeeklySchedule() {
        setTypeOfSchedule("Weekly");
        return this;
    }

    @Step("Choose Monthly Schedule")
    public ScheduleTaskPage chooseMonthlySchedule() {
        setTypeOfSchedule("Monthly");
        return this;
    }

    @Step("Choose Yearly Schedule")
    public ScheduleTaskPage chooseYearlySchedule() {
        setTypeOfSchedule("Yearly");
        return this;
    }

    @Step("Choose day of repeat")
    public ScheduleTaskPage repeatEveryWeekOn(String dayOfWeekLabel) {
        clickOnDayButton(dayOfWeekLabel);
        return this;
    }

    @Step("Type number of months to repeat")
    public ScheduleTaskPage repeatEveryDayMonthly(String howManyDays) {
        setComboboxValue(COMBO_DAY_MONTH_ID, howManyDays);
        return this;
    }

    @Step("Choose number of years to repeat")
    public ScheduleTaskPage repeatEveryDayYearly(String howManyDays) {
        setComboboxValue(COMBO_DAY_YEAR_ID, howManyDays);
        return this;
    }

    @Step("Chose month to repeat ")
    public ScheduleTaskPage repeatEveryMonth(String monthName) {
        setComboboxValue(COMBO_MONTH_ID, (monthName));
        return this;
    }

    @Step("Set Actual Time By Clicking two times on the calendar")
    public ScheduleTaskPage setActualDate() {
        getWizard().setComponentValue("dateSingle", LocalDate.now().toString());
        return this;
    }

    @Step("Set Actual Time By Clicking two times on the clock")
    public ScheduleTaskPage setActualTime(String inputId) {
        DelayUtils.waitForPageToLoad(driver, wait);
        setTime(inputId, LocalTime.now().toString());

        return this;
    }

    @Step("Set Time")
    public ScheduleTaskPage setTime(String inputId, String HHMM) {
        getWizard().setComponentValue(inputId, HHMM);
        return this;
    }

    private void clickOnDayButton(String dayOfWeekButtonLabel) {
        getWizard().clickButtonById("schedulerWeekly-" + dayOfWeekButtonLabel);
    }

}
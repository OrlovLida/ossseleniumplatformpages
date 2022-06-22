package com.oss.pages.exportguiwizard;

import com.oss.framework.utils.DelayUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class ScheduleTaskPage extends ExportGuiWizardPage{

    public ScheduleTaskPage(WebDriver driver){super(driver);}

    private static final String TASK_NAME_ID = "exportgui-components-scheduledtasknametxt";
    private static final String TYPE_OF_SCHEDULE_ID = "exportgui-components-scheduledcronexprtxt";
    private static final String TIME_SINGLE_ID = "timeSingle";
    private static final String COMBO_MONTH_ID = "comboMonthYearly";
    private static final String COMBO_DAY_MONTH_ID = "comboDayMonthly";
    private static final String COMBO_DAY_YEAR_ID = "comboDayYearly";

    @Step("Choose XLS File Type")
    public ScheduleTaskPage typeTaskName(String taskName){
        setTextValue(TASK_NAME_ID, taskName);
    return this;
    }

    @Step("Choose Single Schedule")
    public ScheduleTaskPage chooseSingleSchedule(){
        setComboboxValue(TYPE_OF_SCHEDULE_ID, "Single");
        return this;
    }

    @Step("Choose Daily Schedule")
    public ScheduleTaskPage chooseDailySchedule(){
        setComboboxValue(TYPE_OF_SCHEDULE_ID, "Daily");
        return this;
    }

    @Step("Choose Weekly Schedule")
    public ScheduleTaskPage chooseWeeklySchedule(){
        setComboboxValue(TYPE_OF_SCHEDULE_ID, "Weekly");
        return this;
    }

    @Step("Choose Monthly Schedule")
    public ScheduleTaskPage chooseMonthlySchedule(){
        setComboboxValue(TYPE_OF_SCHEDULE_ID, "Monthly");
        return this;
    }

    @Step("Choose Yearly Schedule")
    public ScheduleTaskPage chooseYearlySchedule(){
        setComboboxValue(TYPE_OF_SCHEDULE_ID, "Yearly");
        return this;
    }

    @Step("Choose day of repeat")
    public ScheduleTaskPage repeatEveryWeekOn(int dayOfWeek){
        clickOnDayButton(dayOfWeek);
        return this;
    }

    @Step("Type number of months to repeat")
    public ScheduleTaskPage repeatEveryDayMonthly(String howManyDays){
        setComboboxValue(COMBO_DAY_MONTH_ID,howManyDays);
        return this;
    }

    @Step("Choose number of years to repeat")
    public ScheduleTaskPage repeatEveryDayYearly(String howManyDays){
        setComboboxValue(COMBO_DAY_YEAR_ID,howManyDays);
        return this;
    }

    @Step("Chose month to repeat ")
    public ScheduleTaskPage repeatEveryMonth(String monthName){
        setComboboxValue(COMBO_MONTH_ID,(monthName));
        return this;
    }

    @Step("Set Actual Time By Clicking two times on the calendar")
    public ScheduleTaskPage setActualDate(){
        clickCalendar();
        clickCalendar();
        return this;
    }

    @Step("Set Actual Time By Clicking two times on the clock")
    public ScheduleTaskPage setActualTime(){
        DelayUtils.waitForPageToLoad(driver, wait);
        clickTime();
        clickTime();
        return this;
    }

    @Step("Set Time")
    public ScheduleTaskPage setTime (String HHMM){
        setTextValue(TIME_SINGLE_ID, HHMM);
        return this;
    }

    private void clickTime(){
        driver.findElement(By.xpath(".//i[@class='OSSIcon fa fa-clock-o']")).click();
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    private void clickCalendar(){
        driver.findElement(By.xpath(".//i[@class='OSSIcon fa fa-calendar']")).click();
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    private void clickOnDayButton(int dayOfWeek){
        List<WebElement> dayButtons = driver.findElements(By.xpath("//button[@class = 'day-button']"));
        dayButtons.get(dayOfWeek-1).click();
    }

}
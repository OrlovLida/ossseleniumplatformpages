package com.oss.pages.exportguiwizard;

import com.oss.framework.components.DateTime;
import com.oss.framework.data.Data;
import com.oss.framework.utils.DelayUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class ScheduleTaskPage extends ExportGuiWizardPage{

    public ScheduleTaskPage(WebDriver driver){super(driver);}

    private String TASK_NAME_ID = "exportgui-components-scheduledtasknametxt";
    private String TYPE_OF_SCHEDULE_ID = "schedulerInput";
    private String DATA_SINGLE_ID = "dateSingle ";
    private String TIME_SINGLE_ID = "timeSingle";
    private String OCCURRENCE_INPUT_DAILY_SINGLE_ID = "occurrenceInputDaily";
    private String TIME_INPUT_ID = "timeInput";
    private String COMBO_MONTH_ID = "comboMonth";
    private String COMBO_DAY_ID = "comboDay";
    private String DAY_BUTTON_CONTAINER_ID= "dayButtonContainer";

    public ScheduleTaskPage typeTaskName(String taskName){
        setValueOnTextField(TASK_NAME_ID, Data.createSingleData(taskName));
    return this;
    }

    public ScheduleTaskPage chooseSingleSchedule(){
        setValueOnCombobox(TYPE_OF_SCHEDULE_ID, "Single");
        return this;
    }

    public ScheduleTaskPage chooseDailySchedule(){
        setValueOnCombobox(TYPE_OF_SCHEDULE_ID, "Daily");
        return this;
    }

    public ScheduleTaskPage chooseWeeklySchedule(){
        setValueOnCombobox(TYPE_OF_SCHEDULE_ID, "Weekly");
        return this;
    }

    public ScheduleTaskPage chooseMonthlySchedule(){
        setValueOnCombobox(TYPE_OF_SCHEDULE_ID, "Monthly");
        return this;
    }

    public ScheduleTaskPage chooseYearlySchedule(){
        setValueOnCombobox(TYPE_OF_SCHEDULE_ID, "Yearly");
        return this;
    }

    public ScheduleTaskPage repeatEveryWeekOn(int dayOfWeek){
        clickOnDayButton(dayOfWeek);
        return this;
    }

    public ScheduleTaskPage repeatEveryDay(String howManyDays){
        setValueOnCombobox(COMBO_DAY_ID,howManyDays);
        return this;
    }

    public ScheduleTaskPage repeatEveryMonth(String monthName){
        setValueOnCombobox(COMBO_MONTH_ID,(monthName));
        return this;
    }

    public ScheduleTaskPage setActualDate(){
        clickCalendar();
        clickCalendar();
        return this;
    }

    public ScheduleTaskPage setActualTime(){
        clickTime();
        clickTime();
        return this;
    }

    public ScheduleTaskPage setTime (String HHMM){
        setValueOnTextField(TIME_SINGLE_ID, Data.createSingleData(HHMM));
        return this;
    }

    private void clickTime(){
        WebElement clock = driver.findElement(By.xpath(".//i[@class='OSSIcon fa fa-clock-o']"));
        clock.click();
        DelayUtils.sleep();
    }

    private void clickCalendar(){
        WebElement calendar = driver.findElement(By.xpath(".//i[@class='OSSIcon fa fa-calendar']"));
        calendar.click();
        DelayUtils.sleep();
    }

    private void clickOnDayButton(int dayOfWeek){
        List<WebElement> dayButtons = driver.findElements(By.xpath("//button[@class = 'dayButtonContainer']"));
        dayButtons.get(dayOfWeek-1).click();
    }

}
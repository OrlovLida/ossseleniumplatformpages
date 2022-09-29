package com.oss.pages.bpm.forecasts;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.list.EditableList;
import com.oss.pages.BasePage;
import org.openqa.selenium.WebDriver;

import java.time.LocalDate;
import java.util.Optional;

/**
 * @author Paweł Rother
 */
public class ForecastWizardPage extends BasePage {
    private static final String BPM_FORECAST_NAME = "name";
    private static final String BPM_FORECAST_NAME_INPUT = "name";

    private static final String BPM_FORECAST_START_DATE = "startDate";
    private static final String BPM_FORECAST_START_DATE_INPUT = "startDate";

    private static final String BPM_FORECAST_END_DATE_SHORT = "endTimeOptimistic";
    private static final String BPM_FORECAST_END_DATE_SHORT_INPUT = "endTimeOptimistic";

    private static final String BPM_FORECAST_END_DATE_LONG = "endTimePessimistic";
    private static final String BPM_FORECAST_END_DATE_LONG_INPUT = "endTimePessimistic";

    private static final String BPM_FORECAST_WORK_DAYS_SHORT = "workingDaysOptimistic";
    private static final String BPM_FORECAST_WORK_DAYS_SHORT_INPUT = "workingDaysOptimistic";

    private static final String BPM_FORECAST_WORK_DAYS_LONG = "workingDaysPessimistic";
    private static final String BPM_FORECAST_WORK_DAYS_LONG_INPUT = "workingDaysPessimistic";

    private static final String BPM_FORECAST_TYPE = "type";
    private static final String BPM_FORECAST_START_WEEK = "weekStartDate";
    private static final String BPM_FORECAST_END_WEEK_SHORT = "weekEndTimeOptimistic";
    private static final String BPM_FORECAST_END_WEEK_LONG = "weekEndTimePessimistic";
    private static final String BPM_FORECAST_LEAD_TIME_DAYS_SHORT = "leadTimeOptimisticDays";
    private static final String BPM_FORECAST_LEAD_TIME_DAYS_LONG = "leadTimePessimisticDays";
    private static final String BPM_FORECAST_LEAD_TIME_WEEKS_SHORT = "leadTimeOptimisticWeeks";
    private static final String BPM_FORECAST_LEAD_TIME_WEEKS_LONG = "leadTimePessimisticWeeks";
    private static final String DELETE_FORECAST_ROW_ACTION_ID = "deleteButton1";
    private final String forecastsListId;


    public ForecastWizardPage(WebDriver driver, String forecastsListId) {
        super(driver);
        this.forecastsListId = forecastsListId;
    }

    public ForecastAttributes addForecastRow(Forecast forecast) {
        waitForPageToLoad();
        EditableList addForecastList = EditableList.createById(driver, wait, forecastsListId);
        EditableList.Row row = addForecastList.addRow();
        waitForPageToLoad();
        setForecastAttributes(forecast, row);
        return getForecastFromRow(addForecastList, addForecastList.getVisibleRows().size() - 1);
    }

    public ForecastAttributes editForecastRow(Forecast forecast, int row) {
        waitForPageToLoad();
        EditableList editForecastList = EditableList.createById(driver, wait, forecastsListId);
        EditableList.Row editForecastRow = editForecastList.getRow(row - 1);
        DelayUtils.sleep(2000);
        setForecastAttributes(forecast, editForecastRow);
        return getForecastFromRow(editForecastList, row - 1);
    }

    public void removeForecastRow(int row) {
        EditableList forecastList = EditableList.createById(driver, wait, forecastsListId);
        EditableList.Row editForecastRow = forecastList.getRow(row - 1);
        DelayUtils.sleep(2000);
        editForecastRow.callAction(DELETE_FORECAST_ROW_ACTION_ID);
    }

    private void setForecastAttributes(Forecast forecast, EditableList.Row row) {
        forecast.getName().ifPresent(name -> setRowValue(row, name, BPM_FORECAST_NAME,
                BPM_FORECAST_NAME_INPUT));

        forecast.getStartPlusDays().ifPresent(startPlusDays ->
                setRowValue(row, LocalDate.now().plusDays(startPlusDays).toString(),
                        BPM_FORECAST_START_DATE, BPM_FORECAST_START_DATE_INPUT));

        forecast.getEndPlusDaysShortWay().ifPresent(endPlusDaysShortWay ->
                setRowValue(row, LocalDate.now().plusDays(endPlusDaysShortWay).toString(),
                        BPM_FORECAST_END_DATE_SHORT, BPM_FORECAST_END_DATE_SHORT_INPUT));

        forecast.getEndPlusDaysLongWay().ifPresent(endPlusDaysLongWay ->
                setRowValue(row, LocalDate.now().plusDays(endPlusDaysLongWay).toString(),
                        BPM_FORECAST_END_DATE_LONG, BPM_FORECAST_END_DATE_LONG_INPUT));

        forecast.getLongWorkWeakShortWay().ifPresent(longWorkWeakShortWay -> {
            if (Boolean.TRUE.equals(longWorkWeakShortWay))
                setRowValue(row, "7", BPM_FORECAST_WORK_DAYS_SHORT, BPM_FORECAST_WORK_DAYS_SHORT_INPUT);
        });

        forecast.getLongWorkWeakLongWay().ifPresent(longWorkWeakLongWay -> {
            if (Boolean.TRUE.equals(longWorkWeakLongWay))
                setRowValue(row, "7", BPM_FORECAST_WORK_DAYS_LONG, BPM_FORECAST_WORK_DAYS_LONG_INPUT);
        });
    }

    private void setRowValue(EditableList.Row row, String value, String columnId, String componentId) {
        row.setValue(value, columnId, componentId);
        waitForPageToLoad();
    }

    private ForecastAttributes getForecastFromRow(EditableList list, int row) {
        String name = list.getRow(row).getCellValue(BPM_FORECAST_NAME);
        String type = list.getRow(row).getCellValue(BPM_FORECAST_TYPE);
        String startDate = list.getRow(row).getCellValue(BPM_FORECAST_START_DATE);
        String startWeek = list.getRow(row).getCellValue(BPM_FORECAST_START_WEEK);
        String endDateShort = list.getRow(row).getCellValue(BPM_FORECAST_END_DATE_SHORT);
        String endWeekShort = list.getRow(row).getCellValue(BPM_FORECAST_END_WEEK_SHORT);
        String endDateLong = list.getRow(row).getCellValue(BPM_FORECAST_END_DATE_LONG);
        String endWeekLong = list.getRow(row).getCellValue(BPM_FORECAST_END_WEEK_LONG);
        String leadTimeDaysShort = list.getRow(row).getCellValue(BPM_FORECAST_LEAD_TIME_DAYS_SHORT);
        String leadTimeWeeksShort = list.getRow(row).getCellValue(BPM_FORECAST_LEAD_TIME_WEEKS_SHORT);
        String workDaysShort = list.getRow(row).getCellValue(BPM_FORECAST_WORK_DAYS_SHORT);
        String leadTimeDaysLong = list.getRow(row).getCellValue(BPM_FORECAST_LEAD_TIME_DAYS_LONG);
        String leadTimeWeeksLong = list.getRow(row).getCellValue(BPM_FORECAST_LEAD_TIME_WEEKS_LONG);
        String workDaysLong = list.getRow(row).getCellValue(BPM_FORECAST_WORK_DAYS_LONG);
        return ForecastAttributes.builder()
                .name(Optional.ofNullable(name))
                .activityType(Optional.ofNullable(type))
                .startDate(Optional.ofNullable(startDate))
                .startWeek(Optional.ofNullable(startWeek))
                .endDateShortWay(Optional.ofNullable(endDateShort))
                .endWeekShortWay(Optional.ofNullable(endWeekShort))
                .endDateLongWay(Optional.ofNullable(endDateLong))
                .endWeekLongWay(Optional.ofNullable(endWeekLong))
                .leadTimeDaysShortWay(Optional.ofNullable(leadTimeDaysShort))
                .leadTimeWeeksShortWay(Optional.ofNullable(leadTimeWeeksShort))
                .workDaysShortWay(Optional.ofNullable(workDaysShort))
                .leadTimeDaysLongWay(Optional.ofNullable(leadTimeDaysLong))
                .leadTimeWeeksLongWay(Optional.ofNullable(leadTimeWeeksLong))
                .workDaysLongWay(Optional.ofNullable(workDaysLong))
                .build();
    }

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, wait);
    }
}

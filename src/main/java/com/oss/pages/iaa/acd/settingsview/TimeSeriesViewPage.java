package com.oss.pages.iaa.acd.settingsview;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.prompts.ConfirmationBox;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.table.OldTable;
import com.oss.pages.iaa.acd.BaseACDPage;

import io.qameta.allure.Step;

import java.time.Duration;

public class TimeSeriesViewPage extends BaseACDPage {

    private static final Logger log = LoggerFactory.getLogger(TimeSeriesViewPage.class);

    private static final String timeSeriesViewSuffixUrl = "%s/#/view/acd/abgadSettings";
    private static final String TIME_SERIES_TABLE_ID = "abgadSettingsTabsContainer";
    private static final String TIME_SERIES_ID_LABEL = "Time Series Id";
    private static final String TIME_SERIES_STATUS_LABEL = "Is Removed";
    private static final String ADD_NEW_TIME_SERIES_BUTTON_ID = "timeSeriesButtons-3";
    private static final String DELETE_TIME_SERIES_BUTTON_ID = "timeSeriesButtons-1";
    private static final String SOURCE_SYSTEM_MULTI_COMBOBOX_ID = "source_system";
    private static final String INDICATORS_NAME_MULTI_SEARCHBOX_ID = "ABGAD_INDICATOR.identifier";
    private static final String MO_NAME_MULTI_SEARCHBOX_ID = "ABGAD_MONITORED_OBJECT.identifier";
    private static final String SHOW_DELETED_SWITCHER_ID = "is_deleted";
    private static final String DELETE_BUTTON_ID = "ConfirmationBox_confirmationBox_action_button";

    public TimeSeriesViewPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("I open Time Series View")
    public static TimeSeriesViewPage goToPage(WebDriver driver, String basicURL) {
        WebDriverWait wait = new WebDriverWait(driver,  Duration.ofSeconds(150));

        String pageUrl = String.format(timeSeriesViewSuffixUrl, basicURL);
        driver.get(pageUrl);
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Opened page: {}", pageUrl);

        return new TimeSeriesViewPage(driver, wait);
    }

    @Step("I click add new Time Series")
    public TimeSeriesWizardPage addNewTimeSeries() {
        clickContextButton(ADD_NEW_TIME_SERIES_BUTTON_ID);
        log.info("I click add new time series button");
        return new TimeSeriesWizardPage(driver, wait);
    }

    @Step("I click delete new Time Series")
    public void deleteTimeSeries() {
        clickContextButton(DELETE_TIME_SERIES_BUTTON_ID);
        log.info("I click delete new time series button");
    }

    @Step("I check if Time Series table is not empty")
    public boolean isDataInTimeSeriesTable() {
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Check if Time Series Table is not empty");
        return !getTimeSeriesTable().hasNoData();
    }

    @Step("Search in Time Series Table by Source System")
    public void searchBySourceSystem(String sourceSystem) {
        setAttributeValue(SOURCE_SYSTEM_MULTI_COMBOBOX_ID, sourceSystem);
        log.info("Search by Source System");
    }

    @Step("Search in Time Series Table by Indicator Name")
    public void searchByIndicatorName(String indicatorName) {
        setAttributeValue(INDICATORS_NAME_MULTI_SEARCHBOX_ID, indicatorName);
        log.info("Search by Indicator Name");
    }

    @Step("Search in Time Series Table by MO Name")
    public void searchByMoName(String moName) {
        setAttributeValue(MO_NAME_MULTI_SEARCHBOX_ID, moName);
        log.info("Search by MO Name");
    }

    @Step("Get first Time Series id")
    public String getFirstTimeSeriesId() {
        return getTimeSeriesTable().getCellValue(0, TIME_SERIES_ID_LABEL);
    }

    @Step("Get first Time Series status")
    public String getFirstTimeSeriesStatus() {
        return getTimeSeriesTable().getCellValue(0, TIME_SERIES_STATUS_LABEL);
    }

    @Step("Select first Time Series from table")
    public void selectFirstTSFromTable() {
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Select first Time Series from table");
        getTimeSeriesTable().selectFirstRow();
    }

    @Step("Confirm Time Series deletion")
    public void confirmTimeSeriesDeletion() {
        log.info("Confirm Time Series deletion");
        ConfirmationBox.create(driver, wait).clickButtonById(DELETE_BUTTON_ID);
    }

    public void turnOnShowDeletedSwitcher(){
        turnOnSwitcher(SHOW_DELETED_SWITCHER_ID);
        log.info("Turn on 'show deleted' switcher");
    }

    private OldTable getTimeSeriesTable() {
        return OldTable.createById(driver, wait, TIME_SERIES_TABLE_ID);
    }
}
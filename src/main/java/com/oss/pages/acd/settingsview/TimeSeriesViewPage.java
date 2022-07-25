package com.oss.pages.acd.settingsview;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.inputs.Button;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.table.OldTable;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.acd.BaseACDPage;

import io.qameta.allure.Step;

public class TimeSeriesViewPage extends BaseACDPage {

    private static final Logger log = LoggerFactory.getLogger(TimeSeriesViewPage.class);

    private static final String TS_TABLE_ID = "abgadSettingsTabsContainer";
    private static final String ADD_TIME_SERIES_WIZARD_ID = "addTimeSeries";
    private static final String INDICATOR_TYPE_RADIO_BUTTON_ID = "radioButtonIndicatorTypeId";
    private static final String TIME_SERIES_ID_LABEL = "Time Series Id";
    private static final String TIME_SERIES_STATUS_LABEL = "Is Removed";
    private String timeSeriesID;
    private String timeSeriesStatus;

    public TimeSeriesViewPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("I open Time Series View")
    public static TimeSeriesViewPage goToPage(WebDriver driver, String suffixURL, String basicURL) {
        WebDriverWait wait = new WebDriverWait(driver, 150);

        String pageUrl = String.format(suffixURL, basicURL);
        driver.get(pageUrl);
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Opened page: {}", pageUrl);

        return new TimeSeriesViewPage(driver, wait);
    }

    @Step("I click add new Time Series")
    public void addNewTimeSeries(String buttonId) {
        Button.createById(driver, buttonId).click();
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("I click add new time series button");
    }

    @Step("I select Indicator Technical Type")
    public void selectRadioButton() {
        Input radioButton = Wizard.createByComponentId(driver, wait, ADD_TIME_SERIES_WIZARD_ID)
                .getComponent(INDICATOR_TYPE_RADIO_BUTTON_ID);
        radioButton.setSingleStringValue("Any - All values");
        log.info("I select Indicator Technical Type");
    }

    @Step("I select checkbox")
    public void selectCheckbox(String checkboxId, String wizardId) {
        Wizard.createByComponentId(driver, wait, wizardId)
                .setComponentValue(checkboxId, "true", Input.ComponentType.CHECKBOX);
        log.info("I select {} checkbox", checkboxId);
    }

    @Step("I check if TS table is not empty")
    public boolean isDataInTSTable() {
        DelayUtils.waitForPageToLoad(driver, wait);
        return !getTSTable().hasNoData();
    }

    private OldTable getTSTable() {
        return OldTable.createById(driver, wait, TS_TABLE_ID);
    }

    public String getTSId() {

        if (!isDataInTSTable()) {
            log.info("Table doesn't have data for chosen filters. ID cannot be found");
        } else {
            timeSeriesID = getTSTable().getCellValue(0, TIME_SERIES_ID_LABEL);
        }
        return timeSeriesID;
    }

    public String getTSStatus() {

        if (!isDataInTSTable()) {
            log.info("Table doesn't have data for chosen filters. ID cannot be found");
        } else {
            timeSeriesStatus = getTSTable().getCellValue(0, TIME_SERIES_STATUS_LABEL);
        }
        return timeSeriesStatus;
    }

    @Step("I select first TS from table")
    public void selectFirstTSFromTable() {
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("I select first TS from table");
        getTSTable().selectFirstRow();
    }
}

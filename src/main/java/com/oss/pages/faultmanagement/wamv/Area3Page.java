package com.oss.pages.faultmanagement.wamv;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.iaa.widgets.list.ListApp;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.propertypanel.OldPropertyPanel;
import com.oss.framework.widgets.table.OldTable;
import com.oss.framework.widgets.tabs.TabsWidget;
import com.oss.pages.faultmanagement.WAMVPage;

import io.qameta.allure.Step;

public class Area3Page extends WAMVPage {
    private static final Logger log = LoggerFactory.getLogger(Area3Page.class);

    private static final String ADDITIONAL_TEXT_TAB_ID = "additional-text";
    private static final String ALARM_DETAILS_TAB_ID = "alarm-details";
    private static final String MO_PROPERTIES_TAB_ID = "mo-properties";
    private static final String SAME_MO_ALARMS_TAB_ID = "mo-alarms";
    private static final String CORRELATED_ALARMS_TAB_ID = "correlated-alarms";
    private static final String ALARM_CHANGES_TAB_ID = "alarm-changes";
    private static final String HISTORICAL_ALARMS_TAB_ID = "historical-alarms";
    private static final String LOCATION_ALARMS_TAB_ID = "location-alarms";
    private static final String TROUBLE_TICKETS_TAB_ID = "trouble-tickets";
    private static final String KNOW_HOW_TAB_ID = "know-how";
    private static final String OUTAGES_TAB_ID = "outages";
    private static final String ROOT_CAUSE_ALARMS_TAB_ID = "root-cause-alarms";

    private static final String AREA_3_ID = "AREA3";
    private static final String ADAPTER_NAME_VALUE = "Adapter Name";
    private static final String NOTIFICATION_IDENTIFIER_VALUE = "Notification Identifier";
    private static final String PERCEIVED_SEVERITY_VALUE = "Perceived Severity";
    private static final String IDENTIFIER_VALUE = "Identifier";
    private static final String PROPERTY_PANEL_ID = "card-content_AREA3";

    public Area3Page(WebDriver driver) {
        super(driver);
    }

    @Step("Selecting tab {tabId}")
    public void selectTabFromArea3(String tabId) {
        DelayUtils.waitForPageToLoad(driver, wait);
        TabsWidget.createById(driver, wait, AREA_3_ID).selectTabById(tabId);
        log.info("Selecting tab {}", tabId);
    }

    @Step("I click on Additional Text tab")
    public void clickOnAdditionalTextTab() {
        selectTabFromArea3(ADDITIONAL_TEXT_TAB_ID);
        log.info("Clicking on Additional Text tab");
    }

    @Step("I click on Alarm Details tab")
    public void clickOnAlarmDetailsTab() {
        selectTabFromArea3(ALARM_DETAILS_TAB_ID);
        log.info("Clicking on Alarm Details tab");
    }

    @Step("I click on MO Properties tab")
    public void clickOnMOPropertiesTab() {
        selectTabFromArea3(MO_PROPERTIES_TAB_ID);
        log.info("Clicking on MO Properties tab");
    }

    @Step("I click on Same MO Alarms tab")
    public void clickOnSameMOAlarmsTab() {
        selectTabFromArea3(SAME_MO_ALARMS_TAB_ID);
        log.info("Clicking on Same MO Alarms tab");
    }

    @Step("I click on Correlated Alarms tab")
    public void clickOnCorrelatedAlarmsTab() {
        selectTabFromArea3(CORRELATED_ALARMS_TAB_ID);
        log.info("Clicking on Correlated Alarms tab");
    }

    @Step("I click on Alarm Changes tab")
    public void clickOnAlarmChangesTab() {
        selectTabFromArea3(ALARM_CHANGES_TAB_ID);
        log.info("Clicking on Alarm Changes tab");
    }

    @Step("I click on Historical Alarms tab")
    public void clickOnHistoricalAlarmsTab() {
        selectTabFromArea3(HISTORICAL_ALARMS_TAB_ID);
        log.info("Clicking on Historical Alarms tab");
    }

    @Step("I click on Location Alarms tab")
    public void clickOnLocationAlarmsTab() {
        selectTabFromArea3(LOCATION_ALARMS_TAB_ID);
        log.info("Clicking on Location Alarms tab");
    }

    @Step("I click on Know How tab")
    public void clickOnKnowHowTab() {
        selectTabFromArea3(KNOW_HOW_TAB_ID);
        log.info("Clicking on Know How tab");
    }

    @Step("I click on Trouble Tickets tab")
    public void clickOnTroubleTicketsTab() {
        selectTabFromArea3(TROUBLE_TICKETS_TAB_ID);
        log.info("Clicking on Trouble Tickets tab");
    }

    @Step("I click on Outages tab")
    public void clickOnOutagesTab() {
        selectTabFromArea3(OUTAGES_TAB_ID);
        log.info("Clicking on Outages tab");
    }

    @Step("I click on Root Cause Alarms tab")
    public void clickRootCauseAlarmsTab() {
        selectTabFromArea3(ROOT_CAUSE_ALARMS_TAB_ID);
        log.info("Clicking on Root Cause Alarms tab");
    }

    @Step("I get adapter name from Alarms Details Tab")
    public String getAdapterNameFromAlarmDetailsTab() {
        log.info("Checking adapter name value from Alarm Details Tab");
        return getPropertyPanel().getPropertyValue(ADAPTER_NAME_VALUE);
    }

    @Step("I get notification identifier from Alarms Details Tab")
    public String getNotificationIdentifierFromAlarmDetailsTab() {
        log.info("Checking notification identifier value from Alarm Details Tab");
        return getPropertyPanel().getPropertyValue(NOTIFICATION_IDENTIFIER_VALUE);
    }

    @Step("I get perceived severity from Alarms Details Tab")
    public String getPerceivedSeverityFromAlarmDetailsTab() {
        log.info("Checking perceived severity value from Alarm Details Tab");
        return getPropertyPanel().getPropertyValue(PERCEIVED_SEVERITY_VALUE);
    }

    @Step("I get identifier from MO Properties Tab")
    public String getIdentifierFromMOPropertiesTab() {
        log.info("Checking identifier from MO Properties  Tab");
        return getPropertyPanel().getPropertyValue(IDENTIFIER_VALUE);
    }

    @Step("I check if {tableId} table exists")
    public boolean checkIfTableExists(String tableId) {
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("I check if {} table exists", tableId);
        if (!getOldTable(tableId).hasNoData()) {
            log.info("Table exists and has some data");
            return true;
        } else if (getOldTable(tableId).hasNoData()) {
            log.info("Table exists and it's empty");
            return true;
        } else {
            log.error("Table doesn't exist");
            return false;
        }
    }

    @Step("I check if additional text is displayed")
    public boolean isAdditionalTextDisplayed(String expectedText, String windowId) {
        List<String> text = ListApp.createFromParent(driver, wait, windowId).getValues();
        if (text.contains(expectedText)) {
            log.info("Expected additional text {} is displayed", expectedText);
            return true;
        } else {
            log.info("Expected additional text {} is not displayed", expectedText);
            return false;
        }
    }
//      TODO method will be working after fix OSSWEB-18155 - add proper data-testid and update it
//    public void checkShowEmptyCheckbox(){
//        Input showEmptyCheckbox = ComponentFactory.create(CHECKBOX_DATA_TEST_ID, Input.ComponentType.CHECKBOX, driver, wait);
//        showEmptyCheckbox.click();
//    }

    private OldPropertyPanel getPropertyPanel() {
        return OldPropertyPanel.createById(driver, wait, PROPERTY_PANEL_ID);
    }

    private OldTable getOldTable(String tableId) {
        return OldTable.createById(driver, wait, tableId);
    }
}

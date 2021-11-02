package com.oss.pages.faultmanagement;

import com.oss.framework.components.search.AdvancedSearch;
import com.oss.framework.listwidget.CommonList;
import com.oss.framework.prompts.ConfirmationBox;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FMDashboardPage extends BasePage {
    private static final Logger log = LoggerFactory.getLogger(FMDashboardPage.class);
    private static final String HTTP_URL_TO_FM_DASHBOARD = "%s/#/dashboard/predefined/id/_FaultManagement";
    private static final String COMMON_LIST_APP_ID = "_UserViewsListALARM_MANAGEMENT";
    private static final String OPEN_BUTTON_ID = "Open";
    private static final String CREATE_BUTTON_ID = "create-user-view";
    private static final String COLUMN_NAME_LABEL = "Name";
    private static final String BUTTONS_GROUP_ID = "frameworkObjectButtonsGroup";
    private static final String REMOVE_ACTION_ID = "remove-user-view";
    private static final String CONFIRMATION_BOX_BUTTON_NAME = "ConfirmationBox_confirmationBoxWidget_action_button";

    public FMDashboardPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    private final CommonList commonAlarmManagement = CommonList.create(driver, wait, COMMON_LIST_APP_ID);
    private final AdvancedSearch search = AdvancedSearch.createByWidgetId(driver, wait, COMMON_LIST_APP_ID);

    @Step("I open FM Dashboard")
    public static FMDashboardPage goToPage(WebDriver driver, String basicURL) {
        WebDriverWait wait = new WebDriverWait(driver, 90);
        String webURL = String.format(HTTP_URL_TO_FM_DASHBOARD, basicURL);
        driver.navigate().to(webURL);
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Opened page: {}", webURL);
        return new FMDashboardPage(driver, wait);
    }

    @Step("I search for specific alarm in list")
    public void searchInAlarmManagementView(String alarmsViewName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        search.fullTextSearch(alarmsViewName);
        log.info("Searching in WAMV: {}", alarmsViewName);
    }

    @Step("I open alarm list by the name")
    public void openSelectedAlarmView(String alarmViewName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        commonAlarmManagement.getRow(COLUMN_NAME_LABEL, alarmViewName).callAction(OPEN_BUTTON_ID);
        log.info("Opening selected alarm view: {}", alarmViewName);
    }

    @Step("I open alarm list by the chosen attribute")
    public void openSelectedAlarmView(String alarmViewName, String attributeName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        commonAlarmManagement.getRow(attributeName, alarmViewName).callAction(OPEN_BUTTON_ID);
        log.info("Opening alarm list: {} by attribute: {}", alarmViewName, attributeName);
    }

    @Step("I open alarm from the rowNumber")
    public WAMVPage openAlarmManagementViewByRow(int rowNumber) {
        DelayUtils.waitForPageToLoad(driver, wait);
        commonAlarmManagement.getAllRows().get(rowNumber).callAction(OPEN_BUTTON_ID);
        log.info("Opening of WAMV in row {}", rowNumber);
        return new WAMVPage(driver);
    }

    @Step("I click on Create New Alarm List button")
    public FMCreateWAMVPage clickCreateNewAlarmList() {
        DelayUtils.waitForPageToLoad(driver, wait);
        commonAlarmManagement.callAction(CREATE_BUTTON_ID);
        log.info("Click Create New Alarm list");
        return new FMCreateWAMVPage(driver);

    }

    @Step("I delete WAMV by name")
    public void deleteWebAlarmManagementView(int rowNumber) {
        DelayUtils.sleep(2000);
        commonAlarmManagement.getAllRows().get(rowNumber).selectRow();
        commonAlarmManagement.getAllRows().get(rowNumber).callAction(REMOVE_ACTION_ID);
        DelayUtils.sleep(1000);
        ConfirmationBox confirmationBox = ConfirmationBox.create(driver, wait);
        confirmationBox.clickButtonByDataAttributeName(CONFIRMATION_BOX_BUTTON_NAME);
        log.info("Deletion of WAMV in row {}", rowNumber);
    }

    @Step("I check the visibility of WAMV on the list")
    public boolean checkVisibilityOfWAMV(String name) {
        log.info("Checking the visibility of WAMV {}", name);
        return commonAlarmManagement.isRowVisible(COLUMN_NAME_LABEL, name);
    }
}
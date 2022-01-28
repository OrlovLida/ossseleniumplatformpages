package com.oss.pages.faultmanagement;

import com.oss.framework.components.search.AdvancedSearch;
import com.oss.framework.widgets.list.CommonList;
import com.oss.framework.components.prompts.ConfirmationBox;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.components.layout.Card;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class FMDashboardPage extends BasePage {
    private static final Logger log = LoggerFactory.getLogger(FMDashboardPage.class);
    private static final String HTTP_URL_TO_FM_DASHBOARD = "%s/#/dashboard/predefined/id/_FaultManagement";
    private static final String OPEN_BUTTON_ID = "Open";
    private static final String CREATE_BUTTON_ID = "create-user-view";
    private static final String COLUMN_NAME_LABEL = "Name";
    private static final String REMOVE_ACTION_ID = "remove-user-view";
    private static final String CONFIRMATION_BOX_BUTTON_NAME = "ConfirmationBox_confirmationBoxWidget_action_button";

    private static final String ALARM_COUNTERS_ID = "_AlarmCounters";
    private static final String ALARM_COUNTERS_VIEW_ID = "_UserViewsListALARM_COUNTERS";
    private static final String ALARM_MANAGEMENT_VIEW_ID = "_UserViewsListALARM_MANAGEMENT";
    private static final String HISTORICAL_ALARM_VIEW_ID = "_UserViewsListHISTORICAL_ALARM_MANAGEMENT";
    private static final String MAP_MONITORING_VIEW_ID = "_UserViewsListMAP_MONITORING";

    public FMDashboardPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    private CommonList createCommonList(String commonListId) {
        return CommonList.create(driver, wait, commonListId);
    }

    @Step("I open FM Dashboard")
    public static FMDashboardPage goToPage(WebDriver driver, String basicURL) {
        WebDriverWait wait = new WebDriverWait(driver, 90);
        String webURL = String.format(HTTP_URL_TO_FM_DASHBOARD, basicURL);
        driver.navigate().to(webURL);
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Opened page: {}", webURL);
        return new FMDashboardPage(driver, wait);
    }

    @Step("I search for specific alarm in View")
    public void searchInSpecificView(String commonListId, String alarmsViewName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        searchInView(alarmsViewName, commonListId);
        log.info("Searching in WAMV: {}", alarmsViewName);
    }

    public boolean isNoDataInView(String commonListId) {
        CommonList commonList = CommonList.create(driver, wait, commonListId);
        return isNoData(commonList);
    }

    private void searchInView(String alarmsViewName, String widgetId) {
        AdvancedSearch search = AdvancedSearch.createByWidgetId(driver, wait, widgetId);
        search.fullTextSearch(alarmsViewName);
    }

    private boolean isNoData(CommonList commonList) {
        DelayUtils.sleep(1000);
        return commonList.hasNoData();
    }

    @Step("I open selected view by the name")
    public WAMVPage openSelectedView(String commonListId, String alarmViewName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        CommonList commonList = createCommonList(commonListId);
        commonList.getRow(COLUMN_NAME_LABEL, alarmViewName).callAction(OPEN_BUTTON_ID);
        log.info("Opening selected alarm view: {}", alarmViewName);
        return new WAMVPage(driver);
    }

    @Step("I open selected view by the name and attribute")
    public WAMVPage openSelectedView(String commonListId, String alarmViewName, String attributeName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        CommonList commonList = createCommonList(commonListId);
        commonList.getRow(attributeName, alarmViewName).callAction(OPEN_BUTTON_ID);
        log.info("Opening alarm list: {} by attribute: {}", alarmViewName, attributeName);
        return new WAMVPage(driver);
    }

    @Step("I open alarm from the rowNumber")
    public WAMVPage openSelectedView(String commonListId, int rowNumber) {
        DelayUtils.waitForPageToLoad(driver, wait);
        CommonList commonList = createCommonList(commonListId);
        commonList.getAllRows().get(rowNumber).callAction(OPEN_BUTTON_ID);
        log.info("Opening of WAMV in row {}", rowNumber);
        return new WAMVPage(driver);
    }

    @Step("I click on Create New Alarm List button")
    public FMCreateWAMVPage clickCreateNewAlarmList() {
        DelayUtils.waitForPageToLoad(driver, wait);
        CommonList commonAlarmManagement = createCommonList(ALARM_MANAGEMENT_VIEW_ID);
        commonAlarmManagement.callAction(CREATE_BUTTON_ID);
        log.info("Click Create New Alarm list");
        return new FMCreateWAMVPage(driver);
    }

    @Step("I delete view by name")
    public void deleteFromView(String commonListId, int rowNumber) {
        DelayUtils.sleep(1000);
        CommonList commonList = createCommonList(commonListId);
        commonList.getAllRows().get(rowNumber).selectRow();
        commonList.getAllRows().get(rowNumber).callAction(REMOVE_ACTION_ID);
        DelayUtils.sleep(1000);
        ConfirmationBox confirmationBox = ConfirmationBox.create(driver, wait);
        confirmationBox.clickButtonById(CONFIRMATION_BOX_BUTTON_NAME);
        log.info("Deletion of WAMV in row {}", rowNumber);
    }

    @Step("I check the visibility of view on the list")
    public boolean checkVisibility(String commonListId, String name) {
        log.info("Checking the visibility of {}", name);
        CommonList commonList = createCommonList(commonListId);
        return commonList.isRowDisplayed(COLUMN_NAME_LABEL, name);
    }

    @Step("Maximize window")
    public void maximizeWindow(String windowId) {
        Card card = Card.createCard(driver, wait, windowId);
        card.maximizeCard();
        log.info("Maximizing window");
    }

    @Step("Minimize window")
    public void minimizeWindow(String windowId) {
        Card card = Card.createCard(driver, wait, windowId);
        card.minimizeCard();
        log.info("Minimizing window");
    }

    @Step("I check if card if macimized")
    public boolean checkCardMaximize(String windowId) {
        DelayUtils.sleep(1000);
        Card card = Card.createCard(driver, wait, windowId);
        return card.isCardMaximized();
    }

    public boolean isHeaderVisible(String commonListId, String headerLabel) {
        CommonList commonList = createCommonList(commonListId);
        return commonList.getHeaders().contains(headerLabel);
    }

    public boolean isHeaderVisible(String commonListId, List<String> headerLabel) {
        CommonList commonList = createCommonList(commonListId);
        return commonList.getHeaders().containsAll(headerLabel);
    }

    public void sortCommonList(String commonListId) {
        CommonList commonList = createCommonList(commonListId);
        commonList.callAction("sort-by-user-view", "Name_desc");
    }
}
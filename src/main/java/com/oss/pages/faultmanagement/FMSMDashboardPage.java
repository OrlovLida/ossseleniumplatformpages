package com.oss.pages.faultmanagement;

import com.oss.framework.components.layout.Card;
import com.oss.framework.components.prompts.ConfirmationBox;
import com.oss.framework.components.search.AdvancedSearch;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.list.CommonList;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class FMSMDashboardPage extends BasePage {
    private static final Logger log = LoggerFactory.getLogger(FMSMDashboardPage.class);
    private static final String HTTP_URL_TO_FM_SM_DASHBOARD = "%s/#/dashboard/predefined/id/_%s";
    private static final String OPEN_BUTTON_ID = "Open";
    private static final String CREATE_BUTTON_ID = "create-user-view";
    private static final String COLUMN_NAME_LABEL = "Name";
    private static final String REMOVE_ACTION_ID = "remove-user-view";
    private static final String CONFIRMATION_BOX_BUTTON_NAME = "ConfirmationBox_confirmationBoxWidget_action_button";
    private static final String ALARM_MANAGEMENT_VIEW_ID = "_UserViewsListALARM_MANAGEMENT";

    public FMSMDashboardPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    private CommonList createCommonList(String commonListId) {
        return CommonList.create(driver, wait, commonListId);
    }

    @Step("I open chosen Dashboard")
    public static FMSMDashboardPage goToPage(WebDriver driver, String basicURL, String chosenDashboard) {
        WebDriverWait wait = new WebDriverWait(driver, 90);
        String webURL = String.format(HTTP_URL_TO_FM_SM_DASHBOARD, basicURL, chosenDashboard);
        driver.navigate().to(webURL);
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Opened page: {}", webURL);
        return new FMSMDashboardPage(driver, wait);
    }

    @Step("I search for specific alarm in View")
    public void searchInSpecificView(String commonListId, String alarmsViewName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        searchInView(alarmsViewName, commonListId);
        log.info("Searching in WAMV: {}", alarmsViewName);
    }

    public boolean isNoDataInView(String commonListId) {
        CommonList commonList = CommonList.create(driver, wait, commonListId);
        log.info("Checking if '{}' has 'No data' shown: {}", commonListId, isNoData(commonList));
        return isNoData(commonList);
    }

    @Step("I search for text in View")
    public void searchInView(String widgetId, String searchedText) {
        AdvancedSearch search = AdvancedSearch.createByWidgetId(driver, wait, widgetId);
        DelayUtils.waitForPageToLoad(driver, wait);
        search.fullTextSearch(searchedText);
        log.info("Searching in view: {}", widgetId);
    }

    private boolean isNoData(CommonList commonList) {
        DelayUtils.waitForPageToLoad(driver, wait);
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
        commonList.getRows().get(rowNumber).callAction(OPEN_BUTTON_ID);
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
        commonList.getRows().get(rowNumber).selectRow();
        commonList.getRows().get(rowNumber).callAction(REMOVE_ACTION_ID);
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
        DelayUtils.waitForPageToLoad(driver, wait);
        card.maximizeCard();
        log.info("Maximizing window");
    }

    @Step("Minimize window")
    public void minimizeWindow(String windowId) {
        Card card = Card.createCard(driver, wait, windowId);
        DelayUtils.waitForPageToLoad(driver, wait);
        card.minimizeCard();
        log.info("Minimizing window");
    }

    @Step("I check if card if maximized")
    public boolean checkCardMaximize(String windowId) {
        DelayUtils.sleep(1000);
        Card card = Card.createCard(driver, wait, windowId);
        log.info("Checking if card {} is maximized", windowId);
        return card.isCardMaximized();
    }

    @Step("I check if header is visible")
    public boolean isHeaderVisible(String commonListId, String headerLabel) {
        log.info("Checking the visibility of header '{}' on view '{}'", headerLabel, commonListId);
        CommonList commonList = createCommonList(commonListId);
        return commonList.getRowHeaders().contains(headerLabel);
            }

    public boolean isHeaderVisible(String commonListId, List<String> headerLabel) {
        CommonList commonList = createCommonList(commonListId);
        return commonList.getRowHeaders().containsAll(headerLabel);
    }

    public void sortCommonList(String commonListId) {
        CommonList commonList = createCommonList(commonListId);
        log.info("Sorting list {}", commonListId);
        commonList.callAction("sort-by-user-view", "Name_desc");
    }
}
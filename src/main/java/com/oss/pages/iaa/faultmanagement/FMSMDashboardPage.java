package com.oss.pages.iaa.faultmanagement;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.layout.Card;
import com.oss.framework.components.prompts.ConfirmationBox;
import com.oss.framework.components.search.AdvancedSearch;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.list.CommonList;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class FMSMDashboardPage extends BasePage {
    private static final Logger log = LoggerFactory.getLogger(FMSMDashboardPage.class);
    private static final String HTTP_URL_TO_FM_SM_DASHBOARD = "%s/#/dashboard/predefined/id/_%s";
    private static final String OPEN_BUTTON_ID = "Open";
    private static final String CREATE_BUTTON_ID = "create-user-view";
    private static final String COLUMN_NAME_LABEL = "Name";
    private static final String REMOVE_ACTION_ID = "remove-user-view";
    private static final String EDIT_ACTION_ID = "edit-user-view";
    private static final String CONFIRMATION_BOX_BUTTON_NAME = "ConfirmationBox_confirmationBoxWidget_action_button";

    public FMSMDashboardPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
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
        log.info("Checking if '{}' has 'No data' shown: {}", commonListId, isNoData(getCommonList(commonListId)));
        return isNoData(getCommonList(commonListId));
    }

    @Step("I search for text in View")
    public void searchInView(String widgetId, String searchedText) {
        AdvancedSearch search = AdvancedSearch.createByWidgetId(driver, wait, widgetId);
        DelayUtils.waitForPageToLoad(driver, wait);
        search.fullTextSearch(searchedText);
        DelayUtils.sleep(8000); //  TODO change it after fix OSSNGSA-11102
        log.info("Searching in view: {}", widgetId);
    }

    private boolean isNoData(CommonList commonList) {
        DelayUtils.waitForPageToLoad(driver, wait);
        return commonList.hasNoData();
    }

    @Step("I open selected view by the name")
    public WAMVPage openSelectedView(String commonListId, String alarmViewName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getCommonList(commonListId).getRow(COLUMN_NAME_LABEL, alarmViewName).callAction(OPEN_BUTTON_ID);
        log.info("Opening selected alarm view: {}", alarmViewName);
        return new WAMVPage(driver);
    }

    @Step("I open selected view by the name and attribute")
    public WAMVPage openSelectedView(String commonListId, String alarmViewName, String attributeName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getCommonList(commonListId).getRow(attributeName, alarmViewName).callAction(OPEN_BUTTON_ID);
        log.info("Opening alarm list: {} by attribute: {}", alarmViewName, attributeName);
        return new WAMVPage(driver);
    }

    @Step("I open alarm from the rowNumber")
    public WAMVPage openSelectedView(String commonListId, int rowNumber) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getCommonList(commonListId).getRows().get(rowNumber).callAction(OPEN_BUTTON_ID);
        log.info("Opening of WAMV in row {}", rowNumber);
        return new WAMVPage(driver);
    }

    @Step("I click on Create New Alarm List button in {chosenViewId}")
    public FMCreateWAMVPage clickCreateNewAlarmList(String chosenViewId) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getCommonList(chosenViewId).callAction(CREATE_BUTTON_ID);
        log.info("Click Create New Alarm list in {}", chosenViewId);
        return new FMCreateWAMVPage(driver, wait);
    }

    public void callActionOnListElement(String commonListId, int rowNumber, String actionId) {
        getRow(commonListId, rowNumber).selectRow();
        getRow(commonListId, rowNumber).callAction(actionId);
        log.info("Call action {} on row {}", actionId, rowNumber);
    }

    public void confirmAction() {
        ConfirmationBox.create(driver, wait).clickButtonById(CONFIRMATION_BOX_BUTTON_NAME);
        log.info("Click confirm");
    }

    @Step("Delete view")
    public void deleteFromView(String commonListId, int rowNumber) {
        callActionOnListElement(commonListId, rowNumber, REMOVE_ACTION_ID);
        confirmAction();
        log.info("Deletion of WAMV in row {}", rowNumber);
    }

    @Step("Click Edit view")
    public void clickEditView(String commonListId, int rowNumber) {
        callActionOnListElement(commonListId, rowNumber, EDIT_ACTION_ID);
        log.info("Click Edit WAMV in row {}", rowNumber);
    }

    @Step("Check the visibility of view on the list")
    public boolean checkVisibility(String commonListId, String name) {
        log.info("Checking the visibility of {}", name);
        DelayUtils.waitForPageToLoad(driver, wait);
        return getCommonList(commonListId).isRowDisplayed(COLUMN_NAME_LABEL, name);
    }

    @Step("Maximize window")
    public void maximizeWindow(String windowId) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getCard(windowId).maximizeCard();
        log.info("Maximizing window");
    }

    @Step("Minimize window")
    public void minimizeWindow(String windowId) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getCard(windowId).minimizeCard();
        log.info("Minimizing window");
    }

    @Step("I check if card if maximized")
    public boolean checkCardMaximize(String windowId) {
        DelayUtils.sleep(1000);
        log.info("Checking if card {} is maximized", windowId);
        return getCard(windowId).isCardMaximized();
    }

    private Card getCard(String windowId) {
        return Card.createCard(driver, wait, windowId);
    }

    @Step("I check if header is visible")
    public boolean isHeaderVisible(String commonListId, String headerLabel) {
        log.info("Checking the visibility of header '{}' on view '{}'", headerLabel, commonListId);
        return getCommonList(commonListId).getRowHeaders().contains(headerLabel);
    }

    public boolean isHeaderVisible(String commonListId, List<String> headerLabel) {
        return getCommonList(commonListId).getRowHeaders().containsAll(headerLabel);
    }

    private CommonList getCommonList(String commonListId) {
        return CommonList.create(driver, wait, commonListId);
    }

    private CommonList.Row getRow(String commonListId, int rowNumber) {
        return getCommonList(commonListId).getRows().get(rowNumber);
    }

    public void sortCommonList(String commonListId) {
        log.info("Sorting list {}", commonListId);
        getCommonList(commonListId).callAction("sort-by-user-view", "Name_desc");
    }
}
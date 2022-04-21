package com.oss.pages.administration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.utils.DelayUtils;

import io.qameta.allure.Step;

public class DashboardManagerPage extends BaseManagerPage {

    private static final Logger log = LoggerFactory.getLogger(DashboardManagerPage.class);

    private static final String SEARCH_FIELD_ID = "dashboardSearch";
    private static final String LIST_ID = "dashboardList";
    private static final String CONFIRM_DELETE_BUTTON_ID = "ConfirmationBox_removeDASHBOARDConfirmationBox_action_button";
    private static final String ADD_CUSTOM_DASHBOARD_LABEL = "Add Custom Dashboard";

    public DashboardManagerPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("Go to Dashboard Manager page")
    public static DashboardManagerPage goToPage(WebDriver driver, String basicURL) {
        WebDriverWait wait = new WebDriverWait(driver, 45);
        String pageUrl = String.format("%s/#/view/dashboardmanager/main", basicURL);
        driver.get(pageUrl);
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Opened page: {}", pageUrl);
        return new DashboardManagerPage(driver, wait);
    }

    @Step("Search for dashboard")
    public void searchForDashboard(String dashboardName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        searchInList(SEARCH_FIELD_ID, dashboardName);
        log.info("Searching dashboard {}", dashboardName);
    }

    @Step("Open dashboard")
    public DashboardPage openDashboard(String dashboardName) {
        openListElement(dashboardName);
        log.info("Opening dashboard {}", dashboardName);
        return new DashboardPage(driver, wait);
    }

    @Step("Check if list contains any dashboards")
    public boolean isAnyDashboardInList() {
        return isAnyElementOnList();
    }

    @Step("Click Delete Dashboard")
    public void clickDeleteDashboard(String dashboardName) {
        clickDeleteElement(CONFIRM_DELETE_BUTTON_ID, dashboardName);
        log.info("Click delete Dashboard");
    }

    @Step("Check if dashboard is deleted")
    public boolean isDashboardDeleted(String dashboardName) {
        log.info("Checking if dashboard is deleted");
        return isElementDeleted(dashboardName);
    }

    @Step("Click Add Custom Dashboard Button")
    public void clickAddCustomDashboard() {
        clickCreateNewElement(ADD_CUSTOM_DASHBOARD_LABEL);
        log.info("Clicking Add Custom Dashboard");
    }

    @Override
    public String getListId() {
        return LIST_ID;
    }
}

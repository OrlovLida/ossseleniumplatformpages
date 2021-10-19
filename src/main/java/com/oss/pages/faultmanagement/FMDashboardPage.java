package com.oss.pages.faultmanagement;

import com.oss.framework.components.search.AdvancedSearch;
import com.oss.framework.listwidget.CommonList;
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

    public FMDashboardPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    private CommonList commonAlarmManagement = CommonList.create(driver, wait, COMMON_LIST_APP_ID);
    private AdvancedSearch search = AdvancedSearch.createByWidgetId(driver, wait, COMMON_LIST_APP_ID);

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

    }

    @Step("I open alarm list by the name")
    public void openSelectedAlarmView(String alarmViewName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        commonAlarmManagement.getRow("Name", alarmViewName).callAction(OPEN_BUTTON_ID);
    }

    @Step("I open alarm list by the chosen attribute")
    public void openSelectedAlarmView(String alarmViewName, String attributeName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        commonAlarmManagement.getRow(attributeName, alarmViewName).callAction(OPEN_BUTTON_ID);
    }

    @Step("I open alarm from the rowNumber")
    public void openAlarmManagementViewByRow(int rowNumber) {
        DelayUtils.waitForPageToLoad(driver, wait);
        commonAlarmManagement.getAllRows().get(rowNumber).callAction(OPEN_BUTTON_ID);
    }

    @Step("I click on Creat New Alarm List button")
    public FMCreateWAMVPage clickCreateNewAlarmList() {
        DelayUtils.waitForPageToLoad(driver, wait);
        commonAlarmManagement.callAction(CREATE_BUTTON_ID);
        return new FMCreateWAMVPage(driver);
    }
}
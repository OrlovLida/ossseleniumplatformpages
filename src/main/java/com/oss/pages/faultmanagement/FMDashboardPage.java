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
    public static final String HTTP_URL_TO_FM_DASHBOARD = "%s/#/dashboard/predefined/id/_FaultManagement";
    public static final String COMMON_LIST_APP_ID = "_UserViewsListALARM_MANAGEMENT";
    public static final String OPEN_BUTTON_ID = "Open";


    public FMDashboardPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
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

    @Step("I search for specific alarm in list")
    public void searchForAlarmList(String alarmListName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        AdvancedSearch search = AdvancedSearch.createByWidgetId(driver, wait, COMMON_LIST_APP_ID);
        search.fullTextSearch(alarmListName);

    }

    @Step("I open alarm list by the name")
    public void openSelectedAlarmList(String alarmListName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        CommonList commonList = CommonList.create(driver, wait, COMMON_LIST_APP_ID);
        commonList.getRow("Name", alarmListName).callAction(OPEN_BUTTON_ID);
    }

    @Step("I open alarm list by the seelected attribute")
    public void openSelectedAlarmList(String attName, String alarmListAtt) {
        DelayUtils.waitForPageToLoad(driver, wait);
        CommonList commonList = CommonList.create(driver, wait, COMMON_LIST_APP_ID);
        commonList.getRow(attName, alarmListAtt).callAction(OPEN_BUTTON_ID);
    }

    @Step("I open N alarm from the list")
    public void openAlarmListFromList(int n) {
        DelayUtils.waitForPageToLoad(driver, wait);
        CommonList commonList = CommonList.create(driver, wait, COMMON_LIST_APP_ID);
        commonList.getAllRows().get(n).callAction(OPEN_BUTTON_ID);
    }

}

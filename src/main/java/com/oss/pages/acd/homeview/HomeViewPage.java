package com.oss.pages.acd.homeview;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.chart.ChartComponent;
import com.oss.framework.components.inputs.Button;
import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.table.OldTable;
import com.oss.pages.acd.BaseACDPage;

import io.qameta.allure.Step;

public class HomeViewPage extends BaseACDPage {

    private static final Logger log = LoggerFactory.getLogger(HomeViewPage.class);

    private static final String COLUMN_CHART_ID = "ColumnChartWindowId";
    private static final String PIE_CHART_ID = "PieChartTableWindowId";
    private static final String HOME_ISSUE_TABLE_ID = "issueTableId";
    private static final String ISSUES_TABLE_REFRESH_BUTTON_ID = "issueTableButtonsId-0";

    private final OldTable table;

    public HomeViewPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        table = OldTable.createById(driver, wait, HOME_ISSUE_TABLE_ID);
    }

    @Step("I Open ACD Home View")
    public static HomeViewPage goToPage(WebDriver driver, String basicURL) {
        WebDriverWait wait = new WebDriverWait(driver, 150);

        String pageUrl = String.format("%s/#/dashboard/predefined/id/_Automation_Control_Desk", basicURL);
        driver.get(pageUrl);
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Opened page: {}", pageUrl);

        return new HomeViewPage(driver, wait);
    }

    @Step("Waiting for column chart presence")
    public void seeColumnChartIsDisplayed() {
        log.info("Waiting for column chart presence");
        DelayUtils.waitForPageToLoad(driver, wait);
        ChartComponent.create(driver, wait, COLUMN_CHART_ID);
    }

    @Step("Waiting for pie chart presence")
    public void seePieChartIsDisplayed() {
        log.info("Waiting for pie chart presence");
        DelayUtils.waitForPageToLoad(driver, wait);
        ChartComponent.create(driver, wait, PIE_CHART_ID);
    }

    @Step("Set value of Issue Id multiSearch")
    public void setValueOfIssueIdSearch() {

        if (Boolean.TRUE.equals(isMultiSearchFilled("id"))) {
            clearAttributeValue("id");
        }

        if (Boolean.FALSE.equals(isDataInScenarioTable())) {
            log.info("Table doesn't have data for chosen filters. Issue ID cannot be set");
        } else {
            String firstIdInTable = table.getCellValue(0, "Issue Id");
            log.info("Setting value of Issue Id");
            ComponentFactory.create("id", driver, wait).setSingleStringValue(firstIdInTable);
            DelayUtils.sleep();
        }
    }

    @Step("Check if there is data in issues table")
    public Boolean isDataInScenarioTable() {
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Checking if there is data in issues table");
        return !table.hasNoData();
    }

    @Step("Refresh Issues table")
    public void refreshIssuesTable() {
        Button button = Button.createById(driver, ISSUES_TABLE_REFRESH_BUTTON_ID);
        button.click();
        log.info("Clicking refresh issues table button");
    }

    @Step("Check if multiSearch is filled")
    public Boolean isMultiSearchFilled(String multiSearchId) {

        return !ComponentFactory.create(multiSearchId, Input.ComponentType.MULTI_SEARCH_FIELD, driver, wait)
                .getStringValues()
                .isEmpty();
    }
}

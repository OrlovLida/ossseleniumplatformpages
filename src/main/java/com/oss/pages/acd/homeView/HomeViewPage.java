package com.oss.pages.acd.homeView;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.chart.ChartComponent;
import com.oss.framework.components.inputs.Button;
import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.iaa.widgets.servicedeskadvancedsearch.ServiceDeskAdvancedSearch;
import com.oss.framework.iaa.widgets.timeperiodchooser.TimePeriodChooser;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.table.OldTable;
import com.oss.pages.acd.BaseACDPage;

import io.qameta.allure.Step;

public class HomeViewPage extends BaseACDPage {

    private static final Logger log = LoggerFactory.getLogger(HomeViewPage.class);

    private static final String columnChartViewId = "ColumnChartWindowId";
    private static final String columnChartRefreshButtonId = "columnChartRefreshButtonId-0";
    private static final String pieChartViewId = "PieChartTableWindowId";
    private static final String pieChartRefreshButtonId = "pieChartRefreshButtonId-0";
    private static final String homeIssueTableWindowId = "IssueTableWindowId";
    private static final String homeIssueTableId = "issueTableId";
    private static final String issuesTableRefreshButtonId = "issueTableButtonsId-0";

    private final OldTable table;
    private final ServiceDeskAdvancedSearch advancedSearch;

    public HomeViewPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);

        table = OldTable.createById(driver, wait, homeIssueTableId);
        advancedSearch = ServiceDeskAdvancedSearch.create(driver, wait, homeIssueTableWindowId);
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
        ChartComponent.create(driver, wait, columnChartViewId);
    }

    @Step("Refresh column chart")
    public void refreshColumnChart() {
        Button button = Button.createById(driver, columnChartRefreshButtonId);
        button.click();
    }

    @Step("Waiting for pie chart presence")
    public void seePieChartIsDisplayed() {
        log.info("Waiting for pie chart presence");
        DelayUtils.waitForPageToLoad(driver, wait);
        ChartComponent.create(driver, wait, pieChartViewId);
    }

    @Step("Refresh pie chart")
    public void refreshPieChart() {
        log.info("Refreshing pie chart");
        Button button = Button.createById(driver, pieChartRefreshButtonId);
        button.click();
    }

    @Step("Check value of Creation Type in table")
    public void checkValueOfCreationTypeAttribute() {
        String firstRowInTable = table.getCellValue(0, "Creation Type");
        log.info("Value of first row for creation type is: {}", firstRowInTable);
    }

    @Step("Set value of Issue Id multiSearch")
    public void setValueOfIssueIdSearch() {

        if (isMultiSearchFilled("id")) {
            clearMultiSearch("id");
        }

        if (!isDataInScenarioTable()) {
            log.info("Table doesn't have data for chosen filters. Issue ID cannot be set");
        } else {
            String firstIdInTable = table.getCellValue(0, "Issue Id");
            log.info("Setting value of Issue Id");
            ComponentFactory.create("id", Input.ComponentType.MULTI_SEARCH_FIELD, driver, wait)
                    .setSingleStringValue(firstIdInTable);

            DelayUtils.sleep();
        }
    }

    @Step("Set value in time period chooser")
    public void setValueInTimePeriodChooser(String widgetId, int days, int hours, int minutes) {

        if (!isTimePeriodChooserFilled(widgetId)) {
            clearTimePeriod(widgetId);
        }

        TimePeriodChooser timePeriod = TimePeriodChooser.create(driver, wait, widgetId);

        timePeriod.chooseOption(TimePeriodChooser.TimePeriodChooserOption.LAST);
        log.info("Setting value in the time period chooser");
        timePeriod.setLastPeriod(days, hours, minutes);
        DelayUtils.sleep();
    }

    @Step("Set value in multiComboBox")
    public void setValueInMultiComboBox(String attributeName, String inputValue) {

        if (isMultiComboBoxFilled(attributeName)) {
            clearMultiComboBox(attributeName);
        }

        DelayUtils.waitForPageToLoad(driver, wait);
        ComponentFactory.create(attributeName, Input.ComponentType.MULTI_COMBOBOX, driver, wait)
                .setSingleStringValue(inputValue);
        log.info("Setting value in MultiComboBox");
    }

    @Step("Clear time period chooser")
    public void clearTimePeriod(String widgetId) {
        TimePeriodChooser timePeriod = TimePeriodChooser.create(driver, wait, widgetId);
        timePeriod.clickClearValue();
        log.info("Clearing time period chooser");
    }

    @Step("Check if there is data in issues table")
    public Boolean isDataInScenarioTable() {
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Checking if there is data in issues table");
        return !table.hasNoData();
    }

    @Step("Clear multiComboBox")
    public void clearMultiComboBox(String multiComboBoxId) {
        Input issueTypeComboBox = advancedSearch.getComponent(multiComboBoxId, Input.ComponentType.MULTI_COMBOBOX);
        DelayUtils.waitForPageToLoad(driver, wait);
        issueTypeComboBox.clear();
        log.info("Clearing multiComboBox");
    }

    @Step("Clear multiSearch")
    public void clearMultiSearch(String multiSearchId) {
        Input issueIdSearch = advancedSearch.getComponent(multiSearchId, Input.ComponentType.MULTI_SEARCH_FIELD);
        issueIdSearch.clear();
        log.info("Clearing multiSearch");
    }

    @Step("Refresh Issues table")
    public void refreshIssuesTable() {
        Button button = Button.createById(driver, issuesTableRefreshButtonId);
        button.click();
        log.info("Clicking refresh issues table button");
    }

    @Step("Check if multiComboBox is filled")
    public Boolean isMultiComboBoxFilled(String multiComboBoxId) {

        return ComponentFactory.create(multiComboBoxId, Input.ComponentType.MULTI_COMBOBOX, driver, wait)
                .getStringValues()
                .size() > 0;
    }

    @Step("Check if multiSearch is filled")
    public Boolean isMultiSearchFilled(String multiSearchId) {

        return ComponentFactory.create(multiSearchId, Input.ComponentType.MULTI_SEARCH_FIELD, driver, wait)
                .getStringValues()
                .size() > 0;
    }

    @Step("Check if timePeriodChooser is filled")
    public Boolean isTimePeriodChooserFilled(String widgetId) {

        return TimePeriodChooser.create(driver, wait, widgetId).toString() != null;
    }
}

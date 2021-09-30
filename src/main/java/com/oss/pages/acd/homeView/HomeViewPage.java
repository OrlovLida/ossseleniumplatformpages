package com.oss.pages.acd.homeView;

import com.oss.framework.components.common.TimePeriodChooser;
import com.oss.framework.components.inputs.Button;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.data.Data;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.view.Card;
import com.oss.framework.widgets.chartwidget.ChartWidget;
import com.oss.framework.widgets.serviceDeskAdvancedSearch.ServiceDeskAdvancedSearch;
import com.oss.framework.widgets.tablewidget.OldTable;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HomeViewPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(HomeViewPage.class);

    private final String columnChartViewId = "ColumnChartWindowId";
    private final String columnChartRefreshButtonId = "columnChartRefreshButtonId-0";
    private final String pieChartViewId = "PieChartTableWindowId";
    private final String pieChartRefreshButtonId = "pieChartRefreshButtonId-0";
    private final String homeIssueTableWindowId = "IssueTableWindowId";
    private final String homeIssueTableId = "issueTableId";
    private final String issuesTableRefreshButtonId = "issueTableButtonsId-0";

    private final OldTable table;
    private final ServiceDeskAdvancedSearch advancedSearch;

    public HomeViewPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);

        table = OldTable.createByComponentDataAttributeName(driver, wait, homeIssueTableId);
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
        ChartWidget.create(driver, wait, columnChartViewId).waitForPresenceAndVisibility(columnChartViewId);
    }

    @Step("Refresh column chart")
    public void refreshColumnChart() {
        Button button = Button.createByXpath(columnChartRefreshButtonId, "li", CSSUtils.DATA_WIDGET_ID, driver);
        button.click();
    }

    @Step("Waiting for pie chart presence")
    public void seePieChartIsDisplayed() {
        log.info("Waiting for pie chart presence");
        DelayUtils.waitForPageToLoad(driver, wait);
        ChartWidget.create(driver, wait, pieChartViewId).waitForPresenceAndVisibility(pieChartViewId);
    }

    @Step("Refresh pie chart")
    public void refreshPieChart() {
        log.info("Refreshing pie chart");
        Button button = Button.createByXpath(pieChartRefreshButtonId, "li", CSSUtils.DATA_WIDGET_ID, driver);
        button.click();
    }

    @Step("Set value of issueType multiComboBox")
    public void setValueOfIssueTypeBox(String issueType) {
        DelayUtils.waitForPageToLoad(driver, wait);
        Input issueTypeComboBox = advancedSearch.getComponent("issue_type", Input.ComponentType.MULTI_COMBOBOX);
        log.info("Setting value of Issue Type: {}", issueType);
        issueTypeComboBox.setValue(Data.createSingleData(issueType));
        DelayUtils.sleep();
    }

    @Step("Set value of Issue Id multiSearch")
    public void setValueOfIssueIdSearch() {
        Input issueIdSearch = advancedSearch.getComponent("id", Input.ComponentType.MULTI_SEARCH_FIELD);

        String firstIdInTable = table.getCellValue(0, "Issue Id");
        log.info("Setting value of Issue Id");
        issueIdSearch.setValue(Data.createSingleData(firstIdInTable));
        DelayUtils.sleep();
    }

    @Step("Set value in time period chooser")
    public void setValueInTimePeriodChooser(String widgetId, int days, int hours, int minutes) {
        TimePeriodChooser timePeriod = TimePeriodChooser.create(driver, wait, widgetId);

        timePeriod.chooseOption(TimePeriodChooser.TimePeriodChooserOption.LAST);
        log.info("Setting value ine the time period chooser");
        timePeriod.setLastPeriod(days, hours, minutes);
        DelayUtils.sleep();
    }

    @Step("Check data in scenario table {issueType} is empty")
    public Boolean checkDataInScenarioTable() {
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Checking if scenario table is empty");
        return table.hasNoData();
    }

    @Step("Clear multiComboBox")
    public void clearMultiComboBox(String multiComboBoxId) {
        Input issueTypeComboBox = advancedSearch.getComponent(multiComboBoxId, Input.ComponentType.MULTI_COMBOBOX);
        DelayUtils.waitForPageToLoad(driver, wait);
        issueTypeComboBox.clear();
        log.info("Clearing multicombobox");
    }

    @Step("Clear multiSearch")
    public void clearMultiSearch(String multiSearchId) {
        Input issueIdSearch = advancedSearch.getComponent(multiSearchId, Input.ComponentType.MULTI_SEARCH_FIELD);
        issueIdSearch.clear();
        log.info("Clearing multisearch");
    }

    @Step("Clear time period chooser")
    public void clearTimePeriod(String widgetId) {
        TimePeriodChooser timePeriod = TimePeriodChooser.create(driver, wait, widgetId);
        timePeriod.clickClearValue();
        log.info("Clearing time period chooser");
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

    @Step("Refresh Issues table")
    public void refreshIssuesTable() {
        Button button = Button.createByXpath(issuesTableRefreshButtonId, "li", CSSUtils.DATA_WIDGET_ID, driver);
        button.click();
        log.info("Clicking refresh issues table button");
    }
}

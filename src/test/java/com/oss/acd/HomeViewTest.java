package com.oss.acd;

import com.oss.BaseTestCase;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.acd.BaseACDPage;
import com.oss.pages.acd.homeView.HomeViewPage;
import com.oss.utils.TestListener;
import io.qameta.allure.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners({TestListener.class})
public class HomeViewTest extends BaseTestCase {

    private static final Logger log = LoggerFactory.getLogger(HomeViewTest.class);

    private HomeViewPage homeViewPage;
    private BaseACDPage baseACDPage;

    @BeforeClass
    public void goToHomeView() {
        homeViewPage = HomeViewPage.goToPage(driver, BASIC_URL);
        baseACDPage = new BaseACDPage(driver, webDriverWait);
    }

    @Test(priority = 1, testName = "Check column chart", description = "Check column chart")
    @Description("Check column chart")
    public void checkColumnChart() {
        baseACDPage.maximizeWindow("ColumnChartWindowId");
        homeViewPage.seeColumnChartIsDisplayed();
        //homeViewPage.refreshColumnChart();
        homeViewPage.seeColumnChartIsDisplayed();
        baseACDPage.minimizeWindow("ColumnChartWindowId");
    }

    @Test(priority = 2, testName = "Check pie chart", description = "Check pie chart")
    @Description("Check pie chart")
    public void checkPieChart() {
        baseACDPage.maximizeWindow("PieChartTableWindowId");
        homeViewPage.seePieChartIsDisplayed();
        //homeViewPage.refreshPieChart();
        homeViewPage.seePieChartIsDisplayed();
        baseACDPage.minimizeWindow("PieChartTableWindowId");
    }

    @Test(priority = 3, testName = "Check if Situations exist", description = "Check if Situations exist")
    @Description("Check if Situations exist")
    public void situationCheck() {
        baseACDPage.maximizeWindow("IssueTableWindowId");
        checkScenarioTableWithFilters("Situation");
    }

    @Test(priority = 4, testName = "Check if Anomalies exist", description = "Check if Anomalies exist")
    @Description("Check if Anomalies exist")
    public void anomalyCheck() {
        DelayUtils.sleep();
        checkScenarioTableWithFilters("Anomaly");
    }

    @Test(priority = 5, testName = "Check if Problems exist", description = "Check if Problems exist")
    @Description("Check if Problems exist")
    public void problemCheck() {
        DelayUtils.sleep();
        checkScenarioTableWithFilters("Problem");
    }

    @Test(priority = 6, testName = "Check if issues table is refreshed and minimize window", description = "Check if issues table is refreshed and minimize window")
    @Description("Check if issues table is refreshed and minimize window")
    public void refreshIssuesTable() {
        //homeViewPage.refreshIssuesTable();
        baseACDPage.minimizeWindow("IssueTableWindowId");
    }

    private void checkScenarioTableWithFilters(String issueType) {

        homeViewPage.setValueInMultiComboBox("issue_type", issueType);

        if (!homeViewPage.isDataInScenarioTable()) {
            homeViewPage.clearMultiComboBox("issue_type");
            log.error("Table doesn't have data for issueType: " + issueType);
            Assert.fail();
        }

        //homeViewPage.setValueInMultiComboBox("creation_type", "Automatically");
        //homeViewPage.checkValueOfCreationTypeAttribute();
        homeViewPage.setValueInTimePeriodChooser("create_time", 1, 2, 3);

        if (!homeViewPage.isDataInScenarioTable()) {
            homeViewPage.clearTimePeriod("create_time");
            homeViewPage.clearMultiComboBox("issue_type");
            log.error("Table doesn't have data for provided filters");
            Assert.fail();
        }
        homeViewPage.setValueOfIssueIdSearch();

        if (!homeViewPage.isDataInScenarioTable()) {
            homeViewPage.clearTimePeriod("create_time");
            homeViewPage.clearMultiComboBox("issue_type");
            homeViewPage.clearMultiSearch("id");
            Assert.fail();
        }

        DelayUtils.sleep();
        homeViewPage.clearTimePeriod("create_time");
        homeViewPage.clearMultiComboBox("issue_type");
        homeViewPage.clearMultiSearch("id");
    }
}
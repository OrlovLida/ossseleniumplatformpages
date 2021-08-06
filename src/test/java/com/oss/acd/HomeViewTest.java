package com.oss.acd;

import com.oss.BaseTestCase;
import com.oss.framework.utils.DelayUtils;
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

    @BeforeClass
    public void goToHomeView() {
        homeViewPage = HomeViewPage.goToPage(driver, BASIC_URL);
    }

    @Test(priority = 1, testName = "Check column chart", description = "Check column chart")
    @Description("Check column chart")
    public void checkColumnChart() {
        homeViewPage.seeColumnChartIsDisplayed();
    }

    @Test(priority = 2, testName = "Check if column chart is refreshed", description = "Check if column chart is refreshed")
    @Description("Check if column chart is refreshed")
    public void refreshColumnChart() {
        homeViewPage.refreshColumnChart();
        homeViewPage.seeColumnChartIsDisplayed();
    }

    @Test(priority = 3, testName = "Check if column chart is maximized", description = "Check if column chart is maximized")
    @Description("Check if column chart is maximized")
    public void maximizeColumnChart() {
        homeViewPage.maximizeColumnChart("ColumnChartWindowId");
        homeViewPage.seeColumnChartIsDisplayed();
    }

    @Test(priority = 4, testName = "Check if column chart is minimized", description = "Check if column chart is minimized")
    @Description("Check if column chart is minimized")
    public void minimizeColumnChart() {
        homeViewPage.minimizeColumnChart("ColumnChartWindowId");
        homeViewPage.seeColumnChartIsDisplayed();
    }

    @Test(priority = 5, testName = "Check pie chart", description = "Check pie chart")
    @Description("Check pie chart")
    public void checkPieChart() {
        homeViewPage.seePieChartIsDisplayed();
    }

    @Test(priority = 6, testName = "Check if pie chart is maximized", description = "Check if pie chart is maximized")
    @Description("Check if pie chart is maximized")
    public void maximizePieChart() {
        homeViewPage.maximizePieChart("PieChartTableWindowId");
        homeViewPage.seePieChartIsDisplayed();
    }

    @Test(priority = 7, testName = "Check if pie chart is refreshed", description = "Check if pie chart is refreshed")
    @Description("Check if pie chart is refreshed")
    public void refreshPieChart() {
        homeViewPage.refreshPieChart();
        homeViewPage.seePieChartIsDisplayed();
    }

    @Test(priority = 8, testName = "Check if pie chart is minimized", description = "Check if pie chart is minimized")
    @Description("Check if pie chart is minimized")
    public void minimizePieChart() {
        homeViewPage.minimizePieChart("PieChartTableWindowId");
        homeViewPage.seePieChartIsDisplayed();
    }

    @Test(priority = 9, testName = "Check if issues table is maximized", description = "Check if issues table is maximized")
    @Description("Check if issues table is maximized")
    public void maximizeIssuesTable() {
        homeViewPage.maximizeIssuesTable("IssueTableWindowId");
    }

    @Test(priority = 10, testName = "Check if Situations exist", description = "Check if Situations exist")
    @Description("Check if Situations exist")
    public void situationCheck() {
        checkScenarioTableWithFilters("Situation");
    }

    @Test(priority = 11, testName = "Check if Anomalies exist", description = "Check if Anomalies exist")
    @Description("Check if Anomalies exist")
    public void anomalyCheck() {
        checkScenarioTableWithFilters("Anomaly");
    }

    @Test(priority = 12, testName = "Check if Problems exist", description = "Check if Problems exist")
    @Description("Check if Problems exist")
    public void problemCheck() {
        checkScenarioTableWithFilters("Problem");
    }

    @Test(priority = 13, testName = "Check if issues table is minimized", description = "Check if issues table is minimized")
    @Description("Check if issues table is minimized")
    public void minimizeIssuesTable() {
        homeViewPage.minimizeIssuesTable("IssueTableWindowId");
    }

    @Test(priority = 14, testName = "Check if issues table is refreshed", description = "Check if issues table is refreshed")
    @Description("Check if issues table is refreshed")
    public void refreshIssuesTable() {
        homeViewPage.refreshIssuesTable();
    }

    private void checkScenarioTableWithFilters(String issueType) {
        homeViewPage.setValueOfIssueTypeBox(issueType);

        if (homeViewPage.checkDataInScenarioTable()) {
            homeViewPage.clearMultiComboBox("issue_type");
            log.error("Table doesn't have data for issueType: " + issueType);
            Assert.fail();
        } else {
            homeViewPage.setValueOfIssueIdSearch();
            homeViewPage.setValueInTimePeriodChooser("create_time", 1, 12, 33);

            DelayUtils.sleep();

            Boolean hasNoData = homeViewPage.checkDataInScenarioTable();

            homeViewPage.clearMultiComboBox("issue_type");
            homeViewPage.clearMultiSearch("id");
            homeViewPage.clearTimePeriod("create_time");

            Assert.assertFalse(hasNoData);
        }
    }
}
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
        homeViewPage.maximizeWindow("ColumnChartWindowId");
        homeViewPage.seeColumnChartIsDisplayed();
        homeViewPage.refreshColumnChart();
        homeViewPage.seeColumnChartIsDisplayed();
        homeViewPage.minimizeWindow("ColumnChartWindowId");
    }

    @Test(priority = 2, testName = "Check pie chart", description = "Check pie chart")
    @Description("Check pie chart")
    public void checkPieChart() {
        homeViewPage.maximizeWindow("PieChartTableWindowId");
        homeViewPage.seePieChartIsDisplayed();
        homeViewPage.refreshPieChart();
        homeViewPage.seePieChartIsDisplayed();
        homeViewPage.minimizeWindow("PieChartTableWindowId");
    }

    @Test(priority = 3, testName = "Check if Situations exist", description = "Check if Situations exist")
    @Description("Check if Situations exist")
    public void situationCheck() {
        homeViewPage.maximizeWindow("IssueTableWindowId");
        checkScenarioTableWithFilters("Situation");
    }

    @Test(priority = 4, testName = "Check if Anomalies exist", description = "Check if Anomalies exist")
    @Description("Check if Anomalies exist")
    public void anomalyCheck() {
        checkScenarioTableWithFilters("Anomaly");
    }

    @Test(priority = 5, testName = "Check if Problems exist", description = "Check if Problems exist")
    @Description("Check if Problems exist")
    public void problemCheck() {
        checkScenarioTableWithFilters("Problem");
    }

    @Test(priority = 6, testName = "Check if issues table is refreshed and minimize window", description = "Check if issues table is refreshed and minimize window")
    @Description("Check if issues table is refreshed and minimize window")
    public void refreshIssuesTable() {
        homeViewPage.refreshIssuesTable();
        homeViewPage.minimizeWindow("IssueTableWindowId");
    }

    private void checkScenarioTableWithFilters(String issueType) {
        homeViewPage.setValueOfIssueTypeBox(issueType);

        if (homeViewPage.checkDataInScenarioTable()) {
            homeViewPage.clearMultiComboBox("issue_type");
            log.error("Table doesn't have data for issueType: " + issueType);
            Assert.fail();
        } else {
            homeViewPage.setValueOfIssueIdSearch();
            homeViewPage.setValueInTimePeriodChooser("create_time", 2, 12, 33);

            DelayUtils.sleep();

            Boolean hasNoData = homeViewPage.checkDataInScenarioTable();

            homeViewPage.clearMultiComboBox("issue_type");
            homeViewPage.clearMultiSearch("id");
            homeViewPage.clearTimePeriod("create_time");

            Assert.assertFalse(hasNoData);
        }
    }
}

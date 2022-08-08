package com.oss.iaa.acd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.iaa.acd.BaseACDPage;
import com.oss.pages.iaa.acd.homeview.HomeViewPage;
import com.oss.utils.TestListener;

import io.qameta.allure.Description;

@Listeners({TestListener.class})
public class HomeViewTest extends BaseTestCase {

    private static final Logger log = LoggerFactory.getLogger(HomeViewTest.class);

    private static final String COLUMN_CHART_WINDOW_ID = "ColumnChartWindowId";
    private static final String PIE_CHART_WINDOW_ID = "PieChartTableWindowId";
    private static final String ISSUE_TABLE_WINDOW_ID = "IssueTableWindowId";

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
        try {
            baseACDPage.maximizeWindow(COLUMN_CHART_WINDOW_ID);
            Assert.assertTrue(baseACDPage.checkCardMaximize(COLUMN_CHART_WINDOW_ID));
            homeViewPage.seeColumnChartIsDisplayed();
            baseACDPage.minimizeWindow(COLUMN_CHART_WINDOW_ID);
            Assert.assertFalse(baseACDPage.checkCardMaximize(COLUMN_CHART_WINDOW_ID));
        } catch (Exception e) {
            log.error(e.getMessage());
            Assert.fail();
        }
    }

    @Test(priority = 2, testName = "Check pie chart", description = "Check pie chart")
    @Description("Check pie chart")
    public void checkPieChart() {
        try {
            baseACDPage.maximizeWindow(PIE_CHART_WINDOW_ID);
            Assert.assertTrue(baseACDPage.checkCardMaximize(PIE_CHART_WINDOW_ID));
            homeViewPage.seePieChartIsDisplayed();
            baseACDPage.minimizeWindow(PIE_CHART_WINDOW_ID);
            Assert.assertFalse(baseACDPage.checkCardMaximize(PIE_CHART_WINDOW_ID));
        } catch (Exception e) {
            log.error(e.getMessage());
            Assert.fail();
        }
    }

    @Test(priority = 3, testName = "Check if Situations exist", description = "Check if Situations exist")
    @Description("Check if Situations exist")
    public void situationCheck() {
        baseACDPage.maximizeWindow(ISSUE_TABLE_WINDOW_ID);
        Assert.assertTrue(baseACDPage.checkCardMaximize(ISSUE_TABLE_WINDOW_ID));
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
        baseACDPage.minimizeWindow(ISSUE_TABLE_WINDOW_ID);
        Assert.assertFalse(baseACDPage.checkCardMaximize(ISSUE_TABLE_WINDOW_ID));
    }

    private void checkScenarioTableWithFilters(String issueType) {

        homeViewPage.setAttributeValue("issue_type", issueType);

        if (!homeViewPage.isDataInScenarioTable()) {
            homeViewPage.clearAttributeValue("issue_type");
            log.error("Table doesn't have data for issueType: " + issueType);
            Assert.fail();
        }

        homeViewPage.setValueInTimePeriodChooser("input_create_time", 1, 2, 3);

        if (!homeViewPage.isDataInScenarioTable()) {
            homeViewPage.clearTimePeriod("input_create_time");
            homeViewPage.clearAttributeValue("issue_type");
            log.error("Table doesn't have data for provided filters");
            Assert.fail();
        }
        homeViewPage.setValueOfIssueIdSearch();

        if (!homeViewPage.isDataInScenarioTable()) {
            homeViewPage.clearTimePeriod("input_create_time");
            homeViewPage.clearAttributeValue("issue_type");
            homeViewPage.clearAttributeValue("id");
            Assert.fail();
        }

        DelayUtils.sleep();
        homeViewPage.clearTimePeriod("input_create_time");
        homeViewPage.clearAttributeValue("issue_type");
        homeViewPage.clearAttributeValue("id");
    }
}
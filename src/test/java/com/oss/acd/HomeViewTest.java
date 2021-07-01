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

    @Test(priority = 3, testName = "Check pie chart", description = "Check pie chart")
    @Description("Check pie chart")
    public void checkPieChart() {
        homeViewPage.seePieChartIsDisplayed();
    }

    @Test(priority = 4, testName = "Check if pie chart is refreshed", description = "Check if pie chart is refreshed")
    @Description("Check if pie chart is refreshed")
    public void refreshPieChart() {
        homeViewPage.refreshPieChart();
        homeViewPage.seePieChartIsDisplayed();
    }

    @Test(priority = 5, testName = "Check if situation exist", description = "Check if situation exist")
    @Description("Check if situation exist")
    public void situationCheck() {
        checkScenarioTableWithFilters("Situation");
    }

    @Test(priority = 6, testName = "Check if Anomaly exist", description = "Check if Anomaly exist")
    @Description("Check if Anomaly exist")
    public void anomalyCheck() {
        checkScenarioTableWithFilters("Anomaly");
    }

    @Test(priority = 7, testName = "Check if problem exist", description = "Check if problem exist")
    @Description("Check if problem exist")
    public void problemCheck() {
        checkScenarioTableWithFilters("Problem");
    }

    private void checkScenarioTableWithFilters(String issueType) {
        homeViewPage.setValueOfIssueTypeBox(issueType);

        if (homeViewPage.checkDataInScenarioTable()) {
            homeViewPage.clearMultiComboBox("issue_type");
            log.error("Table doesn't have data for issueType: " + issueType);
            Assert.fail();
        } else {
            homeViewPage.setValueOfIssueIdSearch();
            homeViewPage.setValueInTimePeriodChooser("create_time");

            DelayUtils.sleep();

            Boolean hasNoData = homeViewPage.checkDataInScenarioTable();

            homeViewPage.clearMultiComboBox("issue_type");
            homeViewPage.clearMultiSearch("id");
            homeViewPage.clearTimePeriod("create_time");

            Assert.assertFalse(hasNoData);
        }
    }
}
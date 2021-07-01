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

    @Test(priority = 2, testName = "Check column chart should be refreshed", description = "Check column chart should be refreshed")
    @Description("Check column chart should be refreshed")
    public void refreshColumnChart() {
        homeViewPage.refreshColumnChart();
        homeViewPage.seeColumnChartIsDisplayed();
    }

    @Test(priority = 3, testName = "Check pie char", description = "Check pie char")
    @Description("Check pie char")
    public void checkPieChart() {
        homeViewPage.seePieChartIsDisplayed();
    }

    @Test(priority = 4, testName = "Check pie chart should be refreshed", description = "Check pie chart should be refreshed")
    @Description("Check pie chart should be refreshed")
    public void refreshPieChart() {
        homeViewPage.refreshPieChart();
        homeViewPage.seePieChartIsDisplayed();
    }

    @Test(priority = 5, testName = "Check is situation exist", description = "Check is situation exist")
    @Description("Check is situation exist")
    public void situationCheck() {
        checkScenarioTableWithFilters("Situation");
    }

    @Test(priority = 6, testName = "Check is violation exist", description = "Check is violation exist")
    @Description("Check is Anomaly exist")
    public void anomalyCheck() {
        checkScenarioTableWithFilters("Anomaly");
    }

    @Test(priority = 7, testName = "Check is problem exist", description = "Check is problem exist")
    @Description("Check is problem exist")
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
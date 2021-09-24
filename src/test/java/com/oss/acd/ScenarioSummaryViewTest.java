package com.oss.acd;

import com.oss.BaseTestCase;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.acd.scenarioSummaryView.ScenarioSummaryViewPage;
import com.oss.utils.TestListener;
import io.qameta.allure.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners({TestListener.class})
public class ScenarioSummaryViewTest extends BaseTestCase {

    private static final Logger log = LoggerFactory.getLogger(ScenarioSummaryViewTest.class);

    private ScenarioSummaryViewPage scenarioSummaryViewPage;

    private String issuesTableRefreshButtonId = "DetectedIssuesButtonId-1";

    @BeforeClass
    public void goToASDScenarioSummaryView() {
        scenarioSummaryViewPage = ScenarioSummaryViewPage.goToPage(driver, BASIC_URL);
    }

    @Test(priority = 1, testName = "Add new predefined filter", description = "Add new predefined filter")
    @Description("Add new predefined filter")
    public void addPredefinedFilter() {
        log.info("Waiting in method addPredefinedFilter");
        scenarioSummaryViewPage.maximizeWindow("PredefinedFiltersWindowId");
        scenarioSummaryViewPage.seePredefinedFilterAreaIsDisplayed();
        scenarioSummaryViewPage.clickAddPredefinedFilter();
        scenarioSummaryViewPage.chooseVisualizationType("Chart");
        log.info("Visualization type - CHART - set");
        scenarioSummaryViewPage.chooseAttribute("Severity");
        log.info("Attribute name - Severity - set");
        scenarioSummaryViewPage.insertAttributeValueToMultiComboBoxComponent("Cleared", "attribute1ValuesId");
        log.info("Attribute Value - Cleared - set");
        scenarioSummaryViewPage.insertAttributeValueToMultiComboBoxComponent("Major", "attribute1ValuesId");
        log.info("Attribute Value - Major - set");
        scenarioSummaryViewPage.insertAttributeValueToMultiComboBoxComponent("Critical", "attribute1ValuesId");
        log.info("Attribute Value - Critical - set");
        scenarioSummaryViewPage.savePredefinedFilter();
        log.info("Predefined filter has been added successfully");
    }

    @Test(priority = 2, testName = "Delete predefined filter", description = "Delete predefined filter")
    @Description("Delete predefined filter")
    public void deletePredefinedFilter() {
        log.info("Waiting in method deletePredefinedFilter");
        scenarioSummaryViewPage.deletePredefinedFilter();
        scenarioSummaryViewPage.minimizeWindow("PredefinedFiltersWindowId");
    }

    @Test(priority = 3, testName = "Check if ASD issues exist", description = "Check if ASD issues exists")
    @Description("Check if ASD issues exist")
    public void asdDetectedIssuesTableCheck() {
        scenarioSummaryViewPage.maximizeWindow("DetectedIssuesWindowId");
        checkIssuesTableWithFilters();
    }

    @Test(priority = 4, testName = "Check if issues table is refreshed and minimize window", description = "Check if issues table is refreshed and minimize window")
    @Description("Check if issues table is refreshed and minimize window")
    public void refreshDetectedIssuesTable() {
        scenarioSummaryViewPage.refreshIssuesTable(issuesTableRefreshButtonId);
        scenarioSummaryViewPage.minimizeWindow("DetectedIssuesWindowId");
    }

    private void checkIssuesTableWithFilters() {
        if (scenarioSummaryViewPage.checkDataInIssuesTable()) {
            log.info("table doesn't have data for issues with roots");
            scenarioSummaryViewPage.turnOnSwitcher();
            if (scenarioSummaryViewPage.checkDataInIssuesTable()) {
                log.error("table doesn't have data for issues without roots");
                Assert.fail();
            } else {
                log.info("table contains data for issues without roots");
                scenarioSummaryViewPage.setValueOfIssueStateBox("Automatically");
                scenarioSummaryViewPage.setValueInTimePeriodChooser("create_time", 3, 12, 33);
                scenarioSummaryViewPage.setValueOfIssueIdSearch();
            }

        } else {
            log.info("table contains data for issues with roots");

            scenarioSummaryViewPage.turnOnSwitcher();
            scenarioSummaryViewPage.setValueOfIssueStateBox("Automatically");
            scenarioSummaryViewPage.setValueInTimePeriodChooser("create_time", 3, 12, 33);
            scenarioSummaryViewPage.setValueOfIssueIdSearch();

            DelayUtils.sleep();

            Boolean hasNoData = scenarioSummaryViewPage.checkDataInIssuesTable();

            scenarioSummaryViewPage.clearMultiComboBox("creation_type");
            scenarioSummaryViewPage.clearMultiSearch("id");
            scenarioSummaryViewPage.clearTimePeriod("create_time");

            Assert.assertFalse(hasNoData);
        }
    }
}

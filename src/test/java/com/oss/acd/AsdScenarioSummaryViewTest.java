package com.oss.acd;

import com.oss.BaseTestCase;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.acd.BaseACDPage;
import com.oss.pages.acd.scenarioSummaryView.AsdScenarioSummaryViewPage;
import com.oss.utils.TestListener;
import io.qameta.allure.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners({TestListener.class})
public class AsdScenarioSummaryViewTest extends BaseTestCase {

    private static final Logger log = LoggerFactory.getLogger(AsdScenarioSummaryViewTest.class);

    private AsdScenarioSummaryViewPage asdScenarioSummaryViewPage;
    private BaseACDPage baseACDPage;

    private final String asdScenarioSummaryViewSuffixUrl = "%s/#/view/acd/asd";
    private final String severityAttributeValue = "attribute1ValuesId";
    private String issuesTableRefreshButtonId = "DetectedIssuesButtonId-1";

    @BeforeClass
    public void goToASDScenarioSummaryView() {
        asdScenarioSummaryViewPage = AsdScenarioSummaryViewPage.goToPage(driver, asdScenarioSummaryViewSuffixUrl, BASIC_URL);
        baseACDPage = new BaseACDPage(driver, webDriverWait);
    }

    @Test(priority = 1, testName = "Add new predefined filter", description = "Add new predefined filter")
    @Description("Add new predefined filter")
    public void addPredefinedFilter() {
        log.info("Waiting in method addPredefinedFilter");
        baseACDPage.maximizeWindow("PredefinedFiltersWindowId");
        baseACDPage.clickAddPredefinedFilter();
        baseACDPage.chooseVisualizationType("Chart");
        log.info("Visualization type - CHART - set");
        baseACDPage.chooseAttribute("Severity");
        log.info("Attribute name - Severity - set");
        baseACDPage.insertAttributeValueToMultiComboBoxComponent("Cleared", severityAttributeValue);
        log.info("Attribute Value - Cleared - set");
        baseACDPage.insertAttributeValueToMultiComboBoxComponent("Major", severityAttributeValue);
        log.info("Attribute Value - Major - set");
        baseACDPage.insertAttributeValueToMultiComboBoxComponent("Critical", severityAttributeValue);
        log.info("Attribute Value - Critical - set");
        baseACDPage.savePredefinedFilter();
        log.info("Predefined filter has been added successfully");
    }

    @Test(priority = 2, testName = "Delete predefined filter", description = "Delete predefined filter")
    @Description("Delete predefined filter")
    public void deletePredefinedFilter() {
        log.info("Waiting in method deletePredefinedFilter");
        baseACDPage.deletePredefinedFilter();
        baseACDPage.minimizeWindow("PredefinedFiltersWindowId");
    }

    @Test(priority = 3, testName = "Check if ASD issues exist", description = "Check if ASD issues exists")
    @Description("Check if ASD issues exist")
    public void asdDetectedIssuesTableCheck() {
        baseACDPage.maximizeWindow("DetectedIssuesWindowId");
        checkIssuesTableWithFilters();
    }

    @Test(priority = 4, testName = "Check if issues table is refreshed and minimize window", description = "Check if issues table is refreshed and minimize window")
    @Description("Check if issues table is refreshed and minimize window")
    public void refreshDetectedIssuesTable() {
        baseACDPage.refreshIssuesTable(issuesTableRefreshButtonId);
        baseACDPage.minimizeWindow("DetectedIssuesWindowId");
    }

    private void checkIssuesTableWithFilters() {
        if (baseACDPage.checkDataInIssuesTable()) {
            log.info("table doesn't have data for issues with roots");
            baseACDPage.turnOnSwitcher();
            if (baseACDPage.checkDataInIssuesTable()) {
                log.error("table doesn't have data for issues without roots");
                Assert.fail();
            } else {
                log.info("table contains data for issues without roots");
                baseACDPage.setValueInMultiComboBox("creation_type", "Automatically");
                baseACDPage.setValueInTimePeriodChooser("create_time", 3, 12, 33);
                baseACDPage.setValueOfIssueIdSearch();

            }

        } else {
            log.info("table contains data for issues with roots");

            baseACDPage.turnOnSwitcher();
            baseACDPage.setValueInMultiComboBox("creation_type", "Automatically");
            baseACDPage.setValueInTimePeriodChooser("create_time", 3, 12, 33);
            baseACDPage.setValueOfIssueIdSearch();

            DelayUtils.sleep();

            Boolean hasNoData = baseACDPage.checkDataInIssuesTable();

            baseACDPage.clearMultiComboBox("creation_type");
            baseACDPage.clearMultiSearch("id");
            baseACDPage.clearTimePeriod("create_time");

            Assert.assertFalse(hasNoData);
        }
    }
}

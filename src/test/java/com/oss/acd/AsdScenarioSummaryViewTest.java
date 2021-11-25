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
    private String issuesTableRefreshButtonId = "undefined-1"; // "DetectedIssuesButtonId-1";

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
        asdScenarioSummaryViewPage.chooseVisualizationType("Chart");
        log.info("Visualization type - CHART - set");
        asdScenarioSummaryViewPage.chooseAttribute("Severity");
        log.info("Attribute name - Severity - set");
        baseACDPage.insertAttributeValueToMultiComboBoxComponent("Cleared", severityAttributeValue);
        log.info("Attribute Value - Cleared - set");
        baseACDPage.insertAttributeValueToMultiComboBoxComponent("Major", severityAttributeValue);
        log.info("Attribute Value - Major - set");
        baseACDPage.insertAttributeValueToMultiComboBoxComponent("Critical", severityAttributeValue);
        log.info("Attribute Value - Critical - set");
        asdScenarioSummaryViewPage.savePredefinedFilter();
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

        if (!asdScenarioSummaryViewPage.isDataInIssuesTable()) {
            log.info("table doesn't have data for issues with roots");
            baseACDPage.turnOnSwitcher();
            if (!asdScenarioSummaryViewPage.isDataInIssuesTable()) {
                log.error("table doesn't have data for issues without roots");
                Assert.fail();
            } else {
                log.info("table contains data for issues without roots");
                asdScenarioSummaryViewPage.setValueInMultiComboBox("creation_type", "Automatically");
                baseACDPage.setValueInTimePeriodChooser("create_time", 3, 12, 33);
                asdScenarioSummaryViewPage.setValueOfIssueIdSearch();
            }

        } else {
            log.info("table contains data for issues with roots");
            baseACDPage.turnOnSwitcher();
            asdScenarioSummaryViewPage.setValueInMultiComboBox("creation_type", "Automatically");
            baseACDPage.setValueInTimePeriodChooser("create_time", 3, 12, 33);
            asdScenarioSummaryViewPage.setValueOfIssueIdSearch();

            if (!asdScenarioSummaryViewPage.isDataInIssuesTable()) {
                asdScenarioSummaryViewPage.clearMultiComboBox("creation_type");
                asdScenarioSummaryViewPage.clearMultiSearch("id");
                baseACDPage.clearTimePeriod("create_time");
                log.error("Table doesn't have data for provided filters");
                Assert.fail();
            }

            DelayUtils.sleep();
            asdScenarioSummaryViewPage.clearMultiComboBox("creation_type");
            asdScenarioSummaryViewPage.clearMultiSearch("id");
            baseACDPage.clearTimePeriod("create_time");
        }
    }
}

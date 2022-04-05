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
    private String issuesTableRefreshButtonId = "undefined-1";
    private String PREDEFINED_FILTERS_WINDOW_ID = "PredefinedFiltersWindowId";
    private final String DETECTED_ISSUES_WINDOW_ID = "DetectedIssuesWindowId";
    private final String WIZARD_TYPE_ID = "widgetType-input";
    private final String WIZARD_ATTRIBUTE_ID = "attribute1Id-input";
    private final String WIZARD_PREDEFINED_FILTERS_ID = "addPredefinedFilterId_prompt-card";
    private final String WIZARD_ATTRIBUTE_VALUES_ID = "attribute1ValuesId";

    @BeforeClass
    public void goToASDScenarioSummaryView() {
        asdScenarioSummaryViewPage = AsdScenarioSummaryViewPage.goToPage(driver, asdScenarioSummaryViewSuffixUrl, BASIC_URL);
        baseACDPage = new BaseACDPage(driver, webDriverWait);
    }

    @Test(priority = 1, testName = "Resize predefined filters area", description = "Resize predefined filters area")
    @Description("Resize predefined filters area")
    public void resizePredefinedFiltersArea() {
        baseACDPage.maximizeWindow(PREDEFINED_FILTERS_WINDOW_ID);
        Assert.assertTrue(baseACDPage.checkCardMaximize(PREDEFINED_FILTERS_WINDOW_ID));
        baseACDPage.minimizeWindow(PREDEFINED_FILTERS_WINDOW_ID);
        Assert.assertFalse(baseACDPage.checkCardMaximize(PREDEFINED_FILTERS_WINDOW_ID));
    }

    @Test(priority = 2, testName = "Check if ASD issues exist", description = "Check if ASD issues exists")
    @Description("Check if ASD issues exist")
    public void asdDetectedIssuesTableCheck() {
        baseACDPage.maximizeWindow(DETECTED_ISSUES_WINDOW_ID);
        Assert.assertTrue(baseACDPage.checkCardMaximize(DETECTED_ISSUES_WINDOW_ID));
        checkIssuesTableWithFilters();
    }

    @Test(priority = 3, testName = "Check if issues table is refreshed and minimize window", description = "Check if issues table is refreshed and minimize window")
    @Description("Check if issues table is refreshed and minimize window")
    public void refreshDetectedIssuesTable() {
        baseACDPage.refreshIssuesTable(issuesTableRefreshButtonId);
        baseACDPage.minimizeWindow(DETECTED_ISSUES_WINDOW_ID);
        Assert.assertFalse(baseACDPage.checkCardMaximize(DETECTED_ISSUES_WINDOW_ID));
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
                baseACDPage.setValueInMultiComboBox("creation_type", "Automatically");
                baseACDPage.setValueInTimePeriodChooser("create_time", 3, 12, 33);
                asdScenarioSummaryViewPage.setValueOfIssueIdSearch();
            }
            clearIssueTableFilters();
        } else {
            log.info("table contains data for issues with roots");
            baseACDPage.turnOnSwitcher();
            baseACDPage.setValueInMultiComboBox("creation_type", "Automatically");
            baseACDPage.setValueInTimePeriodChooser("create_time", 3, 12, 33);
            asdScenarioSummaryViewPage.setValueOfIssueIdSearch();

            if (!asdScenarioSummaryViewPage.isDataInIssuesTable()) {
                clearIssueTableFilters();
                log.error("Table doesn't have data for provided filters");
                Assert.fail();
            }
            DelayUtils.sleep();
            clearIssueTableFilters();
        }
    }

    private void clearIssueTableFilters() {
        asdScenarioSummaryViewPage.clearMultiComboBox("creation_type");
        asdScenarioSummaryViewPage.clearMultiSearch("id");
        baseACDPage.clearTimePeriod("create_time");
    }
}

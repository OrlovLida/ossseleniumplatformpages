package com.oss.acd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.acd.scenarioSummaryView.AsdScenarioSummaryViewPage;
import com.oss.utils.TestListener;

import io.qameta.allure.Description;

@Listeners({TestListener.class})
public class AsdScenarioSummaryViewTest extends BaseTestCase {

    private static final Logger log = LoggerFactory.getLogger(AsdScenarioSummaryViewTest.class);

    private AsdScenarioSummaryViewPage asdScenarioSummaryViewPage;

    private static final String asdScenarioSummaryViewSuffixUrl = "%s/#/view/acd/asd";

    private static final String issuesTableRefreshButtonId = "undefined-1";
    private static final String PREDEFINED_FILTERS_WINDOW_ID = "PredefinedFiltersWindowId";
    private static final String DETECTED_ISSUES_WINDOW_ID = "DetectedIssuesWindowId";
    private static final String SWITCHER_ID = "switcherId";

    @BeforeClass
    public void goToASDScenarioSummaryView() {
        asdScenarioSummaryViewPage = AsdScenarioSummaryViewPage.goToPage(driver, asdScenarioSummaryViewSuffixUrl, BASIC_URL);
    }

    @Test(priority = 1, testName = "Resize predefined filters area", description = "Resize predefined filters area")
    @Description("Resize predefined filters area")
    public void resizePredefinedFiltersArea() {
        asdScenarioSummaryViewPage.maximizeWindow(PREDEFINED_FILTERS_WINDOW_ID);
        Assert.assertTrue(asdScenarioSummaryViewPage.checkCardMaximize(PREDEFINED_FILTERS_WINDOW_ID));
        asdScenarioSummaryViewPage.minimizeWindow(PREDEFINED_FILTERS_WINDOW_ID);
        Assert.assertFalse(asdScenarioSummaryViewPage.checkCardMaximize(PREDEFINED_FILTERS_WINDOW_ID));
    }

    @Test(priority = 2, testName = "Check if ASD issues exist", description = "Check if ASD issues exists")
    @Description("Check if ASD issues exist")
    public void asdDetectedIssuesTableCheck() {
        asdScenarioSummaryViewPage.maximizeWindow(DETECTED_ISSUES_WINDOW_ID);
        Assert.assertTrue(asdScenarioSummaryViewPage.checkCardMaximize(DETECTED_ISSUES_WINDOW_ID));
        checkIssuesTableWithFilters();
    }

    @Test(priority = 3, testName = "Check if issues table is refreshed and minimize window", description = "Check if issues table is refreshed and minimize window")
    @Description("Check if issues table is refreshed and minimize window")
    public void refreshDetectedIssuesTable() {
        asdScenarioSummaryViewPage.refreshIssuesTable(issuesTableRefreshButtonId);
        asdScenarioSummaryViewPage.minimizeWindow(DETECTED_ISSUES_WINDOW_ID);
        Assert.assertFalse(asdScenarioSummaryViewPage.checkCardMaximize(DETECTED_ISSUES_WINDOW_ID));
    }

    private void checkIssuesTableWithFilters() {

        if (Boolean.FALSE.equals(asdScenarioSummaryViewPage.isDataInIssuesTable())) {
            log.info("table doesn't have data for issues with roots");
            asdScenarioSummaryViewPage.turnOnSwitcher(SWITCHER_ID);
            if (Boolean.FALSE.equals(asdScenarioSummaryViewPage.isDataInIssuesTable())) {
                log.error("table doesn't have data for issues without roots");
                Assert.fail();
            } else {
                log.info("table contains data for issues without roots");
                asdScenarioSummaryViewPage.setValueInMultiComboBox("creation_type", "Automatically");
                //asdScenarioSummaryViewPage.setValueInTimePeriodChooser("create_time", 3, 12, 33); //TODO after fix ACD-3363
                asdScenarioSummaryViewPage.setValueOfIssueIdSearch();
            }
        } else {
            log.info("table contains data for issues with roots");
            asdScenarioSummaryViewPage.turnOnSwitcher(SWITCHER_ID);
            asdScenarioSummaryViewPage.setValueInMultiComboBox("creation_type", "Automatically");
            //asdScenarioSummaryViewPage.setValueInTimePeriodChooser("create_time", 3, 12, 33); //TODO after fix ACD-3363
            asdScenarioSummaryViewPage.setValueOfIssueIdSearch();

            if (!asdScenarioSummaryViewPage.isDataInIssuesTable()) {
                clearIssueTableFilters();
                log.error("Table doesn't have data for provided filters");
                Assert.fail();
            }
            DelayUtils.sleep();
            clearIssueTableFilters();
        }
        clearIssueTableFilters();
    }

    private void clearIssueTableFilters() {
        asdScenarioSummaryViewPage.clearMultiComboBox("creation_type");
        asdScenarioSummaryViewPage.clearMultiSearch("id");
        //asdScenarioSummaryViewPage.clearTimePeriod("create_time"); //TODO after fix ACD-3363
    }
}

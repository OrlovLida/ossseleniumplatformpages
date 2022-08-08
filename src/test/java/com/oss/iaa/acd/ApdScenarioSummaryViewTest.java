package com.oss.iaa.acd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.iaa.acd.scenariosummaryview.ApdScenarioSummaryViewPage;

import io.qameta.allure.Description;

public class ApdScenarioSummaryViewTest extends BaseTestCase {

    private static final Logger log = LoggerFactory.getLogger(ApdScenarioSummaryViewTest.class);

    private ApdScenarioSummaryViewPage apdScenarioSummaryViewPage;

    private static final String apdScenarioSummaryViewSuffixUrl = "%s/#/view/acd/apd";

    private static final String PREDEFINED_FILTERS_WINDOW_ID = "PredefinedFiltersWindowId";
    private static final String DETECTED_ISSUES_WINDOW_ID = "DetectedIssuesWindowId";
    private static final String SEVERITY_STATE_TABLE_NAME = "Severity/State";
    private static final String LEFT_HEADER_NEW = "New";
    private static final String SWITCHER_ID = "switcherValue";

    @BeforeClass
    public void goToAPDScenarioSummaryView() {
        apdScenarioSummaryViewPage = ApdScenarioSummaryViewPage.goToPage(driver, apdScenarioSummaryViewSuffixUrl, BASIC_URL);
    }

    @Test(priority = 1, testName = "Resize predefined filters area", description = "Resize predefined filters area")
    @Description("Resize predefined filters area")
    public void resizePredefinedFiltersArea() {
        apdScenarioSummaryViewPage.maximizeWindow(PREDEFINED_FILTERS_WINDOW_ID);
        Assert.assertTrue(apdScenarioSummaryViewPage.checkCardMaximize(PREDEFINED_FILTERS_WINDOW_ID));
        apdScenarioSummaryViewPage.minimizeWindow(PREDEFINED_FILTERS_WINDOW_ID);
        Assert.assertFalse(apdScenarioSummaryViewPage.checkCardMaximize(PREDEFINED_FILTERS_WINDOW_ID));
    }

    @Test(priority = 2, testName = "Check if APD issues exist", description = "Check if APD issues exist")
    @Description("Check if APD issues exist")
    public void apdDetectedIssuesTableCheck() {
        apdScenarioSummaryViewPage.maximizeWindow(DETECTED_ISSUES_WINDOW_ID);
        Assert.assertTrue(apdScenarioSummaryViewPage.checkCardMaximize(DETECTED_ISSUES_WINDOW_ID));
        checkApdIssuesTableWithFilter();
        apdScenarioSummaryViewPage.minimizeWindow(DETECTED_ISSUES_WINDOW_ID);
        Assert.assertFalse(apdScenarioSummaryViewPage.checkCardMaximize(DETECTED_ISSUES_WINDOW_ID));
    }

    @Test(priority = 3, testName = "Check Table2D filtering by choosing cell", description = "Check Table2D filtering by choosing cell")
    @Description("Check Table2D filtering by choosing cell")
    public void Table2DFilteringByCellCheck() {
        apdScenarioSummaryViewPage.turnOnSwitcher(SWITCHER_ID);
        apdScenarioSummaryViewPage.selectCellInTable(SEVERITY_STATE_TABLE_NAME, LEFT_HEADER_NEW, "Critical");
        Assert.assertTrue(apdScenarioSummaryViewPage.checkIfFilteringBySelectingCellWorks(SEVERITY_STATE_TABLE_NAME, LEFT_HEADER_NEW, "Critical"));
        apdScenarioSummaryViewPage.clearFiltersInTable(SEVERITY_STATE_TABLE_NAME);
    }

    @Test(priority = 4, testName = "Check Table2D filtering by choosing row", description = "Check Table2D filtering by choosing row")
    @Description("Check Table2D filtering by choosing row")
    public void Table2DFilteringByRowCheck() {
        apdScenarioSummaryViewPage.turnOnSwitcher(SWITCHER_ID);
        apdScenarioSummaryViewPage.selectRowInTable(SEVERITY_STATE_TABLE_NAME, LEFT_HEADER_NEW);
        apdScenarioSummaryViewPage.sumValuesOfSelectedCells(SEVERITY_STATE_TABLE_NAME);
        Assert.assertTrue(apdScenarioSummaryViewPage.checkIfFilteringBySelectingRowOrColumnWorks(SEVERITY_STATE_TABLE_NAME));
        apdScenarioSummaryViewPage.clearFiltersInTable(SEVERITY_STATE_TABLE_NAME);
    }

    private void checkApdIssuesTableWithFilter() {

        if (Boolean.FALSE.equals(apdScenarioSummaryViewPage.isDataInIssuesTable())) {
            log.info("table doesn't have data for issues with roots");
            apdScenarioSummaryViewPage.turnOnSwitcher(SWITCHER_ID);
            if (Boolean.FALSE.equals(apdScenarioSummaryViewPage.isDataInIssuesTable())) {
                log.error("table doesn't have data for issues without roots");
                Assert.fail();
            } else {
                log.info("table contains data for issues without roots");
                apdScenarioSummaryViewPage.setAttributeValue("creation_type", "Automatically");
                //apdScenarioSummaryViewPage.setValueInTimePeriodChooser("create_time", 3, 12, 33); //TODO after fix ACD-3363
                apdScenarioSummaryViewPage.setValueOfIssueIdSearch();
            }
        } else {
            log.info("table contains data for issues with roots");
            apdScenarioSummaryViewPage.turnOnSwitcher(SWITCHER_ID);
            apdScenarioSummaryViewPage.setAttributeValue("creation_type", "Automatically");
            //apdScenarioSummaryViewPage.setValueInTimePeriodChooser("create_time", 3, 12, 33); //TODO after fix ACD-3363
            apdScenarioSummaryViewPage.setValueOfIssueIdSearch();

            if (Boolean.FALSE.equals(apdScenarioSummaryViewPage.isDataInIssuesTable())) {
                clearIssueTableFilters();
                log.error("Table doesn't have data for provided filters");
                Assert.fail();
            }
        }
        DelayUtils.sleep();
        clearIssueTableFilters();
    }

    private void clearIssueTableFilters() {
        apdScenarioSummaryViewPage.clearAttributeValue("creation_type");
        apdScenarioSummaryViewPage.clearAttributeValue("id");
        //apdScenarioSummaryViewPage.clearTimePeriod("create_time"); //TODO after fix ACD-3363
    }
}

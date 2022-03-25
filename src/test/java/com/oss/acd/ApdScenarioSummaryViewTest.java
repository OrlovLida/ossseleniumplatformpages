package com.oss.acd;

import com.oss.BaseTestCase;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.acd.BaseACDPage;
import com.oss.pages.acd.scenarioSummaryView.ApdScenarioSummaryViewPage;
import io.qameta.allure.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class ApdScenarioSummaryViewTest extends BaseTestCase {

    private static final Logger log = LoggerFactory.getLogger(ApdScenarioSummaryViewTest.class);

    private ApdScenarioSummaryViewPage apdScenarioSummaryViewPage;
    private BaseACDPage baseACDPage;

    private final String apdScenarioSummaryViewSuffixUrl = "%s/#/view/acd/apd";

    private final String PREDEFINED_FILTERS_WINDOW_ID = "PredefinedFiltersWindowId";
    private final String DETECTED_ISSUES_WINDOW_ID = "DetectedIssuesWindowId";
    private final String WIZARD_TYPE_ID = "widgetType-input";
    private final String WIZARD_ATTRIBUTE_ID = "attribute1Id-input";
    private final String WIZARD_PREDEFINED_FILTERS_ID = "addPredefinedFilterId_prompt-card";
    private final String WIZARD_ATTRIBUTE_VALUES_ID = "attribute1ValuesId";
    private final String SEVERITY_STATE_TABLE_NAME = "Severity/State";
    private final String LEFT_HEADER_NEW = "New";

    @BeforeClass
    public void goToAPDScenarioSummaryView() {
        apdScenarioSummaryViewPage = ApdScenarioSummaryViewPage.goToPage(driver, apdScenarioSummaryViewSuffixUrl, BASIC_URL);
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

    @Test(priority = 2, testName = "Check if APD issues exist", description = "Check if APD issues exist")
    @Description("Check if APD issues exist")
    public void apdDetectedIssuesTableCheck() {
        baseACDPage.maximizeWindow(DETECTED_ISSUES_WINDOW_ID);
        Assert.assertTrue(baseACDPage.checkCardMaximize(DETECTED_ISSUES_WINDOW_ID));
        checkApdIssuesTableWithFilter();
        baseACDPage.minimizeWindow(DETECTED_ISSUES_WINDOW_ID);
        Assert.assertFalse(baseACDPage.checkCardMaximize(DETECTED_ISSUES_WINDOW_ID));
    }

    @Test(priority = 3, testName = "Check Table2D filtering by choosing cell", description = "Check Table2D filtering by choosing cell")
    @Description("Check Table2D filtering by choosing cell")
    public void Table2DFilteringByCellCheck() {
        baseACDPage.turnOnSwitcher();
        baseACDPage.selectCellInTable(SEVERITY_STATE_TABLE_NAME, LEFT_HEADER_NEW, "Critical");
        Assert.assertTrue(baseACDPage.checkIfFilteringBySelectingCellWorks(SEVERITY_STATE_TABLE_NAME, LEFT_HEADER_NEW, "Critical"));
        baseACDPage.clearFiltersInTable(SEVERITY_STATE_TABLE_NAME);
    }

    @Test(priority = 4, testName = "Check Table2D filtering by choosing row", description = "Check Table2D filtering by choosing row")
    @Description("Check Table2D filtering by choosing row")
    public void Table2DFilteringByRowCheck() {
        baseACDPage.turnOnSwitcher();
        baseACDPage.selectRowInTable(SEVERITY_STATE_TABLE_NAME, LEFT_HEADER_NEW);
        baseACDPage.sumValuesOfSelectedCells(SEVERITY_STATE_TABLE_NAME);
        Assert.assertTrue(baseACDPage.checkIfFilteringBySelectingRowOrColumnWorks(SEVERITY_STATE_TABLE_NAME));
        baseACDPage.clearFiltersInTable(SEVERITY_STATE_TABLE_NAME);
    }

    private void checkApdIssuesTableWithFilter() {

        if (!apdScenarioSummaryViewPage.isDataInIssuesTable()) {
            log.info("table doesn't have data for issues with roots");
            baseACDPage.turnOnSwitcher();
            if (!apdScenarioSummaryViewPage.isDataInIssuesTable()) {
                log.error("table doesn't have data for issues without roots");
                Assert.fail();
            } else {
                log.info("table contains data for issues without roots");
                baseACDPage.setValueInMultiComboBox("creation_type", "Automatically");
                baseACDPage.setValueInTimePeriodChooser("create_time", 3, 12, 33);
                apdScenarioSummaryViewPage.setValueOfIssueIdSearch();
            }
        } else {
            log.info("table contains data for issues with roots");
            baseACDPage.turnOnSwitcher();
            baseACDPage.setValueInMultiComboBox("creation_type", "Automatically");
            baseACDPage.setValueInTimePeriodChooser("create_time", 3, 12, 33);
            apdScenarioSummaryViewPage.setValueOfIssueIdSearch();

            if (!apdScenarioSummaryViewPage.isDataInIssuesTable()) {
                apdScenarioSummaryViewPage.clearMultiComboBox("creation_type");
                apdScenarioSummaryViewPage.clearMultiSearch("id");
                baseACDPage.clearTimePeriod("create_time");
                log.error("Table doesn't have data for provided filters");
                Assert.fail();
            }
            DelayUtils.sleep();
            apdScenarioSummaryViewPage.clearMultiComboBox("creation_type");
            apdScenarioSummaryViewPage.clearMultiSearch("id");
            baseACDPage.clearTimePeriod("create_time");
        }
    }
}

package com.oss.iaa.acd;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.iaa.acd.scenariosummaryview.ScenarioSummaryPage;

import io.qameta.allure.Description;

public class ScenarioSummaryViewTest extends BaseTestCase {

    private ScenarioSummaryPage scenarioSummaryPage;

    private static final String PREDEFINED_FILTERS_WINDOW_ID = "PredefinedFiltersWindowId";
    private static final String DETECTED_ISSUES_WINDOW_ID = "DetectedIssuesWindowId";
    private static final String SEVERITY_STATE_TABLE_NAME = "Severity/State";
    private static final String LEFT_HEADER_NEW = "New";
    private static final String SWITCHER_ID = "switcherValue";
    private static final String CREATION_TYPE_VALUE = "Automatically";

    @Parameters("chosenView")
    @BeforeClass
    public void goToScenarioSummaryView(
            @Optional("apd") String chosenView
    ) {
        scenarioSummaryPage = ScenarioSummaryPage.goToPage(driver, BASIC_URL, chosenView);
    }

    @Test(priority = 1, testName = "Resize predefined filters area", description = "Resize predefined filters area")
    @Description("Resize predefined filters area")
    public void resizePredefinedFiltersArea() {
        scenarioSummaryPage.maximizeWindow(PREDEFINED_FILTERS_WINDOW_ID);
        Assert.assertTrue(scenarioSummaryPage.checkCardMaximize(PREDEFINED_FILTERS_WINDOW_ID));
        scenarioSummaryPage.minimizeWindow(PREDEFINED_FILTERS_WINDOW_ID);
        Assert.assertFalse(scenarioSummaryPage.checkCardMaximize(PREDEFINED_FILTERS_WINDOW_ID));
    }

    @Test(priority = 2, testName = "Check if issues exist", description = "Check if issues exist")
    @Description("Check if issues exist")
    public void detectedIssuesTableCheck() {
        scenarioSummaryPage.maximizeWindow(DETECTED_ISSUES_WINDOW_ID);
        Assert.assertTrue(scenarioSummaryPage.checkCardMaximize(DETECTED_ISSUES_WINDOW_ID));
        scenarioSummaryPage.checkIssuesTableWithFilter(CREATION_TYPE_VALUE, 1, 12, 33);
        scenarioSummaryPage.minimizeWindow(DETECTED_ISSUES_WINDOW_ID);
        Assert.assertFalse(scenarioSummaryPage.checkCardMaximize(DETECTED_ISSUES_WINDOW_ID));
    }

    @Test(priority = 3, testName = "Check Table2D filtering by choosing cell", description = "Check Table2D filtering by choosing cell")
    @Description("Check Table2D filtering by choosing cell")
    public void table2DFilteringByCellCheck() {
        scenarioSummaryPage.turnOnSwitcher(SWITCHER_ID);
        scenarioSummaryPage.selectCellInTable(SEVERITY_STATE_TABLE_NAME, LEFT_HEADER_NEW, "Critical");
        Assert.assertTrue(scenarioSummaryPage.checkIfFilteringBySelectingCellWorks(SEVERITY_STATE_TABLE_NAME, LEFT_HEADER_NEW, "Critical"));
        scenarioSummaryPage.clearFiltersInTable(SEVERITY_STATE_TABLE_NAME);
    }

    @Test(priority = 4, testName = "Check Table2D filtering by choosing row", description = "Check Table2D filtering by choosing row")
    @Description("Check Table2D filtering by choosing row")
    public void table2DFilteringByRowCheck() {
        scenarioSummaryPage.turnOnSwitcher(SWITCHER_ID);
        scenarioSummaryPage.selectRowInTable(SEVERITY_STATE_TABLE_NAME, LEFT_HEADER_NEW);
        scenarioSummaryPage.sumValuesOfSelectedCells(SEVERITY_STATE_TABLE_NAME);
        Assert.assertTrue(scenarioSummaryPage.checkIfFilteringBySelectingRowOrColumnWorks(SEVERITY_STATE_TABLE_NAME));
        scenarioSummaryPage.clearFiltersInTable(SEVERITY_STATE_TABLE_NAME);
    }
}
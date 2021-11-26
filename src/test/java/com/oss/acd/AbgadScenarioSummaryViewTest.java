package com.oss.acd;

import com.oss.BaseTestCase;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.acd.BaseACDPage;
import com.oss.pages.acd.scenarioSummaryView.AbgadScenarioSummaryViewPage;
import com.oss.utils.TestListener;
import io.qameta.allure.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners({TestListener.class})
public class AbgadScenarioSummaryViewTest extends BaseTestCase {

    private static final Logger log = LoggerFactory.getLogger(AbgadScenarioSummaryViewTest.class);

    private AbgadScenarioSummaryViewPage abgadScenarioSummaryViewPage;
    private BaseACDPage baseACDPage;

    private final String abgadScenarioSummaryViewSuffixUrl = "%s/#/view/acd/abgad";

    private final String PREDEFINED_FILTERS_WINDOW_ID = "PredefinedFiltersWindowId";
    private final String DETECTED_ISSUES_WINDOW_ID = "DetectedIssuesWindowId";
    
    @BeforeClass
    public void goToABGADScenarioSummaryView() {
        abgadScenarioSummaryViewPage = AbgadScenarioSummaryViewPage.goToPage(driver, abgadScenarioSummaryViewSuffixUrl, BASIC_URL);
        baseACDPage = new BaseACDPage(driver, webDriverWait);
    }

    @Test(priority = 1, testName = "Add new predefined filter", description = "Add new predefined filter")
    @Description("Add new predefined filter")
    public void addPredefinedFilter() {
        log.info("Waiting in method addPredefinedFilter");
        baseACDPage.maximizeWindow(PREDEFINED_FILTERS_WINDOW_ID);
        baseACDPage.clickAddPredefinedFilter();
        baseACDPage.chooseVisualizationType("Chart");
        log.info("Visualization type - CHART - set");
        baseACDPage.chooseAttribute("Severity");
        log.info("Attribute name - Severity - set");
        baseACDPage.insertAttributeValueToMultiComboBoxComponent("Cleared");
        log.info("Attribute Value - Cleared - set");
        baseACDPage.insertAttributeValueToMultiComboBoxComponent("Major");
        log.info("Attribute Value - Major - set");
        baseACDPage.insertAttributeValueToMultiComboBoxComponent("Critical");
        log.info("Attribute Value - Critical - set");
        baseACDPage.savePredefinedFilter();
        log.info("Predefined filter has been added successfully");
    }

    @Test(priority = 2, testName = "Delete predefined filter", description = "Delete predefined filter")
    @Description("Delete predefined filter")
    public void deletePredefinedFilter() {
        log.info("Waiting in method deletePredefinedFilter");
        baseACDPage.deletePredefinedFilter();
        baseACDPage.minimizeWindow(PREDEFINED_FILTERS_WINDOW_ID);
    }

    @Test(priority = 3, testName = "Check if ABGAD issues exist", description = "Check if ABGAD issues exist")
    @Description("Check if ABGAD issues exist")
    public void abgadDetectedIssuesTableCheck() {
        baseACDPage.maximizeWindow(DETECTED_ISSUES_WINDOW_ID);
        checkAbgadIssuesTableWithFilter();
        baseACDPage.minimizeWindow(DETECTED_ISSUES_WINDOW_ID);
    }

    private void checkAbgadIssuesTableWithFilter() {

        if (!abgadScenarioSummaryViewPage.isDataInIssuesTable()) {
            log.info("table doesn't have data for issues with roots");
            baseACDPage.turnOnSwitcher();
            if (!abgadScenarioSummaryViewPage.isDataInIssuesTable()) {
                log.error("table doesn't have data for issues without roots");
                Assert.fail();
            } else {
                log.info("table contains data for issues without roots");
                baseACDPage.setValueInMultiComboBox("creation_type", "Automatically");
                baseACDPage.setValueInTimePeriodChooser("create_time", 3, 12, 33);
                abgadScenarioSummaryViewPage.setValueOfIssueIdSearch();
            }
        } else {
            log.info("table contains data for issues with roots");
            baseACDPage.turnOnSwitcher();
            baseACDPage.setValueInMultiComboBox("creation_type", "Automatically");
            baseACDPage.setValueInTimePeriodChooser("create_time", 3, 12, 33);
            abgadScenarioSummaryViewPage.setValueOfIssueIdSearch();

            if (!abgadScenarioSummaryViewPage.isDataInIssuesTable()) {
                abgadScenarioSummaryViewPage.clearMultiComboBox("creation_type");
                abgadScenarioSummaryViewPage.clearMultiSearch("id");
                baseACDPage.clearTimePeriod("create_time");
                log.error("Table doesn't have data for provided filters");
                Assert.fail();
            }
            DelayUtils.sleep();
            abgadScenarioSummaryViewPage.clearMultiComboBox("creation_type");
            abgadScenarioSummaryViewPage.clearMultiSearch("id");
            baseACDPage.clearTimePeriod("create_time");
        }
    }
}

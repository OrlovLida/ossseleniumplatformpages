package com.oss.acd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.acd.scenariosummaryview.AbgadScenarioSummaryViewPage;
import com.oss.utils.TestListener;

import io.qameta.allure.Description;

@Listeners({TestListener.class})
public class AbgadScenarioSummaryViewTest extends BaseTestCase {

    private static final Logger log = LoggerFactory.getLogger(AbgadScenarioSummaryViewTest.class);

    private AbgadScenarioSummaryViewPage abgadScenarioSummaryViewPage;

    private static final String abgadScenarioSummaryViewSuffixUrl = "%s/#/view/acd/abgad";

    private static final String PREDEFINED_FILTERS_WINDOW_ID = "PredefinedFiltersWindowId";
    private static final String DETECTED_ISSUES_WINDOW_ID = "DetectedIssuesWindowId";
    private static final String SWITCHER_ID = "switcherId";

    @BeforeClass
    public void goToABGADScenarioSummaryView() {
        abgadScenarioSummaryViewPage = AbgadScenarioSummaryViewPage.goToPage(driver, abgadScenarioSummaryViewSuffixUrl, BASIC_URL);
    }

    @Test(priority = 1, testName = "Resize predefined filters area", description = "Resize predefined filters area")
    @Description("Resize predefined filters area")
    public void resizePredefinedFiltersArea() {
        abgadScenarioSummaryViewPage.maximizeWindow(PREDEFINED_FILTERS_WINDOW_ID);
        Assert.assertTrue(abgadScenarioSummaryViewPage.checkCardMaximize(PREDEFINED_FILTERS_WINDOW_ID));
        abgadScenarioSummaryViewPage.minimizeWindow(PREDEFINED_FILTERS_WINDOW_ID);
        Assert.assertFalse(abgadScenarioSummaryViewPage.checkCardMaximize(PREDEFINED_FILTERS_WINDOW_ID));
    }

    @Test(priority = 2, testName = "Check if ABGAD issues exist", description = "Check if ABGAD issues exist")
    @Description("Check if ABGAD issues exist")
    public void abgadDetectedIssuesTableCheck() {
        abgadScenarioSummaryViewPage.maximizeWindow(DETECTED_ISSUES_WINDOW_ID);
        Assert.assertTrue(abgadScenarioSummaryViewPage.checkCardMaximize(DETECTED_ISSUES_WINDOW_ID));
        checkAbgadIssuesTableWithFilter();
        abgadScenarioSummaryViewPage.minimizeWindow(DETECTED_ISSUES_WINDOW_ID);
        Assert.assertFalse(abgadScenarioSummaryViewPage.checkCardMaximize(DETECTED_ISSUES_WINDOW_ID));
    }

    private void checkAbgadIssuesTableWithFilter() {

        if (Boolean.FALSE.equals(abgadScenarioSummaryViewPage.isDataInIssuesTable())) {
            log.info("table doesn't have data for issues with roots");
            abgadScenarioSummaryViewPage.turnOnSwitcher(SWITCHER_ID);
            if (Boolean.FALSE.equals(abgadScenarioSummaryViewPage.isDataInIssuesTable())) {
                log.error("table doesn't have data for issues without roots");
                Assert.fail();
            } else {
                log.info("table contains data for issues without roots");
                abgadScenarioSummaryViewPage.setValueInMultiComboBox("creation_type", "Automatically");
                //abgadScenarioSummaryViewPage.setValueInTimePeriodChooser("create_time", 3, 12, 33); //TODO after fix ACD-3363
                abgadScenarioSummaryViewPage.setValueOfIssueIdSearch();
            }
        } else {
            log.info("table contains data for issues with roots");
            abgadScenarioSummaryViewPage.turnOnSwitcher(SWITCHER_ID);
            abgadScenarioSummaryViewPage.setValueInMultiComboBox("creation_type", "Automatically");
            //abgadScenarioSummaryViewPage.setValueInTimePeriodChooser("create_time", 3, 12, 33); //TODO after fix ACD-3363
            abgadScenarioSummaryViewPage.setValueOfIssueIdSearch();

            if (Boolean.FALSE.equals(abgadScenarioSummaryViewPage.isDataInIssuesTable())) {
                clearIssueTableFilters();
                log.error("Table doesn't have data for provided filters");
                Assert.fail();
            }
        }
        DelayUtils.sleep();
        clearIssueTableFilters();
    }

    private void clearIssueTableFilters() {
        abgadScenarioSummaryViewPage.clearMultiComboBox("creation_type");
        abgadScenarioSummaryViewPage.clearMultiSearch("id");
        //baseACDPage.clearTimePeriod("create_time"); //TODO after fix ACD-3363
    }
}

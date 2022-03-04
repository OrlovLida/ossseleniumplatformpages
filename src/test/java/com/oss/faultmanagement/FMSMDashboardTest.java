package com.oss.faultmanagement;

import com.oss.BaseTestCase;
import com.oss.pages.faultmanagement.FMSMDashboardPage;
import com.oss.utils.TestListener;
import io.qameta.allure.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.Arrays;
import java.util.List;

/**
 * @author Bartosz Nowak
 */
@Listeners({TestListener.class})
public class FMSMDashboardTest extends BaseTestCase {
    private static final Logger log = LoggerFactory.getLogger(WAMVBasicTest.class);
    private static final String ALARM_COUNTERS_VIEW_ID = "_UserViewsListALARM_COUNTERS";
    private static final String ALARM_MANAGEMENT_VIEW_ID = "_UserViewsListALARM_MANAGEMENT";

    private FMSMDashboardPage fmsmDashboardPage;

    @Parameters("chosenDashboard")
    @BeforeMethod
    public void goToDashboardPage(
            @Optional("FaultManagement") String chosenDashboard
    ) {
        fmsmDashboardPage = fmsmDashboardPage.goToPage(driver, BASIC_URL, chosenDashboard);
    }

    @Parameters("viewsForChosenDashboard")
    @Test(priority = 1, testName = "Check if maximize and minimize works", description = "Check if maximize and minimize works")
    @Description("I verify if maximize and minimize works")
    public void checkMaximizeAndMinimize(
            @Optional("_AlarmCounters,_UserViewsListALARM_COUNTERS,_UserViewsListALARM_MANAGEMENT,_UserViewsListHISTORICAL_ALARM_MANAGEMENT,_UserViewsListMAP_MONITORING") String viewsForChosenDashboard
    ) {
        List<String> views = Arrays.asList(viewsForChosenDashboard.split(","));
        try {
            for (String view : views) {
                fmsmDashboardPage.maximizeWindow(view);
                Assert.assertTrue(fmsmDashboardPage.checkCardMaximize(view));
                fmsmDashboardPage.minimizeWindow(view);
                Assert.assertFalse(fmsmDashboardPage.checkCardMaximize(view));
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            Assert.fail();
        }
    }

    @Parameters({"searchPhrase", "viewsForChosenDashboard"})
    @Test(priority = 2, testName = "Check if FM or SM Dashboard search panels works", description = "Check if FM or SM Dashboard search panels works")
    @Description("I verify if FM or SM Dashboard search panels works correctly")
    public void checkSearchPanel(
            @Optional("!@#$%^&*()_+=-|?/:;{}[]`") String searchPhrase,
            @Optional("_AlarmCounters,_UserViewsListALARM_COUNTERS,_UserViewsListALARM_MANAGEMENT,_UserViewsListHISTORICAL_ALARM_MANAGEMENT,_UserViewsListMAP_MONITORING") String viewsForChosenDashboard
    ) {
        List<String> views = Arrays.asList(viewsForChosenDashboard.split(","));
        try {
            for (String view : views) {
                fmsmDashboardPage.searchInView(view, searchPhrase);
                Assert.assertTrue(fmsmDashboardPage.isNoDataInView(view));
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            Assert.fail();
        }
    }

    @Parameters("alarmListName")
    @Test(priority = 3, testName = "Check if sorting views works", description = "Check if sorting views works")
    @Description("I verify if sorting works correctly")
    public void checkSortOption(
            @Optional("Selenium_test_alarm_list") String alarmListName
    ) {
        try {
            fmsmDashboardPage.searchInView(ALARM_MANAGEMENT_VIEW_ID, alarmListName);
            fmsmDashboardPage.sortCommonList(ALARM_MANAGEMENT_VIEW_ID);
        } catch (Exception e) {
            log.error(e.getMessage());
            Assert.fail();
        }
    }

    @Parameters("viewsForChosenDashboard")
    @Test(priority = 4, testName = "Check headers", description = "Check headers")
    @Description("I verify if headers in tables are correct")
    public void checkHeaders(
            @Optional("_UserViewsListALARM_COUNTERS,_UserViewsListALARM_MANAGEMENT,_UserViewsListHISTORICAL_ALARM_MANAGEMENT,_UserViewsListMAP_MONITORING") String viewsForChosenDashboard
    ) {
        List<String> views_with_tables = Arrays.asList(viewsForChosenDashboard.split(","));
        try {
            for (String view : views_with_tables) {
                if (fmsmDashboardPage.isNoDataInView(view)) {
                    Assert.assertTrue(fmsmDashboardPage.isNoDataInView(view));

                } else if (view.equals(ALARM_COUNTERS_VIEW_ID)) {
                    Assert.assertTrue(fmsmDashboardPage.isHeaderVisible(view, "Name"));
                    Assert.assertTrue(fmsmDashboardPage.isHeaderVisible(view, "Owner"));

                } else {
                    Assert.assertTrue(fmsmDashboardPage.isHeaderVisible(view, "Name"));
                    Assert.assertTrue(fmsmDashboardPage.isHeaderVisible(view, "Owner"));
                    Assert.assertTrue(fmsmDashboardPage.isHeaderVisible(view, "Description"));
                    Assert.assertTrue(fmsmDashboardPage.isHeaderVisible(view, "Modify date"));
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            Assert.fail();
        }
    }
}

package com.oss.faultmanagement;

import com.oss.BaseTestCase;
import com.oss.pages.faultmanagement.FMDashboardPage;
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
public class FMDashboardTest extends BaseTestCase {
    private static final Logger log = LoggerFactory.getLogger(WAMVBasicTest.class);
    private static final String ALARM_COUNTERS_ID = "_AlarmCounters";
    private static final String ALARM_COUNTERS_VIEW_ID = "_UserViewsListALARM_COUNTERS";
    private static final String ALARM_MANAGEMENT_VIEW_ID = "_UserViewsListALARM_MANAGEMENT";
    private static final String HISTORICAL_ALARM_VIEW_ID = "_UserViewsListHISTORICAL_ALARM_MANAGEMENT";
    private static final String MAP_MONITORING_VIEW_ID = "_UserViewsListMAP_MONITORING";
    private static final List<String> views = Arrays.asList(ALARM_COUNTERS_ID, ALARM_COUNTERS_VIEW_ID, ALARM_MANAGEMENT_VIEW_ID, HISTORICAL_ALARM_VIEW_ID, MAP_MONITORING_VIEW_ID);

    private FMDashboardPage fmDashboardPage;

    @BeforeMethod
    public void goToFMDashboardPage() {
        fmDashboardPage = FMDashboardPage.goToPage(driver, BASIC_URL);
    }

    @Test(priority = 1, testName = "Check if maximize and minimize works", description = "Check if maximize and minimize works")
    @Description("I verify if maximize and minimize works")
    public void checkMaximizeAndMinimize() {
        try {
            for (int i = 0; i <= views.size() - 1; i++) {
                fmDashboardPage.maximizeWindow(views.get(i));
                Assert.assertTrue(fmDashboardPage.checkCardMaximize(views.get(i)));
                fmDashboardPage.minimizeWindow(views.get(i));
                Assert.assertFalse(fmDashboardPage.checkCardMaximize(views.get(i)));
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            Assert.fail();
        }
    }

    @Parameters("searchPhrase")
    @Test(priority = 2, testName = "Check if FM Dashboard search panels works", description = "Check if FM Dashboard search panels works")
    @Description("I verify if FM Dashboard search panels works correctly")
    public void checkSearchPanel(
            @Optional("!@#$%^&*()_+=-|?/:;{}[]`") String searchPhrase
    ) {
        try {
            for (int i = 1; i <= views.size() - 1; i++) {
                fmDashboardPage.searchInSpecificView(views.get(i), searchPhrase);
                Assert.assertTrue(fmDashboardPage.isNoDataInView(views.get(i)));
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            Assert.fail();
        }
    }

    @Parameters("alarmListName")
    @Test(priority = 3, testName = "Check if sort views works", description = "Check if sort views works")
    @Description("I verify if sorting works correctly")
    public void checkSortOption(
            @Optional("Selenium_test_alarm_list") String alarmListName
    ) {
        try {
            fmDashboardPage.searchInSpecificView(ALARM_MANAGEMENT_VIEW_ID, alarmListName);
            fmDashboardPage.sortCommonList(ALARM_MANAGEMENT_VIEW_ID);
        } catch (Exception e) {
            log.error(e.getMessage());
            Assert.fail();
        }
    }


    @Test(priority = 4, testName = "Check headers", description = "Check headers")
    @Description("I verify if headers are correct")
    public void checkHeaders() {
        try {
            for (int i = 1; i <= views.size() - 1; i++) {
                if (fmDashboardPage.isNoDataInView(views.get(i))) {
                    Assert.assertTrue(fmDashboardPage.isNoDataInView(views.get(i)));

                } else if (i == 1) {
                    Assert.assertTrue(fmDashboardPage.isHeaderVisible(views.get(i), "Name"));
                    Assert.assertTrue(fmDashboardPage.isHeaderVisible(views.get(i), "Owner"));

                } else {
                    Assert.assertTrue(fmDashboardPage.isHeaderVisible(views.get(i), "Name"));
                    Assert.assertTrue(fmDashboardPage.isHeaderVisible(views.get(i), "Owner"));
                    Assert.assertTrue(fmDashboardPage.isHeaderVisible(views.get(i), "Description"));
                    Assert.assertTrue(fmDashboardPage.isHeaderVisible(views.get(i), "Modify date"));
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            Assert.fail();
        }
    }
}

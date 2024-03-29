package com.oss.iaa.faultmanagement;

import com.oss.BaseTestCase;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.iaa.faultmanagement.FMCreateWAMVPage;
import com.oss.pages.iaa.faultmanagement.FMSMDashboardPage;
import io.qameta.allure.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateHistoricalAlarmViewTest extends BaseTestCase {

    private static final String date = new SimpleDateFormat("dd-MM-yyyy_HH:mm").format(new Date());
    private static final Logger log = LoggerFactory.getLogger(CreateHistoricalAlarmViewTest.class);
    private static final String HISTORICAL_ALARM_MANAGEMENT_VIEW_ID = "_UserViewsListHISTORICAL_ALARM_MANAGEMENT";

    private static final String FM_DASHBOARD = "FaultManagement";
    private static final String EDITED_SUFFIX = "_edited";
    private static final String FORMATTED_DATE = "_" + date.replace(":", "_");

    private FMSMDashboardPage fmsmDashboardPage;
    private FMCreateWAMVPage fmWAMVPage;

    @BeforeMethod
    public void goToFMDashboardPage(
    ) {
        fmsmDashboardPage = FMSMDashboardPage.goToPage(driver, BASIC_URL, FM_DASHBOARD);
    }

    @Parameters({"name", "description", "folderName", "bookmarkCategory", "rowNumber"})
    @Test(priority = 1, testName = "Create new historical alarm view", description = "Create new historical alarm view")
    @Description("I verify if it is possible to create historical alarm view")
    public void createNewWAMVandDeleteIt(
            @Optional("Selenium_test_alarm_list") String name,
            @Optional("Selenium test description") String description,
            @Optional("Selenium_test_folder") String folderName,
            @Optional("Selenium_fm_folder") String bookmarkCategory,
            @Optional("0") int rowNumber

    ) {
        try {
            fmWAMVPage = fmsmDashboardPage.clickCreateNewAlarmList(HISTORICAL_ALARM_MANAGEMENT_VIEW_ID);
            fmWAMVPage.setName(name + FORMATTED_DATE);
            fmWAMVPage.setDescription(description);
            fmWAMVPage.chooseBookmarkCategory(bookmarkCategory);
            fmWAMVPage.clickNextButton();
            fmWAMVPage.dragAndDropFilterByName(folderName);
            DelayUtils.sleep(1000);  //  TODO change it after fix OSSNGSA-11102
            fmWAMVPage.selectFilterFromList(rowNumber);
            fmWAMVPage.clickAcceptButton();
            Assert.assertTrue(fmsmDashboardPage.checkVisibility(HISTORICAL_ALARM_MANAGEMENT_VIEW_ID, name + FORMATTED_DATE));
        } catch (Exception e) {
            log.error(e.getMessage());
            Assert.fail();
        }
    }

    //TODO bug z edycją bookmarka - fix OSSNGSA-12082
    @Parameters({"name"})
    @Test(priority = 2, testName = "Edit historical alarm view", description = "Edit historical alarm view")
    @Description("I verify if it is possible to edit historical alarm view")
    public void editCreatedWAMV(
            @Optional("Selenium_test_alarm_list") String name
    ) {
        try {
            fmsmDashboardPage.searchInView(HISTORICAL_ALARM_MANAGEMENT_VIEW_ID, name + FORMATTED_DATE);
            fmsmDashboardPage.clickEditView(HISTORICAL_ALARM_MANAGEMENT_VIEW_ID, 0);
            fmWAMVPage = new FMCreateWAMVPage(driver, webDriverWait);
            fmWAMVPage.setName(name + FORMATTED_DATE + EDITED_SUFFIX);
            fmWAMVPage.clickNextButton();
            fmWAMVPage.clickAcceptButton();
            DelayUtils.sleep(1000);  //  TODO change it after fix OSSNGSA-11102
            Assert.assertTrue(fmsmDashboardPage.checkVisibility(HISTORICAL_ALARM_MANAGEMENT_VIEW_ID, name + FORMATTED_DATE + EDITED_SUFFIX));
        } catch (Exception e) {
            log.error(e.getMessage());
            Assert.fail();
        }
    }

    @Parameters({"name"})
    @Test(priority = 3, testName = "Delete historical alarm view", description = "Delete historical alarm view")
    @Description("I verify if it is possible to delete historical alarm view")
    public void deleteNewWAMV(
            @Optional("Selenium_test_alarm_list") String name
    ) {
        try {
            fmsmDashboardPage.searchInView(HISTORICAL_ALARM_MANAGEMENT_VIEW_ID, name + FORMATTED_DATE + EDITED_SUFFIX);
            fmsmDashboardPage.deleteFromView(HISTORICAL_ALARM_MANAGEMENT_VIEW_ID, 0);
            DelayUtils.sleep(1000);  //  TODO change it after fix OSSNGSA-11102
            Assert.assertFalse(fmsmDashboardPage.checkVisibility(HISTORICAL_ALARM_MANAGEMENT_VIEW_ID, name + FORMATTED_DATE + EDITED_SUFFIX));
        } catch (Exception e) {
            log.error(e.getMessage());
            Assert.fail();
        }
    }
}
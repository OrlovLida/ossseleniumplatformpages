package com.oss.iaa.faultmanagement;

import com.oss.BaseTestCase;
import com.oss.pages.iaa.faultmanagement.FMCreateWAMVPage;
import com.oss.pages.iaa.faultmanagement.FMSMDashboardPage;
import io.qameta.allure.Description;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author APodolska
 */
public class CreateAlarmListTest extends BaseTestCase {
    private static final String date = new SimpleDateFormat("dd-MM-yyyy_HH:mm").format(new Date());
    private static final String ALARM_MANAGEMENT_VIEW_ID = "_UserViewsListALARM_MANAGEMENT";

    private FMSMDashboardPage fmsmDashboardPage;
    private FMCreateWAMVPage fmWAMVPage;

    @Parameters("chosenDashboard")
    @BeforeMethod
    public void goToFMDashboardPage(
            @Optional("FaultManagement") String chosenDashboard
    ) {
        fmsmDashboardPage = FMSMDashboardPage.goToPage(driver, BASIC_URL, chosenDashboard);
    }

    @Parameters({"name", "description", "folderName", "rowNumber", "bookmarkCategory"})
    @Test(priority = 1, testName = "Create new alarm management view", description = "Set name, description, folder and filter")
    @Description("I verify if it is possible to create Web Alarm Management View")
    public void createNewWAMVandDeleteIt(
            @Optional("Selenium_test_alarm_list") String name,
            @Optional("Selenium test description") String description,
            @Optional("Selenium_fm_folder") String bookmarkCategory,
            @Optional("Selenium_test_folder") String folderName,
            @Optional("0") int rowNumber
    ) {
        fmWAMVPage = fmsmDashboardPage.clickCreateNewAlarmList(ALARM_MANAGEMENT_VIEW_ID);
        fmWAMVPage.setName(name + '_' + date.replace(":", "_"));
        fmWAMVPage.setDescription(description);
        fmWAMVPage.chooseBookmarkCategory(bookmarkCategory);
        fmWAMVPage.dragAndDropFilterByName(folderName);
        fmWAMVPage.selectFilterFromList(rowNumber);
        fmWAMVPage.clickAcceptButton();
        Assert.assertTrue(fmsmDashboardPage.checkVisibility(ALARM_MANAGEMENT_VIEW_ID, name + '_' + date.replace(":", "_")));
        fmsmDashboardPage.searchInView(ALARM_MANAGEMENT_VIEW_ID, name + '_' + date.replace(":", "_"));
        fmsmDashboardPage.deleteFromView(ALARM_MANAGEMENT_VIEW_ID, 0);
        Assert.assertFalse(fmsmDashboardPage.checkVisibility(ALARM_MANAGEMENT_VIEW_ID, name + '_' + date.replace(":", "_")));
    }
}
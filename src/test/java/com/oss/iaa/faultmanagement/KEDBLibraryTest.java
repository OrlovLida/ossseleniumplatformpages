package com.oss.iaa.faultmanagement;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.comarch.oss.web.pages.NotificationWrapperPage;
import com.oss.BaseTestCase;
import com.oss.pages.iaa.faultmanagement.KEDBLibraryPage;

import io.qameta.allure.Description;

public class KEDBLibraryTest extends BaseTestCase {

    private static final String ALARM_LIBRARY_FULL_NAME = "com.comarch.iaa.fm.kedb.lib.AlarmKEDBLibrary";
    private static final String CORE_LIBRARY_FULL_NAME = "com.comarch.iaa.fm.kedb.lib.CoreKEDBLibrary";
    private static final String WEB_TT_LIBRARY_FULL_NAME = "com.comarch.iaa.fm.kedb.lib.WebTroubleTicketKEDBLibrary";
    private static final String ALARM_CUSTOMER_LIBRARY_FULL_NAME = "com.comarch.iaa.fm.kedb.lib.AlarmKEDBLibraryCustomer";
    private static final String SUCCESS_COMPILATION_STATUS = "Compilation status OK. Bases loaded.";
    private static final int EXPECTED_NUM_OF_HISTORY_TABLE_HEADERS = 4;
    private static final String FILE_NAME = KEDBLibraryPage.createFileName("Selenium_KEDB_");
    public static final String DATE_MASK = "ISO Local Date";
    private static final String EXPORTED_FILE_NAME = FILE_NAME + "*.";
    private KEDBLibraryPage kedbLibraryPage;

    @BeforeMethod
    public void goToKEDBLibraryPage() {
        kedbLibraryPage = KEDBLibraryPage.goToKEDBLibraryPage(driver, BASIC_URL);
    }

    @Parameters({"columnLabel"})
    @Test(priority = 1, testName = "Disable column in KEDB Library Table", description = "Disable column in KEDB Library Table")
    @Description("Disable column in KEDB Library Table")
    public void disableColumnInTable(
            @Optional("Name") String columnLabel
    ) {
        kedbLibraryPage.disableColumnInTable(columnLabel);
        Assert.assertFalse(kedbLibraryPage.isColumnInTable(columnLabel));
    }

    @Parameters({"columnLabel"})
    @Test(priority = 2, testName = "Enable column in KEDB Library Table", description = "Enable column in KEDB Library Table")
    @Description("Enable column in KEDB Library Table")
    public void enableColumnInTable(
            @Optional("Name") String columnLabel
    ) {
        kedbLibraryPage.enableColumnInTable(columnLabel);
        Assert.assertTrue(kedbLibraryPage.isColumnInTable(columnLabel));
    }

    //TODO after fix OSSNGSA-11383 add ALARM_LIBRARY_PARTIAL_NAME
    @DataProvider(name = "libraries")
    public Object[][] libraries() {
        return new Object[][]{
                {ALARM_LIBRARY_FULL_NAME},
                {CORE_LIBRARY_FULL_NAME},
                {WEB_TT_LIBRARY_FULL_NAME},
                {ALARM_CUSTOMER_LIBRARY_FULL_NAME}
        };
    }

    @Test(priority = 3, dataProvider = "libraries", testName = "Check Library", description = "Check Library")
    @Description("Check Library")
    public void checkAlarmKEDBLibrary(String libraryFullName) {
        SoftAssert softAssert = new SoftAssert();
        kedbLibraryPage.setFilterToLibName(libraryFullName);
        softAssert.assertEquals(kedbLibraryPage.getFirstLibName(), libraryFullName);

        kedbLibraryPage.selectFirstRow();
        kedbLibraryPage.selectCompilationStatusTab();
        softAssert.assertEquals(kedbLibraryPage.getCompilationStatus(), SUCCESS_COMPILATION_STATUS);
        softAssert.assertTrue(kedbLibraryPage.isIconSuccessPresent());

        kedbLibraryPage.selectJavaContentTab();
        softAssert.assertFalse(kedbLibraryPage.isAnyError(), "Some errors occurred");

        kedbLibraryPage.selectHistoryTab();
        softAssert.assertEquals(kedbLibraryPage.getNumOfHistoryTableHeaders(), EXPECTED_NUM_OF_HISTORY_TABLE_HEADERS);
        softAssert.assertFalse(kedbLibraryPage.isAnyError(), "Some errors occurred");
        softAssert.assertAll();
    }

    @Test(priority = 4, testName = "Check Refresh", description = "Check Refresh")
    @Description("Check Refresh")
    public void checkRefresh() {
        int numOfLibraries = kedbLibraryPage.getNumOfLibraries();
        kedbLibraryPage.clickRefresh();
        Assert.assertEquals(kedbLibraryPage.getNumOfLibraries(), numOfLibraries);
    }

    @DataProvider(name = "fileTypes")
    public Object[][] fileTypes() {
        return new Object[][]{
                {"CSV"},
                {"XLSX"},
                {"XLS"}
        };
    }

    @Test(priority = 5, dataProvider = "fileTypes", testName = "Export KEDB Libraries", description = "Export KEDB Libraries")
    @Description("Export KEDB Libraries")
    public void exportKEDBLibs(String fileType) {
        kedbLibraryPage.clickExport()
                .chooseFileType(fileType)
                .setFileName(FILE_NAME)
                .changeDateMaskContains(DATE_MASK)
                .closeTheWizard();
        NotificationWrapperPage notifications = kedbLibraryPage.openNotificationPanel();
        notifications.waitForExportFinish()
                .clickDownload();
        notifications.clearNotifications();
        kedbLibraryPage.attachFileToReport(EXPORTED_FILE_NAME + fileType.toLowerCase());

        Assert.assertEquals(notifications.amountOfNotifications(), 0);
        Assert.assertTrue(kedbLibraryPage.checkIfFileIsNotEmpty(EXPORTED_FILE_NAME + fileType.toLowerCase()));
    }
}

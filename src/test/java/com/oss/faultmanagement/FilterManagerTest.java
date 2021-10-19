package com.oss.faultmanagement;

import com.oss.BaseTestCase;
import com.oss.bigdata.kpiview.KpiViewTest;
import com.oss.pages.faultmanagement.filtermanager.FMFilterManagerPage;
import com.oss.utils.TestListener;
import io.qameta.allure.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.*;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Bartosz Nowak
 */
@Listeners({TestListener.class})
public class FilterManagerTest extends BaseTestCase {
    private static final Logger log = LoggerFactory.getLogger(KpiViewTest.class);

    private final String date = new SimpleDateFormat("dd-MM-yyyy_HH:mm").format(new Date());
    private FMFilterManagerPage fmFilterManagerPage;

    @BeforeMethod
    public void goToFMDashboardPage() {
        fmFilterManagerPage = FMFilterManagerPage.goToFilterManagerPage(driver, BASIC_URL);
    }

    @Parameters({"folderName", "description"})
    @Test(priority = 1, testName = "Create new folder and filter", description = "Folder and filter creation verification")
    @Description("I verify if Folder and Filter creates without error")
    public void createNewFolderAndDelete(
            @Optional("selenium_test_folder") String folderName,
            @Optional("Selenium test description") String description
    ) {
        try {
            fmFilterManagerPage.createFolder(folderName + "_" + date, description);
            Assert.assertEquals(fmFilterManagerPage.checkIfFolderNameExists(folderName + "_" + date), true);
            fmFilterManagerPage.deleteFolder(folderName + "_" + date);
            Assert.assertEquals(fmFilterManagerPage.checkIfFolderNameNotExists(folderName + "_" + date), true);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}

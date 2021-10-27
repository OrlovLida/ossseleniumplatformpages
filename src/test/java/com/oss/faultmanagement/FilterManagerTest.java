package com.oss.faultmanagement;

import com.oss.BaseTestCase;
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
    private static final Logger log = LoggerFactory.getLogger(FilterManagerTest.class);

    private final String date = new SimpleDateFormat("dd-MM-yyyy_HH:mm").format(new Date());
    private FMFilterManagerPage fmFilterManagerPage;

    @BeforeMethod
    public void goToFilterManagerPage() {
        fmFilterManagerPage = FMFilterManagerPage.goToFilterManagerPage(driver, BASIC_URL);
    }

    @Parameters({"folderName", "description"})
    @Test(priority = 1, testName = "Create new folder and delete", description = "Folder creation verification")
    @Description("I verify if Folder and Filter creates without error")
    public void createNewFolderAndDelete(
            @Optional("selenium_test_folder") String folderName,
            @Optional("Selenium test description") String description
    ) {
        try {
            fmFilterManagerPage.createFolder(folderName + "_" + date, description);
            Assert.assertTrue(fmFilterManagerPage.checkIfFolderNameExists(folderName + "_" + date));
            fmFilterManagerPage.deleteFolder(folderName + "_" + date);
            Assert.assertTrue(fmFilterManagerPage.checkIfFolderNameNotExists(folderName + "_" + date));

        } catch (Exception e) {
            log.error(e.getMessage());
            Assert.fail();
        }
    }

    @Parameters({"folderName", "description", "filterName"})
    @Test(priority = 2, testName = "Create new folder with filter", description = "Folder with filter creation verification")
    @Description("I verify if Folder and Filter creates without error")
    public void createNewFolderWithFilterAndDelete(
            @Optional("Selenium_test_folder") String folderName,
            @Optional("Selenium test description") String description,
            @Optional("Selenium_test_filter") String filterName
    ) {
        try {
            fmFilterManagerPage.createFolder(folderName + "_" + date, description, filterName);
            Assert.assertTrue(fmFilterManagerPage.checkIfFolderNameExists(folderName + "_" + date));
            fmFilterManagerPage.deleteFolder(folderName + "_" + date);
            Assert.assertTrue(fmFilterManagerPage.checkIfFolderNameNotExists(folderName + "_" + date));

        } catch (Exception e) {
            log.error(e.getMessage());
            Assert.fail();
        }
    }
}

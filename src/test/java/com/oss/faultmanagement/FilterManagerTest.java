package com.oss.faultmanagement;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.faultmanagement.filtermanager.FMFilterManagerPage;
import com.oss.utils.TestListener;

import io.qameta.allure.Description;

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
    @Test(priority = 1, testName = "Create new folder and delete it", description = "Folder creation verification")
    @Description("I verify if Folder creates without error")
    public void createNewFolderAndDelete(
            @Optional("Selenium_test_folder") String folderName,
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
    @Test(priority = 2, testName = "Create new folder with filter and delete it", description = "Folder with filter creation verification")
    @Description("I verify if Folder with Filter creates without error")
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

    @Parameters({"folderName", "description", "filterName", "filterType"})
    @Test(priority = 3, testName = "Create new Filter and delete it", description = "Filter creation verification")
    @Description("I verify if Filter creates without error")
    public void createNewFilter(
            @Optional("Selenium_test_folder") String folderName,
            @Optional("Selenium test description") String description,
            @Optional("Selenium_test_filter") String filterName,
            @Optional("Historical Alarm") String filterType
    ) {
        try {
            String name = filterName + "_" + date;
            fmFilterManagerPage.createFilter(name, description, filterType, folderName);
            fmFilterManagerPage.searchForElement(name);
            fmFilterManagerPage.expandFilterList(folderName);
            Assert.assertTrue(fmFilterManagerPage.checkIfFilterExists(name));
            fmFilterManagerPage.clickDeleteFilter(name);
            Assert.assertFalse(fmFilterManagerPage.checkIfFilterExists(name));

        } catch (Exception e) {
            log.error(e.getMessage());
            Assert.fail();
        }
    }
}
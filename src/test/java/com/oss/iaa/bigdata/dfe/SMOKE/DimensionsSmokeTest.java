package com.oss.iaa.bigdata.dfe.SMOKE;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.iaa.bigdata.dfe.DimensionsPage;

import io.qameta.allure.Description;

public class DimensionsSmokeTest extends BaseTestCase {

    private static final String DIMENSION_NAME = "t:SMOKE#DimHierSelenium";
    private static final String INFO_LOG = "Info";
    private DimensionsPage dimensionsPage;

    @BeforeClass
    public void goToDimensionsView() {
        dimensionsPage = DimensionsPage.goToPage(driver, BASIC_URL);
    }

    @Test(priority = 1, testName = "Check if Dimension is working", description = "Check if Dimension is working")
    @Description("Check if Dimension is working")
    public void checkIfDimensionIsWorking() {
        boolean dimensionExists = dimensionsPage.dimensionExistsIntoTable(DIMENSION_NAME);
        if (dimensionExists) {
            dimensionsPage.selectFirstDimensionInTable();
            dimensionsPage.selectLogsTab();
            dimensionsPage.refreshLogsTable();
            Assert.assertFalse(dimensionsPage.isLogTabTableEmpty(), "Logs Table is empty!");
            Assert.assertTrue(dimensionsPage.isLastLogTimeFromTimeColumnFresh());
            Assert.assertEquals(dimensionsPage.checkSeverity(), INFO_LOG);
        } else {
            Assert.fail("Cannot find existing dimension " + DIMENSION_NAME);
        }
    }
}

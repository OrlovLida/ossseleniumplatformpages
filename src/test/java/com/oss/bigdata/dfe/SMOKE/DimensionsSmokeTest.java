package com.oss.bigdata.dfe.SMOKE;

import com.oss.BaseTestCase;
import com.oss.pages.bigdata.dfe.DimensionsPage;
import io.qameta.allure.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class DimensionsSmokeTest extends BaseTestCase {

    private static final Logger log = LoggerFactory.getLogger(DimensionsSmokeTest.class);
    private DimensionsPage dimensionsPage;
    private static final String DIMENSION_NAME = "t:SMOKE#DimHierSelenium";

    @BeforeClass
    public void goToDimensionsView() {
        dimensionsPage = DimensionsPage.goToPage(driver, BASIC_URL);
    }

    @Test(priority = 1, testName = "Check if Dimension is working", description = "Check if Dimension is working")
    @Description("Check if Dimension is working")
    public void checkIfDimensionIsWorking() {
        boolean dimensionExists = dimensionsPage.dimensionExistsIntoTable(DIMENSION_NAME);
        if (dimensionExists) {
            dimensionsPage.selectFoundDimension();
            dimensionsPage.selectLogsTab();
            dimensionsPage.refreshLogsTable();

            boolean isLastLogTimeFresh = dimensionsPage.isLastLogTimeFromTimeColumnFresh();
            Assert.assertTrue(isLastLogTimeFresh);

            Assert.assertEquals(dimensionsPage.checkSeverity(), "Info");
        } else {
            Assert.fail("Cannot find existing dimension " + DIMENSION_NAME);
        }
    }
}

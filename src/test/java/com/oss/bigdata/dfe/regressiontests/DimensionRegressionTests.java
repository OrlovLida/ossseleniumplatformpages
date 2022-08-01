package com.oss.bigdata.dfe.regressiontests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.bigdata.dfe.DimensionsPage;

import io.qameta.allure.Description;

public class DimensionRegressionTests extends BaseTestCase {

    private static final String CATEGORIES_TYPE = "Selenium Tests";
    private static final String CATEGORY_COLUMN_LABEL = "Category";
    private DimensionsPage dimensionsPage;

    @BeforeMethod
    public void goToDimensionView() {
        dimensionsPage = DimensionsPage.goToPage(driver, BASIC_URL);
    }

    @Test(priority = 1, testName = "Dimensions View is opening ", description = "Dimensions View is opening")
    @Description("Dimensions View is opening")
    public void checkDimensionsView() {
        Assert.assertEquals(dimensionsPage.getViewTitle(), "Dimensions");
    }

    @Parameters({"dimensionName"})
    @Test(priority = 2, testName = "Check Dimensions Details Tab", description = "Check Dimensions Details Tab")
    @Description("Check Dimensions Details Tab")
    public void checkDimensionsDetailsTab(
            @Optional("t:SMOKE#DimHierSelenium") String dimensionName
    ) {
        boolean dimensionExist = dimensionsPage.dimensionExistsIntoTable(dimensionName);
        if (dimensionExist) {
            dimensionsPage.selectFoundDimension();
            dimensionsPage.selectDetailsTab();
            Assert.assertEquals(dimensionsPage.checkNameInPropertyPanel(), dimensionName);
        } else {
            Assert.fail("Cannot find existing Dimension " + dimensionName);
        }
    }

    @Parameters({"dimensionName"})
    @Test(priority = 3, testName = "Check Format Tab", description = "Check Format Tab")
    @Description("Check Format Tab")
    public void checkFormatTab(
            @Optional("t:SMOKE#DimHierSelenium") String dimensionName
    ) {
        boolean dimensionExist = dimensionsPage.dimensionExistsIntoTable(dimensionName);
        if (dimensionExist) {
            dimensionsPage.selectFoundDimension();
            dimensionsPage.selectFormatTab();
            Assert.assertFalse(dimensionsPage.isFormatTabTableEmpty());
        } else {
            Assert.fail("Cannot find Dimension: " + dimensionName);
        }
    }

    @Parameters({"dimensionName"})
    @Test(priority = 4, testName = "Check Used In Tab", description = "Check Used In Tab")
    @Description("Check Used In Tab")
    public void checkUsedTab(
            @Optional("t:SMOKE#DimHierSelenium") String dimensionName
    ) {
        boolean dimensionExist = dimensionsPage.dimensionExistsIntoTable(dimensionName);
        if (dimensionExist) {
            dimensionsPage.selectFoundDimension();
            dimensionsPage.selectUsedTab();
            Assert.assertFalse(dimensionsPage.isUsedInTabTableEmpty());
        } else {
            Assert.fail("Cannot find Dimension: " + dimensionName);
        }
    }

    @Parameters({"dimensionName"})
    @Test(priority = 5, testName = "Check Data Tab", description = "Check Data Tab")
    @Description("Check Data Tab")
    public void checkDataTab(
            @Optional("t:SMOKE#DimHierSelenium") String dimensionName
    ) {
        boolean dimensionExist = dimensionsPage.dimensionExistsIntoTable(dimensionName);
        if (dimensionExist) {
            dimensionsPage.selectFoundDimension();
            dimensionsPage.selectDataTab();
            Assert.assertFalse(dimensionsPage.isDataTabTableEmpty());
        } else {
            Assert.fail("Cannot find Dimension: " + dimensionName);
        }
    }

    @Parameters({"dimensionName"})
    @Test(priority = 6, testName = "Check Hierarchy Tab", description = "Check Hierarchy Tab")
    @Description("Check Hierarchy Tab")
    public void checkHierarchyTab(
            @Optional("t:SMOKE#DimHierSelenium") String dimensionName
    ) {
        boolean dimensionExist = dimensionsPage.dimensionExistsIntoTable(dimensionName);
        if (dimensionExist) {
            dimensionsPage.selectFoundDimension();
            dimensionsPage.selectHierarchyTab();
            Assert.assertFalse(dimensionsPage.isHierarchyTabTableEmpty());
        } else {
            Assert.fail("Cannot find Dimension: " + dimensionName);
        }
    }

    @Test(priority = 7, testName = "Check Categories", description = "Check Categories")
    @Description("Check Categories")
    public void checkCategories() {
        dimensionsPage.searchCategories(CATEGORIES_TYPE);
        Assert.assertEquals(dimensionsPage.getCategoryName(0), CATEGORIES_TYPE);
    }

}



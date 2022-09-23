package com.oss.iaa.bigdata.dfe.regressiontests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.iaa.bigdata.dfe.externalresource.ExternalResourcesPage;

import io.qameta.allure.Description;

public class ExternalResourcesRegressionTests extends BaseTestCase {

    private static final String EXTERNAL_RESOURCE_VIEW_TITLE = "External Resources";
    private static final String EXTERNAL_RESOURCE_NAME = "DFE Data-Model DB";
    private static final String NAME = "Name";
    private static final String TYPE = "Type";
    private static final String DATABASE_TYPE = "Database";
    private ExternalResourcesPage externalResourcesPage;

    @BeforeMethod
    public void openPage() {
        externalResourcesPage = ExternalResourcesPage.goToPage(driver, BASIC_URL);
    }

    @Test(priority = 1, testName = "Opening External Resource View", description = "Opening External Resource View")
    @Description("Opening External Resource View")
    public void openExternalResourceViewTest() {
        Assert.assertEquals(externalResourcesPage.getViewTitle(), EXTERNAL_RESOURCE_VIEW_TITLE);
        Assert.assertFalse(externalResourcesPage.isExternalResourceTableEmpty());
    }

    @Test(priority = 2, testName = "Check details", description = "Check displaying details of selected External Resource")
    @Description("Check displaying details of selected External Resource")
    public void checkDetails() {
        boolean externalResourceExist = externalResourcesPage.externalResourceExistsIntoTable(EXTERNAL_RESOURCE_NAME);
        if (externalResourceExist) {
            externalResourcesPage.selectFirstExternalResourceInTable();
            Assert.assertEquals(externalResourcesPage.getValueFromExternalResourcePropertyPanel(NAME), EXTERNAL_RESOURCE_NAME);
            Assert.assertEquals(externalResourcesPage.getValueFromExternalResourcePropertyPanel(TYPE), DATABASE_TYPE);
        } else {
            Assert.fail("External Resource with name: " + EXTERNAL_RESOURCE_VIEW_TITLE + " does not exist");
        }
    }

    @Test(priority = 3, testName = "Type search", description = "Type search test")
    @Description("Type search test")
    public void typeSearch() {
        externalResourcesPage.setTypeSearch(DATABASE_TYPE);
        externalResourcesPage.selectFirstExternalResourceInTable();
        Assert.assertEquals(externalResourcesPage.getValueFromColumn(TYPE), DATABASE_TYPE);
    }
}

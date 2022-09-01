package com.oss.iaa.administrationpanel;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.administration.administrationpanel.ServicesPage;

import io.qameta.allure.Description;

public class ServicesViewTest extends BaseTestCase {

    private static final String SERVICE_NAME_COLUMN_LABEL = "Service Name";
    private static final String MODULE_NAME_COLUMN_LABEL = "Module Name";

    private ServicesPage servicesPage;

    @BeforeMethod
    public void openServicesView() {
        servicesPage = ServicesPage.goToServicesPage(driver);
    }

    @Test(priority = 1, testName = "Check Help button", description = "Check Help button")
    @Description("Check Help button")
    public void checkHelpButton() {
        Assert.assertFalse(servicesPage.isServicesTableEmpty());

        servicesPage.clickHelp();
        Assert.assertTrue(servicesPage.isTextInHelp());

        servicesPage.clickAccept();
        Assert.assertFalse(servicesPage.isServicesTableEmpty());
    }

    @Parameters({"serviceName"})
    @Test(priority = 2, testName = "Search Service Name", description = "Search Service Name")
    @Description("Search Service Name")
    public void searchServiceName(
            @Optional("bookmark-manager-core") String serviceName
    ) {
        servicesPage.searchForServiceName(serviceName);
        Assert.assertEquals(servicesPage.getValueFromFirstRow(SERVICE_NAME_COLUMN_LABEL), serviceName);
    }

    @Parameters({"moduleName"})
    @Test(priority = 3, testName = "Search Module Name", description = "Search Module Name")
    @Description("Search Module Name")
    public void searchModuleName(
            @Optional("ASWeb") String moduleName
    ) {
        servicesPage.searchForModuleName(moduleName);
        Assert.assertEquals(servicesPage.getValueFromFirstRow(MODULE_NAME_COLUMN_LABEL), moduleName);
    }

    @Test(priority = 4, testName = "Check Pages Navigation", description = "Check Pages Navigation")
    @Description("Check Pages Navigation")
    public void checkPages() {
        String firstServiceOnPage = servicesPage.getValueFromFirstRow(SERVICE_NAME_COLUMN_LABEL);
        servicesPage.goToNextPage();
        String firstServiceOnNewPage = servicesPage.getValueFromFirstRow(SERVICE_NAME_COLUMN_LABEL);
        Assert.assertNotEquals(firstServiceOnNewPage, firstServiceOnPage);
    }

    @Test(priority = 5, testName = "Refresh Services Table", description = "Check Refresh button in Services Table")
    @Description("Check Refresh button in Services Table")
    public void checkRefresh() {
        Assert.assertFalse(servicesPage.isServicesTableEmpty());

        servicesPage.clickRefresh();

        Assert.assertFalse(servicesPage.isServicesTableEmpty());
    }
}

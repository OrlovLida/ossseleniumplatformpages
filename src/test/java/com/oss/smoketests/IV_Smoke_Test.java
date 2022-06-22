package com.oss.smoketests;

import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.physical.LocationWizardPage;
import com.oss.pages.platform.HomePage;
import com.oss.pages.platform.NewInventoryViewPage;
import com.oss.pages.platform.SearchObjectTypePage;

import io.qameta.allure.Description;

public class IV_Smoke_Test extends BaseTestCase {

    @Test(priority = 1, description = "Open Inventory View Search Object Page")
    @Description("Open Inventory View Search Object Page")
    public void openInventoryViewSearchPage() {
        waitForPageToLoad();
        HomePage homePage = new HomePage(driver);
        homePage.chooseFromLeftSideMenu("Inventory View", "Resource Inventory ");
        waitForPageToLoad();
    }

    @Test(priority = 2, description = "Open Inventory View", dependsOnMethods = {"openInventoryViewSearchPage"})
    @Description("Open Inventory View for Site")
    public void loadInventoryView() {
        SearchObjectTypePage searchObjectTypePage = new SearchObjectTypePage(driver, webDriverWait);
        searchObjectTypePage.searchType("Site");
        waitForPageToLoad();
    }

    @Test(priority = 3, description = "Check context actions labels", dependsOnMethods = {"loadInventoryView"})
    @Description("Check context actions labels and open/cancel location wizard")
    public void checkContextActionsLabels() {
        NewInventoryViewPage newInventoryViewPage = new NewInventoryViewPage(driver, webDriverWait);
        newInventoryViewPage.callActionByLabel("Create", "Create Location");
        waitForPageToLoad();
        LocationWizardPage locationWizardPage = new LocationWizardPage(driver);
        locationWizardPage.clickCancel();
        waitForPageToLoad();
    }

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }
}

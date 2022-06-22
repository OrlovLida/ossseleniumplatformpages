package com.oss.smoketests;

import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.advancedsearch.AdvancedSearchWidget;
import com.oss.pages.physical.DeviceWizardPage;
import com.oss.pages.platform.HierarchyViewPage;
import com.oss.pages.platform.HomePage;
import com.oss.pages.platform.SearchObjectTypePage;

import io.qameta.allure.Description;

public class HV_Smoke_Test extends BaseTestCase {

    @Test(priority = 1, description = "Open Hierarchy View Search Object Page")
    @Description("Open Hierarchy View Search Object Page")
    public void openHierarchyViewSearchPage() {
        waitForPageToLoad();
        HomePage homePage = new HomePage(driver);
        homePage.chooseFromLeftSideMenu("Hierarchy View", "Resource Inventory ");
        waitForPageToLoad();
    }

    @Test(priority = 2, description = "Open Hierarchy View", dependsOnMethods = {"openHierarchyViewSearchPage"})
    @Description("Open Hierarchy View for Site")
    public void loadHierarchyView() {
        SearchObjectTypePage searchObjectTypePage = new SearchObjectTypePage(driver, webDriverWait);
        searchObjectTypePage.searchType("Physical Device");
        waitForPageToLoad();
        AdvancedSearchWidget advancedSearchWidget = AdvancedSearchWidget.createById(driver, webDriverWait, "advancedSearch");
        advancedSearchWidget.getTableComponent("advancedSearch").selectRow(0);
        advancedSearchWidget.clickAdd();
    }

    @Test(priority = 3, description = "Check context actions labels", dependsOnMethods = {"loadHierarchyView"})
    @Description("Check context actions labels and open/cancel physical device wizard")
    public void checkContextActionsLabels() {
        HierarchyViewPage hierarchyViewPage = new HierarchyViewPage(driver);
        hierarchyViewPage.callActionByLabel("Create", "Create Physical Device");
        waitForPageToLoad();
        DeviceWizardPage deviceWizardPage = new DeviceWizardPage(driver);
        deviceWizardPage.cancel();
        waitForPageToLoad();
    }

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }
}

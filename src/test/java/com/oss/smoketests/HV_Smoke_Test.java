package com.oss.smoketests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.components.layout.ErrorCard;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.advancedsearch.AdvancedSearchWidget;
import com.oss.pages.platform.HierarchyViewPage;
import com.oss.pages.platform.HomePage;
import com.oss.pages.platform.SearchObjectTypePage;

import io.qameta.allure.Description;

public class HV_Smoke_Test extends BaseTestCase {

    private static final Logger LOGGER = LoggerFactory.getLogger(HV_Smoke_Test.class);

    @Test(priority = 1, description = "Open Hierarchy View Search Object Page")
    @Description("Open Hierarchy View Search Object Page")
    public void openHierarchyViewSearchPage() {
        waitForPageToLoad();
        checkErrorPage();
        HomePage homePage = new HomePage(driver);
        homePage.chooseFromLeftSideMenu("Hierarchy View", "Resource Inventory ");
        waitForPageToLoad();
    }

    @Test(priority = 2, description = "Open Hierarchy View", dependsOnMethods = {"openHierarchyViewSearchPage"})
    @Description("Open Hierarchy View for Site")
    public void loadHierarchyView() {
        checkErrorPage();
        SearchObjectTypePage searchObjectTypePage = new SearchObjectTypePage(driver, webDriverWait);
        searchObjectTypePage.searchType("Physical Device");
        waitForPageToLoad();
        AdvancedSearchWidget advancedSearchWidget = AdvancedSearchWidget.createById(driver, webDriverWait, "advancedSearch");
        advancedSearchWidget.getTableComponent().selectRow(0);
        advancedSearchWidget.clickAdd();
    }

    @Test(priority = 3, description = "Check context actions labels", dependsOnMethods = {"loadHierarchyView"})
    @Description("Check context actions labels")
    public void checkContextActionsLabels() {
        checkErrorPage();
        HierarchyViewPage hierarchyViewPage = new HierarchyViewPage(driver);
        Assert.assertEquals(hierarchyViewPage.getGroupActionLabel("CREATE"), "Create");
        waitForPageToLoad();
    }

    private void checkErrorPage() {
        ErrorCard errorCard = ErrorCard.create(driver, webDriverWait);
        if (errorCard.isErrorPagePresent()) {
            ErrorCard.ErrorInformation errorInformation = errorCard.getErrorInformation();
            LOGGER.error(errorInformation.getErrorText());
            LOGGER.error(errorInformation.getErrorDescription());
            LOGGER.error(errorInformation.getErrorMessage());
            Assert.fail("Error Page is shown.");
        }
    }

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }
}

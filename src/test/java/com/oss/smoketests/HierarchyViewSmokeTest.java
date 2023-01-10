package com.oss.smoketests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.comarch.oss.web.pages.HierarchyViewPage;
import com.comarch.oss.web.pages.SearchObjectTypePage;
import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.GlobalNotificationContainer;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.layout.ErrorCard;
import com.oss.framework.components.mainheader.PerspectiveChooser;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.advancedsearch.AdvancedSearchWidget;
import com.oss.pages.platform.HomePage;

import io.qameta.allure.Description;

public class HierarchyViewSmokeTest extends BaseTestCase {

    private static final Logger LOGGER = LoggerFactory.getLogger(HierarchyViewSmokeTest.class);

    @Test(priority = 1, description = "Open Hierarchy View Search Object Page")
    @Description("Open Hierarchy View Search Object Page")
    public void openHierarchyViewSearchPage() {
        waitForPageToLoad();
        checkErrorPage();
        checkGlobalNotificationContainer();
        PerspectiveChooser.create(driver, webDriverWait).setLivePerspective();
        waitForPageToLoad();
        HomePage homePage = new HomePage(driver);
        homePage.chooseFromLeftSideMenu("Hierarchy View", "Resource Inventory");
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
        checkGlobalNotificationContainer();
        HierarchyViewPage hierarchyViewPage = HierarchyViewPage.getHierarchyViewPage(driver, webDriverWait);
        hierarchyViewPage.selectFirstObject();
        waitForPageToLoad();
        Assert.assertEquals(hierarchyViewPage.getGroupActionLabel(ActionsContainer.SHOW_ON_GROUP_ID), "Show on");
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

    private void checkGlobalNotificationContainer() {
        GlobalNotificationContainer globalNotificationContainer = GlobalNotificationContainer.create(driver, webDriverWait);
        if (globalNotificationContainer.isErrorNotificationPresent()) {
            GlobalNotificationContainer.NotificationInformation information = globalNotificationContainer.getNotificationInformation();
            LOGGER.error(information.getMessage());
            Assert.fail("Global Notification shows error.");
        }
    }

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }
}

package com.oss.smoketests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.comarch.oss.web.pages.SearchObjectTypePage;
import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.GlobalNotificationContainer;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.layout.ErrorCard;
import com.oss.framework.components.mainheader.PerspectiveChooser;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.platform.HomePage;
import com.comarch.oss.web.pages.NewInventoryViewPage;

import io.qameta.allure.Description;

public class InventoryViewSmokeTest extends BaseTestCase {

    private static final Logger LOGGER = LoggerFactory.getLogger(InventoryViewSmokeTest.class);

    @Test(priority = 1, description = "Open Inventory View Search Object Page")
    @Description("Open Inventory View Search Object Page")
    public void openInventoryViewSearchPage() {
        waitForPageToLoad();
        checkErrorPage();
        checkGlobalNotificationContainer();
        PerspectiveChooser.create(driver, webDriverWait).setLivePerspective();
        waitForPageToLoad();
        HomePage homePage = new HomePage(driver);
        homePage.chooseFromLeftSideMenu("Inventory View", "Resource Inventory");
        waitForPageToLoad();
    }

    @Test(priority = 2, description = "Open Inventory View", dependsOnMethods = {"openInventoryViewSearchPage"})
    @Description("Open Inventory View for Site")
    public void loadInventoryView() {
        checkErrorPage();
        SearchObjectTypePage searchObjectTypePage = new SearchObjectTypePage(driver, webDriverWait);
        searchObjectTypePage.searchType("Site");
        waitForPageToLoad();
    }

    @Test(priority = 3, description = "Check context actions labels", dependsOnMethods = {"loadInventoryView"})
    @Description("Check context actions label")
    public void checkContextActionsLabels() {
        checkErrorPage();
        checkGlobalNotificationContainer();
        NewInventoryViewPage newInventoryViewPage = new NewInventoryViewPage(driver, webDriverWait);
        Assert.assertEquals(newInventoryViewPage.getGroupActionLabel(ActionsContainer.CREATE_GROUP_ID), "Create");
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

package com.oss.smoketests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.components.layout.ErrorCard;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.platform.HomePage;
import com.oss.pages.platform.NewInventoryViewPage;
import com.oss.pages.platform.SearchObjectTypePage;

import io.qameta.allure.Description;

public class IV_Smoke_Test extends BaseTestCase {

    private static final Logger LOGGER = LoggerFactory.getLogger(IV_Smoke_Test.class);

    @Test(priority = 1, description = "Open Inventory View Search Object Page")
    @Description("Open Inventory View Search Object Page")
    public void openInventoryViewSearchPage() {
        waitForPageToLoad();
        checkErrorPage();
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
        NewInventoryViewPage newInventoryViewPage = new NewInventoryViewPage(driver, webDriverWait);
        Assert.assertEquals(newInventoryViewPage.getGroupActionLabel("CREATE"), "Create");
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

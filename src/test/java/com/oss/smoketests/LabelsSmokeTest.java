package com.oss.smoketests;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.comarch.oss.web.pages.NewInventoryViewPage;
import com.comarch.oss.web.pages.SearchObjectTypePage;
import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.GlobalNotificationContainer;
import com.oss.framework.components.layout.ErrorCard;
import com.oss.framework.components.mainheader.PerspectiveChooser;
import com.oss.framework.components.mainheader.ToolbarWidget;
import com.oss.framework.utils.DelayUtils;

import io.qameta.allure.Description;

public class LabelsSmokeTest extends BaseTestCase {

    private static final Logger LOGGER = LoggerFactory.getLogger(LabelsSmokeTest.class);
    private static final String RESOURCE_INVENTORY_CATEGORY = "Resource Inventory";
    private static final String INVENTORY_VIEW = "Inventory View";
    private static final String SITE_OBJECT_TYPE = "Site";
    private static final String DM_PREFIX = "DM_";
    private static final String TRANSLATIONS_ERROR_MESSAGE = "Not translated column header";

    @Test(priority = 1, description = "Open Inventory View Search Object Page")
    @Description("Open Inventory View Search Object Page")
    public void openInventoryViewSearchPage() {
        waitForPageToLoad();
        checkErrorPage();
        checkGlobalNotificationContainer();
        PerspectiveChooser.create(driver, webDriverWait).setLivePerspective();
        waitForPageToLoad();

        openInventoryViewForGivenObjectType(SITE_OBJECT_TYPE);
        checkInventoryViewTitle(INVENTORY_VIEW);
        selectObjectOnInventoryView();
        checkColumnsHeaders();
    }

    private void openInventoryViewForGivenObjectType(String objectType) {
        homePage.goToHomePage(driver, BASIC_URL);
        homePage.openApplication(RESOURCE_INVENTORY_CATEGORY, INVENTORY_VIEW);
        waitForPageToLoad();
        SearchObjectTypePage searchObjectTypePage = new SearchObjectTypePage(driver, webDriverWait);
        searchObjectTypePage.searchType(objectType);
        waitForPageToLoad();
    }

    private void selectObjectOnInventoryView() {
        NewInventoryViewPage newInventoryViewPage = new NewInventoryViewPage(driver, webDriverWait);
        newInventoryViewPage.selectFirstRow();
        waitForPageToLoad();
    }

    private void checkInventoryViewTitle(String expectedViewTitle) {
        ToolbarWidget toolbarWidget = ToolbarWidget.create(driver, webDriverWait);
        String viewTitle = toolbarWidget.getViewTitle();

        Assert.assertEquals(viewTitle, expectedViewTitle);
    }

    private void checkColumnsHeaders() {
        NewInventoryViewPage newInventoryViewPage = new NewInventoryViewPage(driver, webDriverWait);
        List<String> columnHeaders = newInventoryViewPage.getActiveColumnsHeaders();

        Assert.assertFalse(columnHeaders.contains(DM_PREFIX), TRANSLATIONS_ERROR_MESSAGE);
    }

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
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
}

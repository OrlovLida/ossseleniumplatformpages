package com.oss.smoketests;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.google.common.collect.ImmutableList;
import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.GlobalNotificationContainer;
import com.oss.framework.components.layout.ErrorCard;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.platform.HomePage;
import com.oss.pages.reconciliation.VS20Page;

import io.qameta.allure.Description;

public class VSViewer20SmokeTest extends BaseTestCase {

    private static final Logger LOGGER = LoggerFactory.getLogger(VSViewer20SmokeTest.class);
    private static final String PATH = "Network Discovery and Reconciliation";
    private static final String VIEW = "VS Viewer 2.0";
    private static final String INCORRECT_COLUMN_NAME = "Incorrect column name.";
    private static final String INCORRECT_COLUMN_NUMBER = "Incorrect column number.";
    private static final List<String> DEFAULT_COLUMN_NAMES = new ImmutableList.Builder<String>()
            .add("CM Domain Name")
            .add("Type")
            .add("Distinguish Name")
            .add("Is Changed")
            .add("Is Removed")
            .add("Is Root")
            .build();

    @Test(priority = 1, description = "Open VS Viewer 2.0 Page")
    @Description("Open VS Viewer 2.0 Page")
    public void openVS20Page() {
        waitForPageToLoad();
        checkErrorPage();
        checkGlobalNotificationContainer();
        HomePage homePage = new HomePage(driver);
        homePage.chooseFromLeftSideMenu(VIEW, PATH);
        waitForPageToLoad();
    }

    @Test(priority = 2, description = "Check VS Viewer 2.0 table", dependsOnMethods = {"openVS20Page"})
    @Description("Check VS Viewer 2.0 table")
    public void checkInterfacesTable() {
        checkErrorPage();
        VS20Page vs20Page = new VS20Page(driver);
        List<String> columnsList = vs20Page.getColumnsHeaders();
        Assert.assertEquals(columnsList.size(), DEFAULT_COLUMN_NAMES.size(), INCORRECT_COLUMN_NUMBER);
        SoftAssert softAssert = new SoftAssert();
        for (int i = 0; i < DEFAULT_COLUMN_NAMES.size(); i++) {
            softAssert.assertEquals(DEFAULT_COLUMN_NAMES.get(i), columnsList.get(i), INCORRECT_COLUMN_NAME);
        }
        softAssert.assertAll();
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

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
import com.oss.pages.reconciliation.InterfacesPage;

import io.qameta.allure.Description;

public class InterfacesSmokeTest extends BaseTestCase {

    private static final Logger LOGGER = LoggerFactory.getLogger(InterfacesSmokeTest.class);
    private static final String PATH = "Network Discovery and Reconciliation";
    private static final String VIEW = "Interfaces";
    private static final String PROPERTIES = "Properties";
    private static final String CM_DOMAINS = "CM Domains";
    private static final String HAS_NO_DATA_ASSERT = "Interfaces view has no data.";
    private static final String INCORRECT_TAB_NAME = "Incorrect tab name.";
    private static final String INCORRECT_COLUMN_NAME = "Incorrect column name.";
    private static final String INCORRECT_PROPERTY_NAME = "Incorrect property name.";
    private static final String INCORRECT_COLUMN_NUMBER = "Incorrect column number.";
    private static final String INCORRECT_PROPERTIES_NUMBER = "Incorrect properties number.";
    private static final List<String> DEFAULT_COLUMN_LIST = new ImmutableList.Builder<String>()
            .add("Name")
            .add("Vendor")
            .add("Vendor Interface Name")
            .add("Narrow reconciliation supported")
            .add("Extended narrow reconciliation supported")
            .add("Discovery reconciliation supported")
            .add("Near Real Time Reconciliation supported")
            .add("Flexible Attributes supported")
            .add("NBI availability supported")
            .add("NBI integrity supported")
            .add("NBI version supported")
            .add("Management System handling mode")
            .add("Management System Resource Specification identifier")
            .add("Id")
            .build();
    private static final List<String> DEFAULT_PROPERTIES_LIST = new ImmutableList.Builder<String>()
            .add("Id")
            .add("Name")
            .add("Vendor")
            .add("Mediation adapter name")
            .add("Uploaders")
            .add("Management System handling mode")
            .add("Management System Resource Specification identifier")
            .add("Mode")
            .add("Narrow reconciliation supported")
            .add("Extended narrow reconciliation supported")
            .add("Discovery reconciliation supported")
            .add("Near Real Time Reconciliation supported")
            .add("Flexible Attributes supported")
            .add("NBI integrity supported")
            .add("NBI availability supported")
            .add("NBI version supported")
            .add("Number of CM Domains")
            .add("Processing requires disc space")
            .add("Protocol")
            .add("Short Name")
            .add("Vendor Interface Name")
            .add("VS Distinguish Name Separator")
            .add("Data format")
            .add("Data source type")
            .add("Default connection port")
            .build();

    @Test(priority = 1, description = "Open Interfaces Page")
    @Description("Open Interfaces Page")
    public void openInterfacesPage() {
        waitForPageToLoad();
        checkErrorPage();
        checkGlobalNotificationContainer();
        HomePage homePage = new HomePage(driver);
        homePage.chooseFromLeftSideMenu(VIEW, PATH);
        waitForPageToLoad();
    }

    @Test(priority = 2, description = "Check Interfaces table", dependsOnMethods = {"openInterfacesPage"})
    @Description("Check Interfaces table")
    public void checkInterfacesTable() {
        checkErrorPage();
        InterfacesPage interfacesPage = InterfacesPage.getInterfacesPage(driver, webDriverWait);
        Assert.assertFalse(interfacesPage.hasNoData(), HAS_NO_DATA_ASSERT);
        List<String> columnsList = interfacesPage.getColumnsHeaders();
        Assert.assertEquals(columnsList.size(), DEFAULT_COLUMN_LIST.size(), INCORRECT_COLUMN_NUMBER);
        SoftAssert softAssert = new SoftAssert();
        for (int i = 0; i < DEFAULT_COLUMN_LIST.size(); i++) {
            softAssert.assertEquals(columnsList.get(i), DEFAULT_COLUMN_LIST.get(i), INCORRECT_COLUMN_NAME);
        }
        softAssert.assertAll();
    }

    @Test(priority = 3, description = "Check Interfaces tabs", dependsOnMethods = {"openInterfacesPage"})
    @Description("Check Interfaces tabs")
    public void checkInterfacesTabs() {
        InterfacesPage interfacesPage = InterfacesPage.getInterfacesPage(driver, webDriverWait);
        interfacesPage.selectFirstInterface();
        waitForPageToLoad();
        checkErrorPage();
        checkGlobalNotificationContainer();
        SoftAssert tabNamesAssert = new SoftAssert();
        tabNamesAssert.assertEquals(interfacesPage.getTabLabel(0), PROPERTIES, INCORRECT_TAB_NAME);
        tabNamesAssert.assertEquals(interfacesPage.getTabLabel(1), CM_DOMAINS, INCORRECT_TAB_NAME);
        tabNamesAssert.assertAll();
        List<String> propertiesLabels = interfacesPage.getPropertiesNames();
        Assert.assertEquals(propertiesLabels.size(), DEFAULT_PROPERTIES_LIST.size(), INCORRECT_PROPERTIES_NUMBER);
        SoftAssert propertiesAssert = new SoftAssert();
        for (int i = 0; i < DEFAULT_PROPERTIES_LIST.size(); i++) {
            propertiesAssert.assertEquals(propertiesLabels.get(i), DEFAULT_PROPERTIES_LIST.get(i), INCORRECT_PROPERTY_NAME);
        }
        propertiesAssert.assertAll();
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

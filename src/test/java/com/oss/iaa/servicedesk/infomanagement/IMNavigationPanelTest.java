package com.oss.iaa.servicedesk.infomanagement;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.iaa.NavigationPanelPage;

import io.qameta.allure.Description;

public class IMNavigationPanelTest extends BaseTestCase {

    private static final String IM_CATEGORY = "Info Management";
    private static final String MESSAGES_SUBCATEGORY = "Messages";
    private static final String MESSAGES_URL_SUFFIX = "views/info-management/notifications";
    private static final String TEMPLATES_SUBCATEGORY = "Templates";
    private static final String TEMPLATES_URL_SUFFIX = "view/info-management/template-list";
    private static final String CONFIGURATION_SUBCATEGORY = "Configuration Panel";
    private static final String CONFIGURATION_URL_SUFFIX = "view/info-management/mail-box/configuration";
    private NavigationPanelPage navigationPanelPage;

    @BeforeMethod
    public void goToNavigationPanelPage() {
        navigationPanelPage = NavigationPanelPage.goToHomePage(driver, BASIC_URL);
    }

    @Test(priority = 1, testName = "Check Messages button", description = "Check Messages button")
    @Description("Check Messages button")
    public void checkMessagesButton() {
        navigationPanelPage.openApplicationInCategory(IM_CATEGORY, MESSAGES_SUBCATEGORY);
        Assert.assertTrue(navigationPanelPage.isChosenUrlOpen(MESSAGES_URL_SUFFIX));
    }

    @Test(priority = 2, testName = "Check Templates button", description = "Check Templates button")
    @Description("Check Templates button")
    public void checkTemplatesButton() {
        navigationPanelPage.openApplicationInCategory(IM_CATEGORY, TEMPLATES_SUBCATEGORY);
        Assert.assertTrue(navigationPanelPage.isChosenUrlOpen(TEMPLATES_URL_SUFFIX));
    }

    @Test(priority = 3, testName = "Check Configuration Panel button", description = "Check Configuration Panel button")
    @Description("Check Configuration Panel button")
    public void checkConfigurationButton() {
        navigationPanelPage.openApplicationInCategory(IM_CATEGORY, CONFIGURATION_SUBCATEGORY);
        Assert.assertTrue(navigationPanelPage.isChosenUrlOpen(CONFIGURATION_URL_SUFFIX));
    }
}
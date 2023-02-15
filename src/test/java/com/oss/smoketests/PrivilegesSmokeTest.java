package com.oss.smoketests;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.alerts.SystemMessageInterface;
import com.oss.framework.components.layout.ErrorCard;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.administration.permissions.UserPermissionsPage;
import com.oss.pages.platform.HomePage;

import io.qameta.allure.Description;

public class PrivilegesSmokeTest extends BaseTestCase {
    private static final String EXPECTED_ERROR_TEXT = "Error 403";
    private static final String EXPECTED_ERROR_DESCRIPTION = "Access denied";
    private static final String EXPECTED_ERROR_MESSAGE = "You don't have permission to visit this page.";
    private static final String USER_LOGIN = "privilegestestuser";
    private static final String NETWORK_VIEW_PATTERN = "%s/#/view/transport/trail/network?perspective=LIVE";
    private static final String USER_PERMISSIONS_TILE = "User Permissions";
    private static final String ADMINISTRATION_TILE = "Administration";
    private static final String PERMISSIONS_TILE = "Permissions";
    private static final String NETWORK_VIEWER_PROFILE = "Network Viewer";
    private static final String PROFILE_NAME_COLUMN = "Profile name";
    private static final String USER_PROFILES_TAB = "User profiles";
    private static final String ALL_PROFILES_TAB = "All profiles";
    private static final String PROFILES_TAB_ID = "1";
    private static final String ASSIGN_PRIVILEGE_MESSAGE = "Profile assigned successfully";
    private static final String DELETE_PRIVILEGE_MESSAGE = "Profile deleted successfully";

    @Test(priority = 1, description = "Check Home Page")
    @Description("Check Home Page")
    public void checkHomePage() {
        waitForPageToLoad();
        Assert.assertFalse(isErrorPageDisplayed());
    }

    @Test(priority = 2, description = "Open Network View without privileges", dependsOnMethods = {"checkHomePage"})
    @Description("Open Network View without privileges")
    public void openNetworkViewWithoutPrivileges() {
        openNetworkView();

        Assert.assertTrue(isErrorPageDisplayed());
        ErrorCard.ErrorInformation errorInformation = ErrorCard.create(driver, webDriverWait).getErrorInformation();
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(errorInformation.getErrorText(), EXPECTED_ERROR_TEXT);
        softAssert.assertEquals(errorInformation.getErrorDescription(), EXPECTED_ERROR_DESCRIPTION);
        softAssert.assertEquals(errorInformation.getErrorMessage(), EXPECTED_ERROR_MESSAGE);
        softAssert.assertAll();

        closeMessage();
    }

    @Test(priority = 3, description = "Open and set User Permissions", dependsOnMethods = {"openNetworkViewWithoutPrivileges"})
    @Description("Open and set User Permissions")
    public void openAndSetUserPermissions() {
        HomePage homePage = new HomePage(driver);
        homePage.chooseFromLeftSideMenu(USER_PERMISSIONS_TILE, ADMINISTRATION_TILE, PERMISSIONS_TILE);
        waitForPageToLoad();

        UserPermissionsPage userPermissionsPage = new UserPermissionsPage(driver, webDriverWait);
        userPermissionsPage.searchForUserInUsersPanel(USER_LOGIN);
        userPermissionsPage.selectFirstUserInUserPanelList();

        userPermissionsPage.selectTabInPermissionsPanel(PROFILES_TAB_ID);
        userPermissionsPage.clickButtonWithLabel(USER_PROFILES_TAB);
        Assert.assertFalse(isObjectPresentInTab(NETWORK_VIEWER_PROFILE));

        userPermissionsPage.clickButtonWithLabel(ALL_PROFILES_TAB);
        userPermissionsPage.searchInUserProfileTable(NETWORK_VIEWER_PROFILE);
        userPermissionsPage.selectFirstRow();
        waitForPageToLoad();

        userPermissionsPage.clickAssign();
        checkPopup(ASSIGN_PRIVILEGE_MESSAGE);

        userPermissionsPage.clickButtonWithLabel(USER_PROFILES_TAB);
        Assert.assertTrue(isObjectPresentInTab(NETWORK_VIEWER_PROFILE));
    }

    @Test(priority = 4, description = "Open Network View with required privileges", dependsOnMethods = {"openAndSetUserPermissions"})
    @Description("Open Network View with required privileges")
    public void openNetworkViewWithRequiredPrivileges() {
        openNetworkView();
        Assert.assertFalse(isErrorPageDisplayed());
    }

    @Test(priority = 5, description = "Open and delete User Permissions", dependsOnMethods = {"openAndSetUserPermissions"})
    @Description("Open and delete User Permissions")
    public void openAndDeleteUserPermissions() {
        HomePage homePage = new HomePage(driver);
        homePage.chooseFromLeftSideMenu(USER_PERMISSIONS_TILE, ADMINISTRATION_TILE, PERMISSIONS_TILE);
        waitForPageToLoad();

        UserPermissionsPage userPermissionsPage = new UserPermissionsPage(driver, webDriverWait);
        userPermissionsPage.searchForUserInUsersPanel(USER_LOGIN);
        userPermissionsPage.selectFirstUserInUserPanelList();

        userPermissionsPage.selectTabInPermissionsPanel(PROFILES_TAB_ID);
        userPermissionsPage.clickButtonWithLabel(USER_PROFILES_TAB);
        userPermissionsPage.searchInUserProfileTable(NETWORK_VIEWER_PROFILE);
        userPermissionsPage.selectFirstRow();
        waitForPageToLoad();

        userPermissionsPage.clickDivest();
        checkPopup(DELETE_PRIVILEGE_MESSAGE);

        userPermissionsPage.clickButtonWithLabel(USER_PROFILES_TAB);
        Assert.assertFalse(isObjectPresentInTab(NETWORK_VIEWER_PROFILE));
    }

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    private void openNetworkView() {
        driver.get(String.format(NETWORK_VIEW_PATTERN, BASIC_URL));
        waitForPageToLoad();
    }

    private boolean isObjectPresentInTab(String profileName) {
        UserPermissionsPage userPermissionsPage = new UserPermissionsPage(driver, webDriverWait);
        if (!userPermissionsPage.getUserProfileTable().hasNoData()) {
            return userPermissionsPage.getUserProfileTable().isValuePresent(PROFILE_NAME_COLUMN, profileName);
        } else return false;
    }

    private void closeMessage() {
        SystemMessageContainer.create(driver, webDriverWait).close();
        waitForPageToLoad();
    }

    private boolean isErrorPageDisplayed() {
        return ErrorCard.create(driver, webDriverWait).isErrorPagePresent();
    }

    private void checkPopup(String message) {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assert.assertEquals(messages.get(0).getMessageType(), SystemMessageContainer.MessageType.SUCCESS);
        Assert.assertEquals((messages.get(0).getText()), message);
        waitForPageToLoad();
    }
}

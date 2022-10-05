package com.oss.iaa.administrationpanel;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.administration.administrationpanel.UsersPage;

import io.qameta.allure.Description;

import static com.oss.configuration.Configuration.CONFIGURATION;

public class UsersViewTest extends BaseTestCase {

    private UsersPage usersPage;

    @BeforeMethod
    public void openUsersView() {
        usersPage = UsersPage.goToUsersPage(driver);
    }

    @Test(priority = 1, testName = "Check Help button", description = "Check Help button")
    @Description("Check Help button")
    public void checkHelpButton() {
        Assert.assertFalse(usersPage.isUsersTableEmpty());

        usersPage.clickHelp();
        Assert.assertTrue(usersPage.isTextInHelp());

        usersPage.clickAccept();
        Assert.assertFalse(usersPage.isUsersTableEmpty());
    }

    @Test(priority = 2, testName = "Check Refresh button in Users table", description = "Check Refresh button in Users table")
    @Description("Check Refresh button in Users table")
    public void checkRefreshUsers() {
        Assert.assertFalse(usersPage.isUsersTableEmpty());

        usersPage.clickRefreshInUsersTable();

        Assert.assertFalse(usersPage.isUsersTableEmpty());
    }

    @Test(priority = 3, testName = "Check Search", description = "Check Search - search for current logged user")
    @Description("Check Search - search for current logged user")
    public void checkSearch() {
        usersPage.searchForUser(CONFIGURATION.getLogin());
        Assert.assertEquals(usersPage.getFirstLoginFromUsersTable(), CONFIGURATION.getLogin());
    }

    @Test(priority = 4, testName = "Check Refresh button in Sessions table", description = "Check Refresh button in Session table")
    @Description("Check Refresh button in Session table")
    public void checkRefreshSessions() {
        Assert.assertFalse(usersPage.isSessionsTableEmpty());

        usersPage.clickRefreshInSessionsTable();

        Assert.assertFalse(usersPage.isSessionsTableEmpty());
    }

    @Test(priority = 10, testName = "Logout test", description = "Logging out user using Administration Panel")
    @Description("Logging out user using Administration Panel")
    public void logoutTest() {
        usersPage.clickReloadButton();
        usersPage.searchForUser(CONFIGURATION.getLogin());
        usersPage.selectFirstRowInUsersTable();
        usersPage.clickLogoutUser();
        usersPage.confirmLogout();
        usersPage.openLoginPage();
        Assert.assertTrue(usersPage.isLoginPageOpened());

        usersPage.logInUser();
        Assert.assertEquals(usersPage.getViewTitle(), "Home");
    }
}

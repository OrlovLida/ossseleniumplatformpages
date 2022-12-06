package com.oss.pages.administration.administrationpanel;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.prompts.ConfirmationBox;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.platform.LoginPage;

import io.qameta.allure.Step;

import java.time.Duration;

import static com.oss.serviceClient.ServicesClient.BASIC_URL;

public class UsersPage extends BaseAdminPanelPage {

    private static final String USERS_PAGE_URL = String.format("%s/#/view/admin/users", BASIC_URL);
    private static final String USERS_TABLE_ID = "ADMINISTRATION_USERS_TABLE_APP_ID";
    private static final String HELP_BUTTON_ID = "USER_HELP_ACTION_ID";
    private static final String HTML_EDITOR_ID = "HELP_HTML_EDITOR_READ_ONLY_COMPONENT_ID";
    private static final String HELP_HEADER_TEXT = "Administration - Users Help";
    private static final String LOGIN_COLUMN_LABEL = "Login (username)";
    private static final String SESSIONS_TABLE_ID = "ADMINISTRATION_USER_SESSIONS_TABLE_APP_ID";
    private static final String RELOAD_BUTTON_ID = "USER_RELOAD_ACTION";
    private static final String LOGOUT_USER_BUTTON_ID = "LOGOUT_USER_WEB_ACTION";
    private static final String LOGOUT_LABEL = "Logout";

    public UsersPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public static UsersPage goToUsersPage(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(45));
        goToPage(driver, wait, USERS_PAGE_URL);
        return new UsersPage(driver, wait);
    }

    @Step("Check if Users table is empty")
    public boolean isUsersTableEmpty() {
        return isTableEmpty(USERS_TABLE_ID);
    }

    @Step("Check if Sessions table is empty")
    public boolean isSessionsTableEmpty() {
        return isTableEmpty(SESSIONS_TABLE_ID);
    }

    @Step("Click Help button")
    public void clickHelp() {
        callActionInTable(USERS_TABLE_ID, HELP_BUTTON_ID);
    }

    @Step("Check if text in Help is displayed")
    public boolean isTextInHelp() {
        return isTextInHelp(HTML_EDITOR_ID, HELP_HEADER_TEXT);
    }

    @Step("Click Refresh in Users table")
    public void clickRefreshInUsersTable() {
        clickRefreshInTable(USERS_TABLE_ID);
        log.info("Clicking Refresh button in Users table");
    }

    @Step("Click Refresh is Sessions table")
    public void clickRefreshInSessionsTable() {
        clickRefreshInTable(SESSIONS_TABLE_ID);
        log.info("Clicking Refresh button in Sessions table");
    }

    @Step("Search for user: {userName}")
    public void searchForUser(String userName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        searchInTable(USERS_TABLE_ID, userName);
        log.info("Searching for user: {}", userName);
    }

    @Step("Get Login from first row in Users table")
    public String getFirstLoginFromUsersTable() {
        return getFirstValueFromTable(USERS_TABLE_ID, LOGIN_COLUMN_LABEL);
    }

    @Step("Select first row in Users table")
    public void selectFirstRowInUsersTable() {
        DelayUtils.waitForPageToLoad(driver, wait);
        selectFirstRowInTable(USERS_TABLE_ID);
        log.info("Selecting first row in Users table");
    }

    @Step("Click Reload button")
    public void clickReloadButton() {
        callActionInTable(USERS_TABLE_ID, RELOAD_BUTTON_ID);
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Clicking Reload button");
    }

    @Step("Click Logout user from web sessions")
    public void clickLogoutUser() {
        DelayUtils.waitForPageToLoad(driver, wait);
        callActionInTable(USERS_TABLE_ID, LOGOUT_USER_BUTTON_ID);
        log.info("Clicking Logout user from web sessions button");
    }

    @Step("Click Logout in Confirmation Box")
    public void confirmLogout() {
        ConfirmationBox.create(driver, wait).clickButtonByLabel(LOGOUT_LABEL);
        log.info("Clicking Logout in Confirmation Box");
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("Open Login Page")
    public void openLoginPage() {
        new LoginPage(driver, BASIC_URL).open();
        log.info("Opening Login Page");
    }

    @Step("Check if Login page is displayed")
    public boolean isLoginPageOpened() {
        return new LoginPage(driver, BASIC_URL).isLoginPageDisplayed();
    }

    @Step("Log in User")
    public void logInUser() {
        new LoginPage(driver, BASIC_URL).login();
        log.info("Logging user to console");
    }
}
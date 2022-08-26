package com.oss.pages.administration.administrationpanel;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.inputs.HtmlEditor;
import com.oss.framework.components.prompts.ConfirmationBox;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.table.OldTable;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;
import com.oss.pages.platform.LoginPage;

import io.qameta.allure.Step;

import static com.oss.transport.infrastructure.ServicesClient.BASIC_URL;

public class UsersPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(UsersPage.class);
    private static final String USERS_PAGE_URL = String.format("%s/#/view/admin/users", BASIC_URL);
    private static final String USERS_TABLE_ID = "ADMINISTRATION_USERS_TABLE_APP_ID";
    private static final String HELP_BUTTON_ID = "USER_HELP_ACTION_ID";
    private static final String HTML_EDITOR_ID = "HELP_HTML_EDITOR_READ_ONLY_COMPONENT_ID";
    private static final String HELP_HEADER_TEXT = "Administration - Users Help";
    private static final String HELP_WIZARD_ID = "ADMINISTRATIVE_PANEL_HELP_WIZARD_ID";
    private static final String REFRESH_BUTTON_ID = "tableRefreshButton";
    private static final String LOGIN_COLUMN_LABEL = "Login (username)";
    private static final String SESSIONS_TABLE_ID = "ADMINISTRATION_USER_SESSIONS_TABLE_APP_ID";
    private static final String RELOAD_BUTTON_ID = "USER_RELOAD_ACTION";
    private static final String LOGOUT_USER_BUTTON_ID = "LOGOUT_USER_WEB_ACTION";
    private static final String LOGOUT_LABEL = "Logout";

    public UsersPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public static UsersPage goToPage(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, 45);
        driver.get(USERS_PAGE_URL);
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Opened page: {}", USERS_PAGE_URL);
        return new UsersPage(driver, wait);
    }

    @Step("Check if Users table is empty")
    public boolean isUsersTableEmpty() {
        return getUsersTable().hasNoData();
    }

    @Step("Check if Sessions table is empty")
    public boolean isSessionsTableEmpty() {
        return getSessionsTable().hasNoData();
    }

    @Step("Click Help button")
    public void clickHelp() {
        getUsersTable().callAction(HELP_BUTTON_ID);
        log.info("Clicking Help button");
    }

    @Step("Check if text in Help is displayed")
    public boolean isTextInHelp() {
        return HtmlEditor.create(driver, wait, HTML_EDITOR_ID).getStringValue().contains(HELP_HEADER_TEXT);
    }

    @Step("Click Accept")
    public void clickAccept() {
        Wizard.createByComponentId(driver, wait, HELP_WIZARD_ID).clickAccept();
        log.info("Clicking Accept button");
    }

    @Step("Click Refresh in Users table")
    public void clickRefreshInUsersTable() {
        getUsersTable().callAction(ActionsContainer.KEBAB_GROUP_ID, REFRESH_BUTTON_ID);
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Clicking Refresh button in Users table");
    }

    @Step("Click Refresh is Sessions table")
    public void clickRefreshInSessionsTable() {
        getSessionsTable().callAction(ActionsContainer.KEBAB_GROUP_ID, REFRESH_BUTTON_ID);
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Clicking Refresh button in Sessions table");
    }

    @Step("Search for user: {userName}")
    public void searchForUser(String userName) {
        getUsersTable().fullTextSearch(userName);
        log.info("Searching for user: {}", userName);
    }

    @Step("Get Login from first row in Users table")
    public String getFirstLoginFromUsersTable() {
        return getUsersTable().getCellValue(0, LOGIN_COLUMN_LABEL);
    }

    @Step("Select first row in Users table")
    public void selectFirstRowInUsersTable() {
        getUsersTable().selectFirstRow();
        log.info("Selecting first row in Users table");
    }

    @Step("Click Reload button")
    public void clickReloadButton() {
        getUsersTable().callAction(RELOAD_BUTTON_ID);
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Clicking Reload button");
    }

    @Step("Click Logout user from web sessions")
    public void clickLogoutUser() {
        getUsersTable().callAction(LOGOUT_USER_BUTTON_ID);
        log.info("Clicking Logout user from web sessions button");
    }

    @Step("Click Logout in Confirmation Box")
    public void confirmLogout() {
        ConfirmationBox.create(driver, wait).clickButtonByLabel(LOGOUT_LABEL);
        log.info("Clicking Logout in Confirmation Box");
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("Log in user")
    public void logInUser() {
        new LoginPage(driver, USERS_PAGE_URL).open().login();
        log.info("Logging user");
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    private OldTable getUsersTable() {
        return OldTable.createById(driver, wait, USERS_TABLE_ID);
    }

    private OldTable getSessionsTable() {
        return OldTable.createById(driver, wait, SESSIONS_TABLE_ID);
    }
}

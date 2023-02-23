package com.oss.pages.administration.permissions;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.contextactions.OldActionsContainer;
import com.oss.framework.components.inputs.Button;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.list.CommonList;
import com.oss.framework.widgets.table.OldTable;
import com.oss.framework.widgets.tabs.TabsWidget;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class UserPermissionsPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(UserPermissionsPage.class);
    private static final String USERS_PANEL_ID = "usersPanelAppId";
    private static final String PERMISSIONS_PANEL_ID = "user_permissionsPanelTemplateId";
    private static final String PROFILES_TABLE_ID = "user_profilesPanelListAppId";
    private static final String OLD_ACTIONS_CONTAINER_ID = "user_permissionsPanelAppId-windowToolbar";
    private static final String ASSIGN_ACTION_ID = "addAction";
    private static final String DIVEST_ACTION_ID = "deleteAction";
    private static final String DIRECT_INHERITED_COLUMN_LABEL = "Direct/Inherited";
    private static final String DIRECT_ROLE = "Direct";
    private static final String INHERITED_ROLE = "Inherited";
    private static final String ROLE_NOT_ASSIGNED_LABEL = "None";

    public UserPermissionsPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("Go to User Permissions page")
    public static UserPermissionsPage goToPage(WebDriver driver, String basicURL) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(45));
        String pageUrl = String.format("%s/#/view/authorization-service/user-permissions", basicURL);
        driver.get(pageUrl);
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Opened page: {}", pageUrl);
        return new UserPermissionsPage(driver, wait);
    }

    @Step("Search for user: {user} in Users Panel search")
    public void searchForUserInUsersPanel(String user) {
        getUsersPanelList().fullTextSearch(user);
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Searching for user: {}", user);
    }

    @Step("Select first user on the list")
    public void selectFirstUserInUserPanelList() {
        getUsersPanelList().selectRow(0);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("Select tab with id: {tabId} in Permissions Panel")
    public void selectTabInPermissionsPanel(String tabId) {
        getPermissionsPanel().selectTabById(tabId);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("Search for: {searchText} in User Profile Table on Permission Panel")
    public void searchInUserProfileTable(String searchText) {
        getUserProfileTable().fullTextSearch(searchText);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("Click button: {label}")
    public void clickButtonWithLabel(String label) {
        Button.createByLabel(driver, label).click();
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("Get profile status from table")
    public String getProfileStatus() {
        if (!getUserProfileTable().hasNoData()) {
            return getUserProfileTable().getCellValue(0, DIRECT_INHERITED_COLUMN_LABEL);
        }
        return "Table has no data";
    }

    @Step("Check if role is assigned direct or inherited")
    public boolean isRoleAssigned() {
        return getProfileStatus().equals(DIRECT_ROLE) || getProfileStatus().equals(INHERITED_ROLE);
    }

    @Step("Click Assign")
    public void assignRole() {
        if (!isAssigned()) {
            selectFirstRow();
            clickAssign();
        }
    }

    public void selectFirstRow() {
        getUserProfileTable().selectFirstRow();
    }

    public void clickAssign() {
        getOldActionsContainer().callActionById(ASSIGN_ACTION_ID);
    }

    public void clickDivest() {
        getOldActionsContainer().callActionById(DIVEST_ACTION_ID);
    }

    private OldActionsContainer getOldActionsContainer() {
        return OldActionsContainer.createById(driver, wait, OLD_ACTIONS_CONTAINER_ID);
    }

    private boolean isAssigned() {
        return !getProfileStatus().equals(ROLE_NOT_ASSIGNED_LABEL);
    }

    private CommonList getUsersPanelList() {
        return CommonList.create(driver, wait, USERS_PANEL_ID);
    }

    private TabsWidget getPermissionsPanel() {
        return TabsWidget.createById(driver, wait, PERMISSIONS_PANEL_ID);
    }

    public OldTable getUserProfileTable() {
        return OldTable.createById(driver, wait, PROFILES_TABLE_ID);
    }
}

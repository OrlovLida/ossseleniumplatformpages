package com.oss.pages.iaa.bigdata.dfe.servergroup;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.utils.DelayUtils;
import com.oss.pages.iaa.bigdata.dfe.BaseDfePage;

import io.qameta.allure.Step;

import java.time.Duration;

public class ServerGroupPage extends BaseDfePage {

    private static final String TABLE_ID = "server-groupAppId";
    private static final String SEARCH_INPUT_ID = "input_server-groupSearchAppId";
    private static final String ADD_NEW_SERVER_GROUP_LABEL = "Add New Server Group";
    private static final String EDIT_SERVER_GROUP_LABEL = "Edit Server Group";
    private static final String DELETE_SERVER_GROUP_LABEL = "Delete Server Group";
    private static final String SERVER_GROUP_NAME_COLUMN_LABEL = "Name";
    private static final String TABS_WIDGET_ID = "card-content_serverGroupTabsId";
    private static final String SERVERS_TAB = "Servers";
    private static final String CONFIRM_DELETE_LABEL = "Delete";
    private static final String TAB_WIDGET_ID = "card-content_serverGroupTabsId";
    private static final String DETAILS_TAB = "Details";
    private static final String PROPERTY_PANEL_ID = "server-group/tabs/detailsAppId";
    private static final String NAME_PROPERTY = "Name";

    public ServerGroupPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("I Open Server Group View")
    public static ServerGroupPage goToPage(WebDriver driver, String basicURL) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(45));
        BaseDfePage.openDfePage(driver, basicURL, wait, "server-group");

        return new ServerGroupPage(driver, wait);
    }

    @Step("I click add new Server Group")
    public void clickAddNewServerGroup() {
        clickContextActionAdd();
    }

    @Step("I check if server group: {name} exists into the table")
    public boolean serverGroupExistIntoTable(String name) {
        return feedExistIntoTable(name, SERVER_GROUP_NAME_COLUMN_LABEL);
    }

    @Step("I select found Server Group")
    public void selectFoundServerGroup() {
        getTable().selectRow(0);
    }

    @Step("Select Details tab")
    public void selectDetailsTab() {
        selectTab(TAB_WIDGET_ID, DETAILS_TAB);
    }

    @Step("I click edit Server Group")
    public void clickEditServerGroup() {
        clickContextActionEdit();
    }

    @Step("I click delete Server Group")
    public void clickDeleteServerGroup() {
        clickContextActionDelete();
    }

    @Step("I click Servers Tab")
    public void selectServersTab() {
        selectTab(TABS_WIDGET_ID, SERVERS_TAB);
    }

    @Step("I confirm the removal")
    public void clickConfirmDelete() {
        confirmDelete(CONFIRM_DELETE_LABEL);
    }

    @Step("Check name in details tab")
    public String checkNameInPropertyPanel() {
        DelayUtils.waitForPageToLoad(driver, wait);
        return getValueFromPropertyPanel(PROPERTY_PANEL_ID, NAME_PROPERTY);
    }

    @Override
    public String getTableId() {
        return TABLE_ID;
    }

    @Override
    public String getContextActionAddLabel() {
        return ADD_NEW_SERVER_GROUP_LABEL;
    }

    @Override
    public String getContextActionEditLabel() {
        return EDIT_SERVER_GROUP_LABEL;
    }

    @Override
    public String getContextActionDeleteLabel() {
        return DELETE_SERVER_GROUP_LABEL;
    }

    @Override
    public String getSearchId() {
        return SEARCH_INPUT_ID;
    }
}

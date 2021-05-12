package com.oss.pages.bigdata.dfe.serverGroup;

import com.oss.framework.prompts.ConfirmationBox;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.tablewidget.OldTable;
import com.oss.framework.widgets.tabswidget.TabWindowWidget;
import com.oss.framework.widgets.tabswidget.TabsInterface;
import com.oss.pages.bigdata.dfe.BaseDfePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerGroupPage extends BaseDfePage {

    private static final Logger log = LoggerFactory.getLogger(ServerGroupPage.class);
    private static final String TABLE_ID = "server-groupAppId";
    private static final String SERVERS_TABLE_ID = "server-group/tabs/serversAppId";
    private final String SEARCH_INPUT_ID = "server-groupSearchAppId";
    private final String SERVER_GROUP_NAME_COLUMN_LABEL = "Name";
    private final String SERVER_NAME_COLUMN_LABEL = "Server Name";
    private final String ADD_NEW_SERVER_GROUP_LABEL = "Add New Server Group";
    private final String ADD_NEW_SERVER_LABEL = "Add New Server";
    private final String EDIT_SERVER_LABEL = "Edit Server";
    private final String DELETE_SERVER_LABEL = "Delete Server";
    private final String EDIT_SERVER_GROUP_LABEL = "Edit Server Group";
    private final String CONFIRM_DELETE_LABEL = "Delete";
    private final String DELETE_SERVER_GROUP_LABEL = "Delete Server Group";

    private final ServerPopupPage serverPopup;
    private final ServerGroupPopupPage serverGroupPopup;

    public ServerGroupPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        serverGroupPopup = new ServerGroupPopupPage(driver, wait);
        serverPopup = new ServerPopupPage(driver, wait);
    }

    public ServerGroupPopupPage getServerGroupPopup() {
        return serverGroupPopup;
    }

    @Step("I Open Server Group View")
    public static ServerGroupPage goToPage(WebDriver driver, String basicURL) {
        WebDriverWait wait = new WebDriverWait(driver, 45);
        BaseDfePage.openDfePage(driver, basicURL, wait, "server-group");
        return new ServerGroupPage(driver, wait);
    }

    @Step("I click add new Server Group")
    public void clickAddNewServerGroup() {
        clickContextActionAdd();
    }

    public Boolean serverGroupExistIntoTable(String serverGroupName) {
        searchFeed(serverGroupName);
        DelayUtils.waitForPageToLoad(driver, wait);
        int numberOfRowsInTable = getNumberOfRowsInTable(SERVER_GROUP_NAME_COLUMN_LABEL);
        log.trace("Fount rows count: {}. Filtered by {}", numberOfRowsInTable, serverGroupName);
        return numberOfRowsInTable == 1;
    }

    @Step("I select found Server Group")
    public void selectFoundServerGroup() {
        getTable(driver, wait).selectRow(0);
    }

    @Step("I click {label} Tab")
    public void selectTab(String label) {
        DelayUtils.waitForPageToLoad(driver, wait);
        TabsInterface tab = TabWindowWidget.create(driver, wait);
        tab.selectTabByLabel(label);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("I click Add New Server")
    public void clickAddNewServer() {
        clickTabsContextAction(ADD_NEW_SERVER_LABEL);
    }

    public String getServerName() {
        return OldTable
                .createByComponentDataAttributeName(driver, wait, SERVERS_TABLE_ID)
                .getCellValue(0, SERVER_NAME_COLUMN_LABEL);
    }

    public Boolean isServerCreated(String serverName) {
        return getServerName().equals(serverName);
    }

    public Boolean isAnyServerExist() {
        return OldTable.createByComponentDataAttributeName(driver, wait, SERVERS_TABLE_ID)
                .getNumberOfRowsInTable(SERVER_NAME_COLUMN_LABEL) >= 1;
    }

    @Step("I select server")
    public void selectServer() {
        OldTable.createByComponentDataAttributeName(driver, wait, SERVERS_TABLE_ID).selectRow(0);
    }

    @Step("I click Edit Server")
    public void clickEditServer() {
        clickTabsContextAction(EDIT_SERVER_LABEL);
    }

    @Step("I click Delete Server")
    public void clickDeleteServer() {
        clickTabsContextAction(DELETE_SERVER_LABEL);
    }

    @Step("I confirm the removal")
    public void confirmDelete() {
        ConfirmationBox confirmationBox = ConfirmationBox.create(driver, wait);
        confirmationBox.clickButtonByLabel(CONFIRM_DELETE_LABEL);
    }

    public Boolean isServerDeleted() {
        DelayUtils.waitForPageToLoad(driver, wait);
        return OldTable
                .createByComponentDataAttributeName(driver, wait, SERVERS_TABLE_ID)
                .hasNoData();
    }

    @Step("I click edit Server Group")
    public void clickEditServerGroup() {
        clickContextActionEdit();
    }

    public ServerPopupPage ServerPopup() {
        return serverPopup;
    }

    @Step("I click delete Server Group")
    public void clickDeleteServerGroup() {
        clickContextActionDelete();
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

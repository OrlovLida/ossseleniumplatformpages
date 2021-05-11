package com.oss.pages.bigdata.dfe.serverGroup;

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
    private final String NAME_COLUMN_LABEL = "Name";
    private final String SERVER_NAME_COLUMN_LABEL = "Server Name";
    private final String ADD_NEW_SERVER_GROUP_LABEL = "Add New Server Group";
    private final String EDIT_SERVER_LABEL = "Edit Server";
    private final String EDIT_SERVER_GROUP_LABEL = "Edit Server Group";
    private final String ADD_NEW_SERVER_LABEL = "Add New Server";
    private final AddNewServerPopupPage addNewServerPopup;


    public ServerGroupPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        serverGroupPopup = new ServerGroupPopupPage(driver, wait);
        addNewServerPopup = new AddNewServerPopupPage(driver, wait);
    }

    private final ServerGroupPopupPage serverGroupPopup;
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
        int numberOfRowsInTable = getNumberOfRowsInTable(NAME_COLUMN_LABEL);
        log.trace("Fount rows count: {}. Filtered by {}", numberOfRowsInTable, serverGroupName);
        return numberOfRowsInTable == 1;
    }

    @Step("I click edit Server Group")
    public void clickEditServerGroup() {
        clickContextActionEdit();
    }

    @Step("I select found Server Group")
    public void selectFoundServerGroup() {
        getTable(driver, wait).selectRow(0);
    }

    @Step("I click Add New Server")
    public void clickAddNewServer() {
        clickContextActionNew(ADD_NEW_SERVER_LABEL);
    }

    @Step("I select server")
    public void selectServer() {
        OldTable.createByComponentDataAttributeName(driver, wait, SERVERS_TABLE_ID).selectRow(0);
    }

    public String getServerName() {
        return OldTable.createByComponentDataAttributeName(driver, wait, SERVERS_TABLE_ID).getCellValue(0,"Server Name");
    }

    private TabsInterface getActionsInterface() {
        return TabWindowWidget.create(driver, wait);
    }

    protected void clickContextActionNew(String actionLabel) {
        getActionsInterface().callActionByLabel(actionLabel);
        log.debug("Clicking context action: {}", actionLabel);
    }

    public AddNewServerPopupPage getAddNewServerPopup() {
        return addNewServerPopup;
    }

    @Step("I click delete Server Group")
    public void clickDeleteServerGroup() {
        clickContextActionDelete();
    }

    @Step("I click {label} Tab")
    public void selectTab(String label) {
        DelayUtils.waitForPageToLoad(driver, wait);
        TabsInterface tab = TabWindowWidget.create(driver, wait);
        tab.selectTabByLabel(label);
        DelayUtils.waitForPageToLoad(driver, wait);
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
        return null;
    }

    @Override
    public String getSearchId() {
        return SEARCH_INPUT_ID;
    }
}

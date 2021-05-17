package com.oss.pages.bigdata.dfe.serverGroup;


import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.tablewidget.OldTable;
import com.oss.pages.bigdata.dfe.BaseDfePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServersTabPage extends BaseDfePage {

    private static final Logger log = LoggerFactory.getLogger(ServersTabPage.class);
    private final String SERVERS_TABLE_ID = "server-group/tabs/serversAppId";
    private final String ADD_NEW_SERVER_LABEL = "Add New Server";
    private final String EDIT_SERVER_LABEL = "Edit Server";
    private final String DELETE_SERVER_LABEL = "Delete Server";
    private final String SERVER_NAME_COLUMN_LABEL = "Server Name";


    public ServersTabPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public OldTable createServersTable() {
        return OldTable
                .createByComponentDataAttributeName(driver, wait, getTableId());
    }

    @Step("I select server")
    public void selectServer() {
        createServersTable().selectRow(0);
    }

    @Step("I click Add New Server")
    public void clickAddNewServer() {
        clickTabsContextAction(getContextActionAddLabel());
    }

    @Step("I check if server is created")
    public Boolean isServerCreated(String serverName) {

        return getServerName(0).equals(serverName);
    }

    @Step("Check if any server exist in servers table")
    public Boolean isAnyServerExist() {
        Boolean serverExist = createServersTable()
                .getNumberOfRowsInTable(SERVER_NAME_COLUMN_LABEL) >= 1;
        log.info("In server table exist at least one server: {}", serverExist);

        return serverExist;
    }

    @Step("I click Edit Server")
    public void clickEditServer() {
        clickTabsContextAction(getContextActionEditLabel());
    }

    @Step("I check if server with name {serverName} exists in row {rowInTheTable}")
    public String getServerName(int rowInTheTable) {
        String serverName = createServersTable()
                .getCellValue(rowInTheTable, SERVER_NAME_COLUMN_LABEL);
        log.info("Checked server name for row {}: {}", rowInTheTable, serverName);

        return serverName;
    }

    @Step("I click Delete Server")
    public void clickDeleteServer() {
        clickTabsContextAction(getContextActionDeleteLabel());
    }

    @Step("I check id server is deleted")
    public Boolean isServerDeleted() {
        DelayUtils.waitForPageToLoad(driver, wait);
        Boolean serverDeleted = createServersTable().hasNoData();
        log.info("Server is deleted: {}", serverDeleted);

        return serverDeleted;
    }

    @Override
    public String getTableId() {
        return SERVERS_TABLE_ID;
    }

    @Override
    public String getContextActionAddLabel() {
        return ADD_NEW_SERVER_LABEL;
    }

    @Override
    public String getContextActionEditLabel() {
        return EDIT_SERVER_LABEL;
    }

    @Override
    public String getContextActionDeleteLabel() {
        return DELETE_SERVER_LABEL;
    }

    @Override
    public String getSearchId() {
        return null;
    }
}

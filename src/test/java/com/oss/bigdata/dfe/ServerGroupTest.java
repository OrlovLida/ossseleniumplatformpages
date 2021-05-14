package com.oss.bigdata.dfe;

import com.oss.BaseTestCase;
import com.oss.pages.bigdata.dfe.serverGroup.ServerGroupPage;
import com.oss.utils.TestListener;
import io.qameta.allure.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

@Listeners({TestListener.class})
public class ServerGroupTest extends BaseTestCase {

    private final static Logger log = LoggerFactory.getLogger(ServerGroupTest.class);
    private final static String PROTOCOL_TYPE = "SFTP";
    private final static String SERVERS_TAB = "Servers";
    private final static String SERVERS_SERVER_NAME = "Selenium Test Server";
    private final static String SERVERS_SERVER_ADDRESS = "Selenium.Test.Address";
    private final static String SERVERS_USER_NAME = "Selenium Test Name";
    private final static String SERVERS_PASSWORD = "Password";
    private final static String SERVERS_DIRECTORY = "/test";
    private final String SERVER_GROUP_NAME_COLUMN_LABEL = "Name";
    private final String CONFIRM_DELETE_LABEL = "Delete";

    private ServerGroupPage serverGroupPage;
    private String serverGroupName;
    private String updatedServerGroupName;

    @BeforeClass
    public void goToServerGroupView() {
        serverGroupPage = ServerGroupPage.goToPage(driver, BASIC_URL);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd");
        String date = simpleDateFormat.format(new Date());
        serverGroupName = "Selenium_" + date + "_ServTest";
        updatedServerGroupName = serverGroupName + "_updated";
    }

    @Test(priority = 1, testName = "Add new server group", description = "dd new server group")
    @Description("Add new server group")
    public void addServerGroup() {
        serverGroupPage.clickAddNewServerGroup();
        serverGroupPage.getServerGroupPopup().fillServerGroupPopup(serverGroupName, PROTOCOL_TYPE);
        serverGroupPage.getServerGroupPopup().clickSave();
        Boolean serverGroupIsCreated = serverGroupPage.existIntoTable(serverGroupName, SERVER_GROUP_NAME_COLUMN_LABEL);

        Assert.assertTrue(serverGroupIsCreated);
    }

    @Test(priority = 2, testName = "Add new server", description = "Add new server")
    @Description("Add new server")
    public void addServer() {
        Boolean serverGroupExists = serverGroupPage.existIntoTable(serverGroupName, SERVER_GROUP_NAME_COLUMN_LABEL);
        if (serverGroupExists) {
            serverGroupPage.selectFoundServerGroup();
            serverGroupPage.selectTab(SERVERS_TAB);
            serverGroupPage.getServersTab().clickAddNewServer();
            serverGroupPage.getServerPopup().fillAddNewServerPopup(SERVERS_SERVER_NAME, SERVERS_SERVER_ADDRESS,
                    SERVERS_USER_NAME, SERVERS_PASSWORD, SERVERS_DIRECTORY);
            serverGroupPage.getServerPopup().clickSave();
            Boolean serverCreated = serverGroupPage.getServersTab().isServerCreated(SERVERS_SERVER_NAME);

            Assert.assertTrue(serverCreated);
        } else {
            log.error("Server group with name: {} doesn't exist, can not add server", serverGroupName);
            Assert.fail();
        }
    }

    @Test(priority = 3, testName = "Edit server", description = "Edit server", enabled = false)
    @Description("Edit server")
    public void editServer() {
        Boolean serverGroupExists = serverGroupPage.existIntoTable(serverGroupName, SERVER_GROUP_NAME_COLUMN_LABEL);
        if (serverGroupExists) {
            serverGroupPage.selectFoundServerGroup();
            serverGroupPage.selectTab(SERVERS_TAB);
            serverGroupPage.getServersTab().selectServer();
            if (serverGroupPage.getServersTab().isServerCreated(SERVERS_SERVER_NAME)) {
                serverGroupPage.getServersTab().clickEditServer();
                serverGroupPage.getServerPopup().fillServerName(updatedServerGroupName);
                serverGroupPage.getServerPopup().clickSave();
                serverGroupPage.getServersTab().selectServer();
                String editedServerName = serverGroupPage.getServersTab().getServerName(0);
                Boolean isServerNameEdited = editedServerName.equals(updatedServerGroupName);

                Assert.assertTrue(isServerNameEdited);
            } else {
                log.error("Server with name: {} doesn't exist.", SERVERS_SERVER_NAME);
                Assert.fail();
            }
        } else {
            log.error("Server group with name: {} doesn't exist.", serverGroupName);
            Assert.fail();
        }
    }

    @Test(priority = 4, testName = "Delete server", description = "Delete server")
    @Description("Delete server")
    public void deleteServer() {
        Boolean serverGroupExists = serverGroupPage.existIntoTable(serverGroupName, SERVER_GROUP_NAME_COLUMN_LABEL);
        if (serverGroupExists) {
            serverGroupPage.selectFoundServerGroup();
            serverGroupPage.selectTab(SERVERS_TAB);
            if (serverGroupPage.getServersTab().isAnyServerExist()) {
                serverGroupPage.getServersTab().selectServer();
                serverGroupPage.getServersTab().clickDeleteServer();
                serverGroupPage.confirmDelete(CONFIRM_DELETE_LABEL);
                Boolean serverDeleted = serverGroupPage.getServersTab().isServerDeleted();

                Assert.assertTrue(serverDeleted);
            } else {
                log.error("There is no existing server to delete");
                Assert.fail();
            }
        } else {
            log.error("Server with name: {} doesn't exist", SERVERS_SERVER_NAME);
            Assert.fail();
        }
    }

    @Test(priority = 5, testName = "Edit server group", description = "Edit server group", enabled = false)
    @Description("Edit server group")
    public void editServerGroup() {
        Boolean serverGroupExists = serverGroupPage.existIntoTable(serverGroupName, SERVER_GROUP_NAME_COLUMN_LABEL);
        if (serverGroupExists) {
            serverGroupPage.selectFoundServerGroup();
            serverGroupPage.clickEditServerGroup();
            serverGroupPage.getServerGroupPopup().fillName(updatedServerGroupName);
            serverGroupPage.getServerGroupPopup().clickSave();
            Boolean serverGroupIsEdited = serverGroupPage.existIntoTable(updatedServerGroupName, SERVER_GROUP_NAME_COLUMN_LABEL);

            Assert.assertTrue(serverGroupIsEdited);
        } else {
            log.error("Server group with name: {} doesn't exist", serverGroupName);
            Assert.fail();
        }
    }

    @Test(priority = 6, testName = "Delete server group", description = "Delete server group")
    @Description("Delete server group")
    public void deleteServerGroup() {
        Boolean serverGroupExists = serverGroupPage.existIntoTable(serverGroupName, SERVER_GROUP_NAME_COLUMN_LABEL);
        if (serverGroupExists) {
            serverGroupPage.selectFoundServerGroup();
            serverGroupPage.clickDeleteServerGroup();
            serverGroupPage.confirmDelete(CONFIRM_DELETE_LABEL);
            Boolean serverGroupIsDeleted = !serverGroupPage.existIntoTable(serverGroupName, SERVER_GROUP_NAME_COLUMN_LABEL);

            Assert.assertTrue(serverGroupIsDeleted);
        } else {
            log.error("Server group with name: {} doesn't exist, can not perform delete action", serverGroupName);
            Assert.fail();
        }
    }
}

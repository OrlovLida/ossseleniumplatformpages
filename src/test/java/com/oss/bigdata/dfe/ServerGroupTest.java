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

    @Test(priority = 1, testName = "Add new server group")
    @Description("Add new server group")
    public void addServerGroup() {
        serverGroupPage.clickAddNewServerGroup();
        serverGroupPage.getServerGroupPopup().fillServerGroupPopup(serverGroupName, PROTOCOL_TYPE);
        serverGroupPage.getServerGroupPopup().clickSave();
        Boolean serverGroupIsCreated = serverGroupPage.serverGroupExistIntoTable(serverGroupName);

        Assert.assertTrue(serverGroupIsCreated);
    }

    @Test(priority = 2, testName = "Add new server")
    @Description("Add new server")
    public void addServer() {
        Boolean serverGroupExists = serverGroupPage.serverGroupExistIntoTable(serverGroupName);
        if (serverGroupExists) {
            serverGroupPage.selectFoundServerGroup();
            serverGroupPage.selectTab(SERVERS_TAB);
            serverGroupPage.clickAddNewServer();
            serverGroupPage.ServerPopup().fillAddNewServerPopup(SERVERS_SERVER_NAME, SERVERS_SERVER_ADDRESS,
                    SERVERS_USER_NAME, SERVERS_PASSWORD, SERVERS_DIRECTORY);
            serverGroupPage.ServerPopup().clickSave();

            Assert.assertTrue(serverGroupPage.isServerCreated(SERVERS_SERVER_NAME));
        } else {
            log.error("Server group with name: {} doesn't exist, can not add server", serverGroupName);
            Assert.fail();
        }
    }

    @Test(priority = 3, testName = "Edit server", enabled = false)
    @Description("Edit server")
    public void editServer() {
        Boolean serverGroupExists = serverGroupPage.serverGroupExistIntoTable(serverGroupName);
        if (serverGroupExists) {
            serverGroupPage.selectFoundServerGroup();
            serverGroupPage.selectTab(SERVERS_TAB);
            serverGroupPage.selectServer();
            if (serverGroupPage.isServerCreated(SERVERS_SERVER_NAME)) {
                serverGroupPage.clickEditServer();
                serverGroupPage.ServerPopup().fillServerName(updatedServerGroupName);
                serverGroupPage.ServerPopup().clickSave();
                serverGroupPage.selectServer();
                String editedServerName = serverGroupPage.getServerName();

                Assert.assertEquals(editedServerName, updatedServerGroupName);
            } else {
                log.error("Server with name: {} doesn't exist.", SERVERS_SERVER_NAME);
                Assert.fail();
            }
        } else {
            log.error("Server group with name: {} doesn't exist.", serverGroupName);
            Assert.fail();
        }
    }

    @Test(priority = 4, testName = "Delete server")
    @Description("Delete server")
    public void deleteServer() {
        Boolean serverGroupExists = serverGroupPage.serverGroupExistIntoTable(serverGroupName);
        if (serverGroupExists) {
            serverGroupPage.selectFoundServerGroup();
            serverGroupPage.selectTab(SERVERS_TAB);
            if (serverGroupPage.isAnyServerExist()) {
                serverGroupPage.selectServer();
                serverGroupPage.clickDeleteServer();
                serverGroupPage.confirmDelete();

                Assert.assertTrue(serverGroupPage.isServerDeleted());
            } else {
                log.error("There is no existing server to delete");
                Assert.fail();
            }
        } else {
            log.error("Server with name: {} doesn't exist", SERVERS_SERVER_NAME);
            Assert.fail();
        }
    }

    @Test(priority = 5, testName = "Edite server group", enabled = false)
    @Description("Edit server group")
    public void editServerGroup() {
        Boolean serverGroupExists = serverGroupPage.serverGroupExistIntoTable(serverGroupName);
        if (serverGroupExists) {
            serverGroupPage.selectFoundServerGroup();
            serverGroupPage.clickEditServerGroup();
            serverGroupPage.getServerGroupPopup().fillName(updatedServerGroupName);
            serverGroupPage.getServerGroupPopup().clickSave();
            Boolean serverGroupIsEdited = serverGroupPage.serverGroupExistIntoTable(updatedServerGroupName);

            Assert.assertTrue(serverGroupIsEdited);
        } else {
            log.error("Server group with name: {} doesn't exist", serverGroupName);
            Assert.fail();
        }
    }

    @Test(priority = 6, testName = "Delete server group")
    @Description("Delete server group")
    public void deleteServerGroup() {
        Boolean serverGroupExists = serverGroupPage.serverGroupExistIntoTable(serverGroupName);
        if (serverGroupExists) {
            serverGroupPage.selectFoundServerGroup();
            serverGroupPage.clickDeleteServerGroup();
            serverGroupPage.confirmDelete();
            Boolean serverGroupIsDeleted = !serverGroupPage.serverGroupExistIntoTable(serverGroupName);

            Assert.assertTrue(serverGroupIsDeleted);
        } else {
            log.error("Server group with name: {} doesn't exist, can not perform delete action", serverGroupName);
            Assert.fail();
        }
    }
}

package com.oss.iaa.bigdata.dfe;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.iaa.bigdata.dfe.servergroup.ServerGroupPage;
import com.oss.pages.iaa.bigdata.dfe.servergroup.ServerGroupPopupPage;
import com.oss.pages.iaa.bigdata.dfe.servergroup.ServerPopupPage;
import com.oss.pages.iaa.bigdata.dfe.servergroup.ServersTabPage;
import com.oss.pages.iaa.bigdata.utils.ConstantsDfe;

import io.qameta.allure.Description;

public class ServerGroupTest extends BaseTestCase {

    private static final String PROTOCOL_TYPE = "SFTP";
    private static final String SERVERS_SERVER_NAME = "Selenium Test Server";
    private static final String SERVERS_SERVER_NAME_UPDATED = SERVERS_SERVER_NAME + "-updated";
    private static final String SERVERS_SERVER_ADDRESS = "Selenium.Test.Address";
    private static final String SERVERS_USER_NAME = "Selenium Test Name";
    private static final String SERVERS_PASSWORD = "Password";
    private static final String SERVERS_DIRECTORY = "/test";
    private static final String ADD_WIZARD_ID = "add-prompt-id_prompt-card";
    private static final String EDIT_WIZARD_ID = "edit-prompt-id_prompt-card";

    private ServerGroupPage serverGroupPage;
    private String serverGroupName;
    private String updatedServerGroupName;

    @BeforeClass
    public void goToServerGroupView() {
        serverGroupPage = ServerGroupPage.goToPage(driver, BASIC_URL);

        serverGroupName = ConstantsDfe.createName() + "_ServTest";
        updatedServerGroupName = serverGroupName + "_updated";
    }

    @Test(priority = 1, testName = "Add new server group", description = "Add new server group")
    @Description("Add new server group")
    public void addServerGroup() {
        serverGroupPage.clickAddNewServerGroup();
        ServerGroupPopupPage serverGroupPopupWizard = new ServerGroupPopupPage(driver, webDriverWait, ADD_WIZARD_ID);
        serverGroupPopupWizard.fillServerGroupPopup(serverGroupName, PROTOCOL_TYPE);
        serverGroupPopupWizard.clickSave();
        boolean serverGroupIsCreated = serverGroupPage.serverGroupExistIntoTable(serverGroupName);

        Assert.assertTrue(serverGroupIsCreated);
    }

    @Test(priority = 2, testName = "Add new server", description = "Add new server")
    @Description("Add new server")
    public void addServer() {
        boolean serverGroupExists = serverGroupPage.serverGroupExistIntoTable(serverGroupName);
        if (serverGroupExists) {
            serverGroupPage.selectFoundServerGroup();
            serverGroupPage.selectServersTab();
            ServersTabPage serversTabPage = new ServersTabPage(driver, webDriverWait);
            serversTabPage.clickAddNewServer();
            ServerPopupPage serverPopupWizard = new ServerPopupPage(driver, webDriverWait, ADD_WIZARD_ID);
            serverPopupWizard.fillAddNewServerPopup(SERVERS_SERVER_NAME, SERVERS_SERVER_ADDRESS,
                    SERVERS_USER_NAME, SERVERS_PASSWORD, SERVERS_DIRECTORY);
            serverPopupWizard.clickSave();
            boolean serverCreated = serversTabPage.isServerCreated(SERVERS_SERVER_NAME);

            Assert.assertTrue(serverCreated);
        } else {
            Assert.fail(String.format("Server group with name: %s doesn't exist, cannot add server", serverGroupName));
        }
    }

    @Test(priority = 3, testName = "Edit server", description = "Edit server")
    @Description("Edit server")
    public void editServer() {
        boolean serverGroupExists = serverGroupPage.serverGroupExistIntoTable(serverGroupName);
        if (serverGroupExists) {
            serverGroupPage.selectFoundServerGroup();
            serverGroupPage.selectServersTab();
            ServersTabPage serversTabPage = new ServersTabPage(driver, webDriverWait);
            serversTabPage.selectServer();
            if (serversTabPage.isServerCreated(SERVERS_SERVER_NAME)) {
                serversTabPage.clickEditServer();
                ServerPopupPage serverPopupWizard = new ServerPopupPage(driver, webDriverWait, EDIT_WIZARD_ID);
                serverPopupWizard.fillServerName(SERVERS_SERVER_NAME_UPDATED);
                serverPopupWizard.clickSave();
                serversTabPage.selectServer();
                String editedServerName = serversTabPage.getServerName(0);
                boolean isServerNameEdited = editedServerName.equals(SERVERS_SERVER_NAME_UPDATED);

                Assert.assertTrue(isServerNameEdited);
            } else {
                Assert.fail(String.format("Server with name: %s doesn't exist.", SERVERS_SERVER_NAME));
            }
        } else {
            Assert.fail(String.format("Server group with name: %s doesn't exist.", serverGroupName));
        }
    }

    @Test(priority = 4, testName = "Delete server", description = "Delete server")
    @Description("Delete server")
    public void deleteServer() {
        boolean serverGroupExists = serverGroupPage.serverGroupExistIntoTable(serverGroupName);
        if (serverGroupExists) {
            serverGroupPage.selectFoundServerGroup();
            serverGroupPage.selectServersTab();
            ServersTabPage serversTabPage = new ServersTabPage(driver, webDriverWait);
            if (serversTabPage.isAnyServerExist()) {
                serversTabPage.selectServer();
                serversTabPage.clickDeleteServer();
                serverGroupPage.clickConfirmDelete();
                boolean serverDeleted = serversTabPage.isServerDeleted();

                Assert.assertTrue(serverDeleted);
            } else {
                Assert.fail("There is no existing server to delete");
            }
        } else {
            Assert.fail(String.format("Server with name: %s doesn't exist", SERVERS_SERVER_NAME));
        }
    }

    @Test(priority = 5, testName = "Edit server group", description = "Edit server group")
    @Description("Edit server group")
    public void editServerGroup() {
        boolean serverGroupExists = serverGroupPage.serverGroupExistIntoTable(serverGroupName);
        if (serverGroupExists) {
            serverGroupPage.selectFoundServerGroup();
            serverGroupPage.clickEditServerGroup();
            ServerGroupPopupPage serverGroupPopupWizard = new ServerGroupPopupPage(driver, webDriverWait, EDIT_WIZARD_ID);
            serverGroupPopupWizard.fillName(updatedServerGroupName);
            serverGroupPopupWizard.clickSave();
            boolean serverGroupIsEdited = serverGroupPage.serverGroupExistIntoTable(updatedServerGroupName);

            Assert.assertTrue(serverGroupIsEdited);
        } else {
            Assert.fail(String.format("Server group with name: %s doesn't exist", serverGroupName));
        }
    }

    @Test(priority = 6, testName = "Delete server group", description = "Delete server group")
    @Description("Delete server group")
    public void deleteServerGroup() {
        boolean serverGroupExists = serverGroupPage.serverGroupExistIntoTable(serverGroupName);
        if (serverGroupExists) {
            serverGroupPage.selectFoundServerGroup();
            serverGroupPage.clickDeleteServerGroup();
            serverGroupPage.clickConfirmDelete();
            boolean serverGroupIsDeleted = !serverGroupPage.serverGroupExistIntoTable(serverGroupName);

            Assert.assertTrue(serverGroupIsDeleted);
        } else {
            Assert.fail(String.format("Server group with name: %s doesn't exist, cannot perform delete action", serverGroupName));
        }
    }
}

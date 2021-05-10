package com.oss.bigdata.dfe;

import com.oss.BaseTestCase;
import com.oss.pages.bigdata.dfe.serverGroup.ServerGroupPage;
import io.qameta.allure.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ServerGroupTest extends BaseTestCase {

    private ServerGroupPage serverGroupPage;
    private String serverGroupName;
    private String updatedServerGroupName;
    private final static Logger log = LoggerFactory.getLogger(ServerGroupTest.class);
    private final static String PROTOCOL_TYPE = "SFTP";
    private final static String SERVERS_TAB = "Servers";
    private final static String SERVERS_SERVER_NAME = "Selenium Test Server";
    private final static String SERVERS_SERVER_ADDRESS = "Selenium Test Address";
    private final static String SERVERS_USER_NAME = "Selenium Test Name";
    private final static String SERVERS_PASSWORD = "Password";
    private final static String SERVERS_DIRECTORY = "Selenium Test";

    @BeforeClass
    public void goToServerGroupView() {
       serverGroupPage = ServerGroupPage.goToPage(driver, BASIC_URL);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd");
        String date = simpleDateFormat.format(new Date());
        serverGroupName = "Selenium_" + date + "_ServTest";
        updatedServerGroupName = serverGroupName + "_updated";
    }

    @Test
    @Description("Add new server group")
    public void addServerGroup() {
        serverGroupPage.clickAddNewServerGroup();
        serverGroupPage.getServerGroupPopup().fillServerGroupPopup(serverGroupName, PROTOCOL_TYPE);
        serverGroupPage.getServerGroupPopup().clickSave();
        Boolean serverGroupIsCreated = serverGroupPage.serverGroupExistIntoTable(serverGroupName);

        Assert.assertTrue(serverGroupIsCreated);
    }

    @Test
    @Description("Add new server")
    public void addServer() {
        Boolean serverGroupExists = serverGroupPage.serverGroupExistIntoTable(serverGroupName);
        if (serverGroupExists) {
            serverGroupPage.selectFoundServerGroup();
            serverGroupPage.selectTab(SERVERS_TAB);
            serverGroupPage.clickAddNewServer();
            serverGroupPage.getAddNewServerPopup().fillAddNewServerPopup(SERVERS_SERVER_NAME, SERVERS_SERVER_ADDRESS,
                    SERVERS_USER_NAME, SERVERS_PASSWORD, SERVERS_DIRECTORY);
            serverGroupPage.getAddNewServerPopup().clickSave();
            Boolean isServerAdded = serverGroupPage.ServerExistsIntoTable(SERVERS_SERVER_NAME);

            Assert.assertTrue(isServerAdded);
        } else {
            log.error("Server group with name: {} doesn't exist, an not add server", serverGroupName);
            Assert.fail();
        }
    }

    @Test
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
}

package com.oss.bigdata.dfe;

import com.oss.BaseTestCase;
import com.oss.pages.bigdata.dfe.serverGroup.ServerGroupPage;
import io.qameta.allure.Description;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ServerGroupViewTest extends BaseTestCase {

    private ServerGroupPage serverGroupPage;
    private String serverGroupName;

    private final static String PROTOCOL_TYPE = "SFTP";

    @BeforeClass
    public void goToServerGroupView() {
       serverGroupPage = ServerGroupPage.goToPage(driver, BASIC_URL);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd");
        String date = simpleDateFormat.format(new Date());
        serverGroupName = "Selenium_" + date + "_ServTest";
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

}

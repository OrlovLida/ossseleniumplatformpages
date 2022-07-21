package com.oss.bigdata.dfe.regressiontests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.bigdata.dfe.servergroup.ServerGroupPage;

import io.qameta.allure.Description;

public class ServerGroupRegressionTests extends BaseTestCase {

    private ServerGroupPage serverGroupPage;

    @BeforeMethod
    public void goToServerGroupsView() {
        serverGroupPage = ServerGroupPage.goToPage(driver, BASIC_URL);
    }

    @Test(priority = 1, testName = "Check if Server Group View is opened", description = "Check if Server Group View is opened")
    @Description("Check if Server Group View is opened")
    public void checkServerGroupsView() {
        Assert.assertEquals(serverGroupPage.getViewTitle(), "Server Groups");
    }

    @Parameters({"serverGroupName"})
    @Test(priority = 2, testName = "Check Server Group Details Tab", description = "Check Server Group Details Tab")
    @Description("Check Server Group Details Tab")
    public void checkServerGroupDetailsTab(
            @Optional("t:GENERAL#Test Server Group") String serverGroupName
    ) {
        boolean serverGroupExists = serverGroupPage.serverGroupExistIntoTable(serverGroupName);
        if (serverGroupExists) {
            serverGroupPage.selectFoundServerGroup();
            serverGroupPage.selectDetailsTab();
            Assert.assertEquals(serverGroupPage.checkNameInPropertyPanel(), serverGroupName);
        } else {
            Assert.fail("Cannot find Server Group " + serverGroupName);
        }
    }

    @Parameters({"categoryName"})
    @Test(priority = 3, testName = "Check Categories", description = "Check Categories")
    @Description("Check Categories")
    public void checkCategories(
            @Optional("DFE General") String categoryName
    ) {
        serverGroupPage.searchCategories(categoryName);
        Assert.assertEquals(serverGroupPage.getCategoryName(0), categoryName);
    }
}
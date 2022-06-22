package com.oss.reconciliation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.navigation.sidemenu.SideMenu;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.platform.HomePage;

import io.qameta.allure.Description;

public class RecoViews_Test extends BaseTestCase {

    private static final Logger LOGGER = LoggerFactory.getLogger(SideMenu.class);

    @BeforeClass
    public void openNetworkDiscoveryControlView() {
        waitForPageToLoad();
    }

    @Test(dataProvider = "data", priority = 1, description = "Checking views from Network Discovery and Reconciliation")
    @Description("Checking views from Network Discovery and Reconciliation")
    public void checkRecoViews(String viewName, String path) {
        String groupName = "Network Discovery and Reconciliation";
        HomePage homePage = new HomePage(driver);
        if (path == null || path.isEmpty()) {
            LOGGER.debug("Checking " + viewName);
            homePage.chooseFromLeftSideMenu(viewName, groupName);
            waitForPageToLoad();
        } else {
            LOGGER.debug("Checking " + viewName + " in " + path);
            homePage.chooseFromLeftSideMenu(viewName, groupName, path);
            waitForPageToLoad();
        }
    }

    @DataProvider(name = "data")
    private Object[][] getData() {
        return new Object[][]{
                {"Network Discovery Control", ""},
                {"Network Inconsistencies View", ""},
                {"VS Viewer", ""},
                {"TO Viewer", ""},
                {"VS Viewer 2.0", ""},
                {"Type Specific VS Viewer", ""},
                {"Metamodel Editor", ""},
                {"Parameter Audit Control", "Parameters Audit"},
                {"Validation Rules Manager", "Parameters Audit"}
        };
    }

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }
}
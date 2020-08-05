package com.oss.E2E;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.sidemenu.SideMenu;
import com.oss.pages.physical.DeviceOverviewPage;
import com.oss.pages.physical.DeviceWizardPage;
import com.oss.pages.transport.NetworkViewPage;

public class InstalationNewRouter extends BaseTestCase {

    private String deviceModel = "1941";

    @BeforeClass
    public void openNetworkView() {
        SideMenu sideMenu = new SideMenu(driver, webDriverWait);
        sideMenu.goToTechnologyByLeftSideMenu("Bookmarks", "SeleniumTests", "LAB Network View");
    }

    @Test(priority = 1)
    public void selectLocation() {
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        networkViewPage.expandDockedPanel("left");
        networkViewPage.selectObjectInViewContent("Name", "Poznan-BU1");
    }

    @Test(priority = 2)
    public void createPhysicalDevice() {
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        networkViewPage.useContextAction("CREATE", "Create Device");
        DeviceWizardPage wizard = new DeviceWizardPage(driver);
        wizard.setModel(deviceModel);
        DelayUtils.sleep(1000);
        wizard.setName("H3_Lab");
        wizard.create();
        networkViewPage.checkSystemMessage();
    }

    @Test(priority = 3)
    public void moveToDeviceOverwiev() {
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        networkViewPage.useContextAction("NAVIGATION", "Device Overview");
    }

    @Test(priority = 4)
    public void selectPort() {
        DeviceOverviewPage deviceOverviewPage = new DeviceOverviewPage(driver);
        deviceOverviewPage.selectTreeRow("GE 1");
    }

    @Test(priority = 5)
    public void moveToInventoryView() {
        DeviceOverviewPage deviceOverviewPage = new DeviceOverviewPage(driver);
        deviceOverviewPage.useContextAction("Inventory View");
    }
}
package com.oss.E2E;

import com.oss.pages.physical.DeviceOverviewPage;
import com.oss.pages.platform.OldInventoryViewPage;
import com.oss.pages.transport.IPv4AddressAssignmentWizardPage;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.sidemenu.SideMenu;
import com.oss.pages.physical.DeviceWizardPage;
import com.oss.pages.transport.NetworkViewPage;

public class InstalationNewRouter extends BaseTestCase {

    private String deviceModel = "1941";

    @BeforeClass
    public void openNetworkView() {
        DelayUtils.sleep(10000);
        SideMenu sideMenu = new SideMenu(driver, webDriverWait);
        sideMenu.goToTechnologyByLeftSideMenu("Bookmarks", "SeleniumTests", "LAB Network View");
    }

    @Test(priority = 1)
    public void selectLocation() {
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        DelayUtils.sleep(500);
        networkViewPage.expandDockedPanel("left");
        DelayUtils.sleep(500);
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
        DelayUtils.sleep(500);
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        DelayUtils.sleep(1000);
        networkViewPage.useContextAction("NAVIGATION", "Device Overview");
    }

    @Test(priority = 4)
    public void selectEthernetInterface() {
        DeviceOverviewPage deviceOverviewPage = new DeviceOverviewPage(driver);
        deviceOverviewPage.expandTreeRow(1,"GE 1");
        deviceOverviewPage.selectTreeRow("GE 1", 1, "GE 1");
        deviceOverviewPage.useContextAction("Inventory View");
    }

    @Test(priority = 5)
    public void moveToInventoryView() {
        OldInventoryViewPage oldInventoryViewPage = new OldInventoryViewPage(driver);
        oldInventoryViewPage.selectRow("Name", "GE 1");
        oldInventoryViewPage.useContextAction("CREATE", "AssignIPv4Host");
    }

    @Test (priority = 6)
    public void assignIPv4Address(){
        IPv4AddressAssignmentWizardPage iPv4AddressAssignmentWizardPage = new IPv4AddressAssignmentWizardPage(driver);
        iPv4AddressAssignmentWizardPage.assignIPAddressMainStep("10.10.20.11", "10.10.20.0/24 [E2ESeleniumTest]", "false");
        iPv4AddressAssignmentWizardPage.assignIPAddressSummaryStep();
    }
}
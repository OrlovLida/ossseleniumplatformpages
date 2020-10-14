package com.oss.E2E;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.sidemenu.SideMenu;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.physical.DeviceOverviewPage;
import com.oss.pages.platform.HomePage;
import com.oss.pages.platform.OldInventoryViewPage;
import com.oss.pages.reconciliation.CmDomainWizardPage;
import com.oss.pages.reconciliation.NetworkDiscoveryControlViewPage;
import com.oss.pages.reconciliation.NetworkInconsistenciesViewPage;
import com.oss.pages.reconciliation.SamplesManagementPage;
import com.oss.pages.transport.IPv4AddressAssignmentWizardPage;
import com.oss.pages.transport.NetworkViewPage;

import static com.oss.framework.components.inputs.Input.ComponentType.TEXT_FIELD;

public class InstalationNewRouter extends BaseTestCase {

    private NetworkDiscoveryControlViewPage networkDiscoveryControlViewPage;

    private String deviceModel = "1941";
    private String cmDomainName = "SeleniumE2ETest";
    private String deviceName = "H3_Lab";
    private String portName = "GE 1";
    private String trailName = "SeleniumTest IP Link";
    private String interfaceName = "CISCO IOS 12/15/XE without mediation";

    @BeforeClass
    public void openNetworkView() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        SideMenu sideMenu = SideMenu.create(driver, webDriverWait);
        sideMenu.callActionByLabel("LAB Network View","Favourites", "SeleniumTests");
    }

    @Test(priority = 1)
    public void selectLocation() {
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.expandDockedPanel("left");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.selectObjectInViewContent("Name", "Poznan-BU1");
    }

    @Test(priority = 2)
    public void createPhysicalDevice() {
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        networkViewPage.useContextAction("CREATE", "Create Device");
        networkViewPage.setModel(deviceModel);
        DelayUtils.sleep(1000);
        networkViewPage.setName(deviceName);
        networkViewPage.setHostname(deviceName);
        DelayUtils.sleep(1000);
        networkViewPage.create();
        networkViewPage.checkSystemMessage();
    }

    @Test(priority = 3)
    public void moveToDeviceOverwiev() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        networkViewPage.expandDockedPanel("left");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.selectObjectInViewContent("Name", deviceName);
        DelayUtils.sleep(15000); //naming musi sie przeliczyc, nie widac progresu w konsolce
        networkViewPage.selectObjectInViewContent("Name", deviceName);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.useContextAction("NAVIGATION", "Device Overview");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 4)
    public void selectEthernetInterface() {
        DeviceOverviewPage deviceOverviewPage = new DeviceOverviewPage(driver);
        deviceOverviewPage.expandTreeRow(1, portName);
        deviceOverviewPage.selectTreeRow(portName, 1, portName);
        DelayUtils.sleep(3000);
        deviceOverviewPage.useContextAction("Inventory View");
    }

    @Test(priority = 5)
    public void moveToInventoryView() {
        OldInventoryViewPage oldInventoryViewPage = new OldInventoryViewPage(driver);
        oldInventoryViewPage.selectRow("Object Type", "Ethernet Interface");
        oldInventoryViewPage.useContextAction("CREATE", "AssignIPv4Host");
    }

    @Test(priority = 6)
    public void assignIPv4Address() {
        IPv4AddressAssignmentWizardPage iPv4AddressAssignmentWizardPage = new IPv4AddressAssignmentWizardPage(driver);
        iPv4AddressAssignmentWizardPage.assignIPAddressMainStep("10.10.20.11", "10.10.20.0/24 [E2ESeleniumTest]", "false");
        iPv4AddressAssignmentWizardPage.assignIPAddressSummaryStep();
    }

    @Test(priority = 7)
    public void createIpLink() {
        openNetworkView();
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        networkViewPage.useContextAction("add_to_view_group", "Network Element");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.queryElementAndAddItToView("name", TEXT_FIELD, deviceName);
        networkViewPage.expandDockedPanel("left");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.selectObjectInViewContent("Name", "H1");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.useContextAction("CREATE", "Create Trail");
        networkViewPage.selectTrailType("IP Link");
        networkViewPage.acceptTrailType();
        networkViewPage.setTrailName(trailName);
        networkViewPage.proceedTrailCreation();
    }

    @Test(priority = 8)
    public void preciseIpLinkTermination() {
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        networkViewPage.expandDockedPanel("bottom");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.selectObjectInDetailsTab("Type", "Start");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.modifyTermination();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.setTrailPort(portName);
        networkViewPage.clickProceed();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.selectObjectInDetailsTab("Type", "End");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.modifyTermination();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.setTrailPort(portName);
        networkViewPage.clickProceed();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 9)
    public void createCmDomain() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkDiscoveryControlViewPage = NetworkDiscoveryControlViewPage.goToNetworkDiscoveryControlViewPage(driver, BASIC_URL);
        networkDiscoveryControlViewPage.openCmDomainWizard();
        CmDomainWizardPage wizard = new CmDomainWizardPage(driver);
        wizard.setName(cmDomainName);
        wizard.setInterface(interfaceName);
        wizard.setDomain("IP");
        wizard.save();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 10)
    public void uploadSamples() {
        DelayUtils.sleep(1000);
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(cmDomainName);
        networkDiscoveryControlViewPage.moveToSamplesManagement();
        SamplesManagementPage samplesManagementPage = new SamplesManagementPage(driver);
        samplesManagementPage.selectPath();
        samplesManagementPage.createDirectory(cmDomainName);
        DelayUtils.sleep(1000);
        samplesManagementPage.uploadSamples("recoSamples/ciscoE2E/H3_Lab_100.100.100.100_20181016_1500_sh_inventory_raw.cli");
        DelayUtils.sleep(1000);
        samplesManagementPage.uploadSamples("recoSamples/ciscoE2E/H3_Lab_100.100.100.100_20181016_1500_sh_ip_interface_brief.cli");
        DelayUtils.sleep(1000);
        samplesManagementPage.uploadSamples("recoSamples/ciscoE2E/H3_Lab_100.100.100.100_20181016_1500_sh_lldp_neighbors_detail.cli");
        DelayUtils.sleep(1000);
        samplesManagementPage.uploadSamples("recoSamples/ciscoE2E/H3_Lab_100.100.100.100_20181016_1500_sh_running_config.cli");
        DelayUtils.sleep(1000);
        samplesManagementPage.uploadSamples("recoSamples/ciscoE2E/H3_Lab_100.100.100.100_20181016_1500_sh_version.cli");
    }

    @Test(priority = 11)
    public void runReconciliationWithFullSample() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        NetworkDiscoveryControlViewPage networkDiscoveryControlViewPage = NetworkDiscoveryControlViewPage.goToNetworkDiscoveryControlViewPage(driver, BASIC_URL);
        DelayUtils.sleep(100);
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(cmDomainName);
        networkDiscoveryControlViewPage.runReconciliation();
        networkDiscoveryControlViewPage.checkReconciliationStartedSystemMessage();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkDiscoveryControlViewPage.waitForEndOfReco();
    }

    @Test(priority = 12)
    public void applyInconsistencies() {
        networkDiscoveryControlViewPage.moveToNivFromNdcv();
        NetworkInconsistenciesViewPage networkInconsistenciesViewPage = new NetworkInconsistenciesViewPage(driver);
        networkInconsistenciesViewPage.expantTree();
        networkInconsistenciesViewPage.clearOldNotification();
        networkInconsistenciesViewPage.applyInconsistencies();
        DelayUtils.sleep(500);
        networkInconsistenciesViewPage.checkNotificationAfterApplyInconsistencies(deviceName);
    }

    @Test(priority = 13)
    public void deleteCmDomain() {
        networkDiscoveryControlViewPage = NetworkDiscoveryControlViewPage.goToNetworkDiscoveryControlViewPage(driver, BASIC_URL);
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(cmDomainName);
        networkDiscoveryControlViewPage.clearOldNotifications();
        networkDiscoveryControlViewPage.deleteCmDomain();
        networkDiscoveryControlViewPage.checkDeleteCmDomainSystemMessage();
        networkDiscoveryControlViewPage.checkDeleteCmDomainNotification(cmDomainName);
    }

    @Test(priority = 14)
    public void deleteIpLink() {
        HomePage.goToHomePage(driver, BASIC_URL);
        openNetworkView();
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        networkViewPage.useContextAction("add_to_view_group", "Trail");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.queryElementAndAddItToView("name", TEXT_FIELD, trailName);
        networkViewPage.useContextAction("EDIT", "Delete Trail");
        networkViewPage.clickConfirmationBoxButtonByLabel("Proceed");
    }

    @Test(priority = 15)
    public void deletePhysicalDevice() {
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        networkViewPage.expandDockedPanel("left");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.selectObjectInViewContent("Name", deviceName);
        networkViewPage.useContextAction("EDIT", "Delete Element");
        networkViewPage.clickConfirmationBoxButtonByLabel("Yes");
        networkViewPage.checkSystemMessage();
    }
}
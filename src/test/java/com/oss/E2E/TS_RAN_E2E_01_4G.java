package com.oss.E2E;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.alerts.SystemMessageContainer;
import com.oss.framework.alerts.SystemMessageContainer.Message;
import com.oss.framework.alerts.SystemMessageInterface;
import com.oss.framework.mainheader.PerspectiveChooser;
import com.oss.framework.sidemenu.SideMenu;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.platform.HomePage;
import com.oss.pages.platform.NewInventoryViewPage;
import com.oss.pages.radio.AntennaArrayWizardPage;
import com.oss.pages.radio.CableWizardPage;
import com.oss.pages.radio.CellSiteConfigurationPage;
import com.oss.pages.radio.ConnectionWizardPage;
import com.oss.pages.radio.HostingWizardPage;
import com.oss.pages.radio.RanAntennaWizardPage;
import com.oss.pages.reconciliation.CmDomainWizardPage;
import com.oss.pages.reconciliation.NetworkDiscoveryControlViewPage;
import com.oss.pages.reconciliation.NetworkDiscoveryControlViewPage.IssueLevel;
import com.oss.pages.reconciliation.NetworkInconsistenciesViewPage;
import com.oss.pages.reconciliation.SamplesManagementPage;
import com.oss.utils.TestListener;

@Listeners({ TestListener.class })
public class TS_RAN_E2E_01_4G extends BaseTestCase {

    private NetworkDiscoveryControlViewPage networkDiscoveryControlViewPage;
    private CellSiteConfigurationPage cellSiteConfigurationPage;
    private NewInventoryViewPage newInventoryViewPage;
    private static final String LOCATION_NAME = "Poznan-BU1";
    private static final String ANTENNA_NAME = "TS-RAN-E2E-01-4G-ANTENNA";
    private static final String ANTENNA_TRAIL_NAME = "TS-RAN-E2E-01-4G-ANTENNA-TRAIL";
    private static final String CABLE_NAME = "TS-RAN-E2E-01-4G-CABLE";
    private static final String CPRI_NAME = "TS-RAN-E2E-01-4G-CPRI";
    private static final String ENODEB_NAME = "DXBL0858UAT6-Burj-Dubai_The-Palace-Hotel_West-Wing";
    private static final String BBU_NAME = "eNodeB,DXBL0858UAT6-Burj-Dubai_The-Palace-Hotel_West-Wing/0/BBU3900,0";
    private static final String RRU_NAME = "eNodeB,DXBL0858UAT6-Burj-Dubai_The-Palace-Hotel_West-Wing/0/MRRU,60";
    private static final String CELL4G_NAME = "DXBL0858UAT61";
    private static final String RAN_ANTENNA_MODEL = "Generic 3-Array Antenna";
    private static final String cmDomainName = "Selenium-TS-RAN-E2E-01-4G";
    private static final String cmInterface = "Huawei U2000 RAN";
    private static final String[] inconsistenciesNames = {
            "PhysicalElement-" + BBU_NAME,
            "PhysicalElement-" + RRU_NAME };
    private static final String inconsistenciesRanName = "ENODEB-" + ENODEB_NAME;


    @BeforeClass
    public void openNetworkDiscoveryControlView() {
        networkDiscoveryControlViewPage = NetworkDiscoveryControlViewPage.goToNetworkDiscoveryControlViewPage(driver, BASIC_URL);
        cellSiteConfigurationPage = new CellSiteConfigurationPage(driver);
        newInventoryViewPage = new NewInventoryViewPage(driver,webDriverWait);
        PerspectiveChooser.create(driver, webDriverWait).setNetworkPerspective();
    }

    @Test(priority = 1)
    public void createCmDomain() {
        networkDiscoveryControlViewPage.openCmDomainWizard();
        CmDomainWizardPage wizard = new CmDomainWizardPage(driver);
        wizard.setName(cmDomainName);
        wizard.setInterface(cmInterface);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        wizard.setDomain("RAN");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        wizard.save();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 2)
    public void uploadSamples() {
        DelayUtils.sleep(1000);
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(cmDomainName);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkDiscoveryControlViewPage.moveToSamplesManagement();
        SamplesManagementPage samplesManagementPage = new SamplesManagementPage(driver);
        samplesManagementPage.selectPath();
        samplesManagementPage.createDirectory(cmDomainName);
        DelayUtils.sleep(1000);
        samplesManagementPage.uploadSamples("recoSamples/huaweiRan/TS_RAN_E2E_01_4G/AIM_NodeB_Inventory_DXBL0858UAT6-Burj-Dubai_The-Palace-Hotel_West-Wing_20200414_151916.xml");
        DelayUtils.sleep(1000);
        samplesManagementPage.uploadSamples("recoSamples/huaweiRan/TS_RAN_E2E_01_4G/SRANNBIExport_XML_DXBL0858UAT6-Burj-Dubai_The-Palace-Hotel_West-Wing_4G.xml");
    }

    @Test(priority = 3)
    public void runReconciliationWithFullSample() {
        openNetworkDiscoveryControlView();
        DelayUtils.sleep(100);
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(cmDomainName);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkDiscoveryControlViewPage.runReconciliation();
        networkDiscoveryControlViewPage.checkReconciliationStartedSystemMessage();
        networkDiscoveryControlViewPage.waitForEndOfReco();
        networkDiscoveryControlViewPage.selectLatestReconciliationState();
        Assert.assertTrue(networkDiscoveryControlViewPage.checkIssues(IssueLevel.STARTUP_FATAL));
        Assert.assertTrue(networkDiscoveryControlViewPage.checkIssues(IssueLevel.FATAL));
        Assert.assertTrue(networkDiscoveryControlViewPage.checkIssues(IssueLevel.ERROR));
        Assert.assertTrue(networkDiscoveryControlViewPage.checkIssues(IssueLevel.WARNING));
    }

    @Test(priority = 4)
    public void assignLocationAndApplyInconsistencies() {
        networkDiscoveryControlViewPage.moveToNivFromNdcv();
        NetworkInconsistenciesViewPage networkInconsistenciesViewPage = new NetworkInconsistenciesViewPage(driver);
        networkInconsistenciesViewPage.expandTree();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        for (String inconsistencieName : inconsistenciesNames) {
            networkInconsistenciesViewPage.assignLocation(inconsistencieName, LOCATION_NAME);
            networkInconsistenciesViewPage.checkUpdateDeviceSystemMessage();
            networkInconsistenciesViewPage.clearOldNotification();
            networkInconsistenciesViewPage.applySelectedInconsistencies();
            DelayUtils.sleep(5000);
            networkInconsistenciesViewPage.checkNotificationAfterApplyInconsistencies(inconsistencieName);
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
        }
        networkInconsistenciesViewPage.assignRanLocation(inconsistenciesRanName, LOCATION_NAME);
        networkInconsistenciesViewPage.checkUpdateDeviceSystemMessage();
        networkInconsistenciesViewPage.clearOldNotification();
        networkInconsistenciesViewPage.applySelectedInconsistencies();
        DelayUtils.sleep(5000);
        networkInconsistenciesViewPage.checkNotificationAfterApplyInconsistencies(inconsistenciesRanName);
    }

    @Test(priority = 5)
    public void deleteCmDomain() {
        openNetworkDiscoveryControlView();
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(cmDomainName);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkDiscoveryControlViewPage.clearOldNotifications();
        networkDiscoveryControlViewPage.deleteCmDomain();
        networkDiscoveryControlViewPage.checkDeleteCmDomainSystemMessage();
        networkDiscoveryControlViewPage.checkDeleteCmDomainNotification(cmDomainName);
        PerspectiveChooser.create(driver, webDriverWait).setLivePerspective();
    }

    @Test(priority = 6)
    public void createRanAntenna() {
        openCellSiteConfigurationView();
        cellSiteConfigurationPage.expandTreeToLocation("Site", LOCATION_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cellSiteConfigurationPage.selectTab("Devices");
        cellSiteConfigurationPage.clickPlusIconAndSelectOption("Create RAN Antenna");
        RanAntennaWizardPage ranAntennaWizardPage = new RanAntennaWizardPage(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        ranAntennaWizardPage.setName(ANTENNA_NAME);
        ranAntennaWizardPage.setModel(RAN_ANTENNA_MODEL);
        DelayUtils.sleep(1000);
        ranAntennaWizardPage.setPreciseLocation(LOCATION_NAME);
        ranAntennaWizardPage.clickAccept();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        AntennaArrayWizardPage antennaArrayWizardPage = new AntennaArrayWizardPage(driver);
        antennaArrayWizardPage.clickAccept();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        checkPopup();
    }

    @Test(priority = 7)
    public void createHostRelation() {
        cellSiteConfigurationPage.getTree().expandTreeRow(LOCATION_NAME);
        cellSiteConfigurationPage.getTree().expandTreeRow("Base Stations");
        cellSiteConfigurationPage.getTree().expandTreeRow(ENODEB_NAME);
        cellSiteConfigurationPage.selectTreeRow(CELL4G_NAME);
        cellSiteConfigurationPage.selectTab("Hosting");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cellSiteConfigurationPage.clickPlusIconAndSelectOption("Host on Antenna Array");
        HostingWizardPage wizard = new HostingWizardPage(driver);
        wizard.setHostingContains(ANTENNA_NAME + "/3-Array Antenna_Array 1");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        wizard.clickAccept();
        checkPopup();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 8)
    public void createCable() {
        openNewInventoryView();
        newInventoryViewPage.selectFirstRow();
        newInventoryViewPage.getMainTable().callAction("CREATE", "AdvanceCableCreateLocationWizardAction");
        CableWizardPage cableWizard = new CableWizardPage(driver);
        cableWizard.setEndLocation(LOCATION_NAME);
        cableWizard.setStartDevice(RRU_NAME);
        cableWizard.setEndDevice(ANTENNA_NAME);
        cableWizard.setModel("Feeder");
        cableWizard.setName(CABLE_NAME);
        cableWizard.accept();
        checkPopup();
    }

    @Test(priority = 9)
    public void createAntennaTrail() {
        openCellSiteConfigurationView();
        cellSiteConfigurationPage.selectTreeRow("All Resources");
        cellSiteConfigurationPage.selectRowByAttributeValueWithLabel("Name", ANTENNA_NAME);
        cellSiteConfigurationPage.selectRowByAttributeValueWithLabel("Name", RRU_NAME);
        cellSiteConfigurationPage.useTableContextActionById("Create Trail");
        cellSiteConfigurationPage.selectTrailType("Antenna Trail");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        ConnectionWizardPage connectionWizardPage = new ConnectionWizardPage(driver);
        connectionWizardPage.setName(ANTENNA_TRAIL_NAME);
        connectionWizardPage.clickNext();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        connectionWizardPage.selectConnectionTermination(2);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        connectionWizardPage.terminateCardComponent("No Card/Component");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        connectionWizardPage.terminatePort("Array 1");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        connectionWizardPage.terminateTerminationPort("-");
        connectionWizardPage.clickAccept();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 10)
    public void createCpriTrail() {
        cellSiteConfigurationPage.selectTreeRow("All Resources");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cellSiteConfigurationPage.selectTreeRow("All Resources");
        cellSiteConfigurationPage.selectRowByAttributeValueWithLabel("Name", BBU_NAME);
        cellSiteConfigurationPage.selectRowByAttributeValueWithLabel("Name", RRU_NAME);
        cellSiteConfigurationPage.useTableContextActionById("Create Trail");
        cellSiteConfigurationPage.selectTrailType("CPRI Trail");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        ConnectionWizardPage connectionWizardPage = new ConnectionWizardPage(driver);
        connectionWizardPage.setName(CPRI_NAME);
        connectionWizardPage.clickNext();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        //TODO check after OSSPHY-47410
//        connectionWizardPage.selectConnectionTermination(1);
//        DelayUtils.waitForPageToLoad(driver, webDriverWait);
//        connectionWizardPage.terminateCardComponent("No Card/Component");
//        DelayUtils.waitForPageToLoad(driver, webDriverWait);
//        connectionWizardPage.terminatePort("CPRI 1");
//        DelayUtils.waitForPageToLoad(driver, webDriverWait);
//        connectionWizardPage.terminateTerminationPort("CPRI 1");
//        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        connectionWizardPage.selectConnectionTermination(2);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        connectionWizardPage.terminateCardComponent("No Card/Component");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        connectionWizardPage.terminatePort("CPRI 1");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        connectionWizardPage.terminateTerminationPort("CPRI 1");
        connectionWizardPage.clickAccept();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 11)
    public void deleteCpriTrail() {
        cellSiteConfigurationPage.expandTreeToLocation("Site", LOCATION_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cellSiteConfigurationPage.selectTab("Trails");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cellSiteConfigurationPage.filterObject("Name", CPRI_NAME);
        cellSiteConfigurationPage.removeObject();
        checkPopup();
    }

    @Test(priority = 12)
    public void deleteAntennaTrail() {
        cellSiteConfigurationPage.selectTreeRow(LOCATION_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cellSiteConfigurationPage.selectTreeRow(LOCATION_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cellSiteConfigurationPage.selectTab("Trails");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cellSiteConfigurationPage.filterObject("Name", ANTENNA_TRAIL_NAME);
        cellSiteConfigurationPage.removeObject();
        checkPopup();
    }

    @Test(priority = 13)
    public void deleteCable() {
        cellSiteConfigurationPage.selectTreeRow(LOCATION_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cellSiteConfigurationPage.selectTreeRow(LOCATION_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cellSiteConfigurationPage.selectTab("Trails");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cellSiteConfigurationPage.filterObject("Name", CABLE_NAME);
        cellSiteConfigurationPage.removeObject();
        checkPopup();
    }

    @Test(priority = 14)
    public void deleteHostRelation() {
        cellSiteConfigurationPage.selectTreeRow(LOCATION_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cellSiteConfigurationPage.selectTreeRow(LOCATION_NAME);
        cellSiteConfigurationPage.selectTab("Hosting");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cellSiteConfigurationPage.filterObject("Hosting Resource", "3-Array Antenna_Array 1");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cellSiteConfigurationPage.removeObject();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        checkPopup();
    }

    @Test(priority = 15)
    public void deleteRanAntenna() {
        cellSiteConfigurationPage.selectTreeRow(LOCATION_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cellSiteConfigurationPage.selectTreeRow(LOCATION_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cellSiteConfigurationPage.selectTab("Devices");
        cellSiteConfigurationPage.filterObject("Name", ANTENNA_NAME);
        cellSiteConfigurationPage.removeObject();
        checkPopup();
    }

    @Test(priority = 16)
    public void deleteRru() {
        cellSiteConfigurationPage.selectTreeRow(LOCATION_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cellSiteConfigurationPage.selectTreeRow(LOCATION_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cellSiteConfigurationPage.selectTab("Devices");
        cellSiteConfigurationPage.filterObject("Name", RRU_NAME);
        cellSiteConfigurationPage.removeObject();
        checkPopup();
    }

    @Test(priority = 17)
    public void deleteBbu() {
        cellSiteConfigurationPage.selectTreeRow(LOCATION_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cellSiteConfigurationPage.selectTreeRow(LOCATION_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cellSiteConfigurationPage.selectTab("Devices");
        cellSiteConfigurationPage.filterObject("Name", BBU_NAME);
        cellSiteConfigurationPage.removeObject();
        checkPopup();
    }

    @Test(priority = 18)
    public void deleteEnodeB() {
        cellSiteConfigurationPage.selectTreeRow(LOCATION_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cellSiteConfigurationPage.selectTreeRow(LOCATION_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cellSiteConfigurationPage.selectTab("Devices");
        cellSiteConfigurationPage.filterObject("Name", ENODEB_NAME);
        cellSiteConfigurationPage.removeObject();
        checkPopup();
    }

    private void openCellSiteConfigurationView() {
        HomePage homePage = new HomePage(driver);
        homePage.goToHomePage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        SideMenu sideMenu = SideMenu.create(driver, webDriverWait);
        sideMenu.callActionByLabel("Cell Site Configuration", "Favourites", "SeleniumTests");
    }

    private void openNewInventoryView() {
        cellSiteConfigurationPage.selectTreeRow(LOCATION_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cellSiteConfigurationPage.useTreeContextAction("NAVIGATION", "OpenInventoryView");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    private void checkPopup() {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<Message> messages = systemMessage.getMessages();
        Assertions.assertThat(messages).hasSize(1);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getMessageType())
                .isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
    }
}
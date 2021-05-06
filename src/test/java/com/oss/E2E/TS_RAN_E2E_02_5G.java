package com.oss.E2E;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.alerts.SystemMessageContainer;
import com.oss.framework.alerts.SystemMessageInterface;
import com.oss.framework.mainheader.PerspectiveChooser;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.platform.HomePage;
import com.oss.pages.platform.NewInventoryViewPage;
import com.oss.pages.platform.OldInventoryView.OldInventoryViewPage;
import com.oss.pages.radio.AntennaArrayWizardPage;
import com.oss.pages.radio.CableWizardPage;
import com.oss.pages.radio.CellSiteConfigurationPage;
import com.oss.pages.radio.ConnectionWizardPage;
import com.oss.pages.radio.HostingWizardPage;
import com.oss.pages.radio.RanAntennaWizardPage;
import com.oss.pages.reconciliation.CmDomainWizardPage;
import com.oss.pages.reconciliation.NetworkDiscoveryControlViewPage;
import com.oss.pages.reconciliation.NetworkInconsistenciesViewPage;
import com.oss.pages.reconciliation.SamplesManagementPage;

import io.qameta.allure.Description;

public class TS_RAN_E2E_02_5G extends BaseTestCase {
    private final String LOCATION_NAME = "Poznan-BU1";

    private static final String RECO_GNODEB_NAME = "DXNNR0599UAT6-Expo-Multilateral-Buildings-Mobility-District";
    private static final String RECO_GNODEB_TYPE = "GNodeB";
    private static final String RECO_GNODEBCUUP_TYPE = "GNodeBCUUP";
    private static final String RECO_GNODEBDU_TYPE = "GNodeBDU";
    private static final String RECO_CELL5G_NAME = "DXNNR0599UAT61";
    private static final String CABLE_NAME = "TS_RAN_E2E_02_Cable";
    private static final String CABLE_MODEL = "Jumper";
    private static final String RECO_RRU_NAME1 = "BTS5900,DXNNR0599UAT6-Expo-Multilateral-Buildings-Mobility-District/0/MPMU,200";
    private static final String RECO_RRU_NAME2 = "BTS5900,DXNNR0599UAT6-Expo-Multilateral-Buildings-Mobility-District/0/MPMU,100";
    // TODO: change BBU name to whatever name we get after RECO can generate its own BBU device
    private static final String RECO_BBU_NAME = "temporary_TS_RAN_E2E_02_BBU";
    private static final String RECO_SWITCH_NAME1 = "BTS5900,DXNNR0599UAT6-Expo-Multilateral-Buildings-Mobility-District/0/RHUB,70";
    private static final String RECO_SWITCH_NAME2 = "BTS5900,DXNNR0599UAT6-Expo-Multilateral-Buildings-Mobility-District/0/RHUB,60";
    private static final String RAN_ANTENNA_NAME = "TS_RAN_E2E_02_RANAntenna";
    private static final String RAN_ANTENNA_MODEL = "Generic 3-Array Antenna";
    private static final String RAN_ANTENNA_ARRAY_NAME = "TS_RAN_E2E_02_RANAntenna/3-Array Antenna_Array 1/Freq(0-80000)";

    private static final String ANTENNA_TRAIL_NAME = "TS_RAN_E2E_02_AntennaTrail";
    private static final String CPRI_TRAIL_NAME = "TS_RAN_E2E_02_CPRI_Trail";

    private static final String CABLE_MODEL_DATA_ATTRIBUTENAME = "model";
    private static final String CABLE_NAME_DATA_ATTRIBUTENAME = "name";
    private static final String CREATE_TRAIL_DATA_ATTRIBUTENAME = "Create Trail";
    private static final String END_LOCATION_DATA_ATTRIBUTENAME = "endTermination";

    private NetworkDiscoveryControlViewPage networkDiscoveryControlViewPage;
    private String CM_DOMAIN_NAME = "Selenium-TS-RAN-E2E-02-5G";
    private String CM_INTERFACE_NAME = "Huawei U2000 RAN";
    private String[] RAN_INCONSISTENCIES_NAMES = {
            "GNODEB-" + RECO_GNODEB_NAME,
            "GNODEBDU-" + RECO_GNODEB_NAME,
            "GNODEBCUUP-" + RECO_GNODEB_NAME
    };
    private String[] PHYSICAL_INCONSISTENCIES_NAMES = {
            "PhysicalElement-" + RECO_RRU_NAME2,
            "PhysicalElement-" + RECO_RRU_NAME1,
            "PhysicalElement-" + RECO_SWITCH_NAME2,
            "PhysicalElement-" + RECO_SWITCH_NAME1,
    };

    private void checkPopup() {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assert.assertNotNull(messages);
        Assert.assertEquals(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getMessageType(),
                SystemMessageContainer.MessageType.SUCCESS);
    }

    public void openHomePage() {
        HomePage homePage = new HomePage(driver);
        homePage.goToHomePage(driver, BASIC_URL);
    }

    @BeforeClass
    public void openNetworkDiscoveryControlView() {
        networkDiscoveryControlViewPage = NetworkDiscoveryControlViewPage.goToNetworkDiscoveryControlViewPage(driver, BASIC_URL);
        PerspectiveChooser.create(driver, webDriverWait).setNetworkPerspective();
    }

    @Test(priority = 1)
    public void createCmDomain() {
        networkDiscoveryControlViewPage.openCmDomainWizard();
        CmDomainWizardPage wizard = new CmDomainWizardPage(driver);
        wizard.setName(CM_DOMAIN_NAME);
        wizard.setInterface(CM_INTERFACE_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        wizard.setDomain("RAN");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        wizard.save();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 2)
    public void uploadSamples() {
        DelayUtils.sleep(1000);
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(CM_DOMAIN_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkDiscoveryControlViewPage.moveToSamplesManagement();
        SamplesManagementPage samplesManagementPage = new SamplesManagementPage(driver);
        samplesManagementPage.selectPath();
        samplesManagementPage.createDirectory(CM_DOMAIN_NAME);
        DelayUtils.sleep(1000);
        samplesManagementPage.uploadSamples(
                "recoSamples/huaweiRan/TS_RAN_E2E_02_5G/Inventory_DXNNR0599UAT6-Expo-Multilateral-Buildings-Mobility-District_20200414_151757.xml");
        DelayUtils.sleep(1000);
        samplesManagementPage.uploadSamples(
                "recoSamples/huaweiRan/TS_RAN_E2E_02_5G/SRANNBIExport_XML_DXNNR0599UAT6_Co-MPT BTS_RT_04_09_2020_14_20_24_322_10_17_4_8.xml");
    }

    @Test(priority = 3)
    public void runReconciliationWithFullSample() {
        openNetworkDiscoveryControlView();
        DelayUtils.sleep(100);
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(CM_DOMAIN_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkDiscoveryControlViewPage.runReconciliation();
        networkDiscoveryControlViewPage.checkReconciliationStartedSystemMessage();
        networkDiscoveryControlViewPage.waitForEndOfReco();
    }

    @Test(priority = 4)
    public void assignLocationAndApplyInconsistencies() {
        networkDiscoveryControlViewPage.moveToNivFromNdcv();
        NetworkInconsistenciesViewPage networkInconsistenciesViewPage = new NetworkInconsistenciesViewPage(driver);
        networkInconsistenciesViewPage.expandTree();
        for (String inconsistencieName : PHYSICAL_INCONSISTENCIES_NAMES) {
            networkInconsistenciesViewPage.assignLocation(inconsistencieName, LOCATION_NAME);
            networkInconsistenciesViewPage.checkUpdateDeviceSystemMessage();
            networkInconsistenciesViewPage.clearOldNotification();
            networkInconsistenciesViewPage.applySelectedInconsistencies();
            DelayUtils.sleep(5000);
            networkInconsistenciesViewPage.checkNotificationAfterApplyInconsistencies(inconsistencieName);
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
        }
        for (String inconsistencieName : RAN_INCONSISTENCIES_NAMES) {
            networkInconsistenciesViewPage.assignRanLocation(inconsistencieName, LOCATION_NAME);
            networkInconsistenciesViewPage.checkUpdateDeviceSystemMessage();
            networkInconsistenciesViewPage.clearOldNotification();
            networkInconsistenciesViewPage.applySelectedInconsistencies();
            DelayUtils.sleep(5000);
            networkInconsistenciesViewPage.checkNotificationAfterApplyInconsistencies(inconsistencieName);
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
        }
    }

    @Test(priority = 5)
    public void deleteCmDomain() {
        openNetworkDiscoveryControlView();
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(CM_DOMAIN_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkDiscoveryControlViewPage.clearOldNotifications();
        networkDiscoveryControlViewPage.deleteCmDomain();
        networkDiscoveryControlViewPage.checkDeleteCmDomainSystemMessage();
        networkDiscoveryControlViewPage.checkDeleteCmDomainNotification(CM_DOMAIN_NAME);
        PerspectiveChooser.create(driver, webDriverWait).setLivePerspective();
    }

    @Test(priority = 6)
    @Description("Open Cell Site Configuration for Location")
    public void openCellSiteConfigurationForLocation() {
        openHomePage();
        new HomePage(driver).setOldObjectType("Location");
        new OldInventoryViewPage(driver)
                .filterObject("Name", LOCATION_NAME)
                .expandShowOnAndChooseView("Cell Site Configuration");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 7)
    @Description("Create RAN Antenna")
    public void createRANAntenna() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        CellSiteConfigurationPage cellSiteConfigurationPage = new CellSiteConfigurationPage(driver);
        cellSiteConfigurationPage.selectTab("Devices");
        cellSiteConfigurationPage.clickPlusIconAndSelectOption("Create RAN Antenna");
        RanAntennaWizardPage ranAntennaWizardPage = new RanAntennaWizardPage(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        ranAntennaWizardPage.setName(RAN_ANTENNA_NAME);
        ranAntennaWizardPage.setModel(RAN_ANTENNA_MODEL);
        DelayUtils.sleep(1000);
        ranAntennaWizardPage.setPreciseLocation(LOCATION_NAME);
        ranAntennaWizardPage.clickAccept();
        AntennaArrayWizardPage antennaArrayWizardPage = new AntennaArrayWizardPage(driver);
        antennaArrayWizardPage.clickAccept();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        checkPopup();
    }

    @Test(priority = 8)
    @Description("Create Hosting between Cell5G and RAN Antenna")
    public void createHostingBetweenCell5GAndRANAntenna() {
        CellSiteConfigurationPage cellSiteConfigurationPage = new CellSiteConfigurationPage(driver);
        cellSiteConfigurationPage.expandTreeToBaseStation("Site", LOCATION_NAME, RECO_GNODEB_NAME);
        cellSiteConfigurationPage.getTree().expandTreeRow(RECO_GNODEB_NAME);
        cellSiteConfigurationPage.selectTreeRow(RECO_CELL5G_NAME);
        cellSiteConfigurationPage.selectTab("Hosting");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cellSiteConfigurationPage.clickPlusIconAndSelectOption("Host on Antenna Array");
        HostingWizardPage wizard = new HostingWizardPage(driver);
        wizard.setHostingContains(RAN_ANTENNA_ARRAY_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        wizard.clickAccept();
        checkPopup();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 9)
    @Description("Create Cable between RRU and RAN Antenna")
    public void createCableBetweenRRUAndRANAntenna() {
        HomePage homePage = new HomePage(driver);
        homePage.goToHomePage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        NewInventoryViewPage newInventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, "Location");
        newInventoryViewPage.searchObject(LOCATION_NAME);
        newInventoryViewPage.selectFirstRow();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        newInventoryViewPage.callAction("CREATE", "AdvanceCableCreateLocationWizardAction");
        /*newInventoryViewPage.getWizard()
                .setComponentValue(END_LOCATION_DATA_ATTRIBUTENAME, LOCATION_NAME, Input.ComponentType.SEARCH_FIELD);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        newInventoryViewPage.getWizard()
                .setComponentValue(CABLE_MODEL_DATA_ATTRIBUTENAME, CABLE_MODEL, Input.ComponentType.SEARCH_FIELD);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        newInventoryViewPage.getWizard()
                .setComponentValue(CABLE_NAME_DATA_ATTRIBUTENAME, CABLE_NAME, Input.ComponentType.TEXT_FIELD);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        newInventoryViewPage.getWizard()
                .clickAccept();*/
        CableWizardPage cableWizardPage = new CableWizardPage(driver);
        cableWizardPage.setEndLocation(LOCATION_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cableWizardPage.setModel(CABLE_MODEL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cableWizardPage.setName(CABLE_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cableWizardPage.accept();
        checkPopup();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 10)
    @Description("Create Antenna Trail Between RRU and RAN Antenna")
    public void createAntennaTrailBetweenRRUAndRANAntenna() {
        NewInventoryViewPage newInventoryViewPage = new NewInventoryViewPage(driver, webDriverWait);
        newInventoryViewPage.callAction("NAVIGATION", "Cell Site Configuration");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        CellSiteConfigurationPage cellSiteConfigurationPage = new CellSiteConfigurationPage(driver);
        cellSiteConfigurationPage.selectTreeRow("All Resources");
        cellSiteConfigurationPage.selectRowByAttributeValueWithLabel("Name", RECO_RRU_NAME1);
        cellSiteConfigurationPage.selectRowByAttributeValueWithLabel("Name", RAN_ANTENNA_NAME);
        cellSiteConfigurationPage.useTableContextActionById(CREATE_TRAIL_DATA_ATTRIBUTENAME);
        cellSiteConfigurationPage.selectTrailType("Antenna Trail");
        ConnectionWizardPage connectionWizardPage = new ConnectionWizardPage(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        connectionWizardPage.setName(ANTENNA_TRAIL_NAME);
        connectionWizardPage.clickNext();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        connectionWizardPage.selectConnectionTermination(1);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        connectionWizardPage.clickAccept();
        checkPopup();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }
    
    /*//TODO: wait for OSSPHY-47340
    @Test(priority = 11)
    @Description("Create CPRI Trail between RRU and BBU")
    public void createCPRITrailBetweenRRUAndBBU() {
        CellSiteConfigurationPage cellSiteConfigurationPage = new CellSiteConfigurationPage(driver);
        cellSiteConfigurationPage.selectResource(RAN_ANTENNA_NAME); // to unselect already selected RAN Antenna
        cellSiteConfigurationPage.selectResource(RECO_BBU_NAME);
        cellSiteConfigurationPage.useTableContextActionById(CREATE_TRAIL_DATA_ATTRIBUTENAME);
        cellSiteConfigurationPage.selectTrailType("Antenna Trail");
        ConnectionWizardPage connectionWizardPage = new ConnectionWizardPage(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        connectionWizardPage.setName(CPRI_TRAIL_NAME);
        connectionWizardPage.clickNext();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        connectionWizardPage.selectConnectionTermination(1);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        connectionWizardPage.clickAccept();
        checkPopup();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }*/

    @Test(priority = 12)
    @Description("Delete CPRI Trail between RRU and BBU")
    public void deleteCPRITrailBetweenRRUAndBBU() {
        CellSiteConfigurationPage cellSiteConfigurationPage = new CellSiteConfigurationPage(driver);
        cellSiteConfigurationPage.selectTreeRow(LOCATION_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cellSiteConfigurationPage.selectTab("Trails");
        /* cellSiteConfigurationPage.filterObject("Name", CPRI_TRAIL_NAME);
        cellSiteConfigurationPage.removeObject();
        checkPopup();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);*/
    }

    @Test(priority = 13)
    @Description("Delete Antenna Trail between RRU and RAN Antenna")
    public void deleteAntennaTrailBetweenRRUAndRANAntenna() {
        CellSiteConfigurationPage cellSiteConfigurationPage = new CellSiteConfigurationPage(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cellSiteConfigurationPage.filterObject("Name", ANTENNA_TRAIL_NAME);
        cellSiteConfigurationPage.removeObject();
        checkPopup();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 14)
    @Description("Delete Cable between RRU and RAN Antenna")
    public void deleteCableBetweenRRUAndRANAntenna() {
        CellSiteConfigurationPage cellSiteConfigurationPage = new CellSiteConfigurationPage(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cellSiteConfigurationPage.filterObject("Name", CABLE_NAME);
        cellSiteConfigurationPage.removeObject();
        checkPopup();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 15)
    @Description("Delete Hosting between Cell 5G and RAN Antenna")
    public void deleteHostingBetweenCell5GAndRANAntenna() {
        CellSiteConfigurationPage cellSiteConfigurationPage = new CellSiteConfigurationPage(driver);
        cellSiteConfigurationPage.expandTreeToBaseStation("Site", LOCATION_NAME, RECO_GNODEB_NAME);
        cellSiteConfigurationPage.getTree().expandTreeRow(RECO_GNODEB_NAME);
        cellSiteConfigurationPage.selectTreeRow(RECO_CELL5G_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cellSiteConfigurationPage.selectTab("Hosting");
        cellSiteConfigurationPage.filterObject("Hosted Resource", RECO_CELL5G_NAME);
        cellSiteConfigurationPage.removeObject();
        checkPopup();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 16)
    @Description("Delete RAN Antenna")
    public void deleteRANAntenna() {
        CellSiteConfigurationPage cellSiteConfigurationPage = new CellSiteConfigurationPage(driver);
        cellSiteConfigurationPage.selectTreeRow(LOCATION_NAME);
        cellSiteConfigurationPage.selectTab("Devices");
        cellSiteConfigurationPage.filterObject("Name", RAN_ANTENNA_NAME);
        cellSiteConfigurationPage.removeObject();
        checkPopup();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 17)
    @Description("Delete Reco devices")
    public void deleteRecoDevices() {
        CellSiteConfigurationPage cellSiteConfigurationPage = new CellSiteConfigurationPage(driver);
        /*///////////////////////
        cellSiteConfigurationPage.expandTreeToBaseStation("Site", LOCATION_NAME, RECO_GNODEB_NAME);
        cellSiteConfigurationPage.selectTreeRow(LOCATION_NAME);
        cellSiteConfigurationPage.selectTab("Devices");
        ///////////////////////*/

        cellSiteConfigurationPage.selectRowByAttributeValueWithLabel("Name", RECO_RRU_NAME1);
        cellSiteConfigurationPage.selectRowByAttributeValueWithLabel("Name", RECO_RRU_NAME2);
        // cellSiteConfigurationPage.selectResource(RECO_BBU_NAME);
        cellSiteConfigurationPage.selectRowByAttributeValueWithLabel("Name", RECO_SWITCH_NAME1);
        cellSiteConfigurationPage.selectRowByAttributeValueWithLabel("Name", RECO_SWITCH_NAME2);
        cellSiteConfigurationPage.removeObject();
        checkPopup();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }
    
    /*@Test(priority = 18)
    @Description("Delete Reco Cell 5G")
    public void deleteRecoCell5G() {
        CellSiteConfigurationPage cellSiteConfigurationPage = new CellSiteConfigurationPage(driver);
        cellSiteConfigurationPage.selectTab("Cells");
        cellSiteConfigurationPage.filterObject("Name", RECO_CELL5G_NAME);
        cellSiteConfigurationPage.removeObject();
        checkPopup();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }*/

    @Test(priority = 19)
    @Description("Delete Reco gNodeBs")
    public void deleteRecoGNodeB() {
        CellSiteConfigurationPage cellSiteConfigurationPage = new CellSiteConfigurationPage(driver);
        cellSiteConfigurationPage.selectTab("Base Stations");
        cellSiteConfigurationPage.filterObject("Type", RECO_GNODEBCUUP_TYPE);
        cellSiteConfigurationPage.removeObject();
        checkPopup();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cellSiteConfigurationPage.filterObject("Type", RECO_GNODEBDU_TYPE);
        cellSiteConfigurationPage.removeObject();
        checkPopup();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cellSiteConfigurationPage.filterObject("Type", RECO_GNODEB_TYPE);
        cellSiteConfigurationPage.removeObject();
        checkPopup();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }
}

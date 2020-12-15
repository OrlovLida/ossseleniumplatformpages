package com.oss.E2E;

import com.oss.BaseTestCase;
import com.oss.framework.alerts.SystemMessageContainer;
import com.oss.framework.alerts.SystemMessageInterface;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.platform.HomePage;
import com.oss.pages.platform.NewInventoryViewPage;
import com.oss.pages.platform.OldInventoryViewPage;
import com.oss.pages.radio.*;
import io.qameta.allure.Description;
import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

public class TS_RAN_E2E_02 extends BaseTestCase {

    private final String LOCATION_NAME = "Poznan-BU1";

    private final String GNODEB_NAME = "DXNNR0599UAT6-Expo-Multilateral-Buildings-Mobility-District";
    private final String CELL5G_NAME = "DXNNR0599UAT61";
    private final String CABLE_NAME = "TS_RAN_E2E_02_Cable";
    private final String CABLE_MODEL = "Jumper";
    private final String RECO_RRU_NAME = "BTS5900,DXNNR0599UAT6-Expo-Multilateral-Buildings-Mobility-District/0/MPMU,200";
    //TODO: change BBU name to whatever name we get after RECO can generate its own BBU device
    private final String RECO_BBU_NAME = "temporary_TS_RAN_E2E_02_BBU";
    private final String RAN_ANTENNA_NAME = "TS_RAN_E2E_02_RANAntenna";
    private final String RAN_ANTENNA_MODEL = "Generic 3-Array Antenna";
    private final String RAN_ANTENNA_ARRAY_NAME = "TS_RAN_E2E_02_RANAntenna/3-Array Antenna_Array 1/Freq(0-80000)";

    private final String ANTENNA_TRAIL_NAME = "TS_RAN_E2E_02_AntennaTrail";
    private final String CPRI_TRAIL_NAME = "TS_RAN_E2E_02_CPRI_Trail";

    private final String CABLE_MODEL_DATA_ATTRIBUTENAME = "model";
    private final String CABLE_NAME_DATA_ATTRIBUTENAME = "name";
    private final String CREATE_TRAIL_DATA_ATTRIBUTENAME = "Create Trail";
    private final String END_LOCATION_DATA_ATTRIBUTENAME = "endTermination";

    private void checkPopup() {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assertions.assertThat(messages).hasSize(1);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getMessageType())
                .isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
    }

    @BeforeClass
    public void openCellSiteConfigurationForLocation() {
        new HomePage(driver).setAndSelectObjectType("Location");
        new OldInventoryViewPage(driver)
                .filterObject("Name", LOCATION_NAME, "Location")
                .expandShowOnAndChooseView("Cell Site Configuration");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 1)
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

    @Test(priority = 2)
    @Description("Create Hosting between Cell5G and RAN Antenna")
    public void createHostingBetweenCell5GAndRANAntenna() {
        CellSiteConfigurationPage cellSiteConfigurationPage = new CellSiteConfigurationPage(driver);
        cellSiteConfigurationPage.expandTreeToBaseStation("Site", LOCATION_NAME, GNODEB_NAME);
        cellSiteConfigurationPage.getTree().expandTreeRow(GNODEB_NAME);
        cellSiteConfigurationPage.selectTreeRow(CELL5G_NAME);
        cellSiteConfigurationPage.selectTab("Hosting");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cellSiteConfigurationPage.clickPlusIconAndSelectOption("Host on Antenna Array");
        HostingWizardPage wizard = new HostingWizardPage(driver);
        wizard.selectArray(RAN_ANTENNA_ARRAY_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        wizard.clickAccept();
        checkPopup();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 3)
    @Description("Create Cable between RRU and RAN Antenna")
    public void createCableBetweenRRUAndRANAntenna() {
        HomePage homePage = new HomePage(driver);
        homePage.goToHomePage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        NewInventoryViewPage newInventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, "Location");
        newInventoryViewPage.openFilterPanel().changeValueInLocationNameInput(LOCATION_NAME).applyFilter();
        newInventoryViewPage.selectFirstRow();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        newInventoryViewPage.callButtonByIDAndChooseAction("CREATE", "AdvanceCableCreateLocationWizardAction");
        newInventoryViewPage.getWizard()
                .setComponentValue(END_LOCATION_DATA_ATTRIBUTENAME, LOCATION_NAME, Input.ComponentType.SEARCH_FIELD);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        newInventoryViewPage.getWizard()
                .setComponentValue(CABLE_MODEL_DATA_ATTRIBUTENAME, CABLE_MODEL, Input.ComponentType.SEARCH_FIELD);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        newInventoryViewPage.getWizard()
                .setComponentValue(CABLE_NAME_DATA_ATTRIBUTENAME, CABLE_NAME, Input.ComponentType.TEXT_FIELD);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        newInventoryViewPage.getWizard()
                .clickAccept();
        checkPopup();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 4)
    @Description("Create Antenna Trail Between RRU and RAN Antenna")
    public void createAntennaTrailBetweenRRUAndRANAntenna() {
        NewInventoryViewPage newInventoryViewPage = new NewInventoryViewPage(driver);
        newInventoryViewPage.callButtonByIDAndChooseAction("NAVIGATION", "Cell Site Configuration");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        CellSiteConfigurationPage cellSiteConfigurationPage = new CellSiteConfigurationPage(driver);
        cellSiteConfigurationPage.selectTreeRow("All Resources");
        cellSiteConfigurationPage.selectResource(RECO_RRU_NAME);
        cellSiteConfigurationPage.selectResource(RAN_ANTENNA_NAME);
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

    //TODO: wait for OSSPHY-47340
    @Test(priority = 5)
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
    }

    @Test(priority = 6)
    @Description("Delete CPRI Trail between RRU and BBU")
    public void deleteCPRITrailBetweenRRUAndBBU() {
        CellSiteConfigurationPage cellSiteConfigurationPage = new CellSiteConfigurationPage(driver);
        cellSiteConfigurationPage.selectTreeRow(LOCATION_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cellSiteConfigurationPage.selectTab("Trails");
        cellSiteConfigurationPage.filterObject("Name", CPRI_TRAIL_NAME);
        cellSiteConfigurationPage.removeObject();
        checkPopup();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 7)
    @Description("Delete Antenna Trail between RRU and RAN Antenna")
    public void deleteAntennaTrailBetweenRRUAndRANAntenna() {
        CellSiteConfigurationPage cellSiteConfigurationPage = new CellSiteConfigurationPage(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cellSiteConfigurationPage.filterObject("Name", ANTENNA_TRAIL_NAME);
        cellSiteConfigurationPage.removeObject();
        checkPopup();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 8)
    @Description("Delete Cable between RRU and RAN Antenna")
    public void deleteCableBetweenRRUAndRANAntenna() {
        CellSiteConfigurationPage cellSiteConfigurationPage = new CellSiteConfigurationPage(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cellSiteConfigurationPage.filterObject("Name", CABLE_NAME);
        cellSiteConfigurationPage.removeObject();
        checkPopup();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 9)
    @Description("Delete Hosting between Cell 5G and RAN Antenna")
    public void deleteHostingBetweenCell5GAndRANAntenna() {
        CellSiteConfigurationPage cellSiteConfigurationPage = new CellSiteConfigurationPage(driver);
        cellSiteConfigurationPage.expandTreeToBaseStation("Site", LOCATION_NAME, GNODEB_NAME);
        cellSiteConfigurationPage.getTree().expandTreeRow(GNODEB_NAME);
        cellSiteConfigurationPage.selectTreeRow(CELL5G_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cellSiteConfigurationPage.selectTab("Hosting");
        cellSiteConfigurationPage.filterObject("Hosted Resource", CELL5G_NAME);
        cellSiteConfigurationPage.removeObject();
        checkPopup();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 10)
    @Description("Delete RAN Antenna")
    public void deleteRANAntenna() {
        CellSiteConfigurationPage cellSiteConfigurationPage  = new CellSiteConfigurationPage(driver);
        cellSiteConfigurationPage.selectTreeRow(LOCATION_NAME);
        cellSiteConfigurationPage.selectTab("Devices");
        cellSiteConfigurationPage.filterObject("Name", RAN_ANTENNA_NAME);
        cellSiteConfigurationPage.removeObject();
        checkPopup();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }
}
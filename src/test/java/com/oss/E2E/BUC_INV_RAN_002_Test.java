package com.oss.E2E;

import java.util.Optional;

import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.alerts.SystemMessageInterface;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.bpm.ProcessOverviewPage;
import com.oss.pages.bpm.TasksPageV2;
import com.oss.pages.platform.HomePage;
import com.oss.pages.platform.NewInventoryViewPage;
import com.oss.pages.platform.SearchObjectTypePage;
import com.oss.pages.radio.CellSiteConfigurationPage;
import com.oss.repositories.AddressRepository;
import com.oss.repositories.LocationInventoryRepository;
import com.oss.repositories.PhysicalInventoryRepository;
import com.oss.repositories.Radio5gRepository;
import com.oss.services.RadioClient;
import com.oss.services.ResourceCatalogClient;
import com.oss.untils.Environment;
import com.oss.utils.TestListener;

import io.qameta.allure.Description;

@Listeners({TestListener.class})
public class BUC_INV_RAN_002_Test extends BaseTestCase {

    private static final String LOCATION_NAME = "Denver1";
    private static final String PROCESS_NAME = "Denver1 - Extension - 5G";
    private static final String COUNTRY_NAME = "United States";
    private static final String REGION_NAME = "Colorado";
    private static final String DISTRICT_NAME = "District 1";
    private static final String CITY_NAME = "Denver";
    private static final String POSTAL_CODE_NAME = "80014";
    private static final String MCCMNC_PRIMARY = "E2ETest [mcc: 999, mnc: 1]";
    private static final String MCC_MNC_OPERATOR = "E2ETest";
    private static final String MCC = "999";
    private static final String MNC = "1";
    private static final String NRP = "Network Resource Process";
    private static final String GNODEB_NAME = "Denver51";
    private static final String GNODEB_ID = "1";
    private static final String SITE = "Site";
    private static final String SYSTEM_MESSAGE_PATTERN = "%s. Checking system message after %s.";
    private static final String GNODEB_DU_NAME = "Denver51_DU";
    private static final String GNODEB_DU_ID = "1";
    private static final String CELL5G_NAME_0 = "Denver511";
    private static final String CELL5G_NAME_1 = "Denver512";
    private static final String CELL5G_NAME_2 = "Denver513";
    private static final String ANTENNA_NAME_0 = "BTS5900,Denver1/0/AIRU,100";
    private static final String ANTENNA_NAME_1 = "BTS5900,Denver1/0/AIRU,101";
    private static final String ANTENNA_NAME_2 = "BTS5900,Denver1/0/AIRU,102";
    private static final String RAN_ANTENNA_MODEL = "HUAWEI Technology Co.,Ltd AAU5614";
    private static final String CELL5G_CARRIER_NAME = "NR3600-n78-140";
    private static final String CELL5G_CARRIER = "NR3600-n78-140 (642000)";
    private static final String BAND_TYPE_NAME = "NR3600-n78";
    private static final String MANUFACTURER = "HUAWEI Technology Co.,Ltd";
    private static final String BBU_MODEL = "BBU5900";
    private static final String DEVICE_MODEL_TYPE = "DeviceModel";
    private static final String GNODEB_MODEL = "HUAWEI Technology Co.,Ltd gNodeB";
    private static final String GNODEB_DU_MODEL = "HUAWEI Technology Co.,Ltd gNodeB DU";
    private static final String BBU_NAME = "Denver41_BBU";
    private static final String[] ANTENNA_NAMES = {ANTENNA_NAME_0, ANTENNA_NAME_1, ANTENNA_NAME_2};
    private static final String[] CELL5G_NAMES = {CELL5G_NAME_0, CELL5G_NAME_1, CELL5G_NAME_2};
    private static final String[] PCI = {"894", "895", "896"};
    private static final String[] RSI = {"532", "348", "144"};
    private static final int DOWNLINK_CHANNEL = 642000;
    private static final int UPLINK_CHANNEL = 642000;
    private static final int DL_FREQUENCY_END = 3680;
    private static final int DL_FREQUENCY_START = 3580;
    private static final int UL_FREQUENCY_END = 3680;
    private static final int UL_FREQUENCY_START = 3580;
    private static final int DL_CENTRE_FREQUENCY = 3630;
    private static final int UL_CENTRE_FREQUENCY = 3630;
    private static final int[] LOCAL_CELLS_ID = {101, 102, 103};
    private final Environment env = Environment.getInstance();

    private String processNRPCode;
    private CellSiteConfigurationPage cellSiteConfigurationPage;
    private PhysicalInventoryRepository physicalInventoryRepository;
    private Radio5gRepository radio5gRepository;
    private SoftAssert softAssert;

    @BeforeClass
    public void openConsole() {
        waitForPageToLoad();
        cellSiteConfigurationPage = new CellSiteConfigurationPage(driver);
        physicalInventoryRepository = new PhysicalInventoryRepository(env);
        radio5gRepository = new Radio5gRepository(env);
        softAssert = new SoftAssert();
    }

    @Test(priority = 1, description = "Check prerequisites")
    @Description("Check prerequisites")
    public void checkPrereq() {
        String locationId = getOrCreateLocations();
        getOrCreateMccMnc();
        getOrCreateCarrier();
        getOrCreateBBU(locationId);
    }

    @Test(priority = 2, description = "Create NRP Process", dependsOnMethods = {"checkPrereq"})
    @Description("Create NRP Process")
    public void createProcessNRP() {
        ProcessOverviewPage processInstancesPage = ProcessOverviewPage.goToProcessOverviewPage(driver, webDriverWait);
        processNRPCode = processInstancesPage.createProcessIPD(PROCESS_NAME, 0L, NRP);
        closeMessage();
    }

    @Test(priority = 3, description = "Start High Level Planning Task", dependsOnMethods = {"createProcessNRP"})
    @Description("Start High Level Planning Task")
    public void startHLPTask() {
        TasksPageV2 tasksPage = TasksPageV2.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.startTask(processNRPCode, TasksPageV2.HIGH_LEVEL_PLANNING_TASK);
        checkTaskAssignment(String.format(SYSTEM_MESSAGE_PATTERN, "Start HLP task", "HLP task start"));
        closeMessage();
    }

    @Test(priority = 4, description = "Create gNodeB", dependsOnMethods = {"startHLPTask"})
    @Description("Create gNodeB")
    public void createGNodeB() {
        openCellSiteConfigurationView();
        cellSiteConfigurationPage.createGNodeB(GNODEB_NAME, GNODEB_ID, GNODEB_MODEL, MCCMNC_PRIMARY);
        checkMessageContainsText("GNodeB was created", String.format(SYSTEM_MESSAGE_PATTERN, "Create GNodeB", "GNodeB create"));
        closeMessage();
    }

    @Test(priority = 5, description = "Create gNodeB DU", dependsOnMethods = {"createGNodeB"})
    @Description("Create GNodeB DU")
    public void createGNodeBDU() {
        cellSiteConfigurationPage.createGNodeBDU(GNODEB_DU_NAME, GNODEB_DU_ID, GNODEB_DU_MODEL, GNODEB_NAME);
        checkMessageContainsText("GNodeB DU was created", String.format(SYSTEM_MESSAGE_PATTERN, "Create GNodeB DU", "GNodeB DU create"));
        closeMessage();
    }

    @Test(priority = 6, description = "Create three cells 5G", dependsOnMethods = {"createGNodeBDU"})
    @Description("Create three cells 5G")
    public void create5Gcells() {
        cellSiteConfigurationPage.expandTreeToBaseStation(SITE, LOCATION_NAME, GNODEB_NAME);
        cellSiteConfigurationPage.createCell5GBulk(3, CELL5G_CARRIER, CELL5G_NAMES, LOCAL_CELLS_ID, PCI, RSI);//TODO krok z LLD z uzupełnieniem wyjaśnic
        checkMessageContainsText("Cells 5G created success", String.format(SYSTEM_MESSAGE_PATTERN, "Create 5G cells", "cells 5G create"));
        closeMessage();
    }

    @Test(priority = 7, description = "Create three ran antenna", dependsOnMethods = {"create5Gcells"})
    @Description("Create three ran antenna")
    public void createRanAntenna() {
        waitForPageToLoad();
        cellSiteConfigurationPage.selectTreeRow(LOCATION_NAME);
        waitForPageToLoad();
        cellSiteConfigurationPage.selectTab("Devices");
        for (String ranAntenna : ANTENNA_NAMES) {
            waitForPageToLoad();
            cellSiteConfigurationPage.createRanAntennaAndArray(ranAntenna, RAN_ANTENNA_MODEL, LOCATION_NAME);
            checkMessageType(String.format(SYSTEM_MESSAGE_PATTERN, "Create ran antenna", "ran antenna create"));
        }
    }

    @Test(priority = 8, description = "Create hosting on BBU and hostings on antenna arrays", dependsOnMethods = {"createRanAntenna"})
    @Description("Create hosting on BBU and hostings on antenna arrays")
    public void createHostingRelation() {
        waitForPageToLoad();
        cellSiteConfigurationPage.selectTreeRow(GNODEB_NAME);
        waitForPageToLoad();
        cellSiteConfigurationPage.createHostingOnDevice(BBU_NAME, true);
        checkMessageType(String.format(SYSTEM_MESSAGE_PATTERN, "Create hosting relation", "GNodeB hosting on device create"));
        cellSiteConfigurationPage.selectTreeRow(GNODEB_DU_NAME);
        waitForPageToLoad();
        cellSiteConfigurationPage.createHostingOnDevice(BBU_NAME, true);
        checkMessageType(String.format(SYSTEM_MESSAGE_PATTERN, "Create hosting relation", "GNodeB DU hosting on device create"));
    }

    @Test(priority = 9, description = "Create hosting relation between Cell 5G and AAU", dependsOnMethods = {"create5Gcells"})
    @Description("Create hosting relation between Cell 5G and AAU")
    public void hostCell5GOnAAU() {
        for (int i = 0; i < CELL5G_NAMES.length; i++) {
            cellSiteConfigurationPage.selectTreeRow(CELL5G_NAMES[i]);
            cellSiteConfigurationPage.createHostingOnDevice(ANTENNA_NAMES[i]);
            checkMessageType(String.format(SYSTEM_MESSAGE_PATTERN, "Create hosting relation", "cell 5G hosting on antenna create"));
        }
    }

    @Test(priority = 10, description = "Create hosting relation between Cell 5G and RAN Antenna Array", dependsOnMethods = {"create5Gcells", "createRanAntenna"})
    @Description("Create hosting relation between Cell 5G and RAN Antenna Array")
    public void hostCell5GOnRANAntennaArray() {
        for (int i = 0; i < CELL5G_NAMES.length; i++) {
            cellSiteConfigurationPage.selectTreeRow(CELL5G_NAMES[i]);
            cellSiteConfigurationPage.createHostingOnAntennaArray(ANTENNA_NAMES[i]);
            checkMessageType(String.format(SYSTEM_MESSAGE_PATTERN, "Create hosting relation", "cell 5G hosting on antenna array create"));
        }
    }

    @Test(priority = 11, description = "Finish rest of NRP and IP Tasks", dependsOnMethods = {"createHostingRelation"})
    @Description("Finish rest of NRP and IP Tasks")
    public void finishProcessesTasks() {
        TasksPageV2 tasksPage = TasksPageV2.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.completeNRP(processNRPCode);
        waitForPageToLoad();
    }

    @Test(priority = 12, description = "Delete hosting relations", dependsOnMethods = {"createHostingRelation"})
    @Description("Delete hosting relations")
    public void deleteHostingRelation() {
        openCellSiteConfigurationView();
        cellSiteConfigurationPage.expandTreeToBaseStation(SITE, LOCATION_NAME, GNODEB_NAME);
        waitForPageToLoad();
        cellSiteConfigurationPage.selectTab("Hosting");
        waitForPageToLoad();
        cellSiteConfigurationPage.filterObject("Hosted Resource", GNODEB_NAME);
        waitForPageToLoad();
        cellSiteConfigurationPage.removeObject();
        checkMessageType(String.format(SYSTEM_MESSAGE_PATTERN, "Delete hosting relation", "hosting of GNodeB delete"));
        waitForPageToLoad();
        cellSiteConfigurationPage.getTree().expandTreeRow(GNODEB_NAME);
        for (String cell : CELL5G_NAMES) {
            waitForPageToLoad();
            cellSiteConfigurationPage.selectTreeRow(cell);
            waitForPageToLoad();
            cellSiteConfigurationPage.selectTab("Hosting");
            waitForPageToLoad();
            cellSiteConfigurationPage.filterObject("Hosted Resource", cell);
            waitForPageToLoad();
            cellSiteConfigurationPage.removeObject();
            checkMessageType(String.format(SYSTEM_MESSAGE_PATTERN, "Delete hosting relation", "hosting of cell 5G delete"));
            waitForPageToLoad();
        }
    }

    @Test(priority = 13, description = "Delete ran antennas", dependsOnMethods = {"createRanAntenna"})
    @Description("Delete ran antennas")
    public void deleteRanAntenna() {
        cellSiteConfigurationPage.selectTreeRow(LOCATION_NAME);
        waitForPageToLoad();
        cellSiteConfigurationPage.selectTab("Devices");
        for (String ranAntenna : ANTENNA_NAMES) {
            waitForPageToLoad();
            cellSiteConfigurationPage.removeDevice("Antennas", MANUFACTURER, ranAntenna);
            checkMessageType(String.format(SYSTEM_MESSAGE_PATTERN, "Delete ran antenna", "ran antenna delete"));
            waitForPageToLoad();
        }
    }

    @Test(priority = 14, description = "Delete cells 5G", dependsOnMethods = {"create5Gcells"})
    @Description("Delete cells 5G")
    public void delete5Gcells() {
        cellSiteConfigurationPage.selectTreeRow(GNODEB_NAME);
        waitForPageToLoad();
        cellSiteConfigurationPage.selectTab("Cells 5G");
        waitForPageToLoad();
        for (String cell : CELL5G_NAMES) {
            cellSiteConfigurationPage.filterObject("Name", cell);
            waitForPageToLoad();
            cellSiteConfigurationPage.removeObject();
            checkMessageType(String.format(SYSTEM_MESSAGE_PATTERN, "Delete 5G cells", "cells 5G delete"));
            waitForPageToLoad();
        }
    }

    @Test(priority = 15, description = "Delete GNodeB DU", dependsOnMethods = {"createGNodeBDU"})
    @Description("Delete GNodeB DU")
    public void deleteGNodeBDU() {
        cellSiteConfigurationPage.selectTreeRow(LOCATION_NAME);
        waitForPageToLoad();
        cellSiteConfigurationPage.selectTab("Base Stations");
        waitForPageToLoad();
        cellSiteConfigurationPage.filterObject("Name", GNODEB_DU_NAME);
        waitForPageToLoad();
        cellSiteConfigurationPage.removeObject();
        checkMessageType(String.format(SYSTEM_MESSAGE_PATTERN, "Delete GNodeB DU", "GNodeB DU delete"));
    }

    @Test(priority = 16, description = "Delete GNodeB", dependsOnMethods = {"createGNodeB"})
    @Description("Delete GNodeB")
    public void deleteGNodeB() {
        waitForPageToLoad();
        cellSiteConfigurationPage.filterObject("Name", GNODEB_NAME);
        waitForPageToLoad();
        cellSiteConfigurationPage.removeObject();
        checkMessageType(String.format(SYSTEM_MESSAGE_PATTERN, "Delete GNodeB", "GNodeB delete"));
    }

    @Test(priority = 17, description = "Checking system message summary")
    @Description("Checking system message summary")
    public void systemMessageSummary() {
        softAssert.assertAll();
    }

    private void openCellSiteConfigurationView() {
        HomePage homePage = new HomePage(driver);
        homePage.goToHomePage(driver, BASIC_URL);
        waitForPageToLoad();
        homePage.chooseFromLeftSideMenu("Inventory View", "Resource Inventory");
        waitForPageToLoad();
        SearchObjectTypePage searchObjectTypePage = new SearchObjectTypePage(driver, webDriverWait);
        searchObjectTypePage.searchType(SITE);
        waitForPageToLoad();
        NewInventoryViewPage newInventoryViewPage = new NewInventoryViewPage(driver, webDriverWait);
        newInventoryViewPage.searchObject(LOCATION_NAME);
        waitForPageToLoad();
        newInventoryViewPage.selectFirstRow();
        waitForPageToLoad();
        newInventoryViewPage.callAction(ActionsContainer.SHOW_ON_GROUP_ID, "Cell Site Configuration");
        waitForPageToLoad();
    }

    private void checkMessageType(String systemMessageLog) {
        SystemMessageInterface systemMessage = getSuccesSystemMessage(systemMessageLog);
        systemMessage.close();
        waitForPageToLoad();
    }

    private SystemMessageInterface getSuccesSystemMessage(String systemMessageLog) {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, new WebDriverWait(driver, 90));
        Optional<SystemMessageContainer.Message> firstSystemMessage = systemMessage.getFirstMessage();
        softAssert.assertTrue(firstSystemMessage.isPresent(), systemMessageLog);
        if (firstSystemMessage.isPresent()) {
            softAssert.assertEquals((systemMessage.getFirstMessage()
                    .orElseThrow(() -> new RuntimeException("The list is empty")).getMessageType()), SystemMessageContainer.MessageType.SUCCESS, systemMessageLog);
        }
        return systemMessage;
    }

    private void checkMessageContainsText(String message, String systemMessageLog) {
        softAssert.assertTrue((getFirstMessage().getText())
                .contains(message), systemMessageLog);
    }

    private void checkMessageText(String systemMessageLog) {
        softAssert.assertEquals((getFirstMessage().getText()), "The task properly assigned.", systemMessageLog);
    }

    private SystemMessageContainer.Message getFirstMessage() {
        return SystemMessageContainer.create(driver, webDriverWait)
                .getFirstMessage()
                .orElseThrow(() -> new RuntimeException("The list is empty"));
    }

    private void checkTaskAssignment(String systemMessageLog) {
        checkMessageText(systemMessageLog);
        checkMessageType(systemMessageLog);
    }

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    private void closeMessage() {
        SystemMessageContainer.create(driver, webDriverWait).close();
        waitForPageToLoad();
    }

    private String getOrCreateLocations() {
        LocationInventoryRepository locationInventoryRepository = new LocationInventoryRepository(env);
        return locationInventoryRepository.getOrCreateLocation(LOCATION_NAME, SITE, prepareAddress());
    }

    private Long prepareAddress() {
        AddressRepository addressRepository = new AddressRepository(Environment.getInstance());
        return addressRepository.updateOrCreateAddress(COUNTRY_NAME, POSTAL_CODE_NAME, REGION_NAME, CITY_NAME, DISTRICT_NAME);
    }

    private void getOrCreateMccMnc() {
        RadioClient.getInstance(Environment.getInstance()).getOrCreateMccMnc(MCC_MNC_OPERATOR, MCC, MNC);
    }

    private Long getOrCreateBandType() {
        return radio5gRepository.getOrCreateBandType(BAND_TYPE_NAME, DL_FREQUENCY_START, DL_FREQUENCY_END, UL_FREQUENCY_START, UL_FREQUENCY_END);
    }

    private void getOrCreateCarrier() {
        radio5gRepository.getOrCreateCarrier(CELL5G_CARRIER_NAME, DOWNLINK_CHANNEL, UPLINK_CHANNEL, DL_CENTRE_FREQUENCY, UL_CENTRE_FREQUENCY, getOrCreateBandType());
    }

    private void getOrCreateBBU(String locationId) {
        if (!isDevicePresent(locationId)) {
            createBBU(locationId);
        }
    }

    private void createBBU(String locationId) {
        ResourceCatalogClient resourceCatalogClient = new ResourceCatalogClient(env);
        Long deviceModelId = resourceCatalogClient.getModelIds(BBU_MODEL);
        physicalInventoryRepository.createDevice(SITE, Long.valueOf(locationId), deviceModelId, BBU_NAME,
                DEVICE_MODEL_TYPE);
    }

    private boolean isDevicePresent(String locationId) {
        return physicalInventoryRepository.isDevicePresent(locationId, BBU_NAME);
    }
}

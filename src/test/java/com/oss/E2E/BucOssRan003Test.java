package com.oss.E2E;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.comarch.oss.web.pages.NewInventoryViewPage;
import com.comarch.oss.web.pages.SearchObjectTypePage;
import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.alerts.SystemMessageInterface;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.bpm.processinstances.ProcessOverviewPage;
import com.oss.pages.bpm.tasks.TasksPageV2;
import com.oss.pages.platform.HomePage;
import com.oss.pages.radio.CellSiteConfigurationPage;
import com.oss.pages.radio.EditCell4GBulkWizardPage;
import com.oss.repositories.AddressRepository;
import com.oss.repositories.LocationInventoryRepository;
import com.oss.repositories.PhysicalInventoryRepository;
import com.oss.repositories.Radio4gRepository;
import com.oss.services.RadioClient;
import com.oss.services.ResourceCatalogClient;
import com.oss.untils.Environment;

import io.qameta.allure.Description;

public class BucOssRan003Test extends BaseTestCase {

    private static final String LOCATION_NAME = "Denver1";
    private static final String COUNTRY_NAME = "United States";
    private static final String REGION_NAME = "Colorado";
    private static final String DISTRICT_NAME = "District 1";
    private static final String CITY_NAME = "Denver";
    private static final String POSTAL_CODE_NAME = "80014";
    private static final String E_NODE_B_NAME = "Denver42";
    private static final String[] CELL_NAMES = new String[]{"Denver421", "Denver422", "Denver423"};
    private static final String[] CELL_IDS = new String[]{"21", "22", "23"};
    private static final String BPM_AND_PLANNING = "BPM and Planning";
    private static final String PROCESS_OPERATIONS = "Process Operations";
    private static final String TASKS = "Tasks";
    private static final String NAME = "Name";
    private static final String SITE = "Site";
    private final static String SYSTEM_MESSAGE_PATTERN = "%s. Checking system message after %s.";
    private static final String ENODEB_MODEL = "HUAWEI Technology Co.,Ltd BTS5900";
    private static final String CARRIER = "L800-B20-5";
    private static final String MCC_MNC_OPERATOR = "E2ETest";
    private static final String MCC = "999";
    private static final String MNC = "1";
    private static final String BAND_TYPE_NAME = "L800-B20";
    private static final int DL_FREQUENCY_END = 821;
    private static final int DL_FREQUENCY_START = 791;
    private static final int UL_FREQUENCY_END = 862;
    private static final int UL_FREQUENCY_START = 832;
    private static final String CARRIER_NAME = "L800-B20-5";
    private static final int DOWNLINK_CHANNEL = 6175;
    private static final int UPLINK_CHANNEL = 24175;
    private static final int DL_CENTRE_FREQUENCY = 793;
    private static final int UL_CENTRE_FREQUENCY = 834;
    private static final String BBU_NAME = "Denver42_BBU";
    private static final String BBU_MODEL = "BBU5900";
    private static final String DEVICE_MODEL_TYPE = "DeviceModel";
    private final Environment env = Environment.getInstance();
    private final Random r = new Random();
    private String processDCPCode;
    private final String pci = Integer.toString(r.nextInt(503));
    private final String rsi = Integer.toString(r.nextInt(503));
    private SoftAssert softAssert;
    private Radio4gRepository radio4gRepository;
    private PhysicalInventoryRepository physicalInventoryRepository;

    @BeforeClass
    public void openConsole() {
        softAssert = new SoftAssert();
        radio4gRepository = new Radio4gRepository(env);
        physicalInventoryRepository = new PhysicalInventoryRepository(env);
        waitForPageToLoad();
    }

    @Test(priority = 1, description = "Check prerequisites")
    @Description("Check prerequisites")
    public void checkPrereq() {
        String locationId = getOrCreateLocations();
        getOrCreateMccMnc();
        getOrCreateCarrier();
        Long bbuId = getOrCreateBBU(locationId);
        Long eNodeBId = getOrCreateEnodeB(locationId, bbuId);
        waitForPageToLoad();

        getOrCreateCells(eNodeBId, bbuId);
    }

    @Test(priority = 2, description = "Create DCP", dependsOnMethods = {"checkPrereq"})
    @Description("Create new Data Correction Process")
    public void createNewProcess() {
        ProcessOverviewPage processInstancesPage = ProcessOverviewPage.goToProcessOverviewPage(driver, webDriverWait);
        processDCPCode = processInstancesPage.createSimpleDCP();
        waitForPageToLoad();
        closeMessage();
    }

    @Test(priority = 3, description = "Start DCP", dependsOnMethods = {"createNewProcess"})
    @Description("Start newly created Data Correction Process")
    public void startDCP() {
        openView(TASKS, BPM_AND_PLANNING, PROCESS_OPERATIONS);
        waitForPageToLoad();
        TasksPageV2 tasksPage = new TasksPageV2(driver);
        tasksPage.startTask(processDCPCode, "Correct data");
        checkPopup("The task properly assigned.", String.format(SYSTEM_MESSAGE_PATTERN, "Start DCP", "DCP start"));
        waitForPageToLoad();
    }

    @Test(priority = 4, description = "Find location and open it in Cell Site Configuration view", dependsOnMethods = {"startDCP"})
    @Description("Find location in new Inventory View and open location in Cell Site Configuration view")
    public void findLocation() {
        openView("Inventory View", "Resource Inventory");
        waitForPageToLoad();
        SearchObjectTypePage searchObjectTypePage = new SearchObjectTypePage(driver, webDriverWait);
        searchObjectTypePage.searchType(SITE);
        waitForPageToLoad();
        NewInventoryViewPage newInventoryViewPage = new NewInventoryViewPage(driver, webDriverWait);
        newInventoryViewPage.searchObject(LOCATION_NAME);
        waitForPageToLoad();
        newInventoryViewPage.selectRow("name", LOCATION_NAME);
        waitForPageToLoad();
        newInventoryViewPage.callAction(ActionsContainer.SHOW_ON_GROUP_ID, "Cell Site Configuration");
        waitForPageToLoad();
    }

    @Test(priority = 5, description = "Modify cells", dependsOnMethods = {"findLocation"})
    @Description("Modify Cells 4G parameters in bulk wizard")
    public void modifyCell4Gparameters() {
        CellSiteConfigurationPage cellSiteConfigurationPage = new CellSiteConfigurationPage(driver);
        cellSiteConfigurationPage.expandTreeToBaseStation(SITE, LOCATION_NAME, E_NODE_B_NAME);
        waitForPageToLoad();
        cellSiteConfigurationPage.selectTab("Cells 4G");
        waitForPageToLoad();
        cellSiteConfigurationPage.clearColumnFilter(NAME);
        waitForPageToLoad();

        for (int i = 0; i < 3; i++) {
            waitForPageToLoad();
            cellSiteConfigurationPage.selectRowByAttributeValueWithLabel(NAME, CELL_NAMES[i]);
        }
        waitForPageToLoad();
        cellSiteConfigurationPage.clickEditIcon();
        EditCell4GBulkWizardPage editCell4GBulkWizardPage = new EditCell4GBulkWizardPage(driver);
        waitForPageToLoad();
        editCell4GBulkWizardPage.setPCIBulk(pci);
        waitForPageToLoad();
        editCell4GBulkWizardPage.setRSIBulk(rsi);
        waitForPageToLoad();
        editCell4GBulkWizardPage.accept();
        checkPopup("Cells 4G updated successfully", String.format(SYSTEM_MESSAGE_PATTERN, "Modify cell 4G parameters", "cell 4G bulk wizard close"));
    }

    @Test(priority = 6, description = "Finish DCP", dependsOnMethods = {"modifyCell4Gparameters"})
    @Description("Finish Data Correction Process")
    public void finishDCP() {
        openView(TASKS, BPM_AND_PLANNING, PROCESS_OPERATIONS);
        waitForPageToLoad();
        TasksPageV2 tasksPage = new TasksPageV2(driver);
        tasksPage.completeTask(processDCPCode, "Correct data");
        checkPopup("Task properly completed.", String.format(SYSTEM_MESSAGE_PATTERN, "Finish DCP", "DCP finish"));
        waitForPageToLoad();
    }

    @Test(priority = 7, description = "Checking system message summary")
    @Description("Checking system message summary")
    public void systemMessageSummary() {
        softAssert.assertAll();
    }

    private void openView(String actionLabel, String... path) {
        waitForPageToLoad();
        HomePage homePage = new HomePage(driver);
        homePage.goToHomePage(driver, BASIC_URL);
        waitForPageToLoad();
        homePage.chooseFromLeftSideMenu(actionLabel, path);
    }

    private void checkPopup(String text, String systemMessageLog) {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        softAssert.assertEquals(messages.size(), 1, systemMessageLog);
        softAssert.assertEquals(messages.get(0).getMessageType(), SystemMessageContainer.MessageType.SUCCESS, systemMessageLog);
        String message = messages.get(0).getText();
        softAssert.assertTrue(message.contains(text), systemMessageLog + ". " + message);
        systemMessage.close();
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
        return radio4gRepository.getOrCreateBandType(BAND_TYPE_NAME, DL_FREQUENCY_START, DL_FREQUENCY_END, UL_FREQUENCY_START, UL_FREQUENCY_END);
    }

    private void getOrCreateCarrier() {
        radio4gRepository.getOrCreateCarrier(CARRIER_NAME, DOWNLINK_CHANNEL, UPLINK_CHANNEL, DL_CENTRE_FREQUENCY, UL_CENTRE_FREQUENCY, getOrCreateBandType());
    }

    private Long getOrCreateEnodeB(String locationId, Long bbuId) {
        return radio4gRepository.getOrCreateENodeB(E_NODE_B_NAME, Long.valueOf(locationId), MCC, MNC, ENODEB_MODEL, bbuId);
    }

    private void getOrCreateCells(Long eNodeBId, Long bbuId) {
        List<Long> cellXids = new java.util.ArrayList<>(Collections.emptyList());
        if (radio4gRepository.getCell4GIdsByENodeBId(eNodeBId).isEmpty()) {
            int i = 0;
            for (String cellName : CELL_NAMES) {
                cellXids.add(radio4gRepository.createCell4gWithDefaultValues(cellName, Integer.parseInt(CELL_IDS[i]), eNodeBId, MCC, MNC, CARRIER, Integer.parseInt(CELL_IDS[i])));
                i++;
            }
            DelayUtils.sleep(1500);
            for (Long cellXid : cellXids) {
                radio4gRepository.createHRCellDevice(bbuId, eNodeBId, cellXid);
            }
        }
    }

    private Long getOrCreateBBU(String locationId) {
        ResourceCatalogClient resourceCatalogClient = new ResourceCatalogClient(env);
        Long deviceModelId = resourceCatalogClient.getModelIds(BBU_MODEL);
        return physicalInventoryRepository.getOrCreateDevice(SITE, Long.valueOf(locationId), deviceModelId, BBU_NAME,
                DEVICE_MODEL_TYPE);
    }
}

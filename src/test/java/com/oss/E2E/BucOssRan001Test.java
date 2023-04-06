package com.oss.E2E;

import java.time.Duration;
import java.util.Optional;

import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.comarch.oss.web.pages.HomePage;
import com.comarch.oss.web.pages.NewInventoryViewPage;
import com.comarch.oss.web.pages.SearchObjectTypePage;
import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.alerts.SystemMessageContainer.Message;
import com.oss.framework.components.alerts.SystemMessageInterface;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.mainheader.PerspectiveChooser;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.bpm.planning.ProcessDetailsPage;
import com.oss.pages.bpm.processinstances.ProcessOverviewPage;
import com.oss.pages.bpm.tasks.TasksPageV2;
import com.oss.pages.radio.CellSiteConfigurationPage;
import com.oss.repositories.AddressRepository;
import com.oss.repositories.LocationInventoryRepository;
import com.oss.repositories.Radio4gRepository;
import com.oss.services.RadioClient;
import com.oss.untils.Environment;

import io.qameta.allure.Description;

public class BucOssRan001Test extends BaseTestCase {

    private static final String LOCATION_NAME = "Denver1";
    private static final String ENODEB_NAME = "Denver41";
    private static final String ENODEB_ID = "141";
    private static final String ENODEB_MODEL = "HUAWEI Technology Co.,Ltd BTS5900";
    private static final String BASE_BAND_UNIT_MODEL = "HUAWEI Technology Co.,Ltd BBU5900";
    private static final String BBU_NAME = "BTS5900,Denver1/0/BBU5900,0";
    private static final String RADIO_UNIT_MODEL = "HUAWEI Technology Co.,Ltd RRU5501";
    private static final String[] RADIO_UNIT_NAMES = {"BTS5900,Denver1/0/MRRU,80", "BTS5900,Denver1/0/MRRU,81", "BTS5900,Denver1/0/MRRU,82"};
    private static final String RAN_ANTENNA_MODEL = "HUAWEI Technology Co.,Ltd APE4518R14V06";
    private static final String[] ANTENNA_NAMES = {"ANT-1", "ANT-2", "ANT-3"};
    private static final String[] ANTENNA_ARRAYS_1 = {"ANT-1/APE4518R14V06_Ly1/Freq(1695-2690)", "ANT-1/APE4518R14V06_Ry2/Freq(1695-2690)"};
    private static final String[] ANTENNA_ARRAYS_2 = {"ANT-2/APE4518R14V06_Ly1/Freq(1695-2690)", "ANT-2/APE4518R14V06_Ry2/Freq(1695-2690)"};
    private static final String[] ANTENNA_ARRAYS_3 = {"ANT-3/APE4518R14V06_Ly1/Freq(1695-2690)", "ANT-3/APE4518R14V06_Ry2/Freq(1695-2690)"};
    private static final String BBU_EQUIPMENT_TYPE = "Base Band Unit";
    private static final String RADIO_UNIT_EQUIPMENT_TYPE = "Remote Radio Head/Unit";
    private static final String[] CELL_NAMES = new String[]{"Denver411", "Denver412", "Denver413"};
    private static final int AMOUNT_OF_CELLS = CELL_NAMES.length;
    private static final String[] PCI = {"100", "101", "102"};
    private static final String[] RSI = {"150", "160", "170"};
    private static final String REFERENCE_POWER = "15.2";
    private static final String[] TAC = {"500", "500", "500"};
    private static final int[] LOCAL_CELLS_ID = {0, 1, 2};
    private static final int CRP = 2;
    private static final String MIMO_MODE = "2Tx2Rx";
    private static final String BANDWITH = "15";
    private static final String TX_POWER = "44.7";
    private static final String TASK_COMPLETED = "Task properly completed.";
    private static final String TASK_ASSIGNED = "The task properly assigned.";
    private static final String SITE = "Site";
    private static final String NAME = "Name";
    private static final String MANUFACTURER = "HUAWEI Technology Co.,Ltd";
    private final static String SYSTEM_MESSAGE_PATTERN = "%s. Checking system message after %s.";
    private static final String NRP = "Network Resource Process";
    private static final String COUNTRY_NAME = "United States";
    private static final String REGION_NAME = "Colorado";
    private static final String DISTRICT_NAME = "District 1";
    private static final String CITY_NAME = "Denver";
    private static final String POSTAL_CODE_NAME = "80014";
    private static final String MCCMNC_PRIMARY = "E2ETest [mcc: 999, mnc: 1]";
    private static final String CARRIER = "L800-B20-5 (6175)";
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
    private static final String VALIDATION_RESULT_NOT_PRESENT_EXCEPTION = "Validation result is not present.";
    private final Environment env = Environment.getInstance();

    private SoftAssert softAssert;
    private CellSiteConfigurationPage cellSiteConfigurationPage;
    private Radio4gRepository radio4gRepository;
    private String processNRPCode;
    private String processIPCode;

    @BeforeClass
    public void openConsole() {
        waitForPageToLoad();
        softAssert = new SoftAssert();
        radio4gRepository = new Radio4gRepository(env);
    }

    @Test(priority = 1, description = "Check prerequisites")
    @Description("Check prerequisites")
    public void checkPrereq() {
        getOrCreateLocations();
        getOrCreateMccMnc();
        getOrCreateCarrier();
    }

    @Test(priority = 2, description = "Create and start NRP Process", dependsOnMethods = {"checkPrereq"})
    @Description("Create and start NRP Process")
    public void createProcessNRP() {
        ProcessOverviewPage processInstancesPage = ProcessOverviewPage.goToProcessOverviewPage(driver, webDriverWait);
        processNRPCode = processInstancesPage.createProcessIPD(LOCATION_NAME, 0L, NRP);
        closeMessage();
    }

    @Test(priority = 3, description = "Start HLP task", dependsOnMethods = {"createProcessNRP"})
    @Description("Start High Level Planning task")
    public void startHLP() {
        TasksPageV2 tasksPage = TasksPageV2.goToTasksPage(driver, webDriverWait, BASIC_URL);
        waitForPageToLoad();
        tasksPage.startTask(processNRPCode, TasksPageV2.HIGH_LEVEL_PLANNING_TASK);
        checkTaskAssignment(String.format(SYSTEM_MESSAGE_PATTERN, "Start HLP", "HLP start"));
        waitForPageToLoad();
    }

    @Test(priority = 4, description = "Find location and open it in Cell Site Configuration view", dependsOnMethods = {"startHLP"})
    @Description("Find location in new Inventory View and open location in Cell Site Configuration view")
    public void findLocation() {
        openCellSiteConfiguration();
    }

    @Test(priority = 5, description = "Create eNodeB", dependsOnMethods = {"findLocation"})
    @Description("Create eNodeB")
    public void createENodeB() {
        waitForPageToLoad();
        cellSiteConfigurationPage = new CellSiteConfigurationPage(driver);
        cellSiteConfigurationPage.createENodeB(ENODEB_NAME, ENODEB_ID, ENODEB_MODEL, MCCMNC_PRIMARY);
        checkMessageText("ENodeB was created", String.format(SYSTEM_MESSAGE_PATTERN, "Create eNodeB", "eNodeB create"));
    }

    @Test(priority = 6, description = "Create Cell4G with Bulk Wizard", dependsOnMethods = {"createENodeB"})
    @Description("Create Cell4G with Bulk Wizard")
    public void createCell4GBulk() {
        waitForPageToLoad();
        cellSiteConfigurationPage.expandTreeToBaseStation(SITE, LOCATION_NAME, ENODEB_NAME);
        cellSiteConfigurationPage.createCell4GBulk(AMOUNT_OF_CELLS, CARRIER, CELL_NAMES, LOCAL_CELLS_ID, CRP);
        checkMessageText("Cells 4G created success", String.format(SYSTEM_MESSAGE_PATTERN, "Create cell 5G bulk", "cell 4G create"));
        checkMessageType(String.format(SYSTEM_MESSAGE_PATTERN, "Create cell 5G bulk", "cell 4G create"));
    }

    @Test(priority = 7, description = "Create Base Band Unit", dependsOnMethods = {"createCell4GBulk"})
    @Description("Create Base Band Unit")
    public void createBaseBandUnit() {
        waitForPageToLoad();
        cellSiteConfigurationPage.selectTreeRow(LOCATION_NAME);
        cellSiteConfigurationPage.createBaseBandUnit(BBU_EQUIPMENT_TYPE, BASE_BAND_UNIT_MODEL, BBU_NAME, LOCATION_NAME);
        checkMessageType(String.format(SYSTEM_MESSAGE_PATTERN, "Create base band unit", "base band unit create"));
        waitForPageToLoad();
    }

    @Test(priority = 8, description = "Create three Radio Units", dependsOnMethods = {"createBaseBandUnit"})
    @Description("Create three Radio Units")
    public void createRadioUnit() {
        for (int i = 0; i < 3; i++) {
            cellSiteConfigurationPage.createRadioUnit(RADIO_UNIT_EQUIPMENT_TYPE, RADIO_UNIT_MODEL, RADIO_UNIT_NAMES[i], LOCATION_NAME);
            checkMessageType(String.format(SYSTEM_MESSAGE_PATTERN, "Create radio unit", "radio unit create"));
            waitForPageToLoad();
        }
    }

    @Test(priority = 9, description = "Create three RAN Antennas", dependsOnMethods = {"createRadioUnit"})
    @Description("Create three RAN Antennas")
    public void createRanAntennaAndArray() {
        for (int i = 0; i < 3; i++) {
            cellSiteConfigurationPage.createRanAntennaAndArray(ANTENNA_NAMES[i], RAN_ANTENNA_MODEL, LOCATION_NAME);
            checkMessageType(String.format(SYSTEM_MESSAGE_PATTERN, "Create ran antenna and array", "ran antenna and array create"));
            waitForPageToLoad();
        }
    }

    @Test(priority = 10, description = "Create hosting relation between eNodeB and BBU", dependsOnMethods = {"createENodeB", "createBaseBandUnit"})
    @Description("Create hosting relation between eNodeB and BBU")
    public void hostENodeBOnBBU() {
        waitForPageToLoad();
        cellSiteConfigurationPage.selectTreeRow(ENODEB_NAME);
        waitForPageToLoad();
        cellSiteConfigurationPage.createHostingOnDevice(BBU_NAME, true);
        checkMessageType(String.format(SYSTEM_MESSAGE_PATTERN, "Host eNodeB on BBU", "hosting on device create"));
        waitForPageToLoad();
    }

    @Test(priority = 11, description = "Create hosting relation between Cell 4G and RRU", dependsOnMethods = {"createCell4GBulk", "createRadioUnit"})
    @Description("Create hosting relation between Cell 4G and RRU")
    public void hostCell4GOnRRU() {
        for (int i = 0; i < 3; i++) {
            cellSiteConfigurationPage.selectTreeRow(CELL_NAMES[i]);
            waitForPageToLoad();
            cellSiteConfigurationPage.createHostingOnDevice(RADIO_UNIT_NAMES[i]);
            checkMessageType(String.format(SYSTEM_MESSAGE_PATTERN, "Host cell 4G on RRU", "hosting on device create"));
            waitForPageToLoad();
        }
    }

    @Test(priority = 12, description = "Create hosting relation between Cell 4G and RAN Antenna Array", dependsOnMethods = {"createCell4GBulk", "createRanAntennaAndArray"})
    @Description("Create hosting relation between Cell 4G and RAN Antenna Array")
    public void hostCell4GOnRANAntennaArray() {
        cellSiteConfigurationPage.selectTreeRow(CELL_NAMES[0]);
        waitForPageToLoad();
        cellSiteConfigurationPage.createHostingOnAntennaArraysContains(ANTENNA_ARRAYS_1);
        checkMessageType(String.format(SYSTEM_MESSAGE_PATTERN, "Host cell 4G on ran antenna array", "hosting on antenna array 1 create"));
        waitForPageToLoad();
        cellSiteConfigurationPage.selectTreeRow(CELL_NAMES[1]);
        waitForPageToLoad();
        cellSiteConfigurationPage.createHostingOnAntennaArraysContains(ANTENNA_ARRAYS_2);
        checkMessageType(String.format(SYSTEM_MESSAGE_PATTERN, "Host cell 4G on ran antenna array", "hosting on antenna array 2 create"));
        waitForPageToLoad();
        cellSiteConfigurationPage.selectTreeRow(CELL_NAMES[2]);
        waitForPageToLoad();
        cellSiteConfigurationPage.createHostingOnAntennaArraysContains(ANTENNA_ARRAYS_3);
        checkMessageType(String.format(SYSTEM_MESSAGE_PATTERN, "Host cell 4G on ran antenna array", "hosting on antenna array 3 create"));
    }

    @Test(priority = 13, description = "Finish HLP task", dependsOnMethods = {"hostCell4GOnRANAntennaArray"})
    @Description("Finish High Level Planning task")
    public void finishHLP() {
        waitForPageToLoad();
        TasksPageV2 tasksPage = TasksPageV2.goToTasksPage(driver, webDriverWait, BASIC_URL);
        waitForPageToLoad();
        tasksPage.completeTask(processNRPCode, TasksPageV2.HIGH_LEVEL_PLANNING_TASK);
        checkTaskCompleted(String.format(SYSTEM_MESSAGE_PATTERN, "Finish HLP", "complete high level planning task"));
    }

    @Test(priority = 14, description = "Start LLP task", dependsOnMethods = {"finishHLP"})
    @Description("Start Low Level Planning")
    public void startLLP() {
        TasksPageV2 tasksPage = TasksPageV2.goToTasksPage(driver, webDriverWait, BASIC_URL);
        waitForPageToLoad();
        tasksPage.startTask(processNRPCode, TasksPageV2.LOW_LEVEL_PLANNING_TASK);
        checkTaskAssignment(String.format(SYSTEM_MESSAGE_PATTERN, "Start LLP", "start low level planning task"));
        waitForPageToLoad();
    }

    @Test(priority = 15, description = "Check validation results", dependsOnMethods = {"startLLP"})
    @Description("Check validation results")
    public void validateProjectPlan() {
        TasksPageV2 tasksPage = TasksPageV2.goToTasksPage(driver, webDriverWait, BASIC_URL);
        waitForPageToLoad();
        tasksPage.findTask(processNRPCode, TasksPageV2.LOW_LEVEL_PLANNING_TASK);
        waitForPageToLoad();
        tasksPage.clickPlanViewButton();
        waitForPageToLoad();
        ProcessDetailsPage processDetailsPage = new ProcessDetailsPage(driver);
        processDetailsPage.selectTab("Validation Results");
        Assert.assertTrue(processDetailsPage.isValidationResultPresent(), VALIDATION_RESULT_NOT_PRESENT_EXCEPTION);
    }

    @Test(priority = 16, description = "Complete cells configuration", dependsOnMethods = {"validateProjectPlan"})
    @Description("Complete cells configuration")
    public void lowLevelLogicalDesign() {
        ProcessDetailsPage processDetailsPage = new ProcessDetailsPage(driver);
        processDetailsPage.closeProcessDetailsPromt();
        openCellSiteConfiguration();
        CellSiteConfigurationPage cellSiteConfigurationPage = new CellSiteConfigurationPage(driver);
        cellSiteConfigurationPage.expandTreeToBaseStation(SITE, LOCATION_NAME, ENODEB_NAME);
        cellSiteConfigurationPage.getTabTable().clearColumnValue(NAME);
        for (int i = 0; i < 3; i++) {
            cellSiteConfigurationPage.selectRowByAttributeValueWithLabel(NAME, CELL_NAMES[i]);
        }
        waitForPageToLoad();
        cellSiteConfigurationPage.editCellsInBulk(PCI, RSI, REFERENCE_POWER, TAC, MIMO_MODE, BANDWITH, TX_POWER);
    }

    @Test(priority = 17, description = "Finish LLP task", dependsOnMethods = {"lowLevelLogicalDesign"})
    @Description("Finish Low Level Planning task")
    public void finishLowLevelPlanningTask() {
        waitForPageToLoad();
        PerspectiveChooser perspectiveChooser = PerspectiveChooser.create(driver, webDriverWait);
        perspectiveChooser.setCurrentTask();
        waitForPageToLoad();
        TasksPageV2 tasksPage = TasksPageV2.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.completeTask(processNRPCode, TasksPageV2.LOW_LEVEL_PLANNING_TASK);
        checkTaskCompleted(String.format(SYSTEM_MESSAGE_PATTERN, "Finish low level planning task", "complete low level planning task"));
        tasksPage.startTask(processNRPCode, TasksPageV2.READY_FOR_INTEGRATION_TASK);
        processIPCode = tasksPage.proceedNRPFromReadyForIntegration(processNRPCode);
    }

    @Test(priority = 18, description = "Finish NRP and IP", dependsOnMethods = {"finishLowLevelPlanningTask"})
    @Description("Finish NRP and IP")
    public void completeProcessNRP() {
        TasksPageV2 tasksPage = TasksPageV2.goToTasksPage(driver, webDriverWait, BASIC_URL);
        waitForPageToLoad();
        tasksPage.completeTask(processIPCode, TasksPageV2.IMPLEMENTATION_TASK);
        checkTaskCompleted(String.format(SYSTEM_MESSAGE_PATTERN, "Complete process NRP", "complete implementation task"));
        tasksPage.startTask(processIPCode, TasksPageV2.ACCEPTANCE_TASK);
        checkTaskAssignment(String.format(SYSTEM_MESSAGE_PATTERN, "Complete process NRP", "start acceptance task"));
        tasksPage.completeTask(processIPCode, TasksPageV2.ACCEPTANCE_TASK);
        checkTaskCompleted(String.format(SYSTEM_MESSAGE_PATTERN, "Complete process NRP", "complete acceptance task"));
        tasksPage.startTask(processNRPCode, TasksPageV2.VERIFICATION_TASK);
        checkTaskAssignment(String.format(SYSTEM_MESSAGE_PATTERN, "Complete process NRP", "start verification task"));
        tasksPage.completeTask(processNRPCode, TasksPageV2.VERIFICATION_TASK);
        checkTaskCompleted(String.format(SYSTEM_MESSAGE_PATTERN, "Complete process NRP", "complete verification task"));
    }

    @Test(priority = 19, description = "Delete eNodeB", dependsOnMethods = {"createENodeB"})
    @Description("Delete eNodeB")
    public void deleteENodeB() {
        openCellSiteConfiguration();
        waitForPageToLoad();
        cellSiteConfigurationPage.removeBaseStation(NAME, ENODEB_NAME);
    }

    @Test(priority = 20, description = "Delete antennas, BBU, RRU", dependsOnMethods = {"createBaseBandUnit", "createRadioUnit"})
    @Description("Delete antennas, BBU, RRU")
    public void deleteDevices() {
        waitForPageToLoad();
        cellSiteConfigurationPage.removeDevice("Base Band Units", MANUFACTURER, BBU_NAME);
        checkMessageType(String.format(SYSTEM_MESSAGE_PATTERN, "Delete devices", "BBU delete"));
        waitForPageToLoad();
        for (int i = 0; i < 3; i++) {
            cellSiteConfigurationPage.removeDevice("Remote Radio Units", MANUFACTURER, RADIO_UNIT_NAMES[i]);
            checkMessageType(String.format(SYSTEM_MESSAGE_PATTERN, "Delete devices", "RRU delete"));
            waitForPageToLoad();
            cellSiteConfigurationPage.removeDevice("Antennas", MANUFACTURER, ANTENNA_NAMES[i]);
            checkMessageType(String.format(SYSTEM_MESSAGE_PATTERN, "Delete devices", "antenna delete"));
            waitForPageToLoad();
        }
    }

    @Test(priority = 21, description = "Checking system message summary")
    @Description("Checking system message summary")
    public void systemMessageSummary() {
        softAssert.assertAll();
    }

    private void openCellSiteConfiguration() {
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

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    private void checkMessageContainsText(String systemMessageLog) {
        String message = getFirstMessage().getText();
        softAssert.assertTrue(message.contains(BucOssRan001Test.TASK_COMPLETED), systemMessageLog + ". " + message);
    }

    private void checkMessageText(String message, String systemMessageLog) {
        softAssert.assertEquals((getFirstMessage().getText()), message, systemMessageLog);
    }

    private Message getFirstMessage() {
        return SystemMessageContainer.create(driver, webDriverWait)
                .getFirstMessage()
                .orElseThrow(() -> new RuntimeException("The list is empty"));
    }

    private void checkTaskAssignment(String systemMessageLog) {
        checkMessageText(TASK_ASSIGNED, systemMessageLog);
        checkMessageType(systemMessageLog);
    }

    private void checkTaskCompleted(String systemMessageLog) {
        checkMessageContainsText(systemMessageLog);
        checkMessageType(systemMessageLog);
    }

    private void checkMessageType(String systemMessageLog) {
        SystemMessageInterface systemMessage = getSuccesSystemMessage(systemMessageLog);
        systemMessage.close();
        waitForPageToLoad();
    }

    private SystemMessageInterface getSuccesSystemMessage(String systemMessageLog) {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, new WebDriverWait(driver, Duration.ofSeconds(90)));
        Optional<Message> firstSystemMessage = systemMessage.getFirstMessage();
        softAssert.assertTrue(firstSystemMessage.isPresent(), systemMessageLog);
        firstSystemMessage.ifPresent(message -> {
            softAssert.assertEquals(message.getMessageType(), SystemMessageContainer.MessageType.SUCCESS, systemMessageLog);
        });
        return systemMessage;
    }

    private void closeMessage() {
        SystemMessageContainer.create(driver, webDriverWait).close();
        waitForPageToLoad();
    }

    private void getOrCreateLocations() {
        LocationInventoryRepository locationInventoryRepository = new LocationInventoryRepository(env);
        locationInventoryRepository.getOrCreateLocation(LOCATION_NAME, SITE, prepareAddress());
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
}

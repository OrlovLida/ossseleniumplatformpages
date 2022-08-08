package com.oss.E2E;

import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.alerts.SystemMessageContainer.Message;
import com.oss.framework.components.alerts.SystemMessageContainer.MessageType;
import com.oss.framework.components.alerts.SystemMessageInterface;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.mainheader.PerspectiveChooser;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.bpm.PlanViewWizardPage;
import com.oss.pages.bpm.ProcessOverviewPage;
import com.oss.pages.bpm.TasksPageV2;
import com.oss.pages.platform.HomePage;
import com.oss.pages.platform.NewInventoryViewPage;
import com.oss.pages.platform.SearchObjectTypePage;
import com.oss.pages.radio.CellSiteConfigurationPage;

import io.qameta.allure.Description;

public class TP_OSS_RM_RAN_001_Test extends BaseTestCase {

    private static final String LOCATION_NAME = "XYZ_SeleniumTests";
    private static final String ENODEB_NAME = "GBM055TST";
    private static final String ENODEB_ID = "1" + (int) (Math.random() * 10);
    private static final String ENODEB_MODEL = "HUAWEI Technology Co.,Ltd BTS5900";
    private static final String MCCMNC_PRIMARY = "E2ETests [mcc: 0001, mnc: 01]";
    private static final String BASE_BAND_UNIT_MODEL = "HUAWEI Technology Co.,Ltd BBU5900";
    private static final String BBU_NAME = "BTS5900,GBM055TST/0/BBU5900,0";
    private static final String RADIO_UNIT_MODEL = "HUAWEI Technology Co.,Ltd RRU5301";
    private static final String[] RADIO_UNIT_NAMES = {"BTS5900,GBM055TST/0/MRRU,60", "BTS5900,GBM055TST/0/MRRU,70", "BTS5900,GBM055TST/0/MRRU,80"};
    private static final String RAN_ANTENNA_MODEL = "HUAWEI Technology Co.,Ltd APE4518R14V06";
    private static final String[] ANTENNA_NAMES = {"TP_OSS_RM_RAN_001_ANTENNA_1", "TP_OSS_RM_RAN_001_ANTENNA_2", "TP_OSS_RM_RAN_001_ANTENNA_3"};
    private static final String BBU_EQUIPMENT_TYPE = "Base Band Unit";
    private static final String RADIO_UNIT_EQUIPMENT_TYPE = "Remote Radio Head/Unit";
    private static final String CARRIER = "E2E Carrier (11)";
    private static final String[] CELL_NAMES = new String[]{"TP_OSS_RM_RAN_001_CELL10", "TP_OSS_RM_RAN_001_CELL20", "TP_OSS_RM_RAN_001_CELL30"};
    private static final int AMOUNT_OF_CELLS = CELL_NAMES.length;
    private static final String PCI = "2";
    private static final String RSI = "2";
    private static final String REFERENCE_POWER = "0";
    private static final String[] TAC = {"2", "2", "2"};
    private static final int[] LOCAL_CELLS_ID = {1, 2, 3};
    private static final int CRP = 2;
    private static final String PA_INPUT = "2";
    private static final String TASK_COMPLETED = "Task properly completed.";
    private static final String TASK_ASSIGNED = "The task properly assigned.";
    private static final String SITE = "Site";
    private static final String NAME = "Name";
    private static final String MANUFACTURER = "HUAWEI Technology Co.,Ltd";
    private SoftAssert softAssert;

    private CellSiteConfigurationPage cellSiteConfigurationPage;
    private String processNRPCode;
    private String processIPCode;

    @BeforeClass
    public void openConsole() {
        waitForPageToLoad();
        softAssert = new SoftAssert();
    }

    @Test(priority = 1, description = "Create and start NRP Process")
    @Description("Create and start NRP Process")
    public void createProcessNRP() {
        ProcessOverviewPage processInstancesPage = ProcessOverviewPage.goToProcessOverviewPage(driver, webDriverWait);
        processNRPCode = processInstancesPage.createSimpleNRP();
        closeMessage();
    }

    @Test(priority = 2, description = "Start HLP task", dependsOnMethods = {"createProcessNRP"})
    @Description("Start High Level Planning task")
    public void startHLP() {
        TasksPageV2 tasksPage = TasksPageV2.goToTasksPage(driver, webDriverWait, BASIC_URL);
        waitForPageToLoad();
        tasksPage.startTask(processNRPCode, TasksPageV2.HIGH_LEVEL_PLANNING_TASK);
        checkTaskAssignment();
        waitForPageToLoad();
    }

    @Test(priority = 3, description = "Find location and open it in Cell Site Configuration view", dependsOnMethods = {"startHLP"})
    @Description("Find location in new Inventory View and open location in Cell Site Configuration view")
    public void findLocation() {
        openCellSiteConfiguration();
    }

    @Test(priority = 4, description = "Create eNodeB", dependsOnMethods = {"findLocation"})
    @Description("Create eNodeB")
    public void createENodeB() {
        waitForPageToLoad();
        cellSiteConfigurationPage = new CellSiteConfigurationPage(driver);
        cellSiteConfigurationPage.createENodeB(ENODEB_NAME, ENODEB_ID, ENODEB_MODEL, MCCMNC_PRIMARY);
        checkMessageText("ENodeB was created");
    }

    @Test(priority = 5, description = "Create Cell4G with Bulk Wizard", dependsOnMethods = {"createENodeB"})
    @Description("Create Cell4G with Bulk Wizard")
    public void createCell4GBulk() {
        waitForPageToLoad();
        cellSiteConfigurationPage.expandTreeToBaseStation(SITE, LOCATION_NAME, ENODEB_NAME);
        cellSiteConfigurationPage.createCell4GBulk(AMOUNT_OF_CELLS, CARRIER, CELL_NAMES, LOCAL_CELLS_ID, CRP);
        checkMessageText("Cells 4G created success");
        checkMessageType();
    }

    @Test(priority = 6, description = "Create Base Band Unit", dependsOnMethods = {"createCell4GBulk"})
    @Description("Create Base Band Unit")
    public void createBaseBandUnit() {
        waitForPageToLoad();
        cellSiteConfigurationPage.selectTreeRow(LOCATION_NAME);
        cellSiteConfigurationPage.createBaseBandUnit(BBU_EQUIPMENT_TYPE, BASE_BAND_UNIT_MODEL, BBU_NAME, LOCATION_NAME);
        checkMessageType();
        waitForPageToLoad();
    }

    @Test(priority = 7, description = "Create three Radio Units", dependsOnMethods = {"createBaseBandUnit"})
    @Description("Create three Radio Units")
    public void createRadioUnit() {
        for (int i = 0; i < 3; i++) {
            cellSiteConfigurationPage.createRadioUnit(RADIO_UNIT_EQUIPMENT_TYPE, RADIO_UNIT_MODEL, RADIO_UNIT_NAMES[i], LOCATION_NAME);
            checkMessageType();
            waitForPageToLoad();
        }
    }

    @Test(priority = 8, description = "Create three RAN Antennas", dependsOnMethods = {"createRadioUnit"})
    @Description("Create three RAN Antennas")
    public void createRanAntennaAndArray() {
        for (int i = 0; i < 3; i++) {
            cellSiteConfigurationPage.createRanAntennaAndArray(ANTENNA_NAMES[i], RAN_ANTENNA_MODEL, LOCATION_NAME);
            checkMessageType();
            waitForPageToLoad();
        }
    }

    @Test(priority = 9, description = "Create hosting relation between eNodeB and BBU", dependsOnMethods = {"createENodeB", "createBaseBandUnit"})
    @Description("Create hosting relation between eNodeB and BBU")
    public void hostENodeBOnBBU() {
        waitForPageToLoad();
        cellSiteConfigurationPage.selectTreeRow(ENODEB_NAME);
        waitForPageToLoad();
        cellSiteConfigurationPage.createHostingOnDevice(BBU_NAME, true);
        checkMessageType();
        waitForPageToLoad();
    }

    @Test(priority = 10, description = "Create hosting relation between Cell 4G and RRU", dependsOnMethods = {"createCell4GBulk", "createRadioUnit"})
    @Description("Create hosting relation between Cell 4G and RRU")
    public void hostCell4GOnRRU() {
        for (int i = 0; i < 3; i++) {
            cellSiteConfigurationPage.selectTreeRow(CELL_NAMES[i]);
            waitForPageToLoad();
            cellSiteConfigurationPage.createHostingOnDevice(RADIO_UNIT_NAMES[i]);
            checkMessageType();
            waitForPageToLoad();
        }
    }

    @Test(priority = 11, description = "Create hosting relation between Cell 4G and RAN Antenna Array", dependsOnMethods = {"createCell4GBulk", "createRanAntennaAndArray"})
    @Description("Create hosting relation between Cell 4G and RAN Antenna Array")
    public void hostCell4GOnRANAntennaArray() {
        for (int i = 0; i < 3; i++) {
            waitForPageToLoad();
            cellSiteConfigurationPage.selectTreeRow(CELL_NAMES[i]);
            waitForPageToLoad();
            cellSiteConfigurationPage.createHostingOnAntennaArray(ANTENNA_NAMES[i]);
            checkMessageType();
            waitForPageToLoad();
        }
    }

    @Test(priority = 12, description = "Finish HLP task", dependsOnMethods = {"hostCell4GOnRANAntennaArray"})
    @Description("Finish High Level Planning task")
    public void finishHLP() {
        waitForPageToLoad();
        TasksPageV2 tasksPage = TasksPageV2.goToTasksPage(driver, webDriverWait, BASIC_URL);
        waitForPageToLoad();
        tasksPage.completeTask(processNRPCode, TasksPageV2.HIGH_LEVEL_PLANNING_TASK);
        checkTaskCompleted();
    }

    @Test(priority = 13, description = "Start LLP task", dependsOnMethods = {"finishHLP"})
    @Description("Start Low Level Planning")
    public void startLLP() {
        TasksPageV2 tasksPage = TasksPageV2.goToTasksPage(driver, webDriverWait, BASIC_URL);
        waitForPageToLoad();
        tasksPage.startTask(processNRPCode, TasksPageV2.LOW_LEVEL_PLANNING_TASK);
        checkTaskAssignment();
        waitForPageToLoad();
    }

    @Test(priority = 14, description = "Check validation results", dependsOnMethods = {"startLLP"})
    @Description("Check validation results")
    public void validateProjectPlan() {
        TasksPageV2 tasksPage = TasksPageV2.goToTasksPage(driver, webDriverWait, BASIC_URL);
        waitForPageToLoad();
        tasksPage.findTask(processNRPCode, TasksPageV2.LOW_LEVEL_PLANNING_TASK);
        waitForPageToLoad();
        tasksPage.clickPlanViewButton();
        waitForPageToLoad();
        PlanViewWizardPage planViewWizardPage = new PlanViewWizardPage(driver);
        planViewWizardPage.selectTab("Validation Results");
        Assert.assertTrue(planViewWizardPage.isValidationResultPresent());
    }

    @Test(priority = 15, description = "Complete cells configuration", dependsOnMethods = {"validateProjectPlan"})
    @Description("Complete cells configuration")
    public void lowLevelLogicalDesign() {
        PlanViewWizardPage planViewWizardPage = new PlanViewWizardPage(driver);
        planViewWizardPage.closeProcessDetailsPromt();
        openCellSiteConfiguration();
        CellSiteConfigurationPage cellSiteConfigurationPage = new CellSiteConfigurationPage(driver);
        cellSiteConfigurationPage.expandTreeToBaseStation(SITE, LOCATION_NAME, ENODEB_NAME);
        cellSiteConfigurationPage.getTabTable().clearColumnValue(NAME);
        for (int i = 0; i < 3; i++) {
            waitForPageToLoad();
            cellSiteConfigurationPage.selectRowByAttributeValueWithLabel(NAME, CELL_NAMES[i]);
        }
        waitForPageToLoad();
        cellSiteConfigurationPage.editCellsInBulk(AMOUNT_OF_CELLS, PCI, RSI, REFERENCE_POWER, TAC, PA_INPUT);
    }

    @Test(priority = 16, description = "Finish LLP task", dependsOnMethods = {"lowLevelLogicalDesign"})
    @Description("Finish Low Level Planning task")
    public void finishLowLevelPlanningTask() {
        waitForPageToLoad();
        PerspectiveChooser perspectiveChooser = PerspectiveChooser.create(driver, webDriverWait);
        perspectiveChooser.setCurrentTask();
        waitForPageToLoad();
        TasksPageV2 tasksPage = TasksPageV2.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.completeTask(processNRPCode, TasksPageV2.LOW_LEVEL_PLANNING_TASK);
        checkTaskCompleted();
        processIPCode = tasksPage.proceedNRPFromReadyForIntegration(processNRPCode);
    }

    @Test(priority = 17, description = "Finish NRP and IP", dependsOnMethods = {"finishLowLevelPlanningTask"})
    @Description("Finish NRP and IP")
    public void completeProcessNRP() {
        TasksPageV2 tasksPage = TasksPageV2.goToTasksPage(driver, webDriverWait, BASIC_URL);
        waitForPageToLoad();
        tasksPage.completeTask(processIPCode, TasksPageV2.IMPLEMENTATION_TASK);
        checkTaskCompleted();
        tasksPage.startTask(processIPCode, TasksPageV2.ACCEPTANCE_TASK);
        checkTaskAssignment();
        tasksPage.completeTask(processIPCode, TasksPageV2.ACCEPTANCE_TASK);
        checkTaskCompleted();
        tasksPage.startTask(processNRPCode, TasksPageV2.VERIFICATION_TASK);
        checkTaskAssignment();
        tasksPage.completeTask(processNRPCode, TasksPageV2.VERIFICATION_TASK);
        checkTaskCompleted();
    }

    @Test(priority = 18, description = "Delete antennas, BBU, RRU", dependsOnMethods = {"createBaseBandUnit", "createRadioUnit"})
    @Description("Delete antennas, BBU, RRU")
    public void deleteDevices() {
        openCellSiteConfiguration();
        waitForPageToLoad();
        cellSiteConfigurationPage.removeDevice("Base Band Units", MANUFACTURER, BBU_NAME);
        checkMessageType();
        waitForPageToLoad();
        for (int i = 0; i < 3; i++) {
            cellSiteConfigurationPage.removeDevice("Remote Radio Units", MANUFACTURER, RADIO_UNIT_NAMES[i]);
            checkMessageType();
            waitForPageToLoad();
            cellSiteConfigurationPage.removeDevice("Antennas", MANUFACTURER, ANTENNA_NAMES[i]);
            checkMessageType();
            waitForPageToLoad();
        }
    }

    @Test(priority = 19, description = "Delete eNodeB", dependsOnMethods = {"createENodeB"})
    @Description("Delete eNodeB")
    public void deleteNodeB() {
        waitForPageToLoad();
        cellSiteConfigurationPage.removeBaseStation(NAME, ENODEB_NAME);
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

    private void checkMessageContainsText(String message) {
        softAssert.assertTrue((getFirstMessage().getText())
                .contains(message));
    }

    private void checkMessageText(String message) {
        softAssert.assertEquals((getFirstMessage().getText()), message);
    }

    private Message getFirstMessage() {
        return SystemMessageContainer.create(driver, webDriverWait)
                .getFirstMessage()
                .orElseThrow(() -> new RuntimeException("The list is empty"));
    }

    private void checkTaskAssignment() {
        checkMessageText(TASK_ASSIGNED);
        checkMessageType();
    }

    private void checkTaskCompleted() {
        checkMessageContainsText(TASK_COMPLETED);
        checkMessageType();
    }

    private void checkMessageType() {
        SystemMessageInterface systemMessage = getSuccesSystemMessage();
        systemMessage.close();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    private SystemMessageInterface getSuccesSystemMessage() {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, new WebDriverWait(driver, 90));
        softAssert.assertEquals((systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getMessageType()), MessageType.SUCCESS);
        return systemMessage;
    }

    private void closeMessage() {
        SystemMessageContainer.create(driver, webDriverWait).close();
        waitForPageToLoad();
    }
}

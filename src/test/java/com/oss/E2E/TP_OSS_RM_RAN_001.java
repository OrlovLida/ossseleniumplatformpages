package com.oss.E2E;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.alerts.SystemMessageContainer;
import com.oss.framework.alerts.SystemMessageContainer.Message;
import com.oss.framework.alerts.SystemMessageContainer.MessageType;
import com.oss.framework.mainheader.PerspectiveChooser;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.bpm.PlanViewWizardPage;
import com.oss.pages.bpm.ProcessWizardPage;
import com.oss.pages.bpm.TasksPage;
import com.oss.pages.platform.HomePage;
import com.oss.pages.platform.OldInventoryView.OldInventoryViewPage;
import com.oss.pages.radio.CellSiteConfigurationPage;

import io.qameta.allure.Description;
import io.qameta.allure.Step;

public class TP_OSS_RM_RAN_001 extends BaseTestCase {

    private static final String LOCATION_NAME = "XYZ_SeleniumTests";
    private static final String ENODEB_NAME = "GBM055TST";
    private static final String ENODEB_ID = "1" + (int) (Math.random() * 10);
    private static final String ENODEB_MODEL = "HUAWEI Technology Co.,Ltd BTS5900";
    private static final String MCCMNC_PRIMARY = "DU03 [mcc: 424, mnc: 03]";
    private static final String BASE_BAND_UNIT_MODEL = "HUAWEI Technology Co.,Ltd BBU5900";
    private static final String BBU_NAME = "BTS5900,GBM055TST/0/BBU5900,0";
    private static final String RADIO_UNIT_MODEL = "HUAWEI Technology Co.,Ltd RRU5301";
    private static final String[] RADIO_UNIT_NAMES = { "BTS5900,GBM055TST/0/MRRU,60", "BTS5900,GBM055TST/0/MRRU,70", "BTS5900,GBM055TST/0/MRRU,80" };
    private static final String RAN_ANTENNA_MODEL = "HUAWEI Technology Co.,Ltd APE4518R14V06";
    private static final String[] ANTENNA_NAMES = { "TP_OSS_RM_RAN_001_ANTENNA_1", "TP_OSS_RM_RAN_001_ANTENNA_2", "TP_OSS_RM_RAN_001_ANTENNA_3" };
    //    private static final String[] ANTENNA_NAMES = { "KPTEST_001_ANTENNA_1", "KPTEST_001_ANTENNA_2", "KPTEST_001_ANTENNA_3" };
    private static final String BBU_EQUIPMENT_TYPE = "Base Band Unit";
    private static final String RADIO_UNIT_EQUIPMENT_TYPE = "Remote Radio Head/Unit";
    private static final String CARRIER = "L800-B20-5 (6175)";
    private static final String[] CELL_NAMES = new String[] { "TP_OSS_RM_RAN_001_CELL10", "TP_OSS_RM_RAN_001_CELL20", "TP_OSS_RM_RAN_001_CELL30" };
    //    private static final String[] CELL_NAMES = new String[] { "KPTEST_001_CELL10", "KPTEST_001_CELL20", "KPTEST_001_CELL30" };
    private static final int AMOUNT_OF_CELLS = CELL_NAMES.length;
    private static final String PCI = "2";
    private static final String RSI = "2";
    private static final String REFERENCE_POWER = "0";
    private static final String[] TAC = { "2", "2", "2" };
    private static final String PA_INPUT = "2";
    private static final String TASK_COMPLETED = "Task properly completed.";
    private static final String TASK_ASSIGNED = "The task properly assigned.";
    private static final String SITE = "Site";
    private static final String NAME = "Name";
    private static final String BUSINESS_PROCESS_MANAGEMENT = "Business Process Management";
    private static final String BPM_AND_PLANNING = "BPM and Planning";
    private static final String PROCESS_INSTANCES = "Process Instances";

    private CellSiteConfigurationPage cellSiteConfigurationPage;
    private String processNRPCode;

    @BeforeClass
    public void openConsole() {
        waitForPageToLoad();
    }

    @Test(priority = 1)
    public void createProcessNRP() {
        homePage.chooseFromLeftSideMenu(PROCESS_INSTANCES, BPM_AND_PLANNING, BUSINESS_PROCESS_MANAGEMENT);
        waitForPageToLoad();
        ProcessWizardPage processWizardPage = new ProcessWizardPage(driver);
        processNRPCode = processWizardPage.createSimpleNRP();
        checkMessageSize();
        checkMessageType();
        checkMessageContainsText(processNRPCode);
    }

    @Test(priority = 2)
    public void startHLP() {
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        waitForPageToLoad();
        tasksPage.startTask(processNRPCode, TasksPage.HIGH_LEVEL_PLANNING_TASK);
        checkMessageType();
        checkTaskAssignment();
        waitForPageToLoad();
    }

    @Test(priority = 3)
    public void findLocation() {
        openCellSiteConfiguration();
    }

    @Test(priority = 4)
    @Description("Create eNodeB")
    public void createENodeB() {
        waitForPageToLoad();
        cellSiteConfigurationPage = new CellSiteConfigurationPage(driver);
        cellSiteConfigurationPage.createENodeB(ENODEB_NAME, ENODEB_ID, ENODEB_MODEL, MCCMNC_PRIMARY);
        checkMessageText("ENodeB was created");
    }

    @Test(priority = 5)
    @Description("Create Cell4G with Bulk Wizard")
    public void createCell4GBulk() {
        waitForPageToLoad();
        cellSiteConfigurationPage.expandTreeToBaseStation(SITE, LOCATION_NAME, ENODEB_NAME);
        cellSiteConfigurationPage.createCell4GBulk(AMOUNT_OF_CELLS, CARRIER, CELL_NAMES);
        checkMessageType();
        checkMessageText("Cells 4G created success");
    }

    @Step("Create Base Band Unit")
    @Test(priority = 6)
    public void createBaseBandUnit() {
        waitForPageToLoad();
        cellSiteConfigurationPage.selectTreeRow(LOCATION_NAME);
        cellSiteConfigurationPage.createBaseBandUnit(BBU_EQUIPMENT_TYPE, BASE_BAND_UNIT_MODEL, BBU_NAME, LOCATION_NAME);
        checkMessageType();
        waitForPageToLoad();
    }

    @Step("Create three Radio Units")
    @Test(priority = 7)
    public void createRadioUnit() {
        for (int i = 0; i < 3; i++) {
            cellSiteConfigurationPage.createRadioUnit(RADIO_UNIT_EQUIPMENT_TYPE, RADIO_UNIT_MODEL, RADIO_UNIT_NAMES[i], LOCATION_NAME);
            checkMessageType();
            waitForPageToLoad();
        }
    }

    @Step("Create three RAN Antennas")
    @Test(priority = 8)
    public void createRanAntennaAndArray() {
        for (int i = 0; i < 3; i++) {
            cellSiteConfigurationPage.createRanAntennaAndArray(ANTENNA_NAMES[i], RAN_ANTENNA_MODEL, LOCATION_NAME);
            checkMessageType();// TODO sprawdzic OSSRAN-5528
            waitForPageToLoad();
        }
    }

    @Test(priority = 9)
    @Description("Create hosting relation between eNodeB and BBU")
    public void hostENodeBOnBBU() {
        //TODO OSSPHY-49316 trzeba się upewnić że odpowiedni device wybiera
        waitForPageToLoad();
        cellSiteConfigurationPage.selectTreeRow(ENODEB_NAME);
        waitForPageToLoad();
        cellSiteConfigurationPage.createHostingOnDevice(BBU_NAME, false);
        checkMessageType();
        waitForPageToLoad();
    }

    @Test(priority = 10)
    @Description("Create hosting relation between Cell 4G and RRU")
    public void hostCell4GOnRRU() {
        for (int i = 0; i < 3; i++) {
            cellSiteConfigurationPage.selectTreeRow(CELL_NAMES[i]);
            waitForPageToLoad();
            cellSiteConfigurationPage.createHostingOnDevice(RADIO_UNIT_NAMES[i]);//TODO OSSPHY-49316 trzeba się upewnić że odpowiedni device wybiera
            checkMessageType();
            waitForPageToLoad();
        }
    }

    @Test(priority = 11)
    @Description("Create hosting relation between Cell 4G and RAN Antenna Array")
    public void hostCell4GOnRANAntennaArray() {
        for (int i = 0; i < 3; i++) {
            waitForPageToLoad();
            cellSiteConfigurationPage.selectTreeRow(CELL_NAMES[i]);
            waitForPageToLoad();
            cellSiteConfigurationPage.createHostingOnAntennaArray(ANTENNA_NAMES[i]);//TODO OSSPHY-49316 trzeba się upewnić że odpowiedni device wybiera
            checkMessageType();
        }
    }

    @Test(priority = 12)
    @Description("Finish HLP")
    public void finishHLP() {
        waitForPageToLoad();
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        waitForPageToLoad();
        tasksPage.completeTask(processNRPCode, TasksPage.HIGH_LEVEL_PLANNING_TASK);
        checkMessageType();
        checkTaskCompleted();
    }

    @Test(priority = 13)
    @Description("Start Low Level Planning")
    public void startLLP() {
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        waitForPageToLoad();
        tasksPage.startTask(processNRPCode, TasksPage.LOW_LEVEL_PLANNING_TASK);
        checkMessageType();
        checkTaskAssignment();
        waitForPageToLoad();
    }

    @Test(priority = 14)
    @Description("Check validation results")
    public void validateProjectPlan() {
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        waitForPageToLoad();
        tasksPage.findTask(processNRPCode, TasksPage.LOW_LEVEL_PLANNING_TASK);
        waitForPageToLoad();
        tasksPage.clickPlanViewButton();
        waitForPageToLoad();
        PlanViewWizardPage planViewWizardPage = new PlanViewWizardPage(driver);
        planViewWizardPage.selectTab("Validation Results");
        Assert.assertTrue(planViewWizardPage.validationErrorsPresent());
    }

    @Test(priority = 15)
    @Description("Complete cells configuration")
    public void lowLevelLogicalDesign() {
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
        //TODO check popup, tac był poza zasięgiem
    }

    @Test(priority = 16)
    @Description("Finish Low Level Planning")
    public void finishLowLevelPlanningTask() {
        //TODO dalej sa validacje, jest screen
        waitForPageToLoad();
        PerspectiveChooser perspectiveChooser = PerspectiveChooser.create(driver, webDriverWait);
        perspectiveChooser.setCurrentTask();
        waitForPageToLoad();
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.completeTask(processNRPCode, TasksPage.LOW_LEVEL_PLANNING_TASK);
        checkMessageType();
        checkTaskCompleted();

        tasksPage.startTask(processNRPCode, TasksPage.READY_FOR_INTEGRATION_TASK);
        waitForPageToLoad();
        checkMessageType();
        checkTaskAssignment();

        tasksPage.completeTask(processNRPCode, TasksPage.READY_FOR_INTEGRATION_TASK);
        checkMessageType();
        checkTaskCompleted();
    }

    @Test(priority = 17)
    @Description("Finish NRP")
    public void completeProcessNRP() {
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        waitForPageToLoad();
        tasksPage.startTask(processNRPCode, "Integrate");
        checkMessageType();
        checkTaskAssignment();
        waitForPageToLoad();
        tasksPage.completeTask(processNRPCode, "Integrate");
        checkMessageType();
        checkTaskCompleted();
        waitForPageToLoad();

        tasksPage.startTask(processNRPCode, "Implement");
        checkMessageType();
        checkTaskAssignment();
        waitForPageToLoad();
        tasksPage.completeTask(processNRPCode, "Implement");
        checkMessageType();
        checkTaskCompleted();
        waitForPageToLoad();

        tasksPage.startTask(processNRPCode, "Accept");
        checkMessageType();
        checkTaskAssignment();
        waitForPageToLoad();
        tasksPage.completeTask(processNRPCode, "Accept");
        checkMessageType();
        checkTaskCompleted();
        waitForPageToLoad();

        tasksPage.startTask(processNRPCode, TasksPage.ACCEPTANCE_TASK);
        waitForPageToLoad();
        tasksPage.completeTask(processNRPCode, TasksPage.ACCEPTANCE_TASK);
        waitForPageToLoad();

        tasksPage.startTask(processNRPCode, TasksPage.VERIFICATION_TASK);
        waitForPageToLoad();
        tasksPage.completeTask(processNRPCode, TasksPage.VERIFICATION_TASK);
        waitForPageToLoad();
    }

    @Test(priority = 18)
    @Description("Delete antennas, BBU, RRU")
    public void deleteDevices() {
        openCellSiteConfiguration();
        waitForPageToLoad();
        cellSiteConfigurationPage.filterObject(NAME, BBU_NAME);
        waitForPageToLoad();
        cellSiteConfigurationPage.removeObject();
        waitForPageToLoad();
        checkMessageType();

        for (int i = 0; i < 3; i++) {
            cellSiteConfigurationPage.filterObject(NAME, RADIO_UNIT_NAMES[i]);
            waitForPageToLoad();
            cellSiteConfigurationPage.removeObject();
            waitForPageToLoad();
            checkMessageType();

            cellSiteConfigurationPage.filterObject(NAME, ANTENNA_NAMES[i]);
            waitForPageToLoad();
            cellSiteConfigurationPage.removeObject();
            waitForPageToLoad();
            checkMessageType();
        }
    }

    @Test(priority = 19)
    @Description("Delete eNodeB")
    public void deleteNodeB() {
        waitForPageToLoad();
        cellSiteConfigurationPage.expandTreeToLocation(SITE, LOCATION_NAME);
        waitForPageToLoad();
        cellSiteConfigurationPage.selectRowByAttributeValueWithLabel(NAME, ENODEB_NAME);
        waitForPageToLoad();
        cellSiteConfigurationPage.removeObject();
    }

    private void openCellSiteConfiguration() {
        HomePage homePage = new HomePage(driver);
        homePage.goToHomePage(driver, BASIC_URL);
        waitForPageToLoad();
        homePage.chooseFromLeftSideMenu("Legacy Inventory Dashboard", "Resource Inventory ");
        waitForPageToLoad();
        homePage.setOldObjectType(SITE);//TODO zmienic na nowe menu i na nowy IV
        waitForPageToLoad();
        OldInventoryViewPage oldInventoryViewPage = new OldInventoryViewPage(driver);
        oldInventoryViewPage.filterObject(NAME, LOCATION_NAME);
        waitForPageToLoad();
        oldInventoryViewPage.expandShowOnAndChooseView("Cell Site Configuration");
        waitForPageToLoad();
    }

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    private void checkMessageType() {
        Assert.assertEquals((getFirstMessage().getMessageType()), MessageType.SUCCESS);
    }

    private void checkMessageContainsText(String message) {
        Assert.assertTrue((getFirstMessage().getText())
                .contains(message));
    }

    private void checkMessageText(String message) {
        Assert.assertEquals((getFirstMessage().getText()), message);
    }

    private void checkMessageSize() {
        Assert.assertEquals((SystemMessageContainer.create(driver, webDriverWait)
                .getMessages()
                .size()), 1);
    }

    private Message getFirstMessage() {
        return SystemMessageContainer.create(driver, webDriverWait)
                .getFirstMessage()
                .orElseThrow(() -> new RuntimeException("The list is empty"));
    }

    private void checkTaskAssignment() {
        checkMessageType();
        checkMessageText(TASK_ASSIGNED);
    }

    private void checkTaskCompleted() {
        checkMessageType();
        checkMessageContainsText(TASK_COMPLETED);
    }
}

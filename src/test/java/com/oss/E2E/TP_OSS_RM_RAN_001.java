package com.oss.E2E;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.alerts.SystemMessageContainer;
import com.oss.framework.alerts.SystemMessageContainer.MessageType;
import com.oss.framework.alerts.SystemMessageInterface;
import com.oss.framework.mainheader.PerspectiveChooser;
import com.oss.framework.sidemenu.SideMenu;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.bpm.PlanViewWizardPage;
import com.oss.pages.bpm.ProcessWizardPage;
import com.oss.pages.bpm.TasksPage;
import com.oss.pages.platform.HomePage;
import com.oss.pages.platform.OldInventoryView.OldInventoryViewPage;
import com.oss.pages.radio.CellSiteConfigurationPage;
import com.oss.pages.reconciliation.CmDomainWizardPage;
import com.oss.pages.reconciliation.NetworkDiscoveryControlViewPage;
import com.oss.pages.reconciliation.NetworkInconsistenciesViewPage;
import com.oss.pages.reconciliation.SamplesManagementPage;

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
    private static final String CM_DOMAIN_NAME = "Selenium-TP-OSS-RM-RAN-001";
    private static final String CM_INTERFACE = "Huawei U2000 RAN";
    private static final String DOMAIN = "RAN";
    private static final String[] INCONSISTENCIES_NAMES = { "ENODEB-GBM055TST", "PhysicalElement-BTS5900,GBM055TST/0/BBU5900,0", "PhysicalElement-BTS5900,GBM055TST/0/MRRU,60", "PhysicalElement-BTS5900,GBM055TST/0/MRRU,70", "PhysicalElement-BTS5900,GBM055TST/0/MRRU,80" };
    private static final String TASK_COMPLETED = "Task properly completed.";
    private static final String TASK_ASSIGNED = "The task properly assigned.";
    private static final String SITE = "Site";
    private static final String NAME = "Name";

    private CellSiteConfigurationPage cellSiteConfigurationPage;
    private NetworkDiscoveryControlViewPage networkDiscoveryControlViewPage;
    private String processNRPCode;

    @BeforeClass
    public void openConsole() {
        HomePage homePage = new HomePage(driver);
        homePage.goToHomePage(driver, BASIC_URL);
        waitForPageToLoad();
    }

    @Test(priority = 1)
    public void createProcessNRP() {
        SideMenu sideMenu = SideMenu.create(driver, webDriverWait);
        sideMenu.callActionByLabel("Process Instances", "Views", "Business Process Management");
        waitForPageToLoad();
        ProcessWizardPage processWizardPage = new ProcessWizardPage(driver);
        processNRPCode = processWizardPage.createSimpleNRP();
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assert.assertEquals(messages.size(), 1);
        checkPopupMessageType(MessageType.SUCCESS);
        Assert.assertTrue(messages.get(0).getText().contains(processNRPCode));
    }

    @Test(priority = 2)
    public void startHLP() {
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        waitForPageToLoad();
        tasksPage.startTask(processNRPCode, TasksPage.HIGH_LEVEL_PLANNING_TASK);
        checkPopupText(TASK_ASSIGNED);
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
        Assert.assertTrue(SystemMessageContainer.create(driver, webDriverWait)
                .getMessages().get(0).getText().contains("Created eNodeB"));
    }

    @Test(priority = 5)
    @Description("Create Cell4G with Bulk Wizard")
    public void createCell4GBulk() {
        waitForPageToLoad();
        cellSiteConfigurationPage.expandTreeToBaseStation(SITE, LOCATION_NAME, ENODEB_NAME);
        cellSiteConfigurationPage.createCell4GBulk(AMOUNT_OF_CELLS, CARRIER, CELL_NAMES);
        checkPopupText("Cells 4G created success");
    }

    @Step("Create Base Band Unit")
    @Test(priority = 6)
    public void createBaseBandUnit() {
        waitForPageToLoad();
        cellSiteConfigurationPage.selectTreeRow(LOCATION_NAME);
        cellSiteConfigurationPage.createBaseBandUnit(BBU_EQUIPMENT_TYPE, BASE_BAND_UNIT_MODEL, BBU_NAME, LOCATION_NAME);
        checkPopupMessageType(SystemMessageContainer.MessageType.SUCCESS);
        waitForPageToLoad();
    }

    @Step("Create three Radio Units")
    @Test(priority = 7)
    public void createRadioUnit() {
        for (int i = 0; i < 3; i++) {
            cellSiteConfigurationPage.createRadioUnit(RADIO_UNIT_EQUIPMENT_TYPE, RADIO_UNIT_MODEL, RADIO_UNIT_NAMES[i], LOCATION_NAME);
            checkPopupMessageType(SystemMessageContainer.MessageType.SUCCESS);
            waitForPageToLoad();
        }
    }

    @Step("Create three RAN Antennas")
    @Test(priority = 8)
    public void createRanAntennaAndArray() {
        for (int i = 0; i < 3; i++) {
            cellSiteConfigurationPage.createRanAntennaAndArray(ANTENNA_NAMES[i], RAN_ANTENNA_MODEL, LOCATION_NAME);
//            checkPopupMessageType(SystemMessageContainer.MessageType.SUCCESS); OSSRAN-5528
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
        checkPopupMessageType(SystemMessageContainer.MessageType.SUCCESS);
        waitForPageToLoad();
    }

    @Test(priority = 10)
    @Description("Create hosting relation between Cell 4G and RRU")
    public void hostCell4GOnRRU() {
        for (int i = 0; i < 3; i++) {
            cellSiteConfigurationPage.selectTreeRow(CELL_NAMES[i]);
            waitForPageToLoad();
            cellSiteConfigurationPage.createHostingOnDevice(RADIO_UNIT_NAMES[i]);//TODO OSSPHY-49316 trzeba się upewnić że odpowiedni device wybiera
            checkPopupMessageType(SystemMessageContainer.MessageType.SUCCESS);
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
            checkPopupMessageType(SystemMessageContainer.MessageType.SUCCESS);
        }
    }

    @Test(priority = 12)
    @Description("Finish HLP")
    public void finishHLP() {
        waitForPageToLoad();
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        waitForPageToLoad();
        tasksPage.completeTask(processNRPCode, TasksPage.HIGH_LEVEL_PLANNING_TASK);
        checkPopupText(TASK_COMPLETED);
    }

    @Test(priority = 13)
    @Description("Start Low Level Planning")
    public void startLLP() {
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        waitForPageToLoad();
        tasksPage.startTask(processNRPCode, TasksPage.LOW_LEVEL_PLANNING_TASK);
        checkPopupText(TASK_ASSIGNED);
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
        checkPopupText(TASK_COMPLETED);

        tasksPage.startTask(processNRPCode, TasksPage.READY_FOR_INTEGRATION_TASK);
        waitForPageToLoad();
        checkPopupText(TASK_ASSIGNED);

        tasksPage.completeTask(processNRPCode, TasksPage.READY_FOR_INTEGRATION_TASK);
        checkPopupText(TASK_COMPLETED);
    }

    @Test(priority = 17)
    @Description("Finish NRP")
    public void completeProcessNRP() {
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        waitForPageToLoad();
        tasksPage.startTask(processNRPCode, "Integrate");
        checkPopupText(TASK_ASSIGNED);
        waitForPageToLoad();
        tasksPage.completeTask(processNRPCode, "Integrate");
        checkPopupText(TASK_COMPLETED);
        waitForPageToLoad();

        tasksPage.startTask(processNRPCode, "Implement");
        checkPopupText(TASK_ASSIGNED);
        waitForPageToLoad();
        tasksPage.completeTask(processNRPCode, "Implement");
        checkPopupText(TASK_COMPLETED);
        waitForPageToLoad();

        tasksPage.startTask(processNRPCode, "Accept");
        checkPopupText(TASK_ASSIGNED);
        waitForPageToLoad();
        tasksPage.completeTask(processNRPCode, "Accept");
        checkPopupText(TASK_COMPLETED);
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
    @Description("Create CM domain")
    public void createCmDomain() {
        openNetworkDiscoveryControlView();
        networkDiscoveryControlViewPage.openCmDomainWizard();
        CmDomainWizardPage wizard = new CmDomainWizardPage(driver);
        wizard.setName(CM_DOMAIN_NAME);
        wizard.setInterface(CM_INTERFACE);
        wizard.setDomain(DOMAIN);
        wizard.save();
        waitForPageToLoad();
    }

    @Test(priority = 19)
    @Description("Upload samples")
    public void uploadSamples() {
        DelayUtils.sleep(1000);
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(CM_DOMAIN_NAME);
        waitForPageToLoad();
        networkDiscoveryControlViewPage.moveToSamplesManagement();
        SamplesManagementPage samplesManagementPage = new SamplesManagementPage(driver);
        samplesManagementPage.selectPath();
        samplesManagementPage.createDirectory(CM_DOMAIN_NAME);
        DelayUtils.sleep(1000);
        samplesManagementPage.uploadSamples("recoSamples/huaweiRan/TP_OSS_RM_RAN_001/AIM_BTS5900_GBM055.xml");
        DelayUtils.sleep(1000);
        samplesManagementPage.uploadSamples("recoSamples/huaweiRan/TP_OSS_RM_RAN_001/SRANNBIExport_XML_Co-MPT BTS_RT_06_17_2020_22_38_11_629_198_19_191_6.xml");
    }

    @Test(priority = 20)
    @Description("Run reconciliation with full sample")
    public void runReconciliationWithFullSample() {
        openNetworkDiscoveryControlView();
        DelayUtils.sleep(100);
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(CM_DOMAIN_NAME);
        waitForPageToLoad();
        networkDiscoveryControlViewPage.runReconciliation();
        checkPopupMessageType(MessageType.INFO);
        Assert.assertEquals(networkDiscoveryControlViewPage.waitForEndOfReco(), "SUCCESS");
    }

    @Test(priority = 21)
    @Description("Check operation type and accept discrepancies")
    public void checkOperationTypeAndAcceptDiscrepancies() {
        networkDiscoveryControlViewPage.moveToNivFromNdcv();
        NetworkInconsistenciesViewPage networkInconsistenciesViewPage = new NetworkInconsistenciesViewPage(driver);
        networkInconsistenciesViewPage.expandTree();
        for (String inconsistencieName : INCONSISTENCIES_NAMES) {
            Assert.assertEquals(networkInconsistenciesViewPage.checkInconsistenciesOperationType(), "MODIFICATION");
            networkInconsistenciesViewPage.clearOldNotification();
            networkInconsistenciesViewPage.applySelectedInconsistencies();
            DelayUtils.sleep(5000);
            networkInconsistenciesViewPage.checkNotificationAfterApplyInconsistencies(inconsistencieName);
        }
    }

    @Test(priority = 22)
    @Description("Delete CM Domain")
    public void deleteCmDomain() {
        openNetworkDiscoveryControlView();
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(CM_DOMAIN_NAME);
        waitForPageToLoad();
        networkDiscoveryControlViewPage.clearOldNotifications();
        networkDiscoveryControlViewPage.deleteCmDomain();
        checkPopupMessageType(MessageType.INFO);
        Assert.assertEquals(networkDiscoveryControlViewPage.checkDeleteCmDomainNotification(), "Deleting CM Domain: " + CM_DOMAIN_NAME + " finished");
    }

    @Test(priority = 23)
    @Description("Delete antennas, BBU, RRU")
    public void deleteDevices() {
        openCellSiteConfiguration();
        waitForPageToLoad();
        cellSiteConfigurationPage.filterObject(NAME, BBU_NAME);
        waitForPageToLoad();
        cellSiteConfigurationPage.removeObject();
        waitForPageToLoad();
        checkPopupMessageType(SystemMessageContainer.MessageType.SUCCESS);

        for (int i = 0; i < 3; i++) {
            cellSiteConfigurationPage.filterObject(NAME, RADIO_UNIT_NAMES[i]);
            waitForPageToLoad();
            cellSiteConfigurationPage.removeObject();
            waitForPageToLoad();
            checkPopupMessageType(SystemMessageContainer.MessageType.SUCCESS);

            cellSiteConfigurationPage.filterObject(NAME, ANTENNA_NAMES[i]);
            waitForPageToLoad();
            cellSiteConfigurationPage.removeObject();
            waitForPageToLoad();
            checkPopupMessageType(SystemMessageContainer.MessageType.SUCCESS);
        }
    }

    @Test(priority = 24)
    @Description("Delete eNodeB")
    public void deleteNodeB() {
        waitForPageToLoad();
        cellSiteConfigurationPage.expandTreeToLocation(SITE, LOCATION_NAME);
        waitForPageToLoad();
        cellSiteConfigurationPage.selectRowByAttributeValueWithLabel(NAME, ENODEB_NAME);
        waitForPageToLoad();
        cellSiteConfigurationPage.removeObject();
    }

    public void openNetworkDiscoveryControlView() {
        networkDiscoveryControlViewPage = NetworkDiscoveryControlViewPage.goToNetworkDiscoveryControlViewPage(driver, BASIC_URL);
    }

    public void openCellSiteConfiguration() {
        HomePage homePage = new HomePage(driver);
        homePage.goToHomePage(driver, BASIC_URL);
        waitForPageToLoad();
        homePage.setOldObjectType(SITE);
        waitForPageToLoad();
        OldInventoryViewPage oldInventoryViewPage = new OldInventoryViewPage(driver);
        oldInventoryViewPage.filterObject(NAME, LOCATION_NAME);
        waitForPageToLoad();
        oldInventoryViewPage.expandShowOnAndChooseView("Cell Site Configuration");
        waitForPageToLoad();
    }

    private void checkPopupMessageType(MessageType messageType) {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assert.assertNotNull(messages);
        Assert.assertEquals(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getMessageType(),
                messageType);
    }

    private void checkPopupText(String text) {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assert.assertEquals(messages.get(0).getMessageType(), SystemMessageContainer.MessageType.SUCCESS);
        Assert.assertEquals(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getText(), text);
    }

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }
}

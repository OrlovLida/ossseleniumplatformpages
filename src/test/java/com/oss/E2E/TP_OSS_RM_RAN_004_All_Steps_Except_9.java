package com.oss.E2E;

import java.util.List;
import java.util.regex.Pattern;

import org.assertj.core.api.Assertions;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.alerts.SystemMessageContainer;
import com.oss.framework.alerts.SystemMessageInterface;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.bpm.PlanViewWizardPage;
import com.oss.pages.bpm.ProcessInstancesPage;
import com.oss.pages.bpm.ProcessWizardPage;
import com.oss.pages.bpm.TasksPage;
import com.oss.pages.physical.DeviceWizardPage;
import com.oss.pages.platform.HomePage;
import com.oss.pages.platform.OldInventoryView.OldInventoryViewPage;
import com.oss.pages.radio.CellBulkWizardPage;
import com.oss.pages.radio.CellSiteConfigurationPage;
import com.oss.pages.radio.EditCell4GWizardPage;
import com.oss.pages.radio.HostingWizardPage;

import io.qameta.allure.Description;
import io.qameta.allure.Step;

public class TP_OSS_RM_RAN_004_All_Steps_Except_9 extends BaseTestCase {

    private static final String LOCATION_NAME = "XYZ_SeleniumTests";
    private static final String ENODEB_NAME = "TP_OSS_RM_RAN_004_eNodeB";
    private static final String CARRIER_L1800 = "L1800 (1392)";
    private static final String CARRIER_L2100 = "L2100 (10562)";
    private static final String[] CELL_NAMES_L1800 = { "TP_OSS_RM_RAN_004_L1800_cell_1", "TP_OSS_RM_RAN_004_L1800_cell_2", "TP_OSS_RM_RAN_004_L1800_cell_3" };
    private static final String[] CELL_NAMES_L2100 = { "TP_OSS_RM_RAN_004_L2100_cell_1", "TP_OSS_RM_RAN_004_L2100_cell_2", "TP_OSS_RM_RAN_004_L2100_cell_3" };

    private static final String RADIO_UNIT_EQUIPMENT_TYPE = "Remote Radio Head/Unit";
    private static final String RADIO_UNIT_MODEL = "HUAWEI Technology Co.,Ltd RRU5301";
    private static final String RADIO_UNIT_NAMES[] = { "TP_OSS_RM_RAN_004_RRU_1", "TP_OSS_RM_RAN_004_RRU_2", "TP_OSS_RM_RAN_004_RRU_3" };

    private static final String[] ANTENNA_NAMES = { "TP_OSS_RM_RAN_004_Antenna_1", "TP_OSS_RM_RAN_004_Antenna_2", "TP_OSS_RM_RAN_004_Antenna_3" };

    private static final String PCI = "2";              // change to public???
    private static final String RSI = "2";              // change to public???
    private static final String REFERENCE_POWER = "0";  // change to public???
    private static final String TAC = "2";              // change to public???
    private static final String PA_OUTPUT = "2";        // change to public???
    private String processNRPCode;
    private String perspectiveContext;                  // change to public???

    private int amountOfCells = 3;
    private int[] localCellsId1800 = { 1, 2, 3 };
    private int[] localCellsId2100 = { 4, 5, 6 };

    private TasksPage tasksPage;
    private CellSiteConfigurationPage cellSiteConfigurationPage;

    private void checkPopup(String text) {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assert.assertEquals(messages.size(), 1);
        Assert.assertEquals(messages.get(0).getMessageType(), SystemMessageContainer.MessageType.SUCCESS);
        Assert.assertTrue((messages.get(0).getText()).contains(text));
    }

    private void hostCell4GOnRRU(String[] cellNames, String[] radioUnitNames) {
        for (int i = 0; i < 3; i++) {
            cellSiteConfigurationPage.selectTreeRow(cellNames[i]);
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            cellSiteConfigurationPage.selectTab("Hosting");
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            cellSiteConfigurationPage.clickPlusIconAndSelectOption("Host on Device");
            HostingWizardPage wizard = new HostingWizardPage(driver);
            //TODO: check below method
            //wizard.selectDevice(radioUnitNames[i]);
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            wizard.clickAccept();
            checkPopup("Hosting Create Success");
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
        }
    }

    private void hostCell4GOnRANAntennaArray(String[] cellNames, String[] antennaNames) {
        for (int i = 0; i < 3; i++) {
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            cellSiteConfigurationPage.selectTreeRow(cellNames[i]);
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            cellSiteConfigurationPage.clickPlusIconAndSelectOption("Host on Antenna Array");
            HostingWizardPage wizard = new HostingWizardPage(driver);
            //TODO: check below method
            //wizard.selectArray(antennaNames[i] + "/APE4518R14V06_Ly1");
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            wizard.clickAccept();
            checkPopup("Hosting Create Success");
        }
    }

    private void createCellBulk(int amountOfCells, String carrier, String[] cellNames, int[] localCellsId) {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cellSiteConfigurationPage.clickPlusIconAndSelectOption("Cell 4G Bulk Wizard");
        CellBulkWizardPage cell4GBulkWizardPage = new CellBulkWizardPage(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cell4GBulkWizardPage.createCell4GBulkWizardWithDefaultValues(amountOfCells, carrier, cellNames, localCellsId);
        checkPopup("Cells 4G created success");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 1)
    @Description("Go to BPM and create process NRP")
    public void createProcessNRP() {
        ProcessInstancesPage processInstancesPage = ProcessInstancesPage.goToProcessInstancesPage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        ProcessWizardPage processWizardPage = new ProcessWizardPage(driver);
        processNRPCode = processWizardPage.createSimpleNRP();
        checkPopup(processNRPCode);
    }

    @Test(priority = 2)
    public void startHLP() {
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        tasksPage.startTask(processNRPCode, TasksPage.HIGH_LEVEL_PLANNING_TASK);
        checkPopup("The task properly assigned.");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 3)
    public void findLocation() {
        HomePage homePage = new HomePage(driver);
        homePage.goToHomePage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        homePage.setOldObjectType("Site");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        OldInventoryViewPage oldInventoryViewPage = new OldInventoryViewPage(driver);
        oldInventoryViewPage.filterObject("Name", LOCATION_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        oldInventoryViewPage.expandShowOnAndChooseView("Cell Site Configuration");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 4)
    @Description("Create Cell4G with Bulk Wizard")
    public void createCell4GL1800Bulk() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cellSiteConfigurationPage.expandTreeToBaseStation("Site", LOCATION_NAME, ENODEB_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cellSiteConfigurationPage.selectTab("Cells 4G");

        createCellBulk(amountOfCells, CARRIER_L1800, CELL_NAMES_L1800, localCellsId1800);
    }

    @Test(priority = 5)
    @Description("Create Cell4G with Bulk Wizard")
    public void createCell4GL2100Bulk() {
        createCellBulk(amountOfCells, CARRIER_L2100, CELL_NAMES_L2100, localCellsId2100);
    }

    @Step("Create three Radio Units")
    @Test(priority = 6)
    public void createRadioUnit() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cellSiteConfigurationPage.selectTreeRow(LOCATION_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cellSiteConfigurationPage.selectTab("Devices");
        for (int i = 0; i < 3; i++) {
            cellSiteConfigurationPage.clickPlusIconAndSelectOption("Create Device");
            DeviceWizardPage deviceWizardPage = new DeviceWizardPage(driver);
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            deviceWizardPage.setEquipmentType(RADIO_UNIT_EQUIPMENT_TYPE);
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            deviceWizardPage.setModel(RADIO_UNIT_MODEL);
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            deviceWizardPage.setName(RADIO_UNIT_NAMES[i]);
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            deviceWizardPage.create();
            checkPopup("Device has been created successfully");
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
        }
    }

    @Test(priority = 7)
    @Description("Create hosting relation between Cell 4G and RRU")
    public void hostCell4GL1800OnRRU() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cellSiteConfigurationPage.selectTreeRow(ENODEB_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        hostCell4GOnRRU(CELL_NAMES_L1800, RADIO_UNIT_NAMES);
    }

    @Test(priority = 8)
    @Description("Create hosting relation between Cell 4G and RAN Antenna Array")
    public void hostCell4GL1800OnRANAntennaArray() {
        hostCell4GOnRANAntennaArray(CELL_NAMES_L1800, ANTENNA_NAMES);
    }

    @Test(priority = 9)
    @Description("Create hosting relation between Cell 4G and RRU")
    public void hostCell4GL2100OnRRU() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cellSiteConfigurationPage.selectTreeRow(ENODEB_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        hostCell4GOnRRU(CELL_NAMES_L1800, RADIO_UNIT_NAMES);
    }

    @Test(priority = 10)
    @Description("Create hosting relation between Cell 4G and RAN Antenna Array")
    public void hostCell4GL2100OnRANAntennaArray() {
        hostCell4GOnRANAntennaArray(CELL_NAMES_L1800, ANTENNA_NAMES);
    }

    @Test(priority = 11)
    public void finishHLP() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        tasksPage.completeTask(processNRPCode, TasksPage.HIGH_LEVEL_PLANNING_TASK);
        checkPopup("Task properly completed.");
    }

    @Test(priority = 13)
    public void startLLP() {
        tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.startTask(processNRPCode, TasksPage.LOW_LEVEL_PLANNING_TASK);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        checkPopup("The task properly assigned.");

        String currentUrl = driver.getCurrentUrl();
        String[] split = currentUrl.split(Pattern.quote("?"));
        perspectiveContext = split[1];
        Assertions.assertThat(perspectiveContext).contains("PLAN");
    }

    @Test(priority = 14)
    public void validateProjectPlan() {
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        tasksPage.findTask(processNRPCode, TasksPage.LOW_LEVEL_PLANNING_TASK);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        tasksPage.clickPlanViewButton();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        PlanViewWizardPage planViewWizardPage = new PlanViewWizardPage(driver);
        planViewWizardPage.selectTab("Validation Results");
        Assertions.assertThat(planViewWizardPage.validationErrorsPresent());
    }

    @Test(priority = 15)
    public void lowLevelLogicalDesign() {
        HomePage homePage = new HomePage(driver);
        homePage.goToHomePage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        homePage.setOldObjectType("Site");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        OldInventoryViewPage oldInventoryViewPage = new OldInventoryViewPage(driver);
        oldInventoryViewPage.filterObject("Name", LOCATION_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        oldInventoryViewPage.expandShowOnAndChooseView("Cell Site Configuration");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        CellSiteConfigurationPage cellSiteConfigurationPage = new CellSiteConfigurationPage(driver);
        cellSiteConfigurationPage.expandTreeToBaseStation("Site", LOCATION_NAME, ENODEB_NAME);

        for (int i = 0; i < 3; i++) {
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            cellSiteConfigurationPage.selectRowByAttributeValueWithLabel("Name", CELL_NAMES_L1800[i]);
        }

        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cellSiteConfigurationPage.clickEditIcon();
        EditCell4GWizardPage editCell4GWizardPage = new EditCell4GWizardPage(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        editCell4GWizardPage.setPCIBulk(PCI);//
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        editCell4GWizardPage.setRSIBulk(RSI);//
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        editCell4GWizardPage.setReferencePowerBulk(REFERENCE_POWER);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        editCell4GWizardPage.setTAC(1, TAC);//
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        editCell4GWizardPage.setTAC(2, TAC);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        editCell4GWizardPage.setTAC(3, TAC);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        editCell4GWizardPage.setPaOutputBulk(PA_OUTPUT);//
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        editCell4GWizardPage.accept();
    }
}

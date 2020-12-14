package com.oss.E2E;

import java.util.List;
import java.util.regex.Pattern;

import org.assertj.core.api.Assertions;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.alerts.SystemMessageContainer;
import com.oss.framework.alerts.SystemMessageInterface;
import com.oss.framework.mainheader.PerspectiveChooser;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.bpm.PlanViewWizardPage;
import com.oss.pages.bpm.ProcessInstancesPage;
import com.oss.pages.bpm.ProcessWizardPage;
import com.oss.pages.bpm.TasksPage;
import com.oss.pages.physical.DeviceWizardPage;
import com.oss.pages.platform.HomePage;
import com.oss.pages.platform.OldInventoryViewPage;
import com.oss.pages.radio.AntennaArrayWizardPage;
import com.oss.pages.radio.CellBulkWizardPage;
import com.oss.pages.radio.CellSiteConfigurationPage;
import com.oss.pages.radio.ENodeBWizardPage;
import com.oss.pages.radio.EditCell4GWizardPage;
import com.oss.pages.radio.HostingWizardPage;
import com.oss.pages.radio.RanAntennaWizardPage;

import io.qameta.allure.Description;
import io.qameta.allure.Step;

public class TP_OSS_RM_RAN_001 extends BaseTestCase {

    private String processNRPCode;
    private String locationName = "XYZ_SeleniumTests"; //czy mamy jakies wymagania co do lokacji
    private String eNodeBName = "GBM055TST";
    private String eNodeBid = "1" + (int) (Math.random() * 10);
    private String eNodeBModel = "HUAWEI Technology Co.,Ltd BTS5900";
    private String MCCMNCPrimary = "DU [mcc: 424, mnc: 03]";

    private String baseBandUnitModel = "HUAWEI Technology Co.,Ltd BBU5900";
    private String bbuName = "BTS5900,GBM055TST/0/BBU5900,0";
    private String radioUnitModel = "HUAWEI Technology Co.,Ltd RRU5301";//HUAWEI Technology Co.,Ltd RRU5301
    private String radioUnitNames[] = { "BTS5900,GBM055TST/0/MRRU,60",
            "BTS5900,GBM055TST/0/MRRU,70",
            "BTS5900,GBM055TST/0/MRRU,80" };

    private String ranAntennaModel = "HUAWEI Technology Co.,Ltd APE4518R14V06";
    private String[] antennaNames = { "SeleniumTestAntenna1", "SeleniumTestAntenna2", "SeleniumTestAntenna3" };
    private String bbuEquipmentType = "Base Band Unit";
    private String radioUnitEquipmentType = "Remote Radio Head/Unit";
    private String carrier = "L800-B20-5 (6175)";
    private String cellNames[] = new String[] { "cell10", "cell20", "cell30" };
    private int amountOfCells = cellNames.length;
    public String perspectiveContext;
    public String pci = "2";
    public String rsi = "2";
    public String referencePower = "0";
    public String tac = "2";
    public String paOutput = "2";

    private String hostingTabName = "Hosting";
    private String cell4GName = "cell4";
    private String showOnlyCompatible = "false";

    //   private String nameOfRRU = "SeleniumTestRadioUnit";
    //   private String antennaArrayName = "APE4518R14V06_Lr1";

    private CellSiteConfigurationPage cellSiteConfigurationPage;
    private AntennaArrayWizardPage antennaArrayWizardPage;
    private TasksPage tasksPage;

    @BeforeClass
    public void goToBPM() {
        ProcessInstancesPage processInstancesPage = ProcessInstancesPage.goToProcessInstancesPage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 1)
    public void createProcessNRP() {
        ProcessWizardPage processWizardPage = new ProcessWizardPage(driver);
        processNRPCode = processWizardPage.createSimpleNRP();
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assertions.assertThat(messages).hasSize(1);
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assertions.assertThat(messages.get(0).getText()).contains(processNRPCode);
    }

    @Test(priority = 2)
    public void startHLP() {
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        tasksPage.startTask(processNRPCode, "High Level Planning");
        checkTaskAssignmentPopup();
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
        oldInventoryViewPage.filterObject("Name", locationName, "Site");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        oldInventoryViewPage.expandShowOnAndChooseView("Cell Site Configuration");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 4)
    @Description("Create eNodeB")
    public void createENodeB() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cellSiteConfigurationPage = new CellSiteConfigurationPage(driver);
        cellSiteConfigurationPage.clickPlusIconAndSelectOption("Create eNodeB");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        ENodeBWizardPage eNodeBWizard = new ENodeBWizardPage(driver);
        eNodeBWizard.createENodeB(eNodeBName, eNodeBid, eNodeBModel, MCCMNCPrimary);
        Assert.assertTrue(SystemMessageContainer.create(driver, webDriverWait)
                .getMessages().get(0).getText().contains("Created eNodeB"));
    }

    @Test(priority = 5)
    @Description("Create Cell4G with Bulk Wizard")
    public void createCell4GBulk() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cellSiteConfigurationPage.expandTreeToBaseStation("Site", locationName, eNodeBName);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cellSiteConfigurationPage.selectTab("Cells 4G");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cellSiteConfigurationPage.clickPlusIconAndSelectOption("Cell 4G Bulk Wizard");
        CellBulkWizardPage cell4GBulkWizardPage = new CellBulkWizardPage(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cell4GBulkWizardPage.createCellBulkWizard(amountOfCells, carrier, cellNames);
        Assert.assertTrue(SystemMessageContainer.create(driver, webDriverWait)
                .getMessages().get(0).getText().contains("Cells 4G created success"));
    }

    @Step("Create Base Band Unit")
    @Test(priority = 6)
    public void createBaseBandUnit() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cellSiteConfigurationPage.selectTreeRow(locationName);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cellSiteConfigurationPage.selectTab("Devices");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cellSiteConfigurationPage.clickPlusIconAndSelectOption("Create Device");
        DeviceWizardPage deviceWizardPage = new DeviceWizardPage(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.setEquipmentType(bbuEquipmentType);
        deviceWizardPage.setModel(baseBandUnitModel);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.setName(bbuName);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.create();
        checkPopup();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Step("Create three Radio Units")
    @Test(priority = 7)
    public void createRadioUnit() {
        for (int i = 0; i < 3; i++) {
            cellSiteConfigurationPage.clickPlusIconAndSelectOption("Create Device");
            DeviceWizardPage deviceWizardPage = new DeviceWizardPage(driver);
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            deviceWizardPage.setEquipmentType(radioUnitEquipmentType);
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            deviceWizardPage.setModel(radioUnitModel);
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            deviceWizardPage.setName(radioUnitNames[i]);
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            deviceWizardPage.create();
            checkPopup();
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
        }
    }

    @Step("Create three RAN Antennas")
    @Test(priority = 8)
    public void createRanAntennaAndArray() {
        for (int i = 0; i < 3; i++) {
            cellSiteConfigurationPage.clickPlusIconAndSelectOption("Create RAN Antenna");
            RanAntennaWizardPage ranAntennaWizardPage = new RanAntennaWizardPage(driver);
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            ranAntennaWizardPage.setName(antennaNames[i]);
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            ranAntennaWizardPage.setModel(ranAntennaModel);
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            ranAntennaWizardPage.setPreciseLocation(locationName);
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            ranAntennaWizardPage.clickAccept();
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            antennaArrayWizardPage = new AntennaArrayWizardPage(driver);
            antennaArrayWizardPage.clickAccept();
            checkPopup();
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
        }
    }
/* USUWANIE NA KONIEC
    @Step("Delete created devices")
    @Test(priority = 10)
    public void deleteDevices() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cellSiteConfigurationPage.filterObject("Name", BBU_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cellSiteConfigurationPage.clickRemoveIcon();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        checkPopup();

        for (int i = 0; i < 3; i++) {
            cellSiteConfigurationPage.filterObject("Name", RADIO_UNIT_NAME);
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            cellSiteConfigurationPage.clickRemoveIcon();
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            checkPopup();

            cellSiteConfigurationPage.filterObject("Name", ANTENNA_NAME);
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            cellSiteConfigurationPage.clickRemoveIcon();
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            checkPopup();
        }
    }

 */

    @Test(priority = 9)
    @Description("Create hosting relation between eNodeB and BBU")
    public void hostENodeBOnBBU() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cellSiteConfigurationPage.selectTreeRow(eNodeBName);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cellSiteConfigurationPage.selectTab(hostingTabName);

        cellSiteConfigurationPage.clickPlusIconByLabel("Host on Device");
        HostingWizardPage wizard = new HostingWizardPage(driver);
        wizard.onlyCompatible("false");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        wizard.selectDevice(bbuName);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        wizard.clickAccept();
        checkPopup();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 10)
    @Description("Create hosting relation between Cell 4G and RRU")
    public void hostCell4GOnRRU() {

        for (int i = 0; i < 3; i++) {
            cellSiteConfigurationPage.selectTreeRow(cellNames[i]);
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            cellSiteConfigurationPage.selectTab(hostingTabName);
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            cellSiteConfigurationPage.clickPlusIconAndSelectOption("Host on Device");
            HostingWizardPage wizard = new HostingWizardPage(driver);
            wizard.selectDevice(radioUnitNames[i]);
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            wizard.clickAccept();
            checkPopup();
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
        }
    }

    @Test(priority = 11)
    @Description("Create hosting relation between Cell 4G and RAN Antenna Array")
    public void hostCell4GOnRANAntennaArray() {
        for (int i = 0; i < 3; i++) {
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            cellSiteConfigurationPage.selectTreeRow(cellNames[i]);
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            cellSiteConfigurationPage.clickPlusIconAndSelectOption("Host on Antenna Array");
            HostingWizardPage wizard = new HostingWizardPage(driver);
            wizard.selectArray(antennaNames[i]);                                                //to nie są w sumie anteny tylko arraye
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            wizard.clickAccept();
            checkPopup();
        }
    }

    @Test(priority = 12)
    public void finishHLP() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.completeTask(processNRPCode, "High Level Planning");
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getText())
                .contains("Task properly completed.");
    }

    @Test(priority = 13)
    public void startLLP() {
        tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.startTask(processNRPCode, "Low Level Planning");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getText())
                .isEqualTo("The task properly assigned.");
        String currentUrl = driver.getCurrentUrl();
        String[] split = currentUrl.split(Pattern.quote("?"));
        perspectiveContext = split[1];
        Assertions.assertThat(perspectiveContext).contains("PLAN");
    }

    @Test(priority = 14)
    public void validateProjectPlan() {
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        tasksPage.findTask(processNRPCode, "Low Level Planning");
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
        oldInventoryViewPage.filterObject("Name", locationName, "Site");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        oldInventoryViewPage.expandShowOnAndChooseView("Cell Site Configuration");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        CellSiteConfigurationPage cellSiteConfigurationPage = new CellSiteConfigurationPage(driver);
        cellSiteConfigurationPage.expandTreeToBaseStation("Site", locationName, eNodeBName);

        for (int i = 0; i < 3; i++) {
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            cellSiteConfigurationPage.selectRowByAttributeValueWithLabel("Name", cellNames[i]);
        }

        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cellSiteConfigurationPage.clickEditIcon();
        EditCell4GWizardPage editCell4GWizardPage = new EditCell4GWizardPage(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        editCell4GWizardPage.setPCIBulk(pci);//
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        editCell4GWizardPage.setRSIBulk(rsi);//
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        editCell4GWizardPage.setReferencePowerBulk(referencePower);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        editCell4GWizardPage.setTAC(1, tac);//
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        editCell4GWizardPage.setTAC(2, tac);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        editCell4GWizardPage.setTAC(3, tac);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        editCell4GWizardPage.setPaOutputBulk(paOutput);//
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        editCell4GWizardPage.accept();
        //jakis popup?

        //resource signal power
        //bandwidth DL i UL
        //Pb
        //local cell id
        //mimo
        //pa
    }

    @Test(priority = 16)
    public void finishLowLevelPlanningTask() { //object names cannot be the same, otherwise validation will fail
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        PerspectiveChooser perspectiveChooser = PerspectiveChooser.create(driver, webDriverWait);
        perspectiveChooser.setCurrentTask();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        tasksPage.completeTask(processNRPCode, "Low Level Planning");
        checkTaskCompletedPopup();

        tasksPage.startTask(processNRPCode, "Ready for Integration");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        checkTaskAssignmentPopup();

        tasksPage.completeTask(processNRPCode, "Ready for Integration");
        checkTaskCompletedPopup();

    }

    @Test(priority = 17)
    public void integrationProject() {
        tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        tasksPage.startTask(processNRPCode, "Integrate");
        checkTaskAssignmentPopup();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        tasksPage.completeTask(processNRPCode, "Integrate"); //Scope definition
        checkTaskCompletedPopup();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        tasksPage.startTask(processNRPCode, "Implement"); //Implementation
        checkTaskAssignmentPopup();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        tasksPage.completeTask(processNRPCode, "Implement");
        checkTaskCompletedPopup();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        tasksPage.startTask(processNRPCode, "Accept");//Acceptance
        checkTaskAssignmentPopup();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        tasksPage.completeTask(processNRPCode, "Accept");
        checkTaskCompletedPopup();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        tasksPage.startTask(processNRPCode, "Acceptance"); //Accepted
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        tasksPage.completeTask(processNRPCode, "Acceptance");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        DelayUtils.sleep(80000);

        //transition to verify
    }

    private void checkPopup() {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assertions.assertThat(messages).hasSize(1);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getMessageType())
                .isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
    }

    private void checkTaskAssignmentPopup() {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getText())
                .isEqualTo("The task properly assigned.");
    }

    private void checkTaskCompletedPopup() {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getText())
                .contains("Task properly completed.");
    }

}

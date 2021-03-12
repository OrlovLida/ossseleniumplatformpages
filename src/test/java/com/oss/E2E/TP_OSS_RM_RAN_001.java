package com.oss.E2E;

import com.oss.BaseTestCase;
import com.oss.framework.alerts.SystemMessageContainer;
import com.oss.framework.alerts.SystemMessageInterface;
import com.oss.framework.mainheader.PerspectiveChooser;
import com.oss.framework.sidemenu.SideMenu;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.bpm.PlanViewWizardPage;
import com.oss.pages.bpm.ProcessWizardPage;
import com.oss.pages.bpm.TasksPage;
import com.oss.pages.physical.DeviceWizardPage;
import com.oss.pages.platform.HomePage;
import com.oss.pages.platform.OldInventoryViewPage;
import com.oss.pages.radio.*;
import com.oss.pages.reconciliation.CmDomainWizardPage;
import com.oss.pages.reconciliation.NetworkDiscoveryControlViewPage;
import com.oss.pages.reconciliation.NetworkInconsistenciesViewPage;
import com.oss.pages.reconciliation.SamplesManagementPage;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import org.assertj.core.api.Assertions;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;
import java.util.regex.Pattern;

public class TP_OSS_RM_RAN_001 extends BaseTestCase {

    private String processNRPCode;
    private String locationName = "XYZ_SeleniumTests";
    private String eNodeBName = "GBM055TST";
    private String eNodeBid = "1" + (int) (Math.random() * 10);
    private String eNodeBModel = "HUAWEI Technology Co.,Ltd BTS5900";
    private String MCCMNCPrimary = "DU [mcc: 424, mnc: 03]";

    private String baseBandUnitModel = "HUAWEI Technology Co.,Ltd BBU5900";
    private String bbuName = "BTS5900,GBM055TST/0/BBU5900,0";
    private String radioUnitModel = "HUAWEI Technology Co.,Ltd RRU5301";
    private String radioUnitNames[] = {"BTS5900,GBM055TST/0/MRRU,60",
            "BTS5900,GBM055TST/0/MRRU,70",
            "BTS5900,GBM055TST/0/MRRU,80"};

    private String ranAntennaModel = "HUAWEI Technology Co.,Ltd APE4518R14V06";
    private String[] antennaNames = {"TP_OSS_RM_RAN_001_ANTENNA_1", "TP_OSS_RM_RAN_001_ANTENNA_2", "TP_OSS_RM_RAN_001_ANTENNA_3"};
    private String bbuEquipmentType = "Base Band Unit";
    private String radioUnitEquipmentType = "Remote Radio Head/Unit";
    private String carrier = "L800-B20-5 (6175)";
    private String cellNames[] = new String[]{"TP_OSS_RM_RAN_001_CELL10", "TP_OSS_RM_RAN_001_CELL20", "TP_OSS_RM_RAN_001_CELL30"};
    private int amountOfCells = cellNames.length;
    public String perspectiveContext;
    public String pci = "2";
    public String rsi = "2";
    public String referencePower = "0";
    public String tac = "2";
    public String paOutput = "2";

    private String hostingTabName = "Hosting";

    private CellSiteConfigurationPage cellSiteConfigurationPage;
    private AntennaArrayWizardPage antennaArrayWizardPage;
    private TasksPage tasksPage;

    private NetworkDiscoveryControlViewPage networkDiscoveryControlViewPage;
    private String cmDomainName = "Selenium-TP-OSS-RM-RAN-001";
    private String cmInterface = "Huawei U2000 RAN";
    private String[] inconsistenciesNames = {
            "ENODEB-GBM055TST",
            "PhysicalElement-BTS5900,GBM055TST/0/BBU5900,0",
            "PhysicalElement-BTS5900,GBM055TST/0/MRRU,60",
            "PhysicalElement-BTS5900,GBM055TST/0/MRRU,70",
            "PhysicalElement-BTS5900,GBM055TST/0/MRRU,80"
    };

    @BeforeClass
    public void goToBPM() {
        HomePage homePage = new HomePage(driver);
        homePage.goToHomePage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        SideMenu sideMenu = SideMenu.create(driver, webDriverWait);
        sideMenu.callActionByLabel("Process Instances", "Views", "Business Process Management");
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
        checkPopupText("The task properly assigned.");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 3)
    public void findLocation() {
        openCellSiteConfiguration();
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
        checkPopupText("Cells 4G created success");
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

    @Test(priority = 9)
    @Description("Create hosting relation between eNodeB and BBU")
    public void hostENodeBOnBBU() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cellSiteConfigurationPage.selectTreeRow(eNodeBName);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cellSiteConfigurationPage.selectTab(hostingTabName);

        cellSiteConfigurationPage.useTableContextActionByLabel("Host on Device");
        HostingWizardPage wizard = new HostingWizardPage(driver);
        wizard.onlyCompatible("false");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        wizard.setDevice(bbuName);
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
            wizard.setDevice(radioUnitNames[i]);
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
            wizard.setHostingContains(antennaNames[i]);
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            wizard.clickAccept();
            checkPopup();
        }
    }

    @Test(priority = 12, enabled = false)
    @Description("Finish HLP")
    public void finishHLP() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        openTasksView();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        tasksPage.completeTask(processNRPCode, "High Level Planning");
        checkPopupText("Task properly completed.");
    }

    @Test(priority = 13)
    @Description("Start Low Level Planning")
    public void startLLP() {
        openTasksView();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        tasksPage.startTask(processNRPCode, "Low Level Planning");
        checkPopupText("The task properly assigned.");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        String currentUrl = driver.getCurrentUrl();
        String[] split = currentUrl.split(Pattern.quote("?"));
        perspectiveContext = split[1];
        Assertions.assertThat(perspectiveContext).contains("PLAN");
    }

    @Test(priority = 14)
    @Description("Check validation results")
    public void validateProjectPlan() {
        openTasksView();
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
    @Description("Complete cells configuration")
    public void lowLevelLogicalDesign() {
        HomePage homePage = new HomePage(driver);
        homePage.goToHomePage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        homePage.setOldObjectType("Site");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        OldInventoryViewPage oldInventoryViewPage = new OldInventoryViewPage(driver);
        oldInventoryViewPage.filterObject("Name", locationName);
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
        editCell4GWizardPage.setPCIBulk(pci);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        editCell4GWizardPage.setRSIBulk(rsi);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        editCell4GWizardPage.setReferencePowerBulk(referencePower);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        editCell4GWizardPage.setTAC(1, tac);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        editCell4GWizardPage.setTAC(2, tac);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        editCell4GWizardPage.setTAC(3, tac);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        editCell4GWizardPage.setPaOutputBulk(paOutput);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        editCell4GWizardPage.accept();
        //TODO check popup
    }

    @Test(priority = 16)
    @Description("Finish Low Level Planning")
    public void finishLowLevelPlanningTask() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        PerspectiveChooser perspectiveChooser = PerspectiveChooser.create(driver, webDriverWait);
        perspectiveChooser.setCurrentTask();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        tasksPage.completeTask(processNRPCode, "Low Level Planning");
        checkPopupText("Task properly completed.");

        tasksPage.startTask(processNRPCode, "Ready for Integration");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        checkPopupText("The task properly assigned.");

        tasksPage.completeTask(processNRPCode, "Ready for Integration");
        checkPopupText("Task properly completed.");
    }

    @Test(priority = 17)
    @Description("Finish NRP")
    public void completeProcessNRP() {
        openTasksView();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        tasksPage.startTask(processNRPCode, "Integrate");
        checkPopupText("The task properly assigned.");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        tasksPage.completeTask(processNRPCode, "Integrate");
        checkPopupText("Task properly completed.");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        tasksPage.startTask(processNRPCode, "Implement");
        checkPopupText("The task properly assigned.");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        tasksPage.completeTask(processNRPCode, "Implement");
        checkPopupText("Task properly completed.");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        tasksPage.startTask(processNRPCode, "Accept");
        checkPopupText("The task properly assigned.");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        tasksPage.completeTask(processNRPCode, "Accept");
        checkPopupText("Task properly completed.");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        tasksPage.startTask(processNRPCode, "Acceptance");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        tasksPage.completeTask(processNRPCode, "Acceptance");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        tasksPage.startTask(processNRPCode, "Verification");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        tasksPage.completeTask(processNRPCode, "Verification");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 18)
    @Description("Create CM domain")
    public void createCmDomain() {
        openNetworkDiscoveryControlView();

        networkDiscoveryControlViewPage.openCmDomainWizard();
        CmDomainWizardPage wizard = new CmDomainWizardPage(driver);
        wizard.setName(cmDomainName);
        wizard.setInterface(cmInterface);
        wizard.setDomain("RAN");
        wizard.save();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 19)
    @Description("Upload samples")
    public void uploadSamples() {
        DelayUtils.sleep(1000);
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(cmDomainName);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkDiscoveryControlViewPage.moveToSamplesManagement();
        SamplesManagementPage samplesManagementPage = new SamplesManagementPage(driver);
        samplesManagementPage.selectPath();
        samplesManagementPage.createDirectory(cmDomainName);
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
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(cmDomainName);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkDiscoveryControlViewPage.runReconciliation();
        networkDiscoveryControlViewPage.checkReconciliationStartedSystemMessage();
        networkDiscoveryControlViewPage.waitForEndOfReco();
    }

    @Test(priority = 21)
    @Description("Check operation type and accept discrepancies")
    public void checkOperationTypeAndAcceptDiscrepancies() {
        networkDiscoveryControlViewPage.moveToNivFromNdcv();
        NetworkInconsistenciesViewPage networkInconsistenciesViewPage = new NetworkInconsistenciesViewPage(driver);
        networkInconsistenciesViewPage.expandTree();
        for (String inconsistencieName : inconsistenciesNames) {
            Assertions.assertThat(networkInconsistenciesViewPage.checkInconsistenciesOperationType().equals("MODIFICATION")).isTrue();
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
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(cmDomainName);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkDiscoveryControlViewPage.clearOldNotifications();
        networkDiscoveryControlViewPage.deleteCmDomain();
        networkDiscoveryControlViewPage.checkDeleteCmDomainSystemMessage();
        networkDiscoveryControlViewPage.checkDeleteCmDomainNotification(cmDomainName);
    }

    @Test(priority = 23)
    @Description("Delete antennas, BBU, RRU")
    public void deleteDevices() {
        openCellSiteConfiguration();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cellSiteConfigurationPage.filterObject("Name", bbuName);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cellSiteConfigurationPage.removeObject();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        checkPopup();

        for (int i = 0; i < 3; i++) {
            cellSiteConfigurationPage.filterObject("Name", radioUnitNames[i]);
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            cellSiteConfigurationPage.removeObject();
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            checkPopup();

            cellSiteConfigurationPage.filterObject("Name", antennaNames[i]);
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            cellSiteConfigurationPage.removeObject();
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            checkPopup();
        }
    }

    @Test(priority = 24)
    @Description("Delete eNodeB")
    public void deleteNodeB() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cellSiteConfigurationPage.expandTreeToLocation("Site", locationName);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cellSiteConfigurationPage.selectRowByAttributeValueWithLabel("Name", eNodeBName);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cellSiteConfigurationPage.removeObject();
    }

    public void openNetworkDiscoveryControlView() {
        networkDiscoveryControlViewPage = NetworkDiscoveryControlViewPage.goToNetworkDiscoveryControlViewPage(driver, BASIC_URL);
    }

    public void openCellSiteConfiguration() {
        HomePage homePage = new HomePage(driver);
        homePage.goToHomePage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        homePage.setOldObjectType("Site");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        OldInventoryViewPage oldInventoryViewPage = new OldInventoryViewPage(driver);
        oldInventoryViewPage.filterObject("Name", locationName);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        oldInventoryViewPage.expandShowOnAndChooseView("Cell Site Configuration");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    public void openTasksView() {
        HomePage homePage = new HomePage(driver);
        homePage.goToHomePage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        SideMenu sideMenu = SideMenu.create(driver, webDriverWait);
        sideMenu.callActionByLabel("Tasks", "Views", "Business Process Management");
    }

    private void checkPopup() {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assertions.assertThat(messages).hasSize(1);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getMessageType())
                .isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
    }

    private void checkPopupText(String text) {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getText())
                .isEqualTo(text);
    }
}

package com.oss.E2E;

import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.bpm.CreateProcessNRPTest;
import com.oss.framework.alerts.SystemMessageContainer;
import com.oss.framework.alerts.SystemMessageContainer.Message;
import com.oss.framework.alerts.SystemMessageContainer.MessageType;
import com.oss.framework.alerts.SystemMessageInterface;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.mainheader.Notifications;
import com.oss.framework.sidemenu.SideMenu;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.bpm.ProcessWizardPage;
import com.oss.pages.bpm.TasksPage;
import com.oss.pages.filtermanager.ShareFilterPage;
import com.oss.pages.mediation.CLIConfigurationWizardPage;
import com.oss.pages.mediation.ViewConnectionConfigurationPage;
import com.oss.pages.physical.DeviceWizardPage;
import com.oss.pages.platform.HierarchyViewPage;
import com.oss.pages.platform.HomePage;
import com.oss.pages.platform.LogManagerPage;
import com.oss.pages.platform.NewInventoryViewPage;
import com.oss.pages.radio.ConnectionWizardPage;
import com.oss.pages.reconciliation.CmDomainWizardPage;
import com.oss.pages.reconciliation.NetworkDiscoveryControlViewPage;
import com.oss.pages.reconciliation.NetworkDiscoveryControlViewPage.IssueLevel;
import com.oss.pages.reconciliation.NetworkInconsistenciesViewPage;
import com.oss.pages.reconciliation.SamplesManagementPage;
import com.oss.pages.templateCM.ChangeConfigurationPage;
import com.oss.pages.templateCM.SetParametersWizardPage;
import com.oss.pages.transport.NetworkViewPage;
import com.oss.pages.transport.ipam.IPAddressAssignmentWizardPage;
import com.oss.pages.transport.ipam.IPAddressManagementViewPage;
import com.oss.pages.transport.ipam.helper.IPAddressAssignmentWizardProperties;

import io.qameta.allure.Description;

import static com.oss.framework.components.inputs.Input.ComponentType.TEXT_FIELD;

public class UC_OSS_RM_PLA_002 extends BaseTestCase {
    private NetworkDiscoveryControlViewPage networkDiscoveryControlViewPage;

    private static final String DEVICE_MODEL = "CISCO1941/K9";
    private static final String LOCATION_NAME = "Poznan-BU1";
    private static final String CM_DOMAIN_NAME = "SeleniumE2ETest";
    private static final String DEVICE_NAME = "H3_Lab";
    private static final String PORT_NAME = "GE 1";
    private static final String TRAIL_NAME = "SeleniumTest IP Link";
    private static final String INTERFACE_NAME = "CISCO IOS 12/15/XE without mediation";
    private static final String TEMPLATE_NAME = "E2E_Test_Loopback_v2";
    private static final String ADDRESS = "10.10.20.11";
    private static final String PORT = "22";
    private static final String PASSWORD = "cisco";
    private static final String COMMAND_TIMEOUT = "20";
    private static final String CONNECTION_TIMEOUT = "20";
    private static final String IP_NETWORK = "E2ESeleniumTest";
    private static final String TEMPLATE_EXECUTION_NOTIFICATION = "Script execution finished";

    private String serialNumber = "SN-" + (int) (Math.random() * 1001);
    private String processIPCode;
    private String processNRPCode;

    String URL = "";

    @BeforeClass
    public void openProcessInstancesPage() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        homePage.chooseFromLeftSideMenu("Process Instances", "Views", "Business Process Management");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 1)
    @Description("Create and start NRP Process")
    public void createProcessNRP() {
        ProcessWizardPage processWizardPage = new ProcessWizardPage(driver);
        processNRPCode = processWizardPage.createSimpleNRP();
        checkMessageSize();
        checkMessageType();
        checkMessageContainsText(processNRPCode);
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.startTask(processNRPCode, TasksPage.HIGH_LEVEL_PLANNING_TASK);
        checkTaskAssignment();
    }

    @Test(priority = 2)
    @Description("Open Network View")
    public void openNetworkView() {
        HomePage homePage = new HomePage(driver);
        homePage.goToHomePage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        SideMenu sideMenu = SideMenu.create(driver, webDriverWait);
        sideMenu.callActionByLabel("LAB Network View", "Favourites", "SeleniumTests");
    }

    @Test(priority = 3)
    @Description("Select Location")
    public void selectLocation() {
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.expandDockedPanel("left");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.selectObjectInViewContent("Name", LOCATION_NAME);
    }

    @Test(priority = 4)
    @Description("Create Physical Device")
    public void createPhysicalDevice() {
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        networkViewPage.useContextAction("CREATE", "Create Device-null");
        DeviceWizardPage deviceWizardPage = new DeviceWizardPage(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.setModel(DEVICE_MODEL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        DelayUtils.sleep(1000);
        deviceWizardPage.setName(DEVICE_NAME);
        deviceWizardPage.setHostname(DEVICE_NAME);
        DelayUtils.sleep(1000);
        deviceWizardPage.setSerialNumber(serialNumber);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.next();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.setPreciseLocation(LOCATION_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.accept();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        checkMessageSize();
        checkMessageType();
    }

    @Test(priority = 5)
    @Description("Open Hierarchy View with newly created device")
    public void moveToHierarchyView() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        networkViewPage.expandDockedPanel("left");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.selectObjectInViewContent("Name", DEVICE_NAME);
        DelayUtils.sleep(5000); // naming has to recalculate, it doesn't show progress in the console
        networkViewPage.selectObjectInViewContent("Name", DEVICE_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.useContextAction(ActionsContainer.SHOW_ON_GROUP_ID, "Hierarchy View-null");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 6)
    @Description("Select Ethernet Interface in Hierarchy View and open it in New Inventory View")
    public void selectEthernetInterface() {
        HierarchyViewPage hierarchyViewPage = new HierarchyViewPage(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        hierarchyViewPage.expandTreeNode(DEVICE_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        hierarchyViewPage.expandTreeNode("Ports");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        hierarchyViewPage.expandTreeNode(PORT_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        hierarchyViewPage.expandTreeNode("Termination Points");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        hierarchyViewPage.expandTreeNode("EthernetInterface_TP");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        hierarchyViewPage.performSearch(PORT_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        hierarchyViewPage.selectNodeByPosition(7);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        hierarchyViewPage.useTreeContextAction(ActionsContainer.SHOW_ON_GROUP_ID, "OpenInventoryView");
    }

    @Test(priority = 7)
    @Description("Select Ethernet Interface in New Inventory View and Assign IP V4 Address")
    public void assignIpV4Address() {
        NewInventoryViewPage newInventoryViewPage = NewInventoryViewPage.getInventoryViewPage(driver, webDriverWait);
        newInventoryViewPage.selectFirstRow();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        newInventoryViewPage.callAction("CREATE", "AssignIPv4Host");
        IPAddressAssignmentWizardPage ipAddressAssignmentWizardPage = new IPAddressAssignmentWizardPage(driver);
        IPAddressAssignmentWizardProperties ipAddressAssignmentWizardProperties = IPAddressAssignmentWizardProperties.builder()
                .address(ADDRESS).subnet("10.10.20.0/24 [" + IP_NETWORK + "]").isPrimary("false").build();
        ipAddressAssignmentWizardPage.assignMoToIPAddress(ipAddressAssignmentWizardProperties);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 8)
    @Description("Open Network View and create IP Link")
    public void createIpLink() {
        openNetworkView();
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        networkViewPage.useContextAction("add_to_view_group", "Device-null");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.queryElementAndAddItToView("serialNumber", TEXT_FIELD, serialNumber);
        networkViewPage.expandDockedPanel("left");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.selectObjectInViewContent("Name", "H1");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.useContextAction("CREATE", "Create Connection-null");
        networkViewPage.selectTrailType("IP Link");
        networkViewPage.acceptTrailType();
        ConnectionWizardPage connectionWizardPage = new ConnectionWizardPage(driver);
        connectionWizardPage.setName(TRAIL_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        connectionWizardPage.clickNext();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        connectionWizardPage.selectConnectionTermination(1);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        connectionWizardPage.terminateCardComponent("No Card/Component");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        connectionWizardPage.terminatePort(PORT_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        connectionWizardPage.terminateTerminationPort(PORT_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        connectionWizardPage.selectConnectionTermination(2);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        connectionWizardPage.terminateCardComponent("No Card/Component");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        connectionWizardPage.terminatePort(PORT_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        connectionWizardPage.terminateTerminationPort(PORT_NAME);
        connectionWizardPage.clickAccept();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.expandDockedPanel("left");
        networkViewPage.selectObjectInViewContent("Name", TRAIL_NAME + " (0%)");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.hideDockedPanel("left");
    }

    @Test(priority = 9)
    @Description("Suppress validation result about incomplete routing")
    public void suppressValidationResult() {
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        networkViewPage.expandDockedPanel("bottom");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.supressValidationResult("INCOMPLETE_ROUTING_STATUS", "Unnecessary in this scenario");
        networkViewPage.hideDockedPanel("bottom");
    }

    @Test(priority = 10)
    @Description("Create Mediation Configuration")
    public void createMediationConfiguration() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        networkViewPage.expandDockedPanel("left");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.selectObjectInViewContent("Name", TRAIL_NAME + " (0%)");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.selectObjectInViewContent("Name", DEVICE_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.useContextAction("CREATE", "Create Mediation Configuration-null");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.clickProceed();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        CLIConfigurationWizardPage cliConfigurationWizardPage = new CLIConfigurationWizardPage(driver);
        cliConfigurationWizardPage.setInputMethod("Search in managed addresses");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cliConfigurationWizardPage.setIPHostAddress(ADDRESS);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cliConfigurationWizardPage.setPort(PORT);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cliConfigurationWizardPage.clickNextStep();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cliConfigurationWizardPage.setCommandTimeout(COMMAND_TIMEOUT);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cliConfigurationWizardPage.setConnectionSetupTimeout(CONNECTION_TIMEOUT);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cliConfigurationWizardPage.setCLIProtocol("SSH");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cliConfigurationWizardPage.clickNextStep();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cliConfigurationWizardPage.setAuthMethod("Password Authentication");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cliConfigurationWizardPage.setAuthPassword(PASSWORD);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cliConfigurationWizardPage.clickNextStep();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cliConfigurationWizardPage.clickAccept();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        checkMessageSize();
        checkMessageType();
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        systemMessage.clickMessageLink();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        URL = driver.getCurrentUrl();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 11)
    @Description("Go through NRP task to IP Implementation task and click Perform Configuration")
    public void startImplementationTaskIP() {
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        processIPCode = tasksPage.proceedNRPToImplementationTask(processNRPCode);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Notifications.create(driver, webDriverWait).clearAllNotification();
        tasksPage.findTask(processIPCode, TasksPage.IMPLEMENTATION_TASK);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        DelayUtils.sleep(2000);
        tasksPage.clickPerformConfigurationButton();
    }

    @Test(priority = 12)
    @Description("Perform Configuration change using prepared CM Template")
    public void performConfigurationChange() {
        ChangeConfigurationPage changeConfigurationPage = new ChangeConfigurationPage(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        changeConfigurationPage.selectObjectType("Router");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        changeConfigurationPage.selectTemplate(TEMPLATE_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        changeConfigurationPage.clickSetParameters();
        SetParametersWizardPage setParametersWizardPage = new SetParametersWizardPage(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        String name = setParametersWizardPage.getParameter("$name[NEW_INVENTORY]");
        Assert.assertEquals(DEVICE_NAME, name);
        setParametersWizardPage.setParameter("$Password[SYSTEM]", "oss");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        setParametersWizardPage.setParameter("$InterfaceName[USER]", "GE 0");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        setParametersWizardPage.clickFillParameters();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        changeConfigurationPage.deployImmediately();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        DelayUtils.sleep(3000);
        ShareFilterPage shareFilterPage = new ShareFilterPage(driver);
        shareFilterPage.closeShareView();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        Notifications.create(driver, webDriverWait).openDetailsForSpecificNotification(TEMPLATE_NAME, TEMPLATE_EXECUTION_NOTIFICATION);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        LogManagerPage logManagerPage = new LogManagerPage(driver);
        Assert.assertEquals(logManagerPage.getStatus(), "UPLOAD_SUCCESS");
    }

    @Test(priority = 13)
    @Description("Assign File to Process")
    public void assignFile() {
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        try {
            java.net.URL resource = CreateProcessNRPTest.class.getClassLoader().getResource("bpm/SeleniumTest.txt");
            assert resource != null;
            String absolutePatch = Paths.get(resource.toURI()).toFile().getAbsolutePath();
            tasksPage.addFile(processIPCode, TasksPage.IMPLEMENTATION_TASK, absolutePatch);
            checkMessageType();
        } catch (URISyntaxException e) {
            throw new RuntimeException("Cannot load file", e);
        }
        DelayUtils.sleep(2000);
        List<String> files = tasksPage.getListOfAttachments();
        Assertions.assertThat(files.get(0)).contains("SeleniumTest");
        Assertions.assertThat(files.size()).isGreaterThan(0);
    }

    @Test(priority = 14)
    @Description("Complete IP and NRP process")
    public void completeIpAndNrp() {
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.completeTask(processIPCode, TasksPage.IMPLEMENTATION_TASK);
        checkTaskCompleted();
        tasksPage.startTask(processIPCode, TasksPage.ACCEPTANCE_TASK);
        checkTaskAssignment();
        tasksPage.completeTask(processIPCode, TasksPage.ACCEPTANCE_TASK);
        checkTaskCompleted();
        tasksPage.startTask(processNRPCode, TasksPage.VERIFICATION_TASK);
        checkTaskAssignment();
        tasksPage.completeTask(processNRPCode, TasksPage.VERIFICATION_TASK);
        checkTaskCompleted();
    }

    @Test(priority = 15)
    @Description("Go to Network Discovery Control View and create CM Domain")
    public void createCmDomain() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkDiscoveryControlViewPage = NetworkDiscoveryControlViewPage.goToNetworkDiscoveryControlViewPage(driver, BASIC_URL);
        networkDiscoveryControlViewPage.openCmDomainWizard();
        CmDomainWizardPage wizard = new CmDomainWizardPage(driver);
        wizard.setName(CM_DOMAIN_NAME);
        wizard.setInterface(INTERFACE_NAME);
        wizard.setDomain("IP");
        wizard.save();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 16)
    @Description("Upload reconciliation samples")
    public void uploadSamples() {
        DelayUtils.sleep(1000);
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(CM_DOMAIN_NAME);
        networkDiscoveryControlViewPage.moveToSamplesManagement();
        SamplesManagementPage samplesManagementPage = new SamplesManagementPage(driver);
        samplesManagementPage.selectPath();
        samplesManagementPage.createDirectory(CM_DOMAIN_NAME);
        DelayUtils.sleep(1000);
        samplesManagementPage.uploadSamples("recoSamples/ciscoE2E/H3_Lab_100.100.100.100_20181016_1500_sh_inventory_raw.cli");
        DelayUtils.sleep(1000);
        samplesManagementPage.uploadSamples("recoSamples/ciscoE2E/H3_Lab_100.100.100.100_20181016_1500_sh_version.cli");
    }

    @Test(priority = 17)
    @Description("Run reconciliation and check if it ended without errors")
    public void runReconciliation() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        NetworkDiscoveryControlViewPage networkDiscoveryControlViewPage = NetworkDiscoveryControlViewPage.goToNetworkDiscoveryControlViewPage(driver, BASIC_URL);
        DelayUtils.sleep(100);
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(CM_DOMAIN_NAME);
        networkDiscoveryControlViewPage.runReconciliation();
        networkDiscoveryControlViewPage.checkReconciliationStartedSystemMessage();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkDiscoveryControlViewPage.waitForEndOfReco();
        networkDiscoveryControlViewPage.selectLatestReconciliationState();
        Assert.assertTrue(networkDiscoveryControlViewPage.checkIssues(IssueLevel.STARTUP_FATAL));
        Assert.assertTrue(networkDiscoveryControlViewPage.checkIssues(IssueLevel.FATAL));
        Assert.assertTrue(networkDiscoveryControlViewPage.checkIssues(IssueLevel.ERROR));
        Assert.assertTrue(networkDiscoveryControlViewPage.checkIssues(IssueLevel.WARNING));
    }

    @Test(priority = 18)
    @Description("Apply inconsistencies from Network to Live")
    public void applyInconsistencies() {
        networkDiscoveryControlViewPage.moveToNivFromNdcv();
        NetworkInconsistenciesViewPage networkInconsistenciesViewPage = new NetworkInconsistenciesViewPage(driver);
        networkInconsistenciesViewPage.expandTree();
        networkInconsistenciesViewPage.clearOldNotification();
        networkInconsistenciesViewPage.applyInconsistencies();
        DelayUtils.sleep(5000);
        networkInconsistenciesViewPage.checkNotificationAfterApplyInconsistencies(DEVICE_NAME);
    }

    @Test(priority = 19)
    @Description("Delete CM Domain")
    public void deleteCmDomain() {
        networkDiscoveryControlViewPage = NetworkDiscoveryControlViewPage.goToNetworkDiscoveryControlViewPage(driver, BASIC_URL);
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(CM_DOMAIN_NAME);
        networkDiscoveryControlViewPage.clearOldNotifications();
        networkDiscoveryControlViewPage.deleteCmDomain();
        networkDiscoveryControlViewPage.checkDeleteCmDomainSystemMessage();
        networkDiscoveryControlViewPage.checkDeleteCmDomainNotification(CM_DOMAIN_NAME);
    }

    @Test(priority = 20)
    @Description("Delete IP Link")
    public void deleteIpLink() {
        HomePage homePage = new HomePage(driver);
        homePage.goToHomePage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        SideMenu sideMenu = SideMenu.create(driver, webDriverWait);
        sideMenu.callActionByLabel("LAB Network View", "Favourites", "SeleniumTests");
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        networkViewPage.useContextAction("add_to_view_group", "Connection-null");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.queryElementAndAddItToView("label", TEXT_FIELD, TRAIL_NAME);
        networkViewPage.useContextAction("EDIT", "Delete Connection-null");
        networkViewPage.delateTrailWizard();
    }

    @Test(priority = 21)
    @Description("Delete Physical Device")
    public void deletePhysicalDevice() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        DelayUtils.sleep(5000);
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        networkViewPage.useContextAction("add_to_view_group", "Device-null");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.queryElementAndAddItToView("name", TEXT_FIELD, DEVICE_NAME);
        networkViewPage.useContextAction("EDIT", "Delete Element-null");
        networkViewPage.clickConfirmationBoxButtonByLabel("Yes");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        checkMessageSize();
        checkMessageType();
    }

    @Test(priority = 22)
    @Description("Delete Mediation Connection")
    public void deleteMediation() {
        ViewConnectionConfigurationPage.goToViewConnectionConfigurationPage(driver, URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        ViewConnectionConfigurationPage viewConnectionConfigurationPage = new ViewConnectionConfigurationPage(driver);
        viewConnectionConfigurationPage.selectRow("Address", ADDRESS);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        viewConnectionConfigurationPage.useContextAction(ActionsContainer.EDIT_GROUP_ID, ViewConnectionConfigurationPage.DELETE_BUTTON_ID);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        viewConnectionConfigurationPage.clickDelete();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 23)
    @Description("Delete IP Address Assignment")
    public void deleteIPAddressAssignment() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        IPAddressManagementViewPage ipAddressManagementViewPage = IPAddressManagementViewPage.goToIPAddressManagementPage(driver, BASIC_URL);
        ipAddressManagementViewPage.searchIpNetwork(IP_NETWORK);
        ipAddressManagementViewPage.expandTreeRow(IP_NETWORK);
        ipAddressManagementViewPage.expandTreeRowContains("%");
        ipAddressManagementViewPage.expandTreeRow(ADDRESS + "/24");
        ipAddressManagementViewPage.deleteHostAssignment("/24 [");
    }

    private void checkMessageType() {
        Assert.assertEquals(MessageType.SUCCESS, (getFirstMessage().getMessageType()));
    }

    private void checkMessageContainsText(String message) {
        Assert.assertTrue((getFirstMessage().getText())
                .contains(message));
    }

    private void checkMessageText() {
        Assert.assertEquals("The task properly assigned.", (getFirstMessage().getText()));
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
        checkMessageText();
    }

    private void checkTaskCompleted() {
        checkMessageType();
        checkMessageContainsText("Task properly completed.");
    }
}

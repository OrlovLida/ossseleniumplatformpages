package com.oss.E2E;

import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.List;

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
import com.oss.framework.prompts.ConfirmationBox;
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
import com.oss.pages.template_cm.ChangeConfigurationPage;
import com.oss.pages.template_cm.SetParametersWizardPage;
import com.oss.pages.transport.NetworkViewPage;
import com.oss.pages.transport.ipam.IPAddressAssignmentWizardPage;
import com.oss.pages.transport.ipam.IPAddressManagementViewPage;
import com.oss.pages.transport.ipam.helper.IPAddressAssignmentWizardProperties;

import io.qameta.allure.Step;

import static com.oss.framework.components.inputs.Input.ComponentType.TEXT_FIELD;

public class UC_OSS_RM_PLA_002_Test extends BaseTestCase {
    private NetworkDiscoveryControlViewPage networkDiscoveryControlViewPage;

    private static final String DEVICE_MODEL = "CISCO1941/K9";
    private static final String LOCATION_NAME = "Poznan-BU1";
    private static final String CM_DOMAIN_NAME = "SeleniumE2ETest";
    private static final String DEVICE_NAME = "H3_Lab";
    private static final String PORT_NAME = "GE 1";
    private static final String TRAIL_NAME = "SeleniumTest IP Link";
    private static final String INTERFACE_NAME = "CISCO IOS 12/15/XE without mediation";
    private static final String TEMPLATE_NAME = "E2E_Test_Loopback_v2";
    private static final String ADDRESS = "10.10.20.171";
    private static final String PORT = "22";
    private static final String PASSWORD = "cisco";
    private static final String COMMAND_TIMEOUT = "20";
    private static final String CONNECTION_TIMEOUT = "20";
    private static final String IP_NETWORK = "E2ESeleniumTest";
    private static final String TEMPLATE_EXECUTION_NOTIFICATION = "Script execution finished";
    private static final String BUSINESS_PROCESS_MANAGEMENT = "Business Process Management";
    private static final String BPM_AND_PLANNING = "BPM and Planning";
    private static final String PROCESS_INSTANCES = "Process Instances";
    private static final String BOOKMARKS = "Bookmarks";
    private static final String FAVOURITES = "Favourites";
    private static final String LAB_NETWORK_VIEW = "LAB Network View";
    private static final String NAME = "Name";
    private static final String LEFT = "left";

    private String serialNumber = "SN-" + (int) (Math.random() * 1001);
    private String processIPCode;
    private String processNRPCode;

    String URL = "";

    @BeforeClass
    public void openConsole() {
        waitForPageToLoad();
    }

    @Test(priority = 1)
    @Step("Create and start NRP Process")
    public void createProcessNRP() {
        waitForPageToLoad();
        homePage.chooseFromLeftSideMenu(PROCESS_INSTANCES, BPM_AND_PLANNING, BUSINESS_PROCESS_MANAGEMENT);
        waitForPageToLoad();
        ProcessWizardPage processWizardPage = new ProcessWizardPage(driver);
        processNRPCode = processWizardPage.createSimpleNRP();
        checkMessageSize();
        checkMessageType(MessageType.SUCCESS);
        checkMessageContainsText(processNRPCode);
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.startTask(processNRPCode, TasksPage.HIGH_LEVEL_PLANNING_TASK);
        checkTaskAssignment();
    }

    @Test(priority = 2)
    @Step("Open Network View")
    public void openNetworkView() {
        HomePage homePage = new HomePage(driver);
        homePage.goToHomePage(driver, BASIC_URL);
        waitForPageToLoad();
        SideMenu sideMenu = SideMenu.create(driver, webDriverWait);
        sideMenu.callActionByLabel(LAB_NETWORK_VIEW, FAVOURITES, BOOKMARKS);
    }

    @Test(priority = 3)
    @Step("Select Location")
    public void selectLocation() {
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        waitForPageToLoad();
        networkViewPage.expandDockedPanel(LEFT);
        waitForPageToLoad();
        networkViewPage.selectObjectInViewContent(NAME, LOCATION_NAME);
    }

    @Test(priority = 4)
    @Step("Create Physical Device")
    public void createPhysicalDevice() {
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        networkViewPage.useContextAction(ActionsContainer.CREATE_GROUP_ID, NetworkViewPage.CREATE_DEVICE_ACTION);
        DeviceWizardPage deviceWizardPage = new DeviceWizardPage(driver);
        waitForPageToLoad();
        deviceWizardPage.setModel(DEVICE_MODEL);
        waitForPageToLoad();
        DelayUtils.sleep(1000);
        deviceWizardPage.setName(DEVICE_NAME);
        deviceWizardPage.setHostname(DEVICE_NAME);
        DelayUtils.sleep(1000);
        deviceWizardPage.setSerialNumber(serialNumber);
        waitForPageToLoad();
        deviceWizardPage.next();
        waitForPageToLoad();
        deviceWizardPage.setPreciseLocation(LOCATION_NAME);
        waitForPageToLoad();
        deviceWizardPage.accept();
        waitForPageToLoad();
        checkMessageSize();
        checkMessageType(MessageType.SUCCESS);
    }

    @Test(priority = 5)
    @Step("Open Hierarchy View with newly created device")
    public void moveToHierarchyView() {
        waitForPageToLoad();
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        networkViewPage.expandDockedPanel(LEFT);
        waitForPageToLoad();
        networkViewPage.selectObjectInViewContent(NAME, DEVICE_NAME);
        DelayUtils.sleep(5000); // naming has to recalculate, it doesn't show progress in the console
        networkViewPage.selectObjectInViewContent(NAME, DEVICE_NAME);
        waitForPageToLoad();
        networkViewPage.useContextAction(ActionsContainer.SHOW_ON_GROUP_ID, NetworkViewPage.HIERARCHY_VIEW_ACTION);
        waitForPageToLoad();
    }

    @Test(priority = 6)
    @Step("Select Ethernet Interface in Hierarchy View and open it in New Inventory View")
    public void selectEthernetInterface() {
        HierarchyViewPage hierarchyViewPage = new HierarchyViewPage(driver);
        waitForPageToLoad();
        String labelpath = DEVICE_NAME + ".Ports." + PORT_NAME + ".Termination Points.EthernetInterface_TP." + PORT_NAME;
        hierarchyViewPage.selectNodeByLabelsPath(labelpath);
        waitForPageToLoad();
        hierarchyViewPage.useTreeContextAction(ActionsContainer.SHOW_ON_GROUP_ID, "OpenInventoryView");
    }

    @Test(priority = 7)
    @Step("Select Ethernet Interface in New Inventory View and Assign IP V4 Address")
    public void assignIpV4Address() {
        NewInventoryViewPage newInventoryViewPage = NewInventoryViewPage.getInventoryViewPage(driver, webDriverWait);
        newInventoryViewPage.selectFirstRow();
        waitForPageToLoad();
        newInventoryViewPage.callAction(ActionsContainer.ASSIGN_GROUP_ID, "AssignIPv4Host");
        IPAddressAssignmentWizardPage ipAddressAssignmentWizardPage = new IPAddressAssignmentWizardPage(driver);
        IPAddressAssignmentWizardProperties ipAddressAssignmentWizardProperties = IPAddressAssignmentWizardProperties.builder()
                .address(ADDRESS).subnet("10.10.20.0/24 [" + IP_NETWORK + "]").isPrimary("false").build();
        ipAddressAssignmentWizardPage.assignMoToIPAddress(ipAddressAssignmentWizardProperties);
        waitForPageToLoad();
    }

    @Test(priority = 8)
    @Step("Open Network View and create IP Link")
    public void createIpLink() {
        openNetworkView();
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        networkViewPage.useContextAction(NetworkViewPage.ADD_TO_VIEW_ACTION, NetworkViewPage.DEVICE_ACTION);
        waitForPageToLoad();
        networkViewPage.queryElementAndAddItToView("serialNumber", TEXT_FIELD, serialNumber);
        networkViewPage.expandDockedPanel(LEFT);
        waitForPageToLoad();
        networkViewPage.selectObjectInViewContent(NAME, "H1");
        waitForPageToLoad();
        networkViewPage.useContextAction(ActionsContainer.CREATE_GROUP_ID, NetworkViewPage.CREATE_CONNECTION_ID);
        networkViewPage.selectTrailType("IP Link");
        networkViewPage.acceptTrailType();
        ConnectionWizardPage connectionWizardPage = new ConnectionWizardPage(driver);
        connectionWizardPage.setName(TRAIL_NAME);
        waitForPageToLoad();
        connectionWizardPage.clickNext();
        waitForPageToLoad();
        connectionWizardPage.selectConnectionTermination(1);
        waitForPageToLoad();
        connectionWizardPage.terminateCardComponent("No Card/Component");
        waitForPageToLoad();
        connectionWizardPage.terminatePort(PORT_NAME);
        waitForPageToLoad();
        connectionWizardPage.terminateTerminationPort(PORT_NAME);
        waitForPageToLoad();
        connectionWizardPage.selectConnectionTermination(2);
        waitForPageToLoad();
        connectionWizardPage.terminateCardComponent("No Card/Component");
        waitForPageToLoad();
        connectionWizardPage.terminatePort(PORT_NAME);
        waitForPageToLoad();
        connectionWizardPage.terminateTerminationPort(PORT_NAME);
        waitForPageToLoad();
        connectionWizardPage.clickAccept();
        waitForPageToLoad();
    }

    @Test(priority = 9)
    @Step("Suppress validation result about incomplete routing")
    public void suppressValidationResult() {
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        networkViewPage.expandDockedPanel("bottom");
        waitForPageToLoad();
        networkViewPage.supressValidationResult("Unnecessary in this scenario");
        networkViewPage.hideDockedPanel("bottom");
    }

    @Test(priority = 10)
    @Step("Create Mediation Configuration")
    public void createMediationConfiguration() {
        waitForPageToLoad();
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        networkViewPage.expandDockedPanel(LEFT);
        waitForPageToLoad();
        networkViewPage.selectObjectInViewContent(NAME, TRAIL_NAME + " (0%)");
        waitForPageToLoad();
        networkViewPage.selectObjectInViewContent(NAME, DEVICE_NAME);
        waitForPageToLoad();
        networkViewPage.useContextActionAndClickConfirmation(ActionsContainer.CREATE_GROUP_ID, NetworkViewPage.CREATE_MEDIATION_CONFIGURATION_ID, ConfirmationBox.PROCEED);
        waitForPageToLoad();
        CLIConfigurationWizardPage cliConfigurationWizardPage = new CLIConfigurationWizardPage(driver);
        cliConfigurationWizardPage.setInputMethod("Search in managed addresses");
        waitForPageToLoad();
        cliConfigurationWizardPage.setIPHostAddress(ADDRESS);
        waitForPageToLoad();
        cliConfigurationWizardPage.setPort(PORT);
        waitForPageToLoad();
        cliConfigurationWizardPage.setCommandTimeout(COMMAND_TIMEOUT);
        waitForPageToLoad();
        cliConfigurationWizardPage.setConnectionSetupTimeout(CONNECTION_TIMEOUT);
        waitForPageToLoad();
        cliConfigurationWizardPage.setCLIProtocol("SSH");
        waitForPageToLoad();
        cliConfigurationWizardPage.setAuthMethod("Password Authentication");
        waitForPageToLoad();
        cliConfigurationWizardPage.setAuthPassword(PASSWORD);
        waitForPageToLoad();
        cliConfigurationWizardPage.clickAccept();
        waitForPageToLoad();
        checkMessageSize();
        checkMessageType(MessageType.SUCCESS);
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        systemMessage.clickMessageLink();
        waitForPageToLoad();
        URL = driver.getCurrentUrl();
        systemMessage.close();
        waitForPageToLoad();
    }

    @Test(priority = 11)
    @Step("Go through NRP task to IP Implementation task and click Perform Configuration")
    public void startImplementationTaskIP() {
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        processIPCode = tasksPage.proceedNRPToImplementationTask(processNRPCode);
        waitForPageToLoad();
        Notifications.create(driver, webDriverWait).clearAllNotification();
        tasksPage.findTask(processIPCode, TasksPage.IMPLEMENTATION_TASK);
        waitForPageToLoad();
        tasksPage.clickPerformConfigurationButton();
    }

    @Test(priority = 12)
    @Step("Perform Configuration change using prepared CM Template")
    public void performConfigurationChange() {
        ChangeConfigurationPage changeConfigurationPage = new ChangeConfigurationPage(driver);
        waitForPageToLoad();
        changeConfigurationPage.selectObjectType("Router");
        waitForPageToLoad();
        changeConfigurationPage.selectTemplate(TEMPLATE_NAME);
        waitForPageToLoad();
        changeConfigurationPage.clickSetParameters();
        SetParametersWizardPage setParametersWizardPage = new SetParametersWizardPage(driver);
        waitForPageToLoad();
        String name = setParametersWizardPage.getName();
        Assert.assertEquals(name, DEVICE_NAME);
        setParametersWizardPage.setPassword("oss");
        waitForPageToLoad();
        setParametersWizardPage.setInterfaceName("GE 0");
        waitForPageToLoad();
        setParametersWizardPage.clickFillParameters();
        waitForPageToLoad();
        changeConfigurationPage.deployImmediately();
        waitForPageToLoad();
        ShareFilterPage shareFilterPage = new ShareFilterPage(driver);
        shareFilterPage.closeShareView();
        waitForPageToLoad();

        Notifications.create(driver, webDriverWait).openDetailsForSpecificNotification(TEMPLATE_NAME, TEMPLATE_EXECUTION_NOTIFICATION);
        waitForPageToLoad();
        LogManagerPage logManagerPage = new LogManagerPage(driver);
        Assert.assertEquals(logManagerPage.getStatus(), "UPLOAD_SUCCESS");
    }

    @Test(priority = 13)
    @Step("Assign File to Process")
    public void assignFile() {
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        try {
            java.net.URL resource = CreateProcessNRPTest.class.getClassLoader().getResource("bpm/SeleniumTest.txt");
            assert resource != null;
            String absolutePatch = Paths.get(resource.toURI()).toFile().getAbsolutePath();
            tasksPage.addFile(processIPCode, TasksPage.IMPLEMENTATION_TASK, absolutePatch);
            checkMessageType(MessageType.SUCCESS);
        } catch (URISyntaxException e) {
            throw new RuntimeException("Cannot load file", e);
        }
        DelayUtils.sleep(2000);
        List<String> files = tasksPage.getListOfAttachments();
        Assert.assertTrue((files.get(0)).contains("SeleniumTest"));
    }

    @Test(priority = 14)
    @Step("Complete IP and NRP process")
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
    @Step("Go to Network Discovery Control View and create CM Domain")
    public void createCmDomain() {
        waitForPageToLoad();
        networkDiscoveryControlViewPage = NetworkDiscoveryControlViewPage.goToNetworkDiscoveryControlViewPage(driver, BASIC_URL);
        networkDiscoveryControlViewPage.openCmDomainWizard();
        CmDomainWizardPage wizard = new CmDomainWizardPage(driver);
        wizard.setName(CM_DOMAIN_NAME);
        wizard.setInterface(INTERFACE_NAME);
        wizard.setDomain("IP");
        wizard.save();
        waitForPageToLoad();
    }

    @Test(priority = 16)
    @Step("Upload reconciliation samples")
    public void uploadSamples() throws URISyntaxException {
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
    @Step("Run reconciliation and check if it ended without errors")
    public void runReconciliation() {
        waitForPageToLoad();
        NetworkDiscoveryControlViewPage networkDiscoveryControlViewPage = NetworkDiscoveryControlViewPage.goToNetworkDiscoveryControlViewPage(driver, BASIC_URL);
        DelayUtils.sleep(100);
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(CM_DOMAIN_NAME);
        networkDiscoveryControlViewPage.runReconciliation();
        checkMessageType(MessageType.INFO);
        waitForPageToLoad();
        Assert.assertEquals(networkDiscoveryControlViewPage.waitForEndOfReco(), "SUCCESS");
        networkDiscoveryControlViewPage.selectLatestReconciliationState();
        Assert.assertTrue(networkDiscoveryControlViewPage.checkIssues(IssueLevel.STARTUP_FATAL));
        Assert.assertTrue(networkDiscoveryControlViewPage.checkIssues(IssueLevel.FATAL));
        Assert.assertTrue(networkDiscoveryControlViewPage.checkIssues(IssueLevel.ERROR));
        Assert.assertTrue(networkDiscoveryControlViewPage.checkIssues(IssueLevel.WARNING));
    }

    @Test(priority = 18)
    @Step("Apply inconsistencies from Network to Live")
    public void applyInconsistencies() {
        networkDiscoveryControlViewPage.moveToNivFromNdcv();
        NetworkInconsistenciesViewPage networkInconsistenciesViewPage = new NetworkInconsistenciesViewPage(driver);
        networkInconsistenciesViewPage.expandTree();
        networkInconsistenciesViewPage.clearOldNotification();
        networkInconsistenciesViewPage.applyInconsistencies();
        DelayUtils.sleep(1000);
        Assert.assertEquals(networkInconsistenciesViewPage.checkNotificationAfterApplyInconsistencies(), "Accepting discrepancies related to " + DEVICE_NAME + " finished");
    }

    @Test(priority = 19)
    @Step("Delete CM Domain")
    public void deleteCmDomain() {
        networkDiscoveryControlViewPage = NetworkDiscoveryControlViewPage.goToNetworkDiscoveryControlViewPage(driver, BASIC_URL);
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(CM_DOMAIN_NAME);
        networkDiscoveryControlViewPage.clearOldNotifications();
        networkDiscoveryControlViewPage.deleteCmDomain();
        checkMessageType(MessageType.INFO);
        Assert.assertEquals(networkDiscoveryControlViewPage.checkDeleteCmDomainNotification(), "Deleting CM Domain: " + CM_DOMAIN_NAME + " finished");
    }

    @Test(priority = 20)
    @Step("Delete IP Link")
    public void deleteIpLink() {
        HomePage homePage = new HomePage(driver);
        homePage.goToHomePage(driver, BASIC_URL);
        waitForPageToLoad();
        SideMenu sideMenu = SideMenu.create(driver, webDriverWait);
        sideMenu.callActionByLabel(LAB_NETWORK_VIEW, FAVOURITES, BOOKMARKS);
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        networkViewPage.useContextAction(NetworkViewPage.ADD_TO_VIEW_ACTION, NetworkViewPage.CONNECTION_ACTION);
        waitForPageToLoad();
        networkViewPage.queryElementAndAddItToView("label", TEXT_FIELD, TRAIL_NAME);
        networkViewPage.useContextActionAndClickConfirmation(ActionsContainer.EDIT_GROUP_ID, NetworkViewPage.DELETE_CONNECTION_ID, ConfirmationBox.DELETE);
    }

    @Test(priority = 21)
    @Step("Delete Mediation Connection")
    public void deleteMediation() {
        ViewConnectionConfigurationPage.goToViewConnectionConfigurationPage(driver, URL);
        waitForPageToLoad();
        ViewConnectionConfigurationPage viewConnectionConfigurationPage = new ViewConnectionConfigurationPage(driver);
        viewConnectionConfigurationPage.selectRow("Address", ADDRESS);
        waitForPageToLoad();
        viewConnectionConfigurationPage.useContextAction(ActionsContainer.EDIT_GROUP_ID, ViewConnectionConfigurationPage.DELETE_BUTTON_ID);
        waitForPageToLoad();
        viewConnectionConfigurationPage.clickDelete();
        waitForPageToLoad();
    }

    @Test(priority = 22)
    @Step("Delete IP Address Assignment")
    public void deleteIPAddressAssignment() {
        waitForPageToLoad();
        IPAddressManagementViewPage ipAddressManagementViewPage = IPAddressManagementViewPage.goToIPAddressManagementPage(driver, BASIC_URL);
        ipAddressManagementViewPage.searchIpNetwork(IP_NETWORK);
        ipAddressManagementViewPage.expandTreeRow(IP_NETWORK);
        ipAddressManagementViewPage.expandTreeRowContains("%");
        ipAddressManagementViewPage.deleteIPHost(ADDRESS + "/24");
    }

    @Test(priority = 23)
    @Step("Delete Physical Device")
    public void deletePhysicalDevice() {
        HomePage homePage = new HomePage(driver);
        homePage.goToHomePage(driver, BASIC_URL);
        waitForPageToLoad();
        SideMenu sideMenu = SideMenu.create(driver, webDriverWait);
        sideMenu.callActionByLabel(LAB_NETWORK_VIEW, FAVOURITES, BOOKMARKS);
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        waitForPageToLoad();
        networkViewPage.useContextAction(NetworkViewPage.ADD_TO_VIEW_ACTION, NetworkViewPage.DEVICE_ACTION);
        waitForPageToLoad();
        networkViewPage.queryElementAndAddItToView("name", TEXT_FIELD, DEVICE_NAME);
        networkViewPage.useContextActionAndClickConfirmation(ActionsContainer.EDIT_GROUP_ID, NetworkViewPage.DELETE_DEVICE_ACTION, ConfirmationBox.YES);
        checkMessageSize();
        checkMessageType(MessageType.SUCCESS);
        waitForPageToLoad();
    }

    private void checkMessageType(MessageType messageType) {
        Assert.assertEquals((getFirstMessage().getMessageType()), messageType);
    }

    private void checkMessageContainsText(String message) {
        Assert.assertTrue((getFirstMessage().getText())
                .contains(message));
    }

    private void checkMessageText() {
        Assert.assertEquals((getFirstMessage().getText()), "The task properly assigned.");
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
        checkMessageType(MessageType.SUCCESS);
        checkMessageText();
    }

    private void checkTaskCompleted() {
        checkMessageType(MessageType.SUCCESS);
        checkMessageContainsText("Task properly completed.");
    }

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }
}

package com.oss.E2E;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;

import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.comarch.oss.web.pages.GlobalSearchPage;
import com.comarch.oss.web.pages.HierarchyViewPage;
import com.comarch.oss.web.pages.LogManagerPage;
import com.comarch.oss.web.pages.NewInventoryViewPage;
import com.comarch.oss.web.pages.bookmarksanddashboards.bookmarks.BookmarkWizardPage;
import com.comarch.oss.web.pages.bookmarksanddashboards.bookmarks.NewBookmarksPage;
import com.comarch.oss.web.pages.filtermanager.ShareFilterPage;
import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.alerts.SystemMessageContainer.Message;
import com.oss.framework.components.alerts.SystemMessageContainer.MessageType;
import com.oss.framework.components.alerts.SystemMessageInterface;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.mainheader.ButtonPanel;
import com.oss.framework.components.mainheader.Notifications;
import com.oss.framework.components.mainheader.ToolbarWidget;
import com.oss.framework.components.prompts.ConfirmationBox;
import com.oss.framework.components.prompts.Popup;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.bpm.processinstances.ProcessOverviewPage;
import com.oss.pages.bpm.tasks.TasksPageV2;
import com.oss.pages.mediation.CLIConfigurationWizardPage;
import com.oss.pages.mediation.ViewConnectionConfigurationPage;
import com.oss.pages.physical.DeviceWizardPage;
import com.oss.pages.reconciliation.NetworkDiscoveryControlViewPage;
import com.oss.pages.reconciliation.NetworkInconsistenciesViewPage;
import com.oss.pages.reconciliation.SamplesManagementPage;
import com.oss.pages.templatecm.changeconfig.ChangeConfigurationPage;
import com.oss.pages.templatecm.changeconfig.SetParametersWizardPage;
import com.oss.pages.transport.NetworkViewPage;
import com.oss.pages.transport.ipam.IPAddressAssignmentWizardPage;
import com.oss.pages.transport.ipam.IPAddressManagementViewPage;
import com.oss.pages.transport.ipam.helper.IPAddressAssignmentWizardProperties;
import com.oss.pages.transport.trail.ConnectionWizardPage;
import com.oss.repositories.AddressRepository;
import com.oss.repositories.IPAMRepository;
import com.oss.repositories.LocationInventoryRepository;
import com.oss.repositories.PhysicalInventoryRepository;
import com.oss.services.ResourceCatalogClient;
import com.oss.template.infrastructure.template.folder.TemplateFolderClient;
import com.oss.untils.Environment;

import io.qameta.allure.Description;

import static com.oss.framework.components.icons.interactiveicons.Star.StarStatus.UNMARK;
import static com.oss.untils.Constants.DEVICE_MODEL_TYPE;

public class BucOssPla002Test extends BaseTestCase {
    public static final String BOTTOM = "bottom";
    public static final String MEDIATION_CONNECTION_NAME = "Mediation connection for BucOssPla002Test";
    private static final String DEVICE_MODEL = "CISCO1941/K9";
    private static final String COUNTRY_NAME = "Poland";
    private static final String REGION_NAME = "Wielkopolska";
    private static final String DISTRICT_NAME = "District 1";
    private static final String CITY_NAME = "Poznan";
    private static final String POSTAL_CODE_NAME = "60001";
    private static final String LOCATION_NAME = "Poznan-BU1";
    private static final String CM_DOMAIN_NAME = "SeleniumE2ETest";
    private static final String DEVICE_NAME = "H3_Lab";
    private static final String BUILDING_TYPE = "Building";
    private static final String DEVICE_H1_NAME = "H1";
    private static final String ROUTER = "Router";
    private static final String PORT_NAME = "GE 1";
    private static final String TRAIL_NAME = "SeleniumTest IP Link";
    private static final String INTERFACE_NAME = "CISCO IOS 12/15/XE without mediation";
    private static final String TEMPLATE_NAME = "E2E_Test_Loopback_v2";
    private static final String ADDRESS = "10.10.20.171";
    private static final String PORT = "22";
    private static final String PASSWORD = "cisco";
    private static final String COMMAND_TIMEOUT = "20";
    private static final String CONNECTION_TIMEOUT = "20";
    private static final String IP_ADDRESS_WIZARD_MODE = "New address selection";
    private static final String IP_NETWORK = "E2ESeleniumTest";
    private static final String IP_NETWORK_DESCRIPTION = "IP Network used for business use cases. Please do not use.";
    private static final String IPV4SUBNET = "10.10.20.0/24";
    private static final String IPV4SUBNET_IDENTIFIER = IPV4SUBNET + " [" + IP_NETWORK + "]";
    private static final String TEMPLATE_FOLDER_NAME = "E2ETests";
    private static final String TEMPLATE_BUSINESS_KEY = "E2ETests/E2E_Test_Loopback_v2;Cisco Systems Inc.;CONFIGURATION";
    private static final String TEMPLATE_CONTENT = "#======= Section INIT =======\n\n# FAKE LOOPBACK script just for E2E tests of Comarch OSS. It doesn't create loopback just do some read only commands.\n\ncli.sendline ('terminal length 0')\ncli.expect (r'.*>|.*#')\ncli.sendline ('show ip interface brief')\ncli.sendline ('configure terminal')\ncli.expect (r'.*(config).*')\n#cli.sendline ('interface $InterfaceName[USER]')\n#cli.expect (r'.*(config).*')\n#cli.sendline ('ip address $IPAddress[USER] $Mask[USER]')\n#cli.expect  (r'.*(config).*#')\n#cli.sendline ('description $name[NEW_INVENTORY]')\n#cli.expect (r'.*(config).*#$')\nprint (\"$name[NEW_INVENTORY]\")\nprint (\"$InterfaceName[USER]\")\nprint (\"$IPAddress[USER]\")\nprint (\"$Mask[USER]\")\ncli.sendline ('exit')\ncli.sendline ('exit')\ncli.sendline ('show ip interface brief')\n#cli.sendline ('show run interface $InterfaceName[USER]')\ncli.expect(pexpect.TIMEOUT, timeout = 10)";
    private static final String TEMPLATE_EXECUTION_NOTIFICATION = "Script execution finished";
    private static final String BOOKMARKS = "Bookmarks";
    private static final String FAVOURITES = "Favourites";
    private static final String LAB_NETWORK_VIEW = "LAB Network View";
    private static final String NAME = "Name";
    private static final String LEFT = "left";
    private static final String RIGHT = "right";
    private static final String TYPE_COLUMN_ID = "col-type";
    private static final String VALIDATION_RESULT_TYPE = "INCOMPLETE_ROUTING_STATUS";
    private static final String SUPPRESSION_REASON = "Unnecessary in this scenario";
    private static final String CATEGORY_NAME = "BucOssPla002TestCategory";
    private static final String DESCRIPTION_CATEGORY = "E2E test category, please do not use";
    private final static String SYSTEM_MESSAGE_PATTERN = "%s. Checking system message after %s.";
    private static final String BUTTON_SAVE_BOOKMARK = "ButtonSaveBookmark";
    private static final String BOOKMARK_NAME = "LAB Network View";
    private static final String NO_CARD_COMPONENT = "No Card/Component";
    private final String serialNumber = "SN-" + (int) (Math.random() * 1001);
    private final Environment env = Environment.getInstance();
    private String URL = "";
    private NetworkDiscoveryControlViewPage networkDiscoveryControlViewPage;
    private SoftAssert softAssert;
    private String processIPCode;
    private String processNRPCode;
    private PhysicalInventoryRepository physicalInventoryRepository;
    private IPAMRepository ipamRepository;
    private TemplateFolderClient templateFolderClient;

    @BeforeClass
    public void openConsole() {
        softAssert = new SoftAssert();
        physicalInventoryRepository = new PhysicalInventoryRepository(env);
        ipamRepository = new IPAMRepository(env);
        templateFolderClient = TemplateFolderClient.getInstance(environmentRequestClient);
        waitForPageToLoad();
        String locationId = getOrCreateBuilding();
        getOrCreateDevice(locationId);
        getOrCreateIPNetwork();
        getOrCreateIPv4Subnet();
        getOrCreateCMTemplateFolder();
        getOrCreateCMTemplate();
        handleBookmarks();
    }

    @Test(priority = 1, description = "Create and start NRP Process")
    @Description("Create and start NRP Process")
    public void createProcessNRP() {
        ProcessOverviewPage processInstancesPage = ProcessOverviewPage.goToProcessOverviewPage(driver, webDriverWait);
        processNRPCode = processInstancesPage.createSimpleNRP();
        waitForPageToLoad();
        closeMessage();
        TasksPageV2 tasksPage = TasksPageV2.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.startTask(processNRPCode, TasksPageV2.HIGH_LEVEL_PLANNING_TASK);
        checkTaskAssignment(String.format(SYSTEM_MESSAGE_PATTERN, "Create process NRP", "high level planning task start"));
    }

    @Test(priority = 2, description = "Open Network View", dependsOnMethods = {"createProcessNRP"})
    @Description("Open Network View from bookmarks")
    public void openNetworkView() {
        closeMessage();
        waitForPageToLoad();
        homePage.chooseFromLeftSideMenu(LAB_NETWORK_VIEW, FAVOURITES, BOOKMARKS);
    }

    @Test(priority = 3, description = "Select location", dependsOnMethods = {"openNetworkView"})
    @Description("Select location")
    public void selectLocation() {
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        networkViewPage.hideDockedPanel(RIGHT);
        networkViewPage.expandDockedPanel(LEFT);
        networkViewPage.selectObjectInViewContent(NAME, LOCATION_NAME);
    }

    @Test(priority = 4, description = "Create device", dependsOnMethods = {"selectLocation"})
    @Description("Create device and check confirmation system message")
    public void createDevice() {
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        networkViewPage.useContextAction(ActionsContainer.CREATE_GROUP_ID, NetworkViewPage.CREATE_DEVICE_ACTION);
        DeviceWizardPage deviceWizardPage = new DeviceWizardPage(driver);
        deviceWizardPage.setModel(DEVICE_MODEL);
        waitForPageToLoad();
        deviceWizardPage.setName(DEVICE_NAME);
        waitForPageToLoad();
        deviceWizardPage.setHostname(DEVICE_NAME);
        waitForPageToLoad();
        deviceWizardPage.setSerialNumber(serialNumber);
        waitForPageToLoad();
        deviceWizardPage.next();
        waitForPageToLoad();
        deviceWizardPage.setPreciseLocation(LOCATION_NAME);
        waitForPageToLoad();
        deviceWizardPage.accept();
        checkMessageSize(String.format(SYSTEM_MESSAGE_PATTERN, "Create device", "device create"));
    }

    @Test(priority = 5, description = "Open device in Hierarchy View", dependsOnMethods = {"createDevice"})
    @Description("Open Hierarchy View with newly created device")
    public void moveToHierarchyView() {
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        networkViewPage.expandDockedPanel(LEFT);
        networkViewPage.useContextAction(ActionsContainer.SHOW_ON_GROUP_ID, NetworkViewPage.HIERARCHY_VIEW_ACTION);
    }

    @Test(priority = 6, description = "Select ethernet interface and open it in New Inventory View", dependsOnMethods = {"moveToHierarchyView"})
    @Description("Select Ethernet Interface in Hierarchy View and open it in New Inventory View")
    public void selectEthernetInterface() {
        HierarchyViewPage hierarchyViewPage = HierarchyViewPage.getHierarchyViewPage(driver, webDriverWait);
        String labelpath = DEVICE_NAME + ".Ports." + PORT_NAME + ".All Termination Points.EthernetInterface_TP." + PORT_NAME;
        hierarchyViewPage.selectNodeByLabelsPath(labelpath);
        waitForPageToLoad();
        hierarchyViewPage.callAction(ActionsContainer.SHOW_ON_GROUP_ID, "InventoryView");
    }

    @Test(priority = 7, description = "Assign IP V4 address", dependsOnMethods = {"selectEthernetInterface"})
    @Description("Select Ethernet Interface in New Inventory View and Assign IP V4 Address")
    public void assignIpV4Address() {
        NewInventoryViewPage newInventoryViewPage = NewInventoryViewPage.getInventoryViewPage(driver, webDriverWait);
        newInventoryViewPage.selectFirstRow();
        waitForPageToLoad();
        newInventoryViewPage.callAction(ActionsContainer.ASSIGN_GROUP_ID, "AssignIPv4Host");
        waitForPageToLoad();
        IPAddressAssignmentWizardPage ipAddressAssignmentWizardPage = new IPAddressAssignmentWizardPage(driver);
        IPAddressAssignmentWizardProperties ipAddressAssignmentWizardProperties = IPAddressAssignmentWizardProperties.builder()
                .wizardMode(IP_ADDRESS_WIZARD_MODE).subnet(IPV4SUBNET_IDENTIFIER).address(ADDRESS).isPrimary("false").build();
        ipAddressAssignmentWizardPage.assignMoToIPAddress(ipAddressAssignmentWizardProperties);
        waitForPageToLoad();
    }

    @Test(priority = 8, description = "Create IP link", dependsOnMethods = {"createDevice"})
    @Description("Open Network View and create IP link")
    public void createIpLink() {
        openNetworkView();
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        networkViewPage.useContextAction(NetworkViewPage.ADD_TO_VIEW_ACTION, NetworkViewPage.DEVICE_ACTION);
        networkViewPage.queryElementAndAddItToView("serialNumber", serialNumber);
        networkViewPage.expandDockedPanel(LEFT);
        networkViewPage.selectObjectInViewContent(NAME, DEVICE_H1_NAME);
        networkViewPage.useContextAction(ActionsContainer.CREATE_GROUP_ID, NetworkViewPage.CREATE_CONNECTION_ID);
        networkViewPage.selectTrailType("IP Link");
        waitForPageToLoad();
        networkViewPage.acceptTrailType();
        waitForPageToLoad();
        ConnectionWizardPage connectionWizardPage = new ConnectionWizardPage(driver);
        connectionWizardPage.setName(TRAIL_NAME);
        waitForPageToLoad();
        connectionWizardPage.clickNext();
        waitForPageToLoad();
        setTermination(connectionWizardPage, "1.1_1");
        setTermination(connectionWizardPage, "1.1_2");
        connectionWizardPage.assignAddressToOpositeInteface(false);
        waitForPageToLoad();
        connectionWizardPage.clickAccept();
        waitForPageToLoad();
    }

    @Test(priority = 9, description = "Suppress validation result about incomplete routing", dependsOnMethods = {"createIpLink"}, enabled = false)
    @Description("Suppress validation result about incomplete routing")
    public void suppressValidationResult() {
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        networkViewPage.suppressValidationResult(TYPE_COLUMN_ID, VALIDATION_RESULT_TYPE, SUPPRESSION_REASON);
        networkViewPage.hideDockedPanel(BOTTOM);
    }

    @Test(priority = 10, description = "Create mediation Configuration", dependsOnMethods = {"assignIpV4Address", "createIpLink"})
    @Description("Create mediation configuration")
    public void createMediationConfiguration() {
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        networkViewPage.expandDockedPanel(LEFT);
        networkViewPage.unselectObjectInViewContent(NAME, TRAIL_NAME + " (0%)");
        networkViewPage.selectObjectInViewContent(NAME, DEVICE_NAME);
        networkViewPage.useContextAction(ActionsContainer.CREATE_GROUP_ID, NetworkViewPage.CREATE_MEDIATION_CONFIGURATION_ID);
        Popup popup = Popup.create(driver, webDriverWait);
        popup.setComponentValue("configSelected", "CLI configuration");
        waitForPageToLoad();
        popup.clickButtonByLabel(ConfirmationBox.PROCEED);
        waitForPageToLoad();
        CLIConfigurationWizardPage cliConfigurationWizardPage = new CLIConfigurationWizardPage(driver);
        cliConfigurationWizardPage.setInputMethod("Search in managed addresses");
        cliConfigurationWizardPage.setIPHostAddress(ADDRESS);
        cliConfigurationWizardPage.setPort(PORT);
        cliConfigurationWizardPage.setName(MEDIATION_CONNECTION_NAME);
        cliConfigurationWizardPage.clickNextStep();
        cliConfigurationWizardPage.setCommandTimeout(COMMAND_TIMEOUT);
        cliConfigurationWizardPage.setConnectionSetupTimeout(CONNECTION_TIMEOUT);
        cliConfigurationWizardPage.setCLIProtocol("SSH");
        cliConfigurationWizardPage.clickNextStep();
        cliConfigurationWizardPage.setAuthMethod("Password Authentication");
        cliConfigurationWizardPage.setAuthPassword(PASSWORD);
        cliConfigurationWizardPage.clickNextStep();
        cliConfigurationWizardPage.clickNextStep();
        cliConfigurationWizardPage.clickAccept();
        checkMessageSize(String.format(SYSTEM_MESSAGE_PATTERN, "Create mediation configuration", "mediation configuration create"));
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        systemMessage.clickMessageLink();
        waitForPageToLoad();
        URL = driver.getCurrentUrl();
        systemMessage.close();
    }

    @Test(priority = 11, description = "Go through NRP task to IP Implementation task and click Perform Configuration", dependsOnMethods = {"createDevice"})
    @Description("Go through NRP task to IP Implementation task and click Perform Configuration")
    public void startImplementationTaskIP() {
        TasksPageV2 tasksPage = TasksPageV2.goToTasksPage(driver, webDriverWait, BASIC_URL);
        processIPCode = tasksPage.proceedNRPToImplementationTask(processNRPCode);
        waitForPageToLoad();
        Notifications.create(driver, webDriverWait).clearAllNotification();
        tasksPage.findTask(processIPCode, TasksPageV2.IMPLEMENTATION_TASK);
        DelayUtils.sleep(3000);
        waitForPageToLoad();
        tasksPage.clickPerformConfigurationButton();
    }

    @Test(priority = 12, description = "Perform Configuration change using prepared CM Template", dependsOnMethods = {"startImplementationTaskIP"})
    @Description("Perform Configuration change using prepared CM Template")
    public void performConfigurationChange() {
        ChangeConfigurationPage changeConfigurationPage = new ChangeConfigurationPage(driver);
        changeConfigurationPage.selectObjectType(ROUTER);
        changeConfigurationPage.selectObject(DEVICE_NAME);
        changeConfigurationPage.selectTemplate(TEMPLATE_NAME);
        changeConfigurationPage.clickSetParameters();
        SetParametersWizardPage setParametersWizardPage = new SetParametersWizardPage(driver);
        String name = setParametersWizardPage.getName();
        Assert.assertEquals(name, DEVICE_NAME);
//        setParametersWizardPage.setPassword("oss");//TODO update after OSSCMF-14379 fix
        setParametersWizardPage.setInterfaceName("GE 0");
        setParametersWizardPage.clickFillParameters();
        changeConfigurationPage.deployImmediately();
    }

    @Test(priority = 13, description = "Check configuration change", dependsOnMethods = {"startImplementationTaskIP"})
    @Description("Check configuration change status")
    public void checkConfigurationChange() {
        ShareFilterPage shareFilterPage = new ShareFilterPage(driver);
        shareFilterPage.closeShareView();
        waitForPageToLoad();
        Notifications.create(driver, webDriverWait).openDetails(TEMPLATE_NAME, TEMPLATE_EXECUTION_NOTIFICATION);
        waitForPageToLoad();
        LogManagerPage logManagerPage = new LogManagerPage(driver);
        Assert.assertEquals(logManagerPage.getStatus(), "UPLOAD_SUCCESS");
    }

    @Test(priority = 14, description = "Assign File to Process", dependsOnMethods = {"startImplementationTaskIP"})
    @Description("Assign File to Process")
    public void assignFile() {
        TasksPageV2 tasksPage = TasksPageV2.goToTasksPage(driver, webDriverWait, BASIC_URL);
        try {
            java.net.URL resource = BucOssPla002Test.class.getClassLoader().getResource("bpm/SeleniumTest.txt");
            assert resource != null;
            String absolutePatch = Paths.get(resource.toURI()).toFile().getAbsolutePath();
            tasksPage.addFile(processIPCode, TasksPageV2.IMPLEMENTATION_TASK, absolutePatch);
            checkMessageType(MessageType.SUCCESS, String.format(SYSTEM_MESSAGE_PATTERN, "Assign file", "adding file"));
        } catch (URISyntaxException e) {
            throw new RuntimeException("Cannot load file", e);
        }
        waitForPageToLoad();
        List<String> files = tasksPage.getListOfAttachments();
        Assert.assertTrue((files.get(0)).contains("SeleniumTest"));
    }

    @Test(priority = 15, description = "Complete IP and NRP process", dependsOnMethods = {"startImplementationTaskIP"})
    @Description("Complete IP and NRP process")
    public void completeIpAndNrp() {
        TasksPageV2 tasksPage = TasksPageV2.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.completeTask(processIPCode, TasksPageV2.IMPLEMENTATION_TASK);
        String completeIpAndNrp = "Complete IP and NRP";
        checkTaskCompleted(String.format(SYSTEM_MESSAGE_PATTERN, completeIpAndNrp, "implementation task complete"));
        tasksPage.startTask(processIPCode, TasksPageV2.ACCEPTANCE_TASK);
        checkTaskAssignment(String.format(SYSTEM_MESSAGE_PATTERN, completeIpAndNrp, "acceptance task start"));
        tasksPage.completeTask(processIPCode, TasksPageV2.ACCEPTANCE_TASK);
        checkTaskCompleted(String.format(SYSTEM_MESSAGE_PATTERN, completeIpAndNrp, "acceptance task complete"));
        tasksPage.startTask(processNRPCode, TasksPageV2.VERIFICATION_TASK);
        checkTaskAssignment(String.format(SYSTEM_MESSAGE_PATTERN, completeIpAndNrp, "verification task start"));
        tasksPage.completeTask(processNRPCode, TasksPageV2.VERIFICATION_TASK);
        checkTaskCompleted(String.format(SYSTEM_MESSAGE_PATTERN, completeIpAndNrp, "verification task complete"));
    }

    @Test(priority = 16, description = "Create CM Domain", dependsOnMethods = {"createDevice", "completeIpAndNrp"})
    @Description("Go to Network Discovery Control View and create CM Domain")
    public void createCmDomain() {
        networkDiscoveryControlViewPage = NetworkDiscoveryControlViewPage.goToNetworkDiscoveryControlViewPage(driver, BASIC_URL);
        networkDiscoveryControlViewPage.createCMDomain(CM_DOMAIN_NAME, INTERFACE_NAME, "IP");
        waitForPageToLoad();
    }

    @Test(priority = 17, description = "Upload reconciliation samples", dependsOnMethods = {"createCmDomain"})
    @Description("Go to Samples Management View and upload reconciliation samples")
    public void uploadSamples() throws URISyntaxException, IOException {
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(CM_DOMAIN_NAME);
        waitForPageToLoad();
        networkDiscoveryControlViewPage.moveToSamplesManagement();
        SamplesManagementPage samplesManagementPage = new SamplesManagementPage(driver);
        samplesManagementPage.selectPath();
        waitForPageToLoad();
        samplesManagementPage.uploadSamplesFromPath("recoSamples/ciscoE2E");
    }

    @Test(priority = 18, description = "Run reconciliation and check results", dependsOnMethods = {"uploadSamples"})
    @Description("Run reconciliation and check if it ended without errors")
    public void runReconciliation() {
        NetworkDiscoveryControlViewPage networkDiscoveryControlViewPage = NetworkDiscoveryControlViewPage.goToNetworkDiscoveryControlViewPage(driver, BASIC_URL);
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(CM_DOMAIN_NAME);
        waitForPageToLoad();
        networkDiscoveryControlViewPage.runReconciliation();
        checkMessageType(MessageType.INFO, String.format(SYSTEM_MESSAGE_PATTERN, "Run reconciliation", "reconciliation start"));
        waitForPageToLoad();
        String status = networkDiscoveryControlViewPage.waitForEndOfReco();
        networkDiscoveryControlViewPage.selectLatestReconciliationState();
        waitForPageToLoad();
        networkDiscoveryControlViewPage.checkIssues(NetworkDiscoveryControlViewPage.IssueLevel.INFO);
        String success = "SUCCESS";
        if (status.equals(success)) {
            waitForPageToLoad();
            Assert.assertTrue(networkDiscoveryControlViewPage.checkIssues(NetworkDiscoveryControlViewPage.IssueLevel.ERROR));
            waitForPageToLoad();
            Assert.assertTrue(networkDiscoveryControlViewPage.checkIssues(NetworkDiscoveryControlViewPage.IssueLevel.WARNING));
        } else {
            waitForPageToLoad();
            Assert.assertTrue(networkDiscoveryControlViewPage.checkIssues(NetworkDiscoveryControlViewPage.IssueLevel.STARTUP_FATAL));
            waitForPageToLoad();
            Assert.assertTrue(networkDiscoveryControlViewPage.checkIssues(NetworkDiscoveryControlViewPage.IssueLevel.FATAL));
        }
        Assert.assertEquals(status, success);
    }

    @Test(priority = 19, description = "Apply inconsistencies", dependsOnMethods = {"runReconciliation"})
    @Description("Go to Network Inconsistencies View, apply inconsistencies from Network to Live and check notification")
    public void applyInconsistencies() {
        networkDiscoveryControlViewPage.moveToNivFromNdcv();
        NetworkInconsistenciesViewPage networkInconsistenciesViewPage = new NetworkInconsistenciesViewPage(driver);
        networkInconsistenciesViewPage.expandTwoLastTreeRows();
        Notifications.create(driver, webDriverWait).clearAllNotification();
        networkInconsistenciesViewPage.applyFirstInconsistenciesGroup();
        DelayUtils.sleep(1000);
        waitForPageToLoad();
        Assert.assertEquals(Notifications.create(driver, new WebDriverWait(driver, Duration.ofSeconds(150))).getNotificationMessage(), String.format(NetworkInconsistenciesViewPage.ACCEPT_DISCREPANCIES_PATTERN, DEVICE_NAME));
    }

    @Test(priority = 20, description = "Delete CM Domain", dependsOnMethods = {"createCmDomain"})
    @Description("Go to Network Discovery Control View and delete CM Domain")
    public void deleteCmDomain() {
        networkDiscoveryControlViewPage = NetworkDiscoveryControlViewPage.goToNetworkDiscoveryControlViewPage(driver, BASIC_URL);
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(CM_DOMAIN_NAME);
        waitForPageToLoad();
        Notifications.create(driver, webDriverWait).clearAllNotification();
        networkDiscoveryControlViewPage.deleteCmDomain();
        checkMessageType(MessageType.INFO, String.format(SYSTEM_MESSAGE_PATTERN, "Delete CM Domain", "CM Domain delete"));
        waitForPageToLoad();
        Assert.assertEquals(Notifications.create(driver, webDriverWait).getNotificationMessage(), "Deleting CM Domain: " + CM_DOMAIN_NAME + " finished");
    }

    @Test(priority = 21, description = "Delete IP link", dependsOnMethods = {"createIpLink"})
    @Description("Delete IP link in Network View")
    public void deleteIpLink() {
        homePage.chooseFromLeftSideMenu(LAB_NETWORK_VIEW, FAVOURITES, BOOKMARKS);
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        networkViewPage.useContextAction(NetworkViewPage.ADD_TO_VIEW_ACTION, NetworkViewPage.CONNECTION_ACTION);
        networkViewPage.queryElementAndAddItToView("label", TRAIL_NAME);
        networkViewPage.useContextAction(ActionsContainer.EDIT_GROUP_ID, NetworkViewPage.DELETE_CONNECTION_ID);
        Popup.create(driver, webDriverWait).clickButtonById("wizard-submit-button-deleteWidgetId");
    }

    @Test(priority = 22, description = "Delete mediation connection", dependsOnMethods = {"createMediationConfiguration"})
    @Description("Go to Connection Configuration View by direct link and delete mediation connection")
    public void deleteMediation() {
        ViewConnectionConfigurationPage viewConnectionConfigurationPage = ViewConnectionConfigurationPage.goToViewConnectionConfigurationPage(driver, URL);
        viewConnectionConfigurationPage.fullTextSearch(MEDIATION_CONNECTION_NAME);
        viewConnectionConfigurationPage.selectRow("Name", MEDIATION_CONNECTION_NAME);
        viewConnectionConfigurationPage.useContextAction(ActionsContainer.EDIT_GROUP_ID, ViewConnectionConfigurationPage.DELETE_BUTTON_ID);
        waitForPageToLoad();
        viewConnectionConfigurationPage.clickDelete();
        waitForPageToLoad();
    }

    @Test(priority = 23, description = "Delete IP address assignment", dependsOnMethods = {"assignIpV4Address", "completeIpAndNrp"})
    @Description("Go to Address Management View by direct link and delete IP address assignment")
    public void deleteIPAddressAssignment() {
        IPAddressManagementViewPage ipAddressManagementViewPage = IPAddressManagementViewPage.goToIPAddressManagementPage(driver, BASIC_URL);
        ipAddressManagementViewPage.searchIpNetwork(IP_NETWORK);
        ipAddressManagementViewPage.expandTreeRow(IP_NETWORK);
        ipAddressManagementViewPage.expandTreeRow("IP Subnets");
        ipAddressManagementViewPage.expandTreeRow("IPV4");
        ipAddressManagementViewPage.expandTreeRowContains(IPV4SUBNET);
        ipAddressManagementViewPage.expandTreeRow("IP Addresses");
        ipAddressManagementViewPage.deleteIPHost(ADDRESS + "/24");
        waitForPageToLoad();
    }

    @Test(priority = 24, description = "Delete device", dependsOnMethods = {"createDevice"})
    @Description("Delete device in Network View and check confirmation system message")
    public void deleteDevice() {
        homePage.chooseFromLeftSideMenu(LAB_NETWORK_VIEW, FAVOURITES, BOOKMARKS);
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        networkViewPage.useContextAction(NetworkViewPage.ADD_TO_VIEW_ACTION, NetworkViewPage.DEVICE_ACTION);
        networkViewPage.queryElementAndAddItToView("name", DEVICE_NAME);
        networkViewPage.useContextActionAndClickConfirmation(ActionsContainer.EDIT_GROUP_ID, NetworkViewPage.DELETE_DEVICE_ACTION, ConfirmationBox.YES);
        checkMessageSize(String.format(SYSTEM_MESSAGE_PATTERN, "Delete device", "device delete"));
    }

    @Test(priority = 25, description = "Checking system message summary")
    @Description("Checking system message summary")
    public void systemMessageSummary() {
        softAssert.assertAll();
    }

    private void setTermination(ConnectionWizardPage connectionWizardPage, String dataPath) {
        connectionWizardPage.selectConnectionTermination(dataPath);
        waitForPageToLoad();
        connectionWizardPage.terminateCardComponent(NO_CARD_COMPONENT);
        waitForPageToLoad();
        connectionWizardPage.terminatePort(PORT_NAME);
        waitForPageToLoad();
        connectionWizardPage.terminateTerminationPort(PORT_NAME);
        waitForPageToLoad();
    }

    private void getOrCreateCMTemplate() {
        if (!templateFolderClient.isTemplatePresent(TEMPLATE_BUSINESS_KEY)) {
            templateFolderClient.createTemplate(TEMPLATE_BUSINESS_KEY, TEMPLATE_CONTENT);
        }
    }

    private void getOrCreateCMTemplateFolder() {
        if (!templateFolderClient.isFolderPresent(TEMPLATE_FOLDER_NAME)) {
            templateFolderClient.createFolder(TEMPLATE_FOLDER_NAME);
        }
    }

    private void getOrCreateIPv4Subnet() {
        ipamRepository.getOrCreateIPv4Subnet(IPV4SUBNET_IDENTIFIER);
    }

    private void getOrCreateIPNetwork() {
        ipamRepository.getOrCreateIPNetwork(IP_NETWORK, IP_NETWORK_DESCRIPTION);
    }

    private void handleBookmarks() {
        NewBookmarksPage bookmarksPage = NewBookmarksPage.goToBookmarksPage(driver, webDriverWait, BASIC_URL);
        waitForPageToLoad();
        if (!bookmarksPage.isObjectPresent(CATEGORY_NAME)) {
            bookmarksPage.createCategory(CATEGORY_NAME, DESCRIPTION_CATEGORY);
        }
        waitForPageToLoad();
        bookmarksPage.expandCategory(CATEGORY_NAME);
        waitForPageToLoad();
        if (!bookmarksPage.isObjectPresent(BOOKMARK_NAME)) {
            createBookmark();
            return;
        }
        if (bookmarksPage.getFavouriteStatus(BOOKMARK_NAME).equals(UNMARK)) {
            bookmarksPage.setFavourite(BOOKMARK_NAME);
        }
    }

    private void createBookmark() {
        openH1DeviceInNetworkView();
        ButtonPanel.create(driver, webDriverWait).clickButton(BUTTON_SAVE_BOOKMARK);
        BookmarkWizardPage bookmarkWizardPage = new BookmarkWizardPage(driver, webDriverWait);
        bookmarkWizardPage.setName(BOOKMARK_NAME);
        bookmarkWizardPage.setCategory(CATEGORY_NAME);
        bookmarkWizardPage.clickSave();
        waitForPageToLoad();
        closeMessage();
        ButtonPanel.create(driver, webDriverWait).clickButton("ButtonFavouriteBookmark");
    }

    private void openH1DeviceInNetworkView() {
        ToolbarWidget globalSearchInput = ToolbarWidget.create(driver, webDriverWait);
        globalSearchInput.searchInGlobalSearch(DEVICE_H1_NAME);
        GlobalSearchPage globalSearchPage = new GlobalSearchPage(driver);
        waitForPageToLoad();
        globalSearchPage.filterObjectType("Physical Device / Router");
        waitForPageToLoad();
        globalSearchPage.expandShowOnAndChooseView(DEVICE_H1_NAME, ActionsContainer.SHOW_ON_GROUP_ID, "ShowOnNetworkVieActionId");
        waitForPageToLoad();
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        networkViewPage.expandViewContentPanel();
        networkViewPage.unselectObjectInViewContent(NAME, DEVICE_H1_NAME);
        waitForPageToLoad();
    }

    private void checkMessageType(MessageType messageType, String systemMessageLog) {
        softAssert.assertEquals((getFirstMessage().getMessageType()), messageType, systemMessageLog);
    }

    private void checkMessageContainsText(String systemMessageLog) {
        softAssert.assertTrue((getFirstMessage().getText())
                .contains("Task properly completed."), systemMessageLog);
    }

    private void checkMessageText(String systemMessageLog) {
        softAssert.assertEquals((getFirstMessage().getText()), "The task properly assigned.", systemMessageLog);
    }

    private void checkMessageSize(String systemMessageLog) {
        softAssert.assertEquals((SystemMessageContainer.create(driver, webDriverWait)
                .getMessages()
                .size()), 1, systemMessageLog);
        checkMessageType(MessageType.SUCCESS, systemMessageLog);
    }

    private Message getFirstMessage() {
        return SystemMessageContainer.create(driver, webDriverWait)
                .getFirstMessage()
                .orElseThrow(() -> new RuntimeException("The list is empty"));
    }

    private void checkTaskAssignment(String systemMessageLog) {
        checkMessageType(MessageType.SUCCESS, systemMessageLog);
        checkMessageText(systemMessageLog);
    }

    private void checkTaskCompleted(String systemMessageLog) {
        checkMessageType(MessageType.SUCCESS, systemMessageLog);
        checkMessageContainsText(systemMessageLog);
    }

    private void closeMessage() {
        SystemMessageContainer.create(driver, webDriverWait).close();
        waitForPageToLoad();
    }

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    private boolean isDevicePresent(String locationId) {
        return physicalInventoryRepository.isDevicePresent(locationId, DEVICE_H1_NAME);
    }

    private void getOrCreateDevice(String locationId) {
        if (!isDevicePresent(locationId)) {
            createPhysicalDevice(locationId);
        }
    }

    private void createPhysicalDevice(String locationId) {
        ResourceCatalogClient resourceCatalogClient = new ResourceCatalogClient(env);
        Long deviceModelId = resourceCatalogClient.getModelIds(DEVICE_MODEL);
        physicalInventoryRepository.createDevice(BUILDING_TYPE, Long.valueOf(locationId), deviceModelId, DEVICE_H1_NAME,
                DEVICE_MODEL_TYPE);
    }

    private String getOrCreateBuilding() {
        LocationInventoryRepository locationInventoryRepository = new LocationInventoryRepository(env);
        return locationInventoryRepository.getOrCreateLocation(LOCATION_NAME, BUILDING_TYPE, prepareAddress());
    }

    private Long prepareAddress() {
        AddressRepository addressRepository = new AddressRepository(Environment.getInstance());
        return addressRepository.updateOrCreateAddress(COUNTRY_NAME, POSTAL_CODE_NAME, REGION_NAME, CITY_NAME, DISTRICT_NAME);
    }
}

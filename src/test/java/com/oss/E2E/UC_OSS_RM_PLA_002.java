package com.oss.E2E;

import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.regex.Pattern;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.bpm.CreateProcessNRPTest;
import com.oss.framework.alerts.SystemMessageContainer;
import com.oss.framework.alerts.SystemMessageContainer.Message;
import com.oss.framework.alerts.SystemMessageContainer.MessageType;
import com.oss.framework.alerts.SystemMessageInterface;
import com.oss.framework.listwidget.EditableList;
import com.oss.framework.mainheader.Notifications;
import com.oss.framework.sidemenu.SideMenu;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.tablewidget.OldTable;
import com.oss.framework.widgets.tablewidget.TableInterface;
import com.oss.pages.bpm.IntegrationProcessWizardPage;
import com.oss.pages.bpm.ProcessWizardPage;
import com.oss.pages.bpm.TasksPage;
import com.oss.pages.filtermanager.ShareFilterPage;
import com.oss.pages.mediation.CLIConfigurationWizardPage;
import com.oss.pages.mediation.ViewConnectionConfigurationPage;
import com.oss.pages.physical.DeviceOverviewPage;
import com.oss.pages.physical.DeviceWizardPage;
import com.oss.pages.platform.HomePage;
import com.oss.pages.platform.LogManagerPage;
import com.oss.pages.platform.OldInventoryViewPage;
import com.oss.pages.reconciliation.CmDomainWizardPage;
import com.oss.pages.reconciliation.NetworkDiscoveryControlViewPage;
import com.oss.pages.reconciliation.NetworkDiscoveryControlViewPage.ErrorLevel;
import com.oss.pages.reconciliation.NetworkInconsistenciesViewPage;
import com.oss.pages.reconciliation.SamplesManagementPage;
import com.oss.pages.templateCM.ChangeConfigurationPage;
import com.oss.pages.templateCM.SetParametersWizardPage;
import com.oss.pages.transport.NetworkViewPage;
import com.oss.pages.transport.ipam.IPAddressManagementViewPage;
import com.oss.pages.transport.ipam.IPv4AddressAssignmentWizardPage;

import io.qameta.allure.Description;

import static com.oss.framework.components.inputs.Input.ComponentType.TEXT_FIELD;

public class UC_OSS_RM_PLA_002 extends BaseTestCase {
    private NetworkDiscoveryControlViewPage networkDiscoveryControlViewPage;
    private ChangeConfigurationPage changeConfigurationPage;
    private SetParametersWizardPage setParametersWizardPage;

    private static final String deviceModel = "CISCO1941/K9";
    private static final String LOCATION_NAME = "Poznan-BU1";
    private static final String cmDomainName = "SeleniumE2ETest";
    private static final String deviceName = "H3_Lab";
    private static final String portName = "GE 1";
    private static final String trailName = "SeleniumTest IP Link";
    private static final String interfaceName = "CISCO IOS 12/15/XE without mediation";
    private static final String TEMPLATE_NAME = "E2E_Test_Loopback_v2";
    private static final String address = "10.10.20.11";
    private static final String port = "22";
    private static final String password = "cisco";
    private static final String commandTimeout = "20";
    private static final String connectionTimeout = "20";
    private static final String ipNetwork = "E2ESeleniumTest";
    private static final String TEMPLATE_EXECUTION_NOTIFICATION = "Scripts execution for template E2E_Test_Loopback_v2";

    private String processIPName = "S.1-" + (int) (Math.random() * 1001);
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
    @Description("Create NRP Process")
    public void createProcessNRP() {
        ProcessWizardPage processWizardPage = new ProcessWizardPage(driver);
        processNRPCode = processWizardPage.createSimpleNRP();
        checkMessageSize(1);
        checkMessageType();
        checkMessageContainsText(processNRPCode);
    }

    @Test(priority = 2)
    @Description("Start High Level Planning Task")
    public void startHLPTask() {
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.startTask(processNRPCode, "High Level Planning");
        checkTaskAssignment();
    }

    @Test(priority = 3)
    public void openNetworkView() {
        HomePage homePage = new HomePage(driver);
        homePage.goToHomePage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        SideMenu sideMenu = SideMenu.create(driver, webDriverWait);
        sideMenu.callActionByLabel("LAB Network View", "Favourites", "SeleniumTests");
    }

    @Test(priority = 4)
    public void selectLocation() {
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.expandDockedPanel("left");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.selectObjectInViewContent("Name", LOCATION_NAME);
    }

    @Test(priority = 5)
    public void createPhysicalDevice() {
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        networkViewPage.useContextAction("CREATE", "Create Device");
        DeviceWizardPage deviceWizardPage = new DeviceWizardPage(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.setModel(deviceModel);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        DelayUtils.sleep(1000);
        deviceWizardPage.setName(deviceName);
        deviceWizardPage.setHostname(deviceName);
        DelayUtils.sleep(1000);
        deviceWizardPage.setSerialNumber(serialNumber);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.next();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.setPreciseLocation(LOCATION_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.accept();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        checkMessageSize(1);
        checkMessageType();
    }

    @Test(priority = 6)
    public void moveToDeviceOverview() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        networkViewPage.expandDockedPanel("left");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.selectObjectInViewContent("Name", deviceName);
        DelayUtils.sleep(15000); // naming has to recalculate, it doesn't show progress in the console
        networkViewPage.selectObjectInViewContent("Name", deviceName);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.useContextAction("NAVIGATION", "Device Overview");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 7)
    public void selectEthernetInterface() {
        DeviceOverviewPage deviceOverviewPage = new DeviceOverviewPage(driver);
        deviceOverviewPage.expandTreeRow(1, portName);
        deviceOverviewPage.selectTreeRow(portName, 1, portName);
        DelayUtils.sleep(5000);
        deviceOverviewPage.useContextAction("Inventory View");
    }

    @Test(priority = 8)
    public void moveToInventoryView() {
        OldInventoryViewPage oldInventoryViewPage = new OldInventoryViewPage(driver);
        oldInventoryViewPage.selectRow("Object Type", "Ethernet Interface");
        oldInventoryViewPage.useContextAction("CREATE", "AssignIPv4Host");
    }

    @Test(priority = 9)
    public void assignIPv4Address() {
        IPv4AddressAssignmentWizardPage iPv4AddressAssignmentWizardPage = new IPv4AddressAssignmentWizardPage(driver);
        iPv4AddressAssignmentWizardPage.assignIPAddressMainStep("10.10.20.11", "10.10.20.0/24 [E2ESeleniumTest]", "false");
        iPv4AddressAssignmentWizardPage.assignIPAddressSummaryStep();
    }

    @Test(priority = 10)
    public void createIpLink() {
        openNetworkView();
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        networkViewPage.useContextAction("add_to_view_group", "Device");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.queryElementAndAddItToView("serialNumber", TEXT_FIELD, serialNumber);
        networkViewPage.expandDockedPanel("left");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.selectObjectInViewContent("Name", "H1");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.useContextAction("CREATE", "Create Connection");
        networkViewPage.selectTrailType("IP Link");
        networkViewPage.acceptTrailType();
        networkViewPage.setTrailName(trailName);
        networkViewPage.proceedTrailCreation();
        networkViewPage.hideDockedPanel("left");
    }

    @Test(priority = 11)
    public void preciseIpLinkTermination() {
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        networkViewPage.expandDockedPanel("bottom");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.selectObjectInDetailsTab("Type", "Start");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.modifyTermination();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.setTrailPort(portName);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.clickProceed();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.selectObjectInDetailsTab("Type", "End");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.modifyTermination();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.setTrailPort(portName);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.clickProceed();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 12)
    public void suppressValidationResult() {
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        networkViewPage.supressValidationResult("INCOMPLETE_ROUTING_STATUS", "Unnecessary in this scenario");
        networkViewPage.hideDockedPanel("bottom");
    }

    @Test(priority = 13)
    public void moveMediationConfiguration() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        networkViewPage.expandDockedPanel("left");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.selectObjectInViewContent("Name", trailName + " (0%)");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.selectObjectInViewContent("Name", deviceName);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.useContextAction("CREATE", "Create Mediation Configuration");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.clickProceed();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 14)
    public void startMediationConfiguration() {
        CLIConfigurationWizardPage cliConfigurationWizardPage = new CLIConfigurationWizardPage(driver);
        cliConfigurationWizardPage.setInputMethod("Search in managed addresses");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cliConfigurationWizardPage.setIPHostAddress(address);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cliConfigurationWizardPage.setPort(port);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cliConfigurationWizardPage.clickNextStep();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 15)
    public void setCLIParameters() {
        CLIConfigurationWizardPage cliConfigurationWizardPage = new CLIConfigurationWizardPage(driver);
        cliConfigurationWizardPage.setCommandTimeout(commandTimeout);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cliConfigurationWizardPage.setConnectionSetupTimeout(connectionTimeout);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cliConfigurationWizardPage.setCLIProtocol("SSH");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cliConfigurationWizardPage.clickNextStep();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 16)
    public void setAuthenticationMethod() {
        CLIConfigurationWizardPage cliConfigurationWizardPage = new CLIConfigurationWizardPage(driver);
        cliConfigurationWizardPage.setAuthMethod("Password Authentication");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cliConfigurationWizardPage.setAuthPassword(password);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cliConfigurationWizardPage.clickNextStep();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 17)
    public void acceptSummary() {
        CLIConfigurationWizardPage cliConfigurationWizardPage = new CLIConfigurationWizardPage(driver);
        cliConfigurationWizardPage.clickAccept();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        checkMessageSize(1);
        checkMessageType();
    }

    @Test(priority = 18)
    public void getURL() {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        systemMessage.clickMessageLink();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        URL = driver.getCurrentUrl();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 19)
    public void completeHLPTask() {
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.completeTask(processNRPCode, "High Level Planning");
        checkTaskCompleted();
    }

    @Test(priority = 20)
    public void startLLPTask() {
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.startTask(processNRPCode, "Low Level Planning");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        checkTaskAssignment();
        String currentUrl = driver.getCurrentUrl();
        String[] split = currentUrl.split(Pattern.quote("?"));
        String perspectiveContext = split[1];
        Assert.assertTrue(perspectiveContext.contains("PLAN"));
    }

    @Test(priority = 21)
    public void completeLLPTask() {
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.completeTask(processNRPCode, "Low Level Planning");
        checkTaskCompleted();
    }

    @Test(priority = 22)
    public void startRFITask() {
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.startTask(processNRPCode, "Ready for Integration");
        checkTaskAssignment();
    }

    @Test(priority = 23)
    public void setupIntegration() {
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.setupIntegration(processNRPCode);
        IntegrationProcessWizardPage integrationWizard = new IntegrationProcessWizardPage(driver);
        integrationWizard.defineIntegrationProcess(processIPName, LocalDate.now().plusDays(0).toString(), 1);
        integrationWizard.clickNext();
        integrationWizard.dragAndDrop(deviceName, processNRPCode, processIPName);
        integrationWizard.clickAccept();
    }

    @Test(priority = 24)
    public void getIPCode() {
        DelayUtils.sleep(3000);
        TableInterface ipTable = OldTable.createByComponentId(driver, webDriverWait, "ip_involved_nrp_group1");
        int rowNumber = ipTable.getRowNumber(processIPName, "Name");
        processIPCode = ipTable.getCellValue(rowNumber, "Code");
        System.out.println(processIPCode);
    }

    @Test(priority = 25)
    public void completeRFITask() {
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.completeTask(processNRPCode, "Ready for Integration");
        checkTaskCompleted();
    }

    @Test(priority = 26)
    public void startSDTaskIP() {
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.startTask(processIPCode, "Scope definition");
        checkTaskAssignment();
    }

    @Test(priority = 27)
    public void completeSDTaskIP() {
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.completeTask(processIPCode, "Scope definition");
        checkTaskCompleted();
    }

    @Test(priority = 28)
    public void startImplementationTaskIP() {
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.startTask(processIPCode, "Implementation");
        checkTaskAssignment();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Notifications.create(driver, webDriverWait).clearAllNotification();
        tasksPage.findTask(processIPCode, "Implementation");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        tasksPage.clickPerformConfigurationButton();
    }

    @Test(priority = 29)
    public void chooseDeviceAndTemplate() {
        changeConfigurationPage = new ChangeConfigurationPage(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        changeConfigurationPage.selectObjectType("Router");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        changeConfigurationPage.selectTemplate(TEMPLATE_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 30)
    public void testParameters() {
        changeConfigurationPage.clickSetParameters();
        setParametersWizardPage = new SetParametersWizardPage(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        String name = setParametersWizardPage.getParameter("$name[NEW_INVENTORY]");
        Assert.assertEquals(deviceName, name);
        setParametersWizardPage.setParameter("$InterfaceName[USER]", "GE 0");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        setParametersWizardPage.setParameter("$Password[SYSTEM]", "oss");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 31)
    public void runTemplate() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        setParametersWizardPage.clickFillParameters();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        changeConfigurationPage.deployImmediately();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        DelayUtils.sleep(3000);
        ShareFilterPage shareFilterPage = new ShareFilterPage(driver);
        shareFilterPage.closeShareView();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        Notifications.create(driver, webDriverWait).openDetailsForSpecificNotification(TEMPLATE_EXECUTION_NOTIFICATION, "FINISHED");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        LogManagerPage logManagerPage = new LogManagerPage(driver);
        Assert.assertEquals(logManagerPage.getStatus(), "UPLOAD_SUCCESS");
    }

    @Test(priority = 32)
    public void assignFile() {
        try {
            TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
            java.net.URL resource = CreateProcessNRPTest.class.getClassLoader().getResource("bpm/SeleniumTest.txt");
            String absolutePatch = Paths.get(resource.toURI()).toFile().getAbsolutePath();
            tasksPage.addFile(processIPCode, "Implementation", absolutePatch);
            checkMessageType();
        } catch (URISyntaxException e) {
            throw new RuntimeException("Cannot load file", e);
        }
        DelayUtils.sleep(2000);
        List<String> attachments = EditableList.createById(driver, webDriverWait, "attachmentManagerBusinessView_commonList").getValues();
        Assert.assertTrue(attachments.size() > 0);
        String allNames = String.join("", attachments);
        Assert.assertTrue(allNames.contains("SeleniumTest"));
    }

    @Test(priority = 33)
    public void completeImplementationTaskIP() {
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.completeTask(processIPCode, "Implementation");
        checkTaskCompleted();
    }

    @Test(priority = 34)
    public void startAcceptanceTaskIP() {
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.startTask(processIPCode, "Acceptance");
        checkTaskAssignment();
    }

    @Test(priority = 35)
    public void completeAcceptanceTaskIP() {
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.completeTask(processIPCode, "Acceptance");
        checkTaskCompleted();
    }

    @Test(priority = 36)
    public void startVerificationTaskNRP() {
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.startTask(processNRPCode, "Verification");
        checkTaskAssignment();
    }

    @Test(priority = 37)
    public void completeVerificationTaskNRP() {
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.completeTask(processNRPCode, "Verification");
        checkTaskCompleted();
    }

    @Test(priority = 38)
    public void createCmDomain() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkDiscoveryControlViewPage = NetworkDiscoveryControlViewPage.goToNetworkDiscoveryControlViewPage(driver, BASIC_URL);
        networkDiscoveryControlViewPage.openCmDomainWizard();
        CmDomainWizardPage wizard = new CmDomainWizardPage(driver);
        wizard.setName(cmDomainName);
        wizard.setInterface(interfaceName);
        wizard.setDomain("IP");
        wizard.save();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 39)
    public void uploadSamples() {
        DelayUtils.sleep(1000);
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(cmDomainName);
        networkDiscoveryControlViewPage.moveToSamplesManagement();
        SamplesManagementPage samplesManagementPage = new SamplesManagementPage(driver);
        samplesManagementPage.selectPath();
        samplesManagementPage.createDirectory(cmDomainName);
        DelayUtils.sleep(1000);
        samplesManagementPage.uploadSamples("recoSamples/ciscoE2E/H3_Lab_100.100.100.100_20181016_1500_sh_inventory_raw.cli");
        DelayUtils.sleep(1000);
        samplesManagementPage.uploadSamples("recoSamples/ciscoE2E/H3_Lab_100.100.100.100_20181016_1500_sh_ip_interface_brief.cli");
        DelayUtils.sleep(1000);
        samplesManagementPage.uploadSamples("recoSamples/ciscoE2E/H3_Lab_100.100.100.100_20181016_1500_sh_lldp_neighbors_detail.cli");
        DelayUtils.sleep(1000);
        samplesManagementPage.uploadSamples("recoSamples/ciscoE2E/H3_Lab_100.100.100.100_20181016_1500_sh_running_config.cli");
        DelayUtils.sleep(1000);
        samplesManagementPage.uploadSamples("recoSamples/ciscoE2E/H3_Lab_100.100.100.100_20181016_1500_sh_version.cli");
    }

    @Test(priority = 40)
    public void runReconciliationWithFullSample() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        NetworkDiscoveryControlViewPage networkDiscoveryControlViewPage =
                NetworkDiscoveryControlViewPage.goToNetworkDiscoveryControlViewPage(driver, BASIC_URL);
        DelayUtils.sleep(100);
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(cmDomainName);
        networkDiscoveryControlViewPage.runReconciliation();
        networkDiscoveryControlViewPage.checkReconciliationStartedSystemMessage();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkDiscoveryControlViewPage.waitForEndOfReco();
        networkDiscoveryControlViewPage.selectLatestReconciliationState();
        Assert.assertTrue(networkDiscoveryControlViewPage.checkIssues(ErrorLevel.STARTUP_FATAL));
        Assert.assertTrue(networkDiscoveryControlViewPage.checkIssues(ErrorLevel.FATAL));
        Assert.assertTrue(networkDiscoveryControlViewPage.checkIssues(ErrorLevel.ERROR));
        Assert.assertTrue(networkDiscoveryControlViewPage.checkIssues(ErrorLevel.WARNING));
    }

    @Test(priority = 41)
    public void applyInconsistencies() {
        networkDiscoveryControlViewPage.moveToNivFromNdcv();
        NetworkInconsistenciesViewPage networkInconsistenciesViewPage = new NetworkInconsistenciesViewPage(driver);
        networkInconsistenciesViewPage.expantTree();
        networkInconsistenciesViewPage.clearOldNotification();
        networkInconsistenciesViewPage.applyInconsistencies();
        DelayUtils.sleep(500);
        networkInconsistenciesViewPage.checkNotificationAfterApplyInconsistencies(deviceName);
    }

    @Test(priority = 42)
    public void deleteCmDomain() {
        networkDiscoveryControlViewPage = NetworkDiscoveryControlViewPage.goToNetworkDiscoveryControlViewPage(driver, BASIC_URL);
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(cmDomainName);
        networkDiscoveryControlViewPage.clearOldNotifications();
        networkDiscoveryControlViewPage.deleteCmDomain();
        networkDiscoveryControlViewPage.checkDeleteCmDomainSystemMessage();
        networkDiscoveryControlViewPage.checkDeleteCmDomainNotification(cmDomainName);
    }

    @Test(priority = 43)
    public void deleteIpLink() {
        HomePage homePage = new HomePage(driver);
        homePage.goToHomePage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        SideMenu sideMenu = SideMenu.create(driver, webDriverWait);
        sideMenu.callActionByLabel("LAB Network View", "Favourites", "SeleniumTests");
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        networkViewPage.useContextAction("add_to_view_group", "Trail");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.queryElementAndAddItToView("label", TEXT_FIELD, trailName);
        networkViewPage.useContextAction("EDIT", "Delete Trail");
        networkViewPage.delateTrailWizard();
    }

    @Test(priority = 44)
    public void deletePhysicalDevice() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        DelayUtils.sleep(5000);
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        networkViewPage.useContextAction("add_to_view_group", "Device");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.queryElementAndAddItToView("serialNumber", TEXT_FIELD, serialNumber);
        networkViewPage.useContextAction("EDIT", "Delete Element");
        networkViewPage.clickConfirmationBoxButtonByLabel("Yes");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        checkMessageSize(1);
        checkMessageType();
    }

    @Test(priority = 45)
    public void deleteMediation() {
        ViewConnectionConfigurationPage.goToViewConnectionConfigurationPage(driver, URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        ViewConnectionConfigurationPage viewConnectionConfigurationPage = new ViewConnectionConfigurationPage(driver);
        viewConnectionConfigurationPage.selectRow("Address", address);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        viewConnectionConfigurationPage.useContextAction("Delete object");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        viewConnectionConfigurationPage.clickDelete();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 46)
    public void deleteIPAddressAssignment() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        IPAddressManagementViewPage ipAddressManagementViewPage = IPAddressManagementViewPage.goToIPAddressManagementPage(driver, BASIC_URL);
        ipAddressManagementViewPage.searchIpNetwork(ipNetwork);
        ipAddressManagementViewPage.expandTreeRow(ipNetwork);
        ipAddressManagementViewPage.expandTreeRowContains("%");
        ipAddressManagementViewPage.expandTreeRow("10.10.20.11/24");
        ipAddressManagementViewPage.deleteObject("/24 [");
    }

    private void checkMessageType() {
        Assert.assertEquals(MessageType.SUCCESS, (getFirstMessage().getMessageType()));
    }

    private void checkMessageContainsText(String message) {
        Assert.assertTrue((getFirstMessage().getText())
                .contains(message));
    }

    private void checkMessageText(String message) {
        Assert.assertEquals(message, (getFirstMessage().getText()));
    }

    private void checkMessageSize(int size) {
        Assert.assertEquals((SystemMessageContainer.create(driver, webDriverWait)
                .getMessages()
                .size()), size);
    }

    private Message getFirstMessage() {
        return SystemMessageContainer.create(driver, webDriverWait)
                .getFirstMessage()
                .orElseThrow(() -> new RuntimeException("The list is empty"));
    }

    private void checkTaskAssignment() {
        checkMessageType();
        checkMessageText("The task properly assigned.");
    }

    private void checkTaskCompleted() {
        checkMessageType();
        checkMessageContainsText("Task properly completed.");
    }
}

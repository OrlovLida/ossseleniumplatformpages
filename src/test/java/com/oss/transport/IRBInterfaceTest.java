package com.oss.transport;

import java.time.LocalDate;
import java.util.regex.Pattern;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.alerts.SystemMessageContainer;
import com.oss.framework.alerts.SystemMessageContainer.Message;
import com.oss.framework.alerts.SystemMessageContainer.MessageType;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.framework.widgets.tablewidget.OldTable;
import com.oss.framework.widgets.tablewidget.TableInterface;
import com.oss.pages.bpm.IntegrationProcessWizardPage;
import com.oss.pages.bpm.ProcessWizardPage;
import com.oss.pages.bpm.TasksPage;
import com.oss.pages.platform.NewInventoryViewPage;
import com.oss.pages.transport.IRBInterfaceWizardPage;
import com.oss.pages.transport.ipam.IPAddressManagementViewPage;
import com.oss.utils.TestListener;

import io.qameta.allure.Description;

@Listeners({ TestListener.class })
public class IRBInterfaceTest extends BaseTestCase {

    private NewInventoryViewPage newInventoryViewPage;
    private String processNRPCode;
    private String processIPCode;
    private String perspectiveContext;
    private String processIPName = "IRBInterfaceSeleniumTest-" + (int) (Math.random() * 1001);

    private static final String IRB_INTERFACE_ID = "117";
    private static final String MTU_VALUE = "1000";
    private static final String DESCRIPTION = "IRBInterfaceSeleniumTest" + (int) (Math.random() * 1001);
    private static final String IRB_INTERFACE_DEVICE_NAME = "IRBInterfaceSeleniumTest";
    private static final String IP_SUBNET = "10.10.20.0/24 [E2ESeleniumTest]";
    private static final String IP_NETWORK = "E2ESeleniumTest";
    private static final String IP_ADDRESS = "10.10.20.2";

    @BeforeClass
    public void openWebConsole() {
        waitForPageToLoad();
        homePage.chooseFromLeftSideMenu("Process Instances", "Views", "Business Process Management");
        newInventoryViewPage = new NewInventoryViewPage(driver);
        waitForPageToLoad();
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
    @Description("Create new IRB Interface")
    public void createNewIRBInterface() {
        homePage.goToHomePage(driver, BASIC_URL);
        waitForPageToLoad();
        homePage.chooseFromLeftSideMenu("IRB Interface", "Wizards", "Transport");
        IRBInterfaceWizardPage irbInterfaceWizardPage = new IRBInterfaceWizardPage(driver);
        waitForPageToLoad();
        irbInterfaceWizardPage.createIRBInterface(IRB_INTERFACE_DEVICE_NAME, IRB_INTERFACE_ID);
    }

    @Test(priority = 4)
    @Description("Checks if IRB Interface is visible in New Inventory View")
    public void checkIRBInterface() {
        homePage.goToHomePage(driver, BASIC_URL);
        homePage.setNewObjectType("IRB Interface");
        waitForPageToLoad();
//        newInventoryViewPage.setFilterPanel("shortIdentifier", IRB_INTERFACE_DEVICE_NAME);
        newInventoryViewPage.setFilterPanel("name", IRB_INTERFACE_ID);
        waitForPageToLoad();
        Assert.assertFalse(newInventoryViewPage.checkIfTableIsEmpty());
    }

    @Test(priority = 5)
    @Description("Assign IP Host Address")
    public void assignIPHostAddress() {
        newInventoryViewPage.selectFirstRow();
        waitForPageToLoad();
        newInventoryViewPage.callAction("CREATE", "AssignIPv4Host_TP");
        IRBInterfaceWizardPage irbInterfaceWizardPage = new IRBInterfaceWizardPage(driver);
        irbInterfaceWizardPage.assignIPtoIRBInterface(IP_ADDRESS, IP_SUBNET);
        waitForPageToLoad();
    }

    @Test(priority = 6)
    @Description("Edit IRB Interface")
    public void editIRBInterface() {
        newInventoryViewPage.selectFirstRow();
        waitForPageToLoad();
        newInventoryViewPage.callAction("EDIT", "EditIRBInterfaceContextAction");
        IRBInterfaceWizardPage irbInterfaceWizardPage = new IRBInterfaceWizardPage(driver);
        irbInterfaceWizardPage.editIRBInterface(MTU_VALUE, DESCRIPTION);
        waitForPageToLoad();
        DelayUtils.sleep(3000);
        newInventoryViewPage.callAction("KEBAB", "refreshButton");
        waitForPageToLoad();
        Assert.assertEquals(MTU_VALUE, newInventoryViewPage.getMainTable().getCellValue(0, "MTU"));
        Assert.assertEquals(DESCRIPTION, newInventoryViewPage.getMainTable().getCellValue(0,"Description"));
    }

    @Test(priority = 7)
    @Description("Complete High Level Planning Task")
    public void completeHLPTask() {
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.completeTask(processNRPCode, "High Level Planning");
        checkTaskCompleted();
    }

    @Test(priority = 8)
    @Description("Start Low Level Planning Task")
    public void startLLPTask() {
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.startTask(processNRPCode, "Low Level Planning");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        checkTaskAssignment();
        String currentUrl = driver.getCurrentUrl();
        String[] split = currentUrl.split(Pattern.quote("?"));
        perspectiveContext = split[1];
        Assert.assertTrue(perspectiveContext.contains("PLAN"));
    }

    @Test(priority = 9)
    @Description("Complete Low Level Planning Task")
    public void completeLLPTask() {
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.completeTask(processNRPCode, "Low Level Planning");
        checkTaskCompleted();
    }

    @Test(priority = 10)
    @Description("Start Ready for Integration Task")
    public void startRFITask() {
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.startTask(processNRPCode, "Ready for Integration");
        checkTaskAssignment();
    }

    @Test(priority = 11)
    @Description("Setup Integration")
    public void setupIntegration() {
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.setupIntegration(processNRPCode);
        IntegrationProcessWizardPage integrationWizard = new IntegrationProcessWizardPage(driver);
        integrationWizard.defineIntegrationProcess(processIPName, LocalDate.now().plusDays(0).toString(), 1);
        waitForPageToLoad();
        integrationWizard.clickNext();
        waitForPageToLoad();
        integrationWizard.clickAccept();
    }

    @Test(priority = 12)
    @Description("Get IP Code")
    public void getIPCode() {
        TableInterface ipTable = OldTable.createByComponentId(driver, webDriverWait, "form.specific.ip_involved_nrp_group.ip_involved_nrp_table");
        processIPCode = ipTable.getCellValue(0, "Code");
        System.out.println(processIPCode);
    }

    @Test(priority = 13)
    @Description("Complete Ready for Integration Task")
    public void completeRFITask() {
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.completeTask(processNRPCode, "Ready for Integration");
        checkTaskCompleted();
    }

    @Test(priority = 14)
    @Description("Start Scope definition Task")
    public void startSDTaskIP() {
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.startTask(processIPCode, "Scope definition");
        checkTaskAssignment();
    }

    @Test(priority = 15)
    @Description("Complete Scope definition Task")
    public void completeSDTaskIP() {
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.completeTask(processIPCode, "Scope definition");
        checkTaskCompleted();
    }

    @Test(priority = 16)
    @Description("Start Implementation Task")
    public void startImplementationTaskIP() {
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.startTask(processIPCode, "Implementation");
        checkTaskAssignment();
    }

    @Test(priority = 17)
    @Description("Complete Implementation Task")
    public void completeImplementationTaskIP() {
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.completeTask(processIPCode, "Implementation");
        checkTaskCompleted();
    }

    @Test(priority = 18)
    @Description("Start Acceptance Task")
    public void startAcceptanceTaskIP() {
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.startTask(processIPCode, "Acceptance");
        checkTaskAssignment();
    }

    @Test(priority = 19)
    @Description("Complete Acceptance Task")
    public void completeAcceptanceTaskIP() {
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.completeTask(processIPCode, "Acceptance");
        checkTaskCompleted();
    }

    @Test(priority = 20)
    @Description("Delete IP Address")
    public void deleteIPAddressAssignment() {
        homePage.goToHomePage(driver, BASIC_URL);
        homePage.chooseFromLeftSideMenu("IP Address management", "Views", "Transport");
        IPAddressManagementViewPage ipAddressManagementViewPage = IPAddressManagementViewPage.goToIPAddressManagementPage(driver, BASIC_URL);
        ipAddressManagementViewPage.searchIpNetwork(IP_NETWORK);
        ipAddressManagementViewPage.expandTreeRow(IP_NETWORK);
        ipAddressManagementViewPage.expandTreeRowContains("%");
        ipAddressManagementViewPage.expandTreeRow(IP_ADDRESS + "/24");
        ipAddressManagementViewPage.deleteObject("/24 [");
    }

    @Test(priority = 21)
    @Description("Delete IRB Interface")
    public void deleteIRBInterface() {
        homePage.goToHomePage(driver, BASIC_URL);
        homePage.setNewObjectType("IRB Interface");
        waitForPageToLoad();
//        newInventoryViewPage.setFilterPanel("shortIdentifier", IRB_INTERFACE_DEVICE_NAME);
//        newInventoryViewPage.setFilterPanel("name", IRB_INTERFACE_ID);
        newInventoryViewPage.setFilterPanel("description", DESCRIPTION);
        waitForPageToLoad();
        newInventoryViewPage.selectFirstRow();
        newInventoryViewPage.callAction("EDIT", "DeleteIRBInterfaceContextAction");
        waitForPageToLoad();
        Wizard.createWizard(driver, webDriverWait).clickButtonByLabel("OK");
        checkMessageType();
        newInventoryViewPage.callAction("KEBAB", "refreshButton");
        Assert.assertTrue(newInventoryViewPage.checkIfTableIsEmpty());
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

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }
}
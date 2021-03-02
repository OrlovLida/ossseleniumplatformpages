package com.oss.transport;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.alerts.SystemMessageContainer;
import com.oss.framework.alerts.SystemMessageContainer.Message;
import com.oss.framework.alerts.SystemMessageContainer.MessageType;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.bpm.ProcessWizardPage;
import com.oss.pages.bpm.TasksPage;
import com.oss.pages.platform.NewInventoryViewPage;
import com.oss.pages.transport.IRBInterfaceWizardPage;
import com.oss.pages.transport.ipam.IPAddressManagementViewPage;
import com.oss.pages.transport.ipam.IPv4AddressAssignmentWizardPage;
import com.oss.utils.TestListener;

import io.qameta.allure.Description;

@Listeners({ TestListener.class })
public class IRBInterfaceTest extends BaseTestCase {

    private NewInventoryViewPage newInventoryViewPage;
    private String processNRPCode;

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
        newInventoryViewPage.searchObject(IRB_INTERFACE_DEVICE_NAME);
        waitForPageToLoad();
        Assert.assertFalse(newInventoryViewPage.checkIfTableIsEmpty());
    }

    @Test(priority = 5)
    @Description("Assign IP Host Address")
    public void assignIPHostAddress() {
        newInventoryViewPage.selectFirstRow();
        waitForPageToLoad();
        newInventoryViewPage.callAction(ActionsContainer.CREATE_GROUP_ID, "AssignIPv4Host");
        IPv4AddressAssignmentWizardPage iPv4AddressAssignmentWizardPage = new IPv4AddressAssignmentWizardPage(driver);
        iPv4AddressAssignmentWizardPage.assignIPtoIRBInterface(IP_ADDRESS, IP_SUBNET, true);
        waitForPageToLoad();
    }

    @Test(priority = 6)
    @Description("Edit IRB Interface")
    public void editIRBInterface() {
        newInventoryViewPage.selectFirstRow();
        waitForPageToLoad();
        newInventoryViewPage.callAction(ActionsContainer.EDIT_GROUP_ID, "EditIRBInterfaceContextAction");
        IRBInterfaceWizardPage irbInterfaceWizardPage = new IRBInterfaceWizardPage(driver);
        irbInterfaceWizardPage.editIRBInterface(MTU_VALUE, DESCRIPTION);
        waitForPageToLoad();
        DelayUtils.sleep(3000);
        newInventoryViewPage.refreshMainTable();
        waitForPageToLoad();
        Assert.assertEquals(MTU_VALUE, newInventoryViewPage.getMainTable().getCellValue(0, "MTU"));
        Assert.assertEquals(DESCRIPTION, newInventoryViewPage.getMainTable().getCellValue(0, "Description"));
    }

    @Test(priority = 7)
    @Description("Finish rest of NRP and IP Tasks")
    public void finishProcessesTasks() {
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.completeNRP(processNRPCode);
    }

    @Test(priority = 8)
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

    @Test(priority = 9)
    @Description("Delete IRB Interface")
    public void deleteIRBInterface() {
        homePage.goToHomePage(driver, BASIC_URL);
        homePage.setNewObjectType("IRB Interface");
        waitForPageToLoad();
        newInventoryViewPage.searchObject(IRB_INTERFACE_DEVICE_NAME);
        waitForPageToLoad();
        newInventoryViewPage.selectFirstRow();
        newInventoryViewPage.callAction(ActionsContainer.EDIT_GROUP_ID, "DeleteIRBInterfaceContextAction");
        waitForPageToLoad();
        Wizard.createWizard(driver, webDriverWait).clickButtonByLabel("OK");
        checkMessageType();
        newInventoryViewPage.refreshMainTable();
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

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }
}
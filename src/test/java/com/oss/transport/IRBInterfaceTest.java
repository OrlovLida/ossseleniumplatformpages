package com.oss.transport;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.alerts.SystemMessageContainer.Message;
import com.oss.framework.components.alerts.SystemMessageContainer.MessageType;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.bpm.TasksPage;
import com.oss.pages.bpm.processinstances.ProcessWizardPage;
import com.oss.pages.platform.NewInventoryViewPage;
import com.oss.pages.platform.SearchObjectTypePage;
import com.oss.pages.transport.IRBInterfaceWizardPage;
import com.oss.pages.transport.ipam.IPAddressAssignmentWizardPage;
import com.oss.pages.transport.ipam.helper.IPAddressAssignmentWizardProperties;
import com.oss.utils.TestListener;

import io.qameta.allure.Description;

import static com.oss.framework.components.contextactions.ActionsContainer.CREATE_GROUP_ID;
import static com.oss.framework.components.contextactions.ActionsContainer.EDIT_GROUP_ID;

@Listeners({TestListener.class})
public class IRBInterfaceTest extends BaseTestCase {

    private static final String IRB_INTERFACE_ID = "1" + (int) (Math.random() * 1001);
    private static final String MTU_VALUE = "1000";
    private static final String DESCRIPTION = "IRBInterfaceSeleniumTest" + (int) (Math.random() * 1001);
    private static final String IRB_INTERFACE_DEVICE_NAME = "IRBInterfaceSeleniumTest";
    private static final String IP_SUBNET = "10.10.20.0/24 [E2ESeleniumTest]";
    private static final String IP_ADDRESS = "10.10.20.3";
    private static final String IRB_INTERFACE_SEARCH_NIV = "IRB Interface";
    private static final String CREATE_IRB_INTERFACE_ID = "CreateIRBInterfaceContextAction";
    private static final String DELETE_ADDRESS_IP_ID = "DeleteIPHostAddressAssignmentInternalAction";
    private static final String CONFIRMATION_REMOVAL_BOX_ID = "ConfirmationBox_deleteBoxAppId_action_button";
    private static final String CONFIRMATION_IP_REMOVAL_BOX_ID = "ConfirmationBox_removeBoxId_action_button";
    private NewInventoryViewPage newInventoryViewPage;
    private String processNRPCode;

    @BeforeClass
    public void openWebConsole() {
        waitForPageToLoad();
        homePage.chooseFromLeftSideMenu("Process Instances", "BPM and Planning", "Business Process Management");
        newInventoryViewPage = new NewInventoryViewPage(driver, webDriverWait);
        waitForPageToLoad();
    }

    @Test(priority = 1)
    @Description("Create NRP Process")
    public void createProcessNRP() {
        ProcessWizardPage processWizardPage = new ProcessWizardPage(driver);
        processNRPCode = processWizardPage.createSimpleNRP();
        checkMessageSize();
        checkMessageType();
        checkMessageContainsText(processNRPCode);
    }

    @Test(priority = 2)
    @Description("Start High Level Planning Task")
    public void startHLPTask() {
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.startTask(processNRPCode, TasksPage.HIGH_LEVEL_PLANNING_TASK);
        checkTaskAssignment();
    }

    @Test(priority = 3)
    @Description("Create new IRB Interface")
    public void createNewIRBInterface() {
        searchInInventoryView("Physical Device");
        NewInventoryViewPage newInventoryViewPage = new NewInventoryViewPage(driver, webDriverWait);
        newInventoryViewPage.searchObject(IRB_INTERFACE_DEVICE_NAME).selectFirstRow();
        newInventoryViewPage.callAction(CREATE_GROUP_ID, CREATE_IRB_INTERFACE_ID);

        waitForPageToLoad();
        IRBInterfaceWizardPage irbInterfaceWizardPage = new IRBInterfaceWizardPage(driver);
        waitForPageToLoad();
        irbInterfaceWizardPage.createIRBInterface(IRB_INTERFACE_ID);
        waitForPageToLoad();
    }

    @Test(priority = 4)
    @Description("Checks if IRB Interface is visible in New Inventory View")
    public void checkIRBInterface() {
        searchInInventoryView(IRB_INTERFACE_SEARCH_NIV);
        waitForPageToLoad();
        newInventoryViewPage.searchObject("IRB:" + IRB_INTERFACE_ID);
        waitForPageToLoad();
        Assert.assertFalse(newInventoryViewPage.checkIfTableIsEmpty());
    }

    @Test(priority = 5)
    @Description("Assign IP Host Address")
    public void assignIPHostAddress() {
        newInventoryViewPage.selectFirstRow();
        waitForPageToLoad();
        newInventoryViewPage.callAction(ActionsContainer.ASSIGN_GROUP_ID, "AssignIPv4Host");
        IPAddressAssignmentWizardPage ipAddressAssignmentWizardPage = new IPAddressAssignmentWizardPage(driver);
        IPAddressAssignmentWizardProperties ipAddressAssignmentWizardProperties = IPAddressAssignmentWizardProperties.builder()
                .address(IP_ADDRESS).subnet(IP_SUBNET).isPrimary("true").build();
        ipAddressAssignmentWizardPage.assignMoToIPAddress(ipAddressAssignmentWizardProperties);
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
        Assert.assertEquals(newInventoryViewPage.getMainTable().getCellValue(0, "mtu"), MTU_VALUE);
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
        searchInInventoryView("IP Host Assignment");
        newInventoryViewPage.searchObject(IP_ADDRESS).selectFirstRow();
        newInventoryViewPage.callAction(EDIT_GROUP_ID, DELETE_ADDRESS_IP_ID).clickConfirmationBox(CONFIRMATION_IP_REMOVAL_BOX_ID);
    }

    @Test(priority = 9)
    @Description("Delete IRB Interface")
    public void deleteIRBInterface() {
        searchInInventoryView(IRB_INTERFACE_SEARCH_NIV);
        waitForPageToLoad();
        newInventoryViewPage.searchObject("IRB:" + IRB_INTERFACE_ID);
        waitForPageToLoad();
        newInventoryViewPage.selectFirstRow();
        newInventoryViewPage.callAction(ActionsContainer.EDIT_GROUP_ID, "DeleteIRBInterfaceContextAction");
        waitForPageToLoad();
        newInventoryViewPage.clickConfirmationBox(CONFIRMATION_REMOVAL_BOX_ID);
        checkMessageType();
        newInventoryViewPage.refreshMainTable();
        Assert.assertTrue(newInventoryViewPage.checkIfTableIsEmpty());
    }

    private void checkMessageType() {
        Assert.assertEquals((getFirstMessage().getMessageType()), MessageType.SUCCESS);
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
        checkMessageType();
        checkMessageText();
    }

    private void searchInInventoryView(String object_type) {
        homePage.chooseFromLeftSideMenu("Inventory View", "Resource Inventory ");
        SearchObjectTypePage searchObjectTypePage = new SearchObjectTypePage(driver, webDriverWait);
        searchObjectTypePage.searchType(object_type);
    }

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }
}

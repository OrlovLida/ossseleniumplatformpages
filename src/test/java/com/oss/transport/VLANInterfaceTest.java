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
import com.oss.pages.transport.VLANInterfaceWizardPage;
import com.oss.pages.transport.ipam.IPAddressAssignmentWizardPage;
import com.oss.pages.transport.ipam.IPAddressManagementViewPage;
import com.oss.pages.transport.ipam.helper.IPAddressAssignmentWizardProperties;
import com.oss.utils.TestListener;

import io.qameta.allure.Description;

@Listeners({ TestListener.class })
public class VLANInterfaceTest extends BaseTestCase {

    private NewInventoryViewPage newInventoryViewPage;
    private String processNRPCode;

    private static final String VLAN_INTERFACE_TYPE = "Subinterface";
    private static final String VLAN_SUBINTERFACE_ID = "100";
    private static final String DEVICE = "VLANInterfaceTest";
    private static final String LOCATION = "VLANInterfaceSeleniumTest";
    private static final String IP_SUBNET = "126.0.0.0/24 [VLANInterfaceSeleniumTest]";
    private static final String IP_NETWORK = "VLANInterfaceSeleniumTest";
    private static final String IP_ADDRESS = "126.0.0.12";
    private static final String EDIT_VLAN_INTERFACE_ACTION_ID = "EditVLANInterfaceContextAction";
    private static final String DELETE_VLAN_INTERFACE_ACTION_ID = "DeleteVLANInterfaceContextAction";
    private static final String MTU_VALUE = "1432";
    private static final String VLAN_INTERFACE_SEARCH_NIV = "VLAN Interface";
    private static final String DESCRIPTION = "VLANInterfaceSeleniumTest" + (int) (Math.random() * 1001);

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
    @Description("Create new VLAN Interface")
    public void createNewVLANInterface() {
        homePage.goToSpecificPageWithContext(driver, "/#/view/transport/ip/ethernet/vlan-interface");
        waitForPageToLoad();
        VLANInterfaceWizardPage vlanInterfaceWizardPage = new VLANInterfaceWizardPage(driver);
        waitForPageToLoad();
        vlanInterfaceWizardPage.setType(VLAN_INTERFACE_TYPE);
        waitForPageToLoad();
        vlanInterfaceWizardPage.setSubinterfaceId(VLAN_SUBINTERFACE_ID);
        waitForPageToLoad();
        vlanInterfaceWizardPage.clickNext();
        waitForPageToLoad();
        vlanInterfaceWizardPage.setInterface(LOCATION, DEVICE, "EthernetInterface", LOCATION + "-Router-1\\GE 0");
        waitForPageToLoad();
        vlanInterfaceWizardPage.clickAccept();
        waitForPageToLoad();
    }

    @Test(priority = 4)
    @Description("Check new VLAN Interface")
    public void checkVLANInterface() {
        homePage.goToSpecificPageWithContext(driver, "/#/dashboard/predefined/id/startDashboard");
        waitForPageToLoad();
        homePage.setNewObjectType(VLAN_INTERFACE_SEARCH_NIV);
        waitForPageToLoad();
        newInventoryViewPage.searchObject(DEVICE);
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
                .address("1260012").subnet(IP_SUBNET).isPrimary("false").build();
        ipAddressAssignmentWizardPage.assignMoToIPAddress(ipAddressAssignmentWizardProperties);
        waitForPageToLoad();
    }

    @Test(priority = 6)
    @Description("Edit VLAN Interface")
    public void editVLANInterface() {
        newInventoryViewPage.selectFirstRow();
        waitForPageToLoad();
        newInventoryViewPage.callAction("EDIT", EDIT_VLAN_INTERFACE_ACTION_ID);
        VLANInterfaceWizardPage vlanInterfaceWizardPage = new VLANInterfaceWizardPage(driver);
        vlanInterfaceWizardPage.editVLANInterface(MTU_VALUE, DESCRIPTION);
        waitForPageToLoad();
        DelayUtils.sleep(3000);
        newInventoryViewPage.refreshMainTable();
        waitForPageToLoad();
        Assert.assertEquals(newInventoryViewPage.getMainTable().getCellValue(0, "mtu"), MTU_VALUE);
        Assert.assertEquals(newInventoryViewPage.getMainTable().getCellValue(0, "description"), DESCRIPTION);
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
        IPAddressManagementViewPage ipAddressManagementViewPage =
                IPAddressManagementViewPage.goToIPAddressManagementViewPageLive(driver, BASIC_URL);
        ipAddressManagementViewPage.searchIpNetwork(IP_NETWORK);
        ipAddressManagementViewPage.expandTreeRow(IP_NETWORK);
        ipAddressManagementViewPage.expandTreeRowContains("%");
        ipAddressManagementViewPage.deleteIPHost(IP_ADDRESS + "/24");
    }

    @Test(priority = 9)
    @Description("Delete VLAN Interface")
    public void deleteVLANInterface() {
        homePage.goToSpecificPageWithContext(driver, "/#/dashboard/predefined/id/startDashboard");
        waitForPageToLoad();
        homePage.setNewObjectType(VLAN_INTERFACE_SEARCH_NIV);
        waitForPageToLoad();
        newInventoryViewPage.searchObject(DEVICE);
        waitForPageToLoad();
        newInventoryViewPage.selectFirstRow();
        newInventoryViewPage.callAction(ActionsContainer.EDIT_GROUP_ID, DELETE_VLAN_INTERFACE_ACTION_ID);
        waitForPageToLoad();
        Wizard.createWizard(driver, webDriverWait).clickButtonByLabel("OK");
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

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

}

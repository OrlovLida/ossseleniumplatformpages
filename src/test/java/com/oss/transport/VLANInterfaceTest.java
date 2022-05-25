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
import com.oss.pages.platform.HierarchyViewPage;
import com.oss.pages.platform.NewInventoryViewPage;
import com.oss.pages.platform.SearchObjectTypePage;
import com.oss.pages.transport.VLANInterfaceWizardPage;
import com.oss.pages.transport.ipam.IPAddressAssignmentWizardPage;
import com.oss.pages.transport.ipam.helper.IPAddressAssignmentWizardProperties;
import com.oss.utils.TestListener;

import io.qameta.allure.Description;

import static com.oss.framework.components.contextactions.ActionsContainer.CREATE_GROUP_ID;
import static com.oss.framework.components.contextactions.ActionsContainer.EDIT_GROUP_ID;
import static com.oss.framework.components.contextactions.ActionsContainer.SHOW_ON_GROUP_ID;

@Listeners({TestListener.class})
public class VLANInterfaceTest extends BaseTestCase {

    private static final String VLAN_INTERFACE_TYPE = "Subinterface";
    private static final String VLAN_SUBINTERFACE_ID = "100";
    private static final String DEVICE = "SeleniumTestDeviceVLANInterface";
    private static final String IP_SUBNET = "126.0.0.0/24 [VLANInterfaceSeleniumTest]";
    private static final String IP_NETWORK = "VLANInterfaceSeleniumTest";
    private static final String IP_ADDRESS = "126.0.0.20";
    private static final String EDIT_VLAN_INTERFACE_ACTION_ID = "EditVLANInterfaceContextAction";
    private static final String DELETE_VLAN_INTERFACE_ACTION_ID = "DeleteVLANInterfaceContextAction";
    private static final String MTU_VALUE = "1432";
    private static final String VLAN_INTERFACE_SEARCH_NIV = "VLAN Interface";
    private static final String DESCRIPTION = "VLANInterfaceSeleniumTest" + (int) (Math.random() * 1001);
    private static final String HIERARCHY_VIEW_ID = "HierarchyView";
    private static final String PORT_NAME = "GE 0";
    private static final String CREATE_VLAN_ACTION_ID = "CreateVLANInterfaceContextAction";
    private static final String LABEL_PATH = DEVICE + ".Ports." + PORT_NAME + ".Termination Points.EthernetInterface_TP." + PORT_NAME;
    private static final String DELETE_ADDRESS_IP_ID = "DeleteIPHostAddressAssignmentInternalAction";
    private static final String CONFIRMATION_REMOVAL_BOX_ID = "ConfirmationBox_removeBoxId_action_button";
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
    @Description("Create new VLAN Interface")
    public void createNewVLANInterface() {
        searchInInventoryView("Physical Device");
        NewInventoryViewPage newInventoryViewPage = new NewInventoryViewPage(driver, webDriverWait);
        newInventoryViewPage.searchObject(DEVICE).selectFirstRow();
        newInventoryViewPage.callAction(SHOW_ON_GROUP_ID, HIERARCHY_VIEW_ID);
        HierarchyViewPage hierarchyViewPage = new HierarchyViewPage(driver);
        hierarchyViewPage.selectNodeByLabelsPath(LABEL_PATH);
        hierarchyViewPage.useTreeContextAction(CREATE_GROUP_ID, CREATE_VLAN_ACTION_ID);

        waitForPageToLoad();
        VLANInterfaceWizardPage vlanInterfaceWizardPage = new VLANInterfaceWizardPage(driver);
        waitForPageToLoad();
        vlanInterfaceWizardPage.setType(VLAN_INTERFACE_TYPE);
        waitForPageToLoad();
        vlanInterfaceWizardPage.setSubinterfaceId(VLAN_SUBINTERFACE_ID);
        waitForPageToLoad();
        vlanInterfaceWizardPage.clickAccept();
        waitForPageToLoad();
    }

    @Test(priority = 4)
    @Description("Check new VLAN Interface")
    public void checkVLANInterface() {
        searchInInventoryView(VLAN_INTERFACE_SEARCH_NIV);
        newInventoryViewPage.searchObject(DEVICE).selectFirstRow();
        waitForPageToLoad();
        Assert.assertFalse(newInventoryViewPage.checkIfTableIsEmpty());
    }

    @Test(priority = 5)
    @Description("Assign IP Host Address")
    public void assignIPHostAddress() {
        newInventoryViewPage.callAction(ActionsContainer.ASSIGN_GROUP_ID, "AssignIPv4Host");
        IPAddressAssignmentWizardPage ipAddressAssignmentWizardPage = new IPAddressAssignmentWizardPage(driver);
        IPAddressAssignmentWizardProperties ipAddressAssignmentWizardProperties = IPAddressAssignmentWizardProperties.builder()
                .address(IP_ADDRESS).subnet(IP_SUBNET).isPrimary("false").build();
        ipAddressAssignmentWizardPage.assignMoToIPAddress(ipAddressAssignmentWizardProperties);
        waitForPageToLoad();
    }

    @Test(priority = 6)
    @Description("Edit VLAN Interface")
    public void editVLANInterface() {
        newInventoryViewPage.selectFirstRow();
        waitForPageToLoad();
        newInventoryViewPage.callAction(EDIT_GROUP_ID, EDIT_VLAN_INTERFACE_ACTION_ID);
        VLANInterfaceWizardPage vlanInterfaceWizardPage = new VLANInterfaceWizardPage(driver);
        vlanInterfaceWizardPage.editVLANInterface(MTU_VALUE, DESCRIPTION);
        waitForPageToLoad();
        newInventoryViewPage.unselectObjectByRowId(0);
        DelayUtils.sleep(3000);
        newInventoryViewPage.refreshMainTable();
        newInventoryViewPage.selectFirstRow();

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
        newInventoryViewPage.searchObject(IP_NETWORK).selectFirstRow();
        newInventoryViewPage.callAction(EDIT_GROUP_ID, DELETE_ADDRESS_IP_ID).clickConfirmationBox(CONFIRMATION_REMOVAL_BOX_ID);
    }

    @Test(priority = 9)
    @Description("Delete VLAN Interface")
    public void deleteVLANInterface() {
        searchInInventoryView("VLAN Interface");
        newInventoryViewPage.searchObject(DEVICE);
        waitForPageToLoad();
        newInventoryViewPage.selectFirstRow();
        newInventoryViewPage.callAction(ActionsContainer.EDIT_GROUP_ID, DELETE_VLAN_INTERFACE_ACTION_ID);
        waitForPageToLoad();
        newInventoryViewPage.clickConfirmationRemovalButton();
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

    private void searchInInventoryView(String object_type) {
        homePage.chooseFromLeftSideMenu("Inventory View", "Resource Inventory ");
        SearchObjectTypePage searchObjectTypePage = new SearchObjectTypePage(driver, webDriverWait);
        searchObjectTypePage.searchType(object_type);
    }
}

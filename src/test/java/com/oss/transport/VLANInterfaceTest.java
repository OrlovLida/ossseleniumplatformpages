package com.oss.transport;

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
import com.oss.pages.bpm.ProcessWizardPage;
import com.oss.pages.bpm.TasksPage;
import com.oss.pages.platform.NewInventoryViewPage;
import com.oss.pages.transport.VLANInterfaceWizardPage;
import com.oss.pages.transport.ipam.IPAddressManagementViewPage;
import com.oss.pages.transport.ipam.IPv4AddressAssignmentWizardPage;
import com.oss.utils.TestListener;

import io.qameta.allure.Description;

@Listeners({ TestListener.class })
public class VLANInterfaceTest extends BaseTestCase {

    private NewInventoryViewPage newInventoryViewPage;
    private String processNRPCode;

    private static final String VLAN_INTERFACE_TYPE = "Subinterface";
    private static final String VLAN_SUBINTERFACE_ID = "100";
    private static final String LOCATION = "VLANInterfaceSeleniumTest";
    private static final String IP_SUBNET = "126.0.0.0/24 [VLANInterfaceSeleniumTest]";
    private static final String IP_NETWORK = "VLANInterfaceSeleniumTest";
    private static final String IP_ADDRESS = "126.0.0.1";
    private static final String EDIT_VLAN_INTERFACE_ACTION_ID = "EditVLANInterfaceContextAction";
    private static final String DELETE_VLAN_INTERFACE_ACTION_ID = "DeleteVLANInterfaceContextAction";
    private static final String MTU_VALUE = "1432";
    private static final String DESCRIPTION = "VLANInterfaceSeleniumTest" + (int) (Math.random() * 1001);

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
    @Description("Create new VLAN Interface")
    public void createNewVLANInterface() {
        homePage.goToHomePage(driver, BASIC_URL);
        waitForPageToLoad();
        homePage.chooseFromLeftSideMenu("VLAN Interface", "Wizards", "Transport");
        VLANInterfaceWizardPage vlanInterfaceWizardPage = new VLANInterfaceWizardPage(driver);
        waitForPageToLoad();
        vlanInterfaceWizardPage.setType(VLAN_INTERFACE_TYPE);
        waitForPageToLoad();
        vlanInterfaceWizardPage.setSubinterfaceId(VLAN_SUBINTERFACE_ID);
        waitForPageToLoad();
        vlanInterfaceWizardPage.clickNext();
        waitForPageToLoad();
        vlanInterfaceWizardPage.setLocation(LOCATION);
        waitForPageToLoad();
        vlanInterfaceWizardPage.clickAccept();
        waitForPageToLoad();
    }

    @Test(priority = 4)
    @Description("Check new VLAN Interface")
    public void checkVLANInterface() {
        homePage.goToHomePage(driver, BASIC_URL);
        homePage.setNewObjectType("VLAN Interface");
        waitForPageToLoad();
        newInventoryViewPage.searchObject(LOCATION);
        waitForPageToLoad();
        Assert.assertFalse(newInventoryViewPage.checkIfTableIsEmpty());
    }

    @Test(priority = 5)
    @Description("Assign IP Host Address")
    public void assignIPHostAddress() {
        newInventoryViewPage.selectFirstRow();
        waitForPageToLoad();
        newInventoryViewPage.callAction("CREATE", "AssignIPv4Host_TP");
        IPv4AddressAssignmentWizardPage iPv4AddressAssignmentWizardPage = new IPv4AddressAssignmentWizardPage(driver);
        iPv4AddressAssignmentWizardPage.assignIPtoIRBInterface(IP_ADDRESS, IP_SUBNET, false);
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
        newInventoryViewPage.callAction("KEBAB", "refreshButton");
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
    @Description("Delete VLAN Interface")
    public void deleteVLANInterface() {
        homePage.goToHomePage(driver, BASIC_URL);
        homePage.setNewObjectType("VLAN Interface");
        waitForPageToLoad();
        newInventoryViewPage.searchObject(LOCATION);
        waitForPageToLoad();
        newInventoryViewPage.selectFirstRow();
        newInventoryViewPage.callAction("EDIT", DELETE_VLAN_INTERFACE_ACTION_ID);
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

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

}
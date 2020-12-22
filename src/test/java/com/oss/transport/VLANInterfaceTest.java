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
import com.oss.pages.bpm.ProcessWizardPage;
import com.oss.pages.bpm.TasksPage;
import com.oss.pages.platform.NewInventoryViewPage;
import com.oss.pages.transport.VLANInterfaceWizardPage;
import com.oss.utils.TestListener;

import io.qameta.allure.Description;

@Listeners({ TestListener.class })
public class VLANInterfaceTest extends BaseTestCase {

    private NewInventoryViewPage newInventoryViewPage;
    private String processNRPCode;

    private static final String VLAN_INTERFACE_TYPE = "Subinterface";
    private static final String VLAN_SUBINTERFACE_ID = "100";
    private static final String LOCATION = "VLANInterfaceSeleniumTest";

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
        newInventoryViewPage.setFilterPanel("location", LOCATION);
        waitForPageToLoad();
        Assert.assertFalse(newInventoryViewPage.checkIfTableIsEmpty());
    }

    @Test(priority = 5)
    @Description("Start High Level Planning Task")
    public void finishProcessesTasks() {
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.completeNRP(processNRPCode);
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

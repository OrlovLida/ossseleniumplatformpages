package com.oss.transport;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.alerts.SystemMessageInterface;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.prompts.ConfirmationBox;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.bpm.TasksPage;
import com.oss.pages.bpm.processinstances.ProcessWizardPage;
import com.oss.pages.platform.HomePage;
import com.oss.pages.platform.NewInventoryViewPage;
import com.oss.pages.transport.NetworkViewPage;
import com.oss.pages.transport.trail.ConnectionWizardPage;
import com.oss.pages.transport.trail.RoutingWizardPage;
import com.oss.utils.TestListener;

import io.qameta.allure.Description;

import static com.oss.framework.components.inputs.Input.ComponentType.TEXT_FIELD;

@Listeners({ TestListener.class })
public class IPLinkTest extends BaseTestCase {
    private static final String IP_LINK_NAME = "IPLink_Selenium";
    private static final String NEW_IP_LINK_NAME = "IPLink_Selenium2";
    private static final String ETHERNET_LINK_NAME = "EthernetLink_Selenium";
    private static final String BUSINESS_PROCESS_MANAGEMENT = "Business Process Management";
    private static final String BPM_AND_PLANNING = "BPM and Planning";
    private static final String PROCESS_INSTANCES = "Process Instances";
    private NetworkViewPage networkViewPage;
    private String processNRPCode;

    @Test(priority = 1)
    @Description("Create NRP process")
    public void createProcessNRP() {
        homePage.chooseFromLeftSideMenu(PROCESS_INSTANCES, BPM_AND_PLANNING, BUSINESS_PROCESS_MANAGEMENT);
        ProcessWizardPage processWizardPage = new ProcessWizardPage(driver);//add wait
        processNRPCode = processWizardPage.createSimpleNRPV2();
        checkPopup(processNRPCode);
    }

    @Test(priority = 2)
    @Description("Start HLP task")
    public void startHLP() {
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        tasksPage.startTask(processNRPCode, TasksPage.HIGH_LEVEL_PLANNING_TASK);
        checkPopup("The task properly assigned.");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 3)
    @Description("Create IP Link")
    public void createIPLink() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        openView();
        networkViewPage = new NetworkViewPage(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.useContextAction(NetworkViewPage.ADD_TO_VIEW_ACTION, NetworkViewPage.DEVICE_ACTION);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.queryElementAndAddItToView("name", "SeleniumIPLinkTests");

        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.useContextAction(NetworkViewPage.ADD_TO_VIEW_ACTION, NetworkViewPage.DEVICE_ACTION);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.queryElementAndAddItToView("name", "SeleniumIPLinkTests2");

        networkViewPage.expandDockedPanel("left");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.selectObjectInViewContent("Name", "SeleniumIPLinkTests");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        networkViewPage.useContextAction(ActionsContainer.CREATE_GROUP_ID, NetworkViewPage.CREATE_CONNECTION_ID);
        networkViewPage.selectTrailType("IP Link");
        networkViewPage.acceptTrailType();
        ConnectionWizardPage connectionWizardPage = new ConnectionWizardPage(driver);
        connectionWizardPage.setName(IP_LINK_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        connectionWizardPage.setCapacityUnit("Mbps");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        connectionWizardPage.setCapacityValue("100");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        connectionWizardPage.clickNext();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        connectionWizardPage.clickAccept();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 4)
    @Description("Edit IP Link termination")
    public void editIPLinkTermination() {
        networkViewPage.expandDockedPanel("left");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.selectObjectInViewContent("Name", "IPLink_Selenium");
        networkViewPage.expandDockedPanel("bottom");
        updateTermination("Start");
        updateTermination("End");
    }

    @Test(priority = 5)
    @Description("Create ethernet link")
    public void createEthernetLink() {
        networkViewPage.expandDockedPanel("left");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.selectObjectInViewContent("Name", "SeleniumIPLinkTests");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.selectObjectInViewContent("Name", "SeleniumIPLinkTests2");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.useContextAction(ActionsContainer.CREATE_GROUP_ID, NetworkViewPage.CREATE_CONNECTION_ID);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.selectTrailType("Ethernet Link");
        networkViewPage.acceptTrailType();
        ConnectionWizardPage connectionWizardPage = new ConnectionWizardPage(driver);
        connectionWizardPage.setName(ETHERNET_LINK_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        connectionWizardPage.setEthernetLinkSpeed("100M");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        connectionWizardPage.clickNext();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        connectionWizardPage.clickAccept();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

    }

    @Test(priority = 6)
    @Description("Add routing to IP Link")
    public void addRouting() {
        networkViewPage.expandDockedPanel("left");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.selectObjectInViewContent("Name", "IPLink_Selenium (0%)");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.startEditingSelectedTrail();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.selectObjectInViewContent("Name", "SeleniumIPLinkTests");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.selectObjectInViewContent("Name", "SeleniumIPLinkTests2");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.selectObjectInViewContent("Name", "EthernetLink_Selenium (0%)");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        RoutingWizardPage routingWizardPage = networkViewPage.addSelectedObjectsToRouting();
        routingWizardPage.proceed();
        //Adapt usage of method to new definition
        //Assert.assertTrue(networkViewPage.isObjectInRouting1stLevel(ETHERNET_LINK_NAME));
    }

    @Test(priority = 7)
    @Description("Edit IP Link attributes")
    public void editIPLink() {
        networkViewPage.useContextAction(ActionsContainer.EDIT_GROUP_ID, NetworkViewPage.ATTRIBUTES_AND_TERMINATIONS_ACTION);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        ConnectionWizardPage connectionWizardPage = new ConnectionWizardPage(driver);
        connectionWizardPage.setName(NEW_IP_LINK_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        connectionWizardPage.setCapacityValue("110");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        connectionWizardPage.clickNext();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        connectionWizardPage.clickAccept();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 8)
    @Description("Checks if IP Link was created correctly")
    public void checkIPLink() {
        HomePage homePage = new HomePage(driver);
        homePage.goToHomePageWithContext(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        homePage.chooseFromLeftSideMenu("Legacy Inventory Dashboard", "Resource Inventory");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        homePage.setNewObjectType("Trail");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        NewInventoryViewPage newInventoryViewPage = new NewInventoryViewPage(driver, webDriverWait);
        newInventoryViewPage.searchObject(NEW_IP_LINK_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertFalse(newInventoryViewPage.checkIfTableIsEmpty());
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 9)
    @Description("Finish NRP process")
    public void finishNRP() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.completeNRP(processNRPCode);
    }

    @Test(priority = 10)
    @Description("Delete IP Link")
    public void deleteIPLink() {
        NewInventoryViewPage newInventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, "Trail");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        newInventoryViewPage.searchObject(NEW_IP_LINK_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        newInventoryViewPage.selectFirstRow();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.useContextActionAndClickConfirmation(ActionsContainer.EDIT_GROUP_ID, NetworkViewPage.DELETE_CONNECTION_ID, ConfirmationBox.DELETE);
        checkPopupStatus();
    }

    private void openView() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        homePage = new HomePage(driver);
        homePage.goToHomePageWithContext(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        homePage.chooseFromLeftSideMenu("Network View", "Resource Inventory");
    }

    private void checkPopupStatus() {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assert.assertEquals(messages.get(0).getMessageType(), SystemMessageContainer.MessageType.SUCCESS);
    }

    private void checkPopup(String text) {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assert.assertEquals(messages.size(), 1);
        Assert.assertEquals(messages.get(0).getMessageType(), SystemMessageContainer.MessageType.SUCCESS);
        Assert.assertTrue((messages.get(0).getText()).contains(text));
    }

    private void updateTermination(String type) {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.selectTermination("Type", type);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.modifyTermination();
        ConnectionWizardPage connectionWizardPage = new ConnectionWizardPage(driver);
        connectionWizardPage.terminateCardComponent("No Card/Component");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        connectionWizardPage.terminatePort("GE 0");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        connectionWizardPage.terminateTerminationPort("GE 0");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        connectionWizardPage.clickAccept();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }
}

package com.oss.transport;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.alerts.PopupAlert;
import com.oss.framework.alerts.SystemMessageContainer;
import com.oss.framework.alerts.SystemMessageContainer.Message;
import com.oss.framework.alerts.SystemMessageContainer.MessageType;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.bpm.ProcessWizardPage;
import com.oss.pages.bpm.TasksPage;
import com.oss.pages.platform.NewInventoryViewPage;
import com.oss.pages.transport.NetworkViewPage;
import com.oss.pages.transport.trail.IPPathWizardPage;
import com.oss.pages.transport.trail.RoutingWizardPage;
import com.oss.utils.TestListener;

import io.qameta.allure.Description;

import static com.oss.framework.components.inputs.Input.ComponentType.TEXT_FIELD;

@Listeners({ TestListener.class })
public class IPPathTest extends BaseTestCase {

    private String processNRPCode;

    private static final String IP_PATH_NAME_1 = "IPPathSeleniumTest";
    private static final String CAPACITY_VALUE_1 = "100";
    private static final String IP_PATH_NAME_2 = "ModifiedIPPathSeleniumTest";
    private static final String TRAIL_FOR_ROUTING = "SeleniumIPLinkForRouting";
    private static final String CAPACITY_VALUE_2 = "110";
    private static final String DEVICE_1 = "SeleniumTestDevice_IPPath_1";
    private static final String DEVICE_2 = "SeleniumTestDevice_IPPath_2";

    @BeforeClass
    public void openWebConsole() {
        waitForPageToLoad();
        homePage.chooseFromLeftSideMenu("Process Instances", "Views", "Business Process Management");
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
    @Description("Create new IP Path")
    public void createNew() {
        homePage.goToHomePage(driver, BASIC_URL);
        waitForPageToLoad();
        homePage.chooseFromLeftSideMenu("Network View", "Views", "Transport");
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        waitForPageToLoad();

        networkViewPage.useContextAction("add_to_view_group", "Connection");
        waitForPageToLoad();
        networkViewPage.queryElementAndAddItToView("name", TEXT_FIELD, TRAIL_FOR_ROUTING);
        waitForPageToLoad();

        networkViewPage.expandDockedPanel("left");
        waitForPageToLoad();
        networkViewPage.unselectObject(TRAIL_FOR_ROUTING);
        waitForPageToLoad();
        networkViewPage.selectObjectInViewContent("Name", DEVICE_1);
        waitForPageToLoad();
        networkViewPage.selectObjectInViewContent("Name", DEVICE_2);
        waitForPageToLoad();

        networkViewPage.useContextAction("CREATE", "Create Connection");
        waitForPageToLoad();
        networkViewPage.selectTrailType("IP Path");
        networkViewPage.acceptTrailType();
        IPPathWizardPage ipPathWizardPage = IPPathWizardPage.getIPPathWizardPage(driver, webDriverWait);
        ipPathWizardPage.setName(IP_PATH_NAME_1);
        ipPathWizardPage.setCapacityValue(CAPACITY_VALUE_1);
        ipPathWizardPage.proceed();
        waitForPageToLoad();
    }

    @Test(priority = 4)
    @Description("Edit IP Path")
    public void edit() {
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        networkViewPage.useContextAction("EDIT", "Attributes and terminations");
        IPPathWizardPage ipPathWizardPage = IPPathWizardPage.getIPPathWizardPage(driver, webDriverWait);
        ipPathWizardPage.setName(IP_PATH_NAME_2);
        ipPathWizardPage.setCapacityValue(CAPACITY_VALUE_2);
        ipPathWizardPage.proceed();
        waitForPageToLoad();
    }

    @Test(priority = 4)
    @Description("Set Routing for IP Path")
    public void setRouting() {
        waitForPageToLoad();
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        networkViewPage.startEditingSelectedTrail();
        networkViewPage.unselectObject(IP_PATH_NAME_2);
        networkViewPage.selectObject(TRAIL_FOR_ROUTING);
        RoutingWizardPage routingWizard = networkViewPage.addSelectedObjectsToRouting();
        routingWizard.proceed();
        waitForPageToLoad();
        networkViewPage.stopEditingTrail();
        waitForPageToLoad();
        networkViewPage.unselectObject(TRAIL_FOR_ROUTING);
        networkViewPage.selectObject(IP_PATH_NAME_2);
        Assert.assertTrue(networkViewPage.isObjectInRouting1stLevel(TRAIL_FOR_ROUTING));
    }

    @Test(priority = 6)
    @Description("Check new IP Path")
    public void checkVLANInterface() {
        driver.get(String.format("%s/#/", BASIC_URL));
        PopupAlert popupAlert = PopupAlert.create(driver, webDriverWait);
        popupAlert.popupAccept();
        homePage.setNewObjectType("Trail");
        waitForPageToLoad();
        NewInventoryViewPage newInventoryViewPage = NewInventoryViewPage.getInventoryViewPage(driver, webDriverWait);
        newInventoryViewPage.searchObject(IP_PATH_NAME_2);
        waitForPageToLoad();
        Assert.assertFalse(newInventoryViewPage.checkIfTableIsEmpty());
        Assert.assertEquals(IP_PATH_NAME_2, newInventoryViewPage.getMainTable().getCellValue(0, "Label"));
        Assert.assertEquals(CAPACITY_VALUE_2 + "000000", newInventoryViewPage.getMainTable().getCellValue(0, "Capacity Value"));
    }

    @Test(priority = 7)
    @Description("Finish rest of NRP and IP Tasks")
    public void finishProcessesTasks() {
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.completeNRP(processNRPCode);
    }

    @Test(priority = 8)
    @Description("Delete IP Path")
    public void delete() {
        homePage.goToHomePage(driver, BASIC_URL);
        homePage.setNewObjectType("Trail");
        waitForPageToLoad();
        NewInventoryViewPage newInventoryViewPage = NewInventoryViewPage.getInventoryViewPage(driver, webDriverWait);
        newInventoryViewPage.searchObject(IP_PATH_NAME_2);
        waitForPageToLoad();
        newInventoryViewPage.selectFirstRow();
        newInventoryViewPage.callAction("EDIT", "DeleteTrailWizardActionId");
        waitForPageToLoad();
        Wizard.createWizard(driver, webDriverWait).clickButtonByLabel("Delete");
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

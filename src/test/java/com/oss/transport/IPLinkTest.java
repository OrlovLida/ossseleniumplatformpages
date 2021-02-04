package com.oss.transport;

import com.oss.BaseTestCase;
import com.oss.framework.alerts.SystemMessageContainer;
import com.oss.framework.alerts.SystemMessageInterface;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.bpm.ProcessWizardPage;
import com.oss.pages.bpm.TasksPage;
import com.oss.pages.platform.HomePage;
import com.oss.pages.platform.NewInventoryViewPage;
import com.oss.pages.transport.NetworkViewPage;
import com.oss.pages.transport.trail.RoutingWizardPage;
import com.oss.utils.TestListener;
import io.qameta.allure.Description;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.List;

import static com.oss.framework.components.inputs.Input.ComponentType.TEXT_FIELD;

@Listeners({TestListener.class})
public class IPLinkTest extends BaseTestCase {
    private String processNRPCode;
    private String ipLinkName = "IPLink_Selenium";
    private String newIpLinkName = "IPLink_Selenium2";
    private String ethernetLinkName = "EthernetLink_Selenium";
    private NetworkViewPage networkViewPage;
    private Wizard wizard;

    public Wizard getWizard() {
        if (wizard == null) {
            wizard = Wizard.createWizard(driver, webDriverWait);
        }
        return wizard;
    }

    @Test(priority = 1)
    @Description("Create NRP process")
    public void createProcessNRP() {
        openView("Process Instances", "Views", "Business Process Management");
        ProcessWizardPage processWizardPage = new ProcessWizardPage(driver);
        processNRPCode = processWizardPage.createSimpleNRP();
        checkPopup(processNRPCode);
    }

    @Test(priority = 2)
    @Description("Start HLP task")
    public void startHLP() {
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        tasksPage.startTask(processNRPCode, "High Level Planning");
        checkPopup("The task properly assigned.");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 3)
    @Description("Create IP Link")
    public void createIPLink() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        openView("IP LINK Test", "Favourites");
        networkViewPage = new NetworkViewPage(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.useContextAction("add_to_view_group", "Network Element");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.queryElementAndAddItToView("name", TEXT_FIELD, "SeleniumIPLinkTests");

        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.useContextAction("add_to_view_group", "Network Element");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.queryElementAndAddItToView("name", TEXT_FIELD, "SeleniumIPLinkTests2");

        networkViewPage.expandDockedPanel("left");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.selectObjectInViewContent("Name", "SeleniumIPLinkTests");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);


        networkViewPage.useContextAction("CREATE", "Create Connection");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        getWizard().setComponentValue("trailTypeCombobox", "IP Link", Input.ComponentType.COMBOBOX);
        getWizard().clickAccept();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        wizard = null;
        getWizard().setComponentValue("name-uid", ipLinkName, Input.ComponentType.TEXT_FIELD);
        getWizard().setComponentValue("capacity-unit-uid", "Mbps", Input.ComponentType.COMBOBOX);
        getWizard().setComponentValue("capacity-value-uid", "100", Input.ComponentType.TEXT_FIELD);
        getWizard().clickButtonByLabel("Proceed");
        wizard = null;
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }


    @Test(priority = 4)
    @Description("Edit IP Link termination")
    public void editIPLinkTermination() {
        networkViewPage.expandDockedPanel("left");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.selectObjectInViewContent("Name", "IPLink_Selenium");

        networkViewPage.expandDockedPanel("bottom");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.selectObjectInDetailsTab("Type", "Start");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.modifyTermination();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.setTrailPort("GE 0");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.clickProceed();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.selectObjectInDetailsTab("Type", "End");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.modifyTermination();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.setTrailPort("GE 0");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.clickProceed();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
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

        networkViewPage.useContextAction("CREATE", "Create Connection");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        getWizard().setComponentValue("trailTypeCombobox", "Ethernet Link", Input.ComponentType.COMBOBOX);
        getWizard().clickAccept();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        wizard = null;
        getWizard().setComponentValue("oss.transport.trail.type.Ethernet Link.Name", ethernetLinkName, Input.ComponentType.TEXT_FIELD);
        getWizard().setComponentValue("oss.transport.trail.type.Ethernet Link.Speed-input", "100M", Input.ComponentType.COMBOBOX);
        getWizard().clickButtonByLabel("Proceed");
        wizard = null;
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

    }

    @Test(priority = 6)
    @Description("Add routing to IP Link")
    public void addRouting() {
        networkViewPage.expandDockedPanel("left");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.selectObjectInViewContent("Name", "IPLink_Selenium");
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
        Assert.assertTrue(networkViewPage.isObjectInRouting1stLevel(ethernetLinkName));
    }

    @Test(priority = 7)
    @Description("Edit IP Link attributes")
    public void editIPLink() {
        networkViewPage.useContextAction("EDIT", "Attributes and terminations");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        getWizard().setComponentValue("name-uid", newIpLinkName, Input.ComponentType.TEXT_FIELD);
        getWizard().setComponentValue("capacity-value-uid", "110", Input.ComponentType.TEXT_FIELD);
        getWizard().clickButtonByLabel("Proceed");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        wizard = null;
    }

    @Test(priority = 8)
    @Description("Checks if IP Link was created correctly")
    public void checkIPLink() {
        HomePage homePage = new HomePage(driver);
        homePage.goToHomePage(driver, BASIC_URL);
        homePage.setNewObjectType("Trail");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        NewInventoryViewPage newInventoryViewPage = new NewInventoryViewPage(driver);
        newInventoryViewPage.searchObject(newIpLinkName);

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
        newInventoryViewPage.searchObject(newIpLinkName);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        newInventoryViewPage.selectFirstRow();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        newInventoryViewPage.callAction("EDIT", "DeleteTrailWizardActionId");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        getWizard().clickButtonByLabel("Next");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        getWizard().clickButtonByLabel("Delete");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        checkPopupStatus();
    }

    private void openView(String actionLabel, String... path) {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        homePage = new HomePage(driver);
        homePage.goToHomePage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        homePage.chooseFromLeftSideMenu(actionLabel, path);
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
}

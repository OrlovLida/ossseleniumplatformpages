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
import com.oss.framework.mainheader.PerspectiveChooser;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.framework.widgets.tablewidget.OldTable;
import com.oss.framework.widgets.tablewidget.TableInterface;
import com.oss.pages.bpm.IntegrationProcessWizardPage;
import com.oss.pages.bpm.ProcessInstancesPage;
import com.oss.pages.bpm.ProcessWizardPage;
import com.oss.pages.bpm.TasksPage;
import com.oss.pages.platform.HomePage;
import com.oss.pages.platform.NewInventoryViewPage;
import com.oss.pages.transport.IRBInterfaceWizardPage;
import com.oss.utils.TestListener;

import io.qameta.allure.Description;

@Listeners({ TestListener.class })
public class IRBInterfaceTest extends BaseTestCase {

    private NewInventoryViewPage newInventoryViewPage;
    //    private Wizard wizard;
    private String processNRPCode;
    private String processIPCode;
    private String perspectiveContext;
    private String processIPName = "S.2-" + (int) (Math.random() * 1001);

    private static final String IRB_INTERFACE_ID = "117";
    private static final String IRB_INTERFACE_DEVICE_NAME = "IRBInterfaceSeleniumTest";

//    public Wizard getWizard() {
//        if (wizard == null) {
//            wizard = Wizard.createWizard(driver, webDriverWait);
//        }
//        return wizard;
//    }

    @BeforeClass
    public void openNetworkDiscoveryControlView() {
//        ProcessInstancesPage processInstancesPage = ProcessInstancesPage.goToProcessInstancesPage(driver, BASIC_URL);
        homePage.chooseFromLeftSideMenu("IRB Interface", "Wizards", "Transport");
        newInventoryViewPage = new NewInventoryViewPage(driver);
        waitForPageToLoad();
    }
//
//    @Test(priority = 1)
//    @Description("Create NRP Process")
//    public void createProcessNRP() {
//        ProcessWizardPage processWizardPage = new ProcessWizardPage(driver);
//        processNRPCode = processWizardPage.createSimpleNRP();
//        checkMessageSize(1);
//        checkMessageType();
//        checkMessageContainsText(processNRPCode);
//    }
//
//    @Test(priority = 2)
//    @Description("Start High Level Planning Task")
//    public void startHLPTask() {
//        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
//        tasksPage.startTask(processNRPCode, "High Level Planning");
//        checkTaskAssignment();
//    }

    @Test(priority = 3)
    @Description("Create new IRB Interface")
    public void createNewIRBInterface() {
//        homePage.chooseFromLeftSideMenu("IRB Interface", "Wizards", "Transport");
        IRBInterfaceWizardPage irbInterfaceWizardPage = new IRBInterfaceWizardPage(driver);
        waitForPageToLoad();
        irbInterfaceWizardPage.createIRBInterface(IRB_INTERFACE_DEVICE_NAME, IRB_INTERFACE_ID);
    }

    @Test(priority = 4)
    @Description("Checks if IRB Interface is visible")
    public void checkIRBInterface() {
        homePage.goToHomePage(driver,BASIC_URL);
        homePage.setNewObjectType("IRB Interface");
        waitForPageToLoad();

//        newInventoryViewPage.setFilterPanel("shortIdentifier", IRB_INTERFACE_DEVICE_NAME);
        newInventoryViewPage.setFilterPanel("name", IRB_INTERFACE_ID);
        waitForPageToLoad();

        Assert.assertFalse(newInventoryViewPage.checkIfTableIsEmpty());
    }

//    @Test(priority = 5)
//    @Description("Assign IP Host Address")
//    public void assignIPHostAddress() {
//        newInventoryViewPage.selectFirstRow();
//        DelayUtils.waitForPageToLoad(driver, webDriverWait);
//        newInventoryViewPage.callAction("CREATE", "AssignIPv4Host_TP");
//
//        IRBInterfaceWizardPage irbInterfaceWizardPage = new IRBInterfaceWizardPage(driver);
//        irbInterfaceWizardPage.assignIPtoIRBInterface("126003", IRB_INTERFACE_DEVICE_NAME);
//        DelayUtils.waitForPageToLoad(driver, webDriverWait);
//    }

    @Test(priority = 6)
    @Description("Edit IRB Interface")
    public void editIRBInterface() {
//        NewInventoryViewPage newInventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, "IRBInterface_TP");
//        DelayUtils.waitForPageToLoad(driver, webDriverWait);
//        PerspectiveChooser perspectiveChooser = PerspectiveChooser.create(driver, webDriverWait);
//        perspectiveChooser.setPlanPerspective(processNRPCode);
//        DelayUtils.waitForPageToLoad(driver, webDriverWait);

//        newInventoryViewPage.openFilterPanel().setValue(Input.ComponentType.TEXT_FIELD,"identifier", "IRBInterfaceSeleniumTest").applyFilter();
//        newInventoryViewPage.setFilterPanel("identifier", IRB_INTERFACE_DEVICE_NAME);
//        newInventoryViewPage.setFilterPanel("name", IRB_INTERFACE_ID);
        newInventoryViewPage.selectFirstRow();
        waitForPageToLoad();
        newInventoryViewPage.callAction("EDIT", "EditIRBInterfaceContextAction");

        IRBInterfaceWizardPage irbInterfaceWizardPage = new IRBInterfaceWizardPage(driver);
        irbInterfaceWizardPage.editIRBInterface("1000");

        //TODO refresh i sprawdzenie czy pojawiła się wartość
    }
//
//    @Test(priority = 7)
//    @Description("Complete High Level Planning Task")
//    public void completeHLPTask() {
//        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
//        tasksPage.completeTask(processNRPCode, "High Level Planning");
//        checkTaskCompleted();
//    }
//
//    @Test(priority = 8)
//    @Description("Start Low Level Planning Task")
//    public void startLLPTask() {
//        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
//        tasksPage.startTask(processNRPCode, "Low Level Planning");
//        DelayUtils.waitForPageToLoad(driver, webDriverWait);
//        checkTaskAssignment();
//        String currentUrl = driver.getCurrentUrl();
//        String[] split = currentUrl.split(Pattern.quote("?"));
//        perspectiveContext = split[1];
//        Assert.assertTrue(perspectiveContext.contains("PLAN"));
//    }
//
//    @Test(priority = 9)
//    @Description("Complete Low Level Planning Task")
//    public void completeLLPTask() {
//        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
//        tasksPage.completeTask(processNRPCode, "Low Level Planning");
//        checkTaskCompleted();
//    }
//
//    @Test(priority = 10)
//    @Description("Start Ready for Integration Task")
//    public void startRFITask() {
//        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
//        tasksPage.startTask(processNRPCode, "Ready for Integration");
//        checkTaskAssignment();
//    }
//
//    @Test(priority = 11)
//    @Description("Setup Integration")
//    public void setupIntegration() {
//        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
//        tasksPage.setupIntegration(processNRPCode);
//        IntegrationProcessWizardPage integrationWizard = new IntegrationProcessWizardPage(driver);
//        integrationWizard.defineIntegrationProcess(processIPName, LocalDate.now().plusDays(0).toString(), 1);
//        integrationWizard.clickNext();
//        integrationWizard.dragAndDrop(deviceName, processNRPCode, processIPName);
//        integrationWizard.clickAccept();
//    }
//
//    @Test(priority = 12)
//    @Description("Get IP Code")
//    public void getIPCode() {
//        DelayUtils.sleep(3000);
//        TableInterface ipTable = OldTable.createByComponentId(driver, webDriverWait, "ip_involved_nrp_group1");
//        int rowNumber = ipTable.getRowNumber(processIPName, "Name");
//        processIPCode = ipTable.getValueCell(rowNumber, "Code");
//        System.out.println(processIPCode);
//    }
//
//    @Test(priority = 13)
//    @Description("Complete Ready for Integration Task")
//    public void completeRFITask() {
//        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
//        tasksPage.completeTask(processNRPCode, "Ready for Integration");
//        checkTaskCompleted();
//    }
//
//    @Test(priority = 14)
//    @Description("Start Scope definition Task")
//    public void startSDTaskIP() {
//        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
//        tasksPage.startTask(processIPCode, "Scope definition");
//        checkTaskAssignment();
//    }
//
//    @Test(priority = 15)
//    @Description("Complete Scope definition Task")
//    public void completeSDTaskIP() {
//        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
//        tasksPage.completeTask(processIPCode, "Scope definition");
//        checkTaskCompleted();
//    }
//
//    @Test(priority = 16)
//    @Description("Start Implementation Task")
//    public void startImplementationTaskIP() {
//        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
//        tasksPage.startTask(processIPCode, "Implementation");
//        checkTaskAssignment();
//    }
//
//    @Test(priority = 17)
//    @Description("Complete Implementation Task")
//    public void completeImplementationTaskIP() {
//        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
//        tasksPage.completeTask(processIPCode, "Implementation");
//        checkTaskCompleted();
//    }
//
//    @Test(priority = 18)
//    @Description("Start Acceptance Task")
//    public void startAcceptanceTaskIP() {
//        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
//        tasksPage.startTask(processIPCode, "Acceptance");
//        checkTaskAssignment();
//    }
//
//    @Test(priority = 19)
//    @Description("Complete Acceptance Task")
//    public void completeAcceptanceTaskIP() {
//        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
//        tasksPage.completeTask(processIPCode, "Acceptance");
//        checkTaskCompleted();
//    }

    @Test(priority = 20)
    @Description("Delete IRB Interface")
    public void deleteIRBInterface() {
//        NewInventoryViewPage newInventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, "IRBInterface_TP");
//        DelayUtils.waitForPageToLoad(driver, webDriverWait);
//
////        IRBInterfaceWizardPage irbInterfaceWizardPage = new IRBInterfaceWizardPage(driver);
////        irbInterfaceWizardPage.deleteIRBInterface("identifier", "IRBInterfaceSeleniumTest");
//        DelayUtils.waitForPageToLoad(driver, webDriverWait);
//        newInventoryViewPage.setFilterPanel("identifier", IRB_INTERFACE_DEVICE_NAME);
//        newInventoryViewPage.setFilterPanel("name", IRB_INTERFACE_ID);
        newInventoryViewPage.selectFirstRow();
        newInventoryViewPage.callAction("EDIT", "DeleteIRBInterfaceContextAction");
        waitForPageToLoad();
        Wizard.createWizard(driver, webDriverWait).clickButtonByLabel("OK");
        waitForPageToLoad();
        checkMessageType();
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
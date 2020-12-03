package com.oss;

import com.oss.framework.alerts.SystemMessageContainer;
import com.oss.framework.alerts.SystemMessageInterface;
import com.oss.framework.mainheader.PerspectiveChooser;
import com.oss.framework.sidemenu.SideMenu;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.tablewidget.OldTable;
import com.oss.pages.bpm.PlanViewWizardPage;
import com.oss.pages.bpm.ProcessInstancesPage;
import com.oss.pages.bpm.ProcessWizardPage;
import com.oss.pages.bpm.TasksPage;
import com.oss.pages.platform.HomePage;
import com.oss.pages.platform.OldInventoryViewPage;
import com.oss.pages.platform.PerspectiveChooserPage;
import com.oss.pages.radio.CellSiteConfigurationPage;
import com.oss.pages.radio.EditCell4GWizardPage;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.Assertion;

import java.util.List;
import java.util.regex.Pattern;

public class BPMandCellconfigurationTest extends BaseTestCase {
    private String processNRPCode;
    private String LOCATION_NAME = "XYZ_SeleniumTests"; //czy mamy jakies wymagania co do lokacji
    private String E_NODE_B_NAME = "XYZ_enodeB";
    public String perspectiveContext;
    private TasksPage tasksPage;
    ProcessInstancesPage processInstancesPage;

    @BeforeClass
    public void goToBPM(){
        ProcessInstancesPage processInstancesPage = ProcessInstancesPage.goToProcessInstancesPage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 1)
    public void createProcessNRP(){
        ProcessWizardPage processWizardPage = new ProcessWizardPage(driver);
        processNRPCode = processWizardPage.createSimpleNRP();


        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assertions.assertThat(messages).hasSize(1);
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assertions.assertThat(messages.get(0).getText()).contains(processNRPCode);
    }

    @Test(priority = 2)
    public void startHLP() {
        tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        tasksPage.startTask(processNRPCode, "High Level Planning");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getText())
                .isEqualTo("The task properly assigned.");
    }

    @Test(priority = 3, enabled = false)
    public void findLocation(){
        HomePage homePage = new HomePage(driver);
        homePage.goToHomePage(driver, BASIC_URL);
        homePage.setAndSelectObjectType("Site");

        OldInventoryViewPage oldInventoryViewPage = new OldInventoryViewPage(driver);
        oldInventoryViewPage.filterObject("Name", LOCATION_NAME, "Site");
        oldInventoryViewPage.expandShowOnAndChooseView( "Cell Site Configuration");
    }

    @Test(priority = 4)
    public void finishHLP(){
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.completeTask(processNRPCode, "High Level Planning");
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getText())
                .contains("Task properly completed.");
    }

    @Test(priority = 5)
    public void startLLP() {
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.startTask(processNRPCode, "Low Level Planning");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getText())
                .isEqualTo("The task properly assigned.");
        String currentUrl = driver.getCurrentUrl();
        String[] split = currentUrl.split(Pattern.quote("?"));
        perspectiveContext = split[1];
        Assertions.assertThat(perspectiveContext).contains("PLAN");
    }

    @Test(priority = 6)
    public void validateProjectPlan(){
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.findTask(processNRPCode, "Low Level Planning");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        tasksPage.clickPlanViewButton();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        PlanViewWizardPage planViewWizardPage = new PlanViewWizardPage(driver);
        planViewWizardPage.selectTab("Validation Results");
        Assertions.assertThat(!planViewWizardPage.validationErrorsPresent());
    }

    @Test(priority = 7, enabled = false)
    public void lowLevelLogicalDesign(){
        HomePage homePage = new HomePage(driver);
        homePage.goToHomePage(driver, BASIC_URL);
        homePage.setAndSelectObjectType("Site");

        OldInventoryViewPage oldInventoryViewPage = new OldInventoryViewPage(driver);
        oldInventoryViewPage.filterObject("Name", LOCATION_NAME, "Site");
        oldInventoryViewPage.expandShowOnAndChooseView( "Cell Site Configuration");

        CellSiteConfigurationPage cellSiteConfigurationPage = new CellSiteConfigurationPage(driver);
        cellSiteConfigurationPage.expandTreeToBaseStation("Site", LOCATION_NAME, E_NODE_B_NAME);


        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cellSiteConfigurationPage.selectRowByAttributeValueWithLabel("Name", "10");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cellSiteConfigurationPage.selectRowByAttributeValueWithLabel("Name", "20");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cellSiteConfigurationPage.selectRowByAttributeValueWithLabel("Name", "30");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        cellSiteConfigurationPage.clickEditIcon();
        EditCell4GWizardPage editCell4GWizardPage = new EditCell4GWizardPage(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        editCell4GWizardPage.setPCIBulk("2");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        editCell4GWizardPage.setRSIBulk("2");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        editCell4GWizardPage.setReferencePowerBulk("2");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        editCell4GWizardPage.setTAC( 1, "2");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        editCell4GWizardPage.setTAC( 2, "2");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        editCell4GWizardPage.setTAC( 3, "2");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        editCell4GWizardPage.setPaOutputBulk("0");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        editCell4GWizardPage.accept();
    }

    @Test(priority = 8)
    public void finishLowLevelPlanningTask(){ //object names cannot be the same, otherwise validation will fail
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        PerspectiveChooser perspectiveChooser = PerspectiveChooser.create(driver, webDriverWait);
        perspectiveChooser.setCurrentTask();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        tasksPage.completeTask(processNRPCode, "Low Level Planning");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getText())
                .contains("Task properly completed.");

        tasksPage.startTask(processNRPCode, "Ready for Integration");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        SystemMessageInterface systemMessage1 = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages1 = systemMessage.getMessages();
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getText())
                .isEqualTo("The task properly assigned.");

        tasksPage.completeTask(processNRPCode, "Ready for Integration");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        SystemMessageInterface systemMessage2 = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages2 = systemMessage.getMessages();
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getText())
                .contains("Task properly completed.");

    }

    @Test(priority = 9)
    public void integrationProject(){
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        ProcessInstancesPage processInstancesPage = ProcessInstancesPage.goToProcessInstancesPage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        processInstancesPage.findProcess(processNRPCode);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
      //  processInstancesPage.selectTask("60615086");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        tasksPage.startTask(processNRPCode, "Integrate");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        tasksPage.completeTask(processNRPCode, "Integrate");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        tasksPage.startTask(processNRPCode, "Implement");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        tasksPage.completeTask(processNRPCode, "Implement");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        tasksPage.startTask(processNRPCode, "Accept");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        tasksPage.completeTask(processNRPCode, "Accept");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        tasksPage.startTask(processNRPCode, "Acceptance");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        tasksPage.completeTask(processNRPCode, "Acceptance");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 10)
    public void completeNRPprocess(){
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        ProcessInstancesPage processInstancesPage = ProcessInstancesPage.goToProcessInstancesPage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        processInstancesPage.findProcess(processNRPCode);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
       // processInstancesPage.selectTask("60615086");//id acceptance
        //?
    }
}

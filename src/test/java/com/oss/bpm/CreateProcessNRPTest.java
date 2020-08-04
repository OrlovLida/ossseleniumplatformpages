/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2020 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.bpm;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Pattern;

import org.assertj.core.api.Assertions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.alerts.SystemMessageContainer;
import com.oss.framework.alerts.SystemMessageInterface;
import com.oss.framework.mainheader.PerspectiveChooser;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.tablewidget.OldTable;
import com.oss.framework.widgets.tablewidget.TableInterface;
import com.oss.pages.BasePage;
import com.oss.pages.bpm.IntegrationProcessWizardPage;
import com.oss.pages.bpm.ProcessInstancesPage;
import com.oss.pages.bpm.ProcessWizardPage;
import com.oss.pages.bpm.TasksPage;
import com.oss.pages.physical.DeviceWizardPage;

/**
 * @author Gabriela Kasza
 */
public class CreateProcessNRPTest extends BaseTestCase {

    private String processNRPName;
    private String processIPName1 = "S.1-" + (int) (Math.random() * 1001);
    private String processIPName2 = "S.2-" + (int) (Math.random() * 1001);
    private String processNRPCode;
    private ProcessInstancesPage processInstancesPage;
    private String processIPCode1;
    private String processIPCode2;
    public String perspectiveContext;
    public String deviceName1 = "Device-Selenium-" + (int) (Math.random() * 1001);

    @BeforeClass
    public void openProcessInstancesPage() {
        processInstancesPage = ProcessInstancesPage.goToProcessInstancesPage(driver, BASIC_URL);
        BasePage basePage = new BasePage(driver);
        basePage.waitForPageToLoad();

    }

    @Test(priority = 1)
    public void createProcessNRP() {
        ProcessWizardPage processWizardPage = new ProcessWizardPage(driver);
        processNRPCode = processWizardPage.createSimpleNRP();
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assertions.assertThat(messages).hasSize(1);
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assertions.assertThat(messages.get(0).getText()).contains(processNRPCode);
        System.out.println("Test");

    }

    @Test(priority = 2)
    public void startHLPTask() {
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, BASIC_URL);
        tasksPage.startTask(processNRPCode, "High Level Planning");

    }

    @Test(priority = 3)
    public void completeHLPTask() {
        DelayUtils.sleep(2000);
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, BASIC_URL);
        tasksPage.completeTask(processNRPCode, "High Level Planning");

    }

    @Test(priority = 4)
    public void startLLPTask() {
        DelayUtils.sleep(3000);
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, BASIC_URL);
        tasksPage.startTask(processNRPCode, "Low Level Planning");
        DelayUtils.sleep(2000);
        String currentUrl = driver.getCurrentUrl();
        String[] split = currentUrl.split(Pattern.quote("?"));
        perspectiveContext = split[1];
        Assertions.assertThat(perspectiveContext).contains("PLAN");
    }

    @Test(priority = 5)
    public void createPhysicalDevice() {
        // DeviceWizardPage deviceWizardPage = DeviceWizardPage.goToDeviceWizardPagePlan(driver,BASIC_URL,"project_id=148835809&perspective=PLAN");
        DeviceWizardPage deviceWizardPage = DeviceWizardPage.goToDeviceWizardPageLive(driver, BASIC_URL);
        PerspectiveChooser perspectiveChooser = PerspectiveChooser.create(driver, webDriverWait);
        perspectiveChooser.setPlanPerspective(processNRPCode);

        deviceWizardPage.setModel("Generic IP Device");
        BasePage basePage = new BasePage(driver);
        basePage.waitForPageToLoad();
        DelayUtils.sleep(2000);
        deviceWizardPage.setName(deviceName1);
        deviceWizardPage.setPreciseLocation(" ");
        deviceWizardPage.setNetworkDomain("Other");
        if (driver.getPageSource().contains("Hostname")){
            deviceWizardPage.setHostname(deviceName1);
        }
        deviceWizardPage.create();
            SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assertions.assertThat(messages).hasSize(1);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getMessageType())
                .isEqualTo(SystemMessageContainer.MessageType.SUCCESS);

    }

    @Test(priority = 6)
    public void assignFile() {
        try {
            TasksPage tasksPage = TasksPage.goToTasksPage(driver, BASIC_URL);
            URL resource = CreateProcessNRPTest.class.getClassLoader().getResource("bpm/SeleniumTest.txt");
            String absolutePatch = Paths.get(resource.toURI()).toFile().getAbsolutePath();
            tasksPage.addFile(processNRPCode, "Low Level Planning",absolutePatch);
            SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
            Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getMessageType())
                    .isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        }
        catch (URISyntaxException e){
            throw new RuntimeException("Cannot load file",e);
        }

    }

    @Test(priority = 7)
    public void completeLLPTask() {
        DelayUtils.sleep(3000);
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, BASIC_URL);
        tasksPage.completeTask(processNRPCode, "Low Level Planning");
    }

    @Test(priority = 8)
    public void startRIRTask() {
        DelayUtils.sleep(3000);
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, BASIC_URL);
        tasksPage.startTask(processNRPCode, "Ready for Integration");
    }

    @Test(priority = 9)
    public void setupIntegration() {
        DelayUtils.sleep(3000);
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, BASIC_URL);
        tasksPage.setupIntegration(processNRPCode);
        IntegrationProcessWizardPage integrationWizard = new IntegrationProcessWizardPage(driver);
        integrationWizard.defineIntegrationProcess(processIPName1, "2020-07-01", 1);
        integrationWizard.defineIntegrationProcess(processIPName2, "2020-07-02", 2);
        integrationWizard.clickNext();
        //integrationWizard.dragAndDrop("DOW193-Router-1", processIPName1);
        integrationWizard.clickAccept();

    }

    @Test(priority = 10)
    public void getIPCode() {
        DelayUtils.sleep(3000);
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, BASIC_URL);
        tasksPage.findTask(processNRPCode, "Ready For Integration");
        DelayUtils.sleep(3000);
        TableInterface ipTable = OldTable.createByComponentId(driver, webDriverWait, "ip_involved_nrp_group1");
        int rowNumber = ipTable.getRowNumber(processIPName1, "Name");
        processIPCode1 = ipTable.getValueCell(rowNumber, "Code");
        int rowNumber2 = ipTable.getRowNumber(processIPName2, "Name");
        processIPCode2 = ipTable.getValueCell(rowNumber2, "Code");
        System.out.println(processIPCode1 + processIPCode2);

    }
    @AfterClass()
    public void switchToLivePerspective(){
        PerspectiveChooser perspectiveChooser = PerspectiveChooser.create(driver, webDriverWait);
        perspectiveChooser.setLivePerspective();
    }

}

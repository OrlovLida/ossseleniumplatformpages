/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2020 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.bpm;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Pattern;

import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.configuration.Configuration;
import com.oss.framework.alerts.SystemMessageContainer;
import com.oss.framework.components.systemMessage.SystemMessage;
import com.oss.framework.listwidget.EditableList;
import com.oss.framework.mainheader.PerspectiveChooser;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.tablewidget.OldTable;
import com.oss.framework.widgets.tablewidget.TableInterface;
import com.oss.framework.widgets.tabswidget.OldTabs;
import com.oss.framework.widgets.tabswidget.TabsInterface;
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
    private String processIPName1= "S.1-"+(int) (Math.random() * 1001);
    private String processIPName2= "S.2-"+(int) (Math.random() * 1001);
    private String processNRPCode;
    private ProcessInstancesPage processInstancesPage;
    private String processIPCode1;
    private String processIPCode2;
    public String perspectiveContext;
    private String physicalDeviceName;


    @BeforeClass
    public void openProcessInstancesPage(){
//        UserSettings userSettings =UserSettings.create(driver,webDriverWait);
//        userSettings.chooseLanguage("English");
        processInstancesPage = ProcessInstancesPage.goToProcessInstancesPage(driver, BASIC_URL);
       PerspectiveChooser perspectiveChooser = PerspectiveChooser.create(driver, webDriverWait);
 //      perspectiveChooser.setPlanDatePerspective("2020-05-12");
//        perspectiveChooser.setPlanPerspective("NRP-103");


    }
    @Test(priority=1)
    public void createProcessNRP(){
        ProcessWizardPage processWizardPage = new ProcessWizardPage(driver);
        processNRPCode = processWizardPage.createSimpleNRP();
        SystemMessageContainer systemMessageContainer = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessageContainer.getMessages();
        Assertions.assertThat(messages).hasSize(1);
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assertions.assertThat(messages.get(0).getText()).contains(processNRPCode);

    }
    @Test(priority=2)
    public  void startHLPTask(){
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, BASIC_URL);
        tasksPage.startTask(processNRPCode,"High Level Planning");

    }
    @Test(priority=3)
    public void completeHLPTask(){
        DelayUtils.sleep(2000);
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, BASIC_URL);
        tasksPage.completeTask(processNRPCode,"High Level Planning");

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
    public void createPhysicalDevice(){
        //DeviceWizardPage deviceWizardPage = DeviceWizardPage.goToDeviceWizardPagePlan(driver,BASIC_URL,"project_id=148835809&perspective=PLAN");
        DeviceWizardPage deviceWizardPage = DeviceWizardPage.goToDeviceWizardPageLive(driver, BASIC_URL);
        PerspectiveChooser perspectiveChooser = PerspectiveChooser.create(driver, webDriverWait);
        perspectiveChooser.setPlanPerspective(processNRPCode);
        physicalDeviceName = deviceWizardPage.createGenericIPDevice();
        SystemMessageContainer systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assertions.assertThat(messages).hasSize(1);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(()->  new RuntimeException("The list is empty")).getMessageType())
                .isEqualTo(SystemMessageContainer.MessageType.SUCCESS);

    }
    @Test (priority = 6)
    public void assignFile(){
        try {
            TasksPage tasksPage = TasksPage.goToTasksPage(driver,BASIC_URL);
            URL a =CreateProcessNRPTest.class.getClassLoader().getResource("SeleniumTest.txt");
            String absolutePath = Paths.get(a.toURI()).toFile().getAbsolutePath();
            tasksPage.addFile(processNRPCode,"Low Level Planning", absolutePath);
            Assertions.assertThat(SystemMessage.create(driver,webDriverWait).getMessage()).contains("Attachment has been added");

        } catch (URISyntaxException e) {
            throw new RuntimeException("Cant load file", e);
        }
//        TasksPage tasksPage = TasksPage.goToTasksPage(driver,BASIC_URL);
//        tasksPage.findTask("NRP-122","Low Level Planning");
//        tasksPage.selectTab("Attachments");
//        List<String> values = EditableList.create(driver, webDriverWait).getValues();



    }
    @Test (priority =  7)
    public void completeLLPTask(){
        DelayUtils.sleep(3000);
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, BASIC_URL);
        tasksPage.completeTask(processNRPCode,"Low Level Planning");
    }

    @Test(priority = 8)
    public void startRIRTask(){
        DelayUtils.sleep(3000);
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, BASIC_URL);
        tasksPage.startTask(processNRPCode, "Ready for Integration");
    }
    @Test(priority = 9)
    public void setupIntegration(){
        TasksPage tasksPage = TasksPage.goToTasksPage(driver,BASIC_URL);
        tasksPage.setupIntegration(processNRPCode);
        IntegrationProcessWizardPage integrationWizard= new IntegrationProcessWizardPage(driver);
        integrationWizard.defineIntegrationProcess(processIPName1,"2020-07-01",1);
        integrationWizard.defineIntegrationProcess(processIPName2,"2020-07-02",2);
        integrationWizard.clickNext();
        //integrationWizard.dragAndDrop(physicalDeviceName,processIPName1);


    }
//    @Test(priority = 10)
//    public void getIPCode(){
//        TasksPage tasksPage = TasksPage.goToTasksPage(driver,BASIC_URL);
//        tasksPage.findTask(processNRPCode,"Ready For Integration");
//        TableInterface ipTable = OldTable.createByComponentId(driver, webDriverWait, "ip_involved_nrp_group1");
//        int rowNumber = ipTable.getRowNumber(processIPName1, "Name");
//        processIPCode1 = ipTable.getValueCell(rowNumber, "Code");
//        int rowNumber2 = ipTable.getRowNumber(processIPName2, "Name");
//        processIPCode2 = ipTable.getValueCell(rowNumber2, "Code");
//        System.out.println(processIPCode1 + processIPCode2);
//
//    }


}

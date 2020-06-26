/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2020 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.bpm;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Pattern;

import org.apache.http.util.Asserts;
import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.common.base.Splitter;
import com.oss.BaseTestCase;
import com.oss.framework.alerts.SystemMessageContainer;
import com.oss.framework.components.Input;
import com.oss.framework.mainheader.MainHeader;
import com.oss.framework.mainheader.UserSettings;
import com.oss.framework.prompts.ConfirmationBox;
import com.oss.framework.prompts.ConfirmationBoxInterface;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.bpm.ProcessInstancesPage;
import com.oss.pages.bpm.ProcessWizardPage;
import com.oss.pages.bpm.TasksPage;
import com.oss.pages.physical.DeviceWizardPage;
import com.oss.pages.physical.LocationWizardPage;


/**
 * @author Gabriela Kasza
 */
public class CreateProcessNRPTest extends BaseTestCase {

    private String processNRPName;
    private String processNRPCode;
    private ProcessInstancesPage processInstancesPage;
    public String perspectiveContext;


    @BeforeClass
    public void openProcessInstancesPage(){
         processInstancesPage = ProcessInstancesPage.goToProcessInstancesPage(driver, BASIC_URL);
//        UserSettings userSettings =UserSettings.create(driver,webDriverWait);
//        userSettings.chooseLanguage("English");
//        System.out.println("Test");


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
        DeviceWizardPage deviceWizardPage = DeviceWizardPage.goToDeviceWizardPagePlan(driver,BASIC_URL,"project_id=148835809&perspective=PLAN");
        String deviceName = deviceWizardPage.createGenericIPDevice();
        SystemMessageContainer systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assertions.assertThat(messages).hasSize(1);
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);


        System.out.println("test");

    }
    @Test (priority = 6)
    public void assignFile(){



    }

}

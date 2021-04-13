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
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.alerts.SystemMessageContainer;
import com.oss.framework.alerts.SystemMessageInterface;
import com.oss.framework.mainheader.PerspectiveChooser;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.bpm.IntegrationProcessWizardPage;
import com.oss.pages.bpm.ProcessInstancesPage;
import com.oss.pages.bpm.ProcessWizardPage;
import com.oss.pages.bpm.TasksPage;
import com.oss.pages.physical.DeviceWizardPage;
import com.oss.utils.TestListener;

/**
 * @author Gabriela Kasza
 */
@Listeners({ TestListener.class })
public class CreateProcessNRPTest extends BaseTestCase {

    private String processNRPName;
    private String processIPName1 = "S.1-" + (int) (Math.random() * 1001);
    private String processIPName2 = "S.2-" + (int) (Math.random() * 1001);
    private String processNRPCode;
    private String processIPCode1;
    private String processIPCode2;
    public String perspectiveContext;
    public String deviceName1 = "Device-Selenium-" + (int) (Math.random() * 1001);
    public String deviceName2 = "Device-Selenium-" + (int) (Math.random() * 1001);

    @BeforeClass
    public void openProcessInstancesPage() {
        ProcessInstancesPage processInstancesPage = ProcessInstancesPage.goToProcessInstancesPage(driver, BASIC_URL);
        DelayUtils.sleep(3000);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        processInstancesPage.changeUser("bpm_webselenium", "bpmweb");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

    }

    @Test(priority = 1)
    public void createProcessNRP() {
        // given
        ProcessWizardPage processWizardPage = new ProcessWizardPage(driver);

        // when
        processNRPCode = processWizardPage.createSimpleNRP();
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();

        // then
        Assertions.assertThat(messages).hasSize(1);
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assertions.assertThat(messages.get(0).getText()).contains(processNRPCode);
    }

    @Test(priority = 2)
    public void startHLPTask() {
        // given
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);

        // when
        tasksPage.startTask(processNRPCode, TasksPage.HIGH_LEVEL_PLANNING_TASK);
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();

        // then
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getText())
                .isEqualTo("The task properly assigned.");
    }

    @Test(priority = 3)
    public void createFirstPhysicalDevice() {

        // given
        DeviceWizardPage deviceWizardPage = DeviceWizardPage.goToDeviceWizardPageLive(driver, BASIC_URL);
        PerspectiveChooser perspectiveChooser = PerspectiveChooser.create(driver, webDriverWait);

        // when
        perspectiveChooser.setPlanPerspective(processNRPCode);
        deviceWizardPage.setEquipmentType("Connection Panel");
        deviceWizardPage.setModel("Generic");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.setName(deviceName1);
        deviceWizardPage.setNetworkDomain(" ");
        if (driver.getPageSource().contains("Hostname")) {
            deviceWizardPage.setHostname(deviceName1);
        }
        deviceWizardPage.next();
        deviceWizardPage.setPreciseLocation(" ");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.accept();
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();

        // then
        Assertions.assertThat(messages).hasSize(1);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getMessageType())
                .isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
    }

    @Test(priority = 4)
    public void completeHLPTask() {
        // given
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);

        // when
        tasksPage.completeTask(processNRPCode, TasksPage.HIGH_LEVEL_PLANNING_TASK);
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();

        // then
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getText())
                .contains("Task properly completed.");
    }

    @Test(priority = 5)
    public void startLLPTask() {
        // given
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);

        // when
        tasksPage.startTask(processNRPCode, TasksPage.LOW_LEVEL_PLANNING_TASK);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();

        // then
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getText())
                .isEqualTo("The task properly assigned.");
        String currentUrl = driver.getCurrentUrl();
        String[] split = currentUrl.split(Pattern.quote("?"));
        perspectiveContext = split[1];
        Assertions.assertThat(perspectiveContext).contains("PLAN");
    }

    @Test(priority = 6)
    public void assignFile() {
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        try {
            URL resource = CreateProcessNRPTest.class.getClassLoader().getResource("bpm/SeleniumTest.txt");
            String absolutePatch = Paths.get(resource.toURI()).toFile().getAbsolutePath();
            tasksPage.addFile(processNRPCode, TasksPage.LOW_LEVEL_PLANNING_TASK, absolutePatch);
            SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
            Assertions
                    .assertThat(
                            systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getMessageType())
                    .isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        } catch (URISyntaxException e) {
            throw new RuntimeException("Cannot load file", e);
        }
        DelayUtils.sleep(2000);
        List<String> files = tasksPage.getListOfAttachments();
        Assertions.assertThat(files.get(0)).contains("SeleniumTest");
        Assertions.assertThat(files.size()).isGreaterThan(0);

    }

    @Test(priority = 7)
    public void createSecondPhysicalDevice() {
        // DeviceWizardPage deviceWizardPage =
        // DeviceWizardPage.goToDeviceWizardPagePlan(driver,BASIC_URL,"project_id=148835809&perspective=PLAN");
        // given
        DeviceWizardPage deviceWizardPage = DeviceWizardPage.goToDeviceWizardPageLive(driver, BASIC_URL);
        PerspectiveChooser perspectiveChooser = PerspectiveChooser.create(driver, webDriverWait);

        // when
        perspectiveChooser.setPlanPerspective(processNRPCode);
        deviceWizardPage.setEquipmentType("Connection Panel");
        deviceWizardPage.setModel("Generic");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.setName(deviceName2);
        deviceWizardPage.setNetworkDomain(" ");
        if (driver.getPageSource().contains("Hostname")) {
            deviceWizardPage.setHostname(deviceName2);
        }
        deviceWizardPage.next();
        deviceWizardPage.setPreciseLocation(" ");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.accept();
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();

        // then
        Assertions.assertThat(messages).hasSize(1);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getMessageType())
                .isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
    }

    @Test(priority = 8)
    public void completeLLPTask() {
        // given
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);

        // when
        tasksPage.completeTask(processNRPCode, TasksPage.LOW_LEVEL_PLANNING_TASK);
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();

        // then
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getText())
                .contains("Task properly completed.");
    }

    @Test(priority = 9)
    public void startRFITask() {
        // given
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);

        // when
        tasksPage.startTask(processNRPCode, TasksPage.READY_FOR_INTEGRATION_TASK);
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();

        // then
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getText())
                .isEqualTo("The task properly assigned.");
    }

    @Test(priority = 10)
    public void setupIntegration() {
        // given
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);

        // when
        tasksPage.setupIntegration(processNRPCode);
        IntegrationProcessWizardPage integrationWizard = new IntegrationProcessWizardPage(driver);
        integrationWizard.defineIntegrationProcess(processIPName1, "2020-07-01", 1);
        integrationWizard.defineIntegrationProcess(processIPName2, "2020-07-02", 2);
        DelayUtils.sleep();
        integrationWizard.clickNext();
        integrationWizard.dragAndDrop(deviceName1, processNRPCode, processIPName1);
        integrationWizard.dragAndDrop(deviceName2, processNRPCode, processIPName2);
        integrationWizard.clickAccept();
    }

    @Test(priority = 11)
    public void getIPCode() {

        // given
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);

        // when
        tasksPage.findTask(processNRPCode, TasksPage.READY_FOR_INTEGRATION_TASK);
        DelayUtils.sleep(3000);
        processIPCode1 = tasksPage.getIPCodeByProcessName(processIPName1);
        processIPCode2 = tasksPage.getIPCodeByProcessName(processIPName2);
        System.out.println(processIPCode1 + processIPCode2);
    }

    @Test(priority = 12)
    public void completeRFITask() {
        // given
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);

        // when
        tasksPage.completeTask(processNRPCode, TasksPage.READY_FOR_INTEGRATION_TASK);
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();

        // then
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getText())
                .contains("Task properly completed.");
    }

    @Test(priority = 13)
    public void startSDTaskIP1() {
        // given
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);

        // when
        tasksPage.startTask(processIPCode1, TasksPage.SCOPE_DEFINITION_TASK);
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();

        // then
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getText())
                .isEqualTo("The task properly assigned.");
    }

    @Test(priority = 14)
    public void completeSDTaskIP1() {
        // given
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);

        // when
        tasksPage.completeTask(processIPCode1, TasksPage.SCOPE_DEFINITION_TASK);
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();

        // then
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getText())
                .contains("Task properly completed.");
    }

    @Test(priority = 15)
    public void startImplementationTaskIP1() {
        // given
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);

        // when
        tasksPage.startTask(processIPCode1, TasksPage.IMPLEMENTATION_TASK);
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();

        // then
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getText())
                .isEqualTo("The task properly assigned.");
    }

    @Test(priority = 16)
    public void completeImplementationTaskIP1() {
        // given
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);

        // when
        tasksPage.completeTask(processIPCode1, TasksPage.IMPLEMENTATION_TASK);
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();

        // then
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getText())
                .contains("Task properly completed.");
    }

    @Test(priority = 17)
    public void startAcceptanceTaskIP1() {
        // given
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);

        // when
        tasksPage.startTask(processIPCode1, TasksPage.ACCEPTANCE_TASK);
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();

        // then
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getText())
                .isEqualTo("The task properly assigned.");
    }

    @Test(priority = 18)
    public void completeAcceptanceTaskIP1() {
        // given
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);

        // when
        tasksPage.completeTask(processIPCode1, TasksPage.ACCEPTANCE_TASK);
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();

        // then
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getText())
                .contains("Task properly completed.");
    }

    @Test(priority = 19)
    public void startSDTaskIP2() {
        // given
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);

        // when
        tasksPage.startTask(processIPCode2, TasksPage.SCOPE_DEFINITION_TASK);
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();

        // then
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getText())
                .isEqualTo("The task properly assigned.");
    }

    @Test(priority = 20)
    public void completeSDTaskIP2() {
        // given
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);

        // when
        tasksPage.completeTask(processIPCode2, TasksPage.SCOPE_DEFINITION_TASK);
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();

        // then
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getText())
                .contains("Task properly completed.");
    }

    @Test(priority = 21)
    public void startImplementationTaskIP2() {
        // given
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.startTask(processIPCode2, TasksPage.IMPLEMENTATION_TASK);

        // when
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();

        // then
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getText())
                .isEqualTo("The task properly assigned.");
    }

    @Test(priority = 22)
    public void completeImplementationTaskIP2() {
        // given
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);

        // when
        tasksPage.completeTask(processIPCode2, TasksPage.IMPLEMENTATION_TASK);
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();

        // then
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getText())
                .contains("Task properly completed.");
    }

    @Test(priority = 23)
    public void startAcceptanceTaskIP2() {
        // given
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);

        // when
        tasksPage.startTask(processIPCode2, TasksPage.ACCEPTANCE_TASK);
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();

        // then
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getText())
                .isEqualTo("The task properly assigned.");
    }

    @Test(priority = 24)
    public void completeAcceptanceTaskIP2() {
        // given
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);

        // when
        tasksPage.completeTask(processIPCode2, TasksPage.ACCEPTANCE_TASK);
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();

        // then
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getText())
                .contains("Task properly completed.");
    }

    @Test(priority = 25)
    public void startVerificationTask() {
        // given
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);

        // when
        tasksPage.startTask(processNRPCode, TasksPage.VERIFICATION_TASK);
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();

        // then
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getText())
                .isEqualTo("The task properly assigned.");
    }

    @Test(priority = 26)
    public void completeVerificationTask() {
        // given
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);

        // when
        tasksPage.completeTask(processNRPCode, TasksPage.VERIFICATION_TASK);
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();

        // then
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getText())
                .contains("Task properly completed.");
    }

    @Test(priority = 27)
    public void checkProcessStatus() {
        // given
        ProcessInstancesPage processInstancesPage = ProcessInstancesPage.goToProcessInstancesPage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        // when
        processInstancesPage.selectPredefinedFilter("Show with Completed");
        String processStatus = processInstancesPage.getProcessStatus(processNRPCode);

        // then
        Assertions.assertThat(processStatus).isEqualTo("Completed");
    }

    @AfterClass()
    public void switchToLivePerspective() {
        PerspectiveChooser perspectiveChooser = PerspectiveChooser.create(driver, webDriverWait);
        perspectiveChooser.setLivePerspective();
    }

}

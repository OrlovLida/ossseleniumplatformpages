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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.alerts.SystemMessageInterface;
import com.oss.framework.components.mainheader.PerspectiveChooser;
import com.oss.framework.components.mainheader.ToolbarWidget;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.bpm.IntegrationProcessWizardPage;
import com.oss.pages.bpm.TasksPage;
import com.oss.pages.bpm.processinstances.ProcessInstancesPage;
import com.oss.pages.bpm.processinstances.ProcessWizardPage;
import com.oss.pages.physical.DeviceWizardPage;
import com.oss.utils.TestListener;

import io.qameta.allure.Description;

/**
 * @author Gabriela Kasza
 */
@Listeners({TestListener.class})
public class CreateProcessNRPTest extends BaseTestCase {
    private static final String BPM_USER_LOGIN = "bpm_webselenium";
    private static final String BPM_USER_PASSWORD = "Webtests123!";
    private static final String BPM_ADMIN_USER_LOGIN = "bpm_admin_webselenium";
    private static final String BPM_ADMIN_USER_PASSWORD = "Webtests123!";

    private static final Logger log = LoggerFactory.getLogger(CreateProcessNRPTest.class);

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
        processInstancesPage.clearAllColumnFilters();

        ToolbarWidget toolbarWidget = ToolbarWidget.create(driver, webDriverWait);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        if (!toolbarWidget.getUserName().equals(BPM_USER_LOGIN)) {
            processInstancesPage.changeUser(BPM_USER_LOGIN, BPM_USER_PASSWORD);
        }
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 1, description = "Create Network Resource Process")
    @Description("Create Network Resource Process")
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

    @Test(priority = 2, description = "Start 'High Level Planning' Task", dependsOnMethods = {"createProcessNRP"})
    @Description("Start 'High Level Planning' Task")
    public void startHLPTask() {
        // given
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.clearAllColumnFilters();

        // when
        tasksPage.startTask(processNRPCode, TasksPage.HIGH_LEVEL_PLANNING_TASK);
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();

        // then
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getText())
                .isEqualTo("The task properly assigned.");
    }

    @Test(priority = 3, description = "Create First Physical Device", dependsOnMethods = {"startHLPTask"})
    @Description("Create First Physical Device")
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
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.next();
        deviceWizardPage.setPreciseLocation("a");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        if (driver.getPageSource().contains("Available Mounting Positions")) {
            deviceWizardPage.setFirstAvailableMountingPosition();
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
        }
        deviceWizardPage.accept();
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();

        // then
        Assertions.assertThat(messages).hasSize(1);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getMessageType())
                .isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
    }

    @Test(priority = 4, description = "Complete 'High Level Planning' Task", dependsOnMethods = {"createFirstPhysicalDevice"})
    @Description("Complete 'High Level Planning' Task")
    public void completeHLPTask() {
        // given
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.clearAllColumnFilters();

        // when
        tasksPage.completeTask(processNRPCode, TasksPage.HIGH_LEVEL_PLANNING_TASK);
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();

        // then
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getText())
                .contains("Task properly completed.");
    }

    @Test(priority = 5, description = "Start 'Low Level Planning' Task", dependsOnMethods = {"completeHLPTask"})
    @Description("Start 'Low Level Planning' Task")
    public void startLLPTask() {
        // given
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.clearAllColumnFilters();

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

    @Test(priority = 6, description = "Assign File to 'Low Level Planning' Task", dependsOnMethods = {"startLLPTask"})
    @Description("Assign File to 'Low Level Planning' Task")
    public void assignFile() {
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.clearAllColumnFilters();
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
        Assertions.assertThat(files.size()).isPositive();

    }

    @Test(priority = 7, description = "Create Second Physical Device", dependsOnMethods = {"startLLPTask"})
    @Description("Create Second Physical Device")
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
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.next();
        deviceWizardPage.setPreciseLocation("t");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        if (driver.getPageSource().contains("Available Mounting Positions")) {
            deviceWizardPage.setFirstAvailableMountingPosition();
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
        }
        deviceWizardPage.accept();
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();

        // then
        Assertions.assertThat(messages).hasSize(1);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getMessageType())
                .isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
    }

    @Test(priority = 8, description = "Complete 'Low Level Design' Task", dependsOnMethods = {"createSecondPhysicalDevice"})
    @Description("Complete 'Low Level Design' Task")
    public void completeLLPTask() {
        // given
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.clearAllColumnFilters();

        // when
        tasksPage.completeTask(processNRPCode, TasksPage.LOW_LEVEL_PLANNING_TASK);
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();

        // then
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getText())
                .contains("Task properly completed.");
    }

    @Test(priority = 9, description = "Start 'Ready for Integration' Task", dependsOnMethods = {"completeLLPTask"})
    @Description("Start 'Ready for Integration' Task")
    public void startRFITask() {
        // given
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.clearAllColumnFilters();

        // when
        tasksPage.startTask(processNRPCode, TasksPage.READY_FOR_INTEGRATION_TASK);
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();

        // then
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getText())
                .isEqualTo("The task properly assigned.");
    }

    @Test(priority = 10, description = "Setup Integration", dependsOnMethods = {"startRFITask"})
    @Description("Setup Integration")
    public void setupIntegration() {
        // given
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.clearAllColumnFilters();

        // when
        tasksPage.setupIntegration(processNRPCode);
        IntegrationProcessWizardPage integrationWizard = new IntegrationProcessWizardPage(driver);
        integrationWizard.defineIntegrationProcess(processIPName1, "2020-07-01", 0);
        integrationWizard.defineIntegrationProcess(processIPName2, "2020-07-02", 1);
        DelayUtils.sleep();
        integrationWizard.clickNext();
        integrationWizard.dragAndDrop(deviceName1, processNRPCode, processIPName1);
        integrationWizard.dragAndDrop(deviceName2, processNRPCode, processIPName2);
        integrationWizard.clickAccept();
    }

    @Test(priority = 11, description = "Get 'Integration Process' Code", dependsOnMethods = {"setupIntegration"})
    @Description("Get 'Integration Process' Code")
    public void getIPCode() {

        // given
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.clearAllColumnFilters();

        // when
        tasksPage.findTask(processNRPCode, TasksPage.READY_FOR_INTEGRATION_TASK);
        DelayUtils.sleep(3000);
        processIPCode1 = tasksPage.getIPCodeByProcessName(processIPName1);
        processIPCode2 = tasksPage.getIPCodeByProcessName(processIPName2);
        log.info(processIPCode1 + processIPCode2);
    }

    @Test(priority = 12, description = "Complete 'Ready for Integration' Task", dependsOnMethods = {"getIPCode"})
    @Description("Complete 'Ready for Integration' Task")
    public void completeRFITask() {
        // given
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.clearAllColumnFilters();

        // when
        tasksPage.completeTask(processNRPCode, TasksPage.READY_FOR_INTEGRATION_TASK);
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();

        // then
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getText())
                .contains("Task properly completed.");
    }

    @Test(priority = 13, description = "Start 'Scope Definition' Task in First Integration Process", dependsOnMethods = {"completeRFITask"})
    @Description("Start 'Scope Definition' Task in First Integration Process")
    public void startSDTaskIP1() {
        // given
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.clearAllColumnFilters();

        // when
        tasksPage.startTask(processIPCode1, TasksPage.SCOPE_DEFINITION_TASK);
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();

        // then
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getText())
                .isEqualTo("The task properly assigned.");
    }

    @Test(priority = 14, description = "Complete 'Scope Definition' Task in First Integration Process", dependsOnMethods = {"startSDTaskIP1"})
    @Description("Complete 'Scope Definition' Task in First Integration Process")
    public void completeSDTaskIP1() {
        // given
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.clearAllColumnFilters();

        // when
        tasksPage.completeTask(processIPCode1, TasksPage.SCOPE_DEFINITION_TASK);
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();

        // then
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getText())
                .contains("Task properly completed.");
    }

    @Test(priority = 15, description = "Start 'Implementation' Task in First Integration Process", dependsOnMethods = {"completeSDTaskIP1"})
    @Description("Start 'Implementation' Task in First Integration Process")
    public void startImplementationTaskIP1() {
        // given
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.clearAllColumnFilters();

        // when
        tasksPage.startTask(processIPCode1, TasksPage.IMPLEMENTATION_TASK);
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();

        // then
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getText())
                .isEqualTo("The task properly assigned.");
    }

    @Test(priority = 16, description = "Complete 'Implementation' Task in First Integration Process", dependsOnMethods = {"startImplementationTaskIP1"})
    @Description("Complete 'Implementation' Task in First Integration Process")
    public void completeImplementationTaskIP1() {
        // given
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.clearAllColumnFilters();

        // when
        tasksPage.completeTask(processIPCode1, TasksPage.IMPLEMENTATION_TASK);
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();

        // then
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getText())
                .contains("Task properly completed.");
    }

    @Test(priority = 17, description = "Start 'Acceptance' Task in First Integration Process", dependsOnMethods = {"completeImplementationTaskIP1"})
    @Description("Start 'Acceptance' Task in First Integration Process")
    public void startAcceptanceTaskIP1() {
        // given
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.clearAllColumnFilters();

        // when
        tasksPage.startTask(processIPCode1, TasksPage.ACCEPTANCE_TASK);
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();

        // then
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getText())
                .isEqualTo("The task properly assigned.");
    }

    @Test(priority = 18, description = "Complete 'Acceptance' Task in First Integration Process", dependsOnMethods = {"startAcceptanceTaskIP1"})
    @Description("Complete 'Acceptance' Task in First Integration Process")
    public void completeAcceptanceTaskIP1() {
        // given
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.clearAllColumnFilters();

        // when
        tasksPage.completeTask(processIPCode1, TasksPage.ACCEPTANCE_TASK);
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();

        // then
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getText())
                .contains("Task properly completed.");
    }

    @Test(priority = 19, description = "Start 'Scope Definition' Task in Second Integration Process", dependsOnMethods = {"completeAcceptanceTaskIP1"})
    @Description("Start 'Scope Definition' Task in Second Integration Process")
    public void startSDTaskIP2() {
        // given
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.clearAllColumnFilters();

        // when
        tasksPage.startTask(processIPCode2, TasksPage.SCOPE_DEFINITION_TASK);
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();

        // then
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getText())
                .isEqualTo("The task properly assigned.");
    }

    @Test(priority = 20, description = "Complete 'Scope Definition' Task in Second Integration Process", dependsOnMethods = {"startSDTaskIP2"})
    @Description("Complete 'Scope Definition' Task in Second Integration Process")
    public void completeSDTaskIP2() {
        // given
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.clearAllColumnFilters();

        // when
        tasksPage.completeTask(processIPCode2, TasksPage.SCOPE_DEFINITION_TASK);
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();

        // then
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getText())
                .contains("Task properly completed.");
    }

    @Test(priority = 21, description = "Start 'Implementation' Task in Second Integration Process", dependsOnMethods = {"completeSDTaskIP2"})
    @Description("Start 'Implementation' Task in Second Integration Process")
    public void startImplementationTaskIP2() {
        // given
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.clearAllColumnFilters();
        tasksPage.startTask(processIPCode2, TasksPage.IMPLEMENTATION_TASK);

        // when
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();

        // then
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getText())
                .isEqualTo("The task properly assigned.");
    }

    @Test(priority = 22, description = "Complete 'Implementation' Task in Second Integration Process", dependsOnMethods = {"startImplementationTaskIP2"})
    @Description("Complete 'Implementation' Task in Second Integration Process")
    public void completeImplementationTaskIP2() {
        // given
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.clearAllColumnFilters();

        // when
        tasksPage.completeTask(processIPCode2, TasksPage.IMPLEMENTATION_TASK);
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();

        // then
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getText())
                .contains("Task properly completed.");
    }

    @Test(priority = 23, description = "Start 'Acceptance' Task in Second Integration Process", dependsOnMethods = {"completeImplementationTaskIP2"})
    @Description("Start 'Acceptance' Task in Second Integration Process")
    public void startAcceptanceTaskIP2() {
        // given
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.clearAllColumnFilters();

        // when
        tasksPage.startTask(processIPCode2, TasksPage.ACCEPTANCE_TASK);
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();

        // then
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getText())
                .isEqualTo("The task properly assigned.");
    }

    @Test(priority = 24, description = "Complete 'Acceptance' Task in Second Integration Process", dependsOnMethods = {"startAcceptanceTaskIP2"})
    @Description("Complete 'Acceptance' Task in Second Integration Process")
    public void completeAcceptanceTaskIP2() {
        // given
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.clearAllColumnFilters();

        // when
        tasksPage.completeTask(processIPCode2, TasksPage.ACCEPTANCE_TASK);
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();

        // then
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getText())
                .contains("Task properly completed.");
    }

    @Test(priority = 25, description = "Start 'Verification' Task in NRP", dependsOnMethods = {"completeAcceptanceTaskIP2"})
    @Description("Start 'Verification' Task in NRP")
    public void startVerificationTask() {
        // given
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.clearAllColumnFilters();

        // when
        tasksPage.startTask(processNRPCode, TasksPage.VERIFICATION_TASK);
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();

        // then
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getText())
                .isEqualTo("The task properly assigned.");
    }

    @Test(priority = 26, description = "Complete 'Verification' Task", dependsOnMethods = {"startVerificationTask"})
    @Description("Complete 'Verification' Task")
    public void completeVerificationTask() {
        // given
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.clearAllColumnFilters();

        // when
        tasksPage.completeTask(processNRPCode, TasksPage.VERIFICATION_TASK);
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();

        // then
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getText())
                .contains("Task properly completed.");
    }

    @Test(priority = 27, description = "Check Process status", dependsOnMethods = {"completeVerificationTask"})
    @Description("Check Process status")
    public void checkProcessStatus() {
        // given
        ProcessInstancesPage processInstancesPage = ProcessInstancesPage.goToProcessInstancesPage(driver, BASIC_URL);
        processInstancesPage.clearAllColumnFilters();
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
        if (!perspectiveChooser.getCurrentPerspective().equals("Live"))
            perspectiveChooser.setLivePerspective();
    }

}

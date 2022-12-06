/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2020 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.bpm;

import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.alerts.SystemMessageInterface;
import com.oss.framework.components.mainheader.PerspectiveChooser;
import com.oss.framework.components.mainheader.ToolbarWidget;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.bpm.tasks.IntegrationProcessWizardPage;
import com.oss.pages.bpm.processinstances.ProcessOverviewPage;
import com.oss.pages.bpm.tasks.TasksPageV2;
import com.oss.pages.physical.DeviceWizardPage;
import com.oss.utils.TestListener;
import io.qameta.allure.Description;
import org.assertj.core.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * @author Gabriela Kasza
 */
@Listeners({TestListener.class})
public class CreateProcessNRPTest extends BaseTestCase {
    private static final String BPM_USER_LOGIN = "bpm_webselenium";
    private static final String BPM_USER_PASSWORD = "Webtests123!";
    private static final String BPM_ADMIN_USER_LOGIN = "bpm_admin_webselenium";
    private static final String BPM_ADMIN_USER_PASSWORD = "Webtests123!";
    private static final String EMPTY_LIST_EXCEPTION = "The list is empty";
    private static final String TASK_PROPERLY_ASSIGNED_MESSAGE = "The task properly assigned.";
    private static final String CONNECTION_PANEL = "Connection Panel";
    private static final String GENERIC = "Generic";
    private static final String HOSTNAME = "Hostname";
    private static final String AVAILABLE_MOUNTING_POSITIONS = "Available Mounting Positions";
    private static final String PLAN_PERSPECTIVE = "PLAN";
    private static final String UPLOAD_FILE_PATH = "bpm/SeleniumTest.txt";
    private static final String CANNOT_LOAD_FILE_EXCEPTION = "Cannot load file";
    private static final String UPLOAD_FILE_NAME = "SeleniumTest";
    private static final String TASK_PROPERLY_COMPLETED_MESSAGE = "Task properly completed.";
    private static final String SHOW_WITH_COMPLETED_FILTER = "Show with Completed";
    private static final String COMPLETED_STATUS = "Completed";
    private static final String LIVE_PERSPECTIVE = "live";
    private static final String NRP = "Network Resource Process";

    private final Logger log = LoggerFactory.getLogger(CreateProcessNRPTest.class);
    private final String processIPName1 = "S.1-" + (int) (Math.random() * 100001);
    private final String processIPName2 = "S.2-" + (int) (Math.random() * 100001);
    private final String processNRPName = "Selenium Test-" + (int) (Math.random() * 100001);
    private final String deviceName1 = "Device-Selenium-" + (int) (Math.random() * 100001);
    private final String deviceName2 = "Device-Selenium-" + (int) (Math.random() * 100001);
    private String processNRPCode;
    private String processIPCode1;
    private String processIPCode2;

    @BeforeClass
    public void openProcessInstancesPage() {
        ProcessOverviewPage processOverviewPage = ProcessOverviewPage.goToProcessOverviewPage(driver, BASIC_URL);

        ToolbarWidget toolbarWidget = ToolbarWidget.create(driver, webDriverWait);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        if (!toolbarWidget.getUserName().equals(BPM_USER_LOGIN)) {
            processOverviewPage.changeUser(BPM_USER_LOGIN, BPM_USER_PASSWORD);
        }
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        processOverviewPage.clearAllColumnFilters();
    }

    @Test(priority = 1, description = "Create Network Resource Process")
    @Description("Create Network Resource Process")
    public void createProcessNRP() {
        ProcessOverviewPage processOverviewPage = ProcessOverviewPage.goToProcessOverviewPage(driver, BASIC_URL);
        processNRPCode = processOverviewPage.createProcessIPD(processNRPName, 0L, NRP);
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();

        Assertions.assertThat(messages).hasSize(1);
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assertions.assertThat(messages.get(0).getText()).contains(processNRPCode);
    }

    @Test(priority = 2, description = "Start 'High Level Planning' Task", dependsOnMethods = {"createProcessNRP"})
    @Description("Start 'High Level Planning' Task")
    public void startHLPTask() {
        // given
        TasksPageV2 tasksPage = TasksPageV2.goToTasksPage(driver, webDriverWait, BASIC_URL).clearFilters();

        // when
        tasksPage.startTask(processNRPCode, TasksPageV2.HIGH_LEVEL_PLANNING_TASK);
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();

        // then
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException(EMPTY_LIST_EXCEPTION)).getText())
                .isEqualTo(TASK_PROPERLY_ASSIGNED_MESSAGE);
    }

    @Test(priority = 3, description = "Create First Physical Device", dependsOnMethods = {"startHLPTask"})
    @Description("Create First Physical Device")
    public void createFirstPhysicalDevice() {

        // given
        DeviceWizardPage deviceWizardPage = DeviceWizardPage.goToDeviceWizardPageLive(driver, BASIC_URL);
        PerspectiveChooser perspectiveChooser = PerspectiveChooser.create(driver, webDriverWait);

        // when
        perspectiveChooser.setPlanPerspective(processNRPCode);
        deviceWizardPage.setEquipmentType(CONNECTION_PANEL);
        deviceWizardPage.setModel(GENERIC);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.setName(deviceName1);
        deviceWizardPage.setNetworkDomain(" ");
        if (driver.getPageSource().contains(HOSTNAME)) {
            deviceWizardPage.setHostname(deviceName1);
        }
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.next();
        deviceWizardPage.setPreciseLocation("");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        if (driver.getPageSource().contains(AVAILABLE_MOUNTING_POSITIONS)) {
            deviceWizardPage.setFirstAvailableMountingPosition();
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
        }
        deviceWizardPage.accept();
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();

        // then
        Assertions.assertThat(messages).hasSize(1);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException(EMPTY_LIST_EXCEPTION)).getMessageType())
                .isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
    }

    @Test(priority = 4, description = "Complete 'High Level Planning' Task", dependsOnMethods = {"createFirstPhysicalDevice"})
    @Description("Complete 'High Level Planning' Task")
    public void completeHLPTask() {
        // given
        TasksPageV2 tasksPage = TasksPageV2.goToTasksPage(driver, webDriverWait, BASIC_URL).clearFilters();

        // when
        tasksPage.completeTask(processNRPCode, TasksPageV2.HIGH_LEVEL_PLANNING_TASK);
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();

        // then
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException(EMPTY_LIST_EXCEPTION)).getText())
                .contains(TASK_PROPERLY_COMPLETED_MESSAGE);
    }

    @Test(priority = 5, description = "Start 'Low Level Planning' Task", dependsOnMethods = {"completeHLPTask"})
    @Description("Start 'Low Level Planning' Task")
    public void startLLPTask() {
        // given
        TasksPageV2 tasksPage = TasksPageV2.goToTasksPage(driver, webDriverWait, BASIC_URL).clearFilters();

        // when
        tasksPage.startTask(processNRPCode, TasksPageV2.LOW_LEVEL_PLANNING_TASK);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();

        // then
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException(EMPTY_LIST_EXCEPTION)).getText())
                .isEqualTo(TASK_PROPERLY_ASSIGNED_MESSAGE);
        String currentUrl = driver.getCurrentUrl();
        String[] split = currentUrl.split(Pattern.quote("?"));
        String perspectiveContext = split[1];
        Assertions.assertThat(perspectiveContext).contains(PLAN_PERSPECTIVE);
    }

    @Test(priority = 6, description = "Assign File to 'Low Level Planning' Task", dependsOnMethods = {"startLLPTask"})
    @Description("Assign File to 'Low Level Planning' Task")
    public void assignFile() {
        TasksPageV2 tasksPage = TasksPageV2.goToTasksPage(driver, webDriverWait, BASIC_URL).clearFilters();
        try {
            URL resource = CreateProcessNRPTest.class.getClassLoader().getResource(UPLOAD_FILE_PATH);
            String absolutePatch = Paths.get(Objects.requireNonNull(resource).toURI()).toFile().getAbsolutePath();
            tasksPage.addFile(processNRPCode, TasksPageV2.LOW_LEVEL_PLANNING_TASK, absolutePatch);
            SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
            Assertions
                    .assertThat(
                            systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException(EMPTY_LIST_EXCEPTION)).getMessageType())
                    .isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        } catch (URISyntaxException e) {
            throw new RuntimeException(CANNOT_LOAD_FILE_EXCEPTION, e);
        }
        DelayUtils.sleep(2000);
        List<String> files = tasksPage.getListOfAttachments();
        Assertions.assertThat(files.get(0)).contains(UPLOAD_FILE_NAME);
        Assertions.assertThat(files).isNotEmpty();

    }

    @Test(priority = 7, description = "Create Second Physical Device", dependsOnMethods = {"startLLPTask"})
    @Description("Create Second Physical Device")
    public void createSecondPhysicalDevice() {
        // given
        DeviceWizardPage deviceWizardPage = DeviceWizardPage.goToDeviceWizardPageLive(driver, BASIC_URL);
        PerspectiveChooser perspectiveChooser = PerspectiveChooser.create(driver, webDriverWait);

        // when
        perspectiveChooser.setPlanPerspective(processNRPCode);
        deviceWizardPage.setEquipmentType(CONNECTION_PANEL);
        deviceWizardPage.setModel(GENERIC);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.setName(deviceName2);
        deviceWizardPage.setNetworkDomain(" ");
        if (driver.getPageSource().contains(HOSTNAME)) {
            deviceWizardPage.setHostname(deviceName2);
        }
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.next();
        deviceWizardPage.setPreciseLocation("");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        if (driver.getPageSource().contains(AVAILABLE_MOUNTING_POSITIONS)) {
            deviceWizardPage.setFirstAvailableMountingPosition();
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
        }
        deviceWizardPage.accept();
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();

        // then
        Assertions.assertThat(messages).hasSize(1);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException(EMPTY_LIST_EXCEPTION)).getMessageType())
                .isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
    }

    @Test(priority = 8, description = "Complete 'Low Level Design' Task", dependsOnMethods = {"createSecondPhysicalDevice"})
    @Description("Complete 'Low Level Design' Task")
    public void completeLLPTask() {
        // given
        TasksPageV2 tasksPage = TasksPageV2.goToTasksPage(driver, webDriverWait, BASIC_URL).clearFilters();

        // when
        tasksPage.completeTask(processNRPCode, TasksPageV2.LOW_LEVEL_PLANNING_TASK);
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();

        // then
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException(EMPTY_LIST_EXCEPTION)).getText())
                .contains(TASK_PROPERLY_COMPLETED_MESSAGE);
    }

    @Test(priority = 9, description = "Start 'Ready for Integration' Task", dependsOnMethods = {"completeLLPTask"})
    @Description("Start 'Ready for Integration' Task")
    public void startRFITask() {
        // given
        TasksPageV2 tasksPage = TasksPageV2.goToTasksPage(driver, webDriverWait, BASIC_URL).clearFilters();

        // when
        tasksPage.startTask(processNRPCode, TasksPageV2.READY_FOR_INTEGRATION_TASK);
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();

        // then
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException(EMPTY_LIST_EXCEPTION)).getText())
                .isEqualTo(TASK_PROPERLY_ASSIGNED_MESSAGE);
    }

    @Test(priority = 10, description = "Setup Integration", dependsOnMethods = {"startRFITask"})
    @Description("Setup Integration")
    public void setupIntegration() {
        // given
        TasksPageV2 tasksPage = TasksPageV2.goToTasksPage(driver, webDriverWait, BASIC_URL).clearFilters();

        // when
        IntegrationProcessWizardPage integrationWizard = tasksPage.setupIntegration(processNRPCode);
        integrationWizard.defineIntegrationProcess(processIPName1, "2020-07-01", 0);
        integrationWizard.defineIntegrationProcess(processIPName2, "2020-07-02", 1);
        DelayUtils.sleep();
        integrationWizard.clickNext();
        DelayUtils.sleep(1500);
        String processNRPCodeName = processNRPName + " (" + processNRPCode + ")";
        integrationWizard.dragAndDrop(deviceName1, processNRPCodeName, processIPName1);
        integrationWizard.dragAndDrop(deviceName2, processNRPCodeName, processIPName2);
        integrationWizard.clickAccept();
    }

    @Test(priority = 11, description = "Get 'Integration Process' Code", dependsOnMethods = {"setupIntegration"})
    @Description("Get 'Integration Process' Code")
    public void getIPCode() {

        // given
        TasksPageV2 tasksPage = TasksPageV2.goToTasksPage(driver, webDriverWait, BASIC_URL).clearFilters();

        // when
        tasksPage.findTask(processNRPCode, TasksPageV2.READY_FOR_INTEGRATION_TASK);
        DelayUtils.sleep(3000);
        processIPCode1 = tasksPage.getIPCodeByProcessName(processIPName1);
        processIPCode2 = tasksPage.getIPCodeByProcessName(processIPName2);
        log.info(processIPCode1 + processIPCode2);
    }

    @Test(priority = 12, description = "Complete 'Ready for Integration' Task", dependsOnMethods = {"getIPCode"})
    @Description("Complete 'Ready for Integration' Task")
    public void completeRFITask() {
        // given
        TasksPageV2 tasksPage = TasksPageV2.goToTasksPage(driver, webDriverWait, BASIC_URL).clearFilters();

        // when
        tasksPage.completeTask(processNRPCode, TasksPageV2.READY_FOR_INTEGRATION_TASK);
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();

        // then
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException(EMPTY_LIST_EXCEPTION)).getText())
                .contains(TASK_PROPERLY_COMPLETED_MESSAGE);
    }

    @Test(priority = 13, description = "Start 'Scope Definition' Task in First Integration Process", dependsOnMethods = {"completeRFITask"})
    @Description("Start 'Scope Definition' Task in First Integration Process")
    public void startSDTaskIP1() {
        // given
        TasksPageV2 tasksPage = TasksPageV2.goToTasksPage(driver, webDriverWait, BASIC_URL).clearFilters();

        // when
        tasksPage.startTask(processIPCode1, TasksPageV2.SCOPE_DEFINITION_TASK);
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();

        // then
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException(EMPTY_LIST_EXCEPTION)).getText())
                .isEqualTo(TASK_PROPERLY_ASSIGNED_MESSAGE);
    }

    @Test(priority = 14, description = "Complete 'Scope Definition' Task in First Integration Process", dependsOnMethods = {"startSDTaskIP1"})
    @Description("Complete 'Scope Definition' Task in First Integration Process")
    public void completeSDTaskIP1() {
        // given
        TasksPageV2 tasksPage = TasksPageV2.goToTasksPage(driver, webDriverWait, BASIC_URL).clearFilters();

        // when
        tasksPage.completeTask(processIPCode1, TasksPageV2.SCOPE_DEFINITION_TASK);
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();

        // then
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException(EMPTY_LIST_EXCEPTION)).getText())
                .contains(TASK_PROPERLY_COMPLETED_MESSAGE);
    }

    @Test(priority = 15, description = "Start 'Implementation' Task in First Integration Process", dependsOnMethods = {"completeSDTaskIP1"})
    @Description("Start 'Implementation' Task in First Integration Process")
    public void startImplementationTaskIP1() {
        // given
        TasksPageV2 tasksPage = TasksPageV2.goToTasksPage(driver, webDriverWait, BASIC_URL).clearFilters();

        // when
        tasksPage.startTask(processIPCode1, TasksPageV2.IMPLEMENTATION_TASK);
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();

        // then
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException(EMPTY_LIST_EXCEPTION)).getText())
                .isEqualTo(TASK_PROPERLY_ASSIGNED_MESSAGE);
    }

    @Test(priority = 16, description = "Complete 'Implementation' Task in First Integration Process", dependsOnMethods = {"startImplementationTaskIP1"})
    @Description("Complete 'Implementation' Task in First Integration Process")
    public void completeImplementationTaskIP1() {
        // given
        TasksPageV2 tasksPage = TasksPageV2.goToTasksPage(driver, webDriverWait, BASIC_URL).clearFilters();

        // when
        tasksPage.completeTask(processIPCode1, TasksPageV2.IMPLEMENTATION_TASK);
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();

        // then
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException(EMPTY_LIST_EXCEPTION)).getText())
                .contains(TASK_PROPERLY_COMPLETED_MESSAGE);
    }

    @Test(priority = 17, description = "Start 'Acceptance' Task in First Integration Process", dependsOnMethods = {"completeImplementationTaskIP1"})
    @Description("Start 'Acceptance' Task in First Integration Process")
    public void startAcceptanceTaskIP1() {
        // given
        TasksPageV2 tasksPage = TasksPageV2.goToTasksPage(driver, webDriverWait, BASIC_URL).clearFilters();

        // when
        tasksPage.startTask(processIPCode1, TasksPageV2.ACCEPTANCE_TASK);
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();

        // then
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException(EMPTY_LIST_EXCEPTION)).getText())
                .isEqualTo(TASK_PROPERLY_ASSIGNED_MESSAGE);
    }

    @Test(priority = 18, description = "Complete 'Acceptance' Task in First Integration Process", dependsOnMethods = {"startAcceptanceTaskIP1"})
    @Description("Complete 'Acceptance' Task in First Integration Process")
    public void completeAcceptanceTaskIP1() {
        // given
        TasksPageV2 tasksPage = TasksPageV2.goToTasksPage(driver, webDriverWait, BASIC_URL).clearFilters();

        // when
        tasksPage.completeTask(processIPCode1, TasksPageV2.ACCEPTANCE_TASK);
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();

        // then
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException(EMPTY_LIST_EXCEPTION)).getText())
                .contains(TASK_PROPERLY_COMPLETED_MESSAGE);
    }

    @Test(priority = 19, description = "Start 'Scope Definition' Task in Second Integration Process", dependsOnMethods = {"completeAcceptanceTaskIP1"})
    @Description("Start 'Scope Definition' Task in Second Integration Process")
    public void startSDTaskIP2() {
        // given
        TasksPageV2 tasksPage = TasksPageV2.goToTasksPage(driver, webDriverWait, BASIC_URL).clearFilters();

        // when
        tasksPage.startTask(processIPCode2, TasksPageV2.SCOPE_DEFINITION_TASK);
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();

        // then
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException(EMPTY_LIST_EXCEPTION)).getText())
                .isEqualTo(TASK_PROPERLY_ASSIGNED_MESSAGE);
    }

    @Test(priority = 20, description = "Complete 'Scope Definition' Task in Second Integration Process", dependsOnMethods = {"startSDTaskIP2"})
    @Description("Complete 'Scope Definition' Task in Second Integration Process")
    public void completeSDTaskIP2() {
        // given
        TasksPageV2 tasksPage = TasksPageV2.goToTasksPage(driver, webDriverWait, BASIC_URL).clearFilters();

        // when
        tasksPage.completeTask(processIPCode2, TasksPageV2.SCOPE_DEFINITION_TASK);
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();

        // then
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException(EMPTY_LIST_EXCEPTION)).getText())
                .contains(TASK_PROPERLY_COMPLETED_MESSAGE);
    }

    @Test(priority = 21, description = "Start 'Implementation' Task in Second Integration Process", dependsOnMethods = {"completeSDTaskIP2"})
    @Description("Start 'Implementation' Task in Second Integration Process")
    public void startImplementationTaskIP2() {
        // given
        TasksPageV2 tasksPage = TasksPageV2.goToTasksPage(driver, webDriverWait, BASIC_URL).clearFilters();
        tasksPage.startTask(processIPCode2, TasksPageV2.IMPLEMENTATION_TASK);

        // when
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();

        // then
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException(EMPTY_LIST_EXCEPTION)).getText())
                .isEqualTo(TASK_PROPERLY_ASSIGNED_MESSAGE);
    }

    @Test(priority = 22, description = "Complete 'Implementation' Task in Second Integration Process", dependsOnMethods = {"startImplementationTaskIP2"})
    @Description("Complete 'Implementation' Task in Second Integration Process")
    public void completeImplementationTaskIP2() {
        // given
        TasksPageV2 tasksPage = TasksPageV2.goToTasksPage(driver, webDriverWait, BASIC_URL).clearFilters();

        // when
        tasksPage.completeTask(processIPCode2, TasksPageV2.IMPLEMENTATION_TASK);
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();

        // then
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException(EMPTY_LIST_EXCEPTION)).getText())
                .contains(TASK_PROPERLY_COMPLETED_MESSAGE);
    }

    @Test(priority = 23, description = "Start 'Acceptance' Task in Second Integration Process", dependsOnMethods = {"completeImplementationTaskIP2"})
    @Description("Start 'Acceptance' Task in Second Integration Process")
    public void startAcceptanceTaskIP2() {
        // given
        TasksPageV2 tasksPage = TasksPageV2.goToTasksPage(driver, webDriverWait, BASIC_URL).clearFilters();

        // when
        tasksPage.startTask(processIPCode2, TasksPageV2.ACCEPTANCE_TASK);
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();

        // then
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException(EMPTY_LIST_EXCEPTION)).getText())
                .isEqualTo(TASK_PROPERLY_ASSIGNED_MESSAGE);
    }

    @Test(priority = 24, description = "Complete 'Acceptance' Task in Second Integration Process", dependsOnMethods = {"startAcceptanceTaskIP2"})
    @Description("Complete 'Acceptance' Task in Second Integration Process")
    public void completeAcceptanceTaskIP2() {
        // given
        TasksPageV2 tasksPage = TasksPageV2.goToTasksPage(driver, webDriverWait, BASIC_URL).clearFilters();

        // when
        tasksPage.completeTask(processIPCode2, TasksPageV2.ACCEPTANCE_TASK);
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();

        // then
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException(EMPTY_LIST_EXCEPTION)).getText())
                .contains(TASK_PROPERLY_COMPLETED_MESSAGE);
    }

    @Test(priority = 25, description = "Start 'Verification' Task in NRP", dependsOnMethods = {"completeAcceptanceTaskIP2"})
    @Description("Start 'Verification' Task in NRP")
    public void startVerificationTask() {
        // given
        TasksPageV2 tasksPage = TasksPageV2.goToTasksPage(driver, webDriverWait, BASIC_URL).clearFilters();

        // when
        tasksPage.startTask(processNRPCode, TasksPageV2.VERIFICATION_TASK);
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();

        // then
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException(EMPTY_LIST_EXCEPTION)).getText())
                .isEqualTo(TASK_PROPERLY_ASSIGNED_MESSAGE);
    }

    @Test(priority = 26, description = "Complete 'Verification' Task", dependsOnMethods = {"startVerificationTask"})
    @Description("Complete 'Verification' Task")
    public void completeVerificationTask() {
        // given
        TasksPageV2 tasksPage = TasksPageV2.goToTasksPage(driver, webDriverWait, BASIC_URL).clearFilters();

        // when
        tasksPage.completeTask(processNRPCode, TasksPageV2.VERIFICATION_TASK);
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();

        // then
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException(EMPTY_LIST_EXCEPTION)).getText())
                .contains(TASK_PROPERLY_COMPLETED_MESSAGE);
    }

    @Test(priority = 27, description = "Check Process status", dependsOnMethods = {"completeVerificationTask"})
    @Description("Check Process status")
    public void checkProcessStatus() {
        // given
        ProcessOverviewPage processOverviewPage = ProcessOverviewPage.goToProcessOverviewPage(driver, BASIC_URL);
        processOverviewPage.clearAllColumnFilters();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        // when
        processOverviewPage.selectPredefinedFilter(SHOW_WITH_COMPLETED_FILTER);
        String processStatus = processOverviewPage.selectProcess(processNRPCode).getProcessStatus();

        // then
        Assertions.assertThat(processStatus).isEqualTo(COMPLETED_STATUS);
    }

    @AfterClass()
    public void switchToLivePerspective() {
        PerspectiveChooser perspectiveChooser = PerspectiveChooser.create(driver, webDriverWait);
        if (!perspectiveChooser.getCurrentPerspective().equalsIgnoreCase(LIVE_PERSPECTIVE))
            perspectiveChooser.setLivePerspective();
    }

}

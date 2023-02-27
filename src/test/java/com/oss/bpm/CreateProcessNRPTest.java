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
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.bpm.planning.ProcessDetailsPage;
import com.oss.pages.bpm.processinstances.ProcessOverviewPage;
import com.oss.pages.bpm.tasks.SetupIntegrationProperties;
import com.oss.pages.bpm.tasks.TasksPageV2;
import com.oss.pages.dms.AttachFileWizardPage;
import com.oss.planning.PlanningContext;
import com.oss.untils.FakeGenerator;
import com.oss.utils.TestListener;
import io.qameta.allure.Description;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

import static com.oss.bpm.BpmPhysicalDataCreator.CHASSIS_NAME;
import static com.oss.bpm.BpmPhysicalDataCreator.createBuilding;
import static com.oss.bpm.BpmPhysicalDataCreator.createIPDevice;
import static com.oss.bpm.BpmPhysicalDataCreator.deleteBuilding;
import static com.oss.bpm.BpmPhysicalDataCreator.deleteIPDevice;
import static com.oss.bpm.BpmPhysicalDataCreator.getDeviceChassisId;
import static com.oss.pages.bpm.tasks.TasksPageV2.ACCEPTANCE_TASK;
import static com.oss.pages.bpm.tasks.TasksPageV2.HIGH_LEVEL_PLANNING_TASK;
import static com.oss.pages.bpm.tasks.TasksPageV2.IMPLEMENTATION_TASK;
import static com.oss.pages.bpm.tasks.TasksPageV2.LOW_LEVEL_PLANNING_TASK;
import static com.oss.pages.bpm.tasks.TasksPageV2.READY_FOR_INTEGRATION_TASK;
import static com.oss.pages.bpm.tasks.TasksPageV2.SCOPE_DEFINITION_TASK;
import static com.oss.pages.bpm.tasks.TasksPageV2.VERIFICATION_TASK;

/**
 * @author Gabriela Kasza
 * @author Paweł Rother
 */

@Listeners({TestListener.class})
public class CreateProcessNRPTest extends BaseTestCase {
    private static final String DEVICE_MODEL = "7705 SAR-8";
    private static final String CHASSIS_IDENTIFIER1 = CHASSIS_NAME + " (%s )";
    private static final String PLAN_PERSPECTIVE = "PLAN";
    private static final String UPLOAD_FILE_PATH = "bpm/SeleniumTest.txt";
    private static final String CANNOT_LOAD_FILE_EXCEPTION = "Cannot get file path.";
    private static final String UPLOAD_FILE_NAME = "SeleniumTest";
    private static final String SHOW_WITH_COMPLETED_FILTER = "Show with Completed";
    private static final String COMPLETED_STATUS = "Completed";
    private static final String LIVE_PERSPECTIVE = "live";
    private static final String NRP = "Network Resource Process";
    private static final String PROCESS_CREATED_MESSAGE_PATTERN = "Process %1$s (%2$s) was created";
    private static final String INVALID_PROCESS_CREATION_MESSAGE = "Invalid process NRP creation message";
    private static final String INVALID_TASK_START_MESSAGE_PATTERN = "Invalid task %s start message.";
    private static final String INVALID_ATTACH_FILE_MESSAGE_PATTERN = "Invalid attach file system message in %s task.";
    private static final String INVALID_OBJECTS_AMOUNT = "Invalid Objects Amount in Process Details View.";
    private static final String INVALID_IP_PROCESSES_AMOUNT = "Invalid IP Processes Amount after Setup Integration.";
    private static final String INVALID_TASK_COMPLETE_MESSAGE_PATTERN = "Invalid task %s complete message.";
    private static final String INVALID_PERSPECTIVE_PATTERN = "Invalid perspective context in %s task.";
    private static final String TASK_COMPLETED_MESSAGE = "Task properly completed.";
    private static final String TASK_ASSIGNED_MESSAGE = "The task properly assigned.";
    private static final String ATTACHMENT_ADDED_MESSAGE = "Attachment has been added";
    private static final String PERSPECTIVE_QUERY = "perspective=%s";

    private static final Random RANDOM = new Random();
    private static final LocalDate TODAY = LocalDate.now();
    private static final PlanningContext LIVE = PlanningContext.live();
    private static final String FILE_NOT_VISIBLE = "Uploaded file is not visible on Attachments tab.";
    private static final String INVALID_NRP_PROCESS_STATUS = "Invalid NRP Process status.";
    private static final String WIZARD_STILL_OPENED = "Attach file wizard is still open after trying to upload file.";
    private final Logger log = LoggerFactory.getLogger(CreateProcessNRPTest.class);
    private final String BUILDING_NAME = FakeGenerator.getCity() + "-BU" + FakeGenerator.getRandomInt();
    private final String ROUTER_1_NAME = "Create NRP Selenium Test 1." + RANDOM.nextInt(Integer.MAX_VALUE);
    private final String ROUTER_2_NAME = "Create NRP Selenium Test 2." + RANDOM.nextInt(Integer.MAX_VALUE);
    private final String processIPName1 = "Create NRP Selenium Test IP 1." + RANDOM.nextInt(Integer.MAX_VALUE);
    private final String processIPName2 = "Create NRP Selenium Test IP 2." + RANDOM.nextInt(Integer.MAX_VALUE);
    private final String processNRPName = "Create NRP Selenium Test NRP " + RANDOM.nextInt(Integer.MAX_VALUE);
    private SoftAssert softAssert;
    private String processNRPCode;
    private PlanningContext processNRPContext;
    private String processIPCode1;
    private String processIPCode2;
    private String buildingId;
    private String device1Id;
    private String chassis1Id;
    private String device2Id;
    private String chassis2Id;

    @BeforeClass
    public void openProcessInstancesPage() {
        softAssert = new SoftAssert();
        ProcessOverviewPage.goToProcessOverviewPage(driver, BASIC_URL).clearAllColumnFilters();
        buildingId = createBuilding(BUILDING_NAME, LIVE);
        log.info("Building id: " + buildingId);
    }

    @Test(priority = 1, description = "Create Network Resource Process")
    @Description("Create Network Resource Process")
    public void createProcessNRP() {
        ProcessOverviewPage processOverviewPage = ProcessOverviewPage.goToProcessOverviewPage(driver, BASIC_URL);
        processNRPCode = processOverviewPage.createProcessIPD(processNRPName, 5L, NRP);

        log.info("NRP Code: " + processNRPCode);

        assertSystemMessage(String.format(PROCESS_CREATED_MESSAGE_PATTERN, processNRPName, processNRPCode),
                SystemMessageContainer.MessageType.SUCCESS, INVALID_PROCESS_CREATION_MESSAGE);

        processNRPContext = PlanningContext.plan(processOverviewPage.getProjectId(processNRPCode));
        log.info("NRP project ID: " + processNRPContext.getProjectId());
    }

    @Test(priority = 2, description = "Start 'High Level Planning' Task", dependsOnMethods = {"createProcessNRP"})
    @Description("Start 'High Level Planning' Task")
    public void startHLPTask() {
        // given
        TasksPageV2 tasksPage = TasksPageV2.goToTasksPage(driver, webDriverWait, BASIC_URL).clearFilters();

        // when
        tasksPage.startTask(processNRPCode, HIGH_LEVEL_PLANNING_TASK);

        // then
        assertSystemMessage(TASK_ASSIGNED_MESSAGE, SystemMessageContainer.MessageType.SUCCESS,
                String.format(INVALID_TASK_START_MESSAGE_PATTERN, HIGH_LEVEL_PLANNING_TASK));
    }

    @Test(priority = 3, description = "Create First Physical Device", dependsOnMethods = {"startHLPTask"})
    @Description("Create First Physical Device")
    public void createFirstPhysicalDevice() {
        device1Id = createIPDevice(ROUTER_1_NAME, DEVICE_MODEL, buildingId, processNRPContext);
        chassis1Id = getDeviceChassisId(device1Id, processNRPContext);

        log.info(String.format("Device 1 ID: %1$s \nChassis 1 ID: %2$s", device1Id, chassis1Id));

        ProcessDetailsPage processDetailsPage =
                ProcessDetailsPage.goToProcessDetailsView(driver, BASIC_URL, processNRPContext.getProjectId());
        Assert.assertEquals(processDetailsPage.getObjectsAmount(), 2, INVALID_OBJECTS_AMOUNT);
    }

    @Test(priority = 4, description = "Complete 'High Level Planning' Task", dependsOnMethods = {"createFirstPhysicalDevice"})
    @Description("Complete 'High Level Planning' Task")
    public void completeHLPTask() {
        // given
        TasksPageV2 tasksPage = TasksPageV2.goToTasksPage(driver, webDriverWait, BASIC_URL).clearFilters();

        // when
        tasksPage.completeTask(processNRPCode, HIGH_LEVEL_PLANNING_TASK);

        // then
        assertSystemMessage(TASK_COMPLETED_MESSAGE, SystemMessageContainer.MessageType.SUCCESS,
                String.format(INVALID_TASK_COMPLETE_MESSAGE_PATTERN, HIGH_LEVEL_PLANNING_TASK));
    }

    @Test(priority = 5, description = "Start 'Low Level Planning' Task", dependsOnMethods = {"completeHLPTask"})
    @Description("Start 'Low Level Planning' Task")
    public void startLLPTask() {
        // given
        TasksPageV2 tasksPage = TasksPageV2.goToTasksPage(driver, webDriverWait, BASIC_URL).clearFilters();

        // when
        tasksPage.startTask(processNRPCode, LOW_LEVEL_PLANNING_TASK);

        // then
        assertSystemMessage(TASK_ASSIGNED_MESSAGE, SystemMessageContainer.MessageType.SUCCESS,
                String.format(INVALID_TASK_START_MESSAGE_PATTERN, LOW_LEVEL_PLANNING_TASK));

        Assert.assertTrue(driver.getCurrentUrl().contains(String.format(PERSPECTIVE_QUERY, PLAN_PERSPECTIVE)),
                String.format(INVALID_PERSPECTIVE_PATTERN, LOW_LEVEL_PLANNING_TASK));
    }

    @Test(priority = 6, description = "Assign File to 'Low Level Planning' Task", dependsOnMethods = {"startLLPTask"})
    @Description("Assign File to 'Low Level Planning' Task")
    public void assignFile() throws URISyntaxException {
        TasksPageV2 tasksPage = TasksPageV2.goToTasksPage(driver, webDriverWait, BASIC_URL).clearFilters();
        try {
            URL resource = CreateProcessNRPTest.class.getClassLoader().getResource(UPLOAD_FILE_PATH);
            String absolutePatch = Paths.get(Objects.requireNonNull(resource).toURI()).toFile().getAbsolutePath();
            tasksPage.addFile(processNRPCode, LOW_LEVEL_PLANNING_TASK, absolutePatch);
            assertSystemMessage(ATTACHMENT_ADDED_MESSAGE, SystemMessageContainer.MessageType.SUCCESS,
                    String.format(INVALID_ATTACH_FILE_MESSAGE_PATTERN, LOW_LEVEL_PLANNING_TASK));
        } catch (URISyntaxException e) {
            throw new URISyntaxException(UPLOAD_FILE_PATH, CANNOT_LOAD_FILE_EXCEPTION);
        }
        DelayUtils.sleep(2000);
        if (AttachFileWizardPage.isWizardVisible(driver)) {
            new AttachFileWizardPage(driver).cancelButton();
            Assert.fail(WIZARD_STILL_OPENED);
        }
        List<String> files = tasksPage.getListOfAttachments();
        if (files.isEmpty()) {
            Assert.fail(FILE_NOT_VISIBLE);
        } else {
            Assert.assertTrue(files.get(0).contains(UPLOAD_FILE_NAME), FILE_NOT_VISIBLE);
        }
    }

    @Test(priority = 7, description = "Create Second Physical Device", dependsOnMethods = {"startLLPTask"})
    @Description("Create Second Physical Device")
    public void createSecondPhysicalDevice() {
        device2Id = createIPDevice(ROUTER_2_NAME, DEVICE_MODEL, buildingId, processNRPContext);
        chassis2Id = getDeviceChassisId(device2Id, processNRPContext);

        log.info(String.format("Device 2 ID: %1$s \nChassis 2 ID: %2$s", device2Id, chassis2Id));

        ProcessDetailsPage processDetailsPage = ProcessDetailsPage.goToProcessDetailsView(driver, BASIC_URL, processNRPContext.getProjectId());
        Assert.assertEquals(processDetailsPage.getObjectsAmount(), 4, INVALID_OBJECTS_AMOUNT);
    }

    @Test(priority = 8, description = "Complete 'Low Level Design' Task", dependsOnMethods = {"createSecondPhysicalDevice"})
    @Description("Complete 'Low Level Design' Task")
    public void completeLLPTask() {
        // given
        TasksPageV2 tasksPage = TasksPageV2.goToTasksPage(driver, webDriverWait, BASIC_URL).clearFilters();

        // when
        tasksPage.completeTask(processNRPCode, LOW_LEVEL_PLANNING_TASK);

        // then
        assertSystemMessage(TASK_COMPLETED_MESSAGE, SystemMessageContainer.MessageType.SUCCESS,
                String.format(INVALID_TASK_COMPLETE_MESSAGE_PATTERN, LOW_LEVEL_PLANNING_TASK));
    }

    @Test(priority = 9, description = "Start 'Ready for Integration' Task", dependsOnMethods = {"completeLLPTask"})
    @Description("Start 'Ready for Integration' Task")
    public void startRFITask() {
        // given
        TasksPageV2 tasksPage = TasksPageV2.goToTasksPage(driver, webDriverWait, BASIC_URL).clearFilters();

        // when
        tasksPage.startTask(processNRPCode, READY_FOR_INTEGRATION_TASK);

        // then
        assertSystemMessage(TASK_ASSIGNED_MESSAGE, SystemMessageContainer.MessageType.SUCCESS,
                String.format(INVALID_TASK_START_MESSAGE_PATTERN, READY_FOR_INTEGRATION_TASK));
    }

    @Test(priority = 10, description = "Setup Integration", dependsOnMethods = {"startRFITask"})
    @Description("Setup Integration")
    public void setupIntegration() {

        SetupIntegrationProperties setupIntegrationProperties_IP_1 = SetupIntegrationProperties.builder()
                .integrationProcessName(processIPName1)
                .finishedDueDate(TODAY.plusDays(2L))
                .objectIdentifiers(Arrays.asList(String.format(CHASSIS_IDENTIFIER1, chassis1Id), ROUTER_1_NAME))
                .build();

        SetupIntegrationProperties setupIntegrationProperties_IP_2 = SetupIntegrationProperties.builder()
                .integrationProcessName(processIPName2)
                .finishedDueDate(TODAY.plusDays(3L))
                .objectIdentifiers(Arrays.asList(ROUTER_2_NAME, String.format(CHASSIS_IDENTIFIER1, chassis2Id)))
                .build();

        // given
        TasksPageV2 tasksPage = TasksPageV2.goToTasksPage(driver, webDriverWait, BASIC_URL).clearFilters();

        // when
        List<String> ipCodes = tasksPage.setupIntegration(processNRPCode, processNRPName,
                Arrays.asList(setupIntegrationProperties_IP_1, setupIntegrationProperties_IP_2));

        //then
        Assert.assertEquals(ipCodes.size(), 2, INVALID_IP_PROCESSES_AMOUNT);

        processIPCode1 = ipCodes.get(0);
        processIPCode2 = ipCodes.get(1);

        log.info("IP1 Code: " + processIPCode1);
        log.info("IP2 Code: " + processIPCode2);
    }

    @Test(priority = 11, description = "Complete 'Ready for Integration' Task", dependsOnMethods = {"setupIntegration"})
    @Description("Complete 'Ready for Integration' Task")
    public void completeRFITask() {
        // given
        TasksPageV2 tasksPage = TasksPageV2.goToTasksPage(driver, webDriverWait, BASIC_URL).clearFilters();

        // when
        tasksPage.completeTask(processNRPCode, READY_FOR_INTEGRATION_TASK);

        // then
        assertSystemMessage(TASK_COMPLETED_MESSAGE, SystemMessageContainer.MessageType.SUCCESS,
                String.format(INVALID_TASK_COMPLETE_MESSAGE_PATTERN, READY_FOR_INTEGRATION_TASK));
    }

    @Test(priority = 12, description = "Start 'Scope Definition' Task in First Integration Process", dependsOnMethods = {"completeRFITask"})
    @Description("Start 'Scope Definition' Task in First Integration Process")
    public void startSDTaskIP1() {
        // given
        TasksPageV2 tasksPage = TasksPageV2.goToTasksPage(driver, webDriverWait, BASIC_URL).clearFilters();

        // when
        tasksPage.startTask(processIPCode1, SCOPE_DEFINITION_TASK);

        // then
        assertSystemMessage(TASK_ASSIGNED_MESSAGE, SystemMessageContainer.MessageType.SUCCESS,
                String.format(INVALID_TASK_START_MESSAGE_PATTERN, SCOPE_DEFINITION_TASK));
    }

    @Test(priority = 13, description = "Complete 'Scope Definition' Task in First Integration Process", dependsOnMethods = {"startSDTaskIP1"})
    @Description("Complete 'Scope Definition' Task in First Integration Process")
    public void completeSDTaskIP1() {
        // given
        TasksPageV2 tasksPage = TasksPageV2.goToTasksPage(driver, webDriverWait, BASIC_URL).clearFilters();

        // when
        tasksPage.completeTask(processIPCode1, SCOPE_DEFINITION_TASK);

        // then
        assertSystemMessage(TASK_COMPLETED_MESSAGE, SystemMessageContainer.MessageType.SUCCESS,
                String.format(INVALID_TASK_COMPLETE_MESSAGE_PATTERN, SCOPE_DEFINITION_TASK));
    }

    @Test(priority = 14, description = "Start 'Implementation' Task in First Integration Process", dependsOnMethods = {"completeSDTaskIP1"})
    @Description("Start 'Implementation' Task in First Integration Process")
    public void startImplementationTaskIP1() {
        // given
        TasksPageV2 tasksPage = TasksPageV2.goToTasksPage(driver, webDriverWait, BASIC_URL).clearFilters();

        // when
        tasksPage.startTask(processIPCode1, IMPLEMENTATION_TASK);

        // then
        assertSystemMessage(TASK_ASSIGNED_MESSAGE, SystemMessageContainer.MessageType.SUCCESS,
                String.format(INVALID_TASK_START_MESSAGE_PATTERN, IMPLEMENTATION_TASK));
    }

    @Test(priority = 15, description = "Complete 'Implementation' Task in First Integration Process", dependsOnMethods = {"startImplementationTaskIP1"})
    @Description("Complete 'Implementation' Task in First Integration Process")
    public void completeImplementationTaskIP1() {
        // given
        TasksPageV2 tasksPage = TasksPageV2.goToTasksPage(driver, webDriverWait, BASIC_URL).clearFilters();

        // when
        tasksPage.completeTask(processIPCode1, IMPLEMENTATION_TASK);

        // then
        assertSystemMessage(TASK_COMPLETED_MESSAGE, SystemMessageContainer.MessageType.SUCCESS,
                String.format(INVALID_TASK_COMPLETE_MESSAGE_PATTERN, IMPLEMENTATION_TASK));
    }

    @Test(priority = 16, description = "Start 'Acceptance' Task in First Integration Process", dependsOnMethods = {"completeImplementationTaskIP1"})
    @Description("Start 'Acceptance' Task in First Integration Process")
    public void startAcceptanceTaskIP1() {
        // given
        TasksPageV2 tasksPage = TasksPageV2.goToTasksPage(driver, webDriverWait, BASIC_URL).clearFilters();

        // when
        tasksPage.startTask(processIPCode1, ACCEPTANCE_TASK);

        // then
        assertSystemMessage(TASK_ASSIGNED_MESSAGE, SystemMessageContainer.MessageType.SUCCESS,
                String.format(INVALID_TASK_START_MESSAGE_PATTERN, ACCEPTANCE_TASK));
    }

    @Test(priority = 17, description = "Complete 'Acceptance' Task in First Integration Process", dependsOnMethods = {"startAcceptanceTaskIP1"})
    @Description("Complete 'Acceptance' Task in First Integration Process")
    public void completeAcceptanceTaskIP1() {
        // given
        TasksPageV2 tasksPage = TasksPageV2.goToTasksPage(driver, webDriverWait, BASIC_URL).clearFilters();

        // when
        tasksPage.completeTask(processIPCode1, ACCEPTANCE_TASK);

        // then
        assertSystemMessage(TASK_COMPLETED_MESSAGE, SystemMessageContainer.MessageType.SUCCESS,
                String.format(INVALID_TASK_COMPLETE_MESSAGE_PATTERN, ACCEPTANCE_TASK));
    }

    @Test(priority = 18, description = "Start 'Scope Definition' Task in Second Integration Process", dependsOnMethods = {"completeAcceptanceTaskIP1"})
    @Description("Start 'Scope Definition' Task in Second Integration Process")
    public void startSDTaskIP2() {
        // given
        TasksPageV2 tasksPage = TasksPageV2.goToTasksPage(driver, webDriverWait, BASIC_URL).clearFilters();

        // when
        tasksPage.startTask(processIPCode2, SCOPE_DEFINITION_TASK);

        // then
        assertSystemMessage(TASK_ASSIGNED_MESSAGE, SystemMessageContainer.MessageType.SUCCESS,
                String.format(INVALID_TASK_START_MESSAGE_PATTERN, SCOPE_DEFINITION_TASK));
    }

    @Test(priority = 19, description = "Complete 'Scope Definition' Task in Second Integration Process", dependsOnMethods = {"startSDTaskIP2"})
    @Description("Complete 'Scope Definition' Task in Second Integration Process")
    public void completeSDTaskIP2() {
        // given
        TasksPageV2 tasksPage = TasksPageV2.goToTasksPage(driver, webDriverWait, BASIC_URL).clearFilters();

        // when
        tasksPage.completeTask(processIPCode2, SCOPE_DEFINITION_TASK);

        // then
        assertSystemMessage(TASK_COMPLETED_MESSAGE, SystemMessageContainer.MessageType.SUCCESS,
                String.format(INVALID_TASK_COMPLETE_MESSAGE_PATTERN, SCOPE_DEFINITION_TASK));
    }

    @Test(priority = 20, description = "Start 'Implementation' Task in Second Integration Process", dependsOnMethods = {"completeSDTaskIP2"})
    @Description("Start 'Implementation' Task in Second Integration Process")
    public void startImplementationTaskIP2() {
        // given
        TasksPageV2 tasksPage = TasksPageV2.goToTasksPage(driver, webDriverWait, BASIC_URL).clearFilters();

        // when
        tasksPage.startTask(processIPCode2, IMPLEMENTATION_TASK);

        // then
        assertSystemMessage(TASK_ASSIGNED_MESSAGE, SystemMessageContainer.MessageType.SUCCESS,
                String.format(INVALID_TASK_START_MESSAGE_PATTERN, IMPLEMENTATION_TASK));
    }

    @Test(priority = 21, description = "Complete 'Implementation' Task in Second Integration Process", dependsOnMethods = {"startImplementationTaskIP2"})
    @Description("Complete 'Implementation' Task in Second Integration Process")
    public void completeImplementationTaskIP2() {
        // given
        TasksPageV2 tasksPage = TasksPageV2.goToTasksPage(driver, webDriverWait, BASIC_URL).clearFilters();

        // when
        tasksPage.completeTask(processIPCode2, IMPLEMENTATION_TASK);

        // then
        assertSystemMessage(TASK_COMPLETED_MESSAGE, SystemMessageContainer.MessageType.SUCCESS,
                String.format(INVALID_TASK_COMPLETE_MESSAGE_PATTERN, IMPLEMENTATION_TASK));
    }

    @Test(priority = 22, description = "Start 'Acceptance' Task in Second Integration Process", dependsOnMethods = {"completeImplementationTaskIP2"})
    @Description("Start 'Acceptance' Task in Second Integration Process")
    public void startAcceptanceTaskIP2() {
        // given
        TasksPageV2 tasksPage = TasksPageV2.goToTasksPage(driver, webDriverWait, BASIC_URL).clearFilters();

        // when
        tasksPage.startTask(processIPCode2, ACCEPTANCE_TASK);

        // then
        assertSystemMessage(TASK_ASSIGNED_MESSAGE, SystemMessageContainer.MessageType.SUCCESS,
                String.format(INVALID_TASK_START_MESSAGE_PATTERN, ACCEPTANCE_TASK));
    }

    @Test(priority = 23, description = "Complete 'Acceptance' Task in Second Integration Process", dependsOnMethods = {"startAcceptanceTaskIP2"})
    @Description("Complete 'Acceptance' Task in Second Integration Process")
    public void completeAcceptanceTaskIP2() {
        // given
        TasksPageV2 tasksPage = TasksPageV2.goToTasksPage(driver, webDriverWait, BASIC_URL).clearFilters();

        // when
        tasksPage.completeTask(processIPCode2, ACCEPTANCE_TASK);

        // then
        assertSystemMessage(TASK_COMPLETED_MESSAGE, SystemMessageContainer.MessageType.SUCCESS,
                String.format(INVALID_TASK_COMPLETE_MESSAGE_PATTERN, ACCEPTANCE_TASK));
    }

    @Test(priority = 24, description = "Start 'Verification' Task in NRP", dependsOnMethods = {"completeAcceptanceTaskIP2"})
    @Description("Start 'Verification' Task in NRP")
    public void startVerificationTask() {
        // given
        TasksPageV2 tasksPage = TasksPageV2.goToTasksPage(driver, webDriverWait, BASIC_URL).clearFilters();

        // when
        tasksPage.startTask(processNRPCode, VERIFICATION_TASK);

        // then
        assertSystemMessage(TASK_ASSIGNED_MESSAGE, SystemMessageContainer.MessageType.SUCCESS,
                String.format(INVALID_TASK_START_MESSAGE_PATTERN, VERIFICATION_TASK));
    }

    @Test(priority = 25, description = "Complete 'Verification' Task", dependsOnMethods = {"startVerificationTask"})
    @Description("Complete 'Verification' Task")
    public void completeVerificationTask() {
        // given
        TasksPageV2 tasksPage = TasksPageV2.goToTasksPage(driver, webDriverWait, BASIC_URL).clearFilters();

        // when
        tasksPage.completeTask(processNRPCode, VERIFICATION_TASK);

        // then
        assertSystemMessage(TASK_COMPLETED_MESSAGE, SystemMessageContainer.MessageType.SUCCESS,
                String.format(INVALID_TASK_COMPLETE_MESSAGE_PATTERN, VERIFICATION_TASK));
    }

    @Test(priority = 26, description = "Check Process status", dependsOnMethods = {"completeVerificationTask"})
    @Description("Check Process status")
    public void checkProcessStatus() {
        // given
        ProcessOverviewPage processOverviewPage = ProcessOverviewPage.goToProcessOverviewPage(driver, BASIC_URL).clearAllColumnFilters();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        // when
        processOverviewPage.selectPredefinedFilter(SHOW_WITH_COMPLETED_FILTER);
        String processStatus = processOverviewPage.selectProcess(processNRPCode).getProcessStatus();

        // then
        Assert.assertEquals(processStatus, COMPLETED_STATUS, INVALID_NRP_PROCESS_STATUS);
    }

    @Test(priority = 27, description = "Checking system message summary")
    @Description("Checking system message summary")
    public void systemMessageSummary() {
        softAssert.assertAll();
    }

    @AfterClass()
    public void switchToLivePerspectiveAndClean() {
        PerspectiveChooser perspectiveChooser = PerspectiveChooser.create(driver, webDriverWait);
        if (!perspectiveChooser.getCurrentPerspective().equalsIgnoreCase(LIVE_PERSPECTIVE))
            perspectiveChooser.setLivePerspective();

        deleteIPDevice(device1Id, LIVE);
        deleteIPDevice(device2Id, LIVE);
        deleteBuilding(buildingId, LIVE);
    }

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    private void assertSystemMessage(String messageContent, SystemMessageContainer.MessageType messageType, String systemMessageLog) {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, new WebDriverWait(driver, Duration.ofSeconds(30)));
        Optional<SystemMessageContainer.Message> messageOptional = systemMessage.getFirstMessage();
        softAssert.assertTrue(messageOptional.isPresent(), systemMessageLog);
        messageOptional.ifPresent(message -> {
            softAssert.assertEquals(message.getText(), messageContent, systemMessageLog);
            softAssert.assertEquals(message.getMessageType(), messageType, systemMessageLog);
            systemMessage.close();
        });
        waitForPageToLoad();
    }

}

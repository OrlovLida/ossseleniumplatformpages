package com.oss.bpm;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.alerts.SystemMessageInterface;
import com.oss.framework.components.mainheader.Notifications;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.bpm.forecasts.Forecast;
import com.oss.pages.bpm.milestones.Milestone;
import com.oss.pages.bpm.processinstances.ProcessOverviewPage;
import com.oss.pages.bpm.processinstances.creation.ProcessCreationWizardProperties;
import com.oss.pages.bpm.processinstances.creation.ScheduleProperties;
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
import org.testng.collections.Lists;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.oss.bpm.BpmPhysicalDataCreator.nextMaxInt;
import static com.oss.pages.bpm.processinstances.ProcessOverviewPage.NAME_LABEL;


@Listeners({TestListener.class})
public class ComboProcessProgramTest extends BaseTestCase {
    private static final String TC1 = "createProcessWithScheduleAndRoles";
    private static final String TC2 = "createComboProgramWithComboProcesses";
    private static final String TC3 = "createComboProcessLinkedToPrograms";
    private static final String TC4 = "createMultipleComboProcessesLinkedToPrograms";
    private static final String PROCESS_DEFINITION_NAME_ROLES = "audit_roles";
    private static final String PROGRAM_DEFINITION_NAME_ROLES = "program_roles";
    private static final String PROGRAM_DEFINITION_NAME = "audit_program";
    private static final String ROLE_ID1 = "Audit Role 1";
    private static final String ROLE_ID2 = "Audit Role 2";
    private static final String PLANNING = "Planning";
    private static final String GENERAL_ENGINEERING = "General Engineering";
    private static final String PROCESS_SCHEDULED_MESSAGE = "Process %s was scheduled";
    private static final String PROCESSES_CREATED_MESSAGE = "Processes from definition %s are being created...";
    private static final String PROCESSES_LINKED_TO_PROGRAM_NOTIFICATION =
            "%1$d out of %1$d processes from definition %2$s were created and linked with Program %3$s (%4$s). " +
                    "Refresh table with processes to see them.";
    private static final String PROCESSES_LINKED_TO_PROGRAMS_NOTIFICATION =
            "%1$d out of %1$d processes from definition %2$s were created and linked with selected Program(s). " +
                    "Refresh table with processes to see them.";
    private static final String MILESTONE_DESCRIPTION_1 = "Milestone 1 - Selenium Test";
    private static final String PROGRAM_ROLES_TASK_1 = "program_roles_1";
    private static final String AUDIT_ROLES_TASK_1 = "audit_roles_1";
    private static final String AUDIT_ROLES_TASK_2 = "audit_roles_2";
    private static final String STATUS_LABEL = "Status";
    private static final String NEW_STATUS = "New";
    private static final String NOT_NEEDED_STATUS = "Not Needed";
    private static final String START_DATE_LABEL = "Start date estimate";
    private static final String RELATION_ROLE_LABEL = "Relation Role";
    private static final String PARENT = "Parent";
    private static final String CHILD = "Child";
    private static final String PROCESS_WITH_PROGRAMS_CREATED_MESSAGE =
            "Process %1$s (%2$s) was created and linked with selected Program(s).";
    private static final String TERMINATE_REASON = "Selenium Termination Process After Tests.";
    private static final String INVALID_PROCESS_SCHEDULED_MESSAGE = "Invalid scheduled process creation system message.";
    private static final String INVALID_PROCESS_CREATION_MESSAGE = "Invalid process creation system message in %s test.";
    private static final String INVALID_BULK_PROCESSES_MESSAGE = "Invalid bulk processes creation system message in '%s' test.";
    private static final String INVALID_PROCESSES_LINKED_TO_PROGRAMS_NOTIFICATION = "Invalid bulk processes creation and link with programs notification in '%s' test.";
    private static final String INVALID_PROCESS_ROLE_ATTRIBUTE_PATTERN = "Invalid Process Role attribute for '%s' test.";
    private static final String INVALID_PROGRAM_ROLE_ATTRIBUTE_PATTERN = "Invalid Program Role attribute for '%s' test.";

    private static final String INVALID_PROCESS_MILESTONE_ATTRIBUTE_PATTERN = "Invalid '%1$s' Milestone attribute for Process in '%2$s' test.";
    private static final String INVALID_PROGRAM_MILESTONE_ATTRIBUTE_PATTERN = "Invalid '%1$s' Milestone attribute for Program in '%2$s' test.";

    private static final String INVALID_PROCESS_FORECAST_ATTRIBUTE_PATTERN = "Invalid '%1$s' Forecast attribute for Process in '%2$s' test.";
    private static final String INVALID_PROGRAM_FORECAST_ATTRIBUTE_PATTERN = "Invalid '%1$s' Forecast attribute for Program in '%2$s' test.";

    private static final String INVALID_PROCESS_RELATED_PROCESSES_PATTERN = "Invalid Related Processes attributes for Process in '%s' test.";
    private static final String INVALID_PROGRAM_RELATED_PROCESSES_PATTERN = "Invalid Related Processes attributes for Program in '%s' test.";

    private final Logger LOGGER = LoggerFactory.getLogger(ComboProcessProgramTest.class);
    private SoftAssert softAssert;
    private final String processNameTC1 = "Combo Process/Program Test Process TC1." + nextMaxInt();
    private final String programNameTC3 = "Combo Process/Program Test Program TC3." + nextMaxInt();
    private final String programNameTC4 = "Combo Process/Program Test Program TC4." + nextMaxInt();
    private final String mainProgramName = "Combo Process/Program Test Main Program " + nextMaxInt();
    private final long plus5Days = 5L;
    private final long plus10Days = 10L;
    private final int plusMinutes = 2;
    private final String WAITING_INFO = String.format("Waiting %d minutes for process creation...", plusMinutes);
    private final int processesAmount = 5;
    private final Multimap<String, String> processRoles = ImmutableMultimap.<String, String>builder()
            .put(ROLE_ID1, PLANNING)
            .put(ROLE_ID1, GENERAL_ENGINEERING)
            .put(ROLE_ID2, PLANNING)
            .build();
    private final Forecast mainForecast = Forecast.builder()
            .startPlusDays(0L)
            .endPlusDaysShortWay(plus5Days)
            .endPlusDaysLongWay(plus10Days)
            .longWorkWeakLongWay(true)
            .longWorkWeakShortWay(false)
            .build();

    private final List<Forecast> processForecasts = Lists.newArrayList(
            Forecast.builder()
                    .name(AUDIT_ROLES_TASK_1)
                    .startPlusDays(0L)
                    .endPlusDaysShortWay(plus5Days)
                    .longWorkWeakShortWay(true)
                    .longWorkWeakLongWay(false)
                    .build(),

            Forecast.builder()
                    .name(AUDIT_ROLES_TASK_2)
                    .startPlusDays(0L)
                    .endPlusDaysLongWay(plus10Days)
                    .longWorkWeakLongWay(true)
                    .longWorkWeakShortWay(false)
                    .build());

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

    private void assertNotification(String notificationMessage, Notifications.NotificationType notificationType, String notificationLog) {
        Notifications notifications = (Notifications) Notifications.create(driver, webDriverWait);
        Optional<Notifications.Notification> notificationOptional = notifications.getFirstNotification();
        softAssert.assertTrue(notificationOptional.isPresent(), notificationLog);
        notificationOptional.ifPresent(notification -> {
            softAssert.assertEquals(notification.getText(), notificationMessage, notificationLog);
            softAssert.assertEquals(notification.getType(), notificationType, notificationLog);
            notification.close();
        });
        notifications.closeNotificationContainer();
        waitForPageToLoad();
    }

    @BeforeClass
    public void openProcessInstancesPage() {
        softAssert = new SoftAssert();
        ProcessOverviewPage.goToProcessOverviewPage(driver, BASIC_URL).clearAllColumnFilters();
    }

    @Test(priority = 1, description = "Create Process with schedule and process roles")
    @Description("Create Process with schedule and process roles")
    public void createProcessWithScheduleAndRoles() {
        ProcessOverviewPage processOverviewPage = ProcessOverviewPage.goToProcessOverviewPage(driver, BASIC_URL);

        final ScheduleProperties scheduleProperties = ScheduleProperties.builder()
                .setSingleSchedule(0L, plusMinutes).build();

        final ProcessCreationWizardProperties processCreationWizardProperties = ProcessCreationWizardProperties.builder()
                .basicProcess(processNameTC1, PROCESS_DEFINITION_NAME_ROLES, plus5Days)
                .withSchedule(scheduleProperties)
                .withProcessRolesAssignment(processRoles)
                .build();

        processOverviewPage.createInstance(processCreationWizardProperties);

        assertSystemMessage(String.format(PROCESS_SCHEDULED_MESSAGE, processNameTC1),
                SystemMessageContainer.MessageType.SUCCESS, INVALID_PROCESS_SCHEDULED_MESSAGE);


        //wait for process creation
        LOGGER.info(WAITING_INFO);
        DelayUtils.sleep((int) TimeUnit.MINUTES.toMillis(plusMinutes));
        processOverviewPage.reloadTable();
        waitForPageToLoad();

        //Assert process roles
        processOverviewPage.selectProcess(NAME_LABEL, processNameTC1).openProcessRolesTab();
        waitForPageToLoad();
        String processRoleGroups1 = processOverviewPage.getProcessRoleGroups(ROLE_ID1);
        String processRoleGroups2 = processOverviewPage.getProcessRoleGroups(ROLE_ID2);
        Assert.assertTrue(processRoleGroups1.contains(PLANNING),
                String.format(INVALID_PROCESS_ROLE_ATTRIBUTE_PATTERN, TC1));
        Assert.assertTrue(processRoleGroups1.contains(GENERAL_ENGINEERING),
                String.format(INVALID_PROCESS_ROLE_ATTRIBUTE_PATTERN, TC1));
        Assert.assertEquals(processRoleGroups2, PLANNING,
                String.format(INVALID_PROCESS_ROLE_ATTRIBUTE_PATTERN, TC1));
    }

    @Test(priority = 2, description = "Create Program and Processes with roles, milestones, forecasts")
    @Description("Create Program and Processes with roles, milestones, forecasts")
    public void createComboProgramWithComboProcesses() {
        String processName = "Combo Process/Program Test Process TC2." + nextMaxInt();
        final String programMilestoneName1 = "Combo Test Program TC2.1." + nextMaxInt();
        final String programMilestoneName2 = "Combo Test Program TC2.2." + nextMaxInt();
        final String processMilestoneName1 = "Combo Test Process TC2.1." + nextMaxInt();
        final String processMilestoneName2 = "Combo Test Process TC2.2." + nextMaxInt();
        ProcessOverviewPage processOverviewPage = ProcessOverviewPage.goToProcessOverviewPage(driver, BASIC_URL);

        final List<Milestone> programMilestones = Lists.newArrayList(
                Milestone.builder()
                        .setLeadTime("10")
                        .setIsActive("true")
                        .setDescription(MILESTONE_DESCRIPTION_1)
                        .setName(programMilestoneName1)
                        .build(),

                Milestone.builder()
                        .setName(programMilestoneName2)
                        .setRelatedTask(PROGRAM_ROLES_TASK_1)
                        .setIsManualCompletion("true")
                        .build());


        final List<Milestone> processMilestones = Lists.newArrayList(
                Milestone.builder()
                        .setLeadTime("5")
                        .setIsManualCompletion("true")
                        .setDescription(MILESTONE_DESCRIPTION_1)
                        .setName(processMilestoneName1)
                        .build(),

                Milestone.builder()
                        .setName(processMilestoneName2)
                        .setIsActive("true")
                        .setRelatedTask(AUDIT_ROLES_TASK_1)
                        .build());

        final ProcessCreationWizardProperties properties = ProcessCreationWizardProperties.builder()
                .basicProgram(mainProgramName, PROGRAM_DEFINITION_NAME_ROLES, plus5Days)
                .withProgramRolesAssignment(processRoles)
                .withProgramMilestones(programMilestones)
                .withProgramForecast(mainForecast)
                .withProcessCreation(processName, PROCESS_DEFINITION_NAME_ROLES, plus5Days)
                .withProcessForecasts(mainForecast, processForecasts)
                .withMultipleProcesses(processesAmount)
                .withProcessMilestones(processMilestones)
                .withProcessRolesAssignment(processRoles)
                .build();

        Notifications.create(driver, webDriverWait).clearAllNotification();

        processOverviewPage.createInstance(properties);

        //Assert message
        assertSystemMessage(String.format(PROCESSES_CREATED_MESSAGE, PROCESS_DEFINITION_NAME_ROLES),
                SystemMessageContainer.MessageType.SUCCESS, String.format(INVALID_BULK_PROCESSES_MESSAGE, TC2));

        String programCode = processOverviewPage.getProcessCode(mainProgramName);

        //Assert notification
        waitForPageToLoad();
        assertNotification(String.format(PROCESSES_LINKED_TO_PROGRAM_NOTIFICATION, 5,
                        PROCESS_DEFINITION_NAME_ROLES, mainProgramName, programCode),
                Notifications.NotificationType.SUCCESS,
                String.format(INVALID_PROCESSES_LINKED_TO_PROGRAMS_NOTIFICATION, TC2));

        //Assert program roles
        processOverviewPage.selectProcess(NAME_LABEL, mainProgramName).openProcessRolesTab();
        String programRoleGroups1 = processOverviewPage.getProcessRoleGroups(ROLE_ID1);
        String programRoleGroups2 = processOverviewPage.getProcessRoleGroups(ROLE_ID2);
        Assert.assertTrue(programRoleGroups1.contains(PLANNING),
                String.format(INVALID_PROGRAM_ROLE_ATTRIBUTE_PATTERN, TC2));
        Assert.assertTrue(programRoleGroups1.contains(GENERAL_ENGINEERING),
                String.format(INVALID_PROGRAM_ROLE_ATTRIBUTE_PATTERN, TC2));
        Assert.assertEquals(programRoleGroups2, PLANNING,
                String.format(INVALID_PROGRAM_ROLE_ATTRIBUTE_PATTERN, TC2));

        //Assert program milestones (Status)
        processOverviewPage.openMilestoneTab();
        String programMilestone1Status = processOverviewPage.getMilestoneValue(programMilestoneName1, STATUS_LABEL);
        String programMilestone2Status = processOverviewPage.getMilestoneValue(programMilestoneName2, STATUS_LABEL);
        Assert.assertEquals(programMilestone1Status, NEW_STATUS,
                String.format(INVALID_PROGRAM_MILESTONE_ATTRIBUTE_PATTERN, STATUS_LABEL, TC2));
        Assert.assertEquals(programMilestone2Status, NOT_NEEDED_STATUS,
                String.format(INVALID_PROGRAM_MILESTONE_ATTRIBUTE_PATTERN, STATUS_LABEL, TC2));

        //Assert program forecast (start date)
        processOverviewPage.openForecastsTab();
        String programMainForecastStartDate = processOverviewPage.getForecastValue(PROGRAM_DEFINITION_NAME_ROLES, START_DATE_LABEL);
        mainForecast.getStartPlusDays().ifPresent(startDatePlusDays ->
                Assert.assertEquals(programMainForecastStartDate, LocalDate.now().plusDays(startDatePlusDays).toString(),
                        String.format(INVALID_PROGRAM_FORECAST_ATTRIBUTE_PATTERN, START_DATE_LABEL, TC2)));

        processName = processName + "-1";
        //Assert program Related Processes
        processOverviewPage.openRelatedProcessesTab();
        Assert.assertEquals(processOverviewPage.getRelatedProcessesAmount(), processesAmount,
                String.format(INVALID_PROGRAM_RELATED_PROCESSES_PATTERN, TC2));
        String relatedProcessRelationRole = processOverviewPage.getRelatedProcessValue(processName, RELATION_ROLE_LABEL);
        Assert.assertEquals(relatedProcessRelationRole, CHILD,
                String.format(INVALID_PROGRAM_RELATED_PROCESSES_PATTERN, TC2));

        //Assert process roles
        processOverviewPage.selectProcess(NAME_LABEL, processName).openProcessRolesTab();
        String processRoleGroups1 = processOverviewPage.getProcessRoleGroups(ROLE_ID1);
        String processRoleGroups2 = processOverviewPage.getProcessRoleGroups(ROLE_ID2);
        Assert.assertTrue(processRoleGroups1.contains(PLANNING),
                String.format(INVALID_PROCESS_ROLE_ATTRIBUTE_PATTERN, TC2));
        Assert.assertTrue(processRoleGroups1.contains(GENERAL_ENGINEERING),
                String.format(INVALID_PROCESS_ROLE_ATTRIBUTE_PATTERN, TC2));
        Assert.assertEquals(processRoleGroups2, PLANNING,
                String.format(INVALID_PROCESS_ROLE_ATTRIBUTE_PATTERN, TC2));

        //Assert process milestones (Status)
        processOverviewPage.openMilestoneTab();
        String processMilestone1Status = processOverviewPage.getMilestoneValue(processMilestoneName1, STATUS_LABEL);
        String processMilestone2Status = processOverviewPage.getMilestoneValue(processMilestoneName2, STATUS_LABEL);
        Assert.assertEquals(processMilestone1Status, NOT_NEEDED_STATUS,
                String.format(INVALID_PROCESS_MILESTONE_ATTRIBUTE_PATTERN, STATUS_LABEL, TC2));
        Assert.assertEquals(processMilestone2Status, NEW_STATUS,
                String.format(INVALID_PROCESS_MILESTONE_ATTRIBUTE_PATTERN, STATUS_LABEL, TC2));

        //Assert process forecasts (start date)
        processOverviewPage.openForecastsTab();
        String processMainForecastStartDate = processOverviewPage.getForecastValue(PROCESS_DEFINITION_NAME_ROLES, START_DATE_LABEL);
        String processForecast1StartDate = processOverviewPage.getForecastValue(AUDIT_ROLES_TASK_1, START_DATE_LABEL);
        String processForecast2StartDate = processOverviewPage.getForecastValue(AUDIT_ROLES_TASK_2, START_DATE_LABEL);
        mainForecast.getStartPlusDays().ifPresent(startDatePlusDays ->
                Assert.assertEquals(processMainForecastStartDate, LocalDate.now().plusDays(startDatePlusDays).toString(),
                        String.format(INVALID_PROCESS_FORECAST_ATTRIBUTE_PATTERN, START_DATE_LABEL, TC2)));
        processForecasts.get(0).getStartPlusDays().ifPresent(startDatePlusDays ->
                Assert.assertEquals(processForecast1StartDate, LocalDate.now().plusDays(startDatePlusDays).toString(),
                        String.format(INVALID_PROCESS_FORECAST_ATTRIBUTE_PATTERN, START_DATE_LABEL, TC2)));
        processForecasts.get(1).getStartPlusDays().ifPresent(startDatePlusDays ->
                Assert.assertEquals(processForecast2StartDate, LocalDate.now().plusDays(startDatePlusDays).toString(),
                        String.format(INVALID_PROCESS_FORECAST_ATTRIBUTE_PATTERN, START_DATE_LABEL, TC2)));

        //Assert process Related Processes
        processOverviewPage.openRelatedProcessesTab();
        Assert.assertEquals(processOverviewPage.getRelatedProcessesAmount(), 1,
                String.format(INVALID_PROCESS_RELATED_PROCESSES_PATTERN, TC2));
        String relatedProgramRelationRole = processOverviewPage.getRelatedProcessValue(mainProgramName, RELATION_ROLE_LABEL);
        Assert.assertEquals(relatedProgramRelationRole, PARENT,
                String.format(INVALID_PROCESS_RELATED_PROCESSES_PATTERN, TC2));
    }

    @Test(priority = 3, description = "Create Process with roles, milestones, forecasts linked to Programs",
            dependsOnMethods = {TC2})
    @Description("Create Process with roles, milestones, forecasts linked to Programs")
    public void createComboProcessLinkedToPrograms() {
        String processName = "Combo Process/Program Test Process TC3." + nextMaxInt();
        final String processMilestoneName1 = "Combo Test Process TC3.1." + nextMaxInt();
        final String processMilestoneName2 = "Combo Test Process TC3.2." + nextMaxInt();
        ProcessOverviewPage processOverviewPage = ProcessOverviewPage.goToProcessOverviewPage(driver, BASIC_URL);

        final List<Milestone> processMilestones = Lists.newArrayList(
                Milestone.builder()
                        .setLeadTime("5")
                        .setIsManualCompletion("true")
                        .setDescription(MILESTONE_DESCRIPTION_1)
                        .setName(processMilestoneName1)
                        .build(),

                Milestone.builder()
                        .setName(processMilestoneName2)
                        .setIsActive("true")
                        .setRelatedTask(AUDIT_ROLES_TASK_1)
                        .build());

        ProcessCreationWizardProperties properties = ProcessCreationWizardProperties.builder()
                .basicProcess(processName, PROCESS_DEFINITION_NAME_ROLES, plus5Days)
                .withProcessMilestones(processMilestones)
                .withProcessForecasts(mainForecast, processForecasts)
                .withProcessRolesAssignment(processRoles)
                .withProgramsToLink(Lists.newArrayList(mainProgramName, programNameTC3))
                .build();

        String programCode1 = processOverviewPage.openProgramCreationWizard()
                .createProgram(programNameTC3, plus5Days, PROGRAM_DEFINITION_NAME);

        SystemMessageContainer.create(driver, webDriverWait).close();

        processOverviewPage.createInstance(properties);

        //Assert message
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, new WebDriverWait(driver, Duration.ofSeconds(30)));
        Optional<SystemMessageContainer.Message> messageOptional = systemMessage.getFirstMessage();
        softAssert.assertTrue(messageOptional.isPresent(),
                String.format(INVALID_PROCESS_CREATION_MESSAGE, TC3));
        systemMessage.close();
        String processCode = processOverviewPage.getProcessCode(processName);
        messageOptional.ifPresent(message -> {
            softAssert.assertEquals(message.getText(), String.format(PROCESS_WITH_PROGRAMS_CREATED_MESSAGE, processName, processCode),
                    String.format(INVALID_PROCESS_CREATION_MESSAGE, TC3));
            softAssert.assertEquals(message.getMessageType(), SystemMessageContainer.MessageType.SUCCESS,
                    String.format(INVALID_PROCESS_CREATION_MESSAGE, TC3));
        });
        waitForPageToLoad();

        //Assert process roles
        processOverviewPage.selectProcess(NAME_LABEL, processName).openProcessRolesTab();
        String processRoleGroups1 = processOverviewPage.getProcessRoleGroups(ROLE_ID1);
        String processRoleGroups2 = processOverviewPage.getProcessRoleGroups(ROLE_ID2);
        Assert.assertTrue(processRoleGroups1.contains(PLANNING),
                String.format(INVALID_PROCESS_ROLE_ATTRIBUTE_PATTERN, TC3));
        Assert.assertTrue(processRoleGroups1.contains(GENERAL_ENGINEERING),
                String.format(INVALID_PROCESS_ROLE_ATTRIBUTE_PATTERN, TC3));
        Assert.assertEquals(processRoleGroups2, PLANNING,
                String.format(INVALID_PROCESS_ROLE_ATTRIBUTE_PATTERN, TC3));

        //Assert process milestones (Status)
        processOverviewPage.openMilestoneTab();
        String processMilestone1Status = processOverviewPage.getMilestoneValue(processMilestoneName1, STATUS_LABEL);
        String processMilestone2Status = processOverviewPage.getMilestoneValue(processMilestoneName2, STATUS_LABEL);
        Assert.assertEquals(processMilestone1Status, NOT_NEEDED_STATUS,
                String.format(INVALID_PROCESS_MILESTONE_ATTRIBUTE_PATTERN, STATUS_LABEL, TC3));
        Assert.assertEquals(processMilestone2Status, NEW_STATUS,
                String.format(INVALID_PROCESS_MILESTONE_ATTRIBUTE_PATTERN, STATUS_LABEL, TC3));

        //Assert process forecast (start date)
        processOverviewPage.openForecastsTab();
        String processMainForecastStartDate = processOverviewPage.getForecastValue(PROCESS_DEFINITION_NAME_ROLES, START_DATE_LABEL);
        String processForecast1StartDate = processOverviewPage.getForecastValue(AUDIT_ROLES_TASK_1, START_DATE_LABEL);
        String processForecast2StartDate = processOverviewPage.getForecastValue(AUDIT_ROLES_TASK_2, START_DATE_LABEL);
        mainForecast.getStartPlusDays().ifPresent(startDatePlusDays ->
                Assert.assertEquals(processMainForecastStartDate, LocalDate.now().plusDays(startDatePlusDays).toString(),
                        String.format(INVALID_PROCESS_FORECAST_ATTRIBUTE_PATTERN, START_DATE_LABEL, TC3)));
        processForecasts.get(0).getStartPlusDays().ifPresent(startDatePlusDays ->
                Assert.assertEquals(processForecast1StartDate, LocalDate.now().plusDays(startDatePlusDays).toString(),
                        String.format(INVALID_PROCESS_FORECAST_ATTRIBUTE_PATTERN, START_DATE_LABEL, TC3)));
        processForecasts.get(1).getStartPlusDays().ifPresent(startDatePlusDays ->
                Assert.assertEquals(processForecast2StartDate, LocalDate.now().plusDays(startDatePlusDays).toString(),
                        String.format(INVALID_PROCESS_FORECAST_ATTRIBUTE_PATTERN, START_DATE_LABEL, TC3)));

        //Assert process Related Processes
        processOverviewPage.openRelatedProcessesTab();
        Assert.assertEquals(processOverviewPage.getRelatedProcessesAmount(), 2,
                String.format(INVALID_PROCESS_RELATED_PROCESSES_PATTERN, TC3));
        String relatedProgramRelationRole = processOverviewPage.getRelatedProcessValue(mainProgramName, RELATION_ROLE_LABEL);
        String relatedProgramRelationRole1 = processOverviewPage.getRelatedProcessValue(programNameTC3, RELATION_ROLE_LABEL);
        Assert.assertEquals(relatedProgramRelationRole, PARENT,
                String.format(INVALID_PROCESS_RELATED_PROCESSES_PATTERN, TC3));
        Assert.assertEquals(relatedProgramRelationRole1, PARENT,
                String.format(INVALID_PROCESS_RELATED_PROCESSES_PATTERN, TC3));

    }

    @Test(priority = 4, description = "Create  Multiple Processes with roles, milestones, forecasts linked to Programs",
            dependsOnMethods = {TC2})
    @Description("Create Multiple Process with roles, milestones, forecasts linked to Programs")
    public void createMultipleComboProcessesLinkedToPrograms() {
        String processName = "Combo Process/Program Test Process TC4." + nextMaxInt();
        final String processMilestoneName1 = "Combo Test Process TC4.1." + nextMaxInt();
        final String processMilestoneName2 = "Combo Test Process TC4.2." + nextMaxInt();
        ProcessOverviewPage processOverviewPage = ProcessOverviewPage.goToProcessOverviewPage(driver, BASIC_URL);

        final List<Milestone> processMilestones = Lists.newArrayList(
                Milestone.builder()
                        .setLeadTime("5")
                        .setIsManualCompletion("true")
                        .setDescription(MILESTONE_DESCRIPTION_1)
                        .setName(processMilestoneName1)
                        .build(),

                Milestone.builder()
                        .setName(processMilestoneName2)
                        .setIsActive("true")
                        .setRelatedTask(AUDIT_ROLES_TASK_1)
                        .build());

        ProcessCreationWizardProperties properties = ProcessCreationWizardProperties.builder()
                .basicProcess(processName, PROCESS_DEFINITION_NAME_ROLES, plus5Days)
                .withProcessMilestones(processMilestones)
                .withProcessForecasts(mainForecast, processForecasts)
                .withProcessRolesAssignment(processRoles)
                .withProgramsToLink(Lists.newArrayList(mainProgramName, programNameTC4))
                .withMultipleProcesses(processesAmount)
                .build();

        String programCode1 = processOverviewPage.openProgramCreationWizard()
                .createProgram(programNameTC4, plus5Days, PROGRAM_DEFINITION_NAME);

        SystemMessageContainer.create(driver, webDriverWait).close();
        Notifications.create(driver, webDriverWait).clearAllNotification();

        processOverviewPage.createInstance(properties);
        processName = processName + "-4";

        //Assert message
        assertSystemMessage(String.format(PROCESSES_CREATED_MESSAGE, PROCESS_DEFINITION_NAME_ROLES),
                SystemMessageContainer.MessageType.SUCCESS,
                String.format(INVALID_BULK_PROCESSES_MESSAGE, TC4));

        //Assert notification
        assertNotification(String.format(PROCESSES_LINKED_TO_PROGRAMS_NOTIFICATION, 5,
                        PROCESS_DEFINITION_NAME_ROLES), Notifications.NotificationType.SUCCESS,
                String.format(INVALID_PROCESSES_LINKED_TO_PROGRAMS_NOTIFICATION, TC4));

        //Assert process roles
        processOverviewPage.selectProcess(NAME_LABEL, processName).openProcessRolesTab();
        String processRoleGroups1 = processOverviewPage.getProcessRoleGroups(ROLE_ID1);
        String processRoleGroups2 = processOverviewPage.getProcessRoleGroups(ROLE_ID2);
        Assert.assertTrue(processRoleGroups1.contains(PLANNING),
                String.format(INVALID_PROCESS_ROLE_ATTRIBUTE_PATTERN, TC4));
        Assert.assertTrue(processRoleGroups1.contains(GENERAL_ENGINEERING),
                String.format(INVALID_PROCESS_ROLE_ATTRIBUTE_PATTERN, TC4));
        Assert.assertEquals(processRoleGroups2, PLANNING,
                String.format(INVALID_PROCESS_ROLE_ATTRIBUTE_PATTERN, TC4));

        //Assert process milestones (Status)
        processOverviewPage.openMilestoneTab();
        String processMilestone1Status = processOverviewPage.getMilestoneValue(processMilestoneName1, STATUS_LABEL);
        String processMilestone2Status = processOverviewPage.getMilestoneValue(processMilestoneName2, STATUS_LABEL);
        Assert.assertEquals(processMilestone1Status, NOT_NEEDED_STATUS,
                String.format(INVALID_PROCESS_MILESTONE_ATTRIBUTE_PATTERN, STATUS_LABEL, TC4));
        Assert.assertEquals(processMilestone2Status, NEW_STATUS,
                String.format(INVALID_PROCESS_MILESTONE_ATTRIBUTE_PATTERN, STATUS_LABEL, TC4));

        //Assert process forecast (start date)
        processOverviewPage.openForecastsTab();
        String processMainForecastStartDate = processOverviewPage.getForecastValue(PROCESS_DEFINITION_NAME_ROLES, START_DATE_LABEL);
        String processForecast1StartDate = processOverviewPage.getForecastValue(AUDIT_ROLES_TASK_1, START_DATE_LABEL);
        String processForecast2StartDate = processOverviewPage.getForecastValue(AUDIT_ROLES_TASK_2, START_DATE_LABEL);
        mainForecast.getStartPlusDays().ifPresent(startDatePlusDays ->
                Assert.assertEquals(processMainForecastStartDate, LocalDate.now().plusDays(startDatePlusDays).toString(),
                        String.format(INVALID_PROCESS_FORECAST_ATTRIBUTE_PATTERN, START_DATE_LABEL, TC4)));
        processForecasts.get(0).getStartPlusDays().ifPresent(startDatePlusDays ->
                Assert.assertEquals(processForecast1StartDate, LocalDate.now().plusDays(startDatePlusDays).toString(),
                        String.format(INVALID_PROCESS_FORECAST_ATTRIBUTE_PATTERN, START_DATE_LABEL, TC4)));
        processForecasts.get(1).getStartPlusDays().ifPresent(startDatePlusDays ->
                Assert.assertEquals(processForecast2StartDate, LocalDate.now().plusDays(startDatePlusDays).toString(),
                        String.format(INVALID_PROCESS_FORECAST_ATTRIBUTE_PATTERN, START_DATE_LABEL, TC4)));

        //Assert process Related Processes
        processOverviewPage.openRelatedProcessesTab();
        Assert.assertEquals(processOverviewPage.getRelatedProcessesAmount(), 2,
                String.format(INVALID_PROCESS_RELATED_PROCESSES_PATTERN, TC4));
        String relatedProgramRelationRole = processOverviewPage.getRelatedProcessValue(mainProgramName, RELATION_ROLE_LABEL);
        String relatedProgramRelationRole1 = processOverviewPage.getRelatedProcessValue(programNameTC4, RELATION_ROLE_LABEL);
        Assert.assertEquals(relatedProgramRelationRole, PARENT,
                String.format(INVALID_PROCESS_RELATED_PROCESSES_PATTERN, TC4));
        Assert.assertEquals(relatedProgramRelationRole1, PARENT,
                String.format(INVALID_PROCESS_RELATED_PROCESSES_PATTERN, TC4));
    }

    @Test(priority = 5, description = "Checking system messages and notifications summary")
    @Description("Checking system messages and notifications summary")
    public void systemMessagesAndNotificationsSummary() {
        softAssert.assertAll();
    }

    @AfterClass
    public void terminateInstances() {
        ProcessOverviewPage processOverviewPage = ProcessOverviewPage.goToProcessOverviewPage(driver, BASIC_URL).clearAllColumnFilters();
        processOverviewPage.selectProcess(ProcessOverviewPage.NAME_LABEL, processNameTC1)
                .terminateProcess(TERMINATE_REASON);
        processOverviewPage.selectProcess(ProcessOverviewPage.NAME_LABEL, programNameTC3)
                .terminateProcess(TERMINATE_REASON, true);
        DelayUtils.sleep(2000);
        processOverviewPage.selectProcess(ProcessOverviewPage.NAME_LABEL, programNameTC4)
                .terminateProcess(TERMINATE_REASON, true);
        DelayUtils.sleep(2000);
        processOverviewPage.selectProcess(ProcessOverviewPage.NAME_LABEL, mainProgramName)
                .terminateProcess(TERMINATE_REASON, true);
    }

}

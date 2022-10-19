package com.oss.bpm;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.alerts.SystemMessageInterface;
import com.oss.framework.components.mainheader.Notifications;
import com.oss.framework.components.mainheader.NotificationsInterface;
import com.oss.framework.components.mainheader.ToolbarWidget;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.bpm.ProcessOverviewPage;
import com.oss.pages.bpm.forecasts.Forecast;
import com.oss.pages.bpm.milestones.Milestone;
import com.oss.pages.bpm.processinstances.ProcessCreationWizardProperties;
import com.oss.pages.bpm.processinstances.ScheduleProperties;
import com.oss.utils.TestListener;
import io.qameta.allure.Description;
import org.assertj.core.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.collections.Lists;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.oss.pages.bpm.ProcessOverviewPage.NAME_LABEL;


@Listeners({TestListener.class})
public class ComboProcessProgramTest extends BaseTestCase {
    private static final String BPM_USER_LOGIN = "bpm_webselenium";
    private static final String BPM_USER_PASSWORD = "Webtests123!";
    private static final String BPM_ADMIN_USER_LOGIN = "bpm_admin_webselenium";
    private static final String BPM_ADMIN_USER_PASSWORD = "Webtests123!";
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
            "Process %1$s (%2$s) was created and linked with Program %3$s (%4$s)";
    private final Logger log = LoggerFactory.getLogger(ComboProcessProgramTest.class);
    private final String programName = "Selenium Test Program-" + (int) (Math.random() * 100001);
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
            .build();
    private String programCode;

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

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

    @Test(priority = 1, description = "Create Process with schedule and process roles")
    @Description("Create Process with schedule and process roles")
    public void createProcessWithScheduleAndRoles() {
        final String processName = "Selenium Test Process-" + (int) (Math.random() * 100001);
        ProcessOverviewPage processOverviewPage = ProcessOverviewPage.goToProcessOverviewPage(driver, BASIC_URL);

        final ScheduleProperties scheduleProperties = ScheduleProperties.builder()
                .setSingleSchedule(0L, plusMinutes).build();

        final ProcessCreationWizardProperties processCreationWizardProperties = ProcessCreationWizardProperties.builder()
                .basicProcess(processName, PROCESS_DEFINITION_NAME_ROLES, plus5Days)
                .withSchedule(scheduleProperties)
                .withProcessRolesAssignment(processRoles)
                .build();

        processOverviewPage.createInstance(processCreationWizardProperties);
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();

        Assertions.assertThat(messages).hasSize(1);
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assert.assertEquals(messages.get(0).getText(), String.format(PROCESS_SCHEDULED_MESSAGE, processName));
        systemMessage.close();

        //wait for process creation
        log.info(WAITING_INFO);
        DelayUtils.sleep((int) TimeUnit.MINUTES.toMillis(plusMinutes));
        processOverviewPage.reloadTable();
        waitForPageToLoad();

        //Assert process roles
        processOverviewPage.selectProcess(NAME_LABEL, processName).openProcessRolesTab();
        waitForPageToLoad();
        String processRoleGroups1 = processOverviewPage.getProcessRoleGroups(ROLE_ID1);
        String processRoleGroups2 = processOverviewPage.getProcessRoleGroups(ROLE_ID2);
        Assert.assertTrue(processRoleGroups1.contains(PLANNING));
        Assert.assertTrue(processRoleGroups1.contains(GENERAL_ENGINEERING));
        Assert.assertEquals(processRoleGroups2, PLANNING);
    }

    @Test(priority = 2, description = "Create Program and Processes with roles, milestones, forecasts")
    @Description("Create Program and Processes with roles, milestones, forecasts")
    public void createComboProgramWithComboProcess() {
        String processName = "Selenium Test Process-" + (int) (Math.random() * 100001);
        final String programMilestoneName1 = "Milestone Program 1." + (int) (Math.random() * 100001);
        final String programMilestoneName2 = "Milestone Program 2." + (int) (Math.random() * 100001);
        final String processMilestoneName1 = "Milestone Process 1." + (int) (Math.random() * 100001);
        final String processMilestoneName2 = "Milestone Process 2." + (int) (Math.random() * 100001);
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

        final List<Forecast> processForecasts = Lists.newArrayList(
                Forecast.builder()
                        .name(AUDIT_ROLES_TASK_1)
                        .startPlusDays(0L)
                        .endPlusDaysShortWay(plus5Days)
                        .longWorkWeakShortWay(true)
                        .build(),

                Forecast.builder()
                        .name(AUDIT_ROLES_TASK_2)
                        .startPlusDays(0L)
                        .endPlusDaysLongWay(plus10Days)
                        .longWorkWeakLongWay(true)
                        .build());

        final ProcessCreationWizardProperties properties = ProcessCreationWizardProperties.builder()
                .basicProgram(programName, PROGRAM_DEFINITION_NAME_ROLES, plus5Days)
                .withProgramRolesAssignment(processRoles)
                .withProgramMilestones(programMilestones)
                .withProgramForecast(mainForecast)
                .withProcessCreation(processName, PROCESS_DEFINITION_NAME_ROLES, plus5Days)
                .withProcessForecasts(mainForecast, processForecasts)
                .withMultipleProcesses(processesAmount)
                .withProcessMilestones(processMilestones)
                .withProcessRolesAssignment(processRoles)
                .build();

        processOverviewPage.createInstance(properties);

        //Assert message
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assertions.assertThat(messages).hasSize(1);
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assert.assertEquals(messages.get(0).getText(), String.format(PROCESSES_CREATED_MESSAGE, PROCESS_DEFINITION_NAME_ROLES));
        systemMessage.close();

        programCode = processOverviewPage.getProcessCode(programName);

        //Assert notification
        waitForPageToLoad();
        NotificationsInterface notifications = Notifications.create(driver, webDriverWait);
        String notification = notifications.getNotificationMessage();
        Assert.assertEquals(notification, String.format(PROCESSES_LINKED_TO_PROGRAM_NOTIFICATION, 5,
                PROCESS_DEFINITION_NAME_ROLES, programName, programCode));

        //Assert program roles
        processOverviewPage.selectProcess(NAME_LABEL, programName).openProcessRolesTab();
        String programRoleGroups1 = processOverviewPage.getProcessRoleGroups(ROLE_ID1);
        String programRoleGroups2 = processOverviewPage.getProcessRoleGroups(ROLE_ID2);
        Assert.assertTrue(programRoleGroups1.contains(PLANNING));
        Assert.assertTrue(programRoleGroups1.contains(GENERAL_ENGINEERING));
        Assert.assertEquals(programRoleGroups2, PLANNING);

        //Assert program milestones (Status)
        processOverviewPage.openMilestoneTab();
        String programMilestone1Status = processOverviewPage.getMilestoneValue(programMilestoneName1, STATUS_LABEL);
        String programMilestone2Status = processOverviewPage.getMilestoneValue(programMilestoneName2, STATUS_LABEL);
        Assert.assertEquals(programMilestone1Status, NEW_STATUS);
        Assert.assertEquals(programMilestone2Status, NOT_NEEDED_STATUS);

        //Assert program forecast (start date)
        processOverviewPage.openForecastsTab();
        String programMainForecastStartDate = processOverviewPage.getForecastValue(PROGRAM_DEFINITION_NAME_ROLES, START_DATE_LABEL);
        mainForecast.getStartPlusDays().ifPresent(startDatePlusDays ->
                Assert.assertEquals(programMainForecastStartDate, LocalDate.now().plusDays(startDatePlusDays).toString()));

        processName = processName + "-1";
        //Assert program Related Processes
        processOverviewPage.openRelatedProcessesTab();
        Assert.assertEquals(processOverviewPage.getRelatedProcessesAmount(), processesAmount);
        String relatedProcessRelationRole = processOverviewPage.getRelatedProcessValue(processName, RELATION_ROLE_LABEL);
        Assert.assertEquals(relatedProcessRelationRole, CHILD);

        //Assert process roles
        processOverviewPage.selectProcess(NAME_LABEL, processName).openProcessRolesTab();
        String processRoleGroups1 = processOverviewPage.getProcessRoleGroups(ROLE_ID1);
        String processRoleGroups2 = processOverviewPage.getProcessRoleGroups(ROLE_ID2);
        Assert.assertTrue(processRoleGroups1.contains(PLANNING));
        Assert.assertTrue(processRoleGroups1.contains(GENERAL_ENGINEERING));
        Assert.assertEquals(processRoleGroups2, PLANNING);

        //Assert process milestones (Status)
        processOverviewPage.openMilestoneTab();
        String processMilestone1Status = processOverviewPage.getMilestoneValue(processMilestoneName1, STATUS_LABEL);
        String processMilestone2Status = processOverviewPage.getMilestoneValue(processMilestoneName2, STATUS_LABEL);
        Assert.assertEquals(processMilestone1Status, NOT_NEEDED_STATUS);
        Assert.assertEquals(processMilestone2Status, NEW_STATUS);

        //Assert process forecasts (start date)
        processOverviewPage.openForecastsTab();
        String processMainForecastStartDate = processOverviewPage.getForecastValue(PROCESS_DEFINITION_NAME_ROLES, START_DATE_LABEL);
        String processForecast1StartDate = processOverviewPage.getForecastValue(AUDIT_ROLES_TASK_1, START_DATE_LABEL);
        String processForecast2StartDate = processOverviewPage.getForecastValue(AUDIT_ROLES_TASK_2, START_DATE_LABEL);
        mainForecast.getStartPlusDays().ifPresent(startDatePlusDays ->
                Assert.assertEquals(processMainForecastStartDate, LocalDate.now().plusDays(startDatePlusDays).toString()));
        processForecasts.get(0).getStartPlusDays().ifPresent(startDatePlusDays ->
                Assert.assertEquals(processForecast1StartDate, LocalDate.now().plusDays(startDatePlusDays).toString()));
        processForecasts.get(1).getStartPlusDays().ifPresent(startDatePlusDays ->
                Assert.assertEquals(processForecast2StartDate, LocalDate.now().plusDays(startDatePlusDays).toString()));

        //Assert process Related Processes
        processOverviewPage.openRelatedProcessesTab();
        Assert.assertEquals(processOverviewPage.getRelatedProcessesAmount(), 1);
        String relatedProgramRelationRole = processOverviewPage.getRelatedProcessValue(programName, RELATION_ROLE_LABEL);
        Assert.assertEquals(relatedProgramRelationRole, PARENT);
    }

    @Test(priority = 3, description = "Create Process with roles, milestones, forecasts linked to Programs",
            dependsOnMethods = {"createComboProgramWithComboProcess"})
    @Description("Create Process with roles, milestones, forecasts linked to Programs")
    public void createComboProcessLinkedToPrograms() {
        String processName = "Selenium Test Process-" + (int) (Math.random() * 100001);
        String programName1 = "Selenium Test Program-" + (int) (Math.random() * 100001);
        final String processMilestoneName1 = "Milestone Process 1." + (int) (Math.random() * 100001);
        final String processMilestoneName2 = "Milestone Process 2." + (int) (Math.random() * 100001);
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

        final List<Forecast> processForecasts = Lists.newArrayList(
                Forecast.builder()
                        .name(AUDIT_ROLES_TASK_1)
                        .startPlusDays(0L)
                        .endPlusDaysShortWay(plus5Days)
                        .longWorkWeakShortWay(true)
                        .build(),

                Forecast.builder()
                        .name(AUDIT_ROLES_TASK_2)
                        .startPlusDays(0L)
                        .endPlusDaysLongWay(plus10Days)
                        .longWorkWeakLongWay(true)
                        .build());

        ProcessCreationWizardProperties properties = ProcessCreationWizardProperties.builder()
                .basicProcess(processName, PROCESS_DEFINITION_NAME_ROLES, plus5Days)
                .withProcessMilestones(processMilestones)
                .withProcessForecasts(mainForecast, processForecasts)
                .withProcessRolesAssignment(processRoles)
                .withProgramsToLink(Lists.newArrayList(programName, programName1))
                .build();

        String programCode1 = processOverviewPage.openProgramCreationWizard()
                .createProgram(programName1, plus5Days, PROGRAM_DEFINITION_NAME);

        processOverviewPage.createInstance(properties);

        //Assert message
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assertions.assertThat(messages).hasSize(1);
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        systemMessage.close();
        String processCode = processOverviewPage.getProcessCode(processName);
        Assert.assertEquals(messages.get(0).getText(),
                String.format(PROCESS_WITH_PROGRAMS_CREATED_MESSAGE, processName, processCode, programName, programCode));

        //Assert process roles
        processOverviewPage.selectProcess(NAME_LABEL, processName).openProcessRolesTab();
        String processRoleGroups1 = processOverviewPage.getProcessRoleGroups(ROLE_ID1);
        String processRoleGroups2 = processOverviewPage.getProcessRoleGroups(ROLE_ID2);
        Assert.assertTrue(processRoleGroups1.contains(PLANNING));
        Assert.assertTrue(processRoleGroups1.contains(GENERAL_ENGINEERING));
        Assert.assertEquals(processRoleGroups2, PLANNING);

        //Assert process milestones (Status)
        processOverviewPage.openMilestoneTab();
        String processMilestone1Status = processOverviewPage.getMilestoneValue(processMilestoneName1, STATUS_LABEL);
        String processMilestone2Status = processOverviewPage.getMilestoneValue(processMilestoneName2, STATUS_LABEL);
        Assert.assertEquals(processMilestone1Status, NOT_NEEDED_STATUS);
        Assert.assertEquals(processMilestone2Status, NEW_STATUS);

        //Assert process forecast (start date)
        processOverviewPage.openForecastsTab();
        String processMainForecastStartDate = processOverviewPage.getForecastValue(PROCESS_DEFINITION_NAME_ROLES, START_DATE_LABEL);
        String processForecast1StartDate = processOverviewPage.getForecastValue(AUDIT_ROLES_TASK_1, START_DATE_LABEL);
        String processForecast2StartDate = processOverviewPage.getForecastValue(AUDIT_ROLES_TASK_2, START_DATE_LABEL);
        mainForecast.getStartPlusDays().ifPresent(startDatePlusDays ->
                Assert.assertEquals(processMainForecastStartDate, LocalDate.now().plusDays(startDatePlusDays).toString()));
        processForecasts.get(0).getStartPlusDays().ifPresent(startDatePlusDays ->
                Assert.assertEquals(processForecast1StartDate, LocalDate.now().plusDays(startDatePlusDays).toString()));
        processForecasts.get(1).getStartPlusDays().ifPresent(startDatePlusDays ->
                Assert.assertEquals(processForecast2StartDate, LocalDate.now().plusDays(startDatePlusDays).toString()));

        //Assert process Related Processes
        processOverviewPage.openRelatedProcessesTab();
        Assert.assertEquals(processOverviewPage.getRelatedProcessesAmount(), 2);
        String relatedProgramRelationRole = processOverviewPage.getRelatedProcessValue(programName, RELATION_ROLE_LABEL);
        String relatedProgramRelationRole1 = processOverviewPage.getRelatedProcessValue(programName1, RELATION_ROLE_LABEL);
        Assert.assertEquals(relatedProgramRelationRole, PARENT);
        Assert.assertEquals(relatedProgramRelationRole1, PARENT);

    }

    @Test(priority = 4, description = "Create  Multiple Processes with roles, milestones, forecasts linked to Programs",
            dependsOnMethods = {"createComboProgramWithComboProcess"})
    @Description("Create Multiple Process with roles, milestones, forecasts linked to Programs")
    public void createMultipleComboProcessesLinkedToPrograms() {
        String processName = "Selenium Test Process-" + (int) (Math.random() * 100001);
        String programName1 = "Selenium Test Program-" + (int) (Math.random() * 100001);
        final String processMilestoneName1 = "Milestone Process 1." + (int) (Math.random() * 100001);
        final String processMilestoneName2 = "Milestone Process 2." + (int) (Math.random() * 100001);
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

        final List<Forecast> processForecasts = Lists.newArrayList(
                Forecast.builder()
                        .name(AUDIT_ROLES_TASK_1)
                        .startPlusDays(0L)
                        .endPlusDaysShortWay(plus5Days)
                        .longWorkWeakShortWay(true)
                        .build(),

                Forecast.builder()
                        .name(AUDIT_ROLES_TASK_2)
                        .startPlusDays(0L)
                        .endPlusDaysLongWay(plus10Days)
                        .longWorkWeakLongWay(true)
                        .build());

        ProcessCreationWizardProperties properties = ProcessCreationWizardProperties.builder()
                .basicProcess(processName, PROCESS_DEFINITION_NAME_ROLES, plus5Days)
                .withProcessMilestones(processMilestones)
                .withProcessForecasts(mainForecast, processForecasts)
                .withProcessRolesAssignment(processRoles)
                .withProgramsToLink(Lists.newArrayList(programName, programName1))
                .withMultipleProcesses(processesAmount)
                .build();

        String programCode1 = processOverviewPage.openProgramCreationWizard()
                .createProgram(programName1, plus5Days, PROGRAM_DEFINITION_NAME);

        processOverviewPage.createInstance(properties);
        processName = processName + "-4";

        //Assert message
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assertions.assertThat(messages).hasSize(1);
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assert.assertEquals(messages.get(0).getText(), String.format(PROCESSES_CREATED_MESSAGE, PROCESS_DEFINITION_NAME_ROLES));
        systemMessage.close();

        //Assert notification
        waitForPageToLoad();
        NotificationsInterface notifications = Notifications.create(driver, webDriverWait);
        String notification = notifications.getNotificationMessage();
        Assert.assertEquals(notification, String.format(PROCESSES_LINKED_TO_PROGRAMS_NOTIFICATION, 5,
                PROCESS_DEFINITION_NAME_ROLES));

        //Assert process roles
        processOverviewPage.selectProcess(NAME_LABEL, processName).openProcessRolesTab();
        String processRoleGroups1 = processOverviewPage.getProcessRoleGroups(ROLE_ID1);
        String processRoleGroups2 = processOverviewPage.getProcessRoleGroups(ROLE_ID2);
        Assert.assertTrue(processRoleGroups1.contains(PLANNING));
        Assert.assertTrue(processRoleGroups1.contains(GENERAL_ENGINEERING));
        Assert.assertEquals(processRoleGroups2, PLANNING);

        //Assert process milestones (Status)
        processOverviewPage.openMilestoneTab();
        String processMilestone1Status = processOverviewPage.getMilestoneValue(processMilestoneName1, STATUS_LABEL);
        String processMilestone2Status = processOverviewPage.getMilestoneValue(processMilestoneName2, STATUS_LABEL);
        Assert.assertEquals(processMilestone1Status, NOT_NEEDED_STATUS);
        Assert.assertEquals(processMilestone2Status, NEW_STATUS);

        //Assert process forecast (start date)
        processOverviewPage.openForecastsTab();
        String processMainForecastStartDate = processOverviewPage.getForecastValue(PROCESS_DEFINITION_NAME_ROLES, START_DATE_LABEL);
        String processForecast1StartDate = processOverviewPage.getForecastValue(AUDIT_ROLES_TASK_1, START_DATE_LABEL);
        String processForecast2StartDate = processOverviewPage.getForecastValue(AUDIT_ROLES_TASK_2, START_DATE_LABEL);
        mainForecast.getStartPlusDays().ifPresent(startDatePlusDays ->
                Assert.assertEquals(processMainForecastStartDate, LocalDate.now().plusDays(startDatePlusDays).toString()));
        processForecasts.get(0).getStartPlusDays().ifPresent(startDatePlusDays ->
                Assert.assertEquals(processForecast1StartDate, LocalDate.now().plusDays(startDatePlusDays).toString()));
        processForecasts.get(1).getStartPlusDays().ifPresent(startDatePlusDays ->
                Assert.assertEquals(processForecast2StartDate, LocalDate.now().plusDays(startDatePlusDays).toString()));

        //Assert process Related Processes
        processOverviewPage.openRelatedProcessesTab();
        Assert.assertEquals(processOverviewPage.getRelatedProcessesAmount(), 2);
        String relatedProgramRelationRole = processOverviewPage.getRelatedProcessValue(programName, RELATION_ROLE_LABEL);
        String relatedProgramRelationRole1 = processOverviewPage.getRelatedProcessValue(programName1, RELATION_ROLE_LABEL);
        Assert.assertEquals(relatedProgramRelationRole, PARENT);
        Assert.assertEquals(relatedProgramRelationRole1, PARENT);
    }
}

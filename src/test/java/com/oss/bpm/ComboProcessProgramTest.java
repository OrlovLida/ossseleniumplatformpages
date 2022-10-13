package com.oss.bpm;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.alerts.SystemMessageInterface;
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
    private static final String ROLE_ID1 = "Audit Role 1";
    private static final String ROLE_ID2 = "Audit Role 2";
    private static final String PLANNING = "Planning";
    private static final String GENERAL_ENGINEERING = "General Engineering";
    private static final String PROCESS_SCHEDULED_MESSAGE = "Process %s was scheduled";
    private static final String MILESTONE_DESCRIPTION_1 = "Milestone 1 - Selenium Test";
    private static final String PROGRAM_ROLES_TASK_1 = "program_roles_1";
    private static final String AUDIT_ROLES_TASK_1 = "audit_roles_1";
    private static final String AUDIT_ROLES_TASK_2 = "audit_roles_1";


    private final Logger log = LoggerFactory.getLogger(ComboProcessProgramTest.class);

    private final String programName = "Selenium Test Program-" + (int) (Math.random() * 100001);
    private final long plus5Days = 5L;
    private final long plus10Days = 10L;
    private final int plusMinutes = 2;

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


    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @BeforeClass
    public void openProcessInstancesPage() {
        ProcessOverviewPage processOverviewPage = ProcessOverviewPage.goToProcessOverviewPage(driver, BASIC_URL);
        DelayUtils.sleep(3000);


        ToolbarWidget toolbarWidget = ToolbarWidget.create(driver, webDriverWait);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        if (!toolbarWidget.getUserName().equals(BPM_USER_LOGIN)) {
            processOverviewPage.changeUser(BPM_USER_LOGIN, BPM_USER_PASSWORD);
        }
        processOverviewPage.clearAllColumnFilters();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
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

        processOverviewPage.openProcessCreationWizard().createInstance(processCreationWizardProperties);
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();

        Assertions.assertThat(messages).hasSize(1);
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assert.assertEquals(messages.get(0).getText(), String.format(PROCESS_SCHEDULED_MESSAGE, processName));
        systemMessage.close();
        DelayUtils.sleep((int) TimeUnit.MINUTES.toMillis(plusMinutes));
        processOverviewPage.reloadTable();
        waitForPageToLoad();
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
        final String programMilestoneName1 = "Milestone Update " + (int) (Math.random() * 100001);
        final String programMilestoneName2 = "Milestone Update " + (int) (Math.random() * 100001);
        final String processMilestoneName1 = "Milestone Update " + (int) (Math.random() * 100001);
        final String processMilestoneName2 = "Milestone Update " + (int) (Math.random() * 100001);
        ProcessOverviewPage processOverviewPage = ProcessOverviewPage.goToProcessOverviewPage(driver, BASIC_URL);

        final List<Milestone> programMilestones = Lists.newArrayList(
                Milestone.builder()
                        .setLeadTime("10")
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
                        .setDescription(MILESTONE_DESCRIPTION_1)
                        .setName(processMilestoneName1)
                        .build(),

                Milestone.builder()
                        .setName(processMilestoneName2)
                        .setRelatedTask(AUDIT_ROLES_TASK_1)
                        .setIsManualCompletion("true")
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

    }


}

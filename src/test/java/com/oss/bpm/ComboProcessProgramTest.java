package com.oss.bpm;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.alerts.SystemMessageInterface;
import com.oss.framework.components.mainheader.ToolbarWidget;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.bpm.ProcessOverviewPage;
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
    private static final String ROLE_ID1 = "Audit Role 1";
    private static final String ROLE_ID2 = "Audit Role 2";
    private static final String PLANNING = "Planning";
    private static final String GENERAL_ENGINEERING = "General Engineering";
    private static final String PROCESS_SCHEDULED_MESSAGE = "Process %s was scheduled";


    private final Logger log = LoggerFactory.getLogger(ComboProcessProgramTest.class);


    private final String programName = "Selenium Test Program-" + (int) (Math.random() * 100001);

    private final String milestoneName1 = "Milestone Update " + (int) (Math.random() * 100001);
    private final String milestoneName2 = "Milestone Update " + (int) (Math.random() * 100001);
    private final String milestoneDescription = "Milestone Update " + (Math.random() * 100001);

    private final long plusDays = 5L;
    private final int plusMinutes = 2;


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

        ScheduleProperties scheduleProperties = ScheduleProperties.builder()
                .setSingleSchedule(0L, plusMinutes).build();

        Multimap<String, String> processRoles = HashMultimap.create();
        processRoles.put(ROLE_ID1, PLANNING);
        processRoles.put(ROLE_ID1, GENERAL_ENGINEERING);
        processRoles.put(ROLE_ID2, PLANNING);

        ProcessCreationWizardProperties processCreationWizardProperties = ProcessCreationWizardProperties.builder()
                .basicProcess(processName, PROCESS_DEFINITION_NAME_ROLES, plusDays)
                .withSchedule(scheduleProperties)
                .withProcessRolesAssignment(processRoles)
                .withProcessCreation(processName, PROCESS_DEFINITION_NAME_ROLES, plusDays)
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

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }


}

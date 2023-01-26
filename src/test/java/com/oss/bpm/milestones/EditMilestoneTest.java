/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2021 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.bpm.milestones;

import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.alerts.SystemMessageInterface;
import com.oss.framework.components.mainheader.ToolbarWidget;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.bpm.milestones.EditMilestoneWizardPage;
import com.oss.pages.bpm.milestones.Milestone;
import com.oss.pages.bpm.milestones.MilestoneViewPage;
import com.oss.pages.bpm.processinstances.ProcessOverviewPage;
import com.oss.pages.bpm.processinstances.creation.MilestonesStepWizardPage;
import com.oss.utils.TestListener;
import io.qameta.allure.Description;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Random;

import static com.oss.bpm.BpmPhysicalDataCreator.BPM_ADMIN_USER_LOGIN;
import static com.oss.bpm.BpmPhysicalDataCreator.BPM_ADMIN_USER_PASSWORD;
import static com.oss.bpm.BpmPhysicalDataCreator.BPM_USER_LOGIN;
import static com.oss.bpm.BpmPhysicalDataCreator.BPM_USER_PASSWORD;

/**
 * @author Gabriela Kasza
 */
@Listeners({TestListener.class})
public class EditMilestoneTest extends BaseTestCase {
    private static final String TC1 = "editDescription";
    private static final String TC2 = "editLeadTime";
    private static final String TC3 = "editRelatedTask";
    private static final String TC4 = "editDueDateEarlierDate";
    private static final String TC5 = "editDueDateLaterDate";
    private static final String TC6 = "editManualCompletion";
    private static final String TC7 = "editName";
    private static final String TC8 = "checkIfModifyUserIsUpdated";

    private static final String INVALID_MILESTONE_EDIT_MESSAGE_PATTERN = "Invalid edit milestone system message in '%s' test.";
    private static final String INVALID_MILESTONE_ATTRIBUTE_PATTERN = "Invalid milestone '%1$s' attribute in '%2$s' test.";

    protected static final String EDIT_MILESTONE_BUTTON = "editMilestonesContextAction";
    private static final String CORRECT_DATA_TASK_NAME = "Correct data";
    private static final String DCP = "Data Correction Process";
    private static final String BPM_CONFIGURATION_NAME = "bpm_selenium";
    private static final String MILESTONE_NAME = "Edit Milestone Test ";
    private static final String PROCESS_NAME = "Edit Milestone Test Process ";
    private static final String BPM_MILESTONE_MODIFY_DATE = "modifyDate";
    private static final String BPM_MILESTONE_MODIFY_USER = "modifierUser.name";
    private static final String BPM_MILESTONE_DESCRIPTION = "description";
    private static final String BPM_MILESTONE_RELATED_TASK_NAME = "relatedTaskName";
    private static final String BPM_MILESTONE_LEAD_TIME = "leadTime";
    private static final String BPM_MILESTONE_DUE_DATE = "dueDate";
    private static final String BPM_MILESTONE_MANUAL_COMPLETION = "manualCompletion";
    private static final String BPM_MILESTONE_NAME = "name";
    private static final String SUCCESS_UPDATE_MILESTONES_MESSAGE = "Milestones were updated";
    private static final String TERMINATE_REASON = "Selenium Termination Process After Tests.";
    private static final String EMPTY_ATTRIBUTE1 = "—";
    private static final String EMPTY_ATTRIBUTE2 = "-";
    private static final Random RANDOM = new Random();
    private SoftAssert softAssert;
    private final String processName = PROCESS_NAME + RANDOM.nextInt(Integer.MAX_VALUE);
    private final String description = "Milestone Update " + RANDOM.nextInt(Integer.MAX_VALUE);
    private final String leadTime = String.valueOf(RANDOM.nextInt(100));
    private final String newMilestoneName = "Edit Milestone Test Update " + RANDOM.nextInt(Integer.MAX_VALUE);
    private String milestoneName = "Edit Milestone Test Update " + RANDOM.nextInt(Integer.MAX_VALUE);

    @BeforeClass
    public void createMilestone() {
        softAssert = new SoftAssert();
        ProcessOverviewPage processOverviewPage = ProcessOverviewPage.goToProcessOverviewPage(driver, BASIC_URL);

        ToolbarWidget toolbarWidget = ToolbarWidget.create(driver, webDriverWait);
        waitForPageToLoad();
        if (!toolbarWidget.getUserName().equals(BPM_USER_LOGIN)) {
            processOverviewPage.changeUser(BPM_USER_LOGIN, BPM_USER_PASSWORD);
        }
        waitForPageToLoad();
        processOverviewPage.clearAllColumnFilters();

        Milestone milestone1 = Milestone.builder()
                .setLeadTime("10")
                .setRelatedTask(CORRECT_DATA_TASK_NAME)
                .setIsActive("true")
                .setName(milestoneName).build();

        MilestonesStepWizardPage milestonesStepWizardPage = processOverviewPage.openProcessCreationWizard()
                .defineProcessAndGoToMilestonesStep(processName, 5L, DCP);
        milestonesStepWizardPage.addMilestoneRow(milestone1);
        milestonesStepWizardPage.clickAcceptButton();
    }

    @Test(priority = 1, description = "Edit Description")
    @Description("Edit Description")
    public void editDescription() {
        waitForPageToLoad();
        // given
        MilestoneViewPage milestoneViewPage = MilestoneViewPage.goToMilestoneViewPage(driver, BASIC_URL);
        waitForPageToLoad();
        // when
        milestoneViewPage.selectMilestone(milestoneName);
        String modifyDate = milestoneViewPage.getMilestoneAttribute(BPM_MILESTONE_MODIFY_DATE);
        milestoneViewPage.callAction(EDIT_MILESTONE_BUTTON);
        EditMilestoneWizardPage editWizard = new EditMilestoneWizardPage(driver);
        Milestone updated_description = Milestone.builder().setDescription(description).build();
        // then
        editWizard.editMilestone(updated_description);

        assertSystemMessage(SUCCESS_UPDATE_MILESTONES_MESSAGE, SystemMessageContainer.MessageType.SUCCESS,
                String.format(INVALID_MILESTONE_EDIT_MESSAGE_PATTERN, TC1));

        String newDescriptionValue = milestoneViewPage.getMilestoneAttribute(BPM_MILESTONE_DESCRIPTION);
        String newModifyDate = milestoneViewPage.getMilestoneAttribute(BPM_MILESTONE_MODIFY_DATE);
        Assert.assertEquals(newDescriptionValue, description,
                String.format(INVALID_MILESTONE_ATTRIBUTE_PATTERN, BPM_MILESTONE_DESCRIPTION, TC1));
        Assert.assertNotEquals(newModifyDate, modifyDate,
                String.format(INVALID_MILESTONE_ATTRIBUTE_PATTERN, BPM_MILESTONE_MODIFY_DATE, TC1));
    }

    @Test(priority = 2, description = "Edit LeadTime")
    @Description("Edit LeadTime")
    public void editLeadTime() {
        waitForPageToLoad();
        // given
        MilestoneViewPage milestoneViewPage = MilestoneViewPage.goToMilestoneViewPage(driver, BASIC_URL);
        waitForPageToLoad();
        // when
        milestoneViewPage.selectMilestone(milestoneName);
        String modifyDate = milestoneViewPage.getMilestoneAttribute(BPM_MILESTONE_MODIFY_DATE);
        milestoneViewPage.callAction(EDIT_MILESTONE_BUTTON);
        EditMilestoneWizardPage editWizard = new EditMilestoneWizardPage(driver);
        Milestone updated_leadTime = Milestone.builder().setLeadTime(leadTime).build();
        // then
        editWizard.editMilestone(updated_leadTime);

        assertSystemMessage(SUCCESS_UPDATE_MILESTONES_MESSAGE, SystemMessageContainer.MessageType.SUCCESS,
                String.format(INVALID_MILESTONE_EDIT_MESSAGE_PATTERN, TC2));


        String newLeadTimeValue = milestoneViewPage.getMilestoneAttribute(BPM_MILESTONE_LEAD_TIME);
        String newModifyDate = milestoneViewPage.getMilestoneAttribute(BPM_MILESTONE_MODIFY_DATE);
        Assert.assertEquals(newLeadTimeValue, leadTime,
                String.format(INVALID_MILESTONE_ATTRIBUTE_PATTERN, BPM_MILESTONE_LEAD_TIME, TC2));
        Assert.assertNotEquals(newModifyDate, modifyDate,
                String.format(INVALID_MILESTONE_ATTRIBUTE_PATTERN, BPM_MILESTONE_MODIFY_DATE, TC2));
    }

    @Test(priority = 3, description = "Edit related Task")
    @Description("Edit related Task")
    public void editRelatedTask() {
        waitForPageToLoad();
        MilestoneViewPage milestoneViewPage = MilestoneViewPage.goToMilestoneViewPage(driver, BASIC_URL);
        waitForPageToLoad();
        milestoneViewPage.selectMilestone(milestoneName);
        milestoneViewPage.callAction(EDIT_MILESTONE_BUTTON);
        EditMilestoneWizardPage editWizard = new EditMilestoneWizardPage(driver);
        Milestone setProcess = Milestone.builder().setRelatedTask("").build();
        editWizard.editMilestone(setProcess);

        assertSystemMessage(SUCCESS_UPDATE_MILESTONES_MESSAGE, SystemMessageContainer.MessageType.SUCCESS,
                String.format(INVALID_MILESTONE_EDIT_MESSAGE_PATTERN, TC3));

        String relatedTask = milestoneViewPage.getMilestoneAttribute(BPM_MILESTONE_RELATED_TASK_NAME);
        Assert.assertTrue(relatedTask.equals(EMPTY_ATTRIBUTE1) || relatedTask.equals(EMPTY_ATTRIBUTE2),
                String.format(INVALID_MILESTONE_ATTRIBUTE_PATTERN, BPM_MILESTONE_RELATED_TASK_NAME, TC3));

    }

    @Test(priority = 4, description = "Edit Due Date for earlier date")
    @Description("Edit Due Date for earlier date")
    public void editDueDateEarlierDate() {
        waitForPageToLoad();
        // given
        MilestoneViewPage milestoneViewPage = MilestoneViewPage.goToMilestoneViewPage(driver, BASIC_URL);
        waitForPageToLoad();
        // when
        milestoneViewPage.selectMilestone(milestoneName);
        String modifyDate = milestoneViewPage.getMilestoneAttribute(BPM_MILESTONE_MODIFY_DATE);
        String dueDate = milestoneViewPage.getMilestoneAttribute(BPM_MILESTONE_DUE_DATE);
        milestoneViewPage.callAction(EDIT_MILESTONE_BUTTON);
        EditMilestoneWizardPage editWizard = new EditMilestoneWizardPage(driver);
        Milestone updated_dueDate = Milestone.builder().setDueDate(LocalDate.parse(dueDate).minusDays(10).toString()).build();
        // then
        editWizard.editMilestone(updated_dueDate);

        assertSystemMessage(SUCCESS_UPDATE_MILESTONES_MESSAGE, SystemMessageContainer.MessageType.SUCCESS,
                String.format(INVALID_MILESTONE_EDIT_MESSAGE_PATTERN, TC4));

        String newDueDateValue = milestoneViewPage.getMilestoneAttribute(BPM_MILESTONE_DUE_DATE);
        String newModifyDate = milestoneViewPage.getMilestoneAttribute(BPM_MILESTONE_MODIFY_DATE);
        updated_dueDate.getDueDate().ifPresent(dueDate1 -> Assert.assertEquals(newDueDateValue, dueDate1,
                String.format(INVALID_MILESTONE_ATTRIBUTE_PATTERN, BPM_MILESTONE_DUE_DATE, TC4)));
        Assert.assertNotEquals(newModifyDate, modifyDate,
                String.format(INVALID_MILESTONE_ATTRIBUTE_PATTERN, BPM_MILESTONE_MODIFY_DATE, TC4));
    }

    @Test(priority = 5, description = "Edit Due Date for later date")
    @Description("Edit Due Date for later date")
    void editDueDateLaterDate() {
        waitForPageToLoad();
        // given
        MilestoneViewPage milestoneViewPage = MilestoneViewPage.goToMilestoneViewPage(driver, BASIC_URL);
        waitForPageToLoad();
        // when
        milestoneViewPage.selectMilestone(milestoneName);
        String modifyDate = milestoneViewPage.getMilestoneAttribute(BPM_MILESTONE_MODIFY_DATE);
        String dueDate = milestoneViewPage.getMilestoneAttribute(BPM_MILESTONE_DUE_DATE);
        milestoneViewPage.callAction(EDIT_MILESTONE_BUTTON);
        EditMilestoneWizardPage editWizard = new EditMilestoneWizardPage(driver);
        Milestone updated_dueDate = Milestone.builder().setDueDate(LocalDate.parse(dueDate).plusDays(10).toString()).build();
        // then
        editWizard.editMilestone(updated_dueDate);

        assertSystemMessage(SUCCESS_UPDATE_MILESTONES_MESSAGE, SystemMessageContainer.MessageType.SUCCESS,
                String.format(INVALID_MILESTONE_EDIT_MESSAGE_PATTERN, TC5));

        String newDueDateValue = milestoneViewPage.getMilestoneAttribute(BPM_MILESTONE_DUE_DATE);
        String newModifyDate = milestoneViewPage.getMilestoneAttribute(BPM_MILESTONE_MODIFY_DATE);
        updated_dueDate.getDueDate().ifPresent(dueDate1 -> Assert.assertEquals(newDueDateValue, dueDate1,
                String.format(INVALID_MILESTONE_ATTRIBUTE_PATTERN, BPM_MILESTONE_DUE_DATE, TC5)));
        Assert.assertNotEquals(newModifyDate, modifyDate,
                String.format(INVALID_MILESTONE_ATTRIBUTE_PATTERN, BPM_MILESTONE_MODIFY_DATE, TC5));

    }

    @Test(priority = 6, description = "Edit Manual Completion")
    @Description("Edit Manual Completion")
    void editManualCompletion() {
        waitForPageToLoad();
        MilestoneViewPage milestoneViewPage = MilestoneViewPage.goToMilestoneViewPage(driver, BASIC_URL);
        waitForPageToLoad();
        milestoneViewPage.selectMilestone(milestoneName);
        String modifyDate = milestoneViewPage.getMilestoneAttribute(BPM_MILESTONE_MODIFY_DATE);
        milestoneViewPage.callAction(EDIT_MILESTONE_BUTTON);
        EditMilestoneWizardPage editWizard = new EditMilestoneWizardPage(driver);
        Milestone aTrue = Milestone.builder().setIsManualCompletion("true").build();
        editWizard.editMilestone(aTrue);

        assertSystemMessage(SUCCESS_UPDATE_MILESTONES_MESSAGE, SystemMessageContainer.MessageType.SUCCESS,
                String.format(INVALID_MILESTONE_EDIT_MESSAGE_PATTERN, TC6));

        String newIsManualCompletionValue = milestoneViewPage.getMilestoneAttribute(BPM_MILESTONE_MANUAL_COMPLETION);
        String newModifyDate = milestoneViewPage.getMilestoneAttribute(BPM_MILESTONE_MODIFY_DATE);
        aTrue.getIsManualCompletion().ifPresent(isManualCompletion ->
                Assert.assertEquals(newIsManualCompletionValue, isManualCompletion,
                        String.format(INVALID_MILESTONE_ATTRIBUTE_PATTERN, BPM_MILESTONE_MANUAL_COMPLETION, TC6)));
        Assert.assertNotEquals(newModifyDate, modifyDate,
                String.format(INVALID_MILESTONE_ATTRIBUTE_PATTERN, BPM_MILESTONE_MODIFY_DATE, TC6));
    }

    @Test(priority = 7, description = "Edit Milestone Name")
    @Description("Edit Milestone name")
    void editName() {
        waitForPageToLoad();
        MilestoneViewPage milestoneViewPage = MilestoneViewPage.goToMilestoneViewPage(driver, BASIC_URL);
        waitForPageToLoad();
        // when
        milestoneViewPage.selectMilestone(milestoneName);
        String modifyDate = milestoneViewPage.getMilestoneAttribute(BPM_MILESTONE_MODIFY_DATE);
        milestoneViewPage.callAction(EDIT_MILESTONE_BUTTON);
        EditMilestoneWizardPage editWizard = new EditMilestoneWizardPage(driver);
        Milestone updated_name = Milestone.builder().setName(newMilestoneName).build();
        // then
        editWizard.editMilestone(updated_name);

        assertSystemMessage(SUCCESS_UPDATE_MILESTONES_MESSAGE, SystemMessageContainer.MessageType.SUCCESS,
                String.format(INVALID_MILESTONE_EDIT_MESSAGE_PATTERN, TC7));

        milestoneViewPage.selectMilestone(newMilestoneName);
        waitForPageToLoad();
        String newNameValue = milestoneViewPage.getMilestoneAttribute(BPM_MILESTONE_NAME);
        String newModifyDate = milestoneViewPage.getMilestoneAttribute(BPM_MILESTONE_MODIFY_DATE);
        Assert.assertEquals(newNameValue, newMilestoneName,
                String.format(INVALID_MILESTONE_ATTRIBUTE_PATTERN, BPM_MILESTONE_NAME, TC7));
        Assert.assertNotEquals(newModifyDate, modifyDate,
                String.format(INVALID_MILESTONE_ATTRIBUTE_PATTERN, BPM_MILESTONE_MODIFY_DATE, TC7));
    }

    @Test(priority = 8, description = "Check if Modify User is updated", dependsOnMethods = {"editName"})
    @Description("Check if Modify User is updated")
    public void checkIfModifyUserIsUpdated() {
        waitForPageToLoad();
        // given
        MilestoneViewPage milestoneViewPage = MilestoneViewPage.goToMilestoneViewPage(driver, BASIC_URL);
        waitForPageToLoad();
        milestoneViewPage.changeUser(BPM_ADMIN_USER_LOGIN, BPM_ADMIN_USER_PASSWORD);
        waitForPageToLoad();
        milestoneViewPage.selectMilestone(newMilestoneName);
        String modifyUser;
        String modifyDate = milestoneViewPage.getMilestoneAttribute(BPM_MILESTONE_MODIFY_DATE);
        try {
            modifyUser = milestoneViewPage.getMilestoneAttribute(BPM_MILESTONE_MODIFY_USER);
        } catch (IllegalArgumentException e) {
            milestoneViewPage.chooseMilestoneAttributesConfiguration(BPM_CONFIGURATION_NAME);
            modifyUser = milestoneViewPage.getMilestoneAttribute(BPM_MILESTONE_MODIFY_USER);
        }

        // when
        milestoneViewPage.callAction(EDIT_MILESTONE_BUTTON);
        EditMilestoneWizardPage editWizard = new EditMilestoneWizardPage(driver);
        Milestone updated_name = Milestone.builder().setName(MILESTONE_NAME + RANDOM.nextInt(Integer.MAX_VALUE)).build();
        editWizard.editMilestone(updated_name);

        assertSystemMessage(SUCCESS_UPDATE_MILESTONES_MESSAGE, SystemMessageContainer.MessageType.SUCCESS,
                String.format(INVALID_MILESTONE_EDIT_MESSAGE_PATTERN, TC8));

        // then
        updated_name.getName().ifPresent(milestoneViewPage::selectMilestone);
        milestoneName = milestoneViewPage.getMilestoneAttribute(BPM_MILESTONE_NAME);
        String newModifyDate = milestoneViewPage.getMilestoneAttribute(BPM_MILESTONE_MODIFY_DATE);
        String newModifyUser = milestoneViewPage.getMilestoneAttribute(BPM_MILESTONE_MODIFY_USER);
        Assert.assertEquals(milestoneName, updated_name.getName().get(),
                String.format(INVALID_MILESTONE_ATTRIBUTE_PATTERN, BPM_MILESTONE_NAME, TC8));
        Assert.assertNotEquals(newModifyDate, modifyDate,
                String.format(INVALID_MILESTONE_ATTRIBUTE_PATTERN, BPM_MILESTONE_MODIFY_DATE, TC8));
        Assert.assertNotEquals(newModifyUser, modifyUser,
                String.format(INVALID_MILESTONE_ATTRIBUTE_PATTERN, BPM_MILESTONE_MODIFY_USER, TC8));
    }

    @Test(priority = 9, description = "Checking system message summary")
    @Description("Checking system message summary")
    public void systemMessageSummary() {
        softAssert.assertAll();
    }

    @AfterClass
    public void terminateProcess() {
        ProcessOverviewPage processOverviewPage = ProcessOverviewPage.goToProcessOverviewPage(driver, BASIC_URL).clearAllColumnFilters();
        processOverviewPage.selectProcess(ProcessOverviewPage.NAME_LABEL, processName).terminateProcess(TERMINATE_REASON);
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

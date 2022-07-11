/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2021 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.bpm.milestones;

import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.mainheader.ToolbarWidget;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.bpm.ProcessOverviewPage;
import com.oss.pages.bpm.milestones.EditMilestoneWizardPage;
import com.oss.pages.bpm.milestones.Milestone;
import com.oss.pages.bpm.milestones.MilestoneViewPage;
import com.oss.pages.bpm.processinstances.ProcessWizardPage;
import com.oss.utils.TestListener;
import io.qameta.allure.Description;
import org.openqa.selenium.NoSuchElementException;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.time.LocalDate;

/**
 * @author Gabriela Kasza
 */
@Listeners({TestListener.class})
public class EditMilestoneTest extends BaseTestCase {
    private static final String BPM_USER_LOGIN = "bpm_webselenium";
    private static final String BPM_USER_PASSWORD = "Webtests123!";
    private static final String BPM_ADMIN_USER_LOGIN = "bpm_admin_webselenium";
    private static final String BPM_ADMIN_USER_PASSWORD = "Webtests123!";

    private static final String CORRECT_DATA_TASK_NAME = "Correct data";
    private static final String DCP = "Data Correction Process";
    private static final String MILESTONE_UPDATE_NAME = "Edit Name";
    private static final String NON_EDITABLE_NAME_EXCEPTION = "Name is not editable. You need Admin permission";
    private static final String BPM_CONFIGURATION_NAME = "bpm_selenium";
    private static final String MILESTONE_NAME = "Milestone U.";
    private static final String PROCESS_NAME = "Selenium Test.Milestone-";
    private static final String BPM_MILESTONE_MODIFY_DATE = "modifyDate";
    private static final String BPM_MILESTONE_MODIFY_USER = "modifierUser.name";
    private static final String BPM_MILESTONE_DESCRIPTION = "description";
    private static final String BPM_MILESTONE_RELATED_TASK_NAME = "relatedTaskName";
    private static final String BPM_MILESTONE_LEAD_TIME = "leadTime";
    private static final String BPM_MILESTONE_DUE_DATE = "dueDate";
    private static final String BPM_MILESTONE_MANUAL_COMPLETION = "manualCompletion";
    private static final String BPM_MILESTONE_NAME = "name";
    private static final String EDIT_MILESTONE_BUTTON = "editMilestonesContextAction";
    private static final String SUCCESS_UPDATE_MILESTONES_MESSAGE = "Milestones were updated";
    private static final String NO_SYSTEM_MESSAGE_EXCEPTION = "There is no any System Message";
    private static final String EMPTY_ATTRIBUTE = "—";

    private final String description = "Milestone Update " + (Math.random() * 1001);
    private final String leadTime = String.valueOf((int) (Math.random() * 101));
    private String milestoneName = "Milestone Update " + (Math.random() * 100001);

    @BeforeClass
    public void createMilestone() {
        ProcessOverviewPage processOverviewPage = ProcessOverviewPage.goToProcessOverviewPage(driver, BASIC_URL);
        processOverviewPage.clearAllColumnFilters();

        ToolbarWidget toolbarWidget = ToolbarWidget.create(driver, webDriverWait);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        if (!toolbarWidget.getUserName().equals(BPM_USER_LOGIN)) {
            processOverviewPage.changeUser(BPM_USER_LOGIN, BPM_USER_PASSWORD);
        }
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        String processName = PROCESS_NAME + (int) (Math.random() * 100001);

        Milestone milestone1 = Milestone.builder()
                .setLeadTime("10")
                .setRelatedTask(CORRECT_DATA_TASK_NAME)
                .setIsActive("true")
                .setName(milestoneName).build();

        ProcessWizardPage processWizardPage = processOverviewPage.openProcessCreationWizard();
        processWizardPage.definedMilestoneInProcess(processName, 5L, DCP).addMilestoneRow(milestone1);
        processWizardPage.clickAcceptButton();
    }

    @Test(priority = 1, description = "Edit Description")
    @Description("Edit Description")
    public void editDescription() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        // given
        MilestoneViewPage milestoneViewPage = MilestoneViewPage.goToMilestoneViewPage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        // when
        milestoneViewPage.selectMilestone(milestoneName);
        String modifyDate = milestoneViewPage.getMilestoneAttribute(BPM_MILESTONE_MODIFY_DATE);
        milestoneViewPage.callAction(EDIT_MILESTONE_BUTTON);
        EditMilestoneWizardPage editWizard = new EditMilestoneWizardPage(driver);
        Milestone updated_description = Milestone.builder().setDescription(description).build();
        // then
        editWizard.editMilestone(updated_description);

        String message = SystemMessageContainer.create(driver, webDriverWait).getFirstMessage().orElseThrow(()
                -> new RuntimeException(NO_SYSTEM_MESSAGE_EXCEPTION)).getText();
        Assert.assertEquals(message, SUCCESS_UPDATE_MILESTONES_MESSAGE);

        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        String newDescriptionValue = milestoneViewPage.getMilestoneAttribute(BPM_MILESTONE_DESCRIPTION);
        String newModifyDate = milestoneViewPage.getMilestoneAttribute(BPM_MILESTONE_MODIFY_DATE);
        Assert.assertEquals(description, newDescriptionValue);
        Assert.assertNotEquals(newModifyDate, modifyDate);
    }

    @Test(priority = 2, description = "Edit LeadTime")
    @Description("Edit LeadTime")
    public void editLeadTime() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        // given
        MilestoneViewPage milestoneViewPage = MilestoneViewPage.goToMilestoneViewPage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        // when
        milestoneViewPage.selectMilestone(milestoneName);
        String modifyDate = milestoneViewPage.getMilestoneAttribute(BPM_MILESTONE_MODIFY_DATE);
        milestoneViewPage.callAction(EDIT_MILESTONE_BUTTON);
        EditMilestoneWizardPage editWizard = new EditMilestoneWizardPage(driver);
        Milestone updated_leadTime = Milestone.builder().setLeadTime(leadTime).build();
        // then
        editWizard.editMilestone(updated_leadTime);

        String message = SystemMessageContainer.create(driver, webDriverWait).getFirstMessage().orElseThrow(()
                -> new RuntimeException(NO_SYSTEM_MESSAGE_EXCEPTION)).getText();
        Assert.assertEquals(message, SUCCESS_UPDATE_MILESTONES_MESSAGE);

        String newLeadTimeValue = milestoneViewPage.getMilestoneAttribute(BPM_MILESTONE_LEAD_TIME);
        String newModifyDate = milestoneViewPage.getMilestoneAttribute(BPM_MILESTONE_MODIFY_DATE);
        Assert.assertEquals(leadTime, newLeadTimeValue);
        Assert.assertNotEquals(newModifyDate, modifyDate);
    }

    @Test(priority = 3, description = "Edit related Task")
    @Description("Edit related Task")
    public void editRelatedTask() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        MilestoneViewPage milestoneViewPage = MilestoneViewPage.goToMilestoneViewPage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        milestoneViewPage.selectMilestone(milestoneName);
        milestoneViewPage.callAction(EDIT_MILESTONE_BUTTON);
        EditMilestoneWizardPage editWizard = new EditMilestoneWizardPage(driver);
        Milestone setProcess = Milestone.builder().setRelatedTask("").build();
        editWizard.editMilestone(setProcess);

        String message = SystemMessageContainer.create(driver, webDriverWait).getFirstMessage().orElseThrow(()
                -> new RuntimeException(NO_SYSTEM_MESSAGE_EXCEPTION)).getText();
        Assert.assertEquals(message, SUCCESS_UPDATE_MILESTONES_MESSAGE);

        String relatedTask = milestoneViewPage.getMilestoneAttribute(BPM_MILESTONE_RELATED_TASK_NAME);
        Assert.assertEquals(relatedTask, EMPTY_ATTRIBUTE);

    }

    @Test(priority = 4, description = "Edit Due Date for earlier date")
    @Description("Edit Due Date for earlier date")
    public void editDueDateEarlierDate() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        // given
        MilestoneViewPage milestoneViewPage = MilestoneViewPage.goToMilestoneViewPage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        // when
        milestoneViewPage.selectMilestone(milestoneName);
        String modifyDate = milestoneViewPage.getMilestoneAttribute(BPM_MILESTONE_MODIFY_DATE);
        String dueDate = milestoneViewPage.getMilestoneAttribute(BPM_MILESTONE_DUE_DATE);
        milestoneViewPage.callAction(EDIT_MILESTONE_BUTTON);
        EditMilestoneWizardPage editWizard = new EditMilestoneWizardPage(driver);
        Milestone updated_dueDate = Milestone.builder().setDueDate(LocalDate.parse(dueDate).minusDays(10).toString()).build();
        // then
        editWizard.editMilestone(updated_dueDate);

        String message = SystemMessageContainer.create(driver, webDriverWait).getFirstMessage().orElseThrow(()
                -> new RuntimeException(NO_SYSTEM_MESSAGE_EXCEPTION)).getText();
        Assert.assertEquals(message, SUCCESS_UPDATE_MILESTONES_MESSAGE);

        String newDueDateValue = milestoneViewPage.getMilestoneAttribute(BPM_MILESTONE_DUE_DATE);
        String newModifyDate = milestoneViewPage.getMilestoneAttribute(BPM_MILESTONE_MODIFY_DATE);
        Assert.assertEquals(updated_dueDate.getDueDate().get(), newDueDateValue);
        Assert.assertNotEquals(newModifyDate, modifyDate);
    }

    @Test(priority = 5, description = "Edit Due Date for later date")
    @Description("Edit Due Date for later date")
    void editDueDateLaterDate() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        // given
        MilestoneViewPage milestoneViewPage = MilestoneViewPage.goToMilestoneViewPage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        // when
        milestoneViewPage.selectMilestone(milestoneName);
        String modifyDate = milestoneViewPage.getMilestoneAttribute(BPM_MILESTONE_MODIFY_DATE);
        String dueDate = milestoneViewPage.getMilestoneAttribute(BPM_MILESTONE_DUE_DATE);
        milestoneViewPage.callAction(EDIT_MILESTONE_BUTTON);
        EditMilestoneWizardPage editWizard = new EditMilestoneWizardPage(driver);
        Milestone updated_dueDate = Milestone.builder().setDueDate(LocalDate.parse(dueDate).plusDays(10).toString()).build();
        // then
        editWizard.editMilestone(updated_dueDate);

        String message = SystemMessageContainer.create(driver, webDriverWait).getFirstMessage().orElseThrow(()
                -> new RuntimeException(NO_SYSTEM_MESSAGE_EXCEPTION)).getText();
        Assert.assertEquals(message, SUCCESS_UPDATE_MILESTONES_MESSAGE);

        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        String newDueDateValue = milestoneViewPage.getMilestoneAttribute(BPM_MILESTONE_DUE_DATE);
        String newModifyDate = milestoneViewPage.getMilestoneAttribute(BPM_MILESTONE_MODIFY_DATE);
        Assert.assertEquals(updated_dueDate.getDueDate().get(), newDueDateValue);
        Assert.assertNotEquals(newModifyDate, modifyDate);

    }

    @Test(priority = 6, description = "Check if Name is editable")
    @Description("Check if Name is editable")
    void checkIfNameIsEditable() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        MilestoneViewPage milestoneViewPage = MilestoneViewPage.goToMilestoneViewPage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        // when
        milestoneViewPage.selectMilestone(milestoneName);
        milestoneViewPage.callAction(EDIT_MILESTONE_BUTTON);
        EditMilestoneWizardPage editWizard = new EditMilestoneWizardPage(driver);
        Milestone updated_name = Milestone.builder().setName(MILESTONE_UPDATE_NAME).build();
        // then
        RuntimeException runtimeException = Assert.expectThrows(NoSuchElementException.class, ()
                -> editWizard.editMilestone(updated_name));
        editWizard.cancel();
        Assert.assertTrue(runtimeException.getMessage().contains(NON_EDITABLE_NAME_EXCEPTION));
    }

    @Test(priority = 7, description = "Edit Manual Completion")
    @Description("Edit Manual Completion")
    void editManualCompletion() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        MilestoneViewPage milestoneViewPage = MilestoneViewPage.goToMilestoneViewPage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        milestoneViewPage.selectMilestone(milestoneName);
        String modifyDate = milestoneViewPage.getMilestoneAttribute(BPM_MILESTONE_MODIFY_DATE);
        milestoneViewPage.callAction(EDIT_MILESTONE_BUTTON);
        EditMilestoneWizardPage editWizard = new EditMilestoneWizardPage(driver);
        Milestone aTrue = Milestone.builder().setIsManualCompletion("true").build();
        editWizard.editMilestone(aTrue);

        String message = SystemMessageContainer.create(driver, webDriverWait).getFirstMessage().orElseThrow(()
                -> new RuntimeException(NO_SYSTEM_MESSAGE_EXCEPTION)).getText();
        Assert.assertEquals(message, SUCCESS_UPDATE_MILESTONES_MESSAGE);

        String newIsManualCompletionValue = milestoneViewPage.getMilestoneAttribute(BPM_MILESTONE_MANUAL_COMPLETION);
        String newModifyDate = milestoneViewPage.getMilestoneAttribute(BPM_MILESTONE_MODIFY_DATE);
        Assert.assertEquals(aTrue.getIsManualCompletion().get(), newIsManualCompletionValue);
        Assert.assertNotEquals(newModifyDate, modifyDate);
    }

    @Test(priority = 8, description = "Check if Modify User is updated")
    @Description("Check if Modify User is updated")
    public void checkIfModifyUserIsUpdated() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        // given
        MilestoneViewPage milestoneViewPage = MilestoneViewPage.goToMilestoneViewPage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        milestoneViewPage.changeUser(BPM_ADMIN_USER_LOGIN, BPM_ADMIN_USER_PASSWORD);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        milestoneViewPage.selectMilestone(milestoneName);
        milestoneViewPage.chooseMilestoneAttributesConfiguration(BPM_CONFIGURATION_NAME);
        String modifyDate = milestoneViewPage.getMilestoneAttribute(BPM_MILESTONE_MODIFY_DATE);
        String modifyUser = milestoneViewPage.getMilestoneAttribute(BPM_MILESTONE_MODIFY_USER);
        // when
        milestoneViewPage.callAction(EDIT_MILESTONE_BUTTON);
        EditMilestoneWizardPage editWizard = new EditMilestoneWizardPage(driver);
        Milestone updated_name = Milestone.builder().setName(MILESTONE_NAME + (int) (Math.random() * 1001)).build();
        editWizard.editMilestone(updated_name);

        String message = SystemMessageContainer.create(driver, webDriverWait).getFirstMessage().orElseThrow(()
                -> new RuntimeException(NO_SYSTEM_MESSAGE_EXCEPTION)).getText();
        Assert.assertEquals(message, SUCCESS_UPDATE_MILESTONES_MESSAGE);

        // then
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        milestoneViewPage.selectMilestone(updated_name.getName().get());
        milestoneName = milestoneViewPage.getMilestoneAttribute(BPM_MILESTONE_NAME);
        String newModifyDate = milestoneViewPage.getMilestoneAttribute(BPM_MILESTONE_MODIFY_DATE);
        String newModifyUser = milestoneViewPage.getMilestoneAttribute(BPM_MILESTONE_MODIFY_USER);
        Assert.assertEquals(updated_name.getName().get(), milestoneName);
        Assert.assertNotEquals(newModifyDate, modifyDate);
        Assert.assertNotEquals(newModifyUser, modifyUser);
    }
}

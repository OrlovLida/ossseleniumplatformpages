/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2021 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.bpm;

import java.time.LocalDate;

import com.oss.framework.components.mainheader.ToolbarWidget;
import io.qameta.allure.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.bpm.milestones.EditMilestoneWizardPage;
import com.oss.pages.bpm.milestones.Milestone;
import com.oss.pages.bpm.milestones.MilestoneViewPage;
import com.oss.pages.bpm.processinstances.ProcessInstancesPage;
import com.oss.pages.bpm.processinstances.ProcessWizardPage;
import com.oss.utils.TestListener;

/**
 * @author Gabriela Kasza
 */
@Listeners({TestListener.class})
public class EditMilestoneTest extends BaseTestCase {
    private final String BPM_USER_LOGIN = "bpm_webselenium";
    private final String BPM_USER_PASSWORD = "Webtests123!";
    private final String BPM_ADMIN_USER_LOGIN = "bpm_admin_webselenium";
    private final String BPM_ADMIN_USER_PASSWORD = "Webtests123!";

    private static final Logger log = LoggerFactory.getLogger(EditMilestoneTest.class);
    private final static String EDIT_MILESTONE_BUTTON = "editMilestonesContextAction";

    private String description = "Milestone Update " + (Math.random() * 1001);
    private String milestoneName = "Milestone Update " + (Math.random() * 1001);
    private String leadTime = String.valueOf((int) (Math.random() * 101));

    @BeforeClass
    public void createMilestone() {
        ProcessInstancesPage processInstancesPage = ProcessInstancesPage.goToProcessInstancesPage(driver, BASIC_URL);

        ToolbarWidget toolbarWidget = ToolbarWidget.create(driver, webDriverWait);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        if (!toolbarWidget.getUserName().equals(BPM_USER_LOGIN)) {
            processInstancesPage.changeUser(BPM_USER_LOGIN, BPM_USER_PASSWORD);
        }
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        String processName = "Selenium Test.Milestone-" + (int) (Math.random() * 1001);
        ProcessWizardPage processWizardPage = new ProcessWizardPage(driver);

        Milestone milestone1 = Milestone.builder()
                .setLeadTime("10")
                .setRelatedTask("Correct data")
                .setIsActive("true")
                .setName(milestoneName).build();

        processWizardPage.definedMilestoneInProcess(processName, 5L,
                "Data Correction Process").addMilestoneRow(milestone1);
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
        String modifyDate = milestoneViewPage.getMilestoneAttribute("modifyDate");
        milestoneViewPage.callAction(EDIT_MILESTONE_BUTTON);
        EditMilestoneWizardPage editWizard = new EditMilestoneWizardPage(driver);
        Milestone updated_description = Milestone.builder().setDescription(description).build();
        // then
        editWizard.editMilestone(updated_description);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        String newDescriptionValue = milestoneViewPage.getMilestoneAttribute("description");
        String newModifyDate = milestoneViewPage.getMilestoneAttribute("modifyDate");
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
        String modifyDate = milestoneViewPage.getMilestoneAttribute("modifyDate");
        milestoneViewPage.callAction(EDIT_MILESTONE_BUTTON);
        EditMilestoneWizardPage editWizard = new EditMilestoneWizardPage(driver);
        Milestone updated_leadTime = Milestone.builder().setLeadTime(leadTime).build();
        // then
        editWizard.editMilestone(updated_leadTime);
        String newLeadTimeValue = milestoneViewPage.getMilestoneAttribute("leadTime");
        String newModifyDate = milestoneViewPage.getMilestoneAttribute("modifyDate");
        Assert.assertEquals(leadTime, newLeadTimeValue);
        Assert.assertNotEquals(newModifyDate, modifyDate);
        log.info("test");
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
        String relatedTask = milestoneViewPage.getMilestoneAttribute("relatedTaskName");
        Assert.assertEquals(relatedTask, "");

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
        String modifyDate = milestoneViewPage.getMilestoneAttribute("modifyDate");
        String dueDate = milestoneViewPage.getMilestoneAttribute("dueDate");
        milestoneViewPage.callAction(EDIT_MILESTONE_BUTTON);
        EditMilestoneWizardPage editWizard = new EditMilestoneWizardPage(driver);
        Milestone updated_dueDate = Milestone.builder().setDueDate(LocalDate.parse(dueDate).minusDays(10).toString()).build();
        // then
        editWizard.editMilestone(updated_dueDate);
        String newDueDateValue = milestoneViewPage.getMilestoneAttribute("dueDate");
        String newModifyDate = milestoneViewPage.getMilestoneAttribute("modifyDate");
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
        String modifyDate = milestoneViewPage.getMilestoneAttribute("modifyDate");
        String dueDate = milestoneViewPage.getMilestoneAttribute("dueDate");
        milestoneViewPage.callAction(EDIT_MILESTONE_BUTTON);
        EditMilestoneWizardPage editWizard = new EditMilestoneWizardPage(driver);
        Milestone updated_dueDate = Milestone.builder().setDueDate(LocalDate.parse(dueDate).plusDays(10).toString()).build();
        // then
        editWizard.editMilestone(updated_dueDate);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        String newDueDateValue = milestoneViewPage.getMilestoneAttribute("dueDate");
        String newModifyDate = milestoneViewPage.getMilestoneAttribute("modifyDate");
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
        Milestone updated_name = Milestone.builder().setName("Edit Name").build();
        // then
        RuntimeException runtimeException = Assert.expectThrows(RuntimeException.class, () -> {
            editWizard.editMilestone(updated_name);
        });
        editWizard.cancel();
        Assert.assertEquals(runtimeException.getMessage(), "Name is not editable. You need Admin permission");

    }

    @Test(priority = 7, description = "Edit Manual Completion")
    @Description("Edit Manual Completion")
    void editManualCompletion() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        MilestoneViewPage milestoneViewPage = MilestoneViewPage.goToMilestoneViewPage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        milestoneViewPage.selectMilestone(milestoneName);
        String modifyDate = milestoneViewPage.getMilestoneAttribute("modifyDate");
        milestoneViewPage.callAction(EDIT_MILESTONE_BUTTON);
        EditMilestoneWizardPage editWizard = new EditMilestoneWizardPage(driver);
        Milestone aTrue = Milestone.builder().setIsManualCompletion("true").build();
        editWizard.editMilestone(aTrue);
        String newIsManualCompletionValue = milestoneViewPage.getMilestoneAttribute("manualCompletion");
        String newModifyDate = milestoneViewPage.getMilestoneAttribute("modifyDate");
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
        String modifyDate = milestoneViewPage.getMilestoneAttribute("modifyDate");
        String modifyUser = milestoneViewPage.getMilestoneAttribute("modifierUser.name");
        // when
        milestoneViewPage.callAction(EDIT_MILESTONE_BUTTON);
        EditMilestoneWizardPage editWizard = new EditMilestoneWizardPage(driver);
        Milestone updated_name = Milestone.builder().setName("Milestone U." + (int) (Math.random() * 1001)).build();
        editWizard.editMilestone(updated_name);
        // then
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        milestoneViewPage.selectMilestone(updated_name.getName().get());
        milestoneName = milestoneViewPage.getMilestoneAttribute("name");
        String newModifyDate = milestoneViewPage.getMilestoneAttribute("modifyDate");
        String newModifyUser = milestoneViewPage.getMilestoneAttribute("modifierUser.name");
        Assert.assertEquals(updated_name.getName().get(), milestoneName);
        Assert.assertNotEquals(newModifyDate, modifyDate);
        Assert.assertNotEquals(newModifyUser, modifyUser);
    }
}

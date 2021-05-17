/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2021 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.bpm;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.bpm.EditMilestoneWizardPage;
import com.oss.pages.bpm.Milestone;
import com.oss.pages.bpm.MilestoneViewPage;
import com.oss.pages.bpm.ProcessInstancesPage;
import com.oss.pages.bpm.ProcessWizardPage;
import com.oss.utils.TestListener;

/**
 * @author Gabriela Kasza
 */
@Listeners({TestListener.class})
public class EditMilestoneTest extends BaseTestCase {
    private static final Logger log = LoggerFactory.getLogger(EditMilestoneTest.class);
    private final static String EDIT_MILESTONE_BUTTON = "editMilestonesContextAction";
    
    private String description = "Milestone Update " + (Math.random() * 1001);
    private String milestoneName = "Milestone Update " + (Math.random() * 1001);
    private String leadTime = String.valueOf((int) (Math.random() * 101));
    
    @BeforeClass
    public void createMilestone() {
        ProcessInstancesPage processInstancesPage = ProcessInstancesPage.goToProcessInstancesPage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        //processInstancesPage.changeUser("bpm_webselenium", "bpmweb");
        String processName = "Selenium Test.Milestone-" + (int) (Math.random() * 1001);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
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
    
    @Test(priority = 1)
    public void editDescription() {
        // given
        MilestoneViewPage milestoneViewPage = MilestoneViewPage.goToMilestoneViewPage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        // when
        milestoneViewPage.selectMilestone(milestoneName);
        String modifyDate = milestoneViewPage.getValuePropertyPanel("modifyDate", 0);
        milestoneViewPage.callAction(EDIT_MILESTONE_BUTTON);
        EditMilestoneWizardPage editWizard = new EditMilestoneWizardPage(driver);
        Milestone updated_description = Milestone.builder().setDescription(description).build();
        // then
        editWizard.editMilestone(updated_description);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        String newDescriptionValue = milestoneViewPage.getValuePropertyPanel("description", 0);
        String newModifyDate = milestoneViewPage.getValuePropertyPanel("modifyDate", 0);
        Assert.assertEquals(description, newDescriptionValue);
        Assert.assertNotEquals(newModifyDate, modifyDate);
    }
    
    @Test(priority = 2)
    public void editLeadTime() {
        // given
        MilestoneViewPage milestoneViewPage = MilestoneViewPage.goToMilestoneViewPage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        // when
        milestoneViewPage.selectMilestone(milestoneName);
        String modifyDate = milestoneViewPage.getValuePropertyPanel("modifyDate", 0);
        milestoneViewPage.callAction(EDIT_MILESTONE_BUTTON);
        EditMilestoneWizardPage editWizard = new EditMilestoneWizardPage(driver);
        Milestone updated_leadTime = Milestone.builder().setLeadTime(leadTime).build();
        // then
        editWizard.editMilestone(updated_leadTime);
        String newLeadTimeValue = milestoneViewPage.getValuePropertyPanel("leadTime", 0);
        String newModifyDate = milestoneViewPage.getValuePropertyPanel("modifyDate", 0);
        Assert.assertEquals(leadTime, newLeadTimeValue);
        Assert.assertNotEquals(newModifyDate, modifyDate);
        log.info("test");
    }
    
    @Test(priority = 3)
    public void editRelatedTask() {
        MilestoneViewPage milestoneViewPage = MilestoneViewPage.goToMilestoneViewPage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        milestoneViewPage.selectMilestone(milestoneName);
        milestoneViewPage.callAction(EDIT_MILESTONE_BUTTON);
        EditMilestoneWizardPage editWizard = new EditMilestoneWizardPage(driver);
        Milestone setProcess = Milestone.builder().setRelatedTask("").build();
        editWizard.editMilestone(setProcess);
        String relatedTask = milestoneViewPage.getValuePropertyPanel("relatedTaskName", 0);
        Assert.assertEquals(relatedTask, "");
        
    }
    
    @Test(priority = 4)
    public void editDueDateEarlierDate() {
        // given
        MilestoneViewPage milestoneViewPage = MilestoneViewPage.goToMilestoneViewPage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        // when
        milestoneViewPage.selectMilestone(milestoneName);
        String modifyDate = milestoneViewPage.getValuePropertyPanel("modifyDate", 0);
        String dueDate = milestoneViewPage.getValuePropertyPanel("dueDate", 0);
        milestoneViewPage.callAction(EDIT_MILESTONE_BUTTON);
        EditMilestoneWizardPage editWizard = new EditMilestoneWizardPage(driver);
        Milestone updated_dueDate = Milestone.builder().setDueDate(LocalDate.parse(dueDate).minusDays(10).toString()).build();
        // then
        editWizard.editMilestone(updated_dueDate);
        String newDueDateValue = milestoneViewPage.getValuePropertyPanel("dueDate", 0);
        String newModifyDate = milestoneViewPage.getValuePropertyPanel("modifyDate", 0);
        Assert.assertEquals(updated_dueDate.getDueDate().get(), newDueDateValue);
        Assert.assertNotEquals(newModifyDate, modifyDate);
    }
    
    @Test(priority = 5)
    void editDueDateLaterDate() {
        // given
        MilestoneViewPage milestoneViewPage = MilestoneViewPage.goToMilestoneViewPage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        // when
        milestoneViewPage.selectMilestone(milestoneName);
        String modifyDate = milestoneViewPage.getValuePropertyPanel("modifyDate", 0);
        String dueDate = milestoneViewPage.getValuePropertyPanel("dueDate", 0);
        milestoneViewPage.callAction(EDIT_MILESTONE_BUTTON);
        EditMilestoneWizardPage editWizard = new EditMilestoneWizardPage(driver);
        Milestone updated_dueDate = Milestone.builder().setDueDate(LocalDate.parse(dueDate).plusDays(10).toString()).build();
        // then
        editWizard.editMilestone(updated_dueDate);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        String newDueDateValue = milestoneViewPage.getValuePropertyPanel("dueDate", 0);
        String newModifyDate = milestoneViewPage.getValuePropertyPanel("modifyDate", 0);
        Assert.assertEquals(updated_dueDate.getDueDate().get(), newDueDateValue);
        Assert.assertNotEquals(newModifyDate, modifyDate);
        
    }
    
    @Test(priority = 6)
    void checkIfNameIsEditable() {
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
    
    @Test(priority = 7)
    void editManualCompletion() {
        MilestoneViewPage milestoneViewPage = MilestoneViewPage.goToMilestoneViewPage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        milestoneViewPage.selectMilestone(milestoneName);
        String modifyDate = milestoneViewPage.getValuePropertyPanel("modifyDate", 0);
        milestoneViewPage.callAction(EDIT_MILESTONE_BUTTON);
        EditMilestoneWizardPage editWizard = new EditMilestoneWizardPage(driver);
        Milestone aTrue = Milestone.builder().setIsManualCompletion("true").build();
        editWizard.editMilestone(aTrue);
        String newIsManualCompletionValue = milestoneViewPage.getValuePropertyPanel("manualCompletion", 0);
        String newModifyDate = milestoneViewPage.getValuePropertyPanel("modifyDate", 0);
        Assert.assertEquals(aTrue.getIsManualCompletion().get(), newIsManualCompletionValue);
        Assert.assertNotEquals(newModifyDate, modifyDate);
    }
    
    @Test(priority = 8)
    public void checkIfModifyUserIsUpdated() {
        // given
        MilestoneViewPage milestoneViewPage = MilestoneViewPage.goToMilestoneViewPage(driver, BASIC_URL);
        milestoneViewPage.changeUser("bpm_admin_webselenium", "bpmweb");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        milestoneViewPage.selectMilestone(milestoneName);
        String modifyDate = milestoneViewPage.getValuePropertyPanel("modifyDate", 0);
        String modifyUser = milestoneViewPage.getValuePropertyPanel("modifierUser.name", 0);
        // when
        milestoneViewPage.callAction(EDIT_MILESTONE_BUTTON);
        EditMilestoneWizardPage editWizard = new EditMilestoneWizardPage(driver);
        Milestone updated_name = Milestone.builder().setName("Milestone U." + (int) (Math.random() * 1001)).build();
        editWizard.editMilestone(updated_name);
        // then
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        milestoneViewPage.selectMilestone(updated_name.getName().get());
        milestoneName = milestoneViewPage.getValuePropertyPanel("name", 0);
        String newModifyDate = milestoneViewPage.getValuePropertyPanel("modifyDate", 0);
        String newModifyUser = milestoneViewPage.getValuePropertyPanel("modifierUser.name", 0);
        Assert.assertEquals(updated_name.getName().get(), milestoneName);
        Assert.assertNotEquals(newModifyDate, modifyDate);
        Assert.assertNotEquals(newModifyUser, modifyUser);
    }
}

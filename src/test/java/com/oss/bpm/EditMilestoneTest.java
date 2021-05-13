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
    
    private String description = "Milestone Update " + (Math.random() * 1001);
    private String milestoneName = "Milestone Update " + (Math.random() * 1001);
    private String leadTime = String.valueOf((int) (Math.random() * 101));
    private String processCode;
    
    @BeforeClass
    public void createMilestone() {
        ProcessInstancesPage processInstancesPage = ProcessInstancesPage.goToProcessInstancesPage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        processInstancesPage.changeUser("bpm_webselenium", "bpmweb");
        String processName = "Selenium Test.Milestone-" + (int) (Math.random() * 1001);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        ProcessWizardPage processWizardPage = new ProcessWizardPage(driver);
        processWizardPage.definedMilestoneInProcess(processName, 5L,
                "Data Correction Process");
        Milestone milestone1 = Milestone.builder()
                .setLeadTime("10")
                .setDescription("Milestone 1 - Selenium Test")
                .setIsManualCompletion("true")
                .setIsActive("true")
                .setName(milestoneName).build();
        processWizardPage.addMilestoneRow(milestone1);
        processWizardPage.clickAcceptButton();
        processCode = processInstancesPage.getProcessCode(processName);
    }
    
    @Test
    public void editDescription() {
        // given
        MilestoneViewPage milestoneViewPage = MilestoneViewPage.goToMilestoneViewPage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        // when
        milestoneViewPage.selectMilestone(milestoneName, processCode);
        String modifyDate = milestoneViewPage.getValuePropertyPanel("modifyDate", 0);
        EditMilestoneWizardPage editWizard = new EditMilestoneWizardPage(driver);
        Milestone updated_description = Milestone.builder().setDescription(description).build();
        // then
        editWizard.editMilestone(updated_description);
        String newDescriptionValue = milestoneViewPage.getValuePropertyPanel("Description", 0);
        String newModifyDate = milestoneViewPage.getValuePropertyPanel("modifyDate", 0);
        Assert.assertEquals(description, newDescriptionValue);
        Assert.assertNotEquals(newModifyDate, modifyDate);
    }
    
    @Test
    public void editLeadTime() {
        // given
        MilestoneViewPage milestoneViewPage = MilestoneViewPage.goToMilestoneViewPage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        // when
        milestoneViewPage.selectMilestone(milestoneName, processCode);
        String modifyDate = milestoneViewPage.getValuePropertyPanel("modifyDate", 0);
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
    
    @Test
    public void editRelatedTask() {
        MilestoneViewPage milestoneViewPage = MilestoneViewPage.goToMilestoneViewPage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        milestoneViewPage.selectMilestone(milestoneName, processCode);
        String relatedTask = milestoneViewPage.getValuePropertyPanel("relatedTaskName", 0);
        Assert.assertEquals(relatedTask, "");
        
    }
    
    @Test
    public void editDueDateEarlierDate() {
        // given
        MilestoneViewPage milestoneViewPage = MilestoneViewPage.goToMilestoneViewPage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        // when
        milestoneViewPage.selectMilestone(milestoneName, processCode);
        String modifyDate = milestoneViewPage.getValuePropertyPanel("modifyDate", 0);
        String dueDate = milestoneViewPage.getValuePropertyPanel("dueDate", 0);
        EditMilestoneWizardPage editWizard = new EditMilestoneWizardPage(driver);
        Milestone updated_dueDate = Milestone.builder().setDueDate(LocalDate.parse(dueDate).minusDays(10).toString()).build();
        // then
        editWizard.editMilestone(updated_dueDate);
        String newDueDateValue = milestoneViewPage.getValuePropertyPanel("dueDate", 0);
        String newModifyDate = milestoneViewPage.getValuePropertyPanel("modifyDate", 0);
        Assert.assertEquals(updated_dueDate.getDueDate().get(), newDueDateValue);
        Assert.assertNotEquals(newModifyDate, modifyDate);
    }
    
    @Test
    void editDueDateLaterDate() {
        // given
        MilestoneViewPage milestoneViewPage = MilestoneViewPage.goToMilestoneViewPage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        // when
        milestoneViewPage.selectMilestone("Milestone 2.915", processCode);
        String modifyDate = milestoneViewPage.getValuePropertyPanel("modifyDate", 0);
        String dueDate = milestoneViewPage.getValuePropertyPanel("dueDate", 0);
        EditMilestoneWizardPage editWizard = new EditMilestoneWizardPage(driver);
        Milestone updated_dueDate = Milestone.builder().setDueDate(LocalDate.parse(dueDate).plusDays(10).toString()).build();
        // then
        editWizard.editMilestone(updated_dueDate);
        String newDueDateValue = milestoneViewPage.getValuePropertyPanel("dueDate", 0);
        String newModifyDate = milestoneViewPage.getValuePropertyPanel("modifyDate", 0);
        Assert.assertEquals(updated_dueDate.getDueDate().get(), newDueDateValue);
        Assert.assertNotEquals(newModifyDate, modifyDate);
        
    }
    
    @Test
    void checkIfNameIsEditable() {
        MilestoneViewPage milestoneViewPage = MilestoneViewPage.goToMilestoneViewPage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        // when
        milestoneViewPage.selectMilestone(milestoneName, processCode);
        EditMilestoneWizardPage editWizard = new EditMilestoneWizardPage(driver);
        Milestone updated_name = Milestone.builder().setName("Edit Name").build();
        // then
        RuntimeException runtimeException = Assert.expectThrows(RuntimeException.class, () -> {
            editWizard.editMilestone(updated_name);
        });
        Assert.assertEquals(runtimeException.getMessage(), "Name is not editable. You need Admin permission");
        
    }
    
    @Test
    public void checkIfModifyUserIsUpdated() {
        // given
        MilestoneViewPage milestoneViewPage = MilestoneViewPage.goToMilestoneViewPage(driver, BASIC_URL);
        milestoneViewPage.changeUser("bpm_admin_webselenium", "bpmweb");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        milestoneViewPage.selectMilestone(milestoneName, processCode);
        String modifyDate = milestoneViewPage.getValuePropertyPanel("modifyDate", 0);
        String modifyUser = milestoneViewPage.getValuePropertyPanel("modifierUser.name", 0);
        // when
        EditMilestoneWizardPage editWizard = new EditMilestoneWizardPage(driver);
        Milestone updated_name = Milestone.builder().setName("Milestone U." + (int) (Math.random() * 1001)).build();
        editWizard.editMilestone(updated_name);
        // then
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        milestoneViewPage.selectMilestone(updated_name.getName().get(), "DCP-2437");
        String newNameValue = milestoneViewPage.getValuePropertyPanel("name", 0);
        String newModifyDate = milestoneViewPage.getValuePropertyPanel("modifyDate", 0);
        String newModifyUser = milestoneViewPage.getValuePropertyPanel("modifierUser.name", 0);
        Assert.assertEquals(updated_name.getName().get(), newNameValue);
        Assert.assertNotEquals(newModifyDate, modifyDate);
        Assert.assertNotEquals(newModifyUser, modifyUser);
    }
}

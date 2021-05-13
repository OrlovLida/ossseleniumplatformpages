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
import com.oss.pages.bpm.Milestone;
import com.oss.pages.bpm.ProcessInstancesPage;
import com.oss.pages.bpm.ProcessWizardPage;
import com.oss.utils.TestListener;

import io.qameta.allure.Description;

/**
 * @author Gabriela Kasza
 */
@Listeners({TestListener.class})
public class CreateMilestoneWithProcessTest extends BaseTestCase {
    
    private static final Logger log = LoggerFactory.getLogger(CreateMilestoneWithProcessTest.class);
    
    private String milestoneName1 = "Milestone 1." + (int) (Math.random() * 1001);
    private String milestoneName2 = "Milestone 2." + (int) (Math.random() * 1001);
    
    @BeforeClass
    public void openProcessInstancesPage() {
        ProcessInstancesPage processInstancesPage = ProcessInstancesPage.goToProcessInstancesPage(driver, BASIC_URL);
        processInstancesPage.changeUser("bpm_webselenium", "bpmweb");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        
    }
    
    @Description("For Basic  Milestone User ")
    @Test(priority = 1)
    public void createProcessWithMilestones() {
        ProcessInstancesPage processInstancesPage = ProcessInstancesPage.goToProcessInstancesPage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        String processName = "Selenium Test.Milestone-" + (int) (Math.random() * 1001);
        ProcessWizardPage processWizardPage = new ProcessWizardPage(driver);
        processWizardPage.definedMilestoneInProcess(processName, 10L, "GK Milestones");
        Milestone milestone1 = Milestone.builder()
                .setLeadTime("0")
                .setDueDate(LocalDate.now().plusDays(5).toString())
                .setDescription("Selenium Test " + LocalDate.now().toString())
                .setIsManualCompletion("true")
                .setIsActive("true")
                .setName(milestoneName1).build();
        
        Milestone milestone2 = Milestone.builder()
                .setName(milestoneName2)
                .setRelatedTask("First Task")
                .build();
        
        Milestone milestonePredefined = Milestone.builder()
                .setIsActive("true")
                .build();
        processWizardPage.addMilestoneRow(milestone1);
        processWizardPage.addMilestoneRow(milestone2);
        Milestone milestonePredefined_1 = processWizardPage.editPredefinedMilestone(milestonePredefined, 1);
        
        String namePredefinedMilestone = milestonePredefined_1.getName().orElseThrow(() -> new RuntimeException("Missing name"));
        String nameMilestone1 = milestone1.getName().orElseThrow(() -> new RuntimeException("Missing name"));
        String nameMilestone2 = milestone2.getName().orElseThrow(() -> new RuntimeException("Missing name"));
        
        processWizardPage.clickAcceptButton();
        
        // then
        processInstancesPage.selectMilestoneTab("Name", processName);
        // Status
        String statusMilestone1 = processInstancesPage.getMilestoneValue(nameMilestone1, "Status");
        String statusMilestone2 = processInstancesPage.getMilestoneValue(nameMilestone2, "Status");
        String statusPredefinedMilestone = processInstancesPage.getMilestoneValue(namePredefinedMilestone, "Status");
        Assert.assertEquals(statusMilestone1, "New");
        Assert.assertEquals(statusMilestone2, "Not Needed");
        Assert.assertEquals(statusPredefinedMilestone, "New");
        
        // Due date
        String dueDateMilestone1 = processInstancesPage.getMilestoneValue(nameMilestone1, "Due Date");
        String dueDateMilestone2 = processInstancesPage.getMilestoneValue(nameMilestone2, "Due Date");
        String dueDatePredefinedMilestone = processInstancesPage.getMilestoneValue(namePredefinedMilestone, "Due Date");
        Assert.assertEquals(dueDateMilestone1, LocalDate.now().plusDays(5).toString());
        Assert.assertEquals(dueDateMilestone2, "");
        Assert.assertEquals(dueDatePredefinedMilestone, LocalDate.now().toString());
        
        // Date of Completion
        String leadTimePredefinedMilestone = milestonePredefined_1.getLeadTime().get();
        String completionDateMilestone1 = processInstancesPage.getMilestoneValue(nameMilestone1, "Date of Completion");
        String completionDateMilestone2 = processInstancesPage.getMilestoneValue(nameMilestone2, "Date of Completion");
        String completionDatePredefinedMilestone = processInstancesPage.getMilestoneValue(namePredefinedMilestone, "Date of Completion");
        Assert.assertEquals(completionDateMilestone1, LocalDate.now().toString());
        Assert.assertEquals(completionDateMilestone2, "");
        Assert.assertEquals(completionDatePredefinedMilestone,
                LocalDate.now().plusDays(Long.parseLong(leadTimePredefinedMilestone)).toString());
        
        // Related Object
        String relatedObjectMilestone1 = processInstancesPage.getMilestoneValue(nameMilestone1, "Related Object");
        String relatedObjectMilestone2 = processInstancesPage.getMilestoneValue(nameMilestone2, "Related Object");
        String relatedObjectPredefinedMilestone = processInstancesPage.getMilestoneValue(namePredefinedMilestone, "Related Object");
        Assert.assertEquals(relatedObjectMilestone1, "Process");
        Assert.assertEquals(relatedObjectMilestone2, "Task" + "(" + milestone2.getRelatedTask().get() + ")");
        Assert.assertEquals(relatedObjectPredefinedMilestone,
                "Task" + "(" + milestonePredefined_1.getRelatedTask().get() + ")");
        
    }
    
    @Test
    public void updatePredefinedMilestone() {
        ProcessInstancesPage processInstancesPage = ProcessInstancesPage.goToProcessInstancesPage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        String processName = "Selenium Test.Milestone-" + (int) (Math.random() * 1001);
        ProcessWizardPage processWizardPage = new ProcessWizardPage(driver);
        processWizardPage.definedMilestoneInProcess(processName, 10L, "GK Milestones");
        
        Milestone milestone1 = Milestone.builder()
                .setDueDate(LocalDate.now().toString())
                .setLeadTime("10")
                .setDescription("Update 1")
                .setIsManualCompletion("false")
                .setIsActive("false")
                .setRelatedTask("")
                .build();
        
        Milestone milestone2 = Milestone.builder()
                .setLeadTime("30")
                .setDescription("Update 2")
                .setIsManualCompletion("true")
                .setIsActive("true")
                .setRelatedTask("")
                .build();
        
        Milestone milestone3 = Milestone.builder()
                .setLeadTime("20")
                .setDescription("Update 3")
                .setIsManualCompletion("false")
                .setIsActive("false")
                .setRelatedTask("")
                .build();
        
        Milestone milestone1_updated = processWizardPage.editPredefinedMilestone(milestone1, 1);
        Milestone milestone2_updated = processWizardPage.editPredefinedMilestone(milestone2, 2);
        Milestone milestone3_updated = processWizardPage.editPredefinedMilestone(milestone3, 3);
        processWizardPage.clickAcceptButton();
        
        processInstancesPage.selectMilestoneTab("Name", processName);
        String nameMilestone1 = milestone1_updated.getName().orElseThrow(() -> new RuntimeException("Missing name"));
        String nameMilestone2 = milestone2_updated.getName().orElseThrow(() -> new RuntimeException("Missing name"));
        String nameMilestone3 = milestone3_updated.getName().orElseThrow(() -> new RuntimeException("Missing name"));
        
        // Status
        String statusMilestone1 = processInstancesPage.getMilestoneValue(nameMilestone1, "Status");
        String statusMilestone2 = processInstancesPage.getMilestoneValue(nameMilestone2, "Status");
        String statusMilestone3 = processInstancesPage.getMilestoneValue(nameMilestone3, "Status");
        Assert.assertEquals(statusMilestone1, "Not Needed");
        Assert.assertEquals(statusMilestone2, "New");
        Assert.assertEquals(statusMilestone3, "Not Needed");
        
        // Due date
        String leadTimeMilestone2 = milestone2_updated.getLeadTime().get();
        String dueDateMilestone1 = processInstancesPage.getMilestoneValue(nameMilestone1, "Due Date");
        String dueDateMilestone2 = processInstancesPage.getMilestoneValue(nameMilestone2, "Due Date");
        String dueDateMilestone3 = processInstancesPage.getMilestoneValue(nameMilestone3, "Due Date");
        Assert.assertEquals(dueDateMilestone1, LocalDate.now().toString());
        Assert.assertEquals(dueDateMilestone2, LocalDate.now().plusDays(Long.parseLong(leadTimeMilestone2)).toString());
        Assert.assertEquals(dueDateMilestone3, "");
        
        // Date of Completion
        String completionDateMilestone1 = processInstancesPage.getMilestoneValue(nameMilestone1, "Date of Completion");
        String completionDateMilestone2 = processInstancesPage.getMilestoneValue(nameMilestone2, "Date of Completion");
        String completionDateMilestone3 = processInstancesPage.getMilestoneValue(nameMilestone3, "Date of Completion");
        
        Assert.assertEquals(completionDateMilestone1, "");
        Assert.assertEquals(completionDateMilestone2, LocalDate.now().plusDays(Long.parseLong(leadTimeMilestone2)).toString());
        Assert.assertEquals(completionDateMilestone3, "");
        
        // Related Object
        String relatedObjectMilestone1 = processInstancesPage.getMilestoneValue(nameMilestone1, "Related Object");
        String relatedObjectMilestone2 = processInstancesPage.getMilestoneValue(nameMilestone2, "Related Object");
        String relatedObjectMilestone3 = processInstancesPage.getMilestoneValue(nameMilestone3, "Related Object");
        Assert.assertEquals(relatedObjectMilestone1, "Process");
        Assert.assertEquals(relatedObjectMilestone2, "Process");
        Assert.assertEquals(relatedObjectMilestone3, "Process");
        
        // Description
        String descriptionMilestone1 = processInstancesPage.getMilestoneValue(nameMilestone1, "Description");
        String descriptionMilestone2 = processInstancesPage.getMilestoneValue(nameMilestone2, "Description");
        String descriptionMilestone3 = processInstancesPage.getMilestoneValue(nameMilestone3, "Description");
        Assert.assertEquals(descriptionMilestone1, "Update 1");
        Assert.assertEquals(descriptionMilestone2, "Update 2");
        Assert.assertEquals(descriptionMilestone3, "Update 3");
    }
    
    @Test
    public void addMilestoneForDCP() {
        ProcessInstancesPage processInstancesPage = ProcessInstancesPage.goToProcessInstancesPage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        ProcessWizardPage processWizardPage = new ProcessWizardPage(driver);
        String processName = "Selenium Test.Milestone-" + (int) (Math.random() * 1001);
        processWizardPage.definedMilestoneInProcess(processName, 10L, "Data Correction Process");
        Milestone milestone1 = Milestone.builder()
                .setLeadTime("10")
                .setDescription("Milestone 1 - Selenium Test")
                .setIsManualCompletion("true")
                .setIsActive("true")
                .setName(milestoneName1).build();
        
        Milestone milestone2 = Milestone.builder()
                .setDueDate(LocalDate.now().plusDays(5).toString())
                .setName(milestoneName2)
                .setRelatedTask("Correct data")
                .setIsManualCompletion("true")
                .setIsActive("false")
                .build();
        
        processWizardPage.addMilestoneRow(milestone1);
        processWizardPage.addMilestoneRow(milestone2);
        processWizardPage.clickAcceptButton();

        processInstancesPage.selectMilestoneTab("Name",processName);
        
        String nameMilestone1 = milestone1.getName().orElseThrow(() -> new RuntimeException("Missing Name"));
        String nameMilestone2 = milestone2.getName().orElseThrow(() -> new RuntimeException("Missing Name"));
        
        // Status
        String statusMilestone1 = processInstancesPage.getMilestoneValue(nameMilestone1, "Status");
        String statusMilestone2 = processInstancesPage.getMilestoneValue(nameMilestone2, "Status");
        Assert.assertEquals(statusMilestone1, "New");
        Assert.assertEquals(statusMilestone2, "Not Needed");
        
        // Due date
        String leadTimeMilestone1 = milestone1.getLeadTime().get();
        String dueDateMilestone1 = processInstancesPage.getMilestoneValue(nameMilestone1, "Due Date");
        String dueDateMilestone2 = processInstancesPage.getMilestoneValue(nameMilestone2, "Due Date");
        Assert.assertEquals(dueDateMilestone1, LocalDate.now().plusDays(Long.parseLong(leadTimeMilestone1)).toString());
        Assert.assertEquals(dueDateMilestone2, LocalDate.now().plusDays(5).toString());
        
        // Date of Completion
        String completionDateMilestone1 = processInstancesPage.getMilestoneValue(nameMilestone1, "Date of Completion");
        String completionDateMilestone2 = processInstancesPage.getMilestoneValue(nameMilestone2, "Date of Completion");
        Assert.assertEquals(completionDateMilestone1, LocalDate.now().plusDays(Long.parseLong(leadTimeMilestone1)).toString());
        Assert.assertEquals(completionDateMilestone2, "");
        
        // Related Object
        String relatedObjectMilestone1 = processInstancesPage.getMilestoneValue(nameMilestone1, "Related Object");
        String relatedObjectMilestone2 = processInstancesPage.getMilestoneValue(nameMilestone2, "Related Object");
        Assert.assertEquals(relatedObjectMilestone1, "Process");
        Assert.assertEquals(relatedObjectMilestone2, "Task" + "(" + milestone2.getRelatedTask().get() + ")");
        
        // Description
        String descriptionMilestone1 = processInstancesPage.getMilestoneValue(nameMilestone1, "Description");
        String descriptionMilestone2 = processInstancesPage.getMilestoneValue(nameMilestone2, "Description");
        Assert.assertEquals(descriptionMilestone1, "Milestone 1 - Selenium Test");
        Assert.assertEquals(descriptionMilestone2, "");
        
    }
    
    @Test
    public void checkIfNameIsNotEditableForPredefinedMilestone() {
        ProcessInstancesPage.goToProcessInstancesPage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        ProcessWizardPage processWizardPage = new ProcessWizardPage(driver);
        processWizardPage.definedMilestoneInProcess("Milestone Process", 0L, "GK Milestones");
        boolean isEditable = processWizardPage.getMilestonePredefinedList().selectRow(0).isEditableAttribute("name");
        processWizardPage.clickCancelButton();
        Assert.assertFalse(isEditable);
        
    }
    
}

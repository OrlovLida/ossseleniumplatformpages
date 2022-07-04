/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2021 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.bpm.milestones;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.components.mainheader.ToolbarWidget;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.bpm.milestones.Milestone;
import com.oss.pages.bpm.processinstances.ProcessInstancesPage;
import com.oss.pages.bpm.processinstances.ProcessWizardPage;
import com.oss.utils.TestListener;

import io.qameta.allure.Description;
import org.testng.collections.Lists;

/**
 * @author Gabriela Kasza
 */
@Listeners({TestListener.class})
public class CreateMilestoneWithProcessTest extends BaseTestCase {
    private static final String BPM_USER_LOGIN = "bpm_webselenium";
    private static final String BPM_USER_PASSWORD = "Webtests123!";
    private static final String BPM_ADMIN_USER_LOGIN = "bpm_admin_webselenium";
    private static final String BPM_ADMIN_USER_PASSWORD = "Webtests123!";

    private static final String MILESTONE_DESCRIPTION = "Selenium Test " + LocalDate.now();
    private static final String PROCESS_NAME = "Selenium Test.Milestone-";
    private static final String GK_MILESTONES = "GK Milestones";
    private static final String MISSING_NAME_EXCEPTION = "Missing name";
    private static final String NAME_LABEL = "Name";
    private static final String STATUS_LABEL = "Status";
    private static final String FIRST_TASK_NAME = "First Task";
    private static final String NEW_STATUS = "New";
    private static final String NOT_NEEDED_STATUS = "Not Needed";
    private static final String DUE_DATE_LABEL = "Due Date";
    private static final String COMPLETION_DATE_LABEL = "Date of Completion";
    private static final String RELATED_OBJECT_LABEL = "Related Object";
    private static final String PROCESS_LABEL = "Process";
    private static final String TASK_LABEL = "Task";
    private static final String DESCRIPTION_LABEL = "Description";
    private static final String DCP = "Data Correction Process";
    private static final String CORRECT_DATA_TASK = "Correct data";
    private static final String DESCRIPTION_EDIT_1 = "Milestone 1 - Selenium Test";
    private static final String CODE_LABEL = "Code";
    private static final String UPDATE_DESCRIPTION_1 = "Update 1";
    private static final String UPDATE_DESCRIPTION_2 = "Update 2";
    private static final String UPDATE_DESCRIPTION_3 = "Update 3";
    private static final String NAME_COLUMN_ID = "name";


    private final String milestoneName1 = "Milestone 1." + (int) (Math.random() * 100001);
    private final String milestoneName2 = "Milestone 2." + (int) (Math.random() * 100001);

    @BeforeClass
    public void openProcessInstancesPage() {
        ProcessInstancesPage processInstancesPage = ProcessInstancesPage.goToProcessInstancesPage(driver, BASIC_URL);

        ToolbarWidget toolbarWidget = ToolbarWidget.create(driver, webDriverWait);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        if (!toolbarWidget.getUserName().equals(BPM_USER_LOGIN)) {
            processInstancesPage.changeUser(BPM_USER_LOGIN, BPM_USER_PASSWORD);
        }
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 1, description = "Create Process with Milestone")
    @Description("Create Process with Milestone")
    public void createProcessWithMilestones() {
        ProcessInstancesPage processInstancesPage = ProcessInstancesPage.goToProcessInstancesPage(driver, BASIC_URL);
        processInstancesPage.clearAllColumnFilters();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        String processName = PROCESS_NAME + (int) (Math.random() * 100001);
        ProcessWizardPage processWizardPage = processInstancesPage.openProcessCreationWizard();
        ProcessWizardPage.MilestoneStepWizard milestoneStep =
                processWizardPage.definedMilestoneInProcess(processName, 10L, GK_MILESTONES);
        Milestone milestone1 = Milestone.builder()
                .setLeadTime("0")
                .setDueDate(LocalDate.now().plusDays(5).toString())
                .setDescription(MILESTONE_DESCRIPTION)
                .setIsManualCompletion("true")
                .setIsActive("true")
                .setName(milestoneName1).build();

        Milestone milestone2 = Milestone.builder()
                .setName(milestoneName2)
                .setRelatedTask(FIRST_TASK_NAME)
                .build();

        Milestone milestonePredefined = Milestone.builder()
                .setIsActive("true")
                .build();
        milestoneStep.addMilestoneRow(milestone1);
        milestoneStep.addMilestoneRow(milestone2);
        Milestone milestonePredefined_1 = milestoneStep.editPredefinedMilestone(milestonePredefined, 1);

        String namePredefinedMilestone = milestonePredefined_1.getName().orElseThrow(() -> new RuntimeException(MISSING_NAME_EXCEPTION));
        String nameMilestone1 = milestone1.getName().orElseThrow(() -> new RuntimeException(MISSING_NAME_EXCEPTION));
        String nameMilestone2 = milestone2.getName().orElseThrow(() -> new RuntimeException(MISSING_NAME_EXCEPTION));

        processWizardPage.clickAcceptButton();

        // then
        processInstancesPage.selectMilestoneTab(NAME_LABEL, processName);
        // Status
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        String statusMilestone1 = processInstancesPage.getMilestoneValue(nameMilestone1, STATUS_LABEL);
        String statusMilestone2 = processInstancesPage.getMilestoneValue(nameMilestone2, STATUS_LABEL);
        String statusPredefinedMilestone = processInstancesPage.getMilestoneValue(namePredefinedMilestone, STATUS_LABEL);
        Assert.assertEquals(statusMilestone1, NEW_STATUS);
        Assert.assertEquals(statusMilestone2, NOT_NEEDED_STATUS);
        Assert.assertEquals(statusPredefinedMilestone, NEW_STATUS);

        // Due date
        String dueDateMilestone1 = processInstancesPage.getMilestoneValue(nameMilestone1, DUE_DATE_LABEL);
        String dueDateMilestone2 = processInstancesPage.getMilestoneValue(nameMilestone2, DUE_DATE_LABEL);
        String dueDatePredefinedMilestone = processInstancesPage.getMilestoneValue(namePredefinedMilestone, DUE_DATE_LABEL);
        Assert.assertEquals(dueDateMilestone1, LocalDate.now().plusDays(5).toString());
        Assert.assertEquals(dueDateMilestone2, "");
        Assert.assertEquals(dueDatePredefinedMilestone, LocalDate.now().toString());

        // Date of Completion
        String leadTimePredefinedMilestone = milestonePredefined_1.getLeadTime().get();
        String completionDateMilestone1 = processInstancesPage.getMilestoneValue(nameMilestone1, COMPLETION_DATE_LABEL);
        String completionDateMilestone2 = processInstancesPage.getMilestoneValue(nameMilestone2, COMPLETION_DATE_LABEL);
        String completionDatePredefinedMilestone = processInstancesPage.getMilestoneValue(namePredefinedMilestone, COMPLETION_DATE_LABEL);
        Assert.assertEquals(completionDateMilestone1, LocalDate.now().toString());
        Assert.assertEquals(completionDateMilestone2, "");
        Assert.assertEquals(completionDatePredefinedMilestone,
                LocalDate.now().plusDays(Long.parseLong(leadTimePredefinedMilestone)).toString());

        // Related Object
        String relatedObjectMilestone1 = processInstancesPage.getMilestoneValue(nameMilestone1, RELATED_OBJECT_LABEL);
        String relatedObjectMilestone2 = processInstancesPage.getMilestoneValue(nameMilestone2, RELATED_OBJECT_LABEL);
        String relatedObjectPredefinedMilestone = processInstancesPage.getMilestoneValue(namePredefinedMilestone, RELATED_OBJECT_LABEL);
        Assert.assertEquals(relatedObjectMilestone1, PROCESS_LABEL);
        Assert.assertEquals(relatedObjectMilestone2, TASK_LABEL + "(" + milestone2.getRelatedTask().get() + ")");
        Assert.assertEquals(relatedObjectPredefinedMilestone,
                TASK_LABEL + "(" + milestonePredefined_1.getRelatedTask().get() + ")");
    }

    @Test(priority = 2, description = "Update Predefined Milestone")
    @Description("Update Predefined Milestone")
    public void updatePredefinedMilestone() {
        ProcessInstancesPage processInstancesPage = ProcessInstancesPage.goToProcessInstancesPage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        String processName = PROCESS_NAME + (int) (Math.random() * 100001);
        ProcessWizardPage processWizardPage = processInstancesPage.openProcessCreationWizard();
        ProcessWizardPage.MilestoneStepWizard milestoneStepWizard =
                processWizardPage.definedMilestoneInProcess(processName, 10L, GK_MILESTONES);

        Milestone milestone1 = Milestone.builder()
                .setDueDate(LocalDate.now().toString())
                .setLeadTime("10")
                .setDescription(UPDATE_DESCRIPTION_1)
                .setIsManualCompletion("false")
                .setIsActive("false")
                .setRelatedTask("")
                .build();

        Milestone milestone2 = Milestone.builder()
                .setLeadTime("30")
                .setDescription(UPDATE_DESCRIPTION_2)
                .setIsManualCompletion("true")
                .setIsActive("true")
                .setRelatedTask("")
                .build();

        Milestone milestone3 = Milestone.builder()
                .setLeadTime("20")
                .setDescription(UPDATE_DESCRIPTION_3)
                .setIsManualCompletion("false")
                .setIsActive("false")
                .setRelatedTask("")
                .build();

        Milestone milestone1_updated = milestoneStepWizard.editPredefinedMilestone(milestone1, 1);
        Milestone milestone2_updated = milestoneStepWizard.editPredefinedMilestone(milestone2, 2);
        Milestone milestone3_updated = milestoneStepWizard.editPredefinedMilestone(milestone3, 3);
        milestoneStepWizard.clickAcceptButton();

        processInstancesPage.selectMilestoneTab(NAME_LABEL, processName);
        String nameMilestone1 = milestone1_updated.getName().orElseThrow(() -> new RuntimeException(MISSING_NAME_EXCEPTION));
        String nameMilestone2 = milestone2_updated.getName().orElseThrow(() -> new RuntimeException(MISSING_NAME_EXCEPTION));
        String nameMilestone3 = milestone3_updated.getName().orElseThrow(() -> new RuntimeException(MISSING_NAME_EXCEPTION));

        // Status
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        String statusMilestone1 = processInstancesPage.getMilestoneValue(nameMilestone1, STATUS_LABEL);
        String statusMilestone2 = processInstancesPage.getMilestoneValue(nameMilestone2, STATUS_LABEL);
        String statusMilestone3 = processInstancesPage.getMilestoneValue(nameMilestone3, STATUS_LABEL);
        Assert.assertEquals(statusMilestone1, NOT_NEEDED_STATUS);
        Assert.assertEquals(statusMilestone2, NEW_STATUS);
        Assert.assertEquals(statusMilestone3, NOT_NEEDED_STATUS);

        // Due date
        String leadTimeMilestone2 = milestone2_updated.getLeadTime().get();
        String dueDateMilestone1 = processInstancesPage.getMilestoneValue(nameMilestone1, DUE_DATE_LABEL);
        String dueDateMilestone2 = processInstancesPage.getMilestoneValue(nameMilestone2, DUE_DATE_LABEL);
        String dueDateMilestone3 = processInstancesPage.getMilestoneValue(nameMilestone3, DUE_DATE_LABEL);
        Assert.assertEquals(dueDateMilestone1, LocalDate.now().toString());
        Assert.assertEquals(dueDateMilestone2, LocalDate.now().plusDays(Long.parseLong(leadTimeMilestone2)).toString());
        Assert.assertEquals(dueDateMilestone3, "");

        // Date of Completion
        String completionDateMilestone1 = processInstancesPage.getMilestoneValue(nameMilestone1, COMPLETION_DATE_LABEL);
        String completionDateMilestone2 = processInstancesPage.getMilestoneValue(nameMilestone2, COMPLETION_DATE_LABEL);
        String completionDateMilestone3 = processInstancesPage.getMilestoneValue(nameMilestone3, COMPLETION_DATE_LABEL);

        Assert.assertEquals(completionDateMilestone1, "");
        Assert.assertEquals(completionDateMilestone2, LocalDate.now().plusDays(Long.parseLong(leadTimeMilestone2)).toString());
        Assert.assertEquals(completionDateMilestone3, "");

        // Related Object
        String relatedObjectMilestone1 = processInstancesPage.getMilestoneValue(nameMilestone1, RELATED_OBJECT_LABEL);
        String relatedObjectMilestone2 = processInstancesPage.getMilestoneValue(nameMilestone2, RELATED_OBJECT_LABEL);
        String relatedObjectMilestone3 = processInstancesPage.getMilestoneValue(nameMilestone3, RELATED_OBJECT_LABEL);
        Assert.assertEquals(relatedObjectMilestone1, PROCESS_LABEL);
        Assert.assertEquals(relatedObjectMilestone2, PROCESS_LABEL);
        Assert.assertEquals(relatedObjectMilestone3, PROCESS_LABEL);

        // Description
        String descriptionMilestone1 = processInstancesPage.getMilestoneValue(nameMilestone1, DESCRIPTION_LABEL);
        String descriptionMilestone2 = processInstancesPage.getMilestoneValue(nameMilestone2, DESCRIPTION_LABEL);
        String descriptionMilestone3 = processInstancesPage.getMilestoneValue(nameMilestone3, DESCRIPTION_LABEL);
        Assert.assertEquals(descriptionMilestone1, UPDATE_DESCRIPTION_1);
        Assert.assertEquals(descriptionMilestone2, UPDATE_DESCRIPTION_2);
        Assert.assertEquals(descriptionMilestone3, UPDATE_DESCRIPTION_3);
    }

    @Test(priority = 3, description = "Add Milestone for Data Correction Process")
    @Description("Add Milestone for Data Correction Process")
    public void addMilestoneForDCP() {
        ProcessInstancesPage processInstancesPage = ProcessInstancesPage.goToProcessInstancesPage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        String processName = PROCESS_NAME + (int) (Math.random() * 100001);
        ProcessWizardPage processWizardPage = processInstancesPage.openProcessCreationWizard();
        ProcessWizardPage.MilestoneStepWizard milestoneStepWizard =
                processWizardPage.definedMilestoneInProcess(processName, 10L, DCP);
        Milestone milestone1 = Milestone.builder()
                .setLeadTime("10")
                .setDescription(DESCRIPTION_EDIT_1)
                .setIsManualCompletion("true")
                .setIsActive("true")
                .setName(milestoneName1).build();

        Milestone milestone2 = Milestone.builder()
                .setDueDate(LocalDate.now().plusDays(5).toString())
                .setName(milestoneName2)
                .setRelatedTask(CORRECT_DATA_TASK)
                .setIsManualCompletion("true")
                .setIsActive("false")
                .build();

        milestoneStepWizard.addMilestoneRow(milestone1);
        milestoneStepWizard.addMilestoneRow(milestone2);
        processWizardPage.clickAcceptButton();

        processInstancesPage.selectMilestoneTab(NAME_LABEL, processName);

        String nameMilestone1 = milestone1.getName().orElseThrow(() -> new RuntimeException(MISSING_NAME_EXCEPTION));
        String nameMilestone2 = milestone2.getName().orElseThrow(() -> new RuntimeException(MISSING_NAME_EXCEPTION));

        // Status
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        String statusMilestone1 = processInstancesPage.getMilestoneValue(nameMilestone1, STATUS_LABEL);
        String statusMilestone2 = processInstancesPage.getMilestoneValue(nameMilestone2, STATUS_LABEL);
        Assert.assertEquals(statusMilestone1, NEW_STATUS);
        Assert.assertEquals(statusMilestone2, NOT_NEEDED_STATUS);

        // Due date
        String leadTimeMilestone1 = milestone1.getLeadTime().get();
        String dueDateMilestone1 = processInstancesPage.getMilestoneValue(nameMilestone1, DUE_DATE_LABEL);
        String dueDateMilestone2 = processInstancesPage.getMilestoneValue(nameMilestone2, DUE_DATE_LABEL);
        Assert.assertEquals(dueDateMilestone1, LocalDate.now().plusDays(Long.parseLong(leadTimeMilestone1)).toString());
        Assert.assertEquals(dueDateMilestone2, LocalDate.now().plusDays(5).toString());

        // Date of Completion
        String completionDateMilestone1 = processInstancesPage.getMilestoneValue(nameMilestone1, COMPLETION_DATE_LABEL);
        String completionDateMilestone2 = processInstancesPage.getMilestoneValue(nameMilestone2, COMPLETION_DATE_LABEL);
        Assert.assertEquals(completionDateMilestone1, LocalDate.now().plusDays(Long.parseLong(leadTimeMilestone1)).toString());
        Assert.assertEquals(completionDateMilestone2, "");

        // Related Object
        String relatedObjectMilestone1 = processInstancesPage.getMilestoneValue(nameMilestone1, RELATED_OBJECT_LABEL);
        String relatedObjectMilestone2 = processInstancesPage.getMilestoneValue(nameMilestone2, RELATED_OBJECT_LABEL);
        Assert.assertEquals(relatedObjectMilestone1, PROCESS_LABEL);
        Assert.assertEquals(relatedObjectMilestone2, TASK_LABEL + "(" + milestone2.getRelatedTask().get() + ")");

        // Description
        String descriptionMilestone1 = processInstancesPage.getMilestoneValue(nameMilestone1, DESCRIPTION_LABEL);
        String descriptionMilestone2 = processInstancesPage.getMilestoneValue(nameMilestone2, DESCRIPTION_LABEL);
        Assert.assertEquals(descriptionMilestone1, DESCRIPTION_EDIT_1);
        Assert.assertEquals(descriptionMilestone2, "");
    }

    @Test(priority = 4, description = "Add Milestone for existing Data Correction Process")
    @Description("Add Milestone for existing Data Correction Process")
    public void addMilestoneForExistingProcess() {
        ProcessInstancesPage processInstancesPage = ProcessInstancesPage.goToProcessInstancesPage(driver, BASIC_URL);
        processInstancesPage.clearAllColumnFilters();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        List<Milestone> milestones = Lists.newArrayList(
                Milestone.builder()
                        .setLeadTime("10")
                        .setDescription(DESCRIPTION_EDIT_1)
                        .setIsManualCompletion("true")
                        .setIsActive("true")
                        .setName(milestoneName1).build(),

                Milestone.builder()
                        .setDueDate(LocalDate.now().plusDays(5).toString())
                        .setName(milestoneName2)
                        .setRelatedTask(CORRECT_DATA_TASK)
                        .setIsManualCompletion("true")
                        .setIsActive("false")
                        .build());

        String processDCPCode = processInstancesPage.createSimpleDCP();

        processInstancesPage.addMilestonesForProcess(processDCPCode, milestones);

        processInstancesPage.selectMilestoneTab(CODE_LABEL, processDCPCode);

        String nameMilestone1 = milestones.get(0).getName().orElseThrow(() -> new RuntimeException(MISSING_NAME_EXCEPTION));
        String nameMilestone2 = milestones.get(1).getName().orElseThrow(() -> new RuntimeException(MISSING_NAME_EXCEPTION));

        // Status
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        String statusMilestone1 = processInstancesPage.getMilestoneValue(nameMilestone1, STATUS_LABEL);
        String statusMilestone2 = processInstancesPage.getMilestoneValue(nameMilestone2, STATUS_LABEL);
        Assert.assertEquals(statusMilestone1, NEW_STATUS);
        Assert.assertEquals(statusMilestone2, NOT_NEEDED_STATUS);

        // Due date
        String leadTimeMilestone1 = milestones.get(0).getLeadTime().get();
        String dueDateMilestone1 = processInstancesPage.getMilestoneValue(nameMilestone1, DUE_DATE_LABEL);
        String dueDateMilestone2 = processInstancesPage.getMilestoneValue(nameMilestone2, DUE_DATE_LABEL);
        Assert.assertEquals(dueDateMilestone1, LocalDate.now().plusDays(Long.parseLong(leadTimeMilestone1)).toString());
        Assert.assertEquals(dueDateMilestone2, LocalDate.now().plusDays(5).toString());

        // Date of Completion
        String completionDateMilestone1 = processInstancesPage.getMilestoneValue(nameMilestone1, COMPLETION_DATE_LABEL);
        String completionDateMilestone2 = processInstancesPage.getMilestoneValue(nameMilestone2, COMPLETION_DATE_LABEL);
        Assert.assertEquals(completionDateMilestone1, LocalDate.now().plusDays(Long.parseLong(leadTimeMilestone1)).toString());
        Assert.assertEquals(completionDateMilestone2, "");

        // Related Object
        String relatedObjectMilestone1 = processInstancesPage.getMilestoneValue(nameMilestone1, RELATED_OBJECT_LABEL);
        String relatedObjectMilestone2 = processInstancesPage.getMilestoneValue(nameMilestone2, RELATED_OBJECT_LABEL);
        Assert.assertEquals(relatedObjectMilestone1, PROCESS_LABEL);
        Assert.assertEquals(relatedObjectMilestone2, TASK_LABEL + "(" + milestones.get(1).getRelatedTask().get() + ")");

        // Description
        String descriptionMilestone1 = processInstancesPage.getMilestoneValue(nameMilestone1, DESCRIPTION_LABEL);
        String descriptionMilestone2 = processInstancesPage.getMilestoneValue(nameMilestone2, DESCRIPTION_LABEL);
        Assert.assertEquals(descriptionMilestone1, DESCRIPTION_EDIT_1);
        Assert.assertEquals(descriptionMilestone2, "");
    }

    @Test(priority = 5, description = "Check if Name is not editable for predefined Milestone")
    @Description("Check if Name is not editable for predefined Milestone")
    public void checkIfNameIsNotEditableForPredefinedMilestone() {
        ProcessInstancesPage processInstancesPage = ProcessInstancesPage.goToProcessInstancesPage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        ProcessWizardPage processWizardPage = processInstancesPage.openProcessCreationWizard();
        ProcessWizardPage.MilestoneStepWizard milestoneStepWizard =
                processWizardPage.definedMilestoneInProcess(PROCESS_NAME, 0L, GK_MILESTONES);
        boolean isEditable = milestoneStepWizard.getMilestonePredefinedList().getRow(0).isAttributeEditable(NAME_COLUMN_ID);
        processWizardPage.clickCancelButton();
        Assert.assertFalse(isEditable);
    }

}

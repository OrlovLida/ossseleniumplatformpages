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
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.bpm.milestones.Milestone;
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
import org.testng.collections.Lists;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.oss.bpm.BpmPhysicalDataCreator.nextMaxInt;

/**
 * @author Gabriela Kasza
 */
@Listeners({TestListener.class})
public class CreateMilestoneWithProcessTest extends BaseTestCase {
    private static final String TC1 = "createProcessWithMilestones";
    private static final String TC2 = "updatePredefinedMilestone";
    private static final String TC3 = "addMilestoneForDCP";
    private static final String TC4 = "addMilestoneForExistingProcess";

    private static final String PROCESS_CREATED_MESSAGE_PATTERN = "Process %1$s (%2$s) was created";
    private static final String INVALID_PROCESS_CREATION_MESSAGE = "Invalid process creation system message message in '%s' test";
    private static final String INVALID_MILESTONE_ATTRIBUTE_PATTERN = "Invalid '%1$s' milestone '%2$s' attribute in '%3$s' test.";

    private static final String MILESTONE_DESCRIPTION = "Selenium Test " + LocalDate.now();
    private static final String PROCESS_NAME = "Create Milestones with Process Test ";
    private static final String MILESTONE_NAME = "Create Milestones with Process Test ";
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
    private static final String UPDATE_DESCRIPTION_1 = "Update 1";
    private static final String UPDATE_DESCRIPTION_2 = "Update 2";
    private static final String UPDATE_DESCRIPTION_3 = "Update 3";
    private static final String TERMINATE_REASON = "Selenium Termination Process After Tests.";
    private final String processNameTC1 = PROCESS_NAME + "TC1." + nextMaxInt();
    private final String processNameTC2 = PROCESS_NAME + "TC2." + nextMaxInt();
    private final String processNameTC3 = PROCESS_NAME + "TC3." + nextMaxInt();
    private final String processNameTC4 = PROCESS_NAME + "TC4." + nextMaxInt();
    private SoftAssert softAssert;

    @BeforeClass
    public void openProcessInstancesPage() {
        softAssert = new SoftAssert();
        ProcessOverviewPage.goToProcessOverviewPage(driver, BASIC_URL).clearAllColumnFilters();
    }

    @Test(priority = 1, description = "Create Process with Milestone")
    @Description("Create Process with Milestone")
    public void createProcessWithMilestones() {
        final String milestoneName1 = MILESTONE_NAME + "TC1.1." + nextMaxInt();
        final String milestoneName2 = MILESTONE_NAME + "TC1.2." + nextMaxInt();
        ProcessOverviewPage processOverviewPage = ProcessOverviewPage.goToProcessOverviewPage(driver, BASIC_URL);
        MilestonesStepWizardPage milestonesStepWizardPage = processOverviewPage.openProcessCreationWizard()
                .defineProcessAndGoToMilestonesStep(processNameTC1, 10L, GK_MILESTONES);
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
        milestonesStepWizardPage.addMilestoneRow(milestone1);
        milestonesStepWizardPage.addMilestoneRow(milestone2);
        Milestone milestonePredefined_1 = milestonesStepWizardPage.editPredefinedMilestone(milestonePredefined, 1);

        String namePredefinedMilestone = milestonePredefined_1.getName().orElseThrow(() -> new RuntimeException(MISSING_NAME_EXCEPTION));
        String nameMilestone1 = milestone1.getName().orElseThrow(() -> new RuntimeException(MISSING_NAME_EXCEPTION));
        String nameMilestone2 = milestone2.getName().orElseThrow(() -> new RuntimeException(MISSING_NAME_EXCEPTION));

        milestonesStepWizardPage.clickAcceptButton();

        assertSystemMessage(String.format(PROCESS_CREATED_MESSAGE_PATTERN, processNameTC1, milestonesStepWizardPage.extractProcessCode()),
                SystemMessageContainer.MessageType.SUCCESS, String.format(INVALID_PROCESS_CREATION_MESSAGE, TC1));

        // then
        processOverviewPage.selectProcess(NAME_LABEL, processNameTC1).openMilestoneTab();
        // Status
        waitForPageToLoad();
        String statusMilestone1 = processOverviewPage.getMilestoneValue(nameMilestone1, STATUS_LABEL);
        String statusMilestone2 = processOverviewPage.getMilestoneValue(nameMilestone2, STATUS_LABEL);
        String statusPredefinedMilestone = processOverviewPage.getMilestoneValue(namePredefinedMilestone, STATUS_LABEL);
        Assert.assertEquals(statusMilestone1, NEW_STATUS,
                String.format(INVALID_MILESTONE_ATTRIBUTE_PATTERN, nameMilestone1, STATUS_LABEL, TC1));
        Assert.assertEquals(statusMilestone2, NOT_NEEDED_STATUS,
                String.format(INVALID_MILESTONE_ATTRIBUTE_PATTERN, nameMilestone2, STATUS_LABEL, TC1));
        Assert.assertEquals(statusPredefinedMilestone, NEW_STATUS,
                String.format(INVALID_MILESTONE_ATTRIBUTE_PATTERN, namePredefinedMilestone, STATUS_LABEL, TC1));

        // Due date
        String dueDateMilestone1 = processOverviewPage.getMilestoneValue(nameMilestone1, DUE_DATE_LABEL);
        String dueDateMilestone2 = processOverviewPage.getMilestoneValue(nameMilestone2, DUE_DATE_LABEL);
        String dueDatePredefinedMilestone = processOverviewPage.getMilestoneValue(namePredefinedMilestone, DUE_DATE_LABEL);
        Assert.assertEquals(dueDateMilestone1, LocalDate.now().plusDays(5).toString(),
                String.format(INVALID_MILESTONE_ATTRIBUTE_PATTERN, nameMilestone1, DUE_DATE_LABEL, TC1));
        Assert.assertEquals(dueDateMilestone2, "",
                String.format(INVALID_MILESTONE_ATTRIBUTE_PATTERN, nameMilestone2, DUE_DATE_LABEL, TC1));
        Assert.assertEquals(dueDatePredefinedMilestone, LocalDate.now().toString(),
                String.format(INVALID_MILESTONE_ATTRIBUTE_PATTERN, namePredefinedMilestone, DUE_DATE_LABEL, TC1));

        // Date of Completion
        String leadTimePredefinedMilestone = milestonePredefined_1.getLeadTime().get();
        String completionDateMilestone1 = processOverviewPage.getMilestoneValue(nameMilestone1, COMPLETION_DATE_LABEL);
        String completionDateMilestone2 = processOverviewPage.getMilestoneValue(nameMilestone2, COMPLETION_DATE_LABEL);
        String completionDatePredefinedMilestone = processOverviewPage.getMilestoneValue(namePredefinedMilestone, COMPLETION_DATE_LABEL);
        Assert.assertEquals(completionDateMilestone1, LocalDate.now().toString(),
                String.format(INVALID_MILESTONE_ATTRIBUTE_PATTERN, nameMilestone1, COMPLETION_DATE_LABEL, TC1));
        Assert.assertEquals(completionDateMilestone2, "",
                String.format(INVALID_MILESTONE_ATTRIBUTE_PATTERN, nameMilestone2, COMPLETION_DATE_LABEL, TC1));
        Assert.assertEquals(completionDatePredefinedMilestone,
                LocalDate.now().plusDays(Long.parseLong(leadTimePredefinedMilestone)).toString(),
                String.format(INVALID_MILESTONE_ATTRIBUTE_PATTERN, namePredefinedMilestone, COMPLETION_DATE_LABEL, TC1));

        // Related Object
        String relatedObjectMilestone1 = processOverviewPage.getMilestoneValue(nameMilestone1, RELATED_OBJECT_LABEL);
        String relatedObjectMilestone2 = processOverviewPage.getMilestoneValue(nameMilestone2, RELATED_OBJECT_LABEL);
        String relatedObjectPredefinedMilestone = processOverviewPage.getMilestoneValue(namePredefinedMilestone, RELATED_OBJECT_LABEL);
        Assert.assertEquals(relatedObjectMilestone1, PROCESS_LABEL,
                String.format(INVALID_MILESTONE_ATTRIBUTE_PATTERN, nameMilestone1, RELATED_OBJECT_LABEL, TC1));
        Assert.assertEquals(relatedObjectMilestone2, TASK_LABEL + "(" + milestone2.getRelatedTask().get() + ")",
                String.format(INVALID_MILESTONE_ATTRIBUTE_PATTERN, nameMilestone2, RELATED_OBJECT_LABEL, TC1));
        Assert.assertEquals(relatedObjectPredefinedMilestone,
                TASK_LABEL + "(" + milestonePredefined_1.getRelatedTask().get() + ")",
                String.format(INVALID_MILESTONE_ATTRIBUTE_PATTERN, namePredefinedMilestone, RELATED_OBJECT_LABEL, TC1));
    }

    @Test(priority = 2, description = "Update Predefined Milestone")
    @Description("Update Predefined Milestone")
    public void updatePredefinedMilestone() {
        final String milestoneName1 = MILESTONE_NAME + "TC2." + nextMaxInt();
        ProcessOverviewPage processOverviewPage = ProcessOverviewPage.goToProcessOverviewPage(driver, BASIC_URL);
        MilestonesStepWizardPage milestonesStepWizardPage = processOverviewPage.openProcessCreationWizard()
                .defineProcessAndGoToMilestonesStep(processNameTC2, 10L, GK_MILESTONES);

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
                .setName(milestoneName1)
                .setLeadTime("20")
                .setDescription(UPDATE_DESCRIPTION_3)
                .setIsManualCompletion("false")
                .setIsActive("false")
                .setRelatedTask("")
                .build();

        Milestone milestone1_updated = milestonesStepWizardPage.editPredefinedMilestone(milestone1, 1);
        Milestone milestone2_updated = milestonesStepWizardPage.editPredefinedMilestone(milestone2, 2);
        Milestone milestone3_updated = milestonesStepWizardPage.editPredefinedMilestone(milestone3, 3);
        milestonesStepWizardPage.clickAcceptButton();

        assertSystemMessage(String.format(PROCESS_CREATED_MESSAGE_PATTERN, processNameTC2, milestonesStepWizardPage.extractProcessCode()),
                SystemMessageContainer.MessageType.SUCCESS, String.format(INVALID_PROCESS_CREATION_MESSAGE, TC2));

        processOverviewPage.selectProcess(NAME_LABEL, processNameTC2).openMilestoneTab();
        String nameMilestone1 = milestone1_updated.getName().orElseThrow(() -> new RuntimeException(MISSING_NAME_EXCEPTION));
        String nameMilestone2 = milestone2_updated.getName().orElseThrow(() -> new RuntimeException(MISSING_NAME_EXCEPTION));
        String nameMilestone3 = milestone3_updated.getName().orElseThrow(() -> new RuntimeException(MISSING_NAME_EXCEPTION));

        // Status
        waitForPageToLoad();
        String statusMilestone1 = processOverviewPage.getMilestoneValue(nameMilestone1, STATUS_LABEL);
        String statusMilestone2 = processOverviewPage.getMilestoneValue(nameMilestone2, STATUS_LABEL);
        String statusMilestone3 = processOverviewPage.getMilestoneValue(nameMilestone3, STATUS_LABEL);
        Assert.assertEquals(statusMilestone1, NOT_NEEDED_STATUS,
                String.format(INVALID_MILESTONE_ATTRIBUTE_PATTERN, nameMilestone1, STATUS_LABEL, TC2));
        Assert.assertEquals(statusMilestone2, NEW_STATUS,
                String.format(INVALID_MILESTONE_ATTRIBUTE_PATTERN, nameMilestone2, STATUS_LABEL, TC2));
        Assert.assertEquals(statusMilestone3, NOT_NEEDED_STATUS,
                String.format(INVALID_MILESTONE_ATTRIBUTE_PATTERN, nameMilestone3, STATUS_LABEL, TC2));

        // Due date
        String leadTimeMilestone2 = milestone2_updated.getLeadTime().get();
        String dueDateMilestone1 = processOverviewPage.getMilestoneValue(nameMilestone1, DUE_DATE_LABEL);
        String dueDateMilestone2 = processOverviewPage.getMilestoneValue(nameMilestone2, DUE_DATE_LABEL);
        String dueDateMilestone3 = processOverviewPage.getMilestoneValue(nameMilestone3, DUE_DATE_LABEL);
        Assert.assertEquals(dueDateMilestone1, LocalDate.now().toString(),
                String.format(INVALID_MILESTONE_ATTRIBUTE_PATTERN, nameMilestone1, DUE_DATE_LABEL, TC2));
        Assert.assertEquals(dueDateMilestone2, LocalDate.now().plusDays(Long.parseLong(leadTimeMilestone2)).toString(),
                String.format(INVALID_MILESTONE_ATTRIBUTE_PATTERN, nameMilestone2, DUE_DATE_LABEL, TC2));
        Assert.assertEquals(dueDateMilestone3, "",
                String.format(INVALID_MILESTONE_ATTRIBUTE_PATTERN, nameMilestone3, DUE_DATE_LABEL, TC2));

        // Date of Completion
        String completionDateMilestone1 = processOverviewPage.getMilestoneValue(nameMilestone1, COMPLETION_DATE_LABEL);
        String completionDateMilestone2 = processOverviewPage.getMilestoneValue(nameMilestone2, COMPLETION_DATE_LABEL);
        String completionDateMilestone3 = processOverviewPage.getMilestoneValue(nameMilestone3, COMPLETION_DATE_LABEL);

        Assert.assertEquals(completionDateMilestone1, "",
                String.format(INVALID_MILESTONE_ATTRIBUTE_PATTERN, nameMilestone1, COMPLETION_DATE_LABEL, TC2));
        Assert.assertEquals(completionDateMilestone2, LocalDate.now().plusDays(Long.parseLong(leadTimeMilestone2)).toString(),
                String.format(INVALID_MILESTONE_ATTRIBUTE_PATTERN, nameMilestone2, COMPLETION_DATE_LABEL, TC2));
        Assert.assertEquals(completionDateMilestone3, "",
                String.format(INVALID_MILESTONE_ATTRIBUTE_PATTERN, nameMilestone3, COMPLETION_DATE_LABEL, TC2));

        // Related Object
        String relatedObjectMilestone1 = processOverviewPage.getMilestoneValue(nameMilestone1, RELATED_OBJECT_LABEL);
        String relatedObjectMilestone2 = processOverviewPage.getMilestoneValue(nameMilestone2, RELATED_OBJECT_LABEL);
        String relatedObjectMilestone3 = processOverviewPage.getMilestoneValue(nameMilestone3, RELATED_OBJECT_LABEL);
        Assert.assertEquals(relatedObjectMilestone1, PROCESS_LABEL,
                String.format(INVALID_MILESTONE_ATTRIBUTE_PATTERN, nameMilestone1, RELATED_OBJECT_LABEL, TC2));
        Assert.assertEquals(relatedObjectMilestone2, PROCESS_LABEL,
                String.format(INVALID_MILESTONE_ATTRIBUTE_PATTERN, nameMilestone2, RELATED_OBJECT_LABEL, TC2));
        Assert.assertEquals(relatedObjectMilestone3, PROCESS_LABEL,
                String.format(INVALID_MILESTONE_ATTRIBUTE_PATTERN, nameMilestone3, RELATED_OBJECT_LABEL, TC2));

        // Description
        String descriptionMilestone1 = processOverviewPage.getMilestoneValue(nameMilestone1, DESCRIPTION_LABEL);
        String descriptionMilestone2 = processOverviewPage.getMilestoneValue(nameMilestone2, DESCRIPTION_LABEL);
        String descriptionMilestone3 = processOverviewPage.getMilestoneValue(nameMilestone3, DESCRIPTION_LABEL);
        Assert.assertEquals(descriptionMilestone1, UPDATE_DESCRIPTION_1,
                String.format(INVALID_MILESTONE_ATTRIBUTE_PATTERN, nameMilestone1, DESCRIPTION_LABEL, TC2));
        Assert.assertEquals(descriptionMilestone2, UPDATE_DESCRIPTION_2,
                String.format(INVALID_MILESTONE_ATTRIBUTE_PATTERN, nameMilestone2, DESCRIPTION_LABEL, TC2));
        Assert.assertEquals(descriptionMilestone3, UPDATE_DESCRIPTION_3,
                String.format(INVALID_MILESTONE_ATTRIBUTE_PATTERN, nameMilestone3, DESCRIPTION_LABEL, TC2));
    }

    @Test(priority = 3, description = "Add Milestone for Data Correction Process")
    @Description("Add Milestone for Data Correction Process")
    public void addMilestoneForDCP() {
        final String milestoneName1 = MILESTONE_NAME + "TC3.1." + nextMaxInt();
        final String milestoneName2 = MILESTONE_NAME + "TC3.2." + nextMaxInt();
        ProcessOverviewPage processOverviewPage = ProcessOverviewPage.goToProcessOverviewPage(driver, BASIC_URL);
        MilestonesStepWizardPage milestonesStepWizardPage = processOverviewPage.openProcessCreationWizard()
                .defineProcessAndGoToMilestonesStep(processNameTC3, 10L, DCP);
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

        milestonesStepWizardPage.addMilestoneRow(milestone1);
        milestonesStepWizardPage.addMilestoneRow(milestone2);
        milestonesStepWizardPage.clickAcceptButton();

        assertSystemMessage(String.format(PROCESS_CREATED_MESSAGE_PATTERN, processNameTC3, milestonesStepWizardPage.extractProcessCode()),
                SystemMessageContainer.MessageType.SUCCESS, String.format(INVALID_PROCESS_CREATION_MESSAGE, TC3));

        processOverviewPage.selectProcess(NAME_LABEL, processNameTC3).openMilestoneTab();

        String nameMilestone1 = milestone1.getName().orElseThrow(() -> new RuntimeException(MISSING_NAME_EXCEPTION));
        String nameMilestone2 = milestone2.getName().orElseThrow(() -> new RuntimeException(MISSING_NAME_EXCEPTION));

        // Status
        waitForPageToLoad();
        String statusMilestone1 = processOverviewPage.getMilestoneValue(nameMilestone1, STATUS_LABEL);
        String statusMilestone2 = processOverviewPage.getMilestoneValue(nameMilestone2, STATUS_LABEL);
        Assert.assertEquals(statusMilestone1, NEW_STATUS,
                String.format(INVALID_MILESTONE_ATTRIBUTE_PATTERN, nameMilestone1, STATUS_LABEL, TC3));
        Assert.assertEquals(statusMilestone2, NOT_NEEDED_STATUS,
                String.format(INVALID_MILESTONE_ATTRIBUTE_PATTERN, nameMilestone2, STATUS_LABEL, TC3));

        // Due date
        String leadTimeMilestone1 = milestone1.getLeadTime().get();
        String dueDateMilestone1 = processOverviewPage.getMilestoneValue(nameMilestone1, DUE_DATE_LABEL);
        String dueDateMilestone2 = processOverviewPage.getMilestoneValue(nameMilestone2, DUE_DATE_LABEL);
        Assert.assertEquals(dueDateMilestone1, LocalDate.now().plusDays(Long.parseLong(leadTimeMilestone1)).toString(),
                String.format(INVALID_MILESTONE_ATTRIBUTE_PATTERN, nameMilestone1, DUE_DATE_LABEL, TC3));
        Assert.assertEquals(dueDateMilestone2, LocalDate.now().plusDays(5).toString(),
                String.format(INVALID_MILESTONE_ATTRIBUTE_PATTERN, nameMilestone2, DUE_DATE_LABEL, TC3));

        // Date of Completion
        String completionDateMilestone1 = processOverviewPage.getMilestoneValue(nameMilestone1, COMPLETION_DATE_LABEL);
        String completionDateMilestone2 = processOverviewPage.getMilestoneValue(nameMilestone2, COMPLETION_DATE_LABEL);
        Assert.assertEquals(completionDateMilestone1, LocalDate.now().plusDays(Long.parseLong(leadTimeMilestone1)).toString(),
                String.format(INVALID_MILESTONE_ATTRIBUTE_PATTERN, nameMilestone1, COMPLETION_DATE_LABEL, TC3));
        Assert.assertEquals(completionDateMilestone2, "",
                String.format(INVALID_MILESTONE_ATTRIBUTE_PATTERN, nameMilestone2, COMPLETION_DATE_LABEL, TC3));

        // Related Object
        String relatedObjectMilestone1 = processOverviewPage.getMilestoneValue(nameMilestone1, RELATED_OBJECT_LABEL);
        String relatedObjectMilestone2 = processOverviewPage.getMilestoneValue(nameMilestone2, RELATED_OBJECT_LABEL);
        Assert.assertEquals(relatedObjectMilestone1, PROCESS_LABEL,
                String.format(INVALID_MILESTONE_ATTRIBUTE_PATTERN, nameMilestone1, RELATED_OBJECT_LABEL, TC3));
        Assert.assertEquals(relatedObjectMilestone2, TASK_LABEL + "(" + milestone2.getRelatedTask().get() + ")",
                String.format(INVALID_MILESTONE_ATTRIBUTE_PATTERN, nameMilestone2, RELATED_OBJECT_LABEL, TC3));

        // Description
        String descriptionMilestone1 = processOverviewPage.getMilestoneValue(nameMilestone1, DESCRIPTION_LABEL);
        String descriptionMilestone2 = processOverviewPage.getMilestoneValue(nameMilestone2, DESCRIPTION_LABEL);
        Assert.assertEquals(descriptionMilestone1, DESCRIPTION_EDIT_1,
                String.format(INVALID_MILESTONE_ATTRIBUTE_PATTERN, nameMilestone1, DESCRIPTION_LABEL, TC3));
        Assert.assertEquals(descriptionMilestone2, "",
                String.format(INVALID_MILESTONE_ATTRIBUTE_PATTERN, nameMilestone2, DESCRIPTION_LABEL, TC3));
    }

    @Test(priority = 4, description = "Add Milestone for existing Data Correction Process")
    @Description("Add Milestone for existing Data Correction Process")
    public void addMilestoneForExistingProcess() {
        final String milestoneName1 = MILESTONE_NAME + "TC4.1." + nextMaxInt();
        final String milestoneName2 = MILESTONE_NAME + "TC4.2." + nextMaxInt();
        ProcessOverviewPage processOverviewPage = ProcessOverviewPage.goToProcessOverviewPage(driver, BASIC_URL).clearAllColumnFilters();

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

        String processDCPCode = processOverviewPage.createProcessIPD(processNameTC4, 5L, DCP);

        processOverviewPage.addMilestonesForProcess(processDCPCode, milestones);

        processOverviewPage.selectProcess(processDCPCode).openMilestoneTab();

        String nameMilestone1 = milestones.get(0).getName().orElseThrow(() -> new RuntimeException(MISSING_NAME_EXCEPTION));
        String nameMilestone2 = milestones.get(1).getName().orElseThrow(() -> new RuntimeException(MISSING_NAME_EXCEPTION));

        // Status
        waitForPageToLoad();
        String statusMilestone1 = processOverviewPage.getMilestoneValue(nameMilestone1, STATUS_LABEL);
        String statusMilestone2 = processOverviewPage.getMilestoneValue(nameMilestone2, STATUS_LABEL);
        Assert.assertEquals(statusMilestone1, NEW_STATUS,
                String.format(INVALID_MILESTONE_ATTRIBUTE_PATTERN, nameMilestone1, STATUS_LABEL, TC4));
        Assert.assertEquals(statusMilestone2, NOT_NEEDED_STATUS,
                String.format(INVALID_MILESTONE_ATTRIBUTE_PATTERN, nameMilestone2, STATUS_LABEL, TC4));

        // Due date
        String leadTimeMilestone1 = milestones.get(0).getLeadTime().get();
        String dueDateMilestone1 = processOverviewPage.getMilestoneValue(nameMilestone1, DUE_DATE_LABEL);
        String dueDateMilestone2 = processOverviewPage.getMilestoneValue(nameMilestone2, DUE_DATE_LABEL);
        Assert.assertEquals(dueDateMilestone1, LocalDate.now().plusDays(Long.parseLong(leadTimeMilestone1)).toString(),
                String.format(INVALID_MILESTONE_ATTRIBUTE_PATTERN, nameMilestone1, DUE_DATE_LABEL, TC4));
        Assert.assertEquals(dueDateMilestone2, LocalDate.now().plusDays(5).toString(),
                String.format(INVALID_MILESTONE_ATTRIBUTE_PATTERN, nameMilestone2, DUE_DATE_LABEL, TC4));

        // Date of Completion
        String completionDateMilestone1 = processOverviewPage.getMilestoneValue(nameMilestone1, COMPLETION_DATE_LABEL);
        String completionDateMilestone2 = processOverviewPage.getMilestoneValue(nameMilestone2, COMPLETION_DATE_LABEL);
        Assert.assertEquals(completionDateMilestone1, LocalDate.now().plusDays(Long.parseLong(leadTimeMilestone1)).toString(),
                String.format(INVALID_MILESTONE_ATTRIBUTE_PATTERN, nameMilestone1, COMPLETION_DATE_LABEL, TC4));
        Assert.assertEquals(completionDateMilestone2, "",
                String.format(INVALID_MILESTONE_ATTRIBUTE_PATTERN, nameMilestone2, COMPLETION_DATE_LABEL, TC4));

        // Related Object
        String relatedObjectMilestone1 = processOverviewPage.getMilestoneValue(nameMilestone1, RELATED_OBJECT_LABEL);
        String relatedObjectMilestone2 = processOverviewPage.getMilestoneValue(nameMilestone2, RELATED_OBJECT_LABEL);
        Assert.assertEquals(relatedObjectMilestone1, PROCESS_LABEL,
                String.format(INVALID_MILESTONE_ATTRIBUTE_PATTERN, nameMilestone1, RELATED_OBJECT_LABEL, TC4));
        Assert.assertEquals(relatedObjectMilestone2, TASK_LABEL + "(" + milestones.get(1).getRelatedTask().get() + ")",
                String.format(INVALID_MILESTONE_ATTRIBUTE_PATTERN, nameMilestone2, RELATED_OBJECT_LABEL, TC4));

        // Description
        String descriptionMilestone1 = processOverviewPage.getMilestoneValue(nameMilestone1, DESCRIPTION_LABEL);
        String descriptionMilestone2 = processOverviewPage.getMilestoneValue(nameMilestone2, DESCRIPTION_LABEL);
        Assert.assertEquals(descriptionMilestone1, DESCRIPTION_EDIT_1,
                String.format(INVALID_MILESTONE_ATTRIBUTE_PATTERN, nameMilestone1, DESCRIPTION_LABEL, TC4));
        Assert.assertEquals(descriptionMilestone2, "",
                String.format(INVALID_MILESTONE_ATTRIBUTE_PATTERN, nameMilestone2, DESCRIPTION_LABEL, TC4));
    }

    @Test(priority = 5, description = "Checking system message summary")
    @Description("Checking system message summary")
    public void systemMessageSummary() {
        softAssert.assertAll();
    }

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @AfterClass
    public void terminateProcesses() {
        ProcessOverviewPage processOverviewPage = ProcessOverviewPage.goToProcessOverviewPage(driver, BASIC_URL).clearAllColumnFilters();
        processOverviewPage.selectProcess(ProcessOverviewPage.NAME_LABEL, processNameTC1).terminateProcess(TERMINATE_REASON);
        processOverviewPage.selectProcess(ProcessOverviewPage.NAME_LABEL, processNameTC2).terminateProcess(TERMINATE_REASON);
        processOverviewPage.selectProcess(ProcessOverviewPage.NAME_LABEL, processNameTC3).terminateProcess(TERMINATE_REASON);
        processOverviewPage.selectProcess(ProcessOverviewPage.NAME_LABEL, processNameTC4).terminateProcess(TERMINATE_REASON);
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

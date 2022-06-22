package com.oss.bpm.milestones;

import com.oss.BaseTestCase;
import com.oss.bpm.ImportExportModelTest;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.mainheader.ToolbarWidget;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.bpm.milestones.Milestone;
import com.oss.pages.bpm.processmodels.ImportModelWizardPage;
import com.oss.pages.bpm.processmodels.ProcessModelsPage;
import com.oss.utils.TestListener;
import io.qameta.allure.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.collections.Lists;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

/**
 * @author Paweł Rother
 */
@Listeners({TestListener.class})
public class EditMilestonesForProcessModelTest extends BaseTestCase {
    private static final String BPM_USER_LOGIN = "bpm_webselenium";
    private static final String BPM_USER_PASSWORD = "Webtests123!";
    private static final String BPM_ADMIN_USER_LOGIN = "bpm_admin_webselenium";
    private static final String BPM_ADMIN_USER_PASSWORD = "Webtests123!";

    private static final String MILESTONE_NAME_1 = "Milestone 1.";
    private static final String MILESTONE_NAME_2 = "Milestone 2.";
    private static final String RELATED_TASK_LABEL = "Related task";
    private static final String DESCRIPTION_LABEL = "Description";
    private static final String LEAD_TIME_LABEL = "Lead time (days)";
    private static final String MANUAL_COMPLETION_LABEL = "Manual completion";
    private static final String CHECK_MARK_ICON_NAME = "fa-check";
    private static final String SECOND_TASK_NAME = "Second Task";
    private static final String MISSING_NAME_EXCEPTION = "Missing Name";
    private static final String DOMAIN = "Inventory Processes";
    private static final String MODEL_NAME = "bpm_selenium_test_process";
    private static final String OTHER_GROUP_ID = "other";
    private static final String IMPORT_ID = "import";
    private static final String IMPORT_PATH = "bpm/bpm_selenium_test_process.bar";
    private static final String SUCCESS_UPDATE_MILESTONES_MESSAGE = "Milestones were updated successfully.";
    private static final String EMPTY_NAME_ERROR_MESSAGE = "Name of milestone can not be empty.";
    private static final String EDIT_MILESTONES_WIZARD_ID = "bpm_models_view_milestones-popup_wizard-app-id";
    private static final String UPLOAD_SUCCESS_MESSAGE = "Upload success";
    private static final String CANNOT_LOAD_FILE_EXCEPTION = "Cannot load file";
    private static final String MILESTONE_DESCRIPTION_1 = "Milestone 1 - Selenium Test";
    private static final String NO_SYSTEM_MESSAGE_EXCEPTION = "There is no any System Message";
    private static final String FIRST_TASK_NAME = "First Task";

    @BeforeClass
    public void importProcessModel() {
        ProcessModelsPage processModelsPage = ProcessModelsPage.goToProcessModelsPage(driver, BASIC_URL);
        ToolbarWidget toolbarWidget = ToolbarWidget.create(driver, webDriverWait);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        if (!toolbarWidget.getUserName().equals(BPM_USER_LOGIN)) {
            processModelsPage.changeUser(BPM_USER_LOGIN, BPM_USER_PASSWORD);
        }
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        processModelsPage.chooseDomain(DOMAIN);
        processModelsPage.callAction(OTHER_GROUP_ID, IMPORT_ID);
        ImportModelWizardPage importModelWizardPage = new ImportModelWizardPage(driver);
        try {
            URL resource = ImportExportModelTest.class.getClassLoader().getResource(IMPORT_PATH);
            String absolutePatch = Paths.get(Objects.requireNonNull(resource).toURI()).toFile().getAbsolutePath();
            importModelWizardPage.attachFile(absolutePatch);
            DelayUtils.sleep(1000);
            Assert.assertEquals(importModelWizardPage.getImportStatus(), UPLOAD_SUCCESS_MESSAGE);
            importModelWizardPage.importButton();
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            Assert.assertFalse(importModelWizardPage.isImportWizardVisible());
        } catch (URISyntaxException e) {
            throw new RuntimeException(CANNOT_LOAD_FILE_EXCEPTION, e);
        }
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 1, description = "Add new Milestones for Process Model")
    @Description("Add new Milestones for Process Model")
    public void addMilestonesForProcessModel() {
        String milestoneName1 = MILESTONE_NAME_1 + (int) (Math.random() * 100001);
        String milestoneName2 = MILESTONE_NAME_2 + (int) (Math.random() * 100001);
        List<Milestone> milestones = Lists.newArrayList(
                Milestone.builder()
                        .setLeadTime("10")
                        .setDescription(MILESTONE_DESCRIPTION_1)
                        .setName(milestoneName1)
                        .build(),

                Milestone.builder()
                        .setName(milestoneName2)
                        .setRelatedTask(FIRST_TASK_NAME)
                        .setIsManualCompletion("true")
                        .build());

        ProcessModelsPage processModelsPage = ProcessModelsPage.goToProcessModelsPage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        processModelsPage.chooseDomain(DOMAIN);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        processModelsPage.addMilestonesForProcessModel(MODEL_NAME, milestones);

        String message = SystemMessageContainer.create(driver, webDriverWait).getFirstMessage().orElseThrow(()
                -> new RuntimeException(NO_SYSTEM_MESSAGE_EXCEPTION)).getText();
        Assert.assertEquals(message, SUCCESS_UPDATE_MILESTONES_MESSAGE);

        processModelsPage.selectMilestoneTab(MODEL_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        String nameMilestone1 = milestones.get(0).getName().orElseThrow(() -> new RuntimeException(MISSING_NAME_EXCEPTION));
        String nameMilestone2 = milestones.get(1).getName().orElseThrow(() -> new RuntimeException(MISSING_NAME_EXCEPTION));

        // Related Task
        String relateTaskMilestone1 = processModelsPage.getMilestoneValue(nameMilestone1, RELATED_TASK_LABEL);
        String relatedTaskMilestone2 = processModelsPage.getMilestoneValue(nameMilestone2, RELATED_TASK_LABEL
        );
        Assert.assertEquals(relateTaskMilestone1, "");
        Assert.assertEquals(relatedTaskMilestone2, milestones.get(1).getRelatedTask().get());

        // Description
        String descriptionMilestone1 = processModelsPage.getMilestoneValue(nameMilestone1, DESCRIPTION_LABEL);
        String descriptionMilestone2 = processModelsPage.getMilestoneValue(nameMilestone2, DESCRIPTION_LABEL);
        Assert.assertEquals(descriptionMilestone1, milestones.get(0).getDescription().get());
        Assert.assertEquals(descriptionMilestone2, "");

        // Lead Time
        String leadTimeMilestone1 = processModelsPage.getMilestoneValue(nameMilestone1, LEAD_TIME_LABEL);
        String leadTimeMilestone2 = processModelsPage.getMilestoneValue(nameMilestone2, LEAD_TIME_LABEL);
        Assert.assertEquals(leadTimeMilestone1, milestones.get(0).getLeadTime().get());
        Assert.assertEquals(leadTimeMilestone2, "0");

        // Manual Completion
        String manualCompletionMilestone1 = processModelsPage.getMilestoneValue(nameMilestone1, MANUAL_COMPLETION_LABEL);
        String manualCompletionMilestone2 = processModelsPage.getMilestoneValue(nameMilestone2, MANUAL_COMPLETION_LABEL);
        Assert.assertEquals(manualCompletionMilestone1, "");
        Assert.assertTrue(manualCompletionMilestone2.contains(CHECK_MARK_ICON_NAME));
    }

    @Test(priority = 2, description = "Edit existing Milestones for Process Model",
            dependsOnMethods = {"addMilestonesForProcessModel"})
    @Description("Edit existing Milestones for Process Model")
    public void editMilestonesForProcessModel() {
        String milestoneName1 = MILESTONE_NAME_1 + (int) (Math.random() * 100001);
        String milestoneName2 = MILESTONE_NAME_2 + (int) (Math.random() * 100001);
        List<Milestone> milestones = Lists.newArrayList(
                Milestone.builder()
                        .setLeadTime("")
                        .setDescription(MILESTONE_DESCRIPTION_1 + " - updated")
                        .setRelatedTask(SECOND_TASK_NAME)
                        .setName(milestoneName1)
                        .setIsManualCompletion("true")
                        .build(),

                Milestone.builder()
                        .setName(milestoneName2)
                        .setLeadTime("10")
                        .setRelatedTask("")
                        .setIsManualCompletion("false")
                        .build());

        ProcessModelsPage processModelsPage = ProcessModelsPage.goToProcessModelsPage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        processModelsPage.chooseDomain(DOMAIN);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        processModelsPage.editMilestonesForProcessModel(MODEL_NAME, milestones);

        String message = SystemMessageContainer.create(driver, webDriverWait).getFirstMessage().orElseThrow(()
                -> new RuntimeException(NO_SYSTEM_MESSAGE_EXCEPTION)).getText();
        Assert.assertEquals(message, SUCCESS_UPDATE_MILESTONES_MESSAGE);

        processModelsPage.selectMilestoneTab(MODEL_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        String nameMilestone1 = milestones.get(0).getName().orElseThrow(() -> new RuntimeException(MISSING_NAME_EXCEPTION));
        String nameMilestone2 = milestones.get(1).getName().orElseThrow(() -> new RuntimeException(MISSING_NAME_EXCEPTION));

        // Related Task
        String relateTaskMilestone1 = processModelsPage.getMilestoneValue(nameMilestone1, RELATED_TASK_LABEL);
        String relatedTaskMilestone2 = processModelsPage.getMilestoneValue(nameMilestone2, RELATED_TASK_LABEL);
        Assert.assertEquals(relateTaskMilestone1, milestones.get(0).getRelatedTask().get());
        Assert.assertEquals(relatedTaskMilestone2, "");

        // Description
        String descriptionMilestone1 = processModelsPage.getMilestoneValue(nameMilestone1, DESCRIPTION_LABEL);
        String descriptionMilestone2 = processModelsPage.getMilestoneValue(nameMilestone2, DESCRIPTION_LABEL);
        Assert.assertEquals(descriptionMilestone1, milestones.get(0).getDescription().get());
        Assert.assertEquals(descriptionMilestone2, "");

        // Lead Time
        String leadTimeMilestone1 = processModelsPage.getMilestoneValue(nameMilestone1, LEAD_TIME_LABEL);
        String leadTimeMilestone2 = processModelsPage.getMilestoneValue(nameMilestone2, LEAD_TIME_LABEL);
        Assert.assertEquals(leadTimeMilestone1, "0");
        Assert.assertEquals(leadTimeMilestone2, milestones.get(1).getLeadTime().get());

        // Manual Completion
        String manualCompletionMilestone1 = processModelsPage.getMilestoneValue(nameMilestone1, MANUAL_COMPLETION_LABEL);
        String manualCompletionMilestone2 = processModelsPage.getMilestoneValue(nameMilestone2, MANUAL_COMPLETION_LABEL);
        Assert.assertTrue(manualCompletionMilestone1.contains(CHECK_MARK_ICON_NAME));
        Assert.assertEquals(manualCompletionMilestone2, "");
    }

    @Test(priority = 3, description = "Remove existing Milestones for Process Model",
            dependsOnMethods = {"addMilestonesForProcessModel"})
    @Description("Remove existing Milestones for Process Model")
    public void removeMilestonesForProcessModel() {
        ProcessModelsPage processModelsPage = ProcessModelsPage.goToProcessModelsPage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        processModelsPage.chooseDomain(DOMAIN);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        processModelsPage.removeMilestonesForProcessModel(MODEL_NAME, 2);

        String message = SystemMessageContainer.create(driver, webDriverWait).getFirstMessage().orElseThrow(()
                -> new RuntimeException(NO_SYSTEM_MESSAGE_EXCEPTION)).getText();
        Assert.assertEquals(message, SUCCESS_UPDATE_MILESTONES_MESSAGE);

        processModelsPage.selectMilestoneTab(MODEL_NAME);
        Assert.assertTrue(processModelsPage.isMilestonesTabEmpty());
    }

    @Test(priority = 4, description = "Try to add Milestone without name for Process Model")
    @Description("Try to add Milestone without name for Process Model")
    public void addMilestoneWithoutName() {
        String milestoneName2 = MILESTONE_NAME_2 + (int) (Math.random() * 100001);
        List<Milestone> milestones = Lists.newArrayList(
                Milestone.builder()
                        .setLeadTime("10")
                        .setDescription(MILESTONE_DESCRIPTION_1)
                        .build(),

                Milestone.builder()
                        .setName(milestoneName2)
                        .setRelatedTask(FIRST_TASK_NAME)
                        .setIsManualCompletion("true")
                        .build());

        ProcessModelsPage processModelsPage = ProcessModelsPage.goToProcessModelsPage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        processModelsPage.chooseDomain(DOMAIN);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        processModelsPage.addMilestonesForProcessModel(MODEL_NAME, milestones);

        String message = SystemMessageContainer.create(driver, webDriverWait).getFirstMessage().orElseThrow(()
                -> new RuntimeException(NO_SYSTEM_MESSAGE_EXCEPTION)).getText();
        Assert.assertEquals(message, EMPTY_NAME_ERROR_MESSAGE);
        SystemMessageContainer.create(driver, webDriverWait).close();
        Wizard editMilestonesWizard = Wizard.createByComponentId(driver, webDriverWait, EDIT_MILESTONES_WIZARD_ID);
        editMilestonesWizard.clickCancel();
    }

    @AfterClass
    public void deleteModel() {
        ProcessModelsPage processModelsPage = ProcessModelsPage.goToProcessModelsPage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        processModelsPage.chooseDomain(DOMAIN);
        processModelsPage.deleteModel(MODEL_NAME);
    }
}

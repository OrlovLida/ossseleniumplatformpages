package com.oss.bpm.milestones;

import com.oss.BaseTestCase;
import com.oss.bpm.ImportExportModelTest;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.alerts.SystemMessageInterface;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.bpm.milestones.Milestone;
import com.oss.pages.bpm.processmodels.ImportModelWizardPage;
import com.oss.pages.bpm.processmodels.ProcessModelsPage;
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

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.oss.bpm.BpmPhysicalDataCreator.nextMaxInt;

/**
 * @author Paweł Rother
 */
@Listeners({TestListener.class})
public class EditMilestonesForProcessModelTest extends BaseTestCase {
    private static final String TC1 = "addMilestonesForProcessModel";
    private static final String TC2 = "editMilestonesForProcessModel";
    private static final String TC3 = "removeMilestonesForProcessModel";
    private static final String TC4 = "addMilestoneWithoutName";

    private static final String INVALID_UPLOAD_FILE_MESSAGE = "Invalid upload file status in import model wizard.";
    private static final String INVALID_IMPORT_WIZARD_VISIBILITY = "Invalid import model wizard visibility.";
    private static final String INVALID_MILESTONE_ATTRIBUTE_PATTERN = "Invalid '%1$s' milestone '%2$s' attribute in '%3$s' test.";

    private static final String MILESTONE_NAME_1 = "Milestones For Model Test 1.";
    private static final String MILESTONE_NAME_2 = "Milestones For Model Test 1.";
    private static final String RELATED_TASK_LABEL = "Related task";
    private static final String DESCRIPTION_LABEL = "Description";
    private static final String LEAD_TIME_LABEL = "Lead time (days)";
    private static final String MANUAL_COMPLETION_LABEL = "Manual completion";
    private static final String CHECK_MARK_ICON_NAME = "commonIcon-CHECK";
    private static final String SECOND_TASK_NAME = "Second Task";
    private static final String MISSING_NAME_EXCEPTION = "Missing Name";
    private static final String DOMAIN = "Inventory Processes";
    private static final String MODEL_NAME = "bpm_selenium_edit_milestones_for_process_model";
    private static final String OTHER_GROUP_ID = "other";
    private static final String IMPORT_ID = "import";
    private static final String IMPORT_PATH = "bpm/bpm_selenium_edit_milestones_for_process_model.bar";
    private static final String SUCCESS_UPDATE_MILESTONES_MESSAGE = "Milestones were updated successfully.";
    private static final String EMPTY_NAME_ERROR_MESSAGE = "Name of milestone can not be empty.";
    private static final String EDIT_MILESTONES_WIZARD_ID = "bpm_models_view_milestones-popup_wizard-app-id";
    private static final String UPLOAD_SUCCESS_MESSAGE = "Upload success";
    private static final String CANNOT_LOAD_FILE_EXCEPTION = "Cannot load file";
    private static final String MILESTONE_DESCRIPTION_1 = "Milestone 1 - Selenium Test";
    private static final String FIRST_TASK_NAME = "First Task";
    private static final String MODEL_STILL_VISIBLE_EXCEPTION = "Process model %s is still visible on Process Models View";
    private static final String INVALID_MILESTONES_UPDATE_MESSAGE_PATTERN = "Invalid milestones update system message in '%s' test.";
    private static final String MILESTONES_VISIBLE_INFO = "There are visible some milestones on Milestones Tab.";
    private SoftAssert softAssert;
    private final String modelKeyword = "Selenium." + nextMaxInt();

    @BeforeClass
    public void importProcessModel() {
        softAssert = new SoftAssert();
        ProcessModelsPage processModelsPage = ProcessModelsPage.goToProcessModelsPage(driver, BASIC_URL);
        processModelsPage.chooseDomain(DOMAIN);
        processModelsPage.callAction(OTHER_GROUP_ID, IMPORT_ID);
        ImportModelWizardPage importModelWizardPage = new ImportModelWizardPage(driver);
        try {
            URL resource = ImportExportModelTest.class.getClassLoader().getResource(IMPORT_PATH);
            String absolutePatch = Paths.get(Objects.requireNonNull(resource).toURI()).toFile().getAbsolutePath();
            importModelWizardPage.attachFile(absolutePatch);
            DelayUtils.sleep(1000);
            Assert.assertEquals(importModelWizardPage.getImportStatus(), UPLOAD_SUCCESS_MESSAGE, INVALID_UPLOAD_FILE_MESSAGE);
            importModelWizardPage.importButton();
            waitForPageToLoad();
            Assert.assertFalse(importModelWizardPage.isImportWizardVisible(), INVALID_IMPORT_WIZARD_VISIBILITY);
        } catch (URISyntaxException e) {
            throw new RuntimeException(CANNOT_LOAD_FILE_EXCEPTION, e);
        }
        waitForPageToLoad();
        processModelsPage.selectModelByName(MODEL_NAME).setKeywordForSelectedModel(modelKeyword);
        waitForPageToLoad();
    }

    @Test(priority = 1, description = "Add new Milestones for Process Model")
    @Description("Add new Milestones for Process Model")
    public void addMilestonesForProcessModel() {
        String milestoneName1 = MILESTONE_NAME_1 + nextMaxInt();
        String milestoneName2 = MILESTONE_NAME_2 + nextMaxInt();
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
        processModelsPage.chooseDomain(DOMAIN).selectModel(MODEL_NAME, modelKeyword).addMilestonesForSelectedModel(milestones);

        assertSystemMessage(SUCCESS_UPDATE_MILESTONES_MESSAGE, SystemMessageContainer.MessageType.SUCCESS,
                String.format(INVALID_MILESTONES_UPDATE_MESSAGE_PATTERN, TC1));

        processModelsPage.selectModel(MODEL_NAME, modelKeyword).openMilestoneTab();

        String nameMilestone1 = milestones.get(0).getName().orElseThrow(() -> new RuntimeException(MISSING_NAME_EXCEPTION));
        String nameMilestone2 = milestones.get(1).getName().orElseThrow(() -> new RuntimeException(MISSING_NAME_EXCEPTION));

        // Related Task
        String relateTaskMilestone1 = processModelsPage.getMilestoneValue(nameMilestone1, RELATED_TASK_LABEL);
        String relatedTaskMilestone2 = processModelsPage.getMilestoneValue(nameMilestone2, RELATED_TASK_LABEL);
        Assert.assertEquals(relateTaskMilestone1, "",
                String.format(INVALID_MILESTONE_ATTRIBUTE_PATTERN, nameMilestone1, RELATED_TASK_LABEL, TC1));
        Assert.assertEquals(relatedTaskMilestone2, milestones.get(1).getRelatedTask().get(),
                String.format(INVALID_MILESTONE_ATTRIBUTE_PATTERN, nameMilestone2, RELATED_TASK_LABEL, TC1));

        // Description
        String descriptionMilestone1 = processModelsPage.getMilestoneValue(nameMilestone1, DESCRIPTION_LABEL);
        String descriptionMilestone2 = processModelsPage.getMilestoneValue(nameMilestone2, DESCRIPTION_LABEL);
        Assert.assertEquals(descriptionMilestone1, milestones.get(0).getDescription().get(),
                String.format(INVALID_MILESTONE_ATTRIBUTE_PATTERN, nameMilestone1, DESCRIPTION_LABEL, TC1));
        Assert.assertEquals(descriptionMilestone2, "",
                String.format(INVALID_MILESTONE_ATTRIBUTE_PATTERN, nameMilestone2, DESCRIPTION_LABEL, TC1));

        // Lead Time
        String leadTimeMilestone1 = processModelsPage.getMilestoneValue(nameMilestone1, LEAD_TIME_LABEL);
        String leadTimeMilestone2 = processModelsPage.getMilestoneValue(nameMilestone2, LEAD_TIME_LABEL);
        Assert.assertEquals(leadTimeMilestone1, milestones.get(0).getLeadTime().get(),
                String.format(INVALID_MILESTONE_ATTRIBUTE_PATTERN, nameMilestone1, LEAD_TIME_LABEL, TC1));
        Assert.assertEquals(leadTimeMilestone2, "0",
                String.format(INVALID_MILESTONE_ATTRIBUTE_PATTERN, nameMilestone2, LEAD_TIME_LABEL, TC1));

        // Manual Completion
        String manualCompletionMilestone1 = processModelsPage.getMilestoneValue(nameMilestone1, MANUAL_COMPLETION_LABEL);
        String manualCompletionMilestone2 = processModelsPage.getMilestoneValue(nameMilestone2, MANUAL_COMPLETION_LABEL);
        Assert.assertEquals(manualCompletionMilestone1, "",
                String.format(INVALID_MILESTONE_ATTRIBUTE_PATTERN, nameMilestone1, MANUAL_COMPLETION_LABEL, TC1));
        Assert.assertTrue(manualCompletionMilestone2.contains(CHECK_MARK_ICON_NAME),
                String.format(INVALID_MILESTONE_ATTRIBUTE_PATTERN, nameMilestone2, MANUAL_COMPLETION_LABEL, TC1));
    }

    @Test(priority = 2, description = "Edit existing Milestones for Process Model", dependsOnMethods = {TC1})
    @Description("Edit existing Milestones for Process Model")
    public void editMilestonesForProcessModel() {
        String milestoneName1 = MILESTONE_NAME_1 + nextMaxInt();
        String milestoneName2 = MILESTONE_NAME_2 + nextMaxInt();
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
        processModelsPage.chooseDomain(DOMAIN).selectModel(MODEL_NAME, modelKeyword).editMilestonesForSelectedModel(milestones);

        assertSystemMessage(SUCCESS_UPDATE_MILESTONES_MESSAGE, SystemMessageContainer.MessageType.SUCCESS,
                String.format(INVALID_MILESTONES_UPDATE_MESSAGE_PATTERN, TC2));

        processModelsPage.selectModel(MODEL_NAME, modelKeyword).openMilestoneTab();

        String nameMilestone1 = milestones.get(0).getName().orElseThrow(() -> new RuntimeException(MISSING_NAME_EXCEPTION));
        String nameMilestone2 = milestones.get(1).getName().orElseThrow(() -> new RuntimeException(MISSING_NAME_EXCEPTION));

        // Related Task
        String relateTaskMilestone1 = processModelsPage.getMilestoneValue(nameMilestone1, RELATED_TASK_LABEL);
        String relatedTaskMilestone2 = processModelsPage.getMilestoneValue(nameMilestone2, RELATED_TASK_LABEL);
        Assert.assertEquals(relateTaskMilestone1, milestones.get(0).getRelatedTask().get(),
                String.format(INVALID_MILESTONE_ATTRIBUTE_PATTERN, nameMilestone1, RELATED_TASK_LABEL, TC2));
        Assert.assertEquals(relatedTaskMilestone2, "",
                String.format(INVALID_MILESTONE_ATTRIBUTE_PATTERN, nameMilestone2, RELATED_TASK_LABEL, TC2));

        // Description
        String descriptionMilestone1 = processModelsPage.getMilestoneValue(nameMilestone1, DESCRIPTION_LABEL);
        String descriptionMilestone2 = processModelsPage.getMilestoneValue(nameMilestone2, DESCRIPTION_LABEL);
        Assert.assertEquals(descriptionMilestone1, milestones.get(0).getDescription().get(),
                String.format(INVALID_MILESTONE_ATTRIBUTE_PATTERN, nameMilestone1, DESCRIPTION_LABEL, TC2));
        Assert.assertEquals(descriptionMilestone2, "",
                String.format(INVALID_MILESTONE_ATTRIBUTE_PATTERN, nameMilestone2, DESCRIPTION_LABEL, TC2));

        // Lead Time
        String leadTimeMilestone1 = processModelsPage.getMilestoneValue(nameMilestone1, LEAD_TIME_LABEL);
        String leadTimeMilestone2 = processModelsPage.getMilestoneValue(nameMilestone2, LEAD_TIME_LABEL);
        Assert.assertEquals(leadTimeMilestone1, "0",
                String.format(INVALID_MILESTONE_ATTRIBUTE_PATTERN, nameMilestone1, LEAD_TIME_LABEL, TC2));
        Assert.assertEquals(leadTimeMilestone2, milestones.get(1).getLeadTime().get(),
                String.format(INVALID_MILESTONE_ATTRIBUTE_PATTERN, nameMilestone2, LEAD_TIME_LABEL, TC2));

        // Manual Completion
        String manualCompletionMilestone1 = processModelsPage.getMilestoneValue(nameMilestone1, MANUAL_COMPLETION_LABEL);
        String manualCompletionMilestone2 = processModelsPage.getMilestoneValue(nameMilestone2, MANUAL_COMPLETION_LABEL);
        Assert.assertTrue(manualCompletionMilestone1.contains(CHECK_MARK_ICON_NAME),
                String.format(INVALID_MILESTONE_ATTRIBUTE_PATTERN, nameMilestone1, MANUAL_COMPLETION_LABEL, TC2));
        Assert.assertEquals(manualCompletionMilestone2, "",
                String.format(INVALID_MILESTONE_ATTRIBUTE_PATTERN, nameMilestone2, MANUAL_COMPLETION_LABEL, TC2));
    }

    @Test(priority = 3, description = "Remove existing Milestones for Process Model", dependsOnMethods = {TC1})
    @Description("Remove existing Milestones for Process Model")
    public void removeMilestonesForProcessModel() {
        ProcessModelsPage processModelsPage = ProcessModelsPage.goToProcessModelsPage(driver, BASIC_URL);
        processModelsPage.chooseDomain(DOMAIN).selectModel(MODEL_NAME, modelKeyword).removeMilestonesForSelectedModel(2);
        waitForPageToLoad();

        assertSystemMessage(SUCCESS_UPDATE_MILESTONES_MESSAGE, SystemMessageContainer.MessageType.SUCCESS,
                String.format(INVALID_MILESTONES_UPDATE_MESSAGE_PATTERN, TC3));

        processModelsPage.selectModel(MODEL_NAME, modelKeyword).openMilestoneTab();
        Assert.assertTrue(processModelsPage.isMilestonesTabEmpty(), MILESTONES_VISIBLE_INFO);
    }

    @Test(priority = 4, description = "Try to add Milestone without name for Process Model")
    @Description("Try to add Milestone without name for Process Model")
    public void addMilestoneWithoutName() {
        String milestoneName2 = MILESTONE_NAME_2 + nextMaxInt();
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
        processModelsPage.chooseDomain(DOMAIN).selectModel(MODEL_NAME, modelKeyword).addMilestonesForSelectedModel(milestones);

        assertSystemMessage(EMPTY_NAME_ERROR_MESSAGE, SystemMessageContainer.MessageType.DANGER,
                String.format(INVALID_MILESTONES_UPDATE_MESSAGE_PATTERN, TC4));

        Wizard editMilestonesWizard = Wizard.createByComponentId(driver, webDriverWait, EDIT_MILESTONES_WIZARD_ID);
        editMilestonesWizard.clickCancel();
    }

    @Test(priority = 5, description = "Checking system message summary")
    @Description("Checking system message summary")
    public void systemMessageSummary() {
        softAssert.assertAll();
    }

    @AfterClass
    public void deleteModel() {
        ProcessModelsPage processModelsPage = ProcessModelsPage.goToProcessModelsPage(driver, BASIC_URL);
        processModelsPage.chooseDomain(DOMAIN).selectModel(MODEL_NAME, modelKeyword).deleteSelectedModel();
        if (processModelsPage.isModelExists(MODEL_NAME, modelKeyword)) {
            throw new IllegalStateException(String.format(MODEL_STILL_VISIBLE_EXCEPTION, MODEL_NAME));
        }
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

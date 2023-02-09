package com.oss.bpm.milestones;

import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.alerts.SystemMessageInterface;
import com.oss.framework.components.prompts.ConfirmationBox;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.bpm.milestones.ChangeStateMilestoneWizardPage;
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

import static com.oss.bpm.milestones.EditMilestoneTest.EDIT_MILESTONE_BUTTON;

/**
 * @author Paweł Rother
 */

@Listeners({TestListener.class})
public class ChangeMilestoneStateTest extends BaseTestCase {
    private static final String TC1 = "firstMilestoneFlow";
    private static final String TC2 = "secondMilestoneFlow";
    private static final String TC3 = "thirdMilestoneFlow";
    private static final String TC4 = "multiChangeState";
    private static final String TC5 = "checkMultiChangeStateDifferent";

    private static final String TEST_NAME = "Milestone Change State Test ";
    private static final String NAME_ID = "name";
    private static final String MODIFY_DATE_ID = "modifyDate";
    private static final String DUE_DATE_ID = "dueDate";
    private static final String COMPLETION_DATE_ID = "completionDate";
    private static final String STATE_ID = "state";
    private static final String CORRECT_DATA_TASK_NAME = "Correct data";
    private static final String DCP = "Data Correction Process";
    private static final String CHANGE_STATE_MILESTONES_SUCCESS_MESSAGE = "All milestones changed state successfully.";
    private static final String NOT_ALL_MILESTONE_CHANGED_STATE_MESSAGE = "Not all milestones changed state.\n" +
            "Following milestones cannot be manually completed:";
    private static final String EMPTY_ATTRIBUTE2 = "—";
    private static final String EMPTY_ATTRIBUTE1 = "-";

    private static final String CHANGE_STATE_BUTTON = "setMilestonesStateContextAction";
    private static final String NEW_STATE = "New";
    private static final String NOT_NEEDED_STATE = "Not Needed";
    private static final String IN_PROGRESS_STATE = "In Progress";
    private static final String SUSPENDED_STATE = "Suspended";
    private static final String COMPLETED_STATE = "Completed";
    private static final String CHANGE_STATE_REASON = "CHANGE STATE SELENIUM TEST";
    private static final String VALIDATION_MESSAGE = "Cannot change states of selected milestones. All selected milestones should have the same state.";
    private static final String LEAD_TIME = "10";
    private static final Random RANDOM = new Random();
    private static final String INVALID_ATTRIBUTE_PATTERN =
            "Invalid '%1$s' parameter after {%2$s} -> {%3$s} state change operation for '%4$s' Milestone in '%5$s' test.";
    private static final String INVALID_CHANGE_STATE_MESSAGE_PATTERN =
            "Invalid system message after {%1$s} -> {%2$s} state change operation for '%3$s' milestone in '%4$s' test.";

    private static final String INVALID_IS_REASON_MANDATORY_PATTERN =
            "Invalid mandatory status for Reason parameter in {%1$s} -> {%2$s} state change operation for '%3$s' Milestone in '%4$s' test.";
    private static final String INVALID_CONFIRMATION_MESSAGE_PATTERN = "Invalid message in Confirmation Box in '%s' test.";
    private static final String CONFIRMATION_BOX_CLOSE_BUTTON_ID = "ConfirmationBox_milestones-state-change_wrong-input-info-app_close_button";
    private static final String TERMINATE_REASON = "Selenium Termination Process After Tests.";

    private SoftAssert softAssert;
    private final String processName = TEST_NAME + RANDOM.nextInt(Integer.MAX_VALUE);
    private final String milestoneName1 = TEST_NAME + "1." + RANDOM.nextInt(Integer.MAX_VALUE);
    private final String milestoneName2 = TEST_NAME + "2." + RANDOM.nextInt(Integer.MAX_VALUE);
    private final String milestoneName3 = TEST_NAME + "3." + RANDOM.nextInt(Integer.MAX_VALUE);
    private final long PLUS_DAYS = 5L;

    private MilestoneViewPage milestoneViewPage;
    private ChangeStateMilestoneWizardPage changeStateMilestoneWizardPage;

    private void assertDueDate(String startDueDate, String dueDate, String newDueDate,
                               String previousState, String nextState, String milestoneName, String testName) {
        String message = String.format(INVALID_ATTRIBUTE_PATTERN, DUE_DATE_ID, previousState, nextState, milestoneName, testName);
        if (startDueDate.equals(EMPTY_ATTRIBUTE2) || startDueDate.equals(EMPTY_ATTRIBUTE1)) {
            Assert.assertTrue(newDueDate.equals(EMPTY_ATTRIBUTE2) || newDueDate.equals(EMPTY_ATTRIBUTE1), message);
        } else {
            Assert.assertEquals(newDueDate, dueDate, message);
        }
    }

    private void changeMilestoneStateSimple(String previousState, String nextState, String milestoneName, String testName) {
        milestoneViewPage.callAction(CHANGE_STATE_BUTTON);
        changeStateMilestoneWizardPage.setState(nextState);
        changeStateMilestoneWizardPage.setReason(ChangeMilestoneStateTest.CHANGE_STATE_REASON);
        changeStateMilestoneWizardPage.accept();
        assertSystemMessage(CHANGE_STATE_MILESTONES_SUCCESS_MESSAGE, SystemMessageContainer.MessageType.SUCCESS,
                String.format(INVALID_CHANGE_STATE_MESSAGE_PATTERN, previousState, nextState, milestoneName, testName));
    }

    private void changeMilestoneState(String previousState, String nextState, boolean isReasonMandatory, String testName) {
        waitForPageToLoad();
        String milestoneName = milestoneViewPage.getMilestoneAttribute(NAME_ID);
        String startModifyDate = milestoneViewPage.getMilestoneAttribute(MODIFY_DATE_ID);
        String startDueDate = milestoneViewPage.getMilestoneAttribute(DUE_DATE_ID);
        String startCompletionDate = milestoneViewPage.getMilestoneAttribute(COMPLETION_DATE_ID);

        milestoneViewPage.callAction(CHANGE_STATE_BUTTON);
        changeStateMilestoneWizardPage.setState(nextState);
        boolean isMandatory = changeStateMilestoneWizardPage.isReasonMandatory();
        Assert.assertEquals(isMandatory, isReasonMandatory,
                String.format(INVALID_IS_REASON_MANDATORY_PATTERN, previousState, nextState, milestoneName, testName));
        if (isMandatory) {
            changeStateMilestoneWizardPage.setReason(ChangeMilestoneStateTest.CHANGE_STATE_REASON);
        }
        if (nextState.equals(COMPLETED_STATE)) {
            changeStateMilestoneWizardPage.setApprovalDate(PLUS_DAYS);
        }
        changeStateMilestoneWizardPage.accept();

        assertSystemMessage(CHANGE_STATE_MILESTONES_SUCCESS_MESSAGE, SystemMessageContainer.MessageType.SUCCESS,
                String.format(INVALID_CHANGE_STATE_MESSAGE_PATTERN, previousState, nextState, milestoneName, testName));
        String newStateValue = milestoneViewPage.getMilestoneAttribute(STATE_ID);
        String newModifyDate = milestoneViewPage.getMilestoneAttribute(MODIFY_DATE_ID);
        String newDueDate = milestoneViewPage.getMilestoneAttribute(DUE_DATE_ID);
        String newCompletionDate = milestoneViewPage.getMilestoneAttribute(COMPLETION_DATE_ID);
        String approvalDate = LocalDate.now().plusDays(PLUS_DAYS).toString();
        String currentLeadDate = LocalDate.now().plusDays(Long.parseLong(LEAD_TIME)).toString();
        Assert.assertEquals(newStateValue, nextState,
                String.format(INVALID_ATTRIBUTE_PATTERN, STATE_ID, previousState, nextState, milestoneName, testName));
        Assert.assertNotEquals(newModifyDate, startModifyDate,
                String.format(INVALID_ATTRIBUTE_PATTERN, MODIFY_DATE_ID, previousState, nextState, milestoneName, testName));
        String message = String.format(INVALID_ATTRIBUTE_PATTERN, COMPLETION_DATE_ID, previousState, nextState, milestoneName, testName);
        switch (nextState) {
            case NOT_NEEDED_STATE:
                Assert.assertTrue(newCompletionDate.equals(EMPTY_ATTRIBUTE2) ||
                        newCompletionDate.equals(EMPTY_ATTRIBUTE1), message);
                assertDueDate(startDueDate, startDueDate, newDueDate, previousState, nextState, milestoneName, testName);
                break;
            case NEW_STATE:
                Assert.assertEquals(newCompletionDate, currentLeadDate, message);
                assertDueDate(startDueDate, startDueDate, newDueDate, previousState, nextState, milestoneName, testName);
                break;
            case IN_PROGRESS_STATE:
                Assert.assertEquals(newCompletionDate, currentLeadDate, message);
                if (previousState.equals(NEW_STATE)) {
                    assertDueDate(startDueDate, startDueDate, newDueDate, previousState, nextState, milestoneName, testName);
                } else {
                    assertDueDate(startDueDate, currentLeadDate, newDueDate, previousState, nextState, milestoneName, testName);
                }
                break;
            case SUSPENDED_STATE:
                Assert.assertEquals(newCompletionDate, startCompletionDate, message);
                break;
            case COMPLETED_STATE:
                Assert.assertEquals(newCompletionDate, approvalDate, message);
                break;
        }
    }


    @BeforeClass
    public void createProcessWithMilestones() {
        softAssert = new SoftAssert();
        ProcessOverviewPage processOverviewPage = ProcessOverviewPage.goToProcessOverviewPage(driver, BASIC_URL).clearAllColumnFilters();
        changeStateMilestoneWizardPage = new ChangeStateMilestoneWizardPage(driver);

        Milestone milestone1 = Milestone.builder()
                .setLeadTime(LEAD_TIME)
                .setRelatedTask(CORRECT_DATA_TASK_NAME)
                .setDueDate(LocalDate.now().toString())
                .setName(milestoneName1).build();
        Milestone milestone2 = Milestone.builder()
                .setLeadTime(LEAD_TIME)
                .setRelatedTask(CORRECT_DATA_TASK_NAME)
                .setIsActive("true")
                .setName(milestoneName2).build();
        Milestone milestone3 = Milestone.builder()
                .setLeadTime(LEAD_TIME)
                .setRelatedTask(CORRECT_DATA_TASK_NAME)
                .setIsManualCompletion("true")
                .setName(milestoneName3).build();

        MilestonesStepWizardPage milestonesStepWizardPage = processOverviewPage.openProcessCreationWizard()
                .defineProcessAndGoToMilestonesStep(processName, 5L, DCP);
        milestonesStepWizardPage.addMilestoneRow(milestone1);
        milestonesStepWizardPage.addMilestoneRow(milestone2);
        milestonesStepWizardPage.addMilestoneRow(milestone3);
        milestonesStepWizardPage.clickAcceptButton();
    }

    @Test(priority = 1, description = "First Milestone Change State Flow")
    @Description("First Milestone Change State Flow:\n not needed → new → in progress → not needed")
    public void firstMilestoneFlow() {
        /**
         * not needed → new → in progress → not needed
         */
        milestoneViewPage = MilestoneViewPage.goToMilestoneViewPage(driver, BASIC_URL);
        milestoneViewPage.selectMilestone(milestoneName1);

        changeMilestoneState(NOT_NEEDED_STATE, NEW_STATE, true, TC1);
        changeMilestoneState(NEW_STATE, IN_PROGRESS_STATE, false, TC1);
        changeMilestoneState(IN_PROGRESS_STATE, NOT_NEEDED_STATE, true, TC1);
    }

    @Test(priority = 2, description = "Second Milestone Change State Flow")
    @Description("Second Milestone Change State Flow:\n new → in progress → completed (not permitted).....{change user}.....in progress → completed")
    public void secondMilestoneFlow() {
        /**
         * new → in progress → completed (not permitted).....{change manual completion flag}.....in progress → completed
         */
        milestoneViewPage = MilestoneViewPage.goToMilestoneViewPage(driver, BASIC_URL);
        milestoneViewPage.selectMilestone(milestoneName2);

        changeMilestoneState(NEW_STATE, IN_PROGRESS_STATE, false, TC2);

        //try to change state to Completed
        String startModifyDate = milestoneViewPage.getMilestoneAttribute(MODIFY_DATE_ID);
        milestoneViewPage.callAction(CHANGE_STATE_BUTTON);
        changeStateMilestoneWizardPage.setState(COMPLETED_STATE);
        changeStateMilestoneWizardPage.setApprovalDate(PLUS_DAYS);
        changeStateMilestoneWizardPage.accept();
        assertSystemMessage(NOT_ALL_MILESTONE_CHANGED_STATE_MESSAGE + milestoneName2 + ".",
                SystemMessageContainer.MessageType.WARNING,
                String.format(INVALID_CHANGE_STATE_MESSAGE_PATTERN, IN_PROGRESS_STATE, COMPLETED_STATE, milestoneName2, TC2));

        String newModifyDate = milestoneViewPage.getMilestoneAttribute(MODIFY_DATE_ID);
        String newStateValue = milestoneViewPage.getMilestoneAttribute(STATE_ID);
        Assert.assertEquals(newModifyDate, startModifyDate,
                String.format(INVALID_ATTRIBUTE_PATTERN, MODIFY_DATE_ID, IN_PROGRESS_STATE, COMPLETED_STATE, milestoneName2, TC2));
        Assert.assertEquals(newStateValue, IN_PROGRESS_STATE,
                String.format(INVALID_ATTRIBUTE_PATTERN, STATE_ID, IN_PROGRESS_STATE, COMPLETED_STATE, milestoneName2, TC2));

        // change isManualCompletion flag and try again
        Milestone editManualCompletionMilestone = Milestone.builder().setIsManualCompletion("true").build();
        milestoneViewPage.callAction(EDIT_MILESTONE_BUTTON);
        EditMilestoneWizardPage editWizard = new EditMilestoneWizardPage(driver);
        editWizard.editMilestone(editManualCompletionMilestone);
        waitForPageToLoad();
        changeMilestoneState(IN_PROGRESS_STATE, COMPLETED_STATE, false, TC2);
    }

    @Test(priority = 3, description = "Third Milestone Change State Flow")
    @Description("Third Milestone Change State Flow:\n not needed → new → suspended → in progress → completed → in progress → suspended → new → not needed")
    public void thirdMilestoneFlow() {
        /**
         * not needed → new → suspended → in progress → completed → in progress → suspended → new → not needed
         */
        milestoneViewPage = MilestoneViewPage.goToMilestoneViewPage(driver, BASIC_URL);
        milestoneViewPage.selectMilestone(milestoneName3);

        changeMilestoneState(NOT_NEEDED_STATE, NEW_STATE, true, TC3);
        changeMilestoneState(NEW_STATE, SUSPENDED_STATE, true, TC3);
        changeMilestoneState(SUSPENDED_STATE, IN_PROGRESS_STATE, false, TC3);
        changeMilestoneState(IN_PROGRESS_STATE, COMPLETED_STATE, false, TC3);
        changeMilestoneState(COMPLETED_STATE, IN_PROGRESS_STATE, true, TC3);
        changeMilestoneState(IN_PROGRESS_STATE, SUSPENDED_STATE, true, TC3);
        changeMilestoneState(SUSPENDED_STATE, NEW_STATE, false, TC3);
        changeMilestoneState(NEW_STATE, NOT_NEEDED_STATE, true, TC3);
    }

    @Test(priority = 4, description = "Multiselect Milestones Change State", dependsOnMethods = {TC1, TC3})
    @Description("Multiselect Milestones Change State:\n not needed → new → in progress → suspended → in progress")
    public void multiChangeState() {
        /**
         * not needed → new → in progress → suspended → in progress
         */
        milestoneViewPage = MilestoneViewPage.goToMilestoneViewPage(driver, BASIC_URL);

        milestoneViewPage.selectMilestone(milestoneName1);
        milestoneViewPage.selectMilestone(milestoneName3);
        String milestoneNames = milestoneName1 + ", " + milestoneName3;
        changeMilestoneStateSimple(NOT_NEEDED_STATE, NEW_STATE, milestoneNames, TC4);
        changeMilestoneStateSimple(NEW_STATE, IN_PROGRESS_STATE, milestoneNames, TC4);
        changeMilestoneStateSimple(IN_PROGRESS_STATE, SUSPENDED_STATE, milestoneNames, TC4);
        changeMilestoneStateSimple(SUSPENDED_STATE, IN_PROGRESS_STATE, milestoneNames, TC4);

        milestoneViewPage.clearFilters();
        milestoneViewPage.selectAll();
        milestoneViewPage.selectAll();
        milestoneViewPage.selectMilestone(milestoneName1);
        String state1 = milestoneViewPage.getMilestoneAttribute(STATE_ID);

        milestoneViewPage.clearFilters();
        milestoneViewPage.selectAll();
        milestoneViewPage.selectAll();
        milestoneViewPage.selectMilestone(milestoneName3);
        String state2 = milestoneViewPage.getMilestoneAttribute(STATE_ID);
        Assert.assertEquals(state1, IN_PROGRESS_STATE,
                String.format(INVALID_ATTRIBUTE_PATTERN, STATE_ID, NOT_NEEDED_STATE, IN_PROGRESS_STATE, milestoneName1, TC4));
        Assert.assertEquals(state2, IN_PROGRESS_STATE,
                String.format(INVALID_ATTRIBUTE_PATTERN, STATE_ID, NOT_NEEDED_STATE, IN_PROGRESS_STATE, milestoneName3, TC4));
    }

    @Test(priority = 5, description = "Multiselect Milestones Change State (Different States)", dependsOnMethods = {TC1, TC2})
    @Description("Multiselect Milestones Change State (Different States)")
    public void checkMultiChangeStateDifferent() {
        milestoneViewPage = MilestoneViewPage.goToMilestoneViewPage(driver, BASIC_URL);

        milestoneViewPage.selectMilestone(milestoneName1);
        milestoneViewPage.selectMilestone(milestoneName2);

        milestoneViewPage.callAction(CHANGE_STATE_BUTTON);
        ConfirmationBox milestoneConfirmationBox = ConfirmationBox.create(driver, webDriverWait);

        String validationMessage = milestoneConfirmationBox.getMessage();
        Assert.assertEquals(validationMessage, VALIDATION_MESSAGE, String.format(INVALID_CONFIRMATION_MESSAGE_PATTERN, TC5));
        milestoneConfirmationBox.clickButtonById(CONFIRMATION_BOX_CLOSE_BUTTON_ID);
    }

    @Test(priority = 6, description = "Checking system message summary")
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



package com.oss.bpm.milestones;

import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.mainheader.ToolbarWidget;
import com.oss.framework.components.prompts.ConfirmationBox;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.bpm.ProcessOverviewPage;
import com.oss.pages.bpm.milestones.ChangeStateMilestoneWizardPage;
import com.oss.pages.bpm.milestones.EditMilestoneWizardPage;
import com.oss.pages.bpm.milestones.Milestone;
import com.oss.pages.bpm.milestones.MilestoneViewPage;
import com.oss.pages.bpm.processinstances.MilestonesStepWizardPage;
import com.oss.utils.TestListener;
import io.qameta.allure.Description;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.time.LocalDate;

import static com.oss.bpm.milestones.EditMilestoneTest.EDIT_MILESTONE_BUTTON;

/**
 * @author Paweł Rother
 */

@Listeners({TestListener.class})
public class ChangeMilestoneStateTest extends BaseTestCase {
    private static final String BPM_USER_LOGIN = "bpm_webselenium";
    private static final String BPM_USER_PASSWORD = "Webtests123!";
    private static final String BPM_ADMIN_USER_LOGIN = "bpm_admin_webselenium";
    private static final String BPM_ADMIN_USER_PASSWORD = "Webtests123!";

    private static final String PROCESS_NAME = "Selenium Test.Milestone-";
    private static final String MODIFY_DATE_ID = "modifyDate";
    private static final String DUE_DATE_ID = "dueDate";
    private static final String COMPLETION_DATE_ID = "completionDate";
    private static final String STATE_ID = "state";
    private static final String CORRECT_DATA_TASK_NAME = "Correct data";
    private static final String DCP = "Data Correction Process";
    private static final String CHANGE_STATE_MILESTONES_SUCCESS_MESSAGE = "All milestones changed state successfully.";
    private static final String NO_SYSTEM_MESSAGE_EXCEPTION = "There is no any System Message";
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
    private final String milestoneName1 = "Milestone Update " + (int) (Math.random() * 100001);
    private final String milestoneName2 = "Milestone Update " + (int) (Math.random() * 100001);
    private final String milestoneName3 = "Milestone Update " + (int) (Math.random() * 100001);
    private final long PLUS_DAYS = 5L;

    private MilestoneViewPage milestoneViewPage;
    private ChangeStateMilestoneWizardPage changeStateMilestoneWizardPage;

    private void assertDueDate(String startDueDate, String dueDate, String newDueDate) {
        if (startDueDate.equals(EMPTY_ATTRIBUTE2) || startDueDate.equals(EMPTY_ATTRIBUTE1)) {
            Assert.assertTrue(newDueDate.equals(EMPTY_ATTRIBUTE2) || newDueDate.equals(EMPTY_ATTRIBUTE1));
        } else {
            Assert.assertEquals(dueDate, newDueDate);
        }
    }

    private void changeMilestoneStateSimple(String nextState) {
        milestoneViewPage.callAction(CHANGE_STATE_BUTTON);
        changeStateMilestoneWizardPage.setState(nextState);
        changeStateMilestoneWizardPage.setReason(ChangeMilestoneStateTest.CHANGE_STATE_REASON);
        changeStateMilestoneWizardPage.accept();
        String message = SystemMessageContainer.create(driver, webDriverWait).getFirstMessage().orElseThrow(()
                -> new RuntimeException(NO_SYSTEM_MESSAGE_EXCEPTION)).getText();
        Assert.assertEquals(message, CHANGE_STATE_MILESTONES_SUCCESS_MESSAGE);
    }

    private void changeMilestoneState(String previousState, String nextState, boolean isReasonMandatory) {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        DelayUtils.sleep(500);

        String startModifyDate = milestoneViewPage.getMilestoneAttribute(MODIFY_DATE_ID);
        String startDueDate = milestoneViewPage.getMilestoneAttribute(DUE_DATE_ID);
        String startCompletionDate = milestoneViewPage.getMilestoneAttribute(COMPLETION_DATE_ID);

        milestoneViewPage.callAction(CHANGE_STATE_BUTTON);
        changeStateMilestoneWizardPage.setState(nextState);
        boolean isMandatory = changeStateMilestoneWizardPage.isReasonMandatory();
        Assert.assertEquals(isMandatory, isReasonMandatory);
        if (isMandatory) {
            changeStateMilestoneWizardPage.setReason(ChangeMilestoneStateTest.CHANGE_STATE_REASON);
        }
        if (nextState.equals(COMPLETED_STATE)) {
            changeStateMilestoneWizardPage.setApprovalDate(PLUS_DAYS);
        }
        changeStateMilestoneWizardPage.accept();

        String message = SystemMessageContainer.create(driver, webDriverWait).getFirstMessage().orElseThrow(()
                -> new RuntimeException(NO_SYSTEM_MESSAGE_EXCEPTION)).getText();
        Assert.assertEquals(message, CHANGE_STATE_MILESTONES_SUCCESS_MESSAGE);
        DelayUtils.sleep(1000);
        String newStateValue = milestoneViewPage.getMilestoneAttribute(STATE_ID);
        String newModifyDate = milestoneViewPage.getMilestoneAttribute(MODIFY_DATE_ID);
        String newDueDate = milestoneViewPage.getMilestoneAttribute(DUE_DATE_ID);
        String newCompletionDate = milestoneViewPage.getMilestoneAttribute(COMPLETION_DATE_ID);
        String approvalDate = LocalDate.now().plusDays(PLUS_DAYS).toString();
        String currentLeadDate = LocalDate.now().plusDays(Long.parseLong(LEAD_TIME)).toString();
        Assert.assertEquals(nextState, newStateValue);
        Assert.assertNotEquals(newModifyDate, startModifyDate);
        switch (nextState) {
            case NOT_NEEDED_STATE:
                Assert.assertTrue(newCompletionDate.equals(EMPTY_ATTRIBUTE2) || newCompletionDate.equals(EMPTY_ATTRIBUTE1));
                assertDueDate(startDueDate, startDueDate, newDueDate);
                break;
            case NEW_STATE:
                Assert.assertEquals(currentLeadDate, newCompletionDate);
                assertDueDate(startDueDate, startDueDate, newDueDate);
                break;
            case IN_PROGRESS_STATE:
                Assert.assertEquals(currentLeadDate, newCompletionDate);
                if (previousState.equals(NEW_STATE)) {
                    assertDueDate(startDueDate, startDueDate, newDueDate);
                } else {
                    assertDueDate(startDueDate, currentLeadDate, newDueDate);
                }
                break;
            case SUSPENDED_STATE:
                Assert.assertEquals(startCompletionDate, newCompletionDate);
                break;
            case COMPLETED_STATE:
                Assert.assertEquals(approvalDate, newCompletionDate);
                break;
        }
    }


    @BeforeClass
    public void createProcessWithMilestones() {
        ProcessOverviewPage processOverviewPage = ProcessOverviewPage.goToProcessOverviewPage(driver, BASIC_URL);

        ToolbarWidget toolbarWidget = ToolbarWidget.create(driver, webDriverWait);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        if (!toolbarWidget.getUserName().equals(BPM_USER_LOGIN)) {
            changeStateMilestoneWizardPage.changeUser(BPM_USER_LOGIN, BPM_USER_PASSWORD);
        }
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        processOverviewPage.clearAllColumnFilters();
        changeStateMilestoneWizardPage = new ChangeStateMilestoneWizardPage(driver);

        String processName = PROCESS_NAME + (int) (Math.random() * 100001);

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
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        milestoneViewPage = MilestoneViewPage.goToMilestoneViewPage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        milestoneViewPage.selectMilestone(milestoneName1);

        changeMilestoneState(NOT_NEEDED_STATE, NEW_STATE, true);
        changeMilestoneState(NEW_STATE, IN_PROGRESS_STATE, false);
        changeMilestoneState(IN_PROGRESS_STATE, NOT_NEEDED_STATE, true);
    }

    @Test(priority = 2, description = "Second Milestone Change State Flow")
    @Description("Second Milestone Change State Flow:\n new → in progress → completed (not permitted).....{change user}.....in progress → completed")
    public void secondMilestoneFlow() {
        /**
         * new → in progress → completed (not permitted).....{change manual completion flag}.....in progress → completed
         */
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        milestoneViewPage = MilestoneViewPage.goToMilestoneViewPage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        milestoneViewPage.selectMilestone(milestoneName2);

        changeMilestoneState(NEW_STATE, IN_PROGRESS_STATE, false);

        //try to change state to Completed
        String startModifyDate = milestoneViewPage.getMilestoneAttribute(MODIFY_DATE_ID);
        milestoneViewPage.callAction(CHANGE_STATE_BUTTON);
        changeStateMilestoneWizardPage.setState(COMPLETED_STATE);
        changeStateMilestoneWizardPage.setApprovalDate(PLUS_DAYS);
        changeStateMilestoneWizardPage.accept();
        String message = SystemMessageContainer.create(driver, webDriverWait).getFirstMessage().orElseThrow(()
                -> new RuntimeException(NO_SYSTEM_MESSAGE_EXCEPTION)).getText();
        Assert.assertEquals(message, NOT_ALL_MILESTONE_CHANGED_STATE_MESSAGE + milestoneName2 + ".");
        String newModifyDate = milestoneViewPage.getMilestoneAttribute(MODIFY_DATE_ID);
        String newStateValue = milestoneViewPage.getMilestoneAttribute(STATE_ID);
        Assert.assertEquals(newModifyDate, startModifyDate);
        Assert.assertEquals(newStateValue, IN_PROGRESS_STATE);

        // change isManualCompletion flag and try again
        Milestone editManualCompletionMilestone = Milestone.builder().setIsManualCompletion("true").build();
        milestoneViewPage.callAction(EDIT_MILESTONE_BUTTON);
        EditMilestoneWizardPage editWizard = new EditMilestoneWizardPage(driver);
        editWizard.editMilestone(editManualCompletionMilestone);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        milestoneViewPage.selectMilestone(milestoneName2);
        changeMilestoneState(IN_PROGRESS_STATE, COMPLETED_STATE, false);
    }

    @Test(priority = 3, description = "Third Milestone Change State Flow")
    @Description("Third Milestone Change State Flow:\n not needed → new → suspended → in progress → completed → in progress → suspended → new → not needed")
    public void thirdMilestoneFlow() {
        /**
         * not needed → new → suspended → in progress → completed → in progress → suspended → new → not needed
         */
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        milestoneViewPage = MilestoneViewPage.goToMilestoneViewPage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        milestoneViewPage.selectMilestone(milestoneName3);

        changeMilestoneState(NOT_NEEDED_STATE, NEW_STATE, true);
        changeMilestoneState(NEW_STATE, SUSPENDED_STATE, true);
        changeMilestoneState(SUSPENDED_STATE, IN_PROGRESS_STATE, false);
        changeMilestoneState(IN_PROGRESS_STATE, COMPLETED_STATE, false);
        changeMilestoneState(COMPLETED_STATE, IN_PROGRESS_STATE, true);
        changeMilestoneState(IN_PROGRESS_STATE, SUSPENDED_STATE, true);
        changeMilestoneState(SUSPENDED_STATE, NEW_STATE, false);
        changeMilestoneState(NEW_STATE, NOT_NEEDED_STATE, true);
    }

    @Test(priority = 4, description = "Multiselect Milestones Change State",
            dependsOnMethods = {"firstMilestoneFlow", "thirdMilestoneFlow"})
    @Description("Multiselect Milestones Change State:\n not needed → new → in progress → suspended → in progress")
    public void multiChangeState() {
        /**
         * not needed → new → in progress → suspended → in progress
         */
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        milestoneViewPage = MilestoneViewPage.goToMilestoneViewPage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        milestoneViewPage.selectMilestone(milestoneName1);
        milestoneViewPage.selectMilestone(milestoneName3);
        changeMilestoneStateSimple(NEW_STATE);
        changeMilestoneStateSimple(IN_PROGRESS_STATE);
        changeMilestoneStateSimple(SUSPENDED_STATE);
        changeMilestoneStateSimple(IN_PROGRESS_STATE);

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
        Assert.assertEquals(state1, IN_PROGRESS_STATE);
        Assert.assertEquals(state2, IN_PROGRESS_STATE);
    }

    @Test(priority = 5, description = "Multiselect Milestones Change State (Different States)",
            dependsOnMethods = {"firstMilestoneFlow", "secondMilestoneFlow"})
    @Description("Multiselect Milestones Change State (Different States)")
    public void checkMultiChangeStateDifferent() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        milestoneViewPage = MilestoneViewPage.goToMilestoneViewPage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        milestoneViewPage.selectMilestone(milestoneName1);
        milestoneViewPage.selectMilestone(milestoneName2);

        milestoneViewPage.callAction(CHANGE_STATE_BUTTON);
        ConfirmationBox milestoneConfirmationBox = ConfirmationBox.create(driver, webDriverWait);

        String validationMessage = milestoneConfirmationBox.getMessage();
        Assert.assertEquals(validationMessage, VALIDATION_MESSAGE);
    }

}



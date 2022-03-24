package com.oss.bpm;

import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.mainheader.ToolbarWidget;
import com.oss.framework.components.prompts.ConfirmationBox;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.bpm.milestones.ChangeStateMilestoneWizardPage;
import com.oss.pages.bpm.milestones.Milestone;
import com.oss.pages.bpm.milestones.MilestoneViewPage;
import com.oss.pages.bpm.processinstances.ProcessInstancesPage;
import com.oss.pages.bpm.processinstances.ProcessWizardPage;
import com.oss.utils.TestListener;
import io.qameta.allure.Description;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.time.LocalDate;

/**
 * @author Paweł Rother
 */

/**
 * Tests must be run by user without BPMMilestone.ADMIN user permissions:
 */

@Listeners({TestListener.class})
public class ChangeMilestoneStateTest extends BaseTestCase {

    private final String BPM_USER_LOGIN = "bpm_webselenium";
    private final String BPM_USER_PASSWORD = "Webtests123!";
    private final String BPM_ADMIN_USER_LOGIN = "bpm_admin_webselenium";
    private final String BPM_ADMIN_USER_PASSWORD = "Webtests123!";

    private final String milestoneName1 = "Milestone Update " + (int) (Math.random() * 10001);
    private final String milestoneName2 = "Milestone Update " + (int) (Math.random() * 10001);
    private final String milestoneName3 = "Milestone Update " + (int) (Math.random() * 10001);
    private final String description = "Milestone Update " + (Math.random() * 1001);
    private static final String CHANGE_STATE_BUTTON = "setMilestonesStateContextAction";
    private static final String NEW_STATE = "New";
    private static final String NOT_NEEDED_STATE = "Not Needed";
    private static final String IN_PROGRESS_STATE = "In Progress";
    private static final String SUSPENDED_STATE = "Suspended";
    private static final String COMPLETED_STATE = "Completed";
    private static final String CHANGE_STATE_REASON = "CHANGE STATE SELENIUM TEST";
    private static final String VALIDATION_MESSAGE = "Cannot change states of selected milestones. All selected milestones should have the same state.";
    private static final String LEAD_TIME = "10";
    private static final long PLUS_DAYS = 5L;

    private MilestoneViewPage milestoneViewPage;
    private ChangeStateMilestoneWizardPage changeStateMilestoneWizardPage;

    private void assertDueDate(String startDueDate, String dueDate, String newDueDate) {
        if (startDueDate.isEmpty()) {
            Assert.assertEquals(newDueDate, "");
        } else {
            Assert.assertEquals(dueDate, newDueDate);
        }
    }

    private void changeMilestoneStateSimple(String nextState, String reason) {
        milestoneViewPage.callAction(CHANGE_STATE_BUTTON);
        changeStateMilestoneWizardPage.setState(nextState);
        changeStateMilestoneWizardPage.setReason(reason);
        changeStateMilestoneWizardPage.accept();
        String message = SystemMessageContainer.create(driver, webDriverWait).getFirstMessage().orElseThrow(() -> new RuntimeException("There is no any System Message")).getText();
        Assert.assertEquals(message, "All milestones changed state successfully.");
    }

    private void changeMilestoneState(String previousState, String nextState, String reason, boolean isReasonMandatory) {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        DelayUtils.sleep(500);

        String startModifyDate = milestoneViewPage.getMilestoneAttribute("modifyDate");
        String startDueDate = milestoneViewPage.getMilestoneAttribute("dueDate");
        String startCompletionDate = milestoneViewPage.getMilestoneAttribute("completionDate");

        milestoneViewPage.callAction(CHANGE_STATE_BUTTON);
        changeStateMilestoneWizardPage.setState(nextState);
        boolean isMandatory = changeStateMilestoneWizardPage.isReasonMandatory();
        Assert.assertEquals(isMandatory, isReasonMandatory);
        if (isMandatory) {
            changeStateMilestoneWizardPage.setReason(reason);
        }
        if (nextState.equals(COMPLETED_STATE)) {
            changeStateMilestoneWizardPage.setApprovalDate(PLUS_DAYS);
        }
        changeStateMilestoneWizardPage.accept();

        String message = SystemMessageContainer.create(driver, webDriverWait).getFirstMessage().orElseThrow(() -> new RuntimeException("There is no any System Message")).getText();
        Assert.assertEquals(message, "All milestones changed state successfully.");
        DelayUtils.sleep(1000);
        String newStateValue = milestoneViewPage.getMilestoneAttribute("state");
        String newModifyDate = milestoneViewPage.getMilestoneAttribute("modifyDate");
        String newDueDate = milestoneViewPage.getMilestoneAttribute("dueDate");
        String newCompletionDate = milestoneViewPage.getMilestoneAttribute("completionDate");
        String approvalDate = LocalDate.now().plusDays(PLUS_DAYS).toString();
        String currentLeadDate = LocalDate.now().plusDays(Long.parseLong(LEAD_TIME)).toString();
        Assert.assertEquals(nextState, newStateValue);
        Assert.assertNotEquals(newModifyDate, startModifyDate);
        switch (nextState) {
            case NOT_NEEDED_STATE:
                Assert.assertEquals(newCompletionDate, "");
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
        ProcessInstancesPage processInstancesPage = ProcessInstancesPage.goToProcessInstancesPage(driver, BASIC_URL);
        processInstancesPage.clearAllColumnFilters();
        changeStateMilestoneWizardPage = new ChangeStateMilestoneWizardPage(driver);

        ToolbarWidget toolbarWidget = ToolbarWidget.create(driver, webDriverWait);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        if (!toolbarWidget.getUserName().equals(BPM_USER_LOGIN)) {
            changeStateMilestoneWizardPage.changeUser(BPM_USER_LOGIN, BPM_USER_PASSWORD);
        }
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        String processName = "Selenium Test.Milestone-" + (int) (Math.random() * 1001);
        ProcessWizardPage processWizardPage = new ProcessWizardPage(driver);

        Milestone milestone1 = Milestone.builder()
                .setLeadTime(LEAD_TIME)
                .setRelatedTask("Correct data")
                .setDueDate(LocalDate.now().toString())
                .setName(milestoneName1).build();
        Milestone milestone2 = Milestone.builder()
                .setLeadTime(LEAD_TIME)
                .setRelatedTask("Correct data")
                .setIsActive("true")
                .setName(milestoneName2).build();
        Milestone milestone3 = Milestone.builder()
                .setLeadTime(LEAD_TIME)
                .setRelatedTask("Correct data")
                .setIsManualCompletion("true")
                .setName(milestoneName3).build();

        ProcessWizardPage.MilestoneStepWizard milestoneStep = processWizardPage.definedMilestoneInProcess(processName, 5L,
                "Data Correction Process");

        milestoneStep.addMilestoneRow(milestone1);
        milestoneStep.addMilestoneRow(milestone2);
        milestoneStep.addMilestoneRow(milestone3);
        processWizardPage.clickAcceptButton();
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

        changeMilestoneState(NOT_NEEDED_STATE, NEW_STATE, CHANGE_STATE_REASON, true);
        changeMilestoneState(NEW_STATE, IN_PROGRESS_STATE, CHANGE_STATE_REASON, false);
        changeMilestoneState(IN_PROGRESS_STATE, NOT_NEEDED_STATE, CHANGE_STATE_REASON, true);
    }

    @Test(priority = 2, description = "Second Milestone Change State Flow")
    @Description("Second Milestone Change State Flow:\n new → in progress → completed (not permitted).....{change user}.....in progress → completed")
    public void secondMilestoneFlow() {
        /**
         * new → in progress → completed (not permitted).....{change user}.....in progress → completed
         */
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        milestoneViewPage = MilestoneViewPage.goToMilestoneViewPage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        milestoneViewPage.selectMilestone(milestoneName2);

        changeMilestoneState(NEW_STATE, IN_PROGRESS_STATE, CHANGE_STATE_REASON, false);

        //try to change state to Completed
        String startModifyDate = milestoneViewPage.getMilestoneAttribute("modifyDate");
        milestoneViewPage.callAction(CHANGE_STATE_BUTTON);
        changeStateMilestoneWizardPage.setState(COMPLETED_STATE);
        changeStateMilestoneWizardPage.setApprovalDate(PLUS_DAYS);
        changeStateMilestoneWizardPage.accept();
        String message = SystemMessageContainer.create(driver, webDriverWait).getFirstMessage().orElseThrow(() -> new RuntimeException("There is no any System Message")).getText();
        Assert.assertEquals("Not all milestones changed state.\n" +
                "Following milestones cannot be manually completed:" + milestoneName2 + ".", message);
        String newModifyDate = milestoneViewPage.getMilestoneAttribute("modifyDate");
        String newStateValue = milestoneViewPage.getMilestoneAttribute("state");
        Assert.assertEquals(startModifyDate, newModifyDate);
        Assert.assertEquals(newStateValue, IN_PROGRESS_STATE);

        // change user for admin and try again
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        changeStateMilestoneWizardPage.changeUser(BPM_ADMIN_USER_LOGIN, BPM_ADMIN_USER_PASSWORD);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        milestoneViewPage.selectMilestone(milestoneName2);
        changeMilestoneState(IN_PROGRESS_STATE, COMPLETED_STATE, CHANGE_STATE_REASON, false);
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
        milestoneViewPage.changeUser(BPM_USER_LOGIN, BPM_USER_PASSWORD);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        milestoneViewPage.selectMilestone(milestoneName3);

        changeMilestoneState(NOT_NEEDED_STATE, NEW_STATE, CHANGE_STATE_REASON, true);
        changeMilestoneState(NEW_STATE, SUSPENDED_STATE, CHANGE_STATE_REASON, true);
        changeMilestoneState(SUSPENDED_STATE, IN_PROGRESS_STATE, CHANGE_STATE_REASON, false);
        changeMilestoneState(IN_PROGRESS_STATE, COMPLETED_STATE, CHANGE_STATE_REASON, false);
        changeMilestoneState(COMPLETED_STATE, IN_PROGRESS_STATE, CHANGE_STATE_REASON, true);
        changeMilestoneState(IN_PROGRESS_STATE, SUSPENDED_STATE, CHANGE_STATE_REASON, true);
        changeMilestoneState(SUSPENDED_STATE, NEW_STATE, CHANGE_STATE_REASON, false);
        changeMilestoneState(NEW_STATE, NOT_NEEDED_STATE, CHANGE_STATE_REASON, true);
    }

    @Test(priority = 4, description = "Multiselect Milestones Change State")
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
        changeMilestoneStateSimple(NEW_STATE, CHANGE_STATE_REASON);
        changeMilestoneStateSimple(IN_PROGRESS_STATE, CHANGE_STATE_REASON);
        changeMilestoneStateSimple(SUSPENDED_STATE, CHANGE_STATE_REASON);
        changeMilestoneStateSimple(IN_PROGRESS_STATE, CHANGE_STATE_REASON);

        milestoneViewPage.clearFilters();
        milestoneViewPage.selectAll();
        milestoneViewPage.selectAll();
        milestoneViewPage.selectMilestone(milestoneName1);
        String state1 = milestoneViewPage.getMilestoneAttribute("state");

        milestoneViewPage.clearFilters();
        milestoneViewPage.selectAll();
        milestoneViewPage.selectAll();
        milestoneViewPage.selectMilestone(milestoneName3);
        String state2 = milestoneViewPage.getMilestoneAttribute("state");
        Assert.assertEquals(state1, IN_PROGRESS_STATE);
        Assert.assertEquals(state2, IN_PROGRESS_STATE);
    }

    @Test(priority = 5, description = "Multiselect Milestones Change State (Different States)")
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



package com.oss.bpm;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.oss.BaseTestCase;
import com.oss.framework.alerts.SystemMessageContainer;
import com.oss.framework.components.inputs.Date;
import com.oss.framework.prompts.ConfirmationBox;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.bpm.*;
import com.oss.utils.TestListener;
import org.apache.logging.log4j.message.Message;
import org.openqa.selenium.chrome.ChromeOptions;
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

    private String BPM_USER_LOGIN = "bpm_webselenium";
    private String BPM_USER_PASSWORD = "bpmweb";
    private String BPM_ADMIN_USER_LOGIN = "bpm_admin_webselenium";
    private String BPM_ADMIN_USER_PASSWORD = "bpmweb";

    private String milestoneName1 = "Milestone Update " + (int) (Math.random() * 10001);
    private String milestoneName2 = "Milestone Update " + (int) (Math.random() * 10001);
    private String milestoneName3 = "Milestone Update " + (int) (Math.random() * 10001);
    private String description = "Milestone Update " + (Math.random() * 1001);
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
        if (startDueDate.equals("")) {
            Assert.assertEquals("", newDueDate);
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
        Assert.assertEquals("All milestones changed state successfully.", message);
    }

    private void changeMilestoneState(String previousState, String nextState, String reason, boolean isReasonMandatory) {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        DelayUtils.sleep(500);

        String startModifyDate = milestoneViewPage.getValuePropertyPanel("modifyDate", 0);
        String startDueDate = milestoneViewPage.getValuePropertyPanel("dueDate", 0);
        String startCompletionDate = milestoneViewPage.getValuePropertyPanel("completionDate", 0);
        //boolean manualCompletion = Boolean.parseBoolean(milestoneViewPage.getValuePropertyPanel("manualCompletion", 0));

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
        Assert.assertEquals("All milestones changed state successfully.", message);
        DelayUtils.sleep(1000);
        String newStateValue = milestoneViewPage.getValuePropertyPanel("state", 0);
        String newModifyDate = milestoneViewPage.getValuePropertyPanel("modifyDate", 0);
        String newDueDate = milestoneViewPage.getValuePropertyPanel("dueDate", 0);
        String newCompletionDate = milestoneViewPage.getValuePropertyPanel("completionDate", 0);
        String approvalDate = LocalDate.now().plusDays(PLUS_DAYS).toString();
        String currentLeadDate = LocalDate.now().plusDays(Long.parseLong(LEAD_TIME)).toString();
        Assert.assertEquals(nextState, newStateValue);
        Assert.assertNotEquals(newModifyDate,startModifyDate);
        switch (nextState) {
            case NOT_NEEDED_STATE:
                Assert.assertEquals("", newCompletionDate);
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
        ProcessInstancesPage.goToProcessInstancesPage(driver, BASIC_URL);
        changeStateMilestoneWizardPage = new ChangeStateMilestoneWizardPage(driver);
        changeStateMilestoneWizardPage.changeUser(BPM_USER_LOGIN, BPM_USER_PASSWORD);
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
//        ProcessWizardPage.MilestoneStepWizard milestoneStep = processWizardPage.definedMilestoneInProcess(processName, 5L,
//                "pr_milestones");
        milestoneStep.addMilestoneRow(milestone1);
        milestoneStep.addMilestoneRow(milestone2);
        milestoneStep.addMilestoneRow(milestone3);
        processWizardPage.clickAcceptButton();
    }

    @Test(priority = 1)
    public void firstMilestoneFlow() {
        /**
         * not needed → new → in progress → not needed
         */
        milestoneViewPage = MilestoneViewPage.goToMilestoneViewPage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        //milestoneViewPage.addColumnAttribute("Manual Completion");
        milestoneViewPage.selectMilestone(milestoneName1);

        changeMilestoneState(NOT_NEEDED_STATE, NEW_STATE, CHANGE_STATE_REASON, true);
        changeMilestoneState(NEW_STATE, IN_PROGRESS_STATE, CHANGE_STATE_REASON, false);
        changeMilestoneState(IN_PROGRESS_STATE, NOT_NEEDED_STATE, CHANGE_STATE_REASON, true);
    }

    @Test(priority = 2)
    public void secondMilestoneFlow() {
        /**
         * new → in progress → completed (not permitted).....{change user}.....in progress → completed
         */
        milestoneViewPage = MilestoneViewPage.goToMilestoneViewPage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        milestoneViewPage.selectMilestone(milestoneName2);

        changeMilestoneState(NEW_STATE, IN_PROGRESS_STATE, CHANGE_STATE_REASON, false);

        //try to change state to Completed
        String startModifyDate = milestoneViewPage.getValuePropertyPanel("modifyDate", 0);
        milestoneViewPage.callAction(CHANGE_STATE_BUTTON);
        changeStateMilestoneWizardPage.setState(COMPLETED_STATE);
        changeStateMilestoneWizardPage.setApprovalDate(PLUS_DAYS);
        changeStateMilestoneWizardPage.accept();
        String message = SystemMessageContainer.create(driver, webDriverWait).getFirstMessage().orElseThrow(() -> new RuntimeException("There is no any System Message")).getText();
        Assert.assertEquals("Not all milestones changed state.\n" +
                "Following milestones cannot be manually completed:" + milestoneName2 + ".", message);
        String newModifyDate = milestoneViewPage.getValuePropertyPanel("modifyDate", 0);
        String newStateValue = milestoneViewPage.getValuePropertyPanel("state", 0);
        Assert.assertEquals(startModifyDate, newModifyDate);
        Assert.assertEquals(IN_PROGRESS_STATE, newStateValue);

        // change user for admin and try again
        changeStateMilestoneWizardPage.changeUser(BPM_ADMIN_USER_LOGIN, BPM_ADMIN_USER_PASSWORD);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        milestoneViewPage.selectMilestone(milestoneName2);
        changeMilestoneState(IN_PROGRESS_STATE, COMPLETED_STATE, CHANGE_STATE_REASON, false);
    }

    @Test(priority = 3)
    public void thirdMilestoneFlow() {
        /**
         * not needed → new → suspended → in progress → completed → in progress → suspended → new → not needed
         */
        milestoneViewPage = MilestoneViewPage.goToMilestoneViewPage(driver, BASIC_URL);
        milestoneViewPage.changeUser(BPM_USER_LOGIN, BPM_USER_PASSWORD);

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

    @Test(priority = 4)
    public void multiChangeState() {
        /**
         * not needed → new → in progress → suspended → in progress
         */

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
        String state1 = milestoneViewPage.getValuePropertyPanel("state", 0);

        milestoneViewPage.clearFilters();
        milestoneViewPage.selectAll();
        milestoneViewPage.selectAll();
        milestoneViewPage.selectMilestone(milestoneName3);
        String state2 = milestoneViewPage.getValuePropertyPanel("state", 0);
        Assert.assertEquals(IN_PROGRESS_STATE,state1);
        Assert.assertEquals(IN_PROGRESS_STATE,state2);
    }

    @Test(priority = 5)
    public void checkMultiChangeStateDifferent() {
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



package com.oss.pages.bpm.milestones;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.inputs.TextField;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;
import org.openqa.selenium.WebDriver;

import java.time.LocalDate;

/**
 * @author Paweł Rother
 */

public class ChangeStateMilestoneWizardPage extends BasePage {
    private static final String CANCEL_BUTTON = "wizard-cancel-button-milestones-state-change_wizard-app";
    private static final String ACCEPT_BUTTON = "wizard-submit-button-milestones-state-change_wizard-app";
    private static final String NEW_STATE_ATTRIBUTE_ID = "milestones-state-change_new-state-component";
    private static final String COMMENT_ATTRIBUTE_ID = "milestones-state-change_comment-component-id";
    private static final String APPROVAL_DATE_ATTRIBUTE_ID = "milestones-state-change_approval-date-component-id";
    private static final String STATE_CHANGE_WIZARD_ID = "milestones-state-change_wizard-app";

    public ChangeStateMilestoneWizardPage(WebDriver driver) {
        super(driver);
    }

    public void cancel() {
        Wizard changeStateWizard = Wizard.createByComponentId(driver, wait, STATE_CHANGE_WIZARD_ID);
        changeStateWizard.clickButtonById(CANCEL_BUTTON);
        changeStateWizard.waitToClose();
    }

    public void accept() {
        Wizard changeStateWizard = Wizard.createByComponentId(driver, wait, STATE_CHANGE_WIZARD_ID);
        changeStateWizard.clickButtonById(ACCEPT_BUTTON);
        changeStateWizard.waitToClose();
    }

    public void changeState(String newState, String reason, long plusDays) {
        setState(newState);
        setReason(reason);
        setApprovalDate(plusDays);
        accept();
    }

    public void changeState(String newState, String reason) {
        setState(newState);
        setReason(reason);
        accept();
    }

    public void setState(String newState) {
        Wizard changeStateWizard = Wizard.createByComponentId(driver, wait, STATE_CHANGE_WIZARD_ID);
        DelayUtils.sleep(2000);
        Input componentNewState = changeStateWizard.getComponent(NEW_STATE_ATTRIBUTE_ID);
        if (!componentNewState.getValue().getStringValue().equals(newState)) {
            componentNewState.setSingleStringValue(newState);
        }
    }

    public boolean isReasonMandatory() {
        Wizard changeStateWizard = Wizard.createByComponentId(driver, wait, STATE_CHANGE_WIZARD_ID);
        DelayUtils.sleep(2000);
        TextField componentComment = (TextField) changeStateWizard.getComponent(COMMENT_ATTRIBUTE_ID);
        return componentComment.isMandatory();
    }

    public void setReason(String reason) {
        Wizard changeStateWizard = Wizard.createByComponentId(driver, wait, STATE_CHANGE_WIZARD_ID);
        DelayUtils.sleep(2000);
        TextField componentComment = (TextField) changeStateWizard.getComponent(COMMENT_ATTRIBUTE_ID);
        componentComment.setSingleStringValue(reason);
    }

    public void setApprovalDate(long plusDays) {
        Wizard changeStateWizard = Wizard.createByComponentId(driver, wait, STATE_CHANGE_WIZARD_ID);
        DelayUtils.sleep(2000);
        Input componentApprovalDate = changeStateWizard.getComponent(APPROVAL_DATE_ATTRIBUTE_ID);
        componentApprovalDate.setSingleStringValue(LocalDate.now().plusDays(plusDays).toString());
    }

}

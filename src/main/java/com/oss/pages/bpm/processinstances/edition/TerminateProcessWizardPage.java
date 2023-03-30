package com.oss.pages.bpm.processinstances.edition;

import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

public class TerminateProcessWizardPage extends BasePage {
    private static final String WIZARD_ID = "bpm_processes_view_kill_process-popup_prompt-card";
    private static final String REASON_FIELD_ID = "reasonField";
    private static final String INCLUDING_CHILD_PROCESSES_CHECKBOX_ID = "includeChildCheckbox";
    private final Wizard wizard;

    public TerminateProcessWizardPage(WebDriver driver) {
        super(driver);
        wizard = Wizard.createByComponentId(driver, wait, WIZARD_ID);
    }

    @Step("Setting termination reason")
    public TerminateProcessWizardPage setTerminationReason(String reason) {
        wizard.setComponentValue(REASON_FIELD_ID, reason);
        return this;
    }

    @Step("Enabling child processes termination")
    public TerminateProcessWizardPage enableChildProcessesTerminate() {
        wizard.setComponentValue(INCLUDING_CHILD_PROCESSES_CHECKBOX_ID, "true");
        return this;
    }

    @Step("Clicking accept button")
    public void clickAccept() {
        wizard.clickAccept();
        wizard.waitToClose();
    }

    @Step("Clicking cancel button")
    public void clickCancel() {
        wizard.clickCancel();
        wizard.waitToClose();
    }
}
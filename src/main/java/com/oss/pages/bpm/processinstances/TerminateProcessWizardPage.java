package com.oss.pages.bpm.processinstances;

import org.openqa.selenium.WebDriver;

import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class TerminateProcessWizardPage extends BasePage {
    private static final String WIZARD_ID = "bpm_processes_view_kill_process-popup_prompt-card";
    private static final String REASON_FIELD_ID = "reasonField";
    final Wizard wizard;

    public TerminateProcessWizardPage(WebDriver driver) {
        super(driver);
        wizard = Wizard.createByComponentId(driver, wait, WIZARD_ID);
    }

    @Step("Setting termination reason")
    public void setTerminationReason(final String reason) {
        wizard.setComponentValue(REASON_FIELD_ID, reason);
    }

    @Step("Clicking accept button")
    public void clickAccept() {
        wizard.clickAccept();
    }

    @Step("Clicking cancel button")
    public void clickCancel() {
        wizard.clickCancel();
    }
}
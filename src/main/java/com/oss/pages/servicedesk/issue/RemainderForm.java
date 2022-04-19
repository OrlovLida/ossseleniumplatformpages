package com.oss.pages.servicedesk.issue;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.servicedesk.BaseSDPage;

import io.qameta.allure.Step;

public class RemainderForm extends BaseSDPage {

    private static final Logger log = LoggerFactory.getLogger(RemainderForm.class);
    private static final String WIZARD_ID = "reminder-prompt-id";
    private static final String REMAINDER_DESCRIPTION_ID = "reminderDescription";
    private final Wizard remainderWizard;

    public RemainderForm(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        remainderWizard = Wizard.createByComponentId(driver, wait, WIZARD_ID);
    }

    @Step("Fill Reminder Wizard")
    public void createReminderWithNote(String noteText) {
        fillNote(noteText);
        clickAccept();
    }

    @Step("Fill description note in Remainder wizard")
    private void fillNote(String noteText) {
        DelayUtils.waitForPageToLoad(driver, wait);
        remainderWizard.setComponentValue(REMAINDER_DESCRIPTION_ID, noteText, Input.ComponentType.TEXT_AREA);
        log.info("Filling description note in Remainder wizard");
    }

    @Step("Click Accept in Reminder wizard")
    private void clickAccept() {
        DelayUtils.waitForPageToLoad(driver, wait);
        remainderWizard.clickAccept();
        log.info("Clicking Accept");
    }
}

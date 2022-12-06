package com.oss.pages.iaa.servicedesk.issue.wizard;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.contextactions.ButtonContainer;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.wizard.Wizard;

import io.qameta.allure.Step;

public class ChangeAttributeWizardPage extends SDWizardPage {

    private static final Logger log = LoggerFactory.getLogger(ChangeAttributeWizardPage.class);
    private static final String SAVE_LABEL = "Save";
    private static final String REASON_NAME_ID = "_changeCommentInput";
    private final Wizard changeAttributeWizard;

    public ChangeAttributeWizardPage(WebDriver driver, WebDriverWait wait, String wizardId) {
        super(driver, wait, wizardId);

        changeAttributeWizard = Wizard.createByComponentId(driver, wait, wizardId);
    }

    @Step("Fill Reason for Change")
    public void fillReasonforChange(String reasonForChange) {
        DelayUtils.waitForPageToLoad(driver, wait);
        changeAttributeWizard.setComponentValue(REASON_NAME_ID, reasonForChange, Input.ComponentType.TEXT_FIELD);
        log.info("Filling Reason for Change");
    }

    @Step("Click Save")
    public void clickSave() {
        DelayUtils.waitForPageToLoad(driver, wait);
        ButtonContainer.create(driver, wait).callActionByLabel(SAVE_LABEL);
        log.info("Clicking Save button in the wizard");

    }

}

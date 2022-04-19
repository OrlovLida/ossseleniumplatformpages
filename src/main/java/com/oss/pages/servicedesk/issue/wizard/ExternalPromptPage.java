package com.oss.pages.servicedesk.issue.wizard;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.contextactions.ButtonContainer;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.wizard.Wizard;

import io.qameta.allure.Step;

public class ExternalPromptPage extends SDWizardPage {

    private static final Logger log = LoggerFactory.getLogger(ExternalPromptPage.class);

    private static final String EXTERNAL_NAME_ID = "_name";
    private static final String CREATE_EXTERNAL_LABEL = "Create External";
    private static final String SAVE_LABEL = "Save";

    private final Wizard externalPrompt;

    public ExternalPromptPage(WebDriver driver, WebDriverWait wait, String wizardId) {
        super(driver, wait);
        externalPrompt = Wizard.createByComponentId(driver, wait, wizardId);
    }

    @Step("Fill External Name")
    public void fillExternalName(String externalName) {
        externalPrompt.setComponentValue(EXTERNAL_NAME_ID, externalName, Input.ComponentType.TEXT_FIELD);
    }

    @Step("Click Create External button in wizard")
    public void clickCreateExternal() {
        DelayUtils.waitForPageToLoad(driver, wait);
        ButtonContainer.create(driver, wait).callActionByLabel(CREATE_EXTERNAL_LABEL);
        log.info("Clicking Create External button in the wizard");
    }

    @Step("Click Save")
    public void clickSave() {
        DelayUtils.waitForPageToLoad(driver, wait);
        ButtonContainer.create(driver, wait).callActionByLabel(SAVE_LABEL);
        log.info("Clicking Save button in the wizard");
    }
}

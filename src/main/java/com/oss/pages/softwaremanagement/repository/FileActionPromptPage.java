package com.oss.pages.softwaremanagement.repository;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

/**
 * Created by Bartłomiej Jędrzejczyk on 2022-04-19
 */
public class FileActionPromptPage extends BasePage {

    private static final String FILE_ACTION_PROMPT_ID = "smComponent_SoftwareRepositoryViewIdFileActionPromptId_prompt-card";
    private static final String NAME_FIELD_ID = "smComponent_SoftwareRepositoryViewIdFileNameTextFieldId";
    private static final String SUBMIT_BUTTON_ID = "smComponent_SoftwareRepositoryViewIdFileActionButtonsId-1";

    private final Wizard fileActionWizard;

    public FileActionPromptPage(WebDriver driver) {
        super(driver);
        fileActionWizard = Wizard.createByComponentId(driver, wait, FILE_ACTION_PROMPT_ID);
    }

    @Step("Set name: {name}")
    public void setName(String name) {
        fileActionWizard.setComponentValue(NAME_FIELD_ID, name, Input.ComponentType.TEXT_FIELD);
    }

    @Step("Click submit button")
    public void clickSubmitButton() {
        fileActionWizard.clickButtonById(SUBMIT_BUTTON_ID);
    }
}

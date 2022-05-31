package com.oss.pages.physical;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class ChangeCardModelWizard extends BasePage {

    private static final String WIZARD_ID = "optional_prompt-card";
    private static final String SUBMIT_ID = "wizard-submit-button-changeCardModelWizard";
    private static final String MODEL_SEARCH = "model";

    private Wizard wizard = Wizard.createByComponentId(driver, wait, WIZARD_ID);

    public ChangeCardModelWizard(WebDriver driver) {
        super(driver);
    }

    @Step("Set Model")
    public void setModelCard(String model) {
        wizard.setComponentValue(MODEL_SEARCH, model, Input.ComponentType.SEARCH_FIELD);
    }

    @Step("Click Update button")
    public void clickSubmit() {
        wizard.clickButtonById(SUBMIT_ID);
    }
}

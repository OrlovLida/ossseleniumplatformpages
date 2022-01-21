package com.oss.pages.physical;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class ChangeCardModelWizard extends BasePage {

    private static final String WIZARD_ID = "card_change_model_wizard_view";

    private Wizard wizard = Wizard.createByComponentId(driver, wait, WIZARD_ID);
    private static final String MODEL_SEARCH = "search_box_card_change_model";

    public ChangeCardModelWizard(WebDriver driver) {
        super(driver);
    }

    @Step("Set Model")
    public void setModelCard(String model) {
        wizard.setComponentValue(MODEL_SEARCH, model, Input.ComponentType.SEARCH_FIELD);
    }

    @Step("Click Update button")
    public void clickUpdate() {
        wizard.clickUpdate();
    }
}

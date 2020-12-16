package com.oss.pages.physical;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class ChangeModelWizardPage extends BasePage {

    private Wizard wizard = Wizard.createByComponentId(driver, wait, "Popup");
    private static final String MODEL_SEARCH = "search_model";

    public ChangeModelWizardPage(WebDriver driver) {
        super(driver);
    }

    @Step("Set Model")
    public void setModel(String model) {
        wizard.setComponentValue(MODEL_SEARCH, model, Input.ComponentType.SEARCH_FIELD);
    }

    @Step("Click Update button")
    public void clickUpdate() {
        wizard.clickUpdate();
    }
}

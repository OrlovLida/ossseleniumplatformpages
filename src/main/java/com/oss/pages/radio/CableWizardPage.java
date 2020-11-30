package com.oss.pages.radio;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

import static com.oss.framework.components.inputs.Input.ComponentType.SEARCH_FIELD;

public class CableWizardPage extends BasePage {

    public CableWizardPage(WebDriver driver) {
        super(driver);
    }

    private Wizard wizard = Wizard.createByComponentId(driver, wait, "Popup");

    @Step("Set model")
    public void setModel(String model) {
        Input input = wizard.getComponent("model", SEARCH_FIELD);
        input.setSingleStringValue(model);
    }
}
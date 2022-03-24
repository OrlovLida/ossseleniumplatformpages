package com.oss.pages.radio;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

import static com.oss.framework.components.inputs.Input.ComponentType.SEARCH_FIELD;
import static com.oss.framework.components.inputs.Input.ComponentType.TEXT_FIELD;

public class CableWizardPage extends BasePage {

    private static final String WIZARD_ID = "Popup";
    private Wizard wizard = Wizard.createByComponentId(driver, wait, WIZARD_ID);

    public CableWizardPage(WebDriver driver) {
        super(driver);
    }

    @Step("Set model")
    public void setModel(String model) {
        Input input = wizard.getComponent("model", SEARCH_FIELD);
        input.setSingleStringValue(model);
    }

    @Step("Set name")
    public void setName(String name) {
        Input input = wizard.getComponent("name", TEXT_FIELD);
        input.setSingleStringValue(name);
    }

    @Step("Set start device")
    public void setStartDevice(String device) {
        Input input = wizard.getComponent("startDeviceTermination", SEARCH_FIELD);
        input.setSingleStringValue(device);
    }

    @Step("Set end device")
    public void setEndDevice(String device) {
        Input input = wizard.getComponent("endDeviceTermination", SEARCH_FIELD);
        input.setSingleStringValue(device);
    }

    @Step("Set end location")
    public void setEndLocation(String device) {
        Input input = wizard.getComponent("endTermination", SEARCH_FIELD);
        input.setSingleStringValue(device);
    }

    @Step("Click accept")
    public void accept() {
        wizard.clickAccept();
    }
}
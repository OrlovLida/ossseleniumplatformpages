package com.oss.pages.radio;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

import static com.oss.framework.components.inputs.Input.ComponentType.COMBOBOX;
import static com.oss.framework.components.inputs.Input.ComponentType.TEXT_FIELD;

public class RanAntennaWizardPage extends BasePage {
    public RanAntennaWizardPage(WebDriver driver) {
        super(driver);
    }

    private Wizard ranAntennaWizard = Wizard.createByComponentId(driver, wait, "antenna-wizard");

    @Step("Set model")
    public void setModel(String model) {
        Input input = ranAntennaWizard.getComponent("model-input", COMBOBOX);
        input.setSingleStringValue(model);
    }
    @Step("Set name")
    public void setName(String name) {
        Input input = ranAntennaWizard.getComponent("name", TEXT_FIELD);
        input.setSingleStringValue(name);
    }

    @Step("Set precise location")
    public void setPreciseLocation(String location) {
        Input input = ranAntennaWizard.getComponent("preciseLocation-input", COMBOBOX);
        input.setSingleStringValue(location);
    }

    @Step("Click Accept button")
    public void clickAccept() {
        ranAntennaWizard.clickAccept();
    }
}

package com.oss.pages.physical;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.data.Data;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

import static com.oss.framework.components.inputs.Input.ComponentType.*;

public class CreatePluggableModuleWizardPage extends BasePage {
    private static final String PLUGGABLE_MODULE_WIZARD_DATA_ATTRIBUTE_NAME = "pluggable-module-wizard-create";
    private static final String PLUGGABLE_MODULE_MODEL_DATA_ATTRIBUTE_NAME = "search_model";
    private static final String PLUGGABLE_MODULE_ACCEPT_BUTTON_DATA_NAME = "wizard-submit-button-pluggable-module-wizard-create";
    private static final String PORT_COMBOBOX_DATA_ATTRIBUTE_NAME = "search_port";

    public CreatePluggableModuleWizardPage(WebDriver driver) {
        super(driver);
    }

    @Step("Set Model")
    public void setModel(String model) {
        getPluggableModuleWizard().setComponentValue(PLUGGABLE_MODULE_MODEL_DATA_ATTRIBUTE_NAME, model, SEARCH_FIELD);
    }

    @Step("Set port")
    public void setPort(String portName) {
        Input devicesOnLocation = getPluggableModuleWizard().getComponent(PORT_COMBOBOX_DATA_ATTRIBUTE_NAME, COMBOBOXV2);
        DelayUtils.waitForPageToLoad(driver, wait);
        devicesOnLocation.setValueContains(Data.createSingleData(portName));
    }

    @Step("Click Accept button")
    public void accept() {
        getPluggableModuleWizard().clickActionById(PLUGGABLE_MODULE_ACCEPT_BUTTON_DATA_NAME);
    }

    private Wizard getPluggableModuleWizard() {
        return Wizard.createByComponentId(driver, wait, PLUGGABLE_MODULE_WIZARD_DATA_ATTRIBUTE_NAME);
    }
}

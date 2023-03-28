package com.oss.pages.physical;

import java.util.List;
import java.util.Set;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.data.Data;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.inputs.SearchField;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

import static com.oss.framework.components.inputs.Input.ComponentType.MULTI_COMBOBOX;
import static com.oss.framework.components.inputs.Input.ComponentType.SEARCH_FIELD;

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
        Input devicesOnLocation = getPluggableModuleWizard().getComponent(PORT_COMBOBOX_DATA_ATTRIBUTE_NAME, MULTI_COMBOBOX);
        DelayUtils.waitForPageToLoad(driver, wait);
        devicesOnLocation.setValueContains(Data.createSingleData(portName));
    }

    @Step("Click Accept button")
    public void accept() {
        Wizard pluggableModuleWizard = getPluggableModuleWizard();
        pluggableModuleWizard.clickButtonById(PLUGGABLE_MODULE_ACCEPT_BUTTON_DATA_NAME);
        pluggableModuleWizard.waitToClose();
    }

    @Step("Get wizard name")
    public String getWizardName() {
        return getPluggableModuleWizard().getWizardName();
    }

    @Step("Get port")
    public List<String> getPorts() {
        return getPluggableModuleWizard().getComponent(PORT_COMBOBOX_DATA_ATTRIBUTE_NAME).getStringValues();
    }

    public Set<String> getAvailableModels() {
        SearchField model = (SearchField) getPluggableModuleWizard().getComponent(PLUGGABLE_MODULE_MODEL_DATA_ATTRIBUTE_NAME);
        return model.getOptions();
    }

    public Input.MouseCursor getPortFieldCursor() {
        return getPluggableModuleWizard().getComponent(PORT_COMBOBOX_DATA_ATTRIBUTE_NAME).cursor();
    }

    private Wizard getPluggableModuleWizard() {
        return Wizard.createByComponentId(driver, wait, PLUGGABLE_MODULE_WIZARD_DATA_ATTRIBUTE_NAME);
    }
}

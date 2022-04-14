package com.oss.pages.radio;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class GNodeBDUWizardPage extends BasePage {

    private static final String WIZARD_DATA_ATTRIBUTE_NAME = "gnodeb-du-wizard-id";
    private static final String NAME_DATA_ATTRIBUTE_NAME = "name";
    private static final String ID_DATA_ATTRIBUTE_NAME = "gNodeBId";
    private static final String MODEL_DATA_ATTRIBUTE_NAME = "gNodeBModel";
    private static final String CONTROLLER_DATA_ATTRIBUTE_NAME = "controller";

    public GNodeBDUWizardPage(WebDriver driver) {
        super(driver);
    }

    @Step("Click Accept button")
    public void accept() {
        getWizard().clickAccept();
    }

    @Step("Create gNodeB DU with mandatory fields (Name, gNodeB ID, gNodeB Model) filled in")
    public void createGNodeBDU(String name, String id, String model, String controller) {
        setName(name);
        setENodeBId(id);
        setENodeBModel(model);
        setController(controller);
        DelayUtils.waitForPageToLoad(driver, wait);
        accept();
    }

    @Step("Set name")
    public void setName(String name) {
        getWizard().setComponentValue(NAME_DATA_ATTRIBUTE_NAME, name, Input.ComponentType.TEXT_FIELD);
    }

    @Step("Set controller")
    public void setController(String controller) {
        getWizard().setComponentValue(CONTROLLER_DATA_ATTRIBUTE_NAME, controller, Input.ComponentType.SEARCH_FIELD);
    }

    @Step("Set gNodeB Id")
    public void setENodeBId(String id) {
        getWizard().setComponentValue(ID_DATA_ATTRIBUTE_NAME, id, Input.ComponentType.TEXT_FIELD);
    }

    @Step("Set gNodeB Model")
    public void setENodeBModel(String model) {
        getWizard().setComponentValue(MODEL_DATA_ATTRIBUTE_NAME, model, Input.ComponentType.COMBOBOX);
    }

    private Wizard getWizard() {
        return Wizard.createByComponentId(driver, wait, WIZARD_DATA_ATTRIBUTE_NAME);
    }

}
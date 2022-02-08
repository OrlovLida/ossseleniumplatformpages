package com.oss.pages.gisview;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.inputs.Button;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

import static com.oss.framework.components.inputs.Input.ComponentType.COMBOBOX;
import static com.oss.framework.components.inputs.Input.ComponentType.SEARCH_FIELD;
import static com.oss.framework.components.inputs.Input.ComponentType.TEXT_FIELD;

public class CreateDuctWizardPage extends BasePage {
    private static final String NAME_ID = "Name_uid";
    private static final String MODEL_ID = "DuctModel_uid";
    private static final String DUCT_TYPE_ID = "DuctType_uid";
    private static final String CREATE_BUTTON_ID = "wizard-submit-button-ductWizardId";
    private static final String WIZARD_ID = "ductWizardId";

    public CreateDuctWizardPage(WebDriver driver) {
        super(driver);
    }

    public void setName(String name) {
        getCreateDuctWizard().setComponentValue(NAME_ID, name, TEXT_FIELD);
    }

    public void setModel(String model) {
        getCreateDuctWizard().setComponentValue(MODEL_ID, model, SEARCH_FIELD);
    }

    public void setType(String type) {
        getCreateDuctWizard().setComponentValue(DUCT_TYPE_ID, type, COMBOBOX);
    }

    public void create() {
       getCreateDuctWizard().clickButtonById(CREATE_BUTTON_ID);
    }

    private Wizard getCreateDuctWizard() {
        return Wizard.createByComponentId(driver, wait, WIZARD_ID);
    }
}

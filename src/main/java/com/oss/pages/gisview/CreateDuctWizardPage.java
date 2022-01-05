package com.oss.pages.gisview;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.inputs.Button;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;

import static com.oss.framework.components.inputs.Input.ComponentType.COMBOBOX;
import static com.oss.framework.components.inputs.Input.ComponentType.SEARCH_FIELD;
import static com.oss.framework.components.inputs.Input.ComponentType.TEXT_FIELD;

public class CreateDuctWizardPage extends BasePage {
    private static final String NAME_ID = "Name_uid";
    private static final String MODEL_ID = "DuctModel_uid";
    private static final String DUCT_TYPE_ID = "DuctType_uid";
    private static final String CREATE_BUTTON_ID = "PromptButtonId-0";
    private static final String WIZARD_ID = "promptId";

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
        Button create = Button.createBySelectorAndId(driver, "a", CREATE_BUTTON_ID);
        create.click();
    }

    private Wizard getCreateDuctWizard() {
        return Wizard.createByComponentId(driver, wait, WIZARD_ID);
    }
}

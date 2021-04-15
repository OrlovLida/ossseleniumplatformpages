package com.oss.pages.gisView;

import com.oss.framework.components.inputs.Button;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import org.openqa.selenium.WebDriver;

import static com.oss.framework.components.inputs.Input.ComponentType.*;

public class CreateDuctWizardPage extends BasePage {
    private static final String NAME_DATA_ATTRIBUTE_NAME = "Name_uid";
    private static final String MODEL_DATA_ATTRIBUTE_NAME = "DuctModel_uid";
    private static final String DUCT_TYPE_DATA_ATTRIBUTE_NAME = "DuctType_uid";
    private static final String CREATE_BUTTON_DATA_ATTRIBUTE_NAME = "PromptButtonId-0";

    public CreateDuctWizardPage(WebDriver driver) {
        super(driver);
    }

    public void setName(String name) {
        getCreateDuctWizard().setComponentValue(NAME_DATA_ATTRIBUTE_NAME, name, TEXT_FIELD);
    }

    public void setModel(String model) {
        getCreateDuctWizard().setComponentValue(MODEL_DATA_ATTRIBUTE_NAME, model, SEARCH_FIELD);
    }

    public void setType(String type) {
        getCreateDuctWizard().setComponentValue(DUCT_TYPE_DATA_ATTRIBUTE_NAME, type, COMBOBOX);

    }

    public void create() {
        Button create = Button.createBySelectorAndId(driver, "a", CREATE_BUTTON_DATA_ATTRIBUTE_NAME);
        create.click();
    }

    private Wizard getCreateDuctWizard() {
        return Wizard.createPopupWizard(driver, wait);
    }
}

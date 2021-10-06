package com.oss.pages.faultmanagement;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

public class FMCrateWizardPage extends BasePage {

    private static final String NAME_TEXT_FIELD_ID = "FilterFolderNameInput";
    private static final String DESCRIPTION_TEXT_FIELD_ID = "FilterFolderDescriptionInput";
    private static final String WIZARD_ID = "webFilter_wizard_modal_template";
    private static final String TYPE_FIELD_ID = "FilterTypeInput";
    private static final String CONDITION_ID = "condition";


    public FMCrateWizardPage(WebDriver driver) {
        super(driver);
    }

    private Wizard folderWizard = Wizard.createByComponentId(driver, wait, WIZARD_ID);

    @Step("I set Name of the wizard")
    public void setName(String name){
        folderWizard.getComponent(NAME_TEXT_FIELD_ID, Input.ComponentType.TEXT_FIELD).setSingleStringValue(name);
    }

    @Step("I set description of the wizard")
    public void setDescription(String description){
        folderWizard.getComponent(DESCRIPTION_TEXT_FIELD_ID, Input.ComponentType.TEXT_FIELD).setSingleStringValue(description);
    }

    @Step("Type Name of the folder")
    public void setTypeValue(String type){
        folderWizard.setComponentValue(TYPE_FIELD_ID, type, Input.ComponentType.COMBOBOX);;
    }

    @Step("Click on Accept and close the wizard")
    public void clickAccept(){
        folderWizard.clickAccept();
    }

    public void clickOnLabel(String label) {
        folderWizard.clickButtonByLabel(label);
    }

    public void clickOnConditon(String label) {
        folderWizard.clickButtonByLabel(label, CONDITION_ID);
    }

}

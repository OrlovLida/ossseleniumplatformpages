package com.oss.pages.filtermanager;

import org.openqa.selenium.WebDriver;

import com.oss.framework.widgets.Wizard;

import io.qameta.allure.Step;

import static com.oss.framework.components.inputs.Input.ComponentType.TEXT_FIELD;

public class CreateFolderWizard extends FilterManagerPage {

    private static final String NAME_TEXT_FIELD_ID = "filterManager_wizard_def_name";
    private static final String WIZARD_ID = "filterManager_wizard_folder_widget";
    private static final String ACCEPT_BUTTON_ID = "wizard-submit-button-filterManager_wizard_folder_widget";
    private Wizard folderWizard = Wizard.createByComponentId(driver, wait, WIZARD_ID);

    public CreateFolderWizard(WebDriver driver) {
        super(driver);
    }

    @Step("Type Name of the folder")
    public CreateFolderWizard typeNameOfTheFolder(String name) {
        folderWizard.getComponent(NAME_TEXT_FIELD_ID, TEXT_FIELD).setSingleStringValue(name);
        return this;
    }

    @Step("Click on Accept and close the wizard")
    public FilterManagerPage clickAccept() {
        folderWizard.clickButtonById(ACCEPT_BUTTON_ID);
        return new FilterManagerPage(driver);
    }

}

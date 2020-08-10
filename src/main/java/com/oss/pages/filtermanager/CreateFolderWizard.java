package com.oss.pages.filtermanager;

import com.oss.framework.components.TextField;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

import static com.oss.framework.components.Input.ComponentType.TEXT_FIELD;

public class CreateFolderWizard extends BasePage {

    public CreateFolderWizard(WebDriver driver){
        super(driver);
    }

    public String NAME_TEXT_FIELD_ID= "filterManager_wizard_def_name";
    public String WIZARD_ID = "filter-manager-folder-wizard-view";
    public String ACCEPT_BUTTON_ID = "wizard-submit-button-filterManager_wizard_folder_widget";

    private Wizard folderWizard = Wizard.createByComponentId(driver, wait, WIZARD_ID);

    @Step("Type Name of the folder")
    public CreateFolderWizard typeNameOfTheFolder(String name){
        folderWizard.getComponent(NAME_TEXT_FIELD_ID, TEXT_FIELD).setSingleStringValue(name);
        return this;
    }

    @Step("Click on Accept and close the wizard")
    public FilterManagerPage clickAccept(){
        folderWizard.clickActionById(ACCEPT_BUTTON_ID);
        return new FilterManagerPage(driver);
    }



}

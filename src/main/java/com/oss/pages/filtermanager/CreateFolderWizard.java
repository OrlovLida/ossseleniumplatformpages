package com.oss.pages.filtermanager;

import com.oss.framework.components.TextField;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

public class CreateFolderWizard extends BasePage {

    public CreateFolderWizard(WebDriver driver){
        super(driver);
    }



    public String NAME_TEXT_FIELD_ID= "filterManager_wizard_def_name";
 //   TextField textField = new TextField(driver, wait, NAME_TEXT_FIELD_ID);


    @Step("Type Name of the folder")
    public CreateFolderWizard typeNameOfTheFolder(String name){
     //   TextField textField =TextField.create(driver, wait, NAME_TEXT_FIELD_ID);
        return this;
    }

}

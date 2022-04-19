package com.oss.pages.templatecm.templatemanager;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

/**
 * Created by Bartłomiej Jędrzejczyk on 2022-04-15
 */
public class CreateEditTemplateFolderPromptPage extends BasePage {

    private static final String EDIT_TEMPLATE_FOLDER_WINDOW_ID = "editTemplateFolderWindow";
    private static final String FOLDER_NAME_ID = "folderName";
    private static final String DESCRIPTION_ID = "description";
    private static final String SAVE_BUTTON_ID = "undefined-1";

    private final Wizard createEditTemplateFolderWizard;

    public CreateEditTemplateFolderPromptPage(WebDriver driver) {
        super(driver);
        createEditTemplateFolderWizard = Wizard.createByComponentId(driver, wait, EDIT_TEMPLATE_FOLDER_WINDOW_ID);
    }

    @Step("Set folder name: {folderName}")
    public void setFolderName(String folderName) {
        createEditTemplateFolderWizard.setComponentValue(FOLDER_NAME_ID, folderName, Input.ComponentType.TEXT_FIELD);
    }

    @Step("Set folder description: {folderDescription}")
    public void setFolderDescription(String folderDescription) {
        createEditTemplateFolderWizard.setComponentValue(DESCRIPTION_ID, folderDescription, Input.ComponentType.TEXT_FIELD);
    }

    @Step("Click save button")
    public void clickSaveButton() {
        createEditTemplateFolderWizard.clickButtonById(SAVE_BUTTON_ID);
    }
}

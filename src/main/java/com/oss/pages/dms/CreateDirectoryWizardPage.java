package com.oss.pages.dms;

import org.openqa.selenium.WebDriver;

import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class CreateDirectoryWizardPage extends BasePage {

    private static final String ACCEPT_ID = "wizard-submit-button-addFolderComponentId";
    private static final String WIZARD_ID = "addFolderViewId_prompt-card";
    private static final String NAME_FIELD_ID = "nameField";
    public static final String TAG_FOLDER_FIELD_ID = "tagInputForm";

    public CreateDirectoryWizardPage(WebDriver driver) {
        super(driver);
    }

    @Step("Set directory name = {name}")
    public void setDirectoryName(String name) {
        getCreateDirectoryWizard().setComponentValue(NAME_FIELD_ID, name);
    }

    @Step("Set tag folder = {tagFolder}")
    public void setTagFolder(String tagFolder) {
        getCreateDirectoryWizard().getComponent(TAG_FOLDER_FIELD_ID).setSingleStringValueContains(tagFolder);
    }

    @Step("Click Accept")
    public void clickAccept() {
        getCreateDirectoryWizard().clickButtonById(ACCEPT_ID);
    }

    private Wizard getCreateDirectoryWizard() {
        return Wizard.createByComponentId(driver, wait, WIZARD_ID);
    }
}

package com.oss.pages.dms;

import org.openqa.selenium.WebDriver;

import com.oss.framework.wizard.Wizard;

import io.qameta.allure.Step;

public class UpdateDirectoryWizardPage extends CreateDirectoryWizardPage {

    private static final String ACCEPT_ID = "wizard-submit-button-editFolderWidgetId";
    private static final String WIZARD_ID = "editFolderViewId_prompt-card";
    private static final String UPDATE_NAME_FIELD_ID = "newFileName";

    public UpdateDirectoryWizardPage(WebDriver driver) {
        super(driver);
    }

    @Step("Set directory name = {name}")
    @Override
    public void setDirectoryName(String name) {
        getWizard().setComponentValue(UPDATE_NAME_FIELD_ID, name);
    }

    @Override
    @Step("Set tag folder = {tagFolder}")
    public void setTagFolder(String tagFolder) {
        getWizard().setComponentValue(TAG_FOLDER_FIELD_ID, tagFolder);
    }

    @Step("Click Accept")
    @Override
    public void clickAccept() {
        getWizard().clickButtonById(ACCEPT_ID);
    }

    private Wizard getWizard() {
        return Wizard.createByComponentId(driver, wait, WIZARD_ID);
    }
}

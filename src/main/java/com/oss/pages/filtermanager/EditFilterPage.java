package com.oss.pages.filtermanager;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.list.DraggableList;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.utils.DragAndDrop;
import com.oss.framework.wizard.Wizard;

import io.qameta.allure.Step;

import static com.oss.framework.components.inputs.Input.ComponentType.TEXT_FIELD;

public class EditFilterPage extends FilterManagerPage {

    private static final String ACCEPT_BUTTON_ID = "wizard-submit-button-webFilter_wizard_filter_widget";
    private static final String WIZARD_ID = "webFilter_wizard_filter_widget";
    private static final String NAME_TEXT_FIELD_ID = "filterManager_wizard_def_name";
    private static final String DESCRIPTION_TEXT_FIELD_ID = "filterManager_wizard_def_desc";
    private final Wizard filterWizard = Wizard.createByComponentId(driver, wait, WIZARD_ID);

    public EditFilterPage(WebDriver driver) {
        super(driver);
    }

    @Step("Change Folder for filer using drag and drop")
    public EditFilterPage changeFolderForFilter(String folderName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        DraggableList availableFolders = DraggableList.create(driver, wait, "Available folders");
        DraggableList enabledFolders = DraggableList.create(driver, wait, "Enabled folders");
        DragAndDrop.DraggableElement source = availableFolders.getDraggableElement(folderName);
        enabledFolders.drop(source);
        return this;
    }

    @Step("Change Name of filter")
    public EditFilterPage changeName(String folderName) {
        filterWizard.getComponent(NAME_TEXT_FIELD_ID, TEXT_FIELD).setSingleStringValue(folderName);
        return this;
    }

    @Step("Change Description of filter")
    public EditFilterPage changeDescription(String folderDescription) {
        filterWizard.getComponent(DESCRIPTION_TEXT_FIELD_ID, TEXT_FIELD).setSingleStringValue(folderDescription);
        return this;
    }

    @Step("Click on Accept and close the wizard")
    public FilterManagerPage clickAccept() {
        DelayUtils.waitForPageToLoad(driver, wait);
        filterWizard.clickButtonById(ACCEPT_BUTTON_ID);
        return new FilterManagerPage(driver);
    }
}

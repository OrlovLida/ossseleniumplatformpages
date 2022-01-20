package com.oss.pages.faultmanagement.filtermanager;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.list.DraggableList;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

/**
 * @author Bartosz Nowak
 */
public class FMCreateWizardPage extends BasePage {
    private static final Logger log = LoggerFactory.getLogger(FMCreateWizardPage.class);
    private static final String NAME_TEXT_FIELD_ID = "FilterFolderNameInput";
    private static final String DESCRIPTION_TEXT_FIELD_ID = "FilterFolderDescriptionInput";
    private static final String WIZARD_ID = "webFilter_wizard_modal_template";
    private static final String TYPE_FIELD_ID = "FilterTypeInput";
    private static final String CONDITION_ID = "condition";
    private static final String DROPDOWNLIST_AVAILABLE = "Available Filters";
    private static final String DROPDOWNLIST_ENABLED = "Enabled Filters";
    private final DraggableList draggableListAvailable = DraggableList.create(driver, wait, DROPDOWNLIST_AVAILABLE);
    private final DraggableList draggableListEnabled = DraggableList.create(driver, wait, DROPDOWNLIST_ENABLED);
    private final Wizard folderWizard = Wizard.createByComponentId(driver, wait, WIZARD_ID);
    public FMCreateWizardPage(WebDriver driver) {
        super(driver);
    }

    @Step("I set Name of the wizard")
    public FMCreateWizardPage setName(String name) {
        folderWizard.getComponent(NAME_TEXT_FIELD_ID, Input.ComponentType.TEXT_FIELD).setSingleStringValue(name);
        log.info("Setting name: {}", name);
        return this;
    }

    @Step("I set description of the wizard")
    public FMCreateWizardPage setDescription(String description) {
        folderWizard.getComponent(DESCRIPTION_TEXT_FIELD_ID, Input.ComponentType.TEXT_FIELD).setSingleStringValue(description);
        log.info("Setting description: {}", description);
        return this;
    }

    @Step("Type Name of the folder")
    public FMCreateWizardPage setTypeValue(String type) {
        folderWizard.setComponentValue(TYPE_FIELD_ID, type, Input.ComponentType.COMBOBOX);
        log.info("Setting type value to: {}", type);
        return this;
    }

    @Step("Click on Accept and close the wizard")
    public FMFilterManagerPage clickAccept() {
        folderWizard.clickAccept();
        log.info("Clicking Accept");
        return new FMFilterManagerPage(driver);
    }

    public void clickOnLabel(String label) {
        folderWizard.clickButtonByLabel(label);
        log.info("Clicking on label: {}", label);
    }

    public void clickOnAddConditon() {
        folderWizard.clickButtonById("Add", CONDITION_ID);
        log.info("Clicking on Add condition");
    }

    @Step("I drag and drop filter by name")
    public void dragAndDropFilterByName(String filterName) {
        draggableListEnabled.drop(draggableListAvailable.getDraggableElement(filterName));
        log.info("Drag filter {} and drop it", filterName);
    }

}

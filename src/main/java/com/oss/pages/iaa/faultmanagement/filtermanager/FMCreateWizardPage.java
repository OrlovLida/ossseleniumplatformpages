package com.oss.pages.iaa.faultmanagement.filtermanager;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.expressioneditor.ExpressionEditor;
import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.list.DraggableList;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.wizard.Wizard;
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
    private static final String DROPDOWNLIST_AVAILABLE_FOLDERS_ID = "Available Folders";
    private static final String DROPDOWNLIST_ENABLED_FOLDERS_ID = "Enabled Folders";
    private static final String DROPDOWNLIST_AVAILABLE_FILTERS_ID = "Available Filters";
    private static final String DROPDOWNLIST_ENABLED_FILTERS_ID = "Enabled Filters";
    private final Wizard folderWizard;

    public FMCreateWizardPage(WebDriver driver) {
        super(driver);
        folderWizard = Wizard.createByComponentId(driver, wait, WIZARD_ID);

    }

    @Step("I set Name of the wizard")
    public FMCreateWizardPage setName(String name) {
        folderWizard.getComponent(NAME_TEXT_FIELD_ID).setSingleStringValue(name);
        log.info("Setting name: {}", name);
        return this;
    }

    @Step("I set description of the wizard")
    public FMCreateWizardPage setDescription(String description) {
        folderWizard.getComponent(DESCRIPTION_TEXT_FIELD_ID).setSingleStringValue(description);
        log.info("Setting description: {}", description);
        return this;
    }

    @Step("Type Name of the folder")
    public FMCreateWizardPage setTypeValue(String type) {
        folderWizard.setComponentValue(TYPE_FIELD_ID, type);
        log.info("Setting type value to: {}", type);
        return this;
    }

    @Step("Click on Accept and close the wizard")
    public FMFilterManagerPage clickAccept() {
        folderWizard.clickAccept();
        log.info("Clicking Accept");
        return new FMFilterManagerPage(driver, wait);
    }

    public void clickOnLabel(String label) {
        folderWizard.clickButtonByLabel(label);
        log.info("Clicking on label: {}", label);
    }

    public void clickOnAddCondition() {
        folderWizard.clickButtonById("Add", CONDITION_ID);
        log.info("Clicking on Add condition");
    }

    public void clickSwitcher(String switcherId) {
        ComponentFactory.create(switcherId, driver, wait).setSingleStringValue(Boolean.TRUE.toString());
        log.info("Click switcher {}", switcherId);
    }

    public void setValueInExpressionEditor(String expressionEditorId, String value) {
        getExpressionEditor(expressionEditorId).setValue(value);
        log.info("Setting value {} in Expression Editor", value);
    }

    @Step("I drag and drop filter by name")
    public void dragAndDropFilterByName(String filterName) {
        getDraggableListEnabledFilters().drop(getDraggableListAvailableFilters().getDraggableElement(filterName));
        log.info("Drag filter {} and drop it", filterName);
    }

    @Step("I drag and drop folder by name")
    public void dragAndDropFolderByName(String folderName) {
        getDraggableListEnabledFolders().drop(getDraggableListAvailableFolders().getDraggableElement(folderName));
        log.info("Drag folder {} and drop it", folderName);
    }

    private DraggableList getDraggableListAvailableFolders() {
        return DraggableList.create(driver, wait, DROPDOWNLIST_AVAILABLE_FOLDERS_ID);
    }

    private DraggableList getDraggableListEnabledFolders() {
        return DraggableList.create(driver, wait, DROPDOWNLIST_ENABLED_FOLDERS_ID);
    }

    private DraggableList getDraggableListAvailableFilters() {
        return DraggableList.create(driver, wait, DROPDOWNLIST_AVAILABLE_FILTERS_ID);
    }

    private DraggableList getDraggableListEnabledFilters() {
        return DraggableList.create(driver, wait, DROPDOWNLIST_ENABLED_FILTERS_ID);
    }

    private ExpressionEditor getExpressionEditor(String componentId) {
        DelayUtils.waitForPageToLoad(driver, wait);
        return ExpressionEditor.createById(driver, wait, componentId);
    }
}
package com.oss.pages.faultmanagement;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.list.DraggableList;
import com.oss.framework.listwidget.EditableList;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class FMCreateWAMVPage extends BasePage {
    private static final Logger log = LoggerFactory.getLogger(FMCreateWAMVPage.class);
    private static final String WIZARD_ID = "UserViewWizardModal";
    private static final String NAME_TEXT_FIELD_ID = "UserViewNameInput";
    private static final String DESCRIPTION_TEXT_FIELD_ID = "UserViewDescriptionInput";
    private static final String DROPDOWNLIST_AVAILABLE = "Available";
    private static final String DROPDOWNLIST_SELECTED = "Selected";
    private static final String EXTENDED_LIST_ID = "ExtendedList-WAMVFiltersInput";
    private final Wizard folderWizard = Wizard.createByComponentId(driver, wait, WIZARD_ID);
    private final DraggableList draggableListAvailable = DraggableList.create(driver, wait, DROPDOWNLIST_AVAILABLE);
    private final DraggableList draggableListSelected = DraggableList.create(driver, wait, DROPDOWNLIST_SELECTED);
    public FMCreateWAMVPage(WebDriver driver) {
        super(driver);
    }

    @Step("I set Name of the WAMV")
    public void setName(String name) {
        folderWizard.getComponent(NAME_TEXT_FIELD_ID, Input.ComponentType.TEXT_FIELD).setSingleStringValue(name);
        log.info("Set WAMV name {}", name);
    }

    @Step("I set description of the WAMV")
    public void setDescription(String description) {
        folderWizard.getComponent(DESCRIPTION_TEXT_FIELD_ID, Input.ComponentType.TEXT_AREA).setSingleStringValue(description);
        log.info("Add following description {} to WAMV", description);
    }

    @Step("I drag and drop filter by name")
    public void dragAndDropFilterByName(String filterName) {
        draggableListSelected.drop(draggableListAvailable.getDraggableElement(filterName));
        log.info("Drag filter {} and drop it", filterName);
    }

    @Step("I select N-th filter from filter list")
    public void selectFilterFromList(int row) {
        EditableList filters = EditableList.createById(driver, wait, EXTENDED_LIST_ID);
        filters.getRow(row).click();
        log.info("Selecting {}. filter from the list", row);
    }

    @Step("I click accept button")
    public void clickAcceptButton() {
        folderWizard.clickAccept();
        log.info("Clicking accept button");
    }
}

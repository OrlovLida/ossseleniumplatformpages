package com.oss.pages.faultmanagement;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.list.DropdownList;
import com.oss.framework.listwidget.EditableList;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

public class FMCreateWAMVPage extends BasePage {

    private static final String WIZARD_ID = "UserViewWizardModal";
    private static final String NAME_TEXT_FIELD_ID = "UserViewNameInput";
    private static final String DESCRIPTION_TEXT_FIELD_ID = "UserViewDescriptionInput";
    private static final String DROPDOWNLIST_AVALIVABLE = "Available";
    private static final String DROPDOWNLIST_SELECTED = "Selected";
    private static final String DROPDOWNLIST_FILTER = "Filters";

    public FMCreateWAMVPage(WebDriver driver) {
        super(driver);
    }

    private Wizard folderWizard = Wizard.createByComponentId(driver, wait, WIZARD_ID);
    private DropdownList dropdownListAvalivable = DropdownList.create(driver, wait, DROPDOWNLIST_AVALIVABLE);
    private DropdownList dropdownListSelected = DropdownList.create(driver, wait, DROPDOWNLIST_SELECTED);
    private DropdownList dropdownListFilter = DropdownList.create(driver, wait, DROPDOWNLIST_FILTER);



    @Step("I set Name of the WAMV")
    public void setName(String name) {
        folderWizard.getComponent(NAME_TEXT_FIELD_ID, Input.ComponentType.TEXT_FIELD).setSingleStringValue(name);
    }

    @Step("I set description of the WAMV")
    public void setDescription(String description) {
        folderWizard.getComponent(DESCRIPTION_TEXT_FIELD_ID, Input.ComponentType.TEXT_AREA).setSingleStringValue(description);
    }

    @Step("I drag and drop filter by name")
    public void dragAndDropFilterByName(String filterName) {
        dropdownListSelected.drop(dropdownListAvalivable.getDraggableElement(filterName));
    }

    @Step("I select N-th filter from filter list")
    public void selectFilterFromList(int row) {
        EditableList filterList = EditableList.createById(driver,wait,"ExtendedList-WAMVFiltersInput");
        filterList.getRow(row).clickCheckbox();
    }

    public void clickAcceptButton() {
        folderWizard.clickAccept();
    }


}

package com.oss.pages.logical;

import org.openqa.selenium.WebDriver;

import com.oss.framework.widgets.list.EditableList;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class LogicalLocationWizard extends BasePage {
    private static final String LOGICAL_LOCATION_TYPE_ID = "logicalLocationType";
    private static final String DIRECT_PHYSICAL_LOCATION_ID = "input_physicalLocation";
    private static final String CREATE_BUTTON_ID = "wizard-submit-button-logical_location-wizard";
    private static final String RECALCULATE_NAMING_BUTTON_ID = "recalculateNaming";
    private static final String DESCRIPTION_ID = "description";
    private static final String REMARKS_ID = "remarks";
    private static final String QUANTITY_ID = "quantity";
    private static final String WIZARD_ID = "optional_prompt-card";
    private static final String TYPE_ID = "detailType";
    private static final String NAMING_PREVIEW_LIST_ID = "namingPreviewList";
    private static final String LOCATION_NAME_ID = "name";
    private static final String NAME_TEXT_FIELD_ID = "name-TEXT_FIELD";
    private final Wizard wizard;

    public LogicalLocationWizard(WebDriver driver) {
        super(driver);
        wizard = Wizard.createByComponentId(driver, wait, WIZARD_ID);
    }

    @Step("Set location type to {}")
    public void setLocationType(String locationType) {
        wizard.setComponentValue(LOGICAL_LOCATION_TYPE_ID, locationType);
    }

    @Step("Set type in location to {}")
    public void setType(String type) {
        wizard.setComponentValue(TYPE_ID, type);
    }

    @Step("Set name of {} logical location to {}")
    public void setLogicalLocationNameInList(int locationIndex, String logicalLocationName) {
        EditableList.createById(driver, wait, NAMING_PREVIEW_LIST_ID).setValue(locationIndex, logicalLocationName, LOCATION_NAME_ID, NAME_TEXT_FIELD_ID);
    }

    @Step("Set Direct Physical Location to {}")
    public void setDirectPhysicalLocation(String directPhysicalLocation) {
        wizard.setComponentValue(DIRECT_PHYSICAL_LOCATION_ID, directPhysicalLocation);
    }

    @Step("Set Remarks to {}")
    public void setRemarks(String remarks) {
        wizard.setComponentValue(REMARKS_ID, remarks);
    }

    @Step("Clicking on Recalculate Naming button")
    public void clickRecalculateNaming() {
        wizard.clickButtonById(RECALCULATE_NAMING_BUTTON_ID);
    }

    @Step("Setting description to {}")
    public void setDescription(String description) {
        wizard.setComponentValue(DESCRIPTION_ID, description);
    }

    @Step("Setting quantity to {}")
    public void setQuantity(String quantity) {
        wizard.setComponentValue(QUANTITY_ID, quantity);
    }

    @Step("Clicking on Next button")
    public void clickNext() {
        wizard.clickNext();
    }

    @Step("Clicking on Create button")
    public void clickCreate() {
        wizard.clickButtonById(CREATE_BUTTON_ID);
    }
}
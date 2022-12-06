package com.oss.pages.physical;

import com.oss.framework.components.data.Data;
import com.oss.framework.utils.DelayUtils;
import org.openqa.selenium.WebDriver;

import com.oss.framework.components.inputs.Input.ComponentType;
import com.oss.framework.widgets.list.EditableList;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SublocationWizardPage extends BasePage {

    private static final String SUBLOCATION_TYPE = "type";
    private static final String SUBLOCATION_NAME = "name";
    private static final String PRECISE_LOCATION = "preciseLocation";
    private static final String WIDTH = "width";
    private static final String DEPTH = "depth";
    private static final String QUANTITY = "quantity";
    private static final String WIZARD_ID = "sublocation-wizard";
    private static final String DESCRIPTION = "description";
    private static final String REMARKS = "remarks";
    private static final String SUBMIT_BUTTON_ID = "wizard-submit-button-sublocation-wizard";
    private static final String SUBLOCATION_MODEL_DATA_ATTRIBUTE_NAME = "input_model";
    private static final String CREATE_BUTTON_ID = "wizard-submit-button-sublocation-wizard";
    private static final String NAMING_PREVIEW_LIST_ID = "namingPreviewList";
    private static final String NAME_IN_LIST_POPUP_FIELD_ID = "name-TEXT_FIELD";
    private static final String RECALCULATE_NAMING_BUTTON_ID = "recalculateNaming";
    private final Wizard wizard;

    public SublocationWizardPage(WebDriver driver) {
        super(driver);
        wizard = Wizard.createByComponentId(driver, wait, WIZARD_ID);
    }

    @Step("Set Sublocation Type")
    public void setSublocationType(String sublocationType) {
        wizard.setComponentValue(SUBLOCATION_TYPE, sublocationType, ComponentType.COMBOBOX);
    }

    @Step("Setting location {locationIndex} name to {subLocationName}")
    public void setSubLocationNameInList(int locationIndex, String subLocationName) {
        EditableList.createById(driver, wait, NAMING_PREVIEW_LIST_ID).setValue(locationIndex, subLocationName, SUBLOCATION_NAME, NAME_IN_LIST_POPUP_FIELD_ID);
    }

    @Step("Clicking on recalculate naming button using ID")
    public void clickRecalculateNaming() {
        wizard.clickButtonById(RECALCULATE_NAMING_BUTTON_ID);
    }

    @Step("Set Sublocation Name")
    public void setSublocationName(String sublocationName) {
        wizard.setComponentValue(SUBLOCATION_NAME, sublocationName, ComponentType.TEXT_FIELD);
    }

    @Step("Set Sublocation Precise Location")
    public void setPreciseLocation(String preciseLocation) {
        wizard.getComponent(PRECISE_LOCATION).setSingleStringValueContains(preciseLocation);
    }

    @Step("Set Sublocation Model")
    public void setSublocationModel(String sublocationModel) {
        wizard.getComponent(SUBLOCATION_MODEL_DATA_ATTRIBUTE_NAME, ComponentType.SEARCH_FIELD)
                .setValueContains(Data.createSingleData(sublocationModel));
    }

    @Step("Set Width")
    public void setWidth(String width) {
        wizard.setComponentValue(WIDTH, width, ComponentType.NUMBER_FIELD);
    }

    @Step("Set Depth")
    public void setDepth(String depth) {
        wizard.setComponentValue(DEPTH, depth, ComponentType.NUMBER_FIELD);
    }

    @Step("Set Quantity")
    public void setQuantity(String quantity) {
        wizard.setComponentValue(QUANTITY, quantity, ComponentType.NUMBER_FIELD);
    }

    @Step("Set Description")
    public void setDescription(String description) {
        wizard.setComponentValue(DESCRIPTION, description, ComponentType.TEXT_AREA);
    }

    @Step("Set Remarks")
    public void setRemarks(String remarks) {
        wizard.setComponentValue(REMARKS,remarks, ComponentType.TEXT_AREA);
    }

    @Step("Click Next Step button")
    public void clickNext() {
        wizard.clickNext();
    }

    @Step("Click Accept button")
    public void clickAccept() {
        wizard.clickAccept();
    }

    @Step("Click Create button")
    public void create() {
        wizard.clickButtonById(SUBMIT_BUTTON_ID);
    }

    @Step("Check if attributes is autocompleted")
    public String getComponentValue(String componentId) {
        //DelayUtils.waitForPageToLoad(driver, wait);
        return wizard.getComponent(componentId).getStringValue();
    }
}

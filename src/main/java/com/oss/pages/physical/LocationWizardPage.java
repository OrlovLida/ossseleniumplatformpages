package com.oss.pages.physical;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.data.Data;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.list.EditableList;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class LocationWizardPage extends BasePage {
    private static final Logger LOGGER = LoggerFactory.getLogger(LocationWizardPage.class);
    private static final String LOCATION_TYPE_DATA_ATTRIBUTE_NAME = "type";
    private static final String LOCATION_PARENT_LOCATION_DATA_ATTRIBUTE_NAME = "parentLocation";
    private static final String LOCATION_DIRECT_PHYSICAL_LOCATION_DATA_ATTRIBUTE_NAME = "physicalLocation";
    private static final String LOGICAL_LOCATION_TYPE_DATA_ATTRIBUTE_NAME = "logicalLocationType";
    private static final String LOCATION_NAME_DATA_ATTRIBUTE_NAME = "name";
    private static final String LOCATION_ABBREVIATION_DATA_ATTRIBUTE_NAME = "abbreviation";
    private static final String LOCATION_ADDRESS_DATA_ATTRIBUTE_NAME = "address";
    private static final String LOCATION_LATITUDE_DATA_ATTRIBUTE_NAME = "latitude";
    private static final String LOCATION_LONGITUDE_DATA_ATTRIBUTE_NAME = "longitude";
    private static final String LOCATION_DESCRIPTION_DATA_ATTRIBUTE_NAME = "description";
    private static final String LOCATION_REMARKS_DATA_ATTRIBUTE_NAME = "remarks";
    private static final String LOCATION_IMPORTANT_CATEGORY_DATA_ATTRIBUTE_NAME = "importanceCategory";
    private static final String LOCATION_UNILATERAL_FLAG_DATA_ATTRIBUTE_NAME = "oss__physical-inventory__physicallocations__type__Site__UnilateralFlag";
    private static final String GEOGRAPHICAL_ADDRESS_SEARCH_DATA_ATTRIBUTE_NAME = "geoSearch";
    private static final String NUMBER_OF_LOCATIONS_DATA_ATTRIBUTE_NAME = "locationsCount";
    private static final String MODEL_DATA_ATTRIBUTE_NAME = "masterModel";
    private static final String STREET_DATA_ATTRIBUTE_NAME = "Street";
    private static final String CITY_ATTRIBUTE_NAME = "City";
    private static final String STREET_NUMBER_DATA_ATTRIBUTE_NAME = "streetNumber";
    private static final String WIZARD_ID = "optional_prompt-card";
    private static final String CREATE_BUTTON_ID = "wizard-submit-button-physical-location-wizard";
    private static final String NAMING_PREVIEW_LIST_ID = "namingPreviewList";
    private static final String NAME_IN_LIST_POPUP_FIELD_ID = "name-TEXT_FIELD";
    private static final String RECALCULATE_NAMING_BUTTON_ID = "recalculateNaming";
    private final Wizard locationWizard = Wizard.createByComponentId(driver, wait, WIZARD_ID);
    private static final String CANCEL_BUTTON_ID = "wizard-cancel-button-physical-location-wizard";
    private static final String SUBMIT_BUTTON_ID = "wizard-submit-button-physical-location-wizard";

    public LocationWizardPage(WebDriver driver) {
        super(driver);
    }

    @Step("Create location with mandatory fields (location type, name, geographical address) filled in")
    public void createLocation(String locationType, String locationName) {
        setLocationType(locationType);
        setLocationName(locationName);
        DelayUtils.waitForPageToLoad(driver, wait);
        clickNext();
        setGeographicalAddress("");
        DelayUtils.waitForPageToLoad(driver, wait);
        clickNext();
        DelayUtils.waitForPageToLoad(driver, wait);
        accept();
    }

    @Step("Clicking on recalculate naming button using ID")
    public void clickRecalculateNaming() {
        locationWizard.clickButtonById(RECALCULATE_NAMING_BUTTON_ID);
    }

    @Step("Create Location in Step Wizard with mandatory fields - Location Type: {locationType} and Name: {locationName}.")
    public void createLocationStepWizard(String locationType, String locationName) {
        setLocationType(locationType);
        setLocationName(locationName);
        clickNext();
        setGeographicalAddress("a");
        DelayUtils.waitForPageToLoad(driver, wait);
        clickNext();
        DelayUtils.waitForPageToLoad(driver, wait);
        accept();
    }

    @Step("Setting location {locationIndex} name to {locationName}")
    public void setLocationNameInList(int locationIndex, String locationName) {
        EditableList.createById(driver, wait, NAMING_PREVIEW_LIST_ID).setValue(locationIndex, locationName, LOCATION_NAME_DATA_ATTRIBUTE_NAME, NAME_IN_LIST_POPUP_FIELD_ID);
    }

    @Step("Create Location with mandatory fields - Location Type: {locationType} and Name: {locationName} in any wizard")
    public void createLocationInAnyWizard(String locationType, String locationName) {
        if (locationWizard.countNumberOfSteps() > 1) {
            createLocationStepWizard(locationType, locationName);
        } else {
            createLocation(locationType, locationName);
        }
    }

    @Step("Create PoP on chosen Location in Direct Physical Location: {directPhysicalLocation}.")
    public void createPoP(String directPhysicalLocation) {
        setDirectPhysicalLocation(directPhysicalLocation);
        DelayUtils.waitForPageToLoad(driver, wait);
        locationWizard.clickNext();
        DelayUtils.waitForPageToLoad(driver, wait);
        locationWizard.clickAccept();
    }

    @Step("Set description")
    public LocationWizardPage setDescription(String description) {
        locationWizard.setComponentValue(LOCATION_DESCRIPTION_DATA_ATTRIBUTE_NAME, description,
                Input.ComponentType.TEXT_FIELD);
        return this;
    }

    @Step("Set location type")
    public void setLocationType(String locationType) {
        locationWizard.setComponentValue(LOCATION_TYPE_DATA_ATTRIBUTE_NAME, locationType, Input.ComponentType.COMBOBOX);
    }

    @Step("Set location name")
    public void setLocationName(String locationName) {
        locationWizard.setComponentValue(LOCATION_NAME_DATA_ATTRIBUTE_NAME, locationName,
                Input.ComponentType.TEXT_FIELD);
    }

    @Step("Set geographical address")
    public void setGeographicalAddress(String geographicalAddress) {
        locationWizard.getComponent(GEOGRAPHICAL_ADDRESS_SEARCH_DATA_ATTRIBUTE_NAME, Input.ComponentType.SEARCH_FIELD)
                .setValueContains(Data.createSingleData(geographicalAddress));
    }

    @Step("Set first Address in the drop-down list")
    public void setFirstAddress() {
        if (locationWizard.getComponent(LOCATION_ADDRESS_DATA_ATTRIBUTE_NAME,
                Input.ComponentType.SEARCH_FIELD).getStringValue().isEmpty()) {
            locationWizard.setComponentValue(LOCATION_ADDRESS_DATA_ATTRIBUTE_NAME, " ",
                    Input.ComponentType.SEARCH_FIELD);
        }
    }

    @Step("Set Direct Physical Location")
    public void setDirectPhysicalLocation(String directPhysicalLocation) {
        locationWizard.getComponent(LOCATION_DIRECT_PHYSICAL_LOCATION_DATA_ATTRIBUTE_NAME,
                Input.ComponentType.SEARCH_FIELD).setValueContains(Data.createSingleData(directPhysicalLocation));
    }

    @Step("Set number of locations to create")
    public void setNumberOfLocations(String count) {
        locationWizard.setComponentValue(NUMBER_OF_LOCATIONS_DATA_ATTRIBUTE_NAME, count, Input.ComponentType.TEXT_FIELD);
    }

    @Step("Set model")
    public void setModel(String model) {
        locationWizard.getComponent(MODEL_DATA_ATTRIBUTE_NAME, Input.ComponentType.SEARCH_FIELD).
                setValueContains(Data.createSingleData(model));
    }

    @Step("Set street")
    public void setStreet(String street) {
        LOGGER.info("Setting street to {}", street);
        locationWizard.setComponentValue(STREET_DATA_ATTRIBUTE_NAME, street);
    }

    @Step("Set street number")
    public void setStreetNumber(String number) {
        LOGGER.info("Setting number to {}", number);
        locationWizard.setComponentValue(STREET_NUMBER_DATA_ATTRIBUTE_NAME, number);
    }

    @Step("Set unilateral flag")
    public void setUnilateralFlag(String checkbox) {
        locationWizard.setComponentValue(LOCATION_UNILATERAL_FLAG_DATA_ATTRIBUTE_NAME, checkbox, Input.ComponentType.CHECKBOX);
    }

    @Step("Set Location Type if empty")
    public void setTypeIfEmpty(String type) {
        if (locationWizard.getComponent(LOGICAL_LOCATION_TYPE_DATA_ATTRIBUTE_NAME, Input.ComponentType.COMBOBOX).getValue() == null) {
            locationWizard.setComponentValue(LOGICAL_LOCATION_TYPE_DATA_ATTRIBUTE_NAME, type, Input.ComponentType.COMBOBOX);
        }
    }

    @Step("Set first City if empty")
    public void setCityIfEmpty() {
        if (locationWizard.getComponent(CITY_ATTRIBUTE_NAME).getValue() == null) {
            locationWizard.setComponentValue(CITY_ATTRIBUTE_NAME, "");
        }
    }

    @Step("Click Next Step button")
    public void clickNext() {
        locationWizard.clickNext();
    }

    @Step("Click Cancel button")
    public void clickCancel() {
        locationWizard.clickButtonById(CANCEL_BUTTON_ID);
    }

    @Step("Click Accept button")
    public void accept() {
        locationWizard.clickAccept();
    }

    @Step("Click Create button")
    public void create() {
        locationWizard.clickButtonById(SUBMIT_BUTTON_ID);
    }

}
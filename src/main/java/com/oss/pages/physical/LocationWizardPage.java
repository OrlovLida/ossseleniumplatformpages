package com.oss.pages.physical;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.data.Data;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

public class LocationWizardPage extends BasePage {

    private static final String LOCATION_TYPE_DATA_ATTRIBUTE_NAME = "type-input";
    private static final String LOCATION_PARENT_LOCATION_DATA_ATTRIBUTE_NAME = "parentLocation";
    private static final String LOCATION_DIRECT_PHYSICAL_LOCATION_DATA_ATTRIBUTE_NAME = "physicalLocation";
    private static final String LOGICAL_LOCATION_TYPE_DATA_ATTRIBUTE_NAME = "logicalLocationType-input";
    private static final String LOCATION_NAME_DATA_ATTRIBUTE_NAME = "name";
    private static final String LOCATION_ABBREVIATION_DATA_ATTRIBUTE_NAME = "abbreviation";
    private static final String LOCATION_ADDRESS_DATA_ATTRIBUTE_NAME = "address";
    private static final String LOCATION_STEP_ADDRESS_SEARCH_DATA_ATTRIBUTE_NAME = "geoSearch";
    private static final String LOCATION_LATITUDE_DATA_ATTRIBUTE_NAME = "latitude";
    private static final String LOCATION_LONGITUDE_DATA_ATTRIBUTE_NAME = "longitude";
    private static final String LOCATION_DESCRIPTION_DATA_ATTRIBUTE_NAME = "description";
    private static final String LOCATION_REMARKS_DATA_ATTRIBUTE_NAME = "remarks";
    private static final String LOCATION_IMPORTANT_CATEGORY_DATA_ATTRIBUTE_NAME = "importanceCategory";
    private static final String GEOGRAPHICAL_ADDRESS_SEARCH_DATA_ATTRIBUTE_NAME = "geoSearch_OSF";
    private static final String NUMBER_OF_LOCATIONS_DATA_ATTRIBUTE_NAME = "locationsCount";
    private static final String MODEL_DATA_ATTRIBUTE_NAME = "masterModel_OSF";
    private static final String STREET_NUMBER_DATA_ATTRIBUTE_NAME = "Street_OSF";


    public static LocationWizardPage goToLocationWizardPageLive(WebDriver driver, String basicURL) {
        driver.get(String.format("%s/#/view/location-inventory/wizard/physicallocation/create/select-type?" + "perspective=LIVE", basicURL));
        return new LocationWizardPage(driver);
    }

    public static LocationWizardPage goToLocationWizardPagePlan(WebDriver driver, String basicURL, String perspective) {
        driver.get(String.format("%s/#/view/location-inventory/wizard/physicallocation/create/select-type?" + perspective, basicURL));
        return new LocationWizardPage(driver);
    }

    public LocationWizardPage(WebDriver driver) {
        super(driver);
    }

    @Step("Create Location with mandatory fields (Location type, Name, Address) filled in")
    public void createLocation(String locationType, String locationName) {
        setLocationType(locationType);
        setLocationName(locationName);
        setFirstAddress();
        DelayUtils.sleep();
        accept();
    }

    @Step("Create Location in Step Wizard with mandatory fields (Location type, Name, Address) filled in")
    public void createLocationStepWizard(String locationType, String locationName) {
        setLocationType(locationType);
        setLocationName(locationName);
        clickNext();
        setGeographicalAddress("a");
        DelayUtils.sleep(13000);
        clickNext();
        DelayUtils.sleep(10000);
        accept();
    }

    @Step("Create Location in Popup Wizard with mandatory fields (Location type, Name, Address) filled in")
    public void createLocationPopupWizard(String locationType, String locationName) {
        Wizard popupWizard = Wizard.createByComponentId(driver, wait, "Popup");
        popupWizard.setComponentValue(LOCATION_TYPE_DATA_ATTRIBUTE_NAME, locationType, Input.ComponentType.COMBOBOX);
        popupWizard.setComponentValue(LOCATION_NAME_DATA_ATTRIBUTE_NAME, locationName, Input.ComponentType.TEXT_FIELD);
        popupWizard.clickNext();
        DelayUtils.sleep(10000);
        popupWizard.clickNext();
        DelayUtils.sleep(10000);
        popupWizard.clickAccept();
    }

    @Step("Create PoP on chosen Location with chosen Direct Physical Location")
    public void createPoP(String directPhysicalLocation) {
        Wizard popupWizard = Wizard.createByComponentId(driver, wait, "Popup");
        setDirectPhysicalLocation(directPhysicalLocation);
        DelayUtils.sleep(3000);
        popupWizard.clickNext();
        DelayUtils.sleep();
        popupWizard.clickAccept();
    }

    @Step("Set Description")
    public LocationWizardPage setDescription(String description) {
        locationWizard.setComponentValue(LOCATION_DESCRIPTION_DATA_ATTRIBUTE_NAME, description, Input.ComponentType.TEXT_FIELD);
        return this;
    }

    @Step("Set Location Type")
    public void setLocationType(String locationType) {
        locationWizard.setComponentValue(LOCATION_TYPE_DATA_ATTRIBUTE_NAME, locationType, Input.ComponentType.COMBOBOX);
    }

    @Step("Set Location Name")
    public void setLocationName(String locationName) {
        locationWizard.setComponentValue(LOCATION_NAME_DATA_ATTRIBUTE_NAME, locationName, Input.ComponentType.TEXT_FIELD);
    }

    @Step("Set first Address in the drop-down list")
    public void setFirstAddress() {
        if (locationWizard.getComponent(LOCATION_ADDRESS_DATA_ATTRIBUTE_NAME, Input.ComponentType.SEARCH_FIELD).getStringValue().isEmpty()) {
            locationWizard.setComponentValue(LOCATION_ADDRESS_DATA_ATTRIBUTE_NAME, " ", Input.ComponentType.SEARCH_FIELD);
        }
    }

    @Step("Set Direct Physical Location")
    public void setDirectPhysicalLocation(String directPhysicalLocation) {
        locationWizard.getComponent(LOCATION_DIRECT_PHYSICAL_LOCATION_DATA_ATTRIBUTE_NAME, Input.ComponentType.SEARCH_FIELD).setValueContains(Data.createSingleData(directPhysicalLocation));
    }

    @Step("Set Geographical Address")
    public void setGeographicalAddress(String geographicalAddress) {
        locationWizard.getComponent(GEOGRAPHICAL_ADDRESS_SEARCH_DATA_ATTRIBUTE_NAME, Input.ComponentType.SEARCH_FIELD).setValueContains(Data.createSingleData(geographicalAddress));
    }

    @Step("Set number of locations to create")
    public void setNumberOfLocations(String count) {
        locationWizard.setComponentValue(NUMBER_OF_LOCATIONS_DATA_ATTRIBUTE_NAME, count, Input.ComponentType.TEXT_FIELD);
    }

    @Step("Set model")
    public void setModel(String model) {
        locationWizard.getComponent(MODEL_DATA_ATTRIBUTE_NAME, Input.ComponentType.SEARCH_FIELD).setValueContains(Data.createSingleData(model));
    }

    @Step("Set street number")
    public void setStreetNumber(String number) {
        locationWizard.setComponentValue(STREET_NUMBER_DATA_ATTRIBUTE_NAME, number, Input.ComponentType.TEXT_FIELD);
    }

    @Step("Set Location Type if empty")
    public void setTypeIfEmpty(String type) {
        if (locationWizard.getComponent(LOGICAL_LOCATION_TYPE_DATA_ATTRIBUTE_NAME, Input.ComponentType.COMBOBOX).getValue() == null) {
            locationWizard.setComponentValue(LOGICAL_LOCATION_TYPE_DATA_ATTRIBUTE_NAME, type, Input.ComponentType.COMBOBOX);
        }
    }

    @Step("Click Next Step button")
    public void clickNext() {
        locationWizard.clickNext();
    }

    @Step("Click Accept button")
    public void accept() {
        locationWizard.clickAccept();
    }

    private Wizard locationWizard = Wizard.createWizard(driver, wait);
}
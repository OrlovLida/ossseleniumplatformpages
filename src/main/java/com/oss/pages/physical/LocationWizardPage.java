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
    private static final String LOCATION_NAME_DATA_ATTRIBUTE_NAME = "name";
    private static final String LOCATION_ABBREVIATION_DATA_ATTRIBUTE_NAME = "abbreviation";
    private static final String LOCATION_LATITUDE_DATA_ATTRIBUTE_NAME = "latitude";
    private static final String LOCATION_LONGITUDE_DATA_ATTRIBUTE_NAME = "longitude";
    private static final String LOCATION_DESCRIPTION_DATA_ATTRIBUTE_NAME = "description";
    private static final String LOCATION_REMARKS_DATA_ATTRIBUTE_NAME = "remarks";
    private static final String LOCATION_IMPORTANT_CATEGORY_DATA_ATTRIBUTE_NAME = "importanceCategory";
    private static final String LOCATION_UNILATERAL_FLAG_DATA_ATTRIBUTE_NAME = "oss__physical-inventory__physicallocations__type__Site__UnilateralFlag";
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

    @Step("Create location with mandatory fields (location type, name, geographical address) filled in")
    public void createLocation(String locationType, String locationName) {
        setLocationType(locationType);
        setLocationName(locationName);
        DelayUtils.waitForPageToLoad(driver, wait);
        clickNext();
        setAnyGeographicalAddress();
        DelayUtils.waitForPageToLoad(driver, wait);
        clickNext();
        DelayUtils.waitForPageToLoad(driver, wait);
        accept();
    }

    @Step("Set description")
    public LocationWizardPage setDescription(String description) {
        locationWizard.setComponentValue(LOCATION_DESCRIPTION_DATA_ATTRIBUTE_NAME, description, Input.ComponentType.TEXT_FIELD);
        return this;
    }

    @Step("Set location type")
    public void setLocationType(String locationType) {
        locationWizard.setComponentValue(LOCATION_TYPE_DATA_ATTRIBUTE_NAME, locationType, Input.ComponentType.COMBOBOX);
    }

    @Step("Set location name")
    public void setLocationName(String locationName) {
        locationWizard.setComponentValue(LOCATION_NAME_DATA_ATTRIBUTE_NAME, locationName, Input.ComponentType.TEXT_FIELD);
    }

    @Step("Set geographical address")
    public void setGeographicalAddress(String geographicalAddress) {
        locationWizard.getComponent(GEOGRAPHICAL_ADDRESS_SEARCH_DATA_ATTRIBUTE_NAME, Input.ComponentType.SEARCH_FIELD).setValueContains(Data.createSingleData(geographicalAddress));
    }

    @Step("Set any geographical address")
    public void setAnyGeographicalAddress() {
        locationWizard.getComponent(GEOGRAPHICAL_ADDRESS_SEARCH_DATA_ATTRIBUTE_NAME, Input.ComponentType.SEARCH_FIELD).setSingleStringValueContains("");
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

    @Step("Set unilateral flag")
    public void setUnilateralFlag(String checkbox) {
        locationWizard.setComponentValue(LOCATION_UNILATERAL_FLAG_DATA_ATTRIBUTE_NAME, checkbox, Input.ComponentType.CHECKBOX);
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
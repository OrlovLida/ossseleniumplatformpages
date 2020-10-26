package com.oss.pages.physical;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.data.Data;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

public class LocationWizardPage extends BasePage {

    private static final String LOCATION_TYPE_DATA_ATTRIBUTE_NAME = "type-input";
    private static final String LOCATION_PARENT_LOCATION_DATA_ATTRIBUTE_NAME = "parentLocation";
    private static final String LOCATION_NAME_DATA_ATTRIBUTE_NAME = "name";
    private static final String LOCATION_ABBREVIATION_DATA_ATTRIBUTE_NAME = "abbreviation";
    private static final String LOCATION_ADDRESS_DATA_ATTRIBUTE_NAME = "address";
    private static final String LOCATION_LATITUDE_DATA_ATTRIBUTE_NAME = "latitude";
    private static final String LOCATION_LONGITUDE_DATA_ATTRIBUTE_NAME = "longitude";
    private static final String LOCATION_DESCRIPTION_DATA_ATTRIBUTE_NAME = "description";
    private static final String LOCATION_REMARKS_DATA_ATTRIBUTE_NAME = "remarks";
    private static final String LOCATION_IMPORTANT_CATEGORY_DATA_ATTRIBUTE_NAME = "importanceCategory";
    private static final String GEOGRAPHICAL_ADDRESS_SEARCH_DATA_ATTRIBUTE_NAME = "geographicalAddress";

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

    @Step("Create Location with mandatory fields (Location type, name, address) filled in")
    public void createLocation(String locationType, String locationName) {
        locationWizard.setComponentValue(LOCATION_TYPE_DATA_ATTRIBUTE_NAME, locationType, Input.ComponentType.COMBOBOX);
        locationWizard.setComponentValue(LOCATION_NAME_DATA_ATTRIBUTE_NAME, locationName, Input.ComponentType.TEXT_FIELD);
        if (locationWizard.getComponent(LOCATION_ADDRESS_DATA_ATTRIBUTE_NAME, Input.ComponentType.SEARCH_FIELD).getStringValue().isEmpty()) {
            locationWizard.setComponentValue(LOCATION_ADDRESS_DATA_ATTRIBUTE_NAME, " ", Input.ComponentType.SEARCH_FIELD);
        }
        accept();
    }

    @Step("Set Description")
    public LocationWizardPage setDescription(String description) {
        locationWizard.setComponentValue(LOCATION_DESCRIPTION_DATA_ATTRIBUTE_NAME, description, Input.ComponentType.TEXT_FIELD);
        return this;
    }

    @Step("Set Location Type")
    public void setLocationType(String locationType) {
        Input locationTypeComponent = locationWizard.getComponent(LOCATION_TYPE_DATA_ATTRIBUTE_NAME, Input.ComponentType.COMBOBOX);
        locationTypeComponent.setValue(Data.createSingleData(locationType));
    }

    @Step("Set Location Name")
    public void setLocationName(String locationName) {
        Input locationNameComponent = locationWizard.getComponent(LOCATION_NAME_DATA_ATTRIBUTE_NAME, Input.ComponentType.TEXT_FIELD);
        locationNameComponent.setValue(Data.createSingleData(locationName));
    }

    @Step("Set Geographical Address")
    public void setGeographicalAddress(String geographicalAddress) {
        Input geographicalAddressComponent = locationWizard.getComponent(GEOGRAPHICAL_ADDRESS_SEARCH_DATA_ATTRIBUTE_NAME, Input.ComponentType.SEARCH_FIELD);
        geographicalAddressComponent.clear();
        geographicalAddressComponent.setSingleStringValue(geographicalAddress);
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
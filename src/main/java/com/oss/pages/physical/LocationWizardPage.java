package com.oss.pages.physical;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.data.Data;
import com.oss.framework.utils.LocatingUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
//import org.testng.Assert;

public class LocationWizardPage extends BasePage {
    public static final String COMBOBOX_ID_TYPE = "physicalinventory_physical_location_form_type";
    public static final String SEARCH_FIELD_ID_PARENT_LOCATION = "physicalinventory_physical_location_form_parent_location";
    public static final String TEXT_FIELD_ID_NAME = "physicalinventory_physical_location_form_name";
    public static final String TEXT_FIELD_ID_ABBREVIATION = "physicalinventory_physical_location_form_abbreviation";
    public static final String TEXT_FIELD_ID_LATITUDE = "physicalinventory_physical_location_form_latitude";
    public static final String TEXT_FIELD_ID_LONGITUDE = "physicalinventory_physical_location_form_longitude";
    public static final String TEXT_FIELD_ID_DESCRIPTION = "physicalinventory_physical_location_form_description";
    public static final String TEXT_FIELD_ID_REMARKS = "physicalinventory_physical_location_remarks";
    public static final String COMBOBOX_ID_IMPORTANCE = "physicalinventory_physical_location_form_importance_category";
    public static final String COMBOBOX_ID_USE = "BuildingUseCategory-MasterBuildingUseCategory-Name";

    private static final String LOCATION_TYPE = "type-input";
    private static final String LOCATION_NAME = "name";
    private static final String GEOGRAPHICAL_ADDRESS_SEARCH = "geographicalAddress";

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

    private Wizard locationWizard = Wizard.createWizard(driver, wait);

//    private Wizard getWizard() {
//        if(this.wizard == null) {
//            this.wizard = Wizard.createWizard(this.driver, this.wait);
//        }
//        return wizard;
//    }

    public void setComponentValue(String componentId, String value, Input.ComponentType componentType) {
        locationWizard.setComponentValue(componentId, value, componentType);
    }

    public String getComponentValue(String componentId, Input.ComponentType componentType) {
        Input value = locationWizard.getComponent(componentId, componentType);
        return value.getStringValue();
    }

    public Input getComponent(String componentId, Input.ComponentType componentType) {
        return locationWizard.getComponent(componentId, componentType);
    }

    public void proceed() {
        locationWizard.proceed();
    }

    public void checkIfSuccess() {
        LocatingUtils.waitUsingXpath("//div[contains(@class,'success')]", wait);
//        Asserts.assertTrue(driver.findElement(By.xpath("//div[contains(@class,'success')]")).isEnabled());
    }

    @Step("Create Location with mandatory fields (Location type, name, address) filled in")
    public void createLocation(String locationType, String randomLocationName) {
        setComponentValue("type-input", locationType, Input.ComponentType.COMBOBOX);
        setComponentValue("name", randomLocationName, Input.ComponentType.TEXT_FIELD);
        if (getComponentValue("address", Input.ComponentType.SEARCH_FIELD).isEmpty()) {
            setComponentValue("address", " ", Input.ComponentType.SEARCH_FIELD);
        }
        accept();
    }

    @Step("Type description")
    public LocationWizardPage typeDescription(String description) {
        setComponentValue("description", description, Input.ComponentType.TEXT_FIELD);
        return this;
    }

    @Step("Set Location Type")
    public void setLocationType(String locationType) {
        Input locationTypeComponent = locationWizard.getComponent(LOCATION_TYPE, Input.ComponentType.COMBOBOX);
        locationTypeComponent.setValue(Data.createSingleData(locationType));
    }

    @Step("Set Location Name")
    public void setLocationName(String locationName) {
        Input locationNameComponent = locationWizard.getComponent(LOCATION_NAME, Input.ComponentType.TEXT_FIELD);
        locationNameComponent.setValue(Data.createSingleData(locationName));
    }

    @Step("Set Geographical Address")
    public void setGeographicalAddress(String geographicalAddress) {
        Input geographicalAddressComponent = locationWizard.getComponent(GEOGRAPHICAL_ADDRESS_SEARCH, Input.ComponentType.SEARCH_FIELD);
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
}


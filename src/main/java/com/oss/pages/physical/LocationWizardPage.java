package com.oss.pages.physical;

import com.oss.framework.components.*;
import com.oss.framework.utils.LocatingUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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


    public static LocationWizardPage goToLocationWizardPageLive(WebDriver driver, String basicURL){
        driver.get(String.format("%s/#/view/location-inventory/wizard/physicallocation/create/select-type?"+"perspective=LIVE",basicURL));
        return new LocationWizardPage(driver);
    }
    public static LocationWizardPage goToLocationWizardPagePlan(WebDriver driver, String basicURL,String perspective){
        driver.get(String.format("%s/#/view/location-inventory/wizard/physicallocation/create/select-type?"+ perspective,basicURL));
        return new LocationWizardPage(driver);
    }

    public LocationWizardPage(WebDriver driver) {
        super(driver);
    }
    private Wizard locationWizard = Wizard.createWizard(driver,wait);

//    private Wizard getWizard() {
//        if(this.wizard == null) {
//            this.wizard = Wizard.createWizard(this.driver, this.wait);
//        }
//        return wizard;
//    }

    public void setComponentValue(String componentId, String value, Input.ComponentType componentType) {
        Input input = locationWizard.getComponent(componentId, componentType);
        input.setSingleStringValue(value);
    }

    public String getComponentValue(String componentId, Input.ComponentType componentType) {
        Input input = locationWizard.getComponent(componentId, componentType);
        return input.getStringValue();
    }

    public Input getComponent(String componentId, Input.ComponentType componentType) {
        return locationWizard.getComponent(componentId, componentType);
    }

    public void proceed() {
        locationWizard.proceed();
    }
    public void accept() {
        locationWizard.clickAccept();
    }
    public void next() {
        locationWizard.clickNext();
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

}


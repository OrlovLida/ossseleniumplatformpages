package com.oss.pages.physical;

import org.openqa.selenium.WebDriver;

import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class ChangeLocationWizardPage extends BasePage {
    private static final String WIZARD_ID = "change-location-wizard";
    private static final String LOCATION_KEBAB_BUTTON_ID = "location_button-dropdown-wizard";
    private static final String LOCATION_CREATE_OBJECTS_BUTTON_ID = "location_button_open-wizard";

    private static final String LOCATION_DATA_ATTRIBUTE_NAME = "location";
    private static final String PHYSICAL_LOCATION_DATA_ATTRIBUTE_NAME = "physicalLocation";
    private static final String PRECISE_LOCATION_DATA_ATTRIBUTE_NAME = "preciseLocation";
    private static final String LOGICAL_LOCATION_DATA_ATTRIBUTE_NAME = "logicalLocation";

    public ChangeLocationWizardPage(WebDriver driver) {
        super(driver);
    }

    public Wizard getWizard() {
        return Wizard.createByComponentId(driver, wait, WIZARD_ID);
    }

    @Step("Set Location")
    public void setLocation(String location) {
        getWizard().setComponentValue(LOCATION_DATA_ATTRIBUTE_NAME, location);
    }

    @Step("Set Physical Location")
    public void setPhysicalLocation(String physicalLocation) {
        getWizard().setComponentValue(PHYSICAL_LOCATION_DATA_ATTRIBUTE_NAME, physicalLocation);
    }

    @Step("Set Precise Location")
    public void setPreciseLocation(String preciseLocation) {
        getWizard().setComponentValue(PRECISE_LOCATION_DATA_ATTRIBUTE_NAME, preciseLocation);
    }

    @Step("Set Logical Location")
    public void setLogicalLocation(String logicalLocation) {
        getWizard().setComponentValue(LOGICAL_LOCATION_DATA_ATTRIBUTE_NAME, logicalLocation);
    }

    public String getLocation() {
        return getWizard().getComponent(LOCATION_DATA_ATTRIBUTE_NAME).getStringValue();
    }

    public String getPhysicalLocation() {
        return getWizard().getComponent(PHYSICAL_LOCATION_DATA_ATTRIBUTE_NAME).getStringValue();
    }

    public String getPreciseLocation() {
        return getWizard().getComponent(PRECISE_LOCATION_DATA_ATTRIBUTE_NAME).getStringValue();
    }

    public String getLogicalLocation() {
        return getWizard().getComponent(LOGICAL_LOCATION_DATA_ATTRIBUTE_NAME).getStringValue();
    }

    public String getWizardName() {
        return getWizard().getWizardName();
    }

    public void createObjectsForLocation() {
        getWizard().clickButtonById(LOCATION_KEBAB_BUTTON_ID);
        getWizard().clickButtonById(LOCATION_CREATE_OBJECTS_BUTTON_ID);
    }

}

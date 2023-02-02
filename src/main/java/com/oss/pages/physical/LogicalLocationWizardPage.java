package com.oss.pages.physical;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class LogicalLocationWizardPage extends BasePage {
    private static final String WIZARD_ID = "optional_prompt-card";

    private static final String LOGICAL_LOCATION_NAME_DATA_ATTRIBUTE_NAME = "name";
    private static final String LOGICAL_LOCATION_LOCATION_DATA_ATTRIBUTE_NAME = "location";
    private static final String LOGICAL_LOCATION_PHYSICAL_LOCATION_LOCATION_DATA_ATTRIBUTE_NAME = "physicalLocation";

    private static final String LOGICAL_LOCATION_CREATE_BUTTON_ID = "wizard-submit-button-logical_location-wizard";

    public LogicalLocationWizardPage(WebDriver driver) {
        super(driver);
    }

    public Wizard getWizard() {
        return Wizard.createByComponentId(driver, wait, WIZARD_ID);
    }

    @Step("Set logical location name")
    public void setName(String name) {
        getWizard().setComponentValue(LOGICAL_LOCATION_NAME_DATA_ATTRIBUTE_NAME, name);
    }

    public String getLocation() {
        return getWizard().getComponent(LOGICAL_LOCATION_LOCATION_DATA_ATTRIBUTE_NAME).getStringValue();
    }

    public String getDirectPhysicalLocation() {
        return getWizard().getComponent(LOGICAL_LOCATION_PHYSICAL_LOCATION_LOCATION_DATA_ATTRIBUTE_NAME).getStringValue();
    }

    @Step("Click Next Step button")
    public void next() {
        getWizard().clickNext();
    }

    @Step("Click Create button")
    public void create() {
        getWizard().clickButtonById(LOGICAL_LOCATION_CREATE_BUTTON_ID);
    }

    public String getCurrentStepTitle() {
        return getWizard().getCurrentStepTitle();
    }

    public boolean isLocationGreyedOut() {
        Input.MouseCursor cursor = getWizard().getComponent(LOGICAL_LOCATION_LOCATION_DATA_ATTRIBUTE_NAME).cursor();
        return Input.MouseCursor.NOT_ALLOWED.equals(cursor);
    }

    public boolean isDirectPhysicalLocationGreyedOut() {
        Input.MouseCursor cursor = getWizard().getComponent(LOGICAL_LOCATION_PHYSICAL_LOCATION_LOCATION_DATA_ATTRIBUTE_NAME).cursor();
        return Input.MouseCursor.NOT_ALLOWED.equals(cursor);
    }

}
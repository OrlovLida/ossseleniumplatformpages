package com.oss.pages.physical;

import org.openqa.selenium.WebDriver;

import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

import static com.oss.framework.components.inputs.Input.ComponentType.SEARCH_FIELD;
import static com.oss.framework.components.inputs.Input.ComponentType.TEXT_FIELD;

public class CreateCopyOfDeviceWizardPage extends BasePage {

    private static final String DEVICE_COPY_WIZARD_DATA_ATTRIBUTE_NAME = "create_copy_common_form_app";
    private static final String DEVICE_COPY_NAME_DATA_ATTRIBUTE_NAME = "text_name";
    private static final String DEVICE_COPY_LOCATION_DATA_ATTRIBUTE_NAME = "search_location";
    private static final String DEVICE_COPY_PRECISE_LOCATION_DATA_ATTRIBUTE_NAME = "search_precise_location";
    private static final String DEVICE_COPY_CREATE_BUTTON_DATA_ATTRIBUTE_NAME = "create_copy_common_buttons_app-1";

    public CreateCopyOfDeviceWizardPage(WebDriver driver) {
        super(driver);
    }

    @Step("Set name")
    public void setName(String name) {
        getCopyDeviceWizard().setComponentValue(DEVICE_COPY_NAME_DATA_ATTRIBUTE_NAME, name, TEXT_FIELD);
    }

    @Step("Set Location")
    public void setLocation(String location) {
        getCopyDeviceWizard().getComponent(DEVICE_COPY_LOCATION_DATA_ATTRIBUTE_NAME, SEARCH_FIELD).clear();
        getCopyDeviceWizard().getComponent(DEVICE_COPY_PRECISE_LOCATION_DATA_ATTRIBUTE_NAME, SEARCH_FIELD).clear();
        getCopyDeviceWizard().setComponentValue(DEVICE_COPY_LOCATION_DATA_ATTRIBUTE_NAME, location, SEARCH_FIELD);
    }

    @Step("Set Precise Location")
    public void setPreciseLocation(String preciseLocation) {
        getCopyDeviceWizard().getComponent(DEVICE_COPY_PRECISE_LOCATION_DATA_ATTRIBUTE_NAME, SEARCH_FIELD).clear();
        getCopyDeviceWizard().setComponentValue(DEVICE_COPY_PRECISE_LOCATION_DATA_ATTRIBUTE_NAME, preciseLocation, SEARCH_FIELD);
    }

    @Step("Click Create button")
    public void create() {
        getCopyDeviceWizard().clickButtonById(DEVICE_COPY_CREATE_BUTTON_DATA_ATTRIBUTE_NAME);
    }

    private Wizard getCopyDeviceWizard() {
        return Wizard.createByComponentId(driver, wait, DEVICE_COPY_WIZARD_DATA_ATTRIBUTE_NAME);
    }
}

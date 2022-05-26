package com.oss.pages.radio;

import org.openqa.selenium.WebDriver;

import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class HostingWizardPage extends BasePage {

    private static final String HOSTING_WIZARD_DATA_ATTRIBUTE_NAME = "hosting-wizard";
    private static final String DEVICES_ON_LOCATION_DATA_ATTRIBUTE_NAME = "devicesOnLocation";
    private static final String OBJECT_TYPE_DATA_ATTRIBUTE_NAME = "objectType";
    private static final String HOSTING_DATA_ATTRIBUTE_NAME = "hosting";
    private static final String ONLY_COMPATIBLE_DATA_ATTRIBUTE_NAME = "onlyCompatible";
    private static final String SHOW_ONLY_UNUSED_ARRAYS_DATA_ATTRIBUTE_NAME = "showOnlyUnusedArrays";

    public HostingWizardPage(WebDriver driver) {
        super(driver);
    }

    @Step("Click Accept button")
    public void clickAccept() {
        getHostingWizard().clickAccept();
    }

    @Step("Use 'Only Compatible' checkbox")
    public void onlyCompatible(String showOnlyCompatible) {
        getHostingWizard().setComponentValue(ONLY_COMPATIBLE_DATA_ATTRIBUTE_NAME, showOnlyCompatible);
    }

    @Step("Set hosting device using contains")
    public HostingWizardPage setDevice(String deviceName) {
        getHostingWizard().getComponent(DEVICES_ON_LOCATION_DATA_ATTRIBUTE_NAME).setSingleStringValueContains(deviceName);
        return this;
    }

    @Step("Set hosting logic using contains")
    public HostingWizardPage setObjectType(String objectType) {
        getHostingWizard().getComponent(OBJECT_TYPE_DATA_ATTRIBUTE_NAME).setSingleStringValueContains(objectType);
        return this;
    }

    @Step("Select hosting object using contains")
    public void setHostingContains(String hostingObjectName) {
        getHostingWizard().getComponent(HOSTING_DATA_ATTRIBUTE_NAME).setSingleStringValueContains(hostingObjectName);
    }

    @Step("Select hosting object")
    public HostingWizardPage setHosting(String hostingObjectName) {
        getHostingWizard().setComponentValue(HOSTING_DATA_ATTRIBUTE_NAME, hostingObjectName);
        return this;
    }

    private Wizard getHostingWizard() {
        return Wizard.createByComponentId(driver, wait, HOSTING_WIZARD_DATA_ATTRIBUTE_NAME);
    }
}

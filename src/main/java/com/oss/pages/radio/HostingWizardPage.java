package com.oss.pages.radio;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.data.Data;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

import static com.oss.framework.components.inputs.Input.ComponentType.*;

public class HostingWizardPage extends BasePage {

    private static final String HOSTING_WIZARD_DATA_ATTRIBUTE_NAME = "hosting-wizard";
    private static final String DEVICES_ON_LOCATION_DATA_ATTRIBUTE_NAME = "devicesOnLocation";
    private static final String OBJECT_TYPE_DATA_ATTRIBUTE_NAME = "objectType";
    private static final String HOSTING_DATA_ATTRIBUTE_NAME = "hosting";
    private static final String ONLY_COMPATIBLE_DATA_ATTRIBUTE_NAME = "onlyCompatible";
    private static final String SHOW_ONLY_UNUSED_ARRAYS_DATA_ATTRIBUTE_NAME = "showOnlyUnusedArrays";

    public HostingWizardPage(WebDriver driver) { super(driver); }

    @Step("Click Accept button")
    public void clickAccept() {
        getHostingWizard().clickAccept();
    }

    @Step("Use 'Only Compatible' checkbox")
    public HostingWizardPage onlyCompatible(String showOnlyCompatible) {
        getHostingWizard().setComponentValue(ONLY_COMPATIBLE_DATA_ATTRIBUTE_NAME, showOnlyCompatible, CHECKBOX);
        return this;
    }

    @Step("Set hosting device using contains")
    public HostingWizardPage setDevice(String deviceName) {
        getHostingWizard().getComponent(DEVICES_ON_LOCATION_DATA_ATTRIBUTE_NAME, COMBOBOXV2).setSingleStringValueContains(deviceName);
        return this;
    }

    @Step("Set hosting logic using contains")
    public HostingWizardPage setObjectType(String objectType) {
        getHostingWizard().getComponent(OBJECT_TYPE_DATA_ATTRIBUTE_NAME, COMBOBOXV2).setSingleStringValueContains(objectType);
        return this;
    }

    @Step("Select hosting object using contains")
    public HostingWizardPage setHostingContains(String hostingObjectName) {
        getHostingWizard().getComponent(HOSTING_DATA_ATTRIBUTE_NAME, COMBOBOXV2).setSingleStringValueContains(hostingObjectName);
        return this;
    }

    @Step("Select hosting object")
    public HostingWizardPage setHosting(String hostingObjectName) {
        getHostingWizard().setComponentValue(HOSTING_DATA_ATTRIBUTE_NAME, hostingObjectName, COMBOBOXV2);
        return this;
    }

    private Wizard getHostingWizard() {
        return Wizard.createByComponentId(driver, wait, HOSTING_WIZARD_DATA_ATTRIBUTE_NAME);
    }
}
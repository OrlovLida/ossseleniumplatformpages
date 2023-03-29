package com.oss.pages.transport.regulatoryLicense;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class RegulatoryLicenseAssignmentWizardPage extends BasePage {
    private static final String OBJECT_TYPE_FIELD_ID = "assign-regulatory-license-assignment-type-field";

    private static final String LOCATION_NAME_FIELD_ID = "assign-regulatory-license-location-field";
    private static final String MICROWAVE_ANTENNA_NAME_FIELD_ID = "assign-regulatory-license-antenna-field";
    private static final String MICROWAVE_LINK_NAME_FIELD_ID = "assign-regulatory-license-microwave-link-field";
    private static final String MICROWAVE_CHANNEL_NAME_FIELD_ID = "assign-regulatory-license-microwave-channel-field";

    private static final String COMPONENT_ID = "assign-regulatory-license-view_prompt-card";

    public RegulatoryLicenseAssignmentWizardPage(WebDriver driver) {
        super(driver);
    }

    @Step("Assign {objectName} to Regulatory License")
    public void assignLocationToRegulatoryLicense(String objectName) {
        setObjectType("Location");
        DelayUtils.waitForPageToLoad(driver, wait);
        setObjectName(objectName, LOCATION_NAME_FIELD_ID);
        DelayUtils.waitForPageToLoad(driver, wait);
        clickAccept();
    }

    @Step("Assign {objectName} to Regulatory License")
    public void assignMicrowaveAntennaToRegulatoryLicense(String objectName) {
        setObjectType("Microwave Antenna");
        DelayUtils.waitForPageToLoad(driver, wait);
        setObjectName(objectName, MICROWAVE_ANTENNA_NAME_FIELD_ID);
        DelayUtils.waitForPageToLoad(driver, wait);
        clickAccept();
    }

    @Step("Assign {objectName} to Regulatory License")
    public void assignMicrowaveLinkToRegulatoryLicense(String objectName) {
        setObjectType("Microwave Link");
        DelayUtils.waitForPageToLoad(driver, wait);
        setObjectName(objectName, MICROWAVE_LINK_NAME_FIELD_ID);
        DelayUtils.waitForPageToLoad(driver, wait);
        clickAccept();
    }

    @Step("Assign {objectName} to Regulatory License")
    public void assignMicrowaveChannelToRegulatoryLicense(String objectName) {
        setObjectType("Microwave Channel");
        DelayUtils.waitForPageToLoad(driver, wait);
        setObjectName(objectName, MICROWAVE_CHANNEL_NAME_FIELD_ID);
        DelayUtils.waitForPageToLoad(driver, wait);
        clickAccept();
    }

    @Step("Set object type to {objectType}")
    private void setObjectType(String objectType) {
        getWizard().setComponentValue(OBJECT_TYPE_FIELD_ID, objectType, Input.ComponentType.COMBOBOX);
    }

    @Step("Set object name to {objectName}")
    private void setObjectName(String objectName, String objectNameFieldId) {
        getWizard().getComponent(objectNameFieldId).setSingleStringValueContains(objectName);
    }

    @Step("Click accept button")
    private void clickAccept() {
        getWizard().clickAccept();
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    private Wizard getWizard() {
        return Wizard.createByComponentId(driver, wait, COMPONENT_ID);
    }
}

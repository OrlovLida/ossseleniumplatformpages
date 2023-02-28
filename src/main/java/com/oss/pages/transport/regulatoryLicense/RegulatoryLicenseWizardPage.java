package com.oss.pages.transport.regulatoryLicense;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class RegulatoryLicenseWizardPage extends BasePage {

    private static final String NUMBER_FIELD_ID = "regulatory-license-number-field";
    private static final String REGULATORY_AGENCY_FIELD_ID = "regulatory-license-regulatory-agency-field";
    private static final String STARTING_DATE_FIELD_ID = "regulatory-license-starting-date-field";
    private static final String EXPIRATION_DATE_FIELD_ID = "regulatory-license-expiration-date-field";
    private static final String OPERATING_HOURS_FIELD_ID = "regulatory-license-operating-hours-field";
    private static final String STATUS_FIELD_ID = "regulatory-license-status-field";
    private static final String TYPE_FIELD_ID = "regulatory-license-type-field";
    private static final String DESCRIPTION_FIELD_ID = "regulatory-license-description-field";
    private static final String COMPONENT_ID = "wizard-regulatory-license-view_prompt-card";

    public RegulatoryLicenseWizardPage(WebDriver driver) {
        super(driver);
    }

    @Step("Set Number to {number}")
    public void setNumber(String number) {
        setTextFieldComponentValue(NUMBER_FIELD_ID, number);
    }

    @Step("Set Regulatory Agency to {regulatoryAgency}")
    public void setRegulatoryAgency(String regulatoryAgency) {
        setComboBoxFieldComponentValue(REGULATORY_AGENCY_FIELD_ID, regulatoryAgency);
    }

    @Step("Set Starting Date to {startingDate}")
    public void setStartingDate(String startingDate) {
        setDateFieldComponentValue(STARTING_DATE_FIELD_ID, startingDate);
    }

    @Step("Set Expiration Date to {expirationDate}")
    public void setExpirationDate(String expirationDate) {
        setDateFieldComponentValue(EXPIRATION_DATE_FIELD_ID, expirationDate);
    }

    @Step("Set Operating Hours to {operatingHours}")
    public void setOperatingHours(String operatingHours) {
        setTextFieldComponentValue(OPERATING_HOURS_FIELD_ID, operatingHours);
    }

    @Step("Set Status to {status}")
    public void setStatus(String status) {
        setComboBoxFieldComponentValue(STATUS_FIELD_ID, status);
    }

    @Step("Set Type to {type}")
    public void setType(String type) {
        setComboBoxFieldComponentValue(TYPE_FIELD_ID, type);
    }

    @Step("Set Description to {description}")
    public void setDescription(String description) {
        setTextFieldComponentValue(DESCRIPTION_FIELD_ID, description);
    }

    @Step("Click accept button")
    public void clickAccept() {
        getWizard().clickAccept();
    }

    private void setTextFieldComponentValue(String componentId, String value) {
        getWizard().setComponentValue(componentId, value, Input.ComponentType.TEXT_FIELD);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    private void setComboBoxFieldComponentValue(String componentId, String value) {
        getWizard().setComponentValue(componentId, value, Input.ComponentType.COMBOBOX);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    private void setDateFieldComponentValue(String componentId, String value) {
        getWizard().setComponentValue(componentId, value, Input.ComponentType.DATE);
    }

    private Wizard getWizard() {
        return Wizard.createByComponentId(driver, wait, COMPONENT_ID);
    }
}

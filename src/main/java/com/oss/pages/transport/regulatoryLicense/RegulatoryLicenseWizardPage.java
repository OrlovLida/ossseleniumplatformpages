package com.oss.pages.transport.regulatoryLicense;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

public class RegulatoryLicenseWizardPage extends BasePage {

    private static final String NUMBER_FIELD_ID = "regulatory-license-number-field";
    private static final String REGULATORY_AGENCY_FIELD_ID = "regulatory-license-regulatory-agency-field";
    private static final String STARTING_DATE_FIELD_ID = "regulatory-license-starting-date-field";
    private static final String EXPIRATION_DATE_FIELD_ID = "regulatory-license-expiration-date-field";
    private static final String OPERATING_HOURS_FIELD_ID = "regulatory-license-operating-hours-field";
    private static final String STATUS_FIELD_ID = "regulatory-license-status-field";
    private static final String TYPE_FIELD_ID = "regulatory-license-type-field";
    private static final String DESCRIPTION_FIELD_ID = "regulatory-license-description-field";

    private final Wizard wizard;

    public RegulatoryLicenseWizardPage(WebDriver driver) {
        super(driver);
        wizard = Wizard.createByComponentId(driver, wait, "wizard-regulatory-license-view");
    }

    @Step("Set Number to {number}")
    public void setNumber(String number) {
        wizard.setComponentValue(NUMBER_FIELD_ID, number, Input.ComponentType.TEXT_FIELD);
    }

    @Step("Set Regulatory Agency to {regulatoryAgency}")
    public void setRegulatoryAgency(String regulatoryAgency) {
        wizard.setComponentValue(REGULATORY_AGENCY_FIELD_ID, regulatoryAgency, Input.ComponentType.COMBOBOX);
    }

    @Step("Set Starting Date to {startingDate}")
    public void setStartingDate(String startingDate) {
        wizard.setComponentValue(STARTING_DATE_FIELD_ID, startingDate, Input.ComponentType.DATE);
    }

    @Step("Set Expiration Date to {expirationDate}")
    public void setExpirationDate(String expirationDate) {
        wizard.setComponentValue(EXPIRATION_DATE_FIELD_ID, expirationDate, Input.ComponentType.DATE);
    }

    @Step("Set Operating Hours to {operatingHours}")
    public void setOperatingHours(String operatingHours) {
        wizard.setComponentValue(OPERATING_HOURS_FIELD_ID, operatingHours, Input.ComponentType.TEXT_FIELD);
    }

    @Step("Set Status to {status}")
    public void setStatus(String status) {
        wizard.setComponentValue(STATUS_FIELD_ID, status, Input.ComponentType.COMBOBOX);
    }

    @Step("Set Type to {type}")
    public void setType(String type) {
        wizard.setComponentValue(TYPE_FIELD_ID, type, Input.ComponentType.COMBOBOX);
    }

    @Step("Set Description to {description}")
    public void setDescription(String description) {
        wizard.setComponentValue(DESCRIPTION_FIELD_ID, description, Input.ComponentType.TEXT_FIELD);
    }

    @Step("Click accept button")
    public void clickAccept() {
        wizard.clickAcceptOldWizard();
        DelayUtils.waitForPageToLoad(driver, wait);
    }
}

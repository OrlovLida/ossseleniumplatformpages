package com.oss.pages.transport.regulatoryLicense;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.CommonHierarchyApp;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import java.util.List;

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
        wizard = Wizard.createWizard(driver, wait);
    }

    @Step("Set Number to {number}")
    public void setNumber(String number) {
        Input numberComponent = getEmptyTextFieldComponent(NUMBER_FIELD_ID);
        numberComponent.setSingleStringValue(number);
    }

    @Step("Set Regulatory Agency to {regulatoryAgency}")
    public void setRegulatoryAgency(String regulatoryAgency) { setComboBoxComponentValue(REGULATORY_AGENCY_FIELD_ID, regulatoryAgency);}

    @Step("Set Starting Date to {startingDate}")
    public void setStartingDate(String startingDate) {
        Input startingDateComponent = getEmptyTextFieldComponent(STARTING_DATE_FIELD_ID);
        startingDateComponent.setSingleStringValue(startingDate);
    }

    @Step("Set Expiration Date to {expirationDate}")
    public void setExpirationDate(String expirationDate) {
        Input expirationDateComponent = getEmptyTextFieldComponent(EXPIRATION_DATE_FIELD_ID);
        expirationDateComponent.setSingleStringValue(expirationDate);
    }

    @Step("Set Operating Hours to {operatingHours}")
    public void setOperatingHours(String operatingHours) {
        Input operatingHoursComponent = getEmptyTextFieldComponent(OPERATING_HOURS_FIELD_ID);
        operatingHoursComponent.setSingleStringValue(operatingHours);
    }

    @Step("Set Status to {status}")
    public void setStatus(String status) { setComboBoxComponentValue(STATUS_FIELD_ID, status);}


    @Step("Set Type to {type}")
    public void setType(String type) { setComboBoxComponentValue(TYPE_FIELD_ID, type);}

    @Step("Set Description to {description}")
    public void setDescription(String description) {
        Input descriptionComponent = getEmptyTextFieldComponent(DESCRIPTION_FIELD_ID);
        descriptionComponent.setSingleStringValue(description);
    }

    private void setComboBoxComponentValue(String componentId, String value){
        wizard.setComponentValue(componentId, value, Input.ComponentType.COMBOBOX);
    }

    private Input getEmptyTextFieldComponent(String componentId){
        Input component = wizard.getComponent(componentId, Input.ComponentType.TEXT_FIELD);
        component.clear();
        return component;
    }

    @Step("Click next step button")
    public void clickNextStep() {
        DelayUtils.waitForPageToLoad(driver, wait);
        wizard.clickNextStep();
    }

    @Step("Click accept button")
    public RegulatoryLicenseOverviewPage clickAccept() {
        wizard.clickAcceptOldWizard();
        return new RegulatoryLicenseOverviewPage(driver);
    }
}

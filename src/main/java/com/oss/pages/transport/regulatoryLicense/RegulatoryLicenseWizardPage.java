package com.oss.pages.transport.regulatoryLicense;

import com.oss.framework.alerts.SystemMessageContainer;
import com.oss.framework.alerts.SystemMessageInterface;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.framework.widgets.tablewidget.OldTable;
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

    private static final String REGULATORY_LICENSES_TABLE_ID = "tableApp";
    private static final String CREATE_BUTTON_DATA_ATTRIBUTENAME = "create";

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

    private OldTable getTableWidget(String tableId){
        return OldTable.createByComponentDataAttributeName(driver, wait, tableId);
    }

    @Step("Click create button")
    public void clickCreate() {
        DelayUtils.waitForPageToLoad(driver, wait);
        OldTable regulatoryLicensesTable = getTableWidget(REGULATORY_LICENSES_TABLE_ID);
        regulatoryLicensesTable.callAction(CREATE_BUTTON_DATA_ATTRIBUTENAME);
    }

    @Step("Click next step button")
    public void clickNextStep() {
        DelayUtils.waitForPageToLoad(driver, wait);
        wizard.clickNextStep();
    }

    @Step("Click accept button")
    public void clickAccept() {
        wizard.clickAcceptOldWizard();
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("Redirect to Regulatory License Overview")
    public void openRegulatoryLicenseOverview() {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, wait);
        systemMessage.clickMessageLink();
        DelayUtils.waitForPageToLoad(driver, wait);
    }
}

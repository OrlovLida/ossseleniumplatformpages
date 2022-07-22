package com.oss.pages.physical;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.inputs.Input.ComponentType;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class SublocationWizardPage extends BasePage {

    private static final String SUBLOCATION_TYPE = "type";
    private static final String SUBLOCATION_NAME = "name";
    private static final String PRECISE_LOCATION = "preciseLocation";
    private static final String WIDTH = "width";
    private static final String DEPTH = "depth";
    private static final String QUANTITY = "quantity";
    private static final String WIZARD_ID = "sublocation-wizard";
    private static final String DESCRIPTION = "description";
    private static final String SUBMIT_BUTTON_ID = "wizard-submit-button-sublocation-wizard";
    private final Wizard wizard;

    public SublocationWizardPage(WebDriver driver) {
        super(driver);
        wizard = Wizard.createByComponentId(driver, wait, WIZARD_ID);
    }

    @Step("Set Sublocation Type")
    public void setSublocationType(String sublocationType) {
        wizard.setComponentValue(SUBLOCATION_TYPE, sublocationType, ComponentType.COMBOBOX);

    }

    @Step("Set Sublocation Name")
    public void setSublocationName(String sublocationName) {
        wizard.setComponentValue(SUBLOCATION_NAME, sublocationName, ComponentType.TEXT_FIELD);
    }

    @Step("Set Sublocation Precise Location")
    public void setPreciseLocation(String preciseLocation) {
        wizard.getComponent(PRECISE_LOCATION).setSingleStringValueContains(preciseLocation);
    }

    @Step("Set Width")
    public void setWidth(String width) {
        wizard.setComponentValue(WIDTH, width, ComponentType.NUMBER_FIELD);
    }

    @Step("Set Depth")
    public void setDepth(String depth) {
        wizard.setComponentValue(DEPTH, depth, ComponentType.NUMBER_FIELD);
    }

    @Step("Set Quantity")
    public void setQuantity(String quantity) {
        wizard.setComponentValue(QUANTITY, quantity, ComponentType.NUMBER_FIELD);
    }

    @Step("Set Description")
    public void setDescription(String description) {
        wizard.setComponentValue(DESCRIPTION, description, ComponentType.TEXT_FIELD);
    }

    @Step("Click Next Step button")
    public void clickNext() {
        wizard.clickNext();
    }

    @Step("Click Accept button")
    public void clickAccept() {
        wizard.clickAccept();
    }

    @Step("Click Create button")
    public void create() {
        wizard.clickButtonById(SUBMIT_BUTTON_ID);
    }
}

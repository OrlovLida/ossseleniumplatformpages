package com.oss.pages.physical;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.inputs.Input.ComponentType;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class CreateCoolingZoneWizardPage extends BasePage {

    public CreateCoolingZoneWizardPage(WebDriver driver) {
        super(driver);
    }

    private Wizard wizard = Wizard.createByComponentId(driver, wait, WIZARD_ID);
    private static final String SET_COOLING_ZONE_NAME = "name";
    private static final String SELECT_COOLING_ZONE_NAME = "cooling_zone_uid";
    private static final String WIZARD_ID = "wizard";
    private static final String ACCEPT_ID = "wizard-submit-button-cooling-zone-update-wizard";

    @Step("Click Accept")
    public void clickAccept() {
        wizard.clickActionById(ACCEPT_ID);
    }

    @Step("Click Update")
    public void clickUpdate() {
        Wizard.createWizard(driver, wait).clickUpdate();
    }

    @Step("Set Cooling Zone name")
    public void setName(String coolingZoneName) {
        wizard.setComponentValue(SET_COOLING_ZONE_NAME, coolingZoneName, ComponentType.TEXT_FIELD);
    }

    @Step("Select Cooling Zone to assign to device")
    public void selectNameFromList(String coolingZoneName) {
        wizard.setComponentValue(SELECT_COOLING_ZONE_NAME, coolingZoneName, Input.ComponentType.SEARCH_FIELD);
    }
}

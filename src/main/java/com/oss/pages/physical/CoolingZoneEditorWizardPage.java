package com.oss.pages.physical;

import org.openqa.selenium.WebDriver;

import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class CoolingZoneEditorWizardPage extends BasePage {

    public CoolingZoneEditorWizardPage(WebDriver driver) {
        super(driver);
    }

    private Wizard wizard = Wizard.createByComponentId(driver, wait, WIZARD_ID);
    private static final String SELECT_COOLING_ZONE_NAME = "coolingZone";
    private static final String WIZARD_ID = "cooling-zone-assignment-wizard";
    private static final String ACCEPT_ID = "wizard-submit-button-cooling-zone-assignment-wizard";

    @Step("Click Update")
    public void clickAccept() {
        wizard.clickButtonById(ACCEPT_ID);
    }

    @Step("Select Cooling Zone to assign to device")
    public void selectNameFromList(String coolingZoneName) {
        wizard.setComponentValue(SELECT_COOLING_ZONE_NAME, coolingZoneName);
    }
}

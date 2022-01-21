package com.oss.pages.physical;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class CoolingZoneEditorWizardPage extends BasePage {

    public CoolingZoneEditorWizardPage(WebDriver driver) {
        super(driver);
    }

    private Wizard wizard = Wizard.createByComponentId(driver, wait, WIZARD_ID);
    private static final String SELECT_COOLING_ZONE_NAME = "cooling_zone_uid";
    private static final String WIZARD_ID = "physical_device_cooling_zone_view";

    @Step("Click Update")
    public void clickUpdate() {
        Wizard.createWizard(driver, wait).clickUpdate();
    }

    @Step("Select Cooling Zone to assign to device")
    public void selectNameFromList(String coolingZoneName) {
        wizard.setComponentValue(SELECT_COOLING_ZONE_NAME, coolingZoneName, Input.ComponentType.SEARCH_FIELD);
    }
}

package com.oss.pages.physical;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

public class CreateCoolingZoneWizardPage extends BasePage {

    public CreateCoolingZoneWizardPage(WebDriver driver) { super(driver); }

    @Step("Click Proceed")
    public void clickProceed() { Wizard.createWizard(driver, wait).proceed(); }

    @Step("Click Update")
    public void clickUpdate() { Wizard.createWizard(driver, wait).clickUpdate(); }

    @Step("Set Cooling Zone name")
    public void setName(String coolingZoneName) {
        Wizard coolingZoneWizard = Wizard.createByComponentId(driver, wait, "Popup");
        Input zoneNameField = coolingZoneWizard.getComponent("cooling_zone_name_id", Input.ComponentType.TEXT_FIELD);
        zoneNameField.setSingleStringValue(coolingZoneName);
    }

    @Step("Select Cooling Zone to assign to device")
    public void selectNameFromList(String coolingZoneName) {
        Wizard coolingZoneWizard = Wizard.createByComponentId(driver, wait, "Popup");
        Input nameField = coolingZoneWizard.getComponent("cooling_zone_uid", Input.ComponentType.SEARCH_FIELD);
        nameField.setSingleStringValue(coolingZoneName);
    }
}

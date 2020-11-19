package com.oss.pages.physical;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

import static com.oss.framework.components.inputs.Input.ComponentType.SEARCH_FIELD;

public class CreateCoolingZoneWizardPage extends BasePage {

    public CreateCoolingZoneWizardPage(WebDriver driver) { super(driver); }

    @Step("Click Proceed")
    public void clickProceed() { Wizard.createWizard(driver, wait).proceed(); }

    @Step("Click Update")
    public void clickUpdate() { Wizard.createWizard(driver, wait).clickUpdate(); }

    //TODO: change 'Popup' to whatever it's called in RC
    @Step("Set Cooling Zone name")
    public void setName(String coolingZoneName) {
        Wizard coolingZoneWizard = Wizard.createByComponentId(driver, wait, "coolingZoneCreateUpdateWizardId");
        Input zoneNameField = coolingZoneWizard.getComponent("cooling_zone_name_id", Input.ComponentType.TEXT_FIELD);
        zoneNameField.setSingleStringValue(coolingZoneName);
    }

    //TODO: change 'Popup' to whatever it's called in RC
    @Step("Select Cooling Zone to assign to device")
    public void selectNameFromList(String coolingZoneName) {
        Wizard coolingZoneWizard = Wizard.createByComponentId(driver, wait, "physical_device_cooling_zone_view");
        Input nameField = coolingZoneWizard.getComponent("cooling_zone_uid", Input.ComponentType.SEARCH_FIELD);
        nameField.setSingleStringValue(coolingZoneName);
    }
}

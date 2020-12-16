package com.oss.pages.radio;

import com.oss.framework.components.inputs.Input;
import static com.oss.framework.components.inputs.Input.ComponentType.COMBOBOXV2;
import com.oss.framework.data.Data;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

public class HostingWizardPage extends BasePage {
    private final Wizard wizard;
    private static final String ONLY_COMPATIBLE = "onlyCompatible";

    public HostingWizardPage(WebDriver driver) {
        super(driver);
        wizard = Wizard.createWizard(driver, wait);
    }

    @Step("Click Accept button")
    public void clickAccept() {
        wizard.clickAccept();
    }

    @Step("Use 'Only Compatible' checkbox")
    public void onlyCompatible(String showOnlyCompatible) {
        wizard.createByComponentId(driver, wait, "hosting-wizard");
        Input componentUseFirstAvailableID = wizard.getComponent(ONLY_COMPATIBLE, Input.ComponentType.CHECKBOX);
        componentUseFirstAvailableID.setSingleStringValue(showOnlyCompatible);
    }

    @Step("Select Device name")
    public void selectDevice(String deviceName) {
        Wizard wizard = Wizard.createByComponentId(driver, wait, "hosting-wizard");
        Input devicesOnLocation = wizard.getComponent("devicesOnLocation", COMBOBOXV2);
        DelayUtils.waitForPageToLoad(driver, wait);
        devicesOnLocation.setValueContains(Data.createSingleData(deviceName));
    }

    @Step("Select RAN Antenna Array")
    public void selectArray(String arrayName) {
        Wizard wizard = Wizard.createByComponentId(driver, wait, "hosting-wizard");
        Input hosting = wizard.getComponent("hosting", COMBOBOXV2);
        DelayUtils.waitForPageToLoad(driver, wait);
        hosting.setValueContains(Data.createSingleData(arrayName));
    }
}
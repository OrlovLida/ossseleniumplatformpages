package com.oss.pages.transport.regulatoryLicense;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

public class RegulatoryLicenseMicrowaveAntennasWizardPage extends BasePage {
    private static final String MICROWAVE_ANTENNA_ID = "assign-regulatory-license-assignment-object-field";

    private final Wizard wizard;

    public RegulatoryLicenseMicrowaveAntennasWizardPage(WebDriver driver) {
        super(driver);
        wizard = Wizard.createByComponentId(driver, wait, "assign-regulatory-license-view");
    }

    @Step("Set Microwave Antenna value to {microwaveAntenna}")
    public void selectMicrowaveAntenna(String microwaveAntenna) {
        //Input microwaveAntennaComponent = wizard.getComponent(MICROWAVE_ANTENNA_ID, Input.ComponentType.SEARCH_FIELD);
        //microwaveAntennaComponent.clear();
        //microwaveAntennaComponent.setSingleStringValue(microwaveAntenna);
        wizard.setComponentValue(MICROWAVE_ANTENNA_ID, microwaveAntenna, Input.ComponentType.SEARCH_FIELD);
    }

    @Step("Click accept button")
    public RegulatoryLicenseOverviewPage clickAccept() {
        wizard.clickAcceptOldWizard();
        DelayUtils.waitForPageToLoad(driver, wait);
        return new RegulatoryLicenseOverviewPage(driver);
    }
}

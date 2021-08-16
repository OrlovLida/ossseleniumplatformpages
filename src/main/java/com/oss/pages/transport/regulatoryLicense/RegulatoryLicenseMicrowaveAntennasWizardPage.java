package com.oss.pages.transport.regulatoryLicense;

import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.data.Data;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import com.oss.pages.transport.regulatoryLicense.RegulatoryLicenseOverviewPage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

public class RegulatoryLicenseMicrowaveAntennasWizardPage extends BasePage {
    private static final String MWANT_WIZARD_VIEW_ID = "assign-regulatory-license-assignment-object-field";

    private final Wizard wizard;

    public RegulatoryLicenseMicrowaveAntennasWizardPage(WebDriver driver) {
        super(driver);
        wizard = Wizard.createWizard(driver, wait);
    }

    @Step("Set Microwave Antenna value to {microwaveAntenna}")
    public void selectMicrowaveAntenna(String microwaveAntenna) {
        Input microwaveAntennaComponent = wizard.getComponent(MWANT_WIZARD_VIEW_ID, Input.ComponentType.SEARCH_FIELD);
        microwaveAntennaComponent.clear();
        microwaveAntennaComponent.setSingleStringValue(microwaveAntenna);
    }

    @Step("Click accept button")
    public RegulatoryLicenseOverviewPage clickAccept() {
        wizard.clickAcceptOldWizard();
        return new RegulatoryLicenseOverviewPage(driver);
    }


}

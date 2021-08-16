package com.oss.pages.transport.regulatoryLicense;

import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.data.Data;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import com.oss.pages.transport.regulatoryLicense.RegulatoryLicenseOverviewPage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

public class RegulatoryLicenseMicrowaveLinksWizardPage extends BasePage {
    private static final String MWL_WIZARD_VIEW_ID = "assign-regulatory-license-assignment-object-field";

    private final Wizard wizard;

    public RegulatoryLicenseMicrowaveLinksWizardPage(WebDriver driver) {
        super(driver);
        wizard = Wizard.createWizard(driver, wait);
    }

    @Step("Set Microwave Link value to {microwaveLink}")
    public void selectMicrowaveLinks(String microwaveLink) {
        Input microwaveLinkComponent = wizard.getComponent(MWL_WIZARD_VIEW_ID, Input.ComponentType.SEARCH_FIELD);
        microwaveLinkComponent.clear();
        microwaveLinkComponent.setSingleStringValue(microwaveLink);
    }

    @Step("Click accept button")
    public RegulatoryLicenseOverviewPage clickAccept() {
        wizard.clickAcceptOldWizard();
        return new RegulatoryLicenseOverviewPage(driver);
    }

}

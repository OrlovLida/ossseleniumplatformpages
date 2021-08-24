package com.oss.pages.transport.regulatoryLicense;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

public class RegulatoryLicenseMicrowaveLinksWizardPage extends BasePage {
    private static final String MICROWAVE_LINK_ID = "assign-regulatory-license-assignment-object-field";

    private final Wizard wizard;

    public RegulatoryLicenseMicrowaveLinksWizardPage(WebDriver driver) {
        super(driver);
        wizard = Wizard.createByComponentId(driver, wait, "assign-regulatory-license-view");
    }

    @Step("Set Microwave Link value to {microwaveLink}")
    public void selectMicrowaveLinks(String microwaveLink) {
        //Input microwaveLinkComponent = wizard.getComponent(MICROWAVE_LINK_ID, Input.ComponentType.SEARCH_FIELD);
        //microwaveLinkComponent.clear();
        //microwaveLinkComponent.setSingleStringValue(microwaveLink);
        wizard.setComponentValue(MICROWAVE_LINK_ID, microwaveLink, Input.ComponentType.SEARCH_FIELD);
    }

    @Step("Click accept button")
    public RegulatoryLicenseOverviewPage clickAccept() {
        wizard.clickAcceptOldWizard();
        DelayUtils.waitForPageToLoad(driver, wait);
        return new RegulatoryLicenseOverviewPage(driver);
    }
}

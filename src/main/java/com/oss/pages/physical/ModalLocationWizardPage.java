package com.oss.pages.physical;

import org.openqa.selenium.WebDriver;

import com.oss.framework.wizard.Wizard;

public class ModalLocationWizardPage extends LocationWizardPage {
    private static final String WIZARD_ID = "simple-wizard-component-physical-location-wizard";

    public ModalLocationWizardPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public Wizard getWizard() {
        return Wizard.createByComponentId(driver, wait, WIZARD_ID);
    }

}
package com.oss.pages.physical;

import org.openqa.selenium.WebDriver;

import com.oss.framework.wizard.Wizard;

public class ModalLogicalLocationWizardPage extends LogicalLocationWizardPage {
    private static final String WIZARD_ID = "simple-wizard-component-logical_location-wizard";

    public ModalLogicalLocationWizardPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public Wizard getWizard() {
        return Wizard.createByComponentId(driver, wait, WIZARD_ID);
    }

}
package com.oss.pages.physical;

import org.openqa.selenium.WebDriver;

import com.oss.framework.wizard.Wizard;

public class ModalSublocationWizardPage extends SublocationWizardPage{
    private static final String WIZARD_ID = "simple-wizard-component-sublocation-wizard";

    public ModalSublocationWizardPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public Wizard getWizard() {
        return Wizard.createByComponentId(driver, wait, WIZARD_ID);
    }

}

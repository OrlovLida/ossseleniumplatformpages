package com.oss.pages.nfv;

import com.oss.framework.wizard.Wizard;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class VNFWizardThirdStep extends VNFWizardStep {

    private VNFWizardThirdStep(WebDriver driver, WebDriverWait wait, Wizard vnfWizard) {
        super(driver, wait, vnfWizard);

    }

    public static final VNFWizardThirdStep create(WebDriver driver, WebDriverWait wait, Wizard vnfWizard) {
        return new VNFWizardThirdStep(driver, wait, vnfWizard);
    }

}

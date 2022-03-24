package com.oss.pages.nfv.vnf;

import com.oss.framework.wizard.Wizard;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class VNFWizardFourthStep extends VNFWizardStep {

    private VNFWizardFourthStep(WebDriver driver, WebDriverWait wait, Wizard vnfWizard) {
        super(driver, wait, vnfWizard);

    }

    public static final VNFWizardFourthStep create(WebDriver driver, WebDriverWait wait, Wizard vnfWizard) {
        return new VNFWizardFourthStep(driver, wait, vnfWizard);
    }

}

package com.oss.pages.nfv.vnfpkg;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

public class VNFPKGManualOnboardingWizardStep extends BasePage {

    protected final Wizard vnfpkgWizard;

    protected VNFPKGManualOnboardingWizardStep(WebDriver driver, WebDriverWait wait, Wizard vnfpkgWizard) {
        super(driver, wait);
        this.vnfpkgWizard = vnfpkgWizard;
    }
}
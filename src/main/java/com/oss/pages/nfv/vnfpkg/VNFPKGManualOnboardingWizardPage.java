package com.oss.pages.nfv.vnfpkg;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

public class VNFPKGManualOnboardingWizardPage extends BasePage {


    private static final String WIZARD_ID = "onboardWizardId";

    private final Wizard vnfpkgWizard;

    private VNFPKGManualOnboardingWizardPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        vnfpkgWizard = Wizard.createByComponentId(driver, wait, WIZARD_ID);
    }

    public static VNFPKGManualOnboardingWizardPage create(WebDriver driver, WebDriverWait wait) {
        return new VNFPKGManualOnboardingWizardPage(driver, wait);
    }

    public Wizard getWizard(){
        return vnfpkgWizard;
    }

    public VNFPKGManualOnboardingWizardFirstStep getFirstStep() {
        return VNFPKGManualOnboardingWizardFirstStep.create(driver, wait, vnfpkgWizard);
    }
}

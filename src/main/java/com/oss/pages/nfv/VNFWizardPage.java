package com.oss.pages.nfv;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class VNFWizardPage extends BasePage {

    private static final String WIZARD_ID = "wizard-widget-id";

    private final Wizard vnfWizard;

    private VNFWizardPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        vnfWizard = Wizard.createByComponentId(driver, wait, WIZARD_ID);
    }

    public static VNFWizardPage create(WebDriver driver, WebDriverWait wait) {
        return new VNFWizardPage(driver, wait);
    }

    public VNFWizardFirstStep getFirstStep() {
        return VNFWizardFirstStep.create(driver, wait, vnfWizard);
    }

    public VNFWizardSecondStep getSecondStep() {
        return VNFWizardSecondStep.create(driver, wait, vnfWizard);
    }

    public VNFWizardThirdStep getThirdStep() {
        return VNFWizardThirdStep.create(driver, wait, vnfWizard);
    }

    public VNFWizardFourthStep getFourthStep() {
        return VNFWizardFourthStep.create(driver, wait, vnfWizard);
    }

    public void goToNextStep() {
        vnfWizard.clickNext();
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public void clickAccept() {
        vnfWizard.clickAccept();
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public Wizard getWizard(){
        return Wizard.createByComponentId(driver, wait, WIZARD_ID);
    }
}

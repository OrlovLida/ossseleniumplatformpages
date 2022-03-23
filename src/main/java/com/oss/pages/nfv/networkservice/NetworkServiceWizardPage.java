package com.oss.pages.nfv.networkservice;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class NetworkServiceWizardPage extends BasePage {

    private static final String WIZARD_ID = "wizard-widget-id";

    private final Wizard vnfWizard;

    private NetworkServiceWizardPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        vnfWizard = Wizard.createByComponentId(driver, wait, WIZARD_ID);
    }

    public static NetworkServiceWizardPage create(WebDriver driver, WebDriverWait wait) {
        return new NetworkServiceWizardPage(driver, wait);
    }

    public NetworkServiceWizardFirstStep getFirstStep() {
        return NetworkServiceWizardFirstStep.create(driver, wait, vnfWizard);
    }

    public NetworkServiceWizardSecondStep getSecondStep() {
        return NetworkServiceWizardSecondStep.create(driver, wait, vnfWizard);
    }

    public NetworkServiceWizardThirdStep getThirdStep() {
        return NetworkServiceWizardThirdStep.create(driver, wait, vnfWizard);
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

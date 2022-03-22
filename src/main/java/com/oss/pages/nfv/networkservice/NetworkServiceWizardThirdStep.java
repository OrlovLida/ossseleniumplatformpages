package com.oss.pages.nfv.networkservice;

import com.oss.framework.wizard.Wizard;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class NetworkServiceWizardThirdStep extends NetworkServiceWizardStep {

    private NetworkServiceWizardThirdStep(WebDriver driver, WebDriverWait wait, Wizard vnfWizard) {
        super(driver, wait, vnfWizard);

    }

    public static final NetworkServiceWizardThirdStep create(WebDriver driver, WebDriverWait wait, Wizard vnfWizard) {
        return new NetworkServiceWizardThirdStep(driver, wait, vnfWizard);
    }

}

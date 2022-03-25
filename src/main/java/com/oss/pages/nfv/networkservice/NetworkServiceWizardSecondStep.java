package com.oss.pages.nfv.networkservice;

import com.oss.framework.wizard.Wizard;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class NetworkServiceWizardSecondStep extends NetworkServiceWizardStep {

    private NetworkServiceWizardSecondStep(WebDriver driver, WebDriverWait wait, Wizard vnfWizard) {
        super(driver, wait, vnfWizard);

    }

    public static final NetworkServiceWizardSecondStep create(WebDriver driver, WebDriverWait wait, Wizard vnfWizard) {
        return new NetworkServiceWizardSecondStep(driver, wait, vnfWizard);
    }

}

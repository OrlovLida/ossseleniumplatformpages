package com.oss.pages.nfv.networkslicesubnet;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class NetworkSliceSubnetWizardPage extends BasePage {

    private static final String WIZARD_ID = "wizard-widget-id";

    private final Wizard networkSliceSubnet;

    private NetworkSliceSubnetWizardPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        networkSliceSubnet = Wizard.createByComponentId(driver, wait, WIZARD_ID);
    }

    public static NetworkSliceSubnetWizardPage create(WebDriver driver, WebDriverWait wait) {
        return new NetworkSliceSubnetWizardPage(driver, wait);
    }

    public NetworkSliceSubnetWizardFirstStep getFirstStep() {
        return NetworkSliceSubnetWizardFirstStep.create(driver, wait, networkSliceSubnet);
    }

    public NetworkSliceSubnetWizardSecondStep getSecondStep() {
        return NetworkSliceSubnetWizardSecondStep.create(driver, wait, networkSliceSubnet);
    }

    public void goToNextStep() {
        networkSliceSubnet.clickNext();
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public void clickAccept() {
        networkSliceSubnet.clickAccept();
    }

    public Wizard getWizard(){
        return Wizard.createByComponentId(driver, wait, WIZARD_ID);
    }
}

package com.oss.pages.nfv.networkslice;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class NetworkSliceWizardPage extends BasePage {

    private static final String WIZARD_ID = "wizard-widget-id";

    private final Wizard networkSlice;

    private NetworkSliceWizardPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        networkSlice = Wizard.createByComponentId(driver, wait, WIZARD_ID);
    }

    public static NetworkSliceWizardPage create(WebDriver driver, WebDriverWait wait) {
        return new NetworkSliceWizardPage(driver, wait);
    }

    public NetworkSliceWizardFirstStep getFirstStep() {
        return NetworkSliceWizardFirstStep.create(driver, wait, networkSlice);
    }

    public NetworkSliceWizardSecondStep getSecondStep() {
        return NetworkSliceWizardSecondStep.create(driver, wait, networkSlice);
    }

    public void goToNextStep() {
        networkSlice.clickNext();
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public void clickAccept() {
        networkSlice.clickAccept();
    }

    public Wizard getWizard(){
        return Wizard.createByComponentId(driver, wait, WIZARD_ID);
    }
}

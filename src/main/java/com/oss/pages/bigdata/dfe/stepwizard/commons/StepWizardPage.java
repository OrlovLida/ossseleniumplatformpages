package com.oss.pages.bigdata.dfe.stepwizard.commons;

import com.oss.framework.components.inputs.Button;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public abstract class StepWizardPage extends BasePage implements StepWizardInterface {

    public StepWizardPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("I click Cancel")
    public void clickCancel(){
        DelayUtils.waitForPageToLoad(driver, wait);
        Button.create(driver, "Cancel").click();
    }

    @Step("I click Next Step")
    public void clickNextStep(){
        DelayUtils.waitForPageToLoad(driver, wait);
        DelayUtils.sleep();
        Button.create(driver, "Next Step").click();
    }

    @Step("I click Accept")
    public void clickAccept(){
        getWizard(driver, wait).clickAcceptOldWizard();
    }

}

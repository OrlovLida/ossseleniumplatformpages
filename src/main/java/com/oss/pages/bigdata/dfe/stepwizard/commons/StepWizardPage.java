package com.oss.pages.bigdata.dfe.stepwizard.commons;

import com.oss.framework.components.inputs.Button;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class StepWizardPage extends BasePage implements StepWizardInterface {

    private static final Logger log = LoggerFactory.getLogger(StepWizardPage.class);

    public StepWizardPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("I click Cancel")
    public void clickCancel(){
        DelayUtils.waitForPageToLoad(driver, wait);
        Button.create(driver, "Cancel").click();
        log.info("Finishing Step Wizard by clicking 'Cancel'");
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
        log.info("Finishing Step Wizard by clicking 'Accept'");
    }


}

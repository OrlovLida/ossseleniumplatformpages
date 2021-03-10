package com.oss.pages.bigdata.dfe.stepwizard.commons;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.oss.framework.components.inputs.Button;

public abstract class StepWizardPage extends BasePage {

    protected Wizard wizard;

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
        Button.create(driver, "Next Step").click();
    }

    @Step("I click Accept")
    public void clickAccept(){
        wizard.clickAcceptOldWizard();
    }

}

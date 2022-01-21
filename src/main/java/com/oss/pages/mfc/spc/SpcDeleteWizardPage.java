package com.oss.pages.mfc.spc;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.prompts.ConfirmationBox;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class SpcDeleteWizardPage<R extends BasePage> extends BasePage {

    private final R returnPage;

    protected SpcDeleteWizardPage(WebDriver driver, WebDriverWait wait, R returnPage) {
        super(driver, wait);
        this.returnPage = returnPage;
    }

    public static <R extends BasePage> SpcDeleteWizardPage<R> create(WebDriver driver, WebDriverWait wait, R returnPage) {
        DelayUtils.waitForPageToLoad(driver, wait);
        return new SpcDeleteWizardPage(driver, wait, returnPage);
    }

    @Step("Cancel deleting SPC")
    public R cancel() {
        ConfirmationBox.create(driver, wait).clickButtonByLabel("Cancel");
        return returnPage;
    }

    @Step("Accept deleting SPC")
    public R accept() {
        ConfirmationBox.create(driver, wait).clickButtonByLabel("OK");
        DelayUtils.sleep(5000);
        return returnPage;
    }

}

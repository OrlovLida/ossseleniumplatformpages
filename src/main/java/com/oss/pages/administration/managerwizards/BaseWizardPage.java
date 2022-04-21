package com.oss.pages.administration.managerwizards;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public abstract class BaseWizardPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(BaseWizardPage.class);

    public abstract String getWizardId();

    protected BaseWizardPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    protected Wizard createWizard() {
        return Wizard.createByComponentId(driver, wait, getWizardId());
    }

    @Step("Fill element name")
    public void fillName(String windowId, String elementName) {
        createWizard().setComponentValue(windowId, elementName, Input.ComponentType.TEXT_FIELD);
    }

    @Step("Click Save")
    public void clickSave() {
        createWizard().clickSave();
        log.info("Clicking Save button");
    }
}

package com.oss.pages.dpe.qvp;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class QVPPrompt extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(QVPPrompt.class);
    private static final String LINK_INPUT_ID = "indicatorsViewUrl";
    private static final String NAME_INPUT_ID = "name";
    private static final String WIZARD_ID = "_wizard";
    private final Wizard promptWizard;

    public QVPPrompt(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        promptWizard = Wizard.createByComponentId(driver, wait, WIZARD_ID);
    }

    @Step("Set link to Indicators View")
    public void setLinkToIndicatorsView(String link) {
        promptWizard.setComponentValue(LINK_INPUT_ID, link);
        log.info("Setting link to Indicators View: {}", link);
    }

    @Step("Set QVP Name")
    public void setQVPName(String name) {
        promptWizard.setComponentValue(NAME_INPUT_ID, name);
        log.info("Setting Name: {}", name);
    }

    @Step("Click Accept")
    public void clickAccept() {
        promptWizard.clickAccept();
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Clicking Accept in prompt QVP Wizard");
    }
}

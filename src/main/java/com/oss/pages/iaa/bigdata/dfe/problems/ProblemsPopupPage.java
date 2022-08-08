package com.oss.pages.iaa.bigdata.dfe.problems;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class ProblemsPopupPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(ProblemsPopupPage.class);
    private static final String NAME_INPUT_ID = "name";
    private static final String DESCRIPTION_INPUT_ID = "description";

    private final Wizard problemsWizard;

    public ProblemsPopupPage(WebDriver driver, WebDriverWait wait, String wizardTestId) {
        super(driver, wait);
        problemsWizard = Wizard.createByComponentId(driver, wait, wizardTestId);
    }

    public void fillName(String name) {
        problemsWizard.setComponentValue(NAME_INPUT_ID, name);
        log.debug("Setting name with: {}", name);
    }

    public void fillDescription(String description) {
        problemsWizard.setComponentValue(DESCRIPTION_INPUT_ID, description);
        log.debug("Setting description with: {}", description);
    }

    @Step("I fill Problems Popup fields with name: {name} and description: {description}")
    public void fillProblemsPopup(String name, String description) {
        fillName(name);
        fillDescription(description);
        log.info("Filled Problems Popup fields");
    }

    @Step("I click Save")
    public void clickSave() {
        problemsWizard.clickSave();
        log.info("Finishing by clicking 'Save'");
    }
}
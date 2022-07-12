package com.oss.pages.bigdata.dfe.stepwizard.commons;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class BasicInformationPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(BasicInformationPage.class);

    private static final String CATEGORY_INPUT_ID = "category";
    private static final String NAME_INPUT_ID = "name";
    private static final String PROCESS_ID = "factId";
    private final Wizard basicInfoWizard;

    public BasicInformationPage(WebDriver driver, WebDriverWait wait, String wizardId) {
        super(driver, wait);
        basicInfoWizard = Wizard.createByComponentId(driver, wait, wizardId);
    }

    public void fillCategory(String category) {
        basicInfoWizard.setComponentValue(CATEGORY_INPUT_ID, category);
        log.debug("Setting category with: {}", category);
    }

    public void fillName(String name) {
        basicInfoWizard.setComponentValue(NAME_INPUT_ID, name);
        log.debug("Setting name with: {}", name);
    }

    public void fillProcess(String process) {
        basicInfoWizard.setComponentValue(PROCESS_ID, process);
        log.debug("Setting process with: {}", process);
    }

    @Step("I click Next Step")
    public void clickNextStep() {

        basicInfoWizard.clickNextStep();

        log.info("I click Next Step");
    }
}

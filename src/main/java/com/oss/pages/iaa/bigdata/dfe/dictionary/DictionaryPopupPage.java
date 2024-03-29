package com.oss.pages.iaa.bigdata.dfe.dictionary;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class DictionaryPopupPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(DictionaryPopupPage.class);
    private static final String NAME_INPUT_ID = "name";
    private static final String DESCRIPTION_INPUT_ID = "description";
    private static final String CATEGORY_INPUT_ID = "category";
    private final Wizard dictionaryWizard;

    public DictionaryPopupPage(WebDriver driver, WebDriverWait wait, String wizardId) {
        super(driver, wait);
        dictionaryWizard = Wizard.createByComponentId(driver, wait, wizardId);
    }

    public void fillName(String name) {
        dictionaryWizard.setComponentValue(NAME_INPUT_ID, name);
        log.info("Setting name with: {}", name);
    }

    public void fillDescription(String description) {
        dictionaryWizard.setComponentValue(DESCRIPTION_INPUT_ID, description);
        log.info("Setting description with: {}", description);
    }

    public void fillCategory(String categoryName) {
        dictionaryWizard.setComponentValue(CATEGORY_INPUT_ID, categoryName);
        log.info("Setting category with: {}", categoryName);
    }

    @Step("I fill Dictionary Popup fields with name: {name} and description: {description}")
    public void fillDictionaryPopup(String name, String description, String categoryName) {
        fillName(name);
        fillDescription(description);
        fillCategory(categoryName);
        log.info("Filled Dictionary Popup fields");
    }

    @Step("I click Save")
    public void clickSave() {
        dictionaryWizard.clickSave();
        log.info("Finishing by clicking 'Save'");
    }
}

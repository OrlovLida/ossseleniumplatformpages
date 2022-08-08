package com.oss.pages.iaa.bigdata.dfe.dictionary;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class EntryPopupPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(EntryPopupPage.class);

    private static final String KEY_INPUT_ID = "name";
    private static final String VALUE_INPUT_ID = "value";
    private final Wizard entryWizard;

    public EntryPopupPage(WebDriver driver, WebDriverWait wait, String wizardId) {
        super(driver, wait);
        entryWizard = Wizard.createByComponentId(driver, wait, wizardId);
    }

    public void fillKey(String key) {
        entryWizard.setComponentValue(KEY_INPUT_ID, key);
        log.debug("Setting name with: {}", key);
    }

    public void fillValue(String value) {
        entryWizard.setComponentValue(VALUE_INPUT_ID, value);
        log.debug("Setting description with: {}", value);
    }

    @Step("I fill Entry Popup fields with key: {key} and value with: {value}")
    public void fillEntryPopup(String key, String value) {
        fillKey(key);
        fillValue(value);
        log.info("Filled Entry Popup fields");
    }

    @Step("I click Save")
    public void clickSave() {
        entryWizard.clickSave();
        log.info("Finishing by clicking 'Save'");
    }
}

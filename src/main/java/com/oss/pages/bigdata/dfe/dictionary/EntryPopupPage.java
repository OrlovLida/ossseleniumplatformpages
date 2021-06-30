package com.oss.pages.bigdata.dfe.dictionary;


import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.oss.framework.components.inputs.Input.ComponentType.TEXT_FIELD;

public class EntryPopupPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(EntryPopupPage.class);

    private final String KEY_INPUT_ID = "name";
    private final String VALUE_INPUT_ID = "value";
    private final Wizard entryWizard;

    public EntryPopupPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        entryWizard = Wizard.createWizard(driver, wait);
    }

    public void fillKey(String key) {
        entryWizard.setComponentValue(KEY_INPUT_ID, key, TEXT_FIELD);
        log.debug("Setting name with: {}", key);
    }

    public void fillValue(String value) {
        entryWizard.setComponentValue(VALUE_INPUT_ID, value, TEXT_FIELD);
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

package com.oss.pages.bigdata.dfe.dictionary;

import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.oss.framework.components.inputs.Input.ComponentType.TEXT_FIELD;

public class DictionaryPopupPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(DictionaryPopupPage.class);
    private final String NAME_INPUT_ID = "name";
    private final String DESCRIPTION_INPUT_ID = "description";
    private final Wizard dictionaryWizard;

    public DictionaryPopupPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        dictionaryWizard = Wizard.createWizard(driver, wait);
    }

    public void fillName(String name) {
        dictionaryWizard.setComponentValue(NAME_INPUT_ID, name, TEXT_FIELD);
        log.debug("Setting name with: {}", name);
    }

    public void fillDescription(String description) {
        dictionaryWizard.setComponentValue(DESCRIPTION_INPUT_ID, description, TEXT_FIELD);
        log.debug("Setting name with: {}", description);
    }

    @Step("I fill Dictionary Popup fields with name: {name} and description: {description}")
    public void fillDictionaryPopup(String name, String description) {
        fillName(name);
        fillDescription(description);
        log.info("Filled Dictionary Popup fields");
    }

    @Step("I click Save")
    public void clickSave() {
        dictionaryWizard.clickSave();
        log.info("Finishing by clicking 'Save'");
    }
}

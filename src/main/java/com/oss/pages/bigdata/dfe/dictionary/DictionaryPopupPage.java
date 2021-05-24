package com.oss.pages.bigdata.dfe.dictionary;

import com.oss.framework.utils.DelayUtils;
import com.oss.pages.bigdata.dfe.BasePopupPage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DictionaryPopupPage extends BasePopupPage {

    private static final Logger log = LoggerFactory.getLogger(DictionaryPopupPage.class);

    private final String NAME_INPUT_ID = "name";
    private final String DESCRIPTION_INPUT_ID = "description";


    public DictionaryPopupPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public void fillName(String name) {
        DelayUtils.waitForPageToLoad(driver, wait);
        fillTextField(name, NAME_INPUT_ID);
        log.debug("Setting name with: {}", name);
    }

    public void fillDescription(String description) {
        DelayUtils.waitForPageToLoad(driver, wait);
        fillTextField(description, DESCRIPTION_INPUT_ID);
        log.debug("Setting description with: {}", description);
    }

    @Step("I fill Dictionary Popup fields with name: {name} and description: {description}")
    public void fillDictionaryPopup(String name, String description) {
        fillName(name);
        fillDescription(description);
        log.info("Filled Dictionary Popup fields");
    }

}
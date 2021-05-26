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

    public DictionaryPopupPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("I fill Dictionary Popup fields with name: {name} and description: {description}")
    public void fillDictionaryPopup(String name, String description) {
        fillName(name);
        fillDescription(description);
        log.info("Filled Dictionary Popup fields");
    }
}

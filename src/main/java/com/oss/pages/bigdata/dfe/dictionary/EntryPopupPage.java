package com.oss.pages.bigdata.dfe.dictionary;


import com.oss.framework.utils.DelayUtils;
import com.oss.pages.bigdata.dfe.BasePopupPage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EntryPopupPage extends BasePopupPage {

    private static final Logger log = LoggerFactory.getLogger(EntryPopupPage.class);

    final private String KEY_INPUT_ID = "name";
    final private String VALUE_INPUT_ID = "value";

    public EntryPopupPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }


    public void fillKey(String key){
        DelayUtils.waitForPageToLoad(driver, wait);
        fillTextField(key, KEY_INPUT_ID);
        log.debug("Setting name with: {}", key);
    }

    public void fillValue(String value){
        DelayUtils.waitForPageToLoad(driver, wait);
        fillTextField(value,VALUE_INPUT_ID);
        log.debug("Setting description with: {}", value);
    }

    @Step("I fill Add New Entry field with key: {key} and value with: {value}")
    public void fillAddNewEntry(String key, String value){
       fillKey(key);
       fillValue(value);
        log.info("Filled Add New Entry");
    }

}

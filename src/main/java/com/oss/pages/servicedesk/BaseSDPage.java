package com.oss.pages.servicedesk;

import com.oss.framework.components.inputs.HtmlEditor;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseSDPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(BaseSDPage.class);

    protected BaseSDPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public void openPage(WebDriver driver, String url) {
        driver.get(url);
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Opening page: {}", url);
    }

    public void setValueInHtmlEditor(String value, String componentId) {
        HtmlEditor htmlEditor = HtmlEditor.create(driver, wait, componentId);
        htmlEditor.clear();
        htmlEditor.setSingleStringValue(value);
    }
}
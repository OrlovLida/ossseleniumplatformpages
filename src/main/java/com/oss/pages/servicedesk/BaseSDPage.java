package com.oss.pages.servicedesk;

import com.oss.framework.utils.DelayUtils;
import com.oss.pages.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseSDPage extends BasePage {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseSDPage.class);

    protected BaseSDPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public static void openPage(WebDriver driver, String url, WebDriverWait wait) {
        driver.get(url);
        DelayUtils.waitForPageToLoad(driver, wait);
        LOGGER.info("Opening page: {}", url);
    }

}
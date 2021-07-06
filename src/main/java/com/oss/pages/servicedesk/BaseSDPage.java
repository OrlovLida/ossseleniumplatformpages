package com.oss.pages.servicedesk;

import com.oss.framework.utils.DelayUtils;
import com.oss.pages.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseSDPage extends BasePage {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseSDPage.class);

    protected BaseSDPage(WebDriver driver) {
        super(driver, new WebDriverWait(driver, 45));
    }

    public void openPage(WebDriver driver, String url) {
        driver.get(url);
        DelayUtils.waitForPageToLoad(driver, wait);
        LOGGER.info("Opening page: {}", url);
    }

    public static void openSDPage(WebDriver driver, String basicURL, WebDriverWait wait, String viewName) {
        String pageUrl = String.format("%s/#/views/service-desk/%s", basicURL, viewName);
        driver.get(pageUrl);
        DelayUtils.waitForPageToLoad(driver, wait);
        LOGGER.info("Opening page: {}", pageUrl);
    }

    public static void openDashboardPage(WebDriver driver, String basicURL, WebDriverWait wait, String viewName) {
        String pageUrl = String.format("%s/#/dashboard/predefined/id/%s", basicURL, viewName);
        driver.get(pageUrl);
        DelayUtils.waitForPageToLoad(driver, wait);
        LOGGER.info("Opening page: {}", pageUrl);
    }

}
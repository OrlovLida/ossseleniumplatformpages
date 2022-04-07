package com.oss.nfv.common;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * @author Marzena Tolpa
 */
public class WebDriversData {

    private final WebDriver webDriver;
    private final WebDriverWait webDriverWait;

    private WebDriversData(WebDriver webDriver, WebDriverWait webDriverWait) {
        this.webDriver = webDriver;
        this.webDriverWait = webDriverWait;
    }

    public static WebDriversData create(WebDriver driver, WebDriverWait webDriverWait) {
        return new WebDriversData(driver, webDriverWait);
    }

    public WebDriver getWebDriver() {
        return webDriver;
    }

    public WebDriverWait getWebDriverWait() {
        return webDriverWait;
    }
}

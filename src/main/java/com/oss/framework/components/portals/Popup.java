package com.oss.framework.components.portals;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Popup {

    private static final String POPUP_CSS_SELECTOR = ".OssWindow.newPrompt";
    private final String title = ".//span[@class='windowHeader-title']";

    private final WebDriver driver;
    private final WebDriverWait webDriverWait;
    private final WebElement webElement;

    public static Popup create(WebDriver webDriver, WebDriverWait webDriverWait) {
        return new Popup(webDriver, webDriverWait);
    }

    private Popup(WebDriver webDriver, WebDriverWait webDriverWait) {
        this.driver = webDriver;
        this.webDriverWait = webDriverWait;
        this.webElement = this.driver.findElement(By.cssSelector(POPUP_CSS_SELECTOR));
    }

    public String getPopupTitle(){
        return this.webElement.findElement(By.xpath(title)).getText();
    }
}

package com.oss.pages.servicedesk.ticket.wizard;

import com.oss.framework.components.inputs.Combobox;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

public class StatusComboBox extends Combobox {

    public static final String STATUS_COMBO_BOX_XPATH = "//div[contains(@class, 'most-wanted__inputs')]//div[contains(@class, 'combo-box')]";

    public static StatusComboBox create(WebDriver driver, WebDriverWait webDriverWait) {
        WebElement webElement = driver.findElement(By.xpath(STATUS_COMBO_BOX_XPATH));
        return new StatusComboBox(driver, webDriverWait, webElement);
    }

    private StatusComboBox(WebDriver driver, WebDriverWait webDriverWait, WebElement webElement) {
        super(driver, webDriverWait, webElement);
    }
}

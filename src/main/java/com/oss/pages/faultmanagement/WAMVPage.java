package com.oss.pages.faultmanagement;

import com.oss.framework.utils.DelayUtils;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WAMVPage extends BasePage {

    public WAMVPage(WebDriver driver) {
        super(driver);
    }

    public WAMVPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//span[@class='titleText']")
    private WebElement headerName;

    @Step
    public static WAMVPage initializeWAMV(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, 90);
        return new WAMVPage(driver, wait);
    }

    @Step
    public void countNumberOfAlarms() {
        //TODO
    }

    @Step("I show current name of Alarm List")
    public String getViewHeader() {
        DelayUtils.waitForPageToLoad(driver, wait);
        return headerName.getText();
    }
}

package com.oss.pages.platform;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.oss.framework.utils.DelayUtils;
import com.oss.pages.BasePage;

import static com.oss.configuration.Configuration.CONFIGURATION;

public class LoginPage extends BasePage {

    private String url;

    @FindBy(id = "username")
    private WebElement userInput;

    @FindBy(id = "password")
    private WebElement passwordInput;

    @FindBy(id = "kc-login")
    private WebElement loginButton;

    public LoginPage(WebDriver driver, String url) {
        super(driver);
        this.url = url;
    }

    public LoginPage open() {
        driver.get(url);
        DelayUtils.waitForVisibility(wait,userInput);
        return this;
    }

    public HomePage login() {
        userInput.sendKeys(CONFIGURATION.getValue("user"));
        passwordInput.sendKeys(CONFIGURATION.getValue("password"));
        loginButton.click();
        return new HomePage(driver);
    }

    public void login(String user, String password){
        userInput.sendKeys(user);
        passwordInput.sendKeys(password);
        loginButton.click();
    }
}

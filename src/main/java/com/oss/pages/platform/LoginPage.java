package com.oss.pages.platform;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.oss.framework.utils.DelayUtils;
import com.oss.pages.BasePage;

import static com.oss.configuration.Configuration.CONFIGURATION;

public class LoginPage extends BasePage {

    private final String url;

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
        DelayUtils.waitForVisibility(wait, userInput);
        return this;
    }

    public HomePage login() {
        userInput.sendKeys(CONFIGURATION.getLogin());
        passwordInput.sendKeys(CONFIGURATION.getPassword());
        loginButton.click();
        return new HomePage(driver);
    }

    public void login(String user, String password) {
        DelayUtils.waitForVisibility(wait, userInput);
        userInput.sendKeys(user);
        passwordInput.sendKeys(password);
        loginButton.click();
    }

    public boolean isLoginPageDisplayed() {
        return userInput.isDisplayed() && passwordInput.isDisplayed() && loginButton.isDisplayed();
    }
}

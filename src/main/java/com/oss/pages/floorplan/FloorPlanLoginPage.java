package com.oss.pages.floorplan;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.oss.framework.utils.DelayUtils;
import com.oss.pages.BasePage;

import static com.oss.configuration.Configuration.CONFIGURATION;

public class FloorPlanLoginPage extends BasePage {
    @FindBy(name = "j_username")
    private WebElement userInput;

    @FindBy(name = "j_password")
    private WebElement passwordInput;

    @FindBy(id = "SubmitButton")
    private WebElement loginButton;

    public FloorPlanLoginPage(WebDriver driver) {
        super(driver);
    }

    public FloorPlanPage login() {
        userInput.sendKeys(CONFIGURATION.getValue("user"));
        passwordInput.sendKeys(CONFIGURATION.getValue("password"));
        //TODO use loginButton after OSSWEB-10577
        driver.findElement(By.xpath(".//div[text() = 'Log in']")).click();
        DelayUtils.waitByXPath(wait, ".//div[@class='oss-ribbonpanel']");
        return new FloorPlanPage(driver);
    }
}

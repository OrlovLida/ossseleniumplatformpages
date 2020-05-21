package com.oss.pages.platform;

import com.oss.pages.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginPanelPage extends BasePage {

    public LoginPanelPage(WebDriver driver){super(driver);}

    @FindBy(xpath = "//div[@class = 'login-panel']//input")
    private WebElement languageInput;
    @FindBy(xpath = "//span[@class='switcher-inner']")
    private WebElement alphaModeSwitcher;


    public void changeLanguageForEnglish(){
        languageInput.click();
        driver.findElement(By.xpath("//div[@class='text-wrapper' and contains(text(), 'English')]")).click();
        String confirmButtonPath = "//button[@class='actionButton btn btn-primary']";
        waitForBy(By.xpath(confirmButtonPath));
        driver.findElement(By.xpath(confirmButtonPath)).click();
    }

    public void changeForAlphaMOde(){
        waitForVisibility(alphaModeSwitcher);
        alphaModeSwitcher.click();
    }
}

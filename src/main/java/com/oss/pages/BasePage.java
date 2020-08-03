package com.oss.pages;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.oss.framework.mainheader.UserSettings;
import com.oss.pages.platform.LoginPage;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.utils.DelayUtils;

public class BasePage {
    protected final WebDriver driver;
    public final WebDriverWait wait;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, 45);
        PageFactory.initElements(driver, this);
    }

    public void waitForVisibility(WebElement webelement) {
        wait.ignoring(StaleElementReferenceException.class).until(ExpectedConditions.visibilityOf(webelement));
    }

    protected void waitForVisibility(List<WebElement> webElements) {
        wait.until(ExpectedConditions.visibilityOfAllElements(webElements));
    }

    protected void waitforclickability(WebElement webelement) {
        wait.until(ExpectedConditions.elementToBeClickable(webelement));
    }

    public void waitForInvisibility(WebElement webelement) {
        wait.ignoring(StaleElementReferenceException.class).until(ExpectedConditions.invisibilityOf(webelement));
    }

    public void waitForInvisibilityOfLoadbars() {
        List<WebElement> loadBars = driver.findElements(By.xpath("//div[@class='load-bar']"));
        wait.until(ExpectedConditions.invisibilityOfAllElements(loadBars));
    }

    public void waitForPageToLoad() {
        DelayUtils.sleep(1000);
        List<WebElement> spinners = driver.findElements(By.xpath("//i[contains(@class,'fa-spin')]"));
        List<WebElement> loadBars = driver.findElements(By.xpath("//div[@class='load-bar']"));
        List<WebElement> appPreloader = driver.findElements(By.xpath("//div[contains(@class,'appPreloader')]"));
        List<WebElement> preloaderWrapper = driver.findElements(By.xpath("//div[@class='preloaderWrapper']"));
        List<WebElement> newList = new ArrayList<>(spinners);
        newList.addAll(loadBars);
        newList.addAll(appPreloader);
        newList.addAll(preloaderWrapper);
        while (newList.size() > 0) {
            wait.until(ExpectedConditions.invisibilityOfAllElements(newList));
            spinners = driver.findElements(By.xpath("//i[contains(@class,'fa-spin')]"));
            loadBars = driver.findElements(By.xpath("//div[@class='load-bar']"));
            appPreloader = driver.findElements(By.xpath("//div[contains(@class,'appPreloader')]"));
            preloaderWrapper = driver.findElements(By.xpath("//div[@class='preloaderWrapper']"));
            newList = new ArrayList<>(spinners);
            newList.addAll(loadBars);
            newList.addAll(appPreloader);
            newList.addAll(preloaderWrapper);
        }
    }

    public void waitForComponent(String xpath) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
    }

    public void waitForBy(By by) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    protected String randomInteger(int length) {
        Random rand = new Random();
        String random = "";

        for (int i = 0; i < length; i++) {
            random += rand.nextInt(9);
        }
        return random;
    }

   public void changeUser(String user, String password){
        UserSettings.create(driver,wait).open().logOut();
        new LoginPage(driver, "url").login(user,password);
    }
}

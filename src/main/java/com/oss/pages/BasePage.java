package com.oss.pages;

import java.util.Random;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.mainheader.UserSettings;
import com.oss.pages.platform.LoginPage;

public class BasePage {
    protected final WebDriver driver;
    public final WebDriverWait wait;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, 45);
        PageFactory.initElements(driver, this);
    }

    protected String randomInteger(int length) {
        Random rand = new Random();
        String random = "";

        for (int i = 0; i < length; i++) {
            random += rand.nextInt(9);
        }
        return random;
    }

    public void changeUser(String user, String password) {
        UserSettings.create(driver, wait).open().logOut();
        new LoginPage(driver, "url").login(user, password);
    }
}

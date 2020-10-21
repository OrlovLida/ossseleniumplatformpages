package com.oss.pages;

import java.util.Random;

import com.oss.framework.mainheader.PerspectiveChooser;
import com.oss.framework.mainheader.ToolbarWidget;
import com.oss.framework.sidemenu.SideMenu;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.platform.*;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.mainheader.UserSettings;

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

    public LoginPanelPage openLoginPanel(){
        ToolbarWidget.create(driver, wait).openLoginPanel();
      return new LoginPanelPage(driver);
    }

    public NotificationWrapperPage openNotificationPanel(){
        ToolbarWidget.create(driver, wait).openNotificationPanel();
        return new NotificationWrapperPage(driver);
    }

    public PerspectiveChooserPage openPerspectiveChooser(){
        ToolbarWidget.create(driver, wait).openQueryContextContainer();
        return new PerspectiveChooserPage(driver);
    }

    @Step("Type object name in 'Search in OSS objects' field")
    public GlobalSearchPage searchInGlobalSearch(String value) {
        ToolbarWidget globalSearchInput = ToolbarWidget.create(driver, wait);
        globalSearchInput.typeAndEnterInGlobalSearch(value);
        return new GlobalSearchPage(driver);
    }

    @Step("Choose option from Left Side Menu")
    public void chooseFromLeftSideMenu(String actionLabel, String... path) {
        SideMenu sideMenu = SideMenu.create(driver, wait);
        sideMenu.callActionByLabel(actionLabel, path);
    }

}

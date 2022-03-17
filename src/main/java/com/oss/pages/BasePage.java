package com.oss.pages;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Random;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.mainheader.LoginPanel;
import com.oss.framework.components.mainheader.ToolbarWidget;
import com.oss.framework.navigation.sidemenu.SideMenu;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.platform.GlobalSearchPage;
import com.oss.pages.platform.LoginPage;
import com.oss.pages.platform.LoginPanelPage;
import com.oss.pages.platform.NotificationWrapperPage;
import com.oss.pages.platform.PerspectiveChooserPage;

import io.qameta.allure.Step;

public class BasePage {
    protected final WebDriver driver;
    protected final WebDriverWait wait;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, 45);
        PageFactory.initElements(driver, this);
    }

    public BasePage(WebDriver driver, WebDriverWait wait){
        this.driver = driver;
        this.wait = wait;
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
        LoginPanel.create(driver, wait).open().logOut();
        new LoginPage(driver, "url").login(user, password);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public void chooseGroupContext(String groupName) {
        LoginPanel.create(driver, wait).chooseGroupContext(groupName);
    }

    public LoginPanelPage openLoginPanel() {
        ToolbarWidget.create(driver, wait).openLoginPanel();
        return new LoginPanelPage(driver);
    }

    public void closeLoginPanel() {
        ToolbarWidget.create(driver, wait).closeLoginPanel();
    }

    public NotificationWrapperPage openNotificationPanel() {
        ToolbarWidget.create(driver, wait).openNotificationPanel();
        return new NotificationWrapperPage(driver);
    }

    public PerspectiveChooserPage openPerspectiveChooser() {
        ToolbarWidget.create(driver, wait).openQueryContextContainer();
        return new PerspectiveChooserPage(driver);
    }

    @Step("Type object name in 'Search in OSS objects' field")
    public GlobalSearchPage searchInGlobalSearch(String value) {
        ToolbarWidget globalSearchInput = ToolbarWidget.create(driver, wait);
        globalSearchInput.typeAndEnterInGlobalSearch(value);
        return new GlobalSearchPage(driver);
    }

    @Step("Choose {actionLabel} from Left Side Menu")
    public void chooseFromLeftSideMenu(String actionLabel, String... path) {
        SideMenu sideMenu = SideMenu.create(driver, wait);
        sideMenu.callActionByLabel(actionLabel, path);
    }

    @Step("Get Absolute Path")
    public String getAbsolutePath(String path) {
        try {
            URL res = getClass().getClassLoader().getResource(path);
            File file = Paths.get(res.toURI()).toFile();
            return file.getAbsolutePath();
        } catch (URISyntaxException e) {
            throw new RuntimeException("Cannot find file", e);
        }
    }
}

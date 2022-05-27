package com.oss.pages.platform;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.mainheader.Notifications;
import com.oss.framework.components.mainheader.ToolbarWidget;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class NotificationWrapperPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(NotificationWrapperPage.class);

    public NotificationWrapperPage(WebDriver driver) {
        super(driver);
    }

    @Step("Clear Notifications")
    public NotificationWrapperPage clearNotifications() {
        Notifications.create(driver, wait).clearAllNotification();
        DelayUtils.waitForPageToLoad(driver, wait);
        return this;
    }

    @Step("Wait for Export to Finish")
    public NotificationWrapperPage waitForExportFinish() {
        DelayUtils.waitByXPath(wait, "//a[contains (text(), 'Download file')]");
        return this;
    }

    public String waitAndGetFinishedNotificationText() {
        return Notifications.create(driver, wait).getNotificationMessage();
    }

    public int amountOfNotifications() {
        return Notifications.create(driver, wait).countNotifications();
    }

    @Step("Close Notification Panel")
    public BasePage close() {
        ToolbarWidget.create(driver, wait).closeNotificationPanel();
        return new BasePage(driver);
    }

    @Step("Click download file")
    public void clickDownload() {
        Notifications.create(driver, wait).clickDownloadFile();
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Clicking download file");
    }
}

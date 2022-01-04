package com.oss.pages.platform;

import org.openqa.selenium.WebDriver;

import com.oss.framework.mainheader.Notifications;
import com.oss.framework.mainheader.ToolbarWidget;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.BasePage;

public class NotificationWrapperPage extends BasePage {

    public NotificationWrapperPage(WebDriver driver) {
        super(driver);
    }

    public NotificationWrapperPage clearNotifications() {
        Notifications.create(driver, wait).clearAllNotification();
        return this;
    }

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

    public BasePage close() {
        ToolbarWidget.create(driver, wait).closeNotificationPanel();
        return new BasePage(driver);
    }
}

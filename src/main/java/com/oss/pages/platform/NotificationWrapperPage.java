package com.oss.pages.platform;

import com.oss.framework.mainheader.Notifications;
import com.oss.framework.mainheader.ToolbarWidget;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.BasePage;
import org.openqa.selenium.WebDriver;

public class NotificationWrapperPage extends BasePage {

    public NotificationWrapperPage(WebDriver driver){super(driver);}

    public NotificationWrapperPage clearNotifications(){
        Notifications.create(driver, wait).clearAllNotification();
        return this;
    }

    public NotificationWrapperPage waitForExportFinish(){
        DelayUtils.waitForComponent(wait,"//a[contains (text(), 'Download file')]");
        return this;
    }

    public String waitAndGetFinishedNotificationText(){
        return Notifications.create(driver, wait).waitAndGetFinishedNotificationText();
    }

    public int amountOfNotifications(){
        return Notifications.create(driver, wait).getAmountOfNotifications();
    }

    public BasePage close(){
        ToolbarWidget.create(driver, wait).closeNotificationPanel();
        return new BasePage(driver);
    }
}

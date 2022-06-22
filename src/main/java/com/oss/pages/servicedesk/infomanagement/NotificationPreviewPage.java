package com.oss.pages.servicedesk.infomanagement;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.iaa.widgets.components.NotificationPreview;
import com.oss.pages.servicedesk.BaseSDPage;

import io.qameta.allure.Step;

public class NotificationPreviewPage extends BaseSDPage {

    private final NotificationPreview notificationPreview;
    private static final String NOTIFICATION_PREVIEW_ID = "notification-preview";

    public NotificationPreviewPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        notificationPreview = NotificationPreview.createById(driver, wait, NOTIFICATION_PREVIEW_ID);
    }

    @Step("Check if New Badge is present")
    public boolean isNewBadgeVisible() {
        return notificationPreview.isNewBadgePresent();
    }

    @Step("Get Message text")
    public String getMessageText() {
        return notificationPreview.getText();
    }

    @Step("Get Channel text")
    public String getMessageChannel() {
        return notificationPreview.getChannelText();
    }

    @Step("Get status aria label")
    public String getStatusLabel() {
        return notificationPreview.getStatusLabel();
    }
}

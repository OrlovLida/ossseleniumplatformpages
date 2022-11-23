package com.oss.pages.iaa.servicedesk.infomanagement;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.prompts.ConfirmationBox;
import com.oss.framework.iaa.widgets.components.NotificationPreview;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.table.TableWidget;
import com.oss.pages.iaa.servicedesk.BaseSDPage;
import com.oss.pages.iaa.servicedesk.issue.wizard.SDWizardPage;

import io.qameta.allure.Step;

public class NotificationPreviewPage extends BaseSDPage {

    private final NotificationPreview notificationPreview;
    private static final String NOTIFICATION_PREVIEW_ID = "notification-preview";
    private static final String CREATE_OBJECT_ID = "create-context";
    private static final String CREATE_OBJECT_PROMPT_ID = "notification-create-context-object-wizard-view_prompt-card";
    private static final String PROCESS_NOTIFICATION_ID = "process";
    private static final String PROCESS_NOTIFICATION_PROMPT_ID = "notification-process-wizard-view_prompt-card";
    private static final String CONFIRM_PROCESS_NOTIFICATION_BUTTON_LABEL = "Process Notification";
    private static final String CREATE_MESSAGE_WIZARD_ID = "notification-wizard_prompt-card";
    private static final String REPLY_ID = "reply";

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

    @Step("Create Object from notification")
    public SDWizardPage clickCreateObject() {
        notificationPreview.getActionsContainer().callActionById(CREATE_OBJECT_ID);
        return new SDWizardPage(driver, wait, CREATE_OBJECT_PROMPT_ID);
    }

    @Step("Process notification")
    public SDWizardPage clickProcessNotification() {
        notificationPreview.getActionsContainer().callActionById(PROCESS_NOTIFICATION_ID);
        return new SDWizardPage(driver, wait, PROCESS_NOTIFICATION_PROMPT_ID);
    }

    @Step("Click confirm Process Notification")
    public void clickConfirmProcessNotification() {
        ConfirmationBox.create(driver, wait).clickButtonByLabel(CONFIRM_PROCESS_NOTIFICATION_BUTTON_LABEL);
        log.info("Clicking confirm Process Notification");
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("Click Reply button")
    public SDWizardPage clickReply() {
        notificationPreview.getActionsContainer().callActionById(REPLY_ID);
        log.info("Clicking Reply button");
        return new SDWizardPage(driver, wait, CREATE_MESSAGE_WIZARD_ID);
    }
}

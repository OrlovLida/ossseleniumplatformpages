package com.oss.pages.servicedesk.ticket.tabs;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.inputs.Button;
import com.oss.framework.listwidget.iaa.MessageListWidget;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.servicedesk.BaseSDPage;

import io.qameta.allure.Step;

public class MessagesTab extends BaseSDPage {

    private static final Logger log = LoggerFactory.getLogger(MessagesTab.class);

    public static final String CREATE_NEW_COMMENT_BUTTON_TEXT = "Create New Comment";
    private static final String CREATE_BUTTON_LABEL = "CREATE";
    private static final String COMMENT_EDITOR_ID = "new-comment-editor";
    private static final String CREATE_NEW_NOTIFICATION_BUTTON_LABEL = "Create New Notification";

    public MessagesTab(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("Click create new comment button")
    public void clickCreateNewCommentButton() {
        DelayUtils.waitForPageToLoad(driver, wait);
        MessageListWidget.create(driver, wait).clickButtonByLabel(CREATE_NEW_COMMENT_BUTTON_TEXT);
        log.info("Click create new comment");
    }

    @Step("Click create in comment editor")
    public void clickCreateCommentButton() {
        Button.create(driver, CREATE_BUTTON_LABEL, "a").click();
        log.info("Click create comment button");
    }

    @Step("Entering comment message")
    public void enterCommentMessage(String message) {
        setValueInHtmlEditor(message, COMMENT_EDITOR_ID);
        log.info("Comment message is entered");
    }

    @Step("Create new notification on Messages Tab")
    public void createNewNotificationOnMessagesTab() {
        MessageListWidget.create(driver, wait).clickButtonByLabel(CREATE_NEW_NOTIFICATION_BUTTON_LABEL);
    }

    @Step("Check if Messages Tab is Empty")
    public boolean isMessagesTabEmpty() {
        log.info("Check if Messages Tab is Empty");
        return MessageListWidget.create(driver, wait).hasNoData();
    }

    @Step("Check Text in Message")
    public String getMessageText(int messageIndex) {
        return MessageListWidget.create(driver, wait).getMessageItems().get(messageIndex).getMessageText();
    }

    @Step("Check type of message")
    public String checkMessageType(int messageIndex) {
        return MessageListWidget.create(driver, wait).getMessageItems().get(messageIndex).getMessageType();
    }

    @Step("Check Badges in message")
    public String getBadgeTextFromMessage(int messageIndex, int budgeIndex) {
        MessageListWidget.MessageItem message = MessageListWidget.create(driver, wait)
                .getMessageItems()
                .get(messageIndex);
        int budgesNumber = message.getBadgesNumber();
        if (budgeIndex < budgesNumber) {
            log.debug("Badge of message with index {} and budgeIndex {} is: {}", messageIndex, budgeIndex, message.getBadgeText(budgeIndex));
            return message.getBadgeText(budgeIndex);
        }
        log.info("No budge for message with index {}, and budge index {}", messageIndex, budgeIndex);
        return "";
    }

    @Step("Check Comment type")
    public String checkCommentType(int messageIndex) {
        return MessageListWidget.create(driver, wait).getMessageItems().get(messageIndex).getCommentType();
    }
}

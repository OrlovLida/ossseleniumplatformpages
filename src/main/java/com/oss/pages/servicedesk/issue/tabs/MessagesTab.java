package com.oss.pages.servicedesk.issue.tabs;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.inputs.Button;
import com.oss.framework.iaa.widgets.list.MessageListWidget;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.tabs.TabsWidget;
import com.oss.pages.servicedesk.BaseSDPage;

import io.qameta.allure.Step;

import static com.oss.pages.servicedesk.ServiceDeskConstants.TABS_WIDGET_ID;

public class MessagesTab extends BaseSDPage {

    private static final Logger log = LoggerFactory.getLogger(MessagesTab.class);

    public static final String CREATE_NEW_COMMENT_BUTTON_TEXT = "Create New Comment";
    private static final String CREATE_BUTTON_LABEL = "CREATE";
    private static final String COMMENT_EDITOR_ID = "new-comment-editor";
    private static final String CREATE_NEW_NOTIFICATION_BUTTON_LABEL = "Create New Notification";
    private static final String MARK_AS_IMPORTANT_LABEL = "Mark as important";

    public MessagesTab(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("Click create new comment button")
    public void clickCreateNewCommentButton() {
        DelayUtils.waitForPageToLoad(driver, wait);
        getMessageListWidget().clickButtonByLabel(CREATE_NEW_COMMENT_BUTTON_TEXT);
        log.info("Click create new comment");
    }

    @Step("Click create in comment editor")
    public void clickCreateCommentButton() {
        Button.createByLabel(driver, CREATE_BUTTON_LABEL).click();
        log.info("Click create comment button");
    }

    @Step("Entering comment message")
    public void enterCommentMessage(String message) {
        setValueInHtmlEditor(message, COMMENT_EDITOR_ID);
        log.info("Comment message is entered");
    }

    @Step("Create new notification on Messages Tab")
    public void createNewNotificationOnMessagesTab() {
        getMessageListWidget().clickButtonByLabel(CREATE_NEW_NOTIFICATION_BUTTON_LABEL);
    }

    @Step("Check if Messages Tab is Empty")
    public boolean isMessagesTabEmpty() {
        DelayUtils.sleep(2000);
        log.info("Check if Messages Tab is Empty");
        return getMessageListWidget().hasNoData();
    }

    @Step("Check Text in Message")
    public String getMessageText(int messageIndex) {
        return getMessageListWidget().getMessageItems().get(messageIndex).getMessageText();
    }

    @Step("Check type of message")
    public String checkMessageType(int messageIndex) {
        return getMessageListWidget().getMessageItems().get(messageIndex).getMessageType();
    }

    @Step("Check Badges in message")
    public String getBadgeTextFromMessage(int messageIndex, int badgeIndex) {
        DelayUtils.waitForPageToLoad(driver, wait);
        MessageListWidget.MessageItem message = getMessageListWidget()
                .getMessageItems()
                .get(messageIndex);
        int badgesNumber = message.getBadgesNumber();
        if (badgeIndex < badgesNumber) {
            log.debug("Badge of message with index {} and badgeIndex {} is: {}", messageIndex, badgeIndex, message.getBadgeText(badgeIndex));
            return message.getBadgeText(badgeIndex);
        }
        log.info("No badge for message with index {}, and badge index {}", messageIndex, badgeIndex);
        return "";
    }

    @Step("Check Comment type")
    public String checkCommentType(int messageIndex) {
        return getMessageListWidget().getMessageItems().get(messageIndex).getCommentType();
    }

    @Step("Mark message as important")
    public void markAsImportant(int messageIndex) {
        getMessageListWidget().getMessageItems().get(messageIndex).clickMessageAction(MARK_AS_IMPORTANT_LABEL);
        log.info("Marking message as important");
    }

    private MessageListWidget getMessageListWidget() {
        return TabsWidget.createById(driver, wait, TABS_WIDGET_ID).getMessageListWidget(TABS_WIDGET_ID);
    }
}
package com.oss.pages.servicedesk.ticket.tabs;

import com.oss.framework.components.inputs.Button;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.servicedesk.BaseSDPage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessagesTab extends BaseSDPage {

    private static final Logger log = LoggerFactory.getLogger(MessagesTab.class);

    public static final String CREATE_NEW_COMMENT_BUTTON_TEXT = "Create New Comment";
    private static final String CREATE_BUTTON_LABEL = "CREATE";
    private static final String COMMENT_EDITOR_ID = "comment-editor";

    public MessagesTab(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("Click create new comment button")
    public void clickCreateNewCommentButton() {
        DelayUtils.waitForPageToLoad(driver, wait);
        Button.create(driver, CREATE_NEW_COMMENT_BUTTON_TEXT, "a").click();
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
}

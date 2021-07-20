package com.oss.pages.servicedesk.ticket.tabs;

import com.oss.framework.components.inputs.Button;
import com.oss.framework.data.Data;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.components.inputs.HtmlEditor;
import com.oss.pages.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MessagesTab extends BasePage {

    public static final String CREATE_NEW_COMMENT_BUTTON_TEXT = "Create New Comment";
    private static final String CREATE_BUTTON_LABEL = "CREATE";
    private static final String COMMENT_EDITOR_ID = "comment-editor";

    public MessagesTab(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public void clickCreateNewCommentButton(WebDriver driver) {
        DelayUtils.waitForPageToLoad(driver, wait);
        Button.create(driver, CREATE_NEW_COMMENT_BUTTON_TEXT, "a").click();
    }

    public void clickCreateCommentButton() {
        Button button = Button.create(driver, CREATE_BUTTON_LABEL, "a");
        button.click();
    }

    public void enterCommentMessage(String message) {
        HtmlEditor htmlEditor = HtmlEditor.create(driver, wait, COMMENT_EDITOR_ID);
        htmlEditor.setValue(Data.createSingleData(message));
    }
}

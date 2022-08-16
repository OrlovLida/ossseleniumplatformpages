package com.oss.pages.iaa.servicedesk.issue.tabs;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.iaa.servicedesk.BaseSDPage;

public abstract class NotesTab extends BaseSDPage {
    protected NotesTab(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public void addTextNote(String textNote) {
        getCommentTextFieldComponent().setSingleStringValue(textNote);
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Adding text note: {}", textNote);
    }

    public String getTextMessage() {
        return getCommentTextFieldComponent().getStringValue();
    }

    private Input getCommentTextFieldComponent() {
        return ComponentFactory.create(getTextFieldId(), driver, wait);
    }

    public abstract String getTextFieldId();
}

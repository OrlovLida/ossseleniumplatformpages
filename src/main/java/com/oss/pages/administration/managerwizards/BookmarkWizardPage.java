package com.oss.pages.administration.managerwizards;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.prompts.Popup;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class BookmarkWizardPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(BookmarkWizardPage.class);
    private static final String BOOKMARK_NAME_FIELD_ID = "viewName";
    private static final String BOOKMARK_CATEGORY_ID = "viewCategory";
    private static final String SAVE_BOOKMARK_LABEL = "Save";

    public BookmarkWizardPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("Fill and Accept Bookmark wizard")
    public void fillBookmarkWizard(String bookmarkName, String categoryName) {
        fillBookmarkName(bookmarkName);
        fillBookmarkCategory(categoryName);
        clickSaveBookmark();
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("Fill Bookmark Name")
    private void fillBookmarkName(String bookmarkName) {
        Popup.create(driver, wait).setComponentValue(BOOKMARK_NAME_FIELD_ID, bookmarkName, Input.ComponentType.TEXT_FIELD);
        log.info("I fill bookmark name with: {}", bookmarkName);
    }

    @Step("Fill Bookmark Category")
    private void fillBookmarkCategory(String categoryName) {
        Popup.create(driver, wait).setComponentValue(BOOKMARK_CATEGORY_ID, categoryName, Input.ComponentType.COMBOBOX);
        log.info("I fill category with: {}", categoryName);
    }

    @Step("Click Save Bookmark")
    private void clickSaveBookmark() {
        Popup.create(driver, wait).clickButtonByLabel(SAVE_BOOKMARK_LABEL);
        log.info("I click Save");
    }
}

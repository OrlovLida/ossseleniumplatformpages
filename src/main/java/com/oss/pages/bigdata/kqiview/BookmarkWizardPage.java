package com.oss.pages.bigdata.kqiview;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.portals.PopupV2;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BookmarkWizardPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(BookmarkWizardPage.class);
    private static final String BOOKMARK_NAME_FIELD_ID = "viewName";
    private static final String BOOKMARK_CATEGORY_ID = "viewCategory";
    private static final String SAVE_BOOKMARK_LABEL = "Save";

    public BookmarkWizardPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public void fillBookmarkName(String bookmarkName) {
        PopupV2.create(driver, wait).setComponentValue(BOOKMARK_NAME_FIELD_ID, bookmarkName, Input.ComponentType.TEXT_FIELD);
        log.info("I fill bookmark name with: {}", bookmarkName);
    }

    public void fillBookmarkCategory(String categoryName) {
        PopupV2.create(driver, wait).setComponentValue(BOOKMARK_CATEGORY_ID, categoryName, Input.ComponentType.COMBOBOX);
        log.info("I fill category with: {}", categoryName);
    }

    public void clickSaveBookmark() {
        PopupV2.create(driver, wait).clickButtonByLabel(SAVE_BOOKMARK_LABEL);
        log.info("I click Save");
    }

    @Step("i fill Save Bookmark wizard")
    public void fillBookmarkWizard(String bookmarkName, String categoryName) {
        fillBookmarkName(bookmarkName);
        fillBookmarkCategory(categoryName);
        clickSaveBookmark();
    }
}

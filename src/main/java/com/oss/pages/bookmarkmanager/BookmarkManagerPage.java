package com.oss.pages.bookmarkmanager;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.contextactions.ButtonContainer;
import com.oss.framework.components.inputs.Button;
import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.list.CommonList;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class BookmarkManagerPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(BookmarkManagerPage.class);
    private static final String SEARCH_FIELD_ID = "bookmarkSearch";
    private static final String LIST_ID = "bookmarksList";
    private static final String ROW_ATTRIBUTE_NAME = "Name";
    private static final String LINK_TEXT = "Open";
    private static final String CREATE_NEW_CATEGORY_LABEL = "New category";
    private static final String DELETE_LABEL = "Delete";
    private static final String CONFIRM_DELETE_BUTTON_ID = "ConfirmationBox_removeBookmarkConfirmationBox_action_button";

    public BookmarkManagerPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("Go to Bookmark Manager page")
    public static BookmarkManagerPage goToPage(WebDriver driver, String basicURL) {
        WebDriverWait wait = new WebDriverWait(driver, 45);

        String pageUrl = String.format("%s/#/view/bookmarkmanager/main", basicURL);
        driver.get(pageUrl);
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Opened page: {}", pageUrl);

        return new BookmarkManagerPage(driver, wait);
    }

    @Step("Search for bookmark")
    public void searchForBookmark(String bookmarkName) {
        ComponentFactory.create(SEARCH_FIELD_ID, Input.ComponentType.SEARCH_BOX, driver, wait)
                .setSingleStringValue(bookmarkName);
        DelayUtils.waitForPageToLoad(driver, wait);
        log.debug("Searching bookmark {}", bookmarkName);
    }

    @Step("Expand found category")
    public void expandBookmarkList(String categoryName) {
        getBookmarkList().expandCategory(categoryName);
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("I expanded category: {}", categoryName);
    }

    @Step("Open bookmark")
    public void openBookmark(String bookmarkName) {
        getBookmarkList().getRow(ROW_ATTRIBUTE_NAME, bookmarkName).clickLink(LINK_TEXT);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("I check if list contains any bookmarks")
    public boolean isAnyBookmarkInList() {
        DelayUtils.waitForPageToLoad(driver, wait);
        return !getBookmarkList().hasNoData();
    }

    @Step("Click Create New Category Button")
    public void clickCreateNewCategory() {
        ButtonContainer.create(driver, wait).callActionByLabel(CREATE_NEW_CATEGORY_LABEL);
        log.info("Clicking Create New Category");
    }

    @Step("Click Delete Bookmark")
    public void clickDeleteBookmark(String bookmarkName) {
        getBookmarkList().getRow(ROW_ATTRIBUTE_NAME, bookmarkName).callActionByLabel(DELETE_LABEL);
        DelayUtils.waitForPageToLoad(driver, wait);
        Button.createById(driver, CONFIRM_DELETE_BUTTON_ID).click();
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("Check if bookmark is deleted")
    public boolean isBookmarkDeleted(String bookmarkName) {
        return !getBookmarkList().isRowDisplayed(ROW_ATTRIBUTE_NAME, bookmarkName);
    }

    private CommonList getBookmarkList() {
        return CommonList.create(driver, wait, LIST_ID);
    }
}

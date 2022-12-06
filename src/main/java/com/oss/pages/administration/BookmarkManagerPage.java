package com.oss.pages.administration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.utils.DelayUtils;

import io.qameta.allure.Step;

import java.time.Duration;

public class BookmarkManagerPage extends BaseManagerPage {

    private static final Logger log = LoggerFactory.getLogger(BookmarkManagerPage.class);
    private static final String SEARCH_FIELD_ID = "bookmarkSearch";
    private static final String LIST_ID = "bookmarksList";
    private static final String CREATE_NEW_CATEGORY_LABEL = "New category";
    private static final String CONFIRM_DELETE_BUTTON_ID = "ConfirmationBox_removeBookmarkConfirmationBox_action_button";

    public BookmarkManagerPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("Go to Bookmark Manager page")
    public static BookmarkManagerPage goToPage(WebDriver driver, String basicURL) {
        WebDriverWait wait = new WebDriverWait(driver,  Duration.ofSeconds(45));
        String pageUrl = String.format("%s/#/view/bookmarkmanager/main", basicURL);
        driver.get(pageUrl);
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Opened page: {}", pageUrl);
        return new BookmarkManagerPage(driver, wait);
    }

    @Step("Search for bookmark")
    public void searchForBookmark(String bookmarkName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        searchInList(SEARCH_FIELD_ID, bookmarkName);
        log.info("Searching bookmark {}", bookmarkName);
    }

    @Step("Expand found category")
    public void expandBookmarkList(String categoryName) {
        getList().expandCategory(categoryName);
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("I expanded category: {}", categoryName);
    }

    @Step("Open bookmark")
    public void openBookmark(String bookmarkName) {
        openListElement(bookmarkName);
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Opening bookmark {}", bookmarkName);
    }

    @Step("I check if list contains any bookmarks")
    public boolean isAnyBookmarkInList() {
        return isAnyElementOnList();
    }

    @Step("Click Create New Category Button")
    public void clickCreateNewCategory() {
        clickCreateNewElement(CREATE_NEW_CATEGORY_LABEL);
        log.info("Click Create New Category");
    }

    @Step("Click Delete Bookmark")
    public void clickDeleteBookmark(String bookmarkName) {
        clickDeleteElementOnList(bookmarkName);
        confirmDeleteElement(CONFIRM_DELETE_BUTTON_ID);
        log.info("Click delete Bookmark");
    }

    @Step("Check if bookmark is deleted")
    public boolean isBookmarkDeleted(String bookmarkName) {
        log.info("Checking if bookmark is deleted");
        return isElementDeleted(bookmarkName);
    }

    @Override
    public String getListId() {
        return LIST_ID;
    }
}

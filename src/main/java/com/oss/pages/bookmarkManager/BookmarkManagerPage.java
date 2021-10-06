package com.oss.pages.bookmarkManager;

import com.oss.framework.components.contextactions.ButtonContainer;
import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.inputs.SearchField;
import com.oss.framework.listwidget.CommonList;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BookmarkManagerPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(BookmarkManagerPage.class);
    private final String SEARCH_FIELD_ID = "bookmarkSearch";
    private final String LIST_ID = "bookmarksList";
    private final String ROW_ATTRIBUTE_NAME = "Name";
    private final String LINK_TEXT = "Open";
    private final String CREATE_NEW_CATEGORY_LABEL = "New category";


    public BookmarkManagerPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("I go to Bookmark Manager page")
    public static BookmarkManagerPage goToPage(WebDriver driver, String basicURL) {
        WebDriverWait wait = new WebDriverWait(driver, 45);

        String pageUrl = String.format("%s/#/view/bookmarkmanager/main", basicURL);
        driver.get(pageUrl);
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Opened page: {}", pageUrl);

        return new BookmarkManagerPage(driver, wait);
    }

    @Step("I search for bookmark")
    public void searchForBookmark(String bookmarkName) {
        SearchField search = (SearchField) ComponentFactory.create(SEARCH_FIELD_ID, Input.ComponentType.SEARCH_FIELD, driver, wait);
        search.clear();
        search.typeValue(bookmarkName);
        log.debug("Searching bookmark {}", bookmarkName);
    }

    @Step("I expand found category")
    public void expandBookmarkList(String categoryName) {
        CommonList.create(driver, wait, LIST_ID).clickOnCategoryByName(categoryName);
        log.info("I expanded category: {}", categoryName);
    }

    @Step("I open bookmark")
    public void openBookmark(String bookmarkName) {
        CommonList.create(driver, wait, LIST_ID).getRow(ROW_ATTRIBUTE_NAME, bookmarkName).clickOnLink(LINK_TEXT);
    }

    @Step("I check if list contains any bookmarks")
    public boolean isAnyBookmarkInList() {
        DelayUtils.waitForPageToLoad(driver, wait);
        return !CommonList.create(driver, wait, LIST_ID).isNoData();
    }

    public void clickCreateNewCategory() {
        ButtonContainer.create(driver, wait).callActionByLabel(CREATE_NEW_CATEGORY_LABEL);
    }
}

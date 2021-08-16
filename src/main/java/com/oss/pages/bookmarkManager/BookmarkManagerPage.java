package com.oss.pages.bookmarkManager;

import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.inputs.SearchField;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BookmarkManagerPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(BookmarkManagerPage.class);
    private final String SEARCH_FIELD_ID = "";

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

    protected void searchFeed(String searchText) {
        SearchField search = (SearchField) ComponentFactory.create("bookmarkSearch", Input.ComponentType.SEARCH_FIELD, driver, wait);
        search.clear();
        search.typeValue(searchText);
        log.debug("Searching feed {}", searchText);
    }


    @Step("I search for bookmark")
    public void searchForBookmark(String bookmarkName) {
        searchFeed(bookmarkName);

    }
}

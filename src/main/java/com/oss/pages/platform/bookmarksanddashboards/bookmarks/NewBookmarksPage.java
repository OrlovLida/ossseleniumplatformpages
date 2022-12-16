package com.oss.pages.platform.bookmarksanddashboards.bookmarks;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.prompts.Popup;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.tabs.TabsWidget;
import com.oss.pages.platform.bookmarksanddashboards.BaseBookmarkAndDashboardPage;
import com.oss.pages.platform.bookmarksanddashboards.CategoryWizardPage;

public class NewBookmarksPage extends BaseBookmarkAndDashboardPage {

    public static final String ACTION_CATEGORY = "action_createCategoryBookmark";
    public static final String ACTION_SHARE_CATEGORY = "ShareFolderAction";
    public static final String MANAGEMENT_VIEW_CONTAINER_TABSCARD = "management-view__container__tabscard";

    public static final String ACTION_EDIT_BOOKMARK_FOLDER = "action_editBookmarkFolder";
    public static final String ACTION_DELETE_BOOKMARK_FOLDER = "action_deleteBookmarkFolder";
    public static final String ACTION_DELETE_BOOKMARK = "action_deleteBookmark";
    public static final String EDIT_BOOKMARK_ACTION = "action_editBookmark";
    public static final String DELETE = "Delete";

    public static final String ID = "id";
    public static final String TABLE_ID = "tab_bookmark_manager_widget";
    private static final String TAB_BOOKMARKS_ID = "tab_bookmarks";
    private static final String SAVE_BOOKMARK_BUTTON_ID = "saveBookmarkBtn";
    private static final String ACCEPT_BUTTON_ID = "wizard-submit-button-share-widget-id";

    protected NewBookmarksPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public static NewBookmarksPage goToBookmarksPage(WebDriver driver, WebDriverWait wait, String basicUrl) {
        driver.get(String.format("%s/#/", basicUrl));
        DelayUtils.waitForPageToLoad(driver, wait);
        TabsWidget tabs = TabsWidget.createById(driver, wait, MANAGEMENT_VIEW_CONTAINER_TABSCARD);
        tabs.selectTabById(TAB_BOOKMARKS_ID);
        return new NewBookmarksPage(driver, wait);
    }

    public void createCategory(String categoryName, String description) {
        callActionById(ACTION_CATEGORY);
        CategoryWizardPage categoryWizard = new CategoryWizardPage(driver, wait);
        categoryWizard.setName(categoryName);
        categoryWizard.setDescription(description);
        categoryWizard.clickButton(SAVE_BOOKMARK_BUTTON_ID);
    }

    public void editCategory(String categoryName, String newCategoryName, String newDescription) {
        selectObject(categoryName);
        callActionById(ACTION_EDIT_BOOKMARK_FOLDER);
        CategoryWizardPage categoryWizard = new CategoryWizardPage(driver, wait);
        categoryWizard.setName(newCategoryName);
        categoryWizard.setDescription(newDescription);
        categoryWizard.clickButton(SAVE_BOOKMARK_BUTTON_ID);

    }

    public void deleteCategory(String categoryName) {
        selectObject(categoryName);
        callActionById(ACTION_DELETE_BOOKMARK_FOLDER);
        Popup popup = Popup.create(driver, wait);
        popup.clickButtonByLabel(DELETE);
    }

    public void editBookmark(String bookmarkName, String newBookmarkName, String newDescription) {
        BookmarkWizardPage bookmarkWizardPage = new BookmarkWizardPage(driver, wait);
        selectObject(bookmarkName);
        callActionById(EDIT_BOOKMARK_ACTION);
        bookmarkWizardPage.setName(newBookmarkName);
        bookmarkWizardPage.setDescription(newDescription);
        bookmarkWizardPage.clickSave();
    }

    public void deleteBookmark(String bookmarkName) {
        selectObject(bookmarkName);
        callActionById(ACTION_DELETE_BOOKMARK);
        Popup popup = Popup.create(driver, wait);
        popup.clickButtonByLabel(DELETE);
    }

    public void openBookmark(String nameBookmark) {
        clickLinkToView(nameBookmark);

    }

    @Override
    public String getTreeTableId() {
        return TABLE_ID;
    }

    @Override
    public String getTabId() {
        return TAB_BOOKMARKS_ID;
    }

}
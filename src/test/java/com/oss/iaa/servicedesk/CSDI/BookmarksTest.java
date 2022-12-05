package com.oss.iaa.servicedesk.CSDI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.components.layout.ErrorCard;
import com.oss.framework.components.mainheader.ButtonPanel;
import com.oss.framework.navigation.toolsmanager.ToolsManagerWindow;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.tabs.TabsWidget;
import com.oss.pages.iaa.servicedesk.issue.ticket.TicketSearchPage;
import com.oss.pages.platform.bookmarksanddashboards.bookmarks.BookmarkWizardPage;
import com.oss.pages.platform.bookmarksanddashboards.bookmarks.NewBookmarksPage;

import io.qameta.allure.Description;

public class BookmarksTest extends BaseTestCase {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookmarksTest.class);

    private static final String TAB_TOOLS_ID = "tab_tab_tools";
    private static final String BOOKMARKS_PAGE = "Bookmarks";
    private static final String CATEGORY_NAME = "Selenium Category";
    private static final String DESCRIPTION_CATEGORY = "Selenium Test";
    private static final String BOOKMARK_NAME = "SeleniumBookmark";
    private static final String CATEGORY = "Incident Management";
    private static final String VIEW_NAME = "Tickets Search";
    private static final String BOOKMARK_DESCRIPTION = "Selenium test";
    private static final String UPDATED_BOOKMARK_NAME = "SeleniumBookmarkUpdated";
    private static final String UPDATED_BOOKMARK_DESCRIPTION = "Selenium test updated";
    private static final String SEVERITY_COMBOBOX_ID = "ticketOut.issueOut.severity";
    private static final String SEVERITY_TYPE = "Minor";
    private static final String BUTTON_SAVE_BOOKMARK = "ButtonSaveBookmark";
    private static final String MANAGEMENT_VIEW_CONTAINER_TABSCARD = "management-view__container__tabscard";

    NewBookmarksPage bookmarksPage;

    @BeforeMethod
    public void goToBookmarksView() {
        bookmarksPage = NewBookmarksPage.goToBookmarksPage(driver, webDriverWait, BASIC_URL);
    }

    @Test(priority = 1, testName = "Create Category", description = "Create Category")
    @Description("Create Category")
    public void createCategory() {
        bookmarksPage.createCategory(CATEGORY_NAME, DESCRIPTION_CATEGORY);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        checkErrorPage(BOOKMARKS_PAGE);
        Assert.assertTrue(bookmarksPage.isObjectPresent(CATEGORY_NAME));
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertEquals(bookmarksPage.getDescription(CATEGORY_NAME), DESCRIPTION_CATEGORY);
    }

    @Test(priority = 2, testName = "Open Ticket Search and and Save Bookmarks", description = "Open Ticket Search and Save Bookmarks")
    @Description("Open Ticket Search and Save Bookmarks")
    public void openTicketSearchAndSaveBookmarks() {
        TabsWidget tabs = TabsWidget.createById(driver, webDriverWait, MANAGEMENT_VIEW_CONTAINER_TABSCARD);
        tabs.selectTabById(TAB_TOOLS_ID);
        ToolsManagerWindow toolsManagerWindow = ToolsManagerWindow.create(driver, webDriverWait);
        toolsManagerWindow.openApplication(CATEGORY, VIEW_NAME);
        TicketSearchPage ticketSearchPage = new TicketSearchPage(driver, webDriverWait);
        ticketSearchPage.filterBy(SEVERITY_COMBOBOX_ID, SEVERITY_TYPE);
        ButtonPanel.create(driver, webDriverWait).clickButton(BUTTON_SAVE_BOOKMARK);

        BookmarkWizardPage bookmarkWizardPage = new BookmarkWizardPage(driver, webDriverWait);
        bookmarkWizardPage.setName(BOOKMARK_NAME);
        bookmarkWizardPage.setDescription(BOOKMARK_DESCRIPTION);
        bookmarkWizardPage.setCategory(CATEGORY_NAME);
        bookmarkWizardPage.clickSave();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        checkErrorPage(BOOKMARKS_PAGE);
        NewBookmarksPage.goToBookmarksPage(driver, webDriverWait, BASIC_URL);
        bookmarksPage.expandCategory(CATEGORY_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertTrue(bookmarksPage.isObjectPresent(BOOKMARK_NAME));
    }

    @Test(priority = 3, testName = "Open Bookmark", description = "Opening previously saved Bookmark")
    @Description("Opening previously saved Bookmark")
    public void openBookmarks() {

        NewBookmarksPage.goToBookmarksPage(driver, webDriverWait, BASIC_URL);
        bookmarksPage.expandCategory(CATEGORY_NAME);
        bookmarksPage.openBookmark(BOOKMARK_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        TicketSearchPage ticketSearchPage = new TicketSearchPage(driver, webDriverWait);
        Assert.assertFalse((ticketSearchPage.isIssueTableEmpty()));
        Assert.assertEquals(ticketSearchPage.getSeverityForNthTicketInTable(0), SEVERITY_TYPE);
    }

    @Test(priority = 4, testName = "Edit Bookmark on Bookmarks View", description = "Edit Bookmark on Bookmarks View")
    @Description("Edit Bookmark on Bookmarks View")
    public void editBookmarkOnBookmarksView() {
        bookmarksPage = NewBookmarksPage.goToBookmarksPage(driver, webDriverWait, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        bookmarksPage.expandCategory(CATEGORY_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        bookmarksPage.editBookmark(BOOKMARK_NAME, UPDATED_BOOKMARK_NAME, UPDATED_BOOKMARK_DESCRIPTION);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertTrue(bookmarksPage.isObjectPresent(UPDATED_BOOKMARK_NAME));
        checkErrorPage(BOOKMARKS_PAGE);
    }

    @Test(priority = 5, testName = "Delete Bookmark on Bookmarks View", description = "Delete Bookmark on Bookmarks View")
    @Description("Delete Bookmark on Bookmarks View")
    public void deleteBookmarkOnBookmarksView() {
        bookmarksPage = NewBookmarksPage.goToBookmarksPage(driver, webDriverWait, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        bookmarksPage.expandCategory(CATEGORY_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        bookmarksPage.deleteBookmark(UPDATED_BOOKMARK_NAME);
        DelayUtils.sleep();
        Assert.assertFalse(bookmarksPage.isObjectPresent(UPDATED_BOOKMARK_NAME));
        checkErrorPage(BOOKMARKS_PAGE);
    }

    @Test(priority = 6, testName = "Delete Category on Bookmarks View", description = "Delete Category on Bookmarks View")
    @Description("Delete Category on Bookmarks View")
    public void deleteCategoryOnBookmarksView() {
        bookmarksPage.deleteCategory(CATEGORY_NAME);
        checkErrorPage(BOOKMARKS_PAGE);
    }

    private void checkErrorPage(String viewName) {
        ErrorCard errorCard = ErrorCard.create(driver, webDriverWait);
        if (errorCard.isErrorPagePresent()) {
            ErrorCard.ErrorInformation errorInformation = errorCard.getErrorInformation();
            LOGGER.error(errorInformation.getErrorText());
            LOGGER.error(errorInformation.getErrorDescription());
            LOGGER.error(errorInformation.getErrorMessage());
            Assert.fail(String.format("Error Page is shown on %s page.", viewName));
        }
    }
}

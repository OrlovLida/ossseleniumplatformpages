package com.oss.smoketests;

import java.util.List;

import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.GlobalNotificationContainer;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.layout.ErrorCard;
import com.oss.framework.components.mainheader.ButtonPanel;
import com.oss.framework.navigation.toolsmanager.ToolsManagerWindow;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.advancedsearch.AdvancedSearchWidget;
import com.oss.framework.widgets.tabs.TabsWidget;
import com.oss.pages.platform.bookmarksanddashboards.bookmarks.BookmarkWizardPage;
import com.oss.pages.platform.bookmarksanddashboards.bookmarks.NewBookmarksPage;
import com.oss.pages.transport.NetworkViewPage;
import com.oss.untils.FakeGenerator;

import io.qameta.allure.Description;

import static com.oss.pages.platform.bookmarksanddashboards.bookmarks.NewBookmarksPage.MANAGEMENT_VIEW_CONTAINER_TABSCARD;
import static com.oss.web.NewBookmarksTest.BOOKMARK_PROPERTIES;
import static com.oss.web.NewBookmarksTest.BUTTON_SAVE_BOOKMARK;

public class Bookmarks_Smoke_Test extends BaseTestCase {
    private static final Logger LOGGER = LoggerFactory.getLogger(Bookmarks_Smoke_Test.class);
    NewBookmarksPage bookmarksPage;
    private AdvancedSearchWidget advancedSearchWidget;
    private static final String ADVANCED_SEARCH_WIDGET_ID = "advancedSearch";
    private static final int INDEX_OF_FIRST_ELEMENT_IN_TABLE = 0;
    public static final String LOCATION_ACTION = "add_to_view_group_Location-null";
    public static final String TAB_TOOLS_ID = "tab_tab_tools";
    private static final String CATEGORY = "Network Domains";
    private static final String VIEW_NAME = "Network View";
    public static final String BOOKMARKS_PAGE = "Bookmarks";
    public static final String NETWORK_PAGE = "Network View";
    private static final String DESCRIPTION_BOOKMARK = FakeGenerator.getIdNumber();
    private static final String UPDATED_BOOKMARK_NAME = FakeGenerator.getIdNumber();
    private static final String UPDATED_BOOKMARK_DESCRIPTION = FakeGenerator.getRandomName();
    public static final String CATEGORY_NAME = "Category " + FakeGenerator.getIdNumber();
    public static final String DESCRIPTION_CATEGORY = FakeGenerator.getRandomName();

    @Test(priority = 1, description = "Open Bookmarks View")
    @Description("Open Bookmarks View")
    public void openBookmarksView() {
        bookmarksPage = NewBookmarksPage.goToBookmarksPage(driver, webDriverWait, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        checkErrorPage(BOOKMARKS_PAGE);
        checkGlobalNotificationContainer(BOOKMARKS_PAGE);
    }

    @Test(priority = 2, description = "Create Category", dependsOnMethods = {"openBookmarksView"})
    @Description("Create Category")
    public void createCategory() {
        bookmarksPage.createCategory(CATEGORY_NAME, DESCRIPTION_CATEGORY);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        checkErrorPage(BOOKMARKS_PAGE);
        checkGlobalNotificationContainer(BOOKMARKS_PAGE);
        Assert.assertTrue(bookmarksPage.isObjectPresent(CATEGORY_NAME));
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertEquals(bookmarksPage.getDescription(CATEGORY_NAME), DESCRIPTION_CATEGORY);
    }

    @Test(priority = 3, description = "Open Network View and Add Location To View ", dependsOnMethods = {"createCategory"})
    @Description("Open Network View and Add Location To View ")
    public void openNetworkViewAndAddLocationToView() {
        TabsWidget tabs = TabsWidget.createById(driver, webDriverWait, MANAGEMENT_VIEW_CONTAINER_TABSCARD);
        tabs.selectTabById(TAB_TOOLS_ID);
        ToolsManagerWindow toolsManagerWindow = ToolsManagerWindow.create(driver, webDriverWait);
        toolsManagerWindow.openApplication(CATEGORY, VIEW_NAME);
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        networkViewPage.useContextAction(NetworkViewPage.ADD_TO_VIEW_ACTION, LOCATION_ACTION);
        advancedSearchWidget = AdvancedSearchWidget.createById(driver, webDriverWait, ADVANCED_SEARCH_WIDGET_ID);
        advancedSearchWidget.getTableComponent().clickRow(INDEX_OF_FIRST_ELEMENT_IN_TABLE);
        advancedSearchWidget.clickAdd();
        checkErrorPage(NETWORK_PAGE);
        checkGlobalNotificationContainer(NETWORK_PAGE);
    }

    @Test(priority = 4, description = "Save new Bookmark on Network View", dependsOnMethods = {"openNetworkViewAndAddLocationToView"})
    @Description("Save new Bookmark on Network View")
    public void saveBookmark() {
        ButtonPanel.create(driver, webDriverWait).clickButton(BUTTON_SAVE_BOOKMARK);
        BookmarkWizardPage bookmarkWizardPage = new BookmarkWizardPage(driver, webDriverWait);
        bookmarkWizardPage.setName(BOOKMARK_PROPERTIES);
        bookmarkWizardPage.setDescription(DESCRIPTION_BOOKMARK);
        bookmarkWizardPage.setCategory(CATEGORY_NAME);
        bookmarkWizardPage.clickSave();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        checkErrorPage(NETWORK_PAGE);
        checkGlobalNotificationContainer(NETWORK_PAGE);
    }

    @Test(priority = 5, description = "Open Bookmarks View and  check Bookmark on Network View", dependsOnMethods = {"saveBookmark"})
    @Description("Open Bookmarks View and check Bookmark on Network View")
    public void checkBookmarkOnNetworkView() {
        bookmarksPage = NewBookmarksPage.goToBookmarksPage(driver, webDriverWait, BASIC_URL);
        bookmarksPage.expandCategory(CATEGORY_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertTrue(bookmarksPage.isObjectPresent(BOOKMARK_PROPERTIES));
        bookmarksPage.openBookmark(BOOKMARK_PROPERTIES);
        checkErrorPage(NETWORK_PAGE);
        checkGlobalNotificationContainer(NETWORK_PAGE);
    }

    @Test(priority = 6, description = "Edit Bookmark on Bookmarks View", dependsOnMethods = {"saveBookmark"})
    @Description("Edit Bookmark on Bookmarks View")
    public void editBookmarkOnBookmarksView() {
        bookmarksPage = NewBookmarksPage.goToBookmarksPage(driver, webDriverWait, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        bookmarksPage.expandCategory(CATEGORY_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        bookmarksPage.editBookmark(BOOKMARK_PROPERTIES, UPDATED_BOOKMARK_NAME, UPDATED_BOOKMARK_DESCRIPTION);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertTrue(bookmarksPage.isObjectPresent(UPDATED_BOOKMARK_NAME));
        checkErrorPage(BOOKMARKS_PAGE);
        checkGlobalNotificationContainer(BOOKMARKS_PAGE);
    }

    @Test(priority = 7, description = "Delete Bookmark on Bookmarks View", dependsOnMethods = {"saveBookmark"})
    @Description("Delete Bookmark on Bookmarks View")
    public void deleteBookmarkOnBookmarksView() {
        bookmarksPage.deleteBookmark(UPDATED_BOOKMARK_NAME);
        DelayUtils.sleep();
        Assert.assertFalse(bookmarksPage.isObjectPresent(UPDATED_BOOKMARK_NAME));
        checkErrorPage(BOOKMARKS_PAGE);
        checkGlobalNotificationContainer(BOOKMARKS_PAGE);
    }

    @Test(priority = 8, description = "Delete Category on Bookmarks View", dependsOnMethods = {"createCategory"})
    @Description("Delete Category on Bookmarks View")
    public void deleteCategoryOnBookmarksView() {
        bookmarksPage.deleteCategory(CATEGORY_NAME);
        checkErrorPage(BOOKMARKS_PAGE);
        checkGlobalNotificationContainer(BOOKMARKS_PAGE);
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

    private void checkSystemMessage() {
        SystemMessageContainer systemMessage = SystemMessageContainer.create(this.driver, new WebDriverWait(this.driver, 5));
        List<String> errors = systemMessage.getErrors();
        errors.forEach(LOGGER::error);
        Assert.assertTrue(errors.isEmpty(), "Some errors occurred during the test. Please check logs for details.\n");
    }

    private void checkGlobalNotificationContainer(String viewName) {
        GlobalNotificationContainer globalNotificationContainer = GlobalNotificationContainer.create(driver, webDriverWait);
        if (globalNotificationContainer.isErrorNotificationPresent()) {
            GlobalNotificationContainer.NotificationInformation information = globalNotificationContainer.getNotificationInformation();
            LOGGER.error(information.getMessage());
            Assert.fail(String.format("Global Notification shows error on %s page.", viewName));
        }
    }
}

package com.oss.web;

import java.time.LocalDate;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.comarch.oss.web.pages.bookmarksanddashboards.bookmarks.BookmarkWizardPage;
import com.comarch.oss.web.pages.bookmarksanddashboards.bookmarks.NewBookmarksPage;
import com.oss.BaseTestCase;
import com.oss.framework.components.mainheader.ButtonPanel;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.propertypanel.PropertyPanel;
import com.comarch.oss.web.pages.NewInventoryViewPage;
import com.oss.untils.FakeGenerator;

import io.qameta.allure.Description;

public class NewBookmarksTest extends BaseTestCase {

    public static final String BOOKMARK_PROPERTIES = "Bookmark properties" + FakeGenerator.getRandomInt();
    public static final String PROPERTY_PANEL_WIDGET = "PropertyPanelWidget";
    public static final String BUTTON_SAVE_BOOKMARK = "ButtonSaveBookmark";
    private static final String DESCRIPTION_BOOKMARK = FakeGenerator.getIdNumber();
    public static final String CATEGORY_NAME = "Category " + FakeGenerator.getIdNumber();
    public static final String DESCRIPTION_CATEGORY = FakeGenerator.getRandomName();
    public static final String ID = "id";

    public static final String FIRST_NAME = "First Name";
    public static final String DIRECTOR = "Director";
    public static final String TEST_MOVIE = "TestMovie";
    public static final String TITLE = "Title";
    private static final String NEW_CATEGORY_NAME = "Edited Category " + FakeGenerator.getIdNumber();
    private static final String NEW_DESCRIPTION_NAME = "New Description: " + LocalDate.now();
    private static final String SAVE_BOOKMARK_POP_ID = "POPUP_MANAGEMENT_CONFIGURATION_ID";

    NewBookmarksPage bookmarksPage;

    @BeforeClass
    public void goToBookmarks() {
        bookmarksPage = NewBookmarksPage.goToBookmarksPage(driver, webDriverWait, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 1)
    public void createCategory() {
        bookmarksPage.createCategory(CATEGORY_NAME, DESCRIPTION_CATEGORY);
        DelayUtils.sleep();
        Assertions.assertThat(bookmarksPage.isObjectPresent(CATEGORY_NAME)).isTrue();
        Assertions.assertThat(bookmarksPage.getDescription(CATEGORY_NAME)).isEqualTo(DESCRIPTION_CATEGORY);
    }

    @Test(priority = 3)
    public void editCategory() {
        bookmarksPage = NewBookmarksPage.goToBookmarksPage(driver, webDriverWait, BASIC_URL);
        bookmarksPage.editCategory(CATEGORY_NAME, NEW_CATEGORY_NAME, NEW_DESCRIPTION_NAME);
        DelayUtils.sleep();
    }

    @Test(priority = 4)
    public void deleteCategory() {
        bookmarksPage.deleteCategory(NEW_CATEGORY_NAME);
        DelayUtils.sleep();
        Assertions.assertThat(bookmarksPage.isObjectPresent(NEW_CATEGORY_NAME)).isFalse();
        DelayUtils.sleep();
    }

    @Test(priority = 2, enabled = false)
    @Description("Save properties of the Bookmark: enable/disable attributes, drag and drop attribute in the Property Panel")
    public void savePropertiesBookmark() {
        NewInventoryViewPage newInventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, TEST_MOVIE);
        PropertyPanel propertyPanelWidget = newInventoryViewPage.getPropertyPanel(0, PROPERTY_PANEL_WIDGET);
        propertyPanelWidget.disableAttributeByLabel(TITLE);
        DelayUtils.sleep(1000);
        propertyPanelWidget.enableAttributeByLabel(FIRST_NAME, DIRECTOR);
        propertyPanelWidget.changePropertyOrder(ID, 1);
        List<String> beforeSaveEdition = newInventoryViewPage.getPropertyPanel(0, PROPERTY_PANEL_WIDGET).getVisibleAttributes();
        int beforeChangePosition = beforeSaveEdition.indexOf(ID);
        ButtonPanel.create(driver, webDriverWait).clickButton(BUTTON_SAVE_BOOKMARK);
        saveBookmark();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        bookmarksPage = NewBookmarksPage.goToBookmarksPage(driver, webDriverWait, BASIC_URL);
        bookmarksPage.expandCategory(CATEGORY_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assertions.assertThat(bookmarksPage.isObjectPresent(BOOKMARK_PROPERTIES)).isTrue();
        bookmarksPage.openBookmark(BOOKMARK_PROPERTIES);
        List<String> afterSaveEdition = newInventoryViewPage.getPropertyPanel(0, PROPERTY_PANEL_WIDGET).getVisibleAttributes();
        int afterChangePosition = afterSaveEdition.indexOf(ID);
        Assertions.assertThat(beforeSaveEdition).isEqualTo(afterSaveEdition);
        Assertions.assertThat(beforeChangePosition).isEqualTo(afterChangePosition);
        DelayUtils.sleep();
    }

    private void saveBookmark() {
        BookmarkWizardPage bookmarkWizardPage = new BookmarkWizardPage(driver, webDriverWait);
        bookmarkWizardPage.setName(BOOKMARK_PROPERTIES);
        bookmarkWizardPage.setDescription(DESCRIPTION_BOOKMARK);
        bookmarkWizardPage.setCategory(CATEGORY_NAME);
        bookmarkWizardPage.clickSave();
    }
}
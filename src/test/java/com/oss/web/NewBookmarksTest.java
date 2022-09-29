package com.oss.web;

import com.oss.BaseTestCase;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.mainheader.ButtonPanel;
import com.oss.framework.components.prompts.Popup;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.propertypanel.PropertyPanel;
import com.oss.pages.platform.NewInventoryViewPage;
import com.oss.pages.platform.bookmark.NewBookmarksPage;
import com.oss.untils.FakeGenerator;
import io.qameta.allure.Description;
import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

public class NewBookmarksTest extends BaseTestCase {

    public static final String BOOKMARK_PROPERTIES = "Bookmark properties" + FakeGenerator.getRandomInt();
    public static final String PROPERTY_PANEL_WIDGET = "PropertyPanelWidget";
    public static final String BUTTON_SAVE_BOOKMARK = "ButtonSaveBookmark";
    public static final String CATEGORY_NAME = "Kategoria 2";
    public static final String OPIS_KATEGORII = "Opis kategorii";
    public static final String A = "A";
    public static final String ID = "id";
    public static final String SAVE_MANAGEMENT_CONFIGURATION_BTN = "saveManagementConfigurationBtn";
    public static final String INPUT_CATEGORIES = "input_categories";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String FIRST_NAME = "First Name";
    public static final String DIRECTOR = "Director";
    public static final String TEST_MOVIE = "TestMovie";
    public static final String TITLE = "Title";


    NewBookmarksPage bookmarksPage;


    @BeforeClass
    public void goToBookmarks() {
        bookmarksPage = NewBookmarksPage.goToBookmarksPage(driver, webDriverWait, BASIC_URL);
        DelayUtils.sleep();
    }


    @Test(priority = 1)
    public void createCategory() {
        bookmarksPage.createCategory(CATEGORY_NAME, OPIS_KATEGORII);
        Assertions.assertThat(bookmarksPage.isElementPresent(CATEGORY_NAME)).isTrue();
        Assertions.assertThat(bookmarksPage.getDescription(CATEGORY_NAME)).isEqualTo(OPIS_KATEGORII);
        DelayUtils.sleep();
    }

    @Test(priority = 2)
    public void editCategory() {
        bookmarksPage.editCategory(CATEGORY_NAME, "Zedytowana kategoria 22", "Nowy opis");
        DelayUtils.sleep();
    }

    @Test(priority = 3)
    public void deleteCategory() {
        bookmarksPage.deleteCategory(CATEGORY_NAME);
        Assertions.assertThat(bookmarksPage.isElementPresent(CATEGORY_NAME)).isFalse();
        DelayUtils.sleep();
    }

    @Test(priority = 4)
    @Description("Save properties of the Bookmark: enable/disable attributes, drag and drop attribute in the Property Panel")
    public void savePropertiesBookmark() {
        String TYPE = TEST_MOVIE;
        NewInventoryViewPage newInventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, TYPE);
        PropertyPanel propertyPanelWidget = newInventoryViewPage.getPropertyPanel(0, PROPERTY_PANEL_WIDGET);
        propertyPanelWidget.disableAttributeByLabel(TITLE);
        DelayUtils.sleep(1000);
        propertyPanelWidget.enableAttributeByLabel(FIRST_NAME, DIRECTOR);
        propertyPanelWidget.changePropertyOrder(ID, 1);
        List<String> beforeSaveEdition = newInventoryViewPage.getPropertyPanel(0, PROPERTY_PANEL_WIDGET).getVisibleAttributes();
        int beforeChangePosition = beforeSaveEdition.indexOf(ID);
        ButtonPanel.create(driver, webDriverWait).clickButton(BUTTON_SAVE_BOOKMARK);
        Popup popup = Popup.create(driver, webDriverWait);
        popup.setComponentValue(NAME, BOOKMARK_PROPERTIES);
        popup.setComponentValue(DESCRIPTION, "OpisBookmarkaZIV");
        popup.setComponentValue(INPUT_CATEGORIES, "A", Input.ComponentType.SEARCH_FIELD);
        popup.clickButtonById(SAVE_MANAGEMENT_CONFIGURATION_BTN);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        bookmarksPage = NewBookmarksPage.goToBookmarksPage(driver, webDriverWait, BASIC_URL);
        bookmarksPage.expandRow(A);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assertions.assertThat(bookmarksPage.isElementPresent(BOOKMARK_PROPERTIES)).isTrue();
        bookmarksPage.openBookmark(BOOKMARK_PROPERTIES);
        List<String> afterSaveEdition = newInventoryViewPage.getPropertyPanel(0, PROPERTY_PANEL_WIDGET).getVisibleAttributes();
        int afterChangePosition = afterSaveEdition.indexOf(ID);
        Assertions.assertThat(beforeSaveEdition).isEqualTo(afterSaveEdition);
        Assertions.assertThat(beforeChangePosition).isEqualTo(afterChangePosition);
        DelayUtils.sleep();

    }
}
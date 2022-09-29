package com.oss.pages.platform.bookmark;

import com.oss.framework.components.prompts.Popup;
import com.oss.framework.widgets.tabs.TabsWidget;
import com.oss.framework.widgets.treetable.TreeTableWidget;
import com.oss.pages.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;

public class NewBookmarksPage extends BasePage {

    public static final String TAB_BOOKMARK_MANAGER_WIDGET = "tab_bookmark_manager_widget";
    public static final String ACTION_CATEGORY = "action_createCategoryBookmark";
    public static final String MANAGEMENT_VIEW_CONTAINER_TABSCARD = "management-view__container__tabscard";
    public static final String TAB_BOOKMARKS = "tab_bookmarks";
    public static final String NAME = "name";
    public static final String ACTION_EDIT_BOOKMARK_FOLDER = "action_editBookmarkFolder";
    public static final String ACTION_DELETE_BOOKMARK_FOLDER = "action_deleteBookmarkFolder";
    public static final String DELETE = "Delete";
    public static final String DESCRIPTION = "description";
    public static final String ID = "id";


    public NewBookmarksPage(WebDriver driver) {
        super(driver);
    }

    public static NewBookmarksPage goToBookmarksPage(WebDriver driver, WebDriverWait wait, String basicUrl) {
        driver.get(String.format("%s/#/", basicUrl));
        TabsWidget tabs = TabsWidget.createById(driver, wait, MANAGEMENT_VIEW_CONTAINER_TABSCARD);
        tabs.selectTabById(TAB_BOOKMARKS);
        return new NewBookmarksPage(driver);
    }


    public void createCategory(String categoryName, String description) {
        TreeTableWidget bookmarkTable = getTable();
        bookmarkTable.callAction(ACTION_CATEGORY);
        CategoryWizardPage category = CategoryWizardPage.createWizard(driver, wait);
        category.setName(categoryName);
        category.setDescription(description);
        category.clickSave();
    }

    public void editCategory(String categoryName, String newCategoryName, String newDescription) {
        TreeTableWidget bookmarkTable = getTable();
        bookmarkTable.selectRowByAttributeValue(NAME, categoryName);
        bookmarkTable.callAction(ACTION_EDIT_BOOKMARK_FOLDER);
        CategoryWizardPage wizard = CategoryWizardPage.createWizard(driver, wait);
        wizard.setName(newCategoryName);
        wizard.setDescription(newDescription);
        wizard.clickSave();

    }

    public void deleteCategory(String categoryName) {
        TreeTableWidget bookmarkTable = getTable();
        bookmarkTable.selectRowByAttributeValue(NAME, categoryName);
        bookmarkTable.callAction(ACTION_DELETE_BOOKMARK_FOLDER);
        Popup popup = Popup.create(driver, wait);
        popup.clickButtonByLabel(DELETE);

    }

    public boolean isElementPresent(String name) {
        ArrayList<String> cellValues = new ArrayList<>();
        int size = getTable().getAllRows().size();
        for (int i = 0; i < size; i++) {
            cellValues.add(getTable().getCellValue(i, NAME));
        }
        return cellValues.contains(name);
    }

    public String getDescription(String categoryName) {
        int index = getTable().getRowNumber(categoryName, NAME);
        return getTable().getCellValue(index, DESCRIPTION);
    }

    public void expandRow(String categoryName) {
        getTable().expandRow(categoryName, NAME);

    }

    public void openBookmark(String nameBookmark) {
        int name = getTable().getRowNumber(nameBookmark, NAME);
        getTable().clickLink(name, ID);

    }


    private TreeTableWidget getTable() {
        return TreeTableWidget.createById(driver, wait, TAB_BOOKMARK_MANAGER_WIDGET);
    }


}
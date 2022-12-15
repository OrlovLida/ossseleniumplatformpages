package com.oss.web;

import static com.oss.configuration.Configuration.CONFIGURATION;

import java.time.LocalDate;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.search.AdvancedSearch;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.list.CommonList;
import com.oss.pages.filtermanager.EditFilterPage;
import com.oss.pages.filtermanager.FilterManagerPage;
import com.oss.pages.platform.NewInventoryViewPage;
import com.oss.utils.TestListener;

import io.qameta.allure.Description;

@Listeners({TestListener.class})
public class FilterManagerTest extends BaseTestCase {

    private NewInventoryViewPage inventoryViewPage;
    private AdvancedSearch advancedSearch;
    private FilterManagerPage filterManagerPage;
    private EditFilterPage editFilterPage;
    private CommonList categoryList;

    private static final String FILTER_NAME = "Id_of_first_object";
    private static final String FILTER2_NAME = "Filter2";
    private static final String FILTER3_NAME = "Filter3";
    private static final String NEW_FILTER_NAME = "Filter3_new_name";
    private static final String FILTER_DESCRIPTION = "Test_description";
    private static final String VALUE_FOR_FILTER2 = "666";
    private static final String VALUE_FOR_FILTER3 = "3";
    private static final String FOLDER_NAME = "SeleniumFilterTest " + LocalDate.now();
    private static final String FOLDER_NEW_NAME = "NewSeleniumFilterTest " + LocalDate.now();
    private static final String FOLDER_DESCRITPION = "DescriptionSeleniumFilterTest ";
    private static final String FOLDER_IS_NOT_VISIBLE = "Folder" + FOLDER_NEW_NAME + " is not visible on Filter Manager View";
    private static final String FILTER_HAS_NOT_WRITE_PERMISSION = "Filter" + FILTER_NAME + "has not write permission";
    private static final String FILTER_HAS_WRITE_PERMISSION = "Filter" + NEW_FILTER_NAME + "has write permisssion";
    private static final String USER2_LOGIN = "webseleniumtests2";
    private static final String USER2_PASSWORD = "oss";
    private static final String ATTRIBUTE_ID = "id";
    private static final String TEST_MOVIE = "TestMovie";
    private static final String UNCATEGORIZED = "Uncategorized";

    @BeforeClass
    public void goToFilterManagerView() {
        filterManagerPage = FilterManagerPage.goToFilterManagerPage(driver, BASIC_URL);
    }

    @Test(priority = 1)
    public void createNewFolder() {
        filterManagerPage.createFolder(FOLDER_NAME);
        categoryList = filterManagerPage.getCommonList();
        categoryList.waitForCategory(FOLDER_NAME);
        Assert.assertTrue(filterManagerPage.isFolderVisible(FOLDER_NAME));

    }

    @Test(priority = 2)
    public void editFolder() {
        filterManagerPage.editFolder(FOLDER_NAME, FOLDER_NEW_NAME, FOLDER_DESCRITPION);
        categoryList = filterManagerPage.getCommonList();
        categoryList.waitForCategory(FOLDER_NEW_NAME);
        Assert.assertTrue(filterManagerPage.isFolderVisible(FOLDER_NEW_NAME));
    }

    @Test(priority = 3)
    public void checkIfCreatedFiltersAreVisibleInFilterManager() {
        inventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, TEST_MOVIE);
        String id = inventoryViewPage.getIdOfMainTableObject(0);
        advancedSearch = inventoryViewPage.getAdvancedSearch();
        advancedSearch.setFilter(ATTRIBUTE_ID, Input.ComponentType.TEXT_FIELD, id);
        advancedSearch.saveAsNewFilter(FILTER_NAME);
        closeMessages();
        advancedSearch.setFilter(ATTRIBUTE_ID, Input.ComponentType.TEXT_FIELD, VALUE_FOR_FILTER2);
        advancedSearch.saveAsNewFilter(FILTER2_NAME);
        closeMessages();
        advancedSearch.setFilter(ATTRIBUTE_ID, Input.ComponentType.TEXT_FIELD, VALUE_FOR_FILTER3);
        advancedSearch.saveAsNewFilter(FILTER3_NAME);
        closeMessages();
        goToFilterManagerView();
        filterManagerPage.expandFolder(UNCATEGORIZED);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertTrue(filterManagerPage.isFilterVisible(FILTER_NAME) && filterManagerPage.isFilterVisible(FILTER2_NAME) && filterManagerPage.isFilterVisible(FILTER3_NAME));
    }

    @Test(priority = 4)
    public void editNameAndDescriptionOfFilter() {
        filterManagerPage.editFilter(FILTER3_NAME);
        editFilterPage = new EditFilterPage(driver);
        editFilterPage.changeName(NEW_FILTER_NAME).changeDescription(FILTER_DESCRIPTION).clickAccept();
        DelayUtils.sleep(2000);
        Assert.assertTrue(filterManagerPage.isFilterVisible(NEW_FILTER_NAME));
    }

    //disabled until fix OSSWEB-21137
    @Test(priority = 5, enabled = false)
    public void changeFolderForFilter() {
        filterManagerPage.editFilter(FILTER2_NAME);
        editFilterPage = new EditFilterPage(driver);
        editFilterPage.changeFolderForFilter(FOLDER_NAME)
                .clickAccept()
                .collapseAllCategories()
                .expandFolder(FOLDER_NAME);
        Assert.assertTrue(filterManagerPage.isFolderVisible(FOLDER_NAME) && filterManagerPage.isFilterVisible(FILTER2_NAME));
    }

    //disabled until fix OSSWEB-19547
    @Test(priority = 6, enabled = false)
    public void addFilterToFavourite() {
        filterManagerPage.markAsAFavorite(FILTER2_NAME);
        Assert.assertTrue(filterManagerPage.isFavorite(FILTER2_NAME));
    }

    @Test(priority = 7)
    public void sharingFolderAndFilterForUser() {
        filterManagerPage = FilterManagerPage.goToFilterManagerPage(driver, BASIC_URL);
        filterManagerPage
                .expandAllCategories()
                .shareFilter(FILTER_NAME, USER2_LOGIN, "W")
                .expandAllCategories()
                .shareFilter(NEW_FILTER_NAME, USER2_LOGIN, "R")
                .shareFolder(FOLDER_NEW_NAME, USER2_LOGIN);
        filterManagerPage.changeUser(USER2_LOGIN, USER2_PASSWORD);
        filterManagerPage = FilterManagerPage.goToFilterManagerPage(driver, BASIC_URL);
        filterManagerPage.expandAllCategories();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertTrue(filterManagerPage.isFilterVisible(FILTER_NAME) && filterManagerPage.isFilterVisible(NEW_FILTER_NAME) /*&& filterManagerPage.isFilterVisible(FILTER2_NAME) //disabled until fix OSSWEB-21137*/);
        Assert.assertTrue(filterManagerPage.isFolderVisible(FOLDER_NEW_NAME), FOLDER_IS_NOT_VISIBLE);
        Assert.assertTrue(filterManagerPage.isEditActionVisible(FILTER_NAME), FILTER_HAS_NOT_WRITE_PERMISSION);
        Assert.assertFalse(filterManagerPage.isEditActionVisible(NEW_FILTER_NAME), FILTER_HAS_WRITE_PERMISSION);
    }

    //disabled until fix OSSWEB-19547
    @Test(priority = 8, enabled = false)
    public void addSharedFilterToFavourite() {
        filterManagerPage.expandAllCategories()
                .markAsAFavorite(NEW_FILTER_NAME);
        Assert.assertFalse(filterManagerPage.isEditActionVisible(NEW_FILTER_NAME));
        Assert.assertTrue(filterManagerPage.isFavorite(NEW_FILTER_NAME));
    }

    //disabled until fix OSSWEB-21137
    @Test(priority = 9, enabled = false)
    public void isFilterSharedWithFolder() {
        filterManagerPage
                .collapseAllCategories()
                .expandFolder(FOLDER_NAME);
        Assert.assertTrue(filterManagerPage.isFilterVisible(FILTER2_NAME));
        Assert.assertFalse(filterManagerPage.isEditActionVisible(FILTER2_NAME));
        Assert.assertFalse(filterManagerPage.isFavorite(FILTER2_NAME));
    }

    @Test(priority = 10)
    @Description("Checking that filters are visible in Inventory View for a second user")
    public void areFiltersVisibleInIV() {
        inventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, TEST_MOVIE);
        advancedSearch = inventoryViewPage.getAdvancedSearch();
        List<String> savedFilters = advancedSearch.getSavedFilters();
        Assert.assertTrue(/*savedFilters.contains(FILTER2_NAME) //disabled until fix OSSWEB-21137 && */savedFilters.contains(FILTER_NAME) && savedFilters.contains(NEW_FILTER_NAME));
    }

    //disabled until fix OSSWEB-19547
    @Test(priority = 11, enabled = false)
    @Description("Checking that filters marked as a favorite is favorite in Inventory View for a second user")
    public void areFiltersFavoriteInIV() {
        List<String> favoriteFilters = advancedSearch.getFavoriteFilters();
        Assert.assertTrue(favoriteFilters.contains(NEW_FILTER_NAME));
    }

    @Test(priority = 12)
    @Description("Checking that filter have a proper value in Inventory View for a second user")
    public void isFilterHaveProperValue() {
        advancedSearch.selectSavedFilterByLabel(NEW_FILTER_NAME);
        String idValue = advancedSearch.getComponent(ATTRIBUTE_ID, Input.ComponentType.TEXT_FIELD).getStringValue();
        Assert.assertEquals(idValue, VALUE_FOR_FILTER3);
    }

    @Test(priority = 13)
    @Description("Deleting shared filter. Checking that is deleted")
    public void removingFilterForSecondUser() {
        filterManagerPage = FilterManagerPage.goToFilterManagerPage(driver, BASIC_URL)
                .expandAllCategories()
                .deleteFilter(FILTER_NAME);
        DelayUtils.sleep(2000);
        Assert.assertFalse(filterManagerPage.isFilterVisible(FILTER_NAME));
    }

    @Test(priority = 14)
    @Description("Checking that is deleted for a first user as well")
    public void checkIfFilteIsRemovedForFirstUser() {
        filterManagerPage.changeUser(CONFIGURATION.getValue("user"), CONFIGURATION.getValue("password"));
        filterManagerPage = FilterManagerPage.goToFilterManagerPage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        filterManagerPage.expandAllCategories();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertFalse(filterManagerPage.isFilterVisible(FILTER_NAME));
    }

    @Test(priority = 15)
    public void removeFolder() {
        filterManagerPage.removeFolder(FOLDER_NEW_NAME);
        DelayUtils.sleep(2000);
        Assert.assertFalse(filterManagerPage.isFolderVisible(FOLDER_NEW_NAME));
        //Assert.assertFalse(filterManagerPage.isFilterVisible(FILTER2_NAME)); //disabled until fix OSSWEB-21137
    }

    @Test(priority = 16)
    public void removeFilter() {
        filterManagerPage.collapseAllCategories().expandFolder(UNCATEGORIZED).deleteFilter(NEW_FILTER_NAME);
        filterManagerPage.collapseAllCategories().expandFolder(UNCATEGORIZED).deleteFilter(FILTER2_NAME);
        Assert.assertFalse(filterManagerPage.isFilterVisible(NEW_FILTER_NAME));
    }

    private void closeMessages() {
        SystemMessageContainer.create(driver, webDriverWait).close();
    }
}

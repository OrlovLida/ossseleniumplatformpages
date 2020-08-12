package com.oss;


import com.oss.framework.utils.DelayUtils;
import com.oss.pages.filtermanager.EditFilterPage;
import com.oss.pages.filtermanager.FilterManagerPage;
import com.oss.pages.filterpanel.FilterPanel;
import com.oss.pages.filterpanel.FilterSettingsFilter;
import com.oss.pages.platform.InventoryViewPage;
import com.oss.utils.TestListener;
import io.qameta.allure.Description;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static com.oss.configuration.Configuration.CONFIGURATION;

@Listeners({TestListener.class})
public class FiltersTest extends BaseTestCase{

    private InventoryViewPage inventoryViewPage;
    private FilterPanel filterPanel;
    private FilterSettingsFilter filterSettingsFilter;
    private FilterManagerPage filterManagerPage;
    private EditFilterPage editFilterPage;
    private int filtersBefore;

    private String FILTER_NAME = "Id_of_first_object";
    private String FILTER2_NAME = "Filter2";
    private String FILTER3_NAME = "Filter3";
    private String VALUE_FOR_FILTER2 = "666";
    private String VALUE_FOR_FILTER3 = "3";
    private String VALUE_FOR_FILTER3_AFTER_EDIT = "4";
    private String VALUE_IN_LOCATION_ID_INPUT;
    private String FOLDER_NAME = "test";
    private String USER2_LOGIN = "webseleniumtests2";
    private String USER2_PASSWORD = "webtests";

    @BeforeClass
    public void goToInventoryView() {
        inventoryViewPage = InventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, "Location");

    }

    @Test(priority = 1)
    @Description("Creating 3 new filters and saving them as new filters")
    public void createNewFilters() {
        String id = inventoryViewPage.getIdOfFirstObject();
        inventoryViewPage
                .openFilterPanel()
                .openFilterSettings()
                .changeTabToFilters();
        filterSettingsFilter = new FilterSettingsFilter(driver);
        filtersBefore = filterSettingsFilter.howManyFilters();
        filterPanel = new FilterPanel(driver);
        filterPanel.typeValueInLocationIdInput(id)
                .saveFilterAs(FILTER_NAME)
                .typeValueInLocationIdInput(VALUE_FOR_FILTER2)
                .saveFilterAs(FILTER2_NAME)
                .typeValueInLocationIdInput(VALUE_FOR_FILTER3)
                .saveFilterAs(FILTER3_NAME);
        Assert.assertEquals(filterSettingsFilter.howManyFilters() - filtersBefore, 3);
    }

    @Test(priority = 2)
    @Description("Adding filter to favorite")
    public void addingFilterToFavorite(){
        filterSettingsFilter
                .markAsFavorite(FILTER2_NAME);
        Assert.assertTrue(filterSettingsFilter.isFilterFavorite(FILTER2_NAME));
    }

    @Test(priority = 3)
    @Description("Checking that filter is applied")
    public void isFilterApply() {
        filterSettingsFilter.selectFilter(FILTER3_NAME)
                .applyFilter();
        Assert.assertTrue(filterPanel.isFilterApplied(FILTER3_NAME));
    }

    @Test(priority = 4)
    @Description("Editing an Existing Filter")
    public void editingAnExistingFilter(){
        filterPanel
                .typeValueInLocationIdInput(VALUE_FOR_FILTER3_AFTER_EDIT)
                .saveFilter();
        filterSettingsFilter
                .selectFilter(FILTER_NAME)
                .applyFilter();
        filterSettingsFilter
                .selectFilter(FILTER3_NAME)
                .applyFilter();
        Assert.assertEquals(filterPanel.getValueOfLocationIdInput(), VALUE_FOR_FILTER3_AFTER_EDIT);
    }

    @Test(priority = 5)
    @Description("Checking that filter is filtering object in Inventory View")
    public void isFilterWorking() {
        filterSettingsFilter
                .selectFilter(FILTER_NAME)
                .applyFilter();
        VALUE_IN_LOCATION_ID_INPUT=filterPanel.getValueOfLocationIdInput();
        System.out.println(VALUE_IN_LOCATION_ID_INPUT);
            filterPanel
                .applyFilter();
        Assert.assertTrue(inventoryViewPage.isOnlyOneObject(VALUE_IN_LOCATION_ID_INPUT));
    }

    @Test(priority = 6)
    @Description("Checking that 'clear All' button is working properly")
    public void cancelingFilter() {
        inventoryViewPage
                .clearAllTags();
        System.out.println(inventoryViewPage.getTableWidget().howManyRowsOnFirstPage());
        Assert.assertTrue(inventoryViewPage.isAllTagsInvisible() && inventoryViewPage.getTableWidget().howManyRowsOnFirstPage()>1);
    }

    @Test(priority = 7)
    @Description("Checking that the filter marked as favorite is favorite in Filter Manager")
    public void isFilterFavoriteInFilterManager() {
        filterManagerPage = FilterManagerPage.goToFilterManagerPage(driver,BASIC_URL)
                .expandAllCategories();
        Assert.assertTrue(filterManagerPage.isFavorite(FILTER2_NAME));
    }

    @Test(priority = 8)
    @Description("Creating Folder and  Checking that the created folder is visible in Filter Manager View")
    public void creatingFolder() {
        filterManagerPage = FilterManagerPage.goToFilterManagerPage(driver,BASIC_URL)
                .createFolder(FOLDER_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertTrue(filterManagerPage.isFolderVisible(FOLDER_NAME));
    }

    @Test(priority = 9)
    @Description("Change Folder for filter")
    public void changeFolderForFilter() {
        filterManagerPage = FilterManagerPage.goToFilterManagerPage(driver,BASIC_URL)
                .expandAllCategories()
                .editFilter(FILTER3_NAME);
        editFilterPage = new EditFilterPage(driver);
        editFilterPage.changeFolderForFilter()
                .clickAccept()
                .expandFolder(FOLDER_NAME);
        Assert.assertTrue(filterManagerPage.isFolderVisible(FOLDER_NAME));
    }

    @Test(priority = 10)
    @Description("Sharing an Existing Filters, Folder and checking that shared filters are visible for second user")
    public void sharingAnExistingFilter(){
        filterManagerPage //= FilterManagerPage.goToFilterManagerPage(driver,BASIC_URL)
                .expandAllCategories()
       // filterManagerPage
                .shareFilter(FILTER2_NAME, USER2_LOGIN, "W")
                .expandAllCategories()
                .shareFilter(FILTER_NAME, USER2_LOGIN, "R")
                .shareFolder(FOLDER_NAME, USER2_LOGIN);
        filterManagerPage.changeUser(USER2_LOGIN, USER2_PASSWORD);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        filterManagerPage.expandAllCategories();
        Assert.assertTrue(filterManagerPage.isFilterVisible(FILTER_NAME) && filterManagerPage.isFilterVisible(FILTER2_NAME));
    }

    @Test(priority = 11)
    @Description("Checking that the shared folder is Visible for second user")
    public void sharingAnExistingFolder(){
        Assert.assertTrue(filterManagerPage.isFolderVisible(FOLDER_NAME));
    }

    @Test(priority = 12)
    @Description("Checking that Shared filter with Write permission could be edited")
    public void isWritePermissionWorking(){
        Assert.assertTrue(filterManagerPage.isEditActionVisible(FILTER2_NAME));
    }

    @Test(priority = 13)
    @Description("Checking that Shared filter with Read permission could not be edited")
    public void isReadPermissionWorking(){
        Assert.assertFalse(filterManagerPage.isEditActionVisible(FILTER_NAME));
        filterManagerPage.changeUser(CONFIGURATION.getValue("user"), CONFIGURATION.getValue("password"));
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }


    @Test(priority = 15)
    @Description("Deleting one filter")
    public void removingFilter() {
        filterManagerPage = FilterManagerPage.goToFilterManagerPage(driver,BASIC_URL)
                .expandAllCategories()
                .deleteFilter(FILTER_NAME);
        Assert.assertFalse(filterManagerPage.isFilterVisible(FILTER_NAME));
    }

    @Test(priority = 25)
    public void deleteAllFiltersAndFolders(){
        filterManagerPage = FilterManagerPage.goToFilterManagerPage(driver,BASIC_URL)
                .deleteAllFilters()
                .deleteAllFolders();
        Assert.assertTrue(filterManagerPage.howManyFilters() == 0 && filterManagerPage.howManyFolders()==1);
    }
}

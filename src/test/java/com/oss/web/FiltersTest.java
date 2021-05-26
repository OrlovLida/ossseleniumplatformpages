package com.oss.web;

import com.oss.BaseTestCase;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.filtermanager.EditFilterPage;
import com.oss.pages.filtermanager.FilterManagerPage;
import com.oss.pages.filterpanel.FilterPanelPage;
import com.oss.pages.filterpanel.FilterSettingsFilter;
import com.oss.pages.platform.NewInventoryViewPage;
import com.oss.utils.TestListener;
import io.qameta.allure.Description;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static com.oss.configuration.Configuration.CONFIGURATION;

@Listeners({TestListener.class})
public class FiltersTest extends BaseTestCase {

    private NewInventoryViewPage inventoryViewPage;
    private FilterPanelPage filterPanelPage;
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
    private String USER2_LOGIN = "webseleniumtests";
    private String USER2_PASSWORD = "Webtests123!";
    private int i = 0;

    @BeforeClass
    public void goToInventoryView() {
        inventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, "Location");
    }

    @Test(priority = 1)
    @Description("Creating three new filters and saving them as new filters. Checking that all three filters are created")
    public void createNewFilters() {
        String id = inventoryViewPage.getIdOfMainTableObject(0);
        inventoryViewPage
                .openFilterPanel()
                .openFilterSettings()
                .changeTabToFilters();
        filterSettingsFilter = new FilterSettingsFilter(driver);
        filtersBefore = filterSettingsFilter.howManyFilters();
        if (filtersBefore > 0 && i < 1) {
            System.out.println("There are " + filtersBefore + " old filters. Start removing them.");
            deleteAllFiltersAndFolders();
            i++;
            inventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, "Location");
            createNewFilters();
        } else {
            filterPanelPage = new FilterPanelPage(driver);
            filterPanelPage.typeValueInLocationIdInput(id)
                    .saveFilterAs(FILTER_NAME)
                    .typeValueInLocationIdInput(VALUE_FOR_FILTER2)
                    .saveFilterAs(FILTER2_NAME)
                    .typeValueInLocationIdInput(VALUE_FOR_FILTER3)
                    .saveFilterAs(FILTER3_NAME);
            Assert.assertEquals(filterSettingsFilter.howManyFilters() - filtersBefore, 3);
        }
    }

    @Test(priority = 2)
    @Description("Adding filter to favorite, checking that the star icon for that filter is filled")
    public void addingFilterToFavorite() {
        filterSettingsFilter
                .markAsFavorite(FILTER2_NAME);
        Assert.assertTrue(filterSettingsFilter.isFilterFavorite(FILTER2_NAME));
    }

    @Test(priority = 3)
    @Description("Checking that filter is applied properly and name of the filter is displayed in Filter Panel")
    public void isFilterApply() {
        filterSettingsFilter.selectFilter(FILTER3_NAME)
                .applyFilter();
        Assert.assertTrue(filterPanelPage.isFilterApplied(FILTER3_NAME));
    }

    @Test(priority = 4)
    @Description("Editing an Existing Filter and save them. Checking that the value is change after saving it")
    public void editingAnExistingFilter() {
        filterPanelPage
                .changeValueInLocationIdInput(VALUE_FOR_FILTER3_AFTER_EDIT)
                .saveFilter();
        filterSettingsFilter
                .selectFilter(FILTER2_NAME)
                .applyFilter();
        filterSettingsFilter
                .selectFilter(FILTER3_NAME)
                .applyFilter();
        Assert.assertEquals(filterPanelPage.getValueOfLocationIdInput(), VALUE_FOR_FILTER3_AFTER_EDIT);
    }

    @Test(priority = 5)
    @Description("Checking that filter is filtering object in Inventory View after apply it")
    public void isFilterWorking() {
        filterSettingsFilter
                .selectFilter(FILTER_NAME)
                .applyFilter();
        VALUE_IN_LOCATION_ID_INPUT = filterPanelPage.getValueOfLocationIdInput();
        filterPanelPage
                .applyFilter();
        Assert.assertTrue(inventoryViewPage.isOnlyOneObject(VALUE_IN_LOCATION_ID_INPUT));
    }

    @Test(priority = 6)
    @Description("Checking that 'clear All' button is working properly (buttons disappeared and filtering is cancel)")
    public void cancelingFilter() {
        inventoryViewPage
                .clearAllTags();
        Assert.assertTrue(inventoryViewPage.isAllTagsInvisible() && inventoryViewPage.getMainTable().howManyRowsOnFirstPage() > 1);
    }

    @Test(priority = 7)
    @Description("Checking that the filter marked as favorite is favorite in Filter Manager")
    public void isFilterFavoriteInFilterManager() {
        filterManagerPage = FilterManagerPage.goToFilterManagerPage(driver, BASIC_URL)
                .expandAllCategories();
        Assert.assertTrue(filterManagerPage.isFavorite(FILTER2_NAME));
    }

    @Test(priority = 8)
    @Description("Creating Folder and checking that the created folder is visible in Filter Manager View")
    public void creatingFolder() {
        filterManagerPage
                .createFolder(FOLDER_NAME)
                .expandAllCategories();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertTrue(filterManagerPage.isFolderVisible(FOLDER_NAME));
    }

    @Test(priority = 9)
    @Description("Change folder for filter. Checking that filter is in proper folder after edit")
    public void changeFolderForFilter() {
        filterManagerPage
                .expandAllCategories()
                .editFilter(FILTER3_NAME);
        editFilterPage = new EditFilterPage(driver);
        editFilterPage.changeFolderForFilter()
                .clickAccept()
                .collapseAllCategories()
                .expandFolder(FOLDER_NAME);
        Assert.assertTrue(filterManagerPage.isFolderVisible(FOLDER_NAME) && filterManagerPage.isFilterVisible(FILTER3_NAME));
    }

    @Test(priority = 10)
    @Description("Sharing an existing Filters, Folder and checking that shared filters are visible for second user")
    public void sharingAnExistingFilter() {
        filterManagerPage
                .expandAllCategories()
                .shareFilter(FILTER_NAME, USER2_LOGIN, "W")
                .expandAllCategories()
                .shareFilter(FILTER2_NAME, USER2_LOGIN, "R")
                .shareFolder(FOLDER_NAME, USER2_LOGIN);
        filterManagerPage.changeUser(USER2_LOGIN, USER2_PASSWORD);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        filterManagerPage.expandAllCategories()
                .markAsAFavorite(FILTER_NAME);
        Assert.assertTrue(filterManagerPage.isFilterVisible(FILTER_NAME) && filterManagerPage.isFilterVisible(FILTER2_NAME));
    }

    @Test(priority = 11)
    @Description("Checking that the shared folder is Visible for second user")
    public void sharingAnExistingFolder() {
        Assert.assertTrue(filterManagerPage.isFolderVisible(FOLDER_NAME));
    }

    @Test(priority = 12)
    @Description("Checking that Shared filter with Write permission could be edited")
    public void isWritePermissionWorking() {
        Assert.assertTrue(filterManagerPage.isEditActionVisible(FILTER_NAME));
    }

    @Test(priority = 13)
    @Description("Checking that Shared filter with Read permission could not be edited")
    public void isReadPermissionWorking() {
        Assert.assertFalse(filterManagerPage.isEditActionVisible(FILTER2_NAME));
    }

    @Test(priority = 14)
    @Description("Checking that filter is shared with folder and is visible for a second user")
    public void isFilterSharedWithFolder() {
        filterManagerPage
                .collapseAllCategories()
                .expandFolder(FOLDER_NAME);
        Assert.assertTrue(filterManagerPage.isFilterVisible(FILTER3_NAME));
    }

    @Test(priority = 15)
    @Description("Checking that filters are visible in Inventory View for a second user")
    public void areFiltersVisibleInIV() {
        inventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, "Location");
        inventoryViewPage
                .openFilterPanel()
                .openFilterSettings()
                .changeTabToFilters();
        Assert.assertTrue(filterSettingsFilter.isFilterVisibleInFilterPanel(FILTER2_NAME) && filterSettingsFilter.isFilterVisibleInFilterPanel(FILTER_NAME));
    }

    @Test(priority = 16)
    @Description("Checking that filters marked as a favorite is favorite in Inventory View for a second user")
    public void areFiltersFavoriteInIV() {
        Assert.assertTrue(filterSettingsFilter.isFilterFavorite(FILTER_NAME));
    }

    @Test(priority = 17)
    @Description("Checking that filter have a proper value in Inventory View for a second user")
    public void isFilterHaveProperValue() {
        filterSettingsFilter
                .selectFilter(FILTER2_NAME)
                .applyFilter();
        Assert.assertEquals(filterPanelPage.getValueOfLocationIdInput(), VALUE_FOR_FILTER2);
    }

    @Test(priority = 18)
    @Description("Deleting shared filter. Checking that is deleted")
    public void removingFilterForSecondUser() {
        filterManagerPage = FilterManagerPage.goToFilterManagerPage(driver, BASIC_URL)
                .expandAllCategories()
                .deleteFilter(FILTER_NAME);
        Assert.assertFalse(filterManagerPage.isFilterVisible(FILTER_NAME));
    }

    @Test(priority = 19)
    @Description("Checking that is deleted for a first user as well")
    public void removingFilter() {
        filterManagerPage.changeUser(CONFIGURATION.getValue("user"), CONFIGURATION.getValue("password"));
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertFalse(filterManagerPage.isFilterVisible(FILTER_NAME));
    }

    @Test(priority = 20)
    @Description("Deleting all filters and folders")
    public void deleteAllFiltersAndFolders() {
        filterManagerPage = FilterManagerPage.goToFilterManagerPage(driver, BASIC_URL)
                .expandAllCategories()
                .deleteAllFilters()
                .deleteAllFolders();
        Assert.assertTrue(filterManagerPage.howManyFilters() == 0 && filterManagerPage.howManyFolders() == 1);
    }
}

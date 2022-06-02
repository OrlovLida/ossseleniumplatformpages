package com.oss.web;

import static com.oss.configuration.Configuration.CONFIGURATION;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.search.AdvancedSearch;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.filtermanager.EditFilterPage;
import com.oss.pages.filtermanager.FilterManagerPage;
import com.oss.pages.platform.NewInventoryViewPage;
import com.oss.utils.TestListener;

import io.qameta.allure.Description;

@Listeners({TestListener.class})
public class FiltersTest extends BaseTestCase {
    
    private static final Logger log = LoggerFactory.getLogger(FiltersTest.class);
    private NewInventoryViewPage inventoryViewPage;
    private AdvancedSearch advancedSearch;
    private FilterManagerPage filterManagerPage;
    private EditFilterPage editFilterPage;
    
    private String FILTER_NAME = "Id_of_first_object";
    private String FILTER2_NAME = "Filter2";
    private String FILTER3_NAME = "Filter3";
    private String VALUE_FOR_FILTER2 = "666";
    private String VALUE_FOR_FILTER3 = "3";
    private String VALUE_FOR_FILTER3_AFTER_EDIT = "4";
    private String FOLDER_NAME = "SeleniumFilterTest " + LocalDate.now();
    private String USER2_LOGIN = "webseleniumtests2";
    private String USER2_PASSWORD = "webtests";
    private String ATTRIBUTE_ID = "id";
    private int i = 0;
    
    @BeforeClass
    public void goToInventoryView() {
        inventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, "Location");
        advancedSearch = inventoryViewPage.getAdvancedSearch();
    }
    
    @Test(priority = 1)
    @Description("Creating three new filters and saving them as new filters. Checking that all three filters are created")
    public void createNewFilters() {
        String id = inventoryViewPage.getIdOfMainTableObject(0);
        int savedFiltersNumber = inventoryViewPage.getSavedFilters().size();
        
        if (savedFiltersNumber > 0 && i < 1) {
            log.info("There are " + savedFiltersNumber + " old filters. Start removing them.");
            deleteAllFiltersAndFolders();
            i++;
            inventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, "Location");
            advancedSearch = inventoryViewPage.getAdvancedSearch();
            createNewFilters();
        } else {
            
            advancedSearch.setFilter(ATTRIBUTE_ID, Input.ComponentType.TEXT_FIELD, id);
            advancedSearch.saveAsNewFilter(FILTER_NAME);
            
            advancedSearch.setFilter(ATTRIBUTE_ID, Input.ComponentType.TEXT_FIELD, VALUE_FOR_FILTER2);
            advancedSearch.saveAsNewFilter(FILTER2_NAME);
            closeMessages();
            
            advancedSearch.setFilter(ATTRIBUTE_ID, Input.ComponentType.TEXT_FIELD, VALUE_FOR_FILTER3);
            advancedSearch.saveAsNewFilter(FILTER3_NAME);
            closeMessages();
            
            Assert.assertEquals(inventoryViewPage.getSavedFilters().size(), 3);
        }
    }
    
    @Test(priority = 2)
    @Description("Adding filter to favorite, checking that the star icon for that filter is filled")
    public void addingFilterToFavorite() {
        advancedSearch.markFavoriteFilter(FILTER2_NAME);
        closeMessages();
        Assert.assertTrue(advancedSearch.getFavoriteFilters().contains(FILTER2_NAME));
    }
    
    @Test(priority = 3)
    @Description("Checking that filter is applied properly and name of the filter is displayed in Filter Panel")
    public void isFilterApply() {
        advancedSearch.selectSavedFilterByLabel(FILTER2_NAME);
        advancedSearch.clickApply();
        Assert.assertEquals(advancedSearch.getAppliedFilters().size(), 1);
        advancedSearch.clearAllFilters();
    }
    
    @Test(priority = 4)
    @Description("Editing an Existing Filter and save them. Checking that the value is change after saving it")
    public void editingAnExistingFilter() {
        advancedSearch.selectSavedFilterByLabel(FILTER3_NAME);
        advancedSearch.setFilter(ATTRIBUTE_ID, Input.ComponentType.TEXT_FIELD, VALUE_FOR_FILTER3_AFTER_EDIT);
        advancedSearch.saveFilter();
        closeMessages();
        advancedSearch.selectSavedFilterByLabel(FILTER2_NAME);
        advancedSearch.selectSavedFilterByLabel(FILTER3_NAME);
        
        String idValue = advancedSearch.getComponent(ATTRIBUTE_ID, Input.ComponentType.TEXT_FIELD).getStringValue();
        Assert.assertEquals(idValue, VALUE_FOR_FILTER3_AFTER_EDIT);
    }
    
    @Test(priority = 5)
    @Description("Checking that filter is filtering object in Inventory View after apply it")
    public void isFilterWorking() {
        advancedSearch.selectSavedFilterByLabel(FILTER_NAME);
        advancedSearch.clickApply();
        
        String attributeValue = inventoryViewPage.getAttributeValue(ATTRIBUTE_ID, 0);
        
        Assert.assertTrue(inventoryViewPage.isOnlyOneObject(attributeValue));
    }
    
    @Test(priority = 6)
    @Description("Checking that 'clear All' button is working properly (buttons disappeared and filtering is cancel)")
    public void cancelingFilter() {
        inventoryViewPage
                .clearFilters();
        Assert.assertEquals(inventoryViewPage.countOfVisibleTags(), 0);
        Assert.assertTrue(inventoryViewPage.getMainTable().getRowsNumber() > 1);
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
        editFilterPage.changeFolderForFilter(FOLDER_NAME)
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
        Assert.assertTrue(filterManagerPage.isFavorite(FILTER_NAME));
        
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
        advancedSearch = inventoryViewPage.getAdvancedSearch();
        List<String> savedFilters = advancedSearch.getSavedFilters();
        Assert.assertTrue(savedFilters.contains(FILTER2_NAME)
                && savedFilters.contains(FILTER_NAME));
    }
    
    @Test(priority = 16)
    @Description("Checking that filters marked as a favorite is favorite in Inventory View for a second user")
    public void areFiltersFavoriteInIV() {
        List<String> favoriteFilters = advancedSearch.getFavoriteFilters();
        Assert.assertTrue(favoriteFilters.contains(FILTER_NAME));
    }
    
    @Test(priority = 17)
    @Description("Checking that filter have a proper value in Inventory View for a second user")
    public void isFilterHaveProperValue() {
        advancedSearch.selectSavedFilterByLabel(FILTER2_NAME);
        String idValue = advancedSearch.getComponent(ATTRIBUTE_ID, Input.ComponentType.TEXT_FIELD).getStringValue();
        
        Assert.assertEquals(idValue, VALUE_FOR_FILTER2);
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
        filterManagerPage.expandAllCategories();
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
        Assert.assertEquals(filterManagerPage.howManyFilters(), 0);
        Assert.assertEquals(filterManagerPage.howManyFolders(), 1);
    }
    
    private void closeMessages() {
        SystemMessageContainer.create(driver, webDriverWait).close();
    }
}

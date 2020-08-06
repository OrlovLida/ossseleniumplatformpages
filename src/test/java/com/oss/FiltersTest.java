package com.oss;


import com.oss.framework.utils.DelayUtils;
import com.oss.pages.filterpanel.FilterPanel;
import com.oss.pages.filterpanel.FilterSettingsFilter;
import com.oss.pages.platform.InventoryViewPage;
import com.oss.utils.TestListener;
import io.qameta.allure.Description;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners({TestListener.class})
public class FiltersTest extends BaseTestCase{

    private InventoryViewPage inventoryViewPage;
    private FilterPanel filterPanel;
    private FilterSettingsFilter filterSettingsFilter;
    private int filtersBefore;

    private String FILTER_NAME = "Id_od_first_object";
    private String FILTER2_NAME = "test2";
    private String FILTER3_NAME = "test3";

    @BeforeClass
    public void goToInventoryView() {
        inventoryViewPage = InventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, "Location");

    }

    @Test(priority = 1)
    @Description("Saving new filter")
    public void createNewFilter() {
        String id = inventoryViewPage.getIdOfFirstObject();
        inventoryViewPage
                .openFilterPanel()
                .openFilterSettings()
                .changeTabToFilters()
                .findFilterInSearch(FILTER_NAME);
        filterSettingsFilter = new FilterSettingsFilter(driver);
        filtersBefore = filterSettingsFilter.howManyFilters();
        filterPanel = new FilterPanel(driver);
        filterPanel.typeValueInLocationIdInput(id)
                .saveFilterAs(FILTER_NAME)
                .typeValueInLocationIdInput("666")
                .saveFilterAs(FILTER2_NAME);
        Assert.assertEquals(filterSettingsFilter.howManyFilters() - filtersBefore, 1);

    }

    @Test(priority = 2)
    @Description("Checking that filter is applied")
    public void isFilterApply() {
        filterSettingsFilter.selectFilter(FILTER_NAME)
                .applyFilter();
        Assert.assertTrue(filterPanel.isFilterApplied(FILTER_NAME));
        filterPanel
                .applyFilter();
        Assert.assertTrue(inventoryViewPage.isOnlyOneObject("666"));
    }

    @Test(priority = 3)
    @Description("Opening Saved Filter")
    public void openingSavedFilter(){
        inventoryViewPage
                .openFilterPanel()
                .typeValueInLocationIdInput("666")
                .saveFilterAs(FILTER3_NAME)
                .openFilterSettings()
                .changeTabToFilters()
                .markAsFavorite(FILTER3_NAME);
        DelayUtils.sleep(5000);
    }

    @Test(enabled = false)
    @Description("Adding filter to favorite")
    public void addingFilterToFavorite(){
        inventoryViewPage
                .openFilterPanel();
    }

    @Test(enabled = false)
    @Description("Editing an Existing Filter")
    public void editingAnExistingFilter(){
        inventoryViewPage
                .openFilterPanel();
    }

    @Test(enabled = false)
    @Description("Sharing an Existing Filter")
    public void sharingAnExistingFilter(){
        inventoryViewPage
                .openFilterPanel();
    }
}

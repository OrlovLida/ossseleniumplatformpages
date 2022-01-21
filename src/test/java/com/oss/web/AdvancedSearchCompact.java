package com.oss.web;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.assertj.core.api.Assertions;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.common.collect.Multimap;
import com.oss.BaseTestCase;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.inputs.ObjectSearchField;
import com.oss.framework.components.table.TableComponent;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.advancedsearch.AdvancedSearchWidget;
import com.oss.framework.widgets.table.TableWidget;
import com.oss.pages.filtermanager.FilterManagerPage;
import com.oss.pages.platform.NewInventoryViewPage;

public class AdvancedSearchCompact extends BaseTestCase {
    private final static String SIMPLE_ATTRIBUTE = "title";
    private final static String SIMPLE_ATTRIBUTE_LABEL = "Title";
    private final static String COMBO_ATTRIBUTE = "LifecycleState.lifecycleState";
    private final static String COMBO_ATTRIBUTE_ID = "gsLifecycleState";
    private final static String COMBO_ATTRIBUTE_LABEL = "Lifecycle State";
    private final static String OSF_ATTRIBUTE_LABEL = "director_OSF";
    private final static int DEFAULT_ROW_INDEX = 0;
    private final static String FILTER_NAME = "WEB_TEST_FILTER";
    private static final String LAST_NAME_ID = "lastName";
    private static final String PLACE_OF_BIRTH_NAME_COLUMN_ID = "placeOfBirth.name";
    private static final String FIRST_NAME_LABEL = "First Name";
    private static final String PLACE_OF_BIRTH_LABEL = "Place of Birth";
    private static final String ADVANCED_SEARCH_TABLE_ID = "advancedSearch";
    private static final String ACTORS_OSF_ID = "actors_OSF";
    private static final String PLACE_OF_BIRTH_OSF_ID = "placeOfBirth_OSF";
    private static final String DIRECTOR_LABEL = "Director";
    private static final String DIRECTOR_LAST_NAME_COLUMN_ID = "director.lastName";
    private static final String LAST_NAME_LABEL = "Last Name";
    private static final String ACTORS_ATTRIBUTE_ID = "actors";
    private static final String ACTORS_LABEL = "Actors";

    private NewInventoryViewPage inventoryViewPage;
    private TableWidget tableWidget;
    
    @BeforeClass
    public void goToInventoryView() {
        String TYPE = "TestMovie";
        inventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, TYPE);
        tableWidget = inventoryViewPage.getMainTable();
    }
    
    @AfterClass
    public void clear() {
        FilterManagerPage.goToFilterManagerPage(driver, BASIC_URL).expandAllCategories().deleteAllFilters();
    }
    
    private Predicate<String> containsValue(String val) {
        return str -> str.contains(val);
    }
    
    private ArrayList<String> getValuesFromTableByKey(String key) {
        ArrayList<String> attributeValues = new ArrayList<>();
        
        int rowsNumber = tableWidget.getRowsNumber();
        for (int i = 0; i < rowsNumber; i++) {
            attributeValues.add(tableWidget.getCellValue(i, key));
        }
        
        return attributeValues;
    }
    
    private boolean checkIfCellContainsValue(String columnId, String val) {
        return getValuesFromTableByKey(columnId).stream().allMatch(containsValue(val));
    }
    
    private String getSimpleValueFromTable() {
        return tableWidget.getCellValue(DEFAULT_ROW_INDEX, SIMPLE_ATTRIBUTE);
    }
    
    @Test(priority = 1)
    public void filterBySearchInput() {
        String attributeValue = tableWidget.getCellValue(DEFAULT_ROW_INDEX, SIMPLE_ATTRIBUTE);
        tableWidget.typeIntoSearch(attributeValue);
        
        DelayUtils.sleep(500);
        Assert.assertTrue(checkIfCellContainsValue(SIMPLE_ATTRIBUTE, attributeValue));
        tableWidget.typeIntoSearch("");
    }
    
    @Test(priority = 2)
    public void clearTags() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        inventoryViewPage.searchByAttributeValue(
                SIMPLE_ATTRIBUTE,
                tableWidget.getCellValue(DEFAULT_ROW_INDEX, SIMPLE_ATTRIBUTE),
                Input.ComponentType.TEXT_FIELD);
        
        inventoryViewPage.searchByAttributeValue(
                COMBO_ATTRIBUTE,
                tableWidget.getCellValue(DEFAULT_ROW_INDEX, COMBO_ATTRIBUTE_ID),
                Input.ComponentType.MULTI_COMBOBOX);
        
        Assertions.assertThat(inventoryViewPage.countOfVisibleTags()).isEqualTo(2);
        
        inventoryViewPage.clearFilter(SIMPLE_ATTRIBUTE_LABEL);
        Assert.assertEquals(inventoryViewPage.countOfVisibleTags(), 1);
        
        inventoryViewPage.clearFilter(COMBO_ATTRIBUTE_LABEL);
        Assert.assertEquals(inventoryViewPage.countOfVisibleTags(), 0);
    }
    
    @Test(priority = 3)
    public void filterBySimpleAttribute() {
        String attributeValue = getSimpleValueFromTable();
        Multimap<String, String> filters =
                inventoryViewPage.searchByAttributeValue(SIMPLE_ATTRIBUTE, attributeValue, Input.ComponentType.TEXT_FIELD);
        
        Assertions.assertThat(filters.keys()).hasSize(1);
        Assertions.assertThat(filters.get(SIMPLE_ATTRIBUTE_LABEL)).containsExactly(attributeValue);
        Assert.assertTrue(checkIfCellContainsValue(SIMPLE_ATTRIBUTE, attributeValue));
        
        inventoryViewPage.clearFilters();
    }
    
    @Test(priority = 4)
    public void filterByCombo() {
        String attributeValue = tableWidget.getCellValue(DEFAULT_ROW_INDEX, COMBO_ATTRIBUTE_ID);
        Multimap<String, String> filters =
                inventoryViewPage.searchByAttributeValue(COMBO_ATTRIBUTE, attributeValue, Input.ComponentType.MULTI_COMBOBOX);
        
        Assertions.assertThat(filters.keys()).hasSize(1);
        Assertions.assertThat(filters.get(COMBO_ATTRIBUTE_LABEL)).containsExactly(attributeValue);
        Assert.assertTrue(checkIfCellContainsValue(COMBO_ATTRIBUTE_ID, attributeValue));
        
        inventoryViewPage.clearFilters();
    }
    
    @Test(priority = 5)
    public void filterByOSF() {
        String attributeValueLabel = "Siân G. Lloyd";
        inventoryViewPage.enableColumn(LAST_NAME_LABEL, DIRECTOR_LABEL);
        Multimap<String, String> filters =
                inventoryViewPage.searchByAttributeValue(OSF_ATTRIBUTE_LABEL, attributeValueLabel, Input.ComponentType.OBJECT_SEARCH_FIELD);
        
        Assertions.assertThat(filters.keys()).hasSize(1);
        Assertions.assertThat(filters.get(DIRECTOR_LABEL)).containsExactly(attributeValueLabel);
        Assert.assertTrue(checkIfCellContainsValue(DIRECTOR_LAST_NAME_COLUMN_ID, attributeValueLabel));
        
        inventoryViewPage.clearFilters();
    }
    
    @Test(priority = 6)
    public void toggleVisibilitySearchAttribute() {
        List<String> attributes = new ArrayList<>();
        attributes.add(ACTORS_ATTRIBUTE_ID);
        inventoryViewPage.unselectVisibilitySearchAttributes(attributes);
        List<String> filters = inventoryViewPage.getAllVisibleFilters();
        
        Assert.assertFalse(filters.contains(ACTORS_LABEL));
        
        inventoryViewPage.selectVisibilitySearchAttributes(attributes);
        List<String> filtersSecond = inventoryViewPage.getAllVisibleFilters();
        Assert.assertTrue(filtersSecond.contains(ACTORS_LABEL));
    }
    
    @Test(priority = 7)
    public void createAndUseFilter() {
        String attributeValue = getSimpleValueFromTable();
        inventoryViewPage.searchByAttributeValue(SIMPLE_ATTRIBUTE, attributeValue, Input.ComponentType.TEXT_FIELD);
        
        tableWidget.saveAsNewFilter(FILTER_NAME);
        inventoryViewPage.clearFilter(SIMPLE_ATTRIBUTE_LABEL);
        
        tableWidget.chooseSavedFiltersByLabel(FILTER_NAME);
        DelayUtils.sleep(500);
        
        Assert.assertTrue(checkIfCellContainsValue(SIMPLE_ATTRIBUTE, attributeValue));
        
        inventoryViewPage.clearFilters();
    }
    
    @Test(priority = 8)
    public void markFilterAsFavByLabel() {
        tableWidget.markFilterAsFavByLabel(FILTER_NAME);
        tableWidget.setQuickFilter(FILTER_NAME);
        
        Assert.assertEquals(inventoryViewPage.countOfVisibleTags(), 1);
        
        inventoryViewPage.clearFilters();
    }
    //Disabled until fix OSSWEB-15793
    @Test(priority = 9, enabled = false)
    public void filterByOSFUsingAdvancedSearchWidget() {
        // given
        ObjectSearchField actorsOSF =
                (ObjectSearchField) tableWidget.getAdvancedSearch().getComponent(ACTORS_OSF_ID, Input.ComponentType.OBJECT_SEARCH_FIELD);
        AdvancedSearchWidget advancedSearchWidget = actorsOSF.openAdvancedSearchWidget();
        TableComponent tableComponent = advancedSearchWidget.getTableComponent(ADVANCED_SEARCH_TABLE_ID);
        String lastName = tableComponent.getCellValue(1, LAST_NAME_ID);
        String placeOfBirth = tableComponent.getCellValue(1, PLACE_OF_BIRTH_NAME_COLUMN_ID);
        advancedSearchWidget.getComponent(LAST_NAME_ID, Input.ComponentType.TEXT_FIELD).setSingleStringValue(lastName);
        advancedSearchWidget.getComponent(PLACE_OF_BIRTH_OSF_ID, Input.ComponentType.OBJECT_SEARCH_FIELD)
                .setSingleStringValue(placeOfBirth);
        // when
        Multimap<String, String> visibleTags = advancedSearchWidget.getAppliedFilters();
        // then
        Assertions.assertThat(visibleTags.keys()).contains(FIRST_NAME_LABEL);
        Assertions.assertThat(visibleTags.values()).contains(lastName);
        Assertions.assertThat(visibleTags.keys()).contains(PLACE_OF_BIRTH_LABEL);
        Assertions.assertThat(visibleTags.values()).contains(placeOfBirth);
        tableComponent.selectRow(0);
        advancedSearchWidget.clickAdd();

        Assertions.assertThat(actorsOSF.getStringValue()).isEqualTo(lastName);
        tableWidget.getAdvancedSearch().clickApply();
        Multimap<String, String> appliedFilters = tableWidget.getAppliedFilters();
        Assertions.assertThat(appliedFilters.keys()).contains(ACTORS_LABEL);
        Assertions.assertThat(appliedFilters.values()).contains(lastName);
    }
}

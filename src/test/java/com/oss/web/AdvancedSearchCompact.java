package com.oss.web;

import com.google.common.collect.Multimap;
import com.oss.BaseTestCase;
import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.tablewidget.TableWidget;
import com.oss.pages.filtermanager.FilterManagerPage;
import com.oss.pages.platform.NewInventoryViewPage;
import org.assertj.core.api.Assertions;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AdvancedSearchCompact extends BaseTestCase {
    private final static String SIMPLE_ATTRIBUTE = "title";
    private final static String SIMPLE_ATTRIBUTE_LABEL = "Title";
    private final static String COMBO_ATTRIBUTE = "LifecycleState.lifecycleState";
    private final static String COMBO_ATTRIBUTE_ID = "gsLifecycleState";
    private final static String COMBO_ATTRIBUTE_LABEL = "Lifecycle State";
    private final static String OSF_ATTRIBUTE_LABEL = "director_OSF";
    private final static int DEFAULT_ROW_INDEX = 0;
    private final static String FILTER_NAME = "WEB_TEST_FILTER";

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
        DelayUtils.waitForPageToLoad(driver,webDriverWait);
        Multimap<String, String> filterFirst = inventoryViewPage.searchByAttributeValue(
                SIMPLE_ATTRIBUTE,
                tableWidget.getCellValue(DEFAULT_ROW_INDEX, SIMPLE_ATTRIBUTE),
                Input.ComponentType.TEXT_FIELD
        );

        Multimap<String, String> filterSecond = inventoryViewPage.searchByAttributeValue(
                COMBO_ATTRIBUTE,
                tableWidget.getCellValue(DEFAULT_ROW_INDEX, COMBO_ATTRIBUTE_ID),
                Input.ComponentType.MULTI_COMBOBOX
        );


        Assertions.assertThat(Stream.concat(filterFirst.keys().stream(), filterSecond.keys().stream()).collect(Collectors.toList())).hasSize(2);

        inventoryViewPage.clearFilter(SIMPLE_ATTRIBUTE_LABEL);
        Assert.assertEquals(inventoryViewPage.countOfVisibleTags(), 1);

        inventoryViewPage.clearFilter(COMBO_ATTRIBUTE_LABEL);
        Assert.assertEquals(inventoryViewPage.countOfVisibleTags(), 0);
    }

    @Test(priority = 3)
    public void filterBySimpleAttribute() {
        String attributeValue = getSimpleValueFromTable();
        Multimap<String, String> filters = inventoryViewPage.searchByAttributeValue(SIMPLE_ATTRIBUTE, attributeValue, Input.ComponentType.TEXT_FIELD);

        Assertions.assertThat(filters.keys()).hasSize(1);
        Assertions.assertThat(filters.get(SIMPLE_ATTRIBUTE_LABEL)).containsExactly(attributeValue);
        Assert.assertTrue(checkIfCellContainsValue(SIMPLE_ATTRIBUTE, attributeValue));

        inventoryViewPage.clearFilters();
    }

    @Test(priority = 4)
    public void filterByCombo() {
        String attributeValue = tableWidget.getCellValue(DEFAULT_ROW_INDEX, COMBO_ATTRIBUTE_ID);
        Multimap<String, String> filters = inventoryViewPage.searchByAttributeValue(COMBO_ATTRIBUTE, attributeValue, Input.ComponentType.MULTI_COMBOBOX);

        Assertions.assertThat(filters.keys()).hasSize(1);
        Assertions.assertThat(filters.get(COMBO_ATTRIBUTE_LABEL)).containsExactly(attributeValue);
        Assert.assertTrue(checkIfCellContainsValue(COMBO_ATTRIBUTE_ID, attributeValue));

        inventoryViewPage.clearFilters();
    }

    @Test(priority = 5)
    public void filterByOSF() {
        String attributeValue = "241";
        inventoryViewPage.enableColumnAndApply("ID", "Director");
        Multimap<String, String> filters = inventoryViewPage.searchByAttributeValue(OSF_ATTRIBUTE_LABEL, attributeValue, Input.ComponentType.OBJECT_SEARCH_FIELD);

        Assertions.assertThat(filters.keys()).hasSize(1);
        Assertions.assertThat(filters.get("Director")).containsExactly(attributeValue);
        Assert.assertTrue(checkIfCellContainsValue("director.id", attributeValue));

        inventoryViewPage.clearFilters();
    }

    @Test(priority = 6)
    public void toggleVisibilitySearchAttribute() {
        List<String> attributes = new ArrayList<>();
        attributes.add("actors");
        inventoryViewPage.toggleVisibilitySearchAttributes(attributes);
        List<String> filters = inventoryViewPage.getMainTable().getAllVisibleFilters();

        Assert.assertTrue(filters.contains("Actors"));

        inventoryViewPage.toggleVisibilitySearchAttributes(attributes);
        List<String> filtersSecond = inventoryViewPage.getMainTable().getAllVisibleFilters();
        Assert.assertFalse(filtersSecond.contains("Actors"));
    }

    @Test(priority = 7)
    public void createAndUseFilter() {
        String attributeValue = getSimpleValueFromTable();
        inventoryViewPage.searchByAttributeValue(SIMPLE_ATTRIBUTE, attributeValue, Input.ComponentType.TEXT_FIELD);

        tableWidget.saveAsNewFilter(FILTER_NAME);
        inventoryViewPage.clearFilter(SIMPLE_ATTRIBUTE_LABEL);


        tableWidget.choseSavedFiltersByLabel(FILTER_NAME);
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
}

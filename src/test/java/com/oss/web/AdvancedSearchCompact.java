package com.oss.web;

import com.google.common.collect.Multimap;
import com.oss.BaseTestCase;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.widgets.tablewidget.TableWidget;
import com.oss.pages.platform.NewInventoryViewPage;
import org.assertj.core.api.Assertions;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AdvancedSearchCompact extends BaseTestCase {
    private final static String SIMPLE_ATTRIBUTE = "title";
    private final static String COMBO_ATTRIBUTE = "LifecycleState.lifecycleState";
    private final static String COMBO_ATTRIBUTE_LABEL = "gsLifecycleState";
    private final static String OSF_ATTRIBUTE_LABEL = "director_OSF";
    private final static int DEFAULT_ROW_INDEX = 1;

    private NewInventoryViewPage inventoryViewPage;
    private TableWidget tableWidget;

    @BeforeClass
    public void goToInventoryView() {
        String TYPE = "Movie";
        inventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, TYPE);
        tableWidget = inventoryViewPage.getMainTable();
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

    @Test
    public void filterBySearchInput() {
        String attributeValue = tableWidget.getCellValue(DEFAULT_ROW_INDEX, SIMPLE_ATTRIBUTE);
        tableWidget.typeIntoSearch(attributeValue);

        Assert.assertTrue(checkIfCellContainsValue(SIMPLE_ATTRIBUTE, attributeValue));
        tableWidget.typeIntoSearch("");
    }

    @Test
    public void clearTags() {
        Multimap<String, String> filterFirst = inventoryViewPage.searchByAttributeValue(
                SIMPLE_ATTRIBUTE,
                tableWidget.getCellValue(DEFAULT_ROW_INDEX, SIMPLE_ATTRIBUTE),
                Input.ComponentType.TEXT_FIELD
        );

        Multimap<String, String> filterSecond = inventoryViewPage.searchByAttributeValue(
                COMBO_ATTRIBUTE,
                tableWidget.getCellValue(DEFAULT_ROW_INDEX, COMBO_ATTRIBUTE_LABEL),
                Input.ComponentType.MULTI_COMBOBOX
        );


        Assertions.assertThat(Stream.concat(filterFirst.keys().stream(), filterSecond.keys().stream()).collect(Collectors.toList())).hasSize(2);

        inventoryViewPage.clearFilter(SIMPLE_ATTRIBUTE);
        Assert.assertEquals(inventoryViewPage.countOfVisibleTags(), 1);

        inventoryViewPage.clearFilter(COMBO_ATTRIBUTE_LABEL);
        Assert.assertEquals(inventoryViewPage.countOfVisibleTags(), 0);
    }

    @Test
    public void filterBySimpleAttribute() {
        String attributeValue = tableWidget.getCellValue(DEFAULT_ROW_INDEX, SIMPLE_ATTRIBUTE);
        Multimap<String, String> filters = inventoryViewPage.searchByAttributeValue(SIMPLE_ATTRIBUTE, attributeValue, Input.ComponentType.TEXT_FIELD);

        Assertions.assertThat(filters.keys()).hasSize(1);
        Assertions.assertThat(filters.get(SIMPLE_ATTRIBUTE)).containsExactly(attributeValue);
        Assert.assertTrue(checkIfCellContainsValue(SIMPLE_ATTRIBUTE, attributeValue));

        inventoryViewPage.clearFilters();
    }

    @Test
    public void filterByCombo() {
        String attributeValue = tableWidget.getCellValue(DEFAULT_ROW_INDEX, COMBO_ATTRIBUTE_LABEL);
        Multimap<String, String> filters = inventoryViewPage.searchByAttributeValue(COMBO_ATTRIBUTE, attributeValue, Input.ComponentType.MULTI_COMBOBOX);

        Assertions.assertThat(filters.keys()).hasSize(1);
        Assertions.assertThat(filters.get(COMBO_ATTRIBUTE_LABEL)).containsExactly(attributeValue);
        Assert.assertTrue(checkIfCellContainsValue(COMBO_ATTRIBUTE_LABEL, attributeValue));

        inventoryViewPage.clearFilters();
    }

    @Test
    public void filterByOSF() {
        String attributeValue = "36";
        inventoryViewPage.enableColumnAndApply("DM_Director.id", "DM_Movie.director");
        Multimap<String, String> filters = inventoryViewPage.searchByAttributeValue(OSF_ATTRIBUTE_LABEL, "36", Input.ComponentType.OBJECT_SEARCH_FIELD);

        Assertions.assertThat(filters.keys()).hasSize(1);
        Assertions.assertThat(filters.get("DM_Movie.director")).containsExactly(attributeValue);
        Assert.assertTrue(checkIfCellContainsValue("director.id", attributeValue));

        inventoryViewPage.clearFilters();
    }

    @Test
    public void toggleVisibilitySearchAttribute() {
        inventoryViewPage.toggleVisibilitySearchAttribute("actors");
        List<String> filters = inventoryViewPage.getMainTable().getAllVisibleFilters();

        Assert.assertFalse(filters.contains("actors"));

        inventoryViewPage.toggleVisibilitySearchAttribute("actors");
        List<String> filtersSecond = inventoryViewPage.getMainTable().getAllVisibleFilters();
        Assert.assertTrue(filtersSecond.contains("actors"));
    }
}

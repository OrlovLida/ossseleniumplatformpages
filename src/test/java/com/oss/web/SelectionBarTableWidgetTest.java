package com.oss.web;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.table.TableRow;
import com.oss.framework.widgets.table.TableWidget;
import com.comarch.oss.web.pages.NewInventoryViewPage;

public class SelectionBarTableWidgetTest extends BaseTestCase {

    private NewInventoryViewPage inventoryViewPage;
    private TableWidget tableWidget;
    private static final String ZERO_SELECTED = "0 selected";
    private static final String ONE_SELECTED = "1 selected";
    private static final String FOUR_SELECTED = "4 selected";
    private static final String THREE_SELECTED = "3 selected";
    private static final String FIVE_SELECTED = "5 selected";

    private static final String ATTRIBUTE_ID = "id";

    @BeforeClass
    public void goToInventoryView() {
        String TYPE = "TestMovie";
        inventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, TYPE);
        tableWidget = inventoryViewPage.getMainTable();
    }

    @Test(priority = 1)
    public void showSelectedRows() {
        inventoryViewPage.selectObjectByRowId(2);
        List<TableRow> selectedRows = inventoryViewPage.getSelectedRows();
        String selectedObjectCount = tableWidget.getSelectedObjectCount();

        Assertions.assertThat(selectedRows).hasSize(1);
        Assertions.assertThat(selectedObjectCount).isEqualTo(ONE_SELECTED);

        selectRowsOnNextPage(3, 5);
        selectRowsOnNextPage(4, 5);
        String selectedObjectCountAllPages = tableWidget.getSelectedObjectCount();

        Assertions.assertThat(selectedObjectCountAllPages).isEqualTo(FIVE_SELECTED);

        tableWidget.showOnlySelectedRows();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        Assertions.assertThat(tableWidget.getPagination().getTotalCount()).isEqualTo(5);
        Assert.assertTrue(inventoryViewPage.getMainTable().isHeaderCheckboxSelected());
    }

    @Test(priority = 2)
    public void unselectObjectByClickOnCheckbox() {
        inventoryViewPage.unselectObjectByRowId(2);

        List<TableRow> selectedRowsAfterUnselect = inventoryViewPage.getSelectedRows();
        String selectedObjectCountAfterUnselect = tableWidget.getSelectedObjectCount();

        Assertions.assertThat(selectedRowsAfterUnselect).hasSize(4);
        Assertions.assertThat(selectedObjectCountAfterUnselect).isEqualTo(FOUR_SELECTED);
        Assert.assertFalse(inventoryViewPage.getMainTable().isHeaderCheckboxSelected());

    }

    @Test(priority = 3)
    public void unselectAllObjects() {
        tableWidget.unselectAllRows();

        List<TableRow> selectedRowsAfterUnselectAll = inventoryViewPage.getSelectedRows();
        String selectedObjectCountAfterUnselectAll = tableWidget.getSelectedObjectCount();
        tableWidget.showAllRows();
        Assertions.assertThat(selectedRowsAfterUnselectAll).isEmpty();
        Assertions.assertThat(selectedObjectCountAfterUnselectAll).isEqualTo(ZERO_SELECTED);
    }

    @Test(priority = 4)
    public void showAllRows() {
        int allRows = inventoryViewPage.getMainTable().getPagination().getTotalCount();
        inventoryViewPage.selectObjectsByRowId(0, 2, 4, 6, 8);
        tableWidget.showOnlySelectedRows();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        tableWidget.showAllRows();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        int totalCount = tableWidget.getPagination().getTotalCount();
        tableWidget.unselectAllRows();
        Assertions.assertThat(totalCount).isEqualTo(allRows);
    }

    @Test(priority = 5)
    public void filteringAndShowSelected() {
        String attributeValue = inventoryViewPage.getAttributeValue(ATTRIBUTE_ID, 0);

        showSelectedAndFiltered(ATTRIBUTE_ID, attributeValue);

        String selectedObjectCount = tableWidget.getSelectedObjectCount();

        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assertions.assertThat(selectedObjectCount).isEqualTo(THREE_SELECTED);
        Assertions.assertThat(tableWidget.getPagination().getTotalCount()).isEqualTo(1);

    }

    @Test(priority = 6)
    public void showSelectedAndClearFilters() {
        inventoryViewPage.clearFilters();
        int totalCount = inventoryViewPage.getMainTable().getPagination().getTotalCount();
        tableWidget.unselectAllRows();
        tableWidget.showAllRows();
        Assertions.assertThat(totalCount).isEqualTo(3);
    }

    @Test(priority = 7)
    public void filteringAndShowAll() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        int allRows = tableWidget.getPagination().getTotalCount();
        String attributeValue = inventoryViewPage.getAttributeValue(ATTRIBUTE_ID, 0);

        showSelectedAndFiltered(ATTRIBUTE_ID, attributeValue);

        tableWidget.showAllRows();

        String selectedObjectCountAfterShowAll = tableWidget.getSelectedObjectCount();

        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assertions.assertThat(selectedObjectCountAfterShowAll).isEqualTo(THREE_SELECTED);
        Assertions.assertThat(tableWidget.getPagination().getTotalCount()).isEqualTo(1);

        inventoryViewPage.clearFilters();
        int totalCount = tableWidget.getPagination().getTotalCount();
        tableWidget.unselectAllRows();
        Assertions.assertThat(totalCount).isEqualTo(allRows);
    }

    @Test(priority = 8)
    public void filteringAndUnselectAll() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        int allRows = tableWidget.getPagination().getTotalCount();
        String attributeValue = inventoryViewPage.getAttributeValue(ATTRIBUTE_ID, 0);

        showSelectedAndFiltered(ATTRIBUTE_ID, attributeValue);
        tableWidget.unselectAllRows();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        String selectedObjectCountAfterUnselectAll = tableWidget.getSelectedObjectCount();

        Assertions.assertThat(selectedObjectCountAfterUnselectAll).isEqualTo(ZERO_SELECTED);
        Assertions.assertThat(tableWidget.getPagination().getTotalCount()).isEqualTo(1);

        inventoryViewPage.clearFilters();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        Assertions.assertThat(tableWidget.getPagination().getTotalCount()).isEqualTo(3);

        tableWidget.showAllRows();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        Assertions.assertThat(tableWidget.getPagination().getTotalCount()).isEqualTo(allRows);

    }

    // Disabled until fix OSSWEB-20724
    @Test(priority = 9, enabled = false)
    public void checkHeaderCheckboxState() {
        tableWidget.selectAllRows();
        Assert.assertTrue(inventoryViewPage.getMainTable().isHeaderCheckboxSelected());
        inventoryViewPage.unselectObjectByRowId(1);
        Assert.assertFalse(tableWidget.isHeaderCheckboxSelected());
        inventoryViewPage.selectObjectByRowId(1);
        Assert.assertTrue(tableWidget.isHeaderCheckboxSelected());
        inventoryViewPage.getMainTable().unselectAllRows();
    }

    // Disabled until fix OSSWEB-20724
    @Test(priority = 10, enabled = false)
    public void checkHeaderCheckboxStateAfterShowSelected() {
        tableWidget.selectAllRows();
        tableWidget.showOnlySelectedRows();
        Assert.assertTrue(tableWidget.isHeaderCheckboxSelected());
        inventoryViewPage.unselectObjectByRowId(0);
        Assert.assertFalse(tableWidget.isHeaderCheckboxSelected());
    }

    private void showSelectedAndFiltered(String attributeId, String attributeValue) {
        inventoryViewPage.searchByAttributeValue(attributeId, attributeValue, Input.ComponentType.NUMBER_FIELD);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        inventoryViewPage.selectObjectByRowId(0);
        inventoryViewPage.clearFilters();

        inventoryViewPage.selectObjectsByRowId(10, 11);

        inventoryViewPage.searchByAttributeValue(attributeId, attributeValue, Input.ComponentType.NUMBER_FIELD);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        tableWidget.showOnlySelectedRows();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    private void selectRowsOnNextPage(int... index) {
        tableWidget.getPagination().goOnNextPage();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        inventoryViewPage.selectObjectsByRowId(index);
    }

}


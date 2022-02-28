package com.oss.web;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.table.TableRow;
import com.oss.framework.widgets.table.TableWidget;
import com.oss.pages.platform.NewInventoryViewPage;

public class SelectionBarTableWidgetTest extends BaseTestCase {

    private NewInventoryViewPage inventoryViewPage;
    private TableWidget tableWidget;
    String zeroSelected = "0 selected";
    String oneSelected = "1 selected";
    String fiveSelected = "5 selected";
    String threeSelected = "3 selected";

    @BeforeClass
    public void goToInventoryView() {
        String TYPE = "TestMovie";
        inventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, TYPE);
        tableWidget = inventoryViewPage.getMainTable();
    }

    @Test(priority = 1)
    public void selectOneRow() {
        inventoryViewPage.selectObjectByRowId(0);
        List<TableRow> selectedRows = inventoryViewPage.getSelectedRows();

        final int allRows = tableWidget.getPagination().getTotalCount();

        String selectedObjectCount = tableWidget.getSelectedObjectCount();

        tableWidget.showOnlySelectedRows();

        Assertions.assertThat(selectedRows).hasSize(1);
        Assertions.assertThat(selectedRows.get(0).getIndex()).isZero();
        Assertions.assertThat(selectedObjectCount).isEqualTo(oneSelected);
        Assertions.assertThat(tableWidget.getPagination().getTotalCount()).isEqualTo(1);

        tableWidget.showAllRows();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        Assertions.assertThat(tableWidget.getPagination().getTotalCount()).isEqualTo(allRows);

        tableWidget.unselectAllRows();

        selectedRows = inventoryViewPage.getSelectedRows();
        Assertions.assertThat(selectedRows).isEmpty();
    }

    @Test(priority = 2)
    public void unselectobjectByClickOnCheckboxes() {

        inventoryViewPage.selectObjectByRowId(3);
        inventoryViewPage.selectObjectByRowId(6);

        List<TableRow> selectedRows = inventoryViewPage.getSelectedRows();
        String selectedObjectCount = tableWidget.getSelectedObjectCount();

        Assertions.assertThat(selectedRows).hasSize(2);
        Assertions.assertThat(selectedObjectCount).isEqualTo("2 selected");

        inventoryViewPage.unselectObjectByRowId(3);

        List<TableRow> selectedRowsAfterChange1 = inventoryViewPage.getSelectedRows();
        String selectedObjectCountAfterChange1 = tableWidget.getSelectedObjectCount();

        Assertions.assertThat(selectedRowsAfterChange1).hasSize(1);
        Assertions.assertThat(selectedObjectCountAfterChange1).isEqualTo(oneSelected);

        inventoryViewPage.unselectObjectByRowId(6);

        List<TableRow> selectedRowsAfterChange2 = inventoryViewPage.getSelectedRows();
        String selectedObjectCountAfterChange2 = tableWidget.getSelectedObjectCount();

        Assertions.assertThat(selectedRowsAfterChange2).isEmpty();
        Assertions.assertThat(selectedObjectCountAfterChange2).isEqualTo(zeroSelected);

    }

    @Test(priority = 3)
    public void unselectAllObjects() {
        for (int i = 0; i < 13; i = i + 3) {
            inventoryViewPage.selectObjectByRowId(i);
        }

        List<TableRow> selectedRows = inventoryViewPage.getSelectedRows();
        String selectedObjectCount = tableWidget.getSelectedObjectCount();

        Assertions.assertThat(selectedRows).hasSize(5);

        Assertions.assertThat(selectedObjectCount).isEqualTo(fiveSelected);

        tableWidget.unselectAllRows();

        List<TableRow> selectedRowsAfterUnselect = inventoryViewPage.getSelectedRows();
        String selectedObjectCountAfterUnselect = tableWidget.getSelectedObjectCount();

        Assertions.assertThat(selectedRowsAfterUnselect).isEmpty();
        Assertions.assertThat(selectedObjectCountAfterUnselect).isEqualTo(zeroSelected);

    }

    @Test(priority = 4)
    public void selectionsFromDifferentPages() {

        final int allRows = tableWidget.getPagination().getTotalCount();
        inventoryViewPage.selectObjectByRowId(2);

        List<TableRow> selectedRows = inventoryViewPage.getSelectedRows();
        String selectedObjectCount = tableWidget.getSelectedObjectCount();

        Assertions.assertThat(selectedRows).hasSize(1);

        Assertions.assertThat(selectedObjectCount).isEqualTo(oneSelected);

        tableWidget.getPagination().goOnNextPage();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        inventoryViewPage.selectObjectByRowId(3);
        inventoryViewPage.selectObjectByRowId(5);

        tableWidget.getPagination().goOnNextPage();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        inventoryViewPage.selectObjectByRowId(4);
        inventoryViewPage.selectObjectByRowId(5);

        String selectedObjectCountAllPages = tableWidget.getSelectedObjectCount();

        Assertions.assertThat(selectedObjectCountAllPages).isEqualTo(fiveSelected);

        tableWidget.showOnlySelectedRows();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        Assertions.assertThat(tableWidget.getPagination().getTotalCount()).isEqualTo(5);

        tableWidget.showAllRows();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        Assertions.assertThat(tableWidget.getPagination().getTotalCount()).isEqualTo(allRows);

        inventoryViewPage.unselectObjectByRowId(2);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        String selectedObjectCountAfterUnselect = tableWidget.getSelectedObjectCount();

        Assertions.assertThat(selectedObjectCountAfterUnselect).isEqualTo("4 selected");

        tableWidget.unselectAllRows();

        List<TableRow> selectedRowsAfterUnselectAll = inventoryViewPage.getSelectedRows();
        String selectedObjectCountAfterUnselectAll = tableWidget.getSelectedObjectCount();

        Assertions.assertThat(selectedRowsAfterUnselectAll).isEmpty();
        Assertions.assertThat(selectedObjectCountAfterUnselectAll).isEqualTo(zeroSelected);

    }

    @Test(priority = 5)
    public void filteringAndShowSelected() {

        String attributeId = "id";
        String attributeValue = "3";

        final int allRows = tableWidget.getPagination().getTotalCount();

        inventoryViewPage.searchByAttributeValue(attributeId, attributeValue, Input.ComponentType.NUMBER_FIELD);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        inventoryViewPage.selectObjectByRowId(0);
        inventoryViewPage.clearFilters();

        inventoryViewPage.selectObjectByRowId(10);
        inventoryViewPage.selectObjectByRowId(11);

        inventoryViewPage.searchByAttributeValue(attributeId, attributeValue, Input.ComponentType.NUMBER_FIELD);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        String selectedObjectCount = tableWidget.getSelectedObjectCount();

        tableWidget.showOnlySelectedRows();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        Assertions.assertThat(selectedObjectCount).isEqualTo(threeSelected);
        Assertions.assertThat(tableWidget.getPagination().getTotalCount()).isEqualTo(1);

        inventoryViewPage.clearFilters();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        Assertions.assertThat(selectedObjectCount).isEqualTo(threeSelected);
        Assertions.assertThat(tableWidget.getPagination().getTotalCount()).isEqualTo(3);

        tableWidget.showAllRows();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        String selectedObjectCountAfterShowAll = tableWidget.getSelectedObjectCount();

        Assertions.assertThat(selectedObjectCountAfterShowAll).isEqualTo(threeSelected);
        Assertions.assertThat(tableWidget.getPagination().getTotalCount()).isEqualTo(allRows);

        inventoryViewPage.searchByAttributeValue(attributeId, attributeValue, Input.ComponentType.NUMBER_FIELD);
        tableWidget.unselectAllRows();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        String selectedObjectCountAfterUnselectAll = tableWidget.getSelectedObjectCount();

        Assertions.assertThat(selectedObjectCountAfterUnselectAll).isEqualTo(zeroSelected);
        Assertions.assertThat(tableWidget.getPagination().getTotalCount()).isEqualTo(1);

        inventoryViewPage.clearFilters();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        String selectedObjectCountAfterClearFilters = tableWidget.getSelectedObjectCount();

        Assertions.assertThat(selectedObjectCountAfterClearFilters).isEqualTo(zeroSelected);
        Assertions.assertThat(tableWidget.getPagination().getTotalCount()).isEqualTo(allRows);

    }

}


package com.oss.web;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.jetbrains.annotations.NotNull;
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
    private static final String zeroSelected = "0 selected";
    private static final String oneSelected = "1 selected";
    private static final String twoSelected = "2 selected";
    private static final String threeSelected = "3 selected";
    private static final String fiveSelected = "5 selected";

    private static String attributeId = "id";
    private static String attributeValue = "3";

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
        Assertions.assertThat(selectedObjectCount).isEqualTo(oneSelected);

        selectRowsOnNextPage(3, 5);
        selectRowsOnNextPage(4, 5);

        String selectedObjectCountAllPages = tableWidget.getSelectedObjectCount();

        Assertions.assertThat(selectedObjectCountAllPages).isEqualTo(fiveSelected);

        tableWidget.showOnlySelectedRows();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        Assertions.assertThat(tableWidget.getPagination().getTotalCount()).isEqualTo(5);
    }

    @Test(priority = 2)
    public void showAllRows() {
        int allRows = tableWidget.getPagination().getTotalCount();

        inventoryViewPage.selectObjectByRowId(2);
        selectRowsOnNextPage(3, 5);
        selectRowsOnNextPage(4, 5);

        tableWidget.showOnlySelectedRows();

        tableWidget.showAllRows();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        Assertions.assertThat(tableWidget.getPagination().getTotalCount()).isEqualTo(allRows);
    }

    @Test(priority = 3)
    public void unselectObjectByClickOnCheckbox() {
        inventoryViewPage.selectSeveralObjectsByRowId(3, 4, 9);

        List<TableRow> selectedRows = inventoryViewPage.getSelectedRows();
        String selectedObjectCount = tableWidget.getSelectedObjectCount();

        Assertions.assertThat(selectedRows).hasSize(3);
        Assertions.assertThat(selectedObjectCount).isEqualTo(threeSelected);

        inventoryViewPage.unselectObjectByRowId(4);

        List<TableRow> selectedRowsAfterUnselect = inventoryViewPage.getSelectedRows();
        String selectedObjectCountAfterUnselect = tableWidget.getSelectedObjectCount();

        Assertions.assertThat(selectedRowsAfterUnselect).hasSize(2);
        Assertions.assertThat(selectedObjectCountAfterUnselect).isEqualTo(twoSelected);

    }

    @Test(priority = 4)
    public void unselectAllObjects() {
        inventoryViewPage.selectSeveralObjectsByRowId(2, 4, 5);
        selectRowsOnNextPage(1, 2);

        inventoryViewPage.unselectObjectByRowId(2);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        tableWidget.unselectAllRows();

        List<TableRow> selectedRowsAfterUnselectAll = inventoryViewPage.getSelectedRows();
        String selectedObjectCountAfterUnselectAll = tableWidget.getSelectedObjectCount();

        Assertions.assertThat(selectedRowsAfterUnselectAll).isEmpty();
        Assertions.assertThat(selectedObjectCountAfterUnselectAll).isEqualTo(zeroSelected);

    }

    @Test(priority = 5)
    public void filteringAndShowSelected() {

        showSelectedAndFiltered(attributeId, attributeValue);

        String selectedObjectCount = tableWidget.getSelectedObjectCount();

        checkIfThreeSelected(selectedObjectCount);
        Assertions.assertThat(tableWidget.getPagination().getTotalCount()).isEqualTo(1);

        inventoryViewPage.clearFilters();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        Assertions.assertThat(tableWidget.getPagination().getTotalCount()).isEqualTo(3);

    }

    @Test(priority = 6)
    public void filteringAndShowAll() {
        int allRows = tableWidget.getPagination().getTotalCount();

        showSelectedAndFiltered(attributeId, attributeValue);
        tableWidget.showAllRows();

        String selectedObjectCountAfterShowAll = tableWidget.getSelectedObjectCount();

        checkIfThreeSelected(selectedObjectCountAfterShowAll);
        Assertions.assertThat(tableWidget.getPagination().getTotalCount()).isEqualTo(1);

        inventoryViewPage.clearFilters();
        Assertions.assertThat(tableWidget.getPagination().getTotalCount()).isEqualTo(allRows);
    }

    @Test(priority = 8)
    public void filteringAndUnselectAll() {
        int allRows = tableWidget.getPagination().getTotalCount();
        showSelectedAndFiltered(attributeId, attributeValue);
        tableWidget.unselectAllRows();

        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        String selectedObjectCountAfterUnselectAll = tableWidget.getSelectedObjectCount();

        Assertions.assertThat(selectedObjectCountAfterUnselectAll).isEqualTo(zeroSelected);
        Assertions.assertThat(tableWidget.getPagination().getTotalCount()).isEqualTo(1);

        inventoryViewPage.clearFilters();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        Assertions.assertThat(tableWidget.getPagination().getTotalCount()).isEqualTo(3);

        tableWidget.showAllRows();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        Assertions.assertThat(tableWidget.getPagination().getTotalCount()).isEqualTo(allRows);

    }

    private void showSelectedAndFiltered(String attributeId, String attributeValue) {
        inventoryViewPage.searchByAttributeValue(attributeId, attributeValue, Input.ComponentType.NUMBER_FIELD);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        inventoryViewPage.selectObjectByRowId(0);
        inventoryViewPage.clearFilters();

        inventoryViewPage.selectSeveralObjectsByRowId(10, 11);

        inventoryViewPage.searchByAttributeValue(attributeId, attributeValue, Input.ComponentType.NUMBER_FIELD);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        tableWidget.openSelectionBar();
        tableWidget.showOnlySelectedRows();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

    }

    private void selectRowsOnNextPage(int @NotNull ... index) {
        tableWidget.getPagination().goOnNextPage();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        inventoryViewPage.selectSeveralObjectsByRowId(index);
    }

    private void checkIfThreeSelected(String selectedObjectCount) {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assertions.assertThat(selectedObjectCount).isEqualTo(threeSelected);
    }

}


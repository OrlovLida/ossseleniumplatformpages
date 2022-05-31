package com.oss.web;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.components.pagination.PaginationComponent;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.table.TableRow;
import com.oss.framework.widgets.treetable.TreeTableWidget;
import com.oss.pages.bpm.PlannersViewPage;

/**
 * @author Faustyna Szczepanik
 */

public class TreeTableWidgetTest extends BaseTestCase {
    private static final String PRIORITY_COLUMN_ID = "priority";
    private static final String DOMAIN_COLUMN_ID = "Domain";
    private static final int PAGE_SIZE_OPTION_100 = 100;
    private static final int PAGE_SIZE_OPTION_50 = 50;

    private PlannersViewPage plannersViewPage;
    private TreeTableWidget treeTableWidget;

    @BeforeClass
    public void goToPlannersView() {
        plannersViewPage = PlannersViewPage.goToPlannersViewPage(driver, BASIC_URL);
        treeTableWidget = plannersViewPage.getTreeTable();
    }

    @Test(priority = 1)
    public void selectFirstRow() {
        plannersViewPage.selectObjectByRowId(0);
        List<TableRow> selectedRows = plannersViewPage.getSelectedRows();
        Assert.assertEquals(selectedRows.size(), 1);
        Assert.assertEquals(selectedRows.get(0).getIndex(), 0);
    }

    @Test(priority = 2)
    public void unselectFirstRow() {
        plannersViewPage.unselectObjectByRowId(0);
        List<TableRow> selectedRows = plannersViewPage.getSelectedRows();
        Assert.assertTrue(selectedRows.isEmpty());
    }

    @Test(priority = 3)
    public void expandNode() {
        plannersViewPage.expandNode(0);
        Assert.assertTrue(plannersViewPage.isNodeExpanded(0));
    }

    @Test(priority = 4)
    public void collapseNode() {
        plannersViewPage.collapseNode(0);
        Assert.assertFalse(plannersViewPage.isNodeExpanded(0));
    }

    @Test(priority = 5)
    public void reorderingColumns() {
        List<String> columnHeaders = plannersViewPage.getActiveColumnsHeaders();
        String firstHeader = columnHeaders.get(0);
        String secondHeader = columnHeaders.get(1);
        String thirdHeader = columnHeaders.get(2);

        plannersViewPage.changeColumnsOrderInTreeTable(firstHeader, 2);
        List<String> newHeaders = plannersViewPage.getActiveColumnsHeaders();

        Assert.assertEquals(newHeaders.indexOf(firstHeader), 2);
        Assert.assertEquals(newHeaders.indexOf(secondHeader), 0);
        Assert.assertEquals(newHeaders.indexOf(thirdHeader), 1);
    }

    @Test(priority = 6)
    public void deleteAndAddColumn() {
        List<String> columnHeaders = plannersViewPage.getActiveColumnsHeaders();
        String secondHeader = columnHeaders.get(1);
        plannersViewPage.disableColumnAndApply(secondHeader);
        columnHeaders = plannersViewPage.getActiveColumnsHeaders();
        Assert.assertFalse(columnHeaders.contains(secondHeader));

        plannersViewPage.enableColumn(secondHeader);
        columnHeaders = plannersViewPage.getActiveColumnsHeaders();
        Assert.assertTrue(columnHeaders.contains(secondHeader));
    }

    @Test(priority = 7)
    public void changeDefaultColumnWidth() {
        int defaultColumnSize = plannersViewPage.getColumnSize(PRIORITY_COLUMN_ID);
        String newColumnSize = String.valueOf(100 + defaultColumnSize);
        treeTableWidget.setColumnWidth(PRIORITY_COLUMN_ID, newColumnSize);
        Assert.assertEquals(plannersViewPage.getColumnSize(PRIORITY_COLUMN_ID), Integer.parseInt(newColumnSize));
        driver.navigate().refresh();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        int columnSizeRefresh = plannersViewPage.getColumnSize(PRIORITY_COLUMN_ID);
        Assert.assertEquals(columnSizeRefresh, Integer.parseInt(newColumnSize));
    }

    @Test(priority = 8)
    public void paginationInitialStatus() {
        Assert.assertFalse(treeTableWidget.getPagination().isFirstPageButtonPresent());
        Assert.assertFalse(treeTableWidget.getPagination().isPreviousPageButtonPresent());
        Assert.assertTrue(treeTableWidget.getPagination().isNextPageButtonPresent());
    }

    @Test(priority = 9)
    public void goOnNextPage() {
        treeTableWidget.getPagination().goOnNextPage();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertTrue(treeTableWidget.getPagination().isFirstPageButtonPresent());
        Assert.assertTrue(treeTableWidget.getPagination().isPreviousPageButtonPresent());
        Assert.assertEquals(treeTableWidget.getPagination().getBottomRageOfRows(), treeTableWidget.getPagination().getStep() + 1);
        Assert.assertEquals(treeTableWidget.getPagination().getTopRageOfRows(), treeTableWidget.getPagination().getStep() * 2);
    }

    @Test(priority = 10)
    public void goOnPreviousPage() {
        treeTableWidget.getPagination().goOnNextPage();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        treeTableWidget.getPagination().goOnPrevPage();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertEquals(treeTableWidget.getPagination().getBottomRageOfRows(), treeTableWidget.getPagination().getStep() + 1);
        Assert.assertEquals(treeTableWidget.getPagination().getTopRageOfRows(), treeTableWidget.getPagination().getStep() * 2);
    }

    @Test(priority = 11)
    public void backToFirstPage() {
        treeTableWidget.getPagination().goOnFirstPage();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        treeTableWidget.getPagination().goOnPrevPage();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertEquals(treeTableWidget.getPagination().getBottomRageOfRows(), 1);
        Assert.assertEquals(treeTableWidget.getPagination().getTopRageOfRows(), treeTableWidget.getPagination().getStep());
    }

    @Test(priority = 12)
    public void changeItemsPerPage() {
        plannersViewPage.setPagination(PAGE_SIZE_OPTION_100);
        Assert.assertEquals(getRowsCount(), PAGE_SIZE_OPTION_100);
        driver.navigate().refresh();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertEquals(getRowsCount(), PAGE_SIZE_OPTION_100);
    }

    @Test(priority = 13)
    public void backToDefaultColumnSettings() {
        plannersViewPage.setDefaultSettings();
        Assert.assertEquals(getRowsCount(), PAGE_SIZE_OPTION_50);

        List<String> defaultActiveHeaders = treeTableWidget.getActiveColumnIds();
        String defaultFirstColumn = treeTableWidget.getActiveColumnIds().get(0);
        int defaultFirstColumnSize = plannersViewPage.getColumnSize(defaultFirstColumn);
        String defaultForthColumn = treeTableWidget.getActiveColumnIds().get(3);

        treeTableWidget.changeColumnsOrderById(defaultForthColumn, 2);
        treeTableWidget.setColumnWidth(defaultFirstColumn, String.valueOf(defaultFirstColumnSize - 50));
        plannersViewPage.enableColumn(DOMAIN_COLUMN_ID);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        plannersViewPage.setDefaultSettings();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        List<String> activeColumnsHeaders = plannersViewPage.getTreeTable().getActiveColumnIds();

        Assert.assertFalse(activeColumnsHeaders.contains(DOMAIN_COLUMN_ID));
        Assert.assertEquals(defaultFirstColumn, activeColumnsHeaders.get(0));
        Assert.assertEquals(plannersViewPage.getColumnSize(activeColumnsHeaders.get(0)), defaultFirstColumnSize);
        Assert.assertEquals(defaultActiveHeaders, activeColumnsHeaders);

    }

    private int getRowsCount() {
        treeTableWidget = plannersViewPage.getTreeTable();
        PaginationComponent pagination = treeTableWidget.getPagination();
        return pagination.getRowsCount();
    }

}

package com.oss.pages.bpm;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.attributechooser.AttributesChooser;
import com.oss.framework.components.pagination.PaginationComponent;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.table.TableRow;
import com.oss.framework.widgets.treetable.TreeTableWidget;
import com.oss.pages.BasePage;

public class PlannersViewPage extends BasePage {

    private static final String TREE_TABLE_ID = "process_instance_hierarchy_table";

    public PlannersViewPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public static PlannersViewPage goToPlannersViewPage(WebDriver driver, String basicURL) {
        driver.get(String.format("%s/#/views/management/views/ProcessInstances?perspective=LIVE", basicURL));
        WebDriverWait wait = new WebDriverWait(driver, 45);
        DelayUtils.waitForPageToLoad(driver, wait);
        return new PlannersViewPage(driver, wait);
    }

    public TreeTableWidget getTreeTable() {
        DelayUtils.waitForPageToLoad(driver, wait);
        return TreeTableWidget.createById(driver, wait, TREE_TABLE_ID);

    }

    public void selectObjectByRowId(int rowId) {
        TreeTableWidget treeTable = getTreeTable();
        treeTable.selectNode(rowId);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public void unselectObjectByRowId(int rowId) {
        TreeTableWidget treeTable = getTreeTable();
        treeTable.unselectNode(rowId);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public List<TableRow> getSelectedRows() {
        return getTreeTable().getSelectedRows();
    }

    public String getAttributeValue(String columnId, int rowId) {
        TreeTableWidget treeTable = getTreeTable();
        return treeTable.getCellValue(rowId, columnId);
    }

    public void expandNode(String label, String columnId) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getTreeTable().expandNode(label, columnId);
    }

    public void expandNode(int index) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getTreeTable().expandNode(index);
    }

    public void collapseNode(int index) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getTreeTable().collapseNode(index);
    }

    public boolean isNodeExpanded(int index) {
        DelayUtils.waitForPageToLoad(driver, wait);
        return getTreeTable().isRowExpanded(index);
    }

    public List<String> getActiveColumnsHeaders() {
        TreeTableWidget treeTable = getTreeTable();
        return treeTable.getActiveColumnHeaders();
    }

    public void changeColumnsOrderInTreeTable(String columnLabel, int position) {
        getTreeTable().changeColumnsOrder(columnLabel, position);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public void enableColumn(String columnLabel, String... path) {
        AttributesChooser attributesChooser = getTreeTable().getAttributesChooser();
        attributesChooser.enableAttributeByLabel(columnLabel, path);
        attributesChooser.clickApply();
    }

    public void disableColumnAndApply(String columnLabel) {
        TreeTableWidget treeTable = getTreeTable();
        treeTable.disableColumnByLabel(columnLabel);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public int getColumnSize(String columnId) {
        int columnIndex = getTreeTable().getActiveColumnIds().indexOf(columnId);
        return getTreeTable().getColumnSize(columnIndex);
    }

    public void setPagination(int paginationValue) {
        PaginationComponent pagination = getTreeTable().getPagination();
        pagination.changeRowsCount(paginationValue);
    }

    public void setDefaultSettings() {
        getTreeTable().getAttributesChooser().clickDefaultSettings();
    }

}

package com.oss.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.assertj.core.api.Assertions;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.components.common.AttributesChooser;
import com.oss.framework.components.common.PaginationComponent;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.tablewidget.TableRow;
import com.oss.framework.widgets.tablewidget.TableWidget;
import com.oss.pages.platform.NewInventoryViewPage;

public class TableWidgetTest extends BaseTestCase {
    private static final int PAGE_SIZE_OPTION_200 = 200;
    private static final int PAGE_SIZE_OPTION_100 = 100;
    private static final int PAGE_SIZE_OPTION_50 = 50;
    private static final String TYPE_DIRECTOR_COLUMN_ID = "director.id";
    private static final String MOVIE_LENGTH_COLUMN_ID = "movieLength";
    private NewInventoryViewPage inventoryViewPage;
    private TableWidget tableWidget;
    
    private ArrayList<String> getValuesFromTableByKey(String key) {
        ArrayList<String> attributeValues = new ArrayList<>();
        
        int rowsNumber = tableWidget.getRowsNumber();
        for (int i = 0; i < rowsNumber; i++) {
            attributeValues.add(tableWidget.getCellValue(i, key));
        }
        
        return attributeValues;
    }
    
    @BeforeClass
    public void goToInventoryView() {
        String TYPE = "TestMovie";
        inventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, TYPE);
        tableWidget = inventoryViewPage.getMainTable();
    }
    
    @Test(priority = 1)
    public void selectFirstRow() {
        // when
        inventoryViewPage.selectObjectByRowId(0);
        List<TableRow> selectedRows = inventoryViewPage.getSelectedRows();
        
        // then
        Assertions.assertThat(selectedRows).hasSize(1);
        Assertions.assertThat(selectedRows.get(0).getIndex()).isEqualTo(0);
        
        inventoryViewPage.unselectObjectByRowId(0);
        selectedRows = inventoryViewPage.getSelectedRows();
        Assertions.assertThat(selectedRows).hasSize(0);
    }
    
    @Test(priority = 2)
    public void resizeColumn() {
        com.oss.framework.widgets.tablewidget.TableWidget tableWidget = inventoryViewPage.getMainTable();
        int defaultSize = tableWidget.getFirstColumnSize();
        DelayUtils.sleep(DelayUtils.HUMAN_REACTION_MS);
        Assertions.assertThat(defaultSize).isEqualTo(200);
        
        int offset = 400;
        tableWidget.resizeFirstColumn(offset);
        DelayUtils.sleep(DelayUtils.HUMAN_REACTION_MS);
        
        int newSize = tableWidget.getFirstColumnSize();
        Assertions.assertThat(defaultSize + offset).isEqualTo(newSize);
        
        tableWidget.resizeFirstColumn(-offset);
        newSize = tableWidget.getFirstColumnSize();
        Assertions.assertThat(defaultSize).isEqualTo(newSize);
    }
    
    @Test(priority = 3)
    public void changeColumnsOrder() {
        List<String> columnHeaders = inventoryViewPage.getActiveColumnsHeaders();
        
        String firstHeader = columnHeaders.get(0);
        String secondHeader = columnHeaders.get(1);
        String thirdHeader = columnHeaders.get(2);
        
        inventoryViewPage.changeColumnsOrderInMainTable(firstHeader, 2);
        
        List<String> newHeaders = inventoryViewPage.getActiveColumnsHeaders();
        
        Assertions.assertThat(newHeaders.indexOf(firstHeader)).isEqualTo(2);
        Assertions.assertThat(newHeaders.indexOf(secondHeader)).isEqualTo(0);
        Assertions.assertThat(newHeaders.indexOf(thirdHeader)).isEqualTo(1);
    }
    
    @Test(priority = 4)
    public void addFirstUnselectedColumn() {
        // given
        List<String> columnHeaders = inventoryViewPage.getActiveColumnsHeaders();
        String firstHeader = columnHeaders.get(1);
        
        // when
        inventoryViewPage.disableColumnAndApply(firstHeader);
        columnHeaders = inventoryViewPage.getActiveColumnsHeaders();
        Assertions.assertThat(columnHeaders).doesNotContain(firstHeader);
        
        inventoryViewPage.enableColumnAndApply(firstHeader);
        
        // then
        columnHeaders = inventoryViewPage.getActiveColumnsHeaders();
        Assertions.assertThat(columnHeaders).contains(firstHeader);
    }
    
    @Test(priority = 5)
    public void paginationInitialStatus() {
        Assertions.assertThat(tableWidget.getPagination().isFirstPageBtnEnabled()).isFalse();
        Assertions.assertThat(tableWidget.getPagination().isPrevPageBtnEnabled()).isFalse();
        Assertions.assertThat(tableWidget.getPagination().isNextPageBtnEnabled()).isTrue();
    }
    
    @Test(priority = 6)
    public void checkNextPage() {
        tableWidget.getPagination().goOnNextPage();
        
        DelayUtils.sleep(2000);
        
        Assertions.assertThat(tableWidget.getPagination().isPrevPageBtnEnabled()).isTrue();
        Assertions.assertThat(tableWidget.getPagination().isFirstPageBtnEnabled()).isTrue();
        Assertions.assertThat(tableWidget.getPagination().getBottomRageOfRows()).isEqualTo(tableWidget.getPagination().getStep() + 1);
        Assertions.assertThat(tableWidget.getPagination().getTopRageOfRows()).isEqualTo(tableWidget.getPagination().getStep() * 2);
    }
    
    @Test(priority = 7)
    public void checkPrevPage() {
        tableWidget.getPagination().goOnNextPage();
        DelayUtils.sleep(2000);
        tableWidget.getPagination().goOnPrevPage();
        DelayUtils.sleep(2000);
        
        Assertions.assertThat(tableWidget.getPagination().getBottomRageOfRows()).isEqualTo(tableWidget.getPagination().getStep() + 1);
        Assertions.assertThat(tableWidget.getPagination().getTopRageOfRows()).isEqualTo(tableWidget.getPagination().getStep() * 2);
    }
    
    @Test(priority = 8)
    public void checkFirstPage() {
        tableWidget.getPagination().goOnFirstPage();
        DelayUtils.sleep(2000);
        
        Assertions.assertThat(tableWidget.getPagination().getBottomRageOfRows()).isEqualTo(1);
        Assertions.assertThat(tableWidget.getPagination().getTopRageOfRows()).isEqualTo(tableWidget.getPagination().getStep());
    }
    
    @Test(priority = 9)
    public void checkSortingByASC() {
        String columnId = "id";
        String attributeId = "plot";
        String attributeValue = "depression";
        
        inventoryViewPage.searchByAttributeValue(attributeId, attributeValue, Input.ComponentType.TEXT_FIELD);
        DelayUtils.sleep(1000);
        List<String> sortedValues = getValuesFromTableByKey(columnId).stream().sorted().collect(Collectors.toList());
        
        tableWidget.sortColumnByASC(columnId);
        DelayUtils.sleep(1000);
        
        List<String> valuesAfterActions = getValuesFromTableByKey(columnId);
        
        Assertions.assertThat(sortedValues).isEqualTo(valuesAfterActions);
        inventoryViewPage.clearFilters();
    }
    
    @Test(priority = 10)
    public void checkSortingByDESC() {
        String columnId = "id";
        String attributeId = "plot";
        String attributeValue = "depression";
        
        inventoryViewPage.searchByAttributeValue(attributeId, attributeValue, Input.ComponentType.TEXT_FIELD);
        DelayUtils.sleep(1000);
        
        List<String> sortedValues = getValuesFromTableByKey(columnId);
        sortedValues.sort(Collections.reverseOrder());
        
        tableWidget.sortColumnByDESC(columnId);
        DelayUtils.sleep(1000);
        
        List<String> valuesAfterActions = getValuesFromTableByKey(columnId);
        
        Assertions.assertThat(sortedValues).isEqualTo(valuesAfterActions);
        tableWidget.turnOffSortingForColumn(columnId);
        inventoryViewPage.clearFilters();
    }
    
    @Test(priority = 11)
    public void checkRefreshActions() {
        String refreshActionId = "refreshButton";
        
        tableWidget.getPagination().goOnNextPage();
        DelayUtils.sleep(1000);
        
        tableWidget.callAction(ActionsContainer.KEBAB_GROUP_ID, refreshActionId);
        DelayUtils.sleep(1000);
        
        Assertions.assertThat(tableWidget.getPagination().getBottomRageOfRows()).isEqualTo(1);
        Assertions.assertThat(tableWidget.getPagination().getTopRageOfRows()).isEqualTo(tableWidget.getPagination().getStep());
    }
    
    @Test(priority = 12)
    public void addColumnByPath() {
        String path = "director.type";
        
        toggleColumnByPath(path);
        List<String> ids = tableWidget.getActiveColumnIds();
        Assertions.assertThat(ids).contains(path);
    }
    
    @Test(priority = 13)
    public void changeDefaultColumnWidth() {
        int defaultColumnSize = inventoryViewPage.getColumnSize(MOVIE_LENGTH_COLUMN_ID);
        String newColumnSize = String.valueOf(100 + defaultColumnSize);
        tableWidget.setColumnWidth(MOVIE_LENGTH_COLUMN_ID, newColumnSize);
        Assertions.assertThat(inventoryViewPage.getColumnSize(MOVIE_LENGTH_COLUMN_ID)).isEqualTo(Integer.parseInt(newColumnSize));
        driver.navigate().refresh();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        int columnSizeRefresh = inventoryViewPage.getColumnSize(MOVIE_LENGTH_COLUMN_ID);
        Assertions.assertThat(columnSizeRefresh).isEqualTo(Integer.parseInt(newColumnSize));
    }
    
    @Test(priority = 14)
    public void setDefaultSettings() {
        inventoryViewPage.setDefaultSettings();
        List<String> defaultActiveHeaders = inventoryViewPage.getMainTable().getActiveColumnIds();
        String defaultFirstColumn = inventoryViewPage.getMainTable().getActiveColumnIds().get(0);
        int defaultFirstColumnSize = inventoryViewPage.getColumnSize(defaultFirstColumn);
        String defaultForthColumn = inventoryViewPage.getMainTable().getActiveColumnIds().get(3);
        
        changeColumnsConfiguration(defaultFirstColumn, defaultFirstColumn, defaultForthColumn, TYPE_DIRECTOR_COLUMN_ID);
        inventoryViewPage.setDefaultSettings();
        
        List<String> activeColumnsHeaders = inventoryViewPage.getMainTable().getActiveColumnIds();
        Assertions.assertThat(defaultFirstColumn).isEqualTo(activeColumnsHeaders.get(0));
        Assertions.assertThat(inventoryViewPage.getColumnSize(activeColumnsHeaders.get(0))).isEqualTo(defaultFirstColumnSize);
        Assertions.assertThat(activeColumnsHeaders).contains(defaultForthColumn);
        Assertions.assertThat(activeColumnsHeaders).doesNotContain(TYPE_DIRECTOR_COLUMN_ID);
        Assertions.assertThat(defaultActiveHeaders).isEqualTo(activeColumnsHeaders);
    }
    
    @Test(priority = 15)
    public void chanePaginationAndRefreshPage() {
        inventoryViewPage.setPagination(PAGE_SIZE_OPTION_200);
        Assert.assertEquals(getRowsCount(), PAGE_SIZE_OPTION_200);
        driver.navigate().refresh();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertEquals(getRowsCount(), PAGE_SIZE_OPTION_200);
    }
    
    @Test(priority = 16)
    public void changePaginationAndOpenDifferentType() {
        inventoryViewPage.setPagination(PAGE_SIZE_OPTION_100);
        Assert.assertEquals(getRowsCount(), PAGE_SIZE_OPTION_100);
        String TYPE = "TestActor";
        inventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, TYPE);
        Assert.assertEquals(getRowsCount(), PAGE_SIZE_OPTION_50);
    }
    
    private int getRowsCount() {
        tableWidget = inventoryViewPage.getMainTable();
        PaginationComponent pagination = tableWidget.getPagination();
        return pagination.getRowsCount();
    }
    
    private void changeColumnsConfiguration(String orderChangeColumn, String resizeColumn, String disabledColumn, String enableColumn) {
        // change column order
        tableWidget = inventoryViewPage.getMainTable();
        tableWidget.changeColumnsOrderById(orderChangeColumn, 2);
        Assertions.assertThat(inventoryViewPage.getMainTable().getActiveColumnIds().indexOf(orderChangeColumn)).isEqualTo(2);
        // change column size
        int columnSize = inventoryViewPage.getColumnSize(resizeColumn);
        int newColumnSize = columnSize - 50;
        tableWidget.setColumnWidth(resizeColumn, String.valueOf(newColumnSize));
        Assertions.assertThat(inventoryViewPage.getColumnSize(resizeColumn)).isEqualTo(newColumnSize);
        // unselect column
        toggleColumnByPath(disabledColumn);
        Assertions.assertThat(inventoryViewPage.getActiveColumnsHeaders()).doesNotContain(disabledColumn);
        
        // select new column
        toggleColumnByPath(enableColumn);
        List<String> ids = tableWidget.getActiveColumnIds();
        Assertions.assertThat(ids).contains(enableColumn);
    }
    
    private void toggleColumnByPath(String columnId) {
        AttributesChooser attributesChooser = inventoryViewPage.getMainTable().getAttributesChooser();
        attributesChooser.toggleAttributeByPath(columnId);
        attributesChooser.clickApply();
    }
    
}

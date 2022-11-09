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
import com.oss.framework.components.attributechooser.AttributesChooser;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.layout.Card;
import com.oss.framework.components.pagination.PaginationComponent;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.table.TableRow;
import com.oss.framework.widgets.table.TableWidget;
import com.oss.pages.platform.NewInventoryViewPage;

public class TableWidgetTest extends BaseTestCase {
    private static final int PAGE_SIZE_OPTION_200 = 200;
    private static final int PAGE_SIZE_OPTION_100 = 100;
    private static final int PAGE_SIZE_OPTION_50 = 50;
    private static final String TYPE_DIRECTOR_COLUMN_ID = "director.id";
    private static final String MOVIE_LENGTH_COLUMN_ID = "movieLength";
    private static final String ONE_SELECTED = "1 selected";
    public static final String TEST_ACTOR_TYPE = "TestActor";
    public static final String ID_COLUMN_ID = "id";
    public static final String MAIN_TABLE_CARD = "MainTableCard";
    public static final String DEFAULT_WIDTH_200 = "200";
    private static final String VIEWS_MANAGEMENT_VIEWS_INVENTORY_VIEW_ULR = "views/management/views/inventory-view/";
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
        Assertions.assertThat(selectedRows.get(0).getIndex()).isZero();

        inventoryViewPage.unselectObjectByRowId(0);
        selectedRows = inventoryViewPage.getSelectedRows();
        Assertions.assertThat(selectedRows).isEmpty();
    }

    @Test(priority = 2)
    public void resizeColumn() {
        com.oss.framework.widgets.table.TableWidget tableWidget = inventoryViewPage.getMainTable();
        int defaultSize = tableWidget.getFirstColumnSize();
        DelayUtils.sleep(DelayUtils.HUMAN_REACTION_MS);

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
        Assertions.assertThat(newHeaders.indexOf(secondHeader)).isZero();
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

        inventoryViewPage.enableColumn(firstHeader);

        // then
        columnHeaders = inventoryViewPage.getActiveColumnsHeaders();
        Assertions.assertThat(columnHeaders).contains(firstHeader);
    }

    @Test(priority = 5)
    public void paginationInitialStatus() {
        Assertions.assertThat(tableWidget.getPagination().isFirstPageButtonPresent()).isFalse();
        Assertions.assertThat(tableWidget.getPagination().isPreviousPageButtonPresent()).isFalse();
        Assertions.assertThat(tableWidget.getPagination().isNextPageButtonPresent()).isTrue();
    }

    @Test(priority = 6)
    public void checkNextPage() {
        tableWidget.getPagination().goOnNextPage();

        DelayUtils.sleep(2000);

        Assertions.assertThat(tableWidget.getPagination().isPreviousPageButtonPresent()).isTrue();
        Assertions.assertThat(tableWidget.getPagination().isFirstPageButtonPresent()).isTrue();
        Assertions.assertThat(tableWidget.getPagination().getBottomRangeOfRows()).isEqualTo(tableWidget.getPagination().getStep() + 1);
        Assertions.assertThat(tableWidget.getPagination().getTopRangeOfRows()).isEqualTo(tableWidget.getPagination().getStep() * 2);
    }

    @Test(priority = 7)
    public void checkPrevPage() {
        tableWidget.getPagination().goOnNextPage();
        DelayUtils.sleep(2000);
        tableWidget.getPagination().goOnPrevPage();
        DelayUtils.sleep(2000);

        Assertions.assertThat(tableWidget.getPagination().getBottomRangeOfRows()).isEqualTo(tableWidget.getPagination().getStep() + 1);
        Assertions.assertThat(tableWidget.getPagination().getTopRangeOfRows()).isEqualTo(tableWidget.getPagination().getStep() * 2);
    }

    @Test(priority = 8)
    public void checkFirstPage() {
        tableWidget.getPagination().goOnFirstPage();
        DelayUtils.sleep(2000);

        Assertions.assertThat(tableWidget.getPagination().getBottomRangeOfRows()).isEqualTo(1);
        Assertions.assertThat(tableWidget.getPagination().getTopRangeOfRows()).isEqualTo(tableWidget.getPagination().getStep());
    }

    @Test(priority = 9)
    public void checkSortingByASC() {
        String columnId = "movieLength";
        String attributeId = "rating";
        String attributeValue = "10";

        inventoryViewPage.searchByAttributeValue(attributeId, attributeValue, Input.ComponentType.NUMBER_FIELD);
        DelayUtils.sleep(1000);
        List<String> sortedValues = getValuesFromTableByKey(columnId).stream()
                .map(Long::parseLong)
                .sorted()
                .map(String::valueOf)
                .collect(Collectors.toList());

        tableWidget.sortColumnByASC(columnId);
        DelayUtils.sleep(1000);

        List<String> valuesAfterActions = getValuesFromTableByKey(columnId);

        Assertions.assertThat(sortedValues).isEqualTo(valuesAfterActions);
        inventoryViewPage.clearFilters();
    }

    @Test(priority = 10)
    public void checkSortingByDESC() {
        String columnId = "movieLength";
        String attributeId = "rating";
        String attributeValue = "10";

        inventoryViewPage.searchByAttributeValue(attributeId, attributeValue, Input.ComponentType.NUMBER_FIELD);
        DelayUtils.sleep(1000);

        List<String> sortedValues = getValuesFromTableByKey(columnId).stream()
                .map(Long::parseLong)
                .sorted(Collections
                        .reverseOrder())
                .map(String::valueOf)
                .collect(Collectors.toList());

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

        Assertions.assertThat(tableWidget.getPagination().getBottomRangeOfRows()).isEqualTo(1);
        Assertions.assertThat(tableWidget.getPagination().getTopRangeOfRows()).isEqualTo(tableWidget.getPagination().getStep());
    }

    @Test(priority = 12)
    public void addColumnByPath() {
        String path = "director.type";

        enableColumn(path);
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
        tableWidget = inventoryViewPage.getMainTable();
        String defColWidth = tableWidget.getDefaultColumnWidth(MOVIE_LENGTH_COLUMN_ID);
        Assertions.assertThat(defColWidth).isEqualTo(DEFAULT_WIDTH_200);
    }

    @Test(priority = 14)
    public void backToDefaultSettings() {
        inventoryViewPage.setDefaultSettings();
        List<String> defaultActiveHeaders = inventoryViewPage.getMainTable().getActiveColumnIds();
        String defaultFirstColumn = inventoryViewPage.getMainTable().getActiveColumnIds().get(0);
        int defaultFirstColumnSize = inventoryViewPage.getColumnSize(defaultFirstColumn);
        String defaultForthColumn = inventoryViewPage.getMainTable().getActiveColumnIds().get(2);

        changeColumnOrder(defaultFirstColumn, 2);
        resizeColumnByAdministration(defaultFirstColumn, defaultFirstColumnSize - 50);
        tableWidget.resizeColumn(2, 130);
        disableColumn(defaultForthColumn);
        enableColumn(TYPE_DIRECTOR_COLUMN_ID);

        inventoryViewPage.setDefaultSettings();

        List<String> activeColumnsHeaders = inventoryViewPage.getMainTable().getActiveColumnIds();
        String defColWidth = tableWidget.getDefaultColumnWidth(defaultFirstColumn);
        Assertions.assertThat(defColWidth).isEqualTo(DEFAULT_WIDTH_200);
        Assertions.assertThat(defaultFirstColumn).isEqualTo(activeColumnsHeaders.get(0));
        Assertions.assertThat(inventoryViewPage.getColumnSize(activeColumnsHeaders.get(0))).isEqualTo(defaultFirstColumnSize);
        Assertions.assertThat(activeColumnsHeaders).contains(defaultForthColumn).doesNotContain(TYPE_DIRECTOR_COLUMN_ID);
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

    @Test(priority = 17)
    public void singleSelect() {
        // when
        inventoryViewPage.selectObjectByRowId(0);
        inventoryViewPage.selectObjectByRowId(3);

        tableWidget.clickRow(6);

        List<TableRow> selectedRows = inventoryViewPage.getSelectedRows();
        String selectedObjectCount = tableWidget.getSelectedObjectCount();
        tableWidget.hideSelectionBar();

        // then
        Assertions.assertThat(selectedRows).hasSize(1);
        Assertions.assertThat(selectedObjectCount).isEqualTo(ONE_SELECTED);
    }
    @Test(priority = 18)
    public void selectAllRows(){
        inventoryViewPage.getMainTable().selectAllRows();
        Assertions.assertThat(inventoryViewPage.getSelectedRows()).hasSize(50);
        inventoryViewPage.getMainTable().getPagination().goOnNextPage();
        Assertions.assertThat(inventoryViewPage.getSelectedRows()).isEmpty();
        inventoryViewPage.getMainTable().getPagination().goOnFirstPage();
        List<TableRow> selectedRows = inventoryViewPage.getSelectedRows();
        inventoryViewPage.getMainTable().unselectAllRows();
        Assertions.assertThat(selectedRows).hasSize(50);
    }

    @Test(priority = 19)
    public void maximizeAndMinimize() {
        Card mainTableCard = Card.createCard(driver, webDriverWait, MAIN_TABLE_CARD);
        mainTableCard.maximizeCard();
        Assertions.assertThat(mainTableCard.isCardMaximized()).isTrue();
        mainTableCard.minimizeCard();
        Assertions.assertThat(mainTableCard.isCardMaximized()).isFalse();
    }

    @Test(priority = 20)
    public void makeLinkPattern() {
        String id = inventoryViewPage.getMainTable().getCellValue(0, ID_COLUMN_ID);
        inventoryViewPage.getMainTable().setLinkPattern(ID_COLUMN_ID, VIEWS_MANAGEMENT_VIEWS_INVENTORY_VIEW_ULR + "{type}?id={%s}");
        inventoryViewPage.getMainTable().clickLink(0, ID_COLUMN_ID);
        Assertions.assertThat(driver.getCurrentUrl()).contains(ID_COLUMN_ID + "=" + id).contains(VIEWS_MANAGEMENT_VIEWS_INVENTORY_VIEW_ULR + TEST_ACTOR_TYPE);
        inventoryViewPage = new NewInventoryViewPage(driver, webDriverWait);
        String id1 = inventoryViewPage.getMainTable().getCellValue(0, ID_COLUMN_ID);
        Assertions.assertThat(id).isEqualTo(id1);
    }

    @Test(priority = 21)
    public void resizeColumnAndRefresh() {
        tableWidget = inventoryViewPage.getMainTable();
        int defaultSize = tableWidget.getFirstColumnSize();
        DelayUtils.sleep(DelayUtils.HUMAN_REACTION_MS);
        int offset = 400;
        tableWidget.resizeFirstColumn(offset);
        DelayUtils.sleep(DelayUtils.HUMAN_REACTION_MS);
        driver.navigate().refresh();
        tableWidget = inventoryViewPage.getMainTable();
        int newSize = tableWidget.getFirstColumnSize();
        Assertions.assertThat(defaultSize + offset).isEqualTo(newSize);
        String firstColumnId = inventoryViewPage.getMainTable().getActiveColumnIds().get(0);
        String defColWidth = tableWidget.getDefaultColumnWidth(firstColumnId);
        Assertions.assertThat(defColWidth).isEqualTo(DEFAULT_WIDTH_200);
    }

    @Test(priority = 22)
    public void resizeColumnAndOverwriteDefaultWidth() {
        int offset = 400;
        tableWidget.resizeColumn(2, offset);
        String thirdColumnId = inventoryViewPage.getMainTable().getActiveColumnIds().get(2);
        tableWidget.setColumnWidth(thirdColumnId, "300");
        int thirdColumnSize = tableWidget.getColumnSize(2);
        Assertions.assertThat(thirdColumnSize).isEqualTo(300);
    }

    private int getRowsCount() {
        tableWidget = inventoryViewPage.getMainTable();
        PaginationComponent pagination = tableWidget.getPagination();
        return pagination.getRowsCount();
    }

    private void changeColumnOrder(String columnId, int position) {
        tableWidget = inventoryViewPage.getMainTable();
        tableWidget.changeColumnsOrderById(columnId, position);
        Assertions.assertThat(inventoryViewPage.getMainTable().getActiveColumnIds().indexOf(columnId)).isEqualTo(position);
    }

    private void resizeColumnByAdministration(String columnId, int columnSize) {
        tableWidget.setColumnWidth(columnId, String.valueOf(columnSize));
        Assertions.assertThat(inventoryViewPage.getColumnSize(columnId)).isEqualTo(columnSize);
    }

    private void disableColumn(String columnId) {
        toggleColumnByPath(columnId);
        Assertions.assertThat(inventoryViewPage.getActiveColumnsHeaders()).doesNotContain(columnId);
    }

    private void enableColumn(String columnId) {
        toggleColumnByPath(columnId);
        List<String> ids = tableWidget.getActiveColumnIds();
        Assertions.assertThat(ids).contains(columnId);
    }

    private void toggleColumnByPath(String columnId) {
        AttributesChooser attributesChooser = inventoryViewPage.getMainTable().getAttributesChooser();
        attributesChooser.toggleAttributeByPath(columnId);
        attributesChooser.clickApply();
    }

}

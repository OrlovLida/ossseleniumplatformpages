package com.oss.web;

import com.oss.BaseTestCase;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.tablewidget.TableRow;
import com.oss.pages.platform.NewInventoryViewPage;
import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

public class TableWidget extends BaseTestCase {
    private NewInventoryViewPage inventoryViewPage;

    @BeforeClass
    public void goToInventoryView() {
        String TYPE = "Movie";
        inventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, TYPE);
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
    public void addFirstUnselectedColumn() {
        // given
        List<String> columnHeaders = inventoryViewPage.getActiveColumnsHeaders();
        String firstHeader = columnHeaders.get(1);

        //when
        inventoryViewPage.removeColumn(firstHeader);
        columnHeaders = inventoryViewPage.getActiveColumnsHeaders();
        Assertions.assertThat(columnHeaders).doesNotContain(firstHeader);

        inventoryViewPage.enableColumnAndApply(firstHeader);

        // then
        columnHeaders = inventoryViewPage.getActiveColumnsHeaders();
        Assertions.assertThat(columnHeaders).contains(firstHeader);
    }

    @Test(priority = 4)
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
}

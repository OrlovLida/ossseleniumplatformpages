package com.oss.pages.reconciliation;

import org.openqa.selenium.WebDriver;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.table.OldTable;
import com.oss.pages.BasePage;

public class ConnectionConfigurationObjectsViewerPage extends BasePage {

    private static final String TABLE_ID = "object-table-id";
    private static final String PROPERTY_PANEL_ID = "attributes-window-id";

    public ConnectionConfigurationObjectsViewerPage(WebDriver driver) {
        super(driver);
    }

    public void queryAndSelectObjectById(String id) {
        searchForObjectById(id);
        DelayUtils.waitForPageToLoad(driver, wait);
        selectFirstRow();
    }

    public void searchForObjectById(String id) {
        getOldTable().searchByAttribute("id", id);
    }

    public void selectFirstRow() {
        getOldTable().selectRow(0);
    }

    public String getValueByRowIndex(int rowIndex) {
        return getAttributesTable().getCellValue(rowIndex, "Value");
    }

    private OldTable getOldTable() {
        return OldTable.createById(driver, wait, TABLE_ID);
    }

    private OldTable getAttributesTable() {
        return OldTable.createById(driver, wait, PROPERTY_PANEL_ID);
    }
}

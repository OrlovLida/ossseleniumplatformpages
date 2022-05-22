package com.oss.pages.reconciliation;

import java.util.List;

import org.openqa.selenium.WebDriver;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.table.TableWidget;
import com.oss.pages.BasePage;

public class InventoryViewVSObject95Page extends BasePage {

    private static final String TABLE_ID = "InventoryView_MainCard_VS_Comarch_COM_x_95_x_Device";

    public InventoryViewVSObject95Page(WebDriver driver) {
        super(driver);
    }

    public List<String> getColumnsIds() {
        return getMainTable().getActiveColumnHeaders();

    }

    public TableWidget getMainTable() {
        DelayUtils.waitForPageToLoad(driver, wait);
        return TableWidget.createById(driver, TABLE_ID, wait);
    }

    public InventoryViewVSObject95Page selectFirstRow() {
        selectObjectByRowId(0);
        return this;
    }

    public void selectObjectByRowId(int rowId) {
        getMainTable().selectRow(rowId);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

}

package com.oss.pages.platform;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Widget;
import com.oss.framework.widgets.Wizard;
import com.oss.framework.widgets.tablewidget.OldTable;
import com.oss.framework.widgets.tablewidget.TableInterface;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

/**
 * @author Ewa Frączek
 */

public class OldInventoryViewPage extends BasePage {

    //TODO: remove
    private TableInterface mainTable;
    //TODO: remove
    private Wizard wizard;

    public OldInventoryViewPage(WebDriver driver) {
        super(driver);
    }

    private static final String INVENTORY_VIEW = "InventoryView";
    private static final String OLD_TABLE_WIDGET = "OSSTableWidget";

    public TableInterface getTableWidget() {
        if (mainTable == null) {
            //TODO: remove  Widget.waitForWidget,
            Widget.waitForWidget(wait, OLD_TABLE_WIDGET);
            mainTable = OldTable.createByWindowDataAttributeName(driver, wait, INVENTORY_VIEW);
        }
        return mainTable;
    }

    public Wizard getWizard() {
        if (wizard == null) {
            wizard = Wizard.createWizard(driver, wait);
        }
        return wizard;
    }

    @Step("Select row")
    public void selectRow(String columnName, String value){
        DelayUtils.waitForPageToLoad(driver,wait);
        getTableWidget().selectRowByAttributeValueWithLabel(columnName, value);
        DelayUtils.waitForPageToLoad(driver,wait);
    }

    @Step("Use context action")
    public void useContextAction(String groupId, String actionId) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getTableWidget().callAction(groupId, actionId);
        DelayUtils.waitForPageToLoad(driver, wait);
    }
}

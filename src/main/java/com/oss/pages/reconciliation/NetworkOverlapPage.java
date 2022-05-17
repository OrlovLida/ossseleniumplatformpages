package com.oss.pages.reconciliation;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.list.EditableList;
import com.oss.framework.widgets.table.OldTable;
import com.oss.framework.widgets.table.TableWidget;
import com.oss.framework.widgets.tabs.TabsInterface;
import com.oss.framework.widgets.tabs.TabsWidget;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

public class NetworkOverlapPage extends BasePage {

    private static final String CONFLICT_TAB = "CurrentConflictTableTabApp";
    private static final String RESOLVE = "cmInventoryIntegration_CONFLICTActionRESOLVEId";
    private static final String CONFLICTED_OBJECTS_TAB = "ConflictTabViewApp";
    private static final String EDITABLE_LIST_ID = "ExtendedList-ConflictedObjectEditableListTabApp";
    private static final String NETWORK_ELEMENT_NAME = "name";
    private static final String STATUS_LABEL = "Status";
    private static final String DOMAIN_HEADER_ID = "overlapCmDomain";

    protected NetworkOverlapPage(WebDriver driver) {
        super(driver);
    }

    public static NetworkOverlapPage goToNetworkOverlapPage(WebDriver driver, String basicURL) {
        driver.get(String.format("%s/#/view/nar/inventory/integration/network-overlap-view" +
                "?perspective=LIVE&withRemoved=true", basicURL));
        return new NetworkOverlapPage(driver);
    }

    @Step("Get conflict status for given rowIndex")
    public String getConflictStatus(int rowIndex) {
        DelayUtils.waitForPageToLoad(driver, wait);
        return getOldTable().getCellValue(rowIndex, STATUS_LABEL);
    }

    @Step("Select row with given index")
    public void selectConflict(int rowIndex) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getOldTable().selectRow(rowIndex);
    }

    @Step("click Resolve button")
    public void clickResolve() {
        getTabsInterface().callActionById(RESOLVE);
    }

    @Step("Get Domain Name from Conflicted Objects tab")
    public String getDomainFromConflictedObjectsTab(int rowIndex) {
        DelayUtils.waitForPageToLoad(driver, wait);
        return getConflictedObjects().getRow(rowIndex).getCellValue(DOMAIN_HEADER_ID);
    }

    @Step("Search for object by name")
    public void searchByObjectName(String name) {
        getOldTable().searchByAttribute(NETWORK_ELEMENT_NAME, Input.ComponentType.TEXT_FIELD, name);
    }

    private EditableList getConflictedObjects() {
        return EditableList.createById(driver, wait, EDITABLE_LIST_ID);
    }

    private TableWidget getTableWidget() {
        return TableWidget.createById(driver, CONFLICT_TAB, wait);
    }

    private OldTable getOldTable() {
        return OldTable.createById(driver, wait, CONFLICT_TAB);
    }

    private TabsInterface getTabsInterface() {
        return TabsWidget.createById(driver, wait, CONFLICTED_OBJECTS_TAB);
    }
}

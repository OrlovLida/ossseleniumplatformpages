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

    @Step("Assert conflict status")
    public void assertConflictStatus(String status) {
        DelayUtils.waitForPageToLoad(driver, wait);
        OldTable oldTable = getOldTable();
        Assert.assertEquals(oldTable.getCellValue(0, STATUS_LABEL), status);
        oldTable.selectRow(0);
    }

    @Step("click Resolve button")
    public void clickResolveButton() {
        TabsInterface tabsInterface = getTabsInterface();
        tabsInterface.callActionById(RESOLVE);

    }

    @Step("Assert if domain exists in Conflicted Objects tab in first two rows")
    public void assertIfDomainExistsInConflictedObjects(String cmDomainName) {
        EditableList confictedObjects = EditableList.createById(driver, wait, EDITABLE_LIST_ID);
        DelayUtils.waitForPageToLoad(driver, wait);
        if (confictedObjects.getRow(0).getCellValue(DOMAIN_HEADER_ID).equals(cmDomainName)) {
            Assert.assertEquals(confictedObjects.getRow(0).getCellValue(DOMAIN_HEADER_ID), cmDomainName);
        }
        else {
            Assert.assertEquals(confictedObjects.getRow(1).getCellValue(DOMAIN_HEADER_ID), cmDomainName);
        }
    }

    @Step("Search for object by name")
    public void searchByObjectName(String name) {
        OldTable oldTable = getOldTable();
        oldTable.searchByAttribute(NETWORK_ELEMENT_NAME, Input.ComponentType.TEXT_FIELD, name);
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

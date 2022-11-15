package com.oss.pages.reconciliation;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.mainheader.Notifications;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.table.OldTable;
import com.oss.framework.widgets.table.TableInterface;
import com.oss.framework.widgets.tabs.TabsInterface;
import com.oss.framework.widgets.tabs.TabsWidget;
import com.oss.framework.widgets.tree.TreeWidget;
import com.oss.framework.widgets.treetable.OldTreeTableWidget;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;
import com.oss.pages.physical.DeviceWizardPage;

import io.qameta.allure.Step;

public class NetworkInconsistenciesViewPage extends BasePage {

    private static final String APPLY_GROUP_BUTTON_ID = "narComponent_GroupDiscrepancyActionApplyId";
    private static final String APPLY_BUTTON_ID = "narComponent_DiscrepancyActionApplyId";
    private static final String PHYSICAL_INCONSITENCIES_TABLE_ID = "narComponent_networkInconsistenciesViewIddiscrepancyDetailsTreeTableId";
    private static final String PHYSICAL_WIZARD_ID = "optional_prompt-card";
    private static final String RAN_INCONSITENCIES_TABLE_ID = "radioAppId";
    private static final String CHANGE_LOCATION_ACTION_ID = "DeviceChangeLocationAction";
    private static final String NIV_TREE = "narComponent_networkInconsistenciesViewIddiscrepanciesTreeTabId";
    private static final String ACCEPT_CHANGE_LOCATION_BUTTON_ID = "wizard-submit-button-change-location-wizard";
    private static final String TREE_ID = "narComponent_networkInconsistenciesViewIddiscrepanciesTreeId";
    private static final String RAN_WIZARD_ID = "changeLocationWizardId";
    private static final String TAB_ID = "narComponent_networkInconsistenciesViewIddiscrepanciesTabsWindowId";
    private static final String ASSIGN_LOCATION_ACTION_ID = "Assign Location";
    private static final String LOCATION = "location";
    private static final String LIVE = "Live";
    private static final String NETWORK = "Network";
    private static final String ELEMENT = "Element";
    private static final String OPERATION_TYPE = "Operation Type";

    public NetworkInconsistenciesViewPage(WebDriver driver) {
        super(driver);
    }

    public TreeWidget getTreeView() {
        DelayUtils.waitForPageToLoad(driver, wait);
        return TreeWidget.createById(driver, wait, TREE_ID);
    }

    @Step("Expand two tree levels of Inconsistencies")
    public void expandTree() {
        DelayUtils.waitForPageToLoad(driver, wait);
        getTreeView().expandLastTreeRow();
        DelayUtils.waitForPageToLoad(driver, wait);
        getTreeView().expandLastTreeRow();
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("Expand tree row that contains {rowName}")
    public void expandTreeRowContains(String rowName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getTreeView().expandTreeRowContains(rowName);
    }

    @Step("Select {inconsistencyName} and use Physical Device Update Wizard to assign location")
    public void assignLocation(String inconsistencyName, String preciseLocation) {
        getTreeView().selectTreeRow(inconsistencyName);
        DelayUtils.waitForPageToLoad(driver, wait);
        TableInterface table = OldTable.createById(driver, wait, PHYSICAL_INCONSITENCIES_TABLE_ID);
        table.callAction(ActionsContainer.EDIT_GROUP_ID, CHANGE_LOCATION_ACTION_ID);
        DelayUtils.waitForPageToLoad(driver, wait);
        Wizard wizard = Wizard.createByComponentId(driver, new WebDriverWait(driver, 90), PHYSICAL_WIZARD_ID);
        Input input = wizard.getComponent(DeviceWizardPage.DEVICE_PRECISE_LOCATION_TYPE_DATA_ATTRIBUTE_NAME);
        input.setSingleStringValueContains(preciseLocation);
        DelayUtils.waitForPageToLoad(driver, wait);
        wizard.clickButtonById(ACCEPT_CHANGE_LOCATION_BUTTON_ID);
    }

    @Step("Check if inconsistency is present by {inconsistencyName}")
    public boolean isInconsistencyPresent(String inconsistencyName) {
        return getTreeView().isRowPresent(inconsistencyName);
    }

    @Step("Select {inconsistencyName} and use assign location option")
    public void assignRanLocation(String inconsistencyName, String location) {
        getTreeView().selectTreeRow(inconsistencyName);
        DelayUtils.waitForPageToLoad(driver, wait);
        TableInterface table = OldTable.createById(driver, wait, RAN_INCONSITENCIES_TABLE_ID);
        table.callAction(ASSIGN_LOCATION_ACTION_ID);
        DelayUtils.waitForPageToLoad(driver, wait);
        Wizard wizard = Wizard.createByComponentId(driver, wait, RAN_WIZARD_ID);
        Input locationField = wizard.getComponent(LOCATION);
        locationField.setSingleStringValueContains(location);
        DelayUtils.waitForPageToLoad(driver, wait);
        wizard.clickAccept();
    }

    @Step("Select first group of Inconsistencies and apply discrepancies to Live perspective")
    public void applyInconsistencies() {
        selectTreeObjectByRowOrder(2);
        TabsInterface nivTabs = TabsWidget.createById(driver, wait, TAB_ID);
        nivTabs.selectTabById(NIV_TREE);
        DelayUtils.sleep(1000);
        nivTabs.callActionById(APPLY_GROUP_BUTTON_ID);
        DelayUtils.sleep(1000);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("Apply discrepancies to Live perspective")
    public void applySelectedInconsistencies() {
        TabsInterface nivTabs = TabsWidget.createById(driver, wait, TAB_ID);
        nivTabs.selectTabById(NIV_TREE);
        DelayUtils.sleep(1000);
        nivTabs.callActionById(APPLY_BUTTON_ID);
        DelayUtils.sleep(1000);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("Clear old notifications")
    public void clearOldNotification() {
        Notifications.create(driver, wait).clearAllNotification();
    }

    @Step("Check notification about accepting inconsistencies")
    public String checkNotificationAfterApplyInconsistencies() {
        return Notifications.create(driver, new WebDriverWait(driver, 150)).getNotificationMessage();
    }

    @Step("Check inconsistencies operation type for first object")
    public String checkInconsistenciesOperationType() {
        DelayUtils.waitForPageToLoad(driver, wait);
        return OldTable.createById(driver, wait, PHYSICAL_INCONSITENCIES_TABLE_ID).getCellValue(0, OPERATION_TYPE);
    }

    @Step("Get inconsistency LIVE name")
    public String getLiveName() {
        return getOldTreeTableWidget().getCellValue(0, LIVE);
    }

    @Step("Get inconsistency NETWORK name")
    public String getNetworkName() {
        return getOldTreeTableWidget().getCellValue(0, NETWORK);
    }

    @Step("Get inconsistency NETWORK info by rowName")
    public String getNetworkInfoByRowName(String rowName) {
        return getOldTreeTableWidget().getCellValue(getOldTreeTableWidget().getRowNumber(rowName, ELEMENT), NETWORK);
    }

    @Step("Select object on inconsistencies tree")
    public void selectTreeObjectByRowOrder(int number) {
        getTreeView().selectTreeRowByOrder(number);
    }

    @Step("Select object on inconsistencies tree")
    public void selectTreeObjectByName(String name) {
        getTreeView().selectTreeRow(name);
    }

    @Step("Expand info about inconsistency by rowName")
    public void expandElementInInconsistenciesTable(String elementName) {
        getOldTreeTableWidget().expandNode(elementName, ELEMENT);
    }

    private OldTreeTableWidget getOldTreeTableWidget() {
        return OldTreeTableWidget.create(driver, wait, PHYSICAL_INCONSITENCIES_TABLE_ID);
    }
}

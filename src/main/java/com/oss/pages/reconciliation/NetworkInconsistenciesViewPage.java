package com.oss.pages.reconciliation;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.inputs.Input;
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

    public static final String ACCEPT_DISCREPANCIES_PATTERN = "Accepting discrepancies related to %s finished";
    private static final String ACCEPT_GROUP_BUTTON_ID = "narComponent_GroupDiscrepancyActionAcceptId";
    private static final String ACCEPT_WITH_PREREQ_GROUP_BUTTON_ID = "narComponent_GroupDiscrepancyActionAcceptPrerequisitesId";
    private static final String ACCEPT_BUTTON_ID = "narComponent_DiscrepancyActionAcceptId";
    private static final String PHYSICAL_INCONSITENCIES_TABLE_ID = "narComponent_networkInconsistenciesViewIddiscrepancyDetailsTreeTableId";
    private static final String PHYSICAL_WIZARD_ID = "optional_prompt-card";
    private static final String RAN_INCONSITENCIES_TABLE_ID = "radioAppId";
    private static final String CHANGE_LOCATION_ACTION_ID = "DeviceChangeLocationAction";
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
        return TreeWidget.createById(driver, wait, TREE_ID);
    }

    @Step("Expand two tree levels of Inconsistencies")
    public void expandTwoLastTreeRows() {
        expandLastTreeRow();
        expandLastTreeRow();
    }

    @Step("Expand last tree level of Inconsistencies")
    public void expandLastTreeRow() {
        getTreeView().expandLastTreeRow();
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("Expand tree row that contains {rowName}")
    public void expandTreeRowContains(String rowName) {
        getTreeView().expandTreeRowContains(rowName);
    }

    @Step("Select {inconsistencyName} and use Physical Device Update Wizard to assign location")
    public void assignLocation(String inconsistencyName, String preciseLocation) {
        selectTreeObjectByName(inconsistencyName);
        DelayUtils.waitForPageToLoad(driver, wait);
        getOldTreeTableWidget().callActionById(ActionsContainer.EDIT_GROUP_ID, CHANGE_LOCATION_ACTION_ID);
        DelayUtils.waitForPageToLoad(driver, wait);
        Wizard wizard = Wizard.createByComponentId(driver, new WebDriverWait(driver, Duration.ofSeconds(90)), PHYSICAL_WIZARD_ID);
        Input input = wizard.getComponent(DeviceWizardPage.DEVICE_PRECISE_LOCATION_TYPE_DATA_ATTRIBUTE_NAME);
        input.setSingleStringValueContains(preciseLocation);
        DelayUtils.waitForPageToLoad(driver, wait);
        wizard.clickButtonById(ACCEPT_CHANGE_LOCATION_BUTTON_ID);
    }

    @Step("Select {inconsistencyName} and use assign location option")
    public void assignRanLocation(String inconsistencyName, String location) {
        selectTreeObjectByName(inconsistencyName);
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

    @Step("Check if inconsistency is present by {inconsistencyName}")
    public boolean isInconsistencyPresent(String inconsistencyName) {
        return getTreeView().isRowPresent(inconsistencyName);
    }

    public boolean isInconsistencyPresentContains(String inconsistencyName) {
        return getTreeView().isRowPresentContains(inconsistencyName);
    }

    @Step("Select first group of Inconsistencies and apply discrepancies to Live perspective")
    public void applyFirstInconsistenciesGroup() {
        selectTreeObjectByRowOrder(2);
        applySelectedInconsistenciesGroup();
    }

    @Step("Select first group of Inconsistencies and apply discrepancies with prerequisites to Live perspective")
    public void applyFirstInconsistenciesGroupWithPrerequsites() {
        selectTreeObjectByRowOrder(2);
        applyInconsistencies(ACCEPT_WITH_PREREQ_GROUP_BUTTON_ID);
    }

    @Step("Apply discrepancies to Live perspective")
    public void applySelectedInconsistencies() {
        applyInconsistencies(ACCEPT_BUTTON_ID);
    }

    @Step("Apply discrepancies to Live perspective")
    public void applySelectedInconsistenciesGroup() {
        applyInconsistencies(ACCEPT_GROUP_BUTTON_ID);
    }

    @Step("Check inconsistencies operation type for first object")
    public String checkInconsistenciesOperationType() {
        return getOldTreeTableWidget().getCellValue(0, OPERATION_TYPE);
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

    @Step("Get inconsistency NETWORK info by rowName")
    public String getNetworkInfoByRowName(String rowName) {
        return getOldTreeTableWidget().getCellValue(getOldTreeTableWidget().getRowNumber(rowName, ELEMENT), NETWORK);
    }

    @Step("Get inconsistency LIVE name")
    public String getLiveName() {
        return getOldTreeTableWidget().getCellValue(0, LIVE);
    }

    @Step("Get inconsistency NETWORK name")
    public String getNetworkName() {
        return getOldTreeTableWidget().getCellValue(0, NETWORK);
    }

    private void applyInconsistencies(String actionId) {
        TabsInterface nivTabs = TabsWidget.createById(driver, wait, TAB_ID);
        nivTabs.callActionById(actionId);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    private OldTreeTableWidget getOldTreeTableWidget() {
        return OldTreeTableWidget.create(driver, wait, PHYSICAL_INCONSITENCIES_TABLE_ID);
    }
}

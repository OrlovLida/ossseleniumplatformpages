package com.oss.pages.platform.OldInventoryView;

import java.util.Map;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.inputs.Button;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.framework.widgets.tablewidget.OldTable;
import com.oss.framework.widgets.tablewidget.TableInterface;
import com.oss.framework.widgets.tabswidget.TabWindowWidget;
import com.oss.framework.widgets.tabswidget.TabsInterface;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

import static com.oss.framework.components.contextactions.ActionsContainer.*;
import static com.oss.framework.widgets.tablewidget.OldTable.createByComponentDataAttributeName;
import static com.oss.pages.platform.OldInventoryView.helper.OldInventoryViewConstants.*;

/**
 * @author Ewa Frączek
 * If any method does not take extra tableId argument - it targets main table (upper).
 */
public class OldInventoryViewPage extends BasePage {

    private static final String CONFIRM_REMOVAL_BUTTON_ID = "ConfirmationBox_deleteAppId_action_button";

    public OldInventoryViewPage(WebDriver driver) {
        super(driver);
    }

    public Wizard getWizard() {
        return Wizard.createWizard(driver, wait);
    }

    @Step("Get table widget for upper table")
    public OldTable getTableWidget() {
        waitForPageToLoad();
        return createByComponentDataAttributeName(driver, wait, "table(" + getTypeBasedOnUrl(driver.getCurrentUrl()) + ")");
    }

    @Step("Get table widget for table by testId {tableTestId}")
    public OldTable getTableWidget(String tableTestId) {
        waitForPageToLoad();
        return createByComponentDataAttributeName(driver, wait, tableTestId);
    }

    @Step("Select row with value {value} in column {columnName} in upper table")
    public void selectRow(String columnName, String value) {
        waitForPageToLoad();
        getTableWidget().selectRowByAttributeValueWithLabel(columnName, value);
        waitForPageToLoad();
    }

    @Step("Select row with value {value} in column {columnName} in table {tableId}")
    public void selectRow(String tableId, String columnName, String value) {
        waitForPageToLoad();
        getTableWidget(tableId).selectRowByAttributeValueWithLabel(columnName, value);
        waitForPageToLoad();
    }

    @Step("Select row in upper table at index {index}")
    public void selectRowInTableAtIndex(int index) {
        getTableWidget().selectRow(index);
    }

    @Step("Select row at index {index} in table {tableTestId}")
    public void selectRowInTableAtIndex(String tableTestId, int index) {
        getTableWidget(tableTestId).selectRow(index);
    }

    @Step("Filter and select {objectName} row in upper table")
    public OldInventoryViewPage filterObject(String columnName, String objectName) {
        waitForPageToLoad();
        TableInterface table = getTableWidget();
        table.searchByAttributeWithLabel(columnName, Input.ComponentType.TEXT_FIELD, objectName);
        table.selectRowByAttributeValueWithLabel(columnName, objectName);
        return this;
    }

    @Step("Expand Show on button and select view {viewName} from the drop-down list")
    public void expandShowOnAndChooseView(String viewName) {
        useContextAction(SHOW_ON_GROUP_ID, viewName);
    }

    @Step("Expand edit group and select action {actionId} from the drop-down list")
    public void expandEditAndChooseAction(String actionId) {
        useContextAction(EDIT_GROUP_ID, actionId);
    }

    @Step("Expand other group and select action {actionId} from the drop-down list")
    public void expandOtherAndChooseAction(String actionId) { useContextAction(OTHER_GROUP_ID, actionId); }

    @Step("Expand create group and select action {actionId} from the drop-down list")
    public void expandCreateAndChooseAction(String actionId) { useContextAction(CREATE_GROUP_ID, actionId); }

    @Step("Click group {group} and action {action} on upper table")
    public void useContextAction(String group, String action) {
        waitForPageToLoad();
        TableInterface table = getTableWidget();
        table.callAction(group, action);
    }

    @Step("Click group {group} and action {action} on table {tableTestId}")
    public void useContextAction(String group, String action, String tableTestId) {
        waitForPageToLoad();
        TableInterface table = getTableWidget(tableTestId);
        table.callAction(group, action);
    }

    @Step("Click removal confirmation button")
    public void clickConfirmRemovalButton() {
        waitForPageToLoad();
        Button confirmationButton = Button.createById(driver, CONFIRM_REMOVAL_BUTTON_ID);
        confirmationButton.click();
    }

    @Step("Obtain property values from table {tableTestId}")
    public Map<String, String> getProperties(String tableTestId) {
        navigateToBottomTabByLabel(PROPERTIES_TAB_LABEL);
        waitForPageToLoad();
        TableInterface table = getTableWidget(tableTestId);
        return table.getPropertyNamesToValues();
    }

    @Step("Navigate to bottom tab {tabId} and get table widget for table {tableTestId}")
    public OldTable getTableWidgetForTab(String tabId, String tableTestId) {
        navigateToBottomTabById(tabId);
        waitForPageToLoad();
        return getTableWidget(tableTestId);
    }

    @Step("Navigate to bottom tab by id {tabId}")
    public void navigateToBottomTabById(String tabId) {
        TabsInterface tabsInterface = TabWindowWidget.create(driver, wait);
        tabsInterface.selectTabById(tabId);
    }

    public void getNumberOfRowsInTable(String tableTestId, String anyLabelExistingInTable) {
        OldTable.createByComponentDataAttributeName(driver, wait, tableTestId);
    }

    @Step("Click group {group} and action {actionId} in current tab")
    public void useContextActionInCurrentTab(String group, String actionId) {
        TabsInterface tab = TabWindowWidget.create(driver, wait);
        tab.callActionById(group, actionId);
    }

    public void navigateToBottomTabByLabel(String tabLabel) {
        waitForPageToLoad();
        TabsInterface tabsInterface = TabWindowWidget.create(driver, wait);
        tabsInterface.selectTabByLabel(tabLabel);
    }

    @Step("Click link in {columnName}")
    public void clickLinkInColumn(String columnName) {
        OldTable oldTable = OldTable.createByOssWindow(driver, wait);
        oldTable.selectLinkInSpecificColumn(columnName);
    }

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    private static String getTypeBasedOnUrl(String url) {
        String normalizedUrl = url.replace('?', '/');
        String[] urlParts = normalizedUrl.split("/");
        for (int i = 0; i < (urlParts.length - 1); i++) {
            if (urlParts[i].equals("type")) {
                return urlParts[i + 1];
            }
        }
        throw new IllegalStateException("Current page does not corresponds with Old Inventory View");
    }
}
package com.oss.pages.platform.OldInventoryView;

import java.util.Map;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.inputs.Button;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.propertypanel.OldPropertyPanel;
import com.oss.framework.widgets.table.OldTable;
import com.oss.framework.widgets.table.TableInterface;
import com.oss.framework.widgets.tabs.TabsInterface;
import com.oss.framework.widgets.tabs.TabsWidget;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

import static com.oss.framework.components.contextactions.ActionsContainer.CREATE_GROUP_ID;
import static com.oss.framework.components.contextactions.ActionsContainer.EDIT_GROUP_ID;
import static com.oss.framework.components.contextactions.ActionsContainer.OTHER_GROUP_ID;
import static com.oss.framework.components.contextactions.ActionsContainer.SHOW_ON_GROUP_ID;
import static com.oss.framework.widgets.table.OldTable.createById;
import static com.oss.pages.platform.OldInventoryView.helper.OldInventoryViewConstants.PROPERTIES_TAB_LABEL;

/**
 * @author Ewa Frączek
 * If any method does not take extra tableId argument - it targets main table (upper).
 */
public class OldInventoryViewPage extends BasePage {

    private static final String CONFIRM_REMOVAL_BUTTON_ID = "ConfirmationBox_deleteAppId_action_button";
    private static final String TAB_ID = "2";

    public OldInventoryViewPage(WebDriver driver) {
        super(driver);
    }

    @Step("Get table widget for upper table")
    public OldTable getTableWidget() {
        waitForPageToLoad();
        return createById(driver, wait, "table(" + getTypeBasedOnUrl(driver.getCurrentUrl()) + ")");
    }

    @Step("Get table widget for table by testId {tableTestId}")
    public OldTable getTableWidget(String tableTestId) {
        waitForPageToLoad();
        return createById(driver, wait, tableTestId);
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
    public void expandOtherAndChooseAction(String actionId) {
        useContextAction(OTHER_GROUP_ID, actionId);
    }

    @Step("Expand create group and select action {actionId} from the drop-down list")
    public void expandCreateAndChooseAction(String actionId) {
        useContextAction(CREATE_GROUP_ID, actionId);
    }

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
        return OldPropertyPanel.createById(driver, wait, tableTestId).getPropertyNamesToValues();
    }

    @Step("Navigate to bottom tab {tabId} and get table widget for table {tableTestId}")
    public OldTable getTableWidgetForTab(String tabId, String tableTestId) {
        navigateToBottomTabById(tabId);
        waitForPageToLoad();
        return getTableWidget(tableTestId);
    }

    @Step("Navigate to bottom tab by id {tabId}")
    public void navigateToBottomTabById(String tabId) {
        TabsInterface tabsInterface = TabsWidget.createById(driver, wait, TAB_ID);
        tabsInterface.selectTabById(tabId);
    }

    @Step("Click group {group} and action {actionId} in current tab")
    public void useContextActionInCurrentTab(String group, String actionId) {
        TabsInterface tab = TabsWidget.createById(driver, wait, TAB_ID);
        tab.callActionById(group, actionId);
    }

    public void navigateToBottomTabByLabel(String tabLabel) {
        waitForPageToLoad();
        TabsInterface tabsInterface = TabsWidget.createById(driver, wait, TAB_ID);
        tabsInterface.selectTabByLabel(tabLabel);
    }

    @Step("Click link in {columnName}")
    public void clickLinkInColumn(String columnName) {
        getTableWidget().clickLink(columnName);
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

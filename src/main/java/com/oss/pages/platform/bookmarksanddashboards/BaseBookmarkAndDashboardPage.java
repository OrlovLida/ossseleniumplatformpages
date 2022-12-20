package com.oss.pages.platform.bookmarksanddashboards;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.search.AdvancedSearch;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.tabs.TabsWidget;
import com.oss.framework.widgets.treetable.TreeTableWidget;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public abstract class BaseBookmarkAndDashboardPage extends BasePage {

    public static final String DESCRIPTION_COLUMN_ID = "description";
    protected static final Logger log = LoggerFactory.getLogger(BaseBookmarkAndDashboardPage.class);
    private static final String NAME_COLUMN_ID = "name";
    private static final String TABS_CONTAINER_ID = "management-view__container__tabscard";
    private static final String REFRESH_BUTTON_ID = "refreshButton";

    protected BaseBookmarkAndDashboardPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public abstract String getTreeTableId();

    public abstract String getTabId();

    public void selectTab() {
        TabsWidget.createById(driver, wait, TABS_CONTAINER_ID).selectTabById(getTabId());
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Opening tab with id: {}", getTabId());
    }

    public void searchInTable(String objectName) {
        getTreeTable().fullTextSearch(objectName);
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Searching in main search for: {}", objectName);
    }

    public void searchByAttribute(String componentId, String value) {
        AdvancedSearch advancedSearch = getTreeTable().getAdvancedSearch();
        advancedSearch.setFilter(componentId, value);
        log.info("Opening Advanced search and setting component with id: {} with value: {}", componentId, value);
        advancedSearch.clickApply();
        log.info("Clicking apply");
    }

    public void selectFirstRow() {
        getTreeTable().selectRow(0);
        log.info("Selecting first row in Table");
    }

    public void expandCategory(String categoryName) {
        getTreeTable().expandRow(getRowByName(categoryName));
        log.info("Expanding Category: {}", categoryName);
    }

    public void selectObject(String objectName) {
        getTreeTable().selectRow(getRowByName(objectName));
        log.info("Selecting: {}", objectName);
    }

    public void clickLinkToView(String objectName) {
        getTreeTable().clickLink(getRowByName(objectName), NAME_COLUMN_ID);
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Clicking Link to: {}", objectName);
    }

    public void callActionById(String actionId) {
        getTreeTable().callAction(actionId);
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Clicking table action: {}", actionId);
    }

    @Step("Call {actionId} action from {groupId} group")
    public void callAction(String groupId, String actionId) {
        getTreeTable().callAction(groupId, actionId);
    }

    public void refreshTable() {
        getTreeTable().callAction(ActionsContainer.KEBAB_GROUP_ID, REFRESH_BUTTON_ID);
        log.info("Refreshing table");
    }

    public void callActionOnObject(String objectName, String actionId) {
        getTreeTable().getRow(getRowByName(objectName)).callAction(actionId);
        log.info("Clicking action: {}, on object: {}", actionId, objectName);
    }

    public void callActionOnObject(String objectName, String groupId, String actionId) {
        getTreeTable().getRow(getRowByName(objectName)).callAction(groupId, actionId);
        log.info("Clicking actions: {}, {}, on object: {}", actionId, groupId, objectName);
    }

    public void enableColumn(String columnLabel, String... path) {
        getTreeTable().enableColumnByLabel(columnLabel, path);
        log.info("Enabling column: {}", columnLabel);
    }

    public void disableColumn(String columnLabel, String... path) {
        getTreeTable().disableColumnByLabel(columnLabel, path);
        log.info("Disabling column: {}", columnLabel);
    }

    public void changeColumnOrder(String columnLabel, int position) {
        getTreeTable().changeColumnsOrder(columnLabel, position);
        log.info("Changing position of column: {} to position: {}", columnLabel, position);
    }

    public void setCellValueByInlineEditor(int row, String columnId, String value) {
        getTreeTable().setCellValue(row, columnId, value);
        log.info("Setting value: {} in row: {}", value, row);
    }

    public String getDescription(String categoryName) {
        int index = getTreeTable().getRowNumber(categoryName, NAME_COLUMN_ID);
        return getTreeTable().getCellValue(index, DESCRIPTION_COLUMN_ID);
    }

    public boolean isObjectPresent(String name) {
        return getTreeTable().isValuePresent(name, NAME_COLUMN_ID);
    }

    protected TreeTableWidget getTreeTable() {
        return TreeTableWidget.createById(driver, wait, getTreeTableId());
    }

    private int getRowByName(String objectName) {
        return getTreeTable().getRowNumber(objectName, NAME_COLUMN_ID);
    }

}

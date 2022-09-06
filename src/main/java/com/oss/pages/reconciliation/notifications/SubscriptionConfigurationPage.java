package com.oss.pages.reconciliation.notifications;

import java.util.List;

import org.openqa.selenium.WebDriver;

import com.oss.framework.widgets.table.TableWidget;
import com.oss.framework.widgets.tabs.TabsWidget;
import com.oss.framework.widgets.tree.TreeWidget;
import com.oss.framework.widgets.tree.TreeWidgetV2;
import com.oss.pages.BasePage;

public class SubscriptionConfigurationPage extends BasePage {

    private static final String TREE_WIDGET_ID = "HierarchyTreeWidget";
    private static final String NOTIFICATION_SUBSCRIPTIONS_WIDGET_ID = "NotificationSubscriptionsWidget";

    public SubscriptionConfigurationPage(WebDriver driver) {
        super(driver);
    }

    public void selectFirstItem() {
        getTreeWidget().selectNode(0);
    }

    public void searchForSubscription(String text) {
        getTableWidget().fullTextSearch(text);
    }

    public List<String> getColumnHeaders() {
        return getTableWidget().getActiveColumnHeaders();
    }

    public String getState() {
        return getTableWidget().getCellValue(0, "state");
    }

    public String getOccupancyPercent() {
        return getTableWidget().getCellValue(0, "occupancyPercent");
    }

    public String getNewestNotification() {
        return getTableWidget().getCellValue(0, "newestNotificationTimestamp");
    }

    public String getOldestNotification() {
        return getTableWidget().getCellValue(0, "oldestNotificationTimestamp");
    }

    public void addRows(String columnName) {
        getTableWidget().enableColumnByLabel(columnName);
    }

    public List<String> getTabLabels() {
        return getTabsWidget().getTabLabels();
    }

    private TableWidget getTableWidget() {
        return TableWidget.createById(driver, NOTIFICATION_SUBSCRIPTIONS_WIDGET_ID, wait);
    }

    private TreeWidgetV2 getTreeWidget() {
        return TreeWidgetV2.create(driver, wait, TREE_WIDGET_ID);
    }

    private TabsWidget getTabsWidget() {
        return TabsWidget.createById(driver, wait, "TopDetailTabsWidget");
    }

}

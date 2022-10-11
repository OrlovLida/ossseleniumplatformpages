package com.oss.pages.reconciliation.notifications;

import java.util.List;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.widgets.table.TableWidget;
import com.oss.framework.widgets.tabs.TabsWidget;
import com.oss.framework.widgets.tree.TreeWidgetV2;
import com.oss.pages.BasePage;

public class SubscriptionConfigurationPage extends BasePage {

    private static final String TREE_WIDGET_ID = "HierarchyTreeWidget";
    private static final String NOTIFICATION_SUBSCRIPTIONS_WIDGET_ID = "NotificationSubscriptionsWidget";
    private static final String CLEAR_BUFFER_ID = "narComponent_RecoControl_NotificationSubscriptionActionClearBufferId";
    private static final String DISABLE_ID = "narComponent_RecoControl_NotificationSubscriptionActionDisableId";
    private static final String ENABLE_ID = "narComponent_RecoControl_NotificationSubscriptionActionEnableId";
    private static final String HOLD_ID = "narComponent_RecoControl_NotificationSubscriptionActionHoldId";
    private static final String BACK_TO_NDCV_WITH_SELECTED_SUBSCRIPTION_ID = "narComponent_RecoControl_CMDomainActionNetworkDiscoveryControlId";
    private static final String BACK_TO_NDCV_WITHOUT_SELECTED_SUBSCRIPTION_ID = "narComponent_RecoControl_CMDomainActionNetworkDiscoveryControlNoCtxId";
    private static final String TABS_WIDGET_ID = "TopDetailCard";
    private static final String STATE_ID = "state";
    private static final String CURRENT_OCCUPANCY_ID = "currentOccupancy";
    private static final String OCCUPANCY_PERCENT_ID = "occupancyPercent";
    private static final String NEWEST_NOTIFICATION_ID = "newestNotificationTimestamp";
    private static final String OLDEST_NOTIFICATION_ID = "oldestNotificationTimestamp";
    private static final String REFRESH_BUTTON_ID = "refreshButton";
    private static final String TOTAL_CAPACITY_ID = "totalCapacity";

    public SubscriptionConfigurationPage(WebDriver driver) {
        super(driver);
    }

    public void selectFirstNodeOnTree() {
        getTreeWidget().selectNode(0);
    }

    public void searchForSubscription(String text) {
        getTableWidget().fullTextSearch(text);
    }

    public List<String> getColumnHeaders() {
        return getTableWidget().getActiveColumnHeaders();
    }

    public String getState() {
        return getTableWidget().getCellValue(0, STATE_ID);
    }

    public String getOccupancyPercent() {
        return getTableWidget().getCellValue(0, OCCUPANCY_PERCENT_ID);
    }

    public String getCurrentOccupancy() {
        return getTableWidget().getCellValue(0, CURRENT_OCCUPANCY_ID);
    }

    public String getTotalCapacity() {
        return getTableWidget().getCellValue(0, TOTAL_CAPACITY_ID);
    }

    public String getNewestNotification() {
        return getTableWidget().getCellValue(0, NEWEST_NOTIFICATION_ID);
    }

    public String getOldestNotification() {
        return getTableWidget().getCellValue(0, OLDEST_NOTIFICATION_ID);
    }

    public int countNotificationSubscriptions() {
        return getTableWidget().countRows();
    }

    public void addRows(String columnName) {
        getTableWidget().enableColumnByLabel(columnName);
    }

    public List<String> getTabLabels() {
        return getTabsWidget().getTabLabels();
    }

    public void goBackToNDCVWithoutSelectedSubscription() {
        getTreeWidget().callActionById(ActionsContainer.SHOW_ON_GROUP_ID, BACK_TO_NDCV_WITHOUT_SELECTED_SUBSCRIPTION_ID);
    }

    public void goBackToNDCVWithSelectedSubscription() {
        getTreeWidget().callActionById(ActionsContainer.SHOW_ON_GROUP_ID, BACK_TO_NDCV_WITH_SELECTED_SUBSCRIPTION_ID);
    }

    public void clearBuffer() {
        getTableWidget().callAction(CLEAR_BUFFER_ID);
    }

    public void disable() {
        getTableWidget().callAction(DISABLE_ID);
    }

    public void enable() {
        getTableWidget().callAction(ENABLE_ID);
    }

    public void hold() {
        getTableWidget().callAction(HOLD_ID);
    }

    public boolean isBufferZeroPercent() {
        return "0%".equals(getOccupancyPercent());
    }

    public void refreshPage() {
        getTableWidget().callAction(ActionsContainer.KEBAB_GROUP_ID, REFRESH_BUTTON_ID);
    }

    public void selectFirstSubscription() {
        getTableWidget().selectRow(0);
    }

    public boolean isDateFormatCorrect(String date) {
        return date.matches("\\d\\d\\d\\d\\-\\d\\d\\-\\d\\d \\d\\d\\:\\d\\d\\:\\d\\d");
    }

    private TableWidget getTableWidget() {
        return TableWidget.createById(driver, NOTIFICATION_SUBSCRIPTIONS_WIDGET_ID, wait);
    }

    private TreeWidgetV2 getTreeWidget() {
        return TreeWidgetV2.create(driver, wait, TREE_WIDGET_ID);
    }

    private TabsWidget getTabsWidget() {
        return TabsWidget.createById(driver, wait, TABS_WIDGET_ID);
    }

}

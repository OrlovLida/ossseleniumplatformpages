package com.oss.pages.reconciliation;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.alerts.SystemMessageContainer;
import com.oss.framework.alerts.SystemMessageContainer.Message;
import com.oss.framework.alerts.SystemMessageContainer.MessageType;
import com.oss.framework.alerts.SystemMessageInterface;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.inputs.Input.ComponentType;
import com.oss.framework.mainheader.Notifications;
import com.oss.framework.mainheader.NotificationsInterface;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.framework.widgets.tablewidget.OldTable;
import com.oss.framework.widgets.tablewidget.TableInterface;
import com.oss.framework.widgets.tabswidget.TabWindowWidget;
import com.oss.framework.widgets.tabswidget.TabsInterface;
import com.oss.framework.widgets.treewidget.TreeWidget;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class NetworkInconsistenciesViewPage extends BasePage {

    private static final String APPLY_GROUP_BUTTON_ID = "narComponent_GroupDiscrepancyActionApplyId";
    private static final String APPLY_BUTTON_ID = "narComponent_DiscrepancyActionApplyId";
    private static final String PHYSICAL_INCONSITENCIES_TABLE_ID = "narComponent_networkInconsistenciesViewIddiscrepancyDetailsTreeTableId";
    private static final String RAN_INCONSITENCIES_TABLE_ID = "radioAppId";
    private static final String CHANGE_LOCATION_ACTION_ID = "DeviceChangeLocationAction";
    private static final String NIV_TREE = "narComponent_networkInconsistenciesViewIddiscrepanciesTreeTabId";
    private static final String PRECISE_LOCATION_ID = "precise_location";
    private static final String PHYSICAL_LOCATION_ID = "physical_location";
    private static final String ACCEPT_CHANGE_LOCATION_BUTTON_ID = "wizard-submit-button-change-location-wizard";
    private static final String TREE_ID = "narComponent_networkInconsistenciesViewIddiscrepanciesTreeId";

    public NetworkInconsistenciesViewPage(WebDriver driver) {
        super(driver);
    }

    public TreeWidget getTreeView() {
        DelayUtils.waitForPageToLoad(driver, wait);
        return TreeWidget.createByDataAttributeName(driver, wait, TREE_ID);
    }

    @Step("Expand two tree levels of Inconsistencies")
    public void expandTree() {
        DelayUtils.waitForPageToLoad(driver, wait);
        Assertions.assertThat(getTreeView().getVisibleTreeRow().size() > 1);
        getTreeView().expandLastTreeRow();
        DelayUtils.waitForPageToLoad(driver, wait);
        getTreeView().expandLastTreeRow();
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("Select {inconsistencyName} and use Physical Device Update Wizard to assign location")
    public void assignLocation(String inconsistencyName, String preciseLocation) {
        getTreeView().selectTreeRow(inconsistencyName);
        DelayUtils.waitForPageToLoad(driver, wait);
        TableInterface table = OldTable.createByComponentDataAttributeName(driver, wait, PHYSICAL_INCONSITENCIES_TABLE_ID);
        table.callAction("EDIT", CHANGE_LOCATION_ACTION_ID);
        DelayUtils.waitForPageToLoad(driver, wait);
        Wizard wizard = Wizard.createWizard(driver, new WebDriverWait(driver, 90));
        wizard.setComponentValue(PHYSICAL_LOCATION_ID, preciseLocation, ComponentType.SEARCH_FIELD);
        DelayUtils.waitForPageToLoad(driver, wait);
        wizard.setComponentValue(PRECISE_LOCATION_ID, preciseLocation, ComponentType.SEARCH_FIELD);
        DelayUtils.waitForPageToLoad(driver, wait);
        wizard.clickActionById(ACCEPT_CHANGE_LOCATION_BUTTON_ID);
    }

    @Step("Select {inconsistencyName} and use assign location option")
    public void assignRanLocation(String inconsistencyName, String location) {
        getTreeView().selectTreeRow(inconsistencyName);
        DelayUtils.waitForPageToLoad(driver, wait);
        TableInterface table = OldTable.createByComponentDataAttributeName(driver, wait, RAN_INCONSITENCIES_TABLE_ID);
        table.callAction("Assign Location");
        DelayUtils.waitForPageToLoad(driver, wait);
        Wizard wizard = Wizard.createByComponentId(driver, wait, "Popup");
        Input locationField = wizard.getComponent("location_OSF", ComponentType.SEARCH_FIELD);
        locationField.setSingleStringValueContains(location);
        DelayUtils.waitForPageToLoad(driver, wait);
        wizard.clickAccept();
    }

    @Step("Check system message after device update")
    public void checkUpdateDeviceSystemMessage() {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, wait);
        List<Message> messages = systemMessage.getMessages();
        Assertions.assertThat(messages).hasSize(1);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getMessageType())
                .isEqualTo(MessageType.SUCCESS);
    }

    @Step("Select first group of Inconsistencies and apply discrepancies to Live perspective")
    public void applyInconsistencies() {
        selectTreeObjectByRowOrder(2);
        TabsInterface nivTabs = TabWindowWidget.create(driver, wait);
        nivTabs.selectTabById(NIV_TREE);
        DelayUtils.sleep(1000);
        nivTabs.callActionById(APPLY_GROUP_BUTTON_ID);
        DelayUtils.sleep(1000);
    }

    @Step("Apply discrepancies to Live perspective")
    public void applySelectedInconsistencies() {
        TabsInterface nivTabs = TabWindowWidget.create(driver, wait);
        nivTabs.selectTabById(NIV_TREE);
        DelayUtils.sleep(1000);
        nivTabs.callActionById(APPLY_BUTTON_ID);
        DelayUtils.sleep(1000);
    }

    @Step("Clear old notifications")
    public void clearOldNotification() {
        NotificationsInterface notifications = Notifications.create(driver, wait);
        notifications.clearAllNotification();
    }

    @Step("Check notification about accepting inconsistencies")
    public void checkNotificationAfterApplyInconsistencies(String groupDiscrepancyLabel) {
        NotificationsInterface notifications = Notifications.create(driver, new WebDriverWait(driver, 150));
        Assertions.assertThat(notifications.waitAndGetFinishedNotificationText().equals("Accepting discrepancies related to " + groupDiscrepancyLabel + " finished")).isTrue();
    }

    @Step("Check inconsistencies operation type for first object")
    public String checkInconsistenciesOperationType() {
        DelayUtils.waitForPageToLoad(driver, wait);
        TableInterface table = OldTable.createByComponentDataAttributeName(driver, wait, PHYSICAL_INCONSITENCIES_TABLE_ID);
        return table.getCellValue(0, "Operation Type");
    }

    @Step("Select object on inconsistencies tree")
    public void selectTreeObjectByRowOrder(int number) {
        getTreeView().selectTreeRowByOrder(number);
    }
}

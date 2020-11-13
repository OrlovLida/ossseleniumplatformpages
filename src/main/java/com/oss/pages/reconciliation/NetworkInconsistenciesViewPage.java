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
import com.oss.framework.widgets.Widget;
import com.oss.framework.widgets.Wizard;
import com.oss.framework.widgets.tablewidget.OldTable;
import com.oss.framework.widgets.tablewidget.TableInterface;
import com.oss.framework.widgets.tabswidget.TabWindowWidget;
import com.oss.framework.widgets.tabswidget.TabsInterface;
import com.oss.framework.widgets.treewidget.TreeWidget;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class NetworkInconsistenciesViewPage extends BasePage {

    private TreeWidget mainTree;
    private String applyGroupButtonId = "narComponent_GroupDiscrepancyActionApplyId";
    private String applyButtonId = "narComponent_DiscrepancyActionApplyId";
    private String inconsistenciesTable = "narComponent_networkInconsistenciesViewIddiscrepancyDetailsTreeTableId";
    private String updateDevice = "UpdateDeviceWizardAction";
    private String nivTree = "narComponent_networkInconsistenciesViewIddiscrepanciesTreeTabId";

    public NetworkInconsistenciesViewPage(WebDriver driver) {
        super(driver);
    }

    public TreeWidget getTreeView() {
        if (mainTree == null) {
            Widget.waitForWidget(wait, "TreeView");
            mainTree = TreeWidget.createByClass(driver, "TreeView", wait);
            DelayUtils.waitForPageToLoad(driver, wait);
        }
        return mainTree;
    }

    @Step("Expand two tree levels of Inconsistencies")
    public void expantTree() {
        DelayUtils.waitForPageToLoad(driver, wait);
        Assertions.assertThat(getTreeView().getVisibleTreeRow().size() > 1);
        getTreeView().expandLastTreeRow();
        DelayUtils.waitForPageToLoad(driver, wait);
        getTreeView().expandLastTreeRow();
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("Select first Device and use Physical Device Update Wizard to assign location")
    public void assignLocation() {
        getTreeView().selectTreeRowByOrder(3);
        DelayUtils.waitForPageToLoad(driver, wait);
        TableInterface table = OldTable.createByComponentDataAttributeName(driver, wait, inconsistenciesTable);
        table.callAction("EDIT", updateDevice);
        DelayUtils.waitForPageToLoad(driver, wait);
        Wizard wizard = Wizard.createWizard(driver, new WebDriverWait(driver, 90));
        Input preciseLocation = wizard.getComponent("search_precise_location", ComponentType.SEARCH_FIELD);
        preciseLocation.setSingleStringValue("a");
        DelayUtils.waitForPageToLoad(driver, wait);
        wizard.clickUpdate();
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
        getTreeView().selectTreeRowByOrder(2);
        TabsInterface nivTabs = TabWindowWidget.create(driver, wait);
        nivTabs.selectTabById(nivTree);
        nivTabs.callActionById(applyGroupButtonId);
        DelayUtils.sleep(1000);
    }

    @Step("Apply discrepancies to Live perspective")
    public void applySelectedInconsistencies() {
        TabsInterface nivTabs = TabWindowWidget.create(driver, wait);
        nivTabs.selectTabById(nivTree);
        nivTabs.callActionById(applyButtonId);
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
        getTreeView().selectTreeRowByOrder(3);
        DelayUtils.sleep(5000);
        DelayUtils.waitForPageToLoad(driver, wait);
        TableInterface table = OldTable.createByComponentDataAttributeName(driver, wait, inconsistenciesTable);
        return table.getValueCell(0, "Operation Type");
    }
}

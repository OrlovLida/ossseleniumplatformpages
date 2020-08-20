package com.oss.pages.reconciliation;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.alerts.SystemMessageContainer;
import com.oss.framework.alerts.SystemMessageContainer.Message;
import com.oss.framework.alerts.SystemMessageContainer.MessageType;
import com.oss.framework.alerts.SystemMessageInterface;
import com.oss.framework.components.Input;
import com.oss.framework.components.Input.ComponentType;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.contextactions.ActionsInterface;
import com.oss.framework.components.notifications.Notifications;
import com.oss.framework.components.notifications.NotificationsInterface;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Widget;
import com.oss.framework.widgets.Wizard;
import com.oss.framework.widgets.tabswidget.TabWindowWidget;
import com.oss.framework.widgets.tabswidget.TabsInterface;
import com.oss.framework.widgets.treewidget.TreeWidget;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class NetworkInconsistenciesViewPage extends BasePage {

    private TreeWidget mainTree;
    private String applyButtonId = "narComponent_GroupDiscrepancyActionApplyId";
    private String groupDiscrepancyLabel = "CiscoSeleniumTest";

    public static NetworkInconsistenciesViewPage goToNetworkInconsistenciesViewPage(WebDriver driver, String basicURL) {
        driver.get(String.format("%s/#/view/reco/network-repository-view/discrepancies" +
                "?perspective=NETWORK", basicURL));
        return new NetworkInconsistenciesViewPage(driver);
    }

    public NetworkInconsistenciesViewPage(WebDriver driver) {
        super(driver);
    }

    public TreeWidget getTreeView() {
        if (mainTree == null) {
            Widget.waitForWidget(wait, "TreeView");
            mainTree = TreeWidget.createByClass(driver, "TreeView", wait);
            wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.className("TreeRow"))));
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
        ActionsInterface actionsContainer = ActionsContainer.createFromParent(driver.findElement(By.xpath("//div[@class='OssWindow']//div[@class='context-actions-wrapper']")), driver, wait);
        actionsContainer.callActionById("EDIT", "UpdateDeviceWizardAction");
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
        nivTabs.selectTabByLabel("narComponent_networkInconsistenciesViewIddiscrepanciesTreeTabId");
        nivTabs.callActionById(applyButtonId);
        DelayUtils.sleep(1000);
    }

    @Step("Clear old notifications")
    public void clearOldNotification() {
        NotificationsInterface notifications = Notifications.create(driver, wait);
        notifications.clearAllNotification();
    }

    @Step("Check notification about accepting inconsistencies")
    public void checkNotificationAfterApplyInconsistencies() {
        NotificationsInterface notifications = Notifications.create(driver, new WebDriverWait(driver, 90));
        Assertions.assertThat(notifications.waitAndGetFinishedNotificationText().equals("Accepting discrepancies related to " + groupDiscrepancyLabel + " finished")).isTrue();
    }
}

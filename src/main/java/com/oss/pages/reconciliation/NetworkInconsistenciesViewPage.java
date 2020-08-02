package com.oss.pages.reconciliation;

import org.assertj.core.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.oss.framework.components.Input;
import com.oss.framework.components.Input.ComponentType;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.contextactions.ActionsInterface;
import com.oss.framework.components.notifications.Notifications;
import com.oss.framework.components.notifications.NotificationsInterface;
import com.oss.framework.components.systemMessage.SystemMessage;
import com.oss.framework.components.systemMessage.SystemMessageInterface;
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

    @Step("Expand two first tree levels of Inconsistencies")
    public void expantTree() {
        waitForPageToLoad();
        Assertions.assertThat(getTreeView().getVisibleTreeRow().size() > 1);
        getTreeView().expandFirstTreeRow();
        waitForPageToLoad();
        getTreeView().expandFirstTreeRow();
        waitForPageToLoad();
    }

    @Step("Select first Device and use Physical Device Update Wizard to assign location")
    public void assignLocation() {
        getTreeView().selectTreeRowByOrder(3);
        waitForPageToLoad();
        ActionsInterface actionsContainer = ActionsContainer.createFromParent(driver.findElement(By.xpath("//div[@class='OssWindow']//div[@class='context-actions-wrapper']")), driver, wait);
        actionsContainer.callActionById("EDIT", "UpdateDeviceWizardAction");
        waitForPageToLoad();
        Wizard wizard = Wizard.createWizard(driver, wait);
        Input preciseLocation = wizard.getComponent("search_precise_location", ComponentType.SEARCH_FIELD);
        preciseLocation.setSingleStringValue("a");
        waitForPageToLoad();
        wizard.clickUpdate();
    }

    @Step("Check system message after device update")
    public void checkUpdateDeviceSystemMessage() {
        SystemMessageInterface info = SystemMessage.create(driver, wait);
        Assertions.assertThat(info.getMessage().equals("Device " + groupDiscrepancyLabel + " has been updated successfully.")).isTrue();
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
        NotificationsInterface notifications = Notifications.create(driver, wait);
        Assertions.assertThat(notifications.waitAndGetFinishedNotificationText().equals("Accepting discrepancies related to " + groupDiscrepancyLabel + " finished")).isTrue();
    }
}

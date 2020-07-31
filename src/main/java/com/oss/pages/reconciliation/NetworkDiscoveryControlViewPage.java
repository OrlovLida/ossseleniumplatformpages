package com.oss.pages.reconciliation;

import org.assertj.core.api.Assertions;
import org.openqa.selenium.WebDriver;

import com.oss.framework.components.notifications.Notifications;
import com.oss.framework.components.notifications.NotificationsInterface;
import com.oss.framework.components.systemMessage.SystemMessage;
import com.oss.framework.components.systemMessage.SystemMessageInterface;
import com.oss.framework.prompts.ConfirmationBox;
import com.oss.framework.prompts.ConfirmationBoxInterface;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Widget;
import com.oss.framework.widgets.tablewidget.OldTable;
import com.oss.framework.widgets.tablewidget.TableInterface;
import com.oss.framework.widgets.tabswidget.TabWindowWidget;
import com.oss.framework.widgets.tabswidget.TabsInterface;
import com.oss.framework.widgets.treewidget.TreeWidget;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class NetworkDiscoveryControlViewPage extends BasePage {

    private TreeWidget mainTree;
    private String reconciliation = "narComponent_CmDomainActionFullReconciliationId";

    public static NetworkDiscoveryControlViewPage goToNetworkDiscoveryControlViewPage(WebDriver driver, String basicURL) {
        driver.get(String.format("%s/#/view/reco/network-repository-view/network-discovery" +
                "?perspective=NETWORK", basicURL));
        return new NetworkDiscoveryControlViewPage(driver);
    }

    protected NetworkDiscoveryControlViewPage(WebDriver driver) {
        super(driver);
    }

    @Step("Open CM Domain Wizard")
    public void createCmDomain(String cmDomainName, String cmInterfaceName, String domainName) {
        waitForPageToLoad();
        TabsInterface tabs = TabWindowWidget.create(driver, wait);
        tabs.selectTabByLabel("narComponent_networkDiscoveryControlViewIdcmDomainsTreeTabId");
        tabs.callActionById("CREATE", "narComponent_CmDomainActionCreateId");
        new CmDomainWizardPage(driver).fillCmDomainWizardAndClose(cmDomainName, cmInterfaceName, domainName);
    }

    public TreeWidget getTreeView() {
        if (mainTree == null) {
            Widget.waitForWidget(wait, "TreeView");
            mainTree = TreeWidget.createByClass(driver, "TreeView", wait);
        }
        return mainTree;
    }

    @Step("Query and select CM Domain in Network Discovery Control View")
    public void queryAndSelectCmDomain(String cmDomainName) {
        waitForPageToLoad();
        getTreeView()
                .performSearchWithEnter(cmDomainName);
        waitForPageToLoad();
        getTreeView()
                .selectTreeRowByText(cmDomainName);
    }

    @Step("Run full reconciliation for selected CM Domain")
    public void runReconciliation() {
        TabsInterface tabs = TabWindowWidget.create(driver, wait);
        tabs.selectTabByLabel("narComponent_networkDiscoveryControlViewIdcmDomainsTreeTabId");
        tabs.callActionById(reconciliation);
        ConfirmationBoxInterface prompt = ConfirmationBox.create(driver, wait);
        prompt.clickButtonByLabel("Reconcile");
    }

    @Step("Check system message after starting reconciliation")
    public void checkReconciliationStartedSystemMessage(){
        SystemMessageInterface info = SystemMessage.create(driver, wait);
        Assertions.assertThat(info.getMessage().equals("Reconciliation started.")).isTrue();
    }

    @Step("Waiting until reconciliation is over")
    public void waitForEndOfReco() {
        TableInterface tableWidget = OldTable.createByClassNameAndOrder(driver, wait, "AppComponentContainer", 6);
        tableWidget.clickOnKebabMenu();
        tableWidget.clickOnAction("Refresh");
        DelayUtils.sleep(500);
        TableInterface table = OldTable.createByClassNameAndOrder(driver, wait, "OSSTableWidget TableFullWidth", 2);
        String status = table.getValueCell(0, "Status");
        while (status.equals("IN_PROGRESS") || status.equals("PENDING")) {
            DelayUtils.sleep(5000);
            tableWidget.clickOnKebabMenu();
            tableWidget.clickOnAction("Refresh");
            DelayUtils.sleep(1000);
            status = table.getValueCell(0, "Status");
        }
        Assertions.assertThat(status.equals("SUCCESS"));
    }

    @Step("Delete selected CM Domain")
    public void deleteCmDomain() {
        TabsInterface tabs = TabWindowWidget.create(driver, wait);
        tabs.selectTabByLabel("narComponent_networkDiscoveryControlViewIdcmDomainsTreeTabId");
        tabs.callActionById("EDIT", "narComponent_CmDomainActionDeleteCmDomainId");
        ConfirmationBoxInterface prompt = ConfirmationBox.create(driver, wait);
        prompt.clickButtonByLabel("Delete");
    }

    @Step("Check system message after deleting CM Domain")
    public void checkDeleteCmDomainSystemMessage() {
        SystemMessageInterface info = SystemMessage.create(driver, wait);
        Assertions.assertThat(info.getMessage().equals("Deleting CM Domain started. Please check notifications for updates.")).isTrue();
    }

    @Step("Check notification after deleting CM Domain")
    public void checkDeleteCmDomainNotification(String cmDomainName) {
        Assertions.assertThat(Notifications.create(driver, wait).waitAndGetFinishedNotificationText().equals("Deleting CM Domain: " + cmDomainName + " finished")).isTrue();
    }

    @Step("Clear old notifications")
    public void clearOldNotifications() {
        NotificationsInterface notifications = Notifications.create(driver, wait);
        notifications.clearAllNotification();
    }

    @Step("Move from Network Discovery Control View to Network Inconsistencies View in context of selected CM Domain")
    public void moveToNivFromNdcv() {
        TabsInterface ndcvTabs = TabWindowWidget.create(driver, wait);
        ndcvTabs.selectTabByLabel("narComponent_networkDiscoveryControlViewIdcmDomainsTreeTabId");
        ndcvTabs.callActionById("NAVIGATION", "narComponent_CmDomainActionShowInconsistenciesId");
        waitForInvisibilityOfLoadbars();
    }

    @Step("Move from Network Discovery Control View to CM Samples Management view in context of selected CM Domain")
    public void moveToSamplesManagement() {
        TabsInterface ndcvTabs = TabWindowWidget.create(driver, wait);
        ndcvTabs.selectTabByLabel("narComponent_networkDiscoveryControlViewIdcmDomainsTreeTabId");
        ndcvTabs.callActionById("NAVIGATION", "narComponent_CmDomainActionCmSamplesManagementId");
        waitForPageToLoad();
    }

}
package com.oss.pages.reconciliation;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.openqa.selenium.WebDriver;

import com.oss.framework.alerts.SystemMessageContainer;
import com.oss.framework.alerts.SystemMessageContainer.Message;
import com.oss.framework.alerts.SystemMessageContainer.MessageType;
import com.oss.framework.alerts.SystemMessageInterface;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.inputs.Input.ComponentType;
import com.oss.framework.mainheader.Notifications;
import com.oss.framework.mainheader.NotificationsInterface;
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
    private static final String RECONCILIATION_ACTION_ID = "narComponent_CmDomainActionFullReconciliationId";
    private static final String RECONCILIATION_TAB_ID = "narComponent_networkDiscoveryControlViewIdcmDomainTabsId";
    private static final String RECONCILIATION_STATE_TABLE_ID = "narComponent_networkDiscoveryControlViewIdreconciliationStatesTableId";
    private static final String RECONCILIATION_TREE_TAB_ID = "narComponent_networkDiscoveryControlViewIdcmDomainsTreeTabId";
    private static final String CREATE_CM_DOMAIN_ACTION_ID = "narComponent_CmDomainActionCreateId";
    private static final String DELETE_CM_DOMAIN_ACTION_ID = "narComponent_CmDomainActionDeleteCmDomainId";
    private static final String SHOW_INCONCISTENCIES_ACTION_ID = "narComponent_CmDomainActionShowInconsistenciesId";
    private static final String SHOW_SAMPLES_MANAGEMENT_ACTION_ID = "narComponent_CmDomainActionCmSamplesManagementId";
    private static final String ISSUES_TABLE_ID = "narComponent_networkDiscoveryControlViewIdissuesTableId";
    private static final String RECO_STATE_REFRESH_BUTTON_ID = "tableRefreshButton";

    public static NetworkDiscoveryControlViewPage goToNetworkDiscoveryControlViewPage(WebDriver driver, String basicURL) {
        driver.get(String.format("%s/#/view/reco/network-repository-view/network-discovery" +
                "?perspective=NETWORK", basicURL));
        return new NetworkDiscoveryControlViewPage(driver);
    }

    protected NetworkDiscoveryControlViewPage(WebDriver driver) {
        super(driver);
    }

    @Step("Open CM Domain Wizard")
    public void openCmDomainWizard() {
        DelayUtils.waitForPageToLoad(driver, wait);
        TabsInterface tabs = TabWindowWidget.create(driver, wait);
        tabs.selectTabById(RECONCILIATION_TREE_TAB_ID);
        tabs.callActionById(ActionsContainer.CREATE_GROUP_ID, CREATE_CM_DOMAIN_ACTION_ID);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("Create CM Domain with Name: {name}, CM Interface: {cmInterface}, Domain: {domain}")
    public void createCMDomain(String name, String cmInterface, String domain) {
        openCmDomainWizard();
        CmDomainWizardPage wizard = new CmDomainWizardPage(driver);
        wizard.fillCmDomainWizard(name, cmInterface, domain);
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
        DelayUtils.waitForPageToLoad(driver, wait);
        getTreeView()
                .performSearchWithEnter(cmDomainName);
        DelayUtils.waitForPageToLoad(driver, wait);
        getTreeView()
                .selectTreeRowByText(cmDomainName);
    }

    @Step("Run full reconciliation for selected CM Domain")
    public void runReconciliation() {
        TabsInterface tabs = TabWindowWidget.create(driver, wait);
        tabs.selectTabById(RECONCILIATION_TREE_TAB_ID);
        tabs.callActionById(RECONCILIATION_ACTION_ID);
        ConfirmationBoxInterface prompt = ConfirmationBox.create(driver, wait);
        prompt.clickButtonByLabel("Reconcile");
    }

    @Step("Check system message after starting reconciliation")
    public void checkReconciliationStartedSystemMessage() {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, wait);
        List<Message> messages = systemMessage.getMessages();
        Assertions.assertThat(messages).hasSize(1);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getMessageType())
                .isEqualTo(MessageType.INFO);
    }

    @Step("Waiting until reconciliation is over")
    public void waitForEndOfReco() {
        TableInterface tableWidget = OldTable.createByComponentDataAttributeName(driver, wait, RECONCILIATION_TAB_ID);
        tableWidget.callAction(ActionsContainer.KEBAB_GROUP_ID, RECO_STATE_REFRESH_BUTTON_ID);
        DelayUtils.sleep(500);
        TableInterface table = OldTable.createByComponentDataAttributeName(driver, wait, RECONCILIATION_STATE_TABLE_ID);
        String status = table.getCellValue(0, "Status");
        while (status.equals("IN_PROGRESS") || status.equals("PENDING")) {
            DelayUtils.sleep(5000);
            tableWidget.callAction(ActionsContainer.KEBAB_GROUP_ID, RECO_STATE_REFRESH_BUTTON_ID);
            DelayUtils.sleep(1000);
            status = table.getCellValue(0, "Status");
        }
        Assertions.assertThat(status.equals("SUCCESS"));
    }

    @Step("Delete selected CM Domain")
    public void deleteCmDomain() {
        TabsInterface tabs = TabWindowWidget.create(driver, wait);
        tabs.selectTabById(RECONCILIATION_TREE_TAB_ID);
        tabs.callActionById(ActionsContainer.EDIT_GROUP_ID, DELETE_CM_DOMAIN_ACTION_ID);
        ConfirmationBoxInterface prompt = ConfirmationBox.create(driver, wait);
        prompt.clickButtonByLabel("Delete");
    }

    @Step("Check system message after deleting CM Domain")
    public void checkDeleteCmDomainSystemMessage() {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, wait);
        List<Message> messages = systemMessage.getMessages();
        Assertions.assertThat(messages).hasSize(1);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getMessageType())
                .isEqualTo(MessageType.INFO);
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
        ndcvTabs.selectTabById(RECONCILIATION_TREE_TAB_ID);
        ndcvTabs.callActionById(ActionsContainer.SHOW_ON_GROUP_ID, SHOW_INCONCISTENCIES_ACTION_ID);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("Move from Network Discovery Control View to CM Samples Management view in context of selected CM Domain")
    public void moveToSamplesManagement() {
        TabsInterface ndcvTabs = TabWindowWidget.create(driver, wait);
        ndcvTabs.selectTabById(RECONCILIATION_TREE_TAB_ID);
        DelayUtils.waitForPageToLoad(driver, wait);
        ndcvTabs.callActionById(ActionsContainer.SHOW_ON_GROUP_ID, SHOW_SAMPLES_MANAGEMENT_ACTION_ID);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("Check if there are Issues with type {type}")
    public boolean checkIssues(IssueLevel errorType) {
        String type = String.valueOf(errorType);
        getIssuesTable().searchByAttributeWithLabel("Issue Level", ComponentType.TEXT_FIELD, type);
        if (getIssuesTable().hasNoData()) {
            return true;
        } else {
            logIssues(type);
            return false;
        }
    }

    @Step("Select latest reconciliation state")
    public void selectLatestReconciliationState() {
        TableInterface table = OldTable.createByComponentDataAttributeName(driver, wait, RECONCILIATION_STATE_TABLE_ID);
        table.selectRow(0);
    }

    private void logIssues(String type) {
        int issuesNumber = getIssuesTable().getTableObjectsCount();
        Assertions.assertThat(issuesNumber).isGreaterThan(0);
        if (issuesNumber <= 10) {
            printIssues(type, issuesNumber);
        } else if (issuesNumber <= 100) {
            getIssuesTable().changeItemsPerPageValue(100);
            printIssues(type, issuesNumber);
        } else {
            System.out.println("There are over 100 issues with type = " + type + ". Printing only latest 100:");
            getIssuesTable().changeItemsPerPageValue(100);
            printIssues(type, 100);
        }
    }

    private void printIssues(String type, int issuesNumber) {
        for (int i = 0; i < issuesNumber; i++) {
            System.out.println("[" + type + "] " + getIssuesTable().getCellValue(i, "Reason"));
        }
    }

    private OldTable getIssuesTable() {
        return OldTable.createByComponentDataAttributeName(driver, wait, ISSUES_TABLE_ID);
    }

    public enum IssueLevel {
        INFO,
        WARNING,
        ERROR,
        FATAL,
        STARTUP_FATAL
    }
}
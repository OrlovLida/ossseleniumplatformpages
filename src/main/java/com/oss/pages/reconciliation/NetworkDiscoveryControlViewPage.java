package com.oss.pages.reconciliation;

import java.util.List;

import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.mainheader.Notifications;
import com.oss.framework.components.mainheader.NotificationsInterface;
import com.oss.framework.components.prompts.ConfirmationBox;
import com.oss.framework.components.prompts.ConfirmationBoxInterface;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.table.OldTable;
import com.oss.framework.widgets.tabs.TabsInterface;
import com.oss.framework.widgets.tabs.TabsWidget;
import com.oss.framework.widgets.tree.TreeWidget;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class NetworkDiscoveryControlViewPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(NetworkDiscoveryControlViewPage.class);

    private static final String TREE_ID = "narComponent_networkDiscoveryControlViewIdcmDomainsTreeId";
    private static final String RECONCILIATION_ACTION_ID = "narComponent_CmDomainActionFullReconciliationId";
    private static final String RECONCILIATION_TAB_ID = "narComponent_networkDiscoveryControlViewIdcmDomainDetailsWindowId";
    private static final String RECONCILIATION_STATE_TABLE_ID = "narComponent_networkDiscoveryControlViewIdreconciliationStatesTableId";
    private static final String RECONCILIATION_TREE_TAB_ID = "narComponent_networkDiscoveryControlViewIdcmDomainsTreeTabId";
    private static final String CREATE_CM_DOMAIN_ACTION_ID = "narComponent_CmDomainActionCreateId";
    private static final String DELETE_CM_DOMAIN_ACTION_ID = "narComponent_CmDomainActionDeleteCmDomainId";
    private static final String CANCEL_WITH_SUBSEQUENT_ACTION_ID = "narComponent_CmDomainActionCancelSubsequentDiscrepanciesId";
    private static final String ACCEPT_WITH_PREREQUISITES_ACTION_ID = "narComponent_CmDomainActionAcceptPrerequisitesDiscrepanciesId";
    private static final String CONFIRM_CANCEL_WITH_SUBSEQUENT_BUTTON_ID = "ConfirmationBox_narComponent_networkInconsistenciesViewIdcancelDiscrepanciesConfirmationBoxId_action_button";
    private static final String SHOW_INCONCISTENCIES_ACTION_ID = "narComponent_CmDomainActionShowInconsistenciesId";
    private static final String SHOW_SAMPLES_MANAGEMENT_ACTION_ID = "narComponent_CmDomainActionCmSamplesManagementId";
    private static final String ISSUES_TABLE_ID = "narComponent_networkDiscoveryControlViewIdissuesTableId";
    private static final String RECO_STATE_REFRESH_BUTTON_ID = "tableRefreshButton";
    private static final String STATUS = "Status";
    private static final String TAB_ID = "narComponent_networkDiscoveryControlViewIdcmDomainWindowId";
    private static final String DETAILS_ID = "narComponent_CmDomainActionDetailsId";
    private static final String ISSUE_LEVEL = "Issue Level";
    private static final String REASON = "Reason";
    private static final String CONFLICT = "conflict";
    private static final String RECONCILIATION_WITH_ID = "Reconciliation with ID";
    private static final String MOVE_TO_NOTIFICATION_SUBSCRIPTIONS_ID = "narComponent_CmDomainActionNotificationSubscriptionsId";
    private static final String TYPE = "Type";

    public NetworkDiscoveryControlViewPage(WebDriver driver) {
        super(driver);
    }

    public static NetworkDiscoveryControlViewPage goToNetworkDiscoveryControlViewPage(WebDriver driver, String basicURL) {
        driver.get(String.format("%s/#/view/reco/network-repository-view/network-discovery" +
                "?perspective=NETWORK", basicURL));
        return new NetworkDiscoveryControlViewPage(driver);
    }

    @Step("Open CM Domain Wizard")
    public void openCmDomainWizard() {
        DelayUtils.waitForPageToLoad(driver, wait);
        TabsInterface tabs = getTabsInterface();
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

    @Step("Create CM Domain with Name: {name}, CM Interface: {cmInterface}, Domain: {domain}")
    public void createCMDomain(String name, String cmInterface, String domain, String stopOn) {
        openCmDomainWizard();
        CmDomainWizardPage wizard = new CmDomainWizardPage(driver);
        wizard.fillCmDomainWizard(name, cmInterface, domain, stopOn);
    }

    public TreeWidget getTreeView() {
        return TreeWidget.createById(driver, wait, TREE_ID);
    }

    @Step("Query and select CM Domain in Network Discovery Control View")
    public void queryAndSelectCmDomain(String cmDomainName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        searchForCmDomain(cmDomainName);
        DelayUtils.waitForPageToLoad(driver, wait);
        selectCmDomain(cmDomainName);
    }

    @Step("Check if CMDomain exists in Network Discovery Control View")
    public boolean isCmDomainPresent(String cmDomainName) {
        return getTreeView().isRowPresent(cmDomainName);
    }

    @Step("Search for CMDomain in Network Discovery Control View")
    public void searchForCmDomain(String cmDomainName) {
        getTreeView().search(cmDomainName);
    }

    @Step("Select CM Domain in Network Discovery Control View")
    public void selectCmDomain(String cmDomainName) {
        getTreeView().selectTreeRow(cmDomainName);
    }

    @Step("Run full reconciliation for selected Domain")
    public void runReconciliation() {
        TabsInterface tabs = getTabsInterface();
        tabs.selectTabById(RECONCILIATION_TREE_TAB_ID);
        tabs.callActionById(RECONCILIATION_ACTION_ID);
        ConfirmationBoxInterface prompt = ConfirmationBox.create(driver, wait);
        prompt.clickButtonByLabel("Reconcile");
    }

    @Step("Waiting until reconciliation is over")
    public String waitForEndOfReco() {
        DelayUtils.waitForPageToLoad(driver, wait);
        OldTable.createById(driver, wait, RECONCILIATION_TAB_ID).callAction(ActionsContainer.KEBAB_GROUP_ID, RECO_STATE_REFRESH_BUTTON_ID);
        DelayUtils.waitForPageToLoad(driver, wait);
        String status = getRecoStateTable().getCellValue(0, STATUS);
        while (status.contains("IN_PROGRESS") || status.contains("PENDING")) {
            DelayUtils.sleep(5000);
            DelayUtils.waitForPageToLoad(driver, wait);
            OldTable.createById(driver, wait, RECONCILIATION_TAB_ID).callAction(ActionsContainer.KEBAB_GROUP_ID, RECO_STATE_REFRESH_BUTTON_ID);
            DelayUtils.waitForPageToLoad(driver, wait);
            try {
                status = getRecoStateTable().getCellValue(0, STATUS);
            } catch (StaleElementReferenceException e) {
                DelayUtils.waitForPageToLoad(driver, wait);
                status = getRecoStateTable().getCellValue(0, STATUS);
            }
        }
        return status;
    }

    @Step("Delete selected CM Domain")
    public void deleteCmDomain() {
        TabsInterface tabs = getTabsInterface();
        tabs.selectTabById(RECONCILIATION_TREE_TAB_ID);
        tabs.callActionById(ActionsContainer.EDIT_GROUP_ID, DELETE_CM_DOMAIN_ACTION_ID);
        ConfirmationBoxInterface prompt = ConfirmationBox.create(driver, wait);
        prompt.clickButtonByLabel("Delete");
    }

    @Step("Cancel all inconsistencies with subsequents")
    public void cancelWithSubsequents() {
        TabsInterface tabs = getTabsInterface();
        tabs.selectTabById(RECONCILIATION_TREE_TAB_ID);
        tabs.callActionById(ActionsContainer.OTHER_GROUP_ID, CANCEL_WITH_SUBSEQUENT_ACTION_ID);
        ConfirmationBoxInterface prompt = ConfirmationBox.create(driver, wait);
        prompt.clickButtonById(CONFIRM_CANCEL_WITH_SUBSEQUENT_BUTTON_ID);
    }

    @Step("Accept all inconsistencies with prerequisites")
    public void acceptWithPrerequisites() {
        TabsInterface tabs = getTabsInterface();
        tabs.selectTabById(RECONCILIATION_TREE_TAB_ID);
        tabs.callActionById(ActionsContainer.OTHER_GROUP_ID, ACCEPT_WITH_PREREQUISITES_ACTION_ID);
    }

    @Step("Count reconciliation states")
    public int countReconciliationStates() {
        return getRecoStateTable().countRows(TYPE);
    }

    @Step("Move from Network Discovery Control View to Network Inconsistencies View in context of selected CM Domain")
    public void moveToNivFromNdcv() {
        TabsInterface ndcvTabs = getTabsInterface();
        ndcvTabs.selectTabById(RECONCILIATION_TREE_TAB_ID);
        ndcvTabs.callActionById(ActionsContainer.SHOW_ON_GROUP_ID, SHOW_INCONCISTENCIES_ACTION_ID);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("Move from Network Discovery Control View to CM Samples Management view in context of selected CM Domain")
    public void moveToSamplesManagement() {
        TabsInterface ndcvTabs = getTabsInterface();
        ndcvTabs.selectTabById(RECONCILIATION_TREE_TAB_ID);
        DelayUtils.waitForPageToLoad(driver, wait);
        ndcvTabs.callActionById(ActionsContainer.SHOW_ON_GROUP_ID, SHOW_SAMPLES_MANAGEMENT_ACTION_ID);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("Move from Network Discovery Control View to Subscription Configuration view in context of selected CM Domain")
    public void moveToSubscriptionConfiguration() {
        getTabsInterface().callActionById(ActionsContainer.SHOW_ON_GROUP_ID, MOVE_TO_NOTIFICATION_SUBSCRIPTIONS_ID);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("Open Details about CMDomain")
    public void openCMDomainDetails() {
        TabsInterface ndcvTabs = getTabsInterface();
        ndcvTabs.callActionById(ActionsContainer.OTHER_GROUP_ID, DETAILS_ID);
    }

    @Step("Check if there are Issues with type {errorType}")
    public boolean checkIssues(IssueLevel errorType) {
        String type = String.valueOf(errorType);
        getIssuesTable().searchByColumn(ISSUE_LEVEL, type);
        DelayUtils.sleep(2000);
        DelayUtils.waitForPageToLoad(driver, wait);
        if (getIssuesTable().hasNoData()) {
            return true;
        } else {
            logIssues(type);
            return false;
        }
    }

    @Step("Select latest reconciliation state")
    public void selectLatestReconciliationState() {
        getRecoStateTable().selectRow(0);
    }

    @Step("Check if conflict event appeared during reconciliation")
    public boolean isConflictEventPresent() {
        getIssuesTable().searchByColumn(ISSUE_LEVEL, "");
        getIssuesTable().searchByColumn(REASON, CONFLICT);
        return getIssuesTable().getCellValue(0, REASON).contains(CONFLICT);
    }

    @Step("Get CMDomain ID from reconciliation start event")
    public String getCMDomainIdFromRecoStartEvent() {
        String cmDomainId = getRecoStartEvent().substring(getRecoStartEvent().indexOf("(ID:"));
        cmDomainId = cmDomainId.substring(5, (cmDomainId.length() - 6));
        return cmDomainId;
    }

    public List<String> getTabsLabels() {
        return TabsWidget.createById(driver, wait, RECONCILIATION_TAB_ID).getTabLabels();
    }

    @Deprecated
    /**
     * @Depracated - not related to NetworkDiscoveryControlViewPage, use the Notifications class directly
     */
    @Step("Check notification after deleting CM Domain")
    public String checkDeleteCmDomainNotification() {
        return Notifications.create(driver, wait).getNotificationMessage();
    }

    @Deprecated
    /**
     * @Depracated - not related to NetworkDiscoveryControlViewPage, use the Notifications class directly
     */
    @Step("Clear old notifications")
    public void clearOldNotifications() {
        NotificationsInterface notifications = Notifications.create(driver, wait);
        notifications.clearAllNotification();
    }

    @Step("Get Reconciliation start event")
    private String getRecoStartEvent() {
        getIssuesTable().searchByColumn(REASON, RECONCILIATION_WITH_ID);
        DelayUtils.waitForPageToLoad(driver, wait);
        return getIssuesTable().getCellValue(0, REASON);
    }

    private void logIssues(String type) {
        int issuesNumber = getIssuesTable().getTotalCount();
        if (issuesNumber <= 10) {
            printIssues(type, issuesNumber);
        } else if (issuesNumber <= 100) {
            getIssuesTable().setPageSize(100);
            printIssues(type, issuesNumber);
        } else {
            log.info("There are over 100 issues with type = '{}'. Printing only latest 100:", type);
            getIssuesTable().setPageSize(100);
            printIssues(type, 100);
        }
    }

    private void printIssues(String type, int issuesNumber) {
        for (int i = 0; i < issuesNumber; i++) {
            log.info("[{}] {}", type, getIssuesTable().getCellValue(i, REASON));
        }
    }

    private OldTable getIssuesTable() {
        return OldTable.createById(driver, wait, ISSUES_TABLE_ID);
    }

    private OldTable getRecoStateTable() {
        return OldTable.createById(driver, wait, RECONCILIATION_STATE_TABLE_ID);
    }

    private TabsInterface getTabsInterface() {
        return TabsWidget.createById(driver, wait, TAB_ID);
    }

    public enum IssueLevel {
        INFO,
        WARNING,
        ERROR,
        FATAL,
        STARTUP_FATAL
    }
}

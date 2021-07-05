package com.oss.E2E;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.alerts.SystemMessageContainer;
import com.oss.framework.alerts.SystemMessageContainer.MessageType;
import com.oss.framework.alerts.SystemMessageInterface;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.mainheader.Notifications;
import com.oss.framework.mainheader.NotificationsInterface;
import com.oss.framework.mainheader.PerspectiveChooser;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.platform.GlobalSearchPage;
import com.oss.pages.platform.NewInventoryViewPage;
import com.oss.pages.reconciliation.NetworkDiscoveryControlViewPage;
import com.oss.pages.reconciliation.NetworkInconsistenciesViewPage;
import com.oss.pages.reconciliation.SamplesManagementPage;

import io.qameta.allure.Description;

public class UC_NAR_001 extends BaseTestCase {

    private static final String ROUTER_NAME = "UCNAR001Router";
    private static final String CM_DOMAIN_NAME = "UC_NAR_001";
    private static final String NARROW_RECO_NOTIFICATION = "Narrow reconciliation for GMOCs IPDevice finished";
    private static final String INTERFACE_NAME = "CISCO IOS XR without mediation";
    private static final String DOMAIN = "IP";
    private static final String EQUIPMENT_TYPE = "Physical Device";

    private NetworkDiscoveryControlViewPage networkDiscoveryControlViewPage;

    private void checkPopupMessageType(MessageType messageType) {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assert.assertNotNull(messages);
        Assert.assertEquals(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getMessageType(),
                messageType);
    }

    @BeforeClass
    public void openNetworkDiscoveryControlView() {
        networkDiscoveryControlViewPage = NetworkDiscoveryControlViewPage.goToNetworkDiscoveryControlViewPage(driver, BASIC_URL);
    }

    @Test(priority = 1)
    @Description("Create CM Domain")
    public void createCmDomain() {
        PerspectiveChooser.create(driver, webDriverWait).setNetworkPerspective();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkDiscoveryControlViewPage = NetworkDiscoveryControlViewPage.goToNetworkDiscoveryControlViewPage(driver, BASIC_URL);
        networkDiscoveryControlViewPage.createCMDomain(CM_DOMAIN_NAME, INTERFACE_NAME, DOMAIN);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 2)
    @Description("Upload basic samples")
    public void uploadBasicSamples() {
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(CM_DOMAIN_NAME);
        networkDiscoveryControlViewPage.moveToSamplesManagement();
        SamplesManagementPage samplesManagementPage = new SamplesManagementPage(driver);
        samplesManagementPage.selectPath();
        samplesManagementPage.createDirectory(CM_DOMAIN_NAME);
        DelayUtils.sleep(1000);
        samplesManagementPage.uploadSamples("recoSamples/UC_NAR_001/create/UCNAR001Router_10.20.0.88_20170707_1324_sh_inventory_raw");
        DelayUtils.sleep(1000);
        samplesManagementPage.uploadSamples("recoSamples/UC_NAR_001/create/UCNAR001Router_10.20.0.88_20170707_1324_sh_version");
    }

    @Test(priority = 3)
    @Description("Run reco with basic samples")
    public void runReconciliationWithBasicSample() {
        openNetworkDiscoveryControlView();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(CM_DOMAIN_NAME);
        networkDiscoveryControlViewPage.runReconciliation();
        checkPopupMessageType(MessageType.INFO);
        Assert.assertEquals(networkDiscoveryControlViewPage.waitForEndOfReco(), "SUCCESS");
        networkDiscoveryControlViewPage.selectLatestReconciliationState();
        Assert.assertTrue(networkDiscoveryControlViewPage.checkIssues(NetworkDiscoveryControlViewPage.IssueLevel.STARTUP_FATAL));
        Assert.assertTrue(networkDiscoveryControlViewPage.checkIssues(NetworkDiscoveryControlViewPage.IssueLevel.FATAL));
        Assert.assertTrue(networkDiscoveryControlViewPage.checkIssues(NetworkDiscoveryControlViewPage.IssueLevel.ERROR));
        Assert.assertTrue(networkDiscoveryControlViewPage.checkIssues(NetworkDiscoveryControlViewPage.IssueLevel.WARNING));
    }

    @Test(priority = 4)
    @Description("Apply inconsistencies")
    public void applyInconsistencies() {
        networkDiscoveryControlViewPage.moveToNivFromNdcv();
        NetworkInconsistenciesViewPage networkInconsistenciesViewPage = new NetworkInconsistenciesViewPage(driver);
        networkInconsistenciesViewPage.expandTree();
        networkInconsistenciesViewPage.assignLocation(ROUTER_NAME, "a");
        checkPopupMessageType(MessageType.SUCCESS);
        networkInconsistenciesViewPage.clearOldNotification();
        networkInconsistenciesViewPage.applyInconsistencies();
        DelayUtils.sleep(5000);
        Assert.assertEquals(networkInconsistenciesViewPage.checkNotificationAfterApplyInconsistencies(), "Accepting discrepancies related to " + ROUTER_NAME + " finished");
    }

    @Test(priority = 5)
    @Description("Open Network Discovery Control View and replace old samples")
    public void replaceOldSamples() {
        openNetworkDiscoveryControlView();
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(CM_DOMAIN_NAME);
        networkDiscoveryControlViewPage.moveToSamplesManagement();
        SamplesManagementPage samplesManagementPage = new SamplesManagementPage(driver);
        samplesManagementPage.selectPath();
        samplesManagementPage.deleteDirectoryContent();
        DelayUtils.sleep(1000);
        samplesManagementPage.uploadSamples("recoSamples/UC_NAR_001/modify/UCNAR001Router_10.20.0.88_20170707_1324_sh_inventory_raw");
        DelayUtils.sleep(1000);
        samplesManagementPage.uploadSamples("recoSamples/UC_NAR_001/modify/UCNAR001Router_10.20.0.88_20170707_1324_sh_version");
    }

    @Test(priority = 6)
    @Description("Search for router in Global Search and open New Inventory View")
    public void searchInGlobalSearchAndOpenInventoryView() {
        PerspectiveChooser.create(driver, webDriverWait).setLivePerspective();
        networkDiscoveryControlViewPage.searchInGlobalSearch(ROUTER_NAME);
        GlobalSearchPage globalSearchPage = new GlobalSearchPage(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        globalSearchPage.filterObjectType(EQUIPMENT_TYPE);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        globalSearchPage.expandShowOnAndChooseView(ROUTER_NAME, ActionsContainer.SHOW_ON_GROUP_ID, "OpenInventoryView");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        NewInventoryViewPage newInventoryViewPage = NewInventoryViewPage.getInventoryViewPage(driver, webDriverWait);
        Assert.assertFalse(newInventoryViewPage.checkIfTableIsEmpty());
    }

    @Test(priority = 7)
    @Description("Run Narrow Reco from context action menu in New Inventory View")
    public void runNarrowReco() {
        NewInventoryViewPage newInventoryViewPage = NewInventoryViewPage.getInventoryViewPage(driver, webDriverWait);
        newInventoryViewPage.selectFirstRow();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        NotificationsInterface notifications = Notifications.create(driver, webDriverWait);
        notifications.clearAllNotification();
        newInventoryViewPage.callAction(ActionsContainer.OTHER_GROUP_ID, "run-narrow-reconciliation");
        DelayUtils.sleep(3000);
        Assert.assertEquals(notifications.waitAndGetFinishedNotificationText(), NARROW_RECO_NOTIFICATION);
        newInventoryViewPage.refreshMainTable();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        newInventoryViewPage.selectFirstRow();
        newInventoryViewPage.callAction(ActionsContainer.SHOW_ON_GROUP_ID, "open-network-inconsistencies-view");
    }

    @Test(priority = 8)
    @Description("Go to Network Inconsistencies View and apply inconsistencies")
    public void goToNIVAndApplyInconsistencies() {
        NetworkInconsistenciesViewPage networkInconsistenciesViewPage = new NetworkInconsistenciesViewPage(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertTrue(networkInconsistenciesViewPage.checkInconsistenciesOperationType().contentEquals("OBJECT MODIFICATION"));
        networkInconsistenciesViewPage.clearOldNotification();
        networkInconsistenciesViewPage.applyInconsistencies();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertEquals(networkInconsistenciesViewPage.checkNotificationAfterApplyInconsistencies(), "Accepting discrepancies related to " + ROUTER_NAME + " finished");
    }

    @Test(priority = 9)
    @Description("Delete CM Domain")
    public void deleteCmDomain() {
        openNetworkDiscoveryControlView();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(CM_DOMAIN_NAME);
        networkDiscoveryControlViewPage.clearOldNotifications();
        networkDiscoveryControlViewPage.deleteCmDomain();
        checkPopupMessageType(MessageType.INFO);
        Assert.assertEquals(networkDiscoveryControlViewPage.checkDeleteCmDomainNotification(), "Deleting CM Domain: " + CM_DOMAIN_NAME + " finished");
    }

    @Test(priority = 10)
    @Description("Delete IP Device")
    public void deleteDevice() {
        PerspectiveChooser.create(driver, webDriverWait).setLivePerspective();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        homePage.goToHomePage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        homePage.setNewObjectType(EQUIPMENT_TYPE);
        NewInventoryViewPage newInventoryViewPage = NewInventoryViewPage.getInventoryViewPage(driver, webDriverWait);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        newInventoryViewPage.searchObject(ROUTER_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        newInventoryViewPage.selectFirstRow().callAction(ActionsContainer.EDIT_GROUP_ID, "DeleteDeviceWizardAction");
        Wizard.createWizard(driver, webDriverWait).clickButtonByLabel("Yes");
        checkPopupMessageType(MessageType.SUCCESS);
    }
}

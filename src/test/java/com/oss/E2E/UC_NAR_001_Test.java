package com.oss.E2E;

import java.net.URISyntaxException;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.alerts.SystemMessageContainer.MessageType;
import com.oss.framework.components.alerts.SystemMessageInterface;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.mainheader.Notifications;
import com.oss.framework.components.mainheader.NotificationsInterface;
import com.oss.framework.components.mainheader.PerspectiveChooser;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.platform.GlobalSearchPage;
import com.oss.pages.platform.NewInventoryViewPage;
import com.oss.pages.reconciliation.NetworkDiscoveryControlViewPage;
import com.oss.pages.reconciliation.NetworkInconsistenciesViewPage;
import com.oss.pages.reconciliation.SamplesManagementPage;

import io.qameta.allure.Description;

public class UC_NAR_001_Test extends BaseTestCase {

    private static final String ROUTER_NAME = "UCNAR001Router";
    private static final String CM_DOMAIN_NAME = "UC_NAR_001";
    private static final String NARROW_RECO_NOTIFICATION = "Narrow reconciliation for GMOCs IPDevice finished";
    private static final String INTERFACE_NAME = "CISCO IOS XR without mediation";
    private static final String DOMAIN = "IP";
    private static final String EQUIPMENT_TYPE = "Physical Device";
    private static final String CONFIRM_ID = "ConfirmationBox_object_delete_wizard_confirmation_box_action_button";
    private SoftAssert softAssert;

    private NetworkDiscoveryControlViewPage networkDiscoveryControlViewPage;

    @BeforeClass
    public void openNetworkDiscoveryControlView() {
        softAssert = new SoftAssert();
        networkDiscoveryControlViewPage = NetworkDiscoveryControlViewPage.goToNetworkDiscoveryControlViewPage(driver, BASIC_URL);
    }

    @Test(priority = 1, description = "Create CM Domain")
    @Description("Set Network perspective, go to Network Discovery Control View and Create CM Domain")
    public void createCmDomain() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        PerspectiveChooser.create(driver, webDriverWait).setNetworkPerspective();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkDiscoveryControlViewPage.createCMDomain(CM_DOMAIN_NAME, INTERFACE_NAME, DOMAIN);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 2, description = "Upload basic samples", dependsOnMethods = {"createCmDomain"})
    @Description("Go to Samples Management view and upload basic samples")
    public void uploadBasicSamples() throws URISyntaxException {
        networkDiscoveryControlViewPage.queryAndSelectDomain(CM_DOMAIN_NAME);
        networkDiscoveryControlViewPage.moveToSamplesManagement();
        SamplesManagementPage samplesManagementPage = new SamplesManagementPage(driver);
        samplesManagementPage.selectPath();
        samplesManagementPage.createDirectory(CM_DOMAIN_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        samplesManagementPage.uploadSamples("recoSamples/UC_NAR_001/create/UCNAR001Router_10.20.0.88_20170707_1324_sh_inventory_raw");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        samplesManagementPage.uploadSamples("recoSamples/UC_NAR_001/create/UCNAR001Router_10.20.0.88_20170707_1324_sh_version");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 3, description = "Run reconciliation with basic samples", dependsOnMethods = {"uploadBasicSamples"})
    @Description("Open Network Discovery Control View, query CM Domain, run reconciliation with basic samples and check if there are no errors after it")
    public void runReconciliationWithBasicSample() {
        openNetworkDiscoveryControlView();
        networkDiscoveryControlViewPage.queryAndSelectDomain(CM_DOMAIN_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkDiscoveryControlViewPage.runReconciliation();
        checkPopupMessageType(MessageType.INFO);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        String status = networkDiscoveryControlViewPage.waitForEndOfReco();
        networkDiscoveryControlViewPage.selectLatestReconciliationState();
        if (status.equals("SUCCESS")) {
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            Assert.assertTrue(networkDiscoveryControlViewPage.checkIssues(NetworkDiscoveryControlViewPage.IssueLevel.ERROR));
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            Assert.assertTrue(networkDiscoveryControlViewPage.checkIssues(NetworkDiscoveryControlViewPage.IssueLevel.WARNING));
        } else {
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            Assert.assertTrue(networkDiscoveryControlViewPage.checkIssues(NetworkDiscoveryControlViewPage.IssueLevel.STARTUP_FATAL));
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            Assert.assertTrue(networkDiscoveryControlViewPage.checkIssues(NetworkDiscoveryControlViewPage.IssueLevel.FATAL));
        }
        Assert.assertEquals(status, "SUCCESS");
    }

    @Test(priority = 4, description = "Apply inconsistencies", dependsOnMethods = {"runReconciliationWithBasicSample"})
    @Description("Move to Network Inconsistencies View, apply inconsistencies and check notification")
    public void applyInconsistencies() {
        networkDiscoveryControlViewPage.moveToNivFromNdcv();
        NetworkInconsistenciesViewPage networkInconsistenciesViewPage = new NetworkInconsistenciesViewPage(driver);
        networkInconsistenciesViewPage.expandTree();
        networkInconsistenciesViewPage.assignLocation(ROUTER_NAME, "1");
        checkPopupMessageType(MessageType.SUCCESS);
        networkInconsistenciesViewPage.clearOldNotification();
        networkInconsistenciesViewPage.applyInconsistencies();
        DelayUtils.sleep(5000);
        Assert.assertEquals(networkInconsistenciesViewPage.checkNotificationAfterApplyInconsistencies(), "Accepting discrepancies related to " + ROUTER_NAME + " finished");
    }

    @Test(priority = 5, description = "Replace samples", dependsOnMethods = {"createCmDomain"})
    @Description("Open Network Discovery Control View, move to Samples Management View and replace old samples")
    public void replaceOldSamples() throws URISyntaxException {
        openNetworkDiscoveryControlView();
        networkDiscoveryControlViewPage.queryAndSelectDomain(CM_DOMAIN_NAME);
        networkDiscoveryControlViewPage.moveToSamplesManagement();
        SamplesManagementPage samplesManagementPage = new SamplesManagementPage(driver);
        samplesManagementPage.selectPath();
        samplesManagementPage.deleteDirectoryContent();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        samplesManagementPage.uploadSamples("recoSamples/UC_NAR_001/modify/UCNAR001Router_10.20.0.88_20170707_1324_sh_inventory_raw");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        samplesManagementPage.uploadSamples("recoSamples/UC_NAR_001/modify/UCNAR001Router_10.20.0.88_20170707_1324_sh_version");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 6, description = "Search router and open it in New Inventory View", dependsOnMethods = {"applyInconsistencies"})
    @Description("Search for router in Global Search and open New Inventory View")
    public void searchInGlobalSearchAndOpenInventoryView() {
        PerspectiveChooser.create(driver, webDriverWait).setLivePerspective();
        networkDiscoveryControlViewPage.searchInGlobalSearch(ROUTER_NAME);
        GlobalSearchPage globalSearchPage = new GlobalSearchPage(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        globalSearchPage.filterObjectType(EQUIPMENT_TYPE);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        globalSearchPage.expandShowOnAndChooseView(ROUTER_NAME, ActionsContainer.SHOW_ON_GROUP_ID, "InventoryView");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        NewInventoryViewPage newInventoryViewPage = NewInventoryViewPage.getInventoryViewPage(driver, webDriverWait);
        Assert.assertFalse(newInventoryViewPage.checkIfTableIsEmpty());
    }

    @Test(priority = 7, description = "Run narrow reconciliation", dependsOnMethods = {"searchInGlobalSearchAndOpenInventoryView"})
    @Description("Run narrow reconciliation from context action in new Inventory View")
    public void runNarrowReco() {
        NewInventoryViewPage newInventoryViewPage = NewInventoryViewPage.getInventoryViewPage(driver, webDriverWait);
        newInventoryViewPage.selectFirstRow();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        NotificationsInterface notifications = Notifications.create(driver, webDriverWait);
        notifications.clearAllNotification();
        newInventoryViewPage.callAction(ActionsContainer.OTHER_GROUP_ID, "run-narrow-reconciliation");
        DelayUtils.sleep(3000);
        Assert.assertEquals(notifications.getNotificationMessage(), NARROW_RECO_NOTIFICATION);
        newInventoryViewPage.refreshMainTable();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        newInventoryViewPage.selectFirstRow();
        newInventoryViewPage.callAction(ActionsContainer.SHOW_ON_GROUP_ID, "open-network-inconsistencies-view");
    }

    @Test(priority = 8, description = "Go to Network Inconsistencies View and apply inconsistencies", dependsOnMethods = {"runNarrowReco"})
    @Description("Go to Network Inconsistencies View, apply inconsistencies and check notification")
    public void goToNIVAndApplyInconsistencies() {
        NetworkInconsistenciesViewPage networkInconsistenciesViewPage = new NetworkInconsistenciesViewPage(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertTrue(networkInconsistenciesViewPage.checkInconsistenciesOperationType().contentEquals("OBJECT MODIFICATION"));
        networkInconsistenciesViewPage.clearOldNotification();
        networkInconsistenciesViewPage.applyInconsistencies();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertEquals(networkInconsistenciesViewPage.checkNotificationAfterApplyInconsistencies(), "Accepting discrepancies related to " + ROUTER_NAME + " finished");
    }

    @Test(priority = 9, description = "Delete CM Domain", dependsOnMethods = {"createCmDomain"})
    @Description("Go to Network Discovery Control View, Delete CM Domain and check notification")
    public void deleteCmDomain() {
        openNetworkDiscoveryControlView();
        networkDiscoveryControlViewPage.queryAndSelectDomain(CM_DOMAIN_NAME);
        networkDiscoveryControlViewPage.clearOldNotifications();
        networkDiscoveryControlViewPage.deleteCmDomain();
        checkPopupMessageType(MessageType.INFO);
        Assert.assertEquals(networkDiscoveryControlViewPage.checkDeleteCmDomainNotification(), "Deleting CM Domain: " + CM_DOMAIN_NAME + " finished");
    }

    @Test(priority = 10, description = "Delete device", dependsOnMethods = {"applyInconsistencies"})
    @Description("Set perspective to Live, open new Inventory View, query device, delete device and check confirmation system message")
    public void deleteDevice() {
        PerspectiveChooser.create(driver, webDriverWait).setLivePerspective();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        homePage.goToHomePage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        homePage.chooseFromLeftSideMenu("Legacy Inventory Dashboard", "Resource Inventory ");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        homePage.setNewObjectType(EQUIPMENT_TYPE);
        NewInventoryViewPage newInventoryViewPage = NewInventoryViewPage.getInventoryViewPage(driver, webDriverWait);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        newInventoryViewPage.searchObject(ROUTER_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        newInventoryViewPage.selectFirstRow().callAction(ActionsContainer.EDIT_GROUP_ID, "DeleteDeviceWizardAction");
        newInventoryViewPage.clickConfirmationBox(CONFIRM_ID);
        checkPopupMessageType(MessageType.SUCCESS);
    }

    private void checkPopupMessageType(MessageType messageType) {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        softAssert.assertNotNull(messages);
        softAssert.assertEquals(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getMessageType(),
                messageType);
    }
}

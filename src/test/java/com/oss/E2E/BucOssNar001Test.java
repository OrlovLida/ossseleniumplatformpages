package com.oss.E2E;

import java.net.URISyntaxException;
import java.time.Duration;
import java.util.List;

import org.openqa.selenium.support.ui.WebDriverWait;
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
import com.oss.pages.logicalfunction.LogicalFunctionWizardPreStep;
import com.oss.pages.platform.GlobalSearchPage;
import com.oss.pages.platform.NewInventoryViewPage;
import com.oss.pages.platform.SearchObjectTypePage;
import com.oss.pages.reconciliation.NetworkDiscoveryControlViewPage;
import com.oss.pages.reconciliation.NetworkInconsistenciesViewPage;
import com.oss.pages.reconciliation.SamplesManagementPage;

import io.qameta.allure.Description;

public class BucOssNar001Test extends BaseTestCase {

    private static final String OBJECT_NAME = "UCNAR001Router";
    private static final String CM_DOMAIN_NAME = "UC_NAR_001";
    private static final String NARROW_RECO_NOTIFICATION = "Narrow reconciliation for GMOCs IPDevice finished";
    private static final String INTERFACE_NAME = "CISCO IOS XR without mediation";
    private static final String DOMAIN = "IP";
    private static final String DEVICE_TYPE = "Physical Device";
    private static final String LF_TYPE = "Logical Function";
    private static final String CONFIRM_ID = "ConfirmationBox_object_delete_wizard_confirmation_box_action_button";
    private static final String DELETE_LF_BUTTON_ID = "logicalInventory_DeleteLogicalFunctionActionModifyId";
    private static final String DELETE_DEVICE_BUTTON_ID = "DeleteDeviceWizardAction";
    private final static String SYSTEM_MESSAGE_PATTERN = "%s. Checking system message after %s.";

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
        waitForPageToLoad();
        PerspectiveChooser.create(driver, webDriverWait).setNetworkPerspective();
        waitForPageToLoad();
        networkDiscoveryControlViewPage.createCMDomain(CM_DOMAIN_NAME, INTERFACE_NAME, DOMAIN);
        waitForPageToLoad();
    }

    @Test(priority = 2, description = "Upload basic samples", dependsOnMethods = {"createCmDomain"})
    @Description("Go to Samples Management view and upload basic samples")
    public void uploadBasicSamples() throws URISyntaxException {
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(CM_DOMAIN_NAME);
        networkDiscoveryControlViewPage.moveToSamplesManagement();
        SamplesManagementPage samplesManagementPage = new SamplesManagementPage(driver);
        samplesManagementPage.selectPath();
        samplesManagementPage.createDirectory(CM_DOMAIN_NAME);
        waitForPageToLoad();
        samplesManagementPage.uploadSamples("recoSamples/UC_NAR_001/create/UCNAR001Router_10.20.0.88_20170707_1324_sh_inventory_raw");
        waitForPageToLoad();
        samplesManagementPage.uploadSamples("recoSamples/UC_NAR_001/create/UCNAR001Router_10.20.0.88_20170707_1324_sh_version");
        waitForPageToLoad();
    }

    @Test(priority = 3, description = "Run reconciliation with basic samples", dependsOnMethods = {"uploadBasicSamples"})
    @Description("Open Network Discovery Control View, query CM Domain, run reconciliation with basic samples and check if there are no errors after it")
    public void runReconciliationWithBasicSample() {
        openNetworkDiscoveryControlView();
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(CM_DOMAIN_NAME);
        waitForPageToLoad();
        networkDiscoveryControlViewPage.runReconciliation();
        checkPopupMessageType(MessageType.INFO, String.format(SYSTEM_MESSAGE_PATTERN, "Run reconciliation with basic sample", "starting reconciliation"));
        waitForPageToLoad();
        String status = networkDiscoveryControlViewPage.waitForEndOfReco();
        networkDiscoveryControlViewPage.selectLatestReconciliationState();
        waitForPageToLoad();
        networkDiscoveryControlViewPage.checkIssues(NetworkDiscoveryControlViewPage.IssueLevel.INFO);
        if (status.equals("SUCCESS")) {
            waitForPageToLoad();
            Assert.assertTrue(networkDiscoveryControlViewPage.checkIssues(NetworkDiscoveryControlViewPage.IssueLevel.ERROR));
            waitForPageToLoad();
            Assert.assertTrue(networkDiscoveryControlViewPage.checkIssues(NetworkDiscoveryControlViewPage.IssueLevel.WARNING));
        } else {
            waitForPageToLoad();
            Assert.assertTrue(networkDiscoveryControlViewPage.checkIssues(NetworkDiscoveryControlViewPage.IssueLevel.STARTUP_FATAL));
            waitForPageToLoad();
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
        networkInconsistenciesViewPage.assignLocation(OBJECT_NAME, "1");
        checkPopupMessageType(MessageType.SUCCESS, String.format(SYSTEM_MESSAGE_PATTERN, "Apply inconsistencies", "assigning location"));
        networkInconsistenciesViewPage.clearOldNotification();
        networkInconsistenciesViewPage.applyInconsistencies();
        DelayUtils.sleep(5000);
        Assert.assertEquals(networkInconsistenciesViewPage.checkNotificationAfterApplyInconsistencies(), "Accepting discrepancies related to " + OBJECT_NAME + " finished");
    }

    @Test(priority = 5, description = "Replace samples", dependsOnMethods = {"createCmDomain"})
    @Description("Open Network Discovery Control View, move to Samples Management View and replace old samples")
    public void replaceOldSamples() throws URISyntaxException {
        openNetworkDiscoveryControlView();
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(CM_DOMAIN_NAME);
        networkDiscoveryControlViewPage.moveToSamplesManagement();
        SamplesManagementPage samplesManagementPage = new SamplesManagementPage(driver);
        samplesManagementPage.selectPath();
        samplesManagementPage.deleteDirectoryContent();
        waitForPageToLoad();
        samplesManagementPage.uploadSamples("recoSamples/UC_NAR_001/modify/UCNAR001Router_10.20.0.88_20170707_1324_sh_inventory_raw");
        waitForPageToLoad();
        samplesManagementPage.uploadSamples("recoSamples/UC_NAR_001/modify/UCNAR001Router_10.20.0.88_20170707_1324_sh_version");
        waitForPageToLoad();
    }

    @Test(priority = 6, description = "Search router and open it in New Inventory View", dependsOnMethods = {"applyInconsistencies"})
    @Description("Search for router in Global Search and open New Inventory View")
    public void searchInGlobalSearchAndOpenInventoryView() {
        PerspectiveChooser.create(driver, webDriverWait).setLivePerspective();
        networkDiscoveryControlViewPage.searchInGlobalSearch(OBJECT_NAME);
        GlobalSearchPage globalSearchPage = new GlobalSearchPage(driver);
        waitForPageToLoad();
        globalSearchPage.filterObjectType(DEVICE_TYPE);
        waitForPageToLoad();
        globalSearchPage.expandShowOnAndChooseView(OBJECT_NAME, ActionsContainer.SHOW_ON_GROUP_ID, "InventoryView");
        waitForPageToLoad();
        NewInventoryViewPage newInventoryViewPage = NewInventoryViewPage.getInventoryViewPage(driver, webDriverWait);
        Assert.assertFalse(newInventoryViewPage.checkIfTableIsEmpty());
    }

    @Test(priority = 7, description = "Run narrow reconciliation", dependsOnMethods = {"searchInGlobalSearchAndOpenInventoryView"})
    @Description("Run narrow reconciliation from context action in new Inventory View")
    public void runNarrowReco() {
        NewInventoryViewPage newInventoryViewPage = NewInventoryViewPage.getInventoryViewPage(driver, webDriverWait);
        newInventoryViewPage.selectFirstRow();
        waitForPageToLoad();
        NotificationsInterface notifications = Notifications.create(driver, new WebDriverWait(driver, Duration.ofSeconds(180)));
        notifications.clearAllNotification();
        newInventoryViewPage.callAction(ActionsContainer.OTHER_GROUP_ID, "run-narrow-reconciliation");
        DelayUtils.sleep(3000);
        Assert.assertEquals(notifications.getNotificationMessage(), NARROW_RECO_NOTIFICATION);
        newInventoryViewPage.refreshMainTable();
        waitForPageToLoad();
        newInventoryViewPage.selectFirstRow();
        newInventoryViewPage.callAction(ActionsContainer.SHOW_ON_GROUP_ID, "open-network-inconsistencies-view");
    }

    @Test(priority = 8, description = "Go to Network Inconsistencies View and apply inconsistencies", dependsOnMethods = {"runNarrowReco"})
    @Description("Go to Network Inconsistencies View, apply inconsistencies and check notification")
    public void goToNIVAndApplyInconsistencies() {
        NetworkInconsistenciesViewPage networkInconsistenciesViewPage = new NetworkInconsistenciesViewPage(driver);
        waitForPageToLoad();
        Assert.assertTrue(networkInconsistenciesViewPage.checkInconsistenciesOperationType().contentEquals("OBJECT MODIFICATION"));
        networkInconsistenciesViewPage.clearOldNotification();
        networkInconsistenciesViewPage.applyInconsistencies();
        waitForPageToLoad();
        Assert.assertEquals(networkInconsistenciesViewPage.checkNotificationAfterApplyInconsistencies(), "Accepting discrepancies related to " + OBJECT_NAME + " finished");
    }

    @Test(priority = 9, description = "Delete CM Domain", dependsOnMethods = {"createCmDomain"})
    @Description("Go to Network Discovery Control View, Delete CM Domain and check notification")
    public void deleteCmDomain() {
        openNetworkDiscoveryControlView();
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(CM_DOMAIN_NAME);
        networkDiscoveryControlViewPage.clearOldNotifications();
        networkDiscoveryControlViewPage.deleteCmDomain();
        checkPopupMessageType(MessageType.INFO, String.format(SYSTEM_MESSAGE_PATTERN, "Delete CM Domain", "CM Domain delete"));
        Assert.assertEquals(networkDiscoveryControlViewPage.checkDeleteCmDomainNotification(), "Deleting CM Domain: " + CM_DOMAIN_NAME + " finished");
    }

    @Test(priority = 10, description = "Delete Logical Function", dependsOnMethods = {"applyInconsistencies"})
    @Description("Set perspective to Live, open new Inventory View, query Logical Function, delete Logical Function and check confirmation system message")
    public void deleteLogicalFunction() {
        PerspectiveChooser.create(driver, webDriverWait).setLivePerspective();
        waitForPageToLoad();
        homePage.chooseFromLeftSideMenu("Inventory View", "Resource Inventory");
        waitForPageToLoad();
        SearchObjectTypePage searchObjectTypePage = new SearchObjectTypePage(driver, webDriverWait);
        searchObjectTypePage.searchType(LF_TYPE);
        waitForPageToLoad();
        NewInventoryViewPage newInventoryViewPage = NewInventoryViewPage.getInventoryViewPage(driver, webDriverWait);
        waitForPageToLoad();
        newInventoryViewPage.searchObject(OBJECT_NAME);
        waitForPageToLoad();
        newInventoryViewPage.selectFirstRow().callAction(ActionsContainer.EDIT_GROUP_ID, DELETE_LF_BUTTON_ID);
        LogicalFunctionWizardPreStep logicalFunctionWizardPreStep = LogicalFunctionWizardPreStep.create(driver, webDriverWait);
        logicalFunctionWizardPreStep.clickAccept();
        checkPopupMessageType(MessageType.SUCCESS, String.format(SYSTEM_MESSAGE_PATTERN, "Delete logical function", "logical function delete"));
    }

    @Test(priority = 11, description = "Delete device", dependsOnMethods = {"applyInconsistencies"})
    @Description("Open new Inventory View, query device, delete device and check confirmation system message")
    public void deleteDevice() {
        homePage.chooseFromLeftSideMenu("Inventory View", "Resource Inventory");
        waitForPageToLoad();
        SearchObjectTypePage searchObjectTypePage = new SearchObjectTypePage(driver, webDriverWait);
        searchObjectTypePage.searchType(DEVICE_TYPE);
        waitForPageToLoad();
        NewInventoryViewPage newInventoryViewPage = NewInventoryViewPage.getInventoryViewPage(driver, webDriverWait);
        waitForPageToLoad();
        newInventoryViewPage.searchObject(OBJECT_NAME);
        waitForPageToLoad();
        newInventoryViewPage.selectFirstRow().callAction(ActionsContainer.EDIT_GROUP_ID, DELETE_DEVICE_BUTTON_ID);
        newInventoryViewPage.clickConfirmationBox(CONFIRM_ID);
        checkPopupMessageType(MessageType.SUCCESS, String.format(SYSTEM_MESSAGE_PATTERN, "Delete device", "device delete"));
    }

    @Test(priority = 12, description = "Checking system message summary")
    @Description("Checking system message summary")
    public void systemMessageSummary() {
        softAssert.assertAll();
    }

    private void checkPopupMessageType(MessageType messageType, String systemMessageLog) {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        softAssert.assertNotNull(messages, systemMessageLog);
        softAssert.assertEquals(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getMessageType(),
                messageType, systemMessageLog);
    }

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }
}

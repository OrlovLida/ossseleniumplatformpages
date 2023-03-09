package com.oss.E2E;

import java.net.URISyntaxException;
import java.time.Duration;
import java.util.List;

import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.comarch.oss.web.pages.GlobalSearchPage;
import com.comarch.oss.web.pages.NewInventoryViewPage;
import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.alerts.SystemMessageContainer.MessageType;
import com.oss.framework.components.alerts.SystemMessageInterface;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.mainheader.Notifications;
import com.oss.framework.components.mainheader.NotificationsInterface;
import com.oss.framework.components.mainheader.PerspectiveChooser;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.reconciliation.CmDomainWizardPage;
import com.oss.pages.reconciliation.NetworkDiscoveryControlViewPage;
import com.oss.pages.reconciliation.NetworkInconsistenciesViewPage;
import com.oss.pages.reconciliation.SamplesManagementPage;

import io.qameta.allure.Description;

public class BucOssNar001Test extends BaseTestCase {

    private static final String OBJECT_NAME = "KRK-SSE1-3";
    private static final String CM_DOMAIN_NAME = "KRK-SSE1-3";
    private static final String NARROW_RECO_NOTIFICATION = "Narrow reconciliation for GMOCs IPDevice finished";
    private static final String INTERFACE_NAME = "CISCO IOS 12/15/XE without mediation";
    private static final String DOMAIN = "IP";
    private static final String DEVICE_TYPE = "Physical Device";
    private final static String SYSTEM_MESSAGE_PATTERN = "%s. Checking system message after %s.";
    private final static String ROUTE_TARGET = "RouteTarget";
    private final static String SUCCESS = "SUCCESS";

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
        if (CSSUtils.isElementPresent(driver, CmDomainWizardPage.CM_DOMAIN_WIZARD_ID)) {
            CmDomainWizardPage wizard = new CmDomainWizardPage(driver);
            wizard.cancel();
            Assert.fail("Create CM Domain wizard was still open.");
        }
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
        samplesManagementPage.uploadSamples("recoSamples/UC_NAR_001/create/KRK-SSE1-3_10.166.10.1_20181107_1306_ip_interfaces");
        waitForPageToLoad();
        samplesManagementPage.uploadSamples("recoSamples/UC_NAR_001/create/KRK-SSE1-3_10.166.10.1_20181107_1306_running-config");
        waitForPageToLoad();
        samplesManagementPage.uploadSamples("recoSamples/UC_NAR_001/create/KRK-SSE1-3_10.166.10.1_20181107_1306_sh_inventory_raw");
        waitForPageToLoad();
        samplesManagementPage.uploadSamples("recoSamples/UC_NAR_001/create/KRK-SSE1-3_10.166.10.1_20181107_1306_sh_version");
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
        if (status.equals(SUCCESS)) {
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
        Assert.assertEquals(status, SUCCESS);
    }

    @Test(priority = 4, description = "Apply inconsistencies", dependsOnMethods = {"runReconciliationWithBasicSample"})
    @Description("Move to Network Inconsistencies View, apply inconsistencies and check notification")
    public void applyInconsistencies() {
        networkDiscoveryControlViewPage.moveToNivFromNdcv();
        NetworkInconsistenciesViewPage networkInconsistenciesViewPage = new NetworkInconsistenciesViewPage(driver);
        if (networkInconsistenciesViewPage.isInconsistencyPresentContains(ROUTE_TARGET)) {
            networkInconsistenciesViewPage.expandTreeRowContains(ROUTE_TARGET);
            networkInconsistenciesViewPage.selectTreeObjectByRowOrder(3);
            Notifications.create(driver, webDriverWait).clearAllNotification();
            networkInconsistenciesViewPage.applySelectedInconsistenciesGroup();
            DelayUtils.sleep(5000);
            Assert.assertEquals(Notifications.create(driver, new WebDriverWait(driver, Duration.ofSeconds(150))).getNotificationMessage(), String.format(NetworkInconsistenciesViewPage.ACCEPT_DISCREPANCIES_PATTERN, ROUTE_TARGET));
        }
        networkInconsistenciesViewPage.expandTreeRowContains("IPDevice");
        networkInconsistenciesViewPage.expandTreeRowContains(OBJECT_NAME);
        waitForPageToLoad();
        networkInconsistenciesViewPage.assignLocation(OBJECT_NAME, "1");
        checkPopupMessageType(MessageType.SUCCESS, String.format(SYSTEM_MESSAGE_PATTERN, "Apply inconsistencies", "assigning location"));
        Notifications.create(driver, webDriverWait).clearAllNotification();
        networkInconsistenciesViewPage.applyFirstInconsistenciesGroupWithPrerequsites();
        DelayUtils.sleep(5000);
        Assert.assertEquals(Notifications.create(driver, new WebDriverWait(driver, Duration.ofSeconds(150))).getNotificationMessage(), String.format(NetworkInconsistenciesViewPage.ACCEPT_DISCREPANCIES_PATTERN, OBJECT_NAME));
    }

    @Test(priority = 5, description = "Replace samples", dependsOnMethods = {"applyInconsistencies"})
    @Description("Open Network Discovery Control View, move to Samples Management View and replace old samples")
    public void replaceOldSamples() throws URISyntaxException {
        openNetworkDiscoveryControlView();
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(CM_DOMAIN_NAME);
        networkDiscoveryControlViewPage.moveToSamplesManagement();
        SamplesManagementPage samplesManagementPage = new SamplesManagementPage(driver);
        samplesManagementPage.selectPath();
        samplesManagementPage.deleteDirectoryContent();
        waitForPageToLoad();
        samplesManagementPage.uploadSamples("recoSamples/UC_NAR_001/modify/KRK-SSE1-3_10.166.10.1_20181107_1306_ip_interfaces");
        waitForPageToLoad();
        samplesManagementPage.uploadSamples("recoSamples/UC_NAR_001/modify/KRK-SSE1-3_10.166.10.1_20181107_1306_running-config");
        waitForPageToLoad();
        samplesManagementPage.uploadSamples("recoSamples/UC_NAR_001/modify/KRK-SSE1-3_10.166.10.1_20181107_1306_sh_inventory_raw");
        waitForPageToLoad();
        samplesManagementPage.uploadSamples("recoSamples/UC_NAR_001/modify/KRK-SSE1-3_10.166.10.1_20181107_1306_sh_version");
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
        Notifications.create(driver, webDriverWait).clearAllNotification();
        networkInconsistenciesViewPage.applyFirstInconsistenciesGroupWithPrerequsites();
        waitForPageToLoad();
        Assert.assertEquals(Notifications.create(driver, new WebDriverWait(driver, Duration.ofSeconds(150))).getNotificationMessage(), String.format(NetworkInconsistenciesViewPage.ACCEPT_DISCREPANCIES_PATTERN, OBJECT_NAME));
    }

    @Test(priority = 9, description = "Replace samples", dependsOnMethods = {"createCmDomain"})
    @Description("Open Network Discovery Control View, move to Samples Management View and replace old samples")
    public void uploadEmptySamples() throws URISyntaxException {
        openNetworkDiscoveryControlView();
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(CM_DOMAIN_NAME);
        networkDiscoveryControlViewPage.moveToSamplesManagement();
        SamplesManagementPage samplesManagementPage = new SamplesManagementPage(driver);
        samplesManagementPage.selectPath();
        samplesManagementPage.deleteDirectoryContent();
        waitForPageToLoad();
        samplesManagementPage.uploadSamples("recoSamples/UC_NAR_001/empty/Selenium1_10.252.255.201_20170707_1324_sh_version");
        waitForPageToLoad();
        samplesManagementPage.uploadSamples("recoSamples/UC_NAR_001/empty/Selenium1_10.252.255.201_20170707_1324_sh_inventory_raw");
        waitForPageToLoad();
        samplesManagementPage.uploadSamples("recoSamples/UC_NAR_001/empty/Selenium1_10.252.255.201_20170707_1324_running-config");
        waitForPageToLoad();
    }

    @Test(priority = 10, description = "Run reconciliation and check results", dependsOnMethods = {"uploadEmptySamples"})
    @Description("Go to Network Discovery Control View, run reconciliation and check if it ended without errors")
    public void runReconciliationWithEmptySample() {
        networkDiscoveryControlViewPage = NetworkDiscoveryControlViewPage.goToNetworkDiscoveryControlViewPage(driver, BASIC_URL);
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(CM_DOMAIN_NAME);
        waitForPageToLoad();
        networkDiscoveryControlViewPage.runReconciliation();
        checkPopupMessageType(MessageType.INFO, String.format(SYSTEM_MESSAGE_PATTERN, "Run reconciliation with empty sample", "starting reconciliation"));
        waitForPageToLoad();
        String status = networkDiscoveryControlViewPage.waitForEndOfReco();
        networkDiscoveryControlViewPage.selectLatestReconciliationState();
        waitForPageToLoad();
        networkDiscoveryControlViewPage.checkIssues(NetworkDiscoveryControlViewPage.IssueLevel.INFO);
        if (status.equals(SUCCESS)) {
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
        Assert.assertEquals(status, SUCCESS);
    }

    @Test(priority = 11, description = "Replace samples", dependsOnMethods = {"runReconciliationWithEmptySample"})
    @Description("Open Network Discovery Control View, move to Samples Management View and replace old samples")
    public void acceptInconsistencies() {
        Notifications.create(driver, webDriverWait).clearAllNotification();
        networkDiscoveryControlViewPage.acceptWithPrerequisites();
        waitForPageToLoad();
        Assert.assertEquals(Notifications.create(driver, webDriverWait).getNotificationMessage(), String.format(NetworkInconsistenciesViewPage.ACCEPT_DISCREPANCIES_PATTERN, CM_DOMAIN_NAME));
    }

    @Test(priority = 12, description = "Delete CM Domain", dependsOnMethods = {"createCmDomain"})
    @Description("Go to Network Discovery Control View, Delete CM Domain and check notification")
    public void deleteCmDomain() {
        openNetworkDiscoveryControlView();
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(CM_DOMAIN_NAME);
        networkDiscoveryControlViewPage.cancelWithSubsequents();
        waitForPageToLoad();
        Notifications.create(driver, webDriverWait).clearAllNotification();
        networkDiscoveryControlViewPage.deleteCmDomain();
        checkPopupMessageType(MessageType.INFO, String.format(SYSTEM_MESSAGE_PATTERN, "Delete CM Domain", "CM Domain delete"));
        Assert.assertEquals(Notifications.create(driver, webDriverWait).getNotificationMessage(), "Deleting CM Domain: " + CM_DOMAIN_NAME + " finished");
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

package com.oss.E2E;

import java.net.URISyntaxException;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.alerts.SystemMessageContainer.Message;
import com.oss.framework.components.alerts.SystemMessageContainer.MessageType;
import com.oss.framework.components.alerts.SystemMessageInterface;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.mainheader.PerspectiveChooser;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.platform.GlobalSearchPage;
import com.oss.pages.platform.NewInventoryViewPage;
import com.oss.pages.reconciliation.CmDomainWizardPage;
import com.oss.pages.reconciliation.NetworkDiscoveryControlViewPage;
import com.oss.pages.reconciliation.NetworkInconsistenciesViewPage;
import com.oss.pages.reconciliation.SamplesManagementPage;
import com.oss.utils.TestListener;

import io.qameta.allure.Description;

@Listeners({TestListener.class})
public class BucOssNar004Test extends BaseTestCase {

    private static final String CM_DOMAIN_NAME = "KRK-SSE8-45";
    private static final String INTERFACE_NAME = "CISCO IOS 12/15/XE without mediation";
    private static final String DOMAIN = "IP";
    private static final String EQUIPMENT_TYPE = "Physical Device";
    private static final String ROUTER_NAME = "KRK-SSE8-45";
    private final static String SYSTEM_MESSAGE_PATTERN = "%s. Checking system message after %s.";
    private NetworkDiscoveryControlViewPage networkDiscoveryControlViewPage;
    private SoftAssert softAssert;

    @BeforeClass
    public void openConsole() {
        waitForPageToLoad();
        softAssert = new SoftAssert();
    }

    @Test(priority = 1, description = "Create CM Domain")
    @Description("Go to Network Discovery Control View and Create CM Domain")
    public void createCmDomain() {
        networkDiscoveryControlViewPage = NetworkDiscoveryControlViewPage.goToNetworkDiscoveryControlViewPage(driver, BASIC_URL);
        waitForPageToLoad();
        networkDiscoveryControlViewPage.openCmDomainWizard();
        CmDomainWizardPage wizard = new CmDomainWizardPage(driver);
        wizard.setName(CM_DOMAIN_NAME);
        wizard.setInterface(INTERFACE_NAME);
        wizard.setDomain(DOMAIN);
        wizard.save();
        waitForPageToLoad();
    }

    @Test(priority = 2, description = "Upload reconciliation samples", dependsOnMethods = {"createCmDomain"})
    @Description("Go to Sample Management View and upload reconciliation samples")
    public void uploadSamples() throws URISyntaxException {
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(CM_DOMAIN_NAME);
        networkDiscoveryControlViewPage.moveToSamplesManagement();
        SamplesManagementPage samplesManagementPage = new SamplesManagementPage(driver);
        samplesManagementPage.selectPath();
        waitForPageToLoad();
        samplesManagementPage.createDirectory(CM_DOMAIN_NAME);
        waitForPageToLoad();
        samplesManagementPage.uploadSamples("recoSamples/UC_NAR_004/KRK-SSE8-45_10.166.10.1_20181107_1306_running-config");
        waitForPageToLoad();
        samplesManagementPage.uploadSamples("recoSamples/UC_NAR_004/KRK-SSE8-45_10.166.10.1_20181107_1306_sh_inventory_raw");
        waitForPageToLoad();
        samplesManagementPage.uploadSamples("recoSamples/UC_NAR_004/KRK-SSE8-45_10.166.10.1_20181107_1306_sh_version");
        waitForPageToLoad();
    }

    @Test(priority = 3, description = "Run reconciliation and check results", dependsOnMethods = {"uploadSamples"})
    @Description("Go to Network Discovery Control View and run reconciliation and check if it ended without errors")
    public void runReconciliationWithFullSample() {
        networkDiscoveryControlViewPage = NetworkDiscoveryControlViewPage.goToNetworkDiscoveryControlViewPage(driver, BASIC_URL);
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(CM_DOMAIN_NAME);
        waitForPageToLoad();
        networkDiscoveryControlViewPage.runReconciliation();
        checkPopupMessageType(String.format(SYSTEM_MESSAGE_PATTERN, "Run reconciliation with full sample", "starting reconciliation"));
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

    @Test(priority = 4, description = "Check inconsistencies", dependsOnMethods = {"runReconciliationWithFullSample"})
    @Description("Go to Network Inconsistencies View and check discrepancy type")
    public void checkOperationType() {
        networkDiscoveryControlViewPage.moveToNivFromNdcv();
        NetworkInconsistenciesViewPage networkInconsistenciesViewPage = new NetworkInconsistenciesViewPage(driver);
        waitForPageToLoad();
        networkInconsistenciesViewPage.expandTree();
        networkInconsistenciesViewPage.selectTreeObjectByRowOrder(3);
        DelayUtils.sleep(3000);
        waitForPageToLoad();
        Assert.assertTrue(networkInconsistenciesViewPage.checkInconsistenciesOperationType().contentEquals("CREATION"));
    }

    @Test(priority = 5, description = "Search and open router in Inventory View", dependsOnMethods = {"runReconciliationWithFullSample"})
    @Description("Set Network perspective and search for router in Global Search and check it in New Inventory View")
    public void searchInGlobalSearchAndOpenInventoryView() {
        PerspectiveChooser.create(driver, webDriverWait).setNetworkPerspective();
        waitForPageToLoad();
        networkDiscoveryControlViewPage.searchInGlobalSearch(ROUTER_NAME);
        GlobalSearchPage globalSearchPage = new GlobalSearchPage(driver);
        waitForPageToLoad();
        globalSearchPage.filterObjectType(EQUIPMENT_TYPE);
        globalSearchPage.expandShowOnAndChooseView(ROUTER_NAME, ActionsContainer.SHOW_ON_GROUP_ID, "InventoryView");
        waitForPageToLoad();
        NewInventoryViewPage newInventoryViewPage = NewInventoryViewPage.getInventoryViewPage(driver, webDriverWait);
        Assert.assertFalse(newInventoryViewPage.checkIfTableIsEmpty());
    }

    @Test(priority = 6, description = "Change reconciliation samples to empty ones", dependsOnMethods = {"createCmDomain"})
    @Description("Move to Samples Management from Network Discovery Control View, delete old reconciliation samples and upload empty samples")
    public void deleteOldSamplesAndPutNewOne() throws URISyntaxException {
        networkDiscoveryControlViewPage = NetworkDiscoveryControlViewPage.goToNetworkDiscoveryControlViewPage(driver, BASIC_URL);
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(CM_DOMAIN_NAME);
        waitForPageToLoad();
        networkDiscoveryControlViewPage.moveToSamplesManagement();
        SamplesManagementPage samplesManagementPage = new SamplesManagementPage(driver);
        samplesManagementPage.selectPath();
        waitForPageToLoad();
        samplesManagementPage.deleteDirectoryContent();
        waitForPageToLoad();
        samplesManagementPage.uploadSamples("recoSamples/ciscoIOS/empty/Selenium1_10.252.255.201_20170707_1324_running-config");
        waitForPageToLoad();
        samplesManagementPage.uploadSamples("recoSamples/ciscoIOS/empty/Selenium1_10.252.255.201_20170707_1324_sh_inventory_raw");
        waitForPageToLoad();
        samplesManagementPage.uploadSamples("recoSamples/ciscoIOS/empty/Selenium1_10.252.255.201_20170707_1324_sh_version");
        waitForPageToLoad();
    }

    @Test(priority = 7, description = "Run reconciliation and check results", dependsOnMethods = {"deleteOldSamplesAndPutNewOne"})
    @Description("Go to Network Discovery Control View, run reconciliation and check if it ended without errors")
    public void runReconciliationWithEmptySample() {
        networkDiscoveryControlViewPage = NetworkDiscoveryControlViewPage.goToNetworkDiscoveryControlViewPage(driver, BASIC_URL);
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(CM_DOMAIN_NAME);
        waitForPageToLoad();
        networkDiscoveryControlViewPage.runReconciliation();
        checkPopupMessageType(String.format(SYSTEM_MESSAGE_PATTERN, "Run reconciliation with empty sample", "starting reconciliation"));
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

    @Test(priority = 8, description = "Delete CM Domain", dependsOnMethods = {"createCmDomain"})
    @Description("Delete CM Domain")
    public void deleteCmDomain() {
        networkDiscoveryControlViewPage = NetworkDiscoveryControlViewPage.goToNetworkDiscoveryControlViewPage(driver, BASIC_URL);
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(CM_DOMAIN_NAME);
        networkDiscoveryControlViewPage.clearOldNotifications();
        networkDiscoveryControlViewPage.deleteCmDomain();
        checkPopupMessageType(String.format(SYSTEM_MESSAGE_PATTERN, "Delete CM Domain", "CM Domain delete"));
        Assert.assertEquals(networkDiscoveryControlViewPage.checkDeleteCmDomainNotification(), "Deleting CM Domain: " + CM_DOMAIN_NAME + " finished");
    }

    @Test(priority = 9, description = "Checking system message summary")
    @Description("Checking system message summary")
    public void systemMessageSummary() {
        softAssert.assertAll();
    }

    private void checkPopupMessageType(String systemMessageLog) {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<Message> messages = systemMessage.getMessages();
        softAssert.assertNotNull(messages, systemMessageLog);
        softAssert.assertEquals(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getMessageType(),
                MessageType.INFO, systemMessageLog);
    }

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

}

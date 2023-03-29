package com.oss.E2E;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.comarch.oss.web.pages.GlobalSearchPage;
import com.comarch.oss.web.pages.NewInventoryViewPage;
import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.alerts.SystemMessageContainer.Message;
import com.oss.framework.components.alerts.SystemMessageContainer.MessageType;
import com.oss.framework.components.alerts.SystemMessageInterface;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.mainheader.Notifications;
import com.oss.framework.components.mainheader.PerspectiveChooser;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.reconciliation.CmDomainWizardPage;
import com.oss.pages.reconciliation.NetworkDiscoveryControlViewPage;
import com.oss.pages.reconciliation.NetworkInconsistenciesViewPage;
import com.oss.pages.reconciliation.SamplesManagementPage;
import com.oss.reconciliation.infrastructure.recoConfig.RecoConfigClient;
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
    private static final String RECO_CONFIG_BODY = "{\n" +
            "  \"adaptation-cisco-cli-ios15\": {\n" +
            "    \"mappingDomainName\": \"CiscoCLI\",\n" +
            "    \"assignGenericModels\": \"true\",\n" +
            "    \"createBlackboxModels\": \"false\"\n" +
            "  }\n" +
            "}";
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
        networkDiscoveryControlViewPage.createCMDomain(CM_DOMAIN_NAME, INTERFACE_NAME, DOMAIN);
        waitForPageToLoad();
        if (CSSUtils.isElementPresent(driver, CmDomainWizardPage.CM_DOMAIN_WIZARD_ID)) {
            CmDomainWizardPage wizard = new CmDomainWizardPage(driver);
            wizard.cancel();
            Assert.fail("Create CM Domain wizard was still open.");
        }
    }

    @Test(priority = 2, description = "Set properties for CM Domain and upload reconciliation samples", dependsOnMethods = {"createCmDomain"})
    @Description("Set the properties for the CM Domain using appropriate endpoint. Go to Sample Management View and upload reconciliation samples")
    public void uploadSamples() throws URISyntaxException, IOException {
        RecoConfigClient recoConfigClient = RecoConfigClient.getInstance(environmentRequestClient);
        recoConfigClient.createRecoConfig(RECO_CONFIG_BODY, CM_DOMAIN_NAME);
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(CM_DOMAIN_NAME);
        networkDiscoveryControlViewPage.moveToSamplesManagement();
        SamplesManagementPage samplesManagementPage = new SamplesManagementPage(driver);
        samplesManagementPage.selectPath();
        waitForPageToLoad();
        samplesManagementPage.createDirectory(CM_DOMAIN_NAME);
        samplesManagementPage.uploadSamplesFromPath("recoSamples/UC_NAR_004");
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
        networkInconsistenciesViewPage.expandTwoLastTreeRows();
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
    public void deleteOldSamplesAndPutNewOne() throws URISyntaxException, IOException {
        networkDiscoveryControlViewPage = NetworkDiscoveryControlViewPage.goToNetworkDiscoveryControlViewPage(driver, BASIC_URL);
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(CM_DOMAIN_NAME);
        waitForPageToLoad();
        networkDiscoveryControlViewPage.moveToSamplesManagement();
        SamplesManagementPage samplesManagementPage = new SamplesManagementPage(driver);
        samplesManagementPage.selectPath();
        waitForPageToLoad();
        samplesManagementPage.deleteDirectoryContent();
        samplesManagementPage.uploadSamplesFromPath("recoSamples/ciscoIOS/empty");
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
        networkDiscoveryControlViewPage.cancelWithSubsequents();
        waitForPageToLoad();
        Notifications.create(driver, webDriverWait).clearAllNotification();
        networkDiscoveryControlViewPage.deleteCmDomain();
        checkPopupMessageType(String.format(SYSTEM_MESSAGE_PATTERN, "Delete CM Domain", "CM Domain delete"));
        Assert.assertEquals(Notifications.create(driver, webDriverWait).getNotificationMessage(), "Deleting CM Domain: " + CM_DOMAIN_NAME + " finished");
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

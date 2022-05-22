package com.oss.reconciliation;

import java.net.URISyntaxException;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.alerts.SystemMessageInterface;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.mainheader.PerspectiveChooser;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.platform.GlobalSearchPage;
import com.oss.pages.platform.NewInventoryViewPage;
import com.oss.pages.reconciliation.NetworkDiscoveryControlViewPage;
import com.oss.pages.reconciliation.NetworkOverlapPage;
import com.oss.pages.reconciliation.ResolveConflictWizardPage;
import com.oss.pages.reconciliation.SamplesManagementPage;
import com.oss.utils.TestListener;

import io.qameta.allure.Description;

@Listeners({TestListener.class})
public class NetworkOverlapTest extends BaseTestCase {

    private static final String CM_DOMAIN_NAME = "Selenium-NetworkOverlapTest-Domain1";
    private static final String CM_DOMAIN_NAME_2 = "Selenium-NetworkOverlapTest-Domain2";
    private static final String INTERFACE_NAME = "Comarch";
    private static final String DOMAIN = "Comarch";
    private static final String DEVICE_1_NAME = "Selenium-Overlap-Device1";
    private static final String EQUIPMENT_TYPE = "Physical Device";

    private NetworkDiscoveryControlViewPage networkDiscoveryControlViewPage;
    private SoftAssert softAssert;
    private NetworkOverlapPage networkOverlapPage;

    @BeforeClass
    public void openConsole() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        softAssert = new SoftAssert();
    }

    @Test(priority = 1, description = "Create CM Domain")
    @Description("Go to Network Discovery Control View and Create CM Domain")
    public void createCmDomain1() {
        networkDiscoveryControlViewPage = NetworkDiscoveryControlViewPage.goToNetworkDiscoveryControlViewPage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkDiscoveryControlViewPage.createCMDomain(CM_DOMAIN_NAME, INTERFACE_NAME, DOMAIN);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 2, description = "Upload reconciliation samples", dependsOnMethods = {"createCmDomain1"})
    @Description("Go to Sample Management View and upload reconciliation samples")
    public void uploadSamples1() throws URISyntaxException {
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(CM_DOMAIN_NAME);
        networkDiscoveryControlViewPage.moveToSamplesManagement();
        SamplesManagementPage samplesManagementPage = new SamplesManagementPage(driver);
        samplesManagementPage.selectPath();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        samplesManagementPage.createDirectory(CM_DOMAIN_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        samplesManagementPage.uploadSamples("recoSamples/NetworkOverlap/Selenium-Overlap-Device.json");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 3, description = "Run reconciliation and check results", dependsOnMethods = {"uploadSamples1"})
    @Description("Go to Network Discovery Control View and run reconciliation and check if it ended without errors")
    public void runReconciliationWithFullSample1() {
        networkDiscoveryControlViewPage = NetworkDiscoveryControlViewPage.goToNetworkDiscoveryControlViewPage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(CM_DOMAIN_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkDiscoveryControlViewPage.runReconciliation();
        checkPopupMessageType();
        String status = networkDiscoveryControlViewPage.waitForEndOfReco();
        networkDiscoveryControlViewPage.selectLatestReconciliationState();
        if (status.contains("SUCCESS")) {
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            Assert.assertTrue(networkDiscoveryControlViewPage.checkIssues(NetworkDiscoveryControlViewPage.IssueLevel.ERROR));
        } else {
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            Assert.assertTrue(networkDiscoveryControlViewPage.checkIssues(NetworkDiscoveryControlViewPage.IssueLevel.STARTUP_FATAL));
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            Assert.assertTrue(networkDiscoveryControlViewPage.checkIssues(NetworkDiscoveryControlViewPage.IssueLevel.FATAL));
        }
    }

    @Test(priority = 4, description = "Create CM Domain", dependsOnMethods = {"runReconciliationWithFullSample1"})
    @Description("Go to Network Discovery Control View and Create CM Domain")
    public void createCmDomain2() {
        networkDiscoveryControlViewPage = NetworkDiscoveryControlViewPage.goToNetworkDiscoveryControlViewPage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkDiscoveryControlViewPage.createCMDomain(CM_DOMAIN_NAME_2, INTERFACE_NAME, DOMAIN);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 5, description = "Upload reconciliation samples", dependsOnMethods = {"createCmDomain2"})
    @Description("Go to Sample Management View and upload reconciliation samples")
    public void uploadSamples2() throws URISyntaxException {
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(CM_DOMAIN_NAME_2);
        networkDiscoveryControlViewPage.moveToSamplesManagement();
        SamplesManagementPage samplesManagementPage = new SamplesManagementPage(driver);
        samplesManagementPage.selectPath();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        samplesManagementPage.createDirectory(CM_DOMAIN_NAME_2);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        samplesManagementPage.uploadSamples("recoSamples/NetworkOverlap/Selenium-Overlap-Device.json");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 6, description = "Run reconciliation and check results", dependsOnMethods = {"uploadSamples2"})
    @Description("Go to Network Discovery Control View and run reconciliation and check if it ended without errors")
    public void runReconciliationWithFullSample2() {
        networkDiscoveryControlViewPage = NetworkDiscoveryControlViewPage.goToNetworkDiscoveryControlViewPage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(CM_DOMAIN_NAME_2);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkDiscoveryControlViewPage.runReconciliation();
        checkPopupMessageType();
        String status = networkDiscoveryControlViewPage.waitForEndOfReco();
        networkDiscoveryControlViewPage.selectLatestReconciliationState();
        if (status.contains("SUCCESS")) {
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            Assert.assertTrue(networkDiscoveryControlViewPage.checkIssues(NetworkDiscoveryControlViewPage.IssueLevel.ERROR));
        } else {
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            Assert.assertTrue(networkDiscoveryControlViewPage.checkIssues(NetworkDiscoveryControlViewPage.IssueLevel.STARTUP_FATAL));
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            Assert.assertTrue(networkDiscoveryControlViewPage.checkIssues(NetworkDiscoveryControlViewPage.IssueLevel.FATAL));
        }
    }

    @Test(priority = 7, description = "Check conflict event", dependsOnMethods = {"runReconciliationWithFullSample2"})
    @Description("Refresh and check if event with information about conflict has appeared")
    public void checkConflictEvent() {
        Assert.assertTrue(networkDiscoveryControlViewPage.isConflictEventPresent());
    }

    @Test(priority = 8, description = "Select conflict", dependsOnMethods = {"checkConflictEvent"})
    @Description("Go to Network Overlap View Page and select conflict")
    public void selectConflict() {
        networkOverlapPage = NetworkOverlapPage.goToNetworkOverlapPage(driver, BASIC_URL);
        networkOverlapPage.searchByObjectName(DEVICE_1_NAME);
        Assert.assertEquals(networkOverlapPage.getConflictStatus(0), "OPEN");
        networkOverlapPage.selectConflict(0);
    }

    @Test(priority = 9, description = "Assert domains in conflicted objects tab", dependsOnMethods = {"selectConflict"})
    @Description("Assert if Domains are present in Conflicted Objects tab")
    public void assertDomainsInConflict() {
        if (networkOverlapPage.getDomainFromConflictedObjectsTab(0).equals(CM_DOMAIN_NAME)) {
            Assert.assertEquals(networkOverlapPage.getDomainFromConflictedObjectsTab(0), CM_DOMAIN_NAME);
            Assert.assertEquals(networkOverlapPage.getDomainFromConflictedObjectsTab(1), CM_DOMAIN_NAME_2);
        } else {
            Assert.assertEquals(networkOverlapPage.getDomainFromConflictedObjectsTab(1), CM_DOMAIN_NAME);
            Assert.assertEquals(networkOverlapPage.getDomainFromConflictedObjectsTab(0), CM_DOMAIN_NAME_2);
        }
    }

    @Test(priority = 10, description = "Resolve conflict with choosing leading domain", dependsOnMethods = {"assertDomainsInConflict"})
    @Description("Resolve conflict with choosing leading domain")
    public void resolveConflict() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkOverlapPage.clickResolve();
        ResolveConflictWizardPage resolveConflictWizardPage = new ResolveConflictWizardPage(driver);
        resolveConflictWizardPage.setChooseManually();
        resolveConflictWizardPage.setLeadingDomain(CM_DOMAIN_NAME);
        resolveConflictWizardPage.setComment("Example comment");
        resolveConflictWizardPage.clickSubmit();
    }

    @Test(priority = 11, description = "Run reconciliation and check results")
    @Description("Go to Network Discovery Control View and run reconciliation and check if it ended without errors")
    public void runReconciliationWithFullSample1Again() {
        networkDiscoveryControlViewPage = NetworkDiscoveryControlViewPage.goToNetworkDiscoveryControlViewPage(driver, BASIC_URL);
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(CM_DOMAIN_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkDiscoveryControlViewPage.runReconciliation();
        String status = networkDiscoveryControlViewPage.waitForEndOfReco();
        networkDiscoveryControlViewPage.selectLatestReconciliationState();
        if (status.contains("SUCCESS")) {
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            Assert.assertTrue(networkDiscoveryControlViewPage.checkIssues(NetworkDiscoveryControlViewPage.IssueLevel.ERROR));
        } else {
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            Assert.assertTrue(networkDiscoveryControlViewPage.checkIssues(NetworkDiscoveryControlViewPage.IssueLevel.STARTUP_FATAL));
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            Assert.assertTrue(networkDiscoveryControlViewPage.checkIssues(NetworkDiscoveryControlViewPage.IssueLevel.FATAL));
        }
    }

    @Test(priority = 12, description = "Search and open router in Inventory View", dependsOnMethods = {"runReconciliationWithFullSample1Again"})
    @Description("Set Network perspective and search for router in Global Search and check it in New Inventory View")
    public void searchInGlobalSearchAndOpenInventoryView() {
        PerspectiveChooser.create(driver, webDriverWait).setNetworkPerspective();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkDiscoveryControlViewPage.searchInGlobalSearch(DEVICE_1_NAME);
        GlobalSearchPage globalSearchPage = new GlobalSearchPage(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        globalSearchPage.filterObjectType(EQUIPMENT_TYPE);
        globalSearchPage.expandShowOnAndChooseView(DEVICE_1_NAME, ActionsContainer.SHOW_ON_GROUP_ID, "InventoryView");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        NewInventoryViewPage newInventoryViewPage = NewInventoryViewPage.getInventoryViewPage(driver, webDriverWait);
        Assert.assertFalse(newInventoryViewPage.checkIfTableIsEmpty());
    }

    @Test(priority = 13, description = "Check if conflict is resolved and check comment", dependsOnMethods = {"searchInGlobalSearchAndOpenInventoryView"})
    @Description("Check if conflict is now resolved and check comment")
    public void checkIfConflictIsResolved() {
        networkOverlapPage = NetworkOverlapPage.goToNetworkOverlapPage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkOverlapPage.searchByObjectName(DEVICE_1_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertEquals(networkOverlapPage.getConflictStatus(0), "RESOLVED");
        Assert.assertEquals(networkOverlapPage.getConflictComment(0), "Example comment");
    }

    @Test(priority = 14, description = "Delete CM Domain 1", dependsOnMethods = {"createCmDomain1"})
    @Description("Delete CM Domain1")
    public void deleteCmDomain1() {
        networkDiscoveryControlViewPage = NetworkDiscoveryControlViewPage.goToNetworkDiscoveryControlViewPage(driver, BASIC_URL);
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(CM_DOMAIN_NAME);
        networkDiscoveryControlViewPage.clearOldNotifications();
        networkDiscoveryControlViewPage.deleteCmDomain();
        checkPopupMessageType();
        Assert.assertEquals(networkDiscoveryControlViewPage.checkDeleteCmDomainNotification(), "Deleting CM Domain: " + CM_DOMAIN_NAME + " finished");
    }

    @Test(priority = 15, description = "Check if conflict is open", dependsOnMethods = {"resolveConflict", "deleteCmDomain1"})
    @Description("Check if conflict is now open after removing domain1")
    public void checkIfConflictIsOpen() {
        networkOverlapPage = NetworkOverlapPage.goToNetworkOverlapPage(driver, BASIC_URL);
        networkOverlapPage.searchByObjectName(DEVICE_1_NAME);
        Assert.assertEquals(networkOverlapPage.getConflictStatus(0), "OPEN");
    }

    @Test(priority = 16, description = "Resolve and Reopen conflict and then check if conflict is open", dependsOnMethods = {"checkIfConflictIsOpen"})
    @Description("Resolve and Reopen conflict and assert open")
    public void resolveAndReopenAndAssertOpen() {
        networkOverlapPage.selectConflict(0);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkOverlapPage.clickResolve();
        ResolveConflictWizardPage resolveConflictWizardPage = new ResolveConflictWizardPage(driver);
        resolveConflictWizardPage.clickSubmit();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkOverlapPage.selectConflict(0);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkOverlapPage.reopenConflict();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertEquals(networkOverlapPage.getConflictStatus(0), "OPEN");
    }

    @Test(priority = 17, description = "Delete CM Domain 2", dependsOnMethods = {"createCmDomain2"})
    @Description("Delete CM Domain2")
    public void deleteCmDomain2() {
        networkDiscoveryControlViewPage = NetworkDiscoveryControlViewPage.goToNetworkDiscoveryControlViewPage(driver, BASIC_URL);
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(CM_DOMAIN_NAME_2);
        networkDiscoveryControlViewPage.clearOldNotifications();
        networkDiscoveryControlViewPage.deleteCmDomain();
        checkPopupMessageType();
        Assert.assertEquals(networkDiscoveryControlViewPage.checkDeleteCmDomainNotification(), "Deleting CM Domain: " + CM_DOMAIN_NAME_2 + " finished");
    }

    @Test(priority = 18, description = "Check if conflict is terminated", dependsOnMethods = {"checkIfConflictIsOpen", "deleteCmDomain2"})
    @Description("Check if conflict is now terminated after removing both domains")
    public void checkIfConflictIsTerminated() {
        networkOverlapPage = NetworkOverlapPage.goToNetworkOverlapPage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkOverlapPage.searchByObjectName(DEVICE_1_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertEquals(networkOverlapPage.getConflictStatus(0), "TERMINATED");
    }

    private void checkPopupMessageType() {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        softAssert.assertNotNull(messages);
        softAssert.assertEquals(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getMessageType(),
                SystemMessageContainer.MessageType.INFO);
    }
}

package com.oss.reconciliation;

import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.alerts.SystemMessageInterface;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.mainheader.PerspectiveChooser;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.platform.GlobalSearchPage;
import com.oss.pages.platform.NewInventoryViewPage;
import com.oss.pages.reconciliation.CmDomainWizardPage;
import com.oss.pages.reconciliation.NetworkDiscoveryControlViewPage;
import com.oss.pages.reconciliation.NetworkOverlapPage;
import com.oss.pages.reconciliation.ResolveConflictWizardPage;
import com.oss.pages.reconciliation.SamplesManagementPage;
import com.oss.utils.TestListener;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.qameta.allure.Description;


import java.net.URISyntaxException;
import java.util.List;

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
        networkDiscoveryControlViewPage.openCmDomainWizard();
        CmDomainWizardPage wizard = new CmDomainWizardPage(driver);
        wizard.setName(CM_DOMAIN_NAME);
        wizard.setInterface(INTERFACE_NAME);
        wizard.setDomain(DOMAIN);
        wizard.save();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 2, description = "Upload reconciliation samples")
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

    @Test(priority = 3, description = "Run reconciliation and check results")
    @Description("Go to Network Discovery Control View and run reconciliation and check if it ended without errors")
    public void runReconciliationWithFullSample1() {
        networkDiscoveryControlViewPage = NetworkDiscoveryControlViewPage.goToNetworkDiscoveryControlViewPage(driver, BASIC_URL);
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(CM_DOMAIN_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkDiscoveryControlViewPage.runReconciliation();
        checkPopupMessageType();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertEquals(networkDiscoveryControlViewPage.waitForEndOfReco(), "SUCCESS");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkDiscoveryControlViewPage.selectLatestReconciliationState();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertTrue(networkDiscoveryControlViewPage.checkIssues(NetworkDiscoveryControlViewPage.IssueLevel.STARTUP_FATAL));
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertTrue(networkDiscoveryControlViewPage.checkIssues(NetworkDiscoveryControlViewPage.IssueLevel.FATAL));
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertTrue(networkDiscoveryControlViewPage.checkIssues(NetworkDiscoveryControlViewPage.IssueLevel.ERROR));
    }

    @Test(priority = 4, description = "Create CM Domain")
    @Description("Go to Network Discovery Control View and Create CM Domain")
    public void createCmDomain2() {
        networkDiscoveryControlViewPage = NetworkDiscoveryControlViewPage.goToNetworkDiscoveryControlViewPage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkDiscoveryControlViewPage.openCmDomainWizard();
        CmDomainWizardPage wizard = new CmDomainWizardPage(driver);
        wizard.setName(CM_DOMAIN_NAME_2);
        wizard.setInterface(INTERFACE_NAME);
        wizard.setDomain(DOMAIN);
        wizard.save();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 5, description = "Upload reconciliation samples")
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

    @Test(priority = 6, description = "Run reconciliation and check results")
    @Description("Go to Network Discovery Control View and run reconciliation and check if it ended without errors")
    public void runReconciliationWithFullSample2() {
        networkDiscoveryControlViewPage = NetworkDiscoveryControlViewPage.goToNetworkDiscoveryControlViewPage(driver, BASIC_URL);
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(CM_DOMAIN_NAME_2);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkDiscoveryControlViewPage.runReconciliation();
        checkPopupMessageType();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertEquals(networkDiscoveryControlViewPage.waitForEndOfReco(), "SUCCESS");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkDiscoveryControlViewPage.selectLatestReconciliationState();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertTrue(networkDiscoveryControlViewPage.checkIssues(NetworkDiscoveryControlViewPage.IssueLevel.STARTUP_FATAL));
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertTrue(networkDiscoveryControlViewPage.checkIssues(NetworkDiscoveryControlViewPage.IssueLevel.FATAL));
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertTrue(networkDiscoveryControlViewPage.checkIssues(NetworkDiscoveryControlViewPage.IssueLevel.ERROR));
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 7, description = "Check conflict event")
    @Description("Refresh and check if event with informatiob about conflict has appeared")
    public void checkConflictEvent() {
        networkDiscoveryControlViewPage.assertConflictEvent();
    }


    @Test(priority = 8, description = "Resolve conflict")
    @Description("Go to Network Overlap View Page and resolve conflict with choosing leading domain")
    public void resolveConflict() {
        networkOverlapPage = NetworkOverlapPage.goToNetworkOverlapPage(driver, BASIC_URL);
        networkOverlapPage.searchByObjectName(DEVICE_1_NAME);
        networkOverlapPage.assertConflictStatus("OPEN");
        networkOverlapPage.assertIfDomainExistsInConflictedObjects(CM_DOMAIN_NAME);
        networkOverlapPage.assertIfDomainExistsInConflictedObjects(CM_DOMAIN_NAME_2);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkOverlapPage.clickResolveButton();
        ResolveConflictWizardPage resolveConflictWizardPage = new ResolveConflictWizardPage(driver);
        resolveConflictWizardPage.fromLastReconciliation();
        resolveConflictWizardPage.chooseLeadingDomain("Selenium-NetworkOverlapTest-Domain1");
        resolveConflictWizardPage.clickSubmit();
    }

    @Test(priority = 9, description = "Run reconciliation and check results")
    @Description("Go to Network Discovery Control View and run reconciliation and check if it ended without errors")
    public void runReconciliationWithFullSample1Again() {
        networkDiscoveryControlViewPage = NetworkDiscoveryControlViewPage.goToNetworkDiscoveryControlViewPage(driver, BASIC_URL);
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(CM_DOMAIN_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkDiscoveryControlViewPage.runReconciliation();
        checkPopupMessageType();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertEquals(networkDiscoveryControlViewPage.waitForEndOfReco(), "SUCCESS");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkDiscoveryControlViewPage.selectLatestReconciliationState();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertTrue(networkDiscoveryControlViewPage.checkIssues(NetworkDiscoveryControlViewPage.IssueLevel.STARTUP_FATAL));
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertTrue(networkDiscoveryControlViewPage.checkIssues(NetworkDiscoveryControlViewPage.IssueLevel.FATAL));
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertTrue(networkDiscoveryControlViewPage.checkIssues(NetworkDiscoveryControlViewPage.IssueLevel.ERROR));
    }

    @Test(priority = 10, description = "Search and open router in Inventory View")
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

    @Test(priority = 11, description = "Check if conflict is resolved")
    @Description("Check if conflict is now resolved")
    public void checkIfConflictIsResolved() {
        networkOverlapPage = NetworkOverlapPage.goToNetworkOverlapPage(driver, BASIC_URL);
        networkOverlapPage.searchByObjectName(DEVICE_1_NAME);
        networkOverlapPage.assertConflictStatus("RESOLVED");
    }

    @Test(priority = 12, description = "Delete CM Domain 1")
    @Description("Delete CM Domain1")
    public void deleteCmDomain1() {
        networkDiscoveryControlViewPage = NetworkDiscoveryControlViewPage.goToNetworkDiscoveryControlViewPage(driver, BASIC_URL);
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(CM_DOMAIN_NAME);
        networkDiscoveryControlViewPage.clearOldNotifications();
        networkDiscoveryControlViewPage.deleteCmDomain();
        checkPopupMessageType();
        Assert.assertEquals(networkDiscoveryControlViewPage.checkDeleteCmDomainNotification(), "Deleting CM Domain: " + CM_DOMAIN_NAME + " finished");
    }

    @Test(priority = 13, description = "Delete CM Domain 2")
    @Description("Delete CM Domain2")
    public void deleteCmDomain2() {
        networkDiscoveryControlViewPage = NetworkDiscoveryControlViewPage.goToNetworkDiscoveryControlViewPage(driver, BASIC_URL);
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(CM_DOMAIN_NAME_2);
        networkDiscoveryControlViewPage.clearOldNotifications();
        networkDiscoveryControlViewPage.deleteCmDomain();
        checkPopupMessageType();
        Assert.assertEquals(networkDiscoveryControlViewPage.checkDeleteCmDomainNotification(), "Deleting CM Domain: " + CM_DOMAIN_NAME_2 + " finished");
    }

    private void checkPopupMessageType() {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        softAssert.assertNotNull(messages);
        softAssert.assertEquals(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getMessageType(),
                SystemMessageContainer.MessageType.INFO);
    }
}

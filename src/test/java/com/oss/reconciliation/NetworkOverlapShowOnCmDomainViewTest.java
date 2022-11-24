package com.oss.reconciliation;

import java.net.URISyntaxException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.alerts.SystemMessageInterface;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.platform.NewInventoryViewPage;
import com.oss.pages.reconciliation.NetworkDiscoveryControlViewPage;
import com.oss.pages.reconciliation.NetworkOverlapPage;
import com.oss.pages.reconciliation.SamplesManagementPage;

import io.qameta.allure.Description;

public class NetworkOverlapShowOnCmDomainViewTest extends BaseTestCase {
    private static final Logger log = LoggerFactory.getLogger(NetworkOverlapShowOnCmDomainViewTest.class);
    private static final String CM_DOMAIN_NAME = "Selenium-NetworkOverlap-ShowOnIV-Domain1";
    private static final String CM_DOMAIN_NAME_2 = "Selenium-NetworkOverlap-ShowOnIV-Domain2";
    private static final String INTERFACE_NAME = "Comarch";
    private static final String DOMAIN = "Comarch";
    private static final String SUCCESS_STATE = "SUCCESS";
    private static final String SAMPLES_PATH = "recoSamples/NetworkOverlap/AT-SYS-Selenium-Overlap-ShowOn-IV-Device.json";
    private static final String OPEN = "OPEN";
    private static final String DEVICE_1_NAME = "AT-SYS-Selenium-Overlap-ShowOn-IV-Device1";
    private static final String CHECKING_IF_TABLE_EMPTY_LOG = "Checking if table is empty";
    private static final String CHECKING_CMDOMAIN_PRESENT_LOG = "Checking if CMDomain is present";

    private NetworkDiscoveryControlViewPage networkDiscoveryControlViewPage;
    private SoftAssert softAssert;
    private NetworkOverlapPage networkOverlapPage;
    private NewInventoryViewPage inventoryViewPage;

    @BeforeClass
    public void openConsole() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        softAssert = new SoftAssert();
    }

    @Test(priority = 1, description = "Check if CMDomain1 is deleted")
    @Description("Check if CMDomain1 is deleted")
    public void checkIfDomain1IsDeleted() {
        networkDiscoveryControlViewPage = NetworkDiscoveryControlViewPage.goToNetworkDiscoveryControlViewPage(driver, BASIC_URL);
        networkDiscoveryControlViewPage.searchForCmDomain(CM_DOMAIN_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        if (networkDiscoveryControlViewPage.isCmDomainPresent(CM_DOMAIN_NAME)) {
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            networkDiscoveryControlViewPage.selectCmDomain(CM_DOMAIN_NAME);
            networkDiscoveryControlViewPage.clearOldNotifications();
            networkDiscoveryControlViewPage.deleteCmDomain();
            checkPopupMessageType();
            Assert.assertEquals(networkDiscoveryControlViewPage.checkDeleteCmDomainNotification(), "Deleting CM Domain: " + CM_DOMAIN_NAME + " finished");
        } else {
            log.info("CMDomain with name: " + CM_DOMAIN_NAME + " doesn't exist");
        }
    }

    @Test(priority = 2, description = "Check if CMDomain2 is deleted")
    @Description("Check if CMDomain2 is deleted")
    public void checkIfDomain2IsDeleted() {
        networkDiscoveryControlViewPage = NetworkDiscoveryControlViewPage.goToNetworkDiscoveryControlViewPage(driver, BASIC_URL);
        networkDiscoveryControlViewPage.searchForCmDomain(CM_DOMAIN_NAME_2);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        if (networkDiscoveryControlViewPage.isCmDomainPresent(CM_DOMAIN_NAME_2)) {
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            networkDiscoveryControlViewPage.selectCmDomain(CM_DOMAIN_NAME_2);
            networkDiscoveryControlViewPage.clearOldNotifications();
            networkDiscoveryControlViewPage.deleteCmDomain();
            checkPopupMessageType();
            Assert.assertEquals(networkDiscoveryControlViewPage.checkDeleteCmDomainNotification(), "Deleting CM Domain: " + CM_DOMAIN_NAME_2 + " finished");
        } else {
            log.info("CMDomain with name: " + CM_DOMAIN_NAME_2 + " doesn't exist");
        }
    }

    @Test(priority = 3, description = "Create CM Domain")
    @Description("Go to Network Discovery Control View and Create CM Domain")
    public void createCmDomain1() {
        networkDiscoveryControlViewPage = NetworkDiscoveryControlViewPage.goToNetworkDiscoveryControlViewPage(driver, BASIC_URL);
        networkDiscoveryControlViewPage.createCMDomain(CM_DOMAIN_NAME, INTERFACE_NAME, DOMAIN);
    }

    @Test(priority = 4, description = "Upload reconciliation samples", dependsOnMethods = {"createCmDomain1"})
    @Description("Go to Sample Management View and upload reconciliation samples")
    public void uploadSamples1() throws URISyntaxException {
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(CM_DOMAIN_NAME);
        networkDiscoveryControlViewPage.moveToSamplesManagement();
        SamplesManagementPage samplesManagementPage = new SamplesManagementPage(driver);
        samplesManagementPage.selectPath();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        samplesManagementPage.createDirectory(CM_DOMAIN_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        samplesManagementPage.uploadSamples(SAMPLES_PATH);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 5, description = "Run reconciliation and check results", dependsOnMethods = {"uploadSamples1"})
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
        if (status.contains(SUCCESS_STATE)) {
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            Assert.assertTrue(networkDiscoveryControlViewPage.checkIssues(NetworkDiscoveryControlViewPage.IssueLevel.ERROR));
        } else {
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            Assert.assertTrue(networkDiscoveryControlViewPage.checkIssues(NetworkDiscoveryControlViewPage.IssueLevel.STARTUP_FATAL));
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            Assert.assertTrue(networkDiscoveryControlViewPage.checkIssues(NetworkDiscoveryControlViewPage.IssueLevel.FATAL));
        }
    }

    @Test(priority = 6, description = "Create CM Domain", dependsOnMethods = {"runReconciliationWithFullSample1"})
    @Description("Go to Network Discovery Control View and Create CM Domain")
    public void createCmDomain2() {
        networkDiscoveryControlViewPage = NetworkDiscoveryControlViewPage.goToNetworkDiscoveryControlViewPage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkDiscoveryControlViewPage.createCMDomain(CM_DOMAIN_NAME_2, INTERFACE_NAME, DOMAIN);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 7, description = "Upload reconciliation samples", dependsOnMethods = {"createCmDomain2"})
    @Description("Go to Sample Management View and upload reconciliation samples")
    public void uploadSamples2() throws URISyntaxException {
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(CM_DOMAIN_NAME_2);
        networkDiscoveryControlViewPage.moveToSamplesManagement();
        SamplesManagementPage samplesManagementPage = new SamplesManagementPage(driver);
        samplesManagementPage.selectPath();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        samplesManagementPage.createDirectory(CM_DOMAIN_NAME_2);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        samplesManagementPage.uploadSamples(SAMPLES_PATH);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 8, description = "Run reconciliation and check results", dependsOnMethods = {"uploadSamples2"})
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
        if (status.contains(SUCCESS_STATE)) {
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            Assert.assertTrue(networkDiscoveryControlViewPage.checkIssues(NetworkDiscoveryControlViewPage.IssueLevel.ERROR));
        } else {
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            Assert.assertTrue(networkDiscoveryControlViewPage.checkIssues(NetworkDiscoveryControlViewPage.IssueLevel.STARTUP_FATAL));
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            Assert.assertTrue(networkDiscoveryControlViewPage.checkIssues(NetworkDiscoveryControlViewPage.IssueLevel.FATAL));
        }
    }

    @Test(priority = 9, description = "Check conflict event", dependsOnMethods = {"runReconciliationWithFullSample2"})
    @Description("Refresh and check if event with information about conflict has appeared")
    public void checkConflictEvent() {
        Assert.assertTrue(networkDiscoveryControlViewPage.isConflictEventPresent());
    }

    @Test(priority = 10, description = "Select conflict", dependsOnMethods = {"runReconciliationWithFullSample2"})
    @Description("Go to Network Overlap View Page and select conflict")
    public void moveToCmDomainView() {
        networkOverlapPage = NetworkOverlapPage.goToNetworkOverlapPage(driver, BASIC_URL);
        networkOverlapPage.searchByObjectName(DEVICE_1_NAME);
        Assert.assertEquals(networkOverlapPage.getConflictStatus(0), OPEN);
        networkOverlapPage.selectConflict(0);
        networkOverlapPage.goToCmDomainView();
    }

    @Test(priority = 11, description = "Check if CMDomain exists", dependsOnMethods = {"moveToCmDomainView"})
    @Description("Check if CMDomain exists on NDCV")
    public void checkIfCMDomainExists() {
        networkDiscoveryControlViewPage = new NetworkDiscoveryControlViewPage(driver);
        Assert.assertTrue(networkDiscoveryControlViewPage.isCmDomainPresent(CM_DOMAIN_NAME_2), CHECKING_CMDOMAIN_PRESENT_LOG);
    }

    private void checkPopupMessageType() {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        softAssert.assertNotNull(messages);
        softAssert.assertEquals(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getMessageType(),
                SystemMessageContainer.MessageType.INFO);
    }
}
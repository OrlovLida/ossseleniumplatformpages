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
import com.oss.framework.components.mainheader.PerspectiveChooser;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.platform.NewInventoryViewPage;
import com.oss.pages.reconciliation.NetworkDiscoveryControlViewPage;
import com.oss.pages.reconciliation.NetworkOverlapPage;
import com.oss.pages.reconciliation.SamplesManagementPage;

import io.qameta.allure.Description;

public class NetworkOverlapShowOnInventoryViewTest extends BaseTestCase {

    private static final Logger log = LoggerFactory.getLogger(NetworkOverlapShowOnInventoryViewTest.class);
    private static final String CM_DOMAIN_NAME = "Selenium-NetworkOverlap-ShowOnIV-Domain1";
    private static final String CM_DOMAIN_NAME_2 = "Selenium-NetworkOverlap-ShowOnIV-Domain2";
    private static final String INTERFACE_NAME = "Comarch";
    private static final String DOMAIN = "Comarch";
    private static final String SUCCESS_STATE = "SUCCESS";
    private static final String SAMPLES_PATH = "recoSamples/NetworkOverlap/AT-SYS-Selenium-Overlap-ShowOn-IV-Device.json";
    private static final String OPEN = "OPEN";
    private static final String DEVICE_1_NAME = "AT-SYS-Selenium-Overlap-ShowOn-IV-Device1";
    private static final String CHECKING_IF_TABLE_NOT_EMPTY_LOG = "Checking if table is not empty";
    private static final String DELETING_DOMAIN_PATTERN_LOG = "Deleting CM Domain: %s finished";
    private static final String CM_DOMAIN_DOES_NOT_EXIST_PATTERN_LOG = "CMDomain with name: %s doesn't exist";
    private static final String CHECK_IF_CONFLICT_PRESENT_LOG = "Checking if conflict is present";
    private static final String CHECKING_ISSUES_ERROR_LOG = "Checking issues of level type ERROR";
    private static final String CHECKING_ISSUES_STARTUP_FATAL_LOG = "Checking issues of level type STARTUP_FATAL";
    private static final String CHECKING_ISSUES_FATAL_LOG = "Checking issues of level type FATAL";
    private static final String CHECKING_CONFLICT_OPEN_LOG = "Checking if conflict is open";

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
        checkIfDomainIsDeleted(CM_DOMAIN_NAME);
    }

    @Test(priority = 2, description = "Check if CMDomain2 is deleted", dependsOnMethods = {"checkIfDomain1IsDeleted"})
    @Description("Check if CMDomain2 is deleted")
    public void checkIfDomain2IsDeleted() {
        checkIfDomainIsDeleted(CM_DOMAIN_NAME_2);
    }

    @Test(priority = 3, description = "Create CM Domain", dependsOnMethods = {"checkIfDomain2IsDeleted"})
    @Description("Go to Network Discovery Control View and Create CM Domain")
    public void createCmDomain1() {
        createCMDomain(CM_DOMAIN_NAME);
    }

    @Test(priority = 4, description = "Upload reconciliation samples", dependsOnMethods = {"createCmDomain1"})
    @Description("Go to Sample Management View and upload reconciliation samples")
    public void uploadSamples1() throws URISyntaxException {
        uploadSamples(CM_DOMAIN_NAME);
    }

    @Test(priority = 5, description = "Run reconciliation and check results", dependsOnMethods = {"uploadSamples1"})
    @Description("Go to Network Discovery Control View and run reconciliation and check if it ended without errors")
    public void runReconciliationWithFullSample1() {
        runReconciliationWithFullSamples(CM_DOMAIN_NAME);
    }

    @Test(priority = 6, description = "Create CM Domain", dependsOnMethods = {"runReconciliationWithFullSample1"})
    @Description("Go to Network Discovery Control View and Create CM Domain")
    public void createCmDomain2() {
        createCMDomain(CM_DOMAIN_NAME_2);
    }

    @Test(priority = 7, description = "Upload reconciliation samples", dependsOnMethods = {"createCmDomain2"})
    @Description("Go to Sample Management View and upload reconciliation samples")
    public void uploadSamples2() throws URISyntaxException {
        uploadSamples(CM_DOMAIN_NAME_2);
    }

    @Test(priority = 8, description = "Run reconciliation and check results", dependsOnMethods = {"uploadSamples2"})
    @Description("Go to Network Discovery Control View and run reconciliation and check if it ended without errors")
    public void runReconciliationWithFullSample2() {
        runReconciliationWithFullSamples(CM_DOMAIN_NAME_2);
    }

    @Test(priority = 9, description = "Check conflict event", dependsOnMethods = {"runReconciliationWithFullSample2"})
    @Description("Refresh and check if event with information about conflict has appeared")
    public void checkConflictEvent() {
        Assert.assertTrue(networkDiscoveryControlViewPage.isConflictEventPresent(), CHECK_IF_CONFLICT_PRESENT_LOG);
    }

    @Test(priority = 10, description = "Select conflict", dependsOnMethods = {"checkConflictEvent"})
    @Description("Go to Network Overlap View Page and select conflict")
    public void moveToInventoryView() {
        networkOverlapPage = NetworkOverlapPage.goToNetworkOverlapPage(driver, BASIC_URL);
        PerspectiveChooser.create(driver, webDriverWait).setNetworkPerspective();
        networkOverlapPage.searchByObjectName(DEVICE_1_NAME);
        Assert.assertEquals(networkOverlapPage.getConflictStatus(0), OPEN, CHECKING_CONFLICT_OPEN_LOG);
        networkOverlapPage.selectConflict(0);
        networkOverlapPage.goToInventoryView();
    }

    @Test(priority = 11, description = "Check if object exists", dependsOnMethods = {"moveToInventoryView"})
    @Description("Check if object exists on Inventory View")
    public void checkIfObjectExists() {
        inventoryViewPage = NewInventoryViewPage.getInventoryViewPage(driver, webDriverWait);
        Assert.assertFalse(inventoryViewPage.checkIfTableIsEmpty(), CHECKING_IF_TABLE_NOT_EMPTY_LOG);
    }

    private void uploadSamples(String cmDomainName) throws URISyntaxException {
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(cmDomainName);
        networkDiscoveryControlViewPage.moveToSamplesManagement();
        SamplesManagementPage samplesManagementPage = new SamplesManagementPage(driver);
        samplesManagementPage.selectPath();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        samplesManagementPage.createDirectory(cmDomainName);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        samplesManagementPage.uploadSamples(SAMPLES_PATH);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    private void checkIfDomainIsDeleted(String cmDomainName) {
        networkDiscoveryControlViewPage = NetworkDiscoveryControlViewPage.goToNetworkDiscoveryControlViewPage(driver, BASIC_URL);
        networkDiscoveryControlViewPage.searchForCmDomain(cmDomainName);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        if (networkDiscoveryControlViewPage.isCmDomainPresent(cmDomainName)) {
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            networkDiscoveryControlViewPage.selectCmDomain(cmDomainName);
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            networkDiscoveryControlViewPage.clearOldNotifications();
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            networkDiscoveryControlViewPage.deleteCmDomain();
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            checkPopupMessageType();
            Assert.assertEquals(networkDiscoveryControlViewPage.checkDeleteCmDomainNotification(), String.format(DELETING_DOMAIN_PATTERN_LOG, cmDomainName));
        } else {
            log.info(String.format(CM_DOMAIN_DOES_NOT_EXIST_PATTERN_LOG, cmDomainName));
        }
    }

    private void createCMDomain(String cmDomainName) {
        networkDiscoveryControlViewPage = NetworkDiscoveryControlViewPage.goToNetworkDiscoveryControlViewPage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkDiscoveryControlViewPage.createCMDomain(cmDomainName, INTERFACE_NAME, DOMAIN);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    private void runReconciliationWithFullSamples(String cmDomainName) {
        networkDiscoveryControlViewPage = NetworkDiscoveryControlViewPage.goToNetworkDiscoveryControlViewPage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(cmDomainName);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkDiscoveryControlViewPage.runReconciliation();
        checkPopupMessageType();
        String status = networkDiscoveryControlViewPage.waitForEndOfReco();
        networkDiscoveryControlViewPage.selectLatestReconciliationState();
        if (status.contains(SUCCESS_STATE)) {
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            Assert.assertTrue(networkDiscoveryControlViewPage.checkIssues(NetworkDiscoveryControlViewPage.IssueLevel.ERROR), CHECKING_ISSUES_ERROR_LOG);
        } else {
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            Assert.assertTrue(networkDiscoveryControlViewPage.checkIssues(NetworkDiscoveryControlViewPage.IssueLevel.STARTUP_FATAL), CHECKING_ISSUES_STARTUP_FATAL_LOG);
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            Assert.assertTrue(networkDiscoveryControlViewPage.checkIssues(NetworkDiscoveryControlViewPage.IssueLevel.FATAL), CHECKING_ISSUES_FATAL_LOG);
        }
    }

    private void checkPopupMessageType() {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        softAssert.assertNotNull(messages);
        softAssert.assertEquals(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getMessageType(),
                SystemMessageContainer.MessageType.INFO);
        softAssert.assertAll();
    }
}

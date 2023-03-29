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
import com.oss.framework.components.mainheader.Notifications;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.reconciliation.NetworkDiscoveryControlViewPage;
import com.oss.pages.reconciliation.NetworkOverlapPage;
import com.oss.pages.reconciliation.SamplesManagementPage;

import io.qameta.allure.Description;

public class NetworkOverlapShowOnCmDomainViewTest extends BaseTestCase {
    private static final Logger log = LoggerFactory.getLogger(NetworkOverlapShowOnCmDomainViewTest.class);
    private static final String CM_DOMAIN_NAME = "Selenium-NetworkOverlap-ShowOn-CmDomainView-Domain1";
    private static final String CM_DOMAIN_NAME_2 = "Selenium-NetworkOverlap-ShowOn-CmDomainView-Domain2";
    private static final String INTERFACE_NAME = "Comarch";
    private static final String DOMAIN = "Comarch";
    private static final String SUCCESS_STATE = "SUCCESS";
    private static final String SAMPLES_PATH = "recoSamples/NetworkOverlap/AT-SYS-Selenium-Overlap-ShowOn-CmDomainView-Device.json";
    private static final String OPEN = "OPEN";
    private static final String DEVICE_1_NAME = "AT-SYS-Selenium-Overlap-ShowOn-CmDomainView-Device1";
    private static final String CHECKING_CMDOMAIN_PRESENT_LOG = "Checking if CMDomain is present";
    private static final String DELETING_DOMAIN_PATTERN_LOG = "Deleting CM Domain: %s finished";
    private static final String CM_DOMAIN_DOES_NOT_EXIST_PATTERN_LOG = "CMDomain with name: %s doesn't exist";
    private static final String CHECK_IF_CONFLICT_PRESENT_LOG = "Checking if conflict is present";
    private static final String CHECKING_CONFLICT_OPEN_LOG = "Checking if conflict is open";
    private static final String CHECKING_ISSUES_ERROR_LOG = "Checking issues of level type ERROR";
    private static final String CHECKING_ISSUES_STARTUP_FATAL_LOG = "Checking issues of level type STARTUP_FATAL";
    private static final String CHECKING_ISSUES_FATAL_LOG = "Checking issues of level type FATAL";

    private NetworkDiscoveryControlViewPage networkDiscoveryControlViewPage;
    private SoftAssert softAssert;
    private NetworkOverlapPage networkOverlapPage;

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

    @Test(priority = 2, description = "Check if CMDomain2 is deleted")
    @Description("Check if CMDomain2 is deleted")
    public void checkIfDomain2IsDeleted() {
        checkIfDomainIsDeleted(CM_DOMAIN_NAME_2);
    }

    @Test(priority = 3, description = "Create CM Domain")
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

    @Test(priority = 10, description = "Select conflict", dependsOnMethods = {"runReconciliationWithFullSample2"})
    @Description("Go to Network Overlap View Page and select conflict")
    public void moveToCmDomainView() {
        networkOverlapPage = NetworkOverlapPage.goToNetworkOverlapPage(driver, BASIC_URL);
        networkOverlapPage.searchByObjectName(DEVICE_1_NAME);
        Assert.assertEquals(networkOverlapPage.getConflictStatus(0), OPEN, CHECKING_CONFLICT_OPEN_LOG);
        networkOverlapPage.selectConflict(0);
        networkOverlapPage.goToCmDomainViewByDomainName(CM_DOMAIN_NAME);
    }

    @Test(priority = 11, description = "Check if CMDomain exists", dependsOnMethods = {"moveToCmDomainView"})
    @Description("Check if CMDomain exists on NDCV")
    public void checkIfCMDomainExists() {
        networkDiscoveryControlViewPage = new NetworkDiscoveryControlViewPage(driver);
        Assert.assertTrue(networkDiscoveryControlViewPage.isCmDomainPresent(CM_DOMAIN_NAME), CHECKING_CMDOMAIN_PRESENT_LOG);
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
            Notifications.create(driver, webDriverWait).clearAllNotification();
            networkDiscoveryControlViewPage.deleteCmDomain();
            checkPopupMessageType();
            Assert.assertEquals(Notifications.create(driver, webDriverWait).getNotificationMessage(), String.format(DELETING_DOMAIN_PATTERN_LOG, cmDomainName));
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
package com.oss.reconciliation;

import java.net.URISyntaxException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.google.common.collect.ImmutableList;
import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.alerts.SystemMessageInterface;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.mainheader.PerspectiveChooser;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.physical.DeviceWizardPage;
import com.oss.pages.platform.GlobalSearchPage;
import com.oss.pages.reconciliation.CreateMatchingWizardPage;
import com.oss.pages.reconciliation.ManualMatchingPage;
import com.oss.pages.reconciliation.NetworkDiscoveryControlViewPage;
import com.oss.pages.reconciliation.NetworkInconsistenciesViewPage;
import com.oss.pages.reconciliation.SamplesManagementPage;
import com.oss.reconciliation.infrastructure.recoConfig.RecoConfigClient;

import io.qameta.allure.Description;

public class ManualMatchingTest extends BaseTestCase {

    private static final Logger log = LoggerFactory.getLogger(ManualMatchingTest.class);
    private static final String CM_DOMAIN_NAME = "Selenium-ManualMatchingTest";
    private static final String RECO_CONFIG_BODY = "{\"supportInventoryObjectMatching\":\"true\",\"adaptation-comarch-to-r1\":{\"createBlackboxModels\":\"false\",\"assignGenericModels\":\"false\"}}";
    private static final String INTERFACE_NAME = "Comarch";
    private static final String DOMAIN = "Comarch";
    private static final String EQUIPMENT_TYPE = "Physical Device";
    private static final String DEVICE_NAME_1 = "SELENIUM_DEVICE_FOR_MATCHING_1";
    private static final String DEVICE_NAME_3 = "SELENIUM_DEVICE_FOR_MATCHING_3";
    private static final String DEVICE_TO_MATCH_NAME = "AT-SYS-DEVICE_TO_MATCH";
    private static final String CREATION_OPERATION_TYPE = "CREATION";
    private static final String DELETE_IN_GLOBALSEARCH_ID = "DeleteDeviceWizardAction";
    private static final String CISCO_MODEL_NAME = "Cisco Systems Inc. WS-C6509";
    private static final String SAMPLES_PATH = "recoSamples/manualMatching/ROUTER_MATCHING_AG.json";

    private static final List<String> columnsHeadersAssertionList = new ImmutableList.Builder<String>()
            .add("Network Type")
            .add("Network Name")
            .add("Inventory Type")
            .add("Inventory Name")
            .add("Inventory ID")
            .add("User")
            .build();

    private ManualMatchingPage manualMatchingPage;
    private SoftAssert softAssert;
    private RecoConfigClient recoConfigClient;
    private NetworkDiscoveryControlViewPage networkDiscoveryControlViewPage;
    private DeviceWizardPage deviceWizardPage;
    private CreateMatchingWizardPage createMatchingWizardPage;
    private NetworkInconsistenciesViewPage networkInconsistenciesViewPage;

    @BeforeClass
    public void openConsole() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        softAssert = new SoftAssert();
        recoConfigClient = RecoConfigClient.getInstance(environmentRequestClient);
        recoConfigClient.createRecoConfig(RECO_CONFIG_BODY, CM_DOMAIN_NAME);
    }

    @Test(priority = 1, description = "Delete CMDomain is it exists")
    @Description("Delete CMDomain is it exists")
    public void deleteDomainIfExists() {
        networkDiscoveryControlViewPage = NetworkDiscoveryControlViewPage.goToNetworkDiscoveryControlViewPage(driver, BASIC_URL);
        networkDiscoveryControlViewPage.searchForCmDomain(CM_DOMAIN_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        if (networkDiscoveryControlViewPage.checkIfCmDomainExists(CM_DOMAIN_NAME)) {
            networkDiscoveryControlViewPage.selectCmDomain(CM_DOMAIN_NAME);
            networkDiscoveryControlViewPage.clearOldNotifications();
            networkDiscoveryControlViewPage.deleteCmDomain();
            checkPopupMessageType();
            Assert.assertEquals(networkDiscoveryControlViewPage.checkDeleteCmDomainNotification(), "Deleting CM Domain: " + CM_DOMAIN_NAME + " finished");
        } else {
            log.info("CMDomain with name: " + CM_DOMAIN_NAME + " doesn't exist");
        }
    }

    @Test(priority = 2, description = "Create CM Domain")
    @Description("Go to Network Discovery Control View and Create CM Domain")
    public void createCmDomain() {
        networkDiscoveryControlViewPage = NetworkDiscoveryControlViewPage.goToNetworkDiscoveryControlViewPage(driver, BASIC_URL);
        networkDiscoveryControlViewPage.createCMDomain(CM_DOMAIN_NAME, INTERFACE_NAME, DOMAIN);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 3, description = "Upload reconciliation samples", dependsOnMethods = {"createCmDomain"})
    @Description("Go to Sample Management View and upload reconciliation samples")
    public void uploadSamples() throws URISyntaxException {
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

    @Test(priority = 4, description = "Run reconciliation and check results", dependsOnMethods = {"uploadSamples"})
    @Description("Go to Network Discovery Control View and run reconciliation and check if it ended without errors")
    public void runReconciliationWithFullSample() {
        networkDiscoveryControlViewPage = NetworkDiscoveryControlViewPage.goToNetworkDiscoveryControlViewPage(driver, BASIC_URL);
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(CM_DOMAIN_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkDiscoveryControlViewPage.runReconciliation();
        checkPopupMessageType();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
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

    @Test(priority = 5, description = "Create Physical Device", dependsOnMethods = {"runReconciliationWithFullSample"})
    @Description("Create PhysicalDevice on PhysicalInventoryPage")
    public void createDevice() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage = DeviceWizardPage.goToDeviceWizardPageLive(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.setModel(CISCO_MODEL_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.setName(DEVICE_TO_MATCH_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.next();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.setFirstAvailableLocation();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.accept();
    }

    @Test(priority = 6, description = "Assert Columns Headers")
    @Description("Assert columns headers on Manual Matching Page")
    public void assertColumnsHeaders() {
        manualMatchingPage = ManualMatchingPage.goToManualMatchingPage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        List<String> attributes = manualMatchingPage.getColumnHeaders();
        Assert.assertEquals(attributes.size(), columnsHeadersAssertionList.size());
        for (int i = 0; i < attributes.size(); i++) {
            log.info("Checking attribute with index: " + i + ", which equals: '" + columnsHeadersAssertionList.get(i) + "' on declared assertionList, and equals '" + attributes.get(i) + "' on properties list taken from GUI");
            softAssert.assertEquals(attributes.get(i), columnsHeadersAssertionList.get(i));
        }
        softAssert.assertAll();
    }

    @Test(priority = 7, description = "Create empty matching")
    @Description("Create Matching that is not connected with any Inventory Object")
    public void createEmptyMatching() {
        manualMatchingPage = ManualMatchingPage.goToManualMatchingPage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        manualMatchingPage.clickCreate();
        createMatchingWizardPage = new CreateMatchingWizardPage(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        createMatchingWizardPage.selectDeviceTO(DEVICE_NAME_3);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        createMatchingWizardPage.createNewIO();
        createMatchingWizardPage.clickAccept();
    }

    @Test(priority = 8, description = "Create matching")
    @Description("Create matching that is connected with Inventory Object")
    public void createMatching() {
        manualMatchingPage = ManualMatchingPage.goToManualMatchingPage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        manualMatchingPage.clickCreate();
        createMatchingWizardPage = new CreateMatchingWizardPage(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        createMatchingWizardPage.selectDeviceTO(DEVICE_NAME_1);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        createMatchingWizardPage.clickNext();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        createMatchingWizardPage.searchByName(DEVICE_TO_MATCH_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        createMatchingWizardPage.selectFirstIO();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        createMatchingWizardPage.clickAccept();
    }

    @Test(priority = 9, description = "Delete and create matching")
    @Description("Delete and create matching again to check if deletion works correctly")
    public void deleteAndCreateMatching() {
        manualMatchingPage = ManualMatchingPage.goToManualMatchingPage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        manualMatchingPage.searchByNetworkName(DEVICE_NAME_3);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        manualMatchingPage.selectRow(0);
        manualMatchingPage.deleteMatching();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        manualMatchingPage.clickCreate();
        createMatchingWizardPage = new CreateMatchingWizardPage(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        createMatchingWizardPage.selectDeviceTO(DEVICE_NAME_3);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        createMatchingWizardPage.createNewIO();
        createMatchingWizardPage.clickAccept();
    }

    @Test(priority = 10, description = "Assert created matchings")
    @Description("Assert if created matchings have proper names")
    public void assertCreatedMatchings() {
        manualMatchingPage = ManualMatchingPage.goToManualMatchingPage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        manualMatchingPage.searchByNetworkName(DEVICE_NAME_1);
        Assert.assertEquals(manualMatchingPage.getCellValue(0, "Network Name"), DEVICE_NAME_1);
        Assert.assertEquals(manualMatchingPage.getCellValue(0, "Inventory Name"), DEVICE_TO_MATCH_NAME);
        manualMatchingPage = ManualMatchingPage.goToManualMatchingPage(driver, BASIC_URL);
        manualMatchingPage.searchByNetworkName(DEVICE_NAME_3);
        Assert.assertEquals(manualMatchingPage.getCellValue(0, "Network Name"), DEVICE_NAME_3);
    }

    @Test(priority = 11, description = "Run reconciliation again")
    @Description("Run reconciliation again to create Inventory Objects after matching them")
    public void runReconciliationAgain() {
        networkDiscoveryControlViewPage = NetworkDiscoveryControlViewPage.goToNetworkDiscoveryControlViewPage(driver, BASIC_URL);
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(CM_DOMAIN_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkDiscoveryControlViewPage.runReconciliation();
        checkPopupMessageType();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
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

    @Test(priority = 12, description = "Assert consistencies", dependsOnMethods = {"runReconciliationAgain"})
    @Description("Assert if consistencies are correct")
    public void assertInconsistencies() {
        networkDiscoveryControlViewPage.moveToNivFromNdcv();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkInconsistenciesViewPage = new NetworkInconsistenciesViewPage(driver);
        networkInconsistenciesViewPage.expandTreeRowContains("IPDevice");
        networkInconsistenciesViewPage.expandTreeRowContains(DEVICE_NAME_1);
        networkInconsistenciesViewPage.expandTreeRowContains(DEVICE_NAME_3);
        Assert.assertTrue(networkInconsistenciesViewPage.isInconsistencyPresent(DEVICE_NAME_1));
        Assert.assertTrue(networkInconsistenciesViewPage.isInconsistencyPresent(DEVICE_NAME_3));
        networkInconsistenciesViewPage.selectTreeObjectByName(DEVICE_NAME_1);
        Assert.assertEquals(networkInconsistenciesViewPage.getLiveName(), DEVICE_TO_MATCH_NAME);
        Assert.assertEquals(networkInconsistenciesViewPage.getNetworkName(), DEVICE_NAME_1);
        Assert.assertEquals(networkInconsistenciesViewPage.checkInconsistenciesOperationType(), CREATION_OPERATION_TYPE);
        networkInconsistenciesViewPage.selectTreeObjectByName(DEVICE_NAME_3);
        Assert.assertEquals(networkInconsistenciesViewPage.getLiveName(), "");
        Assert.assertEquals(networkInconsistenciesViewPage.getNetworkName(), DEVICE_NAME_3);
        Assert.assertEquals(networkInconsistenciesViewPage.checkInconsistenciesOperationType(), CREATION_OPERATION_TYPE);
    }

    @Test(priority = 13, description = "Search for Device and delete it", dependsOnMethods = {"runReconciliationAgain"})
    @Description("Search for device in Global Search and Delete it")
    public void searchInGlobalSearchAndDeleteObject() {
        networkDiscoveryControlViewPage = NetworkDiscoveryControlViewPage.goToNetworkDiscoveryControlViewPage(driver, BASIC_URL);
        PerspectiveChooser.create(driver, webDriverWait).setLivePerspective();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkDiscoveryControlViewPage.searchInGlobalSearch(DEVICE_TO_MATCH_NAME);
        GlobalSearchPage globalSearchPage = new GlobalSearchPage(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        globalSearchPage.filterObjectType(EQUIPMENT_TYPE);
        globalSearchPage.expandShowOnAndChooseView(DEVICE_TO_MATCH_NAME, ActionsContainer.EDIT_GROUP_ID, DELETE_IN_GLOBALSEARCH_ID);
        globalSearchPage.confirmDeletion();
    }
//
//    @Test(priority = 14, description = "Delete both matchings")
//    @Description("Delete both created matchings")
//    public void deleteMatchings() {
//        manualMatchingPage = ManualMatchingPage.goToManualMatchingPage(driver, BASIC_URL);
//        DelayUtils.waitForPageToLoad(driver, webDriverWait);
//        manualMatchingPage.searchByNetworkName(DEVICE_NAME_3);
//        DelayUtils.waitForPageToLoad(driver, webDriverWait);
//        if (!manualMatchingPage.isTableEmpty()){
//            manualMatchingPage.selectRow(0);
//            DelayUtils.waitForPageToLoad(driver, webDriverWait);
//            manualMatchingPage.deleteMatching();
//        }
//        manualMatchingPage = ManualMatchingPage.goToManualMatchingPage(driver, BASIC_URL);
//        DelayUtils.waitForPageToLoad(driver, webDriverWait);
//        manualMatchingPage.searchByNetworkName(DEVICE_NAME_1);
//        DelayUtils.waitForPageToLoad(driver, webDriverWait);
//        DelayUtils.waitForPageToLoad(driver, webDriverWait);
//        if (!manualMatchingPage.isTableEmpty()) {
//            manualMatchingPage.selectRow(0);
//            DelayUtils.waitForPageToLoad(driver, webDriverWait);
//            manualMatchingPage.deleteMatching();
//        }
//    }

    private void checkPopupMessageType() {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        softAssert.assertNotNull(messages);
        softAssert.assertEquals(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getMessageType(),
                SystemMessageContainer.MessageType.INFO);
    }
}

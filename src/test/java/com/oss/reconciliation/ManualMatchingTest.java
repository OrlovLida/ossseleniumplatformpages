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
import com.oss.framework.components.inputs.Button;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.physical.DeviceWizardPage;
import com.oss.pages.reconciliation.CreateMatchingWizardPage;
import com.oss.pages.reconciliation.ManualMatchingPage;
import com.oss.pages.reconciliation.NetworkDiscoveryControlViewPage;
import com.oss.pages.reconciliation.SamplesManagementPage;
import com.oss.reconciliation.infrastructure.recoConfig.RecoConfigClient;
import com.oss.softwaremanagement.infrastructure.repository.SoftwareRepositoryClient;

import io.qameta.allure.Description;

public class ManualMatchingTest extends BaseTestCase {

    private static final Logger log = LoggerFactory.getLogger(ManualMatchingTest.class);
    private static final String CM_DOMAIN_NAME = "Selenium-ManualMatchingTest";
    private static final String RECO_CONFIG_BODY = "{\"supportInventoryObjectMatching\":\"true\",\"adaptation-comarch-to-r1\":{\"createBlackboxModels\":\"false\",\"assignGenericModels\":\"false\"}}";
    private static final String CREATE_BUTTON_ID = "createNewInventoryObjectCheckboxId";
    private static final String INTERFACE_NAME = "Comarch";
    private static final String DOMAIN = "Comarch";

    private ManualMatchingPage manualMatchingPage;
    private SoftAssert softAssert;
    private RecoConfigClient recoConfigClient;
    private NetworkDiscoveryControlViewPage networkDiscoveryControlViewPage;
    private DeviceWizardPage deviceWizardPage;
    private CreateMatchingWizardPage createMatchingWizardPage;

    private static final List<String> columnsHeadersAssertionList = new ImmutableList.Builder<String>()
            .add("Network Type")
            .add("Network Name")
            .add("Inventory Type")
            .add("Inventory Name")
            .add("Inventory ID")
            .add("User")
            .build();

    @BeforeClass
    public void openConsole() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        softAssert = new SoftAssert();
        recoConfigClient = RecoConfigClient.getInstance(environmentRequestClient);
        recoConfigClient.createRecoConfig(RECO_CONFIG_BODY, CM_DOMAIN_NAME);

    }

//    @Test(priority = 1, description = "Check if CMDomain1 is deleted")
//    @Description("Check if CMDomain1 is deleted")
//    public void checkIfDomain1IsDeleted() {
//        networkDiscoveryControlViewPage = NetworkDiscoveryControlViewPage.goToNetworkDiscoveryControlViewPage(driver, BASIC_URL);
//        networkDiscoveryControlViewPage.searchForCmDomain(CM_DOMAIN_NAME);
//        if (networkDiscoveryControlViewPage.checkIfCmDomainExists(CM_DOMAIN_NAME)) {
//            networkDiscoveryControlViewPage.selectCmDomain(CM_DOMAIN_NAME);
//            networkDiscoveryControlViewPage.clearOldNotifications();
//            networkDiscoveryControlViewPage.deleteCmDomain();
//            checkPopupMessageType();
//            Assert.assertEquals(networkDiscoveryControlViewPage.checkDeleteCmDomainNotification(), "Deleting CM Domain: " + CM_DOMAIN_NAME + " finished");
//        }
//        else {
//            log.info("CMDomain with name: " + CM_DOMAIN_NAME + " doesn't exist");
//        }
//    }


    @Test(priority = 2)
    public void createCmDomain() {
        networkDiscoveryControlViewPage = NetworkDiscoveryControlViewPage.goToNetworkDiscoveryControlViewPage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkDiscoveryControlViewPage.createCMDomain(CM_DOMAIN_NAME, INTERFACE_NAME, DOMAIN);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 3)
    public void uploadSamples() throws URISyntaxException {
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(CM_DOMAIN_NAME);
        networkDiscoveryControlViewPage.moveToSamplesManagement();
        SamplesManagementPage samplesManagementPage = new SamplesManagementPage(driver);
        samplesManagementPage.selectPath();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        samplesManagementPage.createDirectory(CM_DOMAIN_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        samplesManagementPage.uploadSamples("recoSamples/manualMatching/ROUTER_MATCHING_AG.json");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 4)
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

    @Test(priority = 5)
    public void createDevice() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage = DeviceWizardPage.goToDeviceWizardPageLive(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.setModel("Cisco Systems Inc. WS-C6509");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.setName("AT-SYS-DEVICE_TO_MATCH");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.next();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.setLocation("x");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.accept();
    }

    @Test(priority = 6)
    public void assertColumnsHeaders() {
        manualMatchingPage = ManualMatchingPage.goToManualMatchingPage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        List<String> attributes = manualMatchingPage.getColumnHeaders();
        Assert.assertEquals(attributes.size(), columnsHeadersAssertionList.size());
        for (int i = 0; i< attributes.size(); i++) {
            log.info("Checking attribute with index: " + i + ", which equals: '" + columnsHeadersAssertionList.get(i) + "' on declared assertionList, and equals '" + attributes.get(i) + "' on properties list taken from GUI");
            softAssert.assertEquals(attributes.get(i), columnsHeadersAssertionList.get(i));
        }
        softAssert.assertAll();
    }



//    @Test(priority = 2)
//    public void selectFirstRow() {
//        DelayUtils.waitForPageToLoad(driver, webDriverWait);
//        manualMatchingPage.searchByNetworkName("poznan_b");
//        DelayUtils.waitForPageToLoad(driver, webDriverWait);
//        manualMatchingPage.selectFirstRow();
//    }

    @Test(priority = 7)
    public void createEmptyMatching() {
        manualMatchingPage = ManualMatchingPage.goToManualMatchingPage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        manualMatchingPage.clickCreate();
        createMatchingWizardPage = new CreateMatchingWizardPage(driver);
        createMatchingWizardPage.selectTO("Device@@DEVICE_FOR_MATCHING_3");
//        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        createMatchingWizardPage.createNewIO();
//        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        createMatchingWizardPage.clickAccept();
    }

    @Test(priority = 8)
    public void createMatching() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        manualMatchingPage.clickCreate();
        createMatchingWizardPage = new CreateMatchingWizardPage(driver);
        createMatchingWizardPage.selectTO("Device@@DEVICE_FOR_MATCHING_1");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        createMatchingWizardPage.clickNext();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        createMatchingWizardPage.searchByName("AT-SYS-DEVICE_TO_MATCH");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        createMatchingWizardPage.selectFirstIO();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        createMatchingWizardPage.clickAccept();
    }

    @Test(priority = 9)
    public void assertCreatedMatchings() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        manualMatchingPage.searchByNetworkName("DEVICE_FOR_MATCHING_1");
        Assert.assertEquals(manualMatchingPage.getCellValue(0, "Network Name"), "DEVICE_FOR_MATCHING_1");
        Assert.assertEquals(manualMatchingPage.getCellValue(0, "Inventory Name"), "AT-SYS-DEVICE_TO_MATCH");
        manualMatchingPage = ManualMatchingPage.goToManualMatchingPage(driver, BASIC_URL);
        manualMatchingPage.searchByNetworkName("DEVICE_FOR_MATCHING_3");
        Assert.assertEquals(manualMatchingPage.getCellValue(0, "Network Name"), "DEVICE_FOR_MATCHING_3");
    }

    @Test(priority = 10)
    @Description("Go to Network Discovery Control View and run reconciliation and check if it ended without errors")
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

//    @Test
//    public void ddd() {
//        manualMatchingPage = ManualMatchingPage.goToManualMatchingPage(driver, BASIC_URL);
//        manualMatchingPage.getCellValue(0, );
//    }
//
//    @Test
//    public void createNewIO() {
//        DelayUtils.waitForPageToLoad(driver, webDriverWait);
//        CreateMatchingWizardPage createMatchingWizardPage = new CreateMatchingWizardPage(driver);
//        createMatchingWizardPage.selectTO("Device@@poznan_c");
//        createMatchingWizardPage.createNewIO();
//        Button.createById(driver, CREATE_BUTTON_ID).click();
//    }
//
//    @Test
//    public void deleteMatching() {
//        manualMatchingPage = ManualMatchingPage.goToManualMatchingPage(driver, BASIC_URL);
//        DelayUtils.waitForPageToLoad(driver, webDriverWait);
//        manualMatchingPage.searchByNetworkName("poznan_c");
//        DelayUtils.waitForPageToLoad(driver, webDriverWait);
//        manualMatchingPage.selectRow(2);
//        manualMatchingPage.deleteMatching();
//    }

    private void checkPopupMessageType() {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        softAssert.assertNotNull(messages);
        softAssert.assertEquals(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getMessageType(),
                SystemMessageContainer.MessageType.INFO);
    }
}

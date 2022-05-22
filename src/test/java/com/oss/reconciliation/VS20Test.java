package com.oss.reconciliation;

import java.net.URISyntaxException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.google.common.collect.ImmutableList;
import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.alerts.SystemMessageInterface;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.reconciliation.CmDomainWizardPage;
import com.oss.pages.reconciliation.InventoryViewVSObject95Page;
import com.oss.pages.reconciliation.MetamodelPage;
import com.oss.pages.reconciliation.NetworkDiscoveryControlViewPage;
import com.oss.pages.reconciliation.SamplesManagementPage;
import com.oss.pages.reconciliation.VS20Page;

public class VS20Test extends BaseTestCase {

    private static final Logger log = LoggerFactory.getLogger(VS20Test.class);
    private static final String INTERFACE_NAME = "Comarch";
    private static final String OBJECT_TYPE = "COM_Device";
    private static final String CM_DOMAIN_NAME = "AT-SYS-VS20-TEST";
    private static final String DISTINGUISH_NAME = "Device_VS20_TEST";
    private static final String SKIPPING_TEST_MESSAGE = "Skipping tests because resource was not available.";
    private static final String NOTIFICATION_MESSAGE = "Generation of VS Objects Metamodel for CM Interface: Comarch finished";
    private static final List<String> assertionVS20ColumnsList = new ImmutableList.Builder<String>()
            .add("CM Domain Name")
            .add("Type")
            .add("Distinguish Name")
            .add("Is Changed")
            .add("Is Removed")
            .add("Is Root")
            .build();
    private static final List<String> assertionInventoryViewColumnsList = new ImmutableList.Builder<String>()
            .add("CM Domain Name")
            .add("Distinguish Name")
            .add("Is Changed")
            .add("Is Removed")
            .add("Is Root")
            .add("ChassisID")
            .add("Description")
            .add("DeviceCategory")
            .add("HardwareVersion")
            .add("Location")
            .add("ManagementAddress")
            .add("ManagementAddressType")
            .add("ManagementDomain")
            .add("ManufactureDate")
            .add("Manufacturer")
            .add("ModelName")
            .add("NBI_FDN")
            .add("Name")
            .add("NeFunctionName")
            .add("NetworkDomain")
            .add("PartNumber")
            .add("SerialNumber")
            .add("Software")
            .add("Type")
            .add("relation-HostedFunctionsList")
            .add("relation-ManagementSystem")
            .build();
    private static final List<String> assertionList = new ImmutableList.Builder<String>()
            .add("CM Domain Id")
            .add("CM Domain Name")
            .add("Distinguish Name")
            .add("Is Changed")
            .add("Is Removed")
            .add("Is Root")
            .add("Type")
            .add("Metamodel Encoded Type")
            .add("ChassisID")
            .add("Description")
            .add("DeviceCategory")
            .add("HardwareVersion")
            .add("Location")
            .add("ManagementAddress")
            .add("ManagementAddressType")
            .add("ManagementDomain")
            .add("ManufactureDate")
            .add("Manufacturer")
            .add("ModelName")
            .add("NBI_FDN")
            .add("Name")
            .add("NeFunctionName")
            .add("NetworkDomain")
            .add("PartNumber")
            .add("SerialNumber")
            .add("Software")
            .add("Type")
            .add("relation-HostedFunctionsList")
            .add("relation-ManagementSystem")
            .build();
    SoftAssert softAssert = new SoftAssert();
    private VS20Page vs20Page;
    private NetworkDiscoveryControlViewPage networkDiscoveryControlViewPage;
    private MetamodelPage metamodelPage;
    private InventoryViewVSObject95Page inventoryView_vsObject95_page;
    private boolean skipTest = false;

    @BeforeClass
    public void openNetworkDiscoveryControlView() {
        networkDiscoveryControlViewPage = NetworkDiscoveryControlViewPage.goToNetworkDiscoveryControlViewPage(driver, BASIC_URL);
    }

    @Test(priority = 1)
    public void createCmDomain() {
        networkDiscoveryControlViewPage.openCmDomainWizard();
        CmDomainWizardPage wizard = new CmDomainWizardPage(driver);
        wizard.setName(CM_DOMAIN_NAME);
        wizard.setInterface("Comarch");
        wizard.setDomain("Comarch");
        wizard.save();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 2)
    public void uploadSamples() throws URISyntaxException {
        DelayUtils.sleep(1000);
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(CM_DOMAIN_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkDiscoveryControlViewPage.moveToSamplesManagement();
        SamplesManagementPage samplesManagementPage = new SamplesManagementPage(driver);
        samplesManagementPage.selectPath();
        samplesManagementPage.createDirectory(CM_DOMAIN_NAME);
        DelayUtils.sleep(1000);
        samplesManagementPage.uploadSamples("recoSamples/VS20Viewer/DEVICE_VS20_TEST.json");
    }

    @Test(priority = 3)
    public void runReconciliationWithFullSample() {
        openNetworkDiscoveryControlView();
        DelayUtils.sleep(100);
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(CM_DOMAIN_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkDiscoveryControlViewPage.runReconciliation();
        checkPopupMessageType();
        String status = networkDiscoveryControlViewPage.waitForEndOfReco();
        networkDiscoveryControlViewPage.selectLatestReconciliationState();
        if (status.contains("SUCCESS")){
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            Assert.assertTrue(networkDiscoveryControlViewPage.checkIssues(NetworkDiscoveryControlViewPage.IssueLevel.ERROR));
        } else {
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            Assert.assertTrue(networkDiscoveryControlViewPage.checkIssues(NetworkDiscoveryControlViewPage.IssueLevel.STARTUP_FATAL));
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            Assert.assertTrue(networkDiscoveryControlViewPage.checkIssues(NetworkDiscoveryControlViewPage.IssueLevel.FATAL));
        }
    }

    @Test(priority = 4)
    public void searchForObjectInMetamodelPage() {
        metamodelPage = MetamodelPage.goToMetamodelPage(driver, BASIC_URL);
        metamodelPage.searchInterfaceByName(INTERFACE_NAME);
        metamodelPage.searchForObject(OBJECT_TYPE);
        if (!metamodelPage.checkIfObjectExists(OBJECT_TYPE)) {
            metamodelPage.clearNotification();
            metamodelPage.generateMetamodel();
            Assert.assertEquals(metamodelPage.checkNotificationMessage(), NOTIFICATION_MESSAGE);
            skipTest = true;
        }
    }

    @Test(priority = 5)
    public void selectRowOnVS20Page() {
        vs20Page = VS20Page.goToVS20Page(driver, BASIC_URL);
        vs20Page.searchItemByCMDomainName(CM_DOMAIN_NAME);
        vs20Page.searchItemByType(OBJECT_TYPE);
        vs20Page.clickFirstItem();
    }

    @Test(priority = 6)
    public void assertProperties() {
        List<String> attributes = vs20Page.getPropertiesToList();
        System.out.println(attributes.size());
        System.out.println(attributes);
        System.out.println(assertionList.size());
        System.out.println(assertionList);
        Assert.assertEquals(attributes.size(), assertionList.size());
        for (int i = 0; i < attributes.size(); i++) {
            log.info("Checking attribute with index: " + i + ", which equals: '" + assertionList.get(i) + "' on declared assertionList, and equals '" + attributes.get(i) + "' on properties list taken from GUI");
            softAssert.assertEquals((attributes.get(i)), assertionList.get(i));
        }
        softAssert.assertAll();
    }

    @Test(priority = 7)
    public void assertColumnsInVS20Viewer() {
        List<String> columnsList = vs20Page.getColumnsIds();
        Assert.assertEquals(columnsList.size(), assertionVS20ColumnsList.size());
        for (int i = 0; i < assertionVS20ColumnsList.size(); i++) {
            log.info("Checking attribute with index: " + i + ", which equals: '" + assertionVS20ColumnsList.get(i) + "' on declared assertionList, and equals '" + columnsList.get(i) + "' on properties list taken from GUI");
            Assert.assertEquals(assertionVS20ColumnsList.get(i), columnsList.get(i));
        }
    }

    @Test(priority = 8)
    public void showOnInventoryView() {
        vs20Page.navigateToInventoryView();
    }

    @Test(priority = 9)
    public void assertColumnsInInventoryView() {
        inventoryView_vsObject95_page = new InventoryViewVSObject95Page(driver);
        List<String> columnsList = inventoryView_vsObject95_page.getColumnsIds();
        Assert.assertEquals(columnsList.size(), assertionInventoryViewColumnsList.size());
        for (int i = 0; i < assertionInventoryViewColumnsList.size(); i++) {
            log.info("Checking attribute with name: " + assertionInventoryViewColumnsList.get(i));
            log.info("Checking attribute with index: " + i + ", which equals: '" + assertionInventoryViewColumnsList.get(i) + "' on declared assertionList, and equals '" + columnsList.get(i) + "' on properties list taken from GUI");
            Assert.assertEquals(assertionInventoryViewColumnsList.get(i), columnsList.get(i));
        }
    }

    @Test(priority = 10)
    public void deleteCMDomain() {
        networkDiscoveryControlViewPage = NetworkDiscoveryControlViewPage.goToNetworkDiscoveryControlViewPage(driver, BASIC_URL);
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(CM_DOMAIN_NAME);
        networkDiscoveryControlViewPage.clearOldNotifications();
        networkDiscoveryControlViewPage.deleteCmDomain();
        checkPopupMessageType();
        Assert.assertEquals(networkDiscoveryControlViewPage.checkDeleteCmDomainNotification(), "Deleting CM Domain: " + CM_DOMAIN_NAME + " finished");
    }

    @BeforeMethod
    protected void checkEnvironment() {
        if (skipTest) {
            throw new SkipException(SKIPPING_TEST_MESSAGE);
        }
    }

    private void checkPopupMessageType() {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assert.assertNotNull(messages);
        Assert.assertEquals(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getMessageType(),
                SystemMessageContainer.MessageType.INFO);
    }
}
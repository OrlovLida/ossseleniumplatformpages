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
import com.oss.framework.components.mainheader.Notifications;
import com.oss.framework.components.mainheader.NotificationsInterface;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.platform.NewInventoryViewPage;
import com.oss.pages.reconciliation.MetamodelPage;
import com.oss.pages.reconciliation.NetworkDiscoveryControlViewPage;
import com.oss.pages.reconciliation.SamplesManagementPage;
import com.oss.pages.reconciliation.VS20Page;

import io.qameta.allure.Description;

public class VS20Test extends BaseTestCase {

    private static final Logger log = LoggerFactory.getLogger(VS20Test.class);
    private static final String INTERFACE_NAME = "Comarch";
    private static final String OBJECT_TYPE = "COM_Device";
    private static final String CM_DOMAIN_NAME = "AT-SYS-VS20-TEST";
    private static final String DISTINGUISH_NAME = "Device_VS20_TEST";
    private static final String SKIPPING_TEST_MESSAGE = "Skipping tests because resource was not available.";
    private static final String NOTIFICATION_MESSAGE = "Generation of VS Objects Metamodel for CM Interface: Comarch finished";
    private static final String TABLE_ID = "InventoryView_MainCard_VS_Comarch_COM_x_95_x_Device";

    private static final List<String> filterList = new ImmutableList.Builder<String>()
            .add("CM Domain Id")
            .add("CM Domain Name")
            .add("Type")
            .add("Distinguish Name")
            .add("Is Changed")
            .add("Is Removed")
            .add("Is Root")
            .build();

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
    private static final String COMARCH = "Comarch";

    SoftAssert softAssert = new SoftAssert();
    private VS20Page vs20Page;
    private NetworkDiscoveryControlViewPage networkDiscoveryControlViewPage;
    private MetamodelPage metamodelPage;
    private NewInventoryViewPage inventoryViewPage;
    private NotificationsInterface notifications;
    private boolean skipTest = false;

    @BeforeClass
    public void openNetworkDiscoveryControlView() {
        networkDiscoveryControlViewPage = NetworkDiscoveryControlViewPage.goToNetworkDiscoveryControlViewPage(driver, BASIC_URL);
    }

    @Test(priority = 1, description = "Delete CMDomain if it exists")
    @Description("Delete CMDomain if it exists")
    public void deleteCMDomainIfExists() {
        networkDiscoveryControlViewPage = NetworkDiscoveryControlViewPage.goToNetworkDiscoveryControlViewPage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkDiscoveryControlViewPage.searchForCmDomain(CM_DOMAIN_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        if (networkDiscoveryControlViewPage.checkIfCmDomainExists(CM_DOMAIN_NAME)) {
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            networkDiscoveryControlViewPage.selectCmDomain(CM_DOMAIN_NAME);
            notifications = Notifications.create(driver, webDriverWait);
            notifications.clearAllNotification();
            networkDiscoveryControlViewPage.deleteCmDomain();
            Assert.assertEquals(notifications.getNotificationMessage(), "Deleting CM Domain: " + CM_DOMAIN_NAME + " finished");
        } else {
            log.info("CMDomain with name: {} doesn't exist", CM_DOMAIN_NAME);
        }
    }

    @Test(priority = 2, description = "Create CMDomain")
    @Description("Create CMDomain")
    public void createCmDomain() {
        networkDiscoveryControlViewPage = NetworkDiscoveryControlViewPage.goToNetworkDiscoveryControlViewPage(driver, BASIC_URL);
        networkDiscoveryControlViewPage.createCMDomain(CM_DOMAIN_NAME, COMARCH, COMARCH);
    }

    @Test(priority = 3, description = "Upload reconciliation samples", dependsOnMethods = {"createCmDomain"})
    @Description("Go to Sample Management View and upload reconciliation samples")
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

    @Test(priority = 4, description = "Run reconciliation with full samples", dependsOnMethods = {"uploadSamples"})
    @Description("Run reconciliation with full samples")
    public void runReconciliationWithFullSample() {
        openNetworkDiscoveryControlView();
        DelayUtils.sleep(100);
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

    @Test(priority = 5, description = "Search for object in Metamodel Page", dependsOnMethods = {"runReconciliationWithFullSample"})
    @Description("Search for object in Metamodel Page")
    public void searchForObjectInMetamodelPage() {
        metamodelPage = MetamodelPage.goToMetamodelPage(driver, BASIC_URL);
        metamodelPage.searchInterfaceByName(INTERFACE_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        metamodelPage.searchForObject(OBJECT_TYPE);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        if (!metamodelPage.isObjectDisplayed(OBJECT_TYPE)) {
            notifications = Notifications.create(driver, webDriverWait);
            notifications.clearAllNotification();
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            metamodelPage.generateMetamodel();
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            Assert.assertEquals(notifications.getNotificationMessage(), NOTIFICATION_MESSAGE);
            skipTest = true;
        }
    }

    @Test(priority = 6, description = "Assert Filters", dependsOnMethods = {"searchForObjectInMetamodelPage"})
    @Description("Assert Filters on VS 2.0 Viewer Page")
    public void assertFilters() {
        vs20Page = VS20Page.goToVS20Page(driver, BASIC_URL);
        List<String> attributes = vs20Page.getAvailableFilters();
        log.info(String.valueOf(attributes.size()));
        log.info(String.valueOf(attributes));
        log.info(String.valueOf(filterList.size()));
        log.info(String.valueOf(filterList));
        Assert.assertEquals(attributes.size(), filterList.size());
        for (int i = 0; i < attributes.size(); i++) {
            log.info("Checking attribute with index: {}, which equals: '{}' on declared assertionList, and equals '{}' on properties list taken from GUI", i, filterList.get(i), attributes.get(i));
            softAssert.assertEquals((attributes.get(i)), filterList.get(i));
        }
        softAssert.assertAll();
    }

    @Test(priority = 7, description = "Select first row on VS2.0 Viewer Page", dependsOnMethods = {"searchForObjectInMetamodelPage"})
    @Description("Select first row on VS2.0 Viewer Page")
    public void selectRowOnVS20Page() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        vs20Page.searchItemByCMDomainName(CM_DOMAIN_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        vs20Page.searchItemByType(OBJECT_TYPE);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        vs20Page.selectFirstRow();
    }

    @Test(priority = 8, description = "Assert all of properties are correct", dependsOnMethods = {"selectRowOnVS20Page"})
    @Description("Assert all of properties are correct")
    public void assertProperties() {
        List<String> attributes = vs20Page.getPropertiesLabels();
        log.info(String.valueOf(attributes.size()));
        log.info(String.valueOf(attributes));
        log.info(String.valueOf(assertionList.size()));
        log.info(String.valueOf(assertionList));
        Assert.assertEquals(attributes.size(), assertionList.size());
        for (int i = 0; i < attributes.size(); i++) {
            log.info("Checking attribute with index: {}, which equals: '{}' on declared assertionList, and equals '{}' on properties list taken from GUI", i, assertionList.get(i), attributes.get(i));
            softAssert.assertEquals((attributes.get(i)), assertionList.get(i));
        }
        softAssert.assertAll();
    }

    @Test(priority = 9, description = "Assert columns in VS2.0 Viewer Page", dependsOnMethods = {"selectRowOnVS20Page"})
    @Description("Assert columns in VS2.0 Viewer Page")
    public void assertColumnsInVS20Viewer() {
        List<String> columnsList = vs20Page.getColumnsIds();
        Assert.assertEquals(columnsList.size(), assertionVS20ColumnsList.size());
        for (int i = 0; i < assertionVS20ColumnsList.size(); i++) {
            log.info("Checking attribute with index: {}, which equals: '{}' on declared assertionList, and equals '{}' on properties list taken from GUI", i, assertionVS20ColumnsList.get(i), columnsList.get(i));
            Assert.assertEquals(assertionVS20ColumnsList.get(i), columnsList.get(i));
        }
    }

    @Test(priority = 10, description = "Click Show On and Inventory View", dependsOnMethods = {"selectRowOnVS20Page"})
    @Description("Click Show On and Inventory View")
    public void showOnInventoryView() {
        vs20Page.navigateToInventoryView();
    }

    @Test(priority = 11, description = "Assert columns in Inventory View", dependsOnMethods = {"showOnInventoryView"})
    @Description("Assert columns in Inventory View")
    public void assertColumnsInInventoryView() {
        inventoryViewPage = NewInventoryViewPage.getInventoryViewPage(driver, webDriverWait, TABLE_ID);
        List<String> columnsList = inventoryViewPage.getColumnsHeaders();
        Assert.assertEquals(columnsList.size(), assertionInventoryViewColumnsList.size());
        for (int i = 0; i < assertionInventoryViewColumnsList.size(); i++) {
            log.info("Checking attribute with name: {}", assertionInventoryViewColumnsList.get(i));
            log.info("Checking attribute with index: {}, which equals: '{}' on declared assertionList, and equals '{}' on properties list taken from GUI", i, assertionInventoryViewColumnsList.get(i), columnsList.get(i));
            Assert.assertEquals(assertionInventoryViewColumnsList.get(i), columnsList.get(i));
        }
    }

    @BeforeMethod
    protected void skipExceptionTest() {
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
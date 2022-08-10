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
    private static final String SKIPPING_TEST_MESSAGE = "Skipping tests because resource was not available.";
    private static final String NOTIFICATION_MESSAGE = "Generation of VS Objects Metamodel for CM Interface: Comarch finished";
    private static final String TABLE_ID = "InventoryView_MainCard_VS_Comarch_COM_x_95_x_Device";

    private static final String conLIST_CM_DOMAIN_ID = "CM Domain Id";
    private static final String LIST_CM_DOMAIN_NAME = "CM Domain Name";
    private static final String LIST_TYPE = "Type";
    private static final String LIST_DISTINGUISH_NAME = "Distinguish Name";
    private static final String LIST_IS_CHANGED = "Is Changed";
    private static final String LIST_IS_REMOVED = "Is Removed";
    private static final String LIST_IS_ROOT = "Is Root";
    private static final String LIST_CHASSIS_ID = "ChassisID";
    private static final String LIST_DESCRIPTION = "Description";
    private static final String LIST_DEVICE_CATEGORY = "DeviceCategory";
    private static final String LIST_HARDWARE_VERSION = "HardwareVersion";
    private static final String LIST_LOCATION = "Location";
    private static final String LIST_MANAGEMENT_ADDRESS = "ManagementAddress";
    private static final String LIST_MANAGEMENT_ADDRESS_TYPE = "ManagementAddressType";
    private static final String LIST_MANAGEMENT_DOMAIN = "ManagementDomain";
    private static final String LIST_MANUFACTURE_DATE = "ManufactureDate";
    private static final String LIST_MANUFACTURER = "Manufacturer";
    private static final String LIST_MODEL_NAME = "ModelName";
    private static final String LIST_NBI_FDN = "NBI_FDN";
    private static final String LIST_NAME = "Name";
    private static final String LIST_NE_FUNCTION_NAME = "NeFunctionName";
    private static final String LIST_NETWORK_DOMAIN = "NetworkDomain";
    private static final String LIST_PART_NUMBER = "PartNumber";
    private static final String LIST_SERIAL_NUMBER = "SerialNumber";
    private static final String LIST_SOFTWARE = "Software";
    private static final String LIST_RELATION_HOSTED_FUNCTIONS_LIST = "relation-HostedFunctionsList";
    private static final String LIST_RELATION_MANAGEMENT_SYSTEM = "relation-ManagementSystem";
    private static final String LIST_METAMODEL_ENCODED_TYPE = "Metamodel Encoded Type";

    private static final List<String> defaultFilterList = new ImmutableList.Builder<String>()
            .add(LIST_CM_DOMAIN_ID)
            .add(LIST_CM_DOMAIN_NAME)
            .add(LIST_TYPE)
            .add(LIST_DISTINGUISH_NAME)
            .add(LIST_IS_CHANGED)
            .add(LIST_IS_REMOVED)
            .add(LIST_IS_ROOT)
            .build();

    private static final List<String> defaultVS20ColumnsList = new ImmutableList.Builder<String>()
            .add(LIST_CM_DOMAIN_NAME)
            .add(LIST_TYPE)
            .add(LIST_DISTINGUISH_NAME)
            .add(LIST_IS_CHANGED)
            .add(LIST_IS_REMOVED)
            .add(LIST_IS_ROOT)
            .build();


    private static final List<String> defaultInventoryViewColumnsList = new ImmutableList.Builder<String>()
            .add(LIST_CM_DOMAIN_NAME)
            .add(LIST_DISTINGUISH_NAME)
            .add(LIST_IS_CHANGED)
            .add(LIST_IS_REMOVED)
            .add(LIST_IS_ROOT)
            .add(LIST_CHASSIS_ID)
            .add(LIST_DESCRIPTION)
            .add(LIST_DEVICE_CATEGORY)
            .add(LIST_HARDWARE_VERSION)
            .add(LIST_LOCATION)
            .add(LIST_MANAGEMENT_ADDRESS)
            .add(LIST_MANAGEMENT_ADDRESS_TYPE)
            .add(LIST_MANAGEMENT_DOMAIN)
            .add(LIST_MANUFACTURE_DATE)
            .add(LIST_MANUFACTURER)
            .add(LIST_MODEL_NAME)
            .add(LIST_NBI_FDN)
            .add(LIST_NAME)
            .add(LIST_NE_FUNCTION_NAME)
            .add(LIST_NETWORK_DOMAIN)
            .add(LIST_PART_NUMBER)
            .add(LIST_SERIAL_NUMBER)
            .add(LIST_SOFTWARE)
            .add(LIST_TYPE)
            .add(LIST_RELATION_HOSTED_FUNCTIONS_LIST)
            .add(LIST_RELATION_MANAGEMENT_SYSTEM)
            .build();

    private static final List<String> defaultPropertiesList = new ImmutableList.Builder<String>()
            .add(LIST_CM_DOMAIN_ID)
            .add(LIST_CM_DOMAIN_NAME)
            .add(LIST_DISTINGUISH_NAME)
            .add(LIST_IS_CHANGED)
            .add(LIST_IS_REMOVED)
            .add(LIST_IS_ROOT)
            .add(LIST_TYPE)
            .add(LIST_METAMODEL_ENCODED_TYPE)
            .add(LIST_CHASSIS_ID)
            .add(LIST_DESCRIPTION)
            .add(LIST_DEVICE_CATEGORY)
            .add(LIST_HARDWARE_VERSION)
            .add(LIST_LOCATION)
            .add(LIST_MANAGEMENT_ADDRESS)
            .add(LIST_MANAGEMENT_ADDRESS_TYPE)
            .add(LIST_MANAGEMENT_DOMAIN)
            .add(LIST_MANUFACTURE_DATE)
            .add(LIST_MANUFACTURER)
            .add(LIST_MODEL_NAME)
            .add(LIST_NBI_FDN)
            .add(LIST_NAME)
            .add(LIST_NE_FUNCTION_NAME)
            .add(LIST_NETWORK_DOMAIN)
            .add(LIST_PART_NUMBER)
            .add(LIST_SERIAL_NUMBER)
            .add(LIST_SOFTWARE)
            .add(LIST_TYPE)
            .add(LIST_RELATION_HOSTED_FUNCTIONS_LIST)
            .add(LIST_RELATION_MANAGEMENT_SYSTEM)
            .build();

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
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkDiscoveryControlViewPage.searchForCmDomain(CM_DOMAIN_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        if (networkDiscoveryControlViewPage.isCmDomainPresent(CM_DOMAIN_NAME)) {
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
        networkDiscoveryControlViewPage.createCMDomain(CM_DOMAIN_NAME, INTERFACE_NAME, INTERFACE_NAME);
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
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        if (status.contains("SUCCESS")) {
            Assert.assertTrue(networkDiscoveryControlViewPage.checkIssues(NetworkDiscoveryControlViewPage.IssueLevel.ERROR));
        } else {
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
        log.info(String.valueOf(defaultFilterList.size()));
        log.info(String.valueOf(defaultFilterList));
        Assert.assertEquals(attributes.size(), defaultFilterList.size());
        for (int i = 0; i < attributes.size(); i++) {
            log.info("Checking attribute with index: {}, which equals: '{}' on declared assertionList, and equals '{}' on properties list taken from GUI", i, defaultFilterList.get(i), attributes.get(i));
            softAssert.assertEquals((attributes.get(i)), defaultFilterList.get(i));
        }
        softAssert.assertAll();
    }

    @Test(priority = 7, description = "Select first row on VS2.0 Viewer Page", dependsOnMethods = {"searchForObjectInMetamodelPage"})
    @Description("Select first row on VS2.0 Viewer Page")
    public void selectRowOnVS20Page() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        vs20Page.searchByCMDomainName(CM_DOMAIN_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        vs20Page.searchByType(OBJECT_TYPE);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        vs20Page.selectFirstRow();
    }

    @Test(priority = 8, description = "Assert all of properties are correct", dependsOnMethods = {"selectRowOnVS20Page"})
    @Description("Assert all of properties are correct")
    public void assertProperties() {
        List<String> attributes = vs20Page.getPropertiesLabels();
        log.info(String.valueOf(attributes.size()));
        log.info(String.valueOf(attributes));
        log.info(String.valueOf(defaultPropertiesList.size()));
        log.info(String.valueOf(defaultPropertiesList));
        Assert.assertEquals(attributes.size(), defaultPropertiesList.size());
        for (int i = 0; i < attributes.size(); i++) {
            log.info("Checking attribute with index: {}, which equals: '{}' on declared assertionList, and equals '{}' on properties list taken from GUI", i, defaultPropertiesList.get(i), attributes.get(i));
            softAssert.assertEquals((attributes.get(i)), defaultPropertiesList.get(i));
        }
        softAssert.assertAll();
    }

    @Test(priority = 9, description = "Assert columns in VS2.0 Viewer Page", dependsOnMethods = {"selectRowOnVS20Page"})
    @Description("Assert columns in VS2.0 Viewer Page")
    public void assertColumnsInVS20Viewer() {
        List<String> columnsList = vs20Page.getColumnsHeaders();
        Assert.assertEquals(columnsList.size(), defaultVS20ColumnsList.size());
        for (int i = 0; i < defaultVS20ColumnsList.size(); i++) {
            log.info("Checking attribute with index: {}, which equals: '{}' on declared assertionList, and equals '{}' on properties list taken from GUI", i, defaultVS20ColumnsList.get(i), columnsList.get(i));
            Assert.assertEquals(defaultVS20ColumnsList.get(i), columnsList.get(i));
        }
    }

    @Test(priority = 10, description = "Click Show On and Type Specific VS Viewer", dependsOnMethods = {"selectRowOnVS20Page"})
    @Description("Click Show On and Type Specific VS Viewer")
    public void showOnTypeSpecificVSViewer() {
        vs20Page.navigateToInventoryView();
    }

    @Test(priority = 11, description = "Assert columns in Type Specific VS Viewer", dependsOnMethods = {"showOnTypeSpecificVSViewer"})
    @Description("Assert columns in Type Specific VS Viewer")
    public void assertColumnsInTypeSpecificVSViewer() {
        inventoryViewPage = NewInventoryViewPage.getInventoryViewPage(driver, webDriverWait, TABLE_ID);
        List<String> columnsList = inventoryViewPage.getColumnsHeaders();
        Assert.assertEquals(columnsList.size(), defaultInventoryViewColumnsList.size());
        for (int i = 0; i < defaultInventoryViewColumnsList.size(); i++) {
            log.info("Checking attribute with name: {}", defaultInventoryViewColumnsList.get(i));
            log.info("Checking attribute with index: {}, which equals: '{}' on declared assertionList, and equals '{}' on properties list taken from GUI", i, defaultInventoryViewColumnsList.get(i), columnsList.get(i));
            Assert.assertEquals(defaultInventoryViewColumnsList.get(i), columnsList.get(i));
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
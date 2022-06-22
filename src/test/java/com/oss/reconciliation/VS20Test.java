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

import io.qameta.allure.Description;

public class VS20Test extends BaseTestCase {

    private static final Logger log = LoggerFactory.getLogger(VS20Test.class);
    private static final String INTERFACE_NAME = "Comarch";
    private static final String OBJECT_TYPE = "COM_Device";
    private static final String CM_DOMAIN_NAME = "AT-SYS-VS20-TEST";
    private static final String DISTINGUISH_NAME = "Device_VS20_TEST";
    private static final String SKIPPING_TEST_MESSAGE = "Skipping tests because resource was not available.";
    private static final String NOTIFICATION_MESSAGE = "Generation of VS Objects Metamodel for CM Interface: Comarch finished";

//    private static final Map<String, String> rightHereMap = new HashMap<String, String>()
//    {
//        {
//            put("VS_Comarch_COM_x_95_x_Device_Description", "Cisco IOS XR Software (Cisco ASR9K Series), Version 6.2.3[Default]rnCopyright (c) 2018 by Cisco Systems, Inc.");
//            put("isRoot", "true");
//            put("cmDomainName", "AT-SYS-VS20-TEST");
//            put("VS_Comarch_COM_x_95_x_Device_NBI_x_95_x_FDN", "MD=CISCO_EPNM!ND=Device_VS20_TEST");
//            put("VS_Comarch_COM_x_95_x_Device_PartNumber", "");
//            put("VS_Comarch_COM_x_95_x_Device_Software", "6.2.3[Default]");
//            put("type", "VS_Comarch_COM_x_95_x_Device");
//            put("VS_Comarch_COM_x_95_x_Device_HardwareVersion", "V01");
//            put("VS_Comarch_COM_x_95_x_Device_NetworkDomain", "");
//            put("isRemoved", "false");
//            put("VS_Comarch_COM_x_95_x_Device_DeviceCategory", "");
//            put("VS_Comarch_COM_x_95_x_Device_NeFunctionName", "");
//            put("isChanged", "true");
//            put("VS_Comarch_COM_x_95_x_Device_ManagementDomain", "ChangeModel_test");
//            put("VS_Comarch_COM_x_95_x_Device_relation_x_45_x_HostedFunctionsList", "Device@@Device_VS20_TEST##IPDeviceFunction@@8a864a1e-418b-48ad-a6d4-257204046339");
//            put("VS_Comarch_COM_x_95_x_Device_ManufactureDate", "2019-01-23T10:40:15.778-03:00");
//            put("VS_Comarch_COM_x_95_x_Device_ChassisID", "");
//            put("VS_Comarch_COM_x_95_x_Device_SerialNumber", "FOX2141P2PZ");
//            put("VS_Comarch_COM_x_95_x_Device_relation_x_45_x_ManagementSystem", "");
//            put("VS_Comarch_COM_x_95_x_Device_Location", "Provincia: AMBA; Localidad: Merlo; Centro: MRL; Ubicacion: Riobamba 494 y Sarandi; Piso: 2; Sala: Transmision Nueva; Traza: Guemes-Merlo;: GEO -34.66711, -58.725129:;");
//            put("VS_Comarch_COM_x_95_x_Device_ModelName", "");
//            put("VS_Comarch_COM_x_95_x_Device_Type", "IPDevice");
//            put("VS_Comarch_COM_x_95_x_Device_ManagementAddress", "10.193.0.9");
//            put("VS_Comarch_COM_x_95_x_Device_ManagementAddressType", "IPv4");
//            put("distinguishName", "Device@@Device_VS20_TEST");
//            put("VS_Comarch_COM_x_95_x_Device_Manufacturer", "Cisco Systems Inc.");
//            put("nativeType", "COM_Device");
//            put("VS_Comarch_COM_x_95_x_Device_Name", "Device_VS20_TEST");
//            put("cmDomainId", "ef0e5edd-c7cb-4e14-bb67-714f1c3cb3b8");
//        }
//    };

    private static final List<String> assertionFilterList = new ImmutableList.Builder<String>()
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

    @BeforeMethod
    protected void skipExceptionTest() {
        if (skipTest) {
            throw new SkipException(SKIPPING_TEST_MESSAGE);
        }
    }

    @Test(priority = 1, description = "Delete CMDomain if it exists")
    @Description("Delete CMDomain if it exists")
    public void deleteCMDomainIfExists() {
        networkDiscoveryControlViewPage = NetworkDiscoveryControlViewPage.goToNetworkDiscoveryControlViewPage(driver, BASIC_URL);
        networkDiscoveryControlViewPage.searchForCmDomain(CM_DOMAIN_NAME);
        if (networkDiscoveryControlViewPage.checkIfCmDomainExists(CM_DOMAIN_NAME)) {
            networkDiscoveryControlViewPage.selectCmDomain(CM_DOMAIN_NAME);
            networkDiscoveryControlViewPage.clearOldNotifications();
            networkDiscoveryControlViewPage.deleteCmDomain();
            Assert.assertEquals(networkDiscoveryControlViewPage.checkDeleteCmDomainNotification(), "Deleting CM Domain: " + CM_DOMAIN_NAME + " finished");
        } else {
            log.info("CMDomain with name: " + CM_DOMAIN_NAME + " doesn't exist");
        }
    }


    @Test(priority = 2, description = "Create CMDomain")
    @Description("Create CMDomain")
    public void createCmDomain() {
        networkDiscoveryControlViewPage.openCmDomainWizard();
        CmDomainWizardPage wizard = new CmDomainWizardPage(driver);
        wizard.setName(CM_DOMAIN_NAME);
        wizard.setInterface("Comarch");
        wizard.setDomain("Comarch");
        wizard.save();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
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
        metamodelPage.searchForObject(OBJECT_TYPE);
        if (!metamodelPage.checkIfObjectExists(OBJECT_TYPE)) {
            metamodelPage.clearNotification();
            metamodelPage.generateMetamodel();
            Assert.assertEquals(metamodelPage.checkNotificationMessage(), NOTIFICATION_MESSAGE);
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
        log.info(String.valueOf(assertionFilterList.size()));
        log.info(String.valueOf(assertionFilterList));
        Assert.assertEquals(attributes.size(), assertionFilterList.size());
        for (int i = 0; i < attributes.size(); i++) {
            log.info("Checking attribute with index: " + i + ", which equals: '" + assertionFilterList.get(i) + "' on declared assertionList, and equals '" + attributes.get(i) + "' on properties list taken from GUI");
            softAssert.assertEquals((attributes.get(i)), assertionFilterList.get(i));
        }
        softAssert.assertAll();
    }

    @Test(priority = 7, description = "Select first row on VS2.0 Viewer Page", dependsOnMethods = {"searchForObjectInMetamodelPage"})
    @Description("Select first row on VS2.0 Viewer Page")
    public void selectRowOnVS20Page() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        vs20Page.searchItemByCMDomainName(CM_DOMAIN_NAME);
        vs20Page.searchItemByType(OBJECT_TYPE);
        vs20Page.clickFirstItem();
    }

    @Test(priority = 8, description = "Assert all of properties are correct", dependsOnMethods = {"selectRowOnVS20Page"})
    @Description("Assert all of properties are correct")
    public void assertProperties() {
        List<String> attributes = vs20Page.getPropertiesToList();
        log.info(String.valueOf(attributes.size()));
        log.info(String.valueOf(attributes));
        log.info(String.valueOf(assertionList.size()));
        log.info(String.valueOf(assertionList));
        Assert.assertEquals(attributes.size(), assertionList.size());
        for (int i = 0; i < attributes.size(); i++) {
            log.info("Checking attribute with index: " + i + ", which equals: '" + assertionList.get(i) + "' on declared assertionList, and equals '" + attributes.get(i) + "' on properties list taken from GUI");
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
            log.info("Checking attribute with index: " + i + ", which equals: '" + assertionVS20ColumnsList.get(i) + "' on declared assertionList, and equals '" + columnsList.get(i) + "' on properties list taken from GUI");
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
        inventoryView_vsObject95_page = new InventoryViewVSObject95Page(driver);
        List<String> columnsList = inventoryView_vsObject95_page.getColumnsIds();
        Assert.assertEquals(columnsList.size(), assertionInventoryViewColumnsList.size());
        for (int i = 0; i < assertionInventoryViewColumnsList.size(); i++) {
            log.info("Checking attribute with name: " + assertionInventoryViewColumnsList.get(i));
            log.info("Checking attribute with index: " + i + ", which equals: '" + assertionInventoryViewColumnsList.get(i) + "' on declared assertionList, and equals '" + columnsList.get(i) + "' on properties list taken from GUI");
            Assert.assertEquals(assertionInventoryViewColumnsList.get(i), columnsList.get(i));
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
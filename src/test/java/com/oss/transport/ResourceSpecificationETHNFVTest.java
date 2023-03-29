package com.oss.transport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.oss.BaseTestCase;
import com.oss.framework.navigation.sidemenu.SideMenu;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.list.CommonList;
import com.oss.framework.widgets.table.OldTable;
import com.oss.pages.resourcecatalog.ResourceSpecificationsViewPage;

import io.qameta.allure.Description;

/**
 * @author Szymon Masarczyk
 */

public class ResourceSpecificationETHNFVTest extends BaseTestCase {

    private static final String RESOURCE_CATALOG_VIEW_SIDE_MENU = "Resource Catalog";
    private static final String RESOURCE_CATALOG_SIDE_MENU = "Resource Catalog";
    private static final String RESOURCE_SPECIFICATIONS_SIDE_MENU = "Resource Specifications";
    private static final String AGGREGATED_ETHERNET_LINK_NAME = "Aggregated Ethernet Link";
    private static final String ETHERNET_LINK_NAME = "Ethernet Link";
    private static final String ELINE_NAME = "E-Line";
    private static final String VIRTUAL_NETWORK_NAME = "Virtual Network";
    private static final String VLAN_NAME = "VLAN";
    private static final String AGGREGATED_ETHERNET_INTERFACE_NAME = "Aggregated Ethernet Interface";
    private static final String ETHERNET_INTERFACE_NAME = "Ethernet Interface";
    private static final String IRB_INTERFACE_NAME = "IRB Interface";
    private static final String LOOPBACK_INTERFACE_NAME = "Loopback Interface";
    private static final String VLAN_INTERFACE_NAME = "VLAN Interface";
    private static final String IP_NETWORK_ELEMENT_NAME = "IP Network Element";
    private static final String COLUMN_SPECIFICATION_NAME_LABEL = "Specification Name";
    private static final String DETAILS_TAB_ID = "0";
    private static final String CHARACTERISTIC_TAB_ID = "tab_1";
    private static final String RELATIONS_TAB_ID = "tab_2";
    private static final String DETAILS_VALUE_COLUMN = "Value";
    private static final String DETAILS_NAME_COLUMN = "Name";
    private static final String ERROR_MESSAGE_INFO = "Incorrect attribute value: %s";
    private static final String FALSE_VALUE = "false";
    private static final String NAMING_RULE_VALUE = "{name}-{InstanceNumber}";
    private static final String ACTION_ANY_VALUE = "ANY";
    private static final String LIFE_CYCLE_ACTIVE_VALUE = "Active";
    private static final String TYPE_RESOURCE_VALUE = "RESOURCE";
    private static final String CATEGORY_BASE_MODEL_TRANSPORT_VALUE = "Base model - Transport";
    private static final String ORIGIN_PRODUCT_VALUE = "Product";
    private static final String CHARACTERISTIC_NAME_COLUMN = "Name";
    private static final String CHARACTERISTIC_TYPE_COLUMN = "Type";
    private static final String CHARACTERISTIC_ALLOWABLE_VALUES_COLUMN = "Allowable values";
    private static final String CHARACTERISTIC_DEFAULT_VALUE_COLUMN = "Default value";
    private static final String CHARACTERISTIC_MANDATORY_COLUMN = "Mandatory";
    private static final String STRING_TYPE = "String";
    private static final String NUMBER_TYPE = "Number";
    private static final String BOOLEAN_TYPE = "Boolean";
    private static final String DATE_TYPE = "Date";
    private static final String RELATIONS_SPECIFICATION_NAME = "Specification";
    private static final String RELATIONS_RELATION_ROLE_NAME = "Relation role";
    private static final String RELATIONS_MAX_CARDINALITY_NAME = "Max cardinality";
    private static final String CHILD_NODE_VALUE = "childNode";
    private static final String IP_REDUNDANCY_GROUP_MEMBERSHIP_VALUE = "ipRedundancyGroupMemberships";

    @BeforeClass
    public void openResourceSpecification() {
        openResourceSpecificationView();
        waitForPageToLoad();
    }

    @Test(priority = 1, description = "Aggregated Ethernet Link Resource Specification")
    @Description("AEL Resource Specification")
    public void aggregatedEthernetLinkRSTest() {
        searchObjectResourceSpecification(AGGREGATED_ETHERNET_LINK_NAME);
        assertDetailsTabAttributes(defaultAggregatedEthernetLinkAttributes());
        selectTab(CHARACTERISTIC_TAB_ID);
        assertCharacteristicTabAttributesForAEL();
    }

    @Test(priority = 2, description = "Ethernet Link Resource Specification")
    @Description("EL Resource Specification")
    public void ethernetLinkRSTest() {
        selectTab(DETAILS_TAB_ID);
        clearAllFiltersOnSearch();
        searchObjectResourceSpecification(ETHERNET_LINK_NAME);
        assertDetailsTabAttributes(defaultEthernetLinkAttributes());
        selectTab(CHARACTERISTIC_TAB_ID);
        assertCharacteristicTabAttributesForEL();
    }

    @Test(priority = 3, description = "E-Line Resource Specification")
    @Description("ELine Resource Specification")
    public void eLineRSTest() {
        selectTab(DETAILS_TAB_ID);
        clearAllFiltersOnSearch();
        searchObjectResourceSpecification(ELINE_NAME);
        assertDetailsTabAttributes(defaultELineAttributes());
        selectTab(CHARACTERISTIC_TAB_ID);
        assertCharacteristicTabAttributesForELine();
    }

    @Test(priority = 4, description = "Virtual Network Resource Specification")
    @Description("Virtual Network Resource Specification")
    public void virtualNetworkRSTest() {
        selectTab(DETAILS_TAB_ID);
        clearAllFiltersOnSearch();
        searchObjectResourceSpecification(VIRTUAL_NETWORK_NAME);
        assertDetailsTabAttributes(defaultVirtualNetworkAttributes());
        selectTab(CHARACTERISTIC_TAB_ID);
        assertCharacteristicTabAttributesForVN();
    }

    @Test(priority = 5, description = "VLAN Resource Specification")
    @Description("VLAN Resource Specification")
    public void vlanRSTest() {
        selectTab(DETAILS_TAB_ID);
        clearAllFiltersOnSearch();
        searchObjectResourceSpecification(VLAN_NAME);
        assertDetailsTabAttributes(defaultVLANAttributes());
        selectTab(CHARACTERISTIC_TAB_ID);
        assertCharacteristicTabAttributesForVLAN();
    }

    @Test(priority = 6, description = "Aggregated Ethernet Interface Resource Specification")
    @Description("AEI Resource Specification")
    public void aeiRSTest() {
        selectTab(DETAILS_TAB_ID);
        clearAllFiltersOnSearch();
        searchObjectResourceSpecification(AGGREGATED_ETHERNET_INTERFACE_NAME);
        assertDetailsTabAttributes(defaultAggregatedEthernetInterfaceAttributes());
        selectTab(CHARACTERISTIC_TAB_ID);
        assertCharacteristicTabAttributesForAEI();
        selectTab(RELATIONS_TAB_ID);
        assertRelationsTabAttributesForAEI();
    }

    @Test(priority = 7, description = "Ethernet Interface Resource Specification")
    @Description("EI Resource Specification")
    public void ethernetInterfaceRSTest() {
        selectTab(DETAILS_TAB_ID);
        clearAllFiltersOnSearch();
        searchObjectResourceSpecification(ETHERNET_INTERFACE_NAME);
        assertDetailsTabAttributes(defaultEthernetInterfaceAttributes());
        selectTab(CHARACTERISTIC_TAB_ID);
        assertCharacteristicTabAttributesForEI();
        selectTab(RELATIONS_TAB_ID);
        assertRelationsTabAttributesForEI();
    }

    @Test(priority = 8, description = "IP Network Element Resource Specification")
    @Description("IPNE Resource Specification")
    public void ipNetworkElementRSTest() {
        selectTab(DETAILS_TAB_ID);
        clearAllFiltersOnSearch();
        searchObjectResourceSpecification(IP_NETWORK_ELEMENT_NAME);
        assertDetailsTabAttributes(defaultIPNetworkElementAttributes());
        selectTab(CHARACTERISTIC_TAB_ID);
        assertCharacteristicTabAttributesForIPNE();
        selectTab(RELATIONS_TAB_ID);
        assertRelationsTabAttributesForIPNE();
    }

    @Test(priority = 9, description = "IRB Interface Resource Specification")
    @Description("IRB Resource Specification")
    public void irbInterfaceRSTest() {
        selectTab(DETAILS_TAB_ID);
        clearAllFiltersOnSearch();
        searchObjectResourceSpecification(IRB_INTERFACE_NAME);
        assertDetailsTabAttributes(defaultIRBInterfaceAttributes());
        selectTab(CHARACTERISTIC_TAB_ID);
        assertCharacteristicTabAttributesForIRB();
    }

    @Test(priority = 10, description = "Loopback Interface Resource Specification")
    @Description("Loopback Interface Resource Specification")
    public void loopbackInterfaceRSTest() {
        selectTab(DETAILS_TAB_ID);
        clearAllFiltersOnSearch();
        searchObjectResourceSpecification(LOOPBACK_INTERFACE_NAME);
        assertDetailsTabAttributes(defaultLoopbackInterfaceAttributes());
        selectTab(CHARACTERISTIC_TAB_ID);
        assertCharacteristicTabAttributesForLoopback();
    }

    @Test(priority = 11, description = "VLAN Interface Resource Specification")
    @Description("VLAN Interface Resource Specification")
    public void vlanInterfaceRSTest() {
        selectTab(DETAILS_TAB_ID);
        clearAllFiltersOnSearch();
        searchObjectResourceSpecification(VLAN_INTERFACE_NAME);
        assertDetailsTabAttributes(defaultVLANInterfaceAttributes());
        selectTab(CHARACTERISTIC_TAB_ID);
        assertCharacteristicTabAttributesForVLANInterface();
        selectTab(RELATIONS_TAB_ID);
        assertRelationsTabAttributesForVLANInterface();
    }

    private void openResourceSpecificationView() {
        SideMenu sidemenu = SideMenu.create(driver, webDriverWait);
        sidemenu.callActionByLabel(RESOURCE_SPECIFICATIONS_SIDE_MENU, RESOURCE_CATALOG_SIDE_MENU, RESOURCE_CATALOG_VIEW_SIDE_MENU);
    }

    private void searchObjectResourceSpecification(String objectName) {
        ResourceSpecificationsViewPage resourceSpecificationsViewPage = ResourceSpecificationsViewPage.create(driver, webDriverWait);
        resourceSpecificationsViewPage.searchByAttribute("name", objectName);
        waitForPageToLoad();
        resourceSpecificationsViewPage.selectTreeNode(objectName, COLUMN_SPECIFICATION_NAME_LABEL);
    }

    private void clearAllFiltersOnSearch() {
        ResourceSpecificationsViewPage resourceSpecificationsViewPage = ResourceSpecificationsViewPage.create(driver, webDriverWait);
        resourceSpecificationsViewPage.clickClearAll();
    }

    private void selectTab(String tabId) {
        ResourceSpecificationsViewPage resourceSpecificationsViewPage = ResourceSpecificationsViewPage.create(driver, webDriverWait);
        resourceSpecificationsViewPage.selectTab(tabId);
    }

    private void assertDetailsTabAttributes(DetailTabAttributes detailTabAttributes) {
        ResourceSpecificationsViewPage resourceSpecificationsViewPage = ResourceSpecificationsViewPage.create(driver, webDriverWait);
        CommonList commonList = resourceSpecificationsViewPage.getDetailsCommonList();
        String actualIdentifier = commonList.getRow(DETAILS_NAME_COLUMN, "Identifier").getValue(DETAILS_VALUE_COLUMN);
        String actualName = commonList.getRow(DETAILS_NAME_COLUMN, "Name").getValue(DETAILS_VALUE_COLUMN);
        String actualCategory = commonList.getRow(DETAILS_NAME_COLUMN, "Category").getValue(DETAILS_VALUE_COLUMN);
        String actualType = commonList.getRow(DETAILS_NAME_COLUMN, "Type").getValue(DETAILS_VALUE_COLUMN);
        String actualInventoryType = commonList.getRow(DETAILS_NAME_COLUMN, "Inventory Type").getValue(DETAILS_VALUE_COLUMN);
        String actualInstanceType = commonList.getRow(DETAILS_NAME_COLUMN, "Instance Type").getValue(DETAILS_VALUE_COLUMN);
        String actualLifeCycle = commonList.getRow(DETAILS_NAME_COLUMN, "Life cycle").getValue(DETAILS_VALUE_COLUMN);
        String actualBaseSpecification = commonList.getRow(DETAILS_NAME_COLUMN, "Base Specification").getValue(DETAILS_VALUE_COLUMN);
        String actualAction = commonList.getRow(DETAILS_NAME_COLUMN, "Action").getValue(DETAILS_VALUE_COLUMN);
        String actualDescription = commonList.getRow(DETAILS_NAME_COLUMN, "Description").getValue(DETAILS_VALUE_COLUMN);
        String actualNamingRule = commonList.getRow(DETAILS_NAME_COLUMN, "Naming Rule").getValue(DETAILS_VALUE_COLUMN);
        String actualHasDescriptor = commonList.getRow(DETAILS_NAME_COLUMN, "Has descriptor").getValue(DETAILS_VALUE_COLUMN);
        String actualIsAbstract = commonList.getRow(DETAILS_NAME_COLUMN, "Is abstract").getValue(DETAILS_VALUE_COLUMN);
        String actualOrigin = commonList.getRow(DETAILS_NAME_COLUMN, "Origin").getValue(DETAILS_VALUE_COLUMN);
        String actualTemplateInputParameter = commonList.getRow(DETAILS_NAME_COLUMN, "Template input parameter").getValue(DETAILS_VALUE_COLUMN);
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(actualIdentifier, detailTabAttributes.identifier, String.format(ERROR_MESSAGE_INFO, actualIdentifier));
        softAssert.assertEquals(actualName, detailTabAttributes.name, String.format(ERROR_MESSAGE_INFO, actualName));
        softAssert.assertEquals(actualCategory, detailTabAttributes.category, String.format(ERROR_MESSAGE_INFO, actualCategory));
        softAssert.assertEquals(actualType, detailTabAttributes.type, String.format(ERROR_MESSAGE_INFO, actualType));
        softAssert.assertEquals(actualInventoryType, detailTabAttributes.inventoryType, String.format(ERROR_MESSAGE_INFO, actualInventoryType));
        softAssert.assertEquals(actualInstanceType, detailTabAttributes.instanceType, String.format(ERROR_MESSAGE_INFO, actualInstanceType));
        softAssert.assertEquals(actualLifeCycle, detailTabAttributes.lifeCycle, String.format(ERROR_MESSAGE_INFO, actualLifeCycle));
        softAssert.assertEquals(actualBaseSpecification, detailTabAttributes.baseSpecification, String.format(ERROR_MESSAGE_INFO, actualBaseSpecification));
        softAssert.assertEquals(actualAction, detailTabAttributes.action, String.format(ERROR_MESSAGE_INFO, actualAction));
        softAssert.assertEquals(actualDescription, detailTabAttributes.description, String.format(ERROR_MESSAGE_INFO, actualDescription));
        softAssert.assertEquals(actualNamingRule, detailTabAttributes.namingRule, String.format(ERROR_MESSAGE_INFO, actualNamingRule));
        softAssert.assertEquals(actualHasDescriptor, detailTabAttributes.hasDescriptor, String.format(ERROR_MESSAGE_INFO, actualHasDescriptor));
        softAssert.assertEquals(actualIsAbstract, detailTabAttributes.isAbstract, String.format(ERROR_MESSAGE_INFO, actualIsAbstract));
        softAssert.assertEquals(actualOrigin, detailTabAttributes.origin, String.format(ERROR_MESSAGE_INFO, actualOrigin));
        softAssert.assertEquals(actualTemplateInputParameter, detailTabAttributes.templateInputParameter, String.format(ERROR_MESSAGE_INFO, actualTemplateInputParameter));
        softAssert.assertAll();
    }

    private void assertCharacteristicTabAttributesForAEL() {
        ResourceSpecificationsViewPage resourceSpecificationsViewPage = ResourceSpecificationsViewPage.create(driver, webDriverWait);
        OldTable oldTable = resourceSpecificationsViewPage.getCharacteristicsAttributesOldTable();
        oldTable.setPageSize(50);

        int rowNumber = oldTable.getRowNumber("AggregationProtocol", CHARACTERISTIC_NAME_COLUMN);
        String aggregationProtocolType = oldTable.getCellValue(rowNumber, CHARACTERISTIC_TYPE_COLUMN);
        String aggregationProtocolAllowableValues = oldTable.getCellTextValue(rowNumber, "Allowable values");
        rowNumber = oldTable.getRowNumber("EffectiveCapacityMbps", CHARACTERISTIC_NAME_COLUMN);
        String effectiveCapacityMbpsType = oldTable.getCellValue(rowNumber, CHARACTERISTIC_TYPE_COLUMN);
        rowNumber = oldTable.getRowNumber("SpeedMbps", CHARACTERISTIC_NAME_COLUMN);
        String speedMbpsType = oldTable.getCellValue(rowNumber, CHARACTERISTIC_TYPE_COLUMN);

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(aggregationProtocolType, STRING_TYPE);
        softAssert.assertEquals(aggregationProtocolAllowableValues, "LACP, NONE");
        softAssert.assertEquals(effectiveCapacityMbpsType, NUMBER_TYPE);
        softAssert.assertEquals(speedMbpsType, NUMBER_TYPE);

        softAssert.assertAll();
    }

    private void assertCharacteristicTabAttributesForEL() {
        ResourceSpecificationsViewPage resourceSpecificationsViewPage = ResourceSpecificationsViewPage.create(driver, webDriverWait);
        OldTable oldTable = resourceSpecificationsViewPage.getCharacteristicsAttributesOldTable();
        oldTable.setPageSize(50);

        int rowNumber = oldTable.getRowNumber("EffectiveCapacityMbps", CHARACTERISTIC_NAME_COLUMN);
        String effectiveCapacityMbpsType = oldTable.getCellValue(rowNumber, CHARACTERISTIC_TYPE_COLUMN);
        rowNumber = oldTable.getRowNumber("EffectiveCapacityUnit", CHARACTERISTIC_NAME_COLUMN);
        String effectiveCapacityUnitType = oldTable.getCellValue(rowNumber, CHARACTERISTIC_TYPE_COLUMN);
        String effectiveCapacityUnitAllowableValues = oldTable.getCellValue(rowNumber, "Allowable values");
        rowNumber = oldTable.getRowNumber("IsTrunk", CHARACTERISTIC_NAME_COLUMN);
        String isTrunkType = oldTable.getCellValue(rowNumber, CHARACTERISTIC_TYPE_COLUMN);
        rowNumber = oldTable.getRowNumber("Latency", CHARACTERISTIC_NAME_COLUMN);
        String latencyType = oldTable.getCellValue(rowNumber, CHARACTERISTIC_TYPE_COLUMN);
        rowNumber = oldTable.getRowNumber("Role", CHARACTERISTIC_NAME_COLUMN);
        String roleType = oldTable.getCellValue(rowNumber, CHARACTERISTIC_TYPE_COLUMN);
        String roleAllowableValues = oldTable.getCellValue(rowNumber, CHARACTERISTIC_ALLOWABLE_VALUES_COLUMN);
        rowNumber = oldTable.getRowNumber("Speed", CHARACTERISTIC_NAME_COLUMN);
        String speedType = oldTable.getCellValue(rowNumber, CHARACTERISTIC_TYPE_COLUMN);
        String speedAllowableValues = oldTable.getCellValue(rowNumber, CHARACTERISTIC_ALLOWABLE_VALUES_COLUMN);
        List<String> speedAllowableValuesList = new ArrayList<>(Arrays.asList(speedAllowableValues.split(", ")));
        List<String> speedAllowableValuesExpectedList = List.of("SPEED_10M", "SPEED_100M", "SPEED_1G", "SPEED_2G", "SPEED_4G", "SPEED_8G", "SPEED_16G", "SPEED_25G", "SPEED_10G", "SPEED_32G", "SPEED_40G", "SPEED_50G", "SPEED_100G", "SPEED_UNKNOWN");
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(effectiveCapacityMbpsType, NUMBER_TYPE);
        softAssert.assertEquals(effectiveCapacityUnitType, STRING_TYPE);
        softAssert.assertEquals(effectiveCapacityUnitAllowableValues, "bps, Kbps, Mbps, Gbps, Tbps");
        softAssert.assertEquals(isTrunkType, BOOLEAN_TYPE);
        softAssert.assertEquals(latencyType, NUMBER_TYPE);
        softAssert.assertEquals(roleType, STRING_TYPE);
        softAssert.assertEquals(roleAllowableValues, "UNI, NNI, E_NNI");
        softAssert.assertEquals(speedType, STRING_TYPE);
        softAssert.assertTrue(speedAllowableValuesList.containsAll(speedAllowableValuesExpectedList) && speedAllowableValuesExpectedList.containsAll(speedAllowableValuesList), String.format(ERROR_MESSAGE_INFO, speedAllowableValues));
        softAssert.assertAll();
    }

    private void assertCharacteristicTabAttributesForELine() {
        ResourceSpecificationsViewPage resourceSpecificationsViewPage = ResourceSpecificationsViewPage.create(driver, webDriverWait);
        OldTable oldTable = resourceSpecificationsViewPage.getCharacteristicsAttributesOldTable();
        oldTable.setPageSize(50);

        int rowNumber = oldTable.getRowNumber("VLANID", CHARACTERISTIC_NAME_COLUMN);
        String vlanIDType = oldTable.getCellValue(rowNumber, CHARACTERISTIC_TYPE_COLUMN);
        rowNumber = oldTable.getRowNumber("ServiceProvider", CHARACTERISTIC_NAME_COLUMN);
        String serviceProviderType = oldTable.getCellValue(rowNumber, CHARACTERISTIC_TYPE_COLUMN);
        rowNumber = oldTable.getRowNumber("ServiceCreationDate", CHARACTERISTIC_NAME_COLUMN);
        String serviceCreationDate = oldTable.getCellValue(rowNumber, CHARACTERISTIC_TYPE_COLUMN);
        rowNumber = oldTable.getRowNumber("Remarks", CHARACTERISTIC_NAME_COLUMN);
        String remarksType = oldTable.getCellValue(rowNumber, CHARACTERISTIC_TYPE_COLUMN);

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(vlanIDType, NUMBER_TYPE);
        softAssert.assertEquals(serviceProviderType, STRING_TYPE);
        softAssert.assertEquals(serviceCreationDate, DATE_TYPE);
        softAssert.assertEquals(remarksType, STRING_TYPE);
        softAssert.assertAll();
    }

    private void assertCharacteristicTabAttributesForVN() {
        ResourceSpecificationsViewPage resourceSpecificationsViewPage = ResourceSpecificationsViewPage.create(driver, webDriverWait);
        OldTable oldTable = resourceSpecificationsViewPage.getCharacteristicsAttributesOldTable();
        oldTable.setPageSize(50);

        int rowNumber = oldTable.getRowNumber("NetworkType", CHARACTERISTIC_NAME_COLUMN);
        String networkTypeType = oldTable.getCellValue(rowNumber, CHARACTERISTIC_TYPE_COLUMN);
        String networkTypeMandatory = oldTable.getCellValue(rowNumber, CHARACTERISTIC_MANDATORY_COLUMN);
        String networkTypeDefaultValue = oldTable.getCellValue(rowNumber, CHARACTERISTIC_DEFAULT_VALUE_COLUMN);
        String networkTypeAllowableValues = oldTable.getCellValue(rowNumber, CHARACTERISTIC_ALLOWABLE_VALUES_COLUMN);
        rowNumber = oldTable.getRowNumber("PhysicalNetwork", CHARACTERISTIC_NAME_COLUMN);
        String physicalNetworkType = oldTable.getCellValue(rowNumber, CHARACTERISTIC_TYPE_COLUMN);
        rowNumber = oldTable.getRowNumber("SegmentationID", CHARACTERISTIC_NAME_COLUMN);
        String segmentationIDType = oldTable.getCellValue(rowNumber, CHARACTERISTIC_TYPE_COLUMN);

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(networkTypeType, STRING_TYPE);
        softAssert.assertEquals(networkTypeMandatory, "True");
        softAssert.assertEquals(networkTypeDefaultValue, "Flat");
        softAssert.assertEquals(networkTypeAllowableValues, "Flat, VLAN, VxLAN, GRE");
        softAssert.assertEquals(physicalNetworkType, STRING_TYPE);
        softAssert.assertEquals(segmentationIDType, NUMBER_TYPE);
        softAssert.assertAll();
    }

    private void assertCharacteristicTabAttributesForVLAN() {
        ResourceSpecificationsViewPage resourceSpecificationsViewPage = ResourceSpecificationsViewPage.create(driver, webDriverWait);
        OldTable oldTable = resourceSpecificationsViewPage.getCharacteristicsAttributesOldTable();
        oldTable.setPageSize(50);

        int rowNumber = oldTable.getRowNumber("NetworkRole", CHARACTERISTIC_NAME_COLUMN);
        String networkRoleType = oldTable.getCellValue(rowNumber, CHARACTERISTIC_TYPE_COLUMN);
        String networkRoleAllowableValues = oldTable.getCellValue(rowNumber, CHARACTERISTIC_ALLOWABLE_VALUES_COLUMN);
        rowNumber = oldTable.getRowNumber("ServiceType", CHARACTERISTIC_NAME_COLUMN);
        String serviceTypeType = oldTable.getCellValue(rowNumber, CHARACTERISTIC_TYPE_COLUMN);
        rowNumber = oldTable.getRowNumber("VLANID", CHARACTERISTIC_NAME_COLUMN);
        String VLANIDType = oldTable.getCellValue(rowNumber, CHARACTERISTIC_TYPE_COLUMN);
        rowNumber = oldTable.getRowNumber("vlanType", CHARACTERISTIC_NAME_COLUMN);
        String vlanTypeType = oldTable.getCellValue(rowNumber, CHARACTERISTIC_TYPE_COLUMN);
        String vlanTypeAllowableValues = oldTable.getCellValue(rowNumber, CHARACTERISTIC_ALLOWABLE_VALUES_COLUMN);

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(networkRoleType, STRING_TYPE);
        softAssert.assertEquals(networkRoleAllowableValues, "Access, AggregationDedicated, AggregationShared, Core, Customer, Unset");
        softAssert.assertEquals(serviceTypeType, STRING_TYPE);
        softAssert.assertEquals(VLANIDType, NUMBER_TYPE);
        softAssert.assertEquals(vlanTypeType, STRING_TYPE);
        softAssert.assertEquals(vlanTypeAllowableValues, "UVLAN, SVLAN, CVLAN, Undefined");
        softAssert.assertAll();
    }

    private void assertCharacteristicTabAttributesForAEI() {
        ResourceSpecificationsViewPage resourceSpecificationsViewPage = ResourceSpecificationsViewPage.create(driver, webDriverWait);
        OldTable oldTable = resourceSpecificationsViewPage.getCharacteristicsAttributesOldTable();
        oldTable.setPageSize(50);

        int rowNumber = oldTable.getRowNumber("administrativeState", CHARACTERISTIC_NAME_COLUMN);
        String administrativeStateType = oldTable.getCellValue(rowNumber, CHARACTERISTIC_TYPE_COLUMN);
        String administrativeStateAllowableValues = oldTable.getCellValue(rowNumber, CHARACTERISTIC_ALLOWABLE_VALUES_COLUMN);
        rowNumber = oldTable.getRowNumber("aggregationProtocol", CHARACTERISTIC_NAME_COLUMN);
        String aggregationProtocolType = oldTable.getCellValue(rowNumber, CHARACTERISTIC_TYPE_COLUMN);
        String aggregationProtocolAllowableValues = oldTable.getCellValue(rowNumber, CHARACTERISTIC_ALLOWABLE_VALUES_COLUMN);
        rowNumber = oldTable.getRowNumber("encapsulation", CHARACTERISTIC_NAME_COLUMN);
        String encapsulationType = oldTable.getCellValue(rowNumber, CHARACTERISTIC_TYPE_COLUMN);
        String encapsulationAllowableValues = oldTable.getCellValue(rowNumber, CHARACTERISTIC_ALLOWABLE_VALUES_COLUMN);
        rowNumber = oldTable.getRowNumber("lacpMode", CHARACTERISTIC_NAME_COLUMN);
        String lacpModeType = oldTable.getCellValue(rowNumber, CHARACTERISTIC_TYPE_COLUMN);
        String lacpModeAllowableValues = oldTable.getCellValue(rowNumber, CHARACTERISTIC_ALLOWABLE_VALUES_COLUMN);
        rowNumber = oldTable.getRowNumber("lacpShortPeriod", CHARACTERISTIC_NAME_COLUMN);
        String lacpShortPeriodType = oldTable.getCellValue(rowNumber, CHARACTERISTIC_TYPE_COLUMN);
        rowNumber = oldTable.getRowNumber("minimumBandwidth", CHARACTERISTIC_NAME_COLUMN);
        String minimumBandwidthType = oldTable.getCellValue(rowNumber, CHARACTERISTIC_TYPE_COLUMN);
        rowNumber = oldTable.getRowNumber("minimumLinks", CHARACTERISTIC_NAME_COLUMN);
        String minimumLinksType = oldTable.getCellValue(rowNumber, CHARACTERISTIC_TYPE_COLUMN);
        rowNumber = oldTable.getRowNumber("mtu", CHARACTERISTIC_NAME_COLUMN);
        String mtuType = oldTable.getCellValue(rowNumber, CHARACTERISTIC_TYPE_COLUMN);
        rowNumber = oldTable.getRowNumber("operationalState", CHARACTERISTIC_NAME_COLUMN);
        String operationalStateType = oldTable.getCellValue(rowNumber, CHARACTERISTIC_TYPE_COLUMN);
        String operationalStateAllowableValues = oldTable.getCellValue(rowNumber, CHARACTERISTIC_ALLOWABLE_VALUES_COLUMN);
        rowNumber = oldTable.getRowNumber("physicalAddress", CHARACTERISTIC_NAME_COLUMN);
        String physicalAddressType = oldTable.getCellValue(rowNumber, CHARACTERISTIC_TYPE_COLUMN);

        List<String> administrativeStateAllowableValuesList = new ArrayList<>(Arrays.asList(administrativeStateAllowableValues.split(", ")));
        List<String> administrativeStateAllowableValuesExpectedList = List.of("DISABLED", "TESTING", "ENABLED", "ACTIVE", "DOWN", "INACTIVE", "MAINTENANCE", "UNKNOWN", "UP");
        List<String> operationalStateAllowableValuesList = new ArrayList<>(Arrays.asList(operationalStateAllowableValues.split(", ")));
        List<String> operationalStateAllowableValuesExpectedList = List.of("DISABLED", "PARTIALLY_DOWN", "ENABLED", "FAILED", "DOWN", "DEGRADED", "UNSPECIFIED", "UNKNOWN", "TRANSITION", "UP");

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(administrativeStateType, STRING_TYPE);
        softAssert.assertTrue(administrativeStateAllowableValuesList.containsAll(administrativeStateAllowableValuesExpectedList) && administrativeStateAllowableValuesExpectedList.containsAll(administrativeStateAllowableValuesList), String.format(ERROR_MESSAGE_INFO, administrativeStateAllowableValuesList));
        softAssert.assertEquals(aggregationProtocolType, STRING_TYPE);
        softAssert.assertEquals(aggregationProtocolAllowableValues, "LACP, NONE");
        softAssert.assertEquals(encapsulationType, STRING_TYPE);
        softAssert.assertEquals(encapsulationAllowableValues, "ENCAPSULATION_ETH, ENCAPSULATION_802_1Q, ENCAPSULATION_QINQ");
        softAssert.assertEquals(lacpModeType, STRING_TYPE);
        softAssert.assertEquals(lacpModeAllowableValues, "ACTIVE, PASSIVE");
        softAssert.assertEquals(lacpShortPeriodType, BOOLEAN_TYPE);
        softAssert.assertEquals(minimumBandwidthType, NUMBER_TYPE);
        softAssert.assertEquals(minimumLinksType, NUMBER_TYPE);
        softAssert.assertEquals(mtuType, NUMBER_TYPE);
        softAssert.assertEquals(operationalStateType, STRING_TYPE);
        softAssert.assertTrue(operationalStateAllowableValuesList.containsAll(operationalStateAllowableValuesExpectedList) && operationalStateAllowableValuesExpectedList.containsAll(operationalStateAllowableValuesList), String.format(ERROR_MESSAGE_INFO, operationalStateAllowableValuesList));
        softAssert.assertEquals(physicalAddressType, STRING_TYPE);
        softAssert.assertAll();
    }

    private void assertCharacteristicTabAttributesForEI() {
        ResourceSpecificationsViewPage resourceSpecificationsViewPage = ResourceSpecificationsViewPage.create(driver, webDriverWait);
        OldTable oldTable = resourceSpecificationsViewPage.getCharacteristicsAttributesOldTable();
        oldTable.setPageSize(50);

        int rowNumber = oldTable.getRowNumber("accessFunction", CHARACTERISTIC_NAME_COLUMN);
        String accessFunctionType = oldTable.getCellValue(rowNumber, CHARACTERISTIC_TYPE_COLUMN);
        String accessFunctionAllowableValues = oldTable.getCellValue(rowNumber, CHARACTERISTIC_ALLOWABLE_VALUES_COLUMN);
        rowNumber = oldTable.getRowNumber("administrativeDuplexMode", CHARACTERISTIC_NAME_COLUMN);
        String administrativeDuplexModeType = oldTable.getCellValue(rowNumber, CHARACTERISTIC_TYPE_COLUMN);
        String administrativeDuplexModeAllowableValues = oldTable.getCellValue(rowNumber, CHARACTERISTIC_ALLOWABLE_VALUES_COLUMN);
        rowNumber = oldTable.getRowNumber("administrativeSpeed", CHARACTERISTIC_NAME_COLUMN);
        String administrativeSpeedType = oldTable.getCellValue(rowNumber, CHARACTERISTIC_TYPE_COLUMN);
        String administrativeSpeedAllowableValues = oldTable.getCellValue(rowNumber, CHARACTERISTIC_ALLOWABLE_VALUES_COLUMN);
        rowNumber = oldTable.getRowNumber("administrativeState", CHARACTERISTIC_NAME_COLUMN);
        String administrativeStateType = oldTable.getCellValue(rowNumber, CHARACTERISTIC_TYPE_COLUMN);
        String administrativeStateAllowableValues = oldTable.getCellValue(rowNumber, CHARACTERISTIC_ALLOWABLE_VALUES_COLUMN);
        rowNumber = oldTable.getRowNumber("autoNegotiation", CHARACTERISTIC_NAME_COLUMN);
        String autoNegotiationType = oldTable.getCellValue(rowNumber, CHARACTERISTIC_TYPE_COLUMN);
        rowNumber = oldTable.getRowNumber("bandwidth", CHARACTERISTIC_NAME_COLUMN);
        String bandwidthType = oldTable.getCellValue(rowNumber, CHARACTERISTIC_TYPE_COLUMN);
        rowNumber = oldTable.getRowNumber("encapsulation", CHARACTERISTIC_NAME_COLUMN);
        String encapsulationType = oldTable.getCellValue(rowNumber, CHARACTERISTIC_TYPE_COLUMN);
        String encapsulationAllowableValues = oldTable.getCellValue(rowNumber, CHARACTERISTIC_ALLOWABLE_VALUES_COLUMN);
        rowNumber = oldTable.getRowNumber("flowControl", CHARACTERISTIC_NAME_COLUMN);
        String flowControlType = oldTable.getCellValue(rowNumber, CHARACTERISTIC_TYPE_COLUMN);
        String flowControlAllowableValues = oldTable.getCellValue(rowNumber, CHARACTERISTIC_ALLOWABLE_VALUES_COLUMN);
        rowNumber = oldTable.getRowNumber("maximumFrameSize", CHARACTERISTIC_NAME_COLUMN);
        String maximumFrameSizeType = oldTable.getCellValue(rowNumber, CHARACTERISTIC_TYPE_COLUMN);
        rowNumber = oldTable.getRowNumber("mtu", CHARACTERISTIC_NAME_COLUMN);
        String mtuType = oldTable.getCellValue(rowNumber, CHARACTERISTIC_TYPE_COLUMN);
        rowNumber = oldTable.getRowNumber("operationalDuplexMode", CHARACTERISTIC_NAME_COLUMN);
        String operationalDuplexModeType = oldTable.getCellValue(rowNumber, CHARACTERISTIC_TYPE_COLUMN);
        String operationalDuplexModeAllowableValues = oldTable.getCellValue(rowNumber, CHARACTERISTIC_ALLOWABLE_VALUES_COLUMN);
        rowNumber = oldTable.getRowNumber("operationalSpeed", CHARACTERISTIC_NAME_COLUMN);
        String operationalSpeedType = oldTable.getCellValue(rowNumber, CHARACTERISTIC_TYPE_COLUMN);
        String operationalSpeedAllowableValues = oldTable.getCellValue(rowNumber, CHARACTERISTIC_ALLOWABLE_VALUES_COLUMN);
        rowNumber = oldTable.getRowNumber("operationalState", CHARACTERISTIC_NAME_COLUMN);
        String operationalStateType = oldTable.getCellValue(rowNumber, CHARACTERISTIC_TYPE_COLUMN);
        String operationalStateAllowableValues = oldTable.getCellValue(rowNumber, CHARACTERISTIC_ALLOWABLE_VALUES_COLUMN);
        rowNumber = oldTable.getRowNumber("physicalAddress", CHARACTERISTIC_NAME_COLUMN);
        String physicalAddressType = oldTable.getCellValue(rowNumber, CHARACTERISTIC_TYPE_COLUMN);
        rowNumber = oldTable.getRowNumber("role", CHARACTERISTIC_NAME_COLUMN);
        String roleType = oldTable.getCellValue(rowNumber, CHARACTERISTIC_TYPE_COLUMN);
        String roleAllowableValues = oldTable.getCellValue(rowNumber, CHARACTERISTIC_ALLOWABLE_VALUES_COLUMN);
        rowNumber = oldTable.getRowNumber("switchport", CHARACTERISTIC_NAME_COLUMN);
        String switchportType = oldTable.getCellValue(rowNumber, CHARACTERISTIC_TYPE_COLUMN);
        String switchportAllowableValues = oldTable.getCellValue(rowNumber, CHARACTERISTIC_ALLOWABLE_VALUES_COLUMN);
        rowNumber = oldTable.getRowNumber("switchportMode", CHARACTERISTIC_NAME_COLUMN);
        String switchportModeType = oldTable.getCellValue(rowNumber, CHARACTERISTIC_TYPE_COLUMN);
        String switchportModeAllowableValues = oldTable.getCellValue(rowNumber, CHARACTERISTIC_ALLOWABLE_VALUES_COLUMN);
        rowNumber = oldTable.getRowNumber("usageState", CHARACTERISTIC_NAME_COLUMN);
        String usageStateType = oldTable.getCellValue(rowNumber, CHARACTERISTIC_TYPE_COLUMN);
        String usageStateAllowableValues = oldTable.getCellValue(rowNumber, CHARACTERISTIC_ALLOWABLE_VALUES_COLUMN);

        List<String> administrativeSpeedAllowableValuesList = new ArrayList<>(Arrays.asList(administrativeSpeedAllowableValues.split(", ")));
        List<String> administrativeSpeedAllowableValuesExpectedList = List.of("Speed10M", "Speed100M", "Speed1G", "Speed2G", "Speed4G", "Speed8G", "Speed10G", "Speed16G", "Speed25G", "Speed32G", "Speed40G", "Speed50G", "Speed100G");
        List<String> administrativeStateAllowableValuesList = new ArrayList<>(Arrays.asList(administrativeStateAllowableValues.split(", ")));
        List<String> administrativeStateAllowableValuesExpectedList = List.of("Active", "Disabled", "Down", "Enabled", "Inactive", "Maintenance", "Testing", "Unknown", "Up");
        List<String> operationalSpeedAllowableValuesList = new ArrayList<>(Arrays.asList(operationalSpeedAllowableValues.split(", ")));
        List<String> operationalSpeedAllowableValuesExpectedList = List.of("Speed10M", "Speed100M", "Speed1G", "Speed2G", "Speed10G", "Speed25G", "Speed40G", "Speed50G", "Speed100G");
        List<String> operationalStateAllowableValuesList = new ArrayList<>(Arrays.asList(operationalStateAllowableValues.split(", ")));
        List<String> operationalStateAllowableValuesExpectedList = List.of("Enabled", "Degraded", "Disabled", "Down", "Failed", "PartiallyDown", "Transition", "Unknown", "Unspecified", "Up");

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(accessFunctionType, STRING_TYPE);
        softAssert.assertEquals(accessFunctionAllowableValues, "Uplink, Downlink");
        softAssert.assertEquals(administrativeDuplexModeType, STRING_TYPE);
        softAssert.assertEquals(administrativeDuplexModeAllowableValues, "HalfDuplex, FullDuplex");
        softAssert.assertEquals(administrativeSpeedType, STRING_TYPE);
        softAssert.assertTrue(administrativeSpeedAllowableValuesList.containsAll(administrativeSpeedAllowableValuesExpectedList) && administrativeSpeedAllowableValuesExpectedList.containsAll(administrativeSpeedAllowableValuesList), String.format(ERROR_MESSAGE_INFO, administrativeSpeedAllowableValuesExpectedList));
        softAssert.assertEquals(administrativeStateType, STRING_TYPE);
        softAssert.assertTrue(administrativeStateAllowableValuesList.containsAll(administrativeStateAllowableValuesExpectedList) && administrativeStateAllowableValuesExpectedList.containsAll(administrativeStateAllowableValuesList), String.format(ERROR_MESSAGE_INFO, administrativeStateAllowableValuesExpectedList));
        softAssert.assertEquals(autoNegotiationType, STRING_TYPE);
        softAssert.assertEquals(bandwidthType, NUMBER_TYPE);
        softAssert.assertEquals(encapsulationType, STRING_TYPE);
        softAssert.assertEquals(encapsulationAllowableValues, "EncapsulationETH, Encapsulation802_1Q, EncapsulationQinQ");
        softAssert.assertEquals(flowControlType, STRING_TYPE);
        softAssert.assertEquals(flowControlAllowableValues, "None, Bidirectional, Ingress, Egress");
        softAssert.assertEquals(maximumFrameSizeType, NUMBER_TYPE);
        softAssert.assertEquals(mtuType, NUMBER_TYPE);
        softAssert.assertEquals(operationalDuplexModeType, STRING_TYPE);
        softAssert.assertEquals(operationalDuplexModeAllowableValues, "HalfDuplex, FullDuplex");
        softAssert.assertEquals(operationalSpeedType, STRING_TYPE);
        softAssert.assertTrue(operationalSpeedAllowableValuesList.containsAll(operationalSpeedAllowableValuesExpectedList) && operationalSpeedAllowableValuesExpectedList.containsAll(operationalSpeedAllowableValuesList), String.format(ERROR_MESSAGE_INFO, operationalSpeedAllowableValuesExpectedList));
        softAssert.assertEquals(operationalStateType, STRING_TYPE);
        softAssert.assertTrue(operationalStateAllowableValuesList.containsAll(operationalStateAllowableValuesExpectedList) && operationalStateAllowableValuesExpectedList.containsAll(operationalStateAllowableValuesList), String.format(ERROR_MESSAGE_INFO, operationalStateAllowableValuesExpectedList));
        softAssert.assertEquals(physicalAddressType, STRING_TYPE);
        softAssert.assertEquals(roleType, STRING_TYPE);
        softAssert.assertEquals(roleAllowableValues, "UNI, NNI, E_NNI");
        softAssert.assertEquals(switchportType, STRING_TYPE);
        softAssert.assertEquals(switchportAllowableValues, "Yes, No, Undefined");
        softAssert.assertEquals(switchportModeType, STRING_TYPE);
        softAssert.assertEquals(switchportModeAllowableValues, "Access, Trunk, QinQTunnel");
        softAssert.assertEquals(usageStateType, STRING_TYPE);
        softAssert.assertEquals(usageStateAllowableValues, "Reserved, Assigned, Disabled, Free");
        softAssert.assertAll();
    }

    private void assertCharacteristicTabAttributesForIPNE() {
        ResourceSpecificationsViewPage resourceSpecificationsViewPage = ResourceSpecificationsViewPage.create(driver, webDriverWait);
        OldTable oldTable = resourceSpecificationsViewPage.getCharacteristicsAttributesOldTable();
        oldTable.setPageSize(50);

        int rowNumber = oldTable.getRowNumber("neRole", CHARACTERISTIC_NAME_COLUMN);
        String neRoleType = oldTable.getCellValue(rowNumber, CHARACTERISTIC_TYPE_COLUMN);
        String neRoleAllowableValues = oldTable.getCellValue(rowNumber, CHARACTERISTIC_ALLOWABLE_VALUES_COLUMN);
        rowNumber = oldTable.getRowNumber("supportedLayers", CHARACTERISTIC_NAME_COLUMN);
        String supportedLayersType = oldTable.getCellValue(rowNumber, CHARACTERISTIC_TYPE_COLUMN);
        String supportedLayersAllowableValues = oldTable.getCellValue(rowNumber, CHARACTERISTIC_ALLOWABLE_VALUES_COLUMN);

        List<String> supportedLayersAllowableValuesList = new ArrayList<>(Arrays.asList(supportedLayersAllowableValues.split(", ")));
        List<String> supportedLayersAllowableValuesExpectedList = List.of("ETHERNET", "VLAN", "IP", "MPLS", "NFV/DC", "INFRASTRUCTURE", "Generic", "E5", "E4", "E3", "E2", "E1", "E0", "T3", "T1", "E0XV", "E1XV", "T1XV", "VT15", "VT15XV", "STS1", "STS1XV", "STS3", "STS12", "STS48", "STS192", "OC1", "OC3", "OC12", "OC48", "OC192", "VPLS", "PWE3", "VC3", "VC4", "VC12", "STM", "STM1", "STM16", "STM4", "STM64");

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(neRoleType, STRING_TYPE);
        softAssert.assertEquals(neRoleAllowableValues, "Leaf, external_Leaf, Spine, Unknown");
        softAssert.assertEquals(supportedLayersType, STRING_TYPE);
        softAssert.assertTrue(supportedLayersAllowableValuesList.containsAll(supportedLayersAllowableValuesExpectedList) && supportedLayersAllowableValuesExpectedList.containsAll(supportedLayersAllowableValuesList), String.format(ERROR_MESSAGE_INFO, supportedLayersAllowableValues));
        softAssert.assertAll();
    }

    private void assertCharacteristicTabAttributesForIRB() {
        ResourceSpecificationsViewPage resourceSpecificationsViewPage = ResourceSpecificationsViewPage.create(driver, webDriverWait);
        OldTable oldTable = resourceSpecificationsViewPage.getCharacteristicsAttributesOldTable();
        oldTable.setPageSize(50);

        int rowNumber = oldTable.getRowNumber("vlanId", CHARACTERISTIC_NAME_COLUMN);
        String vlanIdType = oldTable.getCellValue(rowNumber, CHARACTERISTIC_TYPE_COLUMN);
        rowNumber = oldTable.getRowNumber("mtu", CHARACTERISTIC_NAME_COLUMN);
        String mtuType = oldTable.getCellValue(rowNumber, CHARACTERISTIC_TYPE_COLUMN);
        rowNumber = oldTable.getRowNumber("administrativeState", CHARACTERISTIC_NAME_COLUMN);
        String administrativeStateType = oldTable.getCellValue(rowNumber, CHARACTERISTIC_TYPE_COLUMN);
        String administrativeStateDefaultValue = oldTable.getCellValue(rowNumber, CHARACTERISTIC_DEFAULT_VALUE_COLUMN);
        String administrativeStateAllowableValues = oldTable.getCellValue(rowNumber, CHARACTERISTIC_ALLOWABLE_VALUES_COLUMN);
        rowNumber = oldTable.getRowNumber("operationalState", CHARACTERISTIC_NAME_COLUMN);
        String operationalStateType = oldTable.getCellValue(rowNumber, CHARACTERISTIC_TYPE_COLUMN);
        String operationalStateDefaultValue = oldTable.getCellValue(rowNumber, CHARACTERISTIC_DEFAULT_VALUE_COLUMN);
        String operationalStateAllowableValues = oldTable.getCellValue(rowNumber, CHARACTERISTIC_ALLOWABLE_VALUES_COLUMN);

        List<String> administrativeStateAllowableValuesList = new ArrayList<>(Arrays.asList(administrativeStateAllowableValues.split(", ")));
        List<String> administrativeStateAllowableValuesExpectedList = List.of("Active", "Disabled", "Down", "Enabled", "Inactive", "Maintenance", "Testing", "Unknown", "Up");
        List<String> operationalStateAllowableValuesList = new ArrayList<>(Arrays.asList(operationalStateAllowableValues.split(", ")));
        List<String> operationalStateAllowableValuesExpectedList = List.of("Enabled", "Degraded", "Disabled", "Down", "Failed", "PartiallyDown", "Transition", "Unknown", "Unspecified", "Up");

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(vlanIdType, NUMBER_TYPE);
        softAssert.assertEquals(mtuType, NUMBER_TYPE);
        softAssert.assertEquals(administrativeStateType, STRING_TYPE);
        softAssert.assertEquals(administrativeStateDefaultValue, "Active");
        softAssert.assertTrue(administrativeStateAllowableValuesList.containsAll(administrativeStateAllowableValuesExpectedList) && administrativeStateAllowableValuesExpectedList.containsAll(administrativeStateAllowableValuesList), String.format(ERROR_MESSAGE_INFO, administrativeStateAllowableValuesExpectedList));
        softAssert.assertEquals(operationalStateType, STRING_TYPE);
        softAssert.assertEquals(operationalStateDefaultValue, "Enabled");
        softAssert.assertTrue(operationalStateAllowableValuesList.containsAll(operationalStateAllowableValuesExpectedList) && operationalStateAllowableValuesExpectedList.containsAll(operationalStateAllowableValuesList), String.format(ERROR_MESSAGE_INFO, operationalStateAllowableValues));

        softAssert.assertAll();
    }

    private void assertCharacteristicTabAttributesForLoopback() {
        ResourceSpecificationsViewPage resourceSpecificationsViewPage = ResourceSpecificationsViewPage.create(driver, webDriverWait);
        OldTable oldTable = resourceSpecificationsViewPage.getCharacteristicsAttributesOldTable();
        oldTable.setPageSize(50);

        int rowNumber = oldTable.getRowNumber("administrativeState", CHARACTERISTIC_NAME_COLUMN);
        String administrativeStateType = oldTable.getCellValue(rowNumber, CHARACTERISTIC_TYPE_COLUMN);
        String administrativeStateDefaultValue = oldTable.getCellValue(rowNumber, CHARACTERISTIC_DEFAULT_VALUE_COLUMN);
        String administrativeStateAllowableValues = oldTable.getCellValue(rowNumber, CHARACTERISTIC_ALLOWABLE_VALUES_COLUMN);
        rowNumber = oldTable.getRowNumber("operationalState", CHARACTERISTIC_NAME_COLUMN);
        String operationalStateType = oldTable.getCellValue(rowNumber, CHARACTERISTIC_TYPE_COLUMN);
        String operationalStateDefaultValue = oldTable.getCellValue(rowNumber, CHARACTERISTIC_DEFAULT_VALUE_COLUMN);
        String operationalStateAllowableValues = oldTable.getCellValue(rowNumber, CHARACTERISTIC_ALLOWABLE_VALUES_COLUMN);

        List<String> administrativeStateAllowableValuesList = new ArrayList<>(Arrays.asList(administrativeStateAllowableValues.split(", ")));
        List<String> administrativeStateAllowableValuesExpectedList = List.of("Active", "Disabled", "Down", "Enabled", "Inactive", "Maintenance", "Testing", "Unknown", "Up");
        List<String> operationalStateAllowableValuesList = new ArrayList<>(Arrays.asList(operationalStateAllowableValues.split(", ")));
        List<String> operationalStateAllowableValuesExpectedList = List.of("Enabled", "Degraded", "Disabled", "Down", "Failed", "PartiallyDown", "Transition", "Unknown", "Unspecified", "Up");

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(administrativeStateType, STRING_TYPE);
        softAssert.assertEquals(administrativeStateDefaultValue, "Active");
        softAssert.assertTrue(administrativeStateAllowableValuesList.containsAll(administrativeStateAllowableValuesExpectedList) && administrativeStateAllowableValuesExpectedList.containsAll(administrativeStateAllowableValuesList), String.format(ERROR_MESSAGE_INFO, administrativeStateAllowableValues));
        softAssert.assertEquals(operationalStateType, STRING_TYPE);
        softAssert.assertEquals(operationalStateDefaultValue, "Enabled");
        softAssert.assertTrue(operationalStateAllowableValuesList.containsAll(operationalStateAllowableValuesExpectedList) && operationalStateAllowableValuesExpectedList.containsAll(operationalStateAllowableValuesList), String.format(ERROR_MESSAGE_INFO, operationalStateAllowableValues));
        softAssert.assertAll();
    }

    private void assertCharacteristicTabAttributesForVLANInterface() {
        ResourceSpecificationsViewPage resourceSpecificationsViewPage = ResourceSpecificationsViewPage.create(driver, webDriverWait);
        OldTable oldTable = resourceSpecificationsViewPage.getCharacteristicsAttributesOldTable();
        oldTable.setPageSize(50);

        int rowNumber = oldTable.getRowNumber("administrativeState", CHARACTERISTIC_NAME_COLUMN);
        String administrativeStateType = oldTable.getCellValue(rowNumber, CHARACTERISTIC_TYPE_COLUMN);
        String administrativeStateAllowableValues = oldTable.getCellValue(rowNumber, CHARACTERISTIC_ALLOWABLE_VALUES_COLUMN);
        rowNumber = oldTable.getRowNumber("bandwidth", CHARACTERISTIC_NAME_COLUMN);
        String bandwidthType = oldTable.getCellValue(rowNumber, CHARACTERISTIC_TYPE_COLUMN);
        rowNumber = oldTable.getRowNumber("innerVlanId", CHARACTERISTIC_NAME_COLUMN);
        String innerVlanIdType = oldTable.getCellValue(rowNumber, CHARACTERISTIC_TYPE_COLUMN);
        rowNumber = oldTable.getRowNumber("interfaceRole", CHARACTERISTIC_NAME_COLUMN);
        String interfaceRoleType = oldTable.getCellValue(rowNumber, CHARACTERISTIC_TYPE_COLUMN);
        String interfaceRoleAllowableValues = oldTable.getCellValue(rowNumber, CHARACTERISTIC_ALLOWABLE_VALUES_COLUMN);
        rowNumber = oldTable.getRowNumber("mtu", CHARACTERISTIC_NAME_COLUMN);
        String mtuType = oldTable.getCellValue(rowNumber, CHARACTERISTIC_TYPE_COLUMN);
        rowNumber = oldTable.getRowNumber("nativeVlanId", CHARACTERISTIC_NAME_COLUMN);
        String nativeVlanIdType = oldTable.getCellValue(rowNumber, CHARACTERISTIC_TYPE_COLUMN);
        rowNumber = oldTable.getRowNumber("operationalState", CHARACTERISTIC_NAME_COLUMN);
        String operationalStateType = oldTable.getCellValue(rowNumber, CHARACTERISTIC_TYPE_COLUMN);
        String operationalStateAllowableValues = oldTable.getCellValue(rowNumber, CHARACTERISTIC_ALLOWABLE_VALUES_COLUMN);
        rowNumber = oldTable.getRowNumber("outerVlanId", CHARACTERISTIC_NAME_COLUMN);
        String outerVlanIdType = oldTable.getCellValue(rowNumber, CHARACTERISTIC_TYPE_COLUMN);
        rowNumber = oldTable.getRowNumber("subinterfaceId", CHARACTERISTIC_NAME_COLUMN);
        String subinterfaceIdType = oldTable.getCellValue(rowNumber, CHARACTERISTIC_TYPE_COLUMN);
        rowNumber = oldTable.getRowNumber("vlanInterfaceType", CHARACTERISTIC_NAME_COLUMN);
        String vlanInterfaceTypeType = oldTable.getCellValue(rowNumber, CHARACTERISTIC_TYPE_COLUMN);
        String vlanInterfaceTypeAllowableValues = oldTable.getCellValue(rowNumber, CHARACTERISTIC_ALLOWABLE_VALUES_COLUMN);

        List<String> administrativeStateAllowableValuesList = new ArrayList<>(Arrays.asList(administrativeStateAllowableValues.split(", ")));
        List<String> administrativeStateAllowableValuesExpectedList = List.of("ACTIVE", "DISABLED", "DOWN", "ENABLED", "INACTIVE", "MAINTENANCE", "TESTING", "UNKNOWN", "UP");
        List<String> operationalStateAllowableValuesList = new ArrayList<>(Arrays.asList(operationalStateAllowableValues.split(", ")));
        List<String> operationalStateAllowableValuesExpectedList = List.of("ENABLED", "DEGRADED", "DISABLED", "DOWN", "FAILED", "PARTIALLY_DOWN", "TRANSITION", "UNKNOWN", "UNSPECIFIED", "UP");
        List<String> vlanInterfaceTypeAllowableValuesList = new ArrayList<>(Arrays.asList(vlanInterfaceTypeAllowableValues.split(", ")));
        List<String> vlanInterfaceTypeAllowableValuesExpectedList = List.of("Subinterface", "Trunk", "Access", "Undefined");

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(administrativeStateType, STRING_TYPE);
        softAssert.assertTrue(administrativeStateAllowableValuesList.containsAll(administrativeStateAllowableValuesExpectedList) && administrativeStateAllowableValuesExpectedList.containsAll(administrativeStateAllowableValuesList), String.format(ERROR_MESSAGE_INFO, administrativeStateAllowableValues));
        softAssert.assertEquals(bandwidthType, NUMBER_TYPE);
        softAssert.assertEquals(innerVlanIdType, STRING_TYPE);
        softAssert.assertEquals(interfaceRoleType, STRING_TYPE);
        softAssert.assertEquals(interfaceRoleAllowableValues, "U_Plane, C_Plane, M_Plane, S_Plane");
        softAssert.assertEquals(mtuType, NUMBER_TYPE);
        softAssert.assertEquals(nativeVlanIdType, NUMBER_TYPE);
        softAssert.assertEquals(operationalStateType, STRING_TYPE);
        softAssert.assertTrue(operationalStateAllowableValuesList.containsAll(operationalStateAllowableValuesExpectedList) && operationalStateAllowableValuesExpectedList.containsAll(operationalStateAllowableValuesList), String.format(ERROR_MESSAGE_INFO, operationalStateAllowableValues));
        softAssert.assertEquals(outerVlanIdType, STRING_TYPE);
        softAssert.assertEquals(subinterfaceIdType, NUMBER_TYPE);
        softAssert.assertEquals(vlanInterfaceTypeType, STRING_TYPE);
        softAssert.assertTrue(vlanInterfaceTypeAllowableValuesList.containsAll(vlanInterfaceTypeAllowableValuesExpectedList) && vlanInterfaceTypeAllowableValuesExpectedList.containsAll(vlanInterfaceTypeAllowableValuesList), String.format(ERROR_MESSAGE_INFO, vlanInterfaceTypeAllowableValues));
        softAssert.assertTrue(administrativeStateAllowableValuesList.containsAll(administrativeStateAllowableValuesExpectedList) && administrativeStateAllowableValuesExpectedList.containsAll(administrativeStateAllowableValuesList), String.format(ERROR_MESSAGE_INFO, administrativeStateAllowableValues));
        softAssert.assertAll();
    }

    private void assertRelationsTabAttributesForAEI() {
        ResourceSpecificationsViewPage resourceSpecificationsViewPage = ResourceSpecificationsViewPage.create(driver, webDriverWait);
        CommonList commonList = resourceSpecificationsViewPage.getRelationsCommonList();
        String mplsInterfaceRelationRole = commonList.getRow(RELATIONS_SPECIFICATION_NAME, "MPLS Interface").getValue(RELATIONS_RELATION_ROLE_NAME);
        String vlanInterfaceRelationRole = commonList.getRow(RELATIONS_SPECIFICATION_NAME, "VLAN Interface").getValue(RELATIONS_RELATION_ROLE_NAME);
        String ipRedundancyGroupConfigRelationRole = commonList.getRow(RELATIONS_SPECIFICATION_NAME, "IP Redundancy Group Config").getValue(RELATIONS_RELATION_ROLE_NAME);
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(mplsInterfaceRelationRole, CHILD_NODE_VALUE);
        softAssert.assertEquals(vlanInterfaceRelationRole, CHILD_NODE_VALUE);
        softAssert.assertEquals(ipRedundancyGroupConfigRelationRole, IP_REDUNDANCY_GROUP_MEMBERSHIP_VALUE);
        softAssert.assertAll();
    }

    private void assertRelationsTabAttributesForEI() {
        ResourceSpecificationsViewPage resourceSpecificationsViewPage = ResourceSpecificationsViewPage.create(driver, webDriverWait);
        CommonList commonList = resourceSpecificationsViewPage.getRelationsCommonList();
        String mplsInterfaceRelationRole = commonList.getRow(RELATIONS_SPECIFICATION_NAME, "MPLS Interface").getValue(RELATIONS_RELATION_ROLE_NAME);
        String vlanInterfaceRelationRole = commonList.getRow(RELATIONS_SPECIFICATION_NAME, "VLAN Interface").getValue(RELATIONS_RELATION_ROLE_NAME);
        String ipRedundancyGroupConfigRelationRole = commonList.getRow(RELATIONS_SPECIFICATION_NAME, "IP Redundancy Group Config").getValue(RELATIONS_RELATION_ROLE_NAME);
        String discoveryProtocolEntryRelationRole = commonList.getRow(RELATIONS_SPECIFICATION_NAME, "Discovery Protocol Entry").getValue(RELATIONS_RELATION_ROLE_NAME);
        String discoveryProtocolEntryMaxCardinality = commonList.getRow(RELATIONS_SPECIFICATION_NAME, "Discovery Protocol Entry").getValue(RELATIONS_MAX_CARDINALITY_NAME);
        String pluggableModuleRelationRole = commonList.getRow(RELATIONS_SPECIFICATION_NAME, "Pluggable Module").getValue(RELATIONS_RELATION_ROLE_NAME);
        String pluggableModuleMaxCardinality = commonList.getRow(RELATIONS_SPECIFICATION_NAME, "Pluggable Module").getValue(RELATIONS_MAX_CARDINALITY_NAME);
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(mplsInterfaceRelationRole, CHILD_NODE_VALUE);
        softAssert.assertEquals(vlanInterfaceRelationRole, CHILD_NODE_VALUE);
        softAssert.assertEquals(ipRedundancyGroupConfigRelationRole, IP_REDUNDANCY_GROUP_MEMBERSHIP_VALUE);
        softAssert.assertEquals(discoveryProtocolEntryRelationRole, "DiscoveryProtocolEntry");
        softAssert.assertEquals(discoveryProtocolEntryMaxCardinality, "1");
        softAssert.assertEquals(pluggableModuleRelationRole, "pluggableModule");
        softAssert.assertEquals(pluggableModuleMaxCardinality, "1");
        softAssert.assertAll();
    }

    private void assertRelationsTabAttributesForIPNE() {
        ResourceSpecificationsViewPage resourceSpecificationsViewPage = ResourceSpecificationsViewPage.create(driver, webDriverWait);
        CommonList commonList = resourceSpecificationsViewPage.getRelationsCommonList();
        String evpnInstanceRelationRole = commonList.getRow(RELATIONS_SPECIFICATION_NAME, "EVPN Instance").getValue(RELATIONS_RELATION_ROLE_NAME);
        String vtepRelationRole = commonList.getRow(RELATIONS_SPECIFICATION_NAME, "VTEP").getValue(RELATIONS_RELATION_ROLE_NAME);
        String vtepMaxCardinality = commonList.getRow(RELATIONS_SPECIFICATION_NAME, "VTEP").getValue(RELATIONS_MAX_CARDINALITY_NAME);
        String vxLanInstanceRelationRole = commonList.getRow(RELATIONS_SPECIFICATION_NAME, "VxLAN Instance").getValue(RELATIONS_RELATION_ROLE_NAME);
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(evpnInstanceRelationRole, "DetailEVPNInstances");
        softAssert.assertEquals(vtepRelationRole, "DetailVteps");
        softAssert.assertEquals(vtepMaxCardinality, "1");
        softAssert.assertEquals(vxLanInstanceRelationRole, "DetailVxlanInstances");
        softAssert.assertAll();
    }

    private void assertRelationsTabAttributesForVLANInterface() {
        ResourceSpecificationsViewPage resourceSpecificationsViewPage = ResourceSpecificationsViewPage.create(driver, webDriverWait);
        CommonList commonList = resourceSpecificationsViewPage.getRelationsCommonList();
        String mplsInterfaceRelationRole = commonList.getRow(RELATIONS_SPECIFICATION_NAME, "MPLS Interface").getValue(RELATIONS_RELATION_ROLE_NAME);
        String ipRedundancyGroupConfigRelationRole = commonList.getRow(RELATIONS_SPECIFICATION_NAME, "IP Redundancy Group Config").getValue(RELATIONS_RELATION_ROLE_NAME);
        String ethernetInterfaceRelationRole = commonList.getRow(RELATIONS_SPECIFICATION_NAME, "Ethernet Interface").getValue(RELATIONS_RELATION_ROLE_NAME);
        String ethernetInterfaceMaxCardinality = commonList.getRow(RELATIONS_SPECIFICATION_NAME, "Ethernet Interface").getValue(RELATIONS_MAX_CARDINALITY_NAME);
        String aggregatedEthernetInterfaceRelationRole = commonList.getRow(RELATIONS_SPECIFICATION_NAME, "Aggregated Ethernet Interface").getValue(RELATIONS_RELATION_ROLE_NAME);
        String aggregatedEthernetInterfaceMaxCardinality = commonList.getRow(RELATIONS_SPECIFICATION_NAME, "Aggregated Ethernet Interface").getValue(RELATIONS_MAX_CARDINALITY_NAME);
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(mplsInterfaceRelationRole, CHILD_NODE_VALUE);
        softAssert.assertEquals(ipRedundancyGroupConfigRelationRole, IP_REDUNDANCY_GROUP_MEMBERSHIP_VALUE);
        softAssert.assertEquals(ethernetInterfaceRelationRole, "parentNode");
        softAssert.assertEquals(ethernetInterfaceMaxCardinality, "1");
        softAssert.assertEquals(aggregatedEthernetInterfaceRelationRole, "parentNode");
        softAssert.assertEquals(aggregatedEthernetInterfaceMaxCardinality, "1");
        softAssert.assertAll();
    }

    private static class DetailTabAttributes {
        private String identifier;
        private String name;
        private String category;
        private String type;
        private String inventoryType;
        private String instanceType;
        private String lifeCycle;
        private String baseSpecification;
        private String action;
        private String description;
        private String namingRule;
        private String hasDescriptor;
        private String isAbstract;
        private String origin;
        private String templateInputParameter;
    }

    private DetailTabAttributes defaultAggregatedEthernetLinkAttributes() {
        DetailTabAttributes aelDetailAttributes = new DetailTabAttributes();
        aelDetailAttributes.identifier = "AggregatedEthernetLink";
        aelDetailAttributes.name = "Aggregated Ethernet Link";
        aelDetailAttributes.category = CATEGORY_BASE_MODEL_TRANSPORT_VALUE;
        aelDetailAttributes.type = TYPE_RESOURCE_VALUE;
        aelDetailAttributes.inventoryType = "Trail";
        aelDetailAttributes.instanceType = "AggregatedEthernetLink";
        aelDetailAttributes.lifeCycle = LIFE_CYCLE_ACTIVE_VALUE;
        aelDetailAttributes.baseSpecification = "Trail";
        aelDetailAttributes.action = ACTION_ANY_VALUE;
        aelDetailAttributes.description = "Aggregated Ethernet Link Resource Specification (Product-defined)";
        aelDetailAttributes.namingRule = NAMING_RULE_VALUE;
        aelDetailAttributes.hasDescriptor = FALSE_VALUE;
        aelDetailAttributes.isAbstract = FALSE_VALUE;
        aelDetailAttributes.origin = ORIGIN_PRODUCT_VALUE;
        aelDetailAttributes.templateInputParameter = FALSE_VALUE;
        return aelDetailAttributes;
    }

    private DetailTabAttributes defaultEthernetLinkAttributes() {
        DetailTabAttributes elDetailAttributes = new DetailTabAttributes();
        elDetailAttributes.identifier = "EthernetLink";
        elDetailAttributes.name = "Ethernet Link";
        elDetailAttributes.category = CATEGORY_BASE_MODEL_TRANSPORT_VALUE;
        elDetailAttributes.type = TYPE_RESOURCE_VALUE;
        elDetailAttributes.inventoryType = "Trail";
        elDetailAttributes.instanceType = "EthernetLink";
        elDetailAttributes.lifeCycle = LIFE_CYCLE_ACTIVE_VALUE;
        elDetailAttributes.baseSpecification = "Trail";
        elDetailAttributes.action = ACTION_ANY_VALUE;
        elDetailAttributes.description = "Ethernet Link Resource Specification (Product-defined)";
        elDetailAttributes.namingRule = NAMING_RULE_VALUE;
        elDetailAttributes.hasDescriptor = FALSE_VALUE;
        elDetailAttributes.isAbstract = FALSE_VALUE;
        elDetailAttributes.origin = ORIGIN_PRODUCT_VALUE;
        elDetailAttributes.templateInputParameter = FALSE_VALUE;
        return elDetailAttributes;
    }

    private DetailTabAttributes defaultELineAttributes() {
        DetailTabAttributes eLineDetailAttributes = new DetailTabAttributes();
        eLineDetailAttributes.identifier = "ELine";
        eLineDetailAttributes.name = "E-Line";
        eLineDetailAttributes.category = CATEGORY_BASE_MODEL_TRANSPORT_VALUE;
        eLineDetailAttributes.type = TYPE_RESOURCE_VALUE;
        eLineDetailAttributes.inventoryType = "Trail";
        eLineDetailAttributes.instanceType = "ELine";
        eLineDetailAttributes.lifeCycle = LIFE_CYCLE_ACTIVE_VALUE;
        eLineDetailAttributes.baseSpecification = "Trail";
        eLineDetailAttributes.action = ACTION_ANY_VALUE;
        eLineDetailAttributes.description = "E-Line Resource Specification (Product-defined)";
        eLineDetailAttributes.namingRule = NAMING_RULE_VALUE;
        eLineDetailAttributes.hasDescriptor = FALSE_VALUE;
        eLineDetailAttributes.isAbstract = FALSE_VALUE;
        eLineDetailAttributes.origin = ORIGIN_PRODUCT_VALUE;
        eLineDetailAttributes.templateInputParameter = FALSE_VALUE;
        return eLineDetailAttributes;
    }

    private DetailTabAttributes defaultVirtualNetworkAttributes() {
        DetailTabAttributes detailAttributes = new DetailTabAttributes();
        detailAttributes.identifier = "VirtualNetwork";
        detailAttributes.name = "Virtual Network";
        detailAttributes.category = CATEGORY_BASE_MODEL_TRANSPORT_VALUE;
        detailAttributes.type = TYPE_RESOURCE_VALUE;
        detailAttributes.inventoryType = "Trail";
        detailAttributes.instanceType = "VirtualNetwork";
        detailAttributes.lifeCycle = LIFE_CYCLE_ACTIVE_VALUE;
        detailAttributes.baseSpecification = "Trail";
        detailAttributes.action = ACTION_ANY_VALUE;
        detailAttributes.description = "Virtual Network Resource Specification (Product-defined)";
        detailAttributes.namingRule = NAMING_RULE_VALUE;
        detailAttributes.hasDescriptor = FALSE_VALUE;
        detailAttributes.isAbstract = FALSE_VALUE;
        detailAttributes.origin = ORIGIN_PRODUCT_VALUE;
        detailAttributes.templateInputParameter = FALSE_VALUE;
        return detailAttributes;
    }

    private DetailTabAttributes defaultVLANAttributes() {
        DetailTabAttributes detailAttributes = new DetailTabAttributes();
        detailAttributes.identifier = "VLAN";
        detailAttributes.name = "VLAN";
        detailAttributes.category = CATEGORY_BASE_MODEL_TRANSPORT_VALUE;
        detailAttributes.type = TYPE_RESOURCE_VALUE;
        detailAttributes.inventoryType = "Trail";
        detailAttributes.instanceType = "VLAN";
        detailAttributes.lifeCycle = LIFE_CYCLE_ACTIVE_VALUE;
        detailAttributes.baseSpecification = "Trail";
        detailAttributes.action = ACTION_ANY_VALUE;
        detailAttributes.description = "VLAN Resource Specification (Product-defined)";
        detailAttributes.namingRule = NAMING_RULE_VALUE;
        detailAttributes.hasDescriptor = FALSE_VALUE;
        detailAttributes.isAbstract = FALSE_VALUE;
        detailAttributes.origin = ORIGIN_PRODUCT_VALUE;
        detailAttributes.templateInputParameter = FALSE_VALUE;
        return detailAttributes;
    }

    private DetailTabAttributes defaultAggregatedEthernetInterfaceAttributes() {
        DetailTabAttributes detailAttributes = new DetailTabAttributes();
        detailAttributes.identifier = "AggregatedEthernetInterface_TP";
        detailAttributes.name = "Aggregated Ethernet Interface";
        detailAttributes.category = CATEGORY_BASE_MODEL_TRANSPORT_VALUE;
        detailAttributes.type = TYPE_RESOURCE_VALUE;
        detailAttributes.inventoryType = "TerminationPointNew";
        detailAttributes.instanceType = "AggregatedEthernetInterface_TP";
        detailAttributes.lifeCycle = LIFE_CYCLE_ACTIVE_VALUE;
        detailAttributes.baseSpecification = "TerminationPointNew";
        detailAttributes.action = ACTION_ANY_VALUE;
        detailAttributes.description = "Aggregated Ethernet Interface Resource Specification (Product-defined)";
        detailAttributes.namingRule = NAMING_RULE_VALUE;
        detailAttributes.hasDescriptor = FALSE_VALUE;
        detailAttributes.isAbstract = FALSE_VALUE;
        detailAttributes.origin = ORIGIN_PRODUCT_VALUE;
        detailAttributes.templateInputParameter = FALSE_VALUE;
        return detailAttributes;
    }

    private DetailTabAttributes defaultEthernetInterfaceAttributes() {
        DetailTabAttributes detailAttributes = new DetailTabAttributes();
        detailAttributes.identifier = "EthernetInterface_TP";
        detailAttributes.name = "Ethernet Interface";
        detailAttributes.category = CATEGORY_BASE_MODEL_TRANSPORT_VALUE;
        detailAttributes.type = TYPE_RESOURCE_VALUE;
        detailAttributes.inventoryType = "TerminationPointNew";
        detailAttributes.instanceType = "EthernetInterface_TP";
        detailAttributes.lifeCycle = LIFE_CYCLE_ACTIVE_VALUE;
        detailAttributes.baseSpecification = "TerminationPointNew";
        detailAttributes.action = ACTION_ANY_VALUE;
        detailAttributes.description = "Ethernet Interface Resource Specification (Product-defined)";
        detailAttributes.namingRule = NAMING_RULE_VALUE;
        detailAttributes.hasDescriptor = FALSE_VALUE;
        detailAttributes.isAbstract = FALSE_VALUE;
        detailAttributes.origin = ORIGIN_PRODUCT_VALUE;
        detailAttributes.templateInputParameter = FALSE_VALUE;
        return detailAttributes;
    }

    private DetailTabAttributes defaultIRBInterfaceAttributes() {
        DetailTabAttributes detailAttributes = new DetailTabAttributes();
        detailAttributes.identifier = "IRBInterface_TP";
        detailAttributes.name = "IRB Interface";
        detailAttributes.category = CATEGORY_BASE_MODEL_TRANSPORT_VALUE;
        detailAttributes.type = TYPE_RESOURCE_VALUE;
        detailAttributes.inventoryType = "TerminationPointNew";
        detailAttributes.instanceType = "IRBInterface_TP";
        detailAttributes.lifeCycle = LIFE_CYCLE_ACTIVE_VALUE;
        detailAttributes.baseSpecification = "TerminationPointNew";
        detailAttributes.action = ACTION_ANY_VALUE;
        detailAttributes.description = "IRB Interface Resource Specification (Product-defined)";
        detailAttributes.namingRule = NAMING_RULE_VALUE;
        detailAttributes.hasDescriptor = FALSE_VALUE;
        detailAttributes.isAbstract = FALSE_VALUE;
        detailAttributes.origin = ORIGIN_PRODUCT_VALUE;
        detailAttributes.templateInputParameter = FALSE_VALUE;
        return detailAttributes;
    }

    private DetailTabAttributes defaultLoopbackInterfaceAttributes() {
        DetailTabAttributes detailAttributes = new DetailTabAttributes();
        detailAttributes.identifier = "LoopbackInterface_TP";
        detailAttributes.name = "Loopback Interface";
        detailAttributes.category = CATEGORY_BASE_MODEL_TRANSPORT_VALUE;
        detailAttributes.type = TYPE_RESOURCE_VALUE;
        detailAttributes.inventoryType = "TerminationPointNew";
        detailAttributes.instanceType = "LoopbackInterface_TP";
        detailAttributes.lifeCycle = LIFE_CYCLE_ACTIVE_VALUE;
        detailAttributes.baseSpecification = "TerminationPointNew";
        detailAttributes.action = ACTION_ANY_VALUE;
        detailAttributes.description = "Loopback Interface Resource Specification (Product-defined)";
        detailAttributes.namingRule = NAMING_RULE_VALUE;
        detailAttributes.hasDescriptor = FALSE_VALUE;
        detailAttributes.isAbstract = FALSE_VALUE;
        detailAttributes.origin = ORIGIN_PRODUCT_VALUE;
        detailAttributes.templateInputParameter = FALSE_VALUE;
        return detailAttributes;
    }

    private DetailTabAttributes defaultVLANInterfaceAttributes() {
        DetailTabAttributes detailAttributes = new DetailTabAttributes();
        detailAttributes.identifier = "VLANInterface_TP";
        detailAttributes.name = "VLAN Interface";
        detailAttributes.category = CATEGORY_BASE_MODEL_TRANSPORT_VALUE;
        detailAttributes.type = TYPE_RESOURCE_VALUE;
        detailAttributes.inventoryType = "TerminationPointNew";
        detailAttributes.instanceType = "VLANInterface_TP";
        detailAttributes.lifeCycle = LIFE_CYCLE_ACTIVE_VALUE;
        detailAttributes.baseSpecification = "TerminationPointNew";
        detailAttributes.action = ACTION_ANY_VALUE;
        detailAttributes.description = "VLAN Interface Resource Specification (Product-defined)";
        detailAttributes.namingRule = NAMING_RULE_VALUE;
        detailAttributes.hasDescriptor = FALSE_VALUE;
        detailAttributes.isAbstract = FALSE_VALUE;
        detailAttributes.origin = ORIGIN_PRODUCT_VALUE;
        detailAttributes.templateInputParameter = FALSE_VALUE;
        return detailAttributes;
    }

    private DetailTabAttributes defaultIPNetworkElementAttributes() {
        DetailTabAttributes detailAttributes = new DetailTabAttributes();
        detailAttributes.identifier = "IPNetworkElement";
        detailAttributes.name = "IP Network Element";
        detailAttributes.category = CATEGORY_BASE_MODEL_TRANSPORT_VALUE;
        detailAttributes.type = TYPE_RESOURCE_VALUE;
        detailAttributes.inventoryType = "LogicalFunction";
        detailAttributes.instanceType = "IPNetworkElement";
        detailAttributes.lifeCycle = LIFE_CYCLE_ACTIVE_VALUE;
        detailAttributes.baseSpecification = "NetworkElement";
        detailAttributes.action = ACTION_ANY_VALUE;
        detailAttributes.description = "";
        detailAttributes.namingRule = NAMING_RULE_VALUE;
        detailAttributes.hasDescriptor = FALSE_VALUE;
        detailAttributes.isAbstract = FALSE_VALUE;
        detailAttributes.origin = ORIGIN_PRODUCT_VALUE;
        detailAttributes.templateInputParameter = FALSE_VALUE;
        return detailAttributes;
    }

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }
}

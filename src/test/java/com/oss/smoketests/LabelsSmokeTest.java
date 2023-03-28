package com.oss.smoketests;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.comarch.oss.web.pages.NewInventoryViewPage;
import com.comarch.oss.web.pages.SearchObjectTypePage;
import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.GlobalNotificationContainer;
import com.oss.framework.components.attributechooser.AttributesChooser;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.layout.ErrorCard;
import com.oss.framework.components.mainheader.PerspectiveChooser;
import com.oss.framework.components.mainheader.ToolbarWidget;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.propertypanel.PropertyPanel;
import com.oss.framework.widgets.table.TableWidget;

import io.qameta.allure.Description;
import io.qameta.allure.Step;

public class LabelsSmokeTest extends BaseTestCase {

    private static final Logger LOGGER = LoggerFactory.getLogger(LabelsSmokeTest.class);
    private static final String RESOURCE_INVENTORY_CATEGORY = "Resource Inventory";
    private static final String INVENTORY_VIEW = "Inventory View";
    private static final String SITE_OBJECT_TYPE = "Site";
    private static final String ROUTER_OBJECT_TYPE = "Router";
    private static final String E_NODE_B_OBJECT_TYPE = "eNodeB";
    private static final String ETHERNET_INTERFACE_OBJECT_TYPE = "Ethernet Interface";
    private static final String DM_PREFIX = "DM_";

    private static final String ATTRIBUTES_TRANSLATIONS_ERROR_MESSAGE = " column label is not translated";
    private static final String PROPERTIES_TRANSLATIONS_ERROR_MESSAGE = " property label is not translated ";
    private static final String LABEL_TRANSLATIONS_ERROR_MESSAGE = " column label is not translated ";
    private static final String ACTION_TRANSLATION_ERROR_MESSAGE = " context action is not translated";

    private static final String ASSIGN_EXISTING_REGULATORY_LICENSE_ACTION_ID = "AssignLocationToRegulatoryLicenseApplicationContextAction";
    private static final String ASSIGN_IPV4_SUBNET_TO_SITE_ACTION_ID = "AssignIPv4SubnetToLocationApplication";
    private static final String ASSIGN_IPV4_SUBNET_TO_ROUTER_ACTION_ID = "AssignIPv4SubnetToDeviceApplication";
    private static final String ASSIGN_IPV4_SUBNET_TO_LOGICAL_FUNCTION_ACTION_ID = "AssignIPv4SubnetToLogicalFunctionApplication";
    private static final String ASSIGN_IPV6_SUBNET_TO_SITE_ACTION_ID = "AssignIPv6SubnetToLocationApplication";
    private static final String ASSIGN_IPV6_SUBNET_TO_ROUTER_ACTION_ID = "AssignIPv6SubnetToDeviceApplication";
    private static final String ASSIGN_IPV6_SUBNET_TO_LOGICAL_FUNCTION_ACTION_ID = "AssignIPv6SubnetToLogicalFunctionApplication";
    private static final String ASSIGN_IPV4_HOST_ADDRESS_ACTION_ID = "AssignIPv4Host";
    private static final String ASSIGN_IPV4_LOOPBACK_ADDRESS_ACTION_ID = "AssignLoopbackIPv4Host";
    private static final String ASSIGN_IPV6_HOST_ADDRESS_ACTION_ID = "AssignIPv6Host";
    private static final String ASSIGN_IPV6_LOOPBACK_ADDRESS_ACTION_ID = "AssignLoopbackIPv6Host";
    private static final String ASSIGN_MULTIPLE_IPV4_ADDRESSES_ACTION_ID = "MultipleAssignIPv4Host";
    private static final String ASSIGN_MULTIPLE_IPV4_LOOPBACKS_ACTION_ID = "MultipleAssignLoopbackIPv4Host";
    private static final String ASSIGN_MULTIPLE_IPV6_ADDRESSES_ACTION_ID = "MultipleAssignIPv6Host";
    private static final String ASSIGN_MULTIPLE_IPV6_LOOPBACKS_ACTION_ID = "MultipleAssignLoopbackIPv6Host";
    private static final String ASSIGN_NEW_REGULATORY_LICENSE_ACTION_ID = "AssignLocationToCreatedRegulatoryLicenseApplicationContextAction";
    private static final String CREATE_CONNECTION_ACTION_ID = "CreateTrailWizardActionId";
    private static final String CREATE_CONNECTION_ON_LF_ACTION_ID = "CreateTrailWizardForLFActionId";
    private static final String CREATE_AGGREGATED_ETHERNET_INTERFACE_ACTION_ID = "CreateAggregateEthernetInterfaceContextAction";
    private static final String CREATE_ETHERNET_INTERFACE_ACTION_ID = "CreateEthernetInterfaceLogicalFunctionContextAction";
    private static final String CREATE_IRB_INTERFACE_ACTION_ID = "CreateIRBInterfaceContextAction";
    private static final String CREATE_LOOPBACK_INTERFACE_ACTION_ID = "CreateLoopbackInterfaceContextAction";
    private static final String CREATE_MPLS_TUNNEL_INTERFACE_ACTION_ID = "mplsTunnelInterfaceCreateAction";
    private static final String CREATE_TRAFFIC_CLASS_ACTION_ID = "CreateTrafficClassContextAction";
    private static final String CREATE_TRAFFIC_POLICY_ACTION_ID = "CreateTrafficPolicyContextAction";
    private static final String CREATE_IP_REDUNDANCY_GROUP_CONFIG_ACTION_ID = "IPRedundancyGroupCreateMembershipContextAction";
    private static final String CREATE_MPLS_INTERFACE_ACTION_ID = "CreateMPLSInterfaceContextAction";
    private static final String CREATE_VLAN_INTERFACE_ACTION_ID = "CreateVLANInterfaceContextAction";
    private static final String DELETE_E_NODE_B_ACTION_ID = "deleteRanENodeBWizard";
    private static final String EDIT_E_NODE_B_ACTION_ID = "editRanENodeBWizard";
    private static final String DELETE_ETHERNET_INTERFACE_ACTION_ID = "DeleteEthernetInterfaceContextAction";
    private static final String EDIT_ETHERNET_INTERFACE_ACTION_ID = "EditEthernetInterfaceContextAction";
    private static final String DOWNLOAD_FILE_TO_SOFTWARE_REPOSITORY_ACTION_ID = "smComponent_LogicalDownloadSoftwareOperationActionId";
    private static final String CREATE_VRF_ACTION_ID = "CreateVRFContextAction";
    private static final String CREATE_VSI_ACTION_ID = "CreateVSIContextAction";
    private static final String CREATE_PLUGGABLE_MODULE_ACTION_ID = "CreatePluggableModuleOnDeviceApp";
    private static final String CREATE_PORTS_ACTION_ID = "CreatePortInDeviceAction";
    private static final String CREATE_SLOT_ACTION_ID = "CreateSlotAction";
    private static final String CREATE_PHYSICAL_DEVICE_ACTION_ID = "CreateDeviceOnLocationWizardAction";
    private static final String CREATE_E_NODE_B_ACTION_ID = "createRanENodeBOnLocationWizard";
    private static final String DELETE_LOCATION_ACTION_ID = "RemoveLocationWizardAction";
    private static final String EDIT_LOCATION_ACTION_ID = "UpdateLocationWizardAction";
    private static final String CHANGE_LOCATION_ACTION_ID = "DeviceChangeLocationAction";
    private static final String CHANGE_PHYSICAL_DEVICE_MODEL_ACTION_ID = "ChangeDeviceModelAction";
    private static final String DELETE_PHYSICAL_DEVICE_ACTION_ID = "DeleteDeviceWizardAction";
    private static final String EDIT_PHYSICAL_DEVICE_ACTION_ID = "UpdateDeviceWizardAction";
    private static final String BLOCK_ROUTER_ACTION_ID = "BlockadeWizardForPhysicalElementAction";
    private static final String RESERVE_ROUTER_ACTION_ID = "ReservationWizardForPhysicalElementAction";
    private static final String RESERVE_LOCATION_ACTION_ID = "LocationReservationWizardAction";
    private static final String HIERARCHY_VIEW_ACTION_ID = "HierarchyView";
    private static final String INVENTORY_VIEW_ACTION_ID = "InventoryView";

    private static final String ASSIGN_EXISTING_REGULATORY_LICENSE_ACTION_LABEL = "Existing Regulatory License";
    private static final String ASSIGN_IPV4_SUBNET_ACTION_LABEL = "IPv4 Subnet";
    private static final String ASSIGN_IPV6_SUBNET_ACTION_LABEL = "IPv6 Subnet";
    private static final String ASSIGN_IPV4_HOST_ADDRESS_ACTION_LABEL = "IPv4 Host Address";
    private static final String ASSIGN_IPV4_LOOPBACK_ADDRESS_ACTION_LABEL = "IPv4 Loopback Address";
    private static final String ASSIGN_IPV6_HOST_ADDRESS_ACTION_LABEL = "IPv6 Host Address";
    private static final String ASSIGN_IPV6_LOOPBACK_ADDRESS_ACTION_LABEL = "IPv6 Loopback Address";
    private static final String ASSIGN_MULTIPLE_IPV4_ADDRESSES_ACTION_LABEL = "Multiple IPv4 Addresses";
    private static final String ASSIGN_MULTIPLE_IPV4_LOOPBACKS_ACTION_LABEL = "Multiple IPv4 Loopbacks";
    private static final String ASSIGN_MULTIPLE_IPV6_ADDRESSES_ACTION_LABEL = "Multiple IPv6 Addresses";
    private static final String ASSIGN_MULTIPLE_IPV6_LOOPBACKS_ACTION_LABEL = "Multiple IPv6 Loopbacks";
    private static final String ASSIGN_NEW_REGULATORY_LICENSE_ACTION_LABEL = "New Regulatory License";
    private static final String CREATE_CONNECTION_ACTION_LABEL = "Create Connection";
    private static final String CREATE_AGGREGATED_ETHERNET_INTERFACE_ACTION_LABEL = "Create Aggregated Ethernet Interface";
    private static final String CREATE_ETHERNET_INTERFACE_ACTION_LABEL = "Create Ethernet Interface";
    private static final String CREATE_IRB_INTERFACE_ACTION_LABEL = "Create IRB Interface";
    private static final String CREATE_LOOPBACK_INTERFACE_ACTION_LABEL = "Create Loopback Interface";
    private static final String CREATE_MPLS_TUNNEL_INTERFACE_ACTION_LABEL = "Create MPLS Tunnel Interface";
    private static final String CREATE_TRAFFIC_CLASS_ACTION_LABEL = "Create Traffic Class";
    private static final String CREATE_TRAFFIC_POLICY_ACTION_LABEL = "Create Traffic Policy";
    private static final String DELETE_E_NODE_B_ACTION_LABEL = "Delete eNodeB";
    private static final String EDIT_E_NODE_B_ACTION_LABEL = "Edit eNodeB";
    private static final String DOWNLOAD_FILE_TO_SOFTWARE_REPOSITORY_ACTION_LABEL = "Download file to Software Repository";
    private static final String CREATE_VRF_ACTION_LABEL = "Create VRF";
    private static final String CREATE_VSI_ACTION_LABEL = "Create VSI";
    private static final String CREATE_PLUGGABLE_MODULE_ACTION_LABEL = "Pluggable Module";
    private static final String CREATE_PORTS_ACTION_LABEL = "Ports";
    private static final String CREATE_SLOT_ACTION_LABEL = "Slot";
    private static final String CREATE_PHYSICAL_DEVICE_ACTION_LABEL = "Create Physical Device";
    private static final String CREATE_E_NODE_B_ACTION_LABEL = "eNodeB";
    private static final String CREATE_IP_REDUNDANCY_GROUP_CONFIG_ACTION_LABEL = "Create IP Redundancy Group Config";
    private static final String CREATE_MPLS_INTERFACE_ACTION_LABEL = "Create MPLS Interface";
    private static final String CREATE_VLAN_INTERFACE_ACTION_LABEL = "Create VLAN Interface";
    private static final String DELETE_LOCATION_ACTION_LABEL = "Delete";
    private static final String EDIT_LOCATION_ACTION_LABEL = "Edit Location";
    private static final String DELETE_ETHERNET_INTERFACE_ACTION_LABEL = "Delete Ethernet Interface";
    private static final String EDIT_ETHERNET_INTERFACE_ACTION_LABEL = "Edit Ethernet Interface";
    private static final String CHANGE_LOCATION_ACTION_LABEL = "Change Location";
    private static final String CHANGE_PHYSICAL_DEVICE_MODEL_ACTION_LABEL = "Change Physical Device Model";
    private static final String DELETE_PHYSICAL_DEVICE_ACTION_LABEL = "Delete Physical Device";
    private static final String EDIT_PHYSICAL_DEVICE_ACTION_LABEL = "Edit Physical Device";
    private static final String RESERVE_ROUTER_ACTION_LABEL = "Reserve";
    private static final String BLOCK_ROUTER_ACTION_LABEL = "Block";
    private static final String RESERVE_LOCATION_ACTION_LABEL = "Reserve location";
    private static final String HIERARCHY_VIEW_ACTION_LABEL = "Hierarchy View";
    private static final String INVENTORY_VIEW_ACTION_LABEL = "Inventory View";

    private static final String LOCATION_NODE = "Location";
    private static final String PRECISE_LOCATION_NODE = "Precise Location";
    private static final String LOGICAL_LOCATION_NODE = "Logical Location";
    private static final String MASTER_POSITION_ELEMENT_NODE = "Master Position Element";
    private static final String MANAGEMENT_SYSTEM_NODE = "Management System";
    private static final String ADDRESS_NODE = "Address";
    private static final String PHYSICAL_DEVICE_NODE = "Physical Device";
    private static final String PORT_NODE = "Port";
    private static final String PLUGGABLE_MODULE_NODE = "Pluggable Module";
    private static final String LOGICAL_FUNCTION_NODE = "Logical Function";
    private static final String NETWORK_DOMAIN_NODE = "Network Domain";
    private static final String AUDIT_INFORMATION_NODE = "Audit Information";
    private static final String ASSIGNED_TO_LOGICAL_FUNCTIONS_NODE = "Assigned To Logical Functions";
    private static final String MASTER_ELEMENT_NODE = "Master Element";
    private static final String COOLING_ZONE_NODE = "Cooling Zone";
    private static final String DEVICE_CATEGORY_NODE = "Device Category";
    private static final String BLOCKADE_NODE = "Blockade";
    private static final String MASTER_LOGICAL_FUNCTION_NODE = "Master Logical Function";
    private static final String MODEL_NODE = "Model";
    private static final String PHYSICAL_LOCATION_NODE = "Physical Location";
    private static final String RESERVATION_NODE = "Reservation";
    private static final String RESOURCE_SPECIFICATION_NODE = "Resource Specification";
    private static final String TEMPLATE_NODE = "Template";
    private static final String ROOT_RESOURCE_SPECIFICATION_NODE = "Root Resource Specification";
    private SoftAssert softAssert;

    @Test(priority = 1, description = "Open browser and check environment status")
    @Description("Open browser and check environment status")
    public void openBrowserAndCheckEnvironmentStatus() {
        waitForPageToLoad();
        checkErrorPage();
        checkGlobalNotificationContainer();
        PerspectiveChooser.create(driver, webDriverWait).setLivePerspective();
        waitForPageToLoad();
    }

    @Test(priority = 2, description = "Check Inventory View for Site", dependsOnMethods = {"openBrowserAndCheckEnvironmentStatus"})
    @Description("Check Inventory View for Site")
    public void checkInventoryViewForSite() {
        softAssert = new SoftAssert();
        openInventoryViewForGivenObjectType(SITE_OBJECT_TYPE);
        checkInventoryViewTitle(INVENTORY_VIEW);
        selectObjectOnInventoryView();

        checkColumnsHeaders();
        checkSiteColumnsManagement();
        checkPropertiesHeaders();
        checkSiteContextActions();
        softAssert.assertAll();
    }

    @Test(priority = 3, description = "Check Inventory View for Router", dependsOnMethods = {"openBrowserAndCheckEnvironmentStatus"})
    @Description("Check Inventory View for Router")
    public void checkInventoryViewForRouter() {
        softAssert = new SoftAssert();
        openInventoryViewForGivenObjectType(ROUTER_OBJECT_TYPE);
        checkInventoryViewTitle(INVENTORY_VIEW);
        selectObjectOnInventoryView();

        checkColumnsHeaders();
        checkRouterColumnsManagement();
        checkPropertiesHeaders();
        checkRouterContextActions();
        softAssert.assertAll();
    }

    @Test(priority = 4, description = "Check Inventory View for eNodeB", dependsOnMethods = {"openBrowserAndCheckEnvironmentStatus"})
    @Description("Check Inventory View for eNodeB")
    public void checkInventoryViewForENodeB() {
        softAssert = new SoftAssert();
        openInventoryViewForGivenObjectType(E_NODE_B_OBJECT_TYPE);
        checkInventoryViewTitle(E_NODE_B_OBJECT_TYPE);
        selectObjectOnInventoryView();

        checkColumnsHeaders();
        checkENodeBColumnsManagement();
        checkPropertiesHeaders();
        checkENodeBContextActions();
        softAssert.assertAll();
    }

    @Test(priority = 5, description = "Check Inventory View for Ethernet Interface", dependsOnMethods = {"openBrowserAndCheckEnvironmentStatus"})
    @Description("Check Inventory View for Ethernet Interface")
    public void checkInventoryViewForEthernetInterface() {
        softAssert = new SoftAssert();
        openInventoryViewForGivenObjectType(ETHERNET_INTERFACE_OBJECT_TYPE);
        checkInventoryViewTitle(INVENTORY_VIEW);
        selectObjectOnInventoryView();

        checkColumnsHeaders();
        checkEthernetInterfaceColumnsManagement();
        checkPropertiesHeaders();
        checkEthernetInterfaceContextActions();
        softAssert.assertAll();
    }

    @Description("Open Inventory View for {objectType}")
    private void openInventoryViewForGivenObjectType(String objectType) {
        homePage.goToHomePage(driver, BASIC_URL);
        homePage.openApplication(RESOURCE_INVENTORY_CATEGORY, INVENTORY_VIEW);
        waitForPageToLoad();
        SearchObjectTypePage searchObjectTypePage = new SearchObjectTypePage(driver, webDriverWait);
        searchObjectTypePage.searchType(objectType);
        waitForPageToLoad();
    }

    @Step("Selecting first object")
    private void selectObjectOnInventoryView() {
        NewInventoryViewPage newInventoryViewPage = new NewInventoryViewPage(driver, webDriverWait);
        newInventoryViewPage.selectFirstRow();
        waitForPageToLoad();
    }

    @Step("Checking Inventory View title")
    private void checkInventoryViewTitle(String expectedTitle) {
        ToolbarWidget toolbarWidget = ToolbarWidget.create(driver, webDriverWait);
        String actualTitle = toolbarWidget.getViewTitle();

        Assert.assertEquals(actualTitle, expectedTitle);
    }

    @Step("Checking attributes translation in Columns Management for Site")
    private void checkSiteColumnsManagement() {
        NewInventoryViewPage newInventoryViewPage = new NewInventoryViewPage(driver, webDriverWait);
        AttributesChooser attributesChooser = newInventoryViewPage.getMainTable().getAttributesChooser();
        attributesChooser.expandAttribute(LOCATION_NODE);
        attributesChooser.expandAttribute(ADDRESS_NODE);
        attributesChooser.expandAttribute(AUDIT_INFORMATION_NODE);
        attributesChooser.expandAttribute(BLOCKADE_NODE);
        attributesChooser.expandAttribute(MODEL_NODE);
        attributesChooser.expandAttribute(PHYSICAL_LOCATION_NODE);
        attributesChooser.expandAttribute(RESERVATION_NODE);
        List<String> visibleAttributes = attributesChooser.getVisibleAttributes();

        for (String visibleAttribute : visibleAttributes) {
            softAssert.assertFalse(visibleAttribute.contains(DM_PREFIX), visibleAttribute + ATTRIBUTES_TRANSLATIONS_ERROR_MESSAGE);
        }
        attributesChooser.clickCancel();
    }

    @Step("Checking attributes translation in Columns Management for Router")
    private void checkRouterColumnsManagement() {
        NewInventoryViewPage newInventoryViewPage = new NewInventoryViewPage(driver, webDriverWait);
        AttributesChooser attributesChooser = newInventoryViewPage.getMainTable().getAttributesChooser();
        attributesChooser.expandAttribute(MODEL_NODE);
        attributesChooser.expandAttribute(LOCATION_NODE);
        attributesChooser.expandAttribute(PRECISE_LOCATION_NODE);
        attributesChooser.expandAttribute(LOGICAL_LOCATION_NODE);
        attributesChooser.expandAttribute(MANAGEMENT_SYSTEM_NODE);
        attributesChooser.expandAttribute(AUDIT_INFORMATION_NODE);
        attributesChooser.expandAttribute(BLOCKADE_NODE);
        attributesChooser.expandAttribute(COOLING_ZONE_NODE);
        attributesChooser.expandAttribute(DEVICE_CATEGORY_NODE);
        attributesChooser.expandAttribute(LOGICAL_FUNCTION_NODE);
        attributesChooser.expandAttribute(NETWORK_DOMAIN_NODE);
        attributesChooser.expandAttribute(RESERVATION_NODE);
        List<String> visibleAttributes = attributesChooser.getVisibleAttributes();

        for (String visibleAttribute : visibleAttributes) {
            softAssert.assertFalse(visibleAttribute.contains(DM_PREFIX), visibleAttribute + ATTRIBUTES_TRANSLATIONS_ERROR_MESSAGE);
        }
        attributesChooser.clickCancel();
    }

    @Step("Checking attributes translation in Columns Management for eNodeB")
    private void checkENodeBColumnsManagement() {
        NewInventoryViewPage newInventoryViewPage = new NewInventoryViewPage(driver, webDriverWait);
        AttributesChooser attributesChooser = newInventoryViewPage.getMainTable().getAttributesChooser();
        attributesChooser.expandAttribute(LOCATION_NODE);
        attributesChooser.expandAttribute(MANAGEMENT_SYSTEM_NODE);
        attributesChooser.expandAttribute(AUDIT_INFORMATION_NODE);
        attributesChooser.expandAttribute(MASTER_ELEMENT_NODE);
        attributesChooser.expandAttribute(LOGICAL_LOCATION_NODE);
        attributesChooser.expandAttribute(MASTER_POSITION_ELEMENT_NODE);
        attributesChooser.expandAttribute(MODEL_NODE);
        attributesChooser.expandAttribute(RESOURCE_SPECIFICATION_NODE);
        attributesChooser.expandAttribute(ROOT_RESOURCE_SPECIFICATION_NODE);
        List<String> visibleAttributes = attributesChooser.getVisibleAttributes();

        for (String visibleAttribute : visibleAttributes) {
            softAssert.assertFalse(visibleAttribute.contains(DM_PREFIX), visibleAttribute + ATTRIBUTES_TRANSLATIONS_ERROR_MESSAGE);
        }
        attributesChooser.clickCancel();
    }

    @Step("Checking attributes translation in Columns Management for Ethernet Interface")
    private void checkEthernetInterfaceColumnsManagement() {
        NewInventoryViewPage newInventoryViewPage = new NewInventoryViewPage(driver, webDriverWait);
        AttributesChooser attributesChooser = newInventoryViewPage.getMainTable().getAttributesChooser();
        attributesChooser.expandAttribute(PHYSICAL_DEVICE_NODE);
        attributesChooser.expandAttribute(PORT_NODE);
        attributesChooser.expandAttribute(PLUGGABLE_MODULE_NODE);
        attributesChooser.expandAttribute(LOGICAL_FUNCTION_NODE);
        attributesChooser.expandAttribute(AUDIT_INFORMATION_NODE);
        attributesChooser.expandAttribute(ASSIGNED_TO_LOGICAL_FUNCTIONS_NODE);
        attributesChooser.expandAttribute(BLOCKADE_NODE);
        attributesChooser.expandAttribute(MASTER_LOGICAL_FUNCTION_NODE);
        attributesChooser.expandAttribute(RESERVATION_NODE);
        attributesChooser.expandAttribute(RESOURCE_SPECIFICATION_NODE);
        attributesChooser.expandAttribute(TEMPLATE_NODE);
        List<String> visibleAttributes = attributesChooser.getVisibleAttributes();

        for (String visibleAttribute : visibleAttributes) {
            softAssert.assertFalse(visibleAttribute.contains(DM_PREFIX), visibleAttribute + ATTRIBUTES_TRANSLATIONS_ERROR_MESSAGE);
        }
        attributesChooser.clickCancel();
    }

    @Step("Checking columns headers translation")
    private void checkColumnsHeaders() {
        NewInventoryViewPage newInventoryViewPage = new NewInventoryViewPage(driver, webDriverWait);
        List<String> columnsLabels = newInventoryViewPage.getActiveColumnsHeaders();

        for (String columnsLabel : columnsLabels) {
            softAssert.assertFalse(columnsLabel.contains(DM_PREFIX), columnsLabel + LABEL_TRANSLATIONS_ERROR_MESSAGE);
        }
    }

    @Step("Checking attributes in Property Panel")
    private void checkPropertiesHeaders() {
        NewInventoryViewPage newInventoryViewPage = new NewInventoryViewPage(driver, webDriverWait);
        PropertyPanel propertyPanel = newInventoryViewPage.getPropertyPanel();
        List<String> propertiesLabels = propertyPanel.getPropertyLabels();

        for (String propertiesLabel : propertiesLabels) {
            softAssert.assertFalse(propertiesLabel.contains(DM_PREFIX), propertiesLabel + PROPERTIES_TRANSLATIONS_ERROR_MESSAGE);
        }
    }

    @Step("Checking context actions for Site")
    private void checkSiteContextActions() {
        NewInventoryViewPage newInventoryViewPage = new NewInventoryViewPage(driver, webDriverWait);
        TableWidget tableWidget = newInventoryViewPage.getMainTable();

        String assignExistingRegulatoryLicenseActionLabel = tableWidget.getContextActions().getActionLabel(ActionsContainer.ASSIGN_GROUP_ID, ASSIGN_EXISTING_REGULATORY_LICENSE_ACTION_ID);
        String assignIpv4SubnetActionLabel = tableWidget.getContextActions().getActionLabel(ActionsContainer.ASSIGN_GROUP_ID, ASSIGN_IPV4_SUBNET_TO_SITE_ACTION_ID);
        String assignIpv6SubnetActionLabel = tableWidget.getContextActions().getActionLabel(ActionsContainer.ASSIGN_GROUP_ID, ASSIGN_IPV6_SUBNET_TO_SITE_ACTION_ID);
        String assignNewRegulatoryLicenseActionLabel = tableWidget.getContextActions().getActionLabel(ActionsContainer.ASSIGN_GROUP_ID, ASSIGN_NEW_REGULATORY_LICENSE_ACTION_ID);
        String createConnectionActionLabel = tableWidget.getContextActions().getActionLabel(ActionsContainer.CREATE_GROUP_ID, CREATE_CONNECTION_ACTION_ID);
        String createPhysicalDeviceActionLabel = tableWidget.getContextActions().getActionLabel(ActionsContainer.CREATE_GROUP_ID, CREATE_PHYSICAL_DEVICE_ACTION_ID);
        String createENodeBActionLabel = tableWidget.getContextActions().getActionLabel(ActionsContainer.CREATE_GROUP_ID, CREATE_E_NODE_B_ACTION_ID);
        String deleteLocationActionLabel = tableWidget.getContextActions().getActionLabel(ActionsContainer.EDIT_GROUP_ID, DELETE_LOCATION_ACTION_ID);
        String editLocationActionLabel = tableWidget.getContextActions().getActionLabel(ActionsContainer.EDIT_GROUP_ID, EDIT_LOCATION_ACTION_ID);
        String reserveLocationActionLabel = tableWidget.getContextActions().getActionLabel(ActionsContainer.OTHER_GROUP_ID, RESERVE_LOCATION_ACTION_ID);
        String hierarchyViewActionLabel = tableWidget.getContextActions().getActionLabel(ActionsContainer.SHOW_ON_GROUP_ID, HIERARCHY_VIEW_ACTION_ID);

        softAssert.assertEquals(assignExistingRegulatoryLicenseActionLabel, ASSIGN_EXISTING_REGULATORY_LICENSE_ACTION_LABEL, ASSIGN_EXISTING_REGULATORY_LICENSE_ACTION_LABEL + ACTION_TRANSLATION_ERROR_MESSAGE);
        softAssert.assertEquals(assignIpv4SubnetActionLabel, ASSIGN_IPV4_SUBNET_ACTION_LABEL, ASSIGN_IPV4_SUBNET_ACTION_LABEL + ACTION_TRANSLATION_ERROR_MESSAGE);
        softAssert.assertEquals(assignIpv6SubnetActionLabel, ASSIGN_IPV6_SUBNET_ACTION_LABEL, ASSIGN_IPV6_SUBNET_ACTION_LABEL + ACTION_TRANSLATION_ERROR_MESSAGE);
        softAssert.assertEquals(assignNewRegulatoryLicenseActionLabel, ASSIGN_NEW_REGULATORY_LICENSE_ACTION_LABEL, ASSIGN_NEW_REGULATORY_LICENSE_ACTION_LABEL + ACTION_TRANSLATION_ERROR_MESSAGE);
        softAssert.assertEquals(createConnectionActionLabel, CREATE_CONNECTION_ACTION_LABEL, CREATE_CONNECTION_ACTION_LABEL + ACTION_TRANSLATION_ERROR_MESSAGE);
        softAssert.assertEquals(createPhysicalDeviceActionLabel, CREATE_PHYSICAL_DEVICE_ACTION_LABEL, CREATE_PHYSICAL_DEVICE_ACTION_LABEL + ACTION_TRANSLATION_ERROR_MESSAGE);
        softAssert.assertEquals(createENodeBActionLabel, CREATE_E_NODE_B_ACTION_LABEL, CREATE_E_NODE_B_ACTION_LABEL + ACTION_TRANSLATION_ERROR_MESSAGE);
        softAssert.assertEquals(deleteLocationActionLabel, DELETE_LOCATION_ACTION_LABEL, DELETE_LOCATION_ACTION_LABEL + ACTION_TRANSLATION_ERROR_MESSAGE);
        softAssert.assertEquals(editLocationActionLabel, EDIT_LOCATION_ACTION_LABEL, EDIT_LOCATION_ACTION_LABEL + ACTION_TRANSLATION_ERROR_MESSAGE);
        softAssert.assertEquals(reserveLocationActionLabel, RESERVE_LOCATION_ACTION_LABEL, RESERVE_LOCATION_ACTION_LABEL + ACTION_TRANSLATION_ERROR_MESSAGE);
        softAssert.assertEquals(hierarchyViewActionLabel, HIERARCHY_VIEW_ACTION_LABEL, HIERARCHY_VIEW_ACTION_LABEL + ACTION_TRANSLATION_ERROR_MESSAGE);
    }

    @Step("Checking context actions for Router")
    private void checkRouterContextActions() {
        NewInventoryViewPage newInventoryViewPage = new NewInventoryViewPage(driver, webDriverWait);
        TableWidget tableWidget = newInventoryViewPage.getMainTable();

        String assignIPv4HostAddressActionLabel = tableWidget.getContextActions().getActionLabel(ActionsContainer.ASSIGN_GROUP_ID, ASSIGN_IPV4_HOST_ADDRESS_ACTION_ID);
        String assignIPv4LoopbackAddressActionLabel = tableWidget.getContextActions().getActionLabel(ActionsContainer.ASSIGN_GROUP_ID, ASSIGN_IPV4_LOOPBACK_ADDRESS_ACTION_ID);
        String assignIPv4SubnetActionLabel = tableWidget.getContextActions().getActionLabel(ActionsContainer.ASSIGN_GROUP_ID, ASSIGN_IPV4_SUBNET_TO_ROUTER_ACTION_ID);
        String assignIPv6HostAddressActionLabel = tableWidget.getContextActions().getActionLabel(ActionsContainer.ASSIGN_GROUP_ID, ASSIGN_IPV6_HOST_ADDRESS_ACTION_ID);
        String assignIPv6LoopbackAddressActionLabel = tableWidget.getContextActions().getActionLabel(ActionsContainer.ASSIGN_GROUP_ID, ASSIGN_IPV6_LOOPBACK_ADDRESS_ACTION_ID);
        String assignIPv6SubnetActionLabel = tableWidget.getContextActions().getActionLabel(ActionsContainer.ASSIGN_GROUP_ID, ASSIGN_IPV6_SUBNET_TO_ROUTER_ACTION_ID);
        String assignMultipleIPv4AddressesActionLabel = tableWidget.getContextActions().getActionLabel(ActionsContainer.ASSIGN_GROUP_ID, ASSIGN_MULTIPLE_IPV4_ADDRESSES_ACTION_ID);
        String assignMultipleIPv4LoopbacksActionLabel = tableWidget.getContextActions().getActionLabel(ActionsContainer.ASSIGN_GROUP_ID, ASSIGN_MULTIPLE_IPV4_LOOPBACKS_ACTION_ID);
        String assignMultipleIPv6AddressesActionLabel = tableWidget.getContextActions().getActionLabel(ActionsContainer.ASSIGN_GROUP_ID, ASSIGN_MULTIPLE_IPV6_ADDRESSES_ACTION_ID);
        String assignMultipleIPv6LoopbacksActionLabel = tableWidget.getContextActions().getActionLabel(ActionsContainer.ASSIGN_GROUP_ID, ASSIGN_MULTIPLE_IPV6_LOOPBACKS_ACTION_ID);
        String createConnectionActionLabel = tableWidget.getContextActions().getActionLabel(ActionsContainer.CREATE_GROUP_ID, CREATE_CONNECTION_ACTION_ID);
        String createAggregatedEthernetInterfaceActionLabel = tableWidget.getContextActions().getActionLabel(ActionsContainer.CREATE_GROUP_ID, CREATE_AGGREGATED_ETHERNET_INTERFACE_ACTION_ID);
        String createIrbInterfaceActionLabel = tableWidget.getContextActions().getActionLabel(ActionsContainer.CREATE_GROUP_ID, CREATE_IRB_INTERFACE_ACTION_ID);
        String createLoopbackInterfaceActionLabel = tableWidget.getContextActions().getActionLabel(ActionsContainer.CREATE_GROUP_ID, CREATE_LOOPBACK_INTERFACE_ACTION_ID);
        String createMplsTunnelInterfaceActionLabel = tableWidget.getContextActions().getActionLabel(ActionsContainer.CREATE_GROUP_ID, CREATE_MPLS_TUNNEL_INTERFACE_ACTION_ID);
        String createTrafficClassActionLabel = tableWidget.getContextActions().getActionLabel(ActionsContainer.CREATE_GROUP_ID, CREATE_TRAFFIC_CLASS_ACTION_ID);
        String createTrafficPolicyActionLabel = tableWidget.getContextActions().getActionLabel(ActionsContainer.CREATE_GROUP_ID, CREATE_TRAFFIC_POLICY_ACTION_ID);
        String createVrfActionLabel = tableWidget.getContextActions().getActionLabel(ActionsContainer.CREATE_GROUP_ID, CREATE_VRF_ACTION_ID);
        String createVsiActionLabel = tableWidget.getContextActions().getActionLabel(ActionsContainer.CREATE_GROUP_ID, CREATE_VSI_ACTION_ID);
        String createPluggableModuleActionLabel = tableWidget.getContextActions().getActionLabel(ActionsContainer.CREATE_GROUP_ID, CREATE_PLUGGABLE_MODULE_ACTION_ID);
        String createPortsActionLabel = tableWidget.getContextActions().getActionLabel(ActionsContainer.CREATE_GROUP_ID, CREATE_PORTS_ACTION_ID);
        String createSlotActionLabel = tableWidget.getContextActions().getActionLabel(ActionsContainer.CREATE_GROUP_ID, CREATE_SLOT_ACTION_ID);
        String changeLocationActionLabel = tableWidget.getContextActions().getActionLabel(ActionsContainer.EDIT_GROUP_ID, CHANGE_LOCATION_ACTION_ID);
        String changePhysicalDeviceModelActionLabel = tableWidget.getContextActions().getActionLabel(ActionsContainer.EDIT_GROUP_ID, CHANGE_PHYSICAL_DEVICE_MODEL_ACTION_ID);
        String deletePhysicalDeviceActionLabel = tableWidget.getContextActions().getActionLabel(ActionsContainer.EDIT_GROUP_ID, DELETE_PHYSICAL_DEVICE_ACTION_ID);
        String editPhysicalDeviceActionLabel = tableWidget.getContextActions().getActionLabel(ActionsContainer.EDIT_GROUP_ID, EDIT_PHYSICAL_DEVICE_ACTION_ID);
        String reserveRouterActionLabel = tableWidget.getContextActions().getActionLabel(ActionsContainer.OTHER_GROUP_ID, RESERVE_ROUTER_ACTION_ID);
        String blockRouterActionLabel = tableWidget.getContextActions().getActionLabel(ActionsContainer.OTHER_GROUP_ID, BLOCK_ROUTER_ACTION_ID);
        String hierarchyViewActionLabel = tableWidget.getContextActions().getActionLabel(ActionsContainer.SHOW_ON_GROUP_ID, HIERARCHY_VIEW_ACTION_ID);

        softAssert.assertEquals(assignIPv4HostAddressActionLabel, ASSIGN_IPV4_HOST_ADDRESS_ACTION_LABEL, ASSIGN_IPV4_HOST_ADDRESS_ACTION_LABEL + ACTION_TRANSLATION_ERROR_MESSAGE);
        softAssert.assertEquals(assignIPv4LoopbackAddressActionLabel, ASSIGN_IPV4_LOOPBACK_ADDRESS_ACTION_LABEL, ASSIGN_IPV4_LOOPBACK_ADDRESS_ACTION_LABEL + ACTION_TRANSLATION_ERROR_MESSAGE);
        softAssert.assertEquals(assignIPv4SubnetActionLabel, ASSIGN_IPV4_SUBNET_ACTION_LABEL, ASSIGN_IPV4_SUBNET_ACTION_LABEL + ACTION_TRANSLATION_ERROR_MESSAGE);
        softAssert.assertEquals(assignIPv6HostAddressActionLabel, ASSIGN_IPV6_HOST_ADDRESS_ACTION_LABEL, ASSIGN_IPV6_HOST_ADDRESS_ACTION_LABEL + ACTION_TRANSLATION_ERROR_MESSAGE);
        softAssert.assertEquals(assignIPv6LoopbackAddressActionLabel, ASSIGN_IPV6_LOOPBACK_ADDRESS_ACTION_LABEL, ASSIGN_IPV6_LOOPBACK_ADDRESS_ACTION_LABEL + ACTION_TRANSLATION_ERROR_MESSAGE);
        softAssert.assertEquals(assignIPv6SubnetActionLabel, ASSIGN_IPV6_SUBNET_ACTION_LABEL, ASSIGN_IPV6_SUBNET_ACTION_LABEL + ACTION_TRANSLATION_ERROR_MESSAGE);
        softAssert.assertEquals(assignMultipleIPv4AddressesActionLabel, ASSIGN_MULTIPLE_IPV4_ADDRESSES_ACTION_LABEL, ASSIGN_MULTIPLE_IPV4_ADDRESSES_ACTION_LABEL + ACTION_TRANSLATION_ERROR_MESSAGE);
        softAssert.assertEquals(assignMultipleIPv4LoopbacksActionLabel, ASSIGN_MULTIPLE_IPV4_LOOPBACKS_ACTION_LABEL, ASSIGN_MULTIPLE_IPV4_LOOPBACKS_ACTION_LABEL + ACTION_TRANSLATION_ERROR_MESSAGE);
        softAssert.assertEquals(assignMultipleIPv6AddressesActionLabel, ASSIGN_MULTIPLE_IPV6_ADDRESSES_ACTION_LABEL, ASSIGN_MULTIPLE_IPV6_ADDRESSES_ACTION_LABEL + ACTION_TRANSLATION_ERROR_MESSAGE);
        softAssert.assertEquals(assignMultipleIPv6LoopbacksActionLabel, ASSIGN_MULTIPLE_IPV6_LOOPBACKS_ACTION_LABEL, ASSIGN_MULTIPLE_IPV6_LOOPBACKS_ACTION_LABEL + ACTION_TRANSLATION_ERROR_MESSAGE);
        softAssert.assertEquals(createConnectionActionLabel, CREATE_CONNECTION_ACTION_LABEL, CREATE_CONNECTION_ACTION_LABEL + ACTION_TRANSLATION_ERROR_MESSAGE);
        softAssert.assertEquals(createAggregatedEthernetInterfaceActionLabel, CREATE_AGGREGATED_ETHERNET_INTERFACE_ACTION_LABEL, CREATE_AGGREGATED_ETHERNET_INTERFACE_ACTION_LABEL + ACTION_TRANSLATION_ERROR_MESSAGE);
        softAssert.assertEquals(createIrbInterfaceActionLabel, CREATE_IRB_INTERFACE_ACTION_LABEL, CREATE_IRB_INTERFACE_ACTION_LABEL + ACTION_TRANSLATION_ERROR_MESSAGE);
        softAssert.assertEquals(createLoopbackInterfaceActionLabel, CREATE_LOOPBACK_INTERFACE_ACTION_LABEL, CREATE_LOOPBACK_INTERFACE_ACTION_LABEL + ACTION_TRANSLATION_ERROR_MESSAGE);
        softAssert.assertEquals(createMplsTunnelInterfaceActionLabel, CREATE_MPLS_TUNNEL_INTERFACE_ACTION_LABEL, CREATE_MPLS_TUNNEL_INTERFACE_ACTION_LABEL + ACTION_TRANSLATION_ERROR_MESSAGE);
        softAssert.assertEquals(createTrafficClassActionLabel, CREATE_TRAFFIC_CLASS_ACTION_LABEL, CREATE_TRAFFIC_CLASS_ACTION_LABEL + ACTION_TRANSLATION_ERROR_MESSAGE);
        softAssert.assertEquals(createTrafficPolicyActionLabel, CREATE_TRAFFIC_POLICY_ACTION_LABEL, CREATE_TRAFFIC_POLICY_ACTION_LABEL + ACTION_TRANSLATION_ERROR_MESSAGE);
        softAssert.assertEquals(createVrfActionLabel, CREATE_VRF_ACTION_LABEL, CREATE_VRF_ACTION_LABEL + ACTION_TRANSLATION_ERROR_MESSAGE);
        softAssert.assertEquals(createVsiActionLabel, CREATE_VSI_ACTION_LABEL, CREATE_VSI_ACTION_LABEL + ACTION_TRANSLATION_ERROR_MESSAGE);
        softAssert.assertEquals(createPluggableModuleActionLabel, CREATE_PLUGGABLE_MODULE_ACTION_LABEL, CREATE_PLUGGABLE_MODULE_ACTION_LABEL + ACTION_TRANSLATION_ERROR_MESSAGE);
        softAssert.assertEquals(createPortsActionLabel, CREATE_PORTS_ACTION_LABEL, CREATE_PORTS_ACTION_LABEL + ACTION_TRANSLATION_ERROR_MESSAGE);
        softAssert.assertEquals(createSlotActionLabel, CREATE_SLOT_ACTION_LABEL, CREATE_SLOT_ACTION_LABEL + ACTION_TRANSLATION_ERROR_MESSAGE);
        softAssert.assertEquals(changeLocationActionLabel, CHANGE_LOCATION_ACTION_LABEL, CHANGE_LOCATION_ACTION_LABEL + ACTION_TRANSLATION_ERROR_MESSAGE);
        softAssert.assertEquals(changePhysicalDeviceModelActionLabel, CHANGE_PHYSICAL_DEVICE_MODEL_ACTION_LABEL, CHANGE_PHYSICAL_DEVICE_MODEL_ACTION_LABEL + ACTION_TRANSLATION_ERROR_MESSAGE);
        softAssert.assertEquals(deletePhysicalDeviceActionLabel, DELETE_PHYSICAL_DEVICE_ACTION_LABEL, DELETE_PHYSICAL_DEVICE_ACTION_LABEL + ACTION_TRANSLATION_ERROR_MESSAGE);
        softAssert.assertEquals(editPhysicalDeviceActionLabel, EDIT_PHYSICAL_DEVICE_ACTION_LABEL, EDIT_PHYSICAL_DEVICE_ACTION_LABEL + ACTION_TRANSLATION_ERROR_MESSAGE);
        softAssert.assertEquals(reserveRouterActionLabel, RESERVE_ROUTER_ACTION_LABEL, RESERVE_ROUTER_ACTION_LABEL + ACTION_TRANSLATION_ERROR_MESSAGE);
        softAssert.assertEquals(blockRouterActionLabel, BLOCK_ROUTER_ACTION_LABEL, BLOCK_ROUTER_ACTION_LABEL + ACTION_TRANSLATION_ERROR_MESSAGE);
        softAssert.assertEquals(hierarchyViewActionLabel, HIERARCHY_VIEW_ACTION_LABEL, HIERARCHY_VIEW_ACTION_LABEL + ACTION_TRANSLATION_ERROR_MESSAGE);
    }

    @Step("Checking context actions for eNodeB")
    private void checkENodeBContextActions() {
        NewInventoryViewPage newInventoryViewPage = new NewInventoryViewPage(driver, webDriverWait);
        TableWidget tableWidget = newInventoryViewPage.getMainTable();

        String assignIPv4HostAddressActionLabel = tableWidget.getContextActions().getActionLabel(ActionsContainer.ASSIGN_GROUP_ID, ASSIGN_IPV4_HOST_ADDRESS_ACTION_ID);
        String assignIPv4LoopbackAddressActionLabel = tableWidget.getContextActions().getActionLabel(ActionsContainer.ASSIGN_GROUP_ID, ASSIGN_IPV4_LOOPBACK_ADDRESS_ACTION_ID);
        String assignIPv4SubnetActionLabel = tableWidget.getContextActions().getActionLabel(ActionsContainer.ASSIGN_GROUP_ID, ASSIGN_IPV4_SUBNET_TO_LOGICAL_FUNCTION_ACTION_ID);
        String assignIPv6HostAddressActionLabel = tableWidget.getContextActions().getActionLabel(ActionsContainer.ASSIGN_GROUP_ID, ASSIGN_IPV6_HOST_ADDRESS_ACTION_ID);
        String assignIPv6LoopbackAddressActionLabel = tableWidget.getContextActions().getActionLabel(ActionsContainer.ASSIGN_GROUP_ID, ASSIGN_IPV6_LOOPBACK_ADDRESS_ACTION_ID);
        String assignIPv6SubnetActionLabel = tableWidget.getContextActions().getActionLabel(ActionsContainer.ASSIGN_GROUP_ID, ASSIGN_IPV6_SUBNET_TO_LOGICAL_FUNCTION_ACTION_ID);
        String assignMultipleIPv4AddressesActionLabel = tableWidget.getContextActions().getActionLabel(ActionsContainer.ASSIGN_GROUP_ID, ASSIGN_MULTIPLE_IPV4_ADDRESSES_ACTION_ID);
        String assignMultipleIPv4LoopbacksActionLabel = tableWidget.getContextActions().getActionLabel(ActionsContainer.ASSIGN_GROUP_ID, ASSIGN_MULTIPLE_IPV4_LOOPBACKS_ACTION_ID);
        String assignMultipleIPv6AddressesActionLabel = tableWidget.getContextActions().getActionLabel(ActionsContainer.ASSIGN_GROUP_ID, ASSIGN_MULTIPLE_IPV6_ADDRESSES_ACTION_ID);
        String assignMultipleIPv6LoopbacksActionLabel = tableWidget.getContextActions().getActionLabel(ActionsContainer.ASSIGN_GROUP_ID, ASSIGN_MULTIPLE_IPV6_LOOPBACKS_ACTION_ID);
        String createConnectionActionLabel = tableWidget.getContextActions().getActionLabel(ActionsContainer.CREATE_GROUP_ID, CREATE_CONNECTION_ON_LF_ACTION_ID);
        String createEthernetInterfaceActionLabel = tableWidget.getContextActions().getActionLabel(ActionsContainer.CREATE_GROUP_ID, CREATE_ETHERNET_INTERFACE_ACTION_ID);
        String createIrbInterfaceActionLabel = tableWidget.getContextActions().getActionLabel(ActionsContainer.CREATE_GROUP_ID, CREATE_IRB_INTERFACE_ACTION_ID);
        String createLoopbackInterfaceActionLabel = tableWidget.getContextActions().getActionLabel(ActionsContainer.CREATE_GROUP_ID, CREATE_LOOPBACK_INTERFACE_ACTION_ID);
        String createTrafficClassActionLabel = tableWidget.getContextActions().getActionLabel(ActionsContainer.CREATE_GROUP_ID, CREATE_TRAFFIC_CLASS_ACTION_ID);
        String createTrafficPolicyActionLabel = tableWidget.getContextActions().getActionLabel(ActionsContainer.CREATE_GROUP_ID, CREATE_TRAFFIC_POLICY_ACTION_ID);
        String deleteENodeBActionLabel = tableWidget.getContextActions().getActionLabel(ActionsContainer.EDIT_GROUP_ID, DELETE_E_NODE_B_ACTION_ID);
        String editENodeBActionLabel = tableWidget.getContextActions().getActionLabel(ActionsContainer.EDIT_GROUP_ID, EDIT_E_NODE_B_ACTION_ID);
        String downloadFileToSoftwareRepositoryActionLabel = tableWidget.getContextActions().getActionLabel(ActionsContainer.OTHER_GROUP_ID, DOWNLOAD_FILE_TO_SOFTWARE_REPOSITORY_ACTION_ID);
        String hierarchyViewActionLabel = tableWidget.getContextActions().getActionLabel(ActionsContainer.SHOW_ON_GROUP_ID, HIERARCHY_VIEW_ACTION_ID);

        softAssert.assertEquals(assignIPv4HostAddressActionLabel, ASSIGN_IPV4_HOST_ADDRESS_ACTION_LABEL, ASSIGN_IPV4_HOST_ADDRESS_ACTION_LABEL + ACTION_TRANSLATION_ERROR_MESSAGE);
        softAssert.assertEquals(assignIPv4LoopbackAddressActionLabel, ASSIGN_IPV4_LOOPBACK_ADDRESS_ACTION_LABEL, ASSIGN_IPV4_LOOPBACK_ADDRESS_ACTION_LABEL + ACTION_TRANSLATION_ERROR_MESSAGE);
        softAssert.assertEquals(assignIPv4SubnetActionLabel, ASSIGN_IPV4_SUBNET_ACTION_LABEL, ASSIGN_IPV4_SUBNET_ACTION_LABEL + ACTION_TRANSLATION_ERROR_MESSAGE);
        softAssert.assertEquals(assignIPv6HostAddressActionLabel, ASSIGN_IPV6_HOST_ADDRESS_ACTION_LABEL, ASSIGN_IPV6_HOST_ADDRESS_ACTION_LABEL + ACTION_TRANSLATION_ERROR_MESSAGE);
        softAssert.assertEquals(assignIPv6LoopbackAddressActionLabel, ASSIGN_IPV6_LOOPBACK_ADDRESS_ACTION_LABEL, ASSIGN_IPV6_LOOPBACK_ADDRESS_ACTION_LABEL + ACTION_TRANSLATION_ERROR_MESSAGE);
        softAssert.assertEquals(assignIPv6SubnetActionLabel, ASSIGN_IPV6_SUBNET_ACTION_LABEL, ASSIGN_IPV6_SUBNET_ACTION_LABEL + ACTION_TRANSLATION_ERROR_MESSAGE);
        softAssert.assertEquals(assignMultipleIPv4AddressesActionLabel, ASSIGN_MULTIPLE_IPV4_ADDRESSES_ACTION_LABEL, ASSIGN_MULTIPLE_IPV4_ADDRESSES_ACTION_LABEL + ACTION_TRANSLATION_ERROR_MESSAGE);
        softAssert.assertEquals(assignMultipleIPv4LoopbacksActionLabel, ASSIGN_MULTIPLE_IPV4_LOOPBACKS_ACTION_LABEL, ASSIGN_MULTIPLE_IPV4_LOOPBACKS_ACTION_LABEL + ACTION_TRANSLATION_ERROR_MESSAGE);
        softAssert.assertEquals(assignMultipleIPv6AddressesActionLabel, ASSIGN_MULTIPLE_IPV6_ADDRESSES_ACTION_LABEL, ASSIGN_MULTIPLE_IPV6_ADDRESSES_ACTION_LABEL + ACTION_TRANSLATION_ERROR_MESSAGE);
        softAssert.assertEquals(assignMultipleIPv6LoopbacksActionLabel, ASSIGN_MULTIPLE_IPV6_LOOPBACKS_ACTION_LABEL, ASSIGN_MULTIPLE_IPV6_LOOPBACKS_ACTION_LABEL + ACTION_TRANSLATION_ERROR_MESSAGE);
        softAssert.assertEquals(createConnectionActionLabel, CREATE_CONNECTION_ACTION_LABEL, CREATE_CONNECTION_ACTION_LABEL + ACTION_TRANSLATION_ERROR_MESSAGE);
        softAssert.assertEquals(createEthernetInterfaceActionLabel, CREATE_ETHERNET_INTERFACE_ACTION_LABEL, CREATE_ETHERNET_INTERFACE_ACTION_LABEL + ACTION_TRANSLATION_ERROR_MESSAGE);
        softAssert.assertEquals(createIrbInterfaceActionLabel, CREATE_IRB_INTERFACE_ACTION_LABEL, CREATE_IRB_INTERFACE_ACTION_LABEL + ACTION_TRANSLATION_ERROR_MESSAGE);
        softAssert.assertEquals(createLoopbackInterfaceActionLabel, CREATE_LOOPBACK_INTERFACE_ACTION_LABEL, CREATE_LOOPBACK_INTERFACE_ACTION_LABEL + ACTION_TRANSLATION_ERROR_MESSAGE);
        softAssert.assertEquals(createTrafficClassActionLabel, CREATE_TRAFFIC_CLASS_ACTION_LABEL, CREATE_TRAFFIC_CLASS_ACTION_LABEL + ACTION_TRANSLATION_ERROR_MESSAGE);
        softAssert.assertEquals(createTrafficPolicyActionLabel, CREATE_TRAFFIC_POLICY_ACTION_LABEL, CREATE_TRAFFIC_POLICY_ACTION_LABEL + ACTION_TRANSLATION_ERROR_MESSAGE);
        softAssert.assertEquals(deleteENodeBActionLabel, DELETE_E_NODE_B_ACTION_LABEL, DELETE_E_NODE_B_ACTION_LABEL + ACTION_TRANSLATION_ERROR_MESSAGE);
        softAssert.assertEquals(editENodeBActionLabel, EDIT_E_NODE_B_ACTION_LABEL, EDIT_E_NODE_B_ACTION_LABEL + ACTION_TRANSLATION_ERROR_MESSAGE);
        softAssert.assertEquals(downloadFileToSoftwareRepositoryActionLabel, DOWNLOAD_FILE_TO_SOFTWARE_REPOSITORY_ACTION_LABEL, DOWNLOAD_FILE_TO_SOFTWARE_REPOSITORY_ACTION_LABEL + ACTION_TRANSLATION_ERROR_MESSAGE);
        softAssert.assertEquals(hierarchyViewActionLabel, HIERARCHY_VIEW_ACTION_LABEL, HIERARCHY_VIEW_ACTION_LABEL + ACTION_TRANSLATION_ERROR_MESSAGE);
    }

    @Step("Checking context actions for Ethernet Interface")
    private void checkEthernetInterfaceContextActions() {
        NewInventoryViewPage newInventoryViewPage = new NewInventoryViewPage(driver, webDriverWait);
        TableWidget tableWidget = newInventoryViewPage.getMainTable();

        String assignIPv4HostAddressActionLabel = tableWidget.getContextActions().getActionLabel(ActionsContainer.ASSIGN_GROUP_ID, ASSIGN_IPV4_HOST_ADDRESS_ACTION_ID);
        String assignIPv4LoopbackAddressActionLabel = tableWidget.getContextActions().getActionLabel(ActionsContainer.ASSIGN_GROUP_ID, ASSIGN_IPV4_LOOPBACK_ADDRESS_ACTION_ID);
        String assignIPv6HostAddressActionLabel = tableWidget.getContextActions().getActionLabel(ActionsContainer.ASSIGN_GROUP_ID, ASSIGN_IPV6_HOST_ADDRESS_ACTION_ID);
        String assignIPv6LoopbackAddressActionLabel = tableWidget.getContextActions().getActionLabel(ActionsContainer.ASSIGN_GROUP_ID, ASSIGN_IPV6_LOOPBACK_ADDRESS_ACTION_ID);
        String assignMultipleIPv4AddressesActionLabel = tableWidget.getContextActions().getActionLabel(ActionsContainer.ASSIGN_GROUP_ID, ASSIGN_MULTIPLE_IPV4_ADDRESSES_ACTION_ID);
        String assignMultipleIPv4LoopbacksActionLabel = tableWidget.getContextActions().getActionLabel(ActionsContainer.ASSIGN_GROUP_ID, ASSIGN_MULTIPLE_IPV4_LOOPBACKS_ACTION_ID);
        String assignMultipleIPv6AddressesActionLabel = tableWidget.getContextActions().getActionLabel(ActionsContainer.ASSIGN_GROUP_ID, ASSIGN_MULTIPLE_IPV6_ADDRESSES_ACTION_ID);
        String assignMultipleIPv6LoopbacksActionLabel = tableWidget.getContextActions().getActionLabel(ActionsContainer.ASSIGN_GROUP_ID, ASSIGN_MULTIPLE_IPV6_LOOPBACKS_ACTION_ID);
        String createIpRedundancyGroupConfigActionLabel = tableWidget.getContextActions().getActionLabel(ActionsContainer.CREATE_GROUP_ID, CREATE_IP_REDUNDANCY_GROUP_CONFIG_ACTION_ID);
        String createMplsInterfaceActionLabel = tableWidget.getContextActions().getActionLabel(ActionsContainer.CREATE_GROUP_ID, CREATE_MPLS_INTERFACE_ACTION_ID);
        String createVlanInterfaceActionLabel = tableWidget.getContextActions().getActionLabel(ActionsContainer.CREATE_GROUP_ID, CREATE_VLAN_INTERFACE_ACTION_ID);
        String deleteEthernetInterfaceActionLabel = tableWidget.getContextActions().getActionLabel(ActionsContainer.EDIT_GROUP_ID, DELETE_ETHERNET_INTERFACE_ACTION_ID);
        String editEthernetInterfaceActionLabel = tableWidget.getContextActions().getActionLabel(ActionsContainer.EDIT_GROUP_ID, EDIT_ETHERNET_INTERFACE_ACTION_ID);
        String inventoryViewActionLabel = tableWidget.getContextActions().getActionLabel(ActionsContainer.SHOW_ON_GROUP_ID, INVENTORY_VIEW_ACTION_ID);

        softAssert.assertEquals(assignIPv4HostAddressActionLabel, ASSIGN_IPV4_HOST_ADDRESS_ACTION_LABEL, ASSIGN_IPV4_HOST_ADDRESS_ACTION_LABEL + ACTION_TRANSLATION_ERROR_MESSAGE);
        softAssert.assertEquals(assignIPv4LoopbackAddressActionLabel, ASSIGN_IPV4_LOOPBACK_ADDRESS_ACTION_LABEL, ASSIGN_IPV4_LOOPBACK_ADDRESS_ACTION_LABEL + ACTION_TRANSLATION_ERROR_MESSAGE);
        softAssert.assertEquals(assignIPv6HostAddressActionLabel, ASSIGN_IPV6_HOST_ADDRESS_ACTION_LABEL, ASSIGN_IPV6_HOST_ADDRESS_ACTION_LABEL + ACTION_TRANSLATION_ERROR_MESSAGE);
        softAssert.assertEquals(assignIPv6LoopbackAddressActionLabel, ASSIGN_IPV6_LOOPBACK_ADDRESS_ACTION_LABEL, ASSIGN_IPV6_LOOPBACK_ADDRESS_ACTION_LABEL + ACTION_TRANSLATION_ERROR_MESSAGE);
        softAssert.assertEquals(assignMultipleIPv4AddressesActionLabel, ASSIGN_MULTIPLE_IPV4_ADDRESSES_ACTION_LABEL, ASSIGN_MULTIPLE_IPV4_ADDRESSES_ACTION_LABEL + ACTION_TRANSLATION_ERROR_MESSAGE);
        softAssert.assertEquals(assignMultipleIPv4LoopbacksActionLabel, ASSIGN_MULTIPLE_IPV4_LOOPBACKS_ACTION_LABEL, ASSIGN_MULTIPLE_IPV4_LOOPBACKS_ACTION_LABEL + ACTION_TRANSLATION_ERROR_MESSAGE);
        softAssert.assertEquals(assignMultipleIPv6AddressesActionLabel, ASSIGN_MULTIPLE_IPV6_ADDRESSES_ACTION_LABEL, ASSIGN_MULTIPLE_IPV6_ADDRESSES_ACTION_LABEL + ACTION_TRANSLATION_ERROR_MESSAGE);
        softAssert.assertEquals(assignMultipleIPv6LoopbacksActionLabel, ASSIGN_MULTIPLE_IPV6_LOOPBACKS_ACTION_LABEL, ASSIGN_MULTIPLE_IPV6_LOOPBACKS_ACTION_LABEL + ACTION_TRANSLATION_ERROR_MESSAGE);
        softAssert.assertEquals(createIpRedundancyGroupConfigActionLabel, CREATE_IP_REDUNDANCY_GROUP_CONFIG_ACTION_LABEL, CREATE_IP_REDUNDANCY_GROUP_CONFIG_ACTION_LABEL + ACTION_TRANSLATION_ERROR_MESSAGE);
        softAssert.assertEquals(createMplsInterfaceActionLabel, CREATE_MPLS_INTERFACE_ACTION_LABEL, CREATE_MPLS_INTERFACE_ACTION_LABEL + ACTION_TRANSLATION_ERROR_MESSAGE);
        softAssert.assertEquals(createVlanInterfaceActionLabel, CREATE_VLAN_INTERFACE_ACTION_LABEL, CREATE_VLAN_INTERFACE_ACTION_LABEL + ACTION_TRANSLATION_ERROR_MESSAGE);
        softAssert.assertEquals(deleteEthernetInterfaceActionLabel, DELETE_ETHERNET_INTERFACE_ACTION_LABEL, DELETE_ETHERNET_INTERFACE_ACTION_LABEL + ACTION_TRANSLATION_ERROR_MESSAGE);
        softAssert.assertEquals(editEthernetInterfaceActionLabel, EDIT_ETHERNET_INTERFACE_ACTION_LABEL, EDIT_ETHERNET_INTERFACE_ACTION_LABEL + ACTION_TRANSLATION_ERROR_MESSAGE);
        softAssert.assertEquals(inventoryViewActionLabel, INVENTORY_VIEW_ACTION_LABEL, INVENTORY_VIEW_ACTION_LABEL + ACTION_TRANSLATION_ERROR_MESSAGE);
    }

    @Step("Waiting for page to load")
    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Step("Checking if error page is shown")
    private void checkErrorPage() {
        ErrorCard errorCard = ErrorCard.create(driver, webDriverWait);
        if (errorCard.isErrorPagePresent()) {
            ErrorCard.ErrorInformation errorInformation = errorCard.getErrorInformation();
            LOGGER.error(errorInformation.getErrorText());
            LOGGER.error(errorInformation.getErrorDescription());
            LOGGER.error(errorInformation.getErrorMessage());
            Assert.fail("Error Page is shown.");
        }
    }

    @Step("Checking global notifications")
    private void checkGlobalNotificationContainer() {
        GlobalNotificationContainer globalNotificationContainer = GlobalNotificationContainer.create(driver, webDriverWait);
        if (globalNotificationContainer.isErrorNotificationPresent()) {
            GlobalNotificationContainer.NotificationInformation information = globalNotificationContainer.getNotificationInformation();
            LOGGER.error(information.getMessage());
            Assert.fail("Global Notification shows error.");
        }
    }
}

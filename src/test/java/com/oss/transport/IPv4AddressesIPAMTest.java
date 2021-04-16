package com.oss.transport;
import com.oss.BaseTestCase;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.propertypanel.PropertyPanel;
import com.oss.pages.bpm.ProcessInstancesPage;
import com.oss.pages.bpm.ProcessWizardPage;
import com.oss.pages.bpm.TasksPage;
import com.oss.pages.platform.NewInventoryViewPage;
import com.oss.pages.transport.ipam.IPAddressAssignmentWizardPage;
import com.oss.pages.transport.ipam.IPAddressManagementViewPage;
import com.oss.pages.transport.ipam.IPSubnetWizardPage;
import com.oss.pages.transport.ipam.RoleViewPage;
import com.oss.pages.transport.ipam.helper.IPAddressAssignmentWizardProperties;
import com.oss.pages.transport.ipam.helper.IPSubnetFilterProperties;
import com.oss.pages.transport.ipam.helper.IPSubnetWizardProperties;
import com.oss.utils.TestListener;
import io.qameta.allure.Description;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.oss.framework.components.inputs.Input.ComponentType.*;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.*;

/**
 * @author Ewa Frączek
 */

@Listeners({ TestListener.class })
public class IPv4AddressesIPAMTest extends BaseTestCase {
    protected IPAddressManagementViewPage ipAddressManagementViewPage;
    protected NewInventoryViewPage newInventoryViewPage;
    private TasksPage tasksPage;
    private String processNRPCode;

    private static final String NETWORK_NAME = "IPv4IPAMSeleniumTest1";
    private static final String NETWORK_NAME_UPDATED = "IPv4IPAMSeleniumTestUpdated1";
    private static final String ROLE_NAME = "IPv4IPAMSeleniumTest1";
    private static final String ROLE_NAME_UPDATED = "IPv4IPAMSeleniumTestUpdated1";
    private static final String STANDARD_ROLE = "Standard";
    private static final String MANAGEMENT_SECONDARY_ROLE = "Management - Secondary";
    private static final int AMOUNT_OF_SUBNETS_SELECTED_DURING_SUBNET_CREATION = 3;
    private static final int AMOUNT_OF_SUBNETS_SELECTED_DURING_SUBNET_SPLIT = 2;
    private static final int AMOUNT_OF_SUBNETS_SELECTED_DURING_SUBNET_MERGE = 3;
    private static final String DESCRIPTION = "Description";
    private static final String DESCRIPTION_UPDATED = "DescriptionUpdated";
    private static final String NETWORK_SUBNET_TYPE = "Network";
    private static final String BLOCK_SUBNET_TYPE = "Block";
    private static final String SUBNETS_ADDRESS = "126.0.0.0";
    private static final String FILTER_SUBNETS_START_IP = "126000";
    private static final String FILTER_SUBNETS_END_IP_FOR_CREATION = "12600128";
    private static final String FILTER_SUBNETS_END_IP_FOR_SPLIT = "126000";
    private static final String OPERATOR_HIGHER_OR_EQUAL = ">=";
    private static final String LOWEST_IP_SUBNET_MASK = "24";
    private static final String HIGHER_IP_SUBNET_MASK = "25";
    private static final String ASSIGNMENT_LOCATION_NAME = "IPAMSeleniumTest";
    private static final String ASSIGNMENT_DEVICE_NAME = "IPAMSeleniumTest";
    private static final String ASSIGNMENT_INTERFACE_NAME = "IPAMSeleniumTestFirstInterface";
    private static final String ASSIGNMENT_LOCATION_IDENTIFIER = "IPAMSeleniumTest-BU2"; //"IPSWICH-BU1"; //"Cracow-BU2";
    private static final String ASSIGNMENT_DEVICE_IDENTIFIER = "-Router-7"; //"IPSWICH-BU1"; //"Cracow-BU2";
    private static final String ASSIGNMENT_INTERFACE_IDENTIFIER = "-Router-7\\CLUSTER 0"; //"IPSWICH-BU1"; //"Cracow-BU2";
    private static final String ASSIGNMENT_SECOND_INTERFACE_IDENTIFIER = "-Router-7\\CLUSTER 1"; //"IPSWICH-BU1"; //"Cracow-BU2";
    private static final String HOST_ADDRESS = "126.0.0.1";
    private static final String LOOPBACK_HOST_ADDRESS = "126.0.0.0";
    private static final String SECOND_LOOPBACK_HOST_ADDRESS = "126.0.0.2";
    private static final String LOOPBACK_IPV4_HOST_MASK = "32";
    private static final String TRUE_STRING = String.valueOf(true);
    private static final String FALSE_STRING = String.valueOf(false);
    private static final String LOOPBACK_HOST_ASSIGNMENT_ADDRESS = "126000";
    private static final String RESERVED_STATUS = "Reserved";
    private static final String ASSIGNED_STATUS = "Assigned";
    private static final String BLOCK_TYPE = "BLOCK";
    private static final String NETWORK_TYPE = "NETWORK";

    private static final String NETWORK_INVENTORY_TYPE = "IPNetwork";
    private static final String IPV4_SUBNET_INVENTORY_TYPE = "IPv4Subnet";
    private static final String IPV4_HOST_INVENTORY_TYPE = "IPv4HostAddress";
    private static final String IP_SUBNET_ASSIGNMENT_INVENTORY_TYPE = "IPSubnetAssignment";
    private static final String NETWORK_INVENTORY_PROPERTY_PANEL_ID = "InventoryView_DetailsTab_IPNetwork";
    private static final String SUBNET_INVENTORY_PROPERTY_PANEL_ID = "InventoryView_DetailsTab_IPSubnet";
    private static final String SUBNET_ASSIGNMENT_INVENTORY_PROPERTY_PANEL_ID = "InventoryView_DetailsTab_IPSubnetAssignment";
    private static final String HOST_INVENTORY_PROPERTY_PANEL_ID = "InventoryView_DetailsTab_IPHostAddress";
    private static final int FIRST_INVENTORY_VIEW_ROW_ID = 0;
    private static final int SECOND_INVENTORY_VIEW_ROW_ID = 1;
    private static final int THIRD_INVENTORY_VIEW_ROW_ID = 2;

    private static final String INVENTORY_PROPERTY_NAME = "name";
    private static final String INVENTORY_PROPERTY_IDENTIFIER = "identifier";
    private static final String INVENTORY_PROPERTY_ADDRESS = "address";
    private static final String INVENTORY_PROPERTY_HIGHEST_IP_ADDRESS = "highestIPAddress";
    private static final String INVENTORY_PROPERTY_BROADCAST_IP_ADDRESS = "broadcastIPAddress";
    private static final String INVENTORY_PROPERTY_IP_NETWORK = "ipNetwork_OSF";
    private static final String INVENTORY_PROPERTY_MASK = "mask";
    private static final String INVENTORY_PROPERTY_SUBNET_TYPE = "subnetType";
    private static final String INVENTORY_PROPERTY_ROLE = "role";
    private static final String INVENTORY_PROPERTY_PERCENT_FREE = "percentFree";
    private static final String INVENTORY_PROPERTY_DESCRIPTION = "description";
    private static final String INVENTORY_PROPERTY_STATUS = "status";
    private static final String INVENTORY_PROPERTY_IP_SUBNET = "subnet.name";
    private static final String INVENTORY_PROPERTY_DEVICE = "device.name";

    private Map<String, String> firstIPSubnetProperties = new HashMap<>();
    private Map<String, String> secondIPSubnetProperties = new HashMap<>();
    private Map<String, String> thirdIPSubnetProperties = new HashMap<>();
    private Map<String, String> fourthIPSubnetProperties = new HashMap<>();
    private Map<String, String> fifthIPSubnetProperties = new HashMap<>();

    private Map<String, String> loopbackHostAddressProperties = new HashMap<>();
    private Map<String, String> hostAddressProperties = new HashMap<>();
    private Map<String, String> secondLoopbackHostAddressProperties = new HashMap<>();

    private Map<String, String> loopbackHostAssignmentProperties = new HashMap<>();
    private Map<String, String> hostAssignmentProperties = new HashMap<>();
    private Map<String, String> secondLoopbackHostAssignmentProperties = new HashMap<>();

    private Map<String, String> inventoryViewPropertyNamesForSubnets = new HashMap<>();
    private Map<String, String> inventoryViewPropertyNamesForHosts = new HashMap<>();

    private Set<String> propertiesNotVisibleOnNewInventoryView = new HashSet<>();

    @BeforeClass(enabled = false)
    public void prepareTest() {
        prepareData();
        ProcessInstancesPage.goToProcessInstancesPage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 1, enabled = false)
    @Description("Create Process NRP and start HLP")
    public void createAndStartProcessNRP() {
        ProcessWizardPage processWizardPage = new ProcessWizardPage(driver);
        processNRPCode = processWizardPage.createSimpleNRP();
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        tasksPage.startTask(processNRPCode, TasksPage.HIGH_LEVEL_PLANNING_TASK);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 2)
    @Description("Create Role")
    public void createRole() {
        DelayUtils.sleep(2000);
        ipAddressManagementViewPage = IPAddressManagementViewPage.goToIPAddressManagementViewPageLive(driver, BASIC_URL);
//        PerspectiveChooser perspectiveChooser = PerspectiveChooser.create(driver, webDriverWait);
//        perspectiveChooser.setPlanPerspective(processNRPCode);
        RoleViewPage roleViewPage = ipAddressManagementViewPage.openRoleView();
        roleViewPage.createRole(ROLE_NAME);
        Assert.assertTrue(roleViewPage.doesRoleNameExist(ROLE_NAME));
        roleViewPage.exitRoleView();
    }

    @Test(priority = 3)
    @Description("Create IP Network")
    public void createIPNetwork() {
        ipAddressManagementViewPage.createIPNetwork(NETWORK_NAME, DESCRIPTION);

        ipAddressManagementViewPage.selectTreeRow(NETWORK_NAME);
        Assert.assertEquals(ipAddressManagementViewPage.getPropertyValue(NETWORK_PROPERTY_NAME), NETWORK_NAME);
        Assert.assertEquals(ipAddressManagementViewPage.getPropertyValue(NETWORK_PROPERTY_DESCRIPTION), DESCRIPTION);
    }

    @Test(priority = 4)
    @Description("Create IPv4 Subnets")
    public void createIPv4Subnets() {
        IPSubnetWizardPage ipSubnetWizardPage = ipAddressManagementViewPage.createIPv4Subnet();
        IPSubnetFilterProperties subnetFilterProperties = new IPSubnetFilterProperties(FILTER_SUBNETS_START_IP, FILTER_SUBNETS_END_IP_FOR_CREATION, OPERATOR_HIGHER_OR_EQUAL, LOWEST_IP_SUBNET_MASK);
        ipSubnetWizardPage.ipSubnetWizardSelectStep(subnetFilterProperties, AMOUNT_OF_SUBNETS_SELECTED_DURING_SUBNET_CREATION);
        IPSubnetWizardProperties firstIpSubnetWizardProperties = new IPSubnetWizardProperties(BLOCK_SUBNET_TYPE);
        IPSubnetWizardProperties secondIpSubnetWizardProperties = new IPSubnetWizardProperties(NETWORK_SUBNET_TYPE, ROLE_NAME, DESCRIPTION);
        IPSubnetWizardProperties thirdIpSubnetWizardProperties = new IPSubnetWizardProperties(NETWORK_SUBNET_TYPE);
        ipSubnetWizardPage.ipSubnetWizardPropertiesStep(firstIpSubnetWizardProperties, secondIpSubnetWizardProperties, thirdIpSubnetWizardProperties);
        ipSubnetWizardPage.ipSubnetWizardSummaryStep();

        updatePropertiesAfterIPv4SubnetsCreation();
        ipAddressManagementViewPage = new IPAddressManagementViewPage(driver);
        ipAddressManagementViewPage.selectTreeRow(NETWORK_NAME);
        ipAddressManagementViewPage.selectTreeRow(NETWORK_NAME);
        ipAddressManagementViewPage.expandTreeRow(NETWORK_NAME);
        DelayUtils.sleep(100);
        checkAttributesOnIPAMTree(firstIPSubnetProperties, getAddressAndMask(firstIPSubnetProperties));
        ipAddressManagementViewPage.expandTreeRowContains(getAddressAndMask(firstIPSubnetProperties));
        checkAttributesOnIPAMTree(secondIPSubnetProperties, getAddressAndMask(secondIPSubnetProperties));
        checkAttributesOnIPAMTree(thirdIPSubnetProperties, getAddressAndMask(thirdIPSubnetProperties));
    }

    @Test(priority = 5)
    @Description("Assign IPv4 Subnets")
    public void assignIPv4Subnet() {
        ipAddressManagementViewPage
                .assignIPv4Subnet(getAddressAndMask(firstIPSubnetProperties), LOCATION, ASSIGNMENT_LOCATION_NAME, ROLE_NAME);

        updatePropertiesAfterIPv4SubnetAssignment();
        ipAddressManagementViewPage = new IPAddressManagementViewPage(driver);
        DelayUtils.sleep(100);
        checkAttributesOnIPAMTree(firstIPSubnetProperties, getAddressAndMask(firstIPSubnetProperties));
    }

    @Test(priority = 6)
    @Description("Reserve IPv4 Host Addresses")
    public void reserveIPv4Hosts() {
        ipAddressManagementViewPage
                .reserveIPv4HostAddress(getAddressAndMask(secondIPSubnetProperties), HOST_ADDRESS, DESCRIPTION);
        updatePropertiesAfterIPv4HostsReservation();
        ipAddressManagementViewPage = new IPAddressManagementViewPage(driver);
        checkAttributesOnIPAMTree(hostAddressProperties, getAddressAndMask(hostAddressProperties));
        ipAddressManagementViewPage
                .reserveLoopbackIPv4HostAddress(getAddressAndMask(secondIPSubnetProperties), LOOPBACK_HOST_ADDRESS, DESCRIPTION);
        ipAddressManagementViewPage = new IPAddressManagementViewPage(driver);
        checkAttributesOnIPAMTree(loopbackHostAddressProperties, getAddressAndMask(loopbackHostAddressProperties));
        checkAttributesOnIPAMTree(secondIPSubnetProperties, getAddressAndMask(secondIPSubnetProperties));
    }

    @Test(priority = 7)
    @Description("Assign IPv4 Host Addresses")
    public void assignIPv4Hosts() {
        IPAddressAssignmentWizardPage assignLoopbackAddressFromSubnetContext = ipAddressManagementViewPage
                .assignLoopbackIPv4HostAddressFromSubnetContext(getAddressAndMask(secondIPSubnetProperties));
        IPAddressAssignmentWizardProperties loopbackIpAddressAssignmentWizardProperties = IPAddressAssignmentWizardProperties.builder()
                .address(LOOPBACK_HOST_ASSIGNMENT_ADDRESS).wizardMode(AUTOMATIC_MODE).isPrimary(TRUE_STRING).isInNAT(TRUE_STRING).role(ROLE_NAME)
                .assignmentType(INTERFACE).assignmentName(ASSIGNMENT_INTERFACE_NAME).build();
        IPAddressAssignmentWizardProperties oppositeSideLoopbackIpAddressAssignmentWizardProperties = IPAddressAssignmentWizardProperties.builder()
                .isPrimary(TRUE_STRING).isInNAT(TRUE_STRING).role(ROLE_NAME).build();
        assignLoopbackAddressFromSubnetContext.assignIPAddress(loopbackIpAddressAssignmentWizardProperties, oppositeSideLoopbackIpAddressAssignmentWizardProperties);
        updatePropertiesAfterIPv4HostsAssignment();
        ipAddressManagementViewPage = new IPAddressManagementViewPage(driver);
        ipAddressManagementViewPage.selectTreeRowContains(getAssignmentAssignTo(loopbackHostAssignmentProperties));
        ipAddressManagementViewPage.selectTreeRowContains(getAssignmentAssignTo(loopbackHostAssignmentProperties));
        IPAddressAssignmentWizardPage assignAddressFromSubnetContext = ipAddressManagementViewPage
                .assignIPv4HostAddressFromSubnetContext(getAddressAndMask(secondIPSubnetProperties));
        IPAddressAssignmentWizardProperties ipAddressAssignmentWizardProperties = IPAddressAssignmentWizardProperties.builder()
                .address(HOST_ADDRESS).wizardMode(RESERVED_MODE).isPrimary(FALSE_STRING).assignmentType(PHYSICAL_DEVICE).assignmentName(ASSIGNMENT_DEVICE_NAME).build();
        assignAddressFromSubnetContext.assignIPAddress(ipAddressAssignmentWizardProperties);

        ipAddressManagementViewPage = new IPAddressManagementViewPage(driver);
        checkAttributesOnIPAMTree(hostAssignmentProperties, getAssignmentAssignTo(hostAssignmentProperties));
        checkAttributesOnIPAMTree(hostAddressProperties, getAddressAndMask(hostAddressProperties));
        ipAddressManagementViewPage.expandTreeRow(getAddressAndMask(loopbackHostAddressProperties));
        checkAttributesOnIPAMTree(loopbackHostAssignmentProperties, getAssignmentAssignTo(loopbackHostAssignmentProperties));
        checkAttributesOnIPAMTree(loopbackHostAddressProperties, getAddressAndMask(loopbackHostAddressProperties));
        ipAddressManagementViewPage.expandTreeRow(getAddressAndMask(secondLoopbackHostAddressProperties));
        checkAttributesOnIPAMTree(secondLoopbackHostAssignmentProperties, getAssignmentAssignTo(secondLoopbackHostAssignmentProperties));
        checkAttributesOnIPAMTree(secondLoopbackHostAddressProperties, getAddressAndMask(secondLoopbackHostAddressProperties));
        checkAttributesOnIPAMTree(secondIPSubnetProperties, getAddressAndMask(secondIPSubnetProperties));
    }

    @Test(priority = 8)
    @Description("Edit Role")
    public void editRole() {
        RoleViewPage roleViewPage = ipAddressManagementViewPage.openRoleView();
        roleViewPage.editRole(ROLE_NAME, ROLE_NAME_UPDATED);
        Assert.assertTrue(roleViewPage.doesRoleNameExist(ROLE_NAME_UPDATED));
        Assert.assertFalse(roleViewPage.doesRoleNameExist(ROLE_NAME));
        roleViewPage.exitRoleView();

        updatePropertiesAfterRoleEdition();
        ipAddressManagementViewPage.selectTreeRowContains(getAddressAndMask(firstIPSubnetProperties));
        Assert.assertEquals(ipAddressManagementViewPage.getPropertyValue(SUBNET_PROPERTY_ROLE), firstIPSubnetProperties.get(SUBNET_PROPERTY_ROLE));
        ipAddressManagementViewPage.selectTreeRowContains(getAddressAndMask(firstIPSubnetProperties));
        ipAddressManagementViewPage.selectTreeRowContains(getAddressAndMask(secondIPSubnetProperties));
        Assert.assertEquals(ipAddressManagementViewPage.getPropertyValue(SUBNET_PROPERTY_ROLE), secondIPSubnetProperties.get(SUBNET_PROPERTY_ROLE));
        ipAddressManagementViewPage.selectTreeRowContains(getAddressAndMask(secondIPSubnetProperties));
        checkAttributesOnIPAMTree(loopbackHostAssignmentProperties, getAssignmentAssignTo(loopbackHostAssignmentProperties));
        checkAttributesOnIPAMTree(secondLoopbackHostAssignmentProperties, getAssignmentAssignTo(secondLoopbackHostAssignmentProperties));
    }

    @Test(priority = 9)
    @Description("Edit IP Network")
    public void editIPNetwork() {
        ipAddressManagementViewPage.editIPNetwork(NETWORK_NAME, NETWORK_NAME_UPDATED, DESCRIPTION_UPDATED);
        updatePropertiesAfterIPNetworkEdition();

        ipAddressManagementViewPage = new IPAddressManagementViewPage(driver);
        ipAddressManagementViewPage.selectTreeRow(NETWORK_NAME_UPDATED);
        Assert.assertEquals(ipAddressManagementViewPage.getPropertyValue(NETWORK_PROPERTY_NAME), NETWORK_NAME_UPDATED);
        Assert.assertEquals(ipAddressManagementViewPage.getPropertyValue(NETWORK_PROPERTY_DESCRIPTION), DESCRIPTION_UPDATED);

        ipAddressManagementViewPage.selectTreeRow(NETWORK_NAME_UPDATED);
        ipAddressManagementViewPage.expandTreeRow(NETWORK_NAME_UPDATED);
        checkAttributesOnIPAMTree(firstIPSubnetProperties, getAddressAndMask(firstIPSubnetProperties));
        ipAddressManagementViewPage.expandTreeRowContains(getAddressAndMask(firstIPSubnetProperties));
        ipAddressManagementViewPage.expandTreeRowContains(getAddressAndMask(secondIPSubnetProperties));
        checkAttributesOnIPAMTree(secondIPSubnetProperties, getAddressAndMask(secondIPSubnetProperties));
        checkAttributesOnIPAMTree(thirdIPSubnetProperties, getAddressAndMask(thirdIPSubnetProperties));
        checkAttributesOnIPAMTree(hostAddressProperties, getAddressAndMask(hostAddressProperties));
        checkAttributesOnIPAMTree(loopbackHostAddressProperties, getAddressAndMask(loopbackHostAddressProperties));
        checkAttributesOnIPAMTree(secondLoopbackHostAddressProperties, getAddressAndMask(secondLoopbackHostAddressProperties));
        ipAddressManagementViewPage.expandTreeRow(getAddressAndMask(hostAddressProperties));
        ipAddressManagementViewPage.expandTreeRow(getAddressAndMask(loopbackHostAddressProperties));
        ipAddressManagementViewPage.expandTreeRow(getAddressAndMask(secondLoopbackHostAddressProperties));
        checkAttributesOnIPAMTree(hostAssignmentProperties, getAssignmentAssignTo(hostAssignmentProperties));
        checkAttributesOnIPAMTree(loopbackHostAssignmentProperties, getAssignmentAssignTo(loopbackHostAssignmentProperties));
        checkAttributesOnIPAMTree(secondLoopbackHostAssignmentProperties, getAssignmentAssignTo(secondLoopbackHostAssignmentProperties));
    }

    @Test(priority = 10)
    @Description("Split IPv4 Subnet")
    public void splitIPv4Subnet() {
        IPSubnetWizardPage ipSubnetWizardPage = ipAddressManagementViewPage.splitIPv4Subnet(getAddressAndMask(secondIPSubnetProperties));
        IPSubnetFilterProperties subnetFilterProperties = new IPSubnetFilterProperties(FILTER_SUBNETS_START_IP, FILTER_SUBNETS_END_IP_FOR_SPLIT);
        ipSubnetWizardPage.ipSubnetWizardSelectStep(subnetFilterProperties, AMOUNT_OF_SUBNETS_SELECTED_DURING_SUBNET_SPLIT);
        IPSubnetWizardProperties firstIpSubnetWizardProperties = new IPSubnetWizardProperties(BLOCK_SUBNET_TYPE, MANAGEMENT_SECONDARY_ROLE, DESCRIPTION);
        IPSubnetWizardProperties secondIpSubnetWizardProperties = new IPSubnetWizardProperties(NETWORK_SUBNET_TYPE);
        ipSubnetWizardPage.ipSubnetWizardPropertiesStep(firstIpSubnetWizardProperties, secondIpSubnetWizardProperties);
        ipSubnetWizardPage.ipSubnetWizardSummaryStep();

        updatePropertiesAfterIPv4SubnetsSplit();
        ipAddressManagementViewPage = new IPAddressManagementViewPage(driver);
        checkAttributesOnIPAMTree(secondIPSubnetProperties, getAddressAndMask(secondIPSubnetProperties));
        checkAttributesOnIPAMTree(fourthIPSubnetProperties, getAddressAndMask(fourthIPSubnetProperties));
        ipAddressManagementViewPage.expandTreeRowContains(getAddressAndMask(fourthIPSubnetProperties));
        checkAttributesOnIPAMTree(fifthIPSubnetProperties, getAddressAndMask(fifthIPSubnetProperties));
    }

    @Test(priority = 11)
    @Description("Merge IPv4 Subnets")
    public void mergeIPv4Subnets() {
        IPSubnetWizardPage ipSubnetWizardPage = ipAddressManagementViewPage
                .mergeIPv4Subnet(getAddressAndMask(secondIPSubnetProperties),
                        getAddressAndMask(fourthIPSubnetProperties), getAddressAndMask(fifthIPSubnetProperties));
        ipSubnetWizardPage.ipSubnetWizardSelectStep(AMOUNT_OF_SUBNETS_SELECTED_DURING_SUBNET_MERGE);
        IPSubnetWizardProperties firstIpSubnetWizardProperties = new IPSubnetWizardProperties(NETWORK_SUBNET_TYPE);
        ipSubnetWizardPage.ipSubnetWizardPropertiesStep(firstIpSubnetWizardProperties);
        ipSubnetWizardPage.ipSubnetWizardSummaryStep();

        updatePropertiesAfterIPv4SubnetsMerge();
        ipAddressManagementViewPage = new IPAddressManagementViewPage(driver);
        ipAddressManagementViewPage.expandTreeRow(NETWORK_NAME_UPDATED);
        ipAddressManagementViewPage.selectTreeRow(NETWORK_NAME_UPDATED);
        ipAddressManagementViewPage.selectTreeRow(NETWORK_NAME_UPDATED);
        ipAddressManagementViewPage.expandTreeRowContains(getAddressAndMask(firstIPSubnetProperties));
        checkAttributesOnIPAMTree(secondIPSubnetProperties, getAddressAndMask(secondIPSubnetProperties));
    }

    @Test(priority = 12)
    @Description("Edit IPv4 Subnets")
    public void editIPv4Subnets() {
        ipAddressManagementViewPage.changeIPv4SubnetTypeToBlock(getAddressAndMask(thirdIPSubnetProperties));
        ipAddressManagementViewPage.selectTreeRow(NETWORK_NAME_UPDATED);
        ipAddressManagementViewPage.selectTreeRow(NETWORK_NAME_UPDATED);
        ipAddressManagementViewPage.expandTreeRow(NETWORK_NAME_UPDATED);
        ipAddressManagementViewPage.expandTreeRowContains(getAddressAndMask(firstIPSubnetProperties));
        ipAddressManagementViewPage.editIPv4Subnet(getAddressAndMask(secondIPSubnetProperties), MANAGEMENT_SECONDARY_ROLE, DESCRIPTION_UPDATED);

        updatePropertiesAfterIPv4SubnetsEdition();
        checkAttributesOnIPAMTree(secondIPSubnetProperties, getAddressAndMask(secondIPSubnetProperties));
        checkAttributesOnIPAMTree(thirdIPSubnetProperties, getAddressAndMask(thirdIPSubnetProperties));
    }

    @Test(priority = 13)
    @Description("Edit IPv4 Subnet Assignment")
    public void editIPv4SubnetAssignment() {
        ipAddressManagementViewPage.editRoleForIPv4SubnetAssignment(getAddressAndMask(firstIPSubnetProperties), STANDARD_ROLE);

        updatePropertiesAfterIPv4SubnetAssignmentEdition();
        ipAddressManagementViewPage = new IPAddressManagementViewPage(driver);
        checkAttributesOnIPAMTree(firstIPSubnetProperties, getAddressAndMask(firstIPSubnetProperties));
    }

    @Test(priority = 14)
    @Description("Edit IPv4 Host")
    public void editIPv4Host() {
        ipAddressManagementViewPage.expandTreeRowContains(getAddressAndMask(secondIPSubnetProperties));
        ipAddressManagementViewPage.expandTreeRow(getAddressAndMask(hostAddressProperties));
        ipAddressManagementViewPage.deleteHostAssignment(getAssignmentAssignTo(hostAssignmentProperties));
        ipAddressManagementViewPage.expandTreeRow(NETWORK_NAME_UPDATED);
        ipAddressManagementViewPage.expandTreeRowContains(getAddressAndMask(firstIPSubnetProperties));
        ipAddressManagementViewPage.expandTreeRowContains(getAddressAndMask(secondIPSubnetProperties));
        ipAddressManagementViewPage.changeIPv4HostMask(getAddressAndMask(hostAddressProperties), LOOPBACK_IPV4_HOST_MASK);
        updatePropertiesAfterIPv4HostEdition();
        ipAddressManagementViewPage = new IPAddressManagementViewPage(driver);
        checkAttributesOnIPAMTree(hostAddressProperties, getAddressAndMask(hostAddressProperties));
    }

    @Test(priority = 15)
    @Description("Edit IPv4 Host Assignment")
    public void editIPv4HostAssignment() {
        ipAddressManagementViewPage.expandTreeRow(getAddressAndMask(secondLoopbackHostAddressProperties));
        IPAddressAssignmentWizardProperties ipAddressAssignmentWizardProperties = IPAddressAssignmentWizardProperties.builder()
                .isInNAT(TRUE_STRING).role(STANDARD_ROLE).description(DESCRIPTION).build();
        ipAddressManagementViewPage
                .editIPv4HostAssignment(getAssignmentAssignTo(secondLoopbackHostAssignmentProperties), ipAddressAssignmentWizardProperties);
        updatePropertiesAfterIPv4HostAssignmentEdition();
        checkAttributesOnIPAMTree(secondLoopbackHostAssignmentProperties, getAssignmentAssignTo(secondLoopbackHostAssignmentProperties));
    }

    @Test(priority = 16, enabled = false)
    @Description("Move PLAN to LIVE")
    public void movePlanToLive() {
        DelayUtils.sleep(1000);
        tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.completeNRP(processNRPCode);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 17)
    @Description("Check New Inventory View for IP Network")
    public void checkInventoryViewForIPNetwork() {
        newInventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, NETWORK_INVENTORY_TYPE);
        newInventoryViewPage.searchByAttributeValue(INVENTORY_PROPERTY_NAME, NETWORK_NAME_UPDATED, TEXT_FIELD);
        newInventoryViewPage.getMainTable().selectFirstRow();
        DelayUtils.sleep(100);
        PropertyPanel propertyPanel = newInventoryViewPage.getPropertyPanel(FIRST_INVENTORY_VIEW_ROW_ID, NETWORK_INVENTORY_PROPERTY_PANEL_ID);
        Assert.assertEquals(propertyPanel.getPropertyValue(INVENTORY_PROPERTY_NAME), NETWORK_NAME_UPDATED);
        Assert.assertEquals(propertyPanel.getPropertyValue(INVENTORY_PROPERTY_DESCRIPTION), DESCRIPTION_UPDATED);
    }

    @Test(priority = 18, enabled = false)
    @Description("Check New Inventory View for IPv4 Subnets")
    public void checkInventoryViewForIPv4Subnets() {
        newInventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, IPV4_SUBNET_INVENTORY_TYPE);
        newInventoryViewPage.searchByAttributeValue(INVENTORY_PROPERTY_IP_NETWORK, NETWORK_NAME_UPDATED, SEARCH_FIELD);
        PropertyPanel propertyPanelForFirstRow = newInventoryViewPage.getPropertyPanel(FIRST_INVENTORY_VIEW_ROW_ID, SUBNET_INVENTORY_PROPERTY_PANEL_ID);
        checkAttributesOnNewIV(propertyPanelForFirstRow, firstIPSubnetProperties, inventoryViewPropertyNamesForSubnets);
        PropertyPanel propertyPanelForSecondRow = newInventoryViewPage.getPropertyPanel(SECOND_INVENTORY_VIEW_ROW_ID, SUBNET_INVENTORY_PROPERTY_PANEL_ID);
        checkAttributesOnNewIV(propertyPanelForSecondRow, secondIPSubnetProperties, inventoryViewPropertyNamesForSubnets);
        PropertyPanel propertyPanelForThirdRow = newInventoryViewPage.getPropertyPanel(THIRD_INVENTORY_VIEW_ROW_ID, SUBNET_INVENTORY_PROPERTY_PANEL_ID);
        checkAttributesOnNewIV(propertyPanelForThirdRow, thirdIPSubnetProperties, inventoryViewPropertyNamesForSubnets);
    }

    @Test(priority = 19, enabled = false)
    @Description("Check New Inventory View for IP Subnet Assignments")
    public void checkInventoryViewForIPSubnetAssignments() {
        newInventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, IP_SUBNET_ASSIGNMENT_INVENTORY_TYPE);
        newInventoryViewPage.searchByAttributeValue(INVENTORY_PROPERTY_IP_NETWORK, NETWORK_NAME_UPDATED, SEARCH_FIELD);
        PropertyPanel propertyPanelForFirstRow = newInventoryViewPage.getPropertyPanel(FIRST_INVENTORY_VIEW_ROW_ID, SUBNET_ASSIGNMENT_INVENTORY_PROPERTY_PANEL_ID);
        Assert.assertEquals(propertyPanelForFirstRow.getPropertyValue(INVENTORY_PROPERTY_IDENTIFIER), secondIPSubnetProperties.get(SUBNET_PROPERTY_IDENTIFIER));
        Assert.assertEquals(propertyPanelForFirstRow.getPropertyValue(INVENTORY_PROPERTY_IP_SUBNET), secondIPSubnetProperties.get(SUBNET_PROPERTY_IDENTIFIER));
        Assert.assertEquals(propertyPanelForFirstRow.getPropertyValue(INVENTORY_PROPERTY_MASK), secondIPSubnetProperties.get(SUBNET_PROPERTY_MASK_LENGTH));
        Assert.assertEquals(propertyPanelForFirstRow.getPropertyValue(INVENTORY_PROPERTY_DEVICE), secondIPSubnetProperties.get(SUBNET_PROPERTY_ASSIGNED_TO));
        PropertyPanel propertyPanelForSecondRow = newInventoryViewPage.getPropertyPanel(SECOND_INVENTORY_VIEW_ROW_ID, SUBNET_ASSIGNMENT_INVENTORY_PROPERTY_PANEL_ID);
        Assert.assertEquals(propertyPanelForSecondRow.getPropertyValue(INVENTORY_PROPERTY_NAME), NETWORK_NAME);
    }

    @Test(priority = 20, enabled = false)
    @Description("Check New Inventory View for IPv4 Hosts")
    public void checkInventoryViewForIPv4Hosts() {
        newInventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, IPV4_HOST_INVENTORY_TYPE);
        newInventoryViewPage.searchByAttributeValue(INVENTORY_PROPERTY_IP_NETWORK, NETWORK_NAME_UPDATED, TEXT_FIELD);
        PropertyPanel propertyPanelForFirstRow = newInventoryViewPage.getPropertyPanel(FIRST_INVENTORY_VIEW_ROW_ID, HOST_INVENTORY_PROPERTY_PANEL_ID);
        checkAttributesOnNewIV(propertyPanelForFirstRow, loopbackHostAddressProperties, inventoryViewPropertyNamesForHosts);
        PropertyPanel propertyPanelForSecondRow = newInventoryViewPage.getPropertyPanel(SECOND_INVENTORY_VIEW_ROW_ID, HOST_INVENTORY_PROPERTY_PANEL_ID);
        checkAttributesOnNewIV(propertyPanelForSecondRow, hostAddressProperties, inventoryViewPropertyNamesForHosts);
    }

    @Test(priority = 21)
    @Description("Delete IPv4 Hosts Assignment")
    public void deleteIPv4HostsAssignment() {
        ipAddressManagementViewPage = IPAddressManagementViewPage.goToIPAddressManagementViewPageLive(driver, BASIC_URL);
        DelayUtils.sleep(100);
        ipAddressManagementViewPage.expandTreeRow(NETWORK_NAME_UPDATED);
        ipAddressManagementViewPage.expandTreeRowContains(getAddressAndMask(firstIPSubnetProperties));
        ipAddressManagementViewPage.expandTreeRowContains(getAddressAndMask(secondIPSubnetProperties));
        ipAddressManagementViewPage.expandTreeRow(getAddressAndMask(loopbackHostAddressProperties));
        ipAddressManagementViewPage.deleteHostAssignment(getAssignmentAssignTo(loopbackHostAssignmentProperties));
        ipAddressManagementViewPage.expandTreeRow(NETWORK_NAME_UPDATED);
        ipAddressManagementViewPage.expandTreeRowContains(getAddressAndMask(firstIPSubnetProperties));
        ipAddressManagementViewPage.expandTreeRowContains(getAddressAndMask(secondIPSubnetProperties));
        ipAddressManagementViewPage.expandTreeRow(getAddressAndMask(secondLoopbackHostAddressProperties));
        ipAddressManagementViewPage.deleteHostAssignment(getAssignmentAssignTo(secondLoopbackHostAssignmentProperties));
    }

    @Test(priority = 22)
    @Description("Delete IPv4 Hosts")
    public void deleteIPv4Hosts() {
        ipAddressManagementViewPage = new IPAddressManagementViewPage(driver);
        ipAddressManagementViewPage.expandTreeRow(NETWORK_NAME_UPDATED);
        ipAddressManagementViewPage.expandTreeRowContains(getAddressAndMask(firstIPSubnetProperties));
        ipAddressManagementViewPage.expandTreeRowContains(getAddressAndMask(secondIPSubnetProperties));
        ipAddressManagementViewPage.deleteIPHost(getAddressAndMask(loopbackHostAddressProperties));
    }

    @Test(priority = 23)
    @Description("Delete IPv4 Subnet Assignments")
    public void deleteIPv4SubnetAssignment() {
        ipAddressManagementViewPage = new IPAddressManagementViewPage(driver);
        ipAddressManagementViewPage.expandTreeRow(NETWORK_NAME_UPDATED);
        ipAddressManagementViewPage.deleteIPv4SubnetAssignment(getAddressAndMask(firstIPSubnetProperties));
    }

    @Test(priority = 24)
    @Description("Delete IPv4 Subnets")
    public void deleteIPv4Subnets() {
        ipAddressManagementViewPage = new IPAddressManagementViewPage(driver);
        ipAddressManagementViewPage.expandTreeRow(NETWORK_NAME_UPDATED);
        ipAddressManagementViewPage.deleteIPv4SubnetTypeOfBlock(getAddressAndMask(firstIPSubnetProperties));
        Assert.assertTrue(ipAddressManagementViewPage.isSystemMessageSuccess());
        ipAddressManagementViewPage = new IPAddressManagementViewPage(driver);
        ipAddressManagementViewPage.expandTreeRow(NETWORK_NAME_UPDATED);
        ipAddressManagementViewPage.deleteIPv4SubnetTypeOfBlock(getAddressAndMask(thirdIPSubnetProperties));
        Assert.assertTrue(ipAddressManagementViewPage.isSystemMessageSuccess());
        ipAddressManagementViewPage = new IPAddressManagementViewPage(driver);
        ipAddressManagementViewPage.expandTreeRow(NETWORK_NAME_UPDATED);
        ipAddressManagementViewPage.deleteIPv4SubnetTypeOfNetwork(getAddressAndMask(secondIPSubnetProperties));
    }

    @Test(priority = 25)
    @Description("Delete IP Network")
    public void deleteIPNetwork() {
        ipAddressManagementViewPage = new IPAddressManagementViewPage(driver);
        ipAddressManagementViewPage.deleteIPNetwork(NETWORK_NAME_UPDATED);
        Assert.assertTrue(ipAddressManagementViewPage.isSystemMessageSuccess());
    }

    @Test(priority = 26)
    @Description("Delete Role")
    public void deleteRole() {
        RoleViewPage roleViewPage = ipAddressManagementViewPage.openRoleView();
        roleViewPage.deleteRole(ROLE_NAME_UPDATED);
        Assert.assertTrue(ipAddressManagementViewPage.isSystemMessageSuccess());
        Assert.assertFalse(roleViewPage.doesRoleNameExist(ROLE_NAME_UPDATED));
        roleViewPage.exitRoleView();
    }

    private void prepareData() {
        inventoryViewPropertyNamesForSubnets.put(SUBNET_PROPERTY_IDENTIFIER, INVENTORY_PROPERTY_IDENTIFIER);
        inventoryViewPropertyNamesForSubnets.put(SUBNET_PROPERTY_ADDRESS, INVENTORY_PROPERTY_ADDRESS);
        inventoryViewPropertyNamesForSubnets.put(SUBNET_PROPERTY_HIGHEST_IP_ADDRESS, INVENTORY_PROPERTY_HIGHEST_IP_ADDRESS);
        inventoryViewPropertyNamesForSubnets.put(SUBNET_PROPERTY_BROADCAST_IP_ADDRESS, INVENTORY_PROPERTY_BROADCAST_IP_ADDRESS);
        inventoryViewPropertyNamesForSubnets.put(SUBNET_PROPERTY_IP_NETWORK_NAME, INVENTORY_PROPERTY_IP_NETWORK);
        inventoryViewPropertyNamesForSubnets.put(SUBNET_PROPERTY_MASK_LENGTH, INVENTORY_PROPERTY_MASK);
        inventoryViewPropertyNamesForSubnets.put(SUBNET_PROPERTY_SUBNET_TYPE, INVENTORY_PROPERTY_SUBNET_TYPE);
        inventoryViewPropertyNamesForSubnets.put(SUBNET_PROPERTY_ROLE, INVENTORY_PROPERTY_ROLE);
        inventoryViewPropertyNamesForSubnets.put(SUBNET_PROPERTY_PERCENT_FREE, INVENTORY_PROPERTY_PERCENT_FREE);
        inventoryViewPropertyNamesForSubnets.put(SUBNET_PROPERTY_DESCRIPTION, INVENTORY_PROPERTY_DESCRIPTION);

        inventoryViewPropertyNamesForHosts.put(HOST_PROPERTY_ADDRESS, INVENTORY_PROPERTY_ADDRESS);
        inventoryViewPropertyNamesForHosts.put(HOST_PROPERTY_IDENTIFIER, INVENTORY_PROPERTY_IDENTIFIER);
        inventoryViewPropertyNamesForHosts.put(HOST_PROPERTY_MASK, INVENTORY_PROPERTY_MASK);
        inventoryViewPropertyNamesForHosts.put(HOST_PROPERTY_IP_NETWORK_NAME, INVENTORY_PROPERTY_IP_NETWORK);
        inventoryViewPropertyNamesForHosts.put(HOST_PROPERTY_DESCRIPTION, INVENTORY_PROPERTY_DESCRIPTION);
        inventoryViewPropertyNamesForHosts.put(HOST_PROPERTY_STATUS, INVENTORY_PROPERTY_STATUS);

        propertiesNotVisibleOnNewInventoryView.add(SUBNET_PROPERTY_CHILD_COUNT);
        propertiesNotVisibleOnNewInventoryView.add(SUBNET_PROPERTY_ASSIGNED_TO);
    }

    private void updatePropertiesAfterIPv4SubnetsCreation() {
        firstIPSubnetProperties.put(SUBNET_PROPERTY_IDENTIFIER, SUBNETS_ADDRESS + "/" + LOWEST_IP_SUBNET_MASK + " [" + NETWORK_NAME + "]");
        firstIPSubnetProperties.put(SUBNET_PROPERTY_ADDRESS, SUBNETS_ADDRESS);
        firstIPSubnetProperties.put(SUBNET_PROPERTY_HIGHEST_IP_ADDRESS, "126.0.0.254");
        firstIPSubnetProperties.put(SUBNET_PROPERTY_BROADCAST_IP_ADDRESS, "126.0.0.255");
        firstIPSubnetProperties.put(SUBNET_PROPERTY_IP_NETWORK_NAME, NETWORK_NAME);
        firstIPSubnetProperties.put(SUBNET_PROPERTY_MASK_LENGTH, LOWEST_IP_SUBNET_MASK);
        firstIPSubnetProperties.put(SUBNET_PROPERTY_SUBNET_TYPE, BLOCK_TYPE);
        firstIPSubnetProperties.put(SUBNET_PROPERTY_PERCENT_FREE, "0%");
        firstIPSubnetProperties.put(SUBNET_PROPERTY_CHILD_COUNT, "2");

        secondIPSubnetProperties.put(SUBNET_PROPERTY_IDENTIFIER, SUBNETS_ADDRESS + "/" + HIGHER_IP_SUBNET_MASK + " [" + NETWORK_NAME + "]");
        secondIPSubnetProperties.put(SUBNET_PROPERTY_ADDRESS, SUBNETS_ADDRESS);
        secondIPSubnetProperties.put(SUBNET_PROPERTY_HIGHEST_IP_ADDRESS, "126.0.0.126");
        secondIPSubnetProperties.put(SUBNET_PROPERTY_BROADCAST_IP_ADDRESS, "126.0.0.127");
        secondIPSubnetProperties.put(SUBNET_PROPERTY_IP_NETWORK_NAME, NETWORK_NAME);
        secondIPSubnetProperties.put(SUBNET_PROPERTY_MASK_LENGTH, HIGHER_IP_SUBNET_MASK);
        secondIPSubnetProperties.put(SUBNET_PROPERTY_SUBNET_TYPE, NETWORK_TYPE);
        secondIPSubnetProperties.put(SUBNET_PROPERTY_PERCENT_FREE, "100%");
        secondIPSubnetProperties.put(SUBNET_PROPERTY_CHILD_COUNT, "0");
        secondIPSubnetProperties.put(SUBNET_PROPERTY_ROLE, ROLE_NAME);
        secondIPSubnetProperties.put(SUBNET_PROPERTY_DESCRIPTION, DESCRIPTION);

        thirdIPSubnetProperties.put(SUBNET_PROPERTY_IDENTIFIER, "126.0.0.128/" + HIGHER_IP_SUBNET_MASK + " [" + NETWORK_NAME + "]");
        thirdIPSubnetProperties.put(SUBNET_PROPERTY_ADDRESS, "126.0.0.128");
        thirdIPSubnetProperties.put(SUBNET_PROPERTY_HIGHEST_IP_ADDRESS, "126.0.0.254");
        thirdIPSubnetProperties.put(SUBNET_PROPERTY_BROADCAST_IP_ADDRESS, "126.0.0.255");
        thirdIPSubnetProperties.put(SUBNET_PROPERTY_IP_NETWORK_NAME, NETWORK_NAME);
        thirdIPSubnetProperties.put(SUBNET_PROPERTY_MASK_LENGTH, HIGHER_IP_SUBNET_MASK);
        thirdIPSubnetProperties.put(SUBNET_PROPERTY_SUBNET_TYPE, NETWORK_TYPE);
        thirdIPSubnetProperties.put(SUBNET_PROPERTY_PERCENT_FREE, "100%");
        thirdIPSubnetProperties.put(SUBNET_PROPERTY_CHILD_COUNT, "0");
    }

    private void updatePropertiesAfterIPv4SubnetAssignment() {
        firstIPSubnetProperties.put(SUBNET_PROPERTY_ASSIGNED_TO, ASSIGNMENT_LOCATION_IDENTIFIER);
        firstIPSubnetProperties.put(SUBNET_PROPERTY_ROLE, ROLE_NAME);
    }

    private void updatePropertiesAfterIPv4HostsReservation() {
        secondIPSubnetProperties.put(SUBNET_PROPERTY_CHILD_COUNT, "2");
        secondIPSubnetProperties.put(SUBNET_PROPERTY_PERCENT_FREE, "99%");

        loopbackHostAddressProperties.put(HOST_PROPERTY_ADDRESS, LOOPBACK_HOST_ADDRESS);
        loopbackHostAddressProperties.put(HOST_PROPERTY_IDENTIFIER, LOOPBACK_HOST_ADDRESS + "/" + LOOPBACK_IPV4_HOST_MASK + " [" + NETWORK_NAME + "]");
        loopbackHostAddressProperties.put(HOST_PROPERTY_MASK, LOOPBACK_IPV4_HOST_MASK);
        loopbackHostAddressProperties.put(HOST_PROPERTY_IP_NETWORK_NAME, NETWORK_NAME);
        loopbackHostAddressProperties.put(HOST_PROPERTY_DESCRIPTION, DESCRIPTION);
        loopbackHostAddressProperties.put(HOST_PROPERTY_STATUS, RESERVED_STATUS);
        hostAddressProperties.put(HOST_PROPERTY_ADDRESS, HOST_ADDRESS);
        hostAddressProperties.put(HOST_PROPERTY_IDENTIFIER, HOST_ADDRESS + "/" + HIGHER_IP_SUBNET_MASK + " [" + NETWORK_NAME + "]");
        hostAddressProperties.put(HOST_PROPERTY_MASK, HIGHER_IP_SUBNET_MASK);
        hostAddressProperties.put(HOST_PROPERTY_IP_NETWORK_NAME, NETWORK_NAME);
        hostAddressProperties.put(HOST_PROPERTY_DESCRIPTION, DESCRIPTION);
        hostAddressProperties.put(HOST_PROPERTY_STATUS, RESERVED_STATUS);
    }

    private void updatePropertiesAfterIPv4HostsAssignment() {
        secondIPSubnetProperties.put(SUBNET_PROPERTY_CHILD_COUNT, "3");
        secondIPSubnetProperties.put(SUBNET_PROPERTY_PERCENT_FREE, "99%"); //BUG

        loopbackHostAddressProperties.put(HOST_PROPERTY_STATUS, ASSIGNED_STATUS);
        loopbackHostAddressProperties.put(HOST_PROPERTY_ASSIGNED_TO, ASSIGNMENT_INTERFACE_IDENTIFIER);
        hostAddressProperties.put(HOST_PROPERTY_STATUS, ASSIGNED_STATUS);
        hostAddressProperties.put(HOST_PROPERTY_ASSIGNED_TO, ASSIGNMENT_DEVICE_IDENTIFIER);
        secondLoopbackHostAddressProperties.put(HOST_PROPERTY_ADDRESS, SECOND_LOOPBACK_HOST_ADDRESS);
        secondLoopbackHostAddressProperties.put(HOST_PROPERTY_IDENTIFIER, SECOND_LOOPBACK_HOST_ADDRESS + "/" + LOOPBACK_IPV4_HOST_MASK + " [" + NETWORK_NAME + "]");
        secondLoopbackHostAddressProperties.put(HOST_PROPERTY_MASK, LOOPBACK_IPV4_HOST_MASK);
        secondLoopbackHostAddressProperties.put(HOST_PROPERTY_IP_NETWORK_NAME, NETWORK_NAME);
        secondLoopbackHostAddressProperties.put(HOST_PROPERTY_STATUS, ASSIGNED_STATUS);
        secondLoopbackHostAddressProperties.put(HOST_PROPERTY_ASSIGNED_TO, ASSIGNMENT_SECOND_INTERFACE_IDENTIFIER);

        loopbackHostAssignmentProperties.put(HOST_ASSIGNMENT_PROPERTY_IDENTIFIER, LOOPBACK_HOST_ADDRESS + "/" + LOOPBACK_IPV4_HOST_MASK + " [" + NETWORK_NAME + "]");
        loopbackHostAssignmentProperties.put(HOST_ASSIGNMENT_PROPERTY_IS_PRIMARY, TRUE_STRING);
        loopbackHostAssignmentProperties.put(HOST_ASSIGNMENT_PROPERTY_IS_IN_NAT, TRUE_STRING);
        loopbackHostAssignmentProperties.put(HOST_ASSIGNMENT_PROPERTY_ROLE, ROLE_NAME);
        loopbackHostAssignmentProperties.put(HOST_ASSIGNMENT_PROPERTY_ASSIGNED_TO, ASSIGNMENT_INTERFACE_IDENTIFIER);
        loopbackHostAssignmentProperties.put(HOST_ASSIGNMENT_PROPERTY_IS_OBSOLETE, FALSE_STRING);

        hostAssignmentProperties.put(HOST_ASSIGNMENT_PROPERTY_IDENTIFIER, HOST_ADDRESS + "/" + HIGHER_IP_SUBNET_MASK + " [" + NETWORK_NAME + "]");
        hostAssignmentProperties.put(HOST_ASSIGNMENT_PROPERTY_IS_PRIMARY, FALSE_STRING);
        hostAssignmentProperties.put(HOST_ASSIGNMENT_PROPERTY_IS_IN_NAT, FALSE_STRING);
        hostAssignmentProperties.put(HOST_ASSIGNMENT_PROPERTY_ROLE, STANDARD_ROLE);
        hostAssignmentProperties.put(HOST_ASSIGNMENT_PROPERTY_ASSIGNED_TO, ASSIGNMENT_DEVICE_IDENTIFIER);
        hostAssignmentProperties.put(HOST_ASSIGNMENT_PROPERTY_IS_OBSOLETE, FALSE_STRING);

        secondLoopbackHostAssignmentProperties.put(HOST_ASSIGNMENT_PROPERTY_IDENTIFIER, SECOND_LOOPBACK_HOST_ADDRESS + "/" + LOOPBACK_IPV4_HOST_MASK + " [" + NETWORK_NAME + "]");
        secondLoopbackHostAssignmentProperties.put(HOST_ASSIGNMENT_PROPERTY_IS_PRIMARY, TRUE_STRING);
        secondLoopbackHostAssignmentProperties.put(HOST_ASSIGNMENT_PROPERTY_IS_IN_NAT, TRUE_STRING);
        secondLoopbackHostAssignmentProperties.put(HOST_ASSIGNMENT_PROPERTY_ROLE, ROLE_NAME);
        secondLoopbackHostAssignmentProperties.put(HOST_ASSIGNMENT_PROPERTY_ASSIGNED_TO, ASSIGNMENT_SECOND_INTERFACE_IDENTIFIER);
        secondLoopbackHostAssignmentProperties.put(HOST_ASSIGNMENT_PROPERTY_IS_OBSOLETE, FALSE_STRING);
    }

    private void updatePropertiesAfterRoleEdition() {
        firstIPSubnetProperties.put(SUBNET_PROPERTY_ROLE, ROLE_NAME_UPDATED);
        secondIPSubnetProperties.put(SUBNET_PROPERTY_ROLE, ROLE_NAME_UPDATED);
        loopbackHostAssignmentProperties.put(HOST_ASSIGNMENT_PROPERTY_ROLE, ROLE_NAME_UPDATED);
        secondLoopbackHostAssignmentProperties.put(HOST_ASSIGNMENT_PROPERTY_ROLE, ROLE_NAME_UPDATED);
    }

    private void updatePropertiesAfterIPNetworkEdition() {
        firstIPSubnetProperties.put(SUBNET_PROPERTY_IDENTIFIER, firstIPSubnetProperties.get(SUBNET_PROPERTY_ADDRESS) + "/" + firstIPSubnetProperties.get(SUBNET_PROPERTY_MASK_LENGTH) + " [" + NETWORK_NAME_UPDATED + "]");
        firstIPSubnetProperties.put(SUBNET_PROPERTY_IP_NETWORK_NAME, NETWORK_NAME_UPDATED);
        secondIPSubnetProperties.put(SUBNET_PROPERTY_IDENTIFIER, secondIPSubnetProperties.get(SUBNET_PROPERTY_ADDRESS) + "/" + secondIPSubnetProperties.get(SUBNET_PROPERTY_MASK_LENGTH) + " [" + NETWORK_NAME_UPDATED + "]");
        secondIPSubnetProperties.put(SUBNET_PROPERTY_IP_NETWORK_NAME, NETWORK_NAME_UPDATED);
        thirdIPSubnetProperties.put(SUBNET_PROPERTY_IDENTIFIER, thirdIPSubnetProperties.get(SUBNET_PROPERTY_ADDRESS) + "/" + thirdIPSubnetProperties.get(SUBNET_PROPERTY_MASK_LENGTH) + " [" + NETWORK_NAME_UPDATED + "]");
        thirdIPSubnetProperties.put(SUBNET_PROPERTY_IP_NETWORK_NAME, NETWORK_NAME_UPDATED);
        hostAddressProperties.put(HOST_PROPERTY_IDENTIFIER, HOST_ADDRESS + "/" + HIGHER_IP_SUBNET_MASK + " [" + NETWORK_NAME_UPDATED + "]");
        hostAddressProperties.put(HOST_PROPERTY_IP_NETWORK_NAME, NETWORK_NAME_UPDATED);
        loopbackHostAddressProperties.put(HOST_PROPERTY_IDENTIFIER, LOOPBACK_HOST_ADDRESS + "/" + LOOPBACK_IPV4_HOST_MASK + " [" + NETWORK_NAME_UPDATED + "]");
        loopbackHostAddressProperties.put(HOST_PROPERTY_IP_NETWORK_NAME, NETWORK_NAME_UPDATED);
        secondLoopbackHostAddressProperties.put(HOST_PROPERTY_IDENTIFIER, SECOND_LOOPBACK_HOST_ADDRESS + "/" + LOOPBACK_IPV4_HOST_MASK + " [" + NETWORK_NAME_UPDATED + "]");
        secondLoopbackHostAddressProperties.put(HOST_PROPERTY_IP_NETWORK_NAME, NETWORK_NAME_UPDATED);
        hostAssignmentProperties.put(HOST_PROPERTY_IDENTIFIER, HOST_ADDRESS + "/" + HIGHER_IP_SUBNET_MASK + " [" + NETWORK_NAME_UPDATED + "]");
        loopbackHostAssignmentProperties.put(HOST_PROPERTY_IDENTIFIER, LOOPBACK_HOST_ADDRESS + "/" + LOOPBACK_IPV4_HOST_MASK + " [" + NETWORK_NAME_UPDATED + "]");
        secondLoopbackHostAssignmentProperties.put(HOST_PROPERTY_IDENTIFIER, SECOND_LOOPBACK_HOST_ADDRESS + "/" + LOOPBACK_IPV4_HOST_MASK + " [" + NETWORK_NAME_UPDATED + "]");
    }

    private void updatePropertiesAfterIPv4SubnetsSplit() {
        secondIPSubnetProperties.put(SUBNET_PROPERTY_PERCENT_FREE, "50%");
        secondIPSubnetProperties.put(SUBNET_PROPERTY_CHILD_COUNT, "1");
        secondIPSubnetProperties.put(SUBNET_PROPERTY_SUBNET_TYPE, BLOCK_TYPE);

        fourthIPSubnetProperties.put(SUBNET_PROPERTY_ADDRESS, SUBNETS_ADDRESS);
        fourthIPSubnetProperties.put(SUBNET_PROPERTY_MASK_LENGTH, "26");
        fourthIPSubnetProperties.put(SUBNET_PROPERTY_SUBNET_TYPE, BLOCK_TYPE);
        fourthIPSubnetProperties.put(SUBNET_PROPERTY_CHILD_COUNT, "1");
        fourthIPSubnetProperties.put(SUBNET_PROPERTY_PERCENT_FREE, "50%");
        fourthIPSubnetProperties.put(SUBNET_PROPERTY_DESCRIPTION, DESCRIPTION);
        fourthIPSubnetProperties.put(SUBNET_PROPERTY_ROLE, MANAGEMENT_SECONDARY_ROLE);

        fifthIPSubnetProperties.put(SUBNET_PROPERTY_ADDRESS, SUBNETS_ADDRESS);
        fifthIPSubnetProperties.put(SUBNET_PROPERTY_MASK_LENGTH, "27");
        fifthIPSubnetProperties.put(SUBNET_PROPERTY_SUBNET_TYPE, NETWORK_TYPE);
        fifthIPSubnetProperties.put(SUBNET_PROPERTY_CHILD_COUNT, "3");
        fifthIPSubnetProperties.put(SUBNET_PROPERTY_PERCENT_FREE, "97%"); //bug
        fifthIPSubnetProperties.put(SUBNET_PROPERTY_DESCRIPTION, "");
    }

    private void updatePropertiesAfterIPv4SubnetsMerge() {
        secondIPSubnetProperties.put(SUBNET_PROPERTY_PERCENT_FREE, "99%"); //bug
        secondIPSubnetProperties.put(SUBNET_PROPERTY_CHILD_COUNT, "3");
        secondIPSubnetProperties.put(SUBNET_PROPERTY_SUBNET_TYPE, NETWORK_TYPE);
    }

    private void updatePropertiesAfterIPv4SubnetsEdition() {
        secondIPSubnetProperties.put(SUBNET_PROPERTY_ROLE, MANAGEMENT_SECONDARY_ROLE);
        secondIPSubnetProperties.put(SUBNET_PROPERTY_DESCRIPTION, DESCRIPTION_UPDATED);
        thirdIPSubnetProperties.put(SUBNET_PROPERTY_SUBNET_TYPE, BLOCK_TYPE);
    }

    private void updatePropertiesAfterIPv4SubnetAssignmentEdition() {
        firstIPSubnetProperties.put(SUBNET_PROPERTY_ROLE, STANDARD_ROLE);
    }

    private void updatePropertiesAfterIPv4HostEdition() {
        hostAddressProperties.put(HOST_PROPERTY_IDENTIFIER, HOST_ADDRESS + "/" + LOOPBACK_IPV4_HOST_MASK + " [" + NETWORK_NAME_UPDATED + "]");
        hostAddressProperties.put(HOST_PROPERTY_MASK, LOOPBACK_IPV4_HOST_MASK);
        hostAddressProperties.put(HOST_PROPERTY_STATUS, RESERVED_STATUS);
        hostAddressProperties.remove(HOST_PROPERTY_ASSIGNED_TO);
    }

    private void updatePropertiesAfterIPv4HostAssignmentEdition() {
        secondLoopbackHostAssignmentProperties.put(HOST_ASSIGNMENT_PROPERTY_IS_IN_NAT, TRUE_STRING);
        secondLoopbackHostAssignmentProperties.put(HOST_ASSIGNMENT_PROPERTY_ROLE, STANDARD_ROLE);
        secondLoopbackHostAssignmentProperties.put(HOST_ASSIGNMENT_PROPERTY_DESCRIPTION, DESCRIPTION);
    }

    private void checkAttributesOnIPAMTree(Map<String, String> properties, String row) {
        ipAddressManagementViewPage.selectTreeRowContains(row);
        Set<String> keySet = properties.keySet();
        for (String key : keySet) {
            Assert.assertEquals(ipAddressManagementViewPage.getPropertyValue(key), properties.get(key));
        }
        ipAddressManagementViewPage.selectTreeRowContains(row);
    }

    private void checkAttributesOnNewIV(PropertyPanel propertyPanel, Map<String, String> properties, Map<String, String> inventoryPropertyNames) {
        Set<String> keySet = properties.keySet();
        for (String key : keySet) {
            if (!propertiesNotVisibleOnNewInventoryView.contains(key)) {
                String inventoryPropertyName = inventoryPropertyNames.get(key);
                Assert.assertEquals(propertyPanel.getPropertyValue(inventoryPropertyName), properties.get(key));
            }
        }
    }

    private String getAddressAndMask(Map<String, String> properties) {
        if (properties.containsKey(SUBNET_PROPERTY_ADDRESS)) {
            return properties.get(SUBNET_PROPERTY_ADDRESS) + "/" + properties.get(SUBNET_PROPERTY_MASK_LENGTH);
        }
        return properties.get(HOST_PROPERTY_ADDRESS) + "/" + properties.get(HOST_PROPERTY_MASK);
    }

    private String getAssignmentAssignTo(Map<String, String> properties) {
        return properties.get(HOST_ASSIGNMENT_PROPERTY_ASSIGNED_TO);
    }
}

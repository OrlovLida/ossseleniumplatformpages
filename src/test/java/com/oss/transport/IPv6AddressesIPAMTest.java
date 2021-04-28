package com.oss.transport;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import io.qameta.allure.Description;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.oss.BaseTestCase;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.propertypanel.PropertyPanel;
import com.oss.utils.TestListener;
import com.oss.pages.bpm.ProcessInstancesPage;
import com.oss.pages.bpm.ProcessWizardPage;
import com.oss.pages.bpm.TasksPage;
import com.oss.pages.platform.NewInventoryViewPage;
import com.oss.pages.transport.ipam.IPAddressManagementViewPage;
import com.oss.pages.transport.ipam.RoleViewPage;

import com.oss.pages.transport.ipam.IPAddressAssignmentWizardPage;
import com.oss.pages.transport.ipam.IPSubnetWizardPage;
import com.oss.pages.transport.ipam.helper.IPAddressAssignmentWizardProperties;
import com.oss.pages.transport.ipam.helper.IPSubnetFilterProperties;
import com.oss.pages.transport.ipam.helper.IPSubnetWizardProperties;

import static com.oss.framework.components.inputs.Input.ComponentType.TEXT_FIELD;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.*;

/**
 * @author Ewa Frączek
 */

@Listeners({ TestListener.class })
public class IPv6AddressesIPAMTest extends BaseTestCase {
    private IPAddressManagementViewPage ipAddressManagementViewPage;
    protected NewInventoryViewPage newInventoryViewPage;
    private TasksPage tasksPage;
    private String processNRPCode;

    private static final String NETWORK_NAME = "IPv6IPAMSeleniumTest1";
    private static final String NETWORK_NAME_UPDATED = "IPv6IPAMSeleniumTestUpdated1";
    private static final String ROLE_NAME = "IPv6IPAMSeleniumTest2";
    private static final String ROLE_NAME_UPDATED = "IPv6IPAMSeleniumTestUpdated2";
    private static final String STANDARD_ROLE = "Standard";
    private static final String MANAGEMENT_SECONDARY_ROLE = "Management - Secondary";
    private static final int AMOUNT_OF_SUBNETS_SELECTED_DURING_SUBNET_CREATION = 3;
    private static final int AMOUNT_OF_SUBNETS_SELECTED_DURING_SUBNET_SPLIT = 2;
    private static final int AMOUNT_OF_SUBNETS_SELECTED_DURING_SUBNET_MERGE = 3;
    private static final String DESCRIPTION = "Description";
    private static final String DESCRIPTION_UPDATED = "DescriptionUpdated";
    private static final String NETWORK_SUBNET_TYPE = "Network";
    private static final String BLOCK_SUBNET_TYPE = "Block";
    private static final String OPERATOR_HIGHER_OR_EQUAL = ">=";
    private static final String IPV6_SUBNETS_ADDRESS = "::126:0";
    private static final String FILTER_IPV6_SUBNETS_END_IP_FOR_CREATION = "::126:7f";
    private static final String LOWEST_IPV6_SUBNET_MASK = "121";
    private static final String HIGHER_IPV6_SUBNET_MASK = "122";
    private static final String ASSIGNMENT_LOCATION_NAME = "IPAMSeleniumTest";
    private static final String ASSIGNMENT_DEVICE_NAME = "IPAMSeleniumTest";
    private static final String ASSIGNMENT_INTERFACE_NAME = "IPAMSeleniumTestFirstInterface";
    private static final String ASSIGNMENT_SECOND_INTERFACE_NAME = "IPAMSeleniumTestSecondInterface";
    private static final String ASSIGNMENT_LOCATION_IDENTIFIER = "IPAMSeleniumTest-BU2"; //"IPSWICH-BU1"; //"Cracow-BU2";
    private static final String ASSIGNMENT_DEVICE_IDENTIFIER = "-Router-7"; //"IPSWICH-BU1"; //"Cracow-BU2";
    private static final String ASSIGNMENT_INTERFACE_IDENTIFIER = "-Router-7\\CLUSTER 0"; //"IPSWICH-BU1"; //"Cracow-BU2";
    private static final String ASSIGNMENT_SECOND_INTERFACE_IDENTIFIER = "-Router-7\\CLUSTER 1"; //"IPSWICH-BU1"; //"Cracow-BU2";
    private static final String IPV6_HOST_ADDRESS = "::126:1";
    private static final String LOOPBACK_IPV6_HOST_ADDRESS = "::126:0";
    private static final String SECOND_LOOPBACK_IPV6_HOST_ADDRESS = "::126:2";
    private static final String LOOPBACK_IPV6_HOST_MASK = "128";
    private static final String TRUE_STRING = String.valueOf(true);
    private static final String FALSE_STRING = String.valueOf(false);
    private static final String RESERVED_STATUS = "Reserved";
    private static final String ASSIGNED_STATUS = "Assigned";
    private static final String BLOCK_TYPE = "BLOCK";
    private static final String NETWORK_TYPE = "NETWORK";

    private static final String NETWORK_INVENTORY_TYPE = "IPNetwork";
    private static final String IP_SUBNET_INVENTORY_TYPE = "IPSubnet";
    private static final String IP_HOST_INVENTORY_TYPE = "IPHostAddress";
    private static final String IP_SUBNET_ASSIGNMENT_INVENTORY_TYPE = "IPSubnetAssignment";
    private static final String IP_HOST_ASSIGNMENT_INVENTORY_TYPE = "IPHostAssignment";
    private static final String NETWORK_INVENTORY_PROPERTY_PANEL_ID = "PropertyPanelWidget_IPNetwork";
    private static final String SUBNET_INVENTORY_PROPERTY_PANEL_ID = "PropertyPanelWidget_IPSubnet";
    private static final String SUBNET_ASSIGNMENT_INVENTORY_PROPERTY_PANEL_ID = "PropertyPanelWidget_IPSubnetAssignment";
    private static final String HOST_INVENTORY_PROPERTY_PANEL_ID = "PropertyPanelWidget_IPHostAddress";
    private static final String HOST_ASSIGNMENT_INVENTORY_PROPERTY_PANEL_ID = "PropertyPanelWidget_IPHostAssignment";
    private static final int FIRST_INVENTORY_VIEW_ROW_ID = 0;

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
    private static final String INVENTORY_PROPERTY_IS_PRIMARY = "isPrimary";
    private static final String INVENTORY_PROPERTY_IS_IN_NAT = "isInNat";
    private static final String INVENTORY_PROPERTY_LOCATION = "location.name";
    private static final String INVENTORY_PROPERTY_INTERFACE = "terminationPoint.name";

    private Map<String, String> firstIPv6SubnetProperties = new HashMap<>();
    private Map<String, String> secondIPv6SubnetProperties = new HashMap<>();
    private Map<String, String> thirdIPv6SubnetProperties = new HashMap<>();
    private Map<String, String> fourthIPv6SubnetProperties = new HashMap<>();
    private Map<String, String> fifthIPv6SubnetProperties = new HashMap<>();

    private Map<String, String> loopbackIPv6HostAddressProperties = new HashMap<>();
    private Map<String, String> ipv6HostAddressProperties = new HashMap<>();
    private Map<String, String> secondLoopbackIpv6HostAddressProperties = new HashMap<>();

    private Map<String, String> loopbackIPv6HostAssignmentProperties = new HashMap<>();
    private Map<String, String> ipv6HostAssignmentProperties = new HashMap<>();
    private Map<String, String> secondLoopbackIPv6HostAssignmentProperties = new HashMap<>();

    private Map<String, String> inventoryViewPropertyNamesForSubnets = new HashMap<>();
    private Map<String, String> inventoryViewPropertyNamesForHosts = new HashMap<>();
    private Map<String, String> inventoryViewPropertyNamesForHostAssignments = new HashMap<>();

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
    @Description("Create IPv6 Subnets")
    public void createIPv6Subnets() {
        IPSubnetWizardPage ipv6SubnetWizardPage = ipAddressManagementViewPage.createIPv6Subnet();
        IPSubnetFilterProperties ipv6SubnetFilterProperties = new IPSubnetFilterProperties(IPV6_SUBNETS_ADDRESS, FILTER_IPV6_SUBNETS_END_IP_FOR_CREATION, OPERATOR_HIGHER_OR_EQUAL, LOWEST_IPV6_SUBNET_MASK);
        ipv6SubnetWizardPage.ipSubnetWizardSelectStep(ipv6SubnetFilterProperties, AMOUNT_OF_SUBNETS_SELECTED_DURING_SUBNET_CREATION);
        IPSubnetWizardProperties firstIpv6SubnetWizardProperties = new IPSubnetWizardProperties(BLOCK_SUBNET_TYPE);
        IPSubnetWizardProperties secondIpv6SubnetWizardProperties = new IPSubnetWizardProperties(NETWORK_SUBNET_TYPE, ROLE_NAME, DESCRIPTION);
        IPSubnetWizardProperties thirdIpv6SubnetWizardProperties = new IPSubnetWizardProperties(NETWORK_SUBNET_TYPE);
        ipv6SubnetWizardPage.ipSubnetWizardPropertiesStep(firstIpv6SubnetWizardProperties, secondIpv6SubnetWizardProperties, thirdIpv6SubnetWizardProperties);
        ipv6SubnetWizardPage.ipSubnetWizardSummaryStep();

        updatePropertiesAfterIPv6SubnetsCreation();
        ipAddressManagementViewPage = new IPAddressManagementViewPage(driver);
        ipAddressManagementViewPage.selectTreeRow(NETWORK_NAME);
        ipAddressManagementViewPage.selectTreeRow(NETWORK_NAME);
        ipAddressManagementViewPage.expandTreeRow(NETWORK_NAME);
        DelayUtils.sleep(100);
        checkAttributesOnIPAMTree(firstIPv6SubnetProperties, getAddressAndMask(firstIPv6SubnetProperties));
        ipAddressManagementViewPage.expandTreeRowContains(getAddressAndMask(firstIPv6SubnetProperties));
        checkAttributesOnIPAMTree(secondIPv6SubnetProperties, getAddressAndMask(secondIPv6SubnetProperties));
        checkAttributesOnIPAMTree(thirdIPv6SubnetProperties, getAddressAndMask(thirdIPv6SubnetProperties));
    }

    @Test(priority = 5)
    @Description("Assign IPv6 Subnets")
    public void assignIPv6Subnets() {
        ipAddressManagementViewPage
                .assignIPv6Subnet(getAddressAndMask(firstIPv6SubnetProperties), LOCATION, ASSIGNMENT_LOCATION_NAME, ROLE_NAME);
        updatePropertiesAfterIPv6SubnetAssignment();
        ipAddressManagementViewPage = new IPAddressManagementViewPage(driver);
        DelayUtils.sleep(100);
        checkAttributesOnIPAMTree(firstIPv6SubnetProperties, getAddressAndMask(firstIPv6SubnetProperties));
    }

    @Test(priority = 6)
    @Description("Reserve IPv6 Host Addresses")
    public void reserveIPv6Hosts() {
        ipAddressManagementViewPage
                .reserveIPv6HostAddress(getAddressAndMask(secondIPv6SubnetProperties), IPV6_HOST_ADDRESS, DESCRIPTION);
        updatePropertiesAfterIPv6HostsReservation();
        ipAddressManagementViewPage = new IPAddressManagementViewPage(driver);
        checkAttributesOnIPAMTree(ipv6HostAddressProperties, getAddressAndMask(ipv6HostAddressProperties));
        ipAddressManagementViewPage
                .reserveLoopbackIPv6HostAddress(getAddressAndMask(secondIPv6SubnetProperties), LOOPBACK_IPV6_HOST_ADDRESS, DESCRIPTION);
        ipAddressManagementViewPage = new IPAddressManagementViewPage(driver);
        checkAttributesOnIPAMTree(loopbackIPv6HostAddressProperties, getAddressAndMask(loopbackIPv6HostAddressProperties));
        checkAttributesOnIPAMTree(secondIPv6SubnetProperties, getAddressAndMask(secondIPv6SubnetProperties));
    }

    @Test(priority = 7)
    @Description("Assign IPv6 Host Addresses")
    public void assignIPv6Hosts() {
        IPAddressAssignmentWizardPage assignLoopbackAddressFromHostContext = ipAddressManagementViewPage
                .assignLoopbackIPv6HostAddressFromHostContext(getAddressAndMask(loopbackIPv6HostAddressProperties));
        IPAddressAssignmentWizardProperties loopbackIpAddressAssignmentWizardProperties = IPAddressAssignmentWizardProperties.builder()
                .isPrimary(TRUE_STRING).isInNAT(TRUE_STRING).role(ROLE_NAME).description(DESCRIPTION)
                .assignmentType(INTERFACE).assignmentName(ASSIGNMENT_INTERFACE_NAME).build();
        IPAddressAssignmentWizardProperties oppositeSideLoopbackIpAddressAssignmentWizardProperties = IPAddressAssignmentWizardProperties.builder()
                .isPrimary(TRUE_STRING).isInNAT(TRUE_STRING).role(ROLE_NAME).build();
        assignLoopbackAddressFromHostContext.assignIPAddressFromIPAddressContext(loopbackIpAddressAssignmentWizardProperties, oppositeSideLoopbackIpAddressAssignmentWizardProperties);
        updatePropertiesAfterIPv6HostsAssignment();
        ipAddressManagementViewPage = new IPAddressManagementViewPage(driver);
        ipAddressManagementViewPage.selectTreeRowContains(getAssignmentAddressMaskAndAssignTo(loopbackIPv6HostAssignmentProperties, NETWORK_NAME));
        ipAddressManagementViewPage.selectTreeRowContains(getAssignmentAddressMaskAndAssignTo(loopbackIPv6HostAssignmentProperties, NETWORK_NAME));
        IPAddressAssignmentWizardPage assignAddressFromSubnetContext = ipAddressManagementViewPage
                .assignIPv6HostAddressFromSubnetContext(getAddressAndMask(secondIPv6SubnetProperties));
        IPAddressAssignmentWizardProperties ipAddressAssignmentWizardProperties = IPAddressAssignmentWizardProperties.builder()
                .address(IPV6_HOST_ADDRESS).isPrimary(FALSE_STRING).isInNAT(FALSE_STRING).assignmentType(PHYSICAL_DEVICE).assignmentName(ASSIGNMENT_DEVICE_NAME).build();
        assignAddressFromSubnetContext.assignIPAddress(ipAddressAssignmentWizardProperties);
        ipAddressManagementViewPage = new IPAddressManagementViewPage(driver);
        checkAttributesOnIPAMTree(ipv6HostAssignmentProperties, getAssignmentAddressMaskAndAssignTo(ipv6HostAssignmentProperties, NETWORK_NAME));
        checkAttributesOnIPAMTree(ipv6HostAddressProperties, getAddressAndMask(ipv6HostAddressProperties));
        ipAddressManagementViewPage.expandTreeRow(getAddressAndMask(loopbackIPv6HostAddressProperties));
        checkAttributesOnIPAMTree(loopbackIPv6HostAssignmentProperties, getAssignmentAddressMaskAndAssignTo(loopbackIPv6HostAssignmentProperties, NETWORK_NAME));
        checkAttributesOnIPAMTree(loopbackIPv6HostAddressProperties, getAddressAndMask(loopbackIPv6HostAddressProperties));
        ipAddressManagementViewPage.expandTreeRow(getAddressAndMask(secondLoopbackIpv6HostAddressProperties));
        checkAttributesOnIPAMTree(secondLoopbackIPv6HostAssignmentProperties, getAssignmentAddressMaskAndAssignTo(secondLoopbackIPv6HostAssignmentProperties, NETWORK_NAME));
        checkAttributesOnIPAMTree(secondLoopbackIpv6HostAddressProperties, getAddressAndMask(secondLoopbackIpv6HostAddressProperties));
        checkAttributesOnIPAMTree(secondIPv6SubnetProperties, getAddressAndMask(secondIPv6SubnetProperties));
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
        DelayUtils.sleep(100);
        ipAddressManagementViewPage.selectTreeRowContains(getAddressAndMask(firstIPv6SubnetProperties));
        Assert.assertEquals(ipAddressManagementViewPage.getPropertyValue(SUBNET_PROPERTY_ROLE), firstIPv6SubnetProperties.get(SUBNET_PROPERTY_ROLE));
        ipAddressManagementViewPage.selectTreeRowContains(getAddressAndMask(firstIPv6SubnetProperties));
        ipAddressManagementViewPage.selectTreeRowContains(getAddressAndMask(secondIPv6SubnetProperties));
        Assert.assertEquals(ipAddressManagementViewPage.getPropertyValue(SUBNET_PROPERTY_ROLE), secondIPv6SubnetProperties.get(SUBNET_PROPERTY_ROLE));
        ipAddressManagementViewPage.selectTreeRowContains(getAddressAndMask(secondIPv6SubnetProperties));
        checkAttributesOnIPAMTree(loopbackIPv6HostAssignmentProperties, getAssignmentAddressMaskAndAssignTo(loopbackIPv6HostAssignmentProperties, NETWORK_NAME));
        checkAttributesOnIPAMTree(secondLoopbackIPv6HostAssignmentProperties, getAssignmentAddressMaskAndAssignTo(secondLoopbackIPv6HostAssignmentProperties, NETWORK_NAME));
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
        checkAttributesOnIPAMTree(firstIPv6SubnetProperties, getAddressAndMask(firstIPv6SubnetProperties));
        ipAddressManagementViewPage.expandTreeRowContains(getAddressAndMask(firstIPv6SubnetProperties));
        ipAddressManagementViewPage.expandTreeRowContains(getAddressAndMask(secondIPv6SubnetProperties));
        checkAttributesOnIPAMTree(secondIPv6SubnetProperties, getAddressAndMask(secondIPv6SubnetProperties));
        checkAttributesOnIPAMTree(thirdIPv6SubnetProperties, getAddressAndMask(thirdIPv6SubnetProperties));
        checkAttributesOnIPAMTree(ipv6HostAddressProperties, getAddressAndMask(ipv6HostAddressProperties));
        checkAttributesOnIPAMTree(loopbackIPv6HostAddressProperties, getAddressAndMask(loopbackIPv6HostAddressProperties));
        checkAttributesOnIPAMTree(secondLoopbackIpv6HostAddressProperties, getAddressAndMask(secondLoopbackIpv6HostAddressProperties));
        ipAddressManagementViewPage.expandTreeRow(getAddressAndMask(ipv6HostAddressProperties));
        ipAddressManagementViewPage.expandTreeRow(getAddressAndMask(loopbackIPv6HostAddressProperties));
        ipAddressManagementViewPage.expandTreeRow(getAddressAndMask(secondLoopbackIpv6HostAddressProperties));
        checkAttributesOnIPAMTree(ipv6HostAssignmentProperties, getAssignmentAddressMaskAndAssignTo(ipv6HostAssignmentProperties, NETWORK_NAME_UPDATED));
        checkAttributesOnIPAMTree(loopbackIPv6HostAssignmentProperties, getAssignmentAddressMaskAndAssignTo(loopbackIPv6HostAssignmentProperties, NETWORK_NAME_UPDATED));
        checkAttributesOnIPAMTree(secondLoopbackIPv6HostAssignmentProperties, getAssignmentAddressMaskAndAssignTo(secondLoopbackIPv6HostAssignmentProperties, NETWORK_NAME_UPDATED));
    }

    @Test(priority = 10)
    @Description("Split IPv6 Subnet")
    public void splitIPv6Subnet() {
        IPSubnetWizardPage ipv6SubnetWizardPage = ipAddressManagementViewPage.splitIPv6Subnet(getAddressAndMask(secondIPv6SubnetProperties));
        IPSubnetFilterProperties ipv6SubnetFilterProperties = new IPSubnetFilterProperties(IPV6_SUBNETS_ADDRESS, IPV6_SUBNETS_ADDRESS);
        ipv6SubnetWizardPage.ipSubnetWizardSelectStep(ipv6SubnetFilterProperties, AMOUNT_OF_SUBNETS_SELECTED_DURING_SUBNET_SPLIT);
        IPSubnetWizardProperties firstIpv6SubnetWizardProperties = new IPSubnetWizardProperties(BLOCK_SUBNET_TYPE, MANAGEMENT_SECONDARY_ROLE, DESCRIPTION);
        IPSubnetWizardProperties secondIpv6SubnetWizardProperties = new IPSubnetWizardProperties(NETWORK_SUBNET_TYPE);
        ipv6SubnetWizardPage.ipSubnetWizardPropertiesStep(firstIpv6SubnetWizardProperties, secondIpv6SubnetWizardProperties);
        ipv6SubnetWizardPage.ipSubnetWizardSummaryStep();

        updatePropertiesAfterIPv6SubnetsSplit();
        ipAddressManagementViewPage = new IPAddressManagementViewPage(driver);
        checkAttributesOnIPAMTree(secondIPv6SubnetProperties, getAddressAndMask(secondIPv6SubnetProperties));
        checkAttributesOnIPAMTree(fourthIPv6SubnetProperties, getAddressAndMask(fourthIPv6SubnetProperties));
        ipAddressManagementViewPage.expandTreeRowContains(getAddressAndMask(fourthIPv6SubnetProperties));
        checkAttributesOnIPAMTree(fifthIPv6SubnetProperties, getAddressAndMask(fifthIPv6SubnetProperties));
    }

    @Test(priority = 11)
    @Description("Merge IPv6 Subnets")
    public void mergeIPv6Subnets() {
        IPSubnetWizardPage ipv6SubnetWizardPage = ipAddressManagementViewPage
                .mergeIPv6Subnet(getAddressAndMask(secondIPv6SubnetProperties),
                        getAddressAndMask(fourthIPv6SubnetProperties), getAddressAndMask(fifthIPv6SubnetProperties));
        ipv6SubnetWizardPage.ipSubnetWizardSelectStep(AMOUNT_OF_SUBNETS_SELECTED_DURING_SUBNET_MERGE);
        IPSubnetWizardProperties firstIpv6SubnetWizardProperties = new IPSubnetWizardProperties(NETWORK_SUBNET_TYPE);
        ipv6SubnetWizardPage.ipSubnetWizardPropertiesStep(firstIpv6SubnetWizardProperties);
        ipv6SubnetWizardPage.ipSubnetWizardSummaryStep();

        updatePropertiesAfterIPv6SubnetsMerge();
        ipAddressManagementViewPage = new IPAddressManagementViewPage(driver);
        ipAddressManagementViewPage.expandTreeRow(NETWORK_NAME_UPDATED);
        ipAddressManagementViewPage.selectTreeRow(NETWORK_NAME_UPDATED);
        ipAddressManagementViewPage.selectTreeRow(NETWORK_NAME_UPDATED);
        ipAddressManagementViewPage.expandTreeRowContains(getAddressAndMask(firstIPv6SubnetProperties));
        checkAttributesOnIPAMTree(secondIPv6SubnetProperties, getAddressAndMask(secondIPv6SubnetProperties));
    }

    @Test(priority = 12)
    @Description("Edit IPv6 Subnets")
    public void editIPv6Subnets() {
        ipAddressManagementViewPage.changeIPv6SubnetTypeToBlock(getAddressAndMask(thirdIPv6SubnetProperties));
        ipAddressManagementViewPage.selectTreeRow(NETWORK_NAME_UPDATED);
        ipAddressManagementViewPage.selectTreeRow(NETWORK_NAME_UPDATED);
        ipAddressManagementViewPage.expandTreeRow(NETWORK_NAME_UPDATED);
        ipAddressManagementViewPage.expandTreeRowContains(getAddressAndMask(firstIPv6SubnetProperties));
        ipAddressManagementViewPage.editIPv6Subnet(getAddressAndMask(secondIPv6SubnetProperties), MANAGEMENT_SECONDARY_ROLE, DESCRIPTION_UPDATED);

        updatePropertiesAfterIPv6SubnetsEdition();
        checkAttributesOnIPAMTree(secondIPv6SubnetProperties, getAddressAndMask(secondIPv6SubnetProperties));
        checkAttributesOnIPAMTree(thirdIPv6SubnetProperties, getAddressAndMask(thirdIPv6SubnetProperties));
    }

    @Test(priority = 13)
    @Description("Edit IPv6 Subnet Assignment")
    public void editIPv6SubnetAssignment() {
        ipAddressManagementViewPage.editRoleForIPv6SubnetAssignment(getAddressAndMask(firstIPv6SubnetProperties), STANDARD_ROLE);
        updatePropertiesAfterIPv6SubnetAssignmentEdition();
        ipAddressManagementViewPage = new IPAddressManagementViewPage(driver);
        checkAttributesOnIPAMTree(firstIPv6SubnetProperties, getAddressAndMask(firstIPv6SubnetProperties));
    }

    @Test(priority = 14)
    @Description("Edit IPv6 Host")
    public void editIPv6Host() {
        ipAddressManagementViewPage.expandTreeRowContains(getAddressAndMask(secondIPv6SubnetProperties));
        ipAddressManagementViewPage.expandTreeRow(getAddressAndMask(ipv6HostAddressProperties));
        ipAddressManagementViewPage.deleteHostAssignment(getAssignmentAddressMaskAndAssignTo(ipv6HostAssignmentProperties, NETWORK_NAME_UPDATED));
        ipAddressManagementViewPage.selectTreeRowContains(getAddressAndMask(firstIPv6SubnetProperties));
        ipAddressManagementViewPage.selectTreeRowContains(getAddressAndMask(firstIPv6SubnetProperties));
        ipAddressManagementViewPage.expandTreeRowContains(getAddressAndMask(secondIPv6SubnetProperties));
        ipAddressManagementViewPage.changeIPv6HostMask(getAddressAndMask(ipv6HostAddressProperties), LOOPBACK_IPV6_HOST_MASK);
        updatePropertiesAfterIPv6HostEdition();
        ipAddressManagementViewPage = new IPAddressManagementViewPage(driver);
        checkAttributesOnIPAMTree(ipv6HostAddressProperties, getAddressAndMask(ipv6HostAddressProperties));
    }

    @Test(priority = 15)
    @Description("Edit IPv6 Host Assignment")
    public void editIPv6HostAssignment() {
        ipAddressManagementViewPage.expandTreeRow(getAddressAndMask(secondLoopbackIpv6HostAddressProperties));
        IPAddressAssignmentWizardProperties ipAddressAssignmentWizardProperties = IPAddressAssignmentWizardProperties.builder()
                .isInNAT(FALSE_STRING).role(STANDARD_ROLE).description(DESCRIPTION).build();
        ipAddressManagementViewPage
                .editIPv6HostAssignment(getAssignmentAddressMaskAndAssignTo(secondLoopbackIPv6HostAssignmentProperties, NETWORK_NAME_UPDATED), ipAddressAssignmentWizardProperties);
        updatePropertiesAfterIPv6HostAssignmentEdition();
        checkAttributesOnIPAMTree(secondLoopbackIPv6HostAssignmentProperties, getAssignmentAddressMaskAndAssignTo(secondLoopbackIPv6HostAssignmentProperties, NETWORK_NAME_UPDATED));
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
        DelayUtils.sleep(100);
        PropertyPanel propertyPanel = newInventoryViewPage.getPropertyPanel(FIRST_INVENTORY_VIEW_ROW_ID, NETWORK_INVENTORY_PROPERTY_PANEL_ID);
        Assert.assertEquals(propertyPanel.getPropertyValue(INVENTORY_PROPERTY_NAME), NETWORK_NAME_UPDATED);
        Assert.assertEquals(propertyPanel.getPropertyValue(INVENTORY_PROPERTY_DESCRIPTION), DESCRIPTION_UPDATED);
    }

    @Test(priority = 18)
    @Description("Check New Inventory View for IPv6 Subnets")
    public void checkInventoryViewForIPv6Subnets() {
        newInventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, IP_SUBNET_INVENTORY_TYPE);
        newInventoryViewPage.searchByAttributeValue(INVENTORY_PROPERTY_IDENTIFIER, firstIPv6SubnetProperties.get(SUBNET_PROPERTY_IDENTIFIER), TEXT_FIELD);
        PropertyPanel propertyPanelForFirstRow = newInventoryViewPage.getPropertyPanel(FIRST_INVENTORY_VIEW_ROW_ID, SUBNET_INVENTORY_PROPERTY_PANEL_ID);
        checkAttributesOnNewIV(propertyPanelForFirstRow, firstIPv6SubnetProperties, inventoryViewPropertyNamesForSubnets);
        newInventoryViewPage.clearFilters();
        newInventoryViewPage.searchByAttributeValue(INVENTORY_PROPERTY_IDENTIFIER, secondIPv6SubnetProperties.get(SUBNET_PROPERTY_IDENTIFIER), TEXT_FIELD);
        PropertyPanel propertyPanelForSecondRow = newInventoryViewPage.getPropertyPanel(FIRST_INVENTORY_VIEW_ROW_ID, SUBNET_INVENTORY_PROPERTY_PANEL_ID);
        checkAttributesOnNewIV(propertyPanelForSecondRow, secondIPv6SubnetProperties, inventoryViewPropertyNamesForSubnets);
        newInventoryViewPage.clearFilters();
        newInventoryViewPage.searchByAttributeValue(INVENTORY_PROPERTY_IDENTIFIER, thirdIPv6SubnetProperties.get(SUBNET_PROPERTY_IDENTIFIER), TEXT_FIELD);
        PropertyPanel propertyPanelForThirdRow = newInventoryViewPage.getPropertyPanel(FIRST_INVENTORY_VIEW_ROW_ID, SUBNET_INVENTORY_PROPERTY_PANEL_ID);
        checkAttributesOnNewIV(propertyPanelForThirdRow, thirdIPv6SubnetProperties, inventoryViewPropertyNamesForSubnets);
    }

    @Test(priority = 19)
    @Description("Check New Inventory View for IP Subnet Assignments")
    public void checkInventoryViewForIPv4SubnetAssignments() {
        newInventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, IP_SUBNET_ASSIGNMENT_INVENTORY_TYPE);
        newInventoryViewPage.searchByAttributeValue(INVENTORY_PROPERTY_IDENTIFIER, firstIPv6SubnetProperties.get(SUBNET_PROPERTY_IDENTIFIER), TEXT_FIELD);
        PropertyPanel propertyPanel = newInventoryViewPage.getPropertyPanel(FIRST_INVENTORY_VIEW_ROW_ID, SUBNET_ASSIGNMENT_INVENTORY_PROPERTY_PANEL_ID);
        Assert.assertEquals(propertyPanel.getPropertyValue(INVENTORY_PROPERTY_ROLE), firstIPv6SubnetProperties.get(SUBNET_PROPERTY_ROLE));
        Assert.assertEquals(propertyPanel.getPropertyValue(INVENTORY_PROPERTY_LOCATION), ASSIGNMENT_LOCATION_NAME);
    }

    @Test(priority = 20)
    @Description("Check New Inventory View for IPv6 Hosts")
    public void checkInventoryViewForIPv6Hosts() {
        newInventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, IP_HOST_INVENTORY_TYPE);
        newInventoryViewPage.searchByAttributeValue(INVENTORY_PROPERTY_IDENTIFIER, loopbackIPv6HostAddressProperties.get(HOST_PROPERTY_IDENTIFIER), TEXT_FIELD);
        PropertyPanel propertyPanelForFirstRow = newInventoryViewPage.getPropertyPanel(FIRST_INVENTORY_VIEW_ROW_ID, HOST_INVENTORY_PROPERTY_PANEL_ID);
        checkAttributesOnNewIV(propertyPanelForFirstRow, loopbackIPv6HostAddressProperties, inventoryViewPropertyNamesForHosts);
        newInventoryViewPage.clearFilters();
        newInventoryViewPage.searchByAttributeValue(INVENTORY_PROPERTY_IDENTIFIER, secondLoopbackIpv6HostAddressProperties.get(HOST_PROPERTY_IDENTIFIER), TEXT_FIELD);
        PropertyPanel propertyPanelForSecondRow = newInventoryViewPage.getPropertyPanel(FIRST_INVENTORY_VIEW_ROW_ID, HOST_INVENTORY_PROPERTY_PANEL_ID);
        checkAttributesOnNewIV(propertyPanelForSecondRow, secondLoopbackIpv6HostAddressProperties, inventoryViewPropertyNamesForHosts);
    }

    @Test(priority = 21)
    @Description("Check New Inventory View for IPv6 Host Assignments")
    public void checkInventoryViewForIPv6HostAssignments() {
        newInventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, IP_HOST_ASSIGNMENT_INVENTORY_TYPE);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        newInventoryViewPage.searchByAttributeValue(INVENTORY_PROPERTY_IDENTIFIER, loopbackIPv6HostAssignmentProperties.get(HOST_ASSIGNMENT_PROPERTY_IDENTIFIER), TEXT_FIELD);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        PropertyPanel propertyPanelForFirstRow = newInventoryViewPage.getPropertyPanel(FIRST_INVENTORY_VIEW_ROW_ID, HOST_ASSIGNMENT_INVENTORY_PROPERTY_PANEL_ID);
        checkAttributesOnNewIV(propertyPanelForFirstRow, loopbackIPv6HostAssignmentProperties, inventoryViewPropertyNamesForHostAssignments);
        Assert.assertEquals(propertyPanelForFirstRow.getPropertyValue(INVENTORY_PROPERTY_INTERFACE), ASSIGNMENT_INTERFACE_NAME);
        newInventoryViewPage.clearFilters();
        newInventoryViewPage.searchByAttributeValue(INVENTORY_PROPERTY_IDENTIFIER, secondLoopbackIPv6HostAssignmentProperties.get(HOST_ASSIGNMENT_PROPERTY_IDENTIFIER), TEXT_FIELD);
        PropertyPanel propertyPanelForSecondRow = newInventoryViewPage.getPropertyPanel(FIRST_INVENTORY_VIEW_ROW_ID, HOST_ASSIGNMENT_INVENTORY_PROPERTY_PANEL_ID);
        checkAttributesOnNewIV(propertyPanelForSecondRow, secondLoopbackIPv6HostAssignmentProperties, inventoryViewPropertyNamesForHostAssignments);
        Assert.assertEquals(propertyPanelForFirstRow.getPropertyValue(INVENTORY_PROPERTY_INTERFACE), ASSIGNMENT_SECOND_INTERFACE_NAME);
    }

    @Test(priority = 22)
    @Description("Delete IPv6 Hosts Assignment")
    public void deleteIPv6HostsAssignment() {
        ipAddressManagementViewPage = IPAddressManagementViewPage.goToIPAddressManagementViewPageLive(driver, BASIC_URL);
        DelayUtils.sleep(100);
        ipAddressManagementViewPage.expandTreeRow(NETWORK_NAME_UPDATED);
        ipAddressManagementViewPage.expandTreeRowContains(getAddressAndMask(firstIPv6SubnetProperties));
        ipAddressManagementViewPage.expandTreeRowContains(getAddressAndMask(secondIPv6SubnetProperties));
        ipAddressManagementViewPage.expandTreeRow(getAddressAndMask(loopbackIPv6HostAddressProperties));
        ipAddressManagementViewPage.deleteHostAssignment(getAssignmentAddressMaskAndAssignTo(loopbackIPv6HostAssignmentProperties, NETWORK_NAME_UPDATED));
        ipAddressManagementViewPage.expandTreeRow(NETWORK_NAME_UPDATED);
        ipAddressManagementViewPage.expandTreeRowContains(getAddressAndMask(firstIPv6SubnetProperties));
        ipAddressManagementViewPage.expandTreeRowContains(getAddressAndMask(secondIPv6SubnetProperties));
        ipAddressManagementViewPage.expandTreeRow(getAddressAndMask(secondLoopbackIpv6HostAddressProperties));
        ipAddressManagementViewPage.deleteHostAssignment(getAssignmentAddressMaskAndAssignTo(secondLoopbackIPv6HostAssignmentProperties, NETWORK_NAME_UPDATED));
    }

    @Test(priority = 23)
    @Description("Delete IPv6 Hosts")
    public void deleteIPv6Hosts() {
        ipAddressManagementViewPage = new IPAddressManagementViewPage(driver);
        ipAddressManagementViewPage.expandTreeRow(NETWORK_NAME_UPDATED);
        ipAddressManagementViewPage.expandTreeRowContains(getAddressAndMask(firstIPv6SubnetProperties));
        ipAddressManagementViewPage.expandTreeRowContains(getAddressAndMask(secondIPv6SubnetProperties));
        ipAddressManagementViewPage.deleteIPHost(getAddressAndMask(loopbackIPv6HostAddressProperties));
    }

    @Test(priority = 24)
    @Description("Delete IPv6 Subnet Assignments")
    public void deleteIPv6SubnetAssignment() {
        ipAddressManagementViewPage = new IPAddressManagementViewPage(driver);
        ipAddressManagementViewPage.expandTreeRow(NETWORK_NAME_UPDATED);
        ipAddressManagementViewPage.deleteIPv6SubnetAssignment(getAddressAndMask(firstIPv6SubnetProperties));
    }

    @Test(priority = 25)
    @Description("Delete IPv6 Subnets")
    public void deleteIPv6Subnets() {
        ipAddressManagementViewPage = new IPAddressManagementViewPage(driver);
        ipAddressManagementViewPage.expandTreeRow(NETWORK_NAME_UPDATED);
        ipAddressManagementViewPage.deleteIPv6SubnetTypeOfBlock(getAddressAndMask(firstIPv6SubnetProperties));
        Assert.assertTrue(ipAddressManagementViewPage.isSystemMessageSuccess());
        ipAddressManagementViewPage = new IPAddressManagementViewPage(driver);
        ipAddressManagementViewPage.expandTreeRow(NETWORK_NAME_UPDATED);
        ipAddressManagementViewPage.deleteIPv6SubnetTypeOfBlock(getAddressAndMask(thirdIPv6SubnetProperties));
        Assert.assertTrue(ipAddressManagementViewPage.isSystemMessageSuccess());
        ipAddressManagementViewPage = new IPAddressManagementViewPage(driver);
        ipAddressManagementViewPage.expandTreeRow(NETWORK_NAME_UPDATED);
        ipAddressManagementViewPage.deleteIPv6SubnetTypeOfNetwork(getAddressAndMask(secondIPv6SubnetProperties));
        Assert.assertTrue(ipAddressManagementViewPage.isSystemMessageSuccess());
    }

    @Test(priority = 26)
    @Description("Delete IP Network")
    public void deleteIPNetwork() {
        ipAddressManagementViewPage = new IPAddressManagementViewPage(driver);
        ipAddressManagementViewPage.deleteIPNetwork(NETWORK_NAME_UPDATED);
        Assert.assertTrue(ipAddressManagementViewPage.isSystemMessageSuccess());
    }

    @Test(priority = 27)
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

        inventoryViewPropertyNamesForHostAssignments.put(HOST_ASSIGNMENT_PROPERTY_IDENTIFIER, INVENTORY_PROPERTY_IDENTIFIER);
        inventoryViewPropertyNamesForHostAssignments.put(HOST_ASSIGNMENT_PROPERTY_ROLE, INVENTORY_PROPERTY_ROLE);
        inventoryViewPropertyNamesForHostAssignments.put(HOST_ASSIGNMENT_PROPERTY_IS_IN_NAT, INVENTORY_PROPERTY_IS_IN_NAT);
        inventoryViewPropertyNamesForHostAssignments.put(HOST_ASSIGNMENT_PROPERTY_IS_PRIMARY, INVENTORY_PROPERTY_IS_PRIMARY);
        inventoryViewPropertyNamesForHostAssignments.put(HOST_ASSIGNMENT_PROPERTY_DESCRIPTION, INVENTORY_PROPERTY_DESCRIPTION);
    }

    private void updatePropertiesAfterIPv6SubnetsCreation(){
        firstIPv6SubnetProperties.put(SUBNET_PROPERTY_IDENTIFIER, IPV6_SUBNETS_ADDRESS + "/" + LOWEST_IPV6_SUBNET_MASK + " [" + NETWORK_NAME + "]");
        firstIPv6SubnetProperties.put(SUBNET_PROPERTY_ADDRESS, IPV6_SUBNETS_ADDRESS);
        firstIPv6SubnetProperties.put(SUBNET_PROPERTY_HIGHEST_IP_ADDRESS, "::126:7f");
        firstIPv6SubnetProperties.put(SUBNET_PROPERTY_IP_NETWORK_NAME, NETWORK_NAME);
        firstIPv6SubnetProperties.put(SUBNET_PROPERTY_MASK_LENGTH, LOWEST_IPV6_SUBNET_MASK);
        firstIPv6SubnetProperties.put(SUBNET_PROPERTY_SUBNET_TYPE, BLOCK_TYPE);
        firstIPv6SubnetProperties.put(SUBNET_PROPERTY_PERCENT_FREE, "0%");
        firstIPv6SubnetProperties.put(SUBNET_PROPERTY_CHILD_COUNT, "2");

        secondIPv6SubnetProperties.put(SUBNET_PROPERTY_IDENTIFIER, IPV6_SUBNETS_ADDRESS + "/" + HIGHER_IPV6_SUBNET_MASK +" [" + NETWORK_NAME + "]");
        secondIPv6SubnetProperties.put(SUBNET_PROPERTY_ADDRESS, IPV6_SUBNETS_ADDRESS);
        secondIPv6SubnetProperties.put(SUBNET_PROPERTY_HIGHEST_IP_ADDRESS, "::126:3f");
        secondIPv6SubnetProperties.put(SUBNET_PROPERTY_IP_NETWORK_NAME, NETWORK_NAME);
        secondIPv6SubnetProperties.put(SUBNET_PROPERTY_MASK_LENGTH, HIGHER_IPV6_SUBNET_MASK);
        secondIPv6SubnetProperties.put(SUBNET_PROPERTY_SUBNET_TYPE, NETWORK_TYPE);
        secondIPv6SubnetProperties.put(SUBNET_PROPERTY_PERCENT_FREE, "100%");
        secondIPv6SubnetProperties.put(SUBNET_PROPERTY_CHILD_COUNT, "0");
        secondIPv6SubnetProperties.put(SUBNET_PROPERTY_ROLE, ROLE_NAME);
        secondIPv6SubnetProperties.put(SUBNET_PROPERTY_DESCRIPTION, DESCRIPTION);

        thirdIPv6SubnetProperties.put(SUBNET_PROPERTY_IDENTIFIER, "::126:40/" + HIGHER_IPV6_SUBNET_MASK + " [" + NETWORK_NAME + "]");
        thirdIPv6SubnetProperties.put(SUBNET_PROPERTY_ADDRESS, "::126:40");
        thirdIPv6SubnetProperties.put(SUBNET_PROPERTY_HIGHEST_IP_ADDRESS, "::126:7f");
        thirdIPv6SubnetProperties.put(SUBNET_PROPERTY_IP_NETWORK_NAME, NETWORK_NAME);
        thirdIPv6SubnetProperties.put(SUBNET_PROPERTY_MASK_LENGTH, HIGHER_IPV6_SUBNET_MASK);
        thirdIPv6SubnetProperties.put(SUBNET_PROPERTY_SUBNET_TYPE, NETWORK_TYPE);
        thirdIPv6SubnetProperties.put(SUBNET_PROPERTY_PERCENT_FREE, "100%");
        thirdIPv6SubnetProperties.put(SUBNET_PROPERTY_CHILD_COUNT, "0");
    }

    private void updatePropertiesAfterIPv6SubnetAssignment() {
        firstIPv6SubnetProperties.put(SUBNET_PROPERTY_ASSIGNED_TO, ASSIGNMENT_LOCATION_IDENTIFIER);
        firstIPv6SubnetProperties.put(SUBNET_PROPERTY_ROLE, ROLE_NAME);
    }

    private void updatePropertiesAfterIPv6HostsReservation() {
        secondIPv6SubnetProperties.put(SUBNET_PROPERTY_CHILD_COUNT, "2");
        secondIPv6SubnetProperties.put(SUBNET_PROPERTY_PERCENT_FREE, "98%");

        loopbackIPv6HostAddressProperties.put(HOST_PROPERTY_ADDRESS, LOOPBACK_IPV6_HOST_ADDRESS);
        loopbackIPv6HostAddressProperties.put(HOST_PROPERTY_IDENTIFIER, LOOPBACK_IPV6_HOST_ADDRESS + "/" + LOOPBACK_IPV6_HOST_MASK + " [" + NETWORK_NAME + "]");
        loopbackIPv6HostAddressProperties.put(HOST_PROPERTY_MASK, LOOPBACK_IPV6_HOST_MASK);
        loopbackIPv6HostAddressProperties.put(HOST_PROPERTY_IP_NETWORK_NAME, NETWORK_NAME);
        loopbackIPv6HostAddressProperties.put(HOST_PROPERTY_DESCRIPTION, DESCRIPTION);
        loopbackIPv6HostAddressProperties.put(HOST_PROPERTY_STATUS, RESERVED_STATUS);
        ipv6HostAddressProperties.put(HOST_PROPERTY_ADDRESS, IPV6_HOST_ADDRESS);
        ipv6HostAddressProperties.put(HOST_PROPERTY_IDENTIFIER, IPV6_HOST_ADDRESS + "/" + HIGHER_IPV6_SUBNET_MASK + " [" + NETWORK_NAME + "]");
        ipv6HostAddressProperties.put(HOST_PROPERTY_MASK, HIGHER_IPV6_SUBNET_MASK);
        ipv6HostAddressProperties.put(HOST_PROPERTY_IP_NETWORK_NAME, NETWORK_NAME);
        ipv6HostAddressProperties.put(HOST_PROPERTY_DESCRIPTION, DESCRIPTION);
        ipv6HostAddressProperties.put(HOST_PROPERTY_STATUS, RESERVED_STATUS);
    }

    private void updatePropertiesAfterIPv6HostsAssignment() {
        secondIPv6SubnetProperties.put(SUBNET_PROPERTY_CHILD_COUNT, "3");
        secondIPv6SubnetProperties.put(SUBNET_PROPERTY_PERCENT_FREE, "98%"); //BUG

        loopbackIPv6HostAddressProperties.put(HOST_PROPERTY_STATUS, ASSIGNED_STATUS);
        loopbackIPv6HostAddressProperties.put(HOST_PROPERTY_ASSIGNED_TO, ASSIGNMENT_INTERFACE_IDENTIFIER);
        ipv6HostAddressProperties.put(HOST_PROPERTY_STATUS, ASSIGNED_STATUS);
        ipv6HostAddressProperties.put(HOST_PROPERTY_ASSIGNED_TO, ASSIGNMENT_DEVICE_IDENTIFIER);
        secondLoopbackIpv6HostAddressProperties.put(HOST_PROPERTY_ADDRESS, SECOND_LOOPBACK_IPV6_HOST_ADDRESS);
        secondLoopbackIpv6HostAddressProperties.put(HOST_PROPERTY_IDENTIFIER, SECOND_LOOPBACK_IPV6_HOST_ADDRESS + "/" + LOOPBACK_IPV6_HOST_MASK + " [" + NETWORK_NAME + "]");
        secondLoopbackIpv6HostAddressProperties.put(HOST_PROPERTY_MASK, LOOPBACK_IPV6_HOST_MASK);
        secondLoopbackIpv6HostAddressProperties.put(HOST_PROPERTY_IP_NETWORK_NAME, NETWORK_NAME);
        secondLoopbackIpv6HostAddressProperties.put(HOST_PROPERTY_STATUS, ASSIGNED_STATUS);
        secondLoopbackIpv6HostAddressProperties.put(HOST_PROPERTY_ASSIGNED_TO, ASSIGNMENT_SECOND_INTERFACE_IDENTIFIER);

        loopbackIPv6HostAssignmentProperties.put(HOST_ASSIGNMENT_PROPERTY_IDENTIFIER, LOOPBACK_IPV6_HOST_ADDRESS + "/" + LOOPBACK_IPV6_HOST_MASK + " [" + NETWORK_NAME + "]");
        loopbackIPv6HostAssignmentProperties.put(HOST_ASSIGNMENT_PROPERTY_IS_PRIMARY, TRUE_STRING);
        loopbackIPv6HostAssignmentProperties.put(HOST_ASSIGNMENT_PROPERTY_IS_IN_NAT, TRUE_STRING);
        loopbackIPv6HostAssignmentProperties.put(HOST_ASSIGNMENT_PROPERTY_DESCRIPTION, DESCRIPTION);
        loopbackIPv6HostAssignmentProperties.put(HOST_ASSIGNMENT_PROPERTY_ROLE, ROLE_NAME);
        loopbackIPv6HostAssignmentProperties.put(HOST_ASSIGNMENT_PROPERTY_ASSIGNED_TO, ASSIGNMENT_INTERFACE_IDENTIFIER);
        loopbackIPv6HostAssignmentProperties.put(HOST_ASSIGNMENT_PROPERTY_IS_OBSOLETE, FALSE_STRING);

        ipv6HostAssignmentProperties.put(HOST_ASSIGNMENT_PROPERTY_IDENTIFIER, IPV6_HOST_ADDRESS + "/" + HIGHER_IPV6_SUBNET_MASK + " ["+NETWORK_NAME + "]");
        ipv6HostAssignmentProperties.put(HOST_ASSIGNMENT_PROPERTY_IS_PRIMARY, FALSE_STRING);
        ipv6HostAssignmentProperties.put(HOST_ASSIGNMENT_PROPERTY_IS_IN_NAT, FALSE_STRING);
        ipv6HostAssignmentProperties.put(HOST_ASSIGNMENT_PROPERTY_ROLE, STANDARD_ROLE);
        ipv6HostAssignmentProperties.put(HOST_ASSIGNMENT_PROPERTY_ASSIGNED_TO, ASSIGNMENT_DEVICE_IDENTIFIER);
        ipv6HostAssignmentProperties.put(HOST_ASSIGNMENT_PROPERTY_IS_OBSOLETE, FALSE_STRING);

        secondLoopbackIPv6HostAssignmentProperties.put(HOST_ASSIGNMENT_PROPERTY_IDENTIFIER, SECOND_LOOPBACK_IPV6_HOST_ADDRESS + "/" + LOOPBACK_IPV6_HOST_MASK + " [" + NETWORK_NAME + "]");
        secondLoopbackIPv6HostAssignmentProperties.put(HOST_ASSIGNMENT_PROPERTY_IS_PRIMARY, TRUE_STRING);
        secondLoopbackIPv6HostAssignmentProperties.put(HOST_ASSIGNMENT_PROPERTY_IS_IN_NAT, TRUE_STRING);
        secondLoopbackIPv6HostAssignmentProperties.put(HOST_ASSIGNMENT_PROPERTY_ROLE, ROLE_NAME);
        secondLoopbackIPv6HostAssignmentProperties.put(HOST_ASSIGNMENT_PROPERTY_ASSIGNED_TO, ASSIGNMENT_SECOND_INTERFACE_IDENTIFIER);
        secondLoopbackIPv6HostAssignmentProperties.put(HOST_ASSIGNMENT_PROPERTY_IS_OBSOLETE, FALSE_STRING);
    }

    private void updatePropertiesAfterRoleEdition() {
        firstIPv6SubnetProperties.put(SUBNET_PROPERTY_ROLE, ROLE_NAME_UPDATED);
        secondIPv6SubnetProperties.put(SUBNET_PROPERTY_ROLE, ROLE_NAME_UPDATED);
        loopbackIPv6HostAssignmentProperties.put(HOST_ASSIGNMENT_PROPERTY_ROLE, ROLE_NAME_UPDATED);
        secondLoopbackIPv6HostAssignmentProperties.put(HOST_ASSIGNMENT_PROPERTY_ROLE, ROLE_NAME_UPDATED);
    }

    private void updatePropertiesAfterIPNetworkEdition() {
        firstIPv6SubnetProperties.put(SUBNET_PROPERTY_IDENTIFIER, firstIPv6SubnetProperties.get(SUBNET_PROPERTY_ADDRESS) + "/" + firstIPv6SubnetProperties.get(SUBNET_PROPERTY_MASK_LENGTH) + " [" + NETWORK_NAME_UPDATED + "]");
        firstIPv6SubnetProperties.put(SUBNET_PROPERTY_IP_NETWORK_NAME, NETWORK_NAME_UPDATED);
        secondIPv6SubnetProperties.put(SUBNET_PROPERTY_IDENTIFIER, secondIPv6SubnetProperties.get(SUBNET_PROPERTY_ADDRESS) + "/" + secondIPv6SubnetProperties.get(SUBNET_PROPERTY_MASK_LENGTH) + " [" + NETWORK_NAME_UPDATED + "]");
        secondIPv6SubnetProperties.put(SUBNET_PROPERTY_IP_NETWORK_NAME, NETWORK_NAME_UPDATED);
        thirdIPv6SubnetProperties.put(SUBNET_PROPERTY_IDENTIFIER, thirdIPv6SubnetProperties.get(SUBNET_PROPERTY_ADDRESS) + "/" + thirdIPv6SubnetProperties.get(SUBNET_PROPERTY_MASK_LENGTH) + " [" + NETWORK_NAME_UPDATED + "]");
        thirdIPv6SubnetProperties.put(SUBNET_PROPERTY_IP_NETWORK_NAME, NETWORK_NAME_UPDATED);
        ipv6HostAddressProperties.put(HOST_PROPERTY_IDENTIFIER, IPV6_HOST_ADDRESS + "/" + HIGHER_IPV6_SUBNET_MASK + " [" + NETWORK_NAME_UPDATED + "]");
        ipv6HostAddressProperties.put(HOST_PROPERTY_IP_NETWORK_NAME, NETWORK_NAME_UPDATED);
        loopbackIPv6HostAddressProperties.put(HOST_PROPERTY_IDENTIFIER, LOOPBACK_IPV6_HOST_ADDRESS + "/" + LOOPBACK_IPV6_HOST_MASK + " [" + NETWORK_NAME_UPDATED + "]");
        loopbackIPv6HostAddressProperties.put(HOST_PROPERTY_IP_NETWORK_NAME, NETWORK_NAME_UPDATED);
        secondLoopbackIpv6HostAddressProperties.put(HOST_PROPERTY_IDENTIFIER, SECOND_LOOPBACK_IPV6_HOST_ADDRESS + "/" + LOOPBACK_IPV6_HOST_MASK + " [" + NETWORK_NAME_UPDATED + "]");
        secondLoopbackIpv6HostAddressProperties.put(HOST_PROPERTY_IP_NETWORK_NAME, NETWORK_NAME_UPDATED);
        ipv6HostAssignmentProperties.put(HOST_PROPERTY_IDENTIFIER, IPV6_HOST_ADDRESS + "/" + HIGHER_IPV6_SUBNET_MASK + " [" + NETWORK_NAME_UPDATED + "]");
        loopbackIPv6HostAssignmentProperties.put(HOST_PROPERTY_IDENTIFIER, LOOPBACK_IPV6_HOST_ADDRESS + "/" + LOOPBACK_IPV6_HOST_MASK + " [" + NETWORK_NAME_UPDATED + "]");
        secondLoopbackIPv6HostAssignmentProperties.put(HOST_PROPERTY_IDENTIFIER, SECOND_LOOPBACK_IPV6_HOST_ADDRESS + "/" + LOOPBACK_IPV6_HOST_MASK+" [" + NETWORK_NAME_UPDATED + "]");
    }

    private void updatePropertiesAfterIPv6SubnetsSplit() {
        secondIPv6SubnetProperties.put(SUBNET_PROPERTY_PERCENT_FREE, "50%");
        secondIPv6SubnetProperties.put(SUBNET_PROPERTY_CHILD_COUNT, "1");
        secondIPv6SubnetProperties.put(SUBNET_PROPERTY_SUBNET_TYPE, BLOCK_TYPE);

        fourthIPv6SubnetProperties.put(SUBNET_PROPERTY_ADDRESS, IPV6_SUBNETS_ADDRESS);
        fourthIPv6SubnetProperties.put(SUBNET_PROPERTY_MASK_LENGTH, "123");
        fourthIPv6SubnetProperties.put(SUBNET_PROPERTY_SUBNET_TYPE, BLOCK_TYPE);
        fourthIPv6SubnetProperties.put(SUBNET_PROPERTY_CHILD_COUNT, "1");
        fourthIPv6SubnetProperties.put(SUBNET_PROPERTY_PERCENT_FREE, "50%");
        fourthIPv6SubnetProperties.put(SUBNET_PROPERTY_DESCRIPTION, DESCRIPTION);
        fourthIPv6SubnetProperties.put(SUBNET_PROPERTY_ROLE, MANAGEMENT_SECONDARY_ROLE);

        fifthIPv6SubnetProperties.put(SUBNET_PROPERTY_ADDRESS, IPV6_SUBNETS_ADDRESS);
        fifthIPv6SubnetProperties.put(SUBNET_PROPERTY_MASK_LENGTH, "124");
        fifthIPv6SubnetProperties.put(SUBNET_PROPERTY_SUBNET_TYPE, NETWORK_TYPE);
        fifthIPv6SubnetProperties.put(SUBNET_PROPERTY_CHILD_COUNT, "3");
        fifthIPv6SubnetProperties.put(SUBNET_PROPERTY_PERCENT_FREE, "93%"); //bug
        fifthIPv6SubnetProperties.put(SUBNET_PROPERTY_DESCRIPTION, "");
    }

    private void updatePropertiesAfterIPv6SubnetsMerge() {
        secondIPv6SubnetProperties.put(SUBNET_PROPERTY_PERCENT_FREE, "98%"); //bug
        secondIPv6SubnetProperties.put(SUBNET_PROPERTY_CHILD_COUNT, "3");
        secondIPv6SubnetProperties.put(SUBNET_PROPERTY_SUBNET_TYPE, NETWORK_TYPE);
    }

    private void updatePropertiesAfterIPv6SubnetsEdition() {
        secondIPv6SubnetProperties.put(SUBNET_PROPERTY_ROLE, MANAGEMENT_SECONDARY_ROLE);
        secondIPv6SubnetProperties.put(SUBNET_PROPERTY_DESCRIPTION, DESCRIPTION_UPDATED);
        thirdIPv6SubnetProperties.put(SUBNET_PROPERTY_SUBNET_TYPE, BLOCK_TYPE);
    }

    private void updatePropertiesAfterIPv6SubnetAssignmentEdition() {
        firstIPv6SubnetProperties.put(SUBNET_PROPERTY_ROLE, STANDARD_ROLE);
    }

    private void updatePropertiesAfterIPv6HostEdition() {
        ipv6HostAddressProperties.put(HOST_PROPERTY_IDENTIFIER, IPV6_HOST_ADDRESS + "/" + LOOPBACK_IPV6_HOST_MASK + " [" + NETWORK_NAME_UPDATED + "]");
        ipv6HostAddressProperties.put(HOST_PROPERTY_MASK, LOOPBACK_IPV6_HOST_MASK);
        ipv6HostAddressProperties.put(HOST_PROPERTY_STATUS, RESERVED_STATUS);
        ipv6HostAddressProperties.remove(HOST_PROPERTY_ASSIGNED_TO);
    }

    private void updatePropertiesAfterIPv6HostAssignmentEdition() {
        secondLoopbackIPv6HostAssignmentProperties.put(HOST_ASSIGNMENT_PROPERTY_IS_IN_NAT, FALSE_STRING);
        secondLoopbackIPv6HostAssignmentProperties.put(HOST_ASSIGNMENT_PROPERTY_ROLE, STANDARD_ROLE);
        secondLoopbackIPv6HostAssignmentProperties.put(HOST_ASSIGNMENT_PROPERTY_DESCRIPTION, DESCRIPTION);
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
        Set<String> keySet = properties.keySet().stream()
                .filter(key -> inventoryPropertyNames.keySet().contains(key)).collect(Collectors.toSet());
        for (String key : keySet) {
            String inventoryPropertyName = inventoryPropertyNames.get(key);
            Assert.assertEquals(propertyPanel.getPropertyValue(inventoryPropertyName), properties.get(key));
        }
    }

    private String getAddressAndMask(Map<String, String> properties) {
        if (properties.containsKey(SUBNET_PROPERTY_ADDRESS)) {
            return properties.get(SUBNET_PROPERTY_ADDRESS) + "/" + properties.get(SUBNET_PROPERTY_MASK_LENGTH);
        }
        return properties.get(HOST_PROPERTY_ADDRESS) + "/" + properties.get(HOST_PROPERTY_MASK);
    }

    private String getAssignmentAddressMaskAndAssignTo(Map<String, String> properties, String ipNetwork) {
        CharSequence assignTo = properties.get(HOST_ASSIGNMENT_PROPERTY_ASSIGNED_TO);
        return properties.get(HOST_ASSIGNMENT_PROPERTY_IDENTIFIER).replace(ipNetwork, assignTo);
    }
}

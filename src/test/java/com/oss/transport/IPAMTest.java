package com.oss.transport;

import com.oss.BaseTestCase;

import static com.oss.framework.components.inputs.Input.ComponentType.SEARCH_FIELD;
import static com.oss.framework.components.inputs.Input.ComponentType.TEXT_FIELD;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.propertypanel.PropertyPanel;
import com.oss.pages.bpm.ProcessInstancesPage;
import com.oss.pages.bpm.ProcessWizardPage;
import com.oss.pages.bpm.TasksPage;
import com.oss.pages.platform.NewInventoryViewPage;
import com.oss.pages.transport.ipam.IPAddressManagementViewPage;
import com.oss.pages.transport.ipam.IPSubnetWizardPage;
import com.oss.pages.transport.ipam.RoleViewPage;
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

/**
 * @author Ewa Frączek
 */

@Listeners({ TestListener.class })
public class IPAMTest extends BaseTestCase {
    protected IPAddressManagementViewPage ipAddressManagementViewPage;
    protected NewInventoryViewPage newInventoryViewPage;
    private TasksPage tasksPage;
    private String processNRPCode;

    private static final String NETWORK_NAME = "IPam5SeleniumTest";
    private static final String NETWORK_DESCRIPTION = "Description";
    private static final String NETWORK_NAME_UPDATED = "IPamSeleniumTestUpdated2";
    private static final String NETWORK_DESCRIPTION_UPDATED = "DescriptionUpdated";
    private static final String ROLE_NAME = "Standard";//"IPAMSeleniumTest4";  BUG
    private static final String ROLE_NAME_UPDATED = "Standard";//"IPAMSeleniumTestUpdated3"; BUG
    private static final String STANDARD_ROLE = "Standard";
    private static final String MANAGEMENT_PRIMARY_ROLE = "Management - Primary";
    private static final String MANAGEMENT_SECONDARY_ROLE = "Management - Secondary";
    private static final int AMOUNT_OF_SUBNETS_SELECTED_DURING_SUBNET_CREATION = 3;
    private static final int AMOUNT_OF_SUBNETS_SELECTED_DURING_SUBNET_SPLIT = 2;
    private static final int AMOUNT_OF_SUBNETS_SELECTED_DURING_SUBNET_MERGE = 3;
    private static final String NETWORK_SUBNET_TYPE = "Network";
    private static final String BLOCK_SUBNET_TYPE = "Block";
    private static final String SUBNET_DESCRIPTION = "description";
    private static final String SUBNET_DESCRIPTION_UPDATED = "description2";
    private static final String FILTER_SUBNETS_START_IP = "126000";
    private static final String FILTER_SUBNETS_END_IP_FOR_CREATION = "12600128";
    private static final String FILTER_SUBNETS_END_IP_FOR_SPLIT = "126000";
    private static final String FILTER_SUBNETS_END_IPV6_FOR_SPLIT = "::126:0";
    private static final String OPERATOR_HIGHER_OR_EQUAL = ">=";
    private static final String HIGHEST_IP_SUBNET_MASK = "24";
    private static final String IP_SUBNET_MASK_AFTER_UPDATE = "26";
    private static final String FILTER_IPV6_SUBNETS_START_IP = "::126:0";
    private static final String FILTER_IPV6_SUBNETS_END_IP_FOR_CREATION = "::126:7f";
    private static final String HIGHEST_IPV6_SUBNET_MASK = "121";
    private static final String IPV6_SUBNET_MASK_AFTER_UPDATE = "123";
    private static final String LOCATION_ASSIGNMENT_TYPE = "Location";
    private static final String IPAM_SELENIUM_TEST_ASSIGNMENT_NAME = "IPAMSeleniumTest";
    private static final String IPAM_SELENIUM_TEST_ASSIGNMENT_IDENTIFIER = "IPAMSeleniumTest-BU2"; //"IPSWICH-BU1"; //"Cracow-BU2";
    private static final String HIGH_LEVEL_PLANNING = "High Level Planning";
    private static final String HOST_ADDRESS = "126.0.0.1";
    private static final String IPV6_HOST_ADDRESS = "::126:1";
    private static final String HOST_DESCRIPTION = "hostDescription";
    private static final String LOOPBACK_HOST_ADDRESS = "126.0.0.0";
    private static final String LOOPBACK_IPV6_HOST_ADDRESS = "::126:0";
    private static final String LOOPBACK_HOST_DESCRIPTION = "loopbackHostDescription";
    private static final String LOOPBACK_IPV4_HOST_MASK = "32";
    private static final String LOOPBACK_IPV6_HOST_MASK = "128";

    private static final String NETWORK_PROPERTY_NAME = "Name";
    private static final String NETWORK_PROPERTY_DESCRIPTION = "Description";

    private static final String SUBNET_PROPERTY_IDENTIFIER = "Identifier";
    private static final String SUBNET_PROPERTY_ADDRESS = "Address";
    private static final String SUBNET_PROPERTY_HIGHEST_IP_ADDRESS = "Highest IP Address";
    private static final String SUBNET_PROPERTY_BROADCAST_IP_ADDRESS = "Broadcast IP Address";
    private static final String SUBNET_PROPERTY_IP_NETWORK_NAME = "IP network name";
    private static final String SUBNET_PROPERTY_MASK_LENGTH = "Mask length";
    private static final String SUBNET_PROPERTY_SUBNET_TYPE = "Subnet type";
    private static final String SUBNET_PROPERTY_ROLE = "Role";
    private static final String SUBNET_PROPERTY_PERCENT_FREE = "Percent free";
    private static final String SUBNET_PROPERTY_CHILD_COUNT = "Child count";
    private static final String SUBNET_PROPERTY_DESCRIPTION = "Description";
    private static final String SUBNET_PROPERTY_ASSIGNED_TO = "Assigned to";

    private static final String HOST_PROPERTY_IDENTIFIER = "Identifier";
    private static final String HOST_PROPERTY_ADDRESS = "IP Address";
    private static final String HOST_PROPERTY_IP_NETWORK_NAME = "IP Network";
    private static final String HOST_PROPERTY_MASK = "Mask";
    private static final String HOST_PROPERTY_STATUS = "Status";
    private static final String HOST_PROPERTY_DESCRIPTION = "Description";
    private static final String HOST_PROPERTY_ASSIGNED_TO = "Assigned to";

    private static final String NETWORK_INVENTORY_TYPE = "IPNetwork";
    private static final String IPV4_SUBNET_INVENTORY_TYPE = "IPv4Subnet";
    private static final String IPV6_SUBNET_INVENTORY_TYPE = "IPv6Subnet";
    private static final String IPV4_HOST_INVENTORY_TYPE = "IPv4HostAddress";
    private static final String IPV6_HOST_INVENTORY_TYPE = "IPv6HostAddress";
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

    private Map<String, String> firstIPv6SubnetProperties = new HashMap<>();
    private Map<String, String> secondIPv6SubnetProperties = new HashMap<>();
    private Map<String, String> thirdIPv6SubnetProperties = new HashMap<>();
    private Map<String, String> fourthIPv6SubnetProperties = new HashMap<>();
    private Map<String, String> fifthIPv6SubnetProperties = new HashMap<>();

    private Map<String, String> loopbackHostAddressProperties = new HashMap<>();
    private Map<String, String> hostAddressProperties = new HashMap<>();
    private Map<String, String> loopbackIPv6HostAddressProperties = new HashMap<>();
    private Map<String, String> ipv6HostAddressProperties = new HashMap<>();

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
        tasksPage.startTask(processNRPCode, HIGH_LEVEL_PLANNING);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 2)
    @Description("Create Role")
    public void createRole() {
        DelayUtils.sleep(1500);
        ipAddressManagementViewPage = IPAddressManagementViewPage.goToIPAddressManagementViewPageLive(driver, BASIC_URL);
//        PerspectiveChooser perspectiveChooser = PerspectiveChooser.create(driver, webDriverWait);  BUG
//        perspectiveChooser.setPlanPerspective(processNRPCode);
//        RoleViewPage roleViewPage = ipAddressManagementViewPage.openRoleView();
//        roleViewPage.createRole(ROLE_NAME);
//        Assert.assertTrue(ipAddressManagementViewPage.isSystemMessageSuccess());
//        Assert.assertTrue(roleViewPage.doesRoleNameExist(ROLE_NAME));
//        roleViewPage.exitRoleView();
    }

    @Test(priority = 3)
    @Description("Create IP Network")
    public void createIPNetwork() {
        ipAddressManagementViewPage.createIPNetwork(NETWORK_NAME, NETWORK_DESCRIPTION);
        Assert.assertTrue(ipAddressManagementViewPage.isSystemMessageSuccess());

        ipAddressManagementViewPage.selectTreeRow(NETWORK_NAME);
        Assert.assertEquals(ipAddressManagementViewPage.getPropertyValue(NETWORK_PROPERTY_NAME), NETWORK_NAME);
        Assert.assertEquals(ipAddressManagementViewPage.getPropertyValue(NETWORK_PROPERTY_DESCRIPTION), NETWORK_DESCRIPTION);
    }

    @Test(priority = 4)
    @Description("Create IPv4 Subnets")
    public void createIPv4Subnets() {
        IPSubnetWizardPage ipSubnetWizardPage = ipAddressManagementViewPage.createIPv4Subnet();
        IPSubnetFilterProperties subnetFilterProperties = new IPSubnetFilterProperties(FILTER_SUBNETS_START_IP, FILTER_SUBNETS_END_IP_FOR_CREATION, OPERATOR_HIGHER_OR_EQUAL, HIGHEST_IP_SUBNET_MASK);
        ipSubnetWizardPage.ipSubnetWizardSelectStep(subnetFilterProperties, AMOUNT_OF_SUBNETS_SELECTED_DURING_SUBNET_CREATION);
        IPSubnetWizardProperties firstIpSubnetWizardProperties = new IPSubnetWizardProperties(BLOCK_SUBNET_TYPE);
        IPSubnetWizardProperties secondIpSubnetWizardProperties = new IPSubnetWizardProperties(NETWORK_SUBNET_TYPE, ROLE_NAME, SUBNET_DESCRIPTION);
        IPSubnetWizardProperties thirdIpSubnetWizardProperties = new IPSubnetWizardProperties(NETWORK_SUBNET_TYPE);
        ipSubnetWizardPage.ipSubnetWizardPropertiesStep(firstIpSubnetWizardProperties, secondIpSubnetWizardProperties, thirdIpSubnetWizardProperties);
        ipSubnetWizardPage.ipSubnetWizardSummaryStep();

        updatePropertiesAfterIPv4SubnetsCreation();
        ipAddressManagementViewPage = new IPAddressManagementViewPage(driver);
        ipAddressManagementViewPage.selectTreeRow(NETWORK_NAME);
        ipAddressManagementViewPage.selectTreeRow(NETWORK_NAME);
        ipAddressManagementViewPage.expandTreeRow(NETWORK_NAME);
        DelayUtils.sleep(100);
        checkAttributesOnIPAMTree(firstIPSubnetProperties);
        ipAddressManagementViewPage.expandTreeRowContains(getAddressAndMask(firstIPSubnetProperties));
        checkAttributesOnIPAMTree(secondIPSubnetProperties);
        checkAttributesOnIPAMTree(thirdIPSubnetProperties);
    }

    @Test(priority = 5)
    @Description("Create IPv6 Subnets")
    public void createIPv6Subnets() {
        ipAddressManagementViewPage.selectTreeRow(NETWORK_NAME);
        IPSubnetWizardPage ipv6SubnetWizardPage = ipAddressManagementViewPage.createIPv6Subnet();
        IPSubnetFilterProperties ipv6SubnetFilterProperties = new IPSubnetFilterProperties(FILTER_IPV6_SUBNETS_START_IP, FILTER_IPV6_SUBNETS_END_IP_FOR_CREATION, OPERATOR_HIGHER_OR_EQUAL, HIGHEST_IPV6_SUBNET_MASK);
        ipv6SubnetWizardPage.ipSubnetWizardSelectStep(ipv6SubnetFilterProperties, AMOUNT_OF_SUBNETS_SELECTED_DURING_SUBNET_CREATION);
        IPSubnetWizardProperties firstIpv6SubnetWizardProperties = new IPSubnetWizardProperties(BLOCK_SUBNET_TYPE);
        IPSubnetWizardProperties secondIpv6SubnetWizardProperties = new IPSubnetWizardProperties(NETWORK_SUBNET_TYPE, ROLE_NAME, SUBNET_DESCRIPTION);
        IPSubnetWizardProperties thirdIpv6SubnetWizardProperties = new IPSubnetWizardProperties(NETWORK_SUBNET_TYPE);
        ipv6SubnetWizardPage.ipSubnetWizardPropertiesStep(firstIpv6SubnetWizardProperties, secondIpv6SubnetWizardProperties, thirdIpv6SubnetWizardProperties);
        ipv6SubnetWizardPage.ipSubnetWizardSummaryStep();

        updatePropertiesAfterIPv6SubnetsCreation();
        ipAddressManagementViewPage = new IPAddressManagementViewPage(driver);
        ipAddressManagementViewPage.selectTreeRow(NETWORK_NAME);
        ipAddressManagementViewPage.selectTreeRow(NETWORK_NAME);
        ipAddressManagementViewPage.expandTreeRow(NETWORK_NAME);
        DelayUtils.sleep(100);
        checkAttributesOnIPAMTree(firstIPv6SubnetProperties);
        ipAddressManagementViewPage.expandTreeRowContains(getAddressAndMask(firstIPv6SubnetProperties));
        checkAttributesOnIPAMTree(secondIPv6SubnetProperties);
        checkAttributesOnIPAMTree(thirdIPv6SubnetProperties);
    }

    @Test(priority = 6)
    @Description("Assign IPv6 Subnets")
    public void assignIPv6Subnets() {
        ipAddressManagementViewPage
                .assignIPv6Subnet(getAddressAndMask(firstIPv6SubnetProperties), LOCATION_ASSIGNMENT_TYPE, IPAM_SELENIUM_TEST_ASSIGNMENT_NAME, ROLE_NAME);

        updatePropertiesAfterIPv6SubnetAssignment();
        ipAddressManagementViewPage = new IPAddressManagementViewPage(driver);
        DelayUtils.sleep(100);
        checkAttributesOnIPAMTree(firstIPv6SubnetProperties);
    }

    @Test(priority = 7)
    @Description("Assign IPv4 Subnets")
    public void assignIPv4Subnet() {
        ipAddressManagementViewPage
                .assignIPv4Subnet(getAddressAndMask(firstIPSubnetProperties), LOCATION_ASSIGNMENT_TYPE, IPAM_SELENIUM_TEST_ASSIGNMENT_NAME, ROLE_NAME);

        updatePropertiesAfterIPv4SubnetAssignment();
        ipAddressManagementViewPage = new IPAddressManagementViewPage(driver);
        DelayUtils.sleep(100);
        checkAttributesOnIPAMTree(firstIPSubnetProperties);
    }

    @Test(priority = 8)
    @Description("Reserve IPv4 Host Addresses")
    public void reserveIPv4Hosts() {
        ipAddressManagementViewPage
                .reserveIPv4HostAddress(getAddressAndMask(secondIPSubnetProperties), HOST_ADDRESS, HOST_DESCRIPTION);
        Assert.assertTrue(ipAddressManagementViewPage.isSystemMessageSuccess());
        updatePropertiesAfterIPv4HostsReservation();
        ipAddressManagementViewPage = new IPAddressManagementViewPage(driver);
        checkAttributesOnIPAMTree(hostAddressProperties);
        ipAddressManagementViewPage
                .reserveLoopbackIPv4HostAddress(getAddressAndMask(secondIPSubnetProperties), LOOPBACK_HOST_ADDRESS, LOOPBACK_HOST_DESCRIPTION);
        Assert.assertTrue(ipAddressManagementViewPage.isSystemMessageSuccess());
        ipAddressManagementViewPage = new IPAddressManagementViewPage(driver);
        checkAttributesOnIPAMTree(loopbackHostAddressProperties);
        checkAttributesOnIPAMTree(secondIPSubnetProperties);
    }

    @Test(priority = 9)
    @Description("Reserve IPv6 Host Addresses")
    public void reserveIPv6Hosts() {
        ipAddressManagementViewPage.expandTreeRowContains(getAddressAndMask(firstIPv6SubnetProperties));
        ipAddressManagementViewPage
                .reserveIPv6HostAddress(getAddressAndMask(secondIPv6SubnetProperties), IPV6_HOST_ADDRESS, HOST_DESCRIPTION);
        Assert.assertTrue(ipAddressManagementViewPage.isSystemMessageSuccess());
        updatePropertiesAfterIPv6HostsReservation();
        ipAddressManagementViewPage = new IPAddressManagementViewPage(driver);
        checkAttributesOnIPAMTree(ipv6HostAddressProperties);
        ipAddressManagementViewPage
                .reserveLoopbackIPv6HostAddress(getAddressAndMask(secondIPv6SubnetProperties), LOOPBACK_IPV6_HOST_ADDRESS, LOOPBACK_HOST_DESCRIPTION);
        Assert.assertTrue(ipAddressManagementViewPage.isSystemMessageSuccess());
        ipAddressManagementViewPage = new IPAddressManagementViewPage(driver);
        checkAttributesOnIPAMTree(loopbackIPv6HostAddressProperties);
        checkAttributesOnIPAMTree(secondIPv6SubnetProperties);
    }

    @Test(priority = 10)
    @Description("Edit Role")
    public void editRole() {
//        RoleViewPage roleViewPage = ipAddressManagementViewPage.openRoleView(); BUG OSSWEB-11542
//        roleViewPage.editRole(ROLE_NAME, ROLE_NAME_UPDATED);
//        Assert.assertTrue(ipAddressManagementViewPage.isSystemMessageSuccess());
//        Assert.assertTrue(roleViewPage.doesRoleNameExist(ROLE_NAME_UPDATED));
//        Assert.assertFalse(roleViewPage.doesRoleNameExist(ROLE_NAME));
//        roleViewPage.exitRoleView();

        updatePropertiesAfterRoleEdition();
        ipAddressManagementViewPage.selectTreeRowContains(getAddressAndMask(firstIPSubnetProperties));
        Assert.assertEquals(ipAddressManagementViewPage.getPropertyValue(SUBNET_PROPERTY_ROLE), firstIPSubnetProperties.get(SUBNET_PROPERTY_ROLE));
        ipAddressManagementViewPage.selectTreeRowContains(getAddressAndMask(firstIPSubnetProperties));
        ipAddressManagementViewPage.expandTreeRowContains(getAddressAndMask(firstIPSubnetProperties));
        ipAddressManagementViewPage.selectTreeRowContains(getAddressAndMask(secondIPSubnetProperties));
        Assert.assertEquals(ipAddressManagementViewPage.getPropertyValue(SUBNET_PROPERTY_ROLE), secondIPSubnetProperties.get(SUBNET_PROPERTY_ROLE));
        ipAddressManagementViewPage.selectTreeRowContains(getAddressAndMask(secondIPSubnetProperties));

        ipAddressManagementViewPage.selectTreeRowContains(getAddressAndMask(firstIPv6SubnetProperties));
        Assert.assertEquals(ipAddressManagementViewPage.getPropertyValue(SUBNET_PROPERTY_ROLE), firstIPv6SubnetProperties.get(SUBNET_PROPERTY_ROLE));
        ipAddressManagementViewPage.selectTreeRowContains(getAddressAndMask(firstIPv6SubnetProperties));
        ipAddressManagementViewPage.selectTreeRowContains(getAddressAndMask(secondIPv6SubnetProperties));
        Assert.assertEquals(ipAddressManagementViewPage.getPropertyValue(SUBNET_PROPERTY_ROLE), secondIPv6SubnetProperties.get(SUBNET_PROPERTY_ROLE));
        ipAddressManagementViewPage.selectTreeRowContains(getAddressAndMask(secondIPv6SubnetProperties));
    }

    @Test(priority = 11)
    @Description("Edit IP Network")
    public void editIPNetwork() {
        ipAddressManagementViewPage.editIPNetwork(NETWORK_NAME, NETWORK_NAME_UPDATED, NETWORK_DESCRIPTION_UPDATED);
        updatePropertiesAfterIPNetworkEdition();
        Assert.assertTrue(ipAddressManagementViewPage.isSystemMessageSuccess());

        ipAddressManagementViewPage = new IPAddressManagementViewPage(driver);
        ipAddressManagementViewPage.selectTreeRow(NETWORK_NAME_UPDATED);
        Assert.assertEquals(ipAddressManagementViewPage.getPropertyValue(NETWORK_PROPERTY_NAME), NETWORK_NAME_UPDATED);
        Assert.assertEquals(ipAddressManagementViewPage.getPropertyValue(NETWORK_PROPERTY_DESCRIPTION), NETWORK_DESCRIPTION_UPDATED);

        ipAddressManagementViewPage.selectTreeRow(NETWORK_NAME_UPDATED);
        ipAddressManagementViewPage.expandTreeRow(NETWORK_NAME_UPDATED);
        checkAttributesOnIPAMTree(firstIPSubnetProperties);
        ipAddressManagementViewPage.expandTreeRowContains(getAddressAndMask(firstIPSubnetProperties));
        ipAddressManagementViewPage.expandTreeRowContains(getAddressAndMask(secondIPSubnetProperties));
        checkAttributesOnIPAMTree(secondIPSubnetProperties);
        checkAttributesOnIPAMTree(thirdIPSubnetProperties);
        checkAttributesOnIPAMTree(hostAddressProperties);
        checkAttributesOnIPAMTree(loopbackHostAddressProperties);
        checkAttributesOnIPAMTree(firstIPv6SubnetProperties);
        ipAddressManagementViewPage.expandTreeRowContains(getAddressAndMask(firstIPv6SubnetProperties));
        ipAddressManagementViewPage.expandTreeRowContains(getAddressAndMask(secondIPv6SubnetProperties));
        checkAttributesOnIPAMTree(secondIPv6SubnetProperties);
        checkAttributesOnIPAMTree(thirdIPv6SubnetProperties);
        checkAttributesOnIPAMTree(ipv6HostAddressProperties);
        checkAttributesOnIPAMTree(loopbackIPv6HostAddressProperties);
    }

    @Test(priority = 12)
    @Description("Split IPv4 Subnet")
    public void splitIPv4Subnet() {
        IPSubnetWizardPage ipSubnetWizardPage = ipAddressManagementViewPage.splitIPv4Subnet(getAddressAndMask(secondIPSubnetProperties));
        IPSubnetFilterProperties subnetFilterProperties = new IPSubnetFilterProperties(FILTER_SUBNETS_START_IP, FILTER_SUBNETS_END_IP_FOR_SPLIT);
        ipSubnetWizardPage.ipSubnetWizardSelectStep(subnetFilterProperties, AMOUNT_OF_SUBNETS_SELECTED_DURING_SUBNET_SPLIT);
        IPSubnetWizardProperties firstIpSubnetWizardProperties = new IPSubnetWizardProperties(BLOCK_SUBNET_TYPE, MANAGEMENT_SECONDARY_ROLE, SUBNET_DESCRIPTION);
        IPSubnetWizardProperties secondIpSubnetWizardProperties = new IPSubnetWizardProperties(NETWORK_SUBNET_TYPE);
        ipSubnetWizardPage.ipSubnetWizardPropertiesStep(firstIpSubnetWizardProperties, secondIpSubnetWizardProperties);
        ipSubnetWizardPage.ipSubnetWizardSummaryStep();

        updatePropertiesAfterIPv4SubnetsSplit();
        ipAddressManagementViewPage = new IPAddressManagementViewPage(driver);
        checkAttributesOnIPAMTree(secondIPSubnetProperties);
        checkAttributesOnIPAMTree(fourthIPSubnetProperties);
        ipAddressManagementViewPage.expandTreeRowContains(getAddressAndMask(fourthIPSubnetProperties));
        checkAttributesOnIPAMTree(fifthIPSubnetProperties);
    }

    @Test(priority = 13)
    @Description("Split IPv6 Subnet")
    public void splitIPv6Subnet() {
        ipAddressManagementViewPage.expandTreeRowContains(getAddressAndMask(firstIPv6SubnetProperties));
        IPSubnetWizardPage ipv6SubnetWizardPage = ipAddressManagementViewPage.splitIPv6Subnet(getAddressAndMask(secondIPv6SubnetProperties));
        IPSubnetFilterProperties ipv6SubnetFilterProperties = new IPSubnetFilterProperties(FILTER_IPV6_SUBNETS_START_IP, FILTER_SUBNETS_END_IPV6_FOR_SPLIT);
        ipv6SubnetWizardPage.ipSubnetWizardSelectStep(ipv6SubnetFilterProperties, AMOUNT_OF_SUBNETS_SELECTED_DURING_SUBNET_SPLIT);
        IPSubnetWizardProperties firstIpv6SubnetWizardProperties = new IPSubnetWizardProperties(BLOCK_SUBNET_TYPE, MANAGEMENT_SECONDARY_ROLE, SUBNET_DESCRIPTION);
        IPSubnetWizardProperties secondIpv6SubnetWizardProperties = new IPSubnetWizardProperties(NETWORK_SUBNET_TYPE);
        ipv6SubnetWizardPage.ipSubnetWizardPropertiesStep(firstIpv6SubnetWizardProperties, secondIpv6SubnetWizardProperties);
        ipv6SubnetWizardPage.ipSubnetWizardSummaryStep();

        updatePropertiesAfterIPv6SubnetsSplit();
        ipAddressManagementViewPage = new IPAddressManagementViewPage(driver);
        checkAttributesOnIPAMTree(secondIPv6SubnetProperties);
        checkAttributesOnIPAMTree(fourthIPv6SubnetProperties);
        ipAddressManagementViewPage.expandTreeRowContains(getAddressAndMask(fourthIPv6SubnetProperties));
        checkAttributesOnIPAMTree(fifthIPv6SubnetProperties);
    }

    @Test(priority = 14)
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
        checkAttributesOnIPAMTree(secondIPv6SubnetProperties);
    }

    @Test(priority = 15)
    @Description("Merge IPv4 Subnets")
    public void mergeIPv4Subnets() {
        ipAddressManagementViewPage.expandTreeRowContains(getAddressAndMask(firstIPSubnetProperties));
        ipAddressManagementViewPage.expandTreeRowContains(getAddressAndMask(secondIPSubnetProperties));
        ipAddressManagementViewPage.expandTreeRowContains(getAddressAndMask(fourthIPSubnetProperties));
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
        checkAttributesOnIPAMTree(secondIPSubnetProperties);
    }

    @Test(priority = 16)
    @Description("Edit IPv4 Subnets")
    public void editIPv4Subnets() {
        ipAddressManagementViewPage.changeIPv4SubnetTypeToBlock(getAddressAndMask(thirdIPSubnetProperties));
        Assert.assertTrue(ipAddressManagementViewPage.isSystemMessageSuccess());
        ipAddressManagementViewPage.selectTreeRow(NETWORK_NAME_UPDATED);
        ipAddressManagementViewPage.selectTreeRow(NETWORK_NAME_UPDATED);
        ipAddressManagementViewPage.expandTreeRow(NETWORK_NAME_UPDATED);
        ipAddressManagementViewPage.expandTreeRowContains(getAddressAndMask(firstIPSubnetProperties));
        ipAddressManagementViewPage.editIPv4Subnet(getAddressAndMask(secondIPSubnetProperties), IP_SUBNET_MASK_AFTER_UPDATE, MANAGEMENT_SECONDARY_ROLE, SUBNET_DESCRIPTION_UPDATED);

        updatePropertiesAfterIPv4SubnetsEdition();
        checkAttributesOnIPAMTree(secondIPSubnetProperties);
        checkAttributesOnIPAMTree(thirdIPSubnetProperties);
    }

    @Test(priority = 17)
    @Description("Edit IPv6 Subnets")
    public void editIPv6Subnets() {
        ipAddressManagementViewPage.expandTreeRowContains(getAddressAndMask(firstIPv6SubnetProperties));
        ipAddressManagementViewPage.changeIPv6SubnetTypeToBlock(getAddressAndMask(thirdIPv6SubnetProperties));
        Assert.assertTrue(ipAddressManagementViewPage.isSystemMessageSuccess());
        ipAddressManagementViewPage.selectTreeRowContains(getAddressAndMask(secondIPSubnetProperties));
        ipAddressManagementViewPage.selectTreeRowContains(getAddressAndMask(secondIPSubnetProperties));
        ipAddressManagementViewPage.expandTreeRowContains(getAddressAndMask(firstIPv6SubnetProperties));
        ipAddressManagementViewPage.editIPv6Subnet(getAddressAndMask(secondIPv6SubnetProperties), IPV6_SUBNET_MASK_AFTER_UPDATE, MANAGEMENT_SECONDARY_ROLE, SUBNET_DESCRIPTION_UPDATED);

        updatePropertiesAfterIPv6SubnetsEdition();
        checkAttributesOnIPAMTree(secondIPv6SubnetProperties);
        checkAttributesOnIPAMTree(thirdIPv6SubnetProperties);
    }

    @Test(priority = 18)
    @Description("Edit IPv6 Subnet Assignment")
    public void editIPv6SubnetAssignment() {
        ipAddressManagementViewPage.editRoleForIPv6SubnetAssignment(getAddressAndMask(firstIPv6SubnetProperties), STANDARD_ROLE);

        updatePropertiesAfterIPv6SubnetAssignmentEdition();
        ipAddressManagementViewPage = new IPAddressManagementViewPage(driver);
        checkAttributesOnIPAMTree(firstIPv6SubnetProperties);
    }

    @Test(priority = 19)
    @Description("Edit IPv4 Subnet Assignment")
    public void editIPv4SubnetAssignment() {
        ipAddressManagementViewPage.editRoleForIPv4SubnetAssignment(getAddressAndMask(firstIPSubnetProperties), STANDARD_ROLE);

        updatePropertiesAfterIPv4SubnetAssignmentEdition();
        ipAddressManagementViewPage = new IPAddressManagementViewPage(driver);
        checkAttributesOnIPAMTree(firstIPSubnetProperties);
    }

    @Test(priority = 20)
    @Description("Edit IPv4 Host")
    public void editIPv4Host() {
        ipAddressManagementViewPage.expandTreeRowContains(getAddressAndMask(secondIPSubnetProperties));
        ipAddressManagementViewPage.changeIPv4HostMask(getAddressAndMask(hostAddressProperties), LOOPBACK_IPV4_HOST_MASK);
        updatePropertiesAfterIPv4HostEdition();
        ipAddressManagementViewPage = new IPAddressManagementViewPage(driver);
        checkAttributesOnIPAMTree(hostAddressProperties);
    }

    @Test(priority = 21)
    @Description("Edit IPv6 Host")
    public void editIPv6Host() {
        ipAddressManagementViewPage.expandTreeRowContains(getAddressAndMask(firstIPv6SubnetProperties));
        ipAddressManagementViewPage.expandTreeRowContains(getAddressAndMask(secondIPv6SubnetProperties));
        ipAddressManagementViewPage.changeIPv6HostMask(getAddressAndMask(ipv6HostAddressProperties), LOOPBACK_IPV6_HOST_MASK);
        updatePropertiesAfterIPv6HostEdition();
        ipAddressManagementViewPage = new IPAddressManagementViewPage(driver);
        checkAttributesOnIPAMTree(ipv6HostAddressProperties);
    }

    @Test(priority = 22, enabled = false)
    @Description("Move PLAN to LIVE")
    public void movePlanToLive() {
        DelayUtils.sleep(1000);
        tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.completeNRP(processNRPCode);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test (priority = 23)
    @Description("Check New Inventory View for IP Network")
    public void checkInventoryViewForIPNetwork() {
        newInventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, NETWORK_INVENTORY_TYPE);
        newInventoryViewPage.searchByAttributeValue(INVENTORY_PROPERTY_NAME, NETWORK_NAME_UPDATED, TEXT_FIELD);
        newInventoryViewPage.getMainTable().selectFirstRow();
        DelayUtils.sleep(100);
        PropertyPanel propertyPanel = newInventoryViewPage.getPropertyPanel(FIRST_INVENTORY_VIEW_ROW_ID,NETWORK_INVENTORY_PROPERTY_PANEL_ID);
        Assert.assertEquals(propertyPanel.getPropertyValue(INVENTORY_PROPERTY_NAME), NETWORK_NAME_UPDATED);
        Assert.assertEquals(propertyPanel.getPropertyValue(INVENTORY_PROPERTY_DESCRIPTION), NETWORK_DESCRIPTION_UPDATED);
    }

    @Test(priority = 24, enabled = false)
    @Description("Check New Inventory View for IPv4 Subnets")
    public void checkInventoryViewForIPv4Subnets() {
        newInventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, IPV4_SUBNET_INVENTORY_TYPE);
        newInventoryViewPage.searchByAttributeValue(INVENTORY_PROPERTY_IP_NETWORK, NETWORK_NAME_UPDATED, SEARCH_FIELD);
        PropertyPanel propertyPanelForFirstRow = newInventoryViewPage.getPropertyPanel(FIRST_INVENTORY_VIEW_ROW_ID,SUBNET_INVENTORY_PROPERTY_PANEL_ID);
        checkAttributesOnNewIV(propertyPanelForFirstRow, firstIPSubnetProperties, inventoryViewPropertyNamesForSubnets);
        PropertyPanel propertyPanelForSecondRow = newInventoryViewPage.getPropertyPanel(SECOND_INVENTORY_VIEW_ROW_ID,SUBNET_INVENTORY_PROPERTY_PANEL_ID);
        checkAttributesOnNewIV(propertyPanelForSecondRow, secondIPSubnetProperties, inventoryViewPropertyNamesForSubnets);
        PropertyPanel propertyPanelForThirdRow = newInventoryViewPage.getPropertyPanel(THIRD_INVENTORY_VIEW_ROW_ID,SUBNET_INVENTORY_PROPERTY_PANEL_ID);
        checkAttributesOnNewIV(propertyPanelForThirdRow, thirdIPSubnetProperties, inventoryViewPropertyNamesForSubnets);
    }

    @Test(priority = 25, enabled = false)
    @Description("Check New Inventory View for IPv6 Subnets")
    public void checkInventoryViewForIPv6Subnets() {
        newInventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, IPV6_SUBNET_INVENTORY_TYPE);
        newInventoryViewPage.searchByAttributeValue(INVENTORY_PROPERTY_IP_NETWORK, NETWORK_NAME_UPDATED, SEARCH_FIELD);
        PropertyPanel propertyPanelForFirstRow = newInventoryViewPage.getPropertyPanel(FIRST_INVENTORY_VIEW_ROW_ID,SUBNET_INVENTORY_PROPERTY_PANEL_ID);
        checkAttributesOnNewIV(propertyPanelForFirstRow, firstIPv6SubnetProperties, inventoryViewPropertyNamesForSubnets);
        PropertyPanel propertyPanelForSecondRow = newInventoryViewPage.getPropertyPanel(SECOND_INVENTORY_VIEW_ROW_ID,SUBNET_INVENTORY_PROPERTY_PANEL_ID);
        checkAttributesOnNewIV(propertyPanelForSecondRow, secondIPv6SubnetProperties, inventoryViewPropertyNamesForSubnets);
        PropertyPanel propertyPanelForThirdRow = newInventoryViewPage.getPropertyPanel(THIRD_INVENTORY_VIEW_ROW_ID,SUBNET_INVENTORY_PROPERTY_PANEL_ID);
        checkAttributesOnNewIV(propertyPanelForThirdRow, thirdIPv6SubnetProperties, inventoryViewPropertyNamesForSubnets);
    }

    @Test(priority = 26, enabled = false)
    @Description("Check New Inventory View for IP Subnet Assignments")
    public void checkInventoryViewForIPSubnetAssignments() {
        newInventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, IP_SUBNET_ASSIGNMENT_INVENTORY_TYPE);
        newInventoryViewPage.searchByAttributeValue(INVENTORY_PROPERTY_IP_NETWORK, NETWORK_NAME_UPDATED, SEARCH_FIELD);
        PropertyPanel propertyPanelForFirstRow = newInventoryViewPage.getPropertyPanel(FIRST_INVENTORY_VIEW_ROW_ID,SUBNET_ASSIGNMENT_INVENTORY_PROPERTY_PANEL_ID);
        Assert.assertEquals(propertyPanelForFirstRow.getPropertyValue(INVENTORY_PROPERTY_IDENTIFIER), secondIPSubnetProperties.get(SUBNET_PROPERTY_IDENTIFIER));
        Assert.assertEquals(propertyPanelForFirstRow.getPropertyValue(INVENTORY_PROPERTY_IP_SUBNET), secondIPSubnetProperties.get(SUBNET_PROPERTY_IDENTIFIER));
        Assert.assertEquals(propertyPanelForFirstRow.getPropertyValue(INVENTORY_PROPERTY_MASK), secondIPSubnetProperties.get(SUBNET_PROPERTY_MASK_LENGTH));
        Assert.assertEquals(propertyPanelForFirstRow.getPropertyValue(INVENTORY_PROPERTY_DEVICE), secondIPSubnetProperties.get(SUBNET_PROPERTY_ASSIGNED_TO));
        PropertyPanel propertyPanelForSecondRow = newInventoryViewPage.getPropertyPanel(SECOND_INVENTORY_VIEW_ROW_ID,SUBNET_ASSIGNMENT_INVENTORY_PROPERTY_PANEL_ID);
        Assert.assertEquals(propertyPanelForSecondRow.getPropertyValue(INVENTORY_PROPERTY_NAME), NETWORK_NAME);
        Assert.assertEquals(propertyPanelForFirstRow.getPropertyValue(INVENTORY_PROPERTY_IDENTIFIER), secondIPv6SubnetProperties.get(SUBNET_PROPERTY_IDENTIFIER));
        Assert.assertEquals(propertyPanelForFirstRow.getPropertyValue(INVENTORY_PROPERTY_IP_SUBNET), secondIPv6SubnetProperties.get(SUBNET_PROPERTY_IDENTIFIER));
        Assert.assertEquals(propertyPanelForFirstRow.getPropertyValue(INVENTORY_PROPERTY_MASK), secondIPv6SubnetProperties.get(SUBNET_PROPERTY_MASK_LENGTH));
        Assert.assertEquals(propertyPanelForFirstRow.getPropertyValue(INVENTORY_PROPERTY_DEVICE), secondIPv6SubnetProperties.get(SUBNET_PROPERTY_ASSIGNED_TO));
    }

    @Test(priority = 27, enabled = false)
    @Description("Check New Inventory View for IPv4 Hosts")
    public void checkInventoryViewForIPv4Hosts() {
        newInventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, IPV4_HOST_INVENTORY_TYPE);
        newInventoryViewPage.searchByAttributeValue(INVENTORY_PROPERTY_IP_NETWORK, NETWORK_NAME_UPDATED, TEXT_FIELD);
        PropertyPanel propertyPanelForFirstRow = newInventoryViewPage.getPropertyPanel(FIRST_INVENTORY_VIEW_ROW_ID,HOST_INVENTORY_PROPERTY_PANEL_ID);
        checkAttributesOnNewIV(propertyPanelForFirstRow, loopbackHostAddressProperties, inventoryViewPropertyNamesForHosts);
        PropertyPanel propertyPanelForSecondRow = newInventoryViewPage.getPropertyPanel(SECOND_INVENTORY_VIEW_ROW_ID,HOST_INVENTORY_PROPERTY_PANEL_ID);
        checkAttributesOnNewIV(propertyPanelForSecondRow, hostAddressProperties, inventoryViewPropertyNamesForHosts);
    }

    @Test(priority = 28, enabled = false)
    @Description("Check New Inventory View for IPv6 Hosts")
    public void checkInventoryViewForIPv6Hosts() {
        newInventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, IPV6_HOST_INVENTORY_TYPE);
        newInventoryViewPage.searchByAttributeValue(INVENTORY_PROPERTY_IP_NETWORK, NETWORK_NAME_UPDATED, TEXT_FIELD);
        PropertyPanel propertyPanelForFirstRow = newInventoryViewPage.getPropertyPanel(FIRST_INVENTORY_VIEW_ROW_ID,HOST_INVENTORY_PROPERTY_PANEL_ID);
        checkAttributesOnNewIV(propertyPanelForFirstRow, loopbackIPv6HostAddressProperties, inventoryViewPropertyNamesForHosts);
        PropertyPanel propertyPanelForSecondRow = newInventoryViewPage.getPropertyPanel(SECOND_INVENTORY_VIEW_ROW_ID,HOST_INVENTORY_PROPERTY_PANEL_ID);
        checkAttributesOnNewIV(propertyPanelForSecondRow, ipv6HostAddressProperties, inventoryViewPropertyNamesForHosts);
    }

    @Test(priority = 29)
    @Description("Delete IPv4 Hosts")
    public void deleteIPv4Hosts() {
        ipAddressManagementViewPage = IPAddressManagementViewPage.goToIPAddressManagementViewPageLive(driver, BASIC_URL);
        DelayUtils.sleep(100);
        ipAddressManagementViewPage = new IPAddressManagementViewPage(driver);
        ipAddressManagementViewPage.expandTreeRow(NETWORK_NAME_UPDATED);
        ipAddressManagementViewPage.expandTreeRowContains(getAddressAndMask(firstIPSubnetProperties));
        ipAddressManagementViewPage.expandTreeRowContains(getAddressAndMask(secondIPSubnetProperties));
        ipAddressManagementViewPage.deleteIPHost(getAddressAndMask(loopbackHostAddressProperties));
    }

    @Test(priority = 30)
    @Description("Delete IPv6 Hosts")
    public void deleteIPv6Hosts() {
        ipAddressManagementViewPage = new IPAddressManagementViewPage(driver);
        ipAddressManagementViewPage.expandTreeRow(NETWORK_NAME_UPDATED);
        ipAddressManagementViewPage.expandTreeRowContains(getAddressAndMask(firstIPv6SubnetProperties));
        ipAddressManagementViewPage.expandTreeRowContains(getAddressAndMask(secondIPv6SubnetProperties));
        ipAddressManagementViewPage.deleteIPHost(getAddressAndMask(loopbackIPv6HostAddressProperties));
    }

    @Test(priority = 31)
    @Description("Delete IPv4 Subnet Assignments")
    public void deleteIPv4SubnetAssignment() {
        ipAddressManagementViewPage = new IPAddressManagementViewPage(driver);
        ipAddressManagementViewPage.expandTreeRow(NETWORK_NAME_UPDATED);
        ipAddressManagementViewPage.deleteIPv4SubnetAssignment(getAddressAndMask(firstIPSubnetProperties));
    }

    @Test(priority = 32)
    @Description("Delete IPv6 Subnet Assignments")
    public void deleteIPv6SubnetAssignment() {
        ipAddressManagementViewPage = new IPAddressManagementViewPage(driver);
        ipAddressManagementViewPage.expandTreeRow(NETWORK_NAME_UPDATED);
        ipAddressManagementViewPage.deleteIPv6SubnetAssignment(getAddressAndMask(firstIPv6SubnetProperties));
    }

    @Test(priority = 33)
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
        Assert.assertTrue(ipAddressManagementViewPage.isSystemMessageSuccess());
    }

    @Test(priority = 34)
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

    @Test(priority = 35)
    @Description("Delete IP Network")
    public void deleteIPNetwork() {
        ipAddressManagementViewPage = new IPAddressManagementViewPage(driver);
        ipAddressManagementViewPage.deleteIPNetwork(NETWORK_NAME_UPDATED);
        Assert.assertTrue(ipAddressManagementViewPage.isSystemMessageSuccess());
    }

    @Test(priority = 36)
    @Description("Delete Role")
    public void deleteRole() {
//        RoleViewPage roleViewPage = ipAddressManagementViewPage.openRoleView(); BUG OSSWEB-11542
//        roleViewPage.deleteRole(ROLE_NAME_UPDATED);
//        Assert.assertTrue(ipAddressManagementViewPage.isSystemMessageSuccess());
//        Assert.assertFalse(roleViewPage.doesRoleNameExist(ROLE_NAME_UPDATED));
//        roleViewPage.exitRoleView();
    }

    private void prepareData(){
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

    private void updatePropertiesAfterIPv4SubnetsCreation(){
        firstIPSubnetProperties.put(SUBNET_PROPERTY_IDENTIFIER, "126.0.0.0/24 ["+ NETWORK_NAME +"]");
        firstIPSubnetProperties.put(SUBNET_PROPERTY_ADDRESS, "126.0.0.0");
        firstIPSubnetProperties.put(SUBNET_PROPERTY_HIGHEST_IP_ADDRESS, "126.0.0.254");
        firstIPSubnetProperties.put(SUBNET_PROPERTY_BROADCAST_IP_ADDRESS, "126.0.0.255");
        firstIPSubnetProperties.put(SUBNET_PROPERTY_IP_NETWORK_NAME, NETWORK_NAME);
        firstIPSubnetProperties.put(SUBNET_PROPERTY_MASK_LENGTH, "24");
        firstIPSubnetProperties.put(SUBNET_PROPERTY_SUBNET_TYPE, "BLOCK");
        firstIPSubnetProperties.put(SUBNET_PROPERTY_PERCENT_FREE, "0%");
        firstIPSubnetProperties.put(SUBNET_PROPERTY_CHILD_COUNT, "2");

        secondIPSubnetProperties.put(SUBNET_PROPERTY_IDENTIFIER, "126.0.0.0/25 ["+ NETWORK_NAME +"]");
        secondIPSubnetProperties.put(SUBNET_PROPERTY_ADDRESS, "126.0.0.0");
        secondIPSubnetProperties.put(SUBNET_PROPERTY_HIGHEST_IP_ADDRESS, "126.0.0.126");
        secondIPSubnetProperties.put(SUBNET_PROPERTY_BROADCAST_IP_ADDRESS, "126.0.0.127");
        secondIPSubnetProperties.put(SUBNET_PROPERTY_IP_NETWORK_NAME, NETWORK_NAME);
        secondIPSubnetProperties.put(SUBNET_PROPERTY_MASK_LENGTH, "25");
        secondIPSubnetProperties.put(SUBNET_PROPERTY_SUBNET_TYPE, "NETWORK");
        secondIPSubnetProperties.put(SUBNET_PROPERTY_PERCENT_FREE, "100%");
        secondIPSubnetProperties.put(SUBNET_PROPERTY_CHILD_COUNT, "0");
        secondIPSubnetProperties.put(SUBNET_PROPERTY_ROLE, ROLE_NAME);
        secondIPSubnetProperties.put(SUBNET_PROPERTY_DESCRIPTION, SUBNET_DESCRIPTION);

        thirdIPSubnetProperties.put(SUBNET_PROPERTY_IDENTIFIER, "126.0.0.128/25 ["+ NETWORK_NAME +"]");
        thirdIPSubnetProperties.put(SUBNET_PROPERTY_ADDRESS, "126.0.0.128");
        thirdIPSubnetProperties.put(SUBNET_PROPERTY_HIGHEST_IP_ADDRESS, "126.0.0.254");
        thirdIPSubnetProperties.put(SUBNET_PROPERTY_BROADCAST_IP_ADDRESS, "126.0.0.255");
        thirdIPSubnetProperties.put(SUBNET_PROPERTY_IP_NETWORK_NAME, NETWORK_NAME);
        thirdIPSubnetProperties.put(SUBNET_PROPERTY_MASK_LENGTH, "25");
        thirdIPSubnetProperties.put(SUBNET_PROPERTY_SUBNET_TYPE, "NETWORK");
        thirdIPSubnetProperties.put(SUBNET_PROPERTY_PERCENT_FREE, "100%");
        thirdIPSubnetProperties.put(SUBNET_PROPERTY_CHILD_COUNT, "0");
    }

    private void updatePropertiesAfterIPv6SubnetsCreation(){
        firstIPv6SubnetProperties.put(SUBNET_PROPERTY_IDENTIFIER, "::126:0/121 ["+ NETWORK_NAME +"]");
        firstIPv6SubnetProperties.put(SUBNET_PROPERTY_ADDRESS, "::126:0");
        firstIPv6SubnetProperties.put(SUBNET_PROPERTY_HIGHEST_IP_ADDRESS, "::126:7f");
        firstIPv6SubnetProperties.put(SUBNET_PROPERTY_IP_NETWORK_NAME, NETWORK_NAME);
        firstIPv6SubnetProperties.put(SUBNET_PROPERTY_MASK_LENGTH, "121");
        firstIPv6SubnetProperties.put(SUBNET_PROPERTY_SUBNET_TYPE, "BLOCK");
        firstIPv6SubnetProperties.put(SUBNET_PROPERTY_PERCENT_FREE, "0%");
        firstIPv6SubnetProperties.put(SUBNET_PROPERTY_CHILD_COUNT, "2");

        secondIPv6SubnetProperties.put(SUBNET_PROPERTY_IDENTIFIER, "::126:0/122 ["+ NETWORK_NAME +"]");
        secondIPv6SubnetProperties.put(SUBNET_PROPERTY_ADDRESS, "::126:0");
        secondIPv6SubnetProperties.put(SUBNET_PROPERTY_HIGHEST_IP_ADDRESS, "::126:3f");
        secondIPv6SubnetProperties.put(SUBNET_PROPERTY_IP_NETWORK_NAME, NETWORK_NAME);
        secondIPv6SubnetProperties.put(SUBNET_PROPERTY_MASK_LENGTH, "122");
        secondIPv6SubnetProperties.put(SUBNET_PROPERTY_SUBNET_TYPE, "NETWORK");
        secondIPv6SubnetProperties.put(SUBNET_PROPERTY_PERCENT_FREE, "100%");
        secondIPv6SubnetProperties.put(SUBNET_PROPERTY_CHILD_COUNT, "0");
        secondIPv6SubnetProperties.put(SUBNET_PROPERTY_ROLE, ROLE_NAME);
        secondIPv6SubnetProperties.put(SUBNET_PROPERTY_DESCRIPTION, SUBNET_DESCRIPTION);

        thirdIPv6SubnetProperties.put(SUBNET_PROPERTY_IDENTIFIER, "::126:40/122 ["+ NETWORK_NAME +"]");
        thirdIPv6SubnetProperties.put(SUBNET_PROPERTY_ADDRESS, "::126:40");
        thirdIPv6SubnetProperties.put(SUBNET_PROPERTY_HIGHEST_IP_ADDRESS, "::126:7f");
        thirdIPv6SubnetProperties.put(SUBNET_PROPERTY_IP_NETWORK_NAME, NETWORK_NAME);
        thirdIPv6SubnetProperties.put(SUBNET_PROPERTY_MASK_LENGTH, "122");
        thirdIPv6SubnetProperties.put(SUBNET_PROPERTY_SUBNET_TYPE, "NETWORK");
        thirdIPv6SubnetProperties.put(SUBNET_PROPERTY_PERCENT_FREE, "100%");
        thirdIPv6SubnetProperties.put(SUBNET_PROPERTY_CHILD_COUNT, "0");
    }

    private void updatePropertiesAfterIPv4SubnetAssignment(){
        firstIPSubnetProperties.put(SUBNET_PROPERTY_ASSIGNED_TO, IPAM_SELENIUM_TEST_ASSIGNMENT_IDENTIFIER);
        firstIPSubnetProperties.put(SUBNET_PROPERTY_ROLE, ROLE_NAME);
    }

    private void updatePropertiesAfterIPv6SubnetAssignment(){
        firstIPv6SubnetProperties.put(SUBNET_PROPERTY_ASSIGNED_TO, IPAM_SELENIUM_TEST_ASSIGNMENT_IDENTIFIER);
        firstIPv6SubnetProperties.put(SUBNET_PROPERTY_ROLE, ROLE_NAME);
    }

    private void updatePropertiesAfterIPv4HostsReservation(){
        secondIPSubnetProperties.put(SUBNET_PROPERTY_CHILD_COUNT, "2");
        secondIPSubnetProperties.put(SUBNET_PROPERTY_PERCENT_FREE, "99%");

        loopbackHostAddressProperties.put(HOST_PROPERTY_ADDRESS, LOOPBACK_HOST_ADDRESS);
        loopbackHostAddressProperties.put(HOST_PROPERTY_IDENTIFIER, LOOPBACK_HOST_ADDRESS+"/"+LOOPBACK_IPV4_HOST_MASK+" ["+NETWORK_NAME+"]");
        loopbackHostAddressProperties.put(HOST_PROPERTY_MASK, LOOPBACK_IPV4_HOST_MASK);
        loopbackHostAddressProperties.put(HOST_PROPERTY_IP_NETWORK_NAME, NETWORK_NAME);
        loopbackHostAddressProperties.put(HOST_PROPERTY_DESCRIPTION, LOOPBACK_HOST_DESCRIPTION);
        loopbackHostAddressProperties.put(HOST_PROPERTY_STATUS, "Reserved");
        hostAddressProperties.put(HOST_PROPERTY_ADDRESS, HOST_ADDRESS);
        hostAddressProperties.put(HOST_PROPERTY_IDENTIFIER, HOST_ADDRESS+"/25 ["+NETWORK_NAME+"]");
        hostAddressProperties.put(HOST_PROPERTY_MASK, "25");
        hostAddressProperties.put(HOST_PROPERTY_IP_NETWORK_NAME, NETWORK_NAME);
        hostAddressProperties.put(HOST_PROPERTY_DESCRIPTION, HOST_DESCRIPTION);
        hostAddressProperties.put(HOST_PROPERTY_STATUS, "Reserved");
    }

    private void updatePropertiesAfterIPv6HostsReservation(){
        secondIPv6SubnetProperties.put(SUBNET_PROPERTY_CHILD_COUNT, "2");
        secondIPv6SubnetProperties.put(SUBNET_PROPERTY_PERCENT_FREE, "98%");

        loopbackIPv6HostAddressProperties.put(HOST_PROPERTY_ADDRESS, LOOPBACK_IPV6_HOST_ADDRESS);
        loopbackIPv6HostAddressProperties.put(HOST_PROPERTY_IDENTIFIER, LOOPBACK_IPV6_HOST_ADDRESS+"/"+LOOPBACK_IPV6_HOST_MASK+" ["+NETWORK_NAME+"]");
        loopbackIPv6HostAddressProperties.put(HOST_PROPERTY_MASK, LOOPBACK_IPV6_HOST_MASK);
        loopbackIPv6HostAddressProperties.put(HOST_PROPERTY_IP_NETWORK_NAME, NETWORK_NAME);
        loopbackIPv6HostAddressProperties.put(HOST_PROPERTY_DESCRIPTION, LOOPBACK_HOST_DESCRIPTION);
        loopbackIPv6HostAddressProperties.put(HOST_PROPERTY_STATUS, "Reserved");
        ipv6HostAddressProperties.put(HOST_PROPERTY_ADDRESS, IPV6_HOST_ADDRESS);
        ipv6HostAddressProperties.put(HOST_PROPERTY_IDENTIFIER, IPV6_HOST_ADDRESS+"/122 ["+NETWORK_NAME+"]");
        ipv6HostAddressProperties.put(HOST_PROPERTY_MASK, "122");
        ipv6HostAddressProperties.put(HOST_PROPERTY_IP_NETWORK_NAME, NETWORK_NAME);
        ipv6HostAddressProperties.put(HOST_PROPERTY_DESCRIPTION, HOST_DESCRIPTION);
        ipv6HostAddressProperties.put(HOST_PROPERTY_STATUS, "Reserved");
    }

    private void updatePropertiesAfterRoleEdition(){
        firstIPSubnetProperties.put(SUBNET_PROPERTY_ROLE, ROLE_NAME_UPDATED);
        secondIPSubnetProperties.put(SUBNET_PROPERTY_ROLE, ROLE_NAME_UPDATED);
        firstIPv6SubnetProperties.put(SUBNET_PROPERTY_ROLE, ROLE_NAME_UPDATED);
        secondIPv6SubnetProperties.put(SUBNET_PROPERTY_ROLE, ROLE_NAME_UPDATED);
    }

    private void updatePropertiesAfterIPNetworkEdition(){
        firstIPSubnetProperties.put(SUBNET_PROPERTY_IDENTIFIER, firstIPSubnetProperties.get(SUBNET_PROPERTY_ADDRESS)+"/"+firstIPSubnetProperties.get(SUBNET_PROPERTY_MASK_LENGTH) +" ["+ NETWORK_NAME_UPDATED +"]");
        firstIPSubnetProperties.put(SUBNET_PROPERTY_IP_NETWORK_NAME, NETWORK_NAME_UPDATED);
        secondIPSubnetProperties.put(SUBNET_PROPERTY_IDENTIFIER, secondIPSubnetProperties.get(SUBNET_PROPERTY_ADDRESS)+"/"+secondIPSubnetProperties.get(SUBNET_PROPERTY_MASK_LENGTH)+ " ["+ NETWORK_NAME_UPDATED +"]");
        secondIPSubnetProperties.put(SUBNET_PROPERTY_IP_NETWORK_NAME, NETWORK_NAME_UPDATED);
        thirdIPSubnetProperties.put(SUBNET_PROPERTY_IDENTIFIER, thirdIPSubnetProperties.get(SUBNET_PROPERTY_ADDRESS)+"/"+thirdIPSubnetProperties.get(SUBNET_PROPERTY_MASK_LENGTH)+ " ["+ NETWORK_NAME_UPDATED +"]");
        thirdIPSubnetProperties.put(SUBNET_PROPERTY_IP_NETWORK_NAME, NETWORK_NAME_UPDATED);
        hostAddressProperties.put(HOST_PROPERTY_IDENTIFIER, HOST_ADDRESS+"/25 ["+NETWORK_NAME_UPDATED+"]");
        hostAddressProperties.put(HOST_PROPERTY_IP_NETWORK_NAME, NETWORK_NAME_UPDATED);
        loopbackHostAddressProperties.put(HOST_PROPERTY_IDENTIFIER, LOOPBACK_HOST_ADDRESS+"/"+LOOPBACK_IPV4_HOST_MASK+" ["+NETWORK_NAME_UPDATED+"]");
        loopbackHostAddressProperties.put(HOST_PROPERTY_IP_NETWORK_NAME, NETWORK_NAME_UPDATED);

        firstIPv6SubnetProperties.put(SUBNET_PROPERTY_IDENTIFIER, firstIPv6SubnetProperties.get(SUBNET_PROPERTY_ADDRESS)+"/"+firstIPv6SubnetProperties.get(SUBNET_PROPERTY_MASK_LENGTH) +" ["+ NETWORK_NAME_UPDATED +"]");
        firstIPv6SubnetProperties.put(SUBNET_PROPERTY_IP_NETWORK_NAME, NETWORK_NAME_UPDATED);
        secondIPv6SubnetProperties.put(SUBNET_PROPERTY_IDENTIFIER, secondIPv6SubnetProperties.get(SUBNET_PROPERTY_ADDRESS)+"/"+secondIPv6SubnetProperties.get(SUBNET_PROPERTY_MASK_LENGTH)+ " ["+ NETWORK_NAME_UPDATED +"]");
        secondIPv6SubnetProperties.put(SUBNET_PROPERTY_IP_NETWORK_NAME, NETWORK_NAME_UPDATED);
        thirdIPv6SubnetProperties.put(SUBNET_PROPERTY_IDENTIFIER, thirdIPv6SubnetProperties.get(SUBNET_PROPERTY_ADDRESS)+"/"+thirdIPv6SubnetProperties.get(SUBNET_PROPERTY_MASK_LENGTH)+ " ["+ NETWORK_NAME_UPDATED +"]");
        thirdIPv6SubnetProperties.put(SUBNET_PROPERTY_IP_NETWORK_NAME, NETWORK_NAME_UPDATED);
        ipv6HostAddressProperties.put(HOST_PROPERTY_IDENTIFIER, IPV6_HOST_ADDRESS+"/122 ["+NETWORK_NAME_UPDATED+"]");
        ipv6HostAddressProperties.put(HOST_PROPERTY_IP_NETWORK_NAME, NETWORK_NAME_UPDATED);
        loopbackIPv6HostAddressProperties.put(HOST_PROPERTY_IDENTIFIER, LOOPBACK_IPV6_HOST_ADDRESS+"/"+LOOPBACK_IPV6_HOST_MASK+" ["+NETWORK_NAME_UPDATED+"]");
        loopbackIPv6HostAddressProperties.put(HOST_PROPERTY_IP_NETWORK_NAME, NETWORK_NAME_UPDATED);
    }

    private void updatePropertiesAfterIPv4SubnetsSplit(){
        secondIPSubnetProperties.put(SUBNET_PROPERTY_PERCENT_FREE, "50%");
        secondIPSubnetProperties.put(SUBNET_PROPERTY_CHILD_COUNT, "1");
        secondIPSubnetProperties.put(SUBNET_PROPERTY_SUBNET_TYPE, "BLOCK");

        fourthIPSubnetProperties.put(SUBNET_PROPERTY_ADDRESS, "126.0.0.0");
        fourthIPSubnetProperties.put(SUBNET_PROPERTY_MASK_LENGTH, "26");
        fourthIPSubnetProperties.put(SUBNET_PROPERTY_SUBNET_TYPE, "BLOCK");
        fourthIPSubnetProperties.put(SUBNET_PROPERTY_CHILD_COUNT, "1");
        fourthIPSubnetProperties.put(SUBNET_PROPERTY_PERCENT_FREE, "50%");
        fourthIPSubnetProperties.put(SUBNET_PROPERTY_DESCRIPTION, SUBNET_DESCRIPTION);
        fourthIPSubnetProperties.put(SUBNET_PROPERTY_ROLE, MANAGEMENT_SECONDARY_ROLE);

        fifthIPSubnetProperties.put(SUBNET_PROPERTY_ADDRESS, "126.0.0.0");
        fifthIPSubnetProperties.put(SUBNET_PROPERTY_MASK_LENGTH, "27");
        fifthIPSubnetProperties.put(SUBNET_PROPERTY_SUBNET_TYPE, "NETWORK");
        fifthIPSubnetProperties.put(SUBNET_PROPERTY_CHILD_COUNT, "2");
        fifthIPSubnetProperties.put(SUBNET_PROPERTY_PERCENT_FREE, "97%");
        fifthIPSubnetProperties.put(SUBNET_PROPERTY_DESCRIPTION, "");
    }

    private void updatePropertiesAfterIPv6SubnetsSplit(){
        secondIPv6SubnetProperties.put(SUBNET_PROPERTY_PERCENT_FREE, "50%");
        secondIPv6SubnetProperties.put(SUBNET_PROPERTY_CHILD_COUNT, "1");
        secondIPv6SubnetProperties.put(SUBNET_PROPERTY_SUBNET_TYPE, "BLOCK");

        fourthIPv6SubnetProperties.put(SUBNET_PROPERTY_ADDRESS, "::126:0");
        fourthIPv6SubnetProperties.put(SUBNET_PROPERTY_MASK_LENGTH, "123");
        fourthIPv6SubnetProperties.put(SUBNET_PROPERTY_SUBNET_TYPE, "BLOCK");
        fourthIPv6SubnetProperties.put(SUBNET_PROPERTY_CHILD_COUNT, "1");
        fourthIPv6SubnetProperties.put(SUBNET_PROPERTY_PERCENT_FREE, "50%");
        fourthIPv6SubnetProperties.put(SUBNET_PROPERTY_DESCRIPTION, SUBNET_DESCRIPTION);
        fourthIPv6SubnetProperties.put(SUBNET_PROPERTY_ROLE, MANAGEMENT_SECONDARY_ROLE);

        fifthIPv6SubnetProperties.put(SUBNET_PROPERTY_ADDRESS, "::126:0");
        fifthIPv6SubnetProperties.put(SUBNET_PROPERTY_MASK_LENGTH, "124");
        fifthIPv6SubnetProperties.put(SUBNET_PROPERTY_SUBNET_TYPE, "NETWORK");
        fifthIPv6SubnetProperties.put(SUBNET_PROPERTY_CHILD_COUNT, "2");
        fifthIPv6SubnetProperties.put(SUBNET_PROPERTY_PERCENT_FREE, "93%");
        fifthIPv6SubnetProperties.put(SUBNET_PROPERTY_DESCRIPTION, "");
    }

    private void updatePropertiesAfterIPv4SubnetsMerge() {
        secondIPSubnetProperties.put(SUBNET_PROPERTY_PERCENT_FREE, "99%");
        secondIPSubnetProperties.put(SUBNET_PROPERTY_CHILD_COUNT, "2");
        secondIPSubnetProperties.put(SUBNET_PROPERTY_SUBNET_TYPE, "NETWORK");
    }

    private void updatePropertiesAfterIPv6SubnetsMerge() {
        secondIPv6SubnetProperties.put(SUBNET_PROPERTY_PERCENT_FREE, "98%");
        secondIPv6SubnetProperties.put(SUBNET_PROPERTY_CHILD_COUNT, "2");
        secondIPv6SubnetProperties.put(SUBNET_PROPERTY_SUBNET_TYPE, "NETWORK");
    }

    private void updatePropertiesAfterIPv4SubnetsEdition(){
        secondIPSubnetProperties.put(SUBNET_PROPERTY_IDENTIFIER, "126.0.0.0/"+IP_SUBNET_MASK_AFTER_UPDATE+" ["+NETWORK_NAME_UPDATED+"]");
        secondIPSubnetProperties.put(SUBNET_PROPERTY_MASK_LENGTH, IP_SUBNET_MASK_AFTER_UPDATE);
        secondIPSubnetProperties.put(SUBNET_PROPERTY_ROLE, MANAGEMENT_SECONDARY_ROLE);
        secondIPSubnetProperties.put(SUBNET_PROPERTY_DESCRIPTION, SUBNET_DESCRIPTION_UPDATED);
        secondIPSubnetProperties.put(SUBNET_PROPERTY_HIGHEST_IP_ADDRESS, "126.0.0.62");
        secondIPSubnetProperties.put(SUBNET_PROPERTY_BROADCAST_IP_ADDRESS, "126.0.0.63");
        secondIPSubnetProperties.put(SUBNET_PROPERTY_PERCENT_FREE, "98%");
        thirdIPSubnetProperties.put(SUBNET_PROPERTY_SUBNET_TYPE, "BLOCK");
        firstIPSubnetProperties.put(SUBNET_PROPERTY_PERCENT_FREE, "25%");
        hostAddressProperties.put(HOST_PROPERTY_IDENTIFIER, HOST_ADDRESS+"/26 ["+NETWORK_NAME_UPDATED+"]");
        hostAddressProperties.put(HOST_PROPERTY_MASK, "26");
    }

    private void updatePropertiesAfterIPv6SubnetsEdition(){
        secondIPv6SubnetProperties.put(SUBNET_PROPERTY_IDENTIFIER, "::126:0/"+IPV6_SUBNET_MASK_AFTER_UPDATE+" ["+NETWORK_NAME_UPDATED+"]");
        secondIPv6SubnetProperties.put(SUBNET_PROPERTY_MASK_LENGTH, IPV6_SUBNET_MASK_AFTER_UPDATE);
        secondIPv6SubnetProperties.put(SUBNET_PROPERTY_ROLE, MANAGEMENT_SECONDARY_ROLE);
        secondIPv6SubnetProperties.put(SUBNET_PROPERTY_DESCRIPTION, SUBNET_DESCRIPTION_UPDATED);
        secondIPv6SubnetProperties.put(SUBNET_PROPERTY_HIGHEST_IP_ADDRESS, "::126:1f");
        secondIPv6SubnetProperties.put(SUBNET_PROPERTY_PERCENT_FREE, "97%");
        thirdIPv6SubnetProperties.put(SUBNET_PROPERTY_SUBNET_TYPE, "BLOCK");
        firstIPv6SubnetProperties.put(SUBNET_PROPERTY_PERCENT_FREE, "25%");
        ipv6HostAddressProperties.put(HOST_PROPERTY_IDENTIFIER, IPV6_HOST_ADDRESS+"/123 ["+NETWORK_NAME_UPDATED+"]");
        ipv6HostAddressProperties.put(HOST_PROPERTY_MASK, "123");
    }

    private void updatePropertiesAfterIPv4SubnetAssignmentEdition(){
        firstIPSubnetProperties.put(SUBNET_PROPERTY_ROLE, STANDARD_ROLE);
    }

    private void updatePropertiesAfterIPv6SubnetAssignmentEdition(){
        firstIPv6SubnetProperties.put(SUBNET_PROPERTY_ROLE, STANDARD_ROLE);
    }

    private void updatePropertiesAfterIPv4HostEdition(){
        hostAddressProperties.put(HOST_PROPERTY_IDENTIFIER, HOST_ADDRESS+"/"+LOOPBACK_IPV4_HOST_MASK+" ["+NETWORK_NAME_UPDATED+"]");
        hostAddressProperties.put(HOST_PROPERTY_MASK, LOOPBACK_IPV4_HOST_MASK);
    }

    private void updatePropertiesAfterIPv6HostEdition(){
        ipv6HostAddressProperties.put(HOST_PROPERTY_IDENTIFIER, IPV6_HOST_ADDRESS+"/"+LOOPBACK_IPV6_HOST_MASK+" ["+NETWORK_NAME_UPDATED+"]");
        ipv6HostAddressProperties.put(HOST_PROPERTY_MASK, LOOPBACK_IPV6_HOST_MASK);
    }

    private void checkAttributesOnIPAMTree(Map<String, String> properties){
        ipAddressManagementViewPage.selectTreeRowContains(getAddressAndMask(properties));
        Set<String> keySet = properties.keySet();
        for(String key: keySet){
            Assert.assertEquals(ipAddressManagementViewPage.getPropertyValue(key), properties.get(key));
        }
        ipAddressManagementViewPage.selectTreeRowContains(getAddressAndMask(properties));
    }

    private void checkAttributesOnNewIV(PropertyPanel propertyPanel, Map<String, String> properties, Map<String, String> inventoryPropertyNames){
        Set<String> keySet = properties.keySet();
        for(String key: keySet){
            if(!propertiesNotVisibleOnNewInventoryView.contains(key)){
                String inventoryPropertyName = inventoryPropertyNames.get(key);
                Assert.assertEquals(propertyPanel.getPropertyValue(inventoryPropertyName), properties.get(key));
            }
        }
    }

    private String getAddressAndMask(Map<String, String> properties){
        if(properties.containsKey(SUBNET_PROPERTY_ADDRESS)){
            return properties.get(SUBNET_PROPERTY_ADDRESS)+"/"+properties.get(SUBNET_PROPERTY_MASK_LENGTH);
        }
        return properties.get(HOST_PROPERTY_ADDRESS)+"/"+properties.get(HOST_PROPERTY_MASK);
    }
}
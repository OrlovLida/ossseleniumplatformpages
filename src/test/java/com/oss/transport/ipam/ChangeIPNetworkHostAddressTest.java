package com.oss.transport.ipam;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.bpm.ProcessOverviewPage;
import com.oss.pages.bpm.TasksPage;
import com.oss.pages.bpm.processinstances.ProcessWizardPage;
import com.oss.pages.transport.ipam.IPAddressAssignmentWizardPage;
import com.oss.pages.transport.ipam.IPAddressManagementViewPage;
import com.oss.pages.transport.ipam.IPSubnetWizardPage;
import com.oss.pages.transport.ipam.helper.IPAddressAssignmentWizardProperties;
import com.oss.pages.transport.ipam.helper.IPSubnetFilterProperties;
import com.oss.pages.transport.ipam.helper.IPSubnetWizardProperties;
import com.oss.utils.TestListener;

import io.qameta.allure.Description;

import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.HOST_ASSIGNMENT_PROPERTY_ASSIGNED_TO;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.HOST_ASSIGNMENT_PROPERTY_IDENTIFIER;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.HOST_PROPERTY_IDENTIFIER;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.NETWORK_PROPERTY_NAME;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.NEW_ADDRESS_MODE;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.PHYSICAL_DEVICE;

@Listeners({TestListener.class})
public class ChangeIPNetworkHostAddressTest extends BaseTestCase {
    private static final String FIRST_NETWORK_NAME = "ChangeIPNetworkHost1";
    private static final String SECOND_NETWORK_NAME = "ChangeIPNetworkHost2";
    private static final String THIRD_NETWORK_NAME = "ChangeIPNetworkHost3";
    private static final String DESCRIPTION = "Host address case";
    private static final String BLOCK_SUBNET_TYPE = "Block";
    private static final String NETWORK_SUBNET_TYPE = "Network";
    private static final String IPv6 = "IPv6";
    private static final String IPv4 = "IPv4";
    private static final int FIRST_INDEX = 0;
    private static final int SECOND_INDEX = 1;
    private static final int THIRD_INDEX = 2;
    private static final String FALSE = "FALSE";
    private static final String TASK_NAME = "Correct data";
    private IPAddressManagementViewPage ipAddressManagementViewPage;
    private static final int AMOUNT_OF_SELECTED_SUBNETS_FOR_FIRST_NETWORK = 2;
    private static final int AMOUNT_OF_SELECTED_SUBNETS_FOR_SECOND_AND_THIRD_NETWORK = 1;
    private static final String FIXED_SUBNET_IPv4_ADDRESS = "20.000";
    private static final String FIXED_SUBNET_IPv6_ADDRESS = "::20:0";
    private static final String HIGHER_OR_EQUAL = ">=";
    private static final String IPv4_NETWORK_MASK_ = "24";
    private static final String IPv6_NETWORK_MASK_ = "120";
    private static final String LOWER_IPv4_NETWORK_MASK = "23";
    private static final String LOWER_IPv6_NETWORK_MASK = "119";
    private static final int AMOUNT_OF_HAA = 3;
    private static IPSubnetWizardProperties TYPE_BLOCK_SUBNET_WIZARD_PROPERTIES;
    private static IPSubnetWizardProperties TYPE_NETWORK_SUBNET_WIZARD_PROPERTIES;
    private static IPSubnetFilterProperties IPv4_SUBNET_FILTER_PROPERTIES_FOR_FIRST_NETWORK;
    private static IPSubnetFilterProperties IPv4_SUBNET_FILTER_PROPERTIES_FOR_SECOND_AND_THIRD_NETWORK;
    private static IPSubnetFilterProperties IPv6_SUBNET_FILTER_PROPERTIES_FOR_FIRST_NETWORK;
    private static IPSubnetFilterProperties IPv6_SUBNET_FILTER_PROPERTIES_FOR_SECOND_AND_THIRD_NETWORK;
    private static final String ROUTER_ID = "1595072";
    private static HashMap<String, SubnetTree> firstNetworkProperties;
    private static HashMap<String, SubnetTree> secondNetworkProperties;
    private static HashMap<String, SubnetTree> thirdNetworkProperties;
    private static final String ROUTER_IDENTIFIER = "GK_Location-Router-4";
    private static String dcpProcessCode;

    @BeforeClass
    public void prepareData() {
        TYPE_BLOCK_SUBNET_WIZARD_PROPERTIES = new IPSubnetWizardProperties(BLOCK_SUBNET_TYPE);
        TYPE_NETWORK_SUBNET_WIZARD_PROPERTIES = new IPSubnetWizardProperties(NETWORK_SUBNET_TYPE);
        IPv4_SUBNET_FILTER_PROPERTIES_FOR_FIRST_NETWORK = new IPSubnetFilterProperties(FIXED_SUBNET_IPv4_ADDRESS, FIXED_SUBNET_IPv4_ADDRESS, HIGHER_OR_EQUAL, IPv4_NETWORK_MASK_);
        IPv6_SUBNET_FILTER_PROPERTIES_FOR_FIRST_NETWORK = new IPSubnetFilterProperties(FIXED_SUBNET_IPv6_ADDRESS, FIXED_SUBNET_IPv6_ADDRESS, HIGHER_OR_EQUAL, IPv6_NETWORK_MASK_);
        IPv4_SUBNET_FILTER_PROPERTIES_FOR_SECOND_AND_THIRD_NETWORK = new IPSubnetFilterProperties(FIXED_SUBNET_IPv4_ADDRESS, FIXED_SUBNET_IPv4_ADDRESS, HIGHER_OR_EQUAL, LOWER_IPv4_NETWORK_MASK);
        IPv6_SUBNET_FILTER_PROPERTIES_FOR_SECOND_AND_THIRD_NETWORK = new IPSubnetFilterProperties(FIXED_SUBNET_IPv6_ADDRESS, FIXED_SUBNET_IPv6_ADDRESS, HIGHER_OR_EQUAL, LOWER_IPv6_NETWORK_MASK);
        initializeFirstSubnetTree();
        initializeSecondSubnetTree();
    }

    @Test(priority = 1)
    @Description("Create IP networks")
    public void createIPNetworks() {
        ipAddressManagementViewPage = IPAddressManagementViewPage.goToIPAddressManagementPage(driver, BASIC_URL);
        ipAddressManagementViewPage.createIPNetwork(FIRST_NETWORK_NAME, DESCRIPTION);
        ipAddressManagementViewPage.selectTreeRow(FIRST_NETWORK_NAME);
        Assert.assertEquals(ipAddressManagementViewPage.getPropertyValue(NETWORK_PROPERTY_NAME), FIRST_NETWORK_NAME);
        ipAddressManagementViewPage.unselectTreeRow(FIRST_NETWORK_NAME);
        ipAddressManagementViewPage.createIPNetwork(SECOND_NETWORK_NAME, DESCRIPTION);
        ipAddressManagementViewPage.selectTreeRow(SECOND_NETWORK_NAME);
        Assert.assertEquals(ipAddressManagementViewPage.getPropertyValue(NETWORK_PROPERTY_NAME), SECOND_NETWORK_NAME);
        ipAddressManagementViewPage.unselectTreeRow(SECOND_NETWORK_NAME);
        ipAddressManagementViewPage.createIPNetwork(THIRD_NETWORK_NAME, DESCRIPTION);
        ipAddressManagementViewPage.selectTreeRow(THIRD_NETWORK_NAME);
        Assert.assertEquals(ipAddressManagementViewPage.getPropertyValue(NETWORK_PROPERTY_NAME), THIRD_NETWORK_NAME);
        ipAddressManagementViewPage.unselectTreeRow(THIRD_NETWORK_NAME);
    }

    @Test(priority = 2)
    @Description("Creating subnets for first network")
    public void createFirstNetworkSubnets() {
        ipAddressManagementViewPage.selectTreeRow(FIRST_NETWORK_NAME);
        createSubnets(IPv6_SUBNET_FILTER_PROPERTIES_FOR_FIRST_NETWORK, AMOUNT_OF_SELECTED_SUBNETS_FOR_FIRST_NETWORK, TYPE_BLOCK_SUBNET_WIZARD_PROPERTIES, TYPE_NETWORK_SUBNET_WIZARD_PROPERTIES);
        createSubnets(IPv4_SUBNET_FILTER_PROPERTIES_FOR_FIRST_NETWORK, AMOUNT_OF_SELECTED_SUBNETS_FOR_FIRST_NETWORK, TYPE_BLOCK_SUBNET_WIZARD_PROPERTIES, TYPE_NETWORK_SUBNET_WIZARD_PROPERTIES);
        ipAddressManagementViewPage.selectTreeRow(FIRST_NETWORK_NAME);
        ipAddressManagementViewPage.unselectTreeRow(FIRST_NETWORK_NAME);
    }

    @Test(priority = 3)
    @Description("Creating IP Host Address Assignments and checking theirs properties")
    public void createIPHostAddressAssignmentsForFirstSubnet() {
        ipAddressManagementViewPage.expandTreeRow(FIRST_NETWORK_NAME);
        String assignmentIdentifier;
        for (int indexOfAddress = 0; indexOfAddress < AMOUNT_OF_HAA; indexOfAddress++) {
            ipAddressManagementViewPage.expandTreeRowContains(firstNetworkProperties.get(IPv4).blockSubnet);
            createIPHostAddressAssignment(IPv4);
            ipAddressManagementViewPage.selectTreeRowContains(firstNetworkProperties.get(IPv4).getIPHostAddressAssignment(indexOfAddress));
            assignmentIdentifier = firstNetworkProperties.get(IPv4).getIPHostAddressAssignmentIdentifier(indexOfAddress);
            Assert.assertEquals(ipAddressManagementViewPage.getPropertyValue(HOST_PROPERTY_IDENTIFIER), assignmentIdentifier);
            Assert.assertEquals(ipAddressManagementViewPage.getPropertyValue(HOST_ASSIGNMENT_PROPERTY_ASSIGNED_TO), ROUTER_IDENTIFIER);
            ipAddressManagementViewPage.unselectTreeRow(firstNetworkProperties.get(IPv4).getIPHostAddressAssignment(indexOfAddress));
            ipAddressManagementViewPage.expandTreeRowContains(firstNetworkProperties.get(IPv6).blockSubnet);
            createIPHostAddressAssignment(IPv6);
            ipAddressManagementViewPage.selectTreeRowContains(firstNetworkProperties.get(IPv6).getIPHostAddressAssignment(indexOfAddress));
            assignmentIdentifier = firstNetworkProperties.get(IPv6).getIPHostAddressAssignmentIdentifier(indexOfAddress);
            Assert.assertEquals(ipAddressManagementViewPage.getPropertyValue(HOST_PROPERTY_IDENTIFIER), assignmentIdentifier);
            Assert.assertEquals(ipAddressManagementViewPage.getPropertyValue(HOST_ASSIGNMENT_PROPERTY_ASSIGNED_TO), ROUTER_IDENTIFIER);
            ipAddressManagementViewPage.unselectTreeRow(firstNetworkProperties.get(IPv6).getIPHostAddressAssignment(indexOfAddress));
        }
    }

    @Test(priority = 4)
    @Description("Creating subnets for second and third network")
    public void createSecondAndThirdNetworkSubnets() {
        ipAddressManagementViewPage.selectTreeRow(SECOND_NETWORK_NAME);
        createSubnets(IPv4_SUBNET_FILTER_PROPERTIES_FOR_SECOND_AND_THIRD_NETWORK, AMOUNT_OF_SELECTED_SUBNETS_FOR_SECOND_AND_THIRD_NETWORK, TYPE_BLOCK_SUBNET_WIZARD_PROPERTIES);
        createSubnets(IPv6_SUBNET_FILTER_PROPERTIES_FOR_SECOND_AND_THIRD_NETWORK, AMOUNT_OF_SELECTED_SUBNETS_FOR_SECOND_AND_THIRD_NETWORK, TYPE_BLOCK_SUBNET_WIZARD_PROPERTIES);
        ipAddressManagementViewPage.selectTreeRow(SECOND_NETWORK_NAME);
        ipAddressManagementViewPage.unselectTreeRow(SECOND_NETWORK_NAME);
        ipAddressManagementViewPage.selectTreeRow(THIRD_NETWORK_NAME);
        createSubnets(IPv4_SUBNET_FILTER_PROPERTIES_FOR_SECOND_AND_THIRD_NETWORK, AMOUNT_OF_SELECTED_SUBNETS_FOR_SECOND_AND_THIRD_NETWORK, TYPE_BLOCK_SUBNET_WIZARD_PROPERTIES);
        createSubnets(IPv6_SUBNET_FILTER_PROPERTIES_FOR_SECOND_AND_THIRD_NETWORK, AMOUNT_OF_SELECTED_SUBNETS_FOR_SECOND_AND_THIRD_NETWORK, TYPE_BLOCK_SUBNET_WIZARD_PROPERTIES);
        ipAddressManagementViewPage.selectTreeRow(THIRD_NETWORK_NAME);
        ipAddressManagementViewPage.unselectTreeRow(THIRD_NETWORK_NAME);
    }

    @Test(priority = 5)
    @Description("Creating DCP process")
    public void createDCPProcess() {
        ProcessOverviewPage.goToProcessOverviewPage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        ProcessWizardPage processWizardPage = new ProcessWizardPage(driver);
        dcpProcessCode = processWizardPage.createSimpleDCPV2();
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        tasksPage.startTask(dcpProcessCode, TasksPage.CORRECT_DATA_TASK);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 6)
    @Description("Move IPv4 and IPv6 Host Address Assignments to second network")
    public void changeIPNetworkForIPv4HostAddressAssignments() {
        ipAddressManagementViewPage = IPAddressManagementViewPage.goToIPAddressManagementPage(driver, BASIC_URL);
        ipAddressManagementViewPage.expandTreeRow(FIRST_NETWORK_NAME);
        ipAddressManagementViewPage.expandTreeRowContains(firstNetworkProperties.get(IPv4).blockSubnet);
        ipAddressManagementViewPage.expandTreeRowContains(firstNetworkProperties.get(IPv4).networkSubnet);
        ipAddressManagementViewPage.expandTreeRowContains(firstNetworkProperties.get(IPv4).getIPHostAddress(FIRST_INDEX));
        String firstIPv4HostAddressAssignment = firstNetworkProperties.get(IPv4).getIPHostAddressAssignment(FIRST_INDEX);
        ipAddressManagementViewPage.changeIPNetworkForIPv4AddressAssignment(SECOND_NETWORK_NAME, firstIPv4HostAddressAssignment);
        ipAddressManagementViewPage.expandTreeRowContains(FIRST_NETWORK_NAME);
        ipAddressManagementViewPage.expandTreeRowContains(firstNetworkProperties.get(IPv4).blockSubnet);
        ipAddressManagementViewPage.expandTreeRowContains(firstNetworkProperties.get(IPv4).networkSubnet);
        ipAddressManagementViewPage.expandTreeRowContains(firstNetworkProperties.get(IPv4).getIPHostAddress(SECOND_INDEX));
        ipAddressManagementViewPage.expandTreeRowContains(firstNetworkProperties.get(IPv4).getIPHostAddress(SECOND_INDEX));
        String secondIPv4HostAddressAssignment = firstNetworkProperties.get(IPv4).getIPHostAddressAssignment(SECOND_INDEX);
        String thirdIPv4HostAddressAssignment = firstNetworkProperties.get(IPv4).getIPHostAddressAssignment(THIRD_INDEX);
        String[] arrayOfHostAddressAssignments = {secondIPv4HostAddressAssignment, thirdIPv4HostAddressAssignment};
        ipAddressManagementViewPage.changeIPNetworkForIPv4AddressAssignment(SECOND_NETWORK_NAME, arrayOfHostAddressAssignments);
    }

    @Test(priority = 7)
    @Description("Move IPv6 Host Address Assignments to second network")
    public void changeIPNetworkForIPv6HostAddressAssignments() {
        ipAddressManagementViewPage.expandTreeRow(FIRST_NETWORK_NAME);
        ipAddressManagementViewPage.expandTreeRowContains(firstNetworkProperties.get(IPv6).blockSubnet);
        ipAddressManagementViewPage.expandTreeRowContains(firstNetworkProperties.get(IPv6).networkSubnet);
        ipAddressManagementViewPage.expandTreeRowContains(firstNetworkProperties.get(IPv6).getIPHostAddress(FIRST_INDEX));
        String firstIPv6HostAddressAssignment = firstNetworkProperties.get(IPv6).getIPHostAddressAssignment(FIRST_INDEX);
        ipAddressManagementViewPage.changeIPNetworkForIPv6AddressAssignment(SECOND_NETWORK_NAME, firstIPv6HostAddressAssignment);
        ipAddressManagementViewPage.expandTreeRowContains(FIRST_NETWORK_NAME);
        ipAddressManagementViewPage.expandTreeRowContains(firstNetworkProperties.get(IPv6).blockSubnet);
        ipAddressManagementViewPage.expandTreeRowContains(firstNetworkProperties.get(IPv6).networkSubnet);
        ipAddressManagementViewPage.expandTreeRowContains(firstNetworkProperties.get(IPv6).getIPHostAddress(SECOND_INDEX));
        ipAddressManagementViewPage.expandTreeRowContains(firstNetworkProperties.get(IPv6).getIPHostAddress(THIRD_INDEX));
        String secondIPv6HostAddressAssignment = firstNetworkProperties.get(IPv6).getIPHostAddressAssignment(SECOND_INDEX);
        String thirdIPv6HostAddressAssignment = firstNetworkProperties.get(IPv6).getIPHostAddressAssignment(THIRD_INDEX);
        String[] arrayOfHostAddressAssignments = {secondIPv6HostAddressAssignment, thirdIPv6HostAddressAssignment};
        ipAddressManagementViewPage.changeIPNetworkForIPv6AddressAssignment(SECOND_NETWORK_NAME, arrayOfHostAddressAssignments);
    }

    @Test(priority = 8)
    @Description("Accept planned actions")
    public void acceptPlannedActions() {
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.completeTask(dcpProcessCode, TASK_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 9)
    @Description("Move IPv4 Host Address Assignments to second network")
    public void changeIPNetworkForIPv4HostAddresses() {
        ipAddressManagementViewPage = IPAddressManagementViewPage.goToIPAddressManagementPage(driver, BASIC_URL);
        ipAddressManagementViewPage.expandTreeRow(FIRST_NETWORK_NAME);
        ipAddressManagementViewPage.expandTreeRowContains(firstNetworkProperties.get(IPv4).blockSubnet);
        ipAddressManagementViewPage.expandTreeRowContains(firstNetworkProperties.get(IPv4).networkSubnet);
        String firstIPv4HostAddress = firstNetworkProperties.get(IPv4).getIPHostAddress(FIRST_INDEX);
        ipAddressManagementViewPage.changeIPNetworkForIPv4HostAddress(THIRD_NETWORK_NAME, firstIPv4HostAddress);
        ipAddressManagementViewPage.expandTreeRowContains(FIRST_NETWORK_NAME);
        ipAddressManagementViewPage.expandTreeRowContains(firstNetworkProperties.get(IPv4).blockSubnet);
        ipAddressManagementViewPage.expandTreeRowContains(firstNetworkProperties.get(IPv4).networkSubnet);
        String secondIPv6HostAddress = firstNetworkProperties.get(IPv4).getIPHostAddress(SECOND_INDEX);
        String thirdIPv6HostAddress = firstNetworkProperties.get(IPv4).getIPHostAddress(THIRD_INDEX);
        String[] arrayOfHostAddresses = {secondIPv6HostAddress, thirdIPv6HostAddress};
        ipAddressManagementViewPage.changeIPNetworkForIPv4HostAddress(THIRD_NETWORK_NAME, arrayOfHostAddresses);
    }

    @Test(priority = 10)
    @Description("Move IPv6 Host Address Assignments to second network")
    public void changeIPNetworkForIPv6HostAddresses() {
        ipAddressManagementViewPage.expandTreeRow(FIRST_NETWORK_NAME);
        ipAddressManagementViewPage.expandTreeRowContains(firstNetworkProperties.get(IPv6).blockSubnet);
        ipAddressManagementViewPage.expandTreeRowContains(firstNetworkProperties.get(IPv6).networkSubnet);
        String firstIPv6HostAddress = firstNetworkProperties.get(IPv6).getIPHostAddress(FIRST_INDEX);
        ipAddressManagementViewPage.changeIPNetworkForIPv6HostAddress(THIRD_NETWORK_NAME, firstIPv6HostAddress);
        ipAddressManagementViewPage.expandTreeRowContains(FIRST_NETWORK_NAME);
        ipAddressManagementViewPage.expandTreeRowContains(firstNetworkProperties.get(IPv6).blockSubnet);
        ipAddressManagementViewPage.expandTreeRowContains(firstNetworkProperties.get(IPv6).networkSubnet);
        String secondIPv6HostAddress = firstNetworkProperties.get(IPv6).getIPHostAddress(SECOND_INDEX);
        String thirdIPv6HostAddress = firstNetworkProperties.get(IPv6).getIPHostAddress(THIRD_INDEX);
        String[] arrayOfHostAddresses = {secondIPv6HostAddress, thirdIPv6HostAddress};
        ipAddressManagementViewPage.changeIPNetworkForIPv6HostAddress(THIRD_NETWORK_NAME, arrayOfHostAddresses);
        updateSubnetsProperties();
    }

    @Test(priority = 11)
    @Description("Check if Host Assignments are present in Second Network")
    public void checkAssignmentsPresenceInSecondNetwork() {
        ipAddressManagementViewPage.expandTreeRowContains(SECOND_NETWORK_NAME);
        checkAssignmentsPresenceInSecondNetworkByIPVersion(IPv4);
        checkAssignmentsPresenceInSecondNetworkByIPVersion(IPv6);
    }

    @Test(priority = 12)
    @Description("Check if Host Addresses are present in Third Network")
    public void checkHostAddressesPresenceInThirdNetwork() {
        ipAddressManagementViewPage = IPAddressManagementViewPage.refreshIPAddressManagementViewPage(driver);
        ipAddressManagementViewPage.expandTreeRowContains(THIRD_NETWORK_NAME);
        checkHostAddressPresenceInThirdNetworkByIPVersion(IPv4);
        checkHostAddressPresenceInThirdNetworkByIPVersion(IPv6);
    }

    @Test(priority = 13)
    @Description("Delete all created objects")
    public void deleteAllObjects() {
        ipAddressManagementViewPage = IPAddressManagementViewPage.refreshIPAddressManagementViewPage(driver);
        deleteNetworkWithAllObjects(FIRST_NETWORK_NAME, firstNetworkProperties);
        deleteNetworkWithAllObjects(SECOND_NETWORK_NAME, secondNetworkProperties);
        deleteNetworkWithAllObjects(THIRD_NETWORK_NAME, thirdNetworkProperties);
    }

    private void deleteNetworkWithAllObjects(String networkName, HashMap<String, SubnetTree> networkProperties) {
        deleteSubnetsWithAllObjects(networkName, networkProperties, IPv6);
        ipAddressManagementViewPage.selectTreeRow(networkName);
        ipAddressManagementViewPage.unselectTreeRow(networkName);
        deleteSubnetsWithAllObjects(networkName, networkProperties, IPv4);
        ipAddressManagementViewPage.deleteIPNetwork(networkName);
    }

    private void deleteSubnetsWithAllObjects(String networkName, HashMap<String, SubnetTree> networkProperties, String ipVersion) {
        ipAddressManagementViewPage.expandTreeRowContains(networkName);
        ipAddressManagementViewPage.expandTreeRowContains(networkProperties.get(ipVersion).blockSubnet);
        if (ipVersion.equals(IPv4)) {
            ipAddressManagementViewPage.deleteIPv4SubnetTypeOfNetwork(networkProperties.get(ipVersion).networkSubnet);
            ipAddressManagementViewPage.deleteIPv4SubnetTypeOfBlock(networkProperties.get(ipVersion).blockSubnet);
        } else {
            ipAddressManagementViewPage.deleteIPv6SubnetTypeOfNetwork(networkProperties.get(ipVersion).networkSubnet);
            ipAddressManagementViewPage.deleteIPv6SubnetTypeOfBlock(networkProperties.get(ipVersion).blockSubnet);
        }
    }

    private void checkAssignmentsPresenceInSecondNetworkByIPVersion(String ipVersion) {
        ipAddressManagementViewPage.expandTreeRowContains(secondNetworkProperties.get(ipVersion).blockSubnet);
        ipAddressManagementViewPage.expandTreeRowContains(secondNetworkProperties.get(ipVersion).networkSubnet);
        for (int indexOfAddress = 0; indexOfAddress < AMOUNT_OF_HAA; indexOfAddress++) {
            ipAddressManagementViewPage.expandTreeRowContains(secondNetworkProperties.get(ipVersion).getIPHostAddress(indexOfAddress));
            ipAddressManagementViewPage.selectTreeRowContains(secondNetworkProperties.get(ipVersion).getIPHostAddressAssignment(indexOfAddress));
            String hostAssignmentIdentifier = secondNetworkProperties.get(ipVersion).getIPHostAddressAssignmentIdentifier(indexOfAddress);
            Assert.assertEquals(ipAddressManagementViewPage.getPropertyValue(HOST_ASSIGNMENT_PROPERTY_IDENTIFIER), hostAssignmentIdentifier);
            ipAddressManagementViewPage.unselectTreeRow(secondNetworkProperties.get(ipVersion).getIPHostAddressAssignment(indexOfAddress));
        }
    }

    private void checkHostAddressPresenceInThirdNetworkByIPVersion(String ipVersion) {
        ipAddressManagementViewPage.expandTreeRowContains(thirdNetworkProperties.get(ipVersion).blockSubnet);
        ipAddressManagementViewPage.expandTreeRowContains(thirdNetworkProperties.get(ipVersion).networkSubnet);
        for (int indexOfAddress = 0; indexOfAddress < AMOUNT_OF_HAA; indexOfAddress++) {
            ipAddressManagementViewPage.selectTreeRowContains(thirdNetworkProperties.get(ipVersion).getIPHostAddress(indexOfAddress));
            String hostAssignmentIdentifier = thirdNetworkProperties.get(ipVersion).getIPHostAddressIdentifier(indexOfAddress);
            Assert.assertEquals(ipAddressManagementViewPage.getPropertyValue(HOST_PROPERTY_IDENTIFIER), hostAssignmentIdentifier);
            ipAddressManagementViewPage.unselectTreeRow(thirdNetworkProperties.get(ipVersion).getIPHostAddress(indexOfAddress));
        }
    }

    private void createIPHostAddressAssignment(String ipVersion) {
        IPAddressAssignmentWizardProperties firstHostAssignmentProperties = IPAddressAssignmentWizardProperties.builder()
                .wizardMode(NEW_ADDRESS_MODE).isInNAT(FALSE).isPrimary(FALSE)
                .assignmentType(PHYSICAL_DEVICE).assignmentName(ROUTER_ID).build();
        IPAddressAssignmentWizardPage ipAddressAssignmentWizardPage;
        if (ipVersion.equals(IPv4)) {
            ipAddressAssignmentWizardPage = ipAddressManagementViewPage.assignIPv4HostAddressFromSubnetContextWithVisibleButton(firstNetworkProperties.get(IPv4).networkSubnet);
        } else {
            ipAddressAssignmentWizardPage = ipAddressManagementViewPage.assignIPv6HostAddressFromSubnetContextWithVisibleButton(firstNetworkProperties.get(IPv6).networkSubnet);
        }
        ipAddressAssignmentWizardPage.assignIPAddress(firstHostAssignmentProperties);
        ipAddressManagementViewPage = new IPAddressManagementViewPage(driver);
    }

    private void initializeFirstSubnetTree() {
        firstNetworkProperties = new HashMap<>();
        SubnetTree ipv4SubnetTree = new SubnetTree();
        ipv4SubnetTree.initializeTreeForIPv4SubnetInFirstNetwork();
        SubnetTree ipv6SubnetTree = new SubnetTree();
        ipv6SubnetTree.initializeTreeForIPv6SubnetInFirstNetwork();
        firstNetworkProperties.put(IPv4, ipv4SubnetTree);
        firstNetworkProperties.put(IPv6, ipv6SubnetTree);
    }

    private void initializeSecondSubnetTree() {
        secondNetworkProperties = new HashMap<>();
        SubnetTree ipv4SubnetTree = new SubnetTree();
        ipv4SubnetTree.initializeTreeForIPv4SubnetInSecondNetwork();
        SubnetTree ipv6SubnetTree = new SubnetTree();
        ipv6SubnetTree.initializeTreeForIPv6SubnetInSecondNetwork();
        secondNetworkProperties.put(IPv4, ipv4SubnetTree);
        secondNetworkProperties.put(IPv6, ipv6SubnetTree);
    }

    private void updateSubnetsProperties() {
        secondNetworkProperties.get(IPv4).updateTreeForIPv4SubnetInSecondNetwork();
        secondNetworkProperties.get(IPv6).updateTreeForIPv6SubnetInSecondNetwork();
        thirdNetworkProperties = new HashMap<>();
        SubnetTree ipv4SubnetTree = new SubnetTree();
        SubnetTree ipv6SubnetTree = new SubnetTree();
        ipv4SubnetTree.updateTreeForIPv4SubnetInThirdNetwork();
        ipv6SubnetTree.updateTreeForIPv6SubnetInThirdNetwork();
        thirdNetworkProperties.put(IPv4, ipv4SubnetTree);
        thirdNetworkProperties.put(IPv6, ipv6SubnetTree);
    }

    private void createSubnets(IPSubnetFilterProperties subnetFilterProperties, int amountOfSubnetsToCreate, IPSubnetWizardProperties... ipSubnetWizardProperties) {
        IPSubnetWizardPage ipSubnetWizardPage;
        boolean isSubnetV4Case = subnetFilterProperties.equals(IPv4_SUBNET_FILTER_PROPERTIES_FOR_FIRST_NETWORK) || subnetFilterProperties.equals(IPv4_SUBNET_FILTER_PROPERTIES_FOR_SECOND_AND_THIRD_NETWORK);
        if (isSubnetV4Case)
            ipSubnetWizardPage = ipAddressManagementViewPage.createIPv4Subnet();
        else
            ipSubnetWizardPage = ipAddressManagementViewPage.createIPv6Subnet();
        ipSubnetWizardPage.ipSubnetWizardSelectStep(subnetFilterProperties, amountOfSubnetsToCreate);
        ipSubnetWizardPage.ipSubnetWizardPropertiesStep(ipSubnetWizardProperties);
        ipSubnetWizardPage.ipSubnetWizardSummaryStep();
        ipAddressManagementViewPage = new IPAddressManagementViewPage(driver);
    }

    private static class SubnetTree {
        private HashMap<String, String> hostAddressWithAssignmentsMap;
        private HashMap<String, String> hostAddressAssignmentIdentifierMap;
        private HashMap<String, String> hostAddressIdentifierMap;
        private String networkSubnet;
        private String blockSubnet;

        private List<String> hostAddressesList;
        private static final String IPv4_ADDRESS = "20.0.0.{0}";
        private static final String IPv6_ADDRESS = "::20:{0}";
        private static final String HIGHER_IPv4_NETWORK_MASK = "25";
        private static final String HIGHER_IPv6_NETWORK_MASK = "121";

        public void initializeTreeForIPv4SubnetInFirstNetwork() {
            hostAddressWithAssignmentsMap = new HashMap<>();
            hostAddressAssignmentIdentifierMap = new HashMap<>();
            hostAddressesList = new ArrayList<>();
            for (int i = 1; i <= AMOUNT_OF_HAA; i++) {
                String ipAddress = MessageFormat.format(IPv4_ADDRESS, i) + "/" + HIGHER_IPv4_NETWORK_MASK;
                String hostAddressAssignment = ipAddress + " [" + ROUTER_IDENTIFIER + "]";
                String hostAddressAssignmentIdentifier = ipAddress + " [" + FIRST_NETWORK_NAME + "]";
                hostAddressesList.add(ipAddress);
                hostAddressWithAssignmentsMap.put(ipAddress, hostAddressAssignment);
                hostAddressAssignmentIdentifierMap.put(ipAddress, hostAddressAssignmentIdentifier);
            }
            networkSubnet = MessageFormat.format(IPv4_ADDRESS, 0) + "/" + HIGHER_IPv4_NETWORK_MASK;
            blockSubnet = MessageFormat.format(IPv4_ADDRESS, 0) + "/" + IPv4_NETWORK_MASK_;
        }

        public void initializeTreeForIPv6SubnetInFirstNetwork() {
            hostAddressWithAssignmentsMap = new HashMap<>();
            hostAddressAssignmentIdentifierMap = new HashMap<>();
            hostAddressesList = new ArrayList<>();
            for (int i = 1; i <= AMOUNT_OF_HAA; i++) {
                String ipAddress = MessageFormat.format(IPv6_ADDRESS, i) + "/" + HIGHER_IPv6_NETWORK_MASK;
                String hostAddressAssignment = ipAddress + " [" + ROUTER_IDENTIFIER + "]";
                String hostAddressAssignmentIdentifier = ipAddress + " [" + FIRST_NETWORK_NAME + "]";
                hostAddressesList.add(ipAddress);
                hostAddressWithAssignmentsMap.put(ipAddress, hostAddressAssignment);
                hostAddressAssignmentIdentifierMap.put(ipAddress, hostAddressAssignmentIdentifier);
            }
            networkSubnet = MessageFormat.format(IPv6_ADDRESS, 0) + "/" + HIGHER_IPv6_NETWORK_MASK;
            blockSubnet = MessageFormat.format(IPv6_ADDRESS, 0) + "/" + IPv6_NETWORK_MASK_;
        }

        public void initializeTreeForIPv4SubnetInSecondNetwork() {
            blockSubnet = MessageFormat.format(IPv4_ADDRESS, 0) + "/" + LOWER_IPv4_NETWORK_MASK;
        }

        public void initializeTreeForIPv6SubnetInSecondNetwork() {
            blockSubnet = MessageFormat.format(IPv6_ADDRESS, 0) + "/" + LOWER_IPv6_NETWORK_MASK;
        }

        public void updateTreeForIPv4SubnetInSecondNetwork() {
            hostAddressWithAssignmentsMap = new HashMap<>();
            hostAddressAssignmentIdentifierMap = new HashMap<>();
            hostAddressesList = new ArrayList<>();
            for (int i = 1; i <= AMOUNT_OF_HAA; i++) {
                String ipAddress = MessageFormat.format(IPv4_ADDRESS, i) + "/" + HIGHER_IPv4_NETWORK_MASK;
                String hostAddressAssignment = ipAddress + " [" + ROUTER_IDENTIFIER + "]";
                String hostAddressAssignmentIdentifier = ipAddress + " [" + SECOND_NETWORK_NAME + "]";
                hostAddressesList.add(ipAddress);
                hostAddressWithAssignmentsMap.put(ipAddress, hostAddressAssignment);
                hostAddressAssignmentIdentifierMap.put(ipAddress, hostAddressAssignmentIdentifier);
            }
            networkSubnet = MessageFormat.format(IPv4_ADDRESS, 0) + "/" + HIGHER_IPv4_NETWORK_MASK;
        }

        public void updateTreeForIPv6SubnetInSecondNetwork() {
            hostAddressWithAssignmentsMap = new HashMap<>();
            hostAddressAssignmentIdentifierMap = new HashMap<>();
            hostAddressesList = new ArrayList<>();
            for (int i = 1; i <= AMOUNT_OF_HAA; i++) {
                String ipAddress = MessageFormat.format(IPv6_ADDRESS, i) + "/" + HIGHER_IPv6_NETWORK_MASK;
                String hostAddressAssignment = ipAddress + " [" + ROUTER_IDENTIFIER + "]";
                String hostAddressAssignmentIdentifier = ipAddress + " [" + SECOND_NETWORK_NAME + "]";
                hostAddressesList.add(ipAddress);
                hostAddressWithAssignmentsMap.put(ipAddress, hostAddressAssignment);
                hostAddressAssignmentIdentifierMap.put(ipAddress, hostAddressAssignmentIdentifier);
            }
            networkSubnet = MessageFormat.format(IPv6_ADDRESS, 0) + "/" + HIGHER_IPv6_NETWORK_MASK;
        }

        public void updateTreeForIPv4SubnetInThirdNetwork() {
            hostAddressIdentifierMap = new HashMap<>();
            hostAddressesList = new ArrayList<>();
            for (int i = 1; i <= AMOUNT_OF_HAA; i++) {
                String hostAddress = MessageFormat.format(IPv4_ADDRESS, i) + "/" + HIGHER_IPv4_NETWORK_MASK;
                String hostAddressAssignmentIdentifier = hostAddress + " [" + THIRD_NETWORK_NAME + "]";
                hostAddressesList.add(hostAddress);
                hostAddressIdentifierMap.put(hostAddress, hostAddressAssignmentIdentifier);
            }
            blockSubnet = MessageFormat.format(IPv4_ADDRESS, 0) + "/" + LOWER_IPv4_NETWORK_MASK;
            networkSubnet = MessageFormat.format(IPv4_ADDRESS, 0) + "/" + HIGHER_IPv4_NETWORK_MASK;
        }

        public void updateTreeForIPv6SubnetInThirdNetwork() {
            hostAddressIdentifierMap = new HashMap<>();
            hostAddressesList = new ArrayList<>();
            for (int i = 1; i <= AMOUNT_OF_HAA; i++) {
                String hostAddress = MessageFormat.format(IPv6_ADDRESS, i) + "/" + HIGHER_IPv6_NETWORK_MASK;
                String hostAddressAssignmentIdentifier = hostAddress + " [" + THIRD_NETWORK_NAME + "]";
                hostAddressesList.add(hostAddress);
                hostAddressIdentifierMap.put(hostAddress, hostAddressAssignmentIdentifier);
            }
            blockSubnet = MessageFormat.format(IPv6_ADDRESS, 0) + "/" + LOWER_IPv6_NETWORK_MASK;
            networkSubnet = MessageFormat.format(IPv6_ADDRESS, 0) + "/" + HIGHER_IPv6_NETWORK_MASK;
        }

        public String getIPHostAddressAssignment(int index) {
            String ipHostAddress = getIPHostAddress(index);
            return hostAddressWithAssignmentsMap.get(ipHostAddress);
        }

        public String getIPHostAddressAssignmentIdentifier(int index) {
            String ipHostAddress = getIPHostAddress(index);
            return hostAddressAssignmentIdentifierMap.get(ipHostAddress);
        }

        public String getIPHostAddressIdentifier(int index) {
            String ipHostAddress = getIPHostAddress(index);
            return hostAddressIdentifierMap.get(ipHostAddress);
        }

        public String getIPHostAddress(int index) {
            return hostAddressesList.get(index);
        }

    }

}

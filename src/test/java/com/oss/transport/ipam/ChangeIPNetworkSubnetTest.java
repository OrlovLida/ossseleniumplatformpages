package com.oss.transport.ipam;

import java.util.Arrays;
import java.util.HashMap;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.bpm.processinstances.ProcessOverviewPage;
import com.oss.pages.bpm.tasks.TasksPage;
import com.oss.pages.bpm.processinstances.creation.ProcessWizardPage;
import com.oss.pages.transport.ipam.IPAddressAssignmentWizardPage;
import com.oss.pages.transport.ipam.IPAddressManagementViewPage;
import com.oss.pages.transport.ipam.IPSubnetWizardPage;
import com.oss.pages.transport.ipam.helper.IPAddressAssignmentWizardProperties;
import com.oss.pages.transport.ipam.helper.IPSubnetFilterProperties;
import com.oss.utils.TestListener;

import io.qameta.allure.Description;

import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.HOST_ASSIGNMENT_PROPERTY_IDENTIFIER;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.NETWORK_PROPERTY_NAME;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.NEW_ADDRESS_MODE;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.PHYSICAL_DEVICE;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.SUBNET_PROPERTY_IDENTIFIER;

@Listeners({TestListener.class})
public class ChangeIPNetworkSubnetTest extends BaseTestCase {
    private static final String FALSE = "FALSE";
    private static final String TASK_NAME = "Correct data";
    private static IPAddressManagementViewPage ipAddressManagementViewPage;
    private static final String FIRST_NETWORK_NAME = "ChangeIPNetworkSubnet1";
    private static final String SECOND_NETWORK_NAME = "ChangeIPNetworkSubnet2";
    private static final String THIRD_NETWORK_NAME = "ChangeIPNetworkSubnet3";
    private static final String DESCRIPTION = "Subnet case";
    private static final String LOWER_IPv4_NETWORK_MASK = "23";
    private static final String IPv4_NETWORK_MASK = "24";
    private static final String HIGHER_IPv4_NETWORK_MASK = "25";
    private static final String LOWER_IPv6_NETWORK_MASK = "119";
    private static final String IPv6_NETWORK_MASK = "120";
    private static final String HIGHER_IPv6_NETWORK_MASK = "121";
    private static final String FIRST_IPv4_ADDRESS = "20.0.0.0";
    private static final String SECOND_IPv4_ADDRESS = "30.0.0.0";
    private static final String FIRST_FIXED_IPv4_ADDRESS = "20.000";
    private static final String SECOND_FIXED_IPv4_ADDRESS = "30000";
    private static final String[] FIRST_SUBNET_IPv4_HOST_ADDRESSES = {"20.0.0.1", "20.0.0.2"};
    private static final String[] SECOND_SUBNET_IPv4_HOST_ADDRESSES = {"30.0.0.1", "30.0.0.2"};
    private static final String[] FIRST_SUBNET_IPv4_FIXED_HOST_ADDRESSES = {"20.001", "20.002"};
    private static final String[] SECOND_SUBNET_IPv4_FIXED_HOST_ADDRESSES = {"30001", "30002"};
    private static final String[] FIRST_SUBNET_IPv6_HOST_ADDRESSES = {"::20:1", "::20:2"};
    private static final String[] SECOND_SUBNET_IPv6_HOST_ADDRESSES = {"::30:1", "::30:2"};
    private static final int FIRST_INDEX = 0;
    private static final int SECOND_INDEX = 1;
    private static final String FIRST_IPv6_ADDRESS = "::20:0";
    private static final String SECOND_IPv6_ADDRESS = "::30:0";
    private static final String BLOCK_SUBNET_TYPE = "Block";
    private static final String NETWORK_SUBNET_TYPE = "Network";
    private static final String FIRST_IPv6_IDENTIFIER = "firstIPv6Identifier";
    private static final String FIRST_IPv4_IDENTIFIER = "firstIPv4Identifier";
    private static final String SECOND_IPv6_IDENTIFIER = "secondIPv6Identifier";
    private static final String SECOND_IPv4_IDENTIFIER = "secondIPv4Identifier";
    private static final String EQUAL = "=";
    private static final String ROUTER_ID = "1830300";
    private static final String ROUTER_IDENTIFIER = "MKTEST-Router-1";
    private static String firstDCPProcessCode;
    private static String secondDCPProcessCode;
    private static HashMap<String, SubnetTree> firstNetworkProperties;
    private static HashMap<String, SubnetTree> secondNetworkProperties;
    private static HashMap<String, SubnetTree> thirdNetworkProperties;

    @BeforeClass
    public void prepareData() {
        initializeFirstNetworkProperties();
        initializeSecondNetworkProperties();
    }

    @Test(priority = 1)
    @Description("Creating IP Networks")
    public void createIPNetworks() {
        ipAddressManagementViewPage = IPAddressManagementViewPage.goToIPAddressManagementPage(driver, BASIC_URL);
        createNetworkAndCheckTheirPresence(FIRST_NETWORK_NAME);
        createNetworkAndCheckTheirPresence(SECOND_NETWORK_NAME);
        createNetworkAndCheckTheirPresence(THIRD_NETWORK_NAME);
    }

    @Test(priority = 2)
    @Description("Creating first network subnets")
    public void createFirstNetworkSubnets() {
        IPSubnetFilterProperties firstIPv4SubnetFilterProperties = new IPSubnetFilterProperties(FIRST_FIXED_IPv4_ADDRESS, FIRST_FIXED_IPv4_ADDRESS, EQUAL, IPv4_NETWORK_MASK);
        IPSubnetFilterProperties secondIPv4SubnetFilterProperties = new IPSubnetFilterProperties(SECOND_FIXED_IPv4_ADDRESS, SECOND_FIXED_IPv4_ADDRESS, EQUAL, IPv4_NETWORK_MASK);
        IPSubnetFilterProperties firstIPv6SubnetFilterProperties = new IPSubnetFilterProperties(FIRST_IPv6_ADDRESS, FIRST_IPv6_ADDRESS, EQUAL, IPv6_NETWORK_MASK);
        IPSubnetFilterProperties secondIPv6SubnetFilterProperties = new IPSubnetFilterProperties(SECOND_IPv6_ADDRESS, SECOND_IPv6_ADDRESS, EQUAL, IPv6_NETWORK_MASK);
        ipAddressManagementViewPage.selectTreeRow(FIRST_NETWORK_NAME);
        createSubnets(FIRST_IPv4_IDENTIFIER, NETWORK_SUBNET_TYPE, firstIPv4SubnetFilterProperties, secondIPv4SubnetFilterProperties);
        createSubnets(FIRST_IPv6_IDENTIFIER, NETWORK_SUBNET_TYPE, firstIPv6SubnetFilterProperties, secondIPv6SubnetFilterProperties);
        unselectTreeRow(FIRST_NETWORK_NAME);
    }

    @Test(priority = 3)
    @Description("Creating Host Address Assignments for first network")
    public void createHostAddressAssignmentsForFirstNetwork() {
        ipAddressManagementViewPage.expandTreeRowContains(FIRST_NETWORK_NAME);
        createIPHostAddressAssignments(firstNetworkProperties);
    }

    @Test(priority = 4)
    @Description("Creating second network subnets")
    public void createSecondNetworkSubnets() {
        IPSubnetFilterProperties firstIPv4SubnetFilterProperties = new IPSubnetFilterProperties(FIRST_FIXED_IPv4_ADDRESS, FIRST_FIXED_IPv4_ADDRESS, EQUAL, HIGHER_IPv4_NETWORK_MASK);
        IPSubnetFilterProperties secondIPv4SubnetFilterProperties = new IPSubnetFilterProperties(SECOND_FIXED_IPv4_ADDRESS, SECOND_FIXED_IPv4_ADDRESS, EQUAL, HIGHER_IPv4_NETWORK_MASK);
        IPSubnetFilterProperties firstIPv6SubnetFilterProperties = new IPSubnetFilterProperties(FIRST_IPv6_ADDRESS, FIRST_IPv6_ADDRESS, EQUAL, HIGHER_IPv6_NETWORK_MASK);
        IPSubnetFilterProperties secondIPv6SubnetFilterProperties = new IPSubnetFilterProperties(SECOND_IPv6_ADDRESS, SECOND_IPv6_ADDRESS, EQUAL, HIGHER_IPv6_NETWORK_MASK);
        ipAddressManagementViewPage.selectTreeRow(SECOND_NETWORK_NAME);
        createSubnets(FIRST_IPv4_IDENTIFIER, NETWORK_SUBNET_TYPE, firstIPv4SubnetFilterProperties, secondIPv4SubnetFilterProperties);
        createSubnets(FIRST_IPv6_IDENTIFIER, NETWORK_SUBNET_TYPE, firstIPv6SubnetFilterProperties, secondIPv6SubnetFilterProperties);
        unselectTreeRow(SECOND_NETWORK_NAME);
    }

    @Test(priority = 5)
    @Description("Creating Host Address Assignments for Second Network")
    public void createHostAddressAssignmentsForSecondNetwork() {
        ipAddressManagementViewPage.expandTreeRowContains(SECOND_NETWORK_NAME);
        createIPHostAddressAssignments(secondNetworkProperties);
    }

    @Test(priority = 6)
    @Description("Creating Third Network Subnets")
    public void createThirdNetworkSubnets() {
        IPSubnetFilterProperties firstIPv4SubnetFilterProperties = new IPSubnetFilterProperties(FIRST_FIXED_IPv4_ADDRESS, FIRST_FIXED_IPv4_ADDRESS, EQUAL, LOWER_IPv4_NETWORK_MASK);
        IPSubnetFilterProperties secondIPv4SubnetFilterProperties = new IPSubnetFilterProperties(SECOND_FIXED_IPv4_ADDRESS, SECOND_FIXED_IPv4_ADDRESS, EQUAL, LOWER_IPv4_NETWORK_MASK);
        IPSubnetFilterProperties firstIPv6SubnetFilterProperties = new IPSubnetFilterProperties(FIRST_IPv6_ADDRESS, FIRST_IPv6_ADDRESS, EQUAL, LOWER_IPv6_NETWORK_MASK);
        IPSubnetFilterProperties secondIPv6SubnetFilterProperties = new IPSubnetFilterProperties(SECOND_IPv6_ADDRESS, SECOND_IPv6_ADDRESS, EQUAL, LOWER_IPv6_NETWORK_MASK);
        ipAddressManagementViewPage.selectTreeRow(THIRD_NETWORK_NAME);
        createSubnets(FIRST_IPv4_IDENTIFIER, BLOCK_SUBNET_TYPE, firstIPv4SubnetFilterProperties, secondIPv4SubnetFilterProperties);
        createSubnets(FIRST_IPv6_IDENTIFIER, BLOCK_SUBNET_TYPE, firstIPv6SubnetFilterProperties, secondIPv6SubnetFilterProperties);
        unselectTreeRow(THIRD_NETWORK_NAME);
    }

    @Test(priority = 7)
    @Description("Start first DCP Process")
    public void startFirstDCPProcess() {
        firstDCPProcessCode = createDCPProcess();
    }

    @Test(priority = 8)
    @Description("Move IPv4 Subnets to second network")
    public void moveIPv4SubnetsToSecondNetwork() {
        ipAddressManagementViewPage = IPAddressManagementViewPage.goToIPAddressManagementPage(driver, BASIC_URL);
        ipAddressManagementViewPage.expandTreeRow(FIRST_NETWORK_NAME);
        String firstSubnetToMove = firstNetworkProperties.get(FIRST_IPv4_IDENTIFIER).networkSubnetRowName;
        String secondSubnetToMove = firstNetworkProperties.get(SECOND_IPv4_IDENTIFIER).networkSubnetRowName;
        ipAddressManagementViewPage.changeIPNetworkForIPv4NetworkSubnet(SECOND_NETWORK_NAME, firstSubnetToMove, secondSubnetToMove);
    }

    @Test(priority = 9)
    @Description("Move IPv6 Subnets to second network")
    public void moveIPv6SubnetsToSecondNetwork() {
        ipAddressManagementViewPage = IPAddressManagementViewPage.goToIPAddressManagementPage(driver, BASIC_URL);
        ipAddressManagementViewPage.expandTreeRow(FIRST_NETWORK_NAME);
        String firstSubnetToMove = firstNetworkProperties.get(FIRST_IPv6_IDENTIFIER).networkSubnetRowName;
        String secondSubnetToMove = firstNetworkProperties.get(SECOND_IPv6_IDENTIFIER).networkSubnetRowName;
        ipAddressManagementViewPage.changeIPNetworkForIPv6NetworkSubnet(SECOND_NETWORK_NAME, firstSubnetToMove, secondSubnetToMove);
        updateSecondNetworkProperties();
    }

    @Test(priority = 10)
    @Description("Finish first DCP Process")
    public void finishFirstDCPProcess() {
        finishDCPProcess(firstDCPProcessCode);
    }

    @Test(priority = 11)
    @Description("Check correctness of changes")
    public void checkCorrectnessOfSecondNetworkChanges() {
        ipAddressManagementViewPage = IPAddressManagementViewPage.goToIPAddressManagementPage(driver, BASIC_URL);
        ipAddressManagementViewPage.expandTreeRow(SECOND_NETWORK_NAME);
        checkCorrectnessOfChanges(secondNetworkProperties);
    }

    @Test(priority = 12)
    @Description("Start second DCP process")
    public void startSecondDCPProcess() {
        secondDCPProcessCode = createDCPProcess();
    }

    @Test(priority = 13)
    @Description("Move IPv4 subnets to third network")
    public void moveIPv4SubnetsToThirdNetwork() {
        ipAddressManagementViewPage = IPAddressManagementViewPage.goToIPAddressManagementPage(driver, BASIC_URL);
        ipAddressManagementViewPage.expandTreeRow(SECOND_NETWORK_NAME);
        String firstSubnetToMove = secondNetworkProperties.get(FIRST_IPv4_IDENTIFIER).networkSubnetRowName;
        String secondSubnetToMove = secondNetworkProperties.get(SECOND_IPv4_IDENTIFIER).networkSubnetRowName;
        ipAddressManagementViewPage.changeIPNetworkForIPv4NetworkSubnet(THIRD_NETWORK_NAME, firstSubnetToMove, secondSubnetToMove);
    }

    @Test(priority = 14)
    @Description("Move IPv6 subnets to third network")
    public void moveIPv6SubnetsToThirdNetwork() {
        ipAddressManagementViewPage.expandTreeRow(SECOND_NETWORK_NAME);
        String firstSubnetToMove = secondNetworkProperties.get(FIRST_IPv6_IDENTIFIER).networkSubnetRowName;
        String secondSubnetToMove = secondNetworkProperties.get(SECOND_IPv6_IDENTIFIER).networkSubnetRowName;
        ipAddressManagementViewPage.changeIPNetworkForIPv6NetworkSubnet(THIRD_NETWORK_NAME, firstSubnetToMove, secondSubnetToMove);
        initializeThirdNetworkProperties();
    }

    @Test(priority = 15)
    @Description("Finish second DCP process")
    public void finishSecondDCPProcess() {
        finishDCPProcess(secondDCPProcessCode);
    }

    @Test(priority = 16)
    @Description("Check correctness of changes")
    public void checkCorrectnessOfThirdNetworkChanges() {
        ipAddressManagementViewPage = IPAddressManagementViewPage.goToIPAddressManagementPage(driver, BASIC_URL);
        ipAddressManagementViewPage.expandTreeRow(THIRD_NETWORK_NAME);
        ipAddressManagementViewPage.expandTreeRowContains(thirdNetworkProperties.get(FIRST_IPv4_IDENTIFIER).blockSubnetRowName);
        ipAddressManagementViewPage.expandTreeRowContains(thirdNetworkProperties.get(SECOND_IPv4_IDENTIFIER).blockSubnetRowName);
        ipAddressManagementViewPage.expandTreeRowContains(thirdNetworkProperties.get(FIRST_IPv6_IDENTIFIER).blockSubnetRowName);
        ipAddressManagementViewPage.expandTreeRowContains(thirdNetworkProperties.get(SECOND_IPv6_IDENTIFIER).blockSubnetRowName);
        checkCorrectnessOfChanges(thirdNetworkProperties);
    }

    @Test(priority = 17)
    @Description("Delete Test Data")
    public void deleteTestData() {
        ipAddressManagementViewPage = IPAddressManagementViewPage.refreshIPAddressManagementViewPage(driver);
        deleteNetworkWithSubnets(FIRST_NETWORK_NAME, firstNetworkProperties);
        deleteNetworkWithSubnets(SECOND_NETWORK_NAME, secondNetworkProperties);
        deleteNetworkWithSubnets(THIRD_NETWORK_NAME, thirdNetworkProperties);
    }

    private void initializeFirstNetworkProperties() {
        firstNetworkProperties = new HashMap<>();
        SubnetTree firstIPv4SubnetTree = initializeSubnetTree(FIRST_NETWORK_NAME, FIRST_IPv4_ADDRESS, IPv4_NETWORK_MASK, FIRST_SUBNET_IPv4_HOST_ADDRESSES[FIRST_INDEX]);
        SubnetTree secondIPv4SubnetTree = initializeSubnetTree(FIRST_NETWORK_NAME, SECOND_IPv4_ADDRESS, IPv4_NETWORK_MASK, SECOND_SUBNET_IPv4_HOST_ADDRESSES[FIRST_INDEX]);
        SubnetTree firstIPv6SubnetTree = initializeSubnetTree(FIRST_NETWORK_NAME, FIRST_IPv6_ADDRESS, IPv6_NETWORK_MASK, FIRST_SUBNET_IPv6_HOST_ADDRESSES[FIRST_INDEX]);
        SubnetTree secondIPv6SubnetTree = initializeSubnetTree(FIRST_NETWORK_NAME, SECOND_IPv6_ADDRESS, IPv6_NETWORK_MASK, SECOND_SUBNET_IPv6_HOST_ADDRESSES[FIRST_INDEX]);
        firstIPv4SubnetTree.setFixedHostAddressesMap(FIRST_SUBNET_IPv4_FIXED_HOST_ADDRESSES[FIRST_INDEX]);
        secondIPv4SubnetTree.setFixedHostAddressesMap(SECOND_SUBNET_IPv4_FIXED_HOST_ADDRESSES[FIRST_INDEX]);
        firstIPv6SubnetTree.setFixedHostAddressesMap(FIRST_SUBNET_IPv6_HOST_ADDRESSES[FIRST_INDEX]);
        secondIPv6SubnetTree.setFixedHostAddressesMap(SECOND_SUBNET_IPv6_HOST_ADDRESSES[FIRST_INDEX]);
        firstNetworkProperties.put(FIRST_IPv4_IDENTIFIER, firstIPv4SubnetTree);
        firstNetworkProperties.put(SECOND_IPv4_IDENTIFIER, secondIPv4SubnetTree);
        firstNetworkProperties.put(FIRST_IPv6_IDENTIFIER, firstIPv6SubnetTree);
        firstNetworkProperties.put(SECOND_IPv6_IDENTIFIER, secondIPv6SubnetTree);
    }

    private void initializeSecondNetworkProperties() {
        secondNetworkProperties = new HashMap<>();
        SubnetTree firstIPv4SubnetTree = initializeSubnetTree(SECOND_NETWORK_NAME, FIRST_IPv4_ADDRESS, HIGHER_IPv4_NETWORK_MASK, FIRST_SUBNET_IPv4_HOST_ADDRESSES[SECOND_INDEX]);
        SubnetTree secondIPv4SubnetTree = initializeSubnetTree(SECOND_NETWORK_NAME, SECOND_IPv4_ADDRESS, HIGHER_IPv4_NETWORK_MASK, SECOND_SUBNET_IPv4_HOST_ADDRESSES[SECOND_INDEX]);
        SubnetTree firstIPv6SubnetTree = initializeSubnetTree(SECOND_NETWORK_NAME, FIRST_IPv6_ADDRESS, HIGHER_IPv6_NETWORK_MASK, FIRST_SUBNET_IPv6_HOST_ADDRESSES[SECOND_INDEX]);
        SubnetTree secondIPv6SubnetTree = initializeSubnetTree(SECOND_NETWORK_NAME, SECOND_IPv6_ADDRESS, HIGHER_IPv6_NETWORK_MASK, SECOND_SUBNET_IPv6_HOST_ADDRESSES[SECOND_INDEX]);
        firstIPv4SubnetTree.setFixedHostAddressesMap(FIRST_SUBNET_IPv4_FIXED_HOST_ADDRESSES[SECOND_INDEX]);
        secondIPv4SubnetTree.setFixedHostAddressesMap(SECOND_SUBNET_IPv4_FIXED_HOST_ADDRESSES[SECOND_INDEX]);
        firstIPv6SubnetTree.setFixedHostAddressesMap(FIRST_SUBNET_IPv6_HOST_ADDRESSES[SECOND_INDEX]);
        secondIPv6SubnetTree.setFixedHostAddressesMap(SECOND_SUBNET_IPv6_HOST_ADDRESSES[SECOND_INDEX]);
        secondNetworkProperties.put(FIRST_IPv4_IDENTIFIER, firstIPv4SubnetTree);
        secondNetworkProperties.put(SECOND_IPv4_IDENTIFIER, secondIPv4SubnetTree);
        secondNetworkProperties.put(FIRST_IPv6_IDENTIFIER, firstIPv6SubnetTree);
        secondNetworkProperties.put(SECOND_IPv6_IDENTIFIER, secondIPv6SubnetTree);
    }

    private void initializeThirdNetworkProperties() {
        thirdNetworkProperties = new HashMap<>();
        SubnetTree firstIPv4SubnetTree = initializeSubnetTree(THIRD_NETWORK_NAME, FIRST_IPv4_ADDRESS, IPv4_NETWORK_MASK, FIRST_SUBNET_IPv4_HOST_ADDRESSES);
        SubnetTree secondIPv4SubnetTree = initializeSubnetTree(THIRD_NETWORK_NAME, SECOND_IPv4_ADDRESS, IPv4_NETWORK_MASK, SECOND_SUBNET_IPv4_HOST_ADDRESSES);
        SubnetTree firstIPv6SubnetTree = initializeSubnetTree(THIRD_NETWORK_NAME, FIRST_IPv6_ADDRESS, IPv6_NETWORK_MASK, FIRST_SUBNET_IPv6_HOST_ADDRESSES);
        SubnetTree secondIPv6SubnetTree = initializeSubnetTree(THIRD_NETWORK_NAME, SECOND_IPv6_ADDRESS, IPv6_NETWORK_MASK, SECOND_SUBNET_IPv6_HOST_ADDRESSES);
        firstIPv4SubnetTree.setFixedHostAddressesMap(FIRST_SUBNET_IPv4_FIXED_HOST_ADDRESSES);
        firstIPv4SubnetTree.setBlockSubnetRowName(FIRST_IPv4_ADDRESS, LOWER_IPv4_NETWORK_MASK);
        secondIPv4SubnetTree.setFixedHostAddressesMap(SECOND_SUBNET_IPv4_FIXED_HOST_ADDRESSES);
        secondIPv4SubnetTree.setBlockSubnetRowName(SECOND_IPv4_ADDRESS, LOWER_IPv4_NETWORK_MASK);
        firstIPv6SubnetTree.setFixedHostAddressesMap(FIRST_SUBNET_IPv6_HOST_ADDRESSES);
        firstIPv6SubnetTree.setBlockSubnetRowName(FIRST_IPv6_ADDRESS, LOWER_IPv6_NETWORK_MASK);
        secondIPv6SubnetTree.setFixedHostAddressesMap(SECOND_SUBNET_IPv6_HOST_ADDRESSES);
        secondIPv6SubnetTree.setBlockSubnetRowName(SECOND_IPv6_ADDRESS, LOWER_IPv6_NETWORK_MASK);
        thirdNetworkProperties.put(FIRST_IPv4_IDENTIFIER, firstIPv4SubnetTree);
        thirdNetworkProperties.put(SECOND_IPv4_IDENTIFIER, secondIPv4SubnetTree);
        thirdNetworkProperties.put(FIRST_IPv6_IDENTIFIER, firstIPv6SubnetTree);
        thirdNetworkProperties.put(SECOND_IPv6_IDENTIFIER, secondIPv6SubnetTree);
    }

    private void createSubnets(String ipVersion, String subnetType, IPSubnetFilterProperties... subnetFilterProperties) {
        Arrays.stream(subnetFilterProperties).forEach(property -> {
            IPSubnetWizardPage ipSubnetWizardPage;
            if (ipVersion.equals(FIRST_IPv4_IDENTIFIER) || ipVersion.equals(SECOND_IPv4_IDENTIFIER))
                ipSubnetWizardPage = ipAddressManagementViewPage.createIPv4Subnet();
            else
                ipSubnetWizardPage = ipAddressManagementViewPage.createIPv6Subnet();
            ipSubnetWizardPage.ipSubnetWizardSelectStep(property);
            ipSubnetWizardPage.ipSubnetWizardPropertiesStep(subnetType);
            ipSubnetWizardPage.ipSubnetWizardSummaryStep();
            ipAddressManagementViewPage = new IPAddressManagementViewPage(driver);
        });
    }

    private void createIPHostAddressAssignments(HashMap<String, SubnetTree> networkProperties) {
        networkProperties.forEach((ipVersion, subnetTree) -> {
            String firstIPAddress = subnetTree.getFixedHostAddress(FIRST_INDEX);
            IPAddressAssignmentWizardProperties firstAssignmentProperties = prepareAssignmentWizardProperties(firstIPAddress);
            assignIPHostAddress(firstAssignmentProperties, ipVersion, subnetTree.networkSubnetRowName);
            String assignmentRowName = subnetTree.getHostAddressAssignment(FIRST_INDEX);
            String assignmentIdentifier = subnetTree.getAssignmentIdentifier(FIRST_INDEX);
            selectCheckUnselect(assignmentRowName, assignmentIdentifier, HOST_ASSIGNMENT_PROPERTY_IDENTIFIER);
        });
    }

    private void assignIPHostAddress(IPAddressAssignmentWizardProperties assignmentWizardProperties, String ipSubnetIdentifier, String subnetAddress) {
        IPAddressAssignmentWizardPage ipAddressAssignmentWizardPage;
        if (ipSubnetIdentifier.equals(FIRST_IPv4_IDENTIFIER) || ipSubnetIdentifier.equals(SECOND_IPv4_IDENTIFIER)) {
            ipAddressAssignmentWizardPage = ipAddressManagementViewPage.assignIPv4HostAddressFromSubnetContext(subnetAddress);
        } else {
            ipAddressAssignmentWizardPage = ipAddressManagementViewPage.assignIPv6HostAddressFromSubnetContextAlfa(subnetAddress);
        }
        ipAddressAssignmentWizardPage.assignIPAddress(assignmentWizardProperties);
        ipAddressManagementViewPage = new IPAddressManagementViewPage(driver);
    }

    private String createDCPProcess() {
        ProcessOverviewPage.goToProcessOverviewPage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        ProcessWizardPage processWizardPage = new ProcessWizardPage(driver);
        String dCPProcessCode = processWizardPage.createSimpleDCPV2();
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        tasksPage.startTask(dCPProcessCode, TasksPage.CORRECT_DATA_TASK);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        return dCPProcessCode;
    }

    private void finishDCPProcess(String dcpProcessCode) {
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.completeTask(dcpProcessCode, TASK_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    private SubnetTree initializeSubnetTree(String networkName, String ipAddress, String networkMask, String... hostAddresses) {
        SubnetTree subnetTree = new SubnetTree();
        subnetTree.setNetworkSubnetRowNameAndIdentifier(networkName, ipAddress, networkMask);
        subnetTree.setHostAddressesMap(hostAddresses);
        subnetTree.setAssignmentsMap(networkName, networkMask);
        return subnetTree;
    }

    private void updateSecondNetworkProperties() {
        secondNetworkProperties.clear();
        SubnetTree firstIPv4SubnetTree = initializeSubnetTree(SECOND_NETWORK_NAME, FIRST_IPv4_ADDRESS, IPv4_NETWORK_MASK, FIRST_SUBNET_IPv4_HOST_ADDRESSES);
        SubnetTree secondIPv4SubnetTree = initializeSubnetTree(SECOND_NETWORK_NAME, SECOND_IPv4_ADDRESS, IPv4_NETWORK_MASK, SECOND_SUBNET_IPv4_HOST_ADDRESSES);
        SubnetTree firstIPv6SubnetTree = initializeSubnetTree(SECOND_NETWORK_NAME, FIRST_IPv6_ADDRESS, IPv6_NETWORK_MASK, FIRST_SUBNET_IPv6_HOST_ADDRESSES);
        SubnetTree secondIPv6SubnetTree = initializeSubnetTree(SECOND_NETWORK_NAME, SECOND_IPv6_ADDRESS, IPv6_NETWORK_MASK, SECOND_SUBNET_IPv6_HOST_ADDRESSES);
        firstIPv4SubnetTree.setFixedHostAddressesMap(FIRST_SUBNET_IPv4_FIXED_HOST_ADDRESSES);
        secondIPv4SubnetTree.setFixedHostAddressesMap(SECOND_SUBNET_IPv4_FIXED_HOST_ADDRESSES);
        firstIPv6SubnetTree.setFixedHostAddressesMap(FIRST_SUBNET_IPv6_HOST_ADDRESSES);
        secondIPv6SubnetTree.setFixedHostAddressesMap(SECOND_SUBNET_IPv6_HOST_ADDRESSES);
        secondNetworkProperties.put(FIRST_IPv4_IDENTIFIER, firstIPv4SubnetTree);
        secondNetworkProperties.put(SECOND_IPv4_IDENTIFIER, secondIPv4SubnetTree);
        secondNetworkProperties.put(FIRST_IPv6_IDENTIFIER, firstIPv6SubnetTree);
        secondNetworkProperties.put(SECOND_IPv6_IDENTIFIER, secondIPv6SubnetTree);
    }

    private void createNetworkAndCheckTheirPresence(String networkName) {
        ipAddressManagementViewPage.createIPNetwork(networkName, DESCRIPTION);
        selectCheckUnselect(networkName, networkName, NETWORK_PROPERTY_NAME);

    }

    private void checkCorrectnessOfChanges(HashMap<String, SubnetTree> networkProperties) {
        networkProperties.forEach((subnetIdentifier, subnetTree) -> {
            ipAddressManagementViewPage.expandTreeRowContains(subnetTree.networkSubnetRowName);
            selectCheckUnselect(subnetTree.networkSubnetRowName, subnetTree.networkSubnetIdentifier, SUBNET_PROPERTY_IDENTIFIER);
            ipAddressManagementViewPage.expandTreeRowContains(subnetTree.getHostAddress(FIRST_INDEX));
            selectCheckUnselect(subnetTree.getHostAddressAssignment(FIRST_INDEX), subnetTree.getAssignmentIdentifier(FIRST_INDEX), HOST_ASSIGNMENT_PROPERTY_IDENTIFIER);
            ipAddressManagementViewPage.expandTreeRowContains(subnetTree.getHostAddress(SECOND_INDEX));
            selectCheckUnselect(subnetTree.getHostAddressAssignment(SECOND_INDEX), subnetTree.getAssignmentIdentifier(SECOND_INDEX), HOST_ASSIGNMENT_PROPERTY_IDENTIFIER);
        });
    }

    private void selectCheckUnselect(String objectToClick, String componentToCheck, String propertyName) {
        ipAddressManagementViewPage.selectTreeRowContains(objectToClick);
        Assert.assertEquals(ipAddressManagementViewPage.getPropertyValue(propertyName), componentToCheck);
        ipAddressManagementViewPage.unselectTreeRow(objectToClick);
    }

    private void deleteNetworkWithSubnets(String networkName, HashMap<String, SubnetTree> networkProperties) {
        networkProperties.forEach((subnetIdentifier, subnetTree) -> {
            if (subnetIdentifier.equals(FIRST_IPv4_IDENTIFIER) || subnetIdentifier.equals(SECOND_IPv4_IDENTIFIER))
                deleteIPv4Subnets(networkName, subnetTree);
            else
                deleteIPv6Subnets(networkName, subnetTree);
        });
        ipAddressManagementViewPage.deleteIPNetwork(networkName);
    }

    private void deleteIPv4Subnets(String networkName, SubnetTree subnetTree) {
        if (subnetTree.blockSubnetRowName == null) {
            ipAddressManagementViewPage.expandTreeRow(networkName);
            ipAddressManagementViewPage.deleteIPv4SubnetTypeOfNetwork(subnetTree.networkSubnetRowName);
            unselectTreeRow(networkName);
        } else {
            ipAddressManagementViewPage.expandTreeRow(networkName);
            ipAddressManagementViewPage.expandTreeRowContains(subnetTree.blockSubnetRowName);
            ipAddressManagementViewPage.deleteIPv4SubnetTypeOfNetwork(subnetTree.networkSubnetRowName);
            ipAddressManagementViewPage.deleteIPv4SubnetTypeOfBlock(subnetTree.blockSubnetRowName);
            unselectTreeRow(networkName);
        }
    }

    private void deleteIPv6Subnets(String networkName, SubnetTree subnetTree) {
        if (subnetTree.blockSubnetRowName == null) {
            ipAddressManagementViewPage.expandTreeRow(networkName);
            ipAddressManagementViewPage.deleteIPv6SubnetTypeOfNetwork(subnetTree.networkSubnetRowName);
            unselectTreeRow(networkName);
        } else {
            ipAddressManagementViewPage.expandTreeRow(networkName);
            ipAddressManagementViewPage.expandTreeRowContains(subnetTree.blockSubnetRowName);
            ipAddressManagementViewPage.deleteIPv6SubnetTypeOfNetwork(subnetTree.networkSubnetRowName);
            ipAddressManagementViewPage.deleteIPv6SubnetTypeOfBlock(subnetTree.blockSubnetRowName);
            unselectTreeRow(networkName);
        }
    }

    private void unselectTreeRow(String rowName) {
        ipAddressManagementViewPage.selectTreeRowContains(rowName);
        ipAddressManagementViewPage.unselectTreeRow(rowName);
    }

    private IPAddressAssignmentWizardProperties prepareAssignmentWizardProperties(String ipAddress) {
        return IPAddressAssignmentWizardProperties.builder()
                .wizardMode(NEW_ADDRESS_MODE).isInNAT(FALSE).isPrimary(FALSE)
                .assignmentType(PHYSICAL_DEVICE).assignmentName(ROUTER_ID)
                .address(ipAddress)
                .build();
    }

    private static class SubnetTree {
        private String networkSubnetRowName;
        private String networkSubnetIdentifier;
        private String blockSubnetRowName;
        private HashMap<Integer, String> hostAddressesMap;
        private HashMap<Integer, String> fixedHostAddressesMap;
        private HashMap<String, String> hostAddressesWithHostAssignmentsMap;
        private HashMap<String, String> hostAddressesWithHostAssignmentsIdentifierMap;

        public void setNetworkSubnetRowNameAndIdentifier(String networkName, String ipAddress, String networkMask) {
            networkSubnetRowName = ipAddress + "/" + networkMask;
            networkSubnetIdentifier = networkSubnetRowName + " [" + networkName + "]";
        }

        public void setBlockSubnetRowName(String ipAddress, String networkMask) {
            blockSubnetRowName = ipAddress + "/" + networkMask;
        }

        public void setHostAddressesMap(String... hostAddresses) {
            hostAddressesMap = new HashMap<>();
            for (int index = 0; index < hostAddresses.length; index++)
                hostAddressesMap.put(index, hostAddresses[index]);
        }

        public void setFixedHostAddressesMap(String... fixedHostAddresses) {
            fixedHostAddressesMap = new HashMap<>();
            for (int index = 0; index < fixedHostAddresses.length; index++)
                fixedHostAddressesMap.put(index, fixedHostAddresses[index]);
        }

        public void setAssignmentsMap(String networkName, String networkMask) {
            hostAddressesWithHostAssignmentsMap = new HashMap<>();
            hostAddressesWithHostAssignmentsIdentifierMap = new HashMap<>();
            hostAddressesMap.forEach((index, address) -> {
                String hostAssignmentRowName = address + "/" + networkMask + " [" + ROUTER_IDENTIFIER + "]";
                String hostAssignmentIdentifier = address + "/" + networkMask + " [" + networkName + "]";
                hostAddressesWithHostAssignmentsIdentifierMap.put(address, hostAssignmentIdentifier);
                hostAddressesWithHostAssignmentsMap.put(address, hostAssignmentRowName);
            });
        }

        public String getAssignmentIdentifier(int hostAddressIndex) {
            String hostAddress = getHostAddress(hostAddressIndex);
            return hostAddressesWithHostAssignmentsIdentifierMap.get(hostAddress);
        }

        public String getFixedHostAddress(int hostAddressIndex) {
            return fixedHostAddressesMap.get(hostAddressIndex);
        }

        public String getHostAddress(int hostAddressIndex) {
            return hostAddressesMap.get(hostAddressIndex);
        }

        public String getHostAddressAssignment(int hostAddressIndex) {
            String hostAddress = getHostAddress(hostAddressIndex);
            return hostAddressesWithHostAssignmentsMap.get(hostAddress);
        }

    }

}

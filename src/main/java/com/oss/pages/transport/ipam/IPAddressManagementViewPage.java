package com.oss.pages.transport.ipam;

import java.util.Arrays;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.alerts.SystemMessageInterface;
import com.oss.framework.components.contextactions.OldActionsContainer;
import com.oss.framework.components.prompts.ConfirmationBox;
import com.oss.framework.components.prompts.ConfirmationBoxInterface;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.propertypanel.OldPropertyPanel;
import com.oss.framework.widgets.propertypanel.PropertyPanelInterface;
import com.oss.framework.widgets.tree.TreeWidget;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;
import com.oss.pages.transport.ipam.helper.IPAddressAssignmentWizardProperties;

import io.qameta.allure.Step;

import static com.oss.framework.components.alerts.SystemMessageContainer.MessageType.SUCCESS;
import static com.oss.framework.components.inputs.Input.ComponentType.COMBOBOX;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.ASSIGN_IPV4_ADDRESS_ACTION_FROM_SUBNET_CONTEXT;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.ASSIGN_IPV4_SUBNET_ACTION;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.ASSIGN_IPV6_ADDRESS_ACTION_FROM_SUBNET_CONTEXT;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.ASSIGN_IPV6_ADDRESS_ACTION_FROM_SUBNET_CONTEXT_ALFA;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.ASSIGN_IPV6_SUBNET_ACTION;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.ASSIGN_LOOPBACK_IPV4_ADDRESS_ACTION_FROM_SUBNET_CONTEXT;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.ASSIGN_LOOPBACK_IPV6_ADDRESS_ACTION_FROM_HOST_CONTEXT;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.ASSIGN_OPERATION_ACTION;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.ASSIGN_OPERATION_FOR_IPV4_SUBNET_BLOCK_WITHOUT_ASSIGNMENT;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.ASSIGN_OPERATION_FOR_IPV4_SUBNET_BLOCK_WITH_ASSIGNMENT;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.ASSIGN_OPERATION_FOR_IPV4_SUBNET_NETWORK_WITHOUT_ASSIGNMENT;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.ASSIGN_OPERATION_FOR_IPV6_HOST_ADDRESS;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.ASSIGN_OPERATION_FOR_IPV6_SUBNET_BLOCK_WITHOUT_ASSIGNMENT;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.ASSIGN_OPERATION_FOR_IPV6_SUBNET_BLOCK_WITH_ASSIGNMENT;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.ASSIGN_OPERATION_FOR_IPV6_SUBNET_NETWORK_WITHOUT_ASSIGNMENT;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.CHANGE_IPV4_HOST_ADDRESS_MASK_ACTION;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.CHANGE_IPV4_SUBNET_TYPE_TO_BLOCK_ACTION;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.CHANGE_IPV6_HOST_ADDRESS_MASK_ACTION;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.CHANGE_IPV6_SUBNET_TYPE_TO_BLOCK_ACTION;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.CHANGE_MASK_WIZARD_DATA_ATTRIBUTE_NAME;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.CREATE_IPV4_SUBNET_ACTION;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.CREATE_IPV6_SUBNET_ACTION;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.CREATE_IP_NETWORK_ACTION;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.CREATE_OPERATIONS_FOR_IPV4_SUBNET_NETWORK_GROUP;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.CREATE_OPERATIONS_FOR_IPV6_SUBNET_NETWORK_GROUP;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.CREATE_OPERATIONS_FOR_NETWORK_GROUP;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.CREATE_OPERATION_ACTION;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.DELETE_HOST_ADDRESS_ACTION;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.DELETE_HOST_ASSIGNMENT_ACTION;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.DELETE_IPV4_SUBNET_ASSIGNMENT_ACTION;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.DELETE_IPV4_SUBNET_TYPE_OF_BLOCK_ACTION;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.DELETE_IPV4_SUBNET_TYPE_OF_NETWORK_ACTION;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.DELETE_IPV6_SUBNET_ASSIGNMENT_ACTION;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.DELETE_IPV6_SUBNET_TYPE_OF_BLOCK_ACTION;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.DELETE_IPV6_SUBNET_TYPE_OF_NETWORK_ACTION;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.DELETE_IP_NETWORK_ACTION;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.EDIT_IPV4_HOST_ASSIGNMENT_ACTION;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.EDIT_IPV4_SUBNET_ACTION;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.EDIT_IPV6_HOST_ASSIGNMENT_ACTION;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.EDIT_IPV6_SUBNET_ACTION;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.EDIT_IP_NETWORK_ACTION;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.EDIT_OPERATION_ACTION;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.EDIT_OPERATION_FOR_IPV4_HOST_ADDRESS_GROUP;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.EDIT_OPERATION_FOR_IPV4_HOST_ASSIGNMENT_GROUP;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.EDIT_OPERATION_FOR_IPV4_SUBNET_NETWORK_GROUP;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.EDIT_OPERATION_FOR_IPV6_HOST_ADDRESS_GROUP;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.EDIT_OPERATION_FOR_IPV6_HOST_ASSIGNMENT_GROUP;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.EDIT_OPERATION_FOR_IPV6_SUBNET_NETWORK_GROUP;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.EDIT_OPERATION_FOR_NETWORK_GROUP;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.EDIT_ROLE_OF_IPV4_SUBNET_ASSIGNMENT_ACTION;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.EDIT_ROLE_OF_IPV6_SUBNET_ASSIGNMENT_ACTION;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.IPADDRESS_MANAGEMENT_VIEW_URL;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.IP_NETWORK_REASSIGNMENT_HOST_ADDRESS_IPV4_ASSIGNMENT_CONTEXT_ACTION;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.IP_NETWORK_REASSIGNMENT_HOST_ADDRESS_IPV4_CONTEXT_ACTION;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.IP_NETWORK_REASSIGNMENT_HOST_ADDRESS_IPV6_ASSIGNMENT_CONTEXT_ACTION;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.IP_NETWORK_REASSIGNMENT_HOST_ADDRESS_IPV6_CONTEXT_ACTION;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.IP_NETWORK_REASSIGNMENT_NETWORK_SUBNET_IPV4_CONTEXT_ACTION;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.IP_NETWORK_REASSIGNMENT_NETWORK_SUBNET_IPV6_CONTEXT_ACTION;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.LIVE;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.MASK_DATA_ATTRIBUTE_NAME;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.MERGE_IPV4_SUBNET_ACTION;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.MERGE_IPV6_SUBNET_ACTION;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.NEW_ROLE_DATA_ATTRIBUTE_NAME;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.OK_BUTTON_LABEL;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.OSS_WINDOW_CLASS;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.PERSPECTIVE;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.RESERVE_IPV4_ADDRESS_ACTION;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.RESERVE_IPV6_ADDRESS_ACTION;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.RESERVE_LOOPBACK_IPV4_ADDRESS_ACTION;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.RESERVE_LOOPBACK_IPV6_ADDRESS_ACTION;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.ROLE_ACTION;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.SPLIT_IPV4_SUBNET_ACTION;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.SPLIT_IPV6_SUBNET_ACTION;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.TABS_CONTAINER_CLASS;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.WINDOW_TOOLBAR_CLASS;

public class IPAddressManagementViewPage extends BasePage {

    private static final String WIZARD_ID = "Popup";
    private static final String TREE_ID = "uaaSubnetIPTreeAppID";
    private OldActionsContainer actionsContainer;
    private PropertyPanelInterface propertyPanel;

    public IPAddressManagementViewPage(WebDriver driver) {
        super(driver);
        waitForPageToLoad();
    }

    public static IPAddressManagementViewPage goToIPAddressManagementViewPageLive(WebDriver driver, String basicURL) {
        driver.get(String.format(IPADDRESS_MANAGEMENT_VIEW_URL + PERSPECTIVE, basicURL, LIVE));
        return new IPAddressManagementViewPage(driver);
    }

    @Step("Open ip Address Management")
    public static IPAddressManagementViewPage goToIPAddressManagementPage(WebDriver driver, String basicURL) {
        driver.get(String.format(IPADDRESS_MANAGEMENT_VIEW_URL, basicURL));
        DelayUtils.sleep(1000);
        return new IPAddressManagementViewPage(driver);
    }

    @Step("Refresh IP Address Management Page")
    public static IPAddressManagementViewPage refreshIPAddressManagementViewPage(WebDriver driver) {
        driver.navigate().refresh();
        return new IPAddressManagementViewPage(driver);
    }

    public PropertyPanelInterface getPropertyPanel() {
        if (propertyPanel == null) {
            DelayUtils.waitForVisibility(wait, driver.findElement(By.className(OSS_WINDOW_CLASS)));
            propertyPanel = OldPropertyPanel.create(driver, wait);
            DelayUtils.waitForVisibility(wait, driver.findElement(By.className(TABS_CONTAINER_CLASS)));
        }
        DelayUtils.waitForVisibility(wait, driver.findElement(By.className(TABS_CONTAINER_CLASS)));
        return propertyPanel;
    }

    @Step("Get property {propertyName} value")
    public String getPropertyValue(String propertyName) {
        waitForPageToLoad();
        return getPropertyPanel().getPropertyValue(propertyName);
    }

    @Step("Search IP Network")
    public IPAddressManagementViewPage searchIpNetwork(String value) {
        getTreeView().search(value);
        waitForPageToLoad();
        return this;
    }

    @Step("Open Roles by clicking on Role button")
    public RoleViewPage openRoleView() {
        waitForPageToLoad();
        useContextAction(ROLE_ACTION);
        waitForPageToLoad();
        return new RoleViewPage(driver);
    }

    @Step("Select first object on hierarchy view")
    public void selectFirstTreeRow() {
        waitForPageToLoad();
        getTreeView().selectFirstTreeRow();
    }

    @Step("Select object with name: {name} on hierarchy view")
    public void selectTreeRow(String name) {
        waitForPageToLoad();
        getTreeView().selectTreeRow(name);
    }

    @Step("Select object contains name {name} on hierarchy view")
    public void selectTreeRowContains(String name) {
        waitForPageToLoad();
        getTreeView().selectTreeRowContains(name);
    }

    @Step("Unselect object with name: {name} on hierarchy view")
    public void unselectTreeRow(String name) {
        waitForPageToLoad();
        getTreeView().selectTreeRowContains(name);
    }

    @Step("Check if tree row is expanded")
    public boolean isTreeRowExpanded(String name) {
        waitForPageToLoad();
        return getTreeView().isTreeRowExpanded(name);
    }

    @Step("Expand object with name: {name} on hierarchy view")
    public void expandTreeRow(String name) {
        waitForPageToLoad();
        getTreeView().expandTreeRow(name);
        waitForPageToLoad();
    }

    @Step("Expand object with name: {name} on hierarchy view")
    public void expandTreeRowContains(String name) {
        waitForPageToLoad();
        getTreeView().expandTreeRowContains(name);
        waitForPageToLoad();
    }

    @Step("Use context action")
    public void useContextAction(String actionDataAttributeName) {
        waitForPageToLoad();
        getActionsInterface().callActionById(actionDataAttributeName);
    }

    @Step("Use context action")
    public void useContextAction(String groupId, String actionDataAttributeName) {
        waitForPageToLoad();
        getActionsInterface().callActionById(groupId, actionDataAttributeName);
    }

    @Step("Use context action")
    public void useContextAction(String groupId, String innerGroupDataAttributeName, String actionDataAttributeName) {
        waitForPageToLoad();
        getActionsInterface().callActionById(groupId, innerGroupDataAttributeName, actionDataAttributeName);
    }

    @Step("Check if system message type is SUCCESS")
    public boolean isSystemMessageSuccess() {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, wait);
        SystemMessageContainer.MessageType systemMessageType = systemMessage.getFirstMessage()
                .orElseThrow(() -> new RuntimeException("The list is empty")).getMessageType();
        systemMessage.close();
        return systemMessageType.equals(SUCCESS);
    }

    @Step("Create IP Network with name: {networkName} and description {description}")
    public void createIPNetwork(String networkName, String description) {
        selectFirstTreeRow();
        useContextAction(CREATE_OPERATIONS_FOR_NETWORK_GROUP, CREATE_OPERATION_ACTION, CREATE_IP_NETWORK_ACTION);
        IPNetworkWizardPage ipNetworkWizardPage = new IPNetworkWizardPage(driver);
        ipNetworkWizardPage.createIPNetwork(networkName, description);
        waitForPageToLoad();
    }

    @Step("Edit IP Network with name: {networkName} to network with name: {networkNameUpdated} and description: {description}")
    public void editIPNetwork(String networkName, String networkNameUpdated, String description) {
        selectTreeRow(networkName);
        useContextAction(EDIT_OPERATION_FOR_NETWORK_GROUP, EDIT_OPERATION_ACTION, EDIT_IP_NETWORK_ACTION);
        IPNetworkWizardPage ipNetworkWizardPage = new IPNetworkWizardPage(driver);
        ipNetworkWizardPage.editIPNetwork(networkNameUpdated, description);
        waitForPageToLoad();
    }

    @Step("Create IPv4 Subnet")
    public IPSubnetWizardPage createIPv4Subnet() {
        useContextAction(CREATE_OPERATIONS_FOR_NETWORK_GROUP, CREATE_OPERATION_ACTION, CREATE_IPV4_SUBNET_ACTION);
        return new IPSubnetWizardPage(driver);
    }

    @Step("Create IPv6 Subnet")
    public IPSubnetWizardPage createIPv6Subnet() {
        useContextAction(CREATE_OPERATIONS_FOR_NETWORK_GROUP, CREATE_OPERATION_ACTION, CREATE_IPV6_SUBNET_ACTION);
        return new IPSubnetWizardPage(driver);
    }

    @Step("Change IPv4 Subnet: {rowName} type to Block")
    public void changeIPv4SubnetTypeToBlock(String rowName) {
        selectTreeRowContains(rowName);
        useContextAction(EDIT_OPERATION_FOR_IPV4_SUBNET_NETWORK_GROUP, EDIT_OPERATION_ACTION, CHANGE_IPV4_SUBNET_TYPE_TO_BLOCK_ACTION);
        acceptConfirmationBox();
    }

    @Step("Change IPv6 Subnet: {rowName} type to Block")
    public void changeIPv6SubnetTypeToBlock(String rowName) {
        selectTreeRowContains(rowName);
        useContextAction(EDIT_OPERATION_FOR_IPV6_SUBNET_NETWORK_GROUP, EDIT_OPERATION_ACTION, CHANGE_IPV6_SUBNET_TYPE_TO_BLOCK_ACTION);
        acceptConfirmationBox();
    }

    @Step("Edit IPv4 Subnet: {rowName} role to {role} and description to {description}")
    public void editIPv4Subnet(String rowName, String role, String description) {
        selectTreeRowContains(rowName);
        useContextAction(EDIT_OPERATION_FOR_IPV4_SUBNET_NETWORK_GROUP, EDIT_OPERATION_ACTION, EDIT_IPV4_SUBNET_ACTION);
        editIPSubnet(role, description);
    }

    @Step("Edit IPv6 Subnet: {rowName} role to {role} and description to {description}")
    public void editIPv6Subnet(String rowName, String role, String description) {
        selectTreeRowContains(rowName);
        useContextAction(EDIT_OPERATION_FOR_IPV6_SUBNET_NETWORK_GROUP, EDIT_OPERATION_ACTION, EDIT_IPV6_SUBNET_ACTION);
        editIPSubnet(role, description);
    }

    @Step("Split IPv4 Subnet: {rowName}")
    public IPSubnetWizardPage splitIPv4Subnet(String rowName) {
        selectTreeRowContains(rowName);
        useContextAction(SPLIT_IPV4_SUBNET_ACTION);
        return new IPSubnetWizardPage(driver);
    }

    @Step("Split IPv6 Subnet: {rowName}")
    public IPSubnetWizardPage splitIPv6Subnet(String rowName) {
        selectTreeRowContains(rowName);
        useContextAction(SPLIT_IPV6_SUBNET_ACTION);
        return new IPSubnetWizardPage(driver);
    }

    @Step("Merge IPv4 Subnets")
    public IPSubnetWizardPage mergeIPv4Subnet(String... rowName) {
        Arrays.stream(rowName).forEach(this::selectTreeRowContains);
        useContextAction(MERGE_IPV4_SUBNET_ACTION);
        return new IPSubnetWizardPage(driver);
    }

    @Step("Merge IPv6 Subnets")
    public IPSubnetWizardPage mergeIPv6Subnet(String... rowName) {
        Arrays.stream(rowName).forEach(this::selectTreeRowContains);
        useContextAction(MERGE_IPV6_SUBNET_ACTION);
        return new IPSubnetWizardPage(driver);
    }

    @Step("Assign IPv4 Subnet {rowName}")
    public void assignIPv4Subnet(String rowName, String assignmentType, String assignmentName, String role) {
        selectTreeRowContains(rowName);
        waitForPageToLoad();
        useContextAction(ASSIGN_OPERATION_FOR_IPV4_SUBNET_BLOCK_WITHOUT_ASSIGNMENT, ASSIGN_OPERATION_ACTION, ASSIGN_IPV4_SUBNET_ACTION);
        assignIPSubnet(assignmentType, assignmentName, role);
    }

    @Step("Assign IPv6 Subnet {rowName}")
    public void assignIPv6Subnet(String rowName, String assignmentType, String assignmentName, String role) {
        selectTreeRowContains(rowName);
        waitForPageToLoad();
        useContextAction(ASSIGN_OPERATION_FOR_IPV6_SUBNET_BLOCK_WITHOUT_ASSIGNMENT, ASSIGN_OPERATION_ACTION, ASSIGN_IPV6_SUBNET_ACTION);
        assignIPSubnet(assignmentType, assignmentName, role);
    }

    @Step("Edit role for IPv4 Subnet assignment {rowName} to: {newRoleName}")
    public void editRoleForIPv4SubnetAssignment(String rowName, String newRoleName) {
        selectTreeRowContains(rowName);
        useContextAction(ASSIGN_OPERATION_FOR_IPV4_SUBNET_BLOCK_WITH_ASSIGNMENT, ASSIGN_OPERATION_ACTION, EDIT_ROLE_OF_IPV4_SUBNET_ASSIGNMENT_ACTION);
        editRoleForIPSubnetAssignment(newRoleName);
    }

    @Step("Edit role for IPv6 Subnet assignment {rowName} to: {newRoleName}")
    public void editRoleForIPv6SubnetAssignment(String rowName, String newRoleName) {
        selectTreeRowContains(rowName);
        useContextAction(ASSIGN_OPERATION_FOR_IPV6_SUBNET_BLOCK_WITH_ASSIGNMENT, ASSIGN_OPERATION_ACTION, EDIT_ROLE_OF_IPV6_SUBNET_ASSIGNMENT_ACTION);
        editRoleForIPSubnetAssignment(newRoleName);
    }

    public void reserveGivenIPv4HostAddress(String rowName, String ipAddress) {
        selectTreeRowContains(rowName);
        waitForPageToLoad();
        useContextAction(CREATE_OPERATIONS_FOR_IPV4_SUBNET_NETWORK_GROUP, CREATE_OPERATION_ACTION, RESERVE_IPV4_ADDRESS_ACTION);
        reserveGivenIPAddress(ipAddress);
    }

    public void reserveGivenIPv6HostAddress(String rowName, String ipAddress) {
        selectTreeRowContains(rowName);
        waitForPageToLoad();
        useContextAction(CREATE_OPERATIONS_FOR_IPV6_SUBNET_NETWORK_GROUP, CREATE_OPERATION_ACTION, RESERVE_IPV6_ADDRESS_ACTION);
        reserveGivenIPAddress(ipAddress);
    }

    public void bulkIPv4AddressReservation(String numberOfHostAddressesToReserve, Boolean reserveConsecutive, String... rowName) {
        Arrays.stream(rowName).forEach(this::selectTreeRowContains);
        waitForPageToLoad();
        useContextAction(CREATE_OPERATIONS_FOR_IPV4_SUBNET_NETWORK_GROUP, CREATE_OPERATION_ACTION, RESERVE_IPV4_ADDRESS_ACTION);
        ReserveIPAddressWizardPage reserveIPAddressWizardPage = new ReserveIPAddressWizardPage(driver);
        reserveIPAddressWizardPage.bulkIPAddressReservation(numberOfHostAddressesToReserve, reserveConsecutive);
    }

    public void bulkIPv6AddressReservation(String numberOfHostAddressesToReserve, Boolean reserveConsecutive, String... rowName) {
        Arrays.stream(rowName).forEach(this::selectTreeRowContains);
        waitForPageToLoad();
        useContextAction(CREATE_OPERATIONS_FOR_IPV6_SUBNET_NETWORK_GROUP, CREATE_OPERATION_ACTION, RESERVE_IPV6_ADDRESS_ACTION);
        ReserveIPAddressWizardPage reserveIPAddressWizardPage = new ReserveIPAddressWizardPage(driver);
        reserveIPAddressWizardPage.bulkIPAddressReservation(numberOfHostAddressesToReserve, reserveConsecutive);
    }

    @Step("Reserve IPv4 Host Address {rowName}")
    public void reserveIPv4HostAddress(String rowName, String description) {
        selectTreeRowContains(rowName);
        waitForPageToLoad();
        useContextAction(CREATE_OPERATIONS_FOR_IPV4_SUBNET_NETWORK_GROUP, CREATE_OPERATION_ACTION, RESERVE_IPV4_ADDRESS_ACTION);
        reserveIPHostAndAccept(description);
    }

    @Step("Reserve IPv6 Host Address {rowName}")
    public void reserveIPv6HostAddress(String rowName, String description) {
        selectTreeRowContains(rowName);
        waitForPageToLoad();
        useContextAction(CREATE_OPERATIONS_FOR_IPV6_SUBNET_NETWORK_GROUP, CREATE_OPERATION_ACTION, RESERVE_IPV6_ADDRESS_ACTION);
        reserveIPHostAndAccept(description);
    }

    @Step("Reserve Loopback IPv4 Host Address {rowName}")
    public void reserveLoopbackIPv4HostAddress(String rowName, String description) {
        selectTreeRowContains(rowName);
        waitForPageToLoad();
        useContextAction(CREATE_OPERATIONS_FOR_IPV4_SUBNET_NETWORK_GROUP, CREATE_OPERATION_ACTION, RESERVE_LOOPBACK_IPV4_ADDRESS_ACTION);
        reserveIPHost(description);
    }

    @Step("Reserve Loopback IPv6 Host Address {rowName}")
    public void reserveLoopbackIPv6HostAddress(String rowName, String description) {
        selectTreeRowContains(rowName);
        waitForPageToLoad();
        useContextAction(CREATE_OPERATIONS_FOR_IPV6_SUBNET_NETWORK_GROUP, CREATE_OPERATION_ACTION, RESERVE_LOOPBACK_IPV6_ADDRESS_ACTION);
        reserveIPHost(description);
    }

    @Step("Assign IPv4 Host Address from subnet {rowName} context")
    public IPAddressAssignmentWizardPage assignIPv4HostAddressFromSubnetContext(String rowName) {
        return assignIPHost(rowName, ASSIGN_OPERATION_FOR_IPV4_SUBNET_NETWORK_WITHOUT_ASSIGNMENT, ASSIGN_IPV4_ADDRESS_ACTION_FROM_SUBNET_CONTEXT);
    }

    @Step("Assign IPv4 Host Address from subnet {rowName} context when assign button is visible")
    public IPAddressAssignmentWizardPage assignIPv4HostAddressFromSubnetContextWithVisibleButton(String rowName) {
        return assignIPHostWithVisibleButton(rowName, ASSIGN_OPERATION_FOR_IPV4_SUBNET_NETWORK_WITHOUT_ASSIGNMENT, ASSIGN_IPV4_ADDRESS_ACTION_FROM_SUBNET_CONTEXT);
    }

    @Step("Assign IPv6 Host Address from subnet {rowName} context when assign button is visible")
    public IPAddressAssignmentWizardPage assignIPv6HostAddressFromSubnetContextWithVisibleButton(String rowName) {
        return assignIPHostWithVisibleButton(rowName, ASSIGN_OPERATION_FOR_IPV6_SUBNET_NETWORK_WITHOUT_ASSIGNMENT, ASSIGN_IPV6_ADDRESS_ACTION_FROM_SUBNET_CONTEXT_ALFA);
    }

    @Step("Assign IPv6 Host Address from subnet {rowName} context")
    public IPAddressAssignmentWizardPage assignIPv6HostAddressFromSubnetContext(String rowName) {
        return assignIPHost(rowName, ASSIGN_OPERATION_FOR_IPV6_SUBNET_NETWORK_WITHOUT_ASSIGNMENT, ASSIGN_IPV6_ADDRESS_ACTION_FROM_SUBNET_CONTEXT);
    }

    @Step("Assign IPv6 Host Address from subnet {rowName} context")
    public IPAddressAssignmentWizardPage assignIPv6HostAddressFromSubnetContextAlfa(String rowName) {
        return assignIPHost(rowName, ASSIGN_OPERATION_FOR_IPV6_SUBNET_NETWORK_WITHOUT_ASSIGNMENT, ASSIGN_IPV6_ADDRESS_ACTION_FROM_SUBNET_CONTEXT_ALFA);
    }

    @Step("Assign Loopback IPv4 Host Address from subnet {rowName} context")
    public IPAddressAssignmentWizardPage assignLoopbackIPv4HostAddressFromSubnetContext(String rowName) {
        return assignIPHost(rowName, ASSIGN_OPERATION_FOR_IPV4_SUBNET_NETWORK_WITHOUT_ASSIGNMENT, ASSIGN_LOOPBACK_IPV4_ADDRESS_ACTION_FROM_SUBNET_CONTEXT);
    }

    @Step("Assign Loopback IPv6 Host Address from host {rowName} context")
    public IPAddressAssignmentWizardPage assignLoopbackIPv6HostAddressFromHostContext(String rowName) {
        return assignIPHost(rowName, ASSIGN_OPERATION_FOR_IPV6_HOST_ADDRESS, ASSIGN_LOOPBACK_IPV6_ADDRESS_ACTION_FROM_HOST_CONTEXT);
    }

    @Step("Change IP Network to {destinationNetwork} for IPv4 Host Address Assignment")
    public void changeIPNetworkForIPv4AddressAssignment(String destinationNetwork, String... rowNames) {
        Arrays.stream(rowNames).forEach(this::selectTreeRowContains);
        waitForPageToLoad();
        useContextAction(EDIT_OPERATION_FOR_IPV4_HOST_ASSIGNMENT_GROUP, IP_NETWORK_REASSIGNMENT_HOST_ADDRESS_IPV4_ASSIGNMENT_CONTEXT_ACTION);
        waitForPageToLoad();
        ChangeIPNetworkWizardPage changeIPNetworkWizardPage = new ChangeIPNetworkWizardPage(driver);
        changeIPNetworkWizardPage.changeIPNetworkWithOutConflicts(destinationNetwork);
    }

    @Step("Change IP Network to {destinationNetwork} for IPv6 Host Address Assignment")
    public void changeIPNetworkForIPv6AddressAssignment(String destinationNetwork, String... rowNames) {
        Arrays.stream(rowNames).forEach(this::selectTreeRowContains);
        waitForPageToLoad();
        useContextAction(EDIT_OPERATION_FOR_IPV6_HOST_ASSIGNMENT_GROUP, IP_NETWORK_REASSIGNMENT_HOST_ADDRESS_IPV6_ASSIGNMENT_CONTEXT_ACTION);
        waitForPageToLoad();
        ChangeIPNetworkWizardPage changeIPNetworkWizardPage = new ChangeIPNetworkWizardPage(driver);
        changeIPNetworkWizardPage.changeIPNetworkWithOutConflicts(destinationNetwork);
    }

    @Step("Change IP Network to {destinationNetwork} for IPv4 Host Address")
    public void changeIPNetworkForIPv4HostAddress(String destinationNetwork, String... rowNames) {
        Arrays.stream(rowNames).forEach(this::selectTreeRowContains);
        waitForPageToLoad();
        useContextAction(EDIT_OPERATION_FOR_IPV4_HOST_ADDRESS_GROUP, IP_NETWORK_REASSIGNMENT_HOST_ADDRESS_IPV4_CONTEXT_ACTION);
        waitForPageToLoad();
        ChangeIPNetworkWizardPage changeIPNetworkWizardPage = new ChangeIPNetworkWizardPage(driver);
        changeIPNetworkWizardPage.changeIPNetworkWithOutConflicts(destinationNetwork);
    }

    @Step("Change IP Network to {destinationNetwork} for IPv6 Host Address")
    public void changeIPNetworkForIPv6HostAddress(String destinationNetwork, String... rowNames) {
        Arrays.stream(rowNames).forEach(this::selectTreeRowContains);
        waitForPageToLoad();
        useContextAction(EDIT_OPERATION_FOR_IPV6_HOST_ADDRESS_GROUP, IP_NETWORK_REASSIGNMENT_HOST_ADDRESS_IPV6_CONTEXT_ACTION);
        waitForPageToLoad();
        ChangeIPNetworkWizardPage changeIPNetworkWizardPage = new ChangeIPNetworkWizardPage(driver);
        changeIPNetworkWizardPage.changeIPNetworkWithOutConflicts(destinationNetwork);
    }

    @Step("Change IP Network to {destinationNetwork} for IPv4 Network Subnet")
    public void changeIPNetworkForIPv4NetworkSubnet(String destinationNetwork, String... subnetsRowNames) {
        Arrays.stream(subnetsRowNames).forEach(this::selectTreeRowContains);
        waitForPageToLoad();
        useContextAction(EDIT_OPERATION_FOR_IPV4_SUBNET_NETWORK_GROUP, EDIT_OPERATION_ACTION, IP_NETWORK_REASSIGNMENT_NETWORK_SUBNET_IPV4_CONTEXT_ACTION);
        waitForPageToLoad();
        ChangeIPNetworkWizardPage changeIPNetworkWizardPage = new ChangeIPNetworkWizardPage(driver);
        changeIPNetworkWizardPage.changeIPNetworkWithOutConflicts(destinationNetwork);
    }

    @Step("Change IP Network to {destinationNetwork} for IPv4 Network Subnet")
    public void changeIPNetworkForIPv6NetworkSubnet(String destinationNetwork, String... subnetsRowNames) {
        Arrays.stream(subnetsRowNames).forEach(this::selectTreeRowContains);
        waitForPageToLoad();
        useContextAction(EDIT_OPERATION_FOR_IPV6_SUBNET_NETWORK_GROUP, EDIT_OPERATION_ACTION, IP_NETWORK_REASSIGNMENT_NETWORK_SUBNET_IPV6_CONTEXT_ACTION);
        waitForPageToLoad();
        ChangeIPNetworkWizardPage changeIPNetworkWizardPage = new ChangeIPNetworkWizardPage(driver);
        changeIPNetworkWizardPage.changeIPNetworkWithOutConflicts(destinationNetwork);
    }

    @Step("Change IPv4 Host {rowName} Mask")
    public void changeIPv4HostMask(String rowName, String mask) {
        selectTreeRowContains(rowName);
        waitForPageToLoad();
        useContextAction(EDIT_OPERATION_FOR_IPV4_HOST_ADDRESS_GROUP, EDIT_OPERATION_ACTION, CHANGE_IPV4_HOST_ADDRESS_MASK_ACTION);
        changeIPHostMask(mask);
    }

    @Step("Change IPv6 Host {rowName} Mask")
    public void changeIPv6HostMask(String rowName, String mask) {
        selectTreeRowContains(rowName);
        waitForPageToLoad();
        useContextAction(EDIT_OPERATION_FOR_IPV6_HOST_ADDRESS_GROUP, EDIT_OPERATION_ACTION, CHANGE_IPV6_HOST_ADDRESS_MASK_ACTION);
        changeIPHostMask(mask);
    }

    @Step("Edit IPv4 Host Assignment {rowName}")
    public void editIPv4HostAssignment(String rowName, IPAddressAssignmentWizardProperties ipAddressAssignmentWizardProperties) {
        selectTreeRowContains(rowName);
        waitForPageToLoad();
        useContextAction(EDIT_OPERATION_FOR_IPV4_HOST_ASSIGNMENT_GROUP, EDIT_OPERATION_ACTION, EDIT_IPV4_HOST_ASSIGNMENT_ACTION);
        EditIPAddressPropertiesWizardPage editIPAddressPropertiesWizardPage = new EditIPAddressPropertiesWizardPage(driver);
        editIPAddressPropertiesWizardPage.editIPAddressProperties(ipAddressAssignmentWizardProperties);
    }

    @Step("Edit IPv6 Host Assignment {rowName}")
    public void editIPv6HostAssignment(String rowName, IPAddressAssignmentWizardProperties ipAddressAssignmentWizardProperties) {
        selectTreeRowContains(rowName);
        waitForPageToLoad();
        useContextAction(EDIT_OPERATION_FOR_IPV6_HOST_ASSIGNMENT_GROUP, EDIT_OPERATION_ACTION, EDIT_IPV6_HOST_ASSIGNMENT_ACTION);
        EditIPAddressPropertiesWizardPage editIPAddressPropertiesWizardPage = new EditIPAddressPropertiesWizardPage(driver);
        editIPAddressPropertiesWizardPage.editIPAddressProperties(ipAddressAssignmentWizardProperties);
    }

    @Step("Delete host assignment with: {name}")
    public void deleteHostAssignment(String name) {
        waitForPageToLoad();
        selectTreeRowContains(name);
        useContextAction(DELETE_HOST_ASSIGNMENT_ACTION);
        acceptConfirmationBox();
    }

    @Step("Delete IP Network with: {name}")
    public void deleteIPNetwork(String name) {
        waitForPageToLoad();
        selectTreeRowContains(name);
        useContextAction(DELETE_IP_NETWORK_ACTION);
        acceptConfirmationBox();
    }

    @Step("Delete IPv4 Subnet type of Block with: {name}")
    public void deleteIPv4SubnetTypeOfBlock(String name) {
        waitForPageToLoad();
        selectTreeRowContains(name);
        useContextAction(DELETE_IPV4_SUBNET_TYPE_OF_BLOCK_ACTION);
        acceptConfirmationBox();
    }

    @Step("Delete IPv4 Subnet type of Network with: {name}")
    public void deleteIPv4SubnetTypeOfNetwork(String name) {
        waitForPageToLoad();
        selectTreeRowContains(name);
        useContextAction(DELETE_IPV4_SUBNET_TYPE_OF_NETWORK_ACTION);
        acceptConfirmationBox();
    }

    @Step("Delete IPv6 Subnet type of Block with: {name}")
    public void deleteIPv6SubnetTypeOfBlock(String name) {
        waitForPageToLoad();
        selectTreeRowContains(name);
        useContextAction(DELETE_IPV6_SUBNET_TYPE_OF_BLOCK_ACTION);
        acceptConfirmationBox();
    }

    @Step("Delete IPv6 Subnet type of Network with: {name}")
    public void deleteIPv6SubnetTypeOfNetwork(String name) {
        waitForPageToLoad();
        selectTreeRowContains(name);
        useContextAction(DELETE_IPV6_SUBNET_TYPE_OF_NETWORK_ACTION);
        acceptConfirmationBox();
    }

    @Step("Delete IPv4 Subnet Assignment for Subnet with: {name}")
    public void deleteIPv4SubnetAssignment(String name) {
        waitForPageToLoad();
        selectTreeRowContains(name);
        useContextAction(ASSIGN_OPERATION_FOR_IPV4_SUBNET_BLOCK_WITH_ASSIGNMENT, ASSIGN_OPERATION_ACTION, DELETE_IPV4_SUBNET_ASSIGNMENT_ACTION);
        acceptConfirmationBox();
    }

    @Step("Delete IPv6 Subnet Assignment for Subnet with: {name}")
    public void deleteIPv6SubnetAssignment(String name) {
        waitForPageToLoad();
        selectTreeRowContains(name);
        useContextAction(ASSIGN_OPERATION_FOR_IPV6_SUBNET_BLOCK_WITH_ASSIGNMENT, ASSIGN_OPERATION_ACTION, DELETE_IPV6_SUBNET_ASSIGNMENT_ACTION);
        acceptConfirmationBox();
    }

    @Step("Delete IP Host with: {name}")
    public void deleteIPHost(String name) {
        waitForPageToLoad();
        selectTreeRowContains(name);
        useContextAction(DELETE_HOST_ADDRESS_ACTION);
        acceptConfirmationBox();
    }

    public void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    private TreeWidget getTreeView() {
        return TreeWidget.createById(driver, wait, TREE_ID);
    }

    private OldActionsContainer getActionsInterface() {
        if (actionsContainer == null) {
            DelayUtils.waitForVisibility(wait, driver.findElement(By.className(OSS_WINDOW_CLASS)));
            actionsContainer = OldActionsContainer.createFromParent(driver, wait, driver.findElement(By.className(OSS_WINDOW_CLASS)));
            DelayUtils.waitForVisibility(wait, driver.findElement(By.className(WINDOW_TOOLBAR_CLASS)));
        }
        DelayUtils.waitForVisibility(wait, driver.findElement(By.className(WINDOW_TOOLBAR_CLASS)));
        return actionsContainer;
    }

    private void editIPSubnet(String role, String description) {
        EditIPSubnetWizardPage editIPSubnetWizardPage = new EditIPSubnetWizardPage(driver);
        editIPSubnetWizardPage.editIPSubnet(role, description);
    }

    private void assignIPSubnet(String assignmentType, String assignmentName, String role) {
        waitForPageToLoad();
        AssignIPSubnetWizardPage assignIPSubnetWizardPage = new AssignIPSubnetWizardPage(driver);
        assignIPSubnetWizardPage.assignIPSubnet(assignmentType, assignmentName, role);
    }

    private void editRoleForIPSubnetAssignment(String newRoleName) {
        Wizard editIPSubnetAssignmentRole = Wizard.createByComponentId(driver, wait, WIZARD_ID);
        editIPSubnetAssignmentRole.setComponentValue(NEW_ROLE_DATA_ATTRIBUTE_NAME, newRoleName, COMBOBOX);
        editIPSubnetAssignmentRole.clickOK();
    }

    private void reserveIPHostAndAccept(String description) {
        ReserveIPAddressWizardPage reserveIPAddressWizardPage = new ReserveIPAddressWizardPage(driver);
        reserveIPAddressWizardPage.reserveIPAddressAndAccept(description);
    }

    private void reserveIPHost(String description) {
        ReserveIPAddressWizardPage reserveIPAddressWizardPage = new ReserveIPAddressWizardPage(driver);
        reserveIPAddressWizardPage.reserveIPAddressAndClickOk(description);
    }

    private void reserveGivenIPAddress(String ipAddress) {
        ReserveIPAddressWizardPage reserveIPAddressWizardPage = new ReserveIPAddressWizardPage(driver);
        reserveIPAddressWizardPage.reserveGivenIPAddress(ipAddress);
    }

    private IPAddressAssignmentWizardPage assignIPHost(String rowName, String contextActionGroup, String contextAction) {
        selectTreeRowContains(rowName);
        waitForPageToLoad();
        useContextAction(contextActionGroup, ASSIGN_OPERATION_ACTION, contextAction);
        return new IPAddressAssignmentWizardPage(driver);
    }

    private IPAddressAssignmentWizardPage assignIPHostWithVisibleButton(String rowName, String contextActionGroup, String contextAction) {
        selectTreeRowContains(rowName);
        waitForPageToLoad();
        useContextAction(contextActionGroup, contextAction);
        return new IPAddressAssignmentWizardPage(driver);
    }

    private void changeIPHostMask(String mask) {
        Wizard changeIPHostAddressMaskWizard = Wizard.createByComponentId(driver, wait, CHANGE_MASK_WIZARD_DATA_ATTRIBUTE_NAME);
        changeIPHostAddressMaskWizard.setComponentValue(MASK_DATA_ATTRIBUTE_NAME, mask, COMBOBOX);
        changeIPHostAddressMaskWizard.clickOK();
    }

    private void acceptConfirmationBox() {
        waitForPageToLoad();
        ConfirmationBoxInterface confirmationBox = ConfirmationBox.create(driver, wait);
        confirmationBox.clickButtonByLabel(OK_BUTTON_LABEL);
    }
}

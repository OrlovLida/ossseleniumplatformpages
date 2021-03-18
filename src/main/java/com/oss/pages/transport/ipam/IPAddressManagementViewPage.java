package com.oss.pages.transport.ipam;

import java.util.Arrays;

import com.oss.framework.widgets.Wizard;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.oss.framework.alerts.SystemMessageContainer;
import com.oss.framework.alerts.SystemMessageInterface;
import com.oss.framework.components.contextactions.OldActionsContainer;
import com.oss.framework.prompts.ConfirmationBox;
import com.oss.framework.prompts.ConfirmationBoxInterface;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Widget;
import com.oss.framework.widgets.propertypanel.OldPropertyPanel;
import com.oss.framework.widgets.propertypanel.PropertyPanelInterface;
import com.oss.framework.widgets.treewidget.TreeWidget;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

import static com.oss.framework.alerts.SystemMessageContainer.MessageType.SUCCESS;
import static com.oss.framework.components.inputs.Input.ComponentType.COMBOBOX;
import static com.oss.framework.components.inputs.Input.ComponentType.SEARCH_FIELD;

public class IPAddressManagementViewPage extends BasePage {
    private static final String IPADDRESS_MANAGEMENT_VIEW_URL = "%s/#/view/transport/ipmgt/ipTree?";
    private static final String PERSPECTIVE = "perspective=%s";
    private static final String LIVE = "LIVE";

    private static final String CREATE_OPERATIONS_FOR_NETWORK_GROUP = "CreateOperationsForNetwork";
    private static final String CREATE_OPERATIONS_FOR_IPV4_SUBNET_NETWORK_GROUP = "CreateOperationsForIPv4SubnetNetwork";
    private static final String CREATE_OPERATIONS_FOR_IPV6_SUBNET_NETWORK_GROUP = "CreateOperationsForIPv6SubnetNetwork";
    private static final String EDIT_OPERATION_FOR_NETWORK_GROUP = "EditOperationsForNetwork";
    private static final String EDIT_OPERATION_FOR_IPV4_SUBNET_NETWORK_GROUP = "EditOperationsForIPv4SubnetNetwork";
    private static final String EDIT_OPERATION_FOR_IPV6_SUBNET_NETWORK_GROUP = "EditOperationsForIPv6SubnetNetwork";
    private static final String EDIT_OPERATION_FOR_IPV4_HOST_ADDRESS_GROUP = "EditOperationsForIPv4HostAddress";
    private static final String EDIT_OPERATION_FOR_IPV6_HOST_ADDRESS_GROUP = "EditOperationsForIPv6HostAddress";
    private static final String ASSIGN_OPERATION_FOR_IPV4_SUBNET_WITH_ASSIGNMENT = "AssignOperationsForIPv4SubnetWithAssignment";
    private static final String ASSIGN_OPERATION_FOR_IPV6_SUBNET_WITH_ASSIGNMENT = "AssignOperationsForIPv6SubnetWithAssignment";
    private static final String ASSIGN_OPERATION_FOR_IPV4_SUBNET_WITHOUT_ASSIGNMENT = "AssignOperationFotIPv4SubnetWithoutAssignment";
    private static final String ASSIGN_OPERATION_FOR_IPV6_SUBNET_WITHOUT_ASSIGNMENT = "AssignOperationFotIPv6SubnetWithoutAssignment";
    private static final String CREATE_OPERATION_ACTION = "Create";
    private static final String EDIT_OPERATION_ACTION = "Edit";
    private static final String ASSIGN_OPERATION_ACTION = "Assign";
    private static final String CREATE_IP_NETWORK_ACTION = "CreateIPNetworkContextAction_1";
    private static final String EDIT_IP_NETWORK_ACTION = "EditIPNetworkContextAction";
    private static final String CREATE_IPV4_SUBNET_ACTION = "CreateIPv4SubnetContextAction_2";
    private static final String CREATE_IPV6_SUBNET_ACTION = "CreateIPv6SubnetContextAction_3";
    private static final String EDIT_IPV4_SUBNET_ACTION = "EditIPv4SubnetContextAction_30";
    private static final String EDIT_IPV6_SUBNET_ACTION = "EditIPv6SubnetContextAction_35";
    private static final String CHANGE_IPV4_SUBNET_TYPE_TO_BLOCK_ACTION = "ChangeIPv4SubnetTypeToBlockContextAction_32";
    private static final String CHANGE_IPV6_SUBNET_TYPE_TO_BLOCK_ACTION = "ChangeIPv6SubnetTypeToBlockContextAction_37";
    private static final String ASSIGN_IPV4_SUBNET_ACTION = "AssignSubnetContextAction_7";
    private static final String ASSIGN_IPV6_SUBNET_ACTION = "AssignSubnetContextAction_20";
    private static final String EDIT_ROLE_OF_IPV4_SUBNET_ASSIGNMENT_ACTION = "EditRoleOfSubnetAssignmentContextAction_13";
    private static final String EDIT_ROLE_OF_IPV6_SUBNET_ASSIGNMENT_ACTION = "EditRoleOfSubnetAssignmentContextAction_26";
    private static final String RESERVE_IPV4_ADDRESS_ACTION = "ReserveIPv4HostAddress";
    private static final String RESERVE_IPV6_ADDRESS_ACTION = "ReserveIPv6HostAddress";
    private static final String RESERVE_LOOPBACK_IPV4_ADDRESS_ACTION = "ReserveLoopbackIPv4HostAddress";
    private static final String RESERVE_LOOPBACK_IPV6_ADDRESS_ACTION = "ReserveLoopbackIPv6HostAddress";
    private static final String CHANGE_IPV4_HOST_ADDRESS_MASK_ACTION = "ChangeIPv4HostAddressMaskContextAction";
    private static final String CHANGE_IPV6_HOST_ADDRESS_MASK_ACTION = "ChangeIPv6HostAddressMaskContextAction";
    private static final String DELETE_IPV4_SUBNET_ASSIGNMENT_ACTION = "RemoveSubnetAssignmentContextAction_12";
    private static final String DELETE_IPV6_SUBNET_ASSIGNMENT_ACTION = "RemoveSubnetAssignmentContextAction_25";
    private static final String DELETE_ACTION = "DeleteHostAddressAssignmentContextAction";
    private static final String DELETE_IP_NETWORK_ACTION = "DeleteIPNetworkContextAction";
    private static final String DELETE_IPV4_SUBNET_TYPE_OF_BLOCK_ACTION = "DeleteIPv4SubnetBlockContextAction";
    private static final String DELETE_IPV4_SUBNET_TYPE_OF_NETWORK_ACTION = "DeleteIPv4SubnetNetworkContextAction";
    private static final String DELETE_IPV6_SUBNET_TYPE_OF_BLOCK_ACTION = "DeleteIPv6SubnetBlockContextAction";
    private static final String DELETE_IPV6_SUBNET_TYPE_OF_NETWORK_ACTION = "DeleteIPv6SubnetNetworkContextAction";
    private static final String DELETE_HOST_ADDRESS_ACTION = "DeleteHostAddressContextAction";
    private static final String SPLIT_IPV4_SUBNET_ACTION = "SplitIPv4SubnetContextAction";
    private static final String SPLIT_IPV6_SUBNET_ACTION = "SplitIPv6SubnetContextAction";
    private static final String MERGE_IPV4_SUBNET_ACTION = "MergeIPv4SubnetContextAction";
    private static final String MERGE_IPV6_SUBNET_ACTION = "MergeIPv6SubnetContextAction";
    private static final String ROLE_ACTION = "Role";

    private static final String OK_BUTTON_LABEL = "OK";
    private static final String TREE_VIEW_CLASS = "TreeView";
    private static final String TREE_VIEW_COMPONENT_CLASS = "TreeView";
    private static final String OSS_WINDOW_CLASS = "OssWindow";
    private static final String WINDOW_TOOLBAR_CLASS = "windowToolbar";
    private static final String TABS_CONTAINER_CLASS = "tabsContainer";
    private static final String NEW_ROLE_DATA_ATTRIBUTE_NAME = "new-role-uid";
    private static final String MASK_DATA_ATTRIBUTE_NAME = "mask-uid";
    private static final String POPUP_WIZARD_DATA_ATTRIBUTE_NAME = "Popup";

    private TreeWidget mainTree;
    private OldActionsContainer actionsContainer;
    private PropertyPanelInterface propertyPanel;

    public static IPAddressManagementViewPage goToIPAddressManagementViewPageLive(WebDriver driver, String basicURL) {
        driver.get(String.format(IPADDRESS_MANAGEMENT_VIEW_URL + PERSPECTIVE, basicURL, LIVE));
        return new IPAddressManagementViewPage(driver);
    }

    public IPAddressManagementViewPage(WebDriver driver) {
        super(driver);
        waitForPageToLoad();
    }

    private TreeWidget getTreeView() {
        if (mainTree == null) {
            Widget.waitForWidget(wait, TREE_VIEW_CLASS);
            mainTree = TreeWidget.createByClass(driver, TREE_VIEW_CLASS, wait);
            DelayUtils.waitForVisibility(wait, driver.findElement(By.className(TREE_VIEW_COMPONENT_CLASS)));
        }
        DelayUtils.waitForVisibility(wait, driver.findElement(By.className(TREE_VIEW_COMPONENT_CLASS)));
        return mainTree;
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

    @Step("Open ip Address Management")
    public static IPAddressManagementViewPage goToIPAddressManagementPage(WebDriver driver, String basicURL) {
        driver.get(String.format(IPADDRESS_MANAGEMENT_VIEW_URL, basicURL));
        DelayUtils.sleep(1000);
        return new IPAddressManagementViewPage(driver);
    }

    @Step("Search IP Network")
    public IPAddressManagementViewPage searchIpNetwork(String value) {
        getTreeView()
                .performSearchWithEnter(value);
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
    }

    @Step("Edit IP Network with name: {networkName} to network with name: {networkNameUpdated} and description: {description}")
    public void editIPNetwork(String networkName, String networkNameUpdated, String description) {
        selectTreeRow(networkName);
        useContextAction(EDIT_OPERATION_FOR_NETWORK_GROUP, EDIT_OPERATION_ACTION, EDIT_IP_NETWORK_ACTION);
        IPNetworkWizardPage ipNetworkWizardPage = new IPNetworkWizardPage(driver);
        ipNetworkWizardPage.editIPNetwork(networkNameUpdated, description);
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
    public void editIPv4Subnet(String rowName, String mask, String role, String description) {
        selectTreeRowContains(rowName);
        useContextAction(EDIT_OPERATION_FOR_IPV4_SUBNET_NETWORK_GROUP, EDIT_OPERATION_ACTION, EDIT_IPV4_SUBNET_ACTION);
        editIPSubnet(mask, role, description);
    }

    @Step("Edit IPv6 Subnet: {rowName} role to {role} and description to {description}")
    public void editIPv6Subnet(String rowName, String mask, String role, String description) {
        selectTreeRowContains(rowName);
        useContextAction(EDIT_OPERATION_FOR_IPV6_SUBNET_NETWORK_GROUP, EDIT_OPERATION_ACTION, EDIT_IPV6_SUBNET_ACTION);
        editIPSubnet(mask, role, description);
    }

    private void editIPSubnet(String mask, String role, String description){
        EditIPSubnetWizardPage editIPSubnetWizardPage = new EditIPSubnetWizardPage(driver);
        editIPSubnetWizardPage.editIPSubnet(mask, role, description);
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
    public IPSubnetWizardPage mergeIPv4Subnet(String ... rowName) {
        Arrays.stream(rowName).forEach(this::selectTreeRowContains);
        useContextAction(MERGE_IPV4_SUBNET_ACTION);
        return new IPSubnetWizardPage(driver);
    }

    @Step("Merge IPv6 Subnets")
    public IPSubnetWizardPage mergeIPv6Subnet(String ... rowName) {
        Arrays.stream(rowName).forEach(this::selectTreeRowContains);
        useContextAction(MERGE_IPV6_SUBNET_ACTION);
        return new IPSubnetWizardPage(driver);
    }

    @Step("Assign IPv4 Subnet {rowName}")
    public void assignIPv4Subnet(String rowName, String assignmentType, String assignmentName, String role) {
        selectTreeRowContains(rowName);
        waitForPageToLoad();
        useContextAction(ASSIGN_OPERATION_FOR_IPV4_SUBNET_WITHOUT_ASSIGNMENT, ASSIGN_OPERATION_ACTION, ASSIGN_IPV4_SUBNET_ACTION);
        assignIPSubnet(assignmentType, assignmentName, role);
    }

    @Step("Assign IPv6 Subnet {rowName}")
    public void assignIPv6Subnet(String rowName, String assignmentType, String assignmentName, String role) {
        selectTreeRowContains(rowName);
        waitForPageToLoad();
        useContextAction(ASSIGN_OPERATION_FOR_IPV6_SUBNET_WITHOUT_ASSIGNMENT, ASSIGN_OPERATION_ACTION, ASSIGN_IPV6_SUBNET_ACTION);
        assignIPSubnet(assignmentType, assignmentName, role);
    }

    private void assignIPSubnet(String assignmentType, String assignmentName, String role){
        AssignIPSubnetWizardPage assignIPSubnetWizardPage = new AssignIPSubnetWizardPage(driver);
        assignIPSubnetWizardPage.assignIPSubnet(assignmentType, assignmentName, role);
    }

    @Step("Edit role for IPv4 Subnet assignment {rowName} to: {newRoleName}")
    public void editRoleForIPv4SubnetAssignment(String rowName, String newRoleName) {
        selectTreeRowContains(rowName);
        useContextAction(ASSIGN_OPERATION_FOR_IPV4_SUBNET_WITH_ASSIGNMENT, ASSIGN_OPERATION_ACTION, EDIT_ROLE_OF_IPV4_SUBNET_ASSIGNMENT_ACTION);
        editRoleForIPSubnetAssignment(newRoleName);
    }

    private void editRoleForIPSubnetAssignment(String newRoleName){
        Wizard editIPSubnetAssignmentRole = Wizard.createByComponentId(driver, wait, POPUP_WIZARD_DATA_ATTRIBUTE_NAME);
        editIPSubnetAssignmentRole.setComponentValue(NEW_ROLE_DATA_ATTRIBUTE_NAME, newRoleName, COMBOBOX);
        editIPSubnetAssignmentRole.clickOK();
    }

    @Step("Edit role for IPv6 Subnet assignment {rowName} to: {newRoleName}")
    public void editRoleForIPv6SubnetAssignment(String rowName, String newRoleName) {
        selectTreeRowContains(rowName);
        useContextAction(ASSIGN_OPERATION_FOR_IPV6_SUBNET_WITH_ASSIGNMENT, ASSIGN_OPERATION_ACTION, EDIT_ROLE_OF_IPV6_SUBNET_ASSIGNMENT_ACTION);
        editRoleForIPSubnetAssignment(newRoleName);
    }

    @Step("Reserve IPv4 Host Address {rowName}")
    public void reserveIPv4HostAddress(String rowName, String address, String description) {
        selectTreeRowContains(rowName);
        waitForPageToLoad();
        useContextAction(CREATE_OPERATIONS_FOR_IPV4_SUBNET_NETWORK_GROUP, CREATE_OPERATION_ACTION, RESERVE_IPV4_ADDRESS_ACTION);
        reserveIPHost(address, description);
    }

    @Step("Reserve IPv6 Host Address {rowName}")
    public void reserveIPv6HostAddress(String rowName, String address, String description) {
        selectTreeRowContains(rowName);
        waitForPageToLoad();
        useContextAction(CREATE_OPERATIONS_FOR_IPV6_SUBNET_NETWORK_GROUP, CREATE_OPERATION_ACTION, RESERVE_IPV6_ADDRESS_ACTION);
        reserveIPHost(address, description);
    }

    @Step("Reserve Loopback IPv4 Host Address {rowName}")
    public void reserveLoopbackIPv4HostAddress(String rowName, String address, String description) {
        selectTreeRowContains(rowName);
        waitForPageToLoad();
        useContextAction(CREATE_OPERATIONS_FOR_IPV4_SUBNET_NETWORK_GROUP, CREATE_OPERATION_ACTION, RESERVE_LOOPBACK_IPV4_ADDRESS_ACTION);
        reserveIPHost(address, description);
    }

    @Step("Reserve Loopback IPv6 Host Address {rowName}")
    public void reserveLoopbackIPv6HostAddress(String rowName, String address, String description) {
        selectTreeRowContains(rowName);
        waitForPageToLoad();
        useContextAction(CREATE_OPERATIONS_FOR_IPV6_SUBNET_NETWORK_GROUP, CREATE_OPERATION_ACTION, RESERVE_LOOPBACK_IPV6_ADDRESS_ACTION);
        reserveIPHost(address, description);
    }

    private void reserveIPHost(String address, String description){
        ReserveIPAddressWizardPage reserveIPAddressWizardPage = new ReserveIPAddressWizardPage(driver);
        reserveIPAddressWizardPage.reserveIPAddress(address, description);
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

    private void changeIPHostMask(String mask){
        Wizard changeIPHostAddressMaskWizard = Wizard.createByComponentId(driver, wait, POPUP_WIZARD_DATA_ATTRIBUTE_NAME);
        changeIPHostAddressMaskWizard.setComponentValue(MASK_DATA_ATTRIBUTE_NAME, mask, COMBOBOX);
        changeIPHostAddressMaskWizard.clickOK();
    }

    @Step("Delete objects with: {name}")
    public void deleteObject(String name) {
        waitForPageToLoad();
        selectTreeRowContains(name);
        useContextAction(DELETE_ACTION);
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
        useContextAction(ASSIGN_OPERATION_FOR_IPV4_SUBNET_WITH_ASSIGNMENT, ASSIGN_OPERATION_ACTION, DELETE_IPV4_SUBNET_ASSIGNMENT_ACTION);
        acceptConfirmationBox();
    }

    @Step("Delete IPv6 Subnet Assignment for Subnet with: {name}")
    public void deleteIPv6SubnetAssignment(String name) {
        waitForPageToLoad();
        selectTreeRowContains(name);
        useContextAction(ASSIGN_OPERATION_FOR_IPV6_SUBNET_WITH_ASSIGNMENT, ASSIGN_OPERATION_ACTION, DELETE_IPV6_SUBNET_ASSIGNMENT_ACTION);
        acceptConfirmationBox();
    }

    @Step("Delete IP Host with: {name}")
    public void deleteIPHost(String name) {
        waitForPageToLoad();
        selectTreeRowContains(name);
        useContextAction(DELETE_HOST_ADDRESS_ACTION);
        acceptConfirmationBox();
    }

    private void acceptConfirmationBox() {
        waitForPageToLoad();
        ConfirmationBoxInterface confirmationBox = ConfirmationBox.create(driver, wait);
        confirmationBox.clickButtonByLabel(OK_BUTTON_LABEL);
    }

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoadWithoutAppPreloader(driver, wait);
    }
}

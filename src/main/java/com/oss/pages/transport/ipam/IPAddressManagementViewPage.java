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

    private static final String CREATE_OPERATION_FOR_NETWORK_GROUP = "CreateOperationsForNetwork";
    private static final String EDIT_OPERATION_FOR_NETWORK_GROUP = "EditOperationsForNetwork";
    private static final String EDIT_OPERATION_FOR_IPV4_SUBNET_NETWORK_GROUP = "EditOperationsForIPv4SubnetNetwork";
    private static final String ASSIGN_OPERATION_FOR_IPV4_SUBNET_WITH_ASSIGNMENT = "AssignOperationsForIPv4SubnetWithAssignment";
    private static final String ASSIGN_OPERATION_FOR_IPV4_SUBNET_WITHOUT_ASSIGNMENT = "AssignOperationsForIPv4SubnetWithoutAssignment";
    private static final String CREATE_OPERATION_ACTION = "Create";
    private static final String EDIT_OPERATION_ACTION = "Edit";
    private static final String ASSIGN_OPERATION_ACTION = "Assign";
    private static final String CREATE_IP_NETWORK_ACTION = "CreateIPNetworkContextAction_1";
    private static final String EDIT_IP_NETWORK_ACTION = "EditIPNetworkContextAction";
    private static final String CREATE_IPV4_SUBNET_ACTION = "CreateIPv4SubnetContextAction_2";
    private static final String EDIT_IPV4_SUBNET_ACTION = "EditIPv4SubnetContextAction_24";
    private static final String CHANGE_SUBNET_TYPE_TO_BLOCK_ACTION = "ChangeIPv4SubnetTypeToBlockContextAction_26";
    private static final String ASSIGN_IPV4_SUBNET_ACTION = "AssignSubnetContextAction_8";
    private static final String EDIT_ROLE_OF_SUBNET_ASSIGNMENT_ACTION = "EditRoleOfSubnetAssignmentContextAction_12";
    private static final String DELETE_IPV4_SUBNET_ASSIGNMENT_ACTION = "RemoveSubnetAssignmentContextAction_11";
    private static final String DELETE_ACTION = "Delete";
    private static final String DELETE_IP_NETWORK_ACTION = "DeleteIPNetworkContextAction";
    private static final String DELETE_IPV4_SUBNET_TYPE_OF_BLOCK_ACTION = "DeleteIPv4SubnetBlockContextAction";
    private static final String DELETE_IPV4_SUBNET_TYPE_OF_NETWORK_ACTION = "DeleteIPv4SubnetNetworkContextAction";
    private static final String SPLIT_IPV4_SUBNET_ACTION = "SplitIPv4SubnetContextAction";
    private static final String MERGE_IPV4_SUBNET_ACTION = "MergeIPv4SubnetContextAction";
    //private static final String OTHER_BUTTON_GROUP_ID = "__more-group";
    private static final String ROLE_ACTION = "Role";

    private static final String OK_BUTTON_LABEL = "OK";
    private static final String TREE_VIEW_CLASS = "TreeView";
    private static final String TREE_VIEW_COMPONENT_CLASS = "TreeView";
    private static final String OSS_WINDOW_CLASS = "OssWindow";
    private static final String WINDOW_TOOLBAR_CLASS = "windowToolbar";
    private static final String TABS_CONTAINER_CLASS = "tabsContainer";
    private static final String NEW_ROLE_DATA_ATTRIBUTE_NAME = "new-role-uid";
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
        DelayUtils.sleep(500);
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
        useContextAction(CREATE_OPERATION_FOR_NETWORK_GROUP, CREATE_OPERATION_ACTION, CREATE_IP_NETWORK_ACTION);
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
        useContextAction(CREATE_OPERATION_FOR_NETWORK_GROUP, CREATE_OPERATION_ACTION, CREATE_IPV4_SUBNET_ACTION);
        return new IPSubnetWizardPage(driver);
    }

    @Step("Change IP Subnet: {rowName} type to Block")
    public void changeIPSubnetTypeToBlock(String rowName) {
        selectTreeRowContains(rowName);
        useContextAction(EDIT_OPERATION_FOR_IPV4_SUBNET_NETWORK_GROUP, EDIT_OPERATION_ACTION, CHANGE_SUBNET_TYPE_TO_BLOCK_ACTION);
        acceptConfirmationBox();
    }

    @Step("Edit IPv4 Subnet: {rowName} role to {role} and description to {description}")
    public void editIPv4Subnet(String rowName, String role, String description) {
        selectTreeRowContains(rowName);
        useContextAction(EDIT_OPERATION_FOR_IPV4_SUBNET_NETWORK_GROUP, EDIT_OPERATION_ACTION, EDIT_IPV4_SUBNET_ACTION);
        EditIPSubnetWizardPage editIPSubnetWizardPage = new EditIPSubnetWizardPage(driver);
        editIPSubnetWizardPage.editIPSubnet(role, description);
    }

    @Step("Split IPv4 Subnet: {rowName}")
    public IPSubnetWizardPage splitIPv4Subnet(String rowName) {
        selectTreeRowContains(rowName);
        useContextAction(SPLIT_IPV4_SUBNET_ACTION);
        return new IPSubnetWizardPage(driver);
    }

    @Step("Merge IPv4 Subnets")
    public IPSubnetWizardPage mergeIPv4Subnet(String ... rowName) {
        Arrays.stream(rowName).forEach(this::selectTreeRowContains);
        useContextAction(MERGE_IPV4_SUBNET_ACTION);
        return new IPSubnetWizardPage(driver);
    }

    @Step("Assign IP Subnet")
    public void assignIPSubnet(String rowName, String assignmentType, String assignmentName, String role) {
        selectTreeRowContains(rowName);
        useContextAction(ASSIGN_OPERATION_FOR_IPV4_SUBNET_WITHOUT_ASSIGNMENT, ASSIGN_OPERATION_ACTION, ASSIGN_IPV4_SUBNET_ACTION);
        AssignIPSubnetWizardPage assignIPSubnetWizardPage = new AssignIPSubnetWizardPage(driver);
        assignIPSubnetWizardPage.assignIPSubnet(assignmentType, assignmentName, role);
    }

    @Step("Edit role for subnet assignment")
    public void editRoleForSubnetAssignment(String rowName, String newRoleName) {
        selectTreeRowContains(rowName);
        useContextAction(ASSIGN_OPERATION_FOR_IPV4_SUBNET_WITH_ASSIGNMENT, ASSIGN_OPERATION_ACTION, EDIT_ROLE_OF_SUBNET_ASSIGNMENT_ACTION);
        Wizard editIPSubnetAssignmentRole = Wizard.createByComponentId(driver, wait, POPUP_WIZARD_DATA_ATTRIBUTE_NAME);
        editIPSubnetAssignmentRole.setComponentValue(NEW_ROLE_DATA_ATTRIBUTE_NAME, newRoleName, COMBOBOX);
        editIPSubnetAssignmentRole.clickOK();
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

    @Step("Delete IPv4 Subnet Assignment for Subnet with: {name}")
    public void deleteIPv4SubnetAssignment(String name) {
        waitForPageToLoad();
        selectTreeRowContains(name);
        useContextAction(ASSIGN_OPERATION_FOR_IPV4_SUBNET_WITH_ASSIGNMENT, ASSIGN_OPERATION_ACTION, DELETE_IPV4_SUBNET_ASSIGNMENT_ACTION);
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

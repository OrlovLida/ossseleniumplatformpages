package com.oss.pages.transport.ipam;

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

import static com.oss.framework.alerts.SystemMessageContainer.MessageType.SUCCESS;

import io.qameta.allure.Step;

import java.util.Arrays;

public class IPAddressManagementViewPage extends BasePage {
    private static final String IPADDRESS_MANAGEMENT_VIEW_URL = "%s/#/view/transport/ipmgt/ipTree?";
    private static final String PROJECT_ID = "project_id=%d";
    private static final String PERSPECTIVE = "perspective=%s";
    private static final String LIVE = "LIVE";
    private static final String PLAN = "PLAN";

    private static final String CREATE_IP_NETWORK_ACTION = "Create IP Network";
    private static final String EDIT_IP_NETWORK_ACTION = "Edit IP Network";
    private static final String CREATE_IPV4_SUBNET_ACTION = "Create IPv4 Subnet";
    private static final String EDIT_IPV4_SUBNET_ACTION = "Edit IPv4 Subnet";
    private static final String CREATE_OPERATION_FOR_NETWORK_GROUP = "CreateOperationsForNetwork";
    private static final String EDIT_OPERATION_FOR_NETWORK_GROUP = "EditOperationsForNetwork";
    private static final String EDIT_OPERATION_GROUP = "Edit";
    private static final String CHANGE_SUBNET_TYPE_TO_BLOCK = "Change Subnet Type to Block";
    private static final String EDIT_OPERATION_FOR_IPV4_SUBNET_NETWORK_GROUP = "EditOperationsForIPv4SubnetNetwork";
    private static final String DELETE_BUTTON_DATA_ATTRIBUTE_NAME = "Delete";
    private static final String SPLIT_IPV4_SUBNET_BUTTON_DATA_ATTRIBUTE_NAME = "Split IPv4 Subnet";
    private static final String MERGE_IPV4_SUBNET_BUTTON_DATA_ATTRIBUTE_NAME = "Merge IPv4 Subnet";
    private static final String OTHER_BUTTON_GROUP_ID = "__more-group";
    private static final String ROLE_ACTION_DATA_ATTRIBUTE_NAME = "Role";

    private static final String OK_BUTTON_LABEL = "OK";
    private static final String TREE_VIEW_CLASS = "TreeView";
    private static final String TREE_VIEW_COMPONENT_CLASS = "TreeView";
    private static final String OSS_WINDOW_CLASS = "OssWindow";
    private static final String WINDOW_TOOLBAR_CLASS = "windowToolbar";
    private static final String TABS_CONTAINER_CLASS = "tabsContainer";

    private TreeWidget mainTree;
    private OldActionsContainer actionsContainer;
    private PropertyPanelInterface propertyPanel;

    public static IPAddressManagementViewPage goToIPAddressManagementViewPageLive(WebDriver driver, String basicURL) {
        driver.get(String.format(IPADDRESS_MANAGEMENT_VIEW_URL + PERSPECTIVE, basicURL, LIVE));
        return new IPAddressManagementViewPage(driver);
    }

    public static IPAddressManagementViewPage goToIPAddressManagementViewPagePlan(WebDriver driver, String basicURL, long project) {
        driver.get(String.format(IPADDRESS_MANAGEMENT_VIEW_URL + PROJECT_ID + PERSPECTIVE, basicURL, project, PLAN));
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
        useContextAction(ROLE_ACTION_DATA_ATTRIBUTE_NAME);
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
    public void expandTreeRow(String name){
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
        useContextAction(CREATE_OPERATION_FOR_NETWORK_GROUP, CREATE_IP_NETWORK_ACTION);
        IPNetworkWizardPage ipNetworkWizardPage = new IPNetworkWizardPage(driver);
        ipNetworkWizardPage.createIPNetwork(networkName, description);
    }

    @Step("Edit IP Network with name: {networkName} to network with name: {networkNameUpdated} and description: {description}")
    public void editIPNetwork(String networkName, String networkNameUpdated, String description) {
        selectTreeRow(networkName);
        useContextAction(EDIT_OPERATION_FOR_NETWORK_GROUP, EDIT_IP_NETWORK_ACTION);
        IPNetworkWizardPage ipNetworkWizardPage = new IPNetworkWizardPage(driver);
        ipNetworkWizardPage.editIPNetwork(networkNameUpdated, description);
    }

    @Step("Create IPv4 Subnet")
    public IPSubnetWizardPage createIPv4Subnet() {
        useContextAction(CREATE_OPERATION_FOR_NETWORK_GROUP, CREATE_IPV4_SUBNET_ACTION);
        return new IPSubnetWizardPage(driver);
    }

    @Step("Change IP Subnet: {rowName} type to Block")
    public void changeIPSubnetTypeToBlock(String rowName) {
        selectTreeRowContains(rowName);
        useContextAction(OTHER_BUTTON_GROUP_ID, EDIT_OPERATION_GROUP, CHANGE_SUBNET_TYPE_TO_BLOCK);
        acceptConfirmationBox();
    }

    @Step("Edit IPv4 Subnet: {rowName} role to {role} and description to {description}")
    public void editIPv4Subnet(String rowName, String role, String description) {
        selectTreeRowContains(rowName);
        useContextAction(OTHER_BUTTON_GROUP_ID, EDIT_OPERATION_GROUP, EDIT_IPV4_SUBNET_ACTION);
        EditIPSubnetWizardPage editIPSubnetWizardPage = new EditIPSubnetWizardPage(driver);
        editIPSubnetWizardPage.editIPSubnet(role, description);
    }

    @Step("Split IPv4 Subnet: {rowName}")
    public IPSubnetWizardPage splitIPv4Subnet(String rowName) {
        selectTreeRowContains(rowName);
        useContextAction(SPLIT_IPV4_SUBNET_BUTTON_DATA_ATTRIBUTE_NAME);
        return new IPSubnetWizardPage(driver);
    }

    @Step("Merge IPv4 Subnets")
    public IPSubnetWizardPage mergeIPv4Subnet(String ... rowName) {
        Arrays.stream(rowName).forEach(this::selectTreeRowContains);
        useContextAction(MERGE_IPV4_SUBNET_BUTTON_DATA_ATTRIBUTE_NAME);
        return new IPSubnetWizardPage(driver);
    }

    @Step("Delete objects with: {name}")
    public void deleteObject(String name) {
        waitForPageToLoad();
        selectTreeRowContains(name);
        useContextAction(DELETE_BUTTON_DATA_ATTRIBUTE_NAME);
        acceptConfirmationBox();
    }

    private void acceptConfirmationBox() {
        waitForPageToLoad();
        ConfirmationBoxInterface confirmationBox = ConfirmationBox.create(driver, wait);
        confirmationBox.clickButtonByLabel(OK_BUTTON_LABEL);
    }

    private void waitForPageToLoad(){
        DelayUtils.waitForPageToLoadWithoutAppPreloader(driver, wait);
    }
}

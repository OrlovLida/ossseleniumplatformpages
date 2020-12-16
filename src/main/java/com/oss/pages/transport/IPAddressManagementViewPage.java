package com.oss.pages.transport;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;

import com.oss.framework.alerts.SystemMessageContainer;
import com.oss.framework.alerts.SystemMessageInterface;
import com.oss.framework.components.contextactions.OldActionsContainer;
import com.oss.framework.components.inputs.Button;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.listwidget.CommonList;
import com.oss.framework.prompts.ConfirmationBox;
import com.oss.framework.prompts.ConfirmationBoxInterface;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Widget;
import com.oss.framework.widgets.Wizard;
import com.oss.framework.widgets.propertypanel.OldPropertyPanel;
import com.oss.framework.widgets.propertypanel.PropertyPanelInterface;
import com.oss.framework.widgets.treewidget.TreeWidget;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

import java.util.Arrays;

public class IPAddressManagementViewPage extends BasePage {
    private static final String IPADDRESS_MANAGEMENT_VIEW_URL = "%s/#/view/transport/ipmgt/ipTree?";
    private static final String PROJECT_ID = "project_id=%d";
    private static final String PERSPECTIVE = "perspective=%s";
    private static final String LIVE = "LIVE";
    private static final String PLAN = "PLAN";
    private static final String ROLE_TEXT_FIELD_DATA_ATTRIBUTE = "text-field-uid";
    private static final String XPATH_TO_CONFIRM_DELETION = "//button[@class='actionButton btn btn-primary']";
    private static final String XPATH_TO_ROLES = ".//li[@class = 'listElement']";

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
    private static final String ROLE_BUTTON = "Role";

    private static final String OK_BUTTON_LABEL = "OK";
    private static final String TREE_VIEW_CLASS = "TreeView";
    private static final String TREE_VIEW_COMPONENT_CLASS = "TreeView";
    private static final String OSS_WINDOW_CLASS = "OssWindow";
    private static final String WINDOW_TOOLBAR_CLASS = "windowToolbar";
    private static final String TABS_CONTAINER_CLASS = "tabsContainer";
    private Wizard wizard;
    private CommonList commonList;

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

    @Step("Open Roles by clicking on Role button")
    public IPAddressManagementViewPage OpenRoleView() {
        clickOnRoleButton();
        return this;
    }

    @Step("Open new Roles by clicking on Create new role button")
    public IPAddressManagementViewPage OpenCreateNewRole() {
        clickCreateNewRole();
        return this;
    }

    @Step("Setting Role name")
    public IPAddressManagementViewPage SetNewRoleName() {
        setValueOnTextField(ROLE_TEXT_FIELD_DATA_ATTRIBUTE, Input.ComponentType.TEXT_FIELD, "CreateRoleSeleniumTest");
        return this;
    }

    @Step("Finish by clicking OK button")
    public IPAddressManagementViewPage AcceptRole() {
        clickAcceptNewRole();
        return this;
    }

    @Step("Exit Wizard")
    public IPAddressManagementViewPage ExitWizard() {
        clickOk();
        return this;
    }

    @Step("Click on Edit button next to EditRoleSeleniumTest2 role")
    public IPAddressManagementViewPage OpenEditRole() {
        DelayUtils.sleep(5000);
        commonList = null;
        getCommonList().clickOnEditButtonByListElementName("CreateRoleSeleniumTest");
        return this;
    }

    @Step("Clear text field and rename")
    public IPAddressManagementViewPage SetEditedRoleName() {
        DelayUtils.sleep();
        setValueOnTextField(ROLE_TEXT_FIELD_DATA_ATTRIBUTE, Input.ComponentType.TEXT_FIELD, "EditRoleSeleniumTest2");
        clickAcceptNewRole();
        return this;
    }

    @Step("Delete EditRoleSeleniumTest2")
    public IPAddressManagementViewPage DeleteRoleName() {
        commonList = null;
        getCommonList().clickOnDeleteButtonByListElementName("EditRoleSeleniumTest2");
        return this;
    }

    @Step("Exit Delete Window")
    public IPAddressManagementViewPage ExitDelete() {
        confirmDeletion();
        return this;
    }

    @Step("Search IP Network")
    public IPAddressManagementViewPage searchIpNetwork(String value) {
        getTreeView()
                .performSearchWithEnter(value);
        waitForPageToLoad();
        return this;
    }

    protected Wizard getWizard() {
        if (wizard == null) {
            wizard = Wizard.createWizard(driver, wait);
        }
        return wizard;
    }

    protected CommonList getCommonList() {
        if (commonList == null) {
            commonList = CommonList.create(driver, wait, "role-list-uid");
        }
        return commonList;
    }

    private void clickOnRoleButton() {
        DelayUtils.sleep(5000);
        Button.createBySelectorAndId(driver, "a", ROLE_BUTTON).click();
    }

    private void clickCreateNewRole() {
        DelayUtils.sleep();
        Button.createBySelectorAndId(driver, "a", "buttons-uid-0").click();
    }

    private void clickAcceptNewRole() {
        wizard = null;
        getWizard().clickActionById("wizard-submit-button-window-uid");
    }

    private void clickOk() {
        DelayUtils.sleep();
        wizard = null;
        getWizard().clickActionById("buttons-uid-1");

    }

    private void confirmDeletion() {
        DelayUtils.sleep();
        driver.findElement(By.xpath(XPATH_TO_CONFIRM_DELETION)).click();

    }

    private void setValueOnTextField(String componentId, Input.ComponentType componentType, String value) {
        DelayUtils.sleep();
        getWizard().getComponent(componentId, componentType).clearByAction();
        getWizard().getComponent(componentId, componentType).setSingleStringValue(value);
    }

    public int howManyRoles() {
        return driver.findElements(By.xpath(XPATH_TO_ROLES)).size();
    }

    @Step("Select first object on hierarchy view")
    public void selectFirstTreeRow() {
        waitForPageToLoad();
        getTreeView().selectFirstTreeRow();
    }

    @Step("Select object on hierarchy view")
    public void selectTreeRow(String name) {
        waitForPageToLoad();
        getTreeView().selectTreeRow(name);
    }

    @Step("Select object contains name on hierarchy view")
    public void selectTreeRowContains(String name) {
        waitForPageToLoad();
        getTreeView().selectTreeRowContains(name);
    }

    @Step("Expand object on hierarchy view")
    public void expandTreeRow(String name){
        waitForPageToLoad();
        getTreeView().expandTreeRow(name);
        waitForPageToLoad();
    }

    @Step("Expand object on hierarchy view")
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

    @Step("Accept confirmation box")
    public void acceptConfirmationBox() {
        waitForPageToLoad();
        ConfirmationBoxInterface confirmationBox = ConfirmationBox.create(driver, wait);
        confirmationBox.clickButtonByLabel(OK_BUTTON_LABEL);
    }

    @Step("Close system message")
    public void closeSystemMessage() {
        waitForPageToLoad();
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, wait);
        systemMessage.close();
    }

    public void createIPNetwork(String networkName, String description) {
        useContextAction(CREATE_OPERATION_FOR_NETWORK_GROUP, CREATE_IP_NETWORK_ACTION);
        IPNetworkWizardPage ipNetworkWizardPage = new IPNetworkWizardPage(driver);
        ipNetworkWizardPage.createIPNetwork(networkName, description);
        closeSystemMessage();
    }

    public void editIPNetwork(String networkName, String networkNameUpdated, String description) {
        selectTreeRow(networkName);
        useContextAction(EDIT_OPERATION_FOR_NETWORK_GROUP, EDIT_IP_NETWORK_ACTION);
        IPNetworkWizardPage ipNetworkWizardPage = new IPNetworkWizardPage(driver);
        ipNetworkWizardPage.editIPNetwork(networkNameUpdated, description);
    }

    public IPSubnetWizardPage createIPv4Subnet() {
        useContextAction(CREATE_OPERATION_FOR_NETWORK_GROUP, CREATE_IPV4_SUBNET_ACTION);
        return new IPSubnetWizardPage(driver);
    }

    public void changeIPSubnetTypeToBlock(String rowName) {
        selectTreeRowContains(rowName);
        useContextAction(OTHER_BUTTON_GROUP_ID, EDIT_OPERATION_GROUP, CHANGE_SUBNET_TYPE_TO_BLOCK);
        acceptConfirmationBox();
    }

    public void editIPv4Subnet(String rowName, String role, String description) {
        selectTreeRowContains(rowName);
        useContextAction(OTHER_BUTTON_GROUP_ID, EDIT_OPERATION_GROUP, EDIT_IPV4_SUBNET_ACTION);
        EditIPSubnetWizardPage editIPSubnetWizardPage = new EditIPSubnetWizardPage(driver);
        editIPSubnetWizardPage.editIPSubnet(role, description);
    }

    public IPSubnetWizardPage splitIPv4Subnet(String rowName) {
        selectTreeRowContains(rowName);
        useContextAction(SPLIT_IPV4_SUBNET_BUTTON_DATA_ATTRIBUTE_NAME);
        return new IPSubnetWizardPage(driver);
    }

    public IPSubnetWizardPage mergeIPv4Subnet(String ... rowName) {
        Arrays.stream(rowName).forEach(this::selectTreeRowContains);
        useContextAction(MERGE_IPV4_SUBNET_BUTTON_DATA_ATTRIBUTE_NAME);
        return new IPSubnetWizardPage(driver);
    }

    public void deleteObject(String name) {
        waitForPageToLoad();
        selectTreeRowContains(name);
        useContextAction(DELETE_BUTTON_DATA_ATTRIBUTE_NAME);
        acceptConfirmationBox();
        closeSystemMessage();
    }

    private void waitForPageToLoad(){
        DelayUtils.waitForPageToLoadWithoutAppPreloader(driver, wait);
    }
}

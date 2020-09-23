package com.oss.pages.transport;

import com.oss.framework.components.contextactions.OldActionsContainer;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.inputs.Button;
import com.oss.framework.listwidget.CommonList;
import com.oss.framework.prompts.ConfirmationBox;
import com.oss.framework.prompts.ConfirmationBoxInterface;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Widget;
import com.oss.framework.widgets.Wizard;
import com.oss.framework.widgets.propertypanel.OldPropertyPanel;
import com.oss.framework.widgets.propertypanel.PropertyPanelInterface;
import com.oss.framework.widgets.treewidget.TreeWidget;
import org.openqa.selenium.By;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

public class IPAddressManagementViewPage extends BasePage {

    public static IPAddressManagementViewPage goToIPAddressManagementViewPageLive(WebDriver driver, String basicURL) {
        driver.get(String.format("%s/#/view/transport/ipmgt/ipTree?" + "perspective=LIVE", basicURL));
        return new IPAddressManagementViewPage(driver);
    }

    public static IPAddressManagementViewPage goToIPAddressManagementViewPagePlan(WebDriver driver, String basicURL, long perspective) {
        driver.get(String.format("%s/#/view/transport/ipmgt/ipTree?" + perspective + "perspective=PLAN", basicURL));
        return new IPAddressManagementViewPage(driver);
    }

    public IPAddressManagementViewPage(WebDriver driver) {
        super(driver);
    }

    private static final String IPADDRESS_MANAGEMENT_VIEW_URL = "%s/#/view/transport/ipmgt/ipTree?";
    private static final String ROLE_TEXT_FIELD_DATA_ATTRIBUTE = "text-field-uid";
    private static final String XPATH_TO_CONFIRM_DELETION = "//button[@class='actionButton btn btn-primary']";
    private static final String XPATH_TO_ROLES = ".//li[@class = 'listElement']";
    private Wizard wizard;
    private CommonList commonList;

    private TreeWidget mainTree;
    private OldActionsContainer actionsContainer;
    private PropertyPanelInterface propertyPanel;

    private TreeWidget getTreeView() {
        if (mainTree == null) {
            Widget.waitForWidget(wait, "TreeView");
            mainTree = TreeWidget.createByClass(driver, "TreeView", wait);
            DelayUtils.waitForVisibility(wait, driver.findElement(By.className("TreeViewComponent")));
        }
        DelayUtils.waitForVisibility(wait, driver.findElement(By.className("TreeViewComponent")));
        return mainTree;
    }

    private OldActionsContainer getActionsInterface() {
        if (actionsContainer == null) {
            DelayUtils.waitForVisibility(wait, driver.findElement(By.className("OssWindow")));
            actionsContainer = OldActionsContainer.createFromParent(driver, wait, driver.findElement(By.className("OssWindow")));
            DelayUtils.waitForVisibility(wait, driver.findElement(By.className("windowToolbar")));
        }
        DelayUtils.waitForVisibility(wait, driver.findElement(By.className("windowToolbar")));
        return actionsContainer;
    }

    public PropertyPanelInterface getPropertyPanel(){
        if (propertyPanel == null) {
            DelayUtils.waitForVisibility(wait, driver.findElement(By.className("OssWindow")));
            propertyPanel = OldPropertyPanel.create(driver, wait);
            DelayUtils.waitForVisibility(wait, driver.findElement(By.className("tabsContainer")));
        }
        DelayUtils.waitForVisibility(wait, driver.findElement(By.className("tabsContainer")));
        return propertyPanel;
    }

    @Step("Open ip Address Management")
    public static IPAddressManagementViewPage goToIPAddressManagementPage(WebDriver driver, String basicURL){
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
        getCommonList().getEditButtonByListElementName("CreateRoleSeleniumTest").click();
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
        getCommonList().getDeleteButtonByListElementName("EditRoleSeleniumTest2").click();
        return this;
    }

    @Step("Exit Delete Window")
    public IPAddressManagementViewPage ExitDelete() {
        confirmDeletion();
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

    private void clickOnRoleButton(){
        DelayUtils.sleep(5000);
        Button.createBySelectorAndId(driver, "a", "Role").click();
    }

    private void clickCreateNewRole(){
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

    private void setValueOnTextField (String componentId, Input.ComponentType componentType, String value){
        DelayUtils.sleep();
        getWizard().getComponent(componentId, componentType).clearByAction();
        getWizard().getComponent(componentId, componentType).setSingleStringValue(value);
    }

    public int howManyRoles() {
        return driver.findElements(By.xpath(XPATH_TO_ROLES)).size();
    }


    @Step("Select first object on hierarchy view")
    public void selectFirstTreeRow() {
        DelayUtils.waitForPageToLoadWithoutAppPreloader(driver, wait);
        getTreeView().selectFirstTreeRow();
    }

    @Step("Select object on hierarchy view")
    public void selectTreeRow(String name) {
        DelayUtils.waitForPageToLoadWithoutAppPreloader(driver, wait);
        getTreeView().selectTreeRow(name);
    }

    @Step("Select object contains name on hierarchy view")
    public void selectTreeRowContains(String name) {
        DelayUtils.waitForPageToLoadWithoutAppPreloader(driver, wait);
        getTreeView().selectTreeRowContains(name);
    }

    @Step("Expand object on hierarchy view")
    public void expandTreeRow(String name) {
        DelayUtils.waitForPageToLoadWithoutAppPreloader(driver, wait);
        getTreeView().expandTreeRow(name);
        DelayUtils.waitForPageToLoadWithoutAppPreloader(driver, wait);
    }

    @Step("Expand object on hierarchy view")
    public void expandTreeRowContains(String name) {
        DelayUtils.waitForPageToLoadWithoutAppPreloader(driver, wait);
        getTreeView().expandTreeRowContains(name);
        DelayUtils.waitForPageToLoadWithoutAppPreloader(driver, wait);
    }

    @Step("Use context action")
    public void useContextAction(String groupId, String actionDataAttributeName) {
        DelayUtils.waitForPageToLoadWithoutAppPreloader(driver, wait);
        getActionsInterface().callActionById(groupId, actionDataAttributeName);
    }

    @Step("Use context action")
    public void useContextAction(String groupId, String innerGroupDataAttributeName, String actionDataAttributeName) {
        DelayUtils.waitForPageToLoadWithoutAppPreloader(driver, wait);
        getActionsInterface().callActionById(groupId, innerGroupDataAttributeName, actionDataAttributeName);
    }

    @Step("Use button")
    public void useButton(String buttonId) {
        DelayUtils.waitForPageToLoadWithoutAppPreloader(driver, wait);
        Button button = Button.create(driver, buttonId, "a");
        button.click();
    }

    @Step("Accept confirmation box")
    public void acceptConfirmationBox() {
        DelayUtils.waitForPageToLoadWithoutAppPreloader(driver, wait);
        ConfirmationBoxInterface confirmationBox = ConfirmationBox.create(driver, wait);
        confirmationBox.clickButtonByLabel("OK");
    }
}

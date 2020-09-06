package com.oss.pages.transport;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.inputs.Button;
import com.oss.framework.listwidget.CommonList;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import org.openqa.selenium.By;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

public class IPAddressManagementViewPage extends BasePage {

    public IPAddressManagementViewPage(WebDriver driver) {
        super(driver);
    }

    private static final String IPADDRESS_MANAGEMENT_VIEW_URL = "%s/#/view/transport/ipmgt/ipTree?";
    private static final String ROLE_TEXT_FIELD_DATA_ATTRIBUTE = "text-field-uid";
    private static final String XPATH_TO_CONFIRM_DELETION = "//button[@class='actionButton btn btn-primary']";
    private static final String XPATH_TO_ROLES = ".//li[@class = 'listElement']";
    private Wizard wizard;
    private CommonList commonList;

    @Step("Open ip Address Management")
    public static IPAddressManagementViewPage goToIPAddressManagementPage(WebDriver driver, String basicURL){
        driver.get(String.format(IPADDRESS_MANAGEMENT_VIEW_URL, basicURL));
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
}

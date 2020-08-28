package com.oss.pages.transport;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.inputs.Button;
import com.oss.framework.components.inputs.TextField;
import com.oss.framework.data.Data;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.Keys;

public class IPAddressManagementViewPage extends BasePage {

    public IPAddressManagementViewPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(xpath = XPATH_TO_ROLE_BUTTON)
    private WebElement RoleButton;
    @FindBy(xpath = XPATH_TO_CREATE_NEW_ROLE_BUTTON)
    private WebElement CreateNewRoleButton;
    @FindBy(xpath = XPATH_TO_ROLE_INPUT)
    private WebElement NewRoleInput;
    @FindBy(xpath = XPATH_TO_ACCEPT_NEW_ROLE_BUTTON)
    private WebElement AcceptNewRoleButton;
    @FindBy(xpath = XPATH_TO_EDIT_ROLE)
    private WebElement EditRoleButton;
    @FindBy(xpath = XPATH_TO_WIZARD_OK_BUTTON)
    private WebElement OkButton;
    @FindBy(xpath = XPATH_TO_CLEAR_TEXT_FIELD)
    private WebElement ClearTextField;
    @FindBy(xpath = XPATH_TO_DELETE_ROLE)
    private WebElement DeleteRoleButton;
    @FindBy(xpath = XPATH_TO_CONFIRM_DELETION)
    private WebElement DeleteRoleOk;

    private static final String IPADDRESS_MANAGEMENT_VIEW_URL = "%s/#/view/transport/ipmgt/ipTree?";
    private static final String ROLE_BUTTON_DATA_ATTRIBUTE = "Role";
    private static final String XPATH_TO_ROLE_BUTTON = "//a[@data-attributename='Role']";
    private static final String XPATH_TO_CREATE_NEW_ROLE_BUTTON = "//a[@data-attributename='buttons-uid-0']";
    private static final String XPATH_TO_ROLE_INPUT = "//div[@data-attributename='text-field-uid']";
    private static final String ROLE_TEXT_FIELD_DATA_ATTRIBUTE = "text-field-uid";
    private static final String XPATH_TO_ACCEPT_NEW_ROLE_BUTTON = "//button[@data-attributename='wizard-submit-button-window-uid']";
    private static final String XPATH_TO_CLEAR_TEXT_FIELD = "//input[@class='form-control']";
    private static final String ROLE_WIZARD_OK = "buttons-uid-1";
    private static final String XPATH_TO_WIZARD_OK_BUTTON = "//a[@data-attributename='buttons-uid-1']";
    private static final String XPATH_TO_EDIT_ROLE = "//*[@class='listElement' and contains(string(), 'CreateRoleSeleniumTest')]//button[@class='squareButton btn btn-sm btn-success']";
    private static final String XPATH_TO_DELETE_ROLE = "//*[@class='listElement' and contains(string(), 'EditRoleSeleniumTest2')]//button[@class='squareButton btn btn-sm btn-danger']";
    private static final String XPATH_TO_CONFIRM_DELETION = "//button[@class='actionButton btn btn-primary']";
    private Wizard wizard;

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
    public IPAddressManagementViewPage SetNewRoleName(String name) {
        DelayUtils.waitForVisibility(wait, NewRoleInput);
        setValueOnTextField(ROLE_TEXT_FIELD_DATA_ATTRIBUTE, Data.createSingleData(name));
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
        clickEditRole();
        return this;
    }

    @Step("Clear text field and rename")
    public IPAddressManagementViewPage SetEditedRoleName(String name) {
        DelayUtils.waitForVisibility(wait, NewRoleInput);
        setValueOnTextFieldTest(ROLE_TEXT_FIELD_DATA_ATTRIBUTE, Data.createSingleData(name));
        DelayUtils.sleep(1000);
        clickAcceptNewRole();
        return this;
    }

    @Step("Delete EditRoleSeleniumTest2")
    public IPAddressManagementViewPage DeleteRoleName() {
        clickDeleteRole();
        confirmDeletion();
        return this;
    }

    public Wizard getWizard() {
        if (wizard == null) {
            wizard = Wizard.createWizard(driver, wait);
        }
        return wizard;
    }

    private void clickOnRoleButton(){
        DelayUtils.waitForVisibility(wait,RoleButton);
        Button.createBySelectorAndId(driver, "a", ROLE_BUTTON_DATA_ATTRIBUTE).click(); //TEMP
    }

    private void clickCreateNewRole(){
        DelayUtils.waitForVisibility(wait,CreateNewRoleButton);
        Button.createBySelectorAndId(driver, "a", "buttons-uid-0").click();
    }

    private void clickAcceptNewRole() {
        DelayUtils.waitForVisibility(wait, AcceptNewRoleButton);
        DelayUtils.sleep(1000);
        Button.createById(driver, "wizard-submit-button-window-uid").click();
    }

    private void clickOk() {
        DelayUtils.waitForVisibility(wait, OkButton);
        Button.createBySelectorAndId(driver, "a", ROLE_WIZARD_OK).click();

    }

    private void clickEditRole() {
        DelayUtils.waitForVisibility(wait, EditRoleButton);
        driver.findElement(By.xpath(XPATH_TO_EDIT_ROLE)).click();
    }

    private void clickDeleteRole() {
        DelayUtils.waitForVisibility(wait, DeleteRoleButton);
        driver.findElement(By.xpath(XPATH_TO_DELETE_ROLE)).click();
    }

    private void confirmDeletion() {
        DelayUtils.waitForVisibility(wait, DeleteRoleOk);
        driver.findElement(By.xpath(XPATH_TO_CONFIRM_DELETION)).click();
    }

    protected void setValueOnTextField (String TEXT_FIELD_ID, Data value){
        TextField textField = (TextField) getComponent(TEXT_FIELD_ID, Input.ComponentType.TEXT_FIELD);
        textField.clear();
        textField.setValue(value);
    }

    protected void setValueOnTextFieldTest (String TEXT_FIELD_ID, Data value){
        TextField textField = (TextField) getComponent(TEXT_FIELD_ID, Input.ComponentType.TEXT_FIELD);
        textField.clear();
        ClearTextField.sendKeys(Keys.chord(Keys.CONTROL,"a", Keys.DELETE));
        textField.setValue(value);
    }

    private Input getComponent(String componentId, Input.ComponentType componentType) {
        return getWizard().getComponent(componentId, componentType);
    }

}

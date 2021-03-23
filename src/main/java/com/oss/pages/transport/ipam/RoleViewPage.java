package com.oss.pages.transport.ipam;

import com.oss.framework.components.contextactions.ButtonContainer;
import com.oss.framework.listwidget.CommonList;
import com.oss.framework.prompts.ConfirmationBox;
import com.oss.framework.prompts.ConfirmationBoxInterface;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

import static com.oss.framework.components.inputs.Input.ComponentType.TEXT_FIELD;

public class RoleViewPage extends BasePage {
    private static final String ROLE_TEXT_FIELD_DATA_ATTRIBUTE_NAME = "text-field-uid";
    private static final String ROLE_COMMON_LIST_DATA_ATTRIBUTE_NAME = "list-uid";
    private static final String OK_BUTTON = "ConfirmationBox_removeBoxId_action_button";
    private static final String CREATE_NEW_ROLE_BUTTON_DATA_ATTRIBUTE_NAME = "buttons-uid-0";
    private static final String OK_BUTTON_DATA_ATTRIBUTE_NAME = "buttons-uid-1";
    private Wizard wizard;
    private CommonList commonList;
    private ButtonContainer buttonContainer;

    public RoleViewPage(WebDriver driver){
        super(driver);
    }

    private ButtonContainer getButtonContainer() {
        if (buttonContainer == null) {
            buttonContainer = ButtonContainer.create(driver, wait);
        }
        return buttonContainer;
    }

    protected Wizard getWizard() {
        if (wizard == null) {
            wizard = Wizard.createWizard(driver, wait);
        }
        return wizard;
    }

    private CommonList getCommonList() {
        if (commonList == null) {
            commonList = CommonList.create(driver, wait, ROLE_COMMON_LIST_DATA_ATTRIBUTE_NAME);
        }
        return commonList;
    }

    @Step("Create new role {roleName}")
    public void createRole(String roleName) {
        getButtonContainer().callActionById(CREATE_NEW_ROLE_BUTTON_DATA_ATTRIBUTE_NAME);
        setValueInRoleWizard(roleName);
    }

    @Step("Edit role name: {roleNameBeforeUpdate} to role name: {roleNameAfterUpdate}")
    public void editRole(String roleNameBeforeUpdate, String roleNameAfterUpdate) {
        getCommonList().clickOnEditButtonByListElementName(roleNameBeforeUpdate);
        setValueInRoleWizard(roleNameAfterUpdate);
    }

    @Step("Delete {roleName} role")
    public void deleteRole(String roleName) {
        getCommonList().clickOnDeleteButtonByListElementName(roleName);
        acceptConfirmationBox();
    }

    @Step("Check if role: {roleName} exist")
    public boolean doesRoleNameExist(String roleName) {
        return getCommonList().isListElementVisible(roleName);
    }

    @Step("Exit Role View")
    public void exitRoleView() {
        getButtonContainer().callActionById(OK_BUTTON_DATA_ATTRIBUTE_NAME);
    }

    private void setValueInRoleWizard(String roleName){
        getWizard().setComponentValue(ROLE_TEXT_FIELD_DATA_ATTRIBUTE_NAME, roleName, TEXT_FIELD);
        getWizard().clickAccept();
    }

    private void acceptConfirmationBox() {
        DelayUtils.waitForPageToLoad(driver, wait);
        ConfirmationBoxInterface confirmationBox = ConfirmationBox.create(driver, wait);
        confirmationBox.clickButtonByDataAttributeName(OK_BUTTON);
    }
}
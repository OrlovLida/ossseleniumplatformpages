package com.oss.pages.transport.ipam;

import org.openqa.selenium.WebDriver;

import com.oss.framework.widgets.list.CommonList;
import com.oss.framework.components.prompts.ConfirmationBox;
import com.oss.framework.components.prompts.ConfirmationBoxInterface;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

import static com.oss.framework.components.inputs.Input.ComponentType.TEXT_FIELD;

public class RoleViewPage extends BasePage {
    private static final String ROLE_TEXT_FIELD_DATA_ATTRIBUTE_NAME = "text-field-uid";
    private static final String ROLE_COMMON_LIST_DATA_ATTRIBUTE_NAME = "list-uid";
    private static final String OK_BUTTON = "ConfirmationBox_removeBoxId_action_button";
    private static final String CREATE_NEW_ROLE_BUTTON_DATA_ATTRIBUTE_NAME = "buttons-uid-0";
    private static final String OK_BUTTON_DATA_ATTRIBUTE_NAME = "buttons-uid-1";
    private static final String ROLE_WIZARD_ID = "role-list-uid";
    private static final String CREATE_WIZARD_ID = "create-uid";
    private static final String EDIT_WIZARD_ID = "edit-uid";
    private static final String ATTRIBUTE_NAME = "Role";
    private static final String EDIT_ACTION_ID = "Edit";
    private static final String DELETE_ACTION_ID = "Delete";
    private CommonList commonList;

    public RoleViewPage(WebDriver driver) {
        super(driver);
    }

    @Step("Create new role {roleName}")
    public void createRole(String roleName) {
        getRoleWizard().clickButtonById(CREATE_NEW_ROLE_BUTTON_DATA_ATTRIBUTE_NAME);
        setValueInRoleWizard(CREATE_WIZARD_ID, roleName);
    }

    @Step("Edit role name: {roleNameBeforeUpdate} to role name: {roleNameAfterUpdate}")
    public void editRole(String roleNameBeforeUpdate, String roleNameAfterUpdate) {
        getCommonList().getRow(ATTRIBUTE_NAME, roleNameBeforeUpdate).callAction(EDIT_ACTION_ID);
        setValueInRoleWizard(EDIT_WIZARD_ID, roleNameAfterUpdate);
    }

    @Step("Delete {roleName} role")
    public void deleteRole(String roleName) {
        getCommonList().getRow(ATTRIBUTE_NAME, roleName).callAction(DELETE_ACTION_ID);
        acceptConfirmationBox();
    }

    @Step("Check if role: {roleName} exist")
    public boolean doesRoleNameExist(String roleName) {
        return getCommonList().isRowDisplayed(ATTRIBUTE_NAME, roleName);
    }

    @Step("Exit Role View")
    public void exitRoleView() {
        getRoleWizard().clickButtonById(OK_BUTTON_DATA_ATTRIBUTE_NAME);
    }

    private Wizard getRoleWizard() {
        return Wizard.createByComponentId(driver, wait, ROLE_WIZARD_ID);
    }

    private CommonList getCommonList() {
        if (commonList == null) {
            commonList = CommonList.create(driver, wait, ROLE_COMMON_LIST_DATA_ATTRIBUTE_NAME);
        }
        return commonList;
    }

    private void setValueInRoleWizard(String wizardId, String roleName) {
        Wizard wizard = Wizard.createByComponentId(driver, wait, wizardId);
        wizard.setComponentValue(ROLE_TEXT_FIELD_DATA_ATTRIBUTE_NAME, roleName, TEXT_FIELD);
        wizard.clickAccept();
    }

    private void acceptConfirmationBox() {
        DelayUtils.waitForPageToLoad(driver, wait);
        ConfirmationBoxInterface confirmationBox = ConfirmationBox.create(driver, wait);
        confirmationBox.clickButtonById(OK_BUTTON);
    }
}
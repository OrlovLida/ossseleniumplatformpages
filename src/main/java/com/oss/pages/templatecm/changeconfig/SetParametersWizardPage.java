package com.oss.pages.templatecm.changeconfig;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class SetParametersWizardPage extends BasePage {
    private static final String FILL_BUTTON = "ParameterFormSubmitButtonApp-3";
    private static final String WIZARD_ID = "changeConfigurationParameterPrompt";
    private static final String NEW_INVENTORY_OBJECT_PARAMETERS = "dataSourceParameterItem-header";
    private static final String DEPLOY_SERVER_SETTINGS = "deployServerParameterItem-header";
    private static final String USER_PARAMETERS = "userParameterItem-header";
    private static final String NAME = "$name[NEW_INVENTORY]";
    private static final String PASSWORD = "$Password[SYSTEM]";
    private static final String INTERFACE_NAME = "$InterfaceName[USER]";

    private Wizard setParametersWizard = Wizard.createByComponentId(driver, wait, WIZARD_ID);

    public SetParametersWizardPage(WebDriver driver) {
        super(driver);
    }

    @Step("Set password")
    public void setPassword(String value) {
        rolloutList(DEPLOY_SERVER_SETTINGS);
        setParametersWizard.setComponentValue(PASSWORD, value, Input.ComponentType.TEXT_FIELD);
    }

    @Step("Set interface name")
    public void setInterfaceName(String value) {
        rolloutList(USER_PARAMETERS);
        setParametersWizard.setComponentValue(INTERFACE_NAME, value, Input.ComponentType.TEXT_FIELD);
    }

    @Step("Get name")
    public String getName() {
        rolloutList(NEW_INVENTORY_OBJECT_PARAMETERS);
        return setParametersWizard.getComponent(NAME, Input.ComponentType.TEXT_FIELD).getStringValue().trim();
    }

    @Step("Click fill parameters")
    public void clickFillParameters() {
        DelayUtils.waitForPageToLoad(driver, wait);
        setParametersWizard.clickButtonById(FILL_BUTTON);
    }

    private void rolloutList(String id) {
        DelayUtils.waitForPageToLoad(driver, wait);
        setParametersWizard.rolloutById(id);
    }
}

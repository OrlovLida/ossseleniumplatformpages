package com.oss.pages.mediation;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class CLIConfigurationWizardPage extends BasePage {

    private static final String IP_HOST_ADDRESS_FIELD = "connection-configuration-managed-address";
    private static final String ADDRESS = "connection-configuration-manual-address";
    private static final String PORT = "connection-configuration-port";
    private static final String NAME = "name-id";
    private static final String COMMAND_TIMEOUT_FIELD = "command-timeout";
    private static final String CONNECTION_SETUP_TIMEOUT = "connection-setup-timeout";
    private static final String AUTH_PASSWORD = "cli-password";
    private static final String INPUT_METHOD = "address-radio-button";
    private static final String CLI_PROTOCOL = "cli-protocol";
    private static final String AUTH_METHOD = "authentication-method-radio-button";
    private static final String WIZARD_ID = "cli-window-id";

    private final Wizard wizard;

    public CLIConfigurationWizardPage(WebDriver driver) {
        super(driver);
        wizard = Wizard.createByComponentId(driver, wait, WIZARD_ID);
    }

    @Step("Set input method in connection parameters to {inputMethod}")
    public void setInputMethod(String inputMethod) {
        wizard.setComponentValue(INPUT_METHOD, inputMethod);
        waitForPageToLoad();
    }

    @Step("Set name to {name}")
    public void setName(String name) {
        wizard.setComponentValue(NAME, name);
        waitForPageToLoad();
    }

    @Step("Set CLI Protocol in CLI Parameters to {cliProtocol}")
    public void setCLIProtocol(String cliProtocol) {
        wizard.setComponentValue(CLI_PROTOCOL, cliProtocol);
        waitForPageToLoad();
    }

    @Step("Set Authentication Method in Authentication Method to {authMethod}")
    public void setAuthMethod(String authMethod) {
        wizard.setComponentValue(AUTH_METHOD, authMethod);
        waitForPageToLoad();
    }

    @Step("Set IP Host Address to {ipHostAddress}")
    public void setIPHostAddress(String ipHostAddress) {
        Input ipAddressComponent = wizard.getComponent(IP_HOST_ADDRESS_FIELD);
        ipAddressComponent.clear();
        ipAddressComponent.setSingleStringValueContains(ipHostAddress);
        waitForPageToLoad();
    }

    @Step("Set Port to {port}")
    public void setPort(String port) {
        wizard.setComponentValue(PORT, port);
        waitForPageToLoad();
    }

    @Step("Set Address to {address}")
    public void setAddress(String address) {
        wizard.setComponentValue(ADDRESS, address);
        waitForPageToLoad();
    }

    @Step("Set Command Timeout in CLI configuration parameters to {commandTimeout}")
    public void setCommandTimeout(String commandTimeout) {
        wizard.setComponentValue(COMMAND_TIMEOUT_FIELD, commandTimeout);
        waitForPageToLoad();
    }

    @Step("Set Connection Setup Timeout in CLI configuration parameters to {connectionSetupTimeout}")
    public void setConnectionSetupTimeout(String connectionSetupTimeout) {
        wizard.setComponentValue(CONNECTION_SETUP_TIMEOUT, connectionSetupTimeout);
        waitForPageToLoad();
    }

    @Step("Set Password in authentication method configuration to {authPassword}")
    public void setAuthPassword(String authPassword) {
        wizard.setComponentValue(AUTH_PASSWORD, authPassword);
        waitForPageToLoad();
    }

    @Step("Click Accept button")
    public void clickAccept() {
        wizard.clickAccept();
    }

    @Step("Click Next button")
    public void clickNextStep() {
        wizard.clickNext();
        waitForPageToLoad();
    }

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, wait);
    }

}

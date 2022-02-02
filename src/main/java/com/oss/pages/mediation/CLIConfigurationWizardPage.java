package com.oss.pages.mediation;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.data.Data;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class CLIConfigurationWizardPage extends BasePage {

    private static final String IP_HOST_ADDRESS_FIELD = "connection-configuration-managed-address";
    private static final String ADDRESS = "connection-configuration-manual-address";
    private static final String PORT = "connection-configuration-port";
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

    @Step("Set input method in connection parameters")
    public void setInputMethod(String inputMethod) {
        Input inputMethodComponent = wizard.getComponent(INPUT_METHOD, Input.ComponentType.RADIO_BUTTON);
        inputMethodComponent.setValue(Data.createSingleData(inputMethod));
    }

    @Step("Set CLI Protocol in CLI Parameters")
    public void setCLIProtocol(String cliProtocol) {
        Input cliProtocolComponent = wizard.getComponent(CLI_PROTOCOL, Input.ComponentType.RADIO_BUTTON);
        cliProtocolComponent.setValue(Data.createSingleData(cliProtocol));
    }

    @Step("Set Authentication Method in Authentication Method")
    public void setAuthMethod(String authMethod) {
        Input authMethodComponent = wizard.getComponent(AUTH_METHOD, Input.ComponentType.RADIO_BUTTON);
        authMethodComponent.setValue(Data.createSingleData(authMethod));
    }

    @Step("Set IP Host Address")
    public void setIPHostAddress(String ipHostAddress) {
        Input ipAddressComponent = wizard.getComponent(IP_HOST_ADDRESS_FIELD, Input.ComponentType.SEARCH_FIELD);
        ipAddressComponent.clear();
        ipAddressComponent.setSingleStringValue(ipHostAddress);
    }

    @Step("Set Port")
    public void setPort(String port) {
        Input portComponent = wizard.getComponent(PORT, Input.ComponentType.TEXT_FIELD);
        portComponent.clear();
        portComponent.setSingleStringValue(port);
    }

    @Step("Set Address")
    public void setAddress(String address) {
        Input addressComponent = wizard.getComponent(ADDRESS, Input.ComponentType.TEXT_FIELD);
        addressComponent.clear();
        addressComponent.setSingleStringValue(address);
    }

    @Step("Set Command Timeout in CLI configuration parameters")
    public void setCommandTimeout(String commandTimeout) {
        Input commandTimeoutComponent = wizard.getComponent(COMMAND_TIMEOUT_FIELD, Input.ComponentType.TEXT_FIELD);
        commandTimeoutComponent.clear();
        commandTimeoutComponent.setSingleStringValue(commandTimeout);
    }

    @Step("Set Connection Setup Timeout in CLI configuration parameters")
    public void setConnectionSetupTimeout(String connectionSetupTimeout) {
        Input connectionSetupTimeoutComponent = wizard.getComponent(CONNECTION_SETUP_TIMEOUT, Input.ComponentType.TEXT_FIELD);
        connectionSetupTimeoutComponent.clear();
        connectionSetupTimeoutComponent.setSingleStringValue(connectionSetupTimeout);
    }

    @Step("Set Password in authentication method configuration")
    public void setAuthPassword(String authPassword) {
        Input authPasswordComponent = wizard.getComponent(AUTH_PASSWORD, Input.ComponentType.TEXT_FIELD);
        authPasswordComponent.clear();
        authPasswordComponent.setSingleStringValue(authPassword);
    }

    @Step("Click Accept button")
    public void clickAccept() {
        wizard.clickAccept();
    }

    @Step("Click Next Step button")
    public void clickNextStep() {
        wizard.clickNextStep();
    }

}

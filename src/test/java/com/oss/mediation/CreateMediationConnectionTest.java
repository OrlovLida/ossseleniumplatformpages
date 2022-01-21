package com.oss.mediation;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.alerts.SystemMessageInterface;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.navigation.sidemenu.SideMenu;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.mediation.CLIConfigurationWizardPage;
import com.oss.pages.mediation.ViewConnectionConfigurationPage;

import io.qameta.allure.Description;

public class CreateMediationConnectionTest extends BaseTestCase {

    private String address = "10.10.20.13";
    private String port = "13";
    private String password = "cisco";
    private String commandTimeout = "20";
    private String connectionTimeout = "20";

    @BeforeClass
    @Description("Open CLI Configuration Wizard")
    public void openCLIConfigurationWizard() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        SideMenu sideMenu = SideMenu.create(driver, webDriverWait);
        sideMenu.callActionByLabel("Create CLI configuration", "Wizards", "Mediation");
    }

    @Test(priority = 1)
    @Description("Set Connection Parameters")
    public void startMediationConfiguration() {
        CLIConfigurationWizardPage cliConfigurationWizardPage = new CLIConfigurationWizardPage(driver);
        cliConfigurationWizardPage.setInputMethod("Manual address input");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cliConfigurationWizardPage.setAddress(address);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cliConfigurationWizardPage.setPort(port);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cliConfigurationWizardPage.clickNextStep();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 2)
    @Description("Set CLI Parameters")
    public void setCLIParameters() {
        CLIConfigurationWizardPage cliConfigurationWizardPage = new CLIConfigurationWizardPage(driver);
        cliConfigurationWizardPage.setCommandTimeout(commandTimeout);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cliConfigurationWizardPage.setConnectionSetupTimeout(connectionTimeout);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cliConfigurationWizardPage.setCLIProtocol("SSH");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cliConfigurationWizardPage.clickNextStep();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 3)
    @Description("Set Authentication method")
    public void setAuthenticationMethod() {
        CLIConfigurationWizardPage cliConfigurationWizardPage = new CLIConfigurationWizardPage(driver);
        cliConfigurationWizardPage.setAuthMethod("Password Authentication");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cliConfigurationWizardPage.setAuthPassword(password);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cliConfigurationWizardPage.clickNextStep();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 4)
    @Description("Check and accept summary")
    public void acceptSummary() {
        CLIConfigurationWizardPage cliConfigurationWizardPage = new CLIConfigurationWizardPage(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cliConfigurationWizardPage.clickAccept();
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assertions.assertThat(messages).hasSize(1);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getMessageType())
                .isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
    }

    @Test(priority = 5)
    @Description("Show mediation in Connection Configuration")
    public void showMediationFromPopup() {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        systemMessage.clickMessageLink();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 6)
    @Description("Delete mediation")
    public void deleteMediation() {
        ViewConnectionConfigurationPage viewConnectionConfigurationPage = new ViewConnectionConfigurationPage(driver);
        viewConnectionConfigurationPage.selectRow("Address", address);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        viewConnectionConfigurationPage.useContextAction(ActionsContainer.EDIT_GROUP_ID, ViewConnectionConfigurationPage.DELETE_BUTTON_ID);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        viewConnectionConfigurationPage.clickDelete();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }
}

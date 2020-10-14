package com.oss.mediation;

import com.oss.BaseTestCase;
import com.oss.framework.alerts.SystemMessageContainer;
import com.oss.framework.alerts.SystemMessageInterface;
import com.oss.framework.sidemenu.SideMenu;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.mediation.CLIConfigurationWizardPage;
import com.oss.pages.transport.NetworkViewPage;
import io.qameta.allure.Description;
import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.util.List;

public class MediationTest extends BaseTestCase {

    private String deviceModel = "Cisco Systems Inc. CISCO1941/K9";
    private String deviceName = "SeleniumMediationTestDevice";
    private String address = "10.10.20.11";
    private String port = "22";
    private String password = "cisco";
    private String commandTimeout = "20";
    private String connectionTimeout = "20";

    @BeforeClass
    @Description("Open Lab Network View")
    public void openNetworkView() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        SideMenu sideMenu = SideMenu.create(driver, webDriverWait);
        sideMenu.callActionByLabel("LAB Network View", "Favourites", "SeleniumTests");
    }

    @Test(priority = 1)
    @Description("Select location Poznan-BU1")
    public void selectLocation() {
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.expandDockedPanel("left");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.selectObjectInViewContent("Name", "Poznan-BU1");
    }

    @Test(priority = 2)
    @Description("Create physical device in location Poznan-BU1")
    public void createPhysicalDevice() {
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        networkViewPage.useContextAction("CREATE", "Create Device");
        networkViewPage.setModel(deviceModel);
        DelayUtils.sleep(1000);
        networkViewPage.setName(deviceName);
        networkViewPage.setHostname(deviceName);
        DelayUtils.sleep(1000);
        networkViewPage.create();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assertions.assertThat(messages).hasSize(1);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getMessageType())
                .isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
    }

    @Test(priority = 3)
    @Description("Open Mediation Configuration with physical device from previous step")
    public void moveMediationConfiguration() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        networkViewPage.expandDockedPanel("left");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.useContextAction("CREATE", "Create Mediation Configuration");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.clickProceed();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 4)
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

    @Test(priority = 5)
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

    @Test(priority = 6)
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

    @Test(priority = 7)
    @Description("Check and accept summary")
    public void acceptSummary() {
        CLIConfigurationWizardPage cliConfigurationWizardPage = new CLIConfigurationWizardPage(driver);
        cliConfigurationWizardPage.clickAccept();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assertions.assertThat(messages).hasSize(1);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getMessageType())
                .isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
    }
}

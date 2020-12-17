package com.oss.pages.transport;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import com.oss.pages.platform.NewInventoryViewPage;

import io.qameta.allure.Step;

public class IRBInterfaceWizardPage extends BasePage {

    private String IRB_DEVICE_ID = "irb-main-device";
    private String IRB_VLANID_ID = "irb-main-vlanid";
    private String IPADDRESS_ID = "uid-ipaddress";
    private String IPSUBNET_ID = "uid-ip-subnet";
    private String IRB_MTU_ID = "irb-main-mtu";

    public static IRBInterfaceWizardPage goToIRBInterfaceWizardPage(WebDriver driver, String basicURL) {
        driver.get(String.format("%s/#/view/transport/ip/ethernet/irb-interface?" + "perspective=LIVE", basicURL));
        return new IRBInterfaceWizardPage(driver);
    }

    public IRBInterfaceWizardPage(WebDriver driver) {
        super(driver);
    }

    private Wizard wizard;

    private Wizard getWizard() {
        if (wizard == null) {
            wizard = Wizard.createWizard(driver, wait);
        }
        return wizard;
    }

    @Step("Create IRB Interface with device name {device} and VlanID {vlanId}")
    public IRBInterfaceWizardPage createIRBInterface(String device, String vlanId) {
        getWizard().setComponentValue(IRB_DEVICE_ID, device, Input.ComponentType.SEARCH_FIELD);
        waitForPageToLoad();
        getWizard().setComponentValue(IRB_VLANID_ID, vlanId, Input.ComponentType.TEXT_FIELD);
        waitForPageToLoad();
        getWizard().clickAccept();
        return this;
    }

    @Step("Assign IP address {ipAddress} and Subnet {subnet} to IRB Interface")
    public IRBInterfaceWizardPage assignIPtoIRBInterface(String ipAddress, String subnet) {
        NewInventoryViewPage newInventoryViewPage = new NewInventoryViewPage(driver);
        waitForPageToLoad();
        newInventoryViewPage.editTextFields(IPADDRESS_ID, Input.ComponentType.TEXT_FIELD, ipAddress);
        newInventoryViewPage.editTextFields(IPSUBNET_ID, Input.ComponentType.SEARCH_FIELD, subnet);
        waitForPageToLoad();
        getWizard().clickButtonByLabel("Next Step");
        waitForPageToLoad();
        getWizard().clickAccept();
        return this;
    }

    @Step("Edit IRB Interface and set MTU {mtu}")
    public IRBInterfaceWizardPage editIRBInterface(String mtu) {
        waitForPageToLoad();
        getWizard().setComponentValue(IRB_MTU_ID, mtu, Input.ComponentType.TEXT_FIELD);
        getWizard().clickAccept();
        return this;
    }

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, wait);
    }
}

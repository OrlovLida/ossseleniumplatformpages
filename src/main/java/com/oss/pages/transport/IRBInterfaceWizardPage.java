package com.oss.pages.transport;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class IRBInterfaceWizardPage extends BasePage {

    private static final String IRB_DEVICE_ID = "irb-main-device";
    private static final String IRB_VLANID_ID = "irb-main-vlanid";
    private static final String IRB_MTU_ID = "irb-main-mtu";
    private static final String IRB_DESCRIPTION_ID = "irb-main-description";
    private static final String WIZARD_ID = "Popup";

    public IRBInterfaceWizardPage(WebDriver driver) {
        super(driver);
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

    @Step("Edit IRB Interface and set MTU {mtu} and description {description}")
    public IRBInterfaceWizardPage editIRBInterface(String mtu, String description) {
        waitForPageToLoad();
        getWizard().setComponentValue(IRB_MTU_ID, mtu, Input.ComponentType.TEXT_FIELD);
        waitForPageToLoad();
        getWizard().setComponentValue(IRB_DESCRIPTION_ID, description, Input.ComponentType.TEXT_FIELD);
        waitForPageToLoad();
        getWizard().clickAccept();
        return this;
    }

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    private Wizard getWizard() {
        return Wizard.createByComponentId(driver, wait, WIZARD_ID);
    }
}

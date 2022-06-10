package com.oss.pages.transport;

import org.openqa.selenium.WebDriver;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class IRBInterfaceWizardPage extends BasePage {
    private static final String IRB_VLANID_ID = "irb-main-vlanid";
    private static final String IRB_MTU_ID = "irb-main-mtu";
    private static final String IRB_DESCRIPTION_ID = "irb-main-description";
    private static final String WIZARD_ID = "irbInterfaceWizard";
    private static final String EDIT_WIZARD_ID = "irbInterfaceEditWizard";

    public IRBInterfaceWizardPage(WebDriver driver) {
        super(driver);
    }

    @Step("Create IRB Interface with VlanID {vlanId}")
    public IRBInterfaceWizardPage createIRBInterface(String vlanId) {
        waitForPageToLoad();
        getWizard().setComponentValue(IRB_VLANID_ID, vlanId);
        waitForPageToLoad();
        getWizard().clickAccept();
        return this;
    }

    @Step("Edit IRB Interface and set MTU {mtu} and description {description}")
    public IRBInterfaceWizardPage editIRBInterface(String mtu, String description) {
        waitForPageToLoad();
        getEditWizard().setComponentValue(IRB_MTU_ID, mtu);
        waitForPageToLoad();
        getEditWizard().setComponentValue(IRB_DESCRIPTION_ID, description);
        waitForPageToLoad();
        getEditWizard().clickAccept();
        return this;
    }

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    private Wizard getWizard() {
        return Wizard.createByComponentId(driver, wait, WIZARD_ID);
    }

    private Wizard getEditWizard() {
        return Wizard.createByComponentId(driver, wait, EDIT_WIZARD_ID);
    }
}

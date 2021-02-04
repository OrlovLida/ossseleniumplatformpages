package com.oss.pages.transport;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.inputs.Input.ComponentType;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.CommonHierarchyApp;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

import static com.oss.framework.components.inputs.Input.ComponentType.NUMBER_FIELD;
import static com.oss.framework.components.inputs.Input.ComponentType.TEXT_FIELD;

public class VLANInterfaceWizardPage extends BasePage {

    public VLANInterfaceWizardPage(WebDriver driver) {
        super(driver);
    }

    private static final String TYPE_ID = "typeSelectionApp";
    private static final String SUBINTERFACE_ID = "subinterface-id-uid";
    private static final String WIZARD_ID = "vlanInterfaceWizard";
    private static final String ACCEPT_ID = "vlanInterfaceWizardApp-finish";
    private static final String NEXT_ID = "vlanInterfaceWizardApp-next";
    private static final String IRB_MTU_ID = "mtu-uid";
    private static final String IRB_DESCRIPTION_ID = "description-uid";

    @Step("Set VLAN Interface type to {type}")
    public void setType(String type) {
        getWizard().setComponentValue(TYPE_ID, type, ComponentType.COMBOBOX);
    }

    @Step("Set Subinterface ID to {subinterfaceId}")
    public void setSubinterfaceId(String subinterfaceId) {
        getWizard().setComponentValue(SUBINTERFACE_ID, subinterfaceId, NUMBER_FIELD);
    }

    @Step("Set interface to {paths}")
    public void setInterface(String... paths) {
        CommonHierarchyApp commonHierarchyApp = CommonHierarchyApp.createByClass(driver,wait);
        commonHierarchyApp.navigateToPath(paths);
    }

    @Step("Next")
    public void clickNext() {
        getWizard().clickActionById(NEXT_ID);
    }

    @Step("Accept")
    public void clickAccept() {
        getWizard().clickActionById(ACCEPT_ID);
    }

    @Step("Edit VLAN Interface and set MTU {mtu} and description {description}")
    public void editVLANInterface(String mtu, String description) {
        waitForPageToLoad();
        getWizard().setComponentValue(IRB_MTU_ID, mtu, NUMBER_FIELD);
        waitForPageToLoad();
        getWizard().setComponentValue(IRB_DESCRIPTION_ID, description, TEXT_FIELD);
        waitForPageToLoad();
        getWizard().clickAccept();
    }

    private Wizard getWizard() {
        return Wizard.createByComponentId(driver, wait, WIZARD_ID);
    }

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, wait);
    }
}

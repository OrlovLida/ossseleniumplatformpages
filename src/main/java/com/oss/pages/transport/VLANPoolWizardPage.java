package com.oss.pages.transport;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.inputs.Input.ComponentType;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.commonhierarchy.CommonHierarchyApp;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class VLANPoolWizardPage extends BasePage {

    private static final String WIZARD_ID = "vlanPoolWizard";
    private static final String NAME_ID = "name-uid";
    private static final String DESCRIPTION_ID = "description-uid";
    private static final String VLAN_RANGE_ID = "vlan-range-uid";
    private static final String NEXT_STEP_ID = "vlanPoolWizardApp-next";
    private static final String ACCEPT_ID = "vlanPoolWizardApp-finish";
    private static final String WIDGET_ID = "CommonHierarchyApp-parent-selection-hierarchy-app-uid";

    public VLANPoolWizardPage(WebDriver driver) {
        super(driver);
    }

    @Step("Set Name to {name}")
    public void setName(String name) {
        getWizard().setComponentValue(NAME_ID, name);
    }

    @Step("Set Description to {description}")
    public void setDescription(String description) {
        getWizard().setComponentValue(DESCRIPTION_ID, description);
    }

    @Step("Set VLAN Range to {vlanRange}")
    public void setVlanRange(String vlanRange) {
        getWizard().setComponentValue(VLAN_RANGE_ID, vlanRange);
    }

    @Step("Click next step")
    public void clickNextStep() {
        getWizard().clickButtonById(NEXT_STEP_ID);
    }

    @Step("Navigate through Common Hierarchy App widget selecting {resourceName} and names of interfaces")
    public void selectLocationAndDevice(String locationName, String deviceName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        CommonHierarchyApp commonHierarchyApp = CommonHierarchyApp.create(driver, wait, WIDGET_ID);
        commonHierarchyApp.navigateToPath(locationName, deviceName);
    }

    @Step("Click accept")
    public void clickAccept() {
        getWizard().clickButtonById(ACCEPT_ID);
    }

    private Wizard getWizard() {
        return Wizard.createByComponentId(driver, wait, WIZARD_ID);
    }
}

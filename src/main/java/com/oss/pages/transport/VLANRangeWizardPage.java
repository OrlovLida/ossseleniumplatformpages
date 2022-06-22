package com.oss.pages.transport;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class VLANRangeWizardPage extends BasePage {

    public VLANRangeWizardPage(WebDriver driver) {
        super(driver);
    }

    private static final String WIZARD_ID = "VLANRangeWizardTemplateId";
    private static final String NAME_ATTRIBUTE_ID = "VLAN_RANGE_NAME_ATTRIBUTE_ID";
    private static final String RANGE_ATTRIBUTE_ID = "VLAN_RANGE_VLAN_ID_RANGE_ATTRIBUTE_ID";
    private static final String DESCRIPTION_ATTRIBUTE_ID = "VLAN_RANGE_DESCRIPTION_ATTRIBUTE_ID";

    @Step("Set Name to {name}")
    public VLANRangeWizardPage setName(String name) {
        getWizard().setComponentValue(NAME_ATTRIBUTE_ID, name);
        return this;
    }

    @Step("Set Range to {range}")
    public VLANRangeWizardPage setRange(String range) {
        getWizard().setComponentValue(RANGE_ATTRIBUTE_ID, range);
        return this;
    }

    @Step("Set Description to {description}")
    public VLANRangeWizardPage setDescription(String description) {
        getWizard().setComponentValue(DESCRIPTION_ATTRIBUTE_ID, description);
        return this;
    }

    @Step("Save")
    public VLANRangeWizardPage save() {
        getWizard().clickSave();
        return this;
    }

    private Wizard getWizard() {
        return Wizard.createByComponentId(driver, wait, WIZARD_ID);
    }

}

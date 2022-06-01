package com.oss.pages.transport.regulatoryLicense;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class RegulatoryLicenseAssignmentWizardPage extends BasePage {
    private static final String OBJECT_TYPE_FIELD_ID = "assign-regulatory-license-assignment-type-field";

    private static final String COMPONENT_ID = "assign-regulatory-license-view_prompt-card";

    public RegulatoryLicenseAssignmentWizardPage(WebDriver driver) {
        super(driver);
    }

    @Step("Set object type to {objectType}")
    public void setObjectType(String objectType) {
        getWizard().setComponentValue(OBJECT_TYPE_FIELD_ID, objectType, Input.ComponentType.COMBOBOX);
    }

    @Step("Set object name to {objectName}")
    public void setObjectName(String objectName, String objectNameFieldId){
        getWizard().setComponentValue(objectNameFieldId, objectName, Input.ComponentType.OBJECT_SEARCH_FIELD);
    }

    @Step("Click accept button")
    public RegulatoryLicenseAssignmentWizardPage clickAccept() {
        getWizard().clickAccept();
        DelayUtils.waitForPageToLoad(driver, wait);
        return new RegulatoryLicenseAssignmentWizardPage(driver);
    }

    private Wizard getWizard() {
        return Wizard.createByComponentId(driver, wait, COMPONENT_ID);
    }
}

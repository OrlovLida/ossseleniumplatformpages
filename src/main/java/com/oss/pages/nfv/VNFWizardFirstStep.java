package com.oss.pages.nfv;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.wizard.Wizard;

import static com.oss.framework.components.inputs.Input.ComponentType.CHECKBOX;
import static com.oss.framework.components.inputs.Input.ComponentType.TEXT_FIELD;

public class VNFWizardFirstStep extends VNFWizardStep {

    private static final String NAME_COMPONENT_ID = "attribute-name-comp-idstep1ID";
    private static final String RELATION_CONFIGURATION_COMPONENT_ID = "step1-checkbox-relation-component-id";
    private static final String LEFT_COMPONENT_GROUP_ID = "step1-left-component-group-id-header";

    private VNFWizardFirstStep(WebDriver driver, WebDriverWait wait, Wizard vnfWizard) {
        super(driver, wait,vnfWizard);
    }

    public static final VNFWizardFirstStep create(WebDriver driver, WebDriverWait wait, Wizard vnfWizard) {
        return new VNFWizardFirstStep(driver, wait, vnfWizard);
    }

    public boolean isStructureTreeVisible() {
        return this.vnfWizard.isElementPresentById(LEFT_COMPONENT_GROUP_ID);
    }

    public void setName(String name) {
        vnfWizard.setComponentValue(NAME_COMPONENT_ID, name, TEXT_FIELD);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public String getName() {
        return vnfWizard.getComponent(NAME_COMPONENT_ID, TEXT_FIELD).getStringValue();
    }

    public void toggleRelationConfiguration() {
        boolean selected = isRelationConfigurationSelected();
        vnfWizard.setComponentValue(RELATION_CONFIGURATION_COMPONENT_ID, String.valueOf(!selected), CHECKBOX);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    private boolean isRelationConfigurationSelected() {
        return vnfWizard.getComponent(RELATION_CONFIGURATION_COMPONENT_ID, CHECKBOX).getStringValue().equalsIgnoreCase(Boolean.TRUE.toString());
    }


}

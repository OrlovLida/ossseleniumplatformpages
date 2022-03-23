package com.oss.pages.nfv.networkservice;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.wizard.Wizard;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import static com.oss.framework.components.inputs.Input.ComponentType.CHECKBOX;
import static com.oss.framework.components.inputs.Input.ComponentType.TEXT_FIELD;

public class NetworkServiceWizardFirstStep extends NetworkServiceWizardStep {

    private static final String NAME_COMPONENT_ID = "attribute-name-comp-idstep1ID";
    private static final String RELATION_CONFIGURATION_COMPONENT_ID = "step1-checkbox-relation-component-id";
    private static final String LEFT_COMPONENT_GROUP_ID = "step1-left-component-group-id-header";

    private NetworkServiceWizardFirstStep(WebDriver driver, WebDriverWait wait, Wizard vnfWizard) {
        super(driver, wait,vnfWizard);
    }

    public static final NetworkServiceWizardFirstStep create(WebDriver driver, WebDriverWait wait, Wizard vnfWizard) {
        return new NetworkServiceWizardFirstStep(driver, wait, vnfWizard);
    }

    public boolean isStructureTreeVisible() {
        return this.networkServiceWizard.isElementPresentById(LEFT_COMPONENT_GROUP_ID);
    }

    public void setName(String name) {
        networkServiceWizard.setComponentValue(NAME_COMPONENT_ID, name, TEXT_FIELD);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public String getName() {
        return networkServiceWizard.getComponent(NAME_COMPONENT_ID, TEXT_FIELD).getStringValue();
    }

    public void toggleRelationConfiguration() {
        boolean selected = isRelationConfigurationSelected();
        networkServiceWizard.setComponentValue(RELATION_CONFIGURATION_COMPONENT_ID, String.valueOf(!selected), CHECKBOX);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    private boolean isRelationConfigurationSelected() {
        return networkServiceWizard.getComponent(RELATION_CONFIGURATION_COMPONENT_ID, CHECKBOX).getStringValue().equalsIgnoreCase(Boolean.TRUE.toString());
    }

}

package com.oss.pages.transport.ipam;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

public class EditIPSubnetWizardPage extends BasePage {
    private static final String EDIT_IPSUBNET_WIZARD_DATA_ATTRIBUTE_NAME = "editSubnetFormId";
    private static final String MASK_ATTRIBUTE_ID = "mask-uid";
    private static final String SUBNET_TYPE_ATTRIBUTE_ID = "subnet-field-uid";
    private static final String ROLE_ATTRIBUTE_ID = "role-field-uid";
    private static final String DESCRIPTION_ATTRIBUTE_ID = "description-field-uid";
    private static final String SAVE_BUTTON_ID = "wizard-submit-button-window-uid";

    EditIPSubnetWizardPage(WebDriver driver) {
        super(driver);
    }

    public void editIPSubnet(String maskLength, String role, String description) {
        Wizard editIPSubnet = createWizard();
        setSubnetMask(editIPSubnet, maskLength);
        setRole(editIPSubnet, role);
        setDescription(editIPSubnet, description);
        editIPSubnet.clickButtonById(SAVE_BUTTON_ID);
    }

    public void editIPSubnet(String role, String description) {
        Wizard editIPSubnet = createWizard();
        setRole(editIPSubnet, role);
        setDescription(editIPSubnet, description);
        editIPSubnet.clickButtonById(SAVE_BUTTON_ID);
    }

    private void setSubnetMask(Wizard editIPSubnet, String mask) {
        Input componentNetworkName = editIPSubnet.getComponent(MASK_ATTRIBUTE_ID, Input.ComponentType.COMBOBOX);
        componentNetworkName.clearByAction();
        componentNetworkName.setSingleStringValue(mask);
    }

    private void setSubnetType(Wizard editIPSubnet, String subnetType) {
        Input componentDescription = editIPSubnet.getComponent(SUBNET_TYPE_ATTRIBUTE_ID, Input.ComponentType.COMBOBOX);
        componentDescription.clear();
        componentDescription.setSingleStringValue(subnetType);
    }

    private void setRole(Wizard editIPSubnet, String role) {
        Input componentDescription = editIPSubnet.getComponent(ROLE_ATTRIBUTE_ID, Input.ComponentType.SEARCH_FIELD);
        componentDescription.clear();
        componentDescription.setSingleStringValue(role);
    }

    private void setDescription(Wizard editIPSubnet, String description) {
        Input componentDescription = editIPSubnet.getComponent(DESCRIPTION_ATTRIBUTE_ID, Input.ComponentType.TEXT_FIELD);
        componentDescription.clear();
        componentDescription.setSingleStringValue(description);
    }

    private Wizard createWizard() {
        return Wizard.createByComponentId(driver, wait, EDIT_IPSUBNET_WIZARD_DATA_ATTRIBUTE_NAME);
    }
}

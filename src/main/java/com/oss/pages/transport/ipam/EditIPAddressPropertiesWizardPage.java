package com.oss.pages.transport.ipam;

import org.openqa.selenium.WebDriver;

import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;
import com.oss.pages.transport.ipam.helper.IPAddressAssignmentWizardProperties;

class EditIPAddressPropertiesWizardPage extends BasePage {
    private static final String EDIT_WIZARD_ID = "editIpHostAddressAssignmentWizardId";
    private static final String IS_PRIMARY_COMPONENT_ID = "editIpHostAddressAssignmentIsPrimaryComponentId";
    private static final String IS_IN_NAT_COMPONENT_ID = "editIpHostAddressAssignmentIsInNatComponentId";
    private static final String ROLE_COMPONENT_ID = "editIpHostAddressAssignmentRoleComponentId";
    private static final String DESCRIPTION_COMPONENT_ID = "editIpHostAddressAssignmentDescriptionComponentId";

    EditIPAddressPropertiesWizardPage(WebDriver driver) {
        super(driver);
    }

    void editIPAddressProperties(IPAddressAssignmentWizardProperties ipAddressAssignmentWizardProperties) {
        Wizard editIPAddress = createWizard();
        ipAddressAssignmentWizardProperties.isPrimary().ifPresent(isPrimary -> editIPAddress.setComponentValue(IS_PRIMARY_COMPONENT_ID, isPrimary));
        ipAddressAssignmentWizardProperties.isInNAT().ifPresent(isInNAT -> editIPAddress.setComponentValue(IS_IN_NAT_COMPONENT_ID, isInNAT));
        ipAddressAssignmentWizardProperties.getRole().ifPresent(role -> editIPAddress.setComponentValue(ROLE_COMPONENT_ID, role));
        ipAddressAssignmentWizardProperties.getDescription().ifPresent(description -> editIPAddress.setComponentValue(DESCRIPTION_COMPONENT_ID, description));
        editIPAddress.clickAccept();
    }

    private Wizard createWizard() {
        return Wizard.createByComponentId(driver, wait, EDIT_WIZARD_ID);
    }
}

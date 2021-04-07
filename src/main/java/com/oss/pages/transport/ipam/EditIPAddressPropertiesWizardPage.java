package com.oss.pages.transport.ipam;

import static com.oss.framework.components.inputs.Input.ComponentType.*;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import com.oss.pages.transport.ipam.helper.IPAddressAssignmentWizardProperties;
import org.openqa.selenium.WebDriver;

public class EditIPAddressPropertiesWizardPage extends BasePage {
    private static final String EDIT_IP_ADDRESS_WIZARD_DATA_ATTRIBUTE_NAME = "Popup";
    private static final String IS_PRIMARY_COMPONENT_ID = "isPrimaryFieldUid";
    private static final String IS_IN_NAT_COMPONENT_ID = "isInNatFieldUid";
    private static final String ROLE_COMPONENT_ID = "addressUsageFieldUid";
    private static final String DESCRIPTION_COMPONENT_ID = "descriptionFieldUid";

    public EditIPAddressPropertiesWizardPage(WebDriver driver) {
        super(driver);
    }

    public void editIPAddressProperties(IPAddressAssignmentWizardProperties ipAddressAssignmentWizardProperties){
        Wizard editIPAddress = createWizard();
        ipAddressAssignmentWizardProperties.isPrimary().ifPresent(isPrimary -> editIPAddress.setComponentValue(IS_PRIMARY_COMPONENT_ID, isPrimary, CHECKBOX));
        ipAddressAssignmentWizardProperties.isInNAT().ifPresent(isInNAT -> editIPAddress.setComponentValue(IS_IN_NAT_COMPONENT_ID, isInNAT, CHECKBOX));
        ipAddressAssignmentWizardProperties.getRole().ifPresent(role -> editIPAddress.setComponentValue(ROLE_COMPONENT_ID, role, SEARCH_FIELD));
        ipAddressAssignmentWizardProperties.getDescription().ifPresent(description -> editIPAddress.setComponentValue(DESCRIPTION_COMPONENT_ID, description, TEXT_AREA));
        editIPAddress.clickOK();
    }

    private Wizard createWizard(){
        return Wizard.createByComponentId(driver, wait, EDIT_IP_ADDRESS_WIZARD_DATA_ATTRIBUTE_NAME);
    }
}

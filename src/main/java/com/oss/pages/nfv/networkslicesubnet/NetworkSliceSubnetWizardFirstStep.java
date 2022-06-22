package com.oss.pages.nfv.networkslicesubnet;

import com.oss.framework.wizard.Wizard;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import static com.oss.framework.components.inputs.Input.ComponentType.COMBOBOX;
import static com.oss.framework.components.inputs.Input.ComponentType.TEXT_FIELD;

public class NetworkSliceSubnetWizardFirstStep extends NetworkSliceSubnetWizardStep {
    private static final String NAME_COMPONENT_ID = "attribute-networkSliceSubnetNameTextFieldnetworkSliceSubnetStep1Id";
    private static final String DESCRIPTION_COMPONENT_ID = "attribute-networkSliceSubnetDescriptionTextFieldnetworkSliceSubnetStep1Id";
    private static final String OPERATIONAL_STATE_COMPONENT_ID = "##NFVSeleniumTestRANSliceSubnet-operationalStatenetworkSliceSubnetStep1Id";
    private static final String ADMINISTRATIVE_STATE_COMPONENT_ID = "##NFVSeleniumTestRANSliceSubnet-administrativeStatenetworkSliceSubnetStep1Id";

    private NetworkSliceSubnetWizardFirstStep(WebDriver driver, WebDriverWait wait, Wizard networkSliceWizard) {
        super(driver, wait, networkSliceWizard);
    }

    public static NetworkSliceSubnetWizardFirstStep create(WebDriver driver, WebDriverWait wait, Wizard networkSliceWizard) {
        return new NetworkSliceSubnetWizardFirstStep(driver, wait, networkSliceWizard);
    }

    public void setName(String name) {
        networkSliceSubnetWizard.setComponentValue(NAME_COMPONENT_ID, name);
    }

    public String getName() {
        return networkSliceSubnetWizard.getComponent(NAME_COMPONENT_ID).getStringValue();
    }

    public void setDescription(String description) {
        networkSliceSubnetWizard.setComponentValue(DESCRIPTION_COMPONENT_ID, description);
    }

    public String getDescription() {
        return networkSliceSubnetWizard.getComponent(DESCRIPTION_COMPONENT_ID).getStringValue();
    }

    public void setAdministrativeState(String state) {
        networkSliceSubnetWizard.setComponentValue(ADMINISTRATIVE_STATE_COMPONENT_ID, state);
    }

    public String getAdministrativeState() {
        return networkSliceSubnetWizard.getComponent(ADMINISTRATIVE_STATE_COMPONENT_ID).getStringValue();
    }

    public void setOperationalState(String state) {
        networkSliceSubnetWizard.setComponentValue(OPERATIONAL_STATE_COMPONENT_ID, state);
    }

    public String getOperationalState() {
        return networkSliceSubnetWizard.getComponent(OPERATIONAL_STATE_COMPONENT_ID).getStringValue();
    }

}

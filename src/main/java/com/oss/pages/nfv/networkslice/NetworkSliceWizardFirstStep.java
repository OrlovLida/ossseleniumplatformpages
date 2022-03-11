package com.oss.pages.nfv.networkslice;

import com.oss.framework.wizard.Wizard;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import static com.oss.framework.components.inputs.Input.ComponentType.COMBOBOX;
import static com.oss.framework.components.inputs.Input.ComponentType.TEXT_FIELD;

public class NetworkSliceWizardFirstStep extends NetworkSliceWizardStep {
    private static final String NAME_COMPONENT_ID = "attribute-networkSliceNameTextFieldnetworkSliceStep1Id";
    private static final String DESCRIPTION_COMPONENT_ID = "attribute-networkSliceDescriptionTextFieldnetworkSliceStep1Id";
    private static final String OPERATIONAL_STATE_COMPONENT_ID = "##NetworkSlice-operationalStatenetworkSliceStep1Id";

    private NetworkSliceWizardFirstStep(WebDriver driver, WebDriverWait wait, Wizard networkSliceWizard) {
        super(driver, wait, networkSliceWizard);
    }

    public static final NetworkSliceWizardFirstStep create(WebDriver driver, WebDriverWait wait, Wizard networkSliceWizard) {
        return new NetworkSliceWizardFirstStep(driver, wait, networkSliceWizard);
    }

    public void setName(String name) {
        networkSliceWizard.setComponentValue(NAME_COMPONENT_ID, name, TEXT_FIELD);
    }

    public String getName() {
        return networkSliceWizard.getComponent(NAME_COMPONENT_ID, TEXT_FIELD).getStringValue();
    }

    public void setDescription(String description) {
        networkSliceWizard.setComponentValue(DESCRIPTION_COMPONENT_ID, description, TEXT_FIELD);
    }

    public String getDescription() {
        return networkSliceWizard.getComponent(DESCRIPTION_COMPONENT_ID, TEXT_FIELD).getStringValue();
    }

    public void setOperationalState(String state) {
        networkSliceWizard.setComponentValue(OPERATIONAL_STATE_COMPONENT_ID, state, COMBOBOX);
    }

    public String getOperationalState() {
        return networkSliceWizard.getComponent(OPERATIONAL_STATE_COMPONENT_ID, COMBOBOX).getStringValue();
    }

}

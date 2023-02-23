package com.oss.pages.transport.ipam;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

/**
 * @author Ewa Frączek
 */

public class IPNetworkWizardPage extends BasePage {
    private static final String IPNETWORK_WIZARD_DATA_ATTRIBUTE_NAME = "ADD_IPNETWORK_FORM_ID_prompt-card";
    private static final String ADD_IPNETWORK_NAME_ATTRIBUTE_ID = "ADD_IPNETWORK_NAME_ATTRIBUTE_ID";
    private static final String ADD_IPNETWORK_DESCRIPTION_ATTRIBUTE_ID = "ADD_IPNETWORK_DESCRIPTION_ATTRIBUTE_ID";
    private static final String ADD_IPNETWORK_BUTTON_PANEL_ID = "ADD_IPNETWORK_BUTTON_PANEL_ID-1";

    public IPNetworkWizardPage(WebDriver driver) {
        super(driver);
    }

    public void createIPNetwork(String networkName, String description) {
        Wizard createIPNetwork = createWizard();
        setNetworkName(createIPNetwork, networkName);
        setDescription(createIPNetwork, description);
        createIPNetwork.clickButtonById(ADD_IPNETWORK_BUTTON_PANEL_ID);
        createIPNetwork.waitToClose();
    }

    public void editIPNetwork(String networkName, String description) {
        Wizard editIPNetwork = createWizard();
        setNetworkName(editIPNetwork, networkName);
        setDescription(editIPNetwork, description);
        editIPNetwork.clickButtonById(ADD_IPNETWORK_BUTTON_PANEL_ID);
    }

    private void setNetworkName(Wizard createIPNetwork, String networkName) {
        Input componentNetworkName = createIPNetwork.getComponent(ADD_IPNETWORK_NAME_ATTRIBUTE_ID, Input.ComponentType.TEXT_FIELD);
        componentNetworkName.clearByAction();
        componentNetworkName.setSingleStringValue(networkName);
    }

    private void setDescription(Wizard createIPNetwork, String description) {
        Input componentDescription = createIPNetwork.getComponent(ADD_IPNETWORK_DESCRIPTION_ATTRIBUTE_ID, Input.ComponentType.TEXT_AREA);
        componentDescription.clear();
        componentDescription.setSingleStringValue(description);
    }

    private Wizard createWizard() {
        return Wizard.createByComponentId(driver, wait, IPNETWORK_WIZARD_DATA_ATTRIBUTE_NAME);
    }
}

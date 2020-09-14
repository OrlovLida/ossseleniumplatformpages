package com.oss.pages.transport;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import org.openqa.selenium.WebDriver;

/**
 * @author Ewa Frączek
 */

public class IPNetworkWizardPage extends BasePage {
    private static final String ADD_IPNETWORK_NAME_ATTRIBUTE_ID = "ADD_IPNETWORK_NAME_ATTRIBUTE_ID";
    private static final String ADD_IPNETWORK_DESCRIPTION_ATTRIBUTE_ID = "ADD_IPNETWORK_DESCRIPTION_ATTRIBUTE_ID";

    public IPNetworkWizardPage(WebDriver driver) {
        super(driver);
    }

    public void createIPNetwork(String networkName, String description){
        Wizard createIPNetwork = Wizard.createWizard(driver, wait);
        setNetworkName(createIPNetwork, networkName);
        setDescription(createIPNetwork, description);
        createIPNetwork.clickOK();
    }

    public void createIPNetwork(String networkName){
        Wizard createIPNetwork = Wizard.createWizard(driver, wait);
        setNetworkName(createIPNetwork, networkName);
        createIPNetwork.clickOK();
    }

    public void editIPNetwork(String networkName, String description){
        Wizard editIPNetwork = Wizard.createWizard(driver, wait);
        setNetworkName(editIPNetwork, networkName);
        setDescription(editIPNetwork, description);
        editIPNetwork.clickOK();
    }

    private void setNetworkName(Wizard createIPNetwork, String networkName){
        Input componentNetworkName = createIPNetwork.getComponent(ADD_IPNETWORK_NAME_ATTRIBUTE_ID, Input.ComponentType.TEXT_FIELD);
        componentNetworkName.clearByAction();
        componentNetworkName.setSingleStringValue(networkName);
    }

    private void setDescription(Wizard createIPNetwork, String description){
        Input componentDescription = createIPNetwork.getComponent(ADD_IPNETWORK_DESCRIPTION_ATTRIBUTE_ID, Input.ComponentType.TEXT_AREA);
        componentDescription.clear();
        componentDescription.setSingleStringValue(description);
    }
}

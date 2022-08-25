package com.oss.pages.reconciliation;

import org.openqa.selenium.WebDriver;

import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

public class NEDiscoveryCreateOperationWizardPage extends BasePage {

    private static final String WIZARD_ID = "neDiscoveryOperationCreationPrompt_prompt-card";
    private static final String NAME_TEXT_FIELD_ID = "NAME_TEXT_FIELD";
    private static final String IP_ADDRESS_RANGE_TEXT_FIELD_ID = "IP_ADDRESS_RANGE_TEXT_FIELD";
    private static final String DISCOVERY_MODE_RADIO_BUTTON_ID = "DISCOVERY_MODE_RADIO_BUTTON";
    private static final String HOLD = "Hold";
    private static final String DISCOVERY = "Discovery";
    private static final String RECONCILIATION = "Reconciliation";
    private Wizard wizard = Wizard.createByComponentId(driver, wait, WIZARD_ID);

    public NEDiscoveryCreateOperationWizardPage(WebDriver driver) {
        super(driver);
    }

    public void setName(String name) {
        wizard.setComponentValue(NAME_TEXT_FIELD_ID, name);
    }

    public void setIPAddressRange(String text) {
        wizard.setComponentValue(IP_ADDRESS_RANGE_TEXT_FIELD_ID, text);
    }

    public void clickAccept() {
        wizard.clickAccept();
    }

    public void selectHoldOperationType() {
        wizard.setComponentValue(DISCOVERY_MODE_RADIO_BUTTON_ID, HOLD);
    }

    public void selectDiscoveryOperationType() {
        wizard.setComponentValue(DISCOVERY_MODE_RADIO_BUTTON_ID, DISCOVERY);
    }

    public void selectReconciliationOperationType() {
        wizard.setComponentValue(DISCOVERY_MODE_RADIO_BUTTON_ID, RECONCILIATION);
    }
}

package com.oss.pages.reconciliation;

import org.openqa.selenium.WebDriver;

import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

import static com.oss.framework.components.inputs.Input.ComponentType.COMBOBOX;
import static com.oss.framework.components.inputs.Input.ComponentType.SEARCH_FIELD;
import static com.oss.framework.components.inputs.Input.ComponentType.TEXT_FIELD;

public class CmDomainWizardPage extends BasePage {

    private static final String CM_DOMAIN_WIZARD_ID = "narComponent_networkDiscoveryControlViewIdcmDomainActionPromptId_prompt-card";
    private static final String NAME_ID = "narComponent_networkDiscoveryControlViewIdnameTextFieldId";
    private static final String CM_INTERFACE_ID = "narComponent_networkDiscoveryControlViewIdinterfaceSearchBoxId";
    private static final String DOMAIN_ID = "narComponent_networkDiscoveryControlViewIddomainSearchBoxId";
    private static final String MEDIATOR_NAME_ID = "narComponent_networkDiscoveryControlViewIdmediatorNameComboboxId";
    private static final String MEDIATION_KEY_ID = "narComponent_networkDiscoveryControlViewIdconnectionAgentInstanceSearchBoxId";
    private static final String BASED_ON_ID = "narComponent_networkDiscoveryControlViewIdbasedOnComboboxId";
    private static final String STOP_ON_ID = "narComponent_networkDiscoveryControlViewIdstopOnComboboxId";
    private static final String SAVE_PERSPECTIVE_ID = "narComponent_networkDiscoveryControlViewIdsavePerspectiveComboboxId";
    private static final String HISTORICAL_SNAPSHOT_ID = "narComponent_networkDiscoveryControlViewIdmaxHistoricalNumberFieldId";
    private static final String NETWORK_MODIFICATION_ID = "narComponent_networkDiscoveryControlViewIdmaxFutureNumberFieldId";
    private static final String SAVE_ID = "narComponent_networkDiscoveryControlViewIdCmDomainActionButtonsAppId-1";
    private static final String CANCEL_ID = "narComponent_networkDiscoveryControlViewIdCmDomainActionButtonsAppId-0";
    private Wizard cmDomainWizard = Wizard.createByComponentId(driver, wait, CM_DOMAIN_WIZARD_ID);

    public CmDomainWizardPage(WebDriver driver) {
        super(driver);
    }

    @Step("Fill CM Domain wizard with Name: {name}, CM Interface: {cmInterface}, Domain: {domain}")
    public void fillCmDomainWizard(String name, String cmInterface, String domain) {
        setName(name);
        setInterface(cmInterface);
        setDomain(domain);
        save();
    }

    @Step("Set name for CM Domain")
    public void setName(String name) {
        cmDomainWizard.setComponentValue(NAME_ID, name, TEXT_FIELD);
    }

    @Step("Set interface for CM Domain")
    public void setInterface(String cmInterface) {
        cmDomainWizard.setComponentValue(CM_INTERFACE_ID, cmInterface, SEARCH_FIELD);
    }

    @Step("Set domain for CM Domain Name")
    public void setDomain(String domain) {
        cmDomainWizard.setComponentValue(DOMAIN_ID, domain, TEXT_FIELD);
    }

    @Step("Set mediator name for CM Domain Name")
    public void setMediatorName(String mediatorName) {
        cmDomainWizard.setComponentValue(MEDIATOR_NAME_ID, mediatorName, COMBOBOX);
    }

    @Step("Set mediation key for CM Domain Name")
    public void setMediationKey(String mediationKey) {
        cmDomainWizard.setComponentValue(MEDIATION_KEY_ID, mediationKey, SEARCH_FIELD);
    }

    @Step("Set based on for CM Domain Name")
    public void setBasedOn(String basedOn) {
        cmDomainWizard.setComponentValue(BASED_ON_ID, basedOn, COMBOBOX);
    }

    @Step("Set stop on for CM Domain Name")
    public void setStopOn(String stopOn) {
        cmDomainWizard.setComponentValue(STOP_ON_ID, stopOn, COMBOBOX);
    }

    @Step("Set save perspective for CM Domain Name")
    public void setSavePerspective(String savePerspective) {
        cmDomainWizard.setComponentValue(SAVE_PERSPECTIVE_ID, savePerspective, COMBOBOX);
    }

    @Step("Set historical snapshot number for CM Domain Name")
    public void setHistoricalSnapshot(String historicalSnapshot) {
        cmDomainWizard.setComponentValue(HISTORICAL_SNAPSHOT_ID, historicalSnapshot, TEXT_FIELD);
    }

    @Step("Set network modification number for CM Domain Name")
    public void setNetworkModification(String networkModification) {
        cmDomainWizard.setComponentValue(NETWORK_MODIFICATION_ID, networkModification, TEXT_FIELD);
    }

    @Step("Save CM Domain wizard")
    public void save() {
        cmDomainWizard.clickButtonById(SAVE_ID);
    }

    @Step("Cancel CM Domain wizard")
    public void cancel() {
        cmDomainWizard.clickButtonById(CANCEL_ID);
    }
}

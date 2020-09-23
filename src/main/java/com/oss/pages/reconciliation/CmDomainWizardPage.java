package com.oss.pages.reconciliation;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

import static com.oss.framework.components.inputs.Input.ComponentType.COMBOBOX;
import static com.oss.framework.components.inputs.Input.ComponentType.SEARCH_FIELD;
import static com.oss.framework.components.inputs.Input.ComponentType.TEXT_FIELD;

public class CmDomainWizardPage extends BasePage {

    public CmDomainWizardPage(WebDriver driver) {
        super(driver);
    }

    private Wizard cmDomainWizard = Wizard.createByComponentId(driver, wait, "narComponent_networkDiscoveryControlViewIdcmDomainActionPromptId");

    @Step("Set name for CM Domain")
    public void setName(String name) {
        Input input = cmDomainWizard.getComponent("narComponent_networkDiscoveryControlViewIdnameTextFieldId", TEXT_FIELD);
        input.setSingleStringValue(name);
    }

    @Step("Set interface for CM Domain")
    public void setInterface(String cmInterface) {
        Input input = cmDomainWizard.getComponent("narComponent_networkDiscoveryControlViewIdinterfaceSearchBoxId", SEARCH_FIELD);
        input.setSingleStringValue(cmInterface);
    }

    @Step("Set domain for CM Domain Name")
    public void setDomain(String domain) {
        Input input = cmDomainWizard.getComponent("narComponent_networkDiscoveryControlViewIddomainSearchBoxId", TEXT_FIELD);
        input.setSingleStringValue(domain);
    }

    @Step("Set mediator name for CM Domain Name")
    public void setMediatorName(String mediatorName) {
        Input input = cmDomainWizard.getComponent("narComponent_networkDiscoveryControlViewIdmediatorNameComboboxId", COMBOBOX);
        input.setSingleStringValue(mediatorName);
    }

    @Step("Set mediation key for CM Domain Name")
    public void setMediationKey(String mediationKey) {
        Input input = cmDomainWizard.getComponent("narComponent_networkDiscoveryControlViewIdconnectionAgentInstanceSearchBoxId", SEARCH_FIELD);
        input.setSingleStringValue(mediationKey);
    }

    @Step("Set based on for CM Domain Name")
    public void setBasedOn(String basedOn) {
        Input input = cmDomainWizard.getComponent("narComponent_networkDiscoveryControlViewIdbasedOnComboboxId", COMBOBOX);
        input.setSingleStringValue(basedOn);
    }

    @Step("Set stop on for CM Domain Name")
    public void setStopOn(String stopOn) {
        Input input = cmDomainWizard.getComponent("narComponent_networkDiscoveryControlViewIdstopOnComboboxId", COMBOBOX);
        input.setSingleStringValue(stopOn);
    }

    @Step("Set save perspective for CM Domain Name")
    public void setSavePerspective(String savePerspective) {
        Input input = cmDomainWizard.getComponent("narComponent_networkDiscoveryControlViewIdsavePerspectiveComboboxId", COMBOBOX);
        input.setSingleStringValue(savePerspective);
    }

    @Step("Set historical snapshot number for CM Domain Name")
    public void setHistoricalSnapshot(String historicalSnapshot) {
        Input input = cmDomainWizard.getComponent("narComponent_networkDiscoveryControlViewIdmaxHistoricalNumberFieldId", TEXT_FIELD);
        input.setSingleStringValue(historicalSnapshot);
    }

    @Step("Set network modification number for CM Domain Name")
    public void setNetworkModification(String networkModification) {
        Input input = cmDomainWizard.getComponent("narComponent_networkDiscoveryControlViewIdmaxFutureNumberFieldId", TEXT_FIELD);
        input.setSingleStringValue(networkModification);
    }

    @Step("Save CM Domain wizard")
    public void save() {
        Wizard.createByComponentId(driver, wait, "narComponent_networkDiscoveryControlViewIdcmDomainActionPromptId")
                .clickActionById("narComponent_networkDiscoveryControlViewIdCmDomainActionButtonsAppId-1");
    }

    @Step("Cancel CM Domain wizard")
    public void cancel() {
        Wizard.createByComponentId(driver, wait, "narComponent_networkDiscoveryControlViewIdcmDomainActionPromptId")
                .clickActionById("narComponent_networkDiscoveryControlViewIdCmDomainActionButtonsAppId-0");
    }
}

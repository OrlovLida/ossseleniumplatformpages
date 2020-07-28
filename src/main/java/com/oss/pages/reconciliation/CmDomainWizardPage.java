package com.oss.pages.reconciliation;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.Input;
import com.oss.framework.components.Input.ComponentType;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;

public class CmDomainWizardPage extends BasePage {
    private Wizard wizard;

    public CmDomainWizardPage(WebDriver driver) {
        super(driver);
    }

    public Wizard getWizard() {
        if (this.wizard == null) {
            this.wizard = Wizard.createWizard(this.driver, this.wait);
        }
        return wizard;
    }

    public void fillCmDomainWizardAndClose(String cmDomainName, String cmInterfaceName, String domainName) {
        Wizard wizard = Wizard.createWizard(driver, wait);
        Input name = wizard.getComponent("narComponent_networkDiscoveryControlViewIdnameTextFieldId", ComponentType.TEXT_FIELD);
        name.setSingleStringValue(cmDomainName);
        Input interfaceField = wizard.getComponent("narComponent_networkDiscoveryControlViewIdinterfaceSearchBoxId", ComponentType.SEARCH_FIELD);
        interfaceField.setSingleStringValue(cmInterfaceName);
        Input domain = wizard.getComponent("narComponent_networkDiscoveryControlViewIddomainSearchBoxId", ComponentType.TEXT_FIELD);
        domain.setSingleStringValue(domainName);
        DelayUtils.sleep(2000);
        wizard.clickSave();
        wizard.waitToClose();
    }

}

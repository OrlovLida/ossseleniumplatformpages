package com.oss.pages.reconciliation;

import com.oss.framework.components.data.Data;
import com.oss.framework.components.inputs.Button;
import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;
import org.openqa.selenium.WebDriver;

public class ResolveConflictWizardPage extends BasePage {

    private static final String WIZARD_ID = "ConflictResolverWindowApp_prompt-card";
    private final Wizard wizard;

    public ResolveConflictWizardPage(WebDriver driver) {
        super(driver);
        wizard = Wizard.createByComponentId(driver, wait, WIZARD_ID);
    }

    public void fromLastReconciliation() {
        DelayUtils.waitForPageToLoad(driver, wait);
        Input radioButtons = wizard.getComponent("conflictResolutionOption", Input.ComponentType.RADIO_BUTTON);
        radioButtons.setValue(Data.createSingleData("choose manually"));
    }

    public void clickSubmit() {
        DelayUtils.waitForPageToLoad(driver, wait);
        Button button = Button.createById(driver, "undefined-2");
        button.click();
    }

    public void chooseLeadingDomain(String name) {
        DelayUtils.waitForPageToLoad(driver, wait);
        wizard.setComponentValue("destinationCmDomain-input", name, Input.ComponentType.COMBOBOX);
    }

}

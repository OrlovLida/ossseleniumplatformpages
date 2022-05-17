package com.oss.pages.reconciliation;

import com.oss.framework.components.inputs.Button;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

public class ResolveConflictWizardPage extends BasePage {

    private static final String WIZARD_ID = "ConflictResolverWindowApp_prompt-card";
    private static final String CHOOSE_MANUALLY_ID = "conflictResolutionOption";
    private static final String CHOOSE_MANUALLY = "choose manually";
    private static final String SUBMIT_BUTTON_ID = "ConflictResolverFormSubmitButtonApp-2";
    private static final String LEADING_DOMAIN_ID = "destinationCmDomain-input";
    private final Wizard wizard;

    public ResolveConflictWizardPage(WebDriver driver) {
        super(driver);
        wizard = Wizard.createByComponentId(driver, wait, WIZARD_ID);
    }

    @Step("Click on 'choose manually' radio button")
    public void clickChooseManually() {
        DelayUtils.waitForPageToLoad(driver, wait);
        wizard.setComponentValue(CHOOSE_MANUALLY_ID, CHOOSE_MANUALLY, Input.ComponentType.RADIO_BUTTON);
    }

    @Step("Click Submit button")
    public void clickSubmit() {
        DelayUtils.waitForPageToLoad(driver, wait);
        Button button = Button.createById(driver, SUBMIT_BUTTON_ID);
        button.click();
    }

    @Step("Set Leading Domain")
    public void setLeadingDomain(String name) {
        DelayUtils.waitForPageToLoad(driver, wait);
        wizard.setComponentValue(LEADING_DOMAIN_ID, name, Input.ComponentType.COMBOBOX);
    }

}

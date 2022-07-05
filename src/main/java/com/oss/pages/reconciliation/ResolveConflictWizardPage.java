package com.oss.pages.reconciliation;

import org.openqa.selenium.WebDriver;

import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class ResolveConflictWizardPage extends BasePage {

    private static final String WIZARD_ID = "ConflictResolverWindowApp_prompt-card";
    private static final String CHOOSE_MANUALLY_ID = "conflictResolutionOption";
    private static final String CHOOSE_MANUALLY = "choose manually";
    private static final String SUBMIT_BUTTON_ID = "ConflictResolverFormSubmitButtonApp-2";
    private static final String LEADING_DOMAIN_ID = "destinationCmDomain";
    private static final String COMMENT_ID = "comment";
    private final Wizard wizard;

    public ResolveConflictWizardPage(WebDriver driver) {
        super(driver);
        wizard = Wizard.createByComponentId(driver, wait, WIZARD_ID);
    }

    @Step("Click on 'choose manually' radio button")
    public void setChooseManually() {
        wizard.setComponentValue(CHOOSE_MANUALLY_ID, CHOOSE_MANUALLY);
    }

    @Step("Click Submit button")
    public void clickSubmit() {
        wizard.clickButtonById(SUBMIT_BUTTON_ID);
    }

    @Step("Set Leading Domain")
    public void setLeadingDomain(String name) {
        wizard.setComponentValue(LEADING_DOMAIN_ID, name);
    }

    @Step("Add comment")
    public void setComment(String comment) {
        wizard.setComponentValue(COMMENT_ID, comment);
    }
}

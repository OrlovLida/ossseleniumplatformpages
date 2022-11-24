package com.oss.pages.reconciliation.audit;

import org.openqa.selenium.WebDriver;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class ValidationRulesGroupWizardPage extends BasePage {

    private static final String WIZARD_ID = "networkParametersAudit_validationRuleManagerIdvalidationRulesGroupActionWidgetId";
    private static final String NAME_ID = "networkParametersAudit_validationRuleManagerIdnameTextFieldId";
    private static final String PRIORITY_ID = "networkParametersAudit_validationRuleManagerIdpriorityTextFieldId";
    private static final String ACCEPT_ID = "wizard-submit-button-networkParametersAudit_validationRuleManagerIdvalidationRulesGroupActionWidgetId";

    public ValidationRulesGroupWizardPage(WebDriver driver) {
        super(driver);
    }

    public void createGroup(String name, int priority) {
        setName(name);
        setPriority(String.valueOf(priority));
        waitForPageToLoad();
        accept();
    }

    @Step("Set name to {name}")
    public void setName(String name) {
        getWizard().setComponentValue(NAME_ID, name);
    }

    @Step("Set priority to {priority}")
    public void setPriority(String priority) {
        getWizard().setComponentValue(PRIORITY_ID, priority);
    }

    @Step("Click accept")
    public void accept() {
        getWizard().clickButtonById(ACCEPT_ID);
    }

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    private Wizard getWizard() {
        return Wizard.createByComponentId(driver, wait, WIZARD_ID);
    }
}

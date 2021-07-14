package com.oss.pages.servicedesk.ticket.wizard;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.servicedesk.BaseSDPage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

public class WizardPage extends BaseSDPage {

    private final MOStep moStep;

    public WizardPage(WebDriver driver) {
        super(driver);
        this.moStep = new MOStep(wait);
    }

    public MOStep getMoStep() {
        return moStep;
    }

    @Step("I click Next button in wizard")
    public void clickNextButtonInWizard(WebDriver driver) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getWizard().clickNext();
    }

    @Step("I click Accept button in wizard")
    public void clickAcceptButtonInWizard(WebDriver driver) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getWizard().clickAccept();
    }

    @Step("I insert {value} to multi combo box component with id {componentId}")
    public void insertValueToMultiComboBoxComponent(String text, String componentId) {
        insertValueToComponent(componentId, text, Input.ComponentType.MULTI_COMBOBOX);
    }

    @Step("I insert {value} to combo box component with id {componentId}")
    public void insertValueToComboBoxComponent(String text, String componentId) {
        insertValueToComponent(componentId, text, Input.ComponentType.COMBOBOX);
    }

    @Step("I insert {value} to search component with id {componentId}")
    public void insertValueToSearchComponent(String text, String componentId) {
        insertValueToComponent(componentId, text, Input.ComponentType.SEARCH_FIELD);
    }

    @Step("I insert {value} to text component with id {componentId}")
    public void insertValueToTextComponent(String text, String componentId) {
        insertValueToComponent(text, componentId, Input.ComponentType.TEXT_FIELD);
    }

    private void insertValueToComponent(String text, String componentId, Input.ComponentType componentType) {
        getWizard().setComponentValue(componentId, text, componentType);
    }

    private Wizard getWizard() {
        return Wizard.createWizard(driver, wait);
    }
}

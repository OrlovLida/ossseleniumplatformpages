package com.oss.pages.templateCM;

import com.oss.framework.components.inputs.Button;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;


public class SetParametersWizardPage extends BasePage {
    private String FILL_BUTTON = "ParameterFormSubmitButtonApp-3";

    public enum PossibleLabels {
        VENDOR_SPECIFIC_OBJECT_PARAMETERS("Vendor Specific Object Parameters"),
        TECHNICAL_OBJECT_PARAMETERS("Technical Object Parameters"),
        NEW_INVENTORY_OBJECT_PARAMETERS("New Inventory Object Parameters"),
        DEPLOY_SERVER_SETTINGS("Deploy server settings"),
        USER_PARAMETERS("User parameters");

        private final String label;

        PossibleLabels(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }

    private Wizard setParametersWizard = Wizard.createByComponentId(driver, wait, "changeConfigurationParameterPrompt");

    public SetParametersWizardPage(WebDriver driver) {
        super(driver);
    }

    public void rolloutAllLists() {
        DelayUtils.waitForPageToLoad(driver, wait);
        //TODO Change rolloutByLabel when data-attributename will be added
        for (PossibleLabels value : PossibleLabels.values()) {
            setParametersWizard.rolloutByLabel(value.getLabel());
        }
    }

    @Step("Get parameter")
    public String getParameter(String label) {
        rolloutAllLists();
        Input input = setParametersWizard.getComponent(label, Input.ComponentType.TEXT_FIELD);
        return input.getStringValue().trim();
    }

    @Step("Fill parameters")
    public void clickFillParameters() {
        DelayUtils.waitForPageToLoad(driver, wait);
        Button fillButton = Button.createBySelectorAndId(driver, "a", FILL_BUTTON);
        fillButton.click();
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("Set parameters")
    public void setParameter(String label, String value) {
        rolloutAllLists();
        Input input = setParametersWizard.getComponent(label, Input.ComponentType.TEXT_FIELD);
        input.setSingleStringValue(value);
    }
}

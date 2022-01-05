package com.oss.pages.gisview;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.inputs.Button;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;

public class DuctCopyWizardPage extends BasePage {
    private static final String INPUT_NUMBER_OF_COPIES_ID = "numberOfCopies";
    private static final String COPY_DUCT_BUTTON_ID = "PromptCopyDuctButtonId-0";
    private static final String WIZARD_ID = "prompt_copyduct";

    public DuctCopyWizardPage(WebDriver driver) {
        super(driver);
    }

    public void setNumberOfCopies(String copies) {
        getCreateDuctWizard().setComponentValue(INPUT_NUMBER_OF_COPIES_ID, copies, Input.ComponentType.TEXT_FIELD);
    }

    public void create() {
        Button.createBySelectorAndId(driver, "a", COPY_DUCT_BUTTON_ID).click();
    }

    private Wizard getCreateDuctWizard() {
        return Wizard.createByComponentId(driver, wait, WIZARD_ID);
    }
}
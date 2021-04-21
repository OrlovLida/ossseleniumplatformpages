package com.oss.pages.gisView;

import com.oss.framework.components.inputs.Button;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import org.openqa.selenium.WebDriver;

public class DuctCopyWizardPage extends BasePage {
    private static final String INPUT_NR_OF_COPIES_DATA_ATTRIBUTE_NAME = "numberOfCopies";
    private static final String COPY_DUCT_BUTTON_DATA_ATTRIBUTE_NAME = "PromptCopyDuctButtonId-0";

    public DuctCopyWizardPage(WebDriver driver) {
        super(driver);
    }

    public void setNumberOfCopies(String copies) {
        getCreateDuctWizard().setComponentValue(INPUT_NR_OF_COPIES_DATA_ATTRIBUTE_NAME, copies, Input.ComponentType.TEXT_FIELD);
    }

    public void create() {
        Button.createBySelectorAndId(driver, "a", COPY_DUCT_BUTTON_DATA_ATTRIBUTE_NAME).click();
    }

    private Wizard getCreateDuctWizard() {
        return Wizard.createPopupWizard(driver, wait);
    }
}
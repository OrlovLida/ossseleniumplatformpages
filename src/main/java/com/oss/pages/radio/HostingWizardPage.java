package com.oss.pages.radio;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

public class HostingWizardPage extends BasePage {
    private final Wizard wizard;
    private static final String ONLY_COMPATIBLE = "onlyCompatible";

    public HostingWizardPage(WebDriver driver) {
        super(driver);
        wizard = Wizard.createWizard(driver, wait);
    }

    @Step("Click Accept button")
    public void clickAccept() {
        wizard.clickAccept();
    }

    @Step("Use 'Only Compatible' checkbox")
    public void onlyCompatible(String showOnlyCompatible) {
        wizard.createByComponentId(driver, wait, "hosting-wizard");
        Input componentUseFirstAvailableID = wizard.getComponent(ONLY_COMPATIBLE, Input.ComponentType.CHECKBOX);
        componentUseFirstAvailableID.setSingleStringValue(showOnlyCompatible);
    }

    //TODO: search for the deviceName and use it
    @Step("Create Hosting Relation")
    public void createHostingRelationWizard(String deviceName) {

        clickAccept();
    }
}


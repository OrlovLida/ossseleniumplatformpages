package com.oss.pages.physical;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.data.Data;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

public class SublocationWizardPage extends BasePage {

    private static final String SUBLOCATION_TYPE = "type";
    private static final String SUBLOCATION_NAME = "name";
    private final Wizard wizard;

    public SublocationWizardPage(WebDriver driver) {
        super(driver);
        wizard = Wizard.createWizard(driver, wait);
    }

    @Step("Set Sublocation Type")
    public void setSublocationType(String sublocationType) {
        Input sublocationTypeComponent = wizard.getComponent(SUBLOCATION_TYPE, Input.ComponentType.COMBOBOX);
        sublocationTypeComponent.setValue(Data.createSingleData(sublocationType));
    }

    @Step("Set Sublocation Name")
    public void setSublocationName(String sublocationName) {
        Input sublocationNameComponent = wizard.getComponent(SUBLOCATION_NAME, Input.ComponentType.TEXT_FIELD);
        sublocationNameComponent.setValue(Data.createSingleData(sublocationName));
    }

    @Step("Click Next Step button")
    public void clickNext() {
        wizard.clickNext();
    }

    @Step("Click Accept button")
    public void Accept() {
        wizard.clickAccept();
    }
}

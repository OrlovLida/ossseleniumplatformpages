package com.oss.pages.physical;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.inputs.Input.ComponentType;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class CardCreateWizardPage extends BasePage {

    public static final String WIZARD_ID = "card_create_wizard_view";
    private static final String MODEL_ID = "model";
    private static final String AVAILABLE_SLOTS_ID = "slot";

    public CardCreateWizardPage(WebDriver driver) {
        super(driver);
    }

    private Wizard getWizard() {
        DelayUtils.waitForPageToLoad(driver, wait);
        return Wizard.createByComponentId(driver, wait, WIZARD_ID);
    }

    @Step("Set Model")
    public void setModel(String model) {
        getWizard().setComponentValue(MODEL_ID, model, ComponentType.SEARCH_FIELD);
    }

    @Step("Set Slot")
    public void setSlot(String slot) {
        getWizard().setComponentValue(AVAILABLE_SLOTS_ID, slot, ComponentType.MULTI_COMBOBOX);
    }

    @Step("Click Accept button")
    public void clickAccept() {
        getWizard().clickAccept();
    }

    @Step("Get wizard name")
    public String getWizardName() {
        return getWizard().getWizardName();
    }

}

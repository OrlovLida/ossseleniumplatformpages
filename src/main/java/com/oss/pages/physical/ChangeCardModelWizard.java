package com.oss.pages.physical;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class ChangeCardModelWizard extends BasePage {

    private Wizard wizard = Wizard.createPopupWizard(driver, wait);
    private static final String MODEL_SEARCH = "search_box_card_change_model";

    public ChangeCardModelWizard(WebDriver driver) {
        super(driver);
    }

    @Step("Set Model")
    public void setModelCard(String model) {
        wizard.setComponentValue(MODEL_SEARCH, model, Input.ComponentType.SEARCH_FIELD);
    }

    @Step("Click Update button")
    public void clickUpdate() {
        wizard.clickUpdate();
    }
}

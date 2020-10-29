package com.oss.pages.physical;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.data.Data;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

public class ChangeCardModelWizard extends BasePage {

    private Wizard wizard = Wizard.createByComponentId(driver, wait, "card_change_model_wizard_view");
    private static final String MODEL_SEARCH = "search_box_card_change_model";

    public ChangeCardModelWizard(WebDriver driver) {
        super(driver);
    }

    @Step("Set Model")
    public void setModelCard(String model) {
        Input modelComponent = wizard.getComponent(MODEL_SEARCH, Input.ComponentType.SEARCH_FIELD);
        modelComponent.clear();
        modelComponent.setValue(Data.createSingleData(model));
    }

    @Step("Click Update button")
    public void clickUpdate() {
        wizard.clickUpdate();
    }
}

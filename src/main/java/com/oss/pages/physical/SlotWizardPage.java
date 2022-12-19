package com.oss.pages.physical;

import com.oss.framework.components.data.Data;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

public class SlotWizardPage extends BasePage {

    public static final String WIZARD_ID = "create_slot_wizard_view_prompt-card";
    private static final String MODEL_ID = "search_model";
    private static final String SLOT_NAME_ID = "text_name";
    private static final String SLOT_CREATE_BUTTON_ID = "slot_common_buttons_app-1";
    private static final String SLOT_CANCEL_BUTTON_ID = "slot_common_buttons_app-0";


    public SlotWizardPage(WebDriver driver) { super (driver);}

    private Wizard getWizard() {
        DelayUtils.waitForPageToLoad(driver, wait);
        return Wizard.createByComponentId(driver, wait, WIZARD_ID);
    }

    @Step("Set Model")
    public void setModel(String model) {
        getWizard().getComponent(MODEL_ID,Input.ComponentType.SEARCH_BOX)
                .setValueContains(Data.createSingleData(model));
    }

    @Step("Set Name")
    public void setName(String name) {
        getWizard().setComponentValue(SLOT_NAME_ID, name, Input.ComponentType.TEXT_FIELD);
    }

    @Step("Click Accept button")
    public void clickCreate() {
        getWizard().clickButtonById(SLOT_CREATE_BUTTON_ID);
    }

    @Step("Click Cancel button")
    public void clickCancel() {
        getWizard().clickButtonById(SLOT_CANCEL_BUTTON_ID);
    }

}

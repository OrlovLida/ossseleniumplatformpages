package com.oss.pages.physical;

import org.openqa.selenium.WebDriver;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class ChassisWizardPage extends BasePage {

    public static final String CHASSIS_CREATE_WIZARD_ID = "chassis_create_wizard_view";
    public static final String CHASSIS_UPDATE_WIZARD_ID = "chassis_update_wizard_view";
    private static final String MODEL_ID = "model";
    private static final String CHASSIS_NAME_ID = "name";
    private static final String DESCRIPTION_ID = "description";

    public ChassisWizardPage(WebDriver driver) {
        super(driver);
    }

    private Wizard getWizard() {
        DelayUtils.waitForPageToLoad(driver, wait);
        if (driver.getPageSource().contains(CHASSIS_CREATE_WIZARD_ID)) {
            return Wizard.createByComponentId(driver, wait, CHASSIS_CREATE_WIZARD_ID);
        }
        return Wizard.createByComponentId(driver, wait, CHASSIS_UPDATE_WIZARD_ID);
    }

    @Step("Set Model")
    public void setModel(String model) {
        getWizard().getComponent(MODEL_ID).setSingleStringValueContains(model);
    }

    @Step("Set Name")
    public void setName(String name) {
        getWizard().setComponentValue(CHASSIS_NAME_ID, name);
    }

    @Step("Set Description")
    public void setDescription(String description) {
        getWizard().setComponentValue(DESCRIPTION_ID, description);
    }

    @Step("Click Accept button")
    public void clickAccept() {
        getWizard().clickAccept();
    }

    @Step("Click Cancel button")
    public void clickCancel() {
        getWizard().clickCancel();
    }



}

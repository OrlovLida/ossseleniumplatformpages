package com.oss.pages.physical;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

public class ChassisWizardPage extends BasePage {

    public static final String WIZARD_ID = "chassis_create_wizard_view";
    private static final String MODEL_ID = "model";
    private static final String CHASSIS_NAME_ID = "name";

    public ChassisWizardPage(WebDriver driver) { super (driver);}

    private Wizard getWizard() {
        DelayUtils.waitForPageToLoad(driver, wait);
        return Wizard.createByComponentId(driver, wait, WIZARD_ID);
    }

    @Step("Set Model")
    public void setModel(String model) {
        getWizard().getComponent(MODEL_ID).setSingleStringValueContains(model);
    }

    @Step("Set Name")
    public void setName(String name) {
        getWizard().setComponentValue(CHASSIS_NAME_ID, name);
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

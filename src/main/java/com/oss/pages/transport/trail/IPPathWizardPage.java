package com.oss.pages.transport.trail;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

import static com.oss.framework.components.inputs.Input.ComponentType.TEXT_FIELD;

public class IPPathWizardPage extends BasePage {

    private static final String NAME_ID = "name-uid";
    private static final String CAPACITY_VALUE_ID = "capacity-value-uid";
    private static final String PROCEED_ID = "IP_PATH_BUTTON_APP_ID-1";
    private static final String WIZARD_ID = "Popup";
    private Wizard wizard = Wizard.createByComponentId(driver, wait, WIZARD_ID);

    private IPPathWizardPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public static IPPathWizardPage getIPPathWizardPage(WebDriver driver, WebDriverWait wait) {
        DelayUtils.waitForPageToLoad(driver, wait);
        return new IPPathWizardPage(driver, wait);
    }

    @Step("Set name for IP Path to {name}")
    public void setName(String name) {
        wizard.setComponentValue(NAME_ID, name, TEXT_FIELD);
    }

    @Step("Set Capacity Value for IP Path to {capacityValue}")
    public void setCapacityValue(String capacityValue) {
        wizard.setComponentValue(CAPACITY_VALUE_ID, capacityValue, TEXT_FIELD);
    }

    @Step("Proceed")
    public void proceed() {
        wizard.clickButtonById(PROCEED_ID);
    }

}
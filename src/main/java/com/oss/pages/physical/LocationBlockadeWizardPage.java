package com.oss.pages.physical;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.data.Data;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

import static com.oss.framework.components.inputs.Input.ComponentType.SWITCHER;

public class LocationBlockadeWizardPage extends BasePage {

    private static final String BLOCKADE_REASON_DATA_ATTRIBUTE_NAME = "blockade_reason";
    private static final String BLOCKADE_SWITCH = "is_blocked";
    private static final String WIZARD_ID = "optional";

    public LocationBlockadeWizardPage(WebDriver driver) {
        super(driver);
    }

    @Step("Enable Blockade")
    public void enableLocationBlockade(String reason) {
        changeSwitcher("true");
        DelayUtils.waitForPageToLoad(driver, wait);
        getBlockLocationWizard().getComponent(BLOCKADE_REASON_DATA_ATTRIBUTE_NAME,
                Input.ComponentType.COMBOBOX).setValueContains(Data.createSingleData(reason));
        DelayUtils.waitForPageToLoad(driver, wait);
        accept();
    }

    @Step("Click Accept button")
    public void accept() {
        getBlockLocationWizard().clickAccept();
    }

    public void changeSwitcher(String value) {
        getIsBlockedSwitcher().setSingleStringValue(value);
    }

    private Wizard getBlockLocationWizard() {
        return Wizard.createByComponentId(driver, wait, WIZARD_ID);
    }

    private Input getIsBlockedSwitcher() {
        return ComponentFactory.create(BLOCKADE_SWITCH, SWITCHER, driver, wait);
    }
}

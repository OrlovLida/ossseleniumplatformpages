package com.oss.pages.physical;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.data.Data;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import static com.oss.framework.components.inputs.Input.ComponentType.*;

public class LocationBlockadeWizardPage extends BasePage {

    private static final String LOCATION_BLOCKADE_WIZARD_ID = "Popup";
    private static final String BLOCKADE_REASON_DATA_ATTRIBUTE_NAME = "blockade_reason";
    private static final String BLOCKADE_SWITCH = "is_blocked";

    public LocationBlockadeWizardPage(WebDriver driver) {
        super(driver);
    }

    private Wizard locationBlockadeWizard = Wizard.createByComponentId(driver, wait, LOCATION_BLOCKADE_WIZARD_ID);

    @Step("Enable Blockade")
    public void enableLocationBlockade(String reason) {
        changeSwitcher("true");
        DelayUtils.waitForPageToLoad(driver, wait);
        selectLocationBlockadeReason(reason);
        DelayUtils.waitForPageToLoad(driver, wait);
        accept();
    }

    @Step("Disable Blockade")
    public void disableLocationBlockade() {
        changeSwitcher("false");
        DelayUtils.waitForPageToLoad(driver, wait);
        accept();
    }

    @Step("Set Location Blockade Reason to {reason}")
    public void selectLocationBlockadeReason(String reason) {
        locationBlockadeWizard.getComponent(BLOCKADE_REASON_DATA_ATTRIBUTE_NAME, Input.ComponentType.COMBOBOX)
                .setValueContains(Data.createSingleData(reason));
    }

    @Step("Click Accept button")
    public void accept() {
        locationBlockadeWizard.clickAccept();
    }

    @Step("Sets switch button to {value}")
    public void changeSwitcher(String value) {
        locationBlockadeWizard.setComponentValue(BLOCKADE_SWITCH, value, SWITCHER);
    }

}

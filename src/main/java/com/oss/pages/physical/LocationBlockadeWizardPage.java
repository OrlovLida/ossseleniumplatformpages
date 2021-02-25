package com.oss.pages.physical;

import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.inputs.Switcher;
import com.oss.framework.data.Data;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import com.oss.pages.platform.InputsWizardPage;
import com.oss.pages.platform.LoginPanelPage;
import io.qameta.allure.Step;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.WebDriver;
import static com.oss.framework.components.inputs.Input.ComponentType.*;

public class LocationBlockadeWizardPage extends BasePage {

    private static final String BLOCKADE = "blockade-wizard";
    private static final String LOCATION_BLOCKADE_ACCEPT_BUTTON_DATA_NAME = "wizard-submit-button-blockade-wizard";
    private static final String BLOCKADE_REASON_DATA_ATTRIBUTE_NAME = "blockade_reason";
    private static final String BLOCKADE_SWITCH = "is_blocked";

    public LocationBlockadeWizardPage(WebDriver driver) {
        super(driver);
    }

    @Step("Enable Blockade")
    public void enableLocationBlockade(String Reason) {
        changeSwitcher("true");
        DelayUtils.sleep(1000);
        getBlockLocationWizard().getComponent(BLOCKADE_REASON_DATA_ATTRIBUTE_NAME,
                Input.ComponentType.COMBOBOX).setValueContains(Data.createSingleData(Reason));
        DelayUtils.sleep(1000);
        accept();
    }

    @Step("Click Accept button")
    public void accept() {
        getBlockLocationWizard().clickAccept();
    }

    public void changeSwitcher(String value) {
        getIsBlockedSwitcher().setSingleStringValue(value);
    }

    public Wizard getBlockLocationWizard() {
        return Wizard.createWizardByHeaderText(driver, wait, "Block location");
    }


    private Input getIsBlockedSwitcher() {
        return ComponentFactory.create(BLOCKADE_SWITCH, SWITCHER, driver, wait);
    }
}

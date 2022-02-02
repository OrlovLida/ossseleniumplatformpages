package com.oss.pages.transport.trail;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

import static com.oss.framework.components.inputs.Input.ComponentType.TEXT_FIELD;

public class EthernetLinkWizardPage extends BasePage {
    private final Wizard wizard;
    private static final String NAME_TEXT_FIELD_ID = "trailNameComponent";
    private static final String SPEED_COMBOBOX_ID = "oss.transport.trail.type.Ethernet Link.Speed";

    public EthernetLinkWizardPage(WebDriver driver) {
        super(driver);
        wizard = Wizard.createWizard(driver, wait);
    }

    @Step("Set name to {name}")
    public void setName(String name) {
        wizard.setComponentValue(NAME_TEXT_FIELD_ID, name, TEXT_FIELD);
    }

    @Step("Set speed to {speed}")
    public void setSpeed(String speed) {
        wizard.setComponentValue(SPEED_COMBOBOX_ID, speed, Input.ComponentType.COMBOBOX);
    }
}

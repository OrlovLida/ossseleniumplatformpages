package com.oss.pages.radio;

import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

import static com.oss.framework.components.inputs.Input.ComponentType.TEXT_FIELD;

public class AntennaArrayWizardPage extends BasePage {

    public AntennaArrayWizardPage(WebDriver driver) {
        super(driver);
    }

    private static final String ANTENNA_ARRAY_WIZARD_DATA_ATTRIBUTE_NAME = "antenna-array-wizard";
    private static final String ANTENNA_ARRAY_EL_TILT_0__DATA_ATTRIBUTE_NAME = "electricalTilt_0";
    private static final String ANTENNA_ARRAY_EL_TILT_1__DATA_ATTRIBUTE_NAME = "electricalTilt_1";
    private static final String ANTENNA_ARRAY_EL_TILT_2__DATA_ATTRIBUTE_NAME = "electricalTilt_2";
    private static final String ANTENNA_ARRAY_EL_TILT_3__DATA_ATTRIBUTE_NAME = "electricalTilt_3";
    private static final String ANTENNA_ARRAY_EL_TILT_4__DATA_ATTRIBUTE_NAME = "electricalTilt_4";
    private static final String ANTENNA_ARRAY_EL_TILT_5__DATA_ATTRIBUTE_NAME = "electricalTilt_5";
    private static final String ANTENNA_ARRAY_EL_TILT_6__DATA_ATTRIBUTE_NAME = "electricalTilt_6";

    @Step("Set electrical tilt for first array")
    public void setElTiltFirstArray(String elTilt) {
        getAntennaArrayWizard().setComponentValue(ANTENNA_ARRAY_EL_TILT_0__DATA_ATTRIBUTE_NAME, elTilt, TEXT_FIELD);
    }

    @Step("Set electrical tilt for second array")
    public void setElTiltSecondArray(String elTilt) {
        getAntennaArrayWizard().setComponentValue(ANTENNA_ARRAY_EL_TILT_1__DATA_ATTRIBUTE_NAME, elTilt, TEXT_FIELD);
    }

    @Step("Click Accept button")
    public void clickAccept() {
        getAntennaArrayWizard().clickAccept();
    }

    private Wizard getAntennaArrayWizard() {
        return Wizard.createByComponentId(driver, wait, ANTENNA_ARRAY_WIZARD_DATA_ATTRIBUTE_NAME);
    }
}

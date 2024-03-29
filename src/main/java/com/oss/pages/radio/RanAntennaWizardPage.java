package com.oss.pages.radio;

import org.openqa.selenium.WebDriver;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class RanAntennaWizardPage extends BasePage {

    private static final String RAN_ANTENNA_WIZARD_DATA_ATTRIBUTE_NAME = "antenna-wizard";
    private static final String RAN_ANTENNA_NAME_DATA_ATTRIBUTE_NAME = "name";
    private static final String RAN_ANTENNA_DESCRIPTION_DATA_ATTRIBUTE_NAME = "description";
    private static final String RAN_ANTENNA_MODEL_DATA_ATTRIBUTE_NAME = "model";
    private static final String RAN_ANTENNA_LOCATION_DATA_ATTRIBUTE_NAME = "location";
    private static final String RAN_ANTENNA_PRECISE_LOCATION_DATA_ATTRIBUTE_NAME = "preciseLocation";
    private static final String RAN_ANTENNA_MECHANICAL_TILT_DATA_ATTRIBUTE_NAME = "mechanicalTilt";
    private static final String RAN_ANTENNA_AZIMUTH_DATA_ATTRIBUTE_NAME = "azimuth";
    private static final String RAN_ANTENNA_HEIGHT_AGL_DATA_ATTRIBUTE_NAME = "heightAgl";
    private static final String RAN_ANTENNA_SIDE_TILT_DATA_ATTRIBUTE_NAME = "sideTilt";
    private static final String RAN_ANTENNA_MOUNTING_TYPE_DATA_ATTRIBUTE_NAME = "mountingType";

    public RanAntennaWizardPage(WebDriver driver) {
        super(driver);
    }

    @Step("Create RAN Antenna with mandatory fields (Model, Name, Location, Precise Location) filled in")
    public void createRANAntenna(String model, String name, String location) {
        setName(name);
        setModel(model);
        setLocation(location);
        setPreciseLocation(location);
        DelayUtils.waitForPageToLoad(driver, wait);
        clickAccept();
    }

    @Step("Set name")
    public void setName(String name) {
        getRanAntennaWizard().setComponentValue(RAN_ANTENNA_NAME_DATA_ATTRIBUTE_NAME, name);
    }

    @Step("Set description")
    public void setDescription(String description) {
        getRanAntennaWizard().setComponentValue(RAN_ANTENNA_DESCRIPTION_DATA_ATTRIBUTE_NAME, description);
    }

    @Step("Set model")
    public void setModel(String model) {
        getRanAntennaWizard().setComponentValue(RAN_ANTENNA_MODEL_DATA_ATTRIBUTE_NAME, model);
    }

    @Step("Set location")
    public void setLocation(String location) {
        if (getRanAntennaWizard().getComponent(RAN_ANTENNA_LOCATION_DATA_ATTRIBUTE_NAME)
                .getStringValue().isEmpty()) {
            getRanAntennaWizard().setComponentValue(RAN_ANTENNA_LOCATION_DATA_ATTRIBUTE_NAME, location);
        }
    }

    @Step("Set precise location")
    public void setPreciseLocation(String location) {
        getRanAntennaWizard().getComponent(RAN_ANTENNA_PRECISE_LOCATION_DATA_ATTRIBUTE_NAME).setSingleStringValueContains(location);
    }

    @Step("Set mechanical tilt")
    public void setMechanicalTilt(String mechanicalTilt) {
        getRanAntennaWizard().setComponentValue(RAN_ANTENNA_MECHANICAL_TILT_DATA_ATTRIBUTE_NAME, mechanicalTilt);
    }

    @Step("Set azimuth")
    public void setAzimuth(String azimuth) {
        getRanAntennaWizard().setComponentValue(RAN_ANTENNA_AZIMUTH_DATA_ATTRIBUTE_NAME, azimuth);
    }

    @Step("Set height")
    public void setHeight(String height) {
        getRanAntennaWizard().setComponentValue(RAN_ANTENNA_HEIGHT_AGL_DATA_ATTRIBUTE_NAME, height);
    }

    @Step("Set side tilt")
    public void setSideTilt(String sideTilt) {
        getRanAntennaWizard().setComponentValue(RAN_ANTENNA_SIDE_TILT_DATA_ATTRIBUTE_NAME, sideTilt);
    }

    @Step("Set mounting type")
    public void setMountingType(String mountingType) {
        getRanAntennaWizard().setComponentValue(RAN_ANTENNA_MOUNTING_TYPE_DATA_ATTRIBUTE_NAME, mountingType);
    }

    @Step("Click Accept button")
    public void clickAccept() {
        getRanAntennaWizard().clickAccept();
    }

    @Step("Click Cancel button")
    public void clickCancel() {
        getRanAntennaWizard().clickCancel();
    }

    private Wizard getRanAntennaWizard() {
        return Wizard.createByComponentId(driver, wait, RAN_ANTENNA_WIZARD_DATA_ATTRIBUTE_NAME);
    }

}

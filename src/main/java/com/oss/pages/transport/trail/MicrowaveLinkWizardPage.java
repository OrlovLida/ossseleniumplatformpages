package com.oss.pages.transport.trail;

import com.oss.framework.components.inputs.Combobox;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.inputs.SearchField;
import com.oss.framework.data.Data;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

import static com.oss.framework.components.inputs.Input.ComponentType.TEXT_FIELD;

public class MicrowaveLinkWizardPage extends BasePage {
    private final Wizard wizard;
    private static final String MODE_COMBOBOX_ID = "wizardModeComboboxId-input";
    private static final String USER_LABEL_ID = "oss.transport.trail.type.MicrowaveLink.UserLabel";
    private static final String CAPACITY_UNIT_ID = "trailCapacityUnitComponent";
    private static final String CAPACITY_VALUE_ID = "trailCapacityValueComponent";
    private static final String TERMINATION_START_DEVICE_ID = "terminationDeviceComponent_1_OSF";
    private static final String TERMINATION_END_DEVICE_ID = "terminationDeviceComponent_2_OSF";

    public MicrowaveLinkWizardPage(WebDriver driver) {
        super(driver);
        wizard = Wizard.createWizard(driver, wait);
    }

    @Step("Select wizard mode")
    public void selectWizardMode(String mode) {
        wizard.setComponentValue(MODE_COMBOBOX_ID, mode, Input.ComponentType.COMBOBOX);
    }

    @Step("Set user label to {label}")
    public void setUserLabel(String label) {
        wizard.setComponentValue(USER_LABEL_ID, label, TEXT_FIELD);
    }

    @Step("Set capacity unit to {unit}")
    public void setCapacityUnit(String unit) {
        wizard.setComponentValue(CAPACITY_UNIT_ID, unit, Input.ComponentType.COMBOBOX);
    }

    @Step("Set capacity value to {value}")
    public void setCapacityValue(String value) {
        wizard.setComponentValue(CAPACITY_VALUE_ID, value, TEXT_FIELD);
    }

    @Step("Set start termination network element to {name}")
    public void setStartTerminationNetworkElement(String name) {
        SearchField searchField = (SearchField) wizard.getComponent(TERMINATION_START_DEVICE_ID, Input.ComponentType.SEARCH_FIELD);
        searchField.clear();
        searchField.setValueContains(Data.createSingleData(name));
    }

    @Step("Set end termination network element to {name}")
    public void setEndTerminationNetworkElement(String name) {
        SearchField searchField = (SearchField) wizard.getComponent(TERMINATION_END_DEVICE_ID, Input.ComponentType.SEARCH_FIELD);
        searchField.clear();
        searchField.setValueContains(Data.createSingleData(name));
    }

    public void next() {
        wizard.clickNext();
    }

    public void accept() {
        wizard.clickAccept();
    }
}

package com.oss.pages.physical;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.wizard.Wizard;
import com.oss.framework.widgets.table.OldTable;
import com.oss.framework.widgets.table.TableInterface;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class MediaTerminationPage extends BasePage {

    private static final String DEVICE_FIELD_DATA_ATTRIBUTENAME = "device";
    private static final String CARD_FIELD_DATA_ATTRIBUTENAME = "card";
    private static final String PORT_FIELD_DATA_ATTRIBUTENAME = "port";
    private static final String CABLE_BUTTONS_WIZARD_ID = "cable-table-buttons";
    private static final String START_TERMINATION_WIZARD_ID = "cable-termination-table-start-termination";
    private static final String END_TERMINATION_WIZARD_ID = "cable-termination-table-end-termination";
    private static final String MEDIA_TABLE_ID = "cableterminationconnectorstable_table";
    private static final String UPDATE_CABLE_ID = "cable-table-buttons-2";
    private static final String WIZARD_ID = "Popup";

    public MediaTerminationPage(WebDriver driver) {
        super(driver);
    }

    @Step("Click Update cable")
    public void clickUpdateCable() {
        getCableButtonsWizard().clickButtonById(UPDATE_CABLE_ID);
    }

    @Step("Click Finish")
    public void clickFinish() {
        getWizard().clickButtonByLabel("Finish");
    }

    @Step("Selects first location from Media Table")
    public void setFirstLocation() {
        getMediaTable().selectFirstRow();
    }

    @Step("Select Device for Start Termination")
    public void setDeviceStart(String deviceName) {
        getStartTerminationWidget().setComponentValue(DEVICE_FIELD_DATA_ATTRIBUTENAME, deviceName, Input.ComponentType.SEARCH_FIELD);
    }

    @Step("Select Device for End Termination")
    public void setDeviceEnd(String deviceName) {
        getEndTerminationWidget().setComponentValue(DEVICE_FIELD_DATA_ATTRIBUTENAME, deviceName, Input.ComponentType.SEARCH_FIELD);
    }

    @Step("Select Card for Start Termination")
    public void setCardStart(String cardName) {
        getStartTerminationWidget().setComponentValue(CARD_FIELD_DATA_ATTRIBUTENAME, cardName, Input.ComponentType.SEARCH_FIELD);
    }

    @Step("Select Card for End Termination")
    public void setCardEnd(String cardName) {
        getEndTerminationWidget().setComponentValue(CARD_FIELD_DATA_ATTRIBUTENAME, cardName, Input.ComponentType.SEARCH_FIELD);
    }

    @Step("Select Port for Start Termination")
    public void setPortStart(String portName) {
        getStartTerminationWidget().setComponentValue(PORT_FIELD_DATA_ATTRIBUTENAME, portName, Input.ComponentType.SEARCH_FIELD);
    }

    @Step("Select Port for End Termination")
    public void setPortEnd(String portName) {
        getEndTerminationWidget().setComponentValue(PORT_FIELD_DATA_ATTRIBUTENAME, portName, Input.ComponentType.SEARCH_FIELD);
    }

    public Wizard getCableButtonsWizard() {
        return Wizard.createByComponentId(driver, wait, CABLE_BUTTONS_WIZARD_ID);
    }

    public Wizard getStartTerminationWidget() {
        return Wizard.createByComponentId(driver, wait, START_TERMINATION_WIZARD_ID);
    }

    public Wizard getEndTerminationWidget() {
        return Wizard.createByComponentId(driver, wait, END_TERMINATION_WIZARD_ID);
    }

    private TableInterface getMediaTable() {
        return OldTable.createByComponentDataAttributeName(driver, wait, MEDIA_TABLE_ID);
    }

    private Wizard getWizard() {
        return Wizard.createByComponentId(driver, wait, WIZARD_ID);
    }
}

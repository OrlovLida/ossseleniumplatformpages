package com.oss.pages.physical;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.prompts.ConfirmationBox;
import com.oss.framework.prompts.ConfirmationBoxInterface;
import com.oss.framework.widgets.Wizard;
import com.oss.framework.widgets.tablewidget.OldTable;
import com.oss.framework.widgets.tablewidget.TableInterface;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

public class MediaTerminationPage extends BasePage {

    private static final String DEVICE_FIELD_DATA_ATTRIBUTENAME = "device";
    private static final String CARD_FIELD_DATA_ATTRIBUTENAME = "card";
    private static final String PORT_FIELD_DATA_ATTRIBUTENAME = "port";

    public MediaTerminationPage(WebDriver driver) { super(driver); }

    @Step("Click Update cable")
    public void clickUpdateCable() {
        getCommonButtonsWidget().clickButtonByLabel("Update cable");
    }

    @Step("Click Finish")
    public void clickFinish() { getWizard().clickButtonByLabel("Finish"); }

    @Step("Selects first location from Media Table")
    public void setFirstLocation() { getMediaTable().selectFirstRow(); }

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
        getStartTerminationWidget().setComponentValue(CARD_FIELD_DATA_ATTRIBUTENAME, portName, Input.ComponentType.SEARCH_FIELD);
    }

    @Step("Select Port for End Termination")
    public void setPortEnd(String portName) {
        getEndTerminationWidget().setComponentValue(CARD_FIELD_DATA_ATTRIBUTENAME, portName, Input.ComponentType.SEARCH_FIELD);
    }

    //TODO: temporary method createWizardByClassArrayIndex due to OSSWEB-9886
    public Wizard getCommonButtonsWidget() {
        return Wizard.createWizardByClassArrayIndex(driver, wait, "4");
    }

    //TODO: temporary method createWizardByHeader due to OSSWEB-9886
    public Wizard getStartTerminationWidget() {
        return Wizard.createWizardByHeaderText(driver, wait, "Start Termination");
    }

    //TODO: temporary method createWizardByHeader due to OSSWEB-9886
    public Wizard getEndTerminationWidget() {
        return Wizard.createWizardByHeaderText(driver, wait, "End Termination");
    }

    public TableInterface getMediaTable() {
        return OldTable.createByComponentDataAttributeName(driver, wait, "cableterminationconnectorstable_table");
    }

    public Wizard getWizard() { return Wizard.createByComponentId(driver, wait, "Popup"); }
}

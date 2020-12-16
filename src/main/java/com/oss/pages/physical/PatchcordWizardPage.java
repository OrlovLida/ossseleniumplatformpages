package com.oss.pages.physical;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.prompts.ConfirmationBox;
import com.oss.framework.prompts.ConfirmationBoxInterface;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.framework.widgets.tablewidget.OldTable;
import com.oss.framework.widgets.tablewidget.TableInterface;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

public class PatchcordWizardPage extends BasePage {

    private static final String DEVICE_FIELD_DATA_ATTRIBUTENAME = "device";
    private static final String CARD_FIELD_DATA_ATTRIBUTENAME = "card";
    private static final String PORT_FIELD_DATA_ATTRIBUTENAME = "port";

    public PatchcordWizardPage(WebDriver driver) { super(driver); }

    @Step("Click Update patchcords")
    public void clickUpdatePatchcords() {
        getCommonButtonsWidget().clickButtonByLabel("Update patchcords");
    }

    @Step("Click Finish")
    public void clickFinish() { getWizard().clickButtonByLabel("Finish"); }

    @Step("Click Close")
    public void clickClose() { getCommonButtonsWidget().clickButtonByLabel("Close"); }

    @Step("Select Device for Termination A")
    public void setDeviceTerminationA(String deviceName) {
        getTerminationAWidget().setComponentValue(DEVICE_FIELD_DATA_ATTRIBUTENAME, deviceName, Input.ComponentType.SEARCH_FIELD);
    }

    @Step("Select Device for Termination B")
    public void setDeviceTerminationB(String deviceName) {
        getTerminationBWidget().setComponentValue(DEVICE_FIELD_DATA_ATTRIBUTENAME, deviceName, Input.ComponentType.SEARCH_FIELD);
    }

    @Step("Select Card for Termination A")
    public void setCardTerminationA(String cardName) {
        getTerminationAWidget().setComponentValue(CARD_FIELD_DATA_ATTRIBUTENAME, cardName, Input.ComponentType.SEARCH_FIELD);
    }

    @Step("Select Card for Termination B")
    public void setCardTerminationB(String cardName) {
        getTerminationBWidget().setComponentValue(CARD_FIELD_DATA_ATTRIBUTENAME, cardName, Input.ComponentType.SEARCH_FIELD);
    }

    @Step("Select Port for Termination A")
    public void setPortTerminationA(String portName) {
        getTerminationAWidget().setComponentValue(CARD_FIELD_DATA_ATTRIBUTENAME, portName, Input.ComponentType.SEARCH_FIELD);
    }

    @Step("Select Port for Termination B")
    public void setPortTerminationB(String portName) {
        getTerminationBWidget().setComponentValue(CARD_FIELD_DATA_ATTRIBUTENAME, portName, Input.ComponentType.SEARCH_FIELD);
    }

    @Step("Click Create single-medium patchcord")
    public void clickCreateSingleMediumPatchcord() {
        getPatchcordTable().callActionByLabel("Create single-medium patchcord");
    }

    @Step("Click Proceed from Create single-medium patchcord wizard")
    public void clickProceed() {
        Wizard.createWizard(driver, wait).clickButtonByLabel("Proceed");
    }

    @Step("Selects first Patchcord from Patchcord table")
    public void selectFirstPatchcord() { getPatchcordTable().selectFirstRow(); }

    @Step("Click Remove patchcord")
    public void clickRemovePatchcord() { getPatchcordTable().callActionByLabel("Remove patchcord"); }

    @Step("Filter and select {objectName} row")
    public PatchcordWizardPage filterPatchcordConnectorsStart(String columnName, String portName) {
        getPatchcordConnectorsTableStart().searchByAttributeWithLabel(columnName, Input.ComponentType.TEXT_FIELD, portName);
        getPatchcordConnectorsTableStart().selectRowByAttributeValueWithLabel(columnName, portName);
        return this;
    }

    @Step("Filter and select {objectName} row")
    public PatchcordWizardPage filterPatchcordConnectorsEnd(String columnName, String portName) {
        getPatchcordConnectorsTableEnd().searchByAttributeWithLabel(columnName, Input.ComponentType.TEXT_FIELD, portName);
        getPatchcordConnectorsTableEnd().selectRowByAttributeValueWithLabel(columnName, portName);
        return this;
    }

    public OldTable getPatchcordConnectorsTableStart() {
        DelayUtils.waitForPageToLoad(driver, wait);
        return OldTable.createByComponentDataAttributeName(driver, wait, "patchcord-connectors-table-start");
    }

    public OldTable getPatchcordConnectorsTableEnd() {
        DelayUtils.waitForPageToLoad(driver, wait);
        return OldTable.createByComponentDataAttributeName(driver, wait, "patchcord-connectors-table-end");
    }

    //TODO: temporary method createWizardByClassArrayIndex due to OSSWEB-9896
    public Wizard getCommonButtonsWidget() {
        return Wizard.createWizardByClassArrayIndex(driver, wait, "4");
    }

    //TODO: temporary method createWizardByHeader due to OSSWEB-9896
    public Wizard getTerminationAWidget() {
        return Wizard.createWizardByHeaderText(driver, wait, "Termination A");
    }

    //TODO: temporary method createWizardByHeader due to OSSWEB-9896
    public Wizard getTerminationBWidget() {
        return Wizard.createWizardByHeaderText(driver, wait, "Termination B");
    }

    public TableInterface getPatchcordTable() {
        return OldTable.createByComponentDataAttributeName(driver, wait, "patchcordterminationconnectorstable_table");
    }

    public Wizard getWizard() { return Wizard.createByComponentId(driver, wait, "Popup"); }
}

package com.oss.pages.physical;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.wizard.Wizard;
import com.oss.framework.widgets.table.OldTable;
import com.oss.framework.widgets.table.TableInterface;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class PatchcordWizardPage extends BasePage {

    private static final String DEVICE_ID = "device";
    private static final String CARD_ID = "card";
    private static final String UPDATE_PATCHCORDS_ID = "patchcordTerminationSubmitButtons-1";
    private static final String SUBMITS_BUTTONS_WIZARD_ID = "patchcordTerminationSubmitButtons";
    private static final String START_TERMINATION_WIZARD_ID = "patchcord-termination-table-start-termination";
    private static final String END_TERMINATION_WIZARD_ID = "patchcord-termination-table-end-termination";
    private static final String PATCHCORD_TABLE_ID = "patchcordTerminationTreeTablePatchcordId";
    private static final String START_CONNECTORS_TABLE_ID = "patchcord-connectors-table-start";
    private static final String END_CONNECTORS_TABLE_ID = "patchcord-connectors-table-end";
    private static final String WIZARD_ID = "Popup";

    public PatchcordWizardPage(WebDriver driver) {
        super(driver);
    }

    @Step("Click Update patchcords")
    public void clickUpdatePatchcords() {
        getSubmitsButtonsWizard().clickButtonById(UPDATE_PATCHCORDS_ID);
    }

    @Step("Click Finish")
    public void clickFinish() {
        getWizard().clickButtonByLabel("Finish");
    }

    @Step("Select Device for Termination A")
    public void setDeviceTerminationA(String deviceName) {
        getStartTerminationWizard().setComponentValue(DEVICE_ID, deviceName, Input.ComponentType.SEARCH_FIELD);
    }

    @Step("Select Device for Termination B")
    public void setDeviceTerminationB(String deviceName) {
        getEndTerminationWizard().setComponentValue(DEVICE_ID, deviceName, Input.ComponentType.SEARCH_FIELD);
    }

    @Step("Select Card for Termination A")
    public void setCardTerminationA(String cardName) {
        getStartTerminationWizard().setComponentValue(CARD_ID, cardName, Input.ComponentType.SEARCH_FIELD);
    }

    @Step("Select Card for Termination B")
    public void setCardTerminationB(String cardName) {
        getEndTerminationWizard().setComponentValue(CARD_ID, cardName, Input.ComponentType.SEARCH_FIELD);
    }

    @Step("Select Port for Termination A")
    public void setPortTerminationA(String portName) {
        getStartTerminationWizard().setComponentValue(CARD_ID, portName, Input.ComponentType.SEARCH_FIELD);
    }

    @Step("Select Port for Termination B")
    public void setPortTerminationB(String portName) {
        getEndTerminationWizard().setComponentValue(CARD_ID, portName, Input.ComponentType.SEARCH_FIELD);
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
    public void selectFirstPatchcord() {
        getPatchcordTable().selectFirstRow();
    }

    @Step("Click Remove patchcord")
    public void clickRemovePatchcord() {
        getPatchcordTable().callActionByLabel("Remove patchcord");
    }

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
        return OldTable.createByComponentDataAttributeName(driver, wait, START_CONNECTORS_TABLE_ID);
    }

    public OldTable getPatchcordConnectorsTableEnd() {
        DelayUtils.waitForPageToLoad(driver, wait);
        return OldTable.createByComponentDataAttributeName(driver, wait, END_CONNECTORS_TABLE_ID);
    }

    private Wizard getSubmitsButtonsWizard() {
        return Wizard.createByComponentId(driver, wait, SUBMITS_BUTTONS_WIZARD_ID);
    }

    private Wizard getStartTerminationWizard() {
        return Wizard.createByComponentId(driver, wait, START_TERMINATION_WIZARD_ID);
    }

    private Wizard getEndTerminationWizard() {
        return Wizard.createByComponentId(driver, wait, END_TERMINATION_WIZARD_ID);
    }

    private TableInterface getPatchcordTable() {
        return OldTable.createByComponentDataAttributeName(driver, wait, PATCHCORD_TABLE_ID);
    }

    private Wizard getWizard() {
        return Wizard.createByComponentId(driver, wait, WIZARD_ID);
    }
}

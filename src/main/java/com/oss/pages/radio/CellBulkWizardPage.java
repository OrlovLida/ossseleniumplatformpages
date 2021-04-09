package com.oss.pages.radio;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.listwidget.EditableList;
import com.oss.framework.listwidget.EditableList.Row;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

import static com.oss.framework.components.inputs.Input.ComponentType.CHECKBOX;
import static com.oss.framework.components.inputs.Input.ComponentType.COMBOBOX;
import static com.oss.framework.components.inputs.Input.ComponentType.TEXT_FIELD;

public class CellBulkWizardPage extends BasePage {
    private static final String USE_FIRST_AVAILABLE_ID = "useFirstAvailableId";
    private static final String USE_SAME_LOCATION_AS_BASE_STATION = "sameLocationAsBS";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_PCI = "pci";
    private static final String COLUMN_RSI = "rootSequenceIndex";
    private static final String CELLS_AMOUNT = "amountCells";
    private static final String CARRIER = "carrier-input";
    private static final String BANDWIDTH_UL = "bandwidthUl";
    private static final String BANDWIDTH_DL = "bandwidthDl";
    private static final String TX_POWER = "txPower";
    private static final String TAC = "tac";
    private static final String MIMO = "mimoMode";
    private static final String TX_DIRECTION = "txDirection";
    private static final String PCI = "pci-NUMBER_FIELD";
    private static final String RSI = "rootSequenceIndex-NUMBER_FIELD";
    private static final String NAME = "name-TEXT_FIELD";
    private static final String COLUMN_LOCAL_CELL_ID = "localCellId";
    private static final String LOCAL_CELL_ID = "localCellId-NUMBER_FIELD";
    private final Wizard wizard;

    public CellBulkWizardPage(WebDriver driver) {
        super(driver);
        wizard = Wizard.createByComponentId(driver, wait, "Popup");
    }

    @Step("Click Accept button")
    public void clickAccept() {
        wizard.clickAccept();
    }

    @Step("Click Next button")
    public void clickNext() {
        wizard.clickNext();
    }

    @Step("Create Cells in Bulk Wizard")
    public void createCellBulkWizard(int amountOfCells, String carrier, String[] cellNames) {
        setCellsAmount(String.valueOf(amountOfCells));
        setCarrier(carrier);
        clickNext();
        setFirstAvailableId();
        int rowNumber = 1;
        for (String cellName : cellNames) {
            EditableList list = EditableList.create(driver, wait);
            list.setValueByRowIndex(rowNumber - 1, cellName, COLUMN_NAME, NAME, Input.ComponentType.TEXT_FIELD);
            rowNumber++;
        }
        clickAccept();
    }

    @Step("Create Cells 5G in Bulk Wizard with default values")
    public void createCell5GBulkWizardWithDefaultValues(int amountOfCells, String carrier, String[] cellNames, int[] localCellsId) {
        setCellsAmount(String.valueOf(amountOfCells));
        setCarrier(carrier);
        setSameLocation();
        setBandwidthUl("100");
        setBandwidthDl("100");
        setTxPower("25");
        setTac("11000");
        setTxDirection("DOWNLINK_AND_UPLINK");
        setMimo("2Tx2Rx");
        clickNext();
        setFirstAvailableId();
        int rowNumber = 1;
        for (String cellName : cellNames) {
            Row row = EditableList.create(driver, wait).selectRow(rowNumber - 1);
            row.setEditableAttributeValue(cellName, COLUMN_NAME, NAME, Input.ComponentType.TEXT_FIELD);
            row.setEditableAttributeValue(String.valueOf(localCellsId[rowNumber - 1]), COLUMN_LOCAL_CELL_ID, LOCAL_CELL_ID, TEXT_FIELD);
            row.setEditableAttributeValue("5", COLUMN_PCI, PCI, Input.ComponentType.TEXT_FIELD);
            row.setEditableAttributeValue("10", COLUMN_RSI, RSI, Input.ComponentType.TEXT_FIELD);
            rowNumber++;
        }
        clickAccept();
    }

    @Step("Create Cells 4G in Bulk Wizard with default values")
    public void createCell4GBulkWizardWithDefaultValues(int amountOfCells, String carrier, String[] cellNames, int[] localCellsId) {
        setCellsAmount(String.valueOf(amountOfCells));
        setCarrier(carrier);
        setSameLocation();
        setBandwidthUl("10");
        setBandwidthDl("10");
        setTac("11000");
        setMimo("1Tx1Rx");
        clickNext();
        setFirstAvailableId();
        int rowNumber = 1;
        for (String cellName : cellNames) {
            Row row = EditableList.create(driver, wait).selectRow(rowNumber - 1);
            row.setEditableAttributeValue(cellName, COLUMN_NAME, NAME, Input.ComponentType.TEXT_FIELD);
            row.setEditableAttributeValue(String.valueOf(localCellsId[rowNumber]), COLUMN_LOCAL_CELL_ID, LOCAL_CELL_ID, TEXT_FIELD);
            row.setEditableAttributeValue("5", COLUMN_PCI, PCI, Input.ComponentType.TEXT_FIELD);
            row.setEditableAttributeValue("10", COLUMN_RSI, RSI, Input.ComponentType.TEXT_FIELD);
            rowNumber++;
        }
        clickAccept();
    }

    @Step("Set Amount of cells")
    public void setCellsAmount(String cellsAmount) {
        wizard.setComponentValue(CELLS_AMOUNT, cellsAmount, TEXT_FIELD);
    }

    @Step("Set Carrier")
    public void setCarrier(String carrier) {
        wizard.setComponentValue(CARRIER, carrier, COMBOBOX);
    }

    @Step("Set BandwidthUL")
    public void setBandwidthUl(String bandwidthUl) {
        wizard.setComponentValue(BANDWIDTH_UL, bandwidthUl, TEXT_FIELD);
    }

    @Step("Set BandwidthDL")
    public void setBandwidthDl(String bandwidthDl) {
        wizard.setComponentValue(BANDWIDTH_DL, bandwidthDl, TEXT_FIELD);
    }

    @Step("Set TX Power")
    public void setTxPower(String txPower) {
        wizard.setComponentValue(TX_POWER, txPower, TEXT_FIELD);
    }

    @Step("Set TAC")
    public void setTac(String tac) {
        wizard.setComponentValue(TAC, tac, TEXT_FIELD);
    }

    @Step("Set MIMO")
    public void setMimo(String mimo) {
        wizard.setComponentValue(MIMO, mimo, COMBOBOX);
    }

    @Step("Set TX Direction")
    public void setTxDirection(String txDirection) {
        wizard.setComponentValue(TX_DIRECTION, txDirection, COMBOBOX);
    }

    @Step("Click Same Location as Base Station checkbox")
    public void setSameLocation() {
        wizard.setComponentValue(USE_SAME_LOCATION_AS_BASE_STATION, "true", CHECKBOX);
    }

    @Step("Click Set First Available ID")
    public void setFirstAvailableId() {
        wizard.setComponentValue(USE_FIRST_AVAILABLE_ID, "true", CHECKBOX);
    }
}

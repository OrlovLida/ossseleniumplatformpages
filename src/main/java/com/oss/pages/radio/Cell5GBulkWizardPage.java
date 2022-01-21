package com.oss.pages.radio;

import com.oss.framework.widgets.list.EditableList;
import com.oss.framework.widgets.list.EditableList.Row;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

import static com.oss.framework.components.inputs.Input.ComponentType.*;

public class Cell5GBulkWizardPage extends BasePage {
    private static final String USE_FIRST_AVAILABLE_ID = "useFirstAvailableId";
    private static final String USE_SAME_LOCATION_AS_BASE_STATION = "sameLocationAsBS";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_PCI = "pci";
    private static final String COLUMN_RSI = "rootSequenceIndex";
    private static final String CELLS_AMOUNT = "amountCells";
    private static final String CARRIER = "carrier-input";
    private static final String BANDWIDTH_UL = "bandwidth_ul";
    private static final String BANDWIDTH_DL = "bandwidth_dl";
    private static final String TX_POWER = "txPower";
    private static final String TAC = "tac";
    private static final String MIMO = "mimo_mode";
    private static final String TX_DIRECTION = "txDirection";
    private static final String PCI = "pci-NUMBER_FIELD";
    private static final String RSI = "rootSequenceIndex-NUMBER_FIELD";
    private static final String NAME = "name-TEXT_FIELD";
    private static final String COLUMN_LOCAL_CELL_ID = "localCellId";
    private static final String LOCAL_CELL_ID = "localCellId-NUMBER_FIELD";
    private static final String WIZARD_ID = "cell-5g-bulk-wizard";
    private final Wizard wizard;

    public Cell5GBulkWizardPage(WebDriver driver) {
        super(driver);
        wizard = Wizard.createByComponentId(driver, wait, WIZARD_ID);
    }

    @Step("Click Accept button")
    public void clickAccept() {
        wizard.clickAccept();
    }

    @Step("Click Next button")
    public void clickNext() {
        wizard.clickNext();
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
            Row row = EditableList.create(driver, wait).getRow(rowNumber - 1);
            row.setEditableAttributeValue(cellName, COLUMN_NAME, NAME, TEXT_FIELD);
            row.setEditableAttributeValue(String.valueOf(localCellsId[rowNumber - 1]), COLUMN_LOCAL_CELL_ID, LOCAL_CELL_ID, TEXT_FIELD);
            row.setEditableAttributeValue("5", COLUMN_PCI, PCI, TEXT_FIELD);
            row.setEditableAttributeValue("10", COLUMN_RSI, RSI, TEXT_FIELD);
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

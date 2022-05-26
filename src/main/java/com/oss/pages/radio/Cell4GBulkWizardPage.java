package com.oss.pages.radio;

import org.openqa.selenium.WebDriver;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.list.EditableList;
import com.oss.framework.widgets.list.EditableList.Row;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class Cell4GBulkWizardPage extends BasePage {
    private static final String USE_FIRST_AVAILABLE_ID = "useFirstAvailableId";
    private static final String USE_SAME_LOCATION_AS_BASE_STATION = "sameLocationAsBS";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_PCI = "pci";
    private static final String COLUMN_CRP = "cellReselectionPriority";
    private static final String COLUMN_RSI = "rootSequenceIndex";
    private static final String CELLS_AMOUNT = "amountCells";
    private static final String CARRIER = "carrier-input";
    private static final String BANDWIDTH_UL = "bandwidth_ul";
    private static final String BANDWIDTH_DL = "bandwidth_dl";
    private static final String TAC = "tac";
    private static final String MIMO = "mimo_mode";
    private static final String PCI = "pci-NUMBER_FIELD";
    private static final String RSI = "rootSequenceIndex-NUMBER_FIELD";
    private static final String NAME = "name-TEXT_FIELD";
    private static final String COLUMN_LOCAL_CELL_ID = "localCellId";
    private static final String LOCAL_CELL_ID = "localCellId-NUMBER_FIELD";
    private static final String CELL_TOTAL_TX_POWER_ID = "cellTotalTxPower";
    private static final String CRP_ID = "cellReselectionPriority-NUMBER_FIELD";
    private static final String WIZARD_ID = "cell-4g-bulk-wizard_prompt-card";
    private static final String CELLS_LIST_ID = "ExtendedList-secondStep";
    private final Wizard cell4GBulkWizard;

    public Cell4GBulkWizardPage(WebDriver driver) {
        super(driver);
        cell4GBulkWizard = Wizard.createByComponentId(driver, wait, WIZARD_ID);
    }

    @Step("Click Accept button")
    public void clickAccept() {
        cell4GBulkWizard.clickAccept();
    }

    @Step("Click Next button")
    public void clickNext() {
        cell4GBulkWizard.clickNext();
    }

    @Step("Create Cells in Bulk Wizard")
    public void createCellBulkWizard(int amountOfCells, String carrier, String[] cellNames, int[] localCellsId, int crp) {
        setCellsAmount(String.valueOf(amountOfCells));
        setCarrier(carrier);
        setSameLocation();
        setBandwidthUl("10");
        setBandwidthDl("10");
        setMimo("2Tx2Rx");
        setTotalTxPower("25");
        clickNext();
        setFirstAvailableId();
        int rowNumber = amountOfCells;
        for (String cellName : cellNames) {
            Row row = EditableList.createById(driver, wait, CELLS_LIST_ID).getRow(rowNumber - 1);
            row.setValue(cellName, COLUMN_NAME, NAME);
            row.setValue(String.valueOf(crp), COLUMN_CRP, CRP_ID);
            row.setValue(String.valueOf(localCellsId[rowNumber - 1]), COLUMN_LOCAL_CELL_ID, LOCAL_CELL_ID);
            rowNumber--;
        }
        DelayUtils.waitForPageToLoad(driver, wait);
        clickAccept();
    }

    @Step("Create Cells 4G in Bulk Wizard with default values")
    public void createCell4GBulkWizardWithDefaultValues(int amountOfCells, String carrier, String[] cellNames, int[] localCellsId, int crp) {
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
            Row row = EditableList.createById(driver, wait, CELLS_LIST_ID).getRow(rowNumber - 1);
            row.setValue(cellName, COLUMN_NAME, NAME);
            row.setValue(String.valueOf(localCellsId[rowNumber]), COLUMN_LOCAL_CELL_ID, LOCAL_CELL_ID);
            row.setValue(String.valueOf(crp), COLUMN_CRP, CRP_ID);
            row.setValue("5", COLUMN_PCI, PCI);
            row.setValue("10", COLUMN_RSI, RSI);
            rowNumber++;
        }
        DelayUtils.waitForPageToLoad(driver, wait);
        clickAccept();
    }

    @Step("Set Amount of cells")
    public void setCellsAmount(String cellsAmount) {
        cell4GBulkWizard.setComponentValue(CELLS_AMOUNT, cellsAmount);
    }

    @Step("Set Carrier")
    public void setCarrier(String carrier) {
        cell4GBulkWizard.setComponentValue(CARRIER, carrier);
    }

    @Step("Set BandwidthUL")
    public void setBandwidthUl(String bandwidthUl) {
        cell4GBulkWizard.setComponentValue(BANDWIDTH_UL, bandwidthUl);
    }

    @Step("Set BandwidthDL")
    public void setBandwidthDl(String bandwidthDl) {
        cell4GBulkWizard.setComponentValue(BANDWIDTH_DL, bandwidthDl);
    }

    @Step("Set Total TX Power")
    public void setTotalTxPower(String txPower) {
        cell4GBulkWizard.setComponentValue(CELL_TOTAL_TX_POWER_ID, txPower);
    }

    @Step("Set TAC")
    public void setTac(String tac) {
        cell4GBulkWizard.setComponentValue(TAC, tac);
    }

    @Step("Set MIMO")
    public void setMimo(String mimo) {
        cell4GBulkWizard.setComponentValue(MIMO, mimo);
    }

    @Step("Click Same Location as Base Station checkbox")
    public void setSameLocation() {
        cell4GBulkWizard.setComponentValue(USE_SAME_LOCATION_AS_BASE_STATION, "true");
    }

    @Step("Click Set First Available ID")
    public void setFirstAvailableId() {
        cell4GBulkWizard.setComponentValue(USE_FIRST_AVAILABLE_ID, "true");
    }
}

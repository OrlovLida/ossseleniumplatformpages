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
    private static final String COLUMN_CRP = "cellReselectionPriority";
    private static final String CELLS_AMOUNT = "amountCells";
    private static final String CARRIER = "carrier";
    private static final String BANDWIDTH_UL = "bandwidth_ul";
    private static final String BANDWIDTH_DL = "bandwidth_dl";
    private static final String MIMO = "mimo_mode";
    private static final String NAME = "name-TEXT_FIELD";
    private static final String COLUMN_LOCAL_CELL_ID = "localCellId";
    private static final String LOCAL_CELL_ID = "localCellId-NUMBER_FIELD";
    private static final String CELL_TOTAL_TX_POWER_ID = "cellTotalTxPower";
    private static final String CRP_ID = "cellReselectionPriority-NUMBER_FIELD";
    private static final String WIZARD_ID = "cell-4g-bulk-wizard_prompt-card";
    private static final String CELLS_LIST_ID = "ExtendedList-secondStep";
    private static final String CELL_TYPE_IOT_ID = "cellTypeIot";
    private static final String NON_IOT_TYPE = "NON_IOT";

    public Cell4GBulkWizardPage(WebDriver driver) {
        super(driver);
    }

    public Wizard getWizard() {
        return Wizard.createByComponentId(driver, wait, WIZARD_ID);
    }

    @Step("Click Accept button")
    public void clickAccept() {
        getWizard().clickAccept();
    }

    @Step("Click Next button")
    public void clickNext() {
        getWizard().clickNext();
    }

    @Step("Create Cells in Bulk Wizard")
    public void createCellBulkWizard(int amountOfCells, String carrier, String[] cellNames, int[] localCellsId, int crp) {
        setCellsAmount(String.valueOf(amountOfCells));
        setCarrier(carrier);
        setCellTypeIot(NON_IOT_TYPE);
        setSameLocation();
        waitForPageToLoad();
        clickNext();
        setFirstAvailableId();
        int rowNumber = 1;
        for (String cellName : cellNames) {
            Row row = EditableList.createById(driver, wait, CELLS_LIST_ID).getRow(rowNumber - 1);
            row.setValue(cellName, COLUMN_NAME, NAME);
            row.setValue(String.valueOf(crp), COLUMN_CRP, CRP_ID);
            row.setValue(String.valueOf(localCellsId[rowNumber - 1]), COLUMN_LOCAL_CELL_ID, LOCAL_CELL_ID);
            rowNumber++;
        }
        waitForPageToLoad();
        clickAccept();
    }

    @Step("Set Amount of cells")
    public void setCellsAmount(String cellsAmount) {
        getWizard().setComponentValue(CELLS_AMOUNT, cellsAmount);
    }

    @Step("Set Cell Type IoT")
    public void setCellTypeIot(String cellTypeIot) {
        getWizard().setComponentValue(CELL_TYPE_IOT_ID, cellTypeIot);
    }

    @Step("Set Carrier")
    public void setCarrier(String carrier) {
        getWizard().setComponentValue(CARRIER, carrier);
    }

    @Step("Set BandwidthUL")
    public void setBandwidthUl(String bandwidthUl) {
        getWizard().setComponentValue(BANDWIDTH_UL, bandwidthUl);
    }

    @Step("Set BandwidthDL")
    public void setBandwidthDl(String bandwidthDl) {
        getWizard().setComponentValue(BANDWIDTH_DL, bandwidthDl);
    }

    @Step("Set Total TX Power")
    public void setTotalTxPower(String txPower) {
        getWizard().setComponentValue(CELL_TOTAL_TX_POWER_ID, txPower);
    }

    @Step("Set MIMO")
    public void setMimo(String mimo) {
        getWizard().setComponentValue(MIMO, mimo);
    }

    @Step("Click Same Location as Base Station checkbox")
    public void setSameLocation() {
        getWizard().setComponentValue(USE_SAME_LOCATION_AS_BASE_STATION, "true");
    }

    @Step("Click Set First Available ID")
    public void setFirstAvailableId() {
        getWizard().setComponentValue(USE_FIRST_AVAILABLE_ID, "true");
    }

    protected void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, wait);
    }

}

package com.oss.pages.radio;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.inputs.Button;
import com.oss.framework.widgets.list.EditableList;
import com.oss.framework.wizard.Wizard;

import io.qameta.allure.Step;

public class EditCell4GBulkWizardPage extends Cell4GBulkWizardPage {

    private static final String WIZARD_ID = "cell-bulk-wizard-4g";
    private static final String LIST_ID = "ExtendedList-editableTableId";
    private static final String PCI_DATA_ATTRIBUTE_NAME = "pci";
    private static final String RSI_DATA_ATTRIBUTE_NAME = "rootSequenceIndex";
    private static final String RS_POWER_DATA_ATTRIBUTE_NAME = "referenceSignalPower";
    private static final String TAC_NUMBER_FIELD_DATA_ATTRIBUTE_NAME = "tac-NUMBER_FIELD";
    private static final String PCI_NUMBER_FIELD_DATA_ATTRIBUTE_NAME = "pci-NUMBER_FIELD";
    private static final String RSI_NUMBER_FIELD_DATA_ATTRIBUTE_NAME = "rootSequenceIndex-NUMBER_FIELD";
    private static final String TAC_COLUMN_ID = "tac";
    private static final String PCI_COLUMN_ID = "pci";
    private static final String RSI_COLUMN_ID = "rootSequenceIndex";
    private static final String ACCEPT_BUTTON_DATA_ATTRIBUTE_NAME = "wizard-submit-button-cell-bulk-wizard-4g";

    public EditCell4GBulkWizardPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public Wizard getWizard() {
        return Wizard.createByComponentId(driver, wait, WIZARD_ID);
    }

    @Step("Set PCI")
    public void setPCIBulk(String pci) {
        getWizard().setComponentValue(PCI_DATA_ATTRIBUTE_NAME, pci);
    }

    @Step("Set RSI")
    public void setRSIBulk(String rsi) {
        getWizard().setComponentValue(RSI_DATA_ATTRIBUTE_NAME, rsi);
    }

    @Step("Set reference power")
    public void setReferencePowerBulk(String power) {
        getWizard().setComponentValue(RS_POWER_DATA_ATTRIBUTE_NAME, power);
    }

    @Step("Set TAC")
    public void setTac(int rowNumber, String tac) {
        EditableList list = EditableList.createById(driver, wait, LIST_ID);
        list.setValue(rowNumber, tac, TAC_COLUMN_ID, TAC_NUMBER_FIELD_DATA_ATTRIBUTE_NAME);
    }

    @Step("Set PCI")
    public void setPCI(int rowNumber, String pci) {
        EditableList list = EditableList.createById(driver, wait, LIST_ID);
        list.setValue(rowNumber, pci, PCI_COLUMN_ID, PCI_NUMBER_FIELD_DATA_ATTRIBUTE_NAME);
    }

    @Step("Set RSI")
    public void setRSI(int rowNumber, String rsi) {
        EditableList list = EditableList.createById(driver, wait, LIST_ID);
        list.setValue(rowNumber, rsi, RSI_COLUMN_ID, RSI_NUMBER_FIELD_DATA_ATTRIBUTE_NAME);
    }

    @Step("Click Accept button")
    public void accept() {
        Button.createById(driver, ACCEPT_BUTTON_DATA_ATTRIBUTE_NAME).click();
    }

    @Step("Edit Cells in bulk")
    public void editCellsBulk(String[] pci, String[] rsi, String referencePower, String[] tac, String mimoMode, String bandwidth, String txPower) {
        waitForPageToLoad();
        setReferencePowerBulk(referencePower);
        waitForPageToLoad();
        setMimo(mimoMode);
        waitForPageToLoad();
        setBandwidthUl(bandwidth);
        waitForPageToLoad();
        setBandwidthDl(bandwidth);
        waitForPageToLoad();
        setTotalTxPower(txPower);
        waitForPageToLoad();
        for (int i = 0; i < pci.length; i++) {
            waitForPageToLoad();
            setPCI(i, pci[i]);
            waitForPageToLoad();
            setRSI(i, rsi[i]);
            waitForPageToLoad();
            setTac(i, tac[i]);
            waitForPageToLoad();
        }
        waitForPageToLoad();
        accept();
    }

}

package com.oss.pages.radio;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.inputs.Button;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.list.EditableList;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class EditCell4GBulkWizardPage extends BasePage {

    private static final String WIZARD_ID = "cell-bulk-wizard-4g";
    private static final String LIST_ID = "ExtendedList-editableTableId";
    private static final String PCI_DATA_ATTRIBUTE_NAME = "pci";
    private static final String RSI_DATA_ATTRIBUTE_NAME = "rootSequenceIndex";
    private static final String RS_POWER_DATA_ATTRIBUTE_NAME = "referenceSignalPower";
    private static final String PA_OUTPUT_DATA_ATTRIBUTE_NAME = "paOutput";
    private static final String PA_INPUT_DATA_ATTRIBUTE_NAME = "pa-input";
    private static final String TAC_NUMBER_FIELD_DATA_ATTRIBUTE_NAME = "tac-NUMBER_FIELD";
    private static final String TAC_COLUMN_ID = "tac";
    private static final String ACCEPT_BUTTON_DATA_ATTRIBUTE_NAME = "wizard-submit-button-cell-bulk-wizard-4g";

    private final Wizard wizard;

    public EditCell4GBulkWizardPage(WebDriver driver) {
        super(driver);
        wizard = Wizard.createByComponentId(driver, wait, WIZARD_ID);
    }

    @Step("Set PCI")
    public void setPCIBulk(String pci) {
        wizard.setComponentValue(PCI_DATA_ATTRIBUTE_NAME, pci);
    }

    @Step("Set RSI")
    public void setRSIBulk(String rsi) {
        wizard.setComponentValue(RSI_DATA_ATTRIBUTE_NAME, rsi);
    }

    @Step("Set reference power")
    public void setReferencePowerBulk(String power) {
        wizard.setComponentValue(RS_POWER_DATA_ATTRIBUTE_NAME, power);
    }

    @Step("Set RSI")
    public void setTAC(int rowNumber, String tac) {
        EditableList list = EditableList.createById(driver, wait, LIST_ID);
        list.setValue(rowNumber, tac, TAC_COLUMN_ID, TAC_NUMBER_FIELD_DATA_ATTRIBUTE_NAME);
    }

    @Step("Set Pa Output")
    public void setPaOutputBulk(String pa) {
        wizard.setComponentValue(PA_OUTPUT_DATA_ATTRIBUTE_NAME, pa);
    }

    @Step("Set Pa Input")
    public void setPaInputBulk(String pa) {
        wizard.setComponentValue(PA_INPUT_DATA_ATTRIBUTE_NAME, pa);
    }

    @Step("Click Accept button")
    public void accept() {
        Button.createById(driver, ACCEPT_BUTTON_DATA_ATTRIBUTE_NAME).click();
    }

    @Step("Edit Cells in bulk")
    public void editCellsBulk(int cellsNumber, String pci, String rsi, String referencePower, String[] tac, String paOutput) {
        waitForPageToLoad();
        setPCIBulk(pci);
        waitForPageToLoad();
        setRSIBulk(rsi);
        waitForPageToLoad();
        setReferencePowerBulk(referencePower);
        waitForPageToLoad();
        for (int i = 0; i < cellsNumber; i++) {
            waitForPageToLoad();
            setTAC(i, tac[i]);
            waitForPageToLoad();
        }
        setPaInputBulk(paOutput);
        waitForPageToLoad();
        accept();
    }

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, wait);
    }
}

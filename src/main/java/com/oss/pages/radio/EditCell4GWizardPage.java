package com.oss.pages.radio;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.inputs.Button;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.listwidget.EditableList;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

import static com.oss.framework.components.inputs.Input.ComponentType.TEXT_FIELD;

public class EditCell4GWizardPage extends BasePage {

    private static final String PCI_DATA_ATTRIBUTE_NAME = "pci";
    private static final String RSI_DATA_ATTRIBUTE_NAME = "rootSequenceIndex";
    private static final String RS_POWER_DATA_ATTRIBUTE_NAME = "rsPower";
    private static final String PA_OUTPUT_DATA_ATTRIBUTE_NAME = "paOutput";
    private static final String TAC_NUMBER_FIELD_DATA_ATTRIBUTE_NAME = "tac-NUMBER_FIELD";
    private static final String TAC_COLUMN_ID = "tac";
    private static final String ACCEPT_BUTTON_DATA_ATTRIBUTE_NAME = "wizard-submit-button-cell-bulk-wizard-4g";

    private final Wizard wizard;

    public EditCell4GWizardPage(WebDriver driver) {
        super(driver);
        wizard = Wizard.createWizard(driver, wait);
    }

    @Step("Set PCI")
    public void setPCIBulk(String pci) {
        wizard.setComponentValue(PCI_DATA_ATTRIBUTE_NAME, pci, TEXT_FIELD);
    }

    @Step("Set RSI")
    public void setRSIBulk(String rsi) {
        wizard.setComponentValue(RSI_DATA_ATTRIBUTE_NAME, rsi, TEXT_FIELD);
    }

    @Step("Set reference power")
    public void setReferencePowerBulk(String power) {
        wizard.setComponentValue(RS_POWER_DATA_ATTRIBUTE_NAME, power, TEXT_FIELD);
    }

    @Step("Set RSI")
    public void setTAC(int rowNumber, String tac) {
        EditableList list = EditableList.create(driver, wait);
        list.setValueByRowIndex(rowNumber, tac, TAC_COLUMN_ID, TAC_NUMBER_FIELD_DATA_ATTRIBUTE_NAME, Input.ComponentType.TEXT_FIELD);
    }

    @Step("Set Pa Output")
    public void setPaOutputBulk(String pa) {
        wizard.setComponentValue(PA_OUTPUT_DATA_ATTRIBUTE_NAME, pa, TEXT_FIELD);
    }

    @Step("Click Accept button")
    public void accept() {
        Button.createById(driver, ACCEPT_BUTTON_DATA_ATTRIBUTE_NAME).click();
    }
}

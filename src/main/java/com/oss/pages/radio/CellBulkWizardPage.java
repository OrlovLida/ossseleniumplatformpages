package com.oss.pages.radio;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.listwidget.EditableList;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class CellBulkWizardPage extends BasePage {
    private static final String USE_FIRST_AVAILABLE_ID = "useFirstAvailableId";
    private static final String COLUMN_ID_NAME = "name";
    private final Wizard wizard;

    public CellBulkWizardPage(WebDriver driver) {
        super(driver);
        wizard = Wizard.createWizard(driver, wait);
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
    public void createCellBulkWizard(String type, int amountOfCells, String carrier, boolean useAvailableID, String[] cellNames) {
        Wizard cellBulkWizard = Wizard.createByComponentId(driver, wait, "cell-" + type.toLowerCase() + "-bulk-wizard");
        Input cellIdField = cellBulkWizard.getComponent("amountCells", Input.ComponentType.TEXT_FIELD);
        cellIdField.setSingleStringValue(String.valueOf(amountOfCells));
        Input carrierField = cellBulkWizard.getComponent("carrier-input", Input.ComponentType.COMBOBOX);
        carrierField.setSingleStringValueContains(carrier);
        clickNext();
        Input componentUseFirstAvailableID = cellBulkWizard.getComponent(USE_FIRST_AVAILABLE_ID, Input.ComponentType.CHECKBOX);
        componentUseFirstAvailableID.setSingleStringValue(String.valueOf(useAvailableID));
        int rowNumber = 1;
        for (String cellName : cellNames) {
            EditableList list = EditableList.create(driver, wait);
            list.setValue(cellName, COLUMN_ID_NAME, rowNumber, "name-TEXT_FIELD", Input.ComponentType.TEXT_FIELD);
            rowNumber++;
        }
        clickAccept();
    }
}

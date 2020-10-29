package com.oss.pages.radio;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.listwidget.EditableList;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

public class Cell4GBulkWizardPage extends BasePage {
    private static final String USE_FIRST_AVAILABLE_ID = "useFirstAvailableId";
    private static final String COLUMN_ID_NAME = "name";
    private final Wizard wizard;

    public Cell4GBulkWizardPage(WebDriver driver) {
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

    @Step("Create Cell4GBulk")
    public void createCell4GBulkWizard(String amountOfCells, String carrier4G, String useAvailableID, String[] cellNames) {
        Wizard cell4GBulkWizard = Wizard.createByComponentId(driver, wait, "cell-4g-bulk-wizard");
        Input cellIdField = cell4GBulkWizard.getComponent("amountCells", Input.ComponentType.TEXT_FIELD);
        cellIdField.setSingleStringValue(amountOfCells);
        Input carrierField = cell4GBulkWizard.getComponent("carrier-input", Input.ComponentType.COMBOBOX);
        carrierField.setSingleStringValueContains(carrier4G);
        clickNext();
        Input componentUseFirstAvailableID = cell4GBulkWizard.getComponent(USE_FIRST_AVAILABLE_ID, Input.ComponentType.CHECKBOX);
        componentUseFirstAvailableID.setSingleStringValue(useAvailableID);
        int rowNumber = 1;
        for(String cellName : cellNames) {
            EditableList list = EditableList.create(driver, wait);
            list.setValue(cellName, COLUMN_ID_NAME, rowNumber, "name-TEXT_FIELD", Input.ComponentType.TEXT_FIELD);
            rowNumber++;
        }
        clickAccept();
    }
}

package com.oss.pages.bigdata.dfe.stepwizard.commons;

import com.oss.framework.components.inputs.Input.ComponentType;
import com.oss.framework.listwidget.EditableList;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ColumnMappingPage extends BaseStepPage {

    private static final Logger log = LoggerFactory.getLogger(ColumnMappingPage.class);
    private static final String COLUMN_ROLE_INPUT_ID = "columnRole-COMBOBOX-input";
    private static final String COLUMN_DATA_ID = "columnName";
    private static final String COLUMN_ROLE_ID = "columnRole";

    public ColumnMappingPage(WebDriver driver, WebDriverWait wait, String wizardId) {
        super(driver, wait, wizardId);
    }

    public EditableList.Row getTableRow(String columnNameValue) {
        EditableList columnMappingList = EditableList.create(driver, wait);
        EditableList.Row row = columnMappingList.selectRowByAttributeValue(COLUMN_DATA_ID, columnNameValue);
        return row;
    }

    public String getColumnRoleInputId() {
        return COLUMN_ROLE_INPUT_ID;
    }

    public String getColumnRoleId() {
        return COLUMN_ROLE_ID;
    }
}

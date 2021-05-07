package com.oss.pages.bigdata.dfe.stepwizard.dimension;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.listwidget.EditableList;
import com.oss.pages.bigdata.dfe.stepwizard.commons.ColumnMappingPage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DimensionColumnMappingPage extends ColumnMappingPage {

    private static final Logger log = LoggerFactory.getLogger(DimensionColumnMappingPage.class);
    private static final String COLUMN_DATA_ID = "1_columnName";
    private static final String COLUMN_ROLE_ID = "columnRole";
    private static final String COLUMN_ROLE_INPUT_ID = "columnRole-COMBOBOX-input";

    public DimensionColumnMappingPage(WebDriver driver, WebDriverWait wait, String wizardId) {
        super(driver, wait, wizardId);
    }

    @Step("I fill Column Mapping Step. I set column role: {columnRole} and linkage to dimension table: {linkageToDimensionTable} for column: {columnNameValue}")
    public void fillDimensionColumnMappingStep(String columnNameValue, String columnRole){
        EditableList columnMappingList = EditableList.create(driver, wait);

        EditableList.Row row = columnMappingList.selectRowByAttributeValue(COLUMN_DATA_ID, columnNameValue);

        row.setEditableAttributeValue(columnRole, COLUMN_ROLE_ID, COLUMN_ROLE_INPUT_ID, Input.ComponentType.COMBOBOX);
        log.debug("Setting '{}' as column role for row with column name: {}", columnRole, columnNameValue);
        log.info("Filled Column Mapping Step");
    }
}

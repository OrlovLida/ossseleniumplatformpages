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
    private static final String LINKAGE_TO_DIMENSION_TABLE_INPUT_ID = "linkageToDimensionTable-COMBOBOX-input";
    private static final String COLUMN_DATA_ID = "columnData";
    private static final String COLUMN_ROLE_ID = "columnRole";
    private static final String LINKAGE_TO_DIMENSION_TABLE_ID = "linkageToDimensionTable";

    public ColumnMappingPage(WebDriver driver, WebDriverWait wait, String wizardId) {
        super(driver, wait, wizardId);
    }

    @Step("I fill Column Mapping Step. I set column role: {columnRole} and linkage to dimension table: {linkageToDimensionTable} for column: {columnNameValue}")
    public void fillColumnMappingStep(String columnNameValue, String columnRole, String linkageToDimensionTable){
        EditableList columnMappingList = EditableList.create(driver, wait);

        EditableList.Row row = columnMappingList.selectRowByAttributeValue(COLUMN_DATA_ID, columnNameValue);

        row.setEditableAttributeValue(columnRole, COLUMN_ROLE_ID, COLUMN_ROLE_INPUT_ID, ComponentType.COMBOBOX);
        log.debug("Setting '{}' as column role for row with column name: {}", columnRole, columnNameValue);
        row.setEditableAttributeValue(linkageToDimensionTable, LINKAGE_TO_DIMENSION_TABLE_ID, LINKAGE_TO_DIMENSION_TABLE_INPUT_ID, ComponentType.COMBOBOX);
        log.debug("Setting '{}' as linkage to dimension table for row with column name: {}", linkageToDimensionTable, columnNameValue);
        log.info("Filled Column Mapping Step");
    }

    @Step("I fill Column Mapping Step. Linkage to dimension table: {linkageToDimensionTable} for column: {columnNameValue}")
    public void fillColumnMappingStep(String columnNameValue, String linkageToDimensionTable){
        EditableList columnMappingList = EditableList.create(driver, wait);

        EditableList.Row row = columnMappingList.selectRowByAttributeValue(COLUMN_DATA_ID, columnNameValue);
        row.setEditableAttributeValue(linkageToDimensionTable, LINKAGE_TO_DIMENSION_TABLE_ID, LINKAGE_TO_DIMENSION_TABLE_INPUT_ID, ComponentType.COMBOBOX);
        log.debug("Setting '{}' as linkage to dimension table for row with column name: {}", linkageToDimensionTable, columnNameValue);
        log.info("Filled Column Mapping Step");
    }

}

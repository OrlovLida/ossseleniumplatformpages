package com.oss.pages.bigdata.dfe.stepwizard.etlprocess;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.widgets.list.EditableList;
import com.oss.pages.bigdata.dfe.stepwizard.commons.ColumnMappingPage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EtlProcessColumnMappingPage extends ColumnMappingPage {

    private static final Logger log = LoggerFactory.getLogger(EtlProcessColumnMappingPage.class);
    private static final String LINKAGE_TO_DIMENSION_TABLE_ID = "linkageToDimensionTable";
    private static final String LINKAGE_TO_DIMENSION_TABLE_INPUT_ID = "linkageToDimensionTable-COMBOBOX-input";

    public EtlProcessColumnMappingPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("I fill Column Mapping Step. I set column role: {columnRole} and linkage to dimension table: {linkageToDimensionTable} for column: {columnNameValue}")
    public void fillColumnMappingStep(String columnNameValue, String columnRole, String linkageToDimensionTable) {
        EditableList.Row row = getTableRow(columnNameValue);

        row.clearValue(getColumnRoleId(), getColumnRoleInputId(), Input.ComponentType.COMBOBOX);
        row.setEditableAttributeValue(columnRole, getColumnRoleId(), getColumnRoleInputId(), Input.ComponentType.COMBOBOX);
        log.debug("Setting '{}' as column role for row with column name: {}", columnRole, columnNameValue);
        row.clearValue(LINKAGE_TO_DIMENSION_TABLE_ID, LINKAGE_TO_DIMENSION_TABLE_INPUT_ID, Input.ComponentType.COMBOBOX);
        row.setEditableAttributeValue(linkageToDimensionTable, LINKAGE_TO_DIMENSION_TABLE_ID, LINKAGE_TO_DIMENSION_TABLE_INPUT_ID, Input.ComponentType.COMBOBOX);
        log.debug("Setting '{}' as linkage to dimension table for row with column name: {}", linkageToDimensionTable, columnNameValue);
        log.info("Filled Column Mapping Step");
    }

    @Step("I fill Column Mapping Step. Linkage to dimension table: {linkageToDimensionTable} for column: {columnNameValue}")
    public void fillColumnMappingStep(String columnNameValue, String linkageToDimensionTable) {
        EditableList.Row row = getTableRow(columnNameValue);
        row.setEditableAttributeValue(linkageToDimensionTable, LINKAGE_TO_DIMENSION_TABLE_ID, LINKAGE_TO_DIMENSION_TABLE_INPUT_ID, Input.ComponentType.COMBOBOX);
        log.debug("Setting '{}' as linkage to dimension table for row with column name: {}", linkageToDimensionTable, columnNameValue);
        log.info("Filled Column Mapping Step");
    }
}

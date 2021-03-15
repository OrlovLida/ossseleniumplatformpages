package com.oss.pages.bigdata.dfe.stepwizard.commons;

import com.oss.framework.components.inputs.Input.ComponentType;
import com.oss.framework.listwidget.EditableList;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ColumnMappingPage extends BaseStepPage {

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

        WebElement row = columnMappingList.selectRowByStaticAttributeValue(COLUMN_DATA_ID, columnNameValue);
        columnMappingList.setValue(row, COLUMN_ROLE_ID, columnRole, COLUMN_ROLE_INPUT_ID, ComponentType.COMBOBOX);
        columnMappingList.setValue(row, LINKAGE_TO_DIMENSION_TABLE_ID, linkageToDimensionTable, LINKAGE_TO_DIMENSION_TABLE_INPUT_ID, ComponentType.COMBOBOX);
    }

    @Step("I fill Column Mapping Step. Linkage to dimension table: {linkageToDimensionTable} for column: {columnNameValue}")
    public void fillColumnMappingStep(String columnNameValue, String linkageToDimensionTable){
        EditableList columnMappingList = EditableList.create(driver, wait);

        WebElement row = columnMappingList.selectRowByStaticAttributeValue(COLUMN_DATA_ID, columnNameValue);
        columnMappingList.setValue(row, LINKAGE_TO_DIMENSION_TABLE_ID, linkageToDimensionTable, LINKAGE_TO_DIMENSION_TABLE_INPUT_ID, ComponentType.COMBOBOX);
    }

}

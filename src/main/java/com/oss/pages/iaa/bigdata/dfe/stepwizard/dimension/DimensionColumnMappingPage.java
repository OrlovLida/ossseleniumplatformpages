package com.oss.pages.iaa.bigdata.dfe.stepwizard.dimension;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.widgets.list.EditableList;
import com.oss.pages.iaa.bigdata.dfe.stepwizard.commons.ColumnMappingPage;

import io.qameta.allure.Step;

public class DimensionColumnMappingPage extends ColumnMappingPage {

    private static final Logger log = LoggerFactory.getLogger(DimensionColumnMappingPage.class);
    private static final String DIMENSION_WIZARD_ID = "dimensionsWizardWindow";

    public DimensionColumnMappingPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait, DIMENSION_WIZARD_ID);
    }

    @Step("I fill Column Mapping Step. I set column role: {columnRole}")
    public void fillDimensionColumnMappingStep(String columnNameValue, String columnRole) {
        EditableList.Row row = getTableRow(columnNameValue);
        row.setValue(columnRole, getColumnRoleId(), getColumnRoleInputId(), Input.ComponentType.COMBOBOX);
        log.debug("Setting '{}' as column role for row with column name: {}", columnRole, columnNameValue);
        log.info("Filled Column Mapping Step");
    }
}

package com.oss.pages.bigdata.dfe.stepwizard.dimension;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.listwidget.EditableList;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DimensionColumnMappingPage extends com.oss.pages.bigdata.dfe.stepwizard.commons.ColumnMappingPage {

    private static final Logger log = LoggerFactory.getLogger(DimensionColumnMappingPage.class);

    public DimensionColumnMappingPage(WebDriver driver, WebDriverWait wait, String wizardId) {
        super(driver, wait, wizardId);
    }

    @Step("I fill Column Mapping Step. I set column role: {columnRole}")
    public void fillDimensionColumnMappingStep(String columnNameValue, String columnRole) {
        EditableList.Row row = getTableRow(columnNameValue);

        row.setEditableAttributeValue(columnRole, getColumnRoleId(), getColumnRoleInputId(), Input.ComponentType.COMBOBOX);
        log.debug("Setting '{}' as column role for row with column name: {}", columnRole, columnNameValue);
        log.info("Filled Column Mapping Step");
    }
}

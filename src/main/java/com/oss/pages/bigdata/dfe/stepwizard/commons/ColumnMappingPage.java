package com.oss.pages.bigdata.dfe.stepwizard.commons;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.list.EditableList;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class ColumnMappingPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(ColumnMappingPage.class);
    private static final String LIST_ID = "ExtendedList-column-mapping-data";
    private static final String COLUMN_ROLE_INPUT_ID = "columnRole";
    private static final String COLUMN_DATA_ID = "columnName";
    private static final String COLUMN_ROLE_ID = "columnRole";
    private final Wizard columnMappingWizard;

    public ColumnMappingPage(WebDriver driver, WebDriverWait wait, String wizardId) {
        super(driver, wait);
        columnMappingWizard = Wizard.createByComponentId(driver, wait, wizardId);
    }

    @Step("I click Next Step")
    public void clickNextStep() {
        DelayUtils.waitForPageToLoad(driver, wait);
        columnMappingWizard.clickNextStep();
        log.info("I click Next Step");
    }

    protected EditableList.Row getTableRow(String columnNameValue) {
        EditableList columnMappingList = EditableList.createById(driver, wait, LIST_ID);
        return columnMappingList.getRowByValue(COLUMN_DATA_ID, columnNameValue);
    }

    protected String getColumnRoleInputId() {
        return COLUMN_ROLE_INPUT_ID;
    }

    protected String getColumnRoleId() {
        return COLUMN_ROLE_ID;
    }
}
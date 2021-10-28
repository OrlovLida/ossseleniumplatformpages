package com.oss.pages.bigdata.dfe.stepwizard.commons;

import com.oss.framework.listwidget.EditableList;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ColumnMappingPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(ColumnMappingPage.class);
    private static final String COLUMN_ROLE_INPUT_ID = "columnRole-COMBOBOX-input";
    private static final String COLUMN_DATA_ID = "columnName";
    private static final String COLUMN_ROLE_ID = "columnRole";
    private final Wizard columnMappingWizard;

    public ColumnMappingPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        columnMappingWizard = Wizard.createWizard(driver, wait);
    }

    protected EditableList.Row getTableRow(String columnNameValue) {
        EditableList columnMappingList = EditableList.create(driver, wait);
        return columnMappingList.getRowByAttributeValue(COLUMN_DATA_ID, columnNameValue);
    }

    protected String getColumnRoleInputId() {
        return COLUMN_ROLE_INPUT_ID;
    }

    protected String getColumnRoleId() {
        return COLUMN_ROLE_ID;
    }

    @Step("I click Next Step")
    public void clickNextStep() {
        DelayUtils.waitForPageToLoad(driver, wait);
        columnMappingWizard.clickNextStep();
        log.info("I click Next Step");
    }
}
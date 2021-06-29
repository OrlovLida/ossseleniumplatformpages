package com.oss.pages.servicedesk.ticket.wizard;

import com.oss.framework.components.table.TableComponent;
import com.oss.framework.widgets.tablewidget.TableWidget;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MOStep {

    private static final String MO_TABLE_ID = "table-undefined_result_ManagedObject";
    private static final String MO_COMPONENT_ID = "TT_WIZARD";
    private final WebDriverWait wait;

    public MOStep(WebDriverWait wait) {
        this.wait = wait;
    }

    @Step("I enter {text} into search component")
    public void enterTextIntoSearchComponent(WebDriver driver, String text) {
        getMOTable(driver).typeIntoSearch(text);
    }

    @Step("I select {index} row in MO table")
    public void selectRowInMOTable(WebDriver driver, String index) {
        getMOTableComponent(driver).selectRow(Integer.parseInt(index));
    }

    public TableComponent getMOTableComponent(WebDriver driver) {
        return TableComponent.create(driver, wait, MO_COMPONENT_ID);
    }

    public TableWidget getMOTable(WebDriver driver) {
        return TableWidget.createById(driver, MO_TABLE_ID, wait);
    }
}

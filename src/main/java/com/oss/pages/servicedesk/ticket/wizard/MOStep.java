package com.oss.pages.servicedesk.ticket.wizard;

import com.oss.framework.components.table.TableComponent;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.tablewidget.TableWidget;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MOStep {

    private static final Logger log = LoggerFactory.getLogger(MOStep.class);

    private static final String MO_TABLE_ID = "table-undefined_result_ManagedObject";
    private static final String MO_COMPONENT_ID = "TT_WIZARD";
    private final WebDriverWait wait;

    public MOStep(WebDriverWait wait) {
        this.wait = wait;
    }

    @Step("I enter {text} into search component")
    public void enterTextIntoSearchComponent(WebDriver driver, String text) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getMOTable(driver).typeIntoSearch(text);
        log.info("Search text {}", text);
    }

    @Step("I select {index} row in MO table")
    public void selectRowInMOTable(WebDriver driver, String index) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getMOTableComponent(driver).selectRow(Integer.parseInt(index));
        log.info("Select row {} in table", index);
    }

    public TableComponent getMOTableComponent(WebDriver driver) {
        return TableComponent.create(driver, wait, MO_COMPONENT_ID);
    }

    public TableWidget getMOTable(WebDriver driver) {
        return TableWidget.createById(driver, MO_TABLE_ID, wait);
    }
}

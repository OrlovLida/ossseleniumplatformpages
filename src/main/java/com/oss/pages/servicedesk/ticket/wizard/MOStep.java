package com.oss.pages.servicedesk.ticket.wizard;

import com.oss.framework.components.table.TableComponent;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.table.TableWidget;
import com.oss.pages.servicedesk.BaseSDPage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MOStep extends BaseSDPage {

    private static final Logger log = LoggerFactory.getLogger(MOStep.class);

    private static final String MO_TABLE_ID = "table-undefined_result_ManagedObject";
    private static final String MO_COMPONENT_ID = "TT_WIZARD";

    public MOStep(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("I enter {text} into search component")
    public void enterTextIntoSearchComponent(String text) {
        DelayUtils.waitForPageToLoad(driver, wait);
        TableWidget.createById(driver, MO_TABLE_ID, wait).typeIntoSearch(text);
        log.info("Search text {}", text);
    }

    @Step("I select {index} row in MO table")
    public void selectRowInMOTable(String index) {
        DelayUtils.waitForPageToLoad(driver, wait);
        TableComponent.create(driver, wait, MO_COMPONENT_ID).selectRow(Integer.parseInt(index));
        log.info("Select row {} in table", index);
    }
}

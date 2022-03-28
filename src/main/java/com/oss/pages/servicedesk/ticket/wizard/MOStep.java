package com.oss.pages.servicedesk.ticket.wizard;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.table.TableComponent;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.table.TableWidget;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.servicedesk.BaseSDPage;

import io.qameta.allure.Step;

public class MOStep extends BaseSDPage {

    private static final Logger log = LoggerFactory.getLogger(MOStep.class);

    private static final String MO_TABLE_ID = "table-undefined_result_ManagedObject";
    private static final String MO_COMPONENT_ID = "TT_WIZARD";
    private static final String WIZARD_ID = "_WizardWindow";
    private static final String SELECTION_BAR_ID = "selection-bar-toggler-button";
    private static final String SHOW_SELECTED_ONLY_ID = "show-selected-only-button";

    public MOStep(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("I enter {text} into search component")
    public void enterTextIntoSearchComponent(String text) {
        DelayUtils.waitForPageToLoad(driver, wait);
        TableWidget.createById(driver, MO_TABLE_ID, wait).fullTextSearch(text);
        log.info("Search text {}", text);
    }

    @Step("I select {index} row in MO table")
    public void selectRowInMOTable(String index) {
        DelayUtils.waitForPageToLoad(driver, wait);
        TableComponent.create(driver, wait, MO_COMPONENT_ID).selectRow(Integer.parseInt(index));
        log.info("Select row {} in table", index);
    }

    @Step("I switch on Show All MOs")
    public void showAllMOs() {
        DelayUtils.waitForPageToLoad(driver, wait);
        Wizard wizard = Wizard.createByComponentId(driver, wait, WIZARD_ID);
        wizard.clickButtonById(SELECTION_BAR_ID);
        DelayUtils.waitForPageToLoad(driver, wait);
        wizard.clickButtonById(SHOW_SELECTED_ONLY_ID);
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Show All MO switched on");
    }

    @Step("I select {MOIdentifier} in MO table")
    public void selectObjectInMOTable(String MOIdentifier) {
        DelayUtils.waitForPageToLoad(driver, wait);
        TableComponent objectsTable = TableComponent.create(driver, wait, MO_COMPONENT_ID);
        List<String> objectsList = new ArrayList<String>();
        long numberOfMOs = TableComponent.create(driver, wait, MO_COMPONENT_ID).getVisibleRows().stream().count();
        for (int i = 0; i < numberOfMOs; i++) {
            objectsList.add(objectsTable.getCellValue(i, "identifier"));
        }
        log.info("Select MO {} in table", MOIdentifier);
        objectsTable.selectRow(objectsList.indexOf(MOIdentifier));
    }
}

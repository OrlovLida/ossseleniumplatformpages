package com.oss.pages.reconciliation;

import org.openqa.selenium.WebDriver;

import com.google.common.collect.Table;
import com.oss.framework.components.inputs.Button;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.search.AdvancedSearch;
import com.oss.framework.components.table.TableComponent;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.table.TableWidget;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;
import com.oss.pages.platform.NewInventoryViewPage;

import javafx.scene.control.Tab;

public class CreateMatchingWizardPage extends BasePage {

    private static final String WIZARD_ID = "CreateMatchingWebViewResource_prompt-card";
    private static final String TECHNICAL_OBJECT_TABLE_WIDGET = "table-technicalObjectTable";
    private static final String DIST_NAME_HEADER_ID = "toDistName";
    private static final String NEW_TABLE_ID = "table-undefined_result_PhysicalDevice";
    private final Wizard wizard;

    public CreateMatchingWizardPage(WebDriver driver) {
        super(driver);
        wizard = Wizard.createByComponentId(driver, wait, WIZARD_ID);
    }

    public void createNewIO() {
        wizard.getComponent("createNewInventoryObjectCheckboxId", Input.ComponentType.CHECKBOX)
                .click();
    }

    public void clickNext() {
//        wizard.clickNext();
        Button.createById(driver, "wizard-next-button-MatchingWizardConfig");
    }

    public void selectFirstIO() {
        DelayUtils.waitForPageToLoad(driver, wait);

//        TableComponent tableComponent = TableComponent.create(driver, wait, "table-undefined_result_PhysicalDevice");
//        tableComponent.selectRow(0);
        getTableComponent().selectRow(0);
    }

    public void clickAccept() {
        DelayUtils.waitForPageToLoad(driver, wait);
        wizard.clickAccept();
    }

    public void searchByName(String name) {
        DelayUtils.waitForPageToLoad(driver, wait);
        AdvancedSearch advancedSearch = AdvancedSearch.createByWidgetId(driver, wait, WIZARD_ID);
        advancedSearch.openSearchPanel();
        DelayUtils.waitForPageToLoad(driver, wait);
        advancedSearch.setFilter("name", name);
        DelayUtils.waitForPageToLoad(driver, wait);
        advancedSearch.clickApply();
    }

    public void selectTO(String distName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getTableComponent().getRow(distName, DIST_NAME_HEADER_ID).clickRow();
    }

    private TableWidget getTableWidget() {
        return TableWidget.createById(driver, TECHNICAL_OBJECT_TABLE_WIDGET, wait);
    }

    private TableWidget getNewTableWidget() {
        return TableWidget.createById(driver, NEW_TABLE_ID, wait);
    }

    private TableComponent getTableComponent() {
        return TableComponent.create(driver, wait, WIZARD_ID);
    }
}

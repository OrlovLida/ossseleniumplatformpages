package com.oss.pages.reconciliation;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.search.AdvancedSearch;
import com.oss.framework.components.table.TableComponent;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.table.TableWidget;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

public class CreateMatchingWizardPage extends BasePage {

    private static final String WIZARD_ID = "CreateMatchingWebViewResource_prompt-card";
    private static final String TECHNICAL_OBJECT_TABLE_WIDGET = "table-technicalObjectTable";
    private static final String DIST_NAME_HEADER_ID = "toDistName";
    private static final String NEW_TABLE_ID = "table-undefined_result_PhysicalDevice";
    private static final String CREATE_NEW_IO_ID = "createNewInventoryObjectCheckboxId";
    private final Wizard wizard;

    public CreateMatchingWizardPage(WebDriver driver) {
        super(driver);
        wizard = Wizard.createByComponentId(driver, wait, WIZARD_ID);
    }

    public void createNewIO() {
        wizard.setComponentValue(CREATE_NEW_IO_ID, "true");
    }

    public void clickNext() {
        wizard.clickNext();
    }

    public void selectFirstIO() {
        getTableComponent().selectRow(0);
    }

    public void clickAccept() {
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

    public void selectDeviceTO(String distName) {
        getTableComponent().getRow("Device@@" + distName, DIST_NAME_HEADER_ID).clickRow();
    }

    private TableComponent getTableComponent() {
        return TableComponent.create(driver, wait, WIZARD_ID);
    }
}

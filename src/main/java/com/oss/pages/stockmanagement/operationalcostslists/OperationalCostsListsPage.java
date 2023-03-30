package com.oss.pages.stockmanagement.operationalcostslists;

import com.comarch.oss.web.pages.BasePage;
import com.comarch.oss.web.pages.HomePage;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.table.OldTable;
import com.oss.framework.wizard.Wizard;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static com.oss.pages.stockmanagement.storages.StoragesPage.ASSET_MANAGEMENT;

public class OperationalCostsListsPage extends BasePage {

    private static final String OPERATIONAL_COSTS_LISTS = "Operational Costs Lists";
    private static final String OPERATIONAL_COSTS_LISTS_TABLE_ID = "stockOperationalCostsListView_tableApp";
    private static final String NAME_ATTRIBUTE_LABEL = "List Name";
    private static final String CREATE_OPERATIONAL_COSTS_LIST_ACTION_ID = "create";
    private static final String CREATE_OPERATIONAL_COSTS_LIST_WIZARD_ID = "stockCreateImportOperationalCostWizard_prompt-card";
    private static final String OPERATIONAL_COSTS_LIST_NAME_INPUT_ID = "nameId";
    private static final String OPERATIONAL_COSTS_LIST_DESCRIPTION_INPUT_ID = "descriptionId";
    private static final String WIZARD_ACCEPT_BUTTON_ID = "wizard-submit-button-createOrImportOperationalCostWizardId";
    private static final String REMOVE_OPERATIONAL_COSTS_LIST_ACTION_ID = "remove";
    private static final String REMOVE_OPERATIONAL_COSTS_LIST_BUTTON_ID = "ConfirmationBox_stockRemoveOperationalCostsListWizard_confirmationBoxApp_action_button";
    private static final String REMOVE_OPERATIONAL_COSTS_LIST_WIZARD_ID = "stockRemoveOperationalCostsListWizard_prompt-card";
    private static final String IMPORT_OPERATIONAL_COSTS_LIST_ACTION_ID = "import";
    private static final String IMPORT_OPERATIONAL_COSTS_LIST_WIZARD_ID = "stockCreateImportOperationalCostWizard_prompt-card";
    private static final String OVERRIDE_EXISTING_LIST_INPUT_ID = "checkBoxId";
    private static final String IMPORT_FILE_INPUT_ID = "chooserId";

    public OperationalCostsListsPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public static OperationalCostsListsPage goToOperationalCostsListsPage(WebDriver driver, String basicURL) {
        driver.get(String.format("%s/#/view/stock-management/operational-costs-lists?perspective=LIVE", basicURL));
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(45));
        DelayUtils.waitForPageToLoad(driver, wait);
        return new OperationalCostsListsPage(driver, wait);
    }

    public static OperationalCostsListsPage goToOperationalCostsListsPage(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(45));
        DelayUtils.waitForPageToLoad(driver, wait);
        HomePage homePage = new HomePage(driver);
        homePage.chooseFromLeftSideMenu(OPERATIONAL_COSTS_LISTS, ASSET_MANAGEMENT);
        DelayUtils.waitForPageToLoad(driver, wait);
        return new OperationalCostsListsPage(driver, wait);
    }

    private OldTable getOperationalCostsListsTable() {
        return OldTable.createById(driver, wait, OPERATIONAL_COSTS_LISTS_TABLE_ID);
    }

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public void clearAllColumnFilters() {
        getOperationalCostsListsTable().clearAllColumnValues();
    }

    public OperationalCostsListsPage selectOperationalCostsList(String operationalCostsListName) {
        OldTable table = getOperationalCostsListsTable();
        waitForPageToLoad();
        table.searchByColumn(NAME_ATTRIBUTE_LABEL, operationalCostsListName);
        table.selectFirstRow();
        waitForPageToLoad();
        return this;
    }

    private void callAction(String actionId) {
        getOperationalCostsListsTable().callAction(actionId);
        waitForPageToLoad();
    }

    public void createOperationalCostsList(String operationalCostsListName, String description) {
        callAction(CREATE_OPERATIONAL_COSTS_LIST_ACTION_ID);
        Wizard createWizard = Wizard.createByComponentId(driver, wait, CREATE_OPERATIONAL_COSTS_LIST_WIZARD_ID);
        createWizard.setComponentValue(OPERATIONAL_COSTS_LIST_NAME_INPUT_ID, operationalCostsListName);
        if (!description.isEmpty()) {
            createWizard.setComponentValue(OPERATIONAL_COSTS_LIST_DESCRIPTION_INPUT_ID, description);
        }
        createWizard.clickButtonById(WIZARD_ACCEPT_BUTTON_ID);
        waitForPageToLoad();
        driver.navigate().refresh();
        waitForPageToLoad();
    }

    public void createOperationalCostsList(String operationalCostsListName) {
        createOperationalCostsList(operationalCostsListName, "");
    }

    public void deleteOperationalCostsList(String operationalCostsListName) {
        selectOperationalCostsList(operationalCostsListName);
        callAction(REMOVE_OPERATIONAL_COSTS_LIST_ACTION_ID);
        Wizard deleteWizard = Wizard.createByComponentId(driver, wait, REMOVE_OPERATIONAL_COSTS_LIST_WIZARD_ID);
        deleteWizard.clickButtonById(REMOVE_OPERATIONAL_COSTS_LIST_BUTTON_ID);
        waitForPageToLoad();
    }

    public void importOperationalCostsList(String operationalCostsListName, String description, String operationalCostsListFilePath, boolean overrideExistingList) {
        getOperationalCostsListsTable().callAction(IMPORT_OPERATIONAL_COSTS_LIST_ACTION_ID);
        Wizard importWizard = Wizard.createByComponentId(driver, wait, IMPORT_OPERATIONAL_COSTS_LIST_WIZARD_ID);
        importWizard.setComponentValue(OPERATIONAL_COSTS_LIST_NAME_INPUT_ID, operationalCostsListName);
        if (!description.isEmpty()) {
            importWizard.setComponentValue(OPERATIONAL_COSTS_LIST_DESCRIPTION_INPUT_ID, description);
        }
        if (overrideExistingList) {
            importWizard.setComponentValue(OVERRIDE_EXISTING_LIST_INPUT_ID, "true");
        }
        importWizard.setComponentValue(IMPORT_FILE_INPUT_ID, operationalCostsListFilePath);
        importWizard.clickButtonById(WIZARD_ACCEPT_BUTTON_ID);
        waitForPageToLoad();
        driver.navigate().refresh();
        waitForPageToLoad();
    }

    public void importOperationalCostsList(String operationalCostsListName, String operationalCostsListFilePath, boolean overrideExistingList) {
        importOperationalCostsList(operationalCostsListName, "", operationalCostsListFilePath, overrideExistingList);
    }

    public OperationalCostsItemsPage openOperationalCostsList(String operationalCostsListName) {
        selectOperationalCostsList(operationalCostsListName);
        getOperationalCostsListsTable().clickLink("");
        return new OperationalCostsItemsPage(driver, wait);
    }
}

package com.oss.pages.stockmanagement.pricinglists;

import com.comarch.oss.web.pages.BasePage;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.table.OldTable;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.platform.HomePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static com.oss.pages.stockmanagement.storages.StoragesPage.ASSET_MANAGEMENT;

public class PricingListsPage extends BasePage {
    private static final String PRICING_LISTS = "Pricing Lists";
    private static final String PRICING_LISTS_TABLE_ID = "stockPricingListsView_tableApp";
    private static final String NAME_ATTRIBUTE_LABEL = "List Name";
    private static final String CREATE_PRICING_LIST_ACTION_ID = "create";
    private static final String IMPORT_PRICING_LIST_ACTION_ID = "import";
    private static final String REMOVE_PRICING_LIST_ACTION_ID = "remove";
    private static final String CREATE_PRICING_LIST_WIZARD_ID = "stockCreatePricingListWizard_prompt-card";
    private static final String PRICING_LIST_NAME_INPUT_ID = "nameFieldId";
    private static final String PRICING_LIST_DESCRIPTION_INPUT_ID = "descriptionFieldId";
    private static final String CREATE_WIZARD_PROCEED_BUTTON_ID = "stockCreatePricingListWizard_buttonsApp-1";
    private static final String IMPORT_WIZARD_PROCEED_BUTTON_ID = "stockImportPricingListWizard_buttonsApp-1";
    private static final String REMOVE_PRICING_LIST_WIZARD_ID = "stockRemovePricingListWizard_prompt-card";
    private static final String REMOVE_PRICING_LIST_BUTTON_ID = "ConfirmationBox_stockRemovePricingListWizard_confirmBox_action_button";
    private static final String IMPORT_PRICING_LIST_WIZARD_ID = "stockImportPricingListWizard_prompt-card";
    private static final String OVERRIDE_EXISTING_LIST_INPUT_ID = "checkBoxId";
    private static final String IMPORT_FILE_INPUT_ID = "importFieldId";


    public PricingListsPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public static PricingListsPage goToPricingListsPage(WebDriver driver, String basicURL) {
        driver.get(String.format("%s/#/view/stock-management/pricing-lists?perspective=LIVE", basicURL));
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(45));
        DelayUtils.waitForPageToLoad(driver, wait);
        return new PricingListsPage(driver, wait);
    }

    public static PricingListsPage goToPricingListsPage(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(45));
        DelayUtils.waitForPageToLoad(driver, wait);
        HomePage homePage = new HomePage(driver);
        homePage.chooseFromLeftSideMenu(PRICING_LISTS, ASSET_MANAGEMENT);
        DelayUtils.waitForPageToLoad(driver, wait);
        return new PricingListsPage(driver, wait);
    }

    private OldTable getPricingListsTable() {
        return OldTable.createById(driver, wait, PRICING_LISTS_TABLE_ID);
    }

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public void clearAllColumnFilters() {
        getPricingListsTable().clearAllColumnValues();
    }

    public PricingListsPage selectPricingList(String pricingListName) {
        OldTable table = getPricingListsTable();
        waitForPageToLoad();
        table.searchByAttributeWithLabel(NAME_ATTRIBUTE_LABEL, Input.ComponentType.TEXT_FIELD, pricingListName);
        table.selectFirstRow();
        waitForPageToLoad();
        return this;
    }

    private void callAction(String actionId) {
        getPricingListsTable().callAction(actionId);
        waitForPageToLoad();
    }

    public void createPricingList(String pricingListName, String description) {
        callAction(CREATE_PRICING_LIST_ACTION_ID);
        Wizard createWizard = Wizard.createByComponentId(driver, wait, CREATE_PRICING_LIST_WIZARD_ID);
        createWizard.setComponentValue(PRICING_LIST_NAME_INPUT_ID, pricingListName);
        if (!description.isEmpty()) {
            createWizard.setComponentValue(PRICING_LIST_DESCRIPTION_INPUT_ID, description);
        }
        createWizard.clickButtonById(CREATE_WIZARD_PROCEED_BUTTON_ID);
        waitForPageToLoad();
    }

    public void createPricingList(String pricingListName) {
        createPricingList(pricingListName, "");
    }

    public void deletePricingList(String pricingListName) {
        selectPricingList(pricingListName);
        callAction(REMOVE_PRICING_LIST_ACTION_ID);
        Wizard deleteWizard = Wizard.createByComponentId(driver, wait, REMOVE_PRICING_LIST_WIZARD_ID);
        deleteWizard.clickButtonById(REMOVE_PRICING_LIST_BUTTON_ID);
        waitForPageToLoad();
    }

    public void importPricingList(String pricingListName, String description, String pricingListFilePath, boolean overrideExistingList) {
        getPricingListsTable().callAction(IMPORT_PRICING_LIST_ACTION_ID);
        Wizard importWizard = Wizard.createByComponentId(driver, wait, IMPORT_PRICING_LIST_WIZARD_ID);
        importWizard.setComponentValue(PRICING_LIST_NAME_INPUT_ID, pricingListName);
        if (!description.isEmpty()) {
            importWizard.setComponentValue(PRICING_LIST_DESCRIPTION_INPUT_ID, description);
        }
        if (overrideExistingList) {
            importWizard.setComponentValue(OVERRIDE_EXISTING_LIST_INPUT_ID, "true");
        }
        importWizard.setComponentValue(IMPORT_FILE_INPUT_ID, pricingListFilePath, Input.ComponentType.FILE_CHOOSER);
        importWizard.clickButtonById(IMPORT_WIZARD_PROCEED_BUTTON_ID);
        waitForPageToLoad();
    }

    public void importPricingList(String pricingListName, String pricingListFilePath, boolean overrideExistingList) {
        importPricingList(pricingListName, "", pricingListFilePath, overrideExistingList);
    }

    public PricingItemsPage openPricingList(String pricingListName) {
        selectPricingList(pricingListName);
        getPricingListsTable().clickLink("");
        return new PricingItemsPage(driver, wait);
    }
}

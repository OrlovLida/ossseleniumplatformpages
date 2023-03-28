package com.oss.pages.stockmanagement.operationalcostslists;

import com.comarch.oss.web.pages.BasePage;
import com.google.common.base.Preconditions;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.table.OldTable;
import com.oss.framework.wizard.Wizard;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Optional;

import static com.oss.pages.stockmanagement.pricinglists.PricingItemsPage.CURRENCY_KEY_COLUMN_LABEL;
import static com.oss.pages.stockmanagement.pricinglists.PricingItemsPage.DESCRIPTION_COLUMN_LABEL;
import static com.oss.pages.stockmanagement.pricinglists.PricingItemsPage.NAME_COLUMN_LABEL;
import static com.oss.pages.stockmanagement.pricinglists.PricingItemsPage.PRICE_COLUMN_LABEL;

public class OperationalCostsItemsPage extends BasePage {
    private static final String OPERATIONAL_COSTS_ITEMS_TABLE_ID = "stockOperationalItemsView_tableApp";
    private static final String REMOVE_OPERATIONAL_COSTS_ITEM_ACTION_ID = "remove";
    private static final String REMOVE_OPERATIONAL_COSTS_ITEM_WIZARD_ID = "stockRemoveOperationalItemWizard_prompt-card";
    private static final String REMOVE_OPERATIONAL_COSTS_ITEM_BUTTON_ID = "ConfirmationBox_stockRemoveOperationalItemWizard_confirmBoxApp_action_button";
    private static final String NULL_ARGUMENT_EXCEPTION = "Argument '%s' cannot be empty.";
    private static final String CREATE_OPERATIONAL_COSTS_ITEM_ACTION_ID = "create";
    private static final String CREATE_OPERATIONAL_COSTS_ITEM_WIZARD_ID = "stockCreateOperationalItemWizard_prompt-card";
    private static final String NAME_INPUT_ID = "nameFieldId";
    private static final String DESCRIPTION_INPUT_ID = "descriptionFieldId";
    private static final String CURRENCY_INPUT_ID = "currencyFieldId";
    private static final String PRICE_INPUT_ID = "priceFieldId";
    private static final String PROCEED_CREATE_OPERATIONAL_COSTS_ITEM_BUTTON_ID = "stockCreateOperationalItemWizard_buttonsApp-1";
    private static final String EDIT_OPERATIONAL_COSTS_ITEM_ACTION_ID = "update";
    private static final String EDIT_OPERATIONAL_COSTS_ITEM_WIZARD_ID = "stockUpdateOperationalItemWizard_prompt-card";
    private static final String PROCEED_EDIT_OPERATIONAL_COSTS_ITEM_BUTTON_ID = "stockUpdateOperationalItemWizard_buttonsApp-1";


    public OperationalCostsItemsPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    private OldTable getOperationalCostsItemsTable() {
        return OldTable.createById(driver, wait, OPERATIONAL_COSTS_ITEMS_TABLE_ID);
    }

    public void clearAllColumnFilters() {
        getOperationalCostsItemsTable().clearAllColumnValues();
    }

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public OperationalCostsItemsPage selectOperationalCostsItem(String operationalCostsItemName) {
        OldTable table = getOperationalCostsItemsTable();
        waitForPageToLoad();
        table.searchByColumn(NAME_COLUMN_LABEL, operationalCostsItemName);
        table.selectFirstRow();
        waitForPageToLoad();
        return this;
    }

    private void callAction(String actionId) {
        getOperationalCostsItemsTable().callAction(actionId);
        waitForPageToLoad();
    }

    public String getOperationalCostsItemAttribute(String attributeName) {
        OldTable table = getOperationalCostsItemsTable();
        String attributeValue = table.getCellValue(0, attributeName);
        table.unselectRow(attributeName, attributeValue);
        return attributeValue;
    }

    public OperationalCostsItem getOperationalCostsItemAttributes() {
        return OperationalCostsItem.builder()
                .name(getOperationalCostsItemAttribute(NAME_COLUMN_LABEL))
                .description(getOperationalCostsItemAttribute(DESCRIPTION_COLUMN_LABEL))
                .price(Double.valueOf(getOperationalCostsItemAttribute(PRICE_COLUMN_LABEL)))
                .currency(getOperationalCostsItemAttribute(CURRENCY_KEY_COLUMN_LABEL))
                .build();
    }

    public void removeOperationalCostsItems() {
        callAction(REMOVE_OPERATIONAL_COSTS_ITEM_ACTION_ID);
        Wizard deleteWizard = Wizard.createByComponentId(driver, wait, REMOVE_OPERATIONAL_COSTS_ITEM_WIZARD_ID);
        deleteWizard.clickButtonById(REMOVE_OPERATIONAL_COSTS_ITEM_BUTTON_ID);
        waitForPageToLoad();
    }

    public void addOperationalCostsItem(OperationalCostsItem operationalCostsItem) {
        Optional<String> name = operationalCostsItem.getName();
        Optional<Double> price = operationalCostsItem.getPrice();
        Optional<String> currency = operationalCostsItem.getCurrency();

        Preconditions.checkArgument(name.isPresent(), NULL_ARGUMENT_EXCEPTION, NAME_COLUMN_LABEL);
        Preconditions.checkArgument(price.isPresent(), NULL_ARGUMENT_EXCEPTION, PRICE_COLUMN_LABEL);
        Preconditions.checkArgument(currency.isPresent(), NULL_ARGUMENT_EXCEPTION, CURRENCY_KEY_COLUMN_LABEL);

        callAction(CREATE_OPERATIONAL_COSTS_ITEM_ACTION_ID);
        Wizard wizard = Wizard.createByComponentId(driver, wait, CREATE_OPERATIONAL_COSTS_ITEM_WIZARD_ID);
        wizard.setComponentValue(NAME_INPUT_ID, name.get());
        operationalCostsItem.getDescription().ifPresent(description -> wizard.setComponentValue(DESCRIPTION_INPUT_ID, description));
        wizard.setComponentValue(PRICE_INPUT_ID, String.valueOf(price.get()));
        wizard.setComponentValue(CURRENCY_INPUT_ID, currency.get());
        wizard.clickButtonById(PROCEED_CREATE_OPERATIONAL_COSTS_ITEM_BUTTON_ID);
        waitForPageToLoad();
    }

    public void editOperationalCostsItem(OperationalCostsItem operationalCostsItem) {
        callAction(EDIT_OPERATIONAL_COSTS_ITEM_ACTION_ID);
        Wizard wizard = Wizard.createByComponentId(driver, wait, EDIT_OPERATIONAL_COSTS_ITEM_WIZARD_ID);
        operationalCostsItem.getName().ifPresent(name -> wizard.setComponentValue(NAME_INPUT_ID, name));
        operationalCostsItem.getDescription().ifPresent(description -> wizard.setComponentValue(DESCRIPTION_INPUT_ID, description));
        operationalCostsItem.getPrice().ifPresent(price -> wizard.setComponentValue(PRICE_INPUT_ID, String.valueOf(price)));
        operationalCostsItem.getCurrency().ifPresent(currency -> wizard.setComponentValue(CURRENCY_INPUT_ID, currency));
        wizard.clickButtonById(PROCEED_EDIT_OPERATIONAL_COSTS_ITEM_BUTTON_ID);
        waitForPageToLoad();
    }
}

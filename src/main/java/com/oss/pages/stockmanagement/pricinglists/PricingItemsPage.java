package com.oss.pages.stockmanagement.pricinglists;

import com.comarch.oss.web.pages.BasePage;
import com.google.common.base.Preconditions;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.table.OldTable;
import com.oss.framework.wizard.Wizard;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class PricingItemsPage extends BasePage {
    public static final String NAME_COLUMN_LABEL = "Name";
    public static final String DESCRIPTION_COLUMN_LABEL = "Description";
    public static final String PRICE_COLUMN_LABEL = "Price";
    public static final String CURRENCY_KEY_COLUMN_LABEL = "Currency";
    private static final String CREATE_PRICING_ITEM_ACTION_ID = "create";
    private static final String EDIT_PRICING_ITEM_ACTION_ID = "edit";
    private static final String REMOVE_PRICING_ITEM_ACTION_ID = "remove";
    private static final String PRICING_ITEMS_TABLE_ID = "stockPricingItemsView_tableApp";
    private static final String REMOVE_PRICING_ITEM_WIZARD_ID = "stockRemovePricingItemsWizard_prompt-card";
    private static final String REMOVE_PRICING_ITEM_BUTTON_ID = "ConfirmationBox_stockRemovePricingItemsWizard_confirmBox_action_button";
    private static final String PROCEED_CREATE_PRICING_ITEM_BUTTON_ID = "stockCreatePricingItemsWizard_buttonsApp-1";
    private static final String PROCEED_ADD_MATERIAL_KEY_BUTTON_ID = "stockAddMaterialKeyElementPricingItemWizard_addButtonsApp-0";
    private static final String NULL_ARGUMENT_EXCEPTION = "Argument '%s' cannot be empty.";
    private static final String CREATE_PRICING_ITEM_WIZARD_ID = "stockCreatePricingItemsWizard_prompt-card";
    private static final String MATERIAL_KEY_NAME_INPUT_ID = "nameFieldId";
    private static final String NAME_INPUT_ID = "nameField";
    private static final String DESCRIPTION_INPUT_ID = "descriptionField";
    private static final String PRICE_INPUT_ID = "priceField";
    private static final String CURRENCY_INPUT_ID = "currencyField";
    private static final String ADD_MATERIAL_KEY_BUTTON_LABEL = "Add Material Key element";
    private static final String ADD_MATERIAL_KEY_WIZARD_ID = "stockAddMaterialKeyElementPricingItemWizard_prompt-card";
    private static final String EDIT_PRICING_ITEM_WIZARD_ID = "stockEditPricingItemsWizard_prompt-card";
    private static final String PROCEED_EDIT_PRICING_ITEM_BUTTON_ID = "stockEditPricingItemsWizard_buttonsApp-1";

    public PricingItemsPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    private OldTable getPricingItemsTable() {
        return OldTable.createById(driver, wait, PRICING_ITEMS_TABLE_ID);
    }

    public void clearAllColumnFilters() {
        getPricingItemsTable().clearAllColumnValues();
    }

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public PricingItemsPage selectPricingItem(String pricingItemName) {
        OldTable table = getPricingItemsTable();
        waitForPageToLoad();
        table.searchByColumn(NAME_COLUMN_LABEL, pricingItemName);
        table.selectFirstRow();
        waitForPageToLoad();
        return this;
    }

    private void callAction(String actionId) {
        getPricingItemsTable().callAction(actionId);
        waitForPageToLoad();
    }

    public String getPricingItemAttribute(String attributeName) {
        OldTable table = getPricingItemsTable();
        String attributeValue = table.getCellValue(0, attributeName);
        table.unselectRow(attributeName, attributeValue);
        return attributeValue;
    }

    public PricingItem getPricingItemAttributes() {
        List<String> materialKeyColumns = getPricingItemsTable().getColumnsHeaders().stream().skip(4).collect(Collectors.toList());
        Map<String, String> materialKeys = new HashMap<>();
        if (!materialKeyColumns.isEmpty()) {
            materialKeyColumns.forEach(column -> materialKeys.put(column, getPricingItemAttribute(column)));
        }
        return PricingItem.builder()
                .name(getPricingItemAttribute(NAME_COLUMN_LABEL))
                .description(getPricingItemAttribute(DESCRIPTION_COLUMN_LABEL))
                .price(Double.valueOf(getPricingItemAttribute(PRICE_COLUMN_LABEL)))
                .currency(getPricingItemAttribute(CURRENCY_KEY_COLUMN_LABEL))
                .materialKeys(materialKeys)
                .build();
    }

    public void removePricingItems() {
        callAction(REMOVE_PRICING_ITEM_ACTION_ID);
        Wizard deleteWizard = Wizard.createByComponentId(driver, wait, REMOVE_PRICING_ITEM_WIZARD_ID);
        deleteWizard.clickButtonById(REMOVE_PRICING_ITEM_BUTTON_ID);
        waitForPageToLoad();
    }

    private Wizard fillMaterialKeys(Map<String, String> materialKeys, String wizardId) {
        Wizard wizard = Wizard.createByComponentId(driver, wait, wizardId);
        for (Map.Entry<String, String> materialKey : materialKeys.entrySet()) {
            if (wizard.isElementPresentById(materialKey.getKey())) {
                wizard.setComponentValue(materialKey.getKey(), materialKey.getValue());
            } else {
                wizard.clickButtonByLabel(ADD_MATERIAL_KEY_BUTTON_LABEL);
                waitForPageToLoad();
                Wizard addMaterialKeyWizard = Wizard.createByComponentId(driver, wait, ADD_MATERIAL_KEY_WIZARD_ID);
                addMaterialKeyWizard.setComponentValue(MATERIAL_KEY_NAME_INPUT_ID, materialKey.getKey());
                addMaterialKeyWizard.clickButtonById(PROCEED_ADD_MATERIAL_KEY_BUTTON_ID);
                waitForPageToLoad();
                wizard = Wizard.createByComponentId(driver, wait, wizardId);
                wizard.setComponentValue(materialKey.getKey(), materialKey.getValue());
            }
        }
        return wizard;
    }

    public void addPricingItem(PricingItem pricingItem) {
        Optional<String> name = pricingItem.getName();
        Optional<Double> price = pricingItem.getPrice();
        Optional<String> currency = pricingItem.getCurrency();
        Optional<Map<String, String>> materialKeys = pricingItem.getMaterialKeys();

        Preconditions.checkArgument(name.isPresent(), NULL_ARGUMENT_EXCEPTION, NAME_COLUMN_LABEL);
        Preconditions.checkArgument(price.isPresent(), NULL_ARGUMENT_EXCEPTION, PRICE_COLUMN_LABEL);
        Preconditions.checkArgument(currency.isPresent(), NULL_ARGUMENT_EXCEPTION, CURRENCY_KEY_COLUMN_LABEL);
        Preconditions.checkArgument(materialKeys.isPresent(), NULL_ARGUMENT_EXCEPTION, "materialKeys");

        callAction(CREATE_PRICING_ITEM_ACTION_ID);
        Wizard wizard = Wizard.createByComponentId(driver, wait, CREATE_PRICING_ITEM_WIZARD_ID);
        wizard.setComponentValue(NAME_INPUT_ID, name.get());
        pricingItem.getDescription().ifPresent(description -> wizard.setComponentValue(DESCRIPTION_INPUT_ID, description));
        wizard.setComponentValue(PRICE_INPUT_ID, String.valueOf(price.get()));
        wizard.setComponentValue(CURRENCY_INPUT_ID, currency.get());
        Wizard wizard1 = fillMaterialKeys(materialKeys.get(), CREATE_PRICING_ITEM_WIZARD_ID);
        wizard1.clickButtonById(PROCEED_CREATE_PRICING_ITEM_BUTTON_ID);
        waitForPageToLoad();
        driver.navigate().refresh();
        waitForPageToLoad();
    }

    public void editPricingItem(PricingItem pricingItem) {
        callAction(EDIT_PRICING_ITEM_ACTION_ID);
        Wizard wizard = Wizard.createByComponentId(driver, wait, EDIT_PRICING_ITEM_WIZARD_ID);
        pricingItem.getName().ifPresent(name -> wizard.setComponentValue(NAME_INPUT_ID, name));
        pricingItem.getDescription().ifPresent(description -> wizard.setComponentValue(DESCRIPTION_INPUT_ID, description));
        pricingItem.getPrice().ifPresent(price -> wizard.setComponentValue(PRICE_INPUT_ID, String.valueOf(price)));
        pricingItem.getCurrency().ifPresent(currency -> wizard.setComponentValue(CURRENCY_INPUT_ID, currency));
        Optional<Map<String, String>> materialKeys = pricingItem.getMaterialKeys();
        if (materialKeys.isPresent()) {
            Wizard wizard1 = fillMaterialKeys(materialKeys.get(), EDIT_PRICING_ITEM_WIZARD_ID);
            wizard1.clickButtonById(PROCEED_EDIT_PRICING_ITEM_BUTTON_ID);
            waitForPageToLoad();
            driver.navigate().refresh();
        } else {
            wizard.clickButtonById(PROCEED_EDIT_PRICING_ITEM_BUTTON_ID);
        }
        waitForPageToLoad();
    }
}

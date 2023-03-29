package com.oss.pages.stockmanagement.valorization;

import com.comarch.oss.web.pages.BasePage;
import com.comarch.oss.web.pages.HomePage;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.oss.framework.components.prompts.ConfirmationBox;
import com.oss.framework.components.table.TableComponent;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.list.CommonList;
import com.oss.framework.widgets.propertypanel.OldPropertyPanel;
import com.oss.framework.widgets.table.OldTable;
import com.oss.framework.widgets.tabs.TabsInterface;
import com.oss.framework.widgets.tabs.TabsWidget;
import com.oss.framework.widgets.treetable.OldTreeTableWidget;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.stockmanagement.assets.Asset;
import com.oss.pages.stockmanagement.pricinglists.PricingItem;
import com.oss.planning.ObjectIdentifier;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.oss.pages.stockmanagement.storages.StoragesPage.ASSET_MANAGEMENT;

public class ValorizationPage extends BasePage {
    private static final String NAME_INPUT_ID = "nameField";
    private static final String DESCRIPTION_INPUT_ID = "descriptionField";
    private static final String PRICE_INPUT_ID = "priceField";
    private static final String CURRENCY_INPUT_ID = "currencyField";
    private static final String NULL_ARGUMENT_EXCEPTION = "Argument cannot be empty.";

    public static final String NAME_COLUMN_LABEL = "Name";
    public static final String PRICE_COLUMN_LABEL = "Price";
    public static final String CURRENCY_KEY_COLUMN_LABEL = "Currency";
    private static final String VALORIZATION = "Valorization";
    private static final String TABS_CONTAINER_ID = "stockValorizationCostsViewgeneralCostsTab";
    private static final String GENERAL_COSTS_LABEL = "General Costs";
    private static final String MATERIAL_COSTS_LABEL = "Material Costs";
    private static final String GENERAL_COSTS_TABLE_ID = "stockValorizationCostsView_generalCostsTable";
    private static final String DETAILS_LIST_ID = "stockValorizationCostsView_valorizationDetailsList";
    private static final String OPERATIONAL_COSTS_TABLE_ID = "stockValorizationCostsView_operationalCostsTable";
    private static final String MATERIAL_COSTS_TABLE_ID = "stockValorizationCostsView_materialCostsTable";
    private static final String PERCENTAGE_OUT_OF_RANGE = "Provided percentage is out of range.";
    private static final String CHANGE_BUTTON_ID = "change";
    private static final String CHANGE_LICENSE_PERCENTAGE_WIZARD_ID = "stockValorizationLicenseWizard_prompt-card";
    private static final String LICENCE_PERCENTAGE_INPUT_ID = "license_number_field_id";
    private static final String ITEM_LABEL = "Item";
    private static final String CHANGE_QUANTITY_ACTION_ID = "change_quantity";
    private static final String CHANGE_GENERAL_COST_QUANTITY_WIZARD_ID = "stockValorizationChangeGeneralCostsWizard_prompt-card";
    private static final String QUANTITY_INPUT_ID = "number_field_id";
    private static final String QUANTITY_OUT_OF_RANGE = "Provided quantity is out of range.";
    private static final String ADD_ACTION_ID = "add";
    private static final String ADD_GENERAL_COST_WIZARD_ID = "stockValorizationCreateGeneralCostsWizard_prompt-card";
    private static final String GENERAL_COST_INPUT_ID = "general_costs_form_combo_box_id";
    private static final String OBJECT_IDENTIFIER_LABEL = "Object Identifier";
    private static final String ADD_OPERATIONAL_COST_ITEM_ACTION_ID = "createMaterialCost";
    private static final String AUTO_ASSIGN_ASSET_ACTION_ID = "autoAssignAsset";
    private static final String MANUALLY_ASSIGN_ASSET_ACTION_ID = "assignAssetManually";
    private static final String UNASSIGN_ASSET_ACTION_ID = "unassignAssets";
    private static final String ASSIGNMENT_DETAILS_ACTION_ID = "showAssignmentDetails";
    private static final String ADD_OPERATIONAL_COST_ITEM_WIZARD_ID = "stockValorizationCreateOperationalCostsWizard_prompt-card";
    private static final String OPERATIONAL_COST_ITEM_NAME_INPUT_ID = "operational_Costs_form_combo_box_id";
    private static final String CHANGE_OPERATIONAL_COST_QUANTITY_WIZARD_ID = "stockValorizationChangeOperationalCostsWizard_prompt-card";
    private static final String PROCEED_LABEL = "Proceed";
    private static final String CREATE_PRICING_ITEM_ACTION_ID = "Create Pricing Item";
    private static final String CREATE_PRICING_ITEM_WIZARD_ID = "stockValorizationCreatePricingItemWizard_prompt-card";
    private static final String MANUAL_ASSIGN_ASSET_WIZARD_ID = "stockValorizationAssetAssignWizard_prompt-card";
    private static final String STORAGE_NAMES_INPUT_ID = "choosedStorageIds";
    private static final String ASSET_NAME_INPUT_ID = "assetName";
    private static final String SERIAL_NUMBER_INPUT_ID = "serialNumber";
    private static final String ASSETS_TABLE_ID = "table-stockValorizationAssetAssignWizardassetsTable";
    private static final String ASSIGNMENT_DETAILS_POPUP_ID = "stockValorizationAssetAssignmentDetailsView_prompt-card";
    private static final String ASSIGNMENT_DETAILS_PROPERTY_PANEL_ID = "propertyApp";
    private static final String ASSIGNMENT_DETAILS_CLOSE_BUTTON_ID = "buttonsApp-0";
    private static final String QUANTITY_LABEL = "Quantity";

    public ValorizationPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public static ValorizationPage goToStoragesPage(WebDriver driver, String basicURL, Long projectId) {
        driver.get(String.format("%s/#/view/stock-management/valorization" + "?project_id=%d" + "&perspective=PLAN", basicURL, projectId));
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(45));
        DelayUtils.waitForPageToLoad(driver, wait);
        return new ValorizationPage(driver, wait);
    }

    public static ValorizationPage goToStoragesPage(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(45));
        DelayUtils.waitForPageToLoad(driver, wait);
        new HomePage(driver).chooseFromLeftSideMenu(VALORIZATION, ASSET_MANAGEMENT);
        DelayUtils.waitForPageToLoad(driver, wait);
        return new ValorizationPage(driver, wait);
    }

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    private ValorizationPage selectTab(String label) {
        waitForPageToLoad();
        TabsInterface tab = TabsWidget.createById(driver, wait, TABS_CONTAINER_ID);
        tab.selectTabByLabel(label);
        waitForPageToLoad();
        return this;
    }

    public ValorizationPage openGeneralCostsTab() {
        selectTab(GENERAL_COSTS_LABEL);
        return this;
    }

    public ValorizationPage openMaterialCostsTab() {
        selectTab(MATERIAL_COSTS_LABEL);
        return this;
    }

    private OldTable getGeneralCostsTable() {
        return OldTable.createById(driver, wait, GENERAL_COSTS_TABLE_ID);
    }

    private OldTreeTableWidget getMaterialCostsTable() {
        return OldTreeTableWidget.create(driver, wait, MATERIAL_COSTS_TABLE_ID);
    }

    private OldTable getOperationalCostsTable() {
        return OldTable.createById(driver, wait, OPERATIONAL_COSTS_TABLE_ID);
    }

    private CommonList getDetailsList() {
        return CommonList.create(driver, wait, DETAILS_LIST_ID);
    }

    public void changeLicensePercentage(double percentage) {
        Preconditions.checkArgument(percentage >= 0 && percentage <= 100, PERCENTAGE_OUT_OF_RANGE, percentage);
        getDetailsList().getRows().get(0).callAction(CHANGE_BUTTON_ID);
        waitForPageToLoad();
        Wizard wizard = Wizard.createByComponentId(driver, wait, CHANGE_LICENSE_PERCENTAGE_WIZARD_ID);
        wizard.setComponentValue(LICENCE_PERCENTAGE_INPUT_ID, String.valueOf(percentage));
        wizard.clickButtonByLabel(PROCEED_LABEL);
        waitForPageToLoad();
    }

    public Map<String, String> getDetailsAttributes() {
        CommonList detailsList = getDetailsList();
        Map<String, String> attributes = Maps.newHashMap();
        detailsList.getRowHeaders().forEach(header ->
                attributes.put(header, detailsList.getRows().get(0).getValue(header)));
        return attributes;
    }

    public String getDetailsAttribute(String attributeName) {
        return getDetailsList().getRows().get(0).getValue(attributeName);
    }

    public void changeGeneralCostQuantity(String generalCostItemName, int quantity) {
        Preconditions.checkArgument(quantity >= 0, QUANTITY_OUT_OF_RANGE, quantity);
        OldTable generalCostsTable = getGeneralCostsTable();
        generalCostsTable.selectRowByAttributeValueWithLabel(ITEM_LABEL, generalCostItemName);
        generalCostsTable.callAction(CHANGE_QUANTITY_ACTION_ID);
        waitForPageToLoad();
        Wizard wizard = Wizard.createByComponentId(driver, wait, CHANGE_GENERAL_COST_QUANTITY_WIZARD_ID);
        wizard.setComponentValue(QUANTITY_INPUT_ID, String.valueOf(quantity));
        wizard.clickButtonByLabel(PROCEED_LABEL);
        waitForPageToLoad();
    }

    public void addGeneralCost(String generalCostItemName, int quantity) {
        Preconditions.checkArgument(quantity > 0, QUANTITY_OUT_OF_RANGE, quantity);
        OldTable generalCostsTable = getGeneralCostsTable();
        generalCostsTable.callAction(ADD_ACTION_ID);
        waitForPageToLoad();
        Wizard wizard = Wizard.createByComponentId(driver, wait, ADD_GENERAL_COST_WIZARD_ID);
        wizard.setComponentValue(GENERAL_COST_INPUT_ID, generalCostItemName);
        wizard.setComponentValue(QUANTITY_INPUT_ID, String.valueOf(quantity));
        wizard.clickButtonByLabel(PROCEED_LABEL);
        waitForPageToLoad();
    }

    public Map<String, String> getGeneralCostAttributes(String generalCostItemName) {
        OldTable generalCostsTable = getGeneralCostsTable();
        Map<String, String> attributes = Maps.newHashMap();
        int row = generalCostsTable.getRowNumber(generalCostItemName, ITEM_LABEL);
        generalCostsTable.getColumnsHeaders().forEach(header -> attributes.put(header, generalCostsTable.getCellValue(row, header)));
        return attributes;
    }

    public String getGeneralCostAttribute(String generalCostItemName, String attributeName) {
        OldTable generalCostsTable = getGeneralCostsTable();
        return generalCostsTable.getCellValue(generalCostsTable.getRowNumber(generalCostItemName, ITEM_LABEL), attributeName);
    }

    public ValorizationPage selectMaterialCost(ObjectIdentifier objectIdentifier) {
        OldTreeTableWidget materialCostsTable = getMaterialCostsTable();
        materialCostsTable.unselectAllNodes();
        materialCostsTable.selectNode(objectIdentifier.toString(), OBJECT_IDENTIFIER_LABEL);
        waitForPageToLoad();
        return this;
    }

    public String getMaterialCostAttribute(ObjectIdentifier objectIdentifier, String attributeName) {
        OldTreeTableWidget materialCostsTable = getMaterialCostsTable();
        return materialCostsTable.getCellValue(materialCostsTable.getRowNumber(
                objectIdentifier.toString(), OBJECT_IDENTIFIER_LABEL), attributeName);
    }

    public Map<String, String> getMaterialCostAttributes(ObjectIdentifier objectIdentifier) {
        OldTreeTableWidget materialCostsTable = getMaterialCostsTable();
        Map<String, String> attributes = Maps.newHashMap();
        int row = materialCostsTable.getRowNumber(objectIdentifier.toString(), OBJECT_IDENTIFIER_LABEL);
        materialCostsTable.getColumnsHeader().forEach(header -> attributes.put(header, materialCostsTable.getCellValue(row, header)));
        return attributes;
    }

    public void createPricingItem(ObjectIdentifier objectIdentifier, PricingItem pricingItem) {
        Optional<String> name = pricingItem.getName();
        Optional<Double> price = pricingItem.getPrice();
        Optional<String> currency = pricingItem.getCurrency();

        Preconditions.checkArgument(name.isPresent(), NULL_ARGUMENT_EXCEPTION, NAME_COLUMN_LABEL);
        Preconditions.checkArgument(price.isPresent(), NULL_ARGUMENT_EXCEPTION, PRICE_COLUMN_LABEL);
        Preconditions.checkArgument(currency.isPresent(), NULL_ARGUMENT_EXCEPTION, CURRENCY_KEY_COLUMN_LABEL);

        selectMaterialCost(objectIdentifier);
        getMaterialCostsTable().callActionById(CREATE_PRICING_ITEM_ACTION_ID);
        waitForPageToLoad();
        Wizard wizard = Wizard.createByComponentId(driver, wait, CREATE_PRICING_ITEM_WIZARD_ID);
        wizard.setComponentValue(NAME_INPUT_ID, name.get());
        pricingItem.getDescription().ifPresent(description -> wizard.setComponentValue(DESCRIPTION_INPUT_ID, description));
        wizard.setComponentValue(PRICE_INPUT_ID, String.valueOf(price.get()));
        wizard.setComponentValue(CURRENCY_INPUT_ID, currency.get());
        wizard.clickButtonByLabel(PROCEED_LABEL);
        waitForPageToLoad();
        expandMaterialCosts();
    }

    private ValorizationPage selectOperationalCost(ObjectIdentifier objectIdentifier, String operationalCostItemName) {
        selectMaterialCost(objectIdentifier);
        getOperationalCostsTable().selectRowByAttributeValueWithLabel(ITEM_LABEL, operationalCostItemName);
        waitForPageToLoad();
        return this;
    }

    public String getOperationalCostAttribute(ObjectIdentifier objectIdentifier, String operationalCostItemName, String attributeName) {
        selectMaterialCost(objectIdentifier);
        OldTable operationalCostsTable = getOperationalCostsTable();
        return operationalCostsTable.getCellValue(operationalCostsTable.getRowNumber(operationalCostItemName, ITEM_LABEL), attributeName);
    }

    public Map<String, String> getOperationalCostAttributes(ObjectIdentifier objectIdentifier, String operationalCostItemName) {
        selectMaterialCost(objectIdentifier);
        OldTable operationalCostsTable = getOperationalCostsTable();
        Map<String, String> attributes = Maps.newHashMap();
        int row = operationalCostsTable.getRowNumber(operationalCostItemName, ITEM_LABEL);
        operationalCostsTable.getColumnsHeaders().forEach(header ->
                attributes.put(header, operationalCostsTable.getCellValue(row, header)));
        return attributes;
    }

    public void addOperationalCost(ObjectIdentifier objectIdentifier, String operationalCostItemName, int quantity) {
        Preconditions.checkArgument(quantity > 0, QUANTITY_OUT_OF_RANGE, quantity);
        selectMaterialCost(objectIdentifier);
        getMaterialCostsTable().callActionById(ADD_OPERATIONAL_COST_ITEM_ACTION_ID);
        waitForPageToLoad();
        Wizard wizard = Wizard.createByComponentId(driver, wait, ADD_OPERATIONAL_COST_ITEM_WIZARD_ID);
        wizard.setComponentValue(OPERATIONAL_COST_ITEM_NAME_INPUT_ID, operationalCostItemName);
        wizard.setComponentValue(QUANTITY_INPUT_ID, String.valueOf(quantity));
        wizard.clickButtonByLabel(PROCEED_LABEL);
        waitForPageToLoad();
    }

    public void changeOperationalCostQuantity(ObjectIdentifier objectIdentifier, String operationalCostItemName, int quantity) {
        Preconditions.checkArgument(quantity >= 0, QUANTITY_OUT_OF_RANGE, quantity);
        selectOperationalCost(objectIdentifier, operationalCostItemName);
        getMaterialCostsTable().callActionById(CHANGE_QUANTITY_ACTION_ID);
        waitForPageToLoad();
        Wizard wizard = Wizard.createByComponentId(driver, wait, CHANGE_OPERATIONAL_COST_QUANTITY_WIZARD_ID);
        wizard.setComponentValue(QUANTITY_INPUT_ID, String.valueOf(quantity));
        wizard.clickButtonByLabel(PROCEED_LABEL);
        waitForPageToLoad();
    }

    public void autoAssignAsset(ObjectIdentifier objectIdentifier) {
        selectMaterialCost(objectIdentifier);
        getMaterialCostsTable().callActionById(AUTO_ASSIGN_ASSET_ACTION_ID);
        expandMaterialCosts();
    }

    public void assignAsset(ObjectIdentifier objectIdentifier, Asset asset) {
        selectMaterialCost(objectIdentifier);
        getMaterialCostsTable().callActionById(MANUALLY_ASSIGN_ASSET_ACTION_ID);
        Wizard wizard = Wizard.createByComponentId(driver, wait, MANUAL_ASSIGN_ASSET_WIZARD_ID);
        asset.getStorageName().ifPresent(storageName -> wizard.setComponentValue(STORAGE_NAMES_INPUT_ID, storageName));
        asset.getName().ifPresent(name -> wizard.setComponentValue(ASSET_NAME_INPUT_ID, name));
        asset.getSerialNumber().ifPresent(serialNumber -> wizard.setComponentValue(SERIAL_NUMBER_INPUT_ID, serialNumber));
        TableComponent assetsTable = TableComponent.createById(driver, wait, ASSETS_TABLE_ID);
        assetsTable.selectRow(0);
        wizard.clickAccept();
        waitForPageToLoad();
        expandMaterialCosts();
    }

    public Map<String, String> getAssignmentDetails(ObjectIdentifier objectIdentifier) {
        selectMaterialCost(objectIdentifier);
        getMaterialCostsTable().callActionById(ASSIGNMENT_DETAILS_ACTION_ID);
        Wizard wizard = Wizard.createByComponentId(driver, wait, ASSIGNMENT_DETAILS_POPUP_ID);
        OldPropertyPanel propertyPanel = OldPropertyPanel.createById(driver, wait, ASSIGNMENT_DETAILS_PROPERTY_PANEL_ID);
        Map<String, String> attributes = Maps.newHashMap();
        propertyPanel.getVisibleAttributes().forEach(attribute -> attributes.put(attribute, propertyPanel.getPropertyValue(attribute)));
        wizard.clickButtonById(ASSIGNMENT_DETAILS_CLOSE_BUTTON_ID);
        return attributes;
    }

    public void unassignAsset(ObjectIdentifier objectIdentifier) {
        selectMaterialCost(objectIdentifier);
        getMaterialCostsTable().callActionById(UNASSIGN_ASSET_ACTION_ID);
        ConfirmationBox confirmationBox = ConfirmationBox.create(driver, wait);
        confirmationBox.clickButtonByLabel(PROCEED_LABEL);
        expandMaterialCosts();
    }

    public ValorizationPage expandMaterialCosts() {
        OldTreeTableWidget materialCostsTable = getMaterialCostsTable();
        String materialKeyKey = materialCostsTable.getColumnsHeader().get(1);
        List<String> quantities = materialCostsTable.getAllVisibleNodes(QUANTITY_LABEL);
        List<String> materialKeyValues = new ArrayList<>();
        for (int i = 0; i < quantities.size(); i++) {
            if (Integer.parseInt(quantities.get(i)) > 1) {
                materialKeyValues.add(materialCostsTable.getCellValue(i, materialKeyKey));
            }
        }
        materialKeyValues.forEach(materialKeyValue -> materialCostsTable.expandNode(materialKeyValue, materialKeyKey));
        return this;
    }
}

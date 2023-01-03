package com.oss.pages.stockmanagement.assets;

import com.google.common.base.Preconditions;
import com.oss.framework.components.contextactions.OldActionsContainer;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.list.EditableList;
import com.oss.framework.widgets.table.OldTable;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;
import com.oss.pages.platform.HomePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.oss.pages.stockmanagement.storages.StoragesPage.ASSET_MANAGEMENT;

public class AssetsPage extends BasePage {
    public static final String NAME_COLUMN_LABEL = "Name";
    public static final String SERIAL_NUMBER_COLUMN_LABEL = "Serial Number";
    public static final String DESCRIPTION_COLUMN_LABEL = "Description";
    public static final String STATE_COLUMN_LABEL = "State";
    public static final String MATERIAL_KEY_COLUMN_LABEL = "Material key";
    public static final String OBJECT_COLUMN_LABEL = "Object";
    public static final String MODEL_COLUMN_LABEL = "Model";
    public static final String STORAGE_COLUMN_LABEL = "Storage";
    private static final String NULL_ARGUMENT_EXCEPTION = "Argument '%s' cannot be empty.";
    private static final String ASSETS = "Assets";
    private static final String GLOBAL_ASSETS_TABLE_ID = "stockGlobalAssetsView_assetsTable";
    private static final String STORAGE_ASSETS_TABLE_ID = "stockAssetsView_assetsTableId";
    private static final String IMPORT_WIZARD_ID = "stockImportAssetsWizard_prompt-card";
    private static final String REMOVE_WIZARD_ID = "stockRemoveAssetsWizard_prompt-card";
    private static final String ASSIGN_STORAGE_WIZARD_ID = "stockAssignStorageWizard_prompt-card";
    private static final String CHANGE_STATE_WIZARD_ID = "stateChangeWizardView_prompt-card";
    private static final String END_LIFE_WIZARD_ID = "stockEndLifeAssetsWizard_prompt-card";
    private static final String ASSIGN_PREVIOUS_STORAGE_WIZARD_ID = "stockAssignPreviousStorageWizard_prompt-card";
    private static final String RELEASE_WIZARD_ID = "stockReleaseAssetsWizard_prompt-card";
    private static final String CREATE_WIZARD_ID = "stockCreateAssetsWizard_prompt-card";
    private static final String MATERIAL_KEYS_LIST_ID = "assetMaterialKey";
    private static final String SYNCHRONIZE_ACTION_ID = "synchronize";
    private static final String CHANGE_STORAGE_ACTION_ID = "changeStorage";
    private static final String EDIT_ACTION_ID = "edit";
    private static final String IMPORT_ACTION_ID = "import";
    private static final String REMOVE_ACTION_ID = "remove";
    private static final String CREATE_ACTION_ID = "create";
    private static final String RELEASE_ACTION_ID = "release";
    private static final String END_LIFE_ACTION_ID = "endLife";
    private static final String CHANGE_STATE_ACTION_ID = "changeState";
    private static final String ASSIGN_PREVIOUS_STORAGE_ACTION_ID = "assignToPreviousStorage";
    private static final String ASSIGN_STORAGE_ACTION_ID = "assignToStorage";
    private static final String EDIT_GROUP_ID = "editMenu";
    private static final String OTHERS_GROUP_ID = "otherMenu";
    private static final String ACTION_AVAILABLE_STORAGE_ASSETS_VIEW_PATTERN = "Action %s is available only on Assets View with Storage Context.";
    private static final String ACTION_AVAILABLE_GLOBAL_ASSETS_VIEW_PATTERN = "Action %s is available only on Global Assets View.";
    private static final String NAME_INPUT_ID = "nameField";
    private static final String DESCRIPTION_INPUT_ID = "descriptionField";
    private static final String SERIAL_NUMBER_INPUT_ID = "serialNumberField";
    private static final String STORAGE_NAME_INPUT_ID = "selectStorageComboBoxId";
    private static final String IMPORT_FILE_INPUT_ID = "importFieldId";
    private static final String REPLACE_ASSETS_INPUT_ID = "replaceAssetsCheckBoxId";
    private static final String SELECT_STORAGE_INPUT_ID = "selectStorageComboBoxId";
    private static final String NEXT_STATE_INPUT_ID = "nextState";
    private static final String MATERIAL_KEY_KEY_COLUMN_ID = "materialKeyKey";
    private static final String MATERIAL_KEY_VALUE_COLUMN_ID = "materialKeyValue";
    private static final String MATERIAL_KEY_KEY_INPUT_ID = "materialKeyKey-TEXT_FIELD";
    private static final String MATERIAL_KEY_VALUE_INPUT_ID = "materialKeyValue-TEXT_FIELD";
    private static final String ACTION_BUTTON_ID = "ConfirmationBox_stockRemoveAssetsWizard_confirmRemoveBox_action_button";
    private final boolean isGlobalAssetsView;

    public AssetsPage(WebDriver driver, WebDriverWait wait, boolean isGlobalAssetsView) {
        super(driver, wait);
        this.isGlobalAssetsView = isGlobalAssetsView;
    }

    public static AssetsPage goToAssetsPage(WebDriver driver, String basicURL) {
        driver.get(String.format("%s/#/view/stock-management/global-assets?perspective=LIVE", basicURL));
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(45));
        DelayUtils.waitForPageToLoad(driver, wait);
        return new AssetsPage(driver, wait, true);
    }

    public static AssetsPage goToAssetsPage(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(45));
        DelayUtils.waitForPageToLoad(driver, wait);
        HomePage homePage = new HomePage(driver);
        homePage.chooseFromLeftSideMenu(ASSETS, ASSET_MANAGEMENT);
        DelayUtils.waitForPageToLoad(driver, wait);
        return new AssetsPage(driver, wait, true);
    }

    private OldTable getAssetsTable() {
        if (isGlobalAssetsView) {
            return OldTable.createById(driver, wait, GLOBAL_ASSETS_TABLE_ID);
        } else {
            return OldTable.createById(driver, wait, STORAGE_ASSETS_TABLE_ID);
        }

    }

    public void clearAllColumnFilters() {
        getAssetsTable().clearAllColumnValues();
    }

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public AssetsPage synchronizeAssets() {
        if (isGlobalAssetsView) {
            getAssetsTable().callAction(OldActionsContainer.KEBAB_GROUP_ID, SYNCHRONIZE_ACTION_ID);
        } else {
            getAssetsTable().callAction(SYNCHRONIZE_ACTION_ID);
        }
        waitForPageToLoad();
        return this;
    }

    public AssetsPage selectAsset(String attributeName, String attributeValue) {
        OldTable table = getAssetsTable();
        waitForPageToLoad();
        table.searchByAttributeWithLabel(attributeName, Input.ComponentType.TEXT_FIELD, attributeValue);
        table.selectFirstRow();
        waitForPageToLoad();
        return this;
    }

    public AssetsPage selectAsset(String serialNumber) {
        return selectAsset(SERIAL_NUMBER_COLUMN_LABEL, serialNumber);
    }

    private void callAction(String actionId) {
        getAssetsTable().callAction(actionId);
        waitForPageToLoad();
    }

    private void callAction(String groupId, String actionId) {
        getAssetsTable().callAction(groupId, actionId);
        waitForPageToLoad();
    }

    public void importAssets(String assetsFilePath, boolean replaceAssets) {
        Preconditions.checkState(!isGlobalAssetsView, ACTION_AVAILABLE_STORAGE_ASSETS_VIEW_PATTERN, IMPORT_ACTION_ID);
        callAction(IMPORT_ACTION_ID);
        Wizard importWizard = Wizard.createByComponentId(driver, wait, IMPORT_WIZARD_ID);
        importWizard.setComponentValue(REPLACE_ASSETS_INPUT_ID, String.valueOf(replaceAssets));
        importWizard.setComponentValue(IMPORT_FILE_INPUT_ID, assetsFilePath);
        importWizard.clickAccept();
        waitForPageToLoad();
    }

    public void removeAsset() {
        Preconditions.checkState(!isGlobalAssetsView, ACTION_AVAILABLE_STORAGE_ASSETS_VIEW_PATTERN, IMPORT_ACTION_ID);
        callAction(REMOVE_ACTION_ID);
        Wizard removeWizard = Wizard.createByComponentId(driver, wait, REMOVE_WIZARD_ID);
        removeWizard.clickButtonById(ACTION_BUTTON_ID);
        waitForPageToLoad();
    }

    public void release() {
        Preconditions.checkState(!isGlobalAssetsView, ACTION_AVAILABLE_STORAGE_ASSETS_VIEW_PATTERN, IMPORT_ACTION_ID);
        callAction(RELEASE_ACTION_ID);
        Wizard removeWizard = Wizard.createByComponentId(driver, wait, RELEASE_WIZARD_ID);
        removeWizard.clickButtonById(ACTION_BUTTON_ID);
        waitForPageToLoad();
    }

    public void changeAssetStorage(String newStorageName) {
        if (isGlobalAssetsView) {
            callAction(EDIT_GROUP_ID, CHANGE_STORAGE_ACTION_ID);
        } else {
            callAction(CHANGE_STORAGE_ACTION_ID);
        }
        Wizard wizard = Wizard.createByComponentId(driver, wait, ASSIGN_STORAGE_WIZARD_ID);
        wizard.setComponentValue(SELECT_STORAGE_INPUT_ID, newStorageName);
        wizard.clickAccept();
        waitForPageToLoad();
    }

    public void assignStorage(String storageName) {
        Preconditions.checkState(isGlobalAssetsView, ACTION_AVAILABLE_GLOBAL_ASSETS_VIEW_PATTERN, ASSIGN_STORAGE_ACTION_ID);
        callAction(OTHERS_GROUP_ID, ASSIGN_STORAGE_ACTION_ID);
        Wizard wizard = Wizard.createByComponentId(driver, wait, ASSIGN_STORAGE_WIZARD_ID);
        wizard.setComponentValue(SELECT_STORAGE_INPUT_ID, storageName);
        wizard.clickAccept();
        waitForPageToLoad();
    }

    public void assignPreviousStorage() {
        Preconditions.checkState(isGlobalAssetsView, ACTION_AVAILABLE_GLOBAL_ASSETS_VIEW_PATTERN, ASSIGN_PREVIOUS_STORAGE_ACTION_ID);
        callAction(OTHERS_GROUP_ID, ASSIGN_PREVIOUS_STORAGE_ACTION_ID);
        Wizard wizard = Wizard.createByComponentId(driver, wait, ASSIGN_PREVIOUS_STORAGE_WIZARD_ID);
        wizard.clickButtonById(ACTION_BUTTON_ID);
        waitForPageToLoad();
    }

    public void endLife() {
        Preconditions.checkState(isGlobalAssetsView, ACTION_AVAILABLE_GLOBAL_ASSETS_VIEW_PATTERN, END_LIFE_ACTION_ID);
        callAction(OTHERS_GROUP_ID, END_LIFE_ACTION_ID);
        Wizard wizard = Wizard.createByComponentId(driver, wait, END_LIFE_WIZARD_ID);
        wizard.clickButtonById(ACTION_BUTTON_ID);
        waitForPageToLoad();
    }

    public void changeAssetState(Asset.State state) {
        if (isGlobalAssetsView) {
            callAction(EDIT_GROUP_ID, CHANGE_STATE_ACTION_ID);
        } else {
            callAction(CHANGE_STATE_ACTION_ID);
        }
        Wizard wizard = Wizard.createByComponentId(driver, wait, CHANGE_STATE_WIZARD_ID);
        wizard.setComponentValue(NEXT_STATE_INPUT_ID, state.getLabel());
        wizard.clickAccept();
        waitForPageToLoad();
    }

    public void createAsset(Asset asset) {
        Optional<String> name = asset.getName();
        Optional<Map<String, String>> materialKeys = asset.getMaterialKeys();

        Preconditions.checkArgument(name.isPresent(), NULL_ARGUMENT_EXCEPTION, "name");
        Preconditions.checkArgument(materialKeys.isPresent(), NULL_ARGUMENT_EXCEPTION, "materialKeys");

        callAction(CREATE_ACTION_ID);
        Wizard wizard = Wizard.createByComponentId(driver, wait, CREATE_WIZARD_ID);
        wizard.setComponentValue(NAME_INPUT_ID, name.get());
        asset.getDescription().ifPresent(description -> wizard.setComponentValue(DESCRIPTION_INPUT_ID, description));
        asset.getSerialNumber().ifPresent(serialNumber -> wizard.setComponentValue(SERIAL_NUMBER_INPUT_ID, serialNumber));
        asset.getStorageName().ifPresent(storageName -> wizard.setComponentValue(STORAGE_NAME_INPUT_ID, storageName));
        EditableList materialKeysList = EditableList.createById(driver, wait, MATERIAL_KEYS_LIST_ID);
        List<EditableList.Row> visibleRows = materialKeysList.getVisibleRows();
        if (visibleRows.isEmpty()) {
            materialKeys.get().forEach((key, value) -> {
                EditableList.Row row = materialKeysList.addRow();
                row.setValue(key, MATERIAL_KEY_KEY_COLUMN_ID, MATERIAL_KEY_KEY_INPUT_ID);
                row.setValue(value, MATERIAL_KEY_VALUE_COLUMN_ID, MATERIAL_KEY_VALUE_INPUT_ID);
            });
        } else {
            materialKeys.get().forEach((key, value) -> {
                Optional<EditableList.Row> visibleRow = visibleRows.stream().filter(row ->
                        row.getCellValue(MATERIAL_KEY_KEY_COLUMN_ID).equals(key)).findFirst();
                if (visibleRow.isPresent()) {
                    visibleRow.get().setValue(value, MATERIAL_KEY_VALUE_COLUMN_ID, MATERIAL_KEY_VALUE_INPUT_ID);
                } else {
                    EditableList.Row row = materialKeysList.addRow();
                    row.setValue(key, MATERIAL_KEY_KEY_COLUMN_ID, MATERIAL_KEY_KEY_INPUT_ID);
                    row.setValue(value, MATERIAL_KEY_VALUE_COLUMN_ID, MATERIAL_KEY_VALUE_INPUT_ID);
                }
            });
        }
        waitForPageToLoad();
        wizard.clickAccept();
        waitForPageToLoad();
    }

    public void editAsset(Asset asset) {
        if (isGlobalAssetsView) {
            callAction(EDIT_GROUP_ID, EDIT_ACTION_ID);
        } else {
            callAction(EDIT_ACTION_ID);
        }
        Wizard wizard = Wizard.createByComponentId(driver, wait, CREATE_WIZARD_ID);
        asset.getName().ifPresent(name -> wizard.setComponentValue(NAME_INPUT_ID, name));
        asset.getDescription().ifPresent(description -> wizard.setComponentValue(DESCRIPTION_INPUT_ID, description));
        asset.getSerialNumber().ifPresent(serialNumber -> wizard.setComponentValue(SERIAL_NUMBER_INPUT_ID, serialNumber));
        asset.getStorageName().ifPresent(storageName -> wizard.setComponentValue(STORAGE_NAME_INPUT_ID, storageName));
        asset.getMaterialKeys().ifPresent(materialKeys -> {
            EditableList materialKeysList = EditableList.createById(driver, wait, MATERIAL_KEYS_LIST_ID);
            List<EditableList.Row> visibleRows = materialKeysList.getVisibleRows();
            materialKeys.forEach((key, value) -> {
                Optional<EditableList.Row> visibleRow = visibleRows.stream().filter(row ->
                        row.getCellValue(MATERIAL_KEY_KEY_COLUMN_ID).equals(key)).findFirst();
                if (visibleRow.isPresent()) {
                    visibleRow.get().setValue(value, MATERIAL_KEY_VALUE_COLUMN_ID, MATERIAL_KEY_VALUE_INPUT_ID);
                } else {
                    EditableList.Row row = materialKeysList.addRow();
                    row.setValue(key, MATERIAL_KEY_KEY_COLUMN_ID, MATERIAL_KEY_KEY_INPUT_ID);
                    row.setValue(value, MATERIAL_KEY_VALUE_COLUMN_ID, MATERIAL_KEY_VALUE_INPUT_ID);
                }
            });
        });
        waitForPageToLoad();
        wizard.clickAccept();
        waitForPageToLoad();
    }

    public String getAssetAttribute(String attributeName) {
        OldTable table = getAssetsTable();
        String attributeValue = table.getCellValue(0, attributeName);
        table.unselectRow(attributeName, attributeValue);
        return attributeValue;
    }

    public Asset getAssetAttributes() {
        if (isGlobalAssetsView) {
            return Asset.builder()
                    .name(getAssetAttribute(NAME_COLUMN_LABEL))
                    .description(getAssetAttribute(DESCRIPTION_COLUMN_LABEL))
                    .state(Asset.State.getStateByLabel(getAssetAttribute(STATE_COLUMN_LABEL)))
                    .serialNumber(getAssetAttribute(SERIAL_NUMBER_COLUMN_LABEL))
                    .materialKeyValues(getAssetAttribute(MATERIAL_KEY_COLUMN_LABEL))
                    .objectIdentifier(getAssetAttribute(OBJECT_COLUMN_LABEL))
                    .objectModelName(getAssetAttribute(MODEL_COLUMN_LABEL))
                    .storageName(getAssetAttribute(STORAGE_COLUMN_LABEL))
                    .build();
        } else {
            List<String> materialKeyColumns = getAssetsTable().getColumnsHeaders().stream().skip(5).collect(Collectors.toList());
            String materialKeyValue = "";
            if (!materialKeyColumns.isEmpty()) {
                List<String> materialKeyValues = materialKeyColumns.stream().map(this::getAssetAttribute).collect(Collectors.toList());
                materialKeyValue = String.join(", ", materialKeyValues);
            }
            return Asset.builder()
                    .name(getAssetAttribute(NAME_COLUMN_LABEL))
                    .description(getAssetAttribute(DESCRIPTION_COLUMN_LABEL))
                    .state(Asset.State.getStateByLabel(getAssetAttribute(STATE_COLUMN_LABEL)))
                    .serialNumber(getAssetAttribute(SERIAL_NUMBER_COLUMN_LABEL))
                    .materialKeyValues(materialKeyValue)
                    .objectModelName(getAssetAttribute(MODEL_COLUMN_LABEL))
                    .build();
        }
    }
}

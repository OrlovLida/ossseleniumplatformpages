package com.oss.pages.stockmanagement.storages;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.table.OldTable;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;
import com.oss.pages.platform.HomePage;
import com.oss.pages.stockmanagement.assets.AssetsPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class StoragesPage extends BasePage {

    public static final String ASSET_MANAGEMENT = "Asset Management";
    private static final String STORAGES = "Storages";
    private static final String STORAGES_TABLE_ID = "stockStorageView_storageViewTable";
    private static final String NAME_ATTRIBUTE_LABEL = "Name";
    private static final String CREATE_STORAGE_ACTION_ID = "create";
    private static final String EDIT_STORAGE_ACTION_ID = "edit";
    private static final String IMPORT_STORAGE_ACTION_ID = "import";
    private static final String DELETE_STORAGE_ACTION_ID = "remove";
    private static final String CREATE_STORAGE_WIZARD_ID = "stockCreateAndEditStorageWizard_prompt-card";
    private static final String EDIT_STORAGE_WIZARD_ID = "stockCreateAndEditStorageWizard_prompt-card";
    private static final String IMPORT_STORAGE_WIZARD_ID = "stockImportStorageWizard_prompt-card";
    private static final String DELETE_STORAGE_WIZARD_ID = "stockRemoveStorageWizard_prompt-card";
    private static final String STORAGE_NAME_INPUT_ID = "nameFieldId";
    private static final String STORAGE_ADDRESS_INPUT_ID = "addressFieldId";
    private static final String DELETE_STORAGE_BUTTON_ID = "ConfirmationBox_stockRemoveStorageWizard_messageBox_action_button";
    private static final String OPEN_COLUMN_NAME = "Open";
    private static final String IMPORT_INTO_SWITCHER_ID = "selectStorageExistingOrNew";
    private static final String IMPORT_FILE_INPUT_ID = "importFieldId";
    private static final String SELECT_STORAGE_INPUT_ID = "selectStorageComboBoxId";
    private static final String REPLACE_ASSETS_INPUT_ID = "replaceAssetsCheckBoxId";


    public StoragesPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public static StoragesPage goToStoragesPage(WebDriver driver, String basicURL) {
        driver.get(String.format("%s/#/view/stock-management/storages?perspective=LIVE", basicURL));
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(45));
        DelayUtils.waitForPageToLoad(driver, wait);
        return new StoragesPage(driver, wait);
    }

    public static StoragesPage goToStoragesPage(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(45));
        DelayUtils.waitForPageToLoad(driver, wait);
        HomePage homePage = new HomePage(driver);
        homePage.chooseFromLeftSideMenu(STORAGES, ASSET_MANAGEMENT);
        DelayUtils.waitForPageToLoad(driver, wait);
        return new StoragesPage(driver, wait);
    }

    private OldTable getStoragesTable() {
        return OldTable.createById(driver, wait, STORAGES_TABLE_ID);
    }

    public void clearAllColumnFilters() {
        getStoragesTable().clearAllColumnValues();
    }

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public StoragesPage selectStorage(String storageName) {
        OldTable table = getStoragesTable();
        waitForPageToLoad();
        table.searchByColumn(NAME_ATTRIBUTE_LABEL, storageName);
        table.selectFirstRow();
        waitForPageToLoad();
        return this;
    }

    private void callAction(String actionId) {
        getStoragesTable().callAction(actionId);
        waitForPageToLoad();
    }

    public void createStorage(String storageName, String addressName) {
        callAction(CREATE_STORAGE_ACTION_ID);
        Wizard createWizard = Wizard.createByComponentId(driver, wait, CREATE_STORAGE_WIZARD_ID);
        createWizard.setComponentValue(STORAGE_NAME_INPUT_ID, storageName);
        if (!addressName.isEmpty()) {
            createWizard.setComponentValue(STORAGE_ADDRESS_INPUT_ID, addressName);
        }
        createWizard.clickAccept();
        waitForPageToLoad();
    }

    public void editStorage(String storageName, String newStorageName, String newAddressName) {
        selectStorage(storageName);
        callAction(EDIT_STORAGE_ACTION_ID);
        Wizard createWizard = Wizard.createByComponentId(driver, wait, EDIT_STORAGE_WIZARD_ID);
        createWizard.setComponentValue(STORAGE_NAME_INPUT_ID, newStorageName);
        if (!newAddressName.isEmpty()) {
            createWizard.setComponentValue(STORAGE_ADDRESS_INPUT_ID, newAddressName);
        }
        createWizard.clickAccept();
        waitForPageToLoad();
    }

    public void deleteStorage(String storageName) {
        selectStorage(storageName);
        callAction(DELETE_STORAGE_ACTION_ID);
        Wizard deleteWizard = Wizard.createByComponentId(driver, wait, DELETE_STORAGE_WIZARD_ID);
        deleteWizard.clickButtonById(DELETE_STORAGE_BUTTON_ID);
        waitForPageToLoad();
    }

    public void createStorage(String storageName) {
        createStorage(storageName, "");
    }

    public void editStorage(String storageName, String newStorageName) {
        editStorage(storageName, newStorageName, "");
    }

    public AssetsPage openStorage(String storageName) {
        selectStorage(storageName);
        getStoragesTable().clickLink(OPEN_COLUMN_NAME);
        return new AssetsPage(driver, wait, false);
    }

    public void importAssetsIntoNewStorage(String storageName, String storageAddress, String assetsFilePath) {
        getStoragesTable().callAction(IMPORT_STORAGE_ACTION_ID);
        Wizard importWizard = Wizard.createByComponentId(driver, wait, IMPORT_STORAGE_WIZARD_ID);
        importWizard.setComponentValue(IMPORT_INTO_SWITCHER_ID, String.valueOf(Boolean.TRUE));
        importWizard.setComponentValue(STORAGE_NAME_INPUT_ID, storageName);
        if (!storageAddress.isEmpty()) {
            importWizard.setComponentValue(STORAGE_ADDRESS_INPUT_ID, storageAddress);
        }
        importWizard.setComponentValue(IMPORT_FILE_INPUT_ID, assetsFilePath);
        importWizard.clickAccept();
        waitForPageToLoad();
    }

    public void importAssetsIntoNewStorage(String storageName, String assetsFilePath) {
        importAssetsIntoNewStorage(storageName, "", assetsFilePath);
    }

    public void importAssetsIntoExistingStorage(String storageName, String assetsFilePath, boolean replaceAssets) {
        getStoragesTable().callAction(IMPORT_STORAGE_ACTION_ID);
        Wizard importWizard = Wizard.createByComponentId(driver, wait, IMPORT_STORAGE_WIZARD_ID);
        importWizard.setComponentValue(SELECT_STORAGE_INPUT_ID, storageName);
        importWizard.setComponentValue(REPLACE_ASSETS_INPUT_ID, String.valueOf(replaceAssets));
        importWizard.setComponentValue(IMPORT_FILE_INPUT_ID, assetsFilePath);
        importWizard.clickAccept();
        waitForPageToLoad();
    }

}

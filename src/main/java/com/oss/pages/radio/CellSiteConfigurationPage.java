package com.oss.pages.radio;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.prompts.ConfirmationBox;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.table.OldTable;
import com.oss.framework.widgets.tabs.TabsWidget;
import com.oss.framework.widgets.tree.TreeWidget;
import com.oss.framework.widgets.treetable.OldTreeTableWidget;
import com.oss.pages.BasePage;
import com.oss.pages.physical.DeviceWizardPage;

import io.qameta.allure.Step;

/**
 * @author Milena Miętkiewicz
 */

public class CellSiteConfigurationPage extends BasePage {
    private static final String TAB_TABLE_DATA_ATTRIBUTE_NAME = "TableTabsApp";
    private static final String TREE_DATA_ATTRIBUTE_NAME = "SiteHierarchyApp";
    private static final String CELL_TAB_NAME = "Cells %s";
    private static final String CREATE_CELL_BULK_ACTION = "Cell %s Bulk Wizard";
    private static final String ADD_LABEL = "ADD";
    private static final String EDIT_LABEL = "Edit";
    private static final String ADD_ID = "add";
    private static final String DELETE_DEVICE_ACTION_ID = "DeleteDeviceWizardAction";
    private static final String BASE_STATION_ROW = "Base Stations";
    private static final String CREATE_ENODEB_ACTION_ID = "ADD_ENODE";
    private static final String CREATE_GNODEB_ACTION_ID = "ADD_GNODE";
    private static final String CREATE_GNODEB_DU_ACTION_ID = "ADD_GNODE_DU";
    private static final String TYPE_4G = "4G";
    private static final String TYPE_5G = "5G";
    private static final String DEVICES_TAB = "Devices";
    private static final String BASE_STATIONS_TAB = "Base Stations";
    private static final String CREATE_DEVICE_ACTION_ID = "CreateDeviceOnLocationWizardAction";
    private static final String HOST_ON_DEVICE_ACTION_ID = "hostOnLogicalFunction";
    private static final String HOST_ON_ANTENNA_ARRAY_ACTION_ID = "hostOnAntennaArray";
    private static final String HOSTING_TAB_LABEL = "Hosting";
    private static final String TREE_TABLE_ID = "DevicesTableApp";
    private static final String CREATE_PHYSICAL_DEVICE_ACTION_ID = "CreateDeviceWithoutSelectionWizardAction";
    private static final String CONFIRM_DELETE_BUTTON_ID = "ConfirmationBox_object_delete_wizard_confirmation_box_action_button";

    public CellSiteConfigurationPage(WebDriver driver) {
        super(driver);
    }

    public CellSiteConfigurationPage goToCellSiteConfiguration(WebDriver driver, String basicURL, String locationId) {
        driver.get(String.format("%s/#/view/radio/cellsite/xid?perspective=LIVE&withRemoved=true&ids=%s", basicURL, locationId));
        return new CellSiteConfigurationPage(driver);
    }

    @Step("Click plus icon and select {option} from the drop-down list")
    public void clickPlusIconAndSelectOption(String option) {
        getTabTable().callActionByLabel(ADD_LABEL, option);
    }

    @Step("Click plus icon and select {optionId} from the drop-down list")
    public void clickPlusIconAndSelectOptionById(String optionId) {
        getTabTable().callAction(ADD_ID, optionId);
    }

    @Step("Select {tabName} tab")
    public CellSiteConfigurationPage selectTab(String tabName) {
        waitForPageToLoad();
        TabsWidget tabs = TabsWidget.createById(driver, wait, TAB_TABLE_DATA_ATTRIBUTE_NAME);
        waitForPageToLoad();
        tabs.selectTabByLabel(tabName);
        return this;
    }

    @Step("Filter and select {objectName} row")
    public CellSiteConfigurationPage filterObject(String columnName, String objectName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getTabTable().searchByAttributeWithLabel(columnName, Input.ComponentType.TEXT_FIELD, objectName);
        selectRowByAttributeValueWithLabel(columnName, objectName);
        return this;
    }

    @Step("Clear {columnName}")
    public CellSiteConfigurationPage clearColumnFilter(String columnName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getTabTable().clearColumnValue(columnName);
        return this;
    }

    @Step("Select {label} row")
    public CellSiteConfigurationPage selectRowByAttributeValueWithLabel(String attribute, String label) {
        waitForPageToLoad();
        getTabTable().selectRowByAttributeValueWithLabel(attribute, label);
        waitForPageToLoad();
        return this;
    }

    @Step("Click Edit icon")
    public void clickEditIcon() {
        getTabTable().callActionByLabel(EDIT_LABEL);
    }

    @Step("Remove object")
    public void removeObject() {
        getTabTable().callAction(ActionsContainer.EDIT_GROUP_ID, DELETE_DEVICE_ACTION_ID);
        waitForPageToLoad();
        ConfirmationBox confirmationBox = ConfirmationBox.create(driver, wait);
        confirmationBox.clickButtonById(CONFIRM_DELETE_BUTTON_ID);
        waitForPageToLoad();
    }

    @Step("Remove device {objectName}")
    public void removeDevice(String type, String manufacturer, String objectName) {
        selectTab(DEVICES_TAB);
        waitForPageToLoad();
        selectTreeTable(type, manufacturer, objectName);
        waitForPageToLoad();
        removeObject();
        waitForPageToLoad();
    }

    @Step("Remove base station {objectName}")
    public void removeBaseStation(String columnName, String objectName) {
        selectTab(BASE_STATIONS_TAB);
        waitForPageToLoad();
        filterObject(columnName, objectName);
        waitForPageToLoad();
        removeObject();
    }

    @Step("Expand the tree and select Base Station")
    public CellSiteConfigurationPage expandTreeToBaseStation(String locationType, String locationName, String baseStation) {
        waitForPageToLoad();
        getTree().expandTreeRow(locationType);
        waitForPageToLoad();
        getTree().expandTreeRow(locationName);
        waitForPageToLoad();
        getTree().expandTreeRow(BASE_STATION_ROW);
        waitForPageToLoad();
        getTree().selectTreeRow(baseStation);
        return this;
    }

    @Step("Expand the tree and select location")
    public CellSiteConfigurationPage expandTreeToLocation(String locationType, String locationName) {
        waitForPageToLoad();
        getTree().expandTreeRow(locationType);
        getTree().selectTreeRow(locationName);
        return this;
    }

    @Step("Expand the tree and select Cell")
    public CellSiteConfigurationPage expandTreeToCell(String locationType, String locationName, String baseStationName, String cellName) {
        waitForPageToLoad();
        getTree().expandTreeRow(locationType);
        getTree().expandTreeRow(locationName);
        getTree().expandTreeRow(BASE_STATION_ROW);
        getTree().expandTreeRow(baseStationName);
        getTree().selectTreeRow(cellName);
        return this;
    }

    @Step("Select tree row")
    public void selectTreeRow(String treeRowName) {
        waitForPageToLoad();
        getTree().selectTreeRow(treeRowName);
    }

    @Step("Use tree context action")
    public void useTreeContextAction(String groupId, String actionId) {
        getTree().callActionById(groupId, actionId);
    }

    @Step("Use table context action")
    public void useTableContextActionById(String id) {
        waitForPageToLoad();
        getTabTable().callAction(id);
    }

    @Step("Use table context action")
    public void useTableContextActionByLabel(String actionLabel) {
        waitForPageToLoad();
        getTabTable().callActionByLabel(actionLabel);
    }

    @Step("Get {attributeLabel} value for row number {rowNumber}")
    public String getValueByRowNumber(String attributeLabel, int rowNumber) {
        return getTabTable().getCellValue(rowNumber, attributeLabel);
    }

    @Step("Get row count in a table")
    public int getRowCount(String attributeLabel) {
        return getTabTable().countRows(attributeLabel);
    }

    public boolean hasNoData() {
        DelayUtils.waitForPageToLoad(driver, wait);
        return getTabTable().hasNoData();
    }

    public TreeWidget getTree() {
        return TreeWidget.createById(driver, wait, TREE_DATA_ATTRIBUTE_NAME);
    }

    public OldTable getTabTable() {
        waitForPageToLoad();
        return OldTable.createTableForActiveTab(driver, wait);
    }

    @Step("Create eNodeB with following attributes: Name = {eNodeBName}, ID = {eNodeBid}, Model = {eNodeBModel}, MCCMNC = {mccmncPrimary}")
    public void createENodeB(String eNodeBName, String eNodeBid, String eNodeBModel, String mccmncPrimary) {
        clickPlusIconAndSelectOptionById(CREATE_ENODEB_ACTION_ID);
        waitForPageToLoad();
        ENodeBWizardPage eNodeBWizard = new ENodeBWizardPage(driver);
        eNodeBWizard.createENodeB(eNodeBName, eNodeBid, eNodeBModel, mccmncPrimary);
    }

    @Step("Create gNodeB with following attributes: Name = {gNodeBName}, ID = {gNodeBId}, Model = {gNodeBModel}, MCCMNC = {mccmncPrimary}")
    public void createGNodeB(String gNodeBName, String gNodeBId, String gNodeBModel, String mccmncPrimary) {
        clickPlusIconAndSelectOptionById(CREATE_GNODEB_ACTION_ID);
        new GNodeBWizardPage(driver)
                .createGNodeB(gNodeBName, gNodeBId, gNodeBModel, mccmncPrimary);
    }

    @Step("Create gNodeB DU with following attributes: Name = {gNodeBName}, ID = {gNodeBId}, Model = {gNodeBModel}, Controller = {controller}")
    public void createGNodeBDU(String gNodeBName, String gNodeBId, String gNodeBModel, String controller) {
        clickPlusIconAndSelectOptionById(CREATE_GNODEB_DU_ACTION_ID);
        new GNodeBDUWizardPage(driver)
                .createGNodeBDU(gNodeBName, gNodeBId, gNodeBModel, controller);
    }

    @Step("Create {amountOfCells} Cells 4G by bulk wizard with Carrier = {carrier}")
    public void createCell4GBulk(int amountOfCells, String carrier, String[] cellNames, int[] cellsID, int crp) {
        openCell4GBulkWizard().createCellBulkWizard(amountOfCells, carrier, cellNames, cellsID, crp);
    }

    @Step("Create {amountOfCells} Cells 5G by bulk wizard with Carrier = {carrier}")
    public void createCell5GBulk(int amountOfCells, String carrier, String[] cellNames, int[] cellsID) {
        openCell5GBulkWizard().createCell5GBulkWizardWithDefaultValues(amountOfCells, carrier, cellNames, cellsID);
    }

    @Step("Create Base Band Unit with following attributes: Type = {bbuEquipmentType}, Name = {bbuName}, Model = {baseBandUnitModel}, Location = {locationName}")
    public void createBaseBandUnit(String bbuEquipmentType, String baseBandUnitModel, String bbuName, String locationName) {
        waitForPageToLoad();
        selectTab(DEVICES_TAB);
        waitForPageToLoad();
        clickPlusIconAndSelectOptionById(CREATE_DEVICE_ACTION_ID);
        DeviceWizardPage deviceWizardPage = new DeviceWizardPage(driver);
        waitForPageToLoad();
        deviceWizardPage.setEquipmentType(bbuEquipmentType);
        waitForPageToLoad();
        deviceWizardPage.setModel(baseBandUnitModel);
        waitForPageToLoad();
        deviceWizardPage.setName(bbuName);
        waitForPageToLoad();
        deviceWizardPage.next();
        waitForPageToLoad();
        deviceWizardPage.setPreciseLocation(locationName);
        waitForPageToLoad();
        deviceWizardPage.accept();
    }

    @Step("Create Radio Unit with following attributes: Type = {radioUnitEquipmentType}, Name = {radioUnitName}, Model = {radioUnitModel}, Location = {locationName}")
    public void createRadioUnit(String radioUnitEquipmentType, String radioUnitModel, String radioUnitName, String locationName) {
        clickPlusIconAndSelectOptionById(CREATE_DEVICE_ACTION_ID);
        DeviceWizardPage deviceWizardPage = new DeviceWizardPage(driver);
        waitForPageToLoad();
        deviceWizardPage.setEquipmentType(radioUnitEquipmentType);
        waitForPageToLoad();
        deviceWizardPage.setModel(radioUnitModel);
        waitForPageToLoad();
        deviceWizardPage.setName(radioUnitName);
        waitForPageToLoad();
        deviceWizardPage.next();
        waitForPageToLoad();
        deviceWizardPage.setPreciseLocation(locationName);
        waitForPageToLoad();
        deviceWizardPage.accept();
    }

    @Step("Create Ran Antenna and Array with following attributes: Name = {antennaName}, Model = {ranAntennaModel}, Location = {locationName}")
    public void createRanAntennaAndArray(String antennaName, String ranAntennaModel, String locationName) {
        getTabTable().callAction(ActionsContainer.CREATE_GROUP_ID, CREATE_PHYSICAL_DEVICE_ACTION_ID);
        DeviceWizardPage ranAntennaWizardPage = new DeviceWizardPage(driver);
        waitForPageToLoad();
        ranAntennaWizardPage.setName(antennaName);
        waitForPageToLoad();
        ranAntennaWizardPage.setModel(ranAntennaModel);
        waitForPageToLoad();
        ranAntennaWizardPage.next();
        waitForPageToLoad();
        ranAntennaWizardPage.setPreciseLocation(locationName);
        waitForPageToLoad();
        ranAntennaWizardPage.accept();
        waitForPageToLoad();
    }

    @Step("Create Hosting on device with Name = {deviceName}")
    public void createHostingOnDevice(String deviceName, boolean onlyCompatible) {
        selectTab(HOSTING_TAB_LABEL);
        waitForPageToLoad();
        useTableContextActionById(HOST_ON_DEVICE_ACTION_ID);
        HostingWizardPage hostOnDeviceWizard = new HostingWizardPage(driver);
        waitForPageToLoad();
        hostOnDeviceWizard.onlyCompatible(String.valueOf(onlyCompatible));
        waitForPageToLoad();
        hostOnDeviceWizard.setDevice(deviceName);
        waitForPageToLoad();
        hostOnDeviceWizard.clickAccept();
    }

    @Step("Create Hosting on device with Name = {deviceName}")
    public void createHostingOnDevice(String deviceName) {
        selectTab(HOSTING_TAB_LABEL);
        waitForPageToLoad();
        clickPlusIconAndSelectOptionById(HOST_ON_DEVICE_ACTION_ID);
        HostingWizardPage hostOnDeviceWizard = new HostingWizardPage(driver);
        waitForPageToLoad();
        hostOnDeviceWizard.setDevice(deviceName);
        waitForPageToLoad();
        hostOnDeviceWizard.clickAccept();
    }

    @Step("Create Hosting on Antenna Array with Name = {antennaName}")
    public void createHostingOnAntennaArray(String antennaName) {
        selectTab(HOSTING_TAB_LABEL);
        clickPlusIconAndSelectOptionById(HOST_ON_ANTENNA_ARRAY_ACTION_ID);
        HostingWizardPage hostOnAntennaWizard = new HostingWizardPage(driver);
        hostOnAntennaWizard.setHostingContains(antennaName);
        waitForPageToLoad();
        hostOnAntennaWizard.clickAccept();
    }

    public void editCellsInBulk(int cellsNumber, String pci, String rsi, String referencePower, String[] tac, String paOutput) {
        clickEditIcon();
        new EditCell4GBulkWizardPage(driver).editCellsBulk(cellsNumber, pci, rsi, referencePower, tac, paOutput);
    }

    private void selectTreeTable(String type, String manufacturer, String name) {
        OldTreeTableWidget widget = OldTreeTableWidget.create(driver, wait, TREE_TABLE_ID);
        widget.expandNode(type, "Type");
        waitForPageToLoad();
        widget.expandNode(manufacturer, "Type");
        waitForPageToLoad();
        widget.selectNode(name, "Name");
    }

    private Cell5GBulkWizardPage openCell5GBulkWizard() {
        waitForPageToLoad();
        selectTab(String.format(CELL_TAB_NAME, CellSiteConfigurationPage.TYPE_5G));
        waitForPageToLoad();
        clickPlusIconAndSelectOption(String.format(CREATE_CELL_BULK_ACTION, CellSiteConfigurationPage.TYPE_5G));
        waitForPageToLoad();
        return new Cell5GBulkWizardPage(driver);
    }

    private Cell4GBulkWizardPage openCell4GBulkWizard() {
        waitForPageToLoad();
        selectTab(String.format(CELL_TAB_NAME, CellSiteConfigurationPage.TYPE_4G));
        waitForPageToLoad();
        clickPlusIconAndSelectOption(String.format(CREATE_CELL_BULK_ACTION, CellSiteConfigurationPage.TYPE_4G));
        waitForPageToLoad();
        return new Cell4GBulkWizardPage(driver);
    }

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, wait);
    }
}

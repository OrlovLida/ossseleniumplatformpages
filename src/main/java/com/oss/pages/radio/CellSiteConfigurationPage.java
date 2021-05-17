package com.oss.pages.radio;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.portals.DropdownList;
import com.oss.framework.prompts.ConfirmationBox;
import com.oss.framework.prompts.ConfirmationBoxInterface;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.framework.widgets.tablewidget.OldTable;
import com.oss.framework.widgets.treewidget.TreeWidget;
import com.oss.pages.BasePage;
import com.oss.pages.physical.DeviceWizardPage;

import io.qameta.allure.Step;

import static com.oss.framework.components.inputs.Input.ComponentType.COMBOBOX;

/**
 * @author Milena Miętkiewicz
 */

public class CellSiteConfigurationPage extends BasePage {
    private static final String TAB_TABLE_DATA_ATTRIBUTE_NAME = "TableTabsApp";
    private static final String TREE_DATA_ATTRIBUTE_NAME = "SiteHierarchyApp";
    private static final String CELL_TAB_NAME = "Cells %s";
    private static final String CREATE_CELL_BULK_ACTION = "Cell %s Bulk Wizard";
    private static final String TRAIL_TYPE_ID = "trailType";
    private static final String ADD_LABEL = "ADD";
    private static final String EDIT_LABEL = "Edit";
    private static final String DELETE_LABEL = "Delete";
    private static final String BASE_STATION_ROW = "Base Stations";
    private static final String DEVICES_ROW = "Devices";
    private static final String CREATE_ENODEB_ACTION = "Create eNodeB";
    private static final String CREATE_GNODEB_ACTION = "Create gNodeB";
    private static final String TYPE_4G = "4G";
    private static final String TYPE_5G = "5G";
    private static final String DEVICES_TAB = "Devices";
    private static final String CREATE_DEVICE_ACTION = "Create Device";
    private static final String CREATE_RAN_ANTENNA_ACTION = "Create RAN Antenna";
    private static final String HOST_ON_DEVICE_ACTION_LABEL = "Host on Device";
    private static final String HOST_ON_ANTENNA_ARRAY_ACTION_LABEL = "Host on Antenna Array";
    private static final String HOSTING_TAB_LABEL = "Hosting";

    public CellSiteConfigurationPage(WebDriver driver) {
        super(driver);
    }

    public CellSiteConfigurationPage goToCellSiteConfiguration(WebDriver driver, String basicURL, String locationId) {
        driver.get(String.format("%s/#/view/radio/cellsite/xid?perspective=LIVE&withRemoved=true&ids=" + locationId + "", basicURL));
        return new CellSiteConfigurationPage(driver);
    }

    @Step("Click plus icon and select {option} from the drop-down list")
    public void clickPlusIconAndSelectOption(String option) {
        useTableContextActionByLabel(ADD_LABEL);
        waitForPageToLoad();
        DropdownList.create(driver, wait).selectOption(option);
    }

    @Step("Select {tabName} tab")
    public CellSiteConfigurationPage selectTab(String tabName) {
        waitForPageToLoad();
        getTabTable().selectTabByLabel(tabName, TAB_TABLE_DATA_ATTRIBUTE_NAME);
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
        getTabTable().callActionByLabel(DELETE_LABEL);
        waitForPageToLoad();
        ConfirmationBoxInterface prompt = ConfirmationBox.create(driver, wait);
        prompt.clickButtonByLabel(DELETE_LABEL);
    }

    @Step("Expand the tree and select Base Station")
    public CellSiteConfigurationPage expandTreeToBaseStation(String locationType, String locationName, String baseStation) {
        waitForPageToLoad();
        getTree().expandTreeRow(locationType);
        getTree().expandTreeRow(locationName);
        getTree().expandTreeRow(BASE_STATION_ROW);
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

    @Step("Expand the tree and select device")
    public CellSiteConfigurationPage expandTreeToDevice(String locationType, String locationName, String deviceName) {
        waitForPageToLoad();
        getTree().expandTreeRow(locationType);
        getTree().expandTreeRow(locationName);
        getTree().expandTreeRow(DEVICES_ROW);
        getTree().selectTreeRow(deviceName);
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
        getTabTable().callAction(id);
    }

    @Step("Use table context action")
    public void useTableContextActionByLabel(String actionLabel) {
        waitForPageToLoad();
        getTabTable().callActionByLabel(actionLabel);
    }

    @Step("Select trail type")
    public void selectTrailType(String trailType) {
        Wizard wizard = Wizard.createPopupWizard(driver, wait);
        wizard.setComponentValue(TRAIL_TYPE_ID, trailType, COMBOBOX);
        wizard.clickAccept();
    }

    @Step("Get row number for object with {attributeLabel} {value}")
    public int getRowNumber(String attributeLabel, String value) {
        return getTabTable().getRowNumber(value, attributeLabel);
    }

    @Step("Get {attributeLabel} value for row number {rowNumber}")
    public String getValueByRowNumber(String attributeLabel, int rowNumber) {
        return getTabTable().getCellValue(rowNumber, attributeLabel);
    }

    @Step("Get row count in a table")
    public int getRowCount(String attributeLabel) {
        return getTabTable().getNumberOfRowsInTable(attributeLabel);
    }

    public boolean hasNoData() {
        DelayUtils.waitForPageToLoad(driver, wait);
        return getTabTable().hasNoData();
    }

    public TreeWidget getTree() {
        return TreeWidget.createByDataAttributeName(driver, wait, TREE_DATA_ATTRIBUTE_NAME);
    }

    public OldTable getTabTable() {
        waitForPageToLoad();
        return OldTable.createTableForActiveTab(driver, wait);
    }

    @Step("Create eNodeB with following attributes: Name = {eNodeBName}, ID = {eNodeBid}, Model = {eNodeBModel}, MCCMNC = {MCCMNCPrimary}")
    public void createENodeB(String eNodeBName, String eNodeBid, String eNodeBModel, String MCCMNCPrimary) {
        clickPlusIconAndSelectOption(CREATE_ENODEB_ACTION);
        waitForPageToLoad();
        ENodeBWizardPage eNodeBWizard = new ENodeBWizardPage(driver);
        eNodeBWizard.createENodeB(eNodeBName, eNodeBid, eNodeBModel, MCCMNCPrimary);
    }

    @Step("Create GNodeB with following attributes: Name = {gNodeBName}, ID = {gNodeBId}, Model = {gNodeBModel}, MCCMNC = {MCCMNCPrimary}")
    public void createGNodeB(String gNodeBName, String gNodeBId, String gNodeBModel, String MCCMNCPrimary) {
        clickPlusIconAndSelectOption(CREATE_GNODEB_ACTION);
        new GNodeBWizardPage(driver)
                .createGNodeB(gNodeBName, gNodeBId, gNodeBModel, MCCMNCPrimary);
    }

    @Step("Create {amountOfCells} Cells 4G by bulk wizard with Carrier = {carrier}")
    public void createCell4GBulk(int amountOfCells, String carrier, String[] cellNames) {
        openCellBulkWizard(TYPE_4G).createCellBulkWizard(amountOfCells, carrier, cellNames);
    }

    @Step("Create {amountOfCells} Cells 5G by bulk wizard with Carrier = {carrier}")
    public void createCell5GBulk(int amountOfCells, String carrier, String[] cellNames, int[] cellsID) {
        openCellBulkWizard(TYPE_5G).createCell5GBulkWizardWithDefaultValues(amountOfCells, carrier, cellNames, cellsID);
    }

    private CellBulkWizardPage openCellBulkWizard(String type) {
        waitForPageToLoad();
        selectTab(String.format(CELL_TAB_NAME, type));
        waitForPageToLoad();
        clickPlusIconAndSelectOption(String.format(CREATE_CELL_BULK_ACTION, type));
        waitForPageToLoad();
        return new CellBulkWizardPage(driver);
    }

    @Step("Create Base Band Unit with following attributes: Type = {bbuEquipmentType}, Name = {bbuName}, Model = {baseBandUnitModel}, Location = {locationName}")
    public void createBaseBandUnit(String bbuEquipmentType, String baseBandUnitModel, String bbuName, String locationName) {
        waitForPageToLoad();
        selectTab(DEVICES_TAB);
        waitForPageToLoad();
        clickPlusIconAndSelectOption(CREATE_DEVICE_ACTION);
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
        clickPlusIconAndSelectOption(CREATE_DEVICE_ACTION);
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
        clickPlusIconAndSelectOption(CREATE_RAN_ANTENNA_ACTION);
        RanAntennaWizardPage ranAntennaWizardPage = new RanAntennaWizardPage(driver);
        waitForPageToLoad();
        ranAntennaWizardPage.setName(antennaName);
        waitForPageToLoad();
        ranAntennaWizardPage.setModel(ranAntennaModel);
        waitForPageToLoad();
        ranAntennaWizardPage.setPreciseLocation(locationName);
        waitForPageToLoad();
        ranAntennaWizardPage.clickAccept();
        waitForPageToLoad();
        AntennaArrayWizardPage antennaArrayWizardPage = new AntennaArrayWizardPage(driver);
        waitForPageToLoad();
        antennaArrayWizardPage.clickAccept();
    }

    @Step("Create Hosting on device with Name = {deviceName}")
    public void createHostingOnDevice(String deviceName, boolean onlyCompatible) {
        selectTab(HOSTING_TAB_LABEL);
        waitForPageToLoad();
        useTableContextActionByLabel(HOST_ON_DEVICE_ACTION_LABEL);
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
        clickPlusIconAndSelectOption(HOST_ON_DEVICE_ACTION_LABEL);
        HostingWizardPage hostOnDeviceWizard = new HostingWizardPage(driver);
        waitForPageToLoad();
        hostOnDeviceWizard.setDevice(deviceName);
        waitForPageToLoad();
        hostOnDeviceWizard.clickAccept();
    }

    @Step("Create Hosting on Antenna Array with Name = {deviceName}")
    public void createHostingOnAntennaArray(String antennaName) {
        selectTab(HOSTING_TAB_LABEL);
        clickPlusIconAndSelectOption(HOST_ON_ANTENNA_ARRAY_ACTION_LABEL);
        HostingWizardPage hostOnAntennaWizard = new HostingWizardPage(driver);
        hostOnAntennaWizard.setHostingContains(antennaName);
        waitForPageToLoad();
        hostOnAntennaWizard.clickAccept();
    }

    public void editCellsInBulk(int cellsNumber, String pci, String rsi, String referencePower, String[] tac, String paOutput) {
        clickEditIcon();
        new EditCell4GWizardPage(driver).editCellsBulk(cellsNumber, pci, rsi, referencePower, tac, paOutput);
    }

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, wait);
    }
}

package com.oss.E2E;

import java.util.UUID;

import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.alerts.SystemMessageContainer.MessageType;
import com.oss.framework.components.alerts.SystemMessageInterface;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.mainheader.PerspectiveChooser;
import com.oss.framework.components.prompts.ConfirmationBox;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.table.OldTable;
import com.oss.framework.widgets.table.TableInterface;
import com.oss.framework.widgets.table.TableWidget;
import com.oss.framework.widgets.tabs.TabsWidget;
import com.oss.pages.physical.CardCreateWizardPage;
import com.oss.pages.physical.CoolingZoneEditorWizardPage;
import com.oss.pages.physical.CreateCoolingZoneWizardPage;
import com.oss.pages.physical.DeviceWizardPage;
import com.oss.pages.physical.LocationWizardPage;
import com.oss.pages.physical.MountingEditorWizardPage;
import com.oss.pages.physical.SublocationWizardPage;
import com.oss.pages.platform.HierarchyViewPage;

import io.qameta.allure.Description;

import static java.lang.String.format;

public class ISPConfigurationTest extends BaseTestCase {

    private static final Logger log = LoggerFactory.getLogger(ISPConfigurationTest.class);
    private static final String LOCATION_NAME = "BuildingA_" + UUID.randomUUID().toString();
    private static final String LOCATION_TYPE = "Building";
    private static final String SUBLOCATION_NAME = "RoomA_" + UUID.randomUUID().toString();
    private static final String SUBLOCATION_TYPE = "Room";
    private static final String PHYSICAL_DEVICE_MODEL = "7360 ISAM FX-8";
    private static final String PHYSICAL_DEVICE_NAME = "MSAN_Device";
    private static final String PHYSICAL_DEVICE_MODEL2 = "JDSU OTU8000";
    private static final String PHYSICAL_DEVICE_MODEL3 = "ADVA Optical Networking FMT/1HU";
    private static final String PHYSICAL_DEVICE_NAME2 = "OTDR_Device";
    private static final String PHYSICAL_DEVICE_NAME3 = "DWDM_Device";
    private static final String CHANGE_DEVICE_MODEL_ACTION_ID = "ChangeDeviceModelAction";
    private static final String MODEL_UPDATE = "Alcatel 7302 ISAM";
    private static final String CREATE_CARD_ACTION_ID = "CreateCardOnDeviceAction";
    private static final String CARD_MODEL = "Alcatel NELT-B";
    private static final String CARD_MODEL_UPDATE = "Generic Card";
    private static final String SLOT_LT3 = "Chassis\\LT3";
    private static final String SLOT_LT4 = "Chassis\\LT4";
    private static final String CHANGE_CARD_MODEL_ACTION_ID = "ChangeCardModelAction";
    private static final String MOUNTING_EDITOR_ACTION_ID = "MountingEditorForPhysicalElementAction";
    private static final String MOUNTING_EDITOR_MODEL = "Generic 19\" 42U 600x800 (Bottom-Up)";
    private static final String MOUNTING_EDITOR_NAME = "ISPMountingEditor";
    private static final String COOLING_ZONE_NAME = "ISPCoolingZone";
    private static final String COOLING_UNIT_MODEL = "Generic Cooling Unit";
    private static final String COOLING_UNIT_NAME = "ISPCoolingUnit";
    private static final String ASSIGN_COOLING_ZONE_ACTION_ID = "AssignCoolingZoneWizardAction";
    private static final String UPDATE_DEVICE_ACTION_ID = "UpdateDeviceWizardAction";
    private static final String UPDATE_SUBLOCATION_ACTION_ID = "UpdateSublocationWizardAction";
    private static final String COOLING_CAPACITY = "14";
    private static final String COOLING_CAPACITY2 = "16";
    private static final String DEVICE_HEAT_EMISSION = "3";
    private static final String DEVICE_POWER_CONSUMPTION = "4";
    private static final String POWER_DEVICE_MODEL = "Generic Power Source";
    private static final String POWER_DEVICE_NAME = "Power Source";
    private static final String POWER_DEVICE_CAPACITY = "10";
    private static final String POWER_SUPPLY_UNIT_MODEL = "Generic Power Supply Unit";
    private static final String POWER_SUPPLY_UNIT_NAME = "Power Supply Unit";
    private static final String POWER_SUPPLY_UNIT_CAPACITY = "5";
    private static final String NAME = "Name";
    private static final String NAME_COLUMN = "name";
    private static final String FOOTPRINT = "Footprint";
    private static final String FOOTPRINT_1 = "Footprint1";
    private static final String FOOTPRINT_2 = "Footprint2";
    private static final String FOOTPRINT_3 = "Footprint3";
    private static final String ROW = "Row";
    private static final String ROW_1 = "Row1";
    private static final String POWER_CAPACITY_COLUMN = "Power Capacity [kW]";
    private static final String POWER_LOAD_COLUMN = "Power Load [kW]";
    private static final String POWER_LOAD_RATIO_COLUMN = "Power Load Ratio [%]";
    private static final String COOLING_LOAD_COLUMN = "coolingLoad";
    private static final String COOLING_CAPACITY_COLUMN = "coolingCapacity";
    private static final String COOLING_LOAD_RATIO_COLUMN = "coolingLoadRatio";
    private static final String DELETE_SUBLOCATION_ACTION_ID = "RemoveSublocationWizardAction";
    private static final String DELETE_LOCATION_ACTION_ID = "RemoveLocationWizardAction";
    private static final String TAB_PATTERN = "tab_%s";
    private static final String TABLE_COOLING_ZONES = "CoolingZonesWidget";
    private static final String TABLE_POWER_MANAGEMENT = "PowerManagementTabWidget";
    private static final String TABLE_SUBLOCATIONS = "SublocationsWidget";
    private static final String TABLE_DEVICES = "DevicesWidget";
    private static final String DELETE_DEVICE_ACTION_ID = "DeleteDeviceWizardAction";
    private static final String DELETE_DEVICE_CONFIRMATION_ID = "ConfirmationBox_object_delete_wizard_confirmation_box_action_button";
    private static final String DELETE_COOLING_ZONE_ACTION_ID = "DeleteCoolingZoneWizardAction";
    private static final String CREATE_COOLING_ZONE_ACTION_ID = "CreateCoolingZoneWizardAction";
    private static final String CREATE_DEVICE_FROM_TAB_ACTION_ID = "CreateDeviceWithoutSelectionWizardAction";
    private static final String ASSERT_NOT_EQUALS = "The checked value is %s and it shouldn't be equal to the defined %s value";
    private static final String LOCATION_POWER_CAPACITY = "0.00";
    private static final String DELETE_BUTTON_LABEL = "Delete";
    private static String COOLING_ZONE_COOLING_LOAD = "0.00";
    private static String COOLING_ZONE_LOAD_RATIO = "0.00";
    private static String COOLING_ZONE_CAPACITY = "0.00";
    private final static String CREATE_SUBLOCATION_ACTION_ID = "CreateSublocationInLocationWizardAction";
    private final static String CREATE_PHYSICAL_DEVICE_ACTION_ID = "CreateDeviceOnLocationWizardAction";
    private final static String CREATE_PHYSICAL_LOCATION = "Create Physical Location";
    private final static String INFRASTRUCTURE_MANAGEMENT = "Infrastructure Management";
    private final static String CREATE_INFRASTRUCTURE = "Create Infrastructure";
    private String HIERARCHY_VIEW_URL = "";
    private SoftAssert softAssert;

    @BeforeClass
    @Description("Open Console and set Live perspective")
    public void openConsoleAndSetLivePerspective() {
        waitForPageToLoad();
        PerspectiveChooser.create(driver, webDriverWait).setLivePerspective();
        waitForPageToLoad();
        softAssert = new SoftAssert();
    }

    @Test(priority = 1, description = "Create building")
    @Description("Create building")
    public void createBuilding() {
        homePage.chooseFromLeftSideMenu(CREATE_PHYSICAL_LOCATION, INFRASTRUCTURE_MANAGEMENT, CREATE_INFRASTRUCTURE);
        LocationWizardPage locationWizardPage = new LocationWizardPage(driver);
        locationWizardPage.setLocationType(LOCATION_TYPE);
        waitForPageToLoad();
        locationWizardPage.setLocationName(LOCATION_NAME);
        waitForPageToLoad();
        locationWizardPage.clickNext();
        waitForPageToLoad();
        locationWizardPage.setGeographicalAddress("a");
        waitForPageToLoad();
        locationWizardPage.clickNext();
        waitForPageToLoad();
        locationWizardPage.create();
        checkPopup();
    }

    @Test(priority = 2, description = "Show building in Hierarchy View", dependsOnMethods = {"createBuilding"})
    @Description("Show newly created building in Hierarchy View")
    public void showHierarchyViewForBuildingFromPopup() {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        systemMessage.clickMessageLink();
        systemMessage.close();
        waitForPageToLoad();
        HIERARCHY_VIEW_URL = driver.getCurrentUrl();
    }

    @Test(priority = 3, description = "Open Create Sublocation wizard", dependsOnMethods = {"showHierarchyViewForBuildingFromPopup"})
    @Description("Open Create Sublocation wizard")
    public void openSublocationWizard() {
        HierarchyViewPage hierarchyViewPage = HierarchyViewPage.getHierarchyViewPage(driver, webDriverWait);
        hierarchyViewPage.selectFirstObject();
        waitForPageToLoad();
        hierarchyViewPage.callAction(ActionsContainer.CREATE_GROUP_ID, CREATE_SUBLOCATION_ACTION_ID);
    }

    @Test(priority = 4, description = "Create room", dependsOnMethods = {"openSublocationWizard"})
    @Description("Create room and check confirmation system message")
    public void createRoom() {
        SublocationWizardPage sublocationWizardPage = new SublocationWizardPage(driver);
        waitForPageToLoad();
        sublocationWizardPage.setSublocationType(SUBLOCATION_TYPE);
        waitForPageToLoad();
        sublocationWizardPage.setSublocationName(SUBLOCATION_NAME);
        waitForPageToLoad();
        sublocationWizardPage.clickNext();
        waitForPageToLoad();
        sublocationWizardPage.create();
        waitForPageToLoad();
        checkPopupAndCloseMessage();
    }

    @Test(priority = 5, description = "Open Create Device wizard", dependsOnMethods = {"createRoom"})
    @Description("Open Create Device wizard")
    public void openDeviceWizard() {
        HierarchyViewPage hierarchyViewPage = HierarchyViewPage.getHierarchyViewPage(driver, webDriverWait);
        hierarchyViewPage.callAction(ActionsContainer.CREATE_GROUP_ID, CREATE_PHYSICAL_DEVICE_ACTION_ID);
        waitForPageToLoad();
    }

    @Test(priority = 6, description = "Create physical device", dependsOnMethods = {"openDeviceWizard"})
    @Description("Create physical device and check confirmation system message")
    public void createPhysicalDevice() {
        DeviceWizardPage deviceWizardPage = new DeviceWizardPage(driver);
        deviceWizardPage.setModel(PHYSICAL_DEVICE_MODEL);
        waitForPageToLoad();
        deviceWizardPage.setName(PHYSICAL_DEVICE_NAME);
        waitForPageToLoad();
        deviceWizardPage.next();
        deviceWizardPage.setPreciseLocation(SUBLOCATION_NAME);
        waitForPageToLoad();
        deviceWizardPage.accept();
        checkPopup();
    }

    @Test(priority = 7, description = "Show device in Hierarchy View", dependsOnMethods = {"createPhysicalDevice"})
    @Description("Show device in Hierarchy View by clicking in direct link from system message")
    public void showHierarchyViewFromPopup() {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        systemMessage.clickMessageLink();
        systemMessage.close();
        waitForPageToLoad();
    }

    @Test(priority = 8, description = "Open Change Model wizard", dependsOnMethods = {"showHierarchyViewFromPopup"})
    @Description("Select device and open Change Model wizard")
    public void openChangeModelWizard() {
        HierarchyViewPage hierarchyViewPage = HierarchyViewPage.getHierarchyViewPage(driver, webDriverWait);
        hierarchyViewPage.selectFirstObject();
//        waitForPageToLoad();
//        hierarchyViewPage.callAction(ActionsContainer.EDIT_GROUP_ID, CHANGE_DEVICE_MODEL_ACTION_ID);
//        waitForPageToLoad();
    }

    @Test(priority = 9, description = "Change device model", dependsOnMethods = {"openChangeModelWizard"})
    @Description("Change device model and check confirmation system message")
    public void changeDeviceModel() {
//        ChangeModelWizardPage changeModelWizardPage = new ChangeModelWizardPage(driver);
//        changeModelWizardPage.setModel(MODEL_UPDATE);
//        waitForPageToLoad();
//        DelayUtils.sleep(1000);//TODO ?
//        changeModelWizardPage.clickUpdate(); //TODO OSSPHY-56019
//        checkPopupAndCloseMessage();
    }

    @Test(priority = 10, description = "Open Create Card wizard", dependsOnMethods = {"changeDeviceModel"})
    @Description("Open Create Card wizard")
    public void openCreateCardWizard() {
        waitForPageToLoad();
        HierarchyViewPage hierarchyViewPage = HierarchyViewPage.getHierarchyViewPage(driver, webDriverWait);
        hierarchyViewPage.callAction(ActionsContainer.CREATE_GROUP_ID, CREATE_CARD_ACTION_ID);
        waitForPageToLoad();
    }

    @Test(priority = 11, description = "Create card", dependsOnMethods = {"openCreateCardWizard"})
    @Description("Create card and check confirmation system message")
    public void createCard() {
        CardCreateWizardPage cardCreateWizardPage = new CardCreateWizardPage(driver);
        waitForPageToLoad();
        cardCreateWizardPage.setModel(CARD_MODEL);
        waitForPageToLoad();
        cardCreateWizardPage.setSlot(SLOT_LT3);
        waitForPageToLoad();
        cardCreateWizardPage.setSlot(SLOT_LT4);
        waitForPageToLoad();
        cardCreateWizardPage.clickAccept();
        checkPopupAndCloseMessage();
    }

    @Test(priority = 12, description = "Open Change Card Model wizard", dependsOnMethods = {"createCard"})
    @Description("Refresh page, select newly created card and open Change Card Model wizard")
    public void openChangeCardWizard() {
        driver.navigate().refresh();//TODO do usunięcia po OSSWEB-20673
        waitForPageToLoad();
        HierarchyViewPage hierarchyViewPage = HierarchyViewPage.getHierarchyViewPage(driver, webDriverWait);
        String labelpath = PHYSICAL_DEVICE_NAME + ".Chassis." + PHYSICAL_DEVICE_NAME + "/Chassis.Slots.LT3.Card.NELT-B";
        hierarchyViewPage.selectNodeByLabelsPath(labelpath);
        waitForPageToLoad();
//        hierarchyViewPage.callAction(ActionsContainer.EDIT_GROUP_ID, CHANGE_CARD_MODEL_ACTION_ID);
//        waitForPageToLoad();
    }

    @Test(priority = 13, description = "Change Card Model", dependsOnMethods = {"openChangeCardWizard"})
    @Description("Change Card Model and check confirmation system message")
    public void changeCardModel() {
//        ChangeCardModelWizard changeCardModelWizard = new ChangeCardModelWizard(driver);
//        changeCardModelWizard.setModelCard(CARD_MODEL_UPDATE);//TODO do dopisania w BUC
//        waitForPageToLoad();
//        changeCardModelWizard.clickSubmit();//TODO OSSPHY-56019
//        checkPopupAndCloseMessage();
    }

    @Test(priority = 14, description = "Open Mounting Editor wizard", dependsOnMethods = {"changeCardModel"})
    @Description("Refresh page, select device and open Mounting Editor wizard")
    public void openMountingEditorWizard() {
        driver.navigate().refresh();//TODO ?
        waitForPageToLoad();
        HierarchyViewPage hierarchyViewPage = HierarchyViewPage.getHierarchyViewPage(driver, webDriverWait);
        hierarchyViewPage.selectNodeByLabel(PHYSICAL_DEVICE_NAME);
        waitForPageToLoad();
        hierarchyViewPage.callAction(ActionsContainer.SHOW_ON_GROUP_ID, MOUNTING_EDITOR_ACTION_ID);
        waitForPageToLoad();
    }

    @Test(priority = 15, description = "Set Mounting Editor", dependsOnMethods = {"openMountingEditorWizard"})
    @Description("Set Mounting Editor and check confirmation system message")
    public void setMountingEditor() {
        MountingEditorWizardPage mountingEditorWizardPage = new MountingEditorWizardPage(driver);
        mountingEditorWizardPage.setName(MOUNTING_EDITOR_NAME);
        waitForPageToLoad();
        mountingEditorWizardPage.setPreciseLocation(SUBLOCATION_NAME);
        waitForPageToLoad();
        mountingEditorWizardPage.setModel(MOUNTING_EDITOR_MODEL);
        waitForPageToLoad();
        mountingEditorWizardPage.clickNext();
        waitForPageToLoad();
        mountingEditorWizardPage.clickCheckbox();
        waitForPageToLoad();
        mountingEditorWizardPage.clickAccept();
        checkPopupAndCloseMessage();
    }

    @Test(priority = 16, description = "Move to Location Overview and create cooling zone", dependsOnMethods = {"showHierarchyViewFromPopup"})
    @Description("Move to Location Overview by direct link, create cooling zone from Cooling Zones tab and check confirmation system message")
    public void createCoolingZone() {
        driver.get(format(HIERARCHY_VIEW_URL, BASIC_URL));
        HierarchyViewPage hierarchyViewPage = HierarchyViewPage.getHierarchyViewPage(driver, webDriverWait);
        hierarchyViewPage.selectFirstObject();
        TableWidget tableWidget = getTableWidget(TABLE_COOLING_ZONES);
        tableWidget.callAction(ActionsContainer.CREATE_GROUP_ID, CREATE_COOLING_ZONE_ACTION_ID);
        CreateCoolingZoneWizardPage coolingZoneWizard = new CreateCoolingZoneWizardPage(driver);
        coolingZoneWizard.setName(COOLING_ZONE_NAME);
        waitForPageToLoad();
        coolingZoneWizard.setOperatingLocation(SUBLOCATION_NAME);//TODO do dopisania w BUC
        coolingZoneWizard.clickAccept();
        waitForPageToLoad();
        checkPopupAndCloseMessage();
    }

    @Test(priority = 17, description = "Create cooling unit", dependsOnMethods = {"createCoolingZone"})
    @Description("Create cooling unit from Devices tab and check confirmation system message")
    public void createCoolingUnit() {
        TableWidget tableWidget = getTableWidget(TABLE_DEVICES);
        tableWidget.callAction(ActionsContainer.CREATE_GROUP_ID, CREATE_DEVICE_FROM_TAB_ACTION_ID);
        DeviceWizardPage deviceWizardPage = new DeviceWizardPage(driver);
        deviceWizardPage.setModel(COOLING_UNIT_MODEL);
        waitForPageToLoad();
        deviceWizardPage.setName(COOLING_UNIT_NAME);
        waitForPageToLoad();
        deviceWizardPage.setCoolingCapacity(COOLING_CAPACITY);
        waitForPageToLoad();
        deviceWizardPage.next();
        waitForPageToLoad();
        deviceWizardPage.setPreciseLocation(SUBLOCATION_NAME);
        waitForPageToLoad();
        deviceWizardPage.accept();
        checkPopupAndCloseMessage();
    }

    @Test(priority = 18, description = "Assign cooling unit to cooling zone", dependsOnMethods = {"createCoolingUnit"})
    @Description("Assign cooling unit to cooling zone")
    public void assignCoolingUnitToCoolingZone() {
        TableWidget tableWidget = getTableWidget(TABLE_DEVICES);
        unselectAllAndSelectRow(tableWidget, COOLING_UNIT_NAME);
        tableWidget.callAction(ActionsContainer.OTHER_GROUP_ID, ASSIGN_COOLING_ZONE_ACTION_ID);
        CoolingZoneEditorWizardPage coolingZoneWizard = new CoolingZoneEditorWizardPage(driver);
        waitForPageToLoad();
        coolingZoneWizard.selectNameFromList(COOLING_ZONE_NAME);
        waitForPageToLoad();
        coolingZoneWizard.clickAccept();
        waitForPageToLoad();
    }

    @Test(priority = 19, description = "Create second device", dependsOnMethods = {"showHierarchyViewFromPopup"})
    @Description("Create second device and check confirmation system message")
    public void createSecondDevice() {
        TableWidget tableWidget = getTableWidget(TABLE_DEVICES);
        tableWidget.unselectAllRows();
        waitForPageToLoad();
        tableWidget.callAction(ActionsContainer.CREATE_GROUP_ID, CREATE_DEVICE_FROM_TAB_ACTION_ID);
        waitForPageToLoad();
        DeviceWizardPage deviceWizardPage = new DeviceWizardPage(driver);
        waitForPageToLoad();
        deviceWizardPage.setModel(PHYSICAL_DEVICE_MODEL2);
        waitForPageToLoad();
        deviceWizardPage.setName(PHYSICAL_DEVICE_NAME2);
        waitForPageToLoad();
        deviceWizardPage.setHeatEmission(DEVICE_HEAT_EMISSION);
        waitForPageToLoad();
        deviceWizardPage.setPowerConsumption(DEVICE_POWER_CONSUMPTION);
        waitForPageToLoad();
        deviceWizardPage.next();
        waitForPageToLoad();
        deviceWizardPage.setPreciseLocation(SUBLOCATION_NAME);
        waitForPageToLoad();
        deviceWizardPage.accept();
        checkPopupAndCloseMessage();
    }

    @Test(priority = 20, description = "Assign second device to cooling zone", dependsOnMethods = {"createSecondDevice"})
    @Description("Assign second device to cooling zone")
    public void assignSecondDeviceToCoolingZone() {
        TableWidget tableWidget = getTableWidget(TABLE_DEVICES);
        unselectAllAndSelectRow(tableWidget, PHYSICAL_DEVICE_NAME2);
        tableWidget.callAction(ActionsContainer.OTHER_GROUP_ID, ASSIGN_COOLING_ZONE_ACTION_ID);
        waitForPageToLoad();
        CoolingZoneEditorWizardPage coolingZoneWizardPage = new CoolingZoneEditorWizardPage(driver);
        coolingZoneWizardPage.selectNameFromList(COOLING_ZONE_NAME);
        coolingZoneWizardPage.clickAccept();
        waitForPageToLoad();
        DelayUtils.sleep(10000);
    }

    @Test(priority = 21, description = "Check cooling values", dependsOnMethods = {"assignSecondDeviceToCoolingZone"})
    @Description("Refresh page and check cooling values in Cooling Zones tab")
    public void checkCoolingValues() {
        driver.navigate().refresh();
        waitForPageToLoad();
        HierarchyViewPage hierarchyViewPage = HierarchyViewPage.getHierarchyViewPage(driver, webDriverWait);
        hierarchyViewPage.selectFirstObject();
        waitForPageToLoad();
        TableWidget tableWidget = getTableWidget(TABLE_COOLING_ZONES);
        int rowNumber = tableWidget.getRowNumber(COOLING_ZONE_NAME, NAME_COLUMN);
        String coolingLoad = tableWidget.getCellValue(rowNumber, COOLING_LOAD_COLUMN);
        log.info("Cooling load = {}", coolingLoad);
        String coolingCapacity = tableWidget.getCellValue(rowNumber, COOLING_CAPACITY_COLUMN);
        log.info("Cooling capacity = {}", coolingCapacity);
        String coolingLoadRatio = tableWidget.getCellValue(rowNumber, COOLING_LOAD_RATIO_COLUMN);
        log.info("Cooling load ratio = {}", coolingLoadRatio);
        softAssert = new SoftAssert();
        softAssert.assertNotEquals(coolingLoad, COOLING_ZONE_COOLING_LOAD, String.format(ASSERT_NOT_EQUALS, coolingLoad, COOLING_ZONE_COOLING_LOAD));
        softAssert.assertNotEquals(coolingCapacity, COOLING_ZONE_CAPACITY, String.format(ASSERT_NOT_EQUALS, coolingCapacity, COOLING_ZONE_CAPACITY));
        softAssert.assertNotEquals(coolingLoadRatio, COOLING_ZONE_LOAD_RATIO, String.format(ASSERT_NOT_EQUALS, coolingLoadRatio, COOLING_ZONE_LOAD_RATIO));
        COOLING_ZONE_COOLING_LOAD = coolingLoad;
        COOLING_ZONE_LOAD_RATIO = coolingLoadRatio;
        COOLING_ZONE_CAPACITY = coolingCapacity;
        softAssert.assertAll();
    }

    @Test(priority = 22, description = "Create third device", dependsOnMethods = {"showHierarchyViewFromPopup"})
    @Description("Create third device and check confirmation system message")
    public void createThirdDevice() {
        HierarchyViewPage hierarchyViewPage = HierarchyViewPage.getHierarchyViewPage(driver, webDriverWait);
        hierarchyViewPage.callAction(ActionsContainer.CREATE_GROUP_ID, CREATE_PHYSICAL_DEVICE_ACTION_ID);
        waitForPageToLoad();
        DeviceWizardPage deviceWizardPage = new DeviceWizardPage(driver);
        waitForPageToLoad();
        deviceWizardPage.setModel(PHYSICAL_DEVICE_MODEL3);
        waitForPageToLoad();
        deviceWizardPage.setName(PHYSICAL_DEVICE_NAME3);
        waitForPageToLoad();
        deviceWizardPage.setHeatEmission(DEVICE_HEAT_EMISSION);
        waitForPageToLoad();
        deviceWizardPage.next();
        waitForPageToLoad();
        deviceWizardPage.setPreciseLocation(SUBLOCATION_NAME);
        waitForPageToLoad();
        deviceWizardPage.accept();
        checkPopupAndCloseMessage();
    }

    @Test(priority = 23, description = "Assign third device to cooling zone", dependsOnMethods = {"createThirdDevice"})
    @Description("Assign third device to cooling zone")
    public void assignThirdDeviceToCoolingZone() {
        TableWidget tableWidget = getTableWidget(TABLE_DEVICES);
        unselectAllAndSelectRow(tableWidget, PHYSICAL_DEVICE_NAME3);
        tableWidget.callAction(ActionsContainer.OTHER_GROUP_ID, ASSIGN_COOLING_ZONE_ACTION_ID);
        waitForPageToLoad();
        CoolingZoneEditorWizardPage coolingZoneWizardPage = new CoolingZoneEditorWizardPage(driver);
        coolingZoneWizardPage.selectNameFromList(COOLING_ZONE_NAME);
        coolingZoneWizardPage.clickAccept();
        waitForPageToLoad();
    }

    @Test(priority = 24, description = "Check cooling values", dependsOnMethods = {"assignThirdDeviceToCoolingZone"})
    @Description("Refresh page and check cooling values in Cooling Zones tab")
    public void checkCoolingValues2() {
        TableWidget tableWidget = getTableWidget(TABLE_COOLING_ZONES);
        tableWidget.callAction(ActionsContainer.KEBAB_GROUP_ID, TableWidget.REFRESH_ACTION_ID);
        waitForPageToLoad();
        int rowNumber = tableWidget.getRowNumber(COOLING_ZONE_NAME, NAME_COLUMN);
        String coolingLoad = tableWidget.getCellValue(rowNumber, COOLING_LOAD_COLUMN);
        log.info("Cooling load = {}", coolingLoad);
        String coolingLoadRatio = tableWidget.getCellValue(rowNumber, COOLING_LOAD_RATIO_COLUMN);
        log.info("Cooling load ratio = {}", coolingLoadRatio);
        softAssert = new SoftAssert();
        softAssert.assertNotEquals(coolingLoad, COOLING_ZONE_COOLING_LOAD, String.format(ASSERT_NOT_EQUALS, coolingLoad, COOLING_ZONE_COOLING_LOAD));
        softAssert.assertNotEquals(coolingLoadRatio, COOLING_ZONE_LOAD_RATIO, String.format(ASSERT_NOT_EQUALS, coolingLoadRatio, COOLING_ZONE_LOAD_RATIO));
        COOLING_ZONE_COOLING_LOAD = coolingLoad;
        COOLING_ZONE_LOAD_RATIO = coolingLoadRatio;
        softAssert.assertAll();
    }

    @Test(priority = 25, description = "Update cooling unit", dependsOnMethods = {"assignThirdDeviceToCoolingZone"})
    @Description("Update Cooling Unit and check confirmation system message")
    public void updateCoolingUnit() {
        TableWidget tableWidget = getTableWidget(TABLE_DEVICES);
        unselectAllAndSelectRow(tableWidget, COOLING_UNIT_NAME);
        tableWidget.callAction(ActionsContainer.EDIT_GROUP_ID, UPDATE_DEVICE_ACTION_ID);
        waitForPageToLoad();
        DeviceWizardPage deviceWizardPage = new DeviceWizardPage(driver);
        deviceWizardPage.setCoolingCapacity(COOLING_CAPACITY2);
        waitForPageToLoad();
        deviceWizardPage.nextUpdateWizard();
        deviceWizardPage.acceptUpdateWizard();
        checkPopupAndCloseMessage();
        waitForPageToLoad();
    }

    @Test(priority = 26, description = "Check updated cooling values", dependsOnMethods = {"updateCoolingUnit"})
    @Description("Refresh page and check updated cooling values in cooling zones tab")
    public void checkUpdatedCoolingValues() {
        TableWidget tableWidget = getTableWidget(TABLE_COOLING_ZONES);
        tableWidget.callAction(ActionsContainer.KEBAB_GROUP_ID, TableWidget.REFRESH_ACTION_ID);
        waitForPageToLoad();
        int rowNumber = tableWidget.getRowNumber(COOLING_ZONE_NAME, NAME_COLUMN);
        String coolingCapacity = tableWidget.getCellValue(rowNumber, COOLING_CAPACITY_COLUMN);
        log.info("Cooling capacity = {}", coolingCapacity);
        String coolingLoadRatio = tableWidget.getCellValue(rowNumber, COOLING_LOAD_RATIO_COLUMN);
        log.info("Cooling load ratio = {}", coolingLoadRatio);
        softAssert = new SoftAssert();
        softAssert.assertNotEquals(coolingCapacity, COOLING_ZONE_CAPACITY, String.format(ASSERT_NOT_EQUALS, coolingCapacity, COOLING_ZONE_CAPACITY));
        softAssert.assertNotEquals(coolingLoadRatio, COOLING_ZONE_LOAD_RATIO, String.format(ASSERT_NOT_EQUALS, coolingLoadRatio, COOLING_ZONE_LOAD_RATIO));
        softAssert.assertAll();
    }

    @Test(priority = 27, description = "Create power device", dependsOnMethods = {"showHierarchyViewFromPopup"})
    @Description("Create power device from Devices tab and check confirmation system message")
    public void createPowerDevice() {
        HierarchyViewPage hierarchyViewPage = HierarchyViewPage.getHierarchyViewPage(driver, webDriverWait);
        hierarchyViewPage.callAction(ActionsContainer.CREATE_GROUP_ID, CREATE_PHYSICAL_DEVICE_ACTION_ID);
        waitForPageToLoad();
        DeviceWizardPage deviceWizardPage = new DeviceWizardPage(driver);
        deviceWizardPage.setModel(POWER_DEVICE_MODEL);
        waitForPageToLoad();
        deviceWizardPage.setName(POWER_DEVICE_NAME);
        waitForPageToLoad();
        deviceWizardPage.setPowerCapacity(POWER_DEVICE_CAPACITY);
        deviceWizardPage.next();
        deviceWizardPage.setPreciseLocation(SUBLOCATION_NAME);
        waitForPageToLoad();
        deviceWizardPage.accept();
        checkPopupAndCloseMessage();
    }

    @Test(priority = 28, description = "Check power values", dependsOnMethods = {"createPowerDevice"})
    @Description("Check power values in Power Management tab")
    public void checkPowerValues() {
        TableInterface tableWidget = getOldTableWidget();
        int rowNumber = tableWidget.getRowNumber(SUBLOCATION_NAME, NAME);
        String rowValue = tableWidget.getCellValue(rowNumber, POWER_CAPACITY_COLUMN);
        log.info("Power capacity = {}", rowValue);
        softAssert = new SoftAssert();
        softAssert.assertNotEquals(rowValue, LOCATION_POWER_CAPACITY, String.format(ASSERT_NOT_EQUALS, rowValue, LOCATION_POWER_CAPACITY));
        rowValue = tableWidget.getCellValue(rowNumber, POWER_LOAD_COLUMN);
        log.info("Power load = {}", rowValue);
        softAssert.assertNotEquals(rowValue, "0", String.format(ASSERT_NOT_EQUALS, rowValue, "0"));
        rowValue = tableWidget.getCellValue(rowNumber, POWER_LOAD_RATIO_COLUMN);
        log.info("Power load ratio = {}", rowValue);
        softAssert.assertNotEquals(rowValue, "0", String.format(ASSERT_NOT_EQUALS, rowValue, "0"));
        softAssert.assertAll();
    }

    @Test(priority = 29, description = "Create power supply unit", dependsOnMethods = {"checkPowerValues"})
    @Description("Create power supply unit and check confirmation system message")
    public void createPowerSupplyUnit() {
        HierarchyViewPage hierarchyViewPage = HierarchyViewPage.getHierarchyViewPage(driver, webDriverWait);
        hierarchyViewPage.callAction(ActionsContainer.CREATE_GROUP_ID, CREATE_PHYSICAL_DEVICE_ACTION_ID);
        waitForPageToLoad();
        DeviceWizardPage deviceWizardPage = new DeviceWizardPage(driver);
        deviceWizardPage.setModel(POWER_SUPPLY_UNIT_MODEL);
        waitForPageToLoad();
        deviceWizardPage.setName(POWER_SUPPLY_UNIT_NAME);
        deviceWizardPage.setPowerCapacity(POWER_SUPPLY_UNIT_CAPACITY);
        deviceWizardPage.next();
        deviceWizardPage.setPreciseLocation(SUBLOCATION_NAME);
        waitForPageToLoad();
        deviceWizardPage.accept();
        checkPopupAndCloseMessage();
    }

    @Test(priority = 30, description = "Check power values", dependsOnMethods = {"createPowerSupplyUnit"})
    @Description("Check power values in Power Management tab")
    public void checkPowerValues2() {
        TableInterface tableWidget = OldTable.createById(driver, webDriverWait, TABLE_POWER_MANAGEMENT);
        int rowNumber = tableWidget.getRowNumber(SUBLOCATION_NAME, NAME);
        String rowValue = tableWidget.getCellValue(rowNumber, POWER_CAPACITY_COLUMN);
        log.info("Power capacity = {}", rowValue);
        //TODO dostosowac asercje
        softAssert = new SoftAssert();
        softAssert.assertNotEquals(rowValue, LOCATION_POWER_CAPACITY, String.format(ASSERT_NOT_EQUALS, rowValue, LOCATION_POWER_CAPACITY));
        rowValue = tableWidget.getCellValue(rowNumber, POWER_LOAD_COLUMN);
        log.info("Power row value = {}", rowValue);
        softAssert.assertNotEquals(rowValue, "0", String.format(ASSERT_NOT_EQUALS, rowValue, "0"));
        rowValue = tableWidget.getCellValue(rowNumber, POWER_LOAD_RATIO_COLUMN);
        log.info("Power load ratio = {}", rowValue);
        softAssert.assertNotEquals(rowValue, "0", String.format(ASSERT_NOT_EQUALS, rowValue, "0"));
        softAssert.assertAll();
    }

    @Test(priority = 31, description = "Create row", dependsOnMethods = {"showHierarchyViewFromPopup"})
    @Description("Create row and check confirmation system message")
    public void createRow() {
        HierarchyViewPage hierarchyViewPage = HierarchyViewPage.getHierarchyViewPage(driver, webDriverWait);
        hierarchyViewPage.selectFirstObject();
        waitForPageToLoad();
        hierarchyViewPage.callAction(ActionsContainer.CREATE_GROUP_ID, CREATE_SUBLOCATION_ACTION_ID);
        waitForPageToLoad();
        SublocationWizardPage sublocationWizardPage = new SublocationWizardPage(driver);
        sublocationWizardPage.setSublocationType(ROW);
        waitForPageToLoad();
        sublocationWizardPage.setPreciseLocation(SUBLOCATION_NAME);
        waitForPageToLoad();
        sublocationWizardPage.clickNext();
        sublocationWizardPage.create();
        checkPopupAndCloseMessage();
    }

    @Test(priority = 32, description = "Create 3 footprints", dependsOnMethods = {"showHierarchyViewFromPopup"})
    @Description("Create 3 footprints and check confirmation system message")
    public void createFootprint() {
        HierarchyViewPage hierarchyViewPage = HierarchyViewPage.getHierarchyViewPage(driver, webDriverWait);
        hierarchyViewPage.callAction(ActionsContainer.CREATE_GROUP_ID, CREATE_SUBLOCATION_ACTION_ID);
        SublocationWizardPage sublocationWizardPage = new SublocationWizardPage(driver);
        sublocationWizardPage.setSublocationType(FOOTPRINT);
        waitForPageToLoad();
        sublocationWizardPage.setPreciseLocation(ROW_1);
        waitForPageToLoad();
        sublocationWizardPage.setWidth("1");
        waitForPageToLoad();
        sublocationWizardPage.setDepth("1");
        waitForPageToLoad();
        sublocationWizardPage.clickNext();
        sublocationWizardPage.setQuantity("3");
        waitForPageToLoad();
        sublocationWizardPage.create();
        checkPopupAndCloseMessage();
    }

    @Test(priority = 33, description = "Set rack Precise Location to footprint", dependsOnMethods = {"setMountingEditor"})
    @Description("Set rack Precise Location to footprint and check confirmation system message")
    public void preciseRackLocation() {
        TableWidget tableWidget = getTableWidget(TABLE_SUBLOCATIONS);
        unselectAllAndSelectRow(tableWidget, MOUNTING_EDITOR_NAME);
        tableWidget.callAction(ActionsContainer.EDIT_GROUP_ID, UPDATE_SUBLOCATION_ACTION_ID);
        SublocationWizardPage sublocationWizardPage = new SublocationWizardPage(driver);
        sublocationWizardPage.setPreciseLocation(FOOTPRINT_1);
        waitForPageToLoad();
        sublocationWizardPage.create();
        checkPopupAndCloseMessage();
    }

    @Test(priority = 34, description = "Set device Precise Location to footprint", dependsOnMethods = {"createFootprint"})
    @Description("Set Device Precise Location to footprint and check confirmation system message")
    public void preciseDeviceLocation() {
        TableWidget tableWidget = getTableWidget(TABLE_DEVICES);
        unselectAllAndSelectRow(tableWidget, PHYSICAL_DEVICE_NAME);
        tableWidget.callAction(ActionsContainer.EDIT_GROUP_ID, UPDATE_DEVICE_ACTION_ID);
        DeviceWizardPage deviceWizardPage = new DeviceWizardPage(driver);
        waitForPageToLoad();
        deviceWizardPage.nextUpdateWizard();
        waitForPageToLoad();
        deviceWizardPage.setPreciseLocation(FOOTPRINT_3);
        waitForPageToLoad();
        deviceWizardPage.acceptUpdateWizard();
        waitForPageToLoad();
        checkPopupAndCloseMessage();
    }

    @Test(priority = 35, description = "Delete power supply unit", dependsOnMethods = {"createPowerSupplyUnit"})
    @Description("Delete power supply unit and check confirmation system message")
    public void deletePowerSupplyUnit() {
        deleteObjectFromDeviceTab(POWER_SUPPLY_UNIT_NAME);
        checkPopupAndCloseMessage();
    }

    @Test(priority = 36, description = "Delete power device", dependsOnMethods = {"createPowerDevice"})
    @Description("Delete power device and check confirmation system message")
    public void deletePowerDevice() {
        deleteObjectFromDeviceTab(POWER_DEVICE_NAME);
        checkPopupAndCloseMessage();
    }

    @Test(priority = 37, description = "Delete cooling unit", dependsOnMethods = {"createCoolingUnit"})
    @Description("Delete cooling unit and check confirmation system message")
    public void deleteCoolingUnit() {
        deleteObjectFromDeviceTab(COOLING_UNIT_NAME);
        checkPopupAndCloseMessage();
    }

    @Test(priority = 38, description = "Delete third device", dependsOnMethods = {"createThirdDevice"})
    @Description("Delete third device and check confirmation system message")
    public void deleteThirdDevice() {
        deleteObjectFromDeviceTab(PHYSICAL_DEVICE_NAME3);
        checkPopupAndCloseMessage();
    }

    @Test(priority = 39, description = "Delete second device", dependsOnMethods = {"createSecondDevice"})
    @Description("Delete second device and check confirmation system message")
    public void deleteSecondDevice() {
        deleteObjectFromDeviceTab(PHYSICAL_DEVICE_NAME2);
        checkPopupAndCloseMessage();
    }

    @Test(priority = 40, description = "Delete first device", dependsOnMethods = {"createPhysicalDevice"})
    @Description("Delete first device and check confirmation system message")
    public void deletePhysicalDevice() {
        deleteObjectFromDeviceTab(PHYSICAL_DEVICE_NAME);
        checkPopupAndCloseMessage();
    }

    @Test(priority = 41, description = "Delete cooling zone", dependsOnMethods = {"createCoolingZone"})
    @Description("Delete cooling zone and check confirmation system message")
    public void deleteCoolingZone() {
        TableInterface tableWidget = getTableWidget(TABLE_COOLING_ZONES);
        tableWidget.selectFirstRow();
        tableWidget.callAction(ActionsContainer.EDIT_GROUP_ID, DELETE_COOLING_ZONE_ACTION_ID);
        clickConfirmationBoxByLabel(DELETE_BUTTON_LABEL);//TODO po OSSPHY-56052 zmienić na id
        checkPopupAndCloseMessage();
    }

    @Test(priority = 42, description = "Delete 3 footprints", dependsOnMethods = {"createFootprint"})
    @Description("Delete 3 footprints and check confirmation system message")
    public void deleteFootprints() {
        deleteObjectFromSublocationTab(FOOTPRINT_1);
        checkPopupAndCloseMessage();

        deleteObjectFromSublocationTab(FOOTPRINT_2);
        checkPopupAndCloseMessage();

        deleteObjectFromSublocationTab(FOOTPRINT_3);
        checkPopupAndCloseMessage();
    }

    @Test(priority = 43, description = "Delete row", dependsOnMethods = {"createRow"})
    @Description("Delete row and check confirmation system message")
    public void deleteRow() {
        deleteObjectFromSublocationTab(ROW_1);
        checkPopupAndCloseMessage();
    }

    @Test(priority = 44, description = "Delete room", dependsOnMethods = {"createRoom"})
    @Description("Delete room and check confirmation system message")
    public void deleteRoom() {
        deleteObjectFromSublocationTab(SUBLOCATION_NAME);
    }

    @Test(priority = 45, description = "Delete location", dependsOnMethods = {"createBuilding"})
    @Description("Delete location")
    public void deleteLocation() {
        HierarchyViewPage hierarchyViewPage = HierarchyViewPage.getHierarchyViewPage(driver, webDriverWait);
        hierarchyViewPage.selectFirstObject();
        hierarchyViewPage.callAction(ActionsContainer.EDIT_GROUP_ID, DELETE_LOCATION_ACTION_ID);
        clickConfirmationBoxByLabel(DELETE_BUTTON_LABEL);//TODO po OSSPHY-56052 zmienić na id
        checkPopupAndCloseMessage();
        waitForPageToLoad();
        hierarchyViewPage.hasNoData();
    }

    private void checkPopup() {
        getSuccesSystemMessage();
        waitForPageToLoad();
    }

    private void checkPopupAndCloseMessage() {
        SystemMessageInterface systemMessage = getSuccesSystemMessage();
        systemMessage.close();
        waitForPageToLoad();
    }

    private SystemMessageInterface getSuccesSystemMessage() {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, new WebDriverWait(driver, 90));
        Assert.assertEquals((systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getMessageType()), MessageType.SUCCESS);
        return systemMessage;
    }

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    private void deleteObjectFromDeviceTab(String objectName) {
        TableWidget tableWidget = getTableWidget(TABLE_DEVICES);
        unselectAllAndSelectRow(tableWidget, objectName);
        tableWidget.callAction(ActionsContainer.EDIT_GROUP_ID, DELETE_DEVICE_ACTION_ID);
        waitForPageToLoad();
        clickConfirmationBox(DELETE_DEVICE_CONFIRMATION_ID);
    }

    private void deleteObjectFromSublocationTab(String objectName) {
        TableWidget tableWidget = getTableWidget(TABLE_SUBLOCATIONS);
        unselectAllAndSelectRow(tableWidget, objectName);
        tableWidget.callAction(ActionsContainer.EDIT_GROUP_ID, DELETE_SUBLOCATION_ACTION_ID);
        clickConfirmationBoxByLabel(DELETE_BUTTON_LABEL);//TODO po OSSPHY-56052 zmienić na id
    }

    private TableWidget getTableWidget(String tableId) {
        openTab(tableId);
        return TableWidget.createById(driver, tableId, webDriverWait);
    }

    private OldTable getOldTableWidget() {
        openTab(TABLE_POWER_MANAGEMENT);
        return OldTable.createById(driver, webDriverWait, ISPConfigurationTest.TABLE_POWER_MANAGEMENT);
    }

    private void openTab(String tableId) {
        HierarchyViewPage hierarchyViewPage = HierarchyViewPage.getHierarchyViewPage(driver, webDriverWait);
        TabsWidget tabsWidget = hierarchyViewPage.getBottomTabsWidget();
        tabsWidget.selectTabById(String.format(TAB_PATTERN, tableId));
        waitForPageToLoad();
    }

    private void clickConfirmationBox(String actionId) {
        ConfirmationBox.create(driver, webDriverWait).clickButtonById(actionId);
    }

    private void clickConfirmationBoxByLabel(String actionLabel) {
        ConfirmationBox.create(driver, webDriverWait).clickButtonByLabel(actionLabel);
    }

    private void unselectAllAndSelectRow(TableWidget tableWidget, String value) {
        tableWidget.unselectAllRows();
        waitForPageToLoad();
        tableWidget.selectRowByAttributeValue(NAME_COLUMN, value);
    }
}

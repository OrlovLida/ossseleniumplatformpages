package com.oss.E2E;

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
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.table.TableInterface;
import com.oss.pages.physical.CardCreateWizardPage;
import com.oss.pages.physical.ChangeCardModelWizard;
import com.oss.pages.physical.ChangeModelWizardPage;
import com.oss.pages.physical.CoolingZoneEditorWizardPage;
import com.oss.pages.physical.CreateCoolingZoneWizardPage;
import com.oss.pages.physical.DeviceWizardPage;
import com.oss.pages.physical.LocationOverviewPage;
import com.oss.pages.physical.LocationOverviewPage.TabName;
import com.oss.pages.physical.LocationWizardPage;
import com.oss.pages.physical.MountingEditorWizardPage;
import com.oss.pages.physical.SublocationWizardPage;
import com.oss.pages.platform.HierarchyViewPage;

import io.qameta.allure.Description;

import static java.lang.String.format;

public class ISPConfigurationTest extends BaseTestCase {

    private static final Logger log = LoggerFactory.getLogger(ISPConfigurationTest.class);
    private static final String LOCATION_NAME = "ISPConfiguration_Building";
    private static final String SUBLOCATION_NAME = "ISPConfiguration_Room";
    private static final String PHYSICAL_DEVICE_MODEL = "7360 ISAM FX-8";
    private static final String PHYSICAL_DEVICE_NAME = "ISPPhysicalDevice";
    private static final String PHYSICAL_DEVICE_MODEL2 = "JDSU OTU8000";
    private static final String PHYSICAL_DEVICE_MODEL3 = "ADVA Optical Networking FMT/1HU";
    private static final String PHYSICAL_DEVICE_NAME2 = "ISPPhysicalDevice3";
    private static final String PHYSICAL_DEVICE_NAME3 = "ISPPhysicalDevice2";
    private static final String MODEL_UPDATE = "Alcatel 7302 ISAM";
    private static final String MOUNTING_EDITOR_MODEL = "Generic 19\" 42U 600x800 (Bottom-Up)";
    private static final String MOUNTING_EDITOR_NAME = "ISPMountingEditor";
    private static final String COOLING_ZONE_NAME = "ISPCoolingZone";
    private static final String COOLING_UNIT_MODEL = "Generic Cooling Unit";
    private static final String COOLING_UNIT_NAME = "ISPCoolingUnit";
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
    private static final String DELETE_DEVICE_ACTION_ID = "frameworkCustomMore_DeleteDeviceWizardAction";
    private static final String CREATE_DEVICE_ACTION_ID = "CreateDeviceOnLocationWizardAction";
    private static final String COOLING_ZONE_EDITOR_ACTION_ID = "frameworkCustomMore_cooling_zone_editor_button";
    private static final String DELETE_COOLING_ZONE_ACTION_ID = "cooling-zone-delete-action";
    private static final String UPDATE_DEVICE = "frameworkCustomMore_UpdateDeviceWizardAction";
    private static final String NAME = "Name";
    private static final String ASSERT_NOT_EQUALS = "The checked value is %s and it shouldn't be equal to the defined %s value";
    private static final String LOCATION_POWER_CAPACITY = "0.00";
    private static String COOLING_ZONE_COOLING_LOAD = "0.00";
    private static String COOLING_ZONE_LOAD_RATIO = "0.00";
    private static String COOLING_ZONE_CAPACITY = "0.00";
    private String LOCATION_OVERVIEW_URL = "";
    private SoftAssert softAssert;

    @BeforeClass
    @Description("Open Console and set Live perspective")
    public void openConsoleAndSetLivePerspective() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        PerspectiveChooser.create(driver, webDriverWait).setLivePerspective();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        softAssert = new SoftAssert();
    }

    @Test(priority = 1, description = "Create building")
    @Description("Create building")
    public void createBuilding() {
        homePage.chooseFromLeftSideMenu("Create Physical Location", "Infrastructure management", "Create Infrastructure");
        LocationWizardPage locationWizardPage = new LocationWizardPage(driver);
        locationWizardPage.setLocationType("Building");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        locationWizardPage.setLocationName(LOCATION_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        locationWizardPage.clickNext();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        locationWizardPage.setGeographicalAddress("a");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        locationWizardPage.clickNext();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        locationWizardPage.accept();
        checkPopup();
    }

    @Test(priority = 2, description = "Show building in Location Overview", dependsOnMethods = {"createBuilding"})
    @Description("Show newly created building in Location Overview")
    public void showLocationOverviewFromPopup() {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        systemMessage.clickMessageLink();
        systemMessage.close();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        LOCATION_OVERVIEW_URL = driver.getCurrentUrl();
    }

    @Test(priority = 3, description = "Open Create Sublocation wizard", dependsOnMethods = {"showLocationOverviewFromPopup"})
    @Description("Open Create Sublocation wizard")
    public void openSublocationWizard() {
        LocationOverviewPage locationOverviewPage = new LocationOverviewPage(driver);
        locationOverviewPage.clickButton("Create Sublocation");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 4, description = "Create room", dependsOnMethods = {"openSublocationWizard"})
    @Description("Create room and check confirmation system message")
    public void createRoom() {
        SublocationWizardPage sublocationWizardPage = new SublocationWizardPage(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        sublocationWizardPage.setSublocationType("Room");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        sublocationWizardPage.setSublocationName(SUBLOCATION_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        sublocationWizardPage.clickNext();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        sublocationWizardPage.clickAccept();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        checkPopupAndCloseMessage();
    }

    @Test(priority = 5, description = "Open Create Device wizard", dependsOnMethods = {"createRoom"})
    @Description("Open Create Device wizard")
    public void openDeviceWizard() {
        LocationOverviewPage locationOverviewPage = new LocationOverviewPage(driver);
        locationOverviewPage.clickButton("Create Physical Device");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 6, description = "Create physical device", dependsOnMethods = {"openDeviceWizard"})
    @Description("Create physical device and check confirmation system message")
    public void createPhysicalDevice() {
        DeviceWizardPage deviceWizardPage = new DeviceWizardPage(driver);
        deviceWizardPage.setModel(PHYSICAL_DEVICE_MODEL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.setName(PHYSICAL_DEVICE_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.next();
        deviceWizardPage.setPreciseLocation(SUBLOCATION_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.accept();
        checkPopup();
    }

    @Test(priority = 7, description = "Show device in Hierarchy View", dependsOnMethods = {"createPhysicalDevice"})
    @Description("Show device in Hierarchy View by clicking in direct link from system message")
    public void showHierarchyViewFromPopup() {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        systemMessage.clickMessageLink();
        systemMessage.close();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 8, description = "Open Change Model wizard", dependsOnMethods = {"showHierarchyViewFromPopup"})
    @Description("Select device and open Change Model wizard")
    public void openChangeModelWizard() {
        HierarchyViewPage hierarchyViewPage = new HierarchyViewPage(driver);
        hierarchyViewPage.selectFirstObject();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        hierarchyViewPage.getMainTree().callActionById("EDIT", "ChangeDeviceModelAction");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 9, description = "Change device model", dependsOnMethods = {"openChangeModelWizard"})
    @Description("Change device model and check confirmation system message")
    public void changeDeviceModel() {
        ChangeModelWizardPage changeModelWizardPage = new ChangeModelWizardPage(driver);
        changeModelWizardPage.setModel(MODEL_UPDATE);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        DelayUtils.sleep(1000);
        changeModelWizardPage.clickUpdate();
        checkPopupAndCloseMessage();
    }

    @Test(priority = 10, description = "Open Create Card wizard", dependsOnMethods = {"changeDeviceModel"})
    @Description("Open Create Card wizard")
    public void openCreateCardWizard() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        HierarchyViewPage hierarchyViewPage = new HierarchyViewPage(driver);
        hierarchyViewPage.getMainTree().callActionById("CREATE", "CreateCardOnDeviceAction");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 11, description = "Create card", dependsOnMethods = {"openCreateCardWizard"})
    @Description("Create card and check confirmation system message")
    public void createCard() {
        CardCreateWizardPage cardCreateWizardPage = new CardCreateWizardPage(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cardCreateWizardPage.setModel("Alcatel NELT-B");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cardCreateWizardPage.setSlot("Chassis\\LT3");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cardCreateWizardPage.setSlot("Chassis\\LT4");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cardCreateWizardPage.clickAccept();
        checkPopupAndCloseMessage();
    }

    @Test(priority = 12, description = "Open Change Card Model wizard", dependsOnMethods = {"createCard"})
    @Description("Refresh page, select newly created card and open Change Card Model wizard")
    public void openChangeCardWizard() {
        driver.navigate().refresh();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        HierarchyViewPage hierarchyViewPage = new HierarchyViewPage(driver);
        String labelpath = PHYSICAL_DEVICE_NAME + ".Chassis.Chassis.Slots.LT3.Card.NELT-B";
        hierarchyViewPage.selectNodeByLabelsPath(labelpath);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        hierarchyViewPage.callAction(ActionsContainer.EDIT_GROUP_ID, "ChangeCardModelAction");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 13, description = "Change Card Model", dependsOnMethods = {"openChangeCardWizard"})
    @Description("Change Card Model and check confirmation system message")
    public void changeCardModel() {
        ChangeCardModelWizard changeCardModelWizard = new ChangeCardModelWizard(driver);
        changeCardModelWizard.setModelCard("Alcatel NELT-A");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        changeCardModelWizard.clickSubmit();
        checkPopupAndCloseMessage();
    }

    @Test(priority = 14, description = "Open Mounting Editor wizard", dependsOnMethods = {"changeCardModel"})
    @Description("Refresh page, select device and open Mounting Editor wizard")
    public void openMountingEditorWizard() {
        driver.navigate().refresh();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        HierarchyViewPage hierarchyViewPage = new HierarchyViewPage(driver);
        hierarchyViewPage.selectNodeByLabel(PHYSICAL_DEVICE_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        hierarchyViewPage.callAction(ActionsContainer.SHOW_ON_GROUP_ID, "MountingEditorForPhysicalElementAction");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 15, description = "Set Mounting Editor", dependsOnMethods = {"openMountingEditorWizard"})
    @Description("Set Mounting Editor and check confirmation system message")
    public void setMountingEditor() {
        MountingEditorWizardPage mountingEditorWizardPage = new MountingEditorWizardPage(driver);
        mountingEditorWizardPage.setName(MOUNTING_EDITOR_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        mountingEditorWizardPage.setPreciseLocation(SUBLOCATION_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        mountingEditorWizardPage.setModel(MOUNTING_EDITOR_MODEL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        mountingEditorWizardPage.clickNext();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        mountingEditorWizardPage.clickCheckbox();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        mountingEditorWizardPage.clickAccept();
        checkPopupAndCloseMessage();
    }

    @Test(priority = 16, description = "Move to Location Overview and create cooling zone", dependsOnMethods = {"showLocationOverviewFromPopup"})
    @Description("Move to Location Overview by direct link, create cooling zone from Cooling Zones tab and check confirmation system message")
    public void createCoolingZone() {
        driver.get(format(LOCATION_OVERVIEW_URL, BASIC_URL));
        LocationOverviewPage locationOverviewPage = new LocationOverviewPage(driver);
        locationOverviewPage.selectTab("Cooling Zones");
        locationOverviewPage.clickActionById(TabName.COOLING_ZONES, "cooling-zone-location-context-create-action");
        CreateCoolingZoneWizardPage coolingZoneWizard = new CreateCoolingZoneWizardPage(driver);
        coolingZoneWizard.setName(COOLING_ZONE_NAME);
        coolingZoneWizard.clickAccept();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        checkPopupAndCloseMessage();
    }

    @Test(priority = 17, description = "Create cooling unit", dependsOnMethods = {"createCoolingZone"})
    @Description("Create cooling unit from Devices tab and check confirmation system message")
    public void createCoolingUnit() {
        LocationOverviewPage locationOverviewPage = new LocationOverviewPage(driver);
        locationOverviewPage.selectTab("Devices");
        locationOverviewPage.clickActionById(TabName.DEVICES, CREATE_DEVICE_ACTION_ID);
        DeviceWizardPage deviceWizardPage = new DeviceWizardPage(driver);
        deviceWizardPage.setModel(COOLING_UNIT_MODEL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.setName(COOLING_UNIT_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.setCoolingCapacity(COOLING_CAPACITY);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.next();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.setPreciseLocation(SUBLOCATION_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.accept();
        checkPopupAndCloseMessage();
    }

    @Test(priority = 18, description = "Assign cooling unit to cooling zone", dependsOnMethods = {"createCoolingUnit"})
    @Description("Assign cooling unit to cooling zone")
    public void assignCoolingUnitToCoolingZone() {
        LocationOverviewPage locationOverviewPage = new LocationOverviewPage(driver);
        locationOverviewPage.selectTab("Devices");
        locationOverviewPage.filterObjectInSpecificTab(TabName.DEVICES, NAME, COOLING_UNIT_NAME);
        locationOverviewPage.clickActionById(TabName.DEVICES, COOLING_ZONE_EDITOR_ACTION_ID);
        CoolingZoneEditorWizardPage coolingZoneWizard = new CoolingZoneEditorWizardPage(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        coolingZoneWizard.selectNameFromList(COOLING_ZONE_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        coolingZoneWizard.clickUpdate();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 19, description = "Create second device", dependsOnMethods = {"showLocationOverviewFromPopup"})
    @Description("Create second device and check confirmation system message")
    public void createSecondDevice() {
        LocationOverviewPage locationOverviewPage = new LocationOverviewPage(driver);
        locationOverviewPage.selectTab("Devices");
        locationOverviewPage.clickActionById(TabName.DEVICES, CREATE_DEVICE_ACTION_ID);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        DeviceWizardPage deviceWizardPage = new DeviceWizardPage(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.setModel(PHYSICAL_DEVICE_MODEL2);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.setName(PHYSICAL_DEVICE_NAME2);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.setHeatEmission(DEVICE_HEAT_EMISSION);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.setPowerConsumption(DEVICE_POWER_CONSUMPTION);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.next();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.setPreciseLocation(SUBLOCATION_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.accept();
        checkPopupAndCloseMessage();
    }

    @Test(priority = 20, description = "Assign second device to cooling zone", dependsOnMethods = {"createSecondDevice"})
    @Description("Assign second device to cooling zone")
    public void assignSecondDeviceToCoolingZone() {
        LocationOverviewPage locationOverviewPage = new LocationOverviewPage(driver);
        locationOverviewPage.selectTab("Devices");
        locationOverviewPage.filterObjectInSpecificTab(TabName.DEVICES, NAME, PHYSICAL_DEVICE_NAME2);
        locationOverviewPage.clickActionById(TabName.DEVICES, COOLING_ZONE_EDITOR_ACTION_ID);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        CoolingZoneEditorWizardPage coolingZoneWizardPage = new CoolingZoneEditorWizardPage(driver);
        coolingZoneWizardPage.selectNameFromList(COOLING_ZONE_NAME);
        coolingZoneWizardPage.clickUpdate();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        DelayUtils.sleep(10000);
    }

    @Test(priority = 21, description = "Check cooling values", dependsOnMethods = {"assignSecondDeviceToCoolingZone"})
    @Description("Refresh page and check cooling values in Cooling Zones tab")
    public void checkCoolingValues() {
        driver.navigate().refresh();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        LocationOverviewPage locationOverviewPage = new LocationOverviewPage(driver);
        locationOverviewPage.selectTab("Cooling Zones");
        TableInterface coolingTable = locationOverviewPage.getTabTable(TabName.COOLING_ZONES);
        int rowNumber = coolingTable.getRowNumber(COOLING_ZONE_NAME, NAME);
        String coolingLoad = coolingTable.getCellValue(rowNumber, "Cooling Load [kW]");
        log.info("Cooling load = {}", coolingLoad);
        String coolingCapacity = coolingTable.getCellValue(rowNumber, "Cooling Capacity [kW]");
        log.info("Cooling capacity = {}", coolingCapacity);
        String coolingLoadRatio = coolingTable.getCellValue(rowNumber, "Cooling Load Ratio [%]");
        log.info("Cooling load ratio = {}", coolingLoadRatio);
        Assert.assertNotEquals(coolingLoad, COOLING_ZONE_COOLING_LOAD, String.format(ASSERT_NOT_EQUALS, coolingLoad, COOLING_ZONE_COOLING_LOAD));
        Assert.assertNotEquals(coolingCapacity, COOLING_ZONE_CAPACITY, String.format(ASSERT_NOT_EQUALS, coolingCapacity, COOLING_ZONE_CAPACITY));
        Assert.assertNotEquals(coolingLoadRatio, COOLING_ZONE_LOAD_RATIO, String.format(ASSERT_NOT_EQUALS, coolingLoadRatio, COOLING_ZONE_LOAD_RATIO));
        COOLING_ZONE_COOLING_LOAD = coolingLoad;
        COOLING_ZONE_LOAD_RATIO = coolingLoadRatio;
        COOLING_ZONE_CAPACITY = coolingCapacity;
    }

    @Test(priority = 22, description = "Create third device", dependsOnMethods = {"showLocationOverviewFromPopup"})
    @Description("Create third device and check confirmation system message")
    public void createThirdDevice() {
        LocationOverviewPage locationOverviewPage = new LocationOverviewPage(driver);
        locationOverviewPage.selectTab("Devices");
        locationOverviewPage.clickActionById(TabName.DEVICES, CREATE_DEVICE_ACTION_ID);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        DeviceWizardPage deviceWizardPage = new DeviceWizardPage(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.setModel(PHYSICAL_DEVICE_MODEL3);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.setName(PHYSICAL_DEVICE_NAME3);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.setHeatEmission(DEVICE_HEAT_EMISSION);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.next();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.setPreciseLocation(SUBLOCATION_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.accept();
        checkPopupAndCloseMessage();
    }

    @Test(priority = 23, description = "Assign third device to cooling zone", dependsOnMethods = {"createThirdDevice"})
    @Description("Assign third device to cooling zone")
    public void assignThirdDeviceToCoolingZone() {
        LocationOverviewPage locationOverviewPage = new LocationOverviewPage(driver);
        locationOverviewPage.selectTab("Devices");
        locationOverviewPage.filterObjectInSpecificTab(TabName.DEVICES, NAME, PHYSICAL_DEVICE_NAME3);
        locationOverviewPage.clickActionById(TabName.DEVICES, COOLING_ZONE_EDITOR_ACTION_ID);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        CoolingZoneEditorWizardPage coolingZoneWizardPage = new CoolingZoneEditorWizardPage(driver);
        coolingZoneWizardPage.selectNameFromList(COOLING_ZONE_NAME);
        coolingZoneWizardPage.clickUpdate();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 24, description = "Update cooling unit", dependsOnMethods = {"assignThirdDeviceToCoolingZone"})
    @Description("Update Cooling Unit and check confirmation system message")
    public void updateCoolingUnit() {
        LocationOverviewPage locationOverviewPage = new LocationOverviewPage(driver);
        locationOverviewPage.selectTab("Devices");
        locationOverviewPage.filterObjectInSpecificTab(TabName.DEVICES, NAME, COOLING_UNIT_NAME);
        locationOverviewPage.clickActionById(TabName.DEVICES, UPDATE_DEVICE);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        DeviceWizardPage deviceWizardPage = new DeviceWizardPage(driver);
        deviceWizardPage.setCoolingCapacity(COOLING_CAPACITY2);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.nextUpdateWizard();
        deviceWizardPage.acceptUpdateWizard();
        checkPopupAndCloseMessage();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 25, description = "Check updated cooling values", dependsOnMethods = {"updateCoolingUnit"})
    @Description("Refresh page and check updated cooling values in cooling zones tab")
    public void checkUpdatedCoolingValues() {
        driver.navigate().refresh();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        LocationOverviewPage locationOverviewPage = new LocationOverviewPage(driver);
        locationOverviewPage.selectTab("Cooling Zones");
        TableInterface coolingTable = locationOverviewPage.getTabTable(TabName.COOLING_ZONES);
        int rowNumber = coolingTable.getRowNumber(COOLING_ZONE_NAME, NAME);
        String coolingLoad = coolingTable.getCellValue(rowNumber, "Cooling Load [kW]");
        log.info("Cooling load = {}", coolingLoad);
        String coolingCapacity = coolingTable.getCellValue(rowNumber, "Cooling Capacity [kW]");
        log.info("Cooling capacity = {}", coolingCapacity);
        String coolingLoadRatio = coolingTable.getCellValue(rowNumber, "Cooling Load Ratio [%]");
        log.info("Cooling load ratio= {}", coolingLoadRatio);
        Assert.assertNotEquals(coolingLoad, COOLING_ZONE_COOLING_LOAD, String.format(ASSERT_NOT_EQUALS, coolingLoad, COOLING_ZONE_COOLING_LOAD));
        Assert.assertNotEquals(coolingCapacity, COOLING_ZONE_CAPACITY, String.format(ASSERT_NOT_EQUALS, coolingCapacity, COOLING_ZONE_CAPACITY));
        Assert.assertNotEquals(coolingLoadRatio, COOLING_ZONE_LOAD_RATIO, String.format(ASSERT_NOT_EQUALS, coolingLoadRatio, COOLING_ZONE_LOAD_RATIO));
    }

    @Test(priority = 26, description = "Create power device", dependsOnMethods = {"showLocationOverviewFromPopup"})
    @Description("Create power device from Devices tab and check confirmation system message")
    public void createPowerDevice() {
        LocationOverviewPage locationOverviewPage = new LocationOverviewPage(driver);
        locationOverviewPage.selectTab("Devices");
        locationOverviewPage.clickActionById(TabName.DEVICES, CREATE_DEVICE_ACTION_ID);
        DeviceWizardPage deviceWizardPage = new DeviceWizardPage(driver);
        deviceWizardPage.setModel(POWER_DEVICE_MODEL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.setName(POWER_DEVICE_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.setPowerCapacity(POWER_DEVICE_CAPACITY);
        deviceWizardPage.next();
        deviceWizardPage.setPreciseLocation(SUBLOCATION_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.accept();
        checkPopupAndCloseMessage();
    }

    @Test(priority = 27, description = "Check power values", dependsOnMethods = {"createPowerDevice"})
    @Description("Check power values in Power Management tab")
    public void checkPowerValues() {
        LocationOverviewPage locationOverviewPage = new LocationOverviewPage(driver);
        locationOverviewPage.selectTab("Power Management");
        locationOverviewPage.clickRefreshInSpecificTab(TabName.POWER_MANAGEMENT);
        TableInterface powerManagementTable = locationOverviewPage.getTabTable(TabName.POWER_MANAGEMENT);
        int rowNumber = powerManagementTable.getRowNumber(SUBLOCATION_NAME, NAME);
        String rowValue = powerManagementTable.getCellValue(rowNumber, "Power Capacity [kW]");
        Assert.assertNotEquals(rowValue, LOCATION_POWER_CAPACITY, String.format(ASSERT_NOT_EQUALS, rowValue, LOCATION_POWER_CAPACITY));
        rowValue = powerManagementTable.getCellValue(rowNumber, "Power Load [kW]");
        Assert.assertNotEquals(rowValue, "0", String.format(ASSERT_NOT_EQUALS, rowValue, "0"));
        rowValue = powerManagementTable.getCellValue(rowNumber, "Power Load Ratio [%]");
        Assert.assertNotEquals(rowValue, "0", String.format(ASSERT_NOT_EQUALS, rowValue, "0"));
    }

    @Test(priority = 28, description = "Create power supply unit", dependsOnMethods = {"checkPowerValues"})
    @Description("Create power supply unit and check confirmation system message")
    public void createPowerSupplyUnit() {
        LocationOverviewPage locationOverviewPage = new LocationOverviewPage(driver);
        locationOverviewPage.selectTab("Devices");
        locationOverviewPage.clickActionById(TabName.DEVICES, CREATE_DEVICE_ACTION_ID);
        DeviceWizardPage deviceWizardPage = new DeviceWizardPage(driver);
        deviceWizardPage.setModel(POWER_SUPPLY_UNIT_MODEL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.setName(POWER_SUPPLY_UNIT_NAME);
        deviceWizardPage.setPowerCapacity(POWER_SUPPLY_UNIT_CAPACITY);
        deviceWizardPage.next();
        deviceWizardPage.setPreciseLocation(SUBLOCATION_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.accept();
        checkPopupAndCloseMessage();
    }

    @Test(priority = 29, description = "Create row", dependsOnMethods = {"showLocationOverviewFromPopup"})
    @Description("Create row and check confirmation system message")
    public void createRow() {
        LocationOverviewPage locationOverviewPage = new LocationOverviewPage(driver);
        locationOverviewPage.selectTab("Locations");
        locationOverviewPage.filterObjectInSpecificTab(TabName.LOCATIONS, NAME, SUBLOCATION_NAME);
        locationOverviewPage.clickActionById(TabName.LOCATIONS, "frameworkCustomMore_sublocation-create-action");
        SublocationWizardPage sublocationWizardPage = new SublocationWizardPage(driver);
        sublocationWizardPage.setSublocationType("Row");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        sublocationWizardPage.clickNext();
        sublocationWizardPage.clickAccept();
        checkPopupAndCloseMessage();
    }

    @Test(priority = 30, description = "Create 3 footprints", dependsOnMethods = {"showLocationOverviewFromPopup"})
    @Description("Create 3 footprints and check confirmation system message")
    public void createFootprint() {
        LocationOverviewPage locationOverviewPage = new LocationOverviewPage(driver);
        locationOverviewPage.filterObjectInSpecificTab(TabName.LOCATIONS, NAME, "Row1");
        locationOverviewPage.clickActionById(TabName.LOCATIONS, "frameworkCustomMore_sublocation-create-action");
        SublocationWizardPage sublocationWizardPage = new SublocationWizardPage(driver);
        sublocationWizardPage.setSublocationType("Footprint");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        sublocationWizardPage.setWidth("1");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        sublocationWizardPage.setDepth("1");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        sublocationWizardPage.clickNext();
        sublocationWizardPage.setQuantity("3");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        sublocationWizardPage.clickAccept();
        checkPopupAndCloseMessage();
    }

    @Test(priority = 31, description = "Set rack Precise Location to footprint", dependsOnMethods = {"setMountingEditor"})
    @Description("Set rack Precise Location to footprint and check confirmation system message")
    public void preciseRackLocation() {
        LocationOverviewPage locationOverviewPage = new LocationOverviewPage(driver);
        locationOverviewPage.filterObjectInSpecificTab(TabName.LOCATIONS, NAME, MOUNTING_EDITOR_NAME);
        locationOverviewPage.clickActionById(TabName.LOCATIONS, "frameworkCustomMore_location-update-action");
        SublocationWizardPage sublocationWizardPage = new SublocationWizardPage(driver);
        sublocationWizardPage.setPreciseLocation("Footprint1");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        sublocationWizardPage.clickAccept();
        checkPopupAndCloseMessage();
    }

    @Test(priority = 32, description = "Set device Precise Location to footprint", dependsOnMethods = {"createFootprint"})
    @Description("Set Device Precise Location to footprint and check confirmation system message")
    public void preciseDeviceLocation() {
        LocationOverviewPage locationOverviewPage = new LocationOverviewPage(driver);
        locationOverviewPage.selectTab("Devices");
        locationOverviewPage.filterObjectInSpecificTab(TabName.DEVICES, NAME, PHYSICAL_DEVICE_NAME);
        locationOverviewPage.clickActionById(TabName.DEVICES, UPDATE_DEVICE);
        DeviceWizardPage deviceWizardPage = new DeviceWizardPage(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.nextUpdateWizard();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.setPreciseLocation("Footprint3");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.acceptUpdateWizard();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        checkPopupAndCloseMessage();
    }

    @Test(priority = 33, description = "Delete power supply unit", dependsOnMethods = {"createPowerSupplyUnit"})
    @Description("Delete power supply unit and check confirmation system message")
    public void deletePowerSupplyUnit() {
        LocationOverviewPage locationOverviewPage = new LocationOverviewPage(driver);
        locationOverviewPage.filterObjectInSpecificTab(TabName.DEVICES, NAME, POWER_SUPPLY_UNIT_NAME);
        locationOverviewPage.clickActionById(TabName.DEVICES, DELETE_DEVICE_ACTION_ID);
        locationOverviewPage.clickButtonInConfirmationBox("Yes");
        checkPopupAndCloseMessage();
    }

    @Test(priority = 34, description = "Delete power device", dependsOnMethods = {"createPowerDevice"})
    @Description("Delete power device and check confirmation system message")
    public void deletePowerDevice() {
        LocationOverviewPage locationOverviewPage = new LocationOverviewPage(driver);
        locationOverviewPage.selectTab("Devices");
        locationOverviewPage.filterObjectInSpecificTab(TabName.DEVICES, NAME, POWER_DEVICE_NAME);
        locationOverviewPage.clickActionById(TabName.DEVICES, DELETE_DEVICE_ACTION_ID);
        locationOverviewPage.clickButtonInConfirmationBox("Yes");
        checkPopupAndCloseMessage();
    }

    @Test(priority = 35, description = "Delete third device", dependsOnMethods = {"createThirdDevice"})
    @Description("Delete third device and check confirmation system message")
    public void deleteThirdDevice() {
        LocationOverviewPage locationOverviewPage = new LocationOverviewPage(driver);
        locationOverviewPage.selectTab("Devices");
        locationOverviewPage.filterObjectInSpecificTab(TabName.DEVICES, NAME, PHYSICAL_DEVICE_NAME3);
        locationOverviewPage.clickActionById(TabName.DEVICES, DELETE_DEVICE_ACTION_ID);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        locationOverviewPage.clickButtonInConfirmationBox("Yes");
        checkPopupAndCloseMessage();
    }

    @Test(priority = 36, description = "Delete second device", dependsOnMethods = {"createSecondDevice"})
    @Description("Delete second device and check confirmation system message")
    public void deleteSecondDevice() {
        LocationOverviewPage locationOverviewPage = new LocationOverviewPage(driver);
        locationOverviewPage.selectTab("Devices");
        locationOverviewPage.filterObjectInSpecificTab(TabName.DEVICES, NAME, PHYSICAL_DEVICE_NAME2);
        locationOverviewPage.clickActionById(TabName.DEVICES, DELETE_DEVICE_ACTION_ID);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        locationOverviewPage.clickButtonInConfirmationBox("Yes");
        checkPopupAndCloseMessage();
    }

    @Test(priority = 37, description = "Delete first device", dependsOnMethods = {"createPhysicalDevice"})
    @Description("Delete first device and check confirmation system message")
    public void deletePhysicalDevice() {
        LocationOverviewPage locationOverviewPage = new LocationOverviewPage(driver);
        locationOverviewPage.selectTab("Devices");
        locationOverviewPage.filterObjectInSpecificTab(TabName.DEVICES, NAME, PHYSICAL_DEVICE_NAME);
        locationOverviewPage.clickActionById(TabName.DEVICES, DELETE_DEVICE_ACTION_ID);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        locationOverviewPage.clickButtonInConfirmationBox("Yes");
        checkPopupAndCloseMessage();
    }

    @Test(priority = 38, description = "Delete cooling unit", dependsOnMethods = {"createCoolingUnit"})
    @Description("Delete cooling unit and check confirmation system message")
    public void deleteCoolingUnit() {
        LocationOverviewPage locationOverviewPage = new LocationOverviewPage(driver);
        locationOverviewPage.selectTab("Devices");
        locationOverviewPage.filterObjectInSpecificTab(TabName.DEVICES, NAME, COOLING_UNIT_NAME);
        locationOverviewPage.clickActionById(TabName.DEVICES, DELETE_DEVICE_ACTION_ID);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        locationOverviewPage.clickButtonInConfirmationBox("Yes");
        checkPopupAndCloseMessage();
    }

    @Test(priority = 39, description = "Delete cooling zone", dependsOnMethods = {"createCoolingZone"})
    @Description("Delete cooling zone and check confirmation system message")
    public void deleteCoolingZone() {
        LocationOverviewPage locationOverviewPage = new LocationOverviewPage(driver);
        locationOverviewPage.selectTab("Cooling Zones");
        locationOverviewPage.filterObjectInSpecificTab(TabName.COOLING_ZONES, NAME, COOLING_ZONE_NAME);
        locationOverviewPage.clickActionById(TabName.COOLING_ZONES, DELETE_COOLING_ZONE_ACTION_ID);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        locationOverviewPage.clickButtonInConfirmationBox("Delete");
        checkPopupAndCloseMessage();
    }

    @Test(priority = 40, description = "Delete 3 footprints", dependsOnMethods = {"createFootprint"})
    @Description("Delete 3 footprints and check confirmation system message")
    public void deleteFootprints() {
        LocationOverviewPage locationOverviewPage = new LocationOverviewPage(driver);
        locationOverviewPage.selectTab("Locations");

        locationOverviewPage.filterObjectInSpecificTab(TabName.LOCATIONS, NAME, "Footprint1");
        locationOverviewPage.clickActionById(TabName.LOCATIONS, "frameworkCustomMore_location-delete-action");
        locationOverviewPage.clickButtonInConfirmationBox("Delete");
        checkPopupAndCloseMessage();

        locationOverviewPage.filterObjectInSpecificTab(TabName.LOCATIONS, NAME, "Footprint2");
        locationOverviewPage.clickActionById(TabName.LOCATIONS, "frameworkCustomMore_location-delete-action");
        locationOverviewPage.clickButtonInConfirmationBox("Delete");
        checkPopupAndCloseMessage();

        locationOverviewPage.filterObjectInSpecificTab(TabName.LOCATIONS, NAME, "Footprint3");
        locationOverviewPage.clickActionById(TabName.LOCATIONS, "frameworkCustomMore_location-delete-action");
        locationOverviewPage.clickButtonInConfirmationBox("Delete");
        checkPopupAndCloseMessage();
    }

    @Test(priority = 41, description = "Delete row", dependsOnMethods = {"createRow"})
    @Description("Delete row and check confirmation system message")
    public void deleteRow() {
        LocationOverviewPage locationOverviewPage = new LocationOverviewPage(driver);
        locationOverviewPage.filterObjectInSpecificTab(TabName.LOCATIONS, NAME, "Row1");
        locationOverviewPage.clickActionById(TabName.LOCATIONS, "frameworkCustomMore_location-delete-action");
        locationOverviewPage.clickButtonInConfirmationBox("Delete");
        checkPopupAndCloseMessage();
    }

    @Test(priority = 42, description = "Delete room", dependsOnMethods = {"createRoom"})
    @Description("Delete room and check confirmation system message")
    public void deleteRoom() {
        LocationOverviewPage locationOverviewPage = new LocationOverviewPage(driver);
        locationOverviewPage.selectTab("Locations");
        locationOverviewPage.filterObjectInSpecificTab(TabName.LOCATIONS, NAME, SUBLOCATION_NAME);
        locationOverviewPage.clickActionById(TabName.LOCATIONS, "frameworkCustomMore_location-delete-action");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        locationOverviewPage.clickButtonInConfirmationBox("Delete");
        checkPopupAndCloseMessage();
    }

    @Test(priority = 43, description = "Delete location", dependsOnMethods = {"createBuilding"})
    @Description("Delete location")
    public void deleteLocation() {
        LocationOverviewPage locationOverviewPage = new LocationOverviewPage(driver);
        locationOverviewPage.clickButton("Delete Location");
        locationOverviewPage.clickButtonInConfirmationBox("Delete");
    }

    private void checkPopup() {
        getSuccesSystemMessage();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    private void checkPopupAndCloseMessage() {
        SystemMessageInterface systemMessage = getSuccesSystemMessage();
        systemMessage.close();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    private SystemMessageInterface getSuccesSystemMessage() {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, new WebDriverWait(driver, 90));
        softAssert.assertEquals((systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getMessageType()), MessageType.SUCCESS);
        return systemMessage;
    }
}

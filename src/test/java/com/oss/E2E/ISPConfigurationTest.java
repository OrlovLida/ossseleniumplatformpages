package com.oss.E2E;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.alerts.SystemMessageContainer;
import com.oss.framework.alerts.SystemMessageContainer.MessageType;
import com.oss.framework.alerts.SystemMessageInterface;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.mainheader.PerspectiveChooser;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.tablewidget.TableInterface;
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

    private String LOCATION_OVERVIEW_URL = "";
    private static final String LOCATION_NAME = "ISPConfiguration_Building";
    private static final String SUBLOCATION_NAME = "ISPConfiguration_Room";
    private static final String PHYSICAL_DEVICE_MODEL = "7360 ISAM FX-8";
    private static final String PHYSICAL_DEVICE_NAME = "ISPPhysicalDevice";
    private static final String PHYSICAL_DEVICE_MODEL2 = "ADVA Optical Networking FMT/1HU";
    private static final String PHYSICAL_DEVICE_MODEL3 = "JDSU OTU8000";
    private static final String PHYSICAL_DEVICE_NAME2 = "ISPPhysicalDevice2";
    private static final String PHYSICAL_DEVICE_NAME3 = "ISPPhysicalDevice3";
    private static final String MODEL_UPDATE = "Alcatel 7302";
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
    private static String COOLING_ZONE_COOLING_LOAD = "0.00";
    private static String COOLING_ZONE_LOAD_RATIO = "0.00";
    private static String COOLING_ZONE_CAPACITY = "0.00";
    private static final String DELETE_DEVICE = "Delete Device";
    private static final String UPDATE_DEVICE = "UpdateDeviceWizardAction";
    private static final String NAME = "Name";
    private static final String ASSERT_NOT_EQUALS = "The checked value is %s and it shouldn't be equal to the defined %s value";
    private static final String LOCATION_POWER_CAPACITY = "0.00";

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
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, new WebDriverWait(driver, 75));
        Assert.assertEquals((systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getMessageType()), MessageType.SUCCESS);
        return systemMessage;
    }

    @BeforeClass
    @Description("Open Create Location Wizard")
    public void openCreateLocationWizard() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        PerspectiveChooser.create(driver, webDriverWait).setLivePerspective();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        homePage.chooseFromLeftSideMenu("Create Physical Location", "Infrastructure management", "Create Infrastructure");
    }

    @Test(priority = 1)
    @Description("Create Building")
    public void createBuilding() {
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

    @Test(priority = 2)
    @Description("Show building in Location Overview")
    public void showLocationOverviewFromPopup() {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        systemMessage.clickMessageLink();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        LOCATION_OVERVIEW_URL = driver.getCurrentUrl();
    }

    @Test(priority = 3)
    @Description("Open Create Sublocation Wizard")
    public void openSublocationWizard() {
        LocationOverviewPage locationOverviewPage = new LocationOverviewPage(driver);
        locationOverviewPage.clickButton("Create Sublocation");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 4)
    @Description("Create Room")
    public void createRoom() {
        SublocationWizardPage sublocationWizardPage = new SublocationWizardPage(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        sublocationWizardPage.setSublocationType("Room");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        sublocationWizardPage.setSublocationName(SUBLOCATION_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        sublocationWizardPage.clickNext();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        sublocationWizardPage.accept();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        checkPopupAndCloseMessage();
    }

    @Test(priority = 5)
    @Description("Open Create Device Wizard")
    public void openDeviceWizard() {
        LocationOverviewPage locationOverviewPage = new LocationOverviewPage(driver);
        locationOverviewPage.clickButton("Create Device");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 6)
    @Description("Create Physical Device")
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

    @Test(priority = 7)
    @Description("Show device in Device Overview")
    public void showHierarchyViewFromPopup() {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        systemMessage.clickMessageLink();
        systemMessage.close();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 8)
    @Description("Open Change Model Wizard")
    public void openChangeModelWizard() {
        HierarchyViewPage hierarchyViewPage = new HierarchyViewPage(driver);
        hierarchyViewPage.selectFirstObject();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        hierarchyViewPage.getMainTree().callActionById("EDIT", "DeviceChangeModelAction");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 9)
    @Description("Change device model")
    public void changeDeviceModel() {
        ChangeModelWizardPage changeModelWizardPage = new ChangeModelWizardPage(driver);
        changeModelWizardPage.setModel(MODEL_UPDATE);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        DelayUtils.sleep(1000);
        changeModelWizardPage.clickUpdate();
        checkPopupAndCloseMessage();
    }

    @Test(priority = 10)
    @Description("Open Create Card Wizard")
    public void openCreateCardWizard() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        HierarchyViewPage hierarchyViewPage = new HierarchyViewPage(driver);
        hierarchyViewPage.getMainTree().callActionById("CREATE", "CreateCardOnDeviceAction");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 11)
    @Description("Set Card Model")
    public void setCardModel() {
        CardCreateWizardPage cardCreateWizardPage = new CardCreateWizardPage(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cardCreateWizardPage.setModel("Alcatel NELT-B");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cardCreateWizardPage.setSlot("LT3");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cardCreateWizardPage.setSlot("LT4");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cardCreateWizardPage.clickAccept();
        checkPopupAndCloseMessage();
    }

    @Test(priority = 12)
    @Description("Open Change Card Model Wizard")
    public void openChangeCardWizard() {
        driver.navigate().refresh();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        HierarchyViewPage hierarchyViewPage = new HierarchyViewPage(driver);
        String labelpath = PHYSICAL_DEVICE_NAME + ".Chassis.Chassis.Slots.LT3.Card.NELT-B";
        hierarchyViewPage.selectNodeByLabelsPath(labelpath);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        hierarchyViewPage.useTreeContextAction(ActionsContainer.EDIT_GROUP_ID, "CardChangeModelAction");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 13)
    @Description("Change Card Model")
    public void changeCardModel() {
        ChangeCardModelWizard changeCardModelWizard = new ChangeCardModelWizard(driver);
        changeCardModelWizard.setModelCard("Alcatel NELT-A");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        changeCardModelWizard.clickUpdate();
        checkPopupAndCloseMessage();
    }

    @Test(priority = 14)
    @Description("Open Mounting Editor Wizard")
    public void openMountingEditorWizard() {
        driver.navigate().refresh();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        HierarchyViewPage hierarchyViewPage = new HierarchyViewPage(driver);
        hierarchyViewPage.selectNodeByLabel(PHYSICAL_DEVICE_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        hierarchyViewPage.useTreeContextAction(ActionsContainer.SHOW_ON_GROUP_ID, "MountingEditorForPhysicalElementAction");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 15)
    @Description("Set Mounting Editor")
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

    @Test(priority = 16)
    @Description("Move to Location Overview and create Cooling Zone")
    public void createCoolingZone() {
        driver.get(format(LOCATION_OVERVIEW_URL, BASIC_URL));
        LocationOverviewPage locationOverviewPage = new LocationOverviewPage(driver);
        locationOverviewPage.selectTab("Cooling Zones");
        locationOverviewPage.clickButtonByLabelInSpecificTab(TabName.COOLING_ZONES, "Create Cooling Zone");
        CreateCoolingZoneWizardPage coolingZoneWizard = new CreateCoolingZoneWizardPage(driver);
        coolingZoneWizard.setName(COOLING_ZONE_NAME);
        coolingZoneWizard.clickAccept();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        checkPopupAndCloseMessage();
    }

    @Test(priority = 17)
    @Description("Create Cooling Unit")
    public void createCoolingUnit() {
        LocationOverviewPage locationOverviewPage = new LocationOverviewPage(driver);
        locationOverviewPage.selectTab("Devices");
        locationOverviewPage.clickButtonByLabelInSpecificTab(TabName.DEVICES, "Create Device");
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

    @Test(priority = 18)
    @Description("Assign Cooling Unit to Cooling Zone")
    public void assignCoolingUnitToCoolingZone() {
        LocationOverviewPage locationOverviewPage = new LocationOverviewPage(driver);
        locationOverviewPage.selectTab("Devices");
        locationOverviewPage.filterObjectInSpecificTab(TabName.DEVICES, NAME, COOLING_UNIT_NAME);
        locationOverviewPage.clickButtonByLabelInSpecificTab(TabName.DEVICES, "Cooling Zone Editor");
        CoolingZoneEditorWizardPage coolingZoneWizard = new CoolingZoneEditorWizardPage(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        coolingZoneWizard.selectNameFromList(COOLING_ZONE_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        coolingZoneWizard.clickUpdate();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 19)
    @Description("Create physical Device3")
    public void createDevice3() {
        LocationOverviewPage locationOverviewPage = new LocationOverviewPage(driver);
        locationOverviewPage.selectTab("Devices");
        locationOverviewPage.clickButtonByLabelInSpecificTab(TabName.DEVICES, "Create Device");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        DeviceWizardPage deviceWizardPage = new DeviceWizardPage(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.setModel(PHYSICAL_DEVICE_MODEL3);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.setName(PHYSICAL_DEVICE_NAME3);
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

    @Test(priority = 20)
    @Description("Assign Device to Cooling Zone")
    public void assignDeviceToCoolingZone() {
        LocationOverviewPage locationOverviewPage = new LocationOverviewPage(driver);
        locationOverviewPage.selectTab("Devices");
        locationOverviewPage.filterObjectInSpecificTab(TabName.DEVICES, NAME, PHYSICAL_DEVICE_NAME3);
        locationOverviewPage.clickButtonByLabelInSpecificTab(TabName.DEVICES, "Cooling Zone Editor");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        CoolingZoneEditorWizardPage coolingZoneWizardPage = new CoolingZoneEditorWizardPage(driver);
        coolingZoneWizardPage.selectNameFromList(COOLING_ZONE_NAME);
        coolingZoneWizardPage.clickUpdate();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        driver.navigate().refresh();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        locationOverviewPage = new LocationOverviewPage(driver);
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

    @Test(priority = 21)
    @Description("Create Physical Device2")
    public void createPhysicalDevice2() {
        LocationOverviewPage locationOverviewPage = new LocationOverviewPage(driver);
        locationOverviewPage.selectTab("Devices");
        locationOverviewPage.clickButtonByLabelInSpecificTab(TabName.DEVICES, "Create Device");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        DeviceWizardPage deviceWizardPage = new DeviceWizardPage(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.setModel(PHYSICAL_DEVICE_MODEL2);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.setName(PHYSICAL_DEVICE_NAME2);
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

    @Test(priority = 22)
    @Description("Assign Physcial Device2 to Cooling Zone")
    public void assignDevice2ToCoolingZone() {
        LocationOverviewPage locationOverviewPage = new LocationOverviewPage(driver);
        locationOverviewPage.selectTab("Devices");
        locationOverviewPage.filterObjectInSpecificTab(TabName.DEVICES, NAME, PHYSICAL_DEVICE_NAME2);
        locationOverviewPage.clickButtonByLabelInSpecificTab(TabName.DEVICES, "Cooling Zone Editor");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        CoolingZoneEditorWizardPage coolingZoneWizardPage = new CoolingZoneEditorWizardPage(driver);
        coolingZoneWizardPage.selectNameFromList(COOLING_ZONE_NAME);
        coolingZoneWizardPage.clickUpdate();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 23)
    @Description("Update Cooling Unit")
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
        driver.navigate().refresh();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        locationOverviewPage = new LocationOverviewPage(driver);
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

    @Test(priority = 24)
    @Description("Create Power Device")
    public void createPowerDevice() {
        LocationOverviewPage locationOverviewPage = new LocationOverviewPage(driver);
        locationOverviewPage.selectTab("Devices");
        locationOverviewPage.clickButtonByLabelInSpecificTab(TabName.DEVICES, "Create Device");
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

    @Test(priority = 25)
    @Description("Create Power Supply Unit")
    public void createPowerSupplyUnit() {
        LocationOverviewPage locationOverviewPage = new LocationOverviewPage(driver);
        locationOverviewPage.selectTab("Devices");
        locationOverviewPage.clickButtonByLabelInSpecificTab(TabName.DEVICES, "Create Device");
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

    @Test(priority = 26)
    @Description("Create Row")
    public void createRow() {
        LocationOverviewPage locationOverviewPage = new LocationOverviewPage(driver);
        locationOverviewPage.selectTab("Locations");
        locationOverviewPage.filterObjectInSpecificTab(TabName.LOCATIONS, NAME, SUBLOCATION_NAME);
        locationOverviewPage.clickButtonByLabelInSpecificTab(TabName.LOCATIONS, "Create Sublocation");
        SublocationWizardPage sublocationWizardPage = new SublocationWizardPage(driver);
        sublocationWizardPage.setSublocationType("Row");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        sublocationWizardPage.clickNext();
        sublocationWizardPage.accept();
        checkPopupAndCloseMessage();
    }

    @Test(priority = 27)
    @Description("Create 3x Footprint")
    public void createFootprint() {
        LocationOverviewPage locationOverviewPage = new LocationOverviewPage(driver);
        locationOverviewPage.filterObjectInSpecificTab(TabName.LOCATIONS, NAME, "rh01");
        locationOverviewPage.clickButtonByLabelInSpecificTab(TabName.LOCATIONS, "Create Sublocation");
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
        sublocationWizardPage.accept();
        checkPopupAndCloseMessage();
    }

    @Test(priority = 28)
    @Description("Edit Rack Precise Location to Footprint")
    public void preciseRackLocation() {
        LocationOverviewPage locationOverviewPage = new LocationOverviewPage(driver);
        locationOverviewPage.filterObjectInSpecificTab(TabName.LOCATIONS, NAME, MOUNTING_EDITOR_NAME);
        locationOverviewPage.clickButtonByLabelInSpecificTab(TabName.LOCATIONS, "Edit Location");
        SublocationWizardPage sublocationWizardPage = new SublocationWizardPage(driver);
        sublocationWizardPage.setPreciseLocation("fp01");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        sublocationWizardPage.accept();
        checkPopupAndCloseMessage();
    }

    @Test(priority = 29)
    @Description("Edit Device Precise Location to Footprint")
    public void preciseDeviceLocation() {
        LocationOverviewPage locationOverviewPage = new LocationOverviewPage(driver);
        locationOverviewPage.selectTab("Devices");
        locationOverviewPage.filterObjectInSpecificTab(TabName.DEVICES, NAME, PHYSICAL_DEVICE_NAME);
        locationOverviewPage.clickActionById(TabName.DEVICES, UPDATE_DEVICE);
        DeviceWizardPage deviceWizardPage = new DeviceWizardPage(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.nextUpdateWizard();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.setPreciseLocation("fp03");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.acceptUpdateWizard();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        checkPopupAndCloseMessage();
    }

    @Test(priority = 30)
    @Description("Delete Power Supply Unit")
    public void deletePowerSupplyUnit() {
        LocationOverviewPage locationOverviewPage = new LocationOverviewPage(driver);
        locationOverviewPage.filterObjectInSpecificTab(TabName.DEVICES, NAME, POWER_SUPPLY_UNIT_NAME);
        locationOverviewPage.clickButtonByLabelInSpecificTab(TabName.DEVICES, DELETE_DEVICE);
        locationOverviewPage.clickButtonInConfirmationBox("Yes");
        checkPopupAndCloseMessage();
    }

    @Test(priority = 31)
    @Description("Delete Power Device")
    public void deletePowerDevice() {
        LocationOverviewPage locationOverviewPage = new LocationOverviewPage(driver);
        locationOverviewPage.selectTab("Devices");
        locationOverviewPage.filterObjectInSpecificTab(TabName.DEVICES, NAME, POWER_DEVICE_NAME);
        locationOverviewPage.clickButtonByLabelInSpecificTab(TabName.DEVICES, DELETE_DEVICE);
        locationOverviewPage.clickButtonInConfirmationBox("Yes");
        checkPopupAndCloseMessage();
    }

    @Test(priority = 32)
    @Description("Delete Physical Device3")
    public void deletePhysicalDevice3() {
        LocationOverviewPage locationOverviewPage = new LocationOverviewPage(driver);
        locationOverviewPage.selectTab("Devices");
        locationOverviewPage.filterObjectInSpecificTab(TabName.DEVICES, NAME, PHYSICAL_DEVICE_NAME3);
        locationOverviewPage.clickButtonByLabelInSpecificTab(TabName.DEVICES, DELETE_DEVICE);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        locationOverviewPage.clickButtonInConfirmationBox("Yes");
        checkPopupAndCloseMessage();
    }

    @Test(priority = 33)
    @Description("Delete Physical Device2")
    public void deletePhysicalDevice2() {
        LocationOverviewPage locationOverviewPage = new LocationOverviewPage(driver);
        locationOverviewPage.selectTab("Devices");
        locationOverviewPage.filterObjectInSpecificTab(TabName.DEVICES, NAME, PHYSICAL_DEVICE_NAME2);
        locationOverviewPage.clickButtonByLabelInSpecificTab(TabName.DEVICES, DELETE_DEVICE);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        locationOverviewPage.clickButtonInConfirmationBox("Yes");
        checkPopupAndCloseMessage();
    }

    @Test(priority = 34)
    @Description("Delete Physical Device")
    public void deletePhysicalDevice() {
        LocationOverviewPage locationOverviewPage = new LocationOverviewPage(driver);
        locationOverviewPage.selectTab("Devices");
        locationOverviewPage.filterObjectInSpecificTab(TabName.DEVICES, NAME, PHYSICAL_DEVICE_NAME);
        locationOverviewPage.clickButtonByLabelInSpecificTab(TabName.DEVICES, DELETE_DEVICE);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        locationOverviewPage.clickButtonInConfirmationBox("Yes");
        checkPopupAndCloseMessage();
    }

    @Test(priority = 35)
    @Description("Delete Cooling Unit")
    public void deleteCoolingUnit() {
        LocationOverviewPage locationOverviewPage = new LocationOverviewPage(driver);
        locationOverviewPage.selectTab("Devices");
        locationOverviewPage.filterObjectInSpecificTab(TabName.DEVICES, NAME, COOLING_UNIT_NAME);
        locationOverviewPage.clickButtonByLabelInSpecificTab(TabName.DEVICES, DELETE_DEVICE);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        locationOverviewPage.clickButtonInConfirmationBox("Yes");
        checkPopupAndCloseMessage();
    }

    @Test(priority = 36)
    @Description("Delete Cooling Zone")
    public void deleteCoolingZone() {
        LocationOverviewPage locationOverviewPage = new LocationOverviewPage(driver);
        locationOverviewPage.selectTab("Cooling Zones");
        locationOverviewPage.filterObjectInSpecificTab(TabName.COOLING_ZONES, NAME, COOLING_ZONE_NAME);
        locationOverviewPage.clickButtonByLabelInSpecificTab(TabName.COOLING_ZONES, "Delete Cooling Zone");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        locationOverviewPage.clickButtonInConfirmationBox("Delete");
        checkPopupAndCloseMessage();
    }

    @Test(priority = 37)
    @Description("Delete Footprints")
    public void deleteFootprints() {
        LocationOverviewPage locationOverviewPage = new LocationOverviewPage(driver);
        locationOverviewPage.selectTab("Locations");

        locationOverviewPage.filterObjectInSpecificTab(TabName.LOCATIONS, NAME, "fp01");
        locationOverviewPage.clickButtonByLabelInSpecificTab(TabName.LOCATIONS, "Delete Location");
        locationOverviewPage.clickButtonInConfirmationBox("Delete");
        checkPopupAndCloseMessage();

        locationOverviewPage.filterObjectInSpecificTab(TabName.LOCATIONS, NAME, "fp02");
        locationOverviewPage.clickButtonByLabelInSpecificTab(TabName.LOCATIONS, "Delete Location");
        locationOverviewPage.clickButtonInConfirmationBox("Delete");
        checkPopupAndCloseMessage();

        locationOverviewPage.filterObjectInSpecificTab(TabName.LOCATIONS, NAME, "fp03");
        locationOverviewPage.clickButtonByLabelInSpecificTab(TabName.LOCATIONS, "Delete Location");
        locationOverviewPage.clickButtonInConfirmationBox("Delete");
        checkPopupAndCloseMessage();
    }

    @Test(priority = 38)
    @Description("Delete Row")
    public void deleteRow() {
        LocationOverviewPage locationOverviewPage = new LocationOverviewPage(driver);
        locationOverviewPage.filterObjectInSpecificTab(TabName.LOCATIONS, NAME, "rh01");
        locationOverviewPage.clickButtonByLabelInSpecificTab(TabName.LOCATIONS, "Delete Location");
        locationOverviewPage.clickButtonInConfirmationBox("Delete");
        checkPopupAndCloseMessage();
    }

    @Test(priority = 39)
    @Description("Delete Room")
    public void deleteRoom() {
        LocationOverviewPage locationOverviewPage = new LocationOverviewPage(driver);
        locationOverviewPage.selectTab("Locations");
        locationOverviewPage.filterObjectInSpecificTab(TabName.LOCATIONS, NAME, SUBLOCATION_NAME);
        locationOverviewPage.clickButtonByLabelInSpecificTab(TabName.LOCATIONS, "Delete Location");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        locationOverviewPage.clickButtonInConfirmationBox("Delete");
        checkPopupAndCloseMessage();
    }

    @Test(priority = 40)
    @Description("Delete Location")
    public void deleteLocation() {
        LocationOverviewPage locationOverviewPage = new LocationOverviewPage(driver);
        locationOverviewPage.clickButton("Delete Location");
        locationOverviewPage.clickButtonInConfirmationBox("Delete");
    }
}

package com.oss.E2E;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.alerts.SystemMessageContainer;
import com.oss.framework.alerts.SystemMessageInterface;
import com.oss.framework.mainheader.PerspectiveChooser;
import com.oss.framework.prompts.ConfirmationBox;
import com.oss.framework.prompts.ConfirmationBoxInterface;
import com.oss.framework.sidemenu.SideMenu;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.framework.widgets.tablewidget.TableInterface;
import com.oss.pages.physical.CardCreateWizardPage;
import com.oss.pages.physical.ChangeCardModelWizard;
import com.oss.pages.physical.ChangeModelWizardPage;
import com.oss.pages.physical.CreateCoolingZoneWizardPage;
import com.oss.pages.physical.DeviceOverviewPage;
import com.oss.pages.physical.DeviceWizardPage;
import com.oss.pages.physical.LocationOverviewPage;
import com.oss.pages.physical.LocationOverviewPage.TabName;
import com.oss.pages.physical.LocationWizardPage;
import com.oss.pages.physical.MountingEditorWizardPage;
import com.oss.pages.physical.SublocationWizardPage;

import io.qameta.allure.Description;

import static java.lang.String.format;

public class ISPConfiguration extends BaseTestCase {

    String LOCATION_OVERVIEW_URL = "";
    public static final String LOCATION_NAME = "ISPConfiguration_Building";
    public static final String SUBLOCATION_NAME = "ISPConfiguration_Room";
    public static final String GEOGRAPHICAL_ADDRESS = "test";//"Test";
    public static final String PHYSICAL_DEVICE_MODEL = "Alcatel 7360 ISAM FX-8";
    public static final String PHYSICAL_DEVICE_NAME = "ISPPhysicalDevice";
    public static final String PHYSICAL_DEVICE_MODEL2 = "Cisco Systems Inc. Nexus 7010";
    public static final String PHYSICAL_DEVICE_NAME2 = "ISPPhysicalDevice2";
    public static final String MODEL_UPDATE = "Alcatel 7302";
    public static final String MOUNTING_EDITOR_MODEL = "Generic 19\" 42U 600x800 (Bottom-Up)";
    public static final String MOUNTING_EDITOR_NAME = "ISPMountingEditor";
    public static final String COOLING_ZONE_NAME = "ISPCoolingZone";
    public static final String COOLING_UNIT_MODEL = "Generic Cooling Unit";
    public static final String COOLING_UNIT_NAME = "ISPCoolingUnit";
    public static final String COOLING_CAPACITY = "14";
    public static final String COOLING_CAPACITY2 = "16";
    public static final String DEVICE_HEAT_EMISSION = "3";
    public static final String DEVICE_POWER_CONSUMPTION = "4";
    public static final String POWER_DEVICE_MODEL = "Generic Generic Power Source";
    public static final String POWER_DEVICE_NAME = "Power Source";
    public static final String POWER_DEVICE_CAPACITY = "10";
    public static final String POWER_SUPPLY_UNIT_MODEL = "Generic Power Supply Unit";
    public static final String POWER_SUPPLY_UNIT_NAME = "Power Supply Unit";
    public static final String POWER_SUPPLY_UNIT_CAPACITY = "5";
    public static String LOCATION_POWER_CAPACITY = "0";
    public static String COOLING_ZONE_COOLING_LOAD = "0";
    public static String COOLING_ZONE_LOAD_RATIO = "0";

    private void checkPopup() {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assertions.assertThat(messages).hasSize(1);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getMessageType())
                .isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
    }

    //TODO: after OSSPHY-46774 change all usages of filter{x}Object() methods to filterObject() in the whole scenario
    @BeforeClass
    @Description("Open Create Location Wizard")
    public void openCreateLocationWizard() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        PerspectiveChooser.create(driver, webDriverWait).setLivePerspective();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        SideMenu sideMenu = SideMenu.create(driver, webDriverWait);
        sideMenu.callActionByLabel("Create Location", "Wizards", "Physical Inventory");
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
        locationWizardPage.setGeographicalAddress(GEOGRAPHICAL_ADDRESS);
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
        DeviceOverviewPage deviceOverviewPage = new DeviceOverviewPage(driver);
        deviceOverviewPage.useContextAction("Create Sublocation");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 4)
    @Description("Create Room")
    public void createRoom() {
        SublocationWizardPage sublocationWizardPage = new SublocationWizardPage(driver);
        sublocationWizardPage.setSublocationType("Room");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        sublocationWizardPage.setSublocationName(SUBLOCATION_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        sublocationWizardPage.clickNext();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        sublocationWizardPage.Accept();
        checkPopup();
    }

    @Test(priority = 5, enabled = false)
    @Description("Open Create Device Wizard")
    public void openDeviceWizard() {
        DeviceOverviewPage deviceOverviewPage = new DeviceOverviewPage(driver);
        deviceOverviewPage.useContextAction("Create Device");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 6, enabled = false)
    @Description("Create Physical Device")
    public void createPhysicalDevice() {
        DeviceWizardPage deviceWizardPage = new DeviceWizardPage(driver);
        deviceWizardPage.setModel(PHYSICAL_DEVICE_MODEL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.setName(PHYSICAL_DEVICE_NAME);
        deviceWizardPage.setPreciseLocation(SUBLOCATION_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.create();
        checkPopup();
    }

    @Test(priority = 7, enabled = false)
    @Description("Show device in Device Overview")
    public void showDeviceOverviewFromPopup() {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        systemMessage.clickMessageLink();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 8, enabled = false)
    @Description("Open Change Model Wizard")
    public void openChangeModelWizard() {
        DeviceOverviewPage deviceOverviewPage = new DeviceOverviewPage(driver);
        deviceOverviewPage.selectTreeRow(PHYSICAL_DEVICE_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceOverviewPage.useContextAction("Change Model");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 9, enabled = false)
    @Description("Change device model")
    public void changeDeviceModel() {
        ChangeModelWizardPage changeModelWizardPage = new ChangeModelWizardPage(driver);
        changeModelWizardPage.setModel(MODEL_UPDATE);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        changeModelWizardPage.clickUpdate();
        checkPopup();
    }

    @Test(priority = 10, enabled = false)
    @Description("Open Create Card Wizard")
    public void openCreateCardWizard() {
        DeviceOverviewPage deviceOverviewPage = new DeviceOverviewPage(driver);
        deviceOverviewPage.selectTreeRow(PHYSICAL_DEVICE_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceOverviewPage.useContextAction("Create Card");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 11, enabled = false)
    @Description("Set Card Model")
    public void setCardModel() {
        CardCreateWizardPage cardCreateWizardPage = new CardCreateWizardPage(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cardCreateWizardPage.setModel("Alcatel NELT-B");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cardCreateWizardPage.setSlots("LT3");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cardCreateWizardPage.setSlots("LT4");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cardCreateWizardPage.clickAccept();
        checkPopup();
    }

    @Test(priority = 12, enabled = false)
    @Description("Open Change Card Model Wizard")
    public void openChangeCardWizard() {
        DeviceOverviewPage deviceOverviewPage = new DeviceOverviewPage(driver);
        deviceOverviewPage.selectTreeRow("NELT-B");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceOverviewPage.useContextAction("Change model");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 13, enabled = false)
    @Description("Change Card Model")
    public void changeCardModel() {
        ChangeCardModelWizard changeCardModelWizard = new ChangeCardModelWizard(driver);
        changeCardModelWizard.setModelCard("Alcatel NGLT-A");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        changeCardModelWizard.clickUpdate();
        checkPopup();
    }

    @Test(priority = 14, enabled = false)
    @Description("Open Mounting Editor Wizard")
    public void openMountingEditorWizard() {
        DeviceOverviewPage deviceOverviewPage = new DeviceOverviewPage(driver);
        deviceOverviewPage.selectTreeRow(PHYSICAL_DEVICE_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceOverviewPage.useContextAction("Mounting Editor");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 15, enabled = false)
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
        checkPopup();
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
        coolingZoneWizard.clickProceed();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        checkPopup();
    }

    @Test(priority = 17, enabled = false)
    @Description("Create Cooling Unit")
    public void createCoolingUnit() {
        LocationOverviewPage locationOverviewPage = new LocationOverviewPage(driver);
        locationOverviewPage.selectTab("Devices");
        locationOverviewPage.clickButtonByLabelInSpecificTab(TabName.DEVICES, "Create Device");
        DeviceWizardPage deviceWizardPage = new DeviceWizardPage(driver);
        deviceWizardPage.setModel(COOLING_UNIT_MODEL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.setName(COOLING_UNIT_NAME);
        deviceWizardPage.setPreciseLocation(SUBLOCATION_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.setCoolingCapacity(COOLING_CAPACITY);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.create();
        checkPopup();
    }

    @Test(priority = 18, enabled = false)
    @Description("Assign Cooling Unit to Cooling Zone")
    public void assignCoolingUnitToCoolingZone() {
        LocationOverviewPage locationOverviewPage = new LocationOverviewPage(driver);
        locationOverviewPage.selectTab("Devices");
        locationOverviewPage.filterObjectInSpecificTab(TabName.DEVICES, "Name", COOLING_UNIT_NAME);
        locationOverviewPage.clickButtonByLabelInSpecificTab(TabName.DEVICES, "Cooling Zone Editor");
        CreateCoolingZoneWizardPage coolingZoneWizard = new CreateCoolingZoneWizardPage(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        coolingZoneWizard.setName(COOLING_ZONE_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        coolingZoneWizard.clickUpdate();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 19, enabled = false)
    @Description("Update physical Device")
    public void updateDevice() {
        LocationOverviewPage locationOverviewPage = new LocationOverviewPage(driver);
        locationOverviewPage.selectTab("Devices");
        locationOverviewPage.filterObjectInSpecificTab(TabName.DEVICES, "Name", PHYSICAL_DEVICE_NAME);
        locationOverviewPage.clickButtonByLabelInSpecificTab(TabName.DEVICES, "Update Device");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        DeviceWizardPage deviceWizardPage = new DeviceWizardPage(driver);
        deviceWizardPage.setHeatEmission(DEVICE_HEAT_EMISSION);
        deviceWizardPage.setPowerConsumption(DEVICE_POWER_CONSUMPTION);
        deviceWizardPage.update();
        checkPopup();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 20, enabled = false)
    @Description("Assign Device to Cooling Zone")
    public void assignDeviceToCoolingZone() {
        LocationOverviewPage locationOverviewPage = new LocationOverviewPage(driver);
        locationOverviewPage.selectTab("Devices");
        locationOverviewPage.filterObjectInSpecificTab(TabName.DEVICES, "Name", PHYSICAL_DEVICE_NAME);
        locationOverviewPage.clickButtonByLabelInSpecificTab(TabName.DEVICES, "Cooling Zone Editor");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        CreateCoolingZoneWizardPage coolingZoneWizardPage = new CreateCoolingZoneWizardPage(driver);
        coolingZoneWizardPage.selectNameFromList(COOLING_ZONE_NAME);
        coolingZoneWizardPage.clickUpdate();
        checkPopup();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        locationOverviewPage.selectTab("Cooling Zones");
        TableInterface coolingTable = locationOverviewPage.getTabTable(TabName.COOLING_ZONES);
        int rowNumber = coolingTable.getRowNumber(COOLING_ZONE_NAME, "Name");
        String rowValue = coolingTable.getValueCell(rowNumber, "Cooling Load [kW]");
        Assertions.assertThat(Integer.parseInt(COOLING_ZONE_COOLING_LOAD)).isNotEqualTo(Integer.parseInt(rowValue));
    }

    @Test(priority = 21, enabled = false)
    @Description("Create Physical Device2")
    public void createPhysicalDevice2() {
        LocationOverviewPage locationOverviewPage = new LocationOverviewPage(driver);
        locationOverviewPage.selectTab("Devices");
        locationOverviewPage.clickButtonByLabelInSpecificTab(TabName.DEVICES, "Create Device");
        DeviceWizardPage deviceWizardPage = new DeviceWizardPage(driver);
        deviceWizardPage.setModel(PHYSICAL_DEVICE_MODEL2);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.setName(PHYSICAL_DEVICE_NAME);
        deviceWizardPage.setPreciseLocation(SUBLOCATION_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.create();
        checkPopup();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 22, enabled = false)
    @Description("Assign Physcial Device2 to Cooling Zone")
    public void assignDevice2ToCoolingZone() {
        LocationOverviewPage locationOverviewPage = new LocationOverviewPage(driver);
        locationOverviewPage.selectTab("Devices");
        locationOverviewPage.filterObjectInSpecificTab(TabName.DEVICES, "Name", PHYSICAL_DEVICE_NAME2);
        locationOverviewPage.clickButtonByLabelInSpecificTab(TabName.DEVICES, "Cooling Zone Editor");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        CreateCoolingZoneWizardPage coolingZoneWizardPage = new CreateCoolingZoneWizardPage(driver);
        coolingZoneWizardPage.selectNameFromList(COOLING_ZONE_NAME);
        coolingZoneWizardPage.clickUpdate();
        checkPopup();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 23, enabled = false)
    @Description("Update Cooling Unit")
    public void updateCoolingUnit() {
        LocationOverviewPage locationOverviewPage = new LocationOverviewPage(driver);
        locationOverviewPage.selectTab("Devices");
        locationOverviewPage.filterObjectInSpecificTab(TabName.DEVICES, "Name", COOLING_UNIT_NAME);
        locationOverviewPage.clickButtonByLabelInSpecificTab(TabName.DEVICES, "Update Device");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        DeviceWizardPage deviceWizardPage = new DeviceWizardPage(driver);
        deviceWizardPage.setCoolingCapacity(COOLING_CAPACITY2);
        deviceWizardPage.update();
        checkPopup();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        locationOverviewPage.selectTab("Cooling Zones");
        TableInterface coolingTable = locationOverviewPage.getTabTable(TabName.COOLING_ZONES);
        int rowNumber = coolingTable.getRowNumber(COOLING_ZONE_NAME, "Name");
        String rowValue = coolingTable.getValueCell(rowNumber, "Cooling Load Ratio [%]");
        Assertions.assertThat(Integer.parseInt(COOLING_ZONE_LOAD_RATIO)).isNotEqualTo(Integer.parseInt(rowValue));
    }

    @Test(priority = 24, enabled = false)
    @Description("Create Power Device")
    public void createPowerDevice() {
        LocationOverviewPage locationOverviewPage = new LocationOverviewPage(driver);
        locationOverviewPage.selectTab("Devices");
        locationOverviewPage.clickButtonByLabelInSpecificTab(TabName.DEVICES, "Create Device");
        DeviceWizardPage deviceWizardPage = new DeviceWizardPage(driver);
        deviceWizardPage.setModel(POWER_DEVICE_MODEL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.setName(POWER_DEVICE_NAME);
        deviceWizardPage.setPreciseLocation(SUBLOCATION_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.setPowerCapacity(POWER_DEVICE_CAPACITY);
        deviceWizardPage.create();
        checkPopup();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        locationOverviewPage.selectTab("Cooling Zones");
        TableInterface coolingTable = locationOverviewPage.getTabTable(TabName.COOLING_ZONES);
        int rowNumber = coolingTable.getRowNumber(SUBLOCATION_NAME, "Name");
        String rowValue = coolingTable.getValueCell(rowNumber, "Power Capacity [kW]");
        Assertions.assertThat(Integer.parseInt(LOCATION_POWER_CAPACITY)).isNotEqualTo(Integer.parseInt(rowValue));
        LOCATION_POWER_CAPACITY = rowValue;
        rowValue = coolingTable.getValueCell(rowNumber, "Power Load [kW]");
        Assertions.assertThat(Integer.parseInt(rowValue)).isNotEqualTo(0);
        rowValue = coolingTable.getValueCell(rowNumber, "Power Load Ratio [%]");
        Assertions.assertThat(Integer.parseInt(rowValue)).isNotEqualTo(0);
    }

    @Test(priority = 25, enabled = false)
    @Description("Create Power Supply Unit")
    public void createPowerSupplyUnit() {
        LocationOverviewPage locationOverviewPage = new LocationOverviewPage(driver);
        locationOverviewPage.selectTab("Devices");
        locationOverviewPage.clickButtonByLabelInSpecificTab(TabName.DEVICES, "Create Device");
        DeviceWizardPage deviceWizardPage = new DeviceWizardPage(driver);
        deviceWizardPage.setModel(POWER_SUPPLY_UNIT_MODEL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.setPreciseLocation(SUBLOCATION_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.setName(POWER_SUPPLY_UNIT_NAME);
        deviceWizardPage.setPowerCapacity(POWER_SUPPLY_UNIT_CAPACITY);
        deviceWizardPage.create();
        checkPopup();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 26, enabled = false)
    @Description("Delete Power Supply Unit")
    public void deletePowerSupplyUnit() {
        LocationOverviewPage locationOverviewPage = new LocationOverviewPage(driver);
        locationOverviewPage.selectTab("Devices");
        locationOverviewPage.filterObjectInSpecificTab(TabName.DEVICES, "Name", POWER_SUPPLY_UNIT_NAME);
        locationOverviewPage.clickButtonByLabelInSpecificTab(TabName.DEVICES, "Delete Element");
        checkPopup();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 27, enabled = false)
    @Description("Delete Power Device")
    public void deletePowerDevice() {
        LocationOverviewPage locationOverviewPage = new LocationOverviewPage(driver);
        locationOverviewPage.selectTab("Devices");
        locationOverviewPage.filterObjectInSpecificTab(TabName.DEVICES, "Name", POWER_DEVICE_NAME);
        locationOverviewPage.clickButtonByLabelInSpecificTab(TabName.DEVICES, "Delete Element");
        checkPopup();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 28, enabled = false)
    @Description("Delete Physical Device2")
    public void deletePhysicalDevice2() {
        LocationOverviewPage locationOverviewPage = new LocationOverviewPage(driver);
        locationOverviewPage.selectTab("Devices");
        locationOverviewPage.filterObjectInSpecificTab(TabName.DEVICES, "Name", PHYSICAL_DEVICE_NAME2);
        locationOverviewPage.clickButtonByLabelInSpecificTab(TabName.DEVICES, "Delete Element");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        ConfirmationBoxInterface prompt = ConfirmationBox.create(driver, webDriverWait);
        prompt.clickButtonByLabel("Yes");
        checkPopup();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 29, enabled = false)
    @Description("Delete Physical Device")
    public void deletePhysicalDevice() {
        LocationOverviewPage locationOverviewPage = new LocationOverviewPage(driver);
        locationOverviewPage.selectTab("Devices");
        locationOverviewPage.filterObjectInSpecificTab(TabName.DEVICES, "Name", PHYSICAL_DEVICE_NAME);
        locationOverviewPage.clickButtonByLabelInSpecificTab(TabName.DEVICES, "Delete Element");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        ConfirmationBoxInterface prompt = ConfirmationBox.create(driver, webDriverWait);
        prompt.clickButtonByLabel("Yes");
        checkPopup();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 30, enabled = false)
    @Description("Delete Cooling Unit")
    public void deleteCoolingUnit() {
        LocationOverviewPage locationOverviewPage = new LocationOverviewPage(driver);
        locationOverviewPage.selectTab("Devices");
        locationOverviewPage.filterObjectInSpecificTab(TabName.DEVICES, "Name", COOLING_UNIT_NAME);
        locationOverviewPage.clickButtonByLabelInSpecificTab(TabName.DEVICES, "Delete Element");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        ConfirmationBoxInterface prompt = ConfirmationBox.create(driver, webDriverWait);
        prompt.clickButtonByLabel("Yes");
        checkPopup();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 31)
    @Description("Delete Cooling Zone")
    public void deleteCoolingZone() {
        LocationOverviewPage locationOverviewPage = new LocationOverviewPage(driver);
        locationOverviewPage.selectTab("Cooling Zones");
        locationOverviewPage.filterObjectInSpecificTab(TabName.COOLING_ZONES, "Name", COOLING_ZONE_NAME);
        locationOverviewPage.clickButtonByLabelInSpecificTab(TabName.COOLING_ZONES, "Delete Cooling Zone");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        ConfirmationBoxInterface prompt = ConfirmationBox.create(driver, webDriverWait);
        prompt.clickButtonByLabel("Delete Cooling Zone");
        checkPopup();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 32, enabled = false)
    @Description("Delete Rack")
    public void deleteRack() {
        LocationOverviewPage locationOverviewPage = new LocationOverviewPage(driver);
        locationOverviewPage.selectTab("Locations");
        locationOverviewPage.filterObjectInSpecificTab(TabName.LOCATIONS, "Name", MOUNTING_EDITOR_NAME);
        locationOverviewPage.clickButtonByLabelInSpecificTab(TabName.LOCATIONS, "Remove Location");
        checkPopup();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 33)
    @Description("Delete Room")
    public void deleteRoom() {
        LocationOverviewPage locationOverviewPage = new LocationOverviewPage(driver);
        locationOverviewPage.selectTab("Locations");
        locationOverviewPage.filterObjectInSpecificTab(TabName.LOCATIONS, "Name", SUBLOCATION_NAME + " 001");
        locationOverviewPage.clickButtonByLabelInSpecificTab(TabName.LOCATIONS, "Remove Location");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        ConfirmationBoxInterface prompt = ConfirmationBox.create(driver, webDriverWait);
        prompt.clickButtonByLabel("Delete");
        checkPopup();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 34)
    @Description("Delete Location")
    public void deleteLocation() {
        DeviceOverviewPage deviceOverviewPage = new DeviceOverviewPage(driver);
        deviceOverviewPage.useContextAction("Remove Location");
        Wizard removalWizard = Wizard.createByComponentId(driver, webDriverWait, "Popup");
        removalWizard.clickButtonByLabel("Delete");
    }
}
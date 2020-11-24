package com.oss.E2E;

import com.oss.BaseTestCase;
import com.oss.framework.alerts.SystemMessageContainer;
import com.oss.framework.alerts.SystemMessageInterface;
import com.oss.framework.mainheader.PerspectiveChooser;
import com.oss.framework.prompts.ConfirmationBox;
import com.oss.framework.prompts.ConfirmationBoxInterface;
import com.oss.framework.sidemenu.SideMenu;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.framework.widgets.tablewidget.OldTable;
import com.oss.pages.physical.*;
import io.qameta.allure.Description;
import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

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

    @Test(priority = 5)
    @Description("Open Create Device Wizard")
    public void openDeviceWizard() {
        DeviceOverviewPage deviceOverviewPage = new DeviceOverviewPage(driver);
        deviceOverviewPage.useContextAction("Create Device");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 6)
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

    @Test(priority = 7)
    @Description("Show device in Device Overview")
    public void showDeviceOverviewFromPopup() {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        systemMessage.clickMessageLink();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 8)
    @Description("Open Change Model Wizard")
    public void openChangeModelWizard() {
        DeviceOverviewPage deviceOverviewPage = new DeviceOverviewPage(driver);
        deviceOverviewPage.selectTreeRow(PHYSICAL_DEVICE_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceOverviewPage.useContextAction("Change Model");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 9)
    @Description("Change device model")
    public void changeDeviceModel() {
        ChangeModelWizardPage changeModelWizardPage = new ChangeModelWizardPage(driver);
        changeModelWizardPage.setModel(MODEL_UPDATE);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        changeModelWizardPage.clickUpdate();
        checkPopup();
    }

    @Test(priority = 10)
    @Description("Open Create Card Wizard")
    public void openCreateCardWizard() {
        DeviceOverviewPage deviceOverviewPage = new DeviceOverviewPage(driver);
        deviceOverviewPage.selectTreeRow(PHYSICAL_DEVICE_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceOverviewPage.useContextAction("Create Card");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 11)
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

    @Test(priority = 12)
    @Description("Open Change Card Model Wizard")
    public void openChangeCardWizard() {
        DeviceOverviewPage deviceOverviewPage = new DeviceOverviewPage(driver);
        deviceOverviewPage.selectTreeRow("NELT-B");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceOverviewPage.useContextAction("Change model");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 13)
    @Description("Change Card Model")
    public void changeCardModel() {
        ChangeCardModelWizard changeCardModelWizard = new ChangeCardModelWizard(driver);
        changeCardModelWizard.setModelCard("Alcatel NGLT-A");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        changeCardModelWizard.clickUpdate();
        checkPopup();
    }

    @Test(priority = 14)
    @Description("Open Mounting Editor Wizard")
    public void openMountingEditorWizard() {
        DeviceOverviewPage deviceOverviewPage = new DeviceOverviewPage(driver);
        deviceOverviewPage.selectTreeRow(PHYSICAL_DEVICE_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceOverviewPage.useContextAction("Mounting Editor");
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
        checkPopup();
    }

    @Test(priority = 16)
    @Description("Move to Location Overview and create Cooling Zone")
    public void createCoolingZone() {
        driver.get(format(LOCATION_OVERVIEW_URL, BASIC_URL));
        LocationOverviewPage locationOverviewPage = new LocationOverviewPage(driver);
        locationOverviewPage.selectTab("Cooling Zones");
        locationOverviewPage.clickIconByLabel("Create Cooling Zone");
        CreateCoolingZoneWizardPage coolingZoneWizard = new CreateCoolingZoneWizardPage(driver);
        coolingZoneWizard.setName(COOLING_ZONE_NAME);
        coolingZoneWizard.clickProceed();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        checkPopup();
    }

    @Test(priority = 17)
    @Description("Create Cooling Unit")
    public void createCoolingUnit() {
        LocationOverviewPage locationOverviewPage = new LocationOverviewPage(driver);
        locationOverviewPage.selectTab("Devices");
        locationOverviewPage.clickButton("Create Device");
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

    @Test(priority = 18)
    @Description("Assign Cooling Unit to Cooling Zone")
    public void assignCoolingUnitToCoolingZone() {
        LocationOverviewPage locationOverviewPage = new LocationOverviewPage(driver);
        locationOverviewPage.selectTab("Devices");
        locationOverviewPage.filterDevicesObject("Name", COOLING_UNIT_NAME);
        locationOverviewPage.clickIconByLabel("Cooling Zone Editor");
        CreateCoolingZoneWizardPage coolingZoneWizard = new CreateCoolingZoneWizardPage(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        coolingZoneWizard.setName(COOLING_ZONE_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        coolingZoneWizard.clickUpdate();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 19)
    @Description("Update physical Device")
    public void updateDevice() {
        LocationOverviewPage locationOverviewPage = new LocationOverviewPage(driver);
        locationOverviewPage.selectTab("Devices");
        locationOverviewPage.filterDevicesObject("Name", PHYSICAL_DEVICE_NAME);
        locationOverviewPage.clickIconByLabel("Update Device");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        DeviceWizardPage deviceWizardPage = new DeviceWizardPage(driver);
        deviceWizardPage.setHeatEmission(DEVICE_HEAT_EMISSION);
        deviceWizardPage.setPowerConsumption(DEVICE_POWER_CONSUMPTION);
        deviceWizardPage.update();
        checkPopup();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 20)
    @Description("Assign Device to Cooling Zone")
    public void assignDeviceToCoolingZone() {
        LocationOverviewPage locationOverviewPage = new LocationOverviewPage(driver);
        locationOverviewPage.selectTab("Devices");
        locationOverviewPage.filterDevicesObject("Name", PHYSICAL_DEVICE_NAME);
        locationOverviewPage.clickIconByLabel("Cooling Zone Editor");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        CreateCoolingZoneWizardPage coolingZoneWizardPage = new CreateCoolingZoneWizardPage(driver);
        coolingZoneWizardPage.selectNameFromList(COOLING_ZONE_NAME);
        coolingZoneWizardPage.clickUpdate();
        checkPopup();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        locationOverviewPage.selectTab("Cooling Zones");
        OldTable coolingTable = locationOverviewPage.getCoolingZonesTabTable();
        int rowNumber = coolingTable.getRowNumber(COOLING_ZONE_NAME, "Name");
        String rowValue = coolingTable.getValueCell(rowNumber, "Cooling Load [kW]");
        Assertions.assertThat(Integer.parseInt(COOLING_ZONE_COOLING_LOAD)).isNotEqualTo(Integer.parseInt(rowValue));
    }

    @Test(priority = 21)
    @Description("Create Physical Device2")
    public void createPhysicalDevice2() {
        LocationOverviewPage locationOverviewPage = new LocationOverviewPage(driver);
        locationOverviewPage.selectTab("Devices");
        locationOverviewPage.clickButton("Create Device");
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

    @Test(priority = 22)
    @Description("Assign Physcial Device2 to Cooling Zone")
    public void assignDevice2ToCoolingZone() {
        LocationOverviewPage locationOverviewPage = new LocationOverviewPage(driver);
        locationOverviewPage.selectTab("Devices");
        locationOverviewPage.filterDevicesObject("Name", PHYSICAL_DEVICE_NAME2);
        locationOverviewPage.clickIconByLabel("Cooling Zone Editor");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        CreateCoolingZoneWizardPage coolingZoneWizardPage = new CreateCoolingZoneWizardPage(driver);
        coolingZoneWizardPage.selectNameFromList(COOLING_ZONE_NAME);
        coolingZoneWizardPage.clickUpdate();
        checkPopup();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 23)
    @Description("Update Cooling Unit")
    public void updateCoolingUnit() {
        LocationOverviewPage locationOverviewPage = new LocationOverviewPage(driver);
        locationOverviewPage.selectTab("Devices");
        locationOverviewPage.filterDevicesObject("Name", COOLING_UNIT_NAME);
        locationOverviewPage.clickIconByLabel("Update Device");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        DeviceWizardPage deviceWizardPage = new DeviceWizardPage(driver);
        deviceWizardPage.setCoolingCapacity(COOLING_CAPACITY2);
        deviceWizardPage.update();
        checkPopup();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        locationOverviewPage.selectTab("Cooling Zones");
        OldTable coolingTable = locationOverviewPage.getCoolingZonesTabTable();
        int rowNumber = coolingTable.getRowNumber(COOLING_ZONE_NAME, "Name");
        String rowValue = coolingTable.getValueCell(rowNumber, "Cooling Load Ratio [%]");
        Assertions.assertThat(Integer.parseInt(COOLING_ZONE_LOAD_RATIO)).isNotEqualTo(Integer.parseInt(rowValue));
    }

    @Test(priority = 24)
    @Description("Create Power Device")
    public void createPowerDevice() {
        LocationOverviewPage locationOverviewPage = new LocationOverviewPage(driver);
        locationOverviewPage.selectTab("Devices");
        locationOverviewPage.clickButton("Create Device");
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
        OldTable powerTable = locationOverviewPage.getCoolingZonesTabTable();
        int rowNumber = powerTable.getRowNumber(SUBLOCATION_NAME, "Name");
        String rowValue = powerTable.getValueCell(rowNumber, "Power Capacity [kW]");
        Assertions.assertThat(Integer.parseInt(LOCATION_POWER_CAPACITY)).isNotEqualTo(Integer.parseInt(rowValue));
        LOCATION_POWER_CAPACITY = rowValue;
        rowValue = powerTable.getValueCell(rowNumber, "Power Load [kW]");
        Assertions.assertThat(Integer.parseInt(rowValue)).isNotEqualTo(0);
        rowValue = powerTable.getValueCell(rowNumber, "Power Load Ratio [%]");
        Assertions.assertThat(Integer.parseInt(rowValue)).isNotEqualTo(0);
    }

    @Test(priority = 25)
    @Description("Create Power Supply Unit")
    public void createPowerSupplyUnit() {
        LocationOverviewPage locationOverviewPage = new LocationOverviewPage(driver);
        locationOverviewPage.selectTab("Devices");
        locationOverviewPage.clickButton("Create Device");
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

    @Test(priority = 26)
    @Description("Delete Power Supply Unit")
    public void deletePowerSupplyUnit() {
        LocationOverviewPage locationOverviewPage = new LocationOverviewPage(driver);
        locationOverviewPage.selectTab("Devices");
        locationOverviewPage.filterDevicesObject("Name", POWER_SUPPLY_UNIT_NAME);
        locationOverviewPage.clickRemoveDevice();
        checkPopup();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 27)
    @Description("Delete Power Device")
    public void deletePowerDevice() {
        LocationOverviewPage locationOverviewPage = new LocationOverviewPage(driver);
        locationOverviewPage.selectTab("Devices");
        locationOverviewPage.filterDevicesObject("Name", POWER_DEVICE_NAME);
        locationOverviewPage.clickRemoveDevice();
        checkPopup();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 28)
    @Description("Delete Physical Device2")
    public void deletePhysicalDevice2() {
        LocationOverviewPage locationOverviewPage = new LocationOverviewPage(driver);
        locationOverviewPage.selectTab("Devices");
        locationOverviewPage.filterDevicesObject("Name", PHYSICAL_DEVICE_NAME2);
        locationOverviewPage.clickRemoveDevice();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        ConfirmationBoxInterface prompt = ConfirmationBox.create(driver, webDriverWait);
        prompt.clickButtonByLabel("Yes");
        checkPopup();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 29)
    @Description("Delete Physical Device")
    public void deletePhysicalDevice() {
        LocationOverviewPage locationOverviewPage = new LocationOverviewPage(driver);
        locationOverviewPage.selectTab("Devices");
        locationOverviewPage.filterDevicesObject("Name", PHYSICAL_DEVICE_NAME);
        locationOverviewPage.clickRemoveDevice();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        ConfirmationBoxInterface prompt = ConfirmationBox.create(driver, webDriverWait);
        prompt.clickButtonByLabel("Yes");
        checkPopup();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 30)
    @Description("Delete Cooling Unit")
    public void deleteCoolingUnit() {
        LocationOverviewPage locationOverviewPage = new LocationOverviewPage(driver);
        locationOverviewPage.selectTab("Devices");
        locationOverviewPage.filterDevicesObject("Name", COOLING_UNIT_NAME);
        locationOverviewPage.clickRemoveDevice();
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
        locationOverviewPage.filterCoolingObject("Name", COOLING_ZONE_NAME);
        locationOverviewPage.clickDeleteCoolingZone();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        ConfirmationBoxInterface prompt = ConfirmationBox.create(driver, webDriverWait);
        prompt.clickButtonByLabel("Delete Cooling Zone");
        checkPopup();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 32)
    @Description("Delete Rack")
    public void deleteRack() {
        LocationOverviewPage locationOverviewPage = new LocationOverviewPage(driver);
        locationOverviewPage.selectTab("Locations");
        locationOverviewPage.filterLocationsObject("Name", MOUNTING_EDITOR_NAME);
        locationOverviewPage.clickRemoveLocationIcon();
        checkPopup();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 33)
    @Description("Delete Room")
    public void deleteRoom() {
        LocationOverviewPage locationOverviewPage = new LocationOverviewPage(driver);
        locationOverviewPage.selectTab("Locations");
        locationOverviewPage.filterLocationsObject("Name", SUBLOCATION_NAME);
        locationOverviewPage.clickRemoveLocationIcon();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        ConfirmationBoxInterface prompt = ConfirmationBox.create(driver, webDriverWait);
        prompt.clickButtonByLabel("Delete");
        checkPopup();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 33)
    @Description("Delete Location")
    public void deleteLocation() {
        DeviceOverviewPage deviceOverviewPage = new DeviceOverviewPage(driver);
        deviceOverviewPage.useContextAction("Remove Location");
        Wizard removalWizard = Wizard.createByComponentId(driver, webDriverWait, "Popup");
        removalWizard.clickButtonByLabel("Delete");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        ConfirmationBoxInterface prompt = ConfirmationBox.create(driver, webDriverWait);
        prompt.clickButtonByLabel("Delete");
    }
}
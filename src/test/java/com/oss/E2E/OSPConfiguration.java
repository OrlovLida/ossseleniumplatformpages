package com.oss.E2E;

import com.oss.BaseTestCase;
import com.oss.framework.alerts.SystemMessageContainer;
import com.oss.framework.alerts.SystemMessageInterface;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.gisView.CreateDuctWizardPage;
import com.oss.pages.gisView.DuctCopyWizardPage;
import com.oss.pages.gisView.GisViewPage;
import com.oss.pages.gisView.RoutingWizardPage;
import com.oss.pages.physical.*;
import com.oss.pages.radio.CableWizardPage;
import io.qameta.allure.Description;
import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

public class OSPConfiguration extends BaseTestCase {
    private GisViewPage gisViewPage;
    private String BUILDING_A_NAME = "bsBuilding A";
    private String BUILDING_C_NAME = "bsBuilding C";
    private String GEOGRAPHICAL_ADDRESS_BUILDING = " Jasna 1, Gliwice";
    private String GEOGRAPHICAL_ADDRESS_MANHOLE = " Jasna 34848, Gliwice";
    private String MANHOLE_MODEL = "SK-1";
    private String MANHOLE_A_NAME = "Manhole A";
    private String DUCT_MODEL = "HDPE 100x4,3";
    private String DUCT_MODEL2 = "HDPE 320x2,9";
    private String DUCT_TYPE_PRIMARY = "Primary Duct";
    private String DUCT_TYPE_SUBDUCT = "Subduct";
    private String ODF_MODEL = "Optomer PS-5/12";
    private String ODF_A_NAME = "ODF A";
    private String ODF_C_NAME = "ODF C";
    private String CARD_MODEL = "Optomer KS-24";
    private String CARD_NAME = "KS-24";
    private String PLUGGABLE_MODULE_MODEL = "Generic SC/PC Front/Back";
    private String CABLE_MODEL = "1/4\"\" SCF 14-50J 4.0m";
    private String CABLE_NAME = "SeleniumCable";
    private String CABLE_NAME2 = "SeleniumCable2";

    private LocationOverviewPage locationOverviewPage;
    private DeviceOverviewPage deviceOverviewPage;
    private DuctCopyWizardPage ductCopyWizardPage;

    @BeforeClass
    public void openGisViewByLink() {
        gisViewPage = GisViewPage.goToGisViewPage(driver, BASIC_URL);
    }

    @Test(priority = 1, enabled = false)
    @Description("Create two buildings")
    public void createBuilding() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.searchFirstResult("gliwice");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.clickOnMapByCoordinates(2, 4);
        gisViewPage.useContextAction("Draw Single Location", "Building");
        gisViewPage.clickOnMapByCoordinates(4, 2);

        LocationWizardPage locationWizardPage = new LocationWizardPage(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        locationWizardPage.setLocationName(BUILDING_A_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        locationWizardPage.clickNext();

        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        locationWizardPage.setGeographicalAddress(GEOGRAPHICAL_ADDRESS_BUILDING);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        locationWizardPage.clickNext();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        locationWizardPage.setNumberOfLocations("2");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        locationWizardPage.accept();
        checkPopup();
    }

    @Test(priority = 2, enabled = false)
    @Description("Edit name and address of the building")
    public void modifyBuilding() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.chooseObjectFromList("[Buildings] Building A_002", 4, 2);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.useContextAction("Edit", "Edit Location");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        LocationWizardPage locationWizardPage = new LocationWizardPage(driver);
        locationWizardPage.setLocationName("Building C");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        locationWizardPage.clickNext();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        locationWizardPage.setStreetNumber("10");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        locationWizardPage.accept();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.useContextActionById("Move Location");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.dragAndDropObject(4, 2, 8, 4);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.useContextActionById("Save Edited Shape");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        checkPopup();
    }

    @Test(priority = 3, enabled = false)
    @Description("Create manhole")
    public void createManhole() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.searchFirstResult("gliwice");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        gisViewPage.clickOnMapByCoordinates(2, 4);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.useContextAction("Draw Single Location", "Manhole");
        gisViewPage.clickOnMapByCoordinates(8, 2);
        LocationWizardPage locationWizardPage = new LocationWizardPage(driver);
        locationWizardPage.setModel(MANHOLE_MODEL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        locationWizardPage.setLocationName(MANHOLE_A_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        locationWizardPage.clickNext();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        locationWizardPage.setGeographicalAddress(GEOGRAPHICAL_ADDRESS_MANHOLE);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        locationWizardPage.clickNext();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        locationWizardPage.accept();
        checkPopup();
    }

    @Test(priority = 4, enabled = false)
    @Description("Create duct between building A na manhole")
    public void createDuctFromBuildingAToManhole() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.clickOnMapByCoordinates(2, 4);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.useContextActionById("Draw New Duct");
        gisViewPage.clickOnMapByCoordinates(4, 2);
        gisViewPage.clickOnMapByCoordinates(8, 2);
        gisViewPage.doubleClickOnMapByCoordinates(8, 2);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        CreateDuctWizardPage createDuctWizardPage = new CreateDuctWizardPage(driver);
        createDuctWizardPage.setModel(DUCT_MODEL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        createDuctWizardPage.setType(DUCT_TYPE_PRIMARY);
        createDuctWizardPage.create();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        checkPopup();
    }

    @Test(priority = 5, enabled = false)
    @Description("Create duct between building C and manhole")
    public void createDuctFromBuildingCToManhole() {
        gisViewPage.clickOnMapByCoordinates(2, 4);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.useContextActionById("Draw New Duct");
        gisViewPage.clickOnMapByCoordinates(8, 4);
        gisViewPage.clickOnMapByCoordinates(8, 2);
        gisViewPage.doubleClickOnMapByCoordinates(8, 2);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        CreateDuctWizardPage createDuctWizardPage = new CreateDuctWizardPage(driver);
        createDuctWizardPage.setModel(DUCT_MODEL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        createDuctWizardPage.setType(DUCT_TYPE_PRIMARY);
        createDuctWizardPage.create();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        checkPopup();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.enableLayerInTree("Duct");
    }

    @Test(priority = 6, enabled = false)
    @Description("Create copy of ducts")
    public void createCopyOfDucts() {
        gisViewPage.clickOnMapByCoordinates(2, 4);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.clickOnMapByCoordinates(6, 2);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.useContextAction("Edit", "Copy Duct");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        DuctCopyWizardPage ductCopyWizardPage = new DuctCopyWizardPage(driver);
        ductCopyWizardPage.setNumberOfCopies("1");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        ductCopyWizardPage.create();
        checkPopup();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.useContextActionById("Refresh");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);


        gisViewPage.clickOnMapByCoordinates(8, 3);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.useContextAction("Edit", "Copy Duct");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        ductCopyWizardPage.setNumberOfCopies("1");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        ductCopyWizardPage.create();
        checkPopup();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.useContextActionById("Refresh");
    }

    @Test(priority = 7, enabled = false)
    @Description("Create duct between Building A and C")
    public void createDuctFromBuildingAToBuildingC() {
        gisViewPage.clickOnMapByCoordinates(2, 4);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.useContextActionById("Draw New Duct");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.clickOnMapByCoordinates(4, 2);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.clickOnMapByCoordinates(8, 4);
        gisViewPage.clickOnMapByCoordinates(8, 4);
        gisViewPage.doubleClickOnMapByCoordinates(8, 4);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        CreateDuctWizardPage createDuctWizardPage = new CreateDuctWizardPage(driver);
        createDuctWizardPage.setModel(DUCT_MODEL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        createDuctWizardPage.setType(DUCT_TYPE_SUBDUCT);
        createDuctWizardPage.create();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        checkPopup();
    }

    @Test(priority = 8)
    @Description("Create routing through manhole")
    public void createSubductRouting() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.searchFirstResult("gliwice");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.clickOnMapByCoordinates(6, 3);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.useContextAction("Edit", "Routing");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        RoutingWizardPage routingWizardPage = new RoutingWizardPage(driver);
        routingWizardPage.InsertPhysicalLocation(MANHOLE_A_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);


        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        routingWizardPage.selectDuctForSegment(0);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        routingWizardPage.selectDuctForSegment(3);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        routingWizardPage.clickOk();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 9)
    @Description("Check duct utilization %")
    public void checkDuctUtilizationValue() {
        checkDuctUtilization("[Subduct] subduct", 8, 3, "119.71");
    }

    @Test(priority = 10)
    public void changePrimaryDuctModel() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.chooseObjectFromList("[gisLayer_PrimaryDuct] Identifier: SingleDuct 15187", 8, 3);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.useContextAction("Edit", "Edit Duct");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        CreateDuctWizardPage createDuctWizardPage = new CreateDuctWizardPage(driver);
        createDuctWizardPage.setModel(DUCT_MODEL2);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        createDuctWizardPage.create();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 11)
    @Description("Check duct utilization %")
    public void checkDuctUtilizationAfterModelChange() {
        checkDuctUtilization("[Subduct] subduct", 8, 3, "10.13");
    }

    @Test(priority = 12, enabled = false)
    @Description("Create ODF in location")
    public void createODF() {
        // temporary
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.searchFirstResult("Gliwice-BU458");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.useContextAction("Show on", "Location Overview");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        locationOverviewPage = new LocationOverviewPage(driver);
        locationOverviewPage.clickButton("Create Device");

        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        DeviceWizardPage deviceWizardPage = new DeviceWizardPage(driver);
        deviceWizardPage.setEquipmentType("ODF");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.setModel(ODF_MODEL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.setName(ODF_A_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.create();
    }

    @Test(priority = 13, enabled = false)
    @Description("Create pluggable module in device")
    public void createPluggableModule() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        locationOverviewPage.selectTab("Devices");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        locationOverviewPage.clickButtonFromDeviceTabByLabel("Refresh");
        locationOverviewPage.selectDeviceFromTableByAttributeValueWithLabel("Name", ODF_A_NAME);
        locationOverviewPage.clickButtonFromDeviceTabByLabel("Device Overview");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        deviceOverviewPage = new DeviceOverviewPage(driver);
        deviceOverviewPage.selectTreeRow("2");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceOverviewPage.useContextAction("Create Pluggable Module");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        CreatePluggableModuleWizardPage createPluggableModule = new CreatePluggableModuleWizardPage(driver);
        createPluggableModule.setModel(PLUGGABLE_MODULE_MODEL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        createPluggableModule.accept();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        checkPopup();
    }

    @Test(priority = 14, enabled = false)
    @Description("Create card in device")
    public void createCard() {
        deviceOverviewPage.selectTreeRow(ODF_A_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceOverviewPage.useContextAction("Create Card");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        CardCreateWizardPage cardCreateWizardPage = new CardCreateWizardPage(driver);
        cardCreateWizardPage.setModel(CARD_MODEL);
        cardCreateWizardPage.setSlots("1");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cardCreateWizardPage.clickAccept();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        checkPopup();
    }

    @Test(priority = 15, enabled = false)
    @Description("Create copy of device")
    public void createDeviceCopy() {
        deviceOverviewPage.selectTreeRow(ODF_A_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceOverviewPage.useContextAction("Create Copy");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        CreateCopyOfDeviceWizardPage createCopyOfDeviceWizardPage = new CreateCopyOfDeviceWizardPage(driver);
        createCopyOfDeviceWizardPage.setName(ODF_C_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        createCopyOfDeviceWizardPage.setLocation(BUILDING_C_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        createCopyOfDeviceWizardPage.create();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        checkPopup();
    }

    @Test(priority = 16, enabled = false)
    @Description("Create Cable Between Two Buildings")
    public void createCableFromBuildingAToBuildingC() {
        gisViewPage.searchFirstResult("gliwice");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.clickOnMapByCoordinates(2, 4);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.useContextActionById("Draw New Cable");
        gisViewPage.clickOnMapByCoordinates(5, 2);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.clickOnMapByCoordinates(8, 4);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.clickOnMapByCoordinates(8, 4);
        CableWizardPage cableWizardPage = new CableWizardPage(driver);
        cableWizardPage.setModel(CABLE_MODEL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cableWizardPage.setName(CABLE_NAME);
        cableWizardPage.accept();
        checkPopup();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 17, enabled = true)
    @Description("Route Cable over Primary Ducts")
    public void routeCableOverPrimaryDucts() {
        gisViewPage.searchFirstResult(CABLE_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.useContextAction("Edit", "Routing");
        CableRoutingViewPage cableRoutingViewPage = new CableRoutingViewPage(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cableRoutingViewPage.clickInsertLocation();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cableRoutingViewPage.insertLocationToRouting(MANHOLE_A_NAME);
        cableRoutingViewPage.clickAddLocationToRouting();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cableRoutingViewPage.clickSave();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cableRoutingViewPage.clickClose();
        checkPopup();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 18, enabled = false)
    @Description("Create second Cable between Building A and Building C")
    public void createCable2FromBuildingAToBuildingC() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.searchFirstResult("gliwice");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.clickOnMapByCoordinates(2, 4);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.useContextActionById("Draw New Cable");
        gisViewPage.clickOnMapByCoordinates(5, 2);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.clickOnMapByCoordinates(8, 4);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.clickOnMapByCoordinates(8, 4);
        CableWizardPage cableWizardPage = new CableWizardPage(driver);
        cableWizardPage.setModel(CABLE_MODEL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cableWizardPage.setName(CABLE_NAME2);
        cableWizardPage.accept();
        checkPopup();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 19, enabled = true)
    @Description("Route second Cable over Subduct")
    public void routeCable2OverSubduct() {
        gisViewPage.searchFirstResult(CABLE_NAME2);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.useContextAction("Edit", "Routing");
        CableRoutingViewPage cableRoutingViewPage = new CableRoutingViewPage(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cableRoutingViewPage.clickInsertLocation();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cableRoutingViewPage.insertLocationToRouting(MANHOLE_A_NAME);
        cableRoutingViewPage.clickAddLocationToRouting();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cableRoutingViewPage.clickSave();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cableRoutingViewPage.clickClose();
        checkPopup();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 20)
    @Description("Create ODF devices on Building A")
    public void createODFDeviceOnBuildingA() {
        gisViewPage.searchFirstResult(BUILDING_A_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.useContextAction("Create", "Create Device");
        DeviceWizardPage deviceWizardPage = new DeviceWizardPage(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.setModel(ODF_MODEL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.setName(ODF_A_NAME);
        deviceWizardPage.create();
        checkPopup();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 21)
    @Description("Create ODF devices on Building C")
    public void createODFDeviceOnBuildingC() {
        gisViewPage.searchFirstResult(BUILDING_C_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.useContextAction("Create", "Create Device");
        DeviceWizardPage deviceWizardPage = new DeviceWizardPage(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.setModel(ODF_MODEL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.setName(ODF_C_NAME);
        deviceWizardPage.create();
        checkPopup();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 22)
    @Description("Add Card to ODF on Building A")
    public void addCardToODFOnBuildingA() {
        gisViewPage.searchFirstResult(ODF_A_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.useContextAction("Show on", "Device Overview");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        DeviceOverviewPage deviceOverviewPage = new DeviceOverviewPage(driver);
        deviceOverviewPage.selectTreeRow(ODF_A_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceOverviewPage.useContextAction("Create Card");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        CardCreateWizardPage cardCreateWizardPage = new CardCreateWizardPage(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cardCreateWizardPage.setModel(CARD_MODEL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cardCreateWizardPage.setSlots("1");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cardCreateWizardPage.clickAccept();
        checkPopup();
    }

    @Test(priority = 23)
    @Description("Add Pluggable Module to ODF on Building A")
    public void addPluggableModuleToODFOnBuildingA() {
        DeviceOverviewPage deviceOverviewPage = new DeviceOverviewPage(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceOverviewPage.useContextAction("Create Pluggable Module");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        CreatePluggableModuleWizardPage createPluggableModuleWizardPage = new CreatePluggableModuleWizardPage(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        createPluggableModuleWizardPage.setModel(PLUGGABLE_MODULE_MODEL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        createPluggableModuleWizardPage.setPort("1");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        createPluggableModuleWizardPage.accept();
        checkPopup();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 24)
    @Description("Add Card to ODF on Building C")
    public void addCardToODFOnBuildingC() {
        gisViewPage = GisViewPage.goToGisViewPage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.searchFirstResult(ODF_C_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.useContextAction("Show on", "Device Overview");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        DeviceOverviewPage deviceOverviewPage = new DeviceOverviewPage(driver);
        deviceOverviewPage.selectTreeRow(ODF_C_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceOverviewPage.useContextAction("Create Card");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        CardCreateWizardPage cardCreateWizardPage = new CardCreateWizardPage(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cardCreateWizardPage.setModel(CARD_MODEL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cardCreateWizardPage.setSlots("1");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cardCreateWizardPage.clickAccept();
        checkPopup();
    }

    @Test(priority = 25)
    @Description("Add Pluggable Module to ODF on Building C")
    public void addPluggableModuleToODFOnBuildingC() {
        DeviceOverviewPage deviceOverviewPage = new DeviceOverviewPage(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceOverviewPage.useContextAction("Create Pluggable Module");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        CreatePluggableModuleWizardPage createPluggableModuleWizardPage = new CreatePluggableModuleWizardPage(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        createPluggableModuleWizardPage.setModel(PLUGGABLE_MODULE_MODEL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        createPluggableModuleWizardPage.setPort("1");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        createPluggableModuleWizardPage.accept();
        checkPopup();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 26)
    @Description("Add Termination to Cable")
    public void addTerminationToCable() {
        gisViewPage = GisViewPage.goToGisViewPage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.searchFirstResult(CABLE_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.useContextAction("Edit", "Media Termination");
        MediaTerminationPage mediaTerminationPage = new MediaTerminationPage(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        mediaTerminationPage.setFirstLocation();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        mediaTerminationPage.setDeviceStart(ODF_A_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        mediaTerminationPage.setPortStart("1");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        mediaTerminationPage.setDeviceEnd(ODF_C_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        mediaTerminationPage.setPortEnd("2");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        mediaTerminationPage.clickUpdateCable();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        mediaTerminationPage.clickFinish();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 27)
    @Description("Create Patchcord on Building A")
    public void createPatchcordOnBuildingA() {
        gisViewPage = GisViewPage.goToGisViewPage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.searchFirstResult(BUILDING_A_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.useContextAction("Show on", "Patchcord wizard");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        PatchcordWizardPage patchcordWizardPage = new PatchcordWizardPage(driver);
        patchcordWizardPage.getPatchcordConnectorsTableStart().selectRowByAttributeValueWithLabel("Port", "1");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        patchcordWizardPage.setDeviceTerminationB(ODF_A_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        patchcordWizardPage.getPatchcordConnectorsTableEnd().selectRowByAttributeValueWithLabel("Port", "2");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        patchcordWizardPage.clickCreateSingleMediumPatchcord();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        patchcordWizardPage.clickProceed();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        patchcordWizardPage.clickUpdatePatchcords();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        patchcordWizardPage.clickFinish();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 28)
    @Description("Delete Patchcord on building A")
    public void deletePatchcordOnBuildingA() {
        PatchcordWizardPage patchcordWizardPage = new PatchcordWizardPage(driver);
        patchcordWizardPage.selectFirstPatchcord();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        patchcordWizardPage.clickRemovePatchcord();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        patchcordWizardPage.clickUpdatePatchcords();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        patchcordWizardPage.clickFinish();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        DelayUtils.sleep(200);
        patchcordWizardPage.clickClose();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 29)
    @Description("Delete Media Termination from Cable")
    public void deleteMediaTerminationFromCable() {
        gisViewPage.searchFirstResult(CABLE_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.useContextAction("Edit", "Media Termination");
        MediaTerminationPage mediaTerminationPage = new MediaTerminationPage(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        mediaTerminationPage.setFirstLocation();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        mediaTerminationPage.getMediaTable().callActionByLabel("Detach start");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        mediaTerminationPage.getMediaTable().callActionByLabel("Detach end");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        mediaTerminationPage.clickUpdateCable();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        mediaTerminationPage.clickFinish();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 30)
    @Description("Delete Pluggable Module from ODF device on Building C")
    public void deletePluggableModuleFromODFOnBuildingC() {
        gisViewPage.searchFirstResult(ODF_C_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.useContextAction("Show on", "Device Overview");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        DeviceOverviewPage deviceOverviewPage = new DeviceOverviewPage(driver);
        deviceOverviewPage.selectTreeRow(PLUGGABLE_MODULE_MODEL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceOverviewPage.useContextAction("Delete Element");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceOverviewPage.clickYes();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        checkPopup();
    }

    @Test(priority = 31)
    @Description("Delete Card from ODF device on Building C")
    public void deleteCardFromODFOnBuildingC() {
        DeviceOverviewPage deviceOverviewPage = new DeviceOverviewPage(driver);
        deviceOverviewPage.selectTreeRow(CARD_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceOverviewPage.useContextAction("Delete Element");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceOverviewPage.clickYes();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        checkPopup();
    }

    @Test(priority = 32)
    @Description("Delete ODF device on Building C")
    public void deleteODFOnBuildingC() {
        DeviceOverviewPage deviceOverviewPage = new DeviceOverviewPage(driver);
        deviceOverviewPage.selectTreeRow(ODF_C_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceOverviewPage.useContextAction("Delete Element");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceOverviewPage.clickYes();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        checkPopup();
    }

    @Test(priority = 33)
    @Description("Delete Pluggable Module from ODF device on Building A")
    public void deletePluggableModuleFromODFOnBuildingA() {
        gisViewPage = GisViewPage.goToGisViewPage(driver, BASIC_URL);
        gisViewPage.searchFirstResult(ODF_A_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.useContextAction("Show on", "Device Overview");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        DeviceOverviewPage deviceOverviewPage = new DeviceOverviewPage(driver);
        deviceOverviewPage.selectTreeRow(PLUGGABLE_MODULE_MODEL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceOverviewPage.useContextAction("Delete Element");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceOverviewPage.clickYes();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        checkPopup();
    }

    @Test(priority = 34)
    @Description("Delete Card from ODF device on Building A")
    public void deleteCardFromODFOnBuildingA() {
        DeviceOverviewPage deviceOverviewPage = new DeviceOverviewPage(driver);
        deviceOverviewPage.selectTreeRow(CARD_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceOverviewPage.useContextAction("Delete Element");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceOverviewPage.clickYes();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        checkPopup();
    }

    @Test(priority = 35)
    @Description("Delete ODF device on Building A")
    public void deleteODFOnBuildingA() {
        DeviceOverviewPage deviceOverviewPage = new DeviceOverviewPage(driver);
        deviceOverviewPage.selectTreeRow(ODF_A_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceOverviewPage.useContextAction("Delete Element");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceOverviewPage.clickYes();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        checkPopup();
    }

    @Test(priority = 36)
    @Description("Delete Cable Routing")
    public void deleteCableRouting() {
        gisViewPage = GisViewPage.goToGisViewPage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.searchFirstResult(CABLE_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.useContextAction("Edit", "Routing");
        CableRoutingViewPage cableRoutingViewPage = new CableRoutingViewPage(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cableRoutingViewPage.clickRemoveLocation(MANHOLE_A_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cableRoutingViewPage.clickSave();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cableRoutingViewPage.clickClose();
        checkPopup();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 37)
    @Description("Delete Cable2 Routing")
    public void deleteCable2Routing() {
        gisViewPage.searchFirstResult(CABLE_NAME2);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.useContextAction("Edit", "Routing");
        CableRoutingViewPage cableRoutingViewPage = new CableRoutingViewPage(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cableRoutingViewPage.clickRemoveLocation(MANHOLE_A_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cableRoutingViewPage.clickSave();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cableRoutingViewPage.clickClose();
        checkPopup();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 38)
    @Description("Delete Cable2")
    public void deleteCable2() {
        gisViewPage.searchFirstResult(CABLE_NAME2);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.useContextActionById("Remove Connections");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.clickDelete();
        checkPopup();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 39)
    @Description("Delete Cable")
    public void deleteCable() {
        gisViewPage.searchFirstResult(CABLE_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.useContextActionById("Remove Connections");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.clickDelete();
        checkPopup();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    public void checkPopup() {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assertions.assertThat(messages).hasSize(1);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getMessageType())
                .isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
    }

    public void checkDuctUtilization(String ductName, int x, int y, String expectedUtilization) {
        gisViewPage.searchFirstResult("gliwice");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.useContextActionById("Refresh");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.chooseObjectFromList(ductName, x, y);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.expandDockedPanel("bottom");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.selectTab("6");//Routing
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        String utilization = gisViewPage.getCellValue(0, "Utilization [%]");
        Assertions.assertThat(utilization).isEqualTo(expectedUtilization);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.hideDockedPanel("bottom");
    }
}

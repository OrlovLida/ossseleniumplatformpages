package com.oss.E2E;

import com.oss.BaseTestCase;
import com.oss.framework.alerts.SystemMessageContainer;
import com.oss.framework.alerts.SystemMessageInterface;
import com.oss.framework.prompts.ConfirmationBox;
import com.oss.framework.prompts.ConfirmationBoxInterface;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.gisView.CreateDuctWizardPage;
import com.oss.pages.gisView.DuctCopyWizardPage;
import com.oss.pages.gisView.GisViewPage;
import com.oss.pages.physical.*;
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
        gisViewPage.clickOnMapByCoordinates(5, 2);

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
        gisViewPage.chooseObjectFromList("[Buildings] Building A_002", 5, 2);
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
        gisViewPage.dragAndDropObject(5, 2, 8, 4);
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
        gisViewPage.clickOnMapByCoordinates(5, 2);
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

    @Test(priority = 6)
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

    @Test(priority = 7)
    @Description("Create duct between Building A and C")
    public void createDuctFromBuildingAToBuildingC() {
        gisViewPage.clickOnMapByCoordinates(2, 4);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.useContextActionById("Draw New Duct");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.clickOnMapByCoordinates(5, 2);
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

    @Test(priority = 8, enabled = false)
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

    @Test(priority = 9, enabled = false)
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

    @Test(priority = 10, enabled = false)
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

    @Test(priority = 11, enabled = false)
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

    @Test(priority = 10, enabled = false)
    @Description("Create Cable Between Two Buildings")
    public void createCableFromBuildingAToBuildingC() {
        /* =============== Tworzenie Building A ========== */
        /*DelayUtils.waitForPageToLoad(driver, webDriverWait);
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
        *//* =============================================== *//*
        *//* =============== Tworzenie Building C ========== *//*
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.searchFirstResult("gliwice");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.clickOnMapByCoordinates(2, 4);
        gisViewPage.useContextAction("Draw Single Location", "Building");
        gisViewPage.clickOnMapByCoordinates(4, 8);

        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        locationWizardPage.setLocationName(BUILDING_C_NAME);
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
        checkPopup();*/
        /* =============================================== */

        gisViewPage.searchFirstResult("gliwice");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.clickOnMapByCoordinates(2, 4);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.useContextActionById("Draw New Cable");
        gisViewPage.clickOnMapByCoordinates(4, 2);  // to sa kordy building A
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.clickOnMapByCoordinates(4, 8);  // to sa kordy building C
        DelayUtils.sleep(200);
        gisViewPage.clickOnMapByCoordinates(4, 8);
        CreateCableWizardPage createCableWizardPage = new CreateCableWizardPage(driver);
        createCableWizardPage.setCableModel(CABLE_MODEL);
        DelayUtils.sleep(5000);
        //createCableWizardPage.clickAccept();
        /*gisViewPage.clickOnMapByCoordinates(2, 4);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        DelayUtils.sleep(2000);
        gisViewPage.clickOnMapByCoordinates(3,4);
        DelayUtils.sleep(2000);
        gisViewPage.useContextActionById("Draw New Duct");
        gisViewPage.clickOnMapByCoordinates(4, 2);
        gisViewPage.clickOnMapByCoordinates(5, 3);
        gisViewPage.doubleClickOnMapByCoordinates(5, 3);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        CreateDuctWizardPage createDuctWizardPage = new CreateDuctWizardPage(driver);
        createDuctWizardPage.setModel(DUCT_MODEL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        createDuctWizardPage.setType(DUCT_TYPE_PRIMARY);
        createDuctWizardPage.create();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        checkPopup();*/
    }

    /*@Test(priority = 11, enabled = false)
    @Description("Route Cable over Primary Ducts")
    public void routeCableOverPrimaryDucts() {

    }

    @Test(priority = 12, enabled = false)
    @Description("Create second Cable between Building A and Building C")
    public void createCable2FromBuildingAToBuildingC() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.searchFirstResult("gliwice");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.clickOnMapByCoordinates(2, 4);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.useContextActionById("Draw New Cable");
        gisViewPage.clickOnMapByCoordinates(4, 2);  // to sa kordy building A
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.clickOnMapByCoordinates(4, 8);  // to sa kordy building C
        DelayUtils.sleep(200);
        gisViewPage.clickOnMapByCoordinates(4, 8);
        CreateCableWizardPage createCableWizardPage = new CreateCableWizardPage(driver);
        createCableWizardPage.setCableModel(CABLE_MODEL);
        DelayUtils.sleep(5000);
    }

    @Test(priority = 13, enabled = false)
    @Description("Route second Cable over Subduct")
    public void routeCable2OverSubduct() {

    }*/

    /*@Test(priority = 14)
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

    @Test(priority = 15)
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

    @Test(priority = 16)
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

    @Test(priority = 17)
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

    @Test(priority = 18)
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

    @Test(priority = 19)
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

    @Test(priority = 20)
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
        //mediaTerminationPage.setCardStart(CARD_NAME);
        //DelayUtils.waitForPageToLoad(driver, webDriverWait);
        //mediaTerminationPage.setPortStart("1");
        //DelayUtils.waitForPageToLoad(driver, webDriverWait);
        mediaTerminationPage.setDeviceEnd(ODF_C_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        //mediaTerminationPage.setCardStart(CARD_NAME);
        //DelayUtils.waitForPageToLoad(driver, webDriverWait);
        //mediaTerminationPage.setPortStart("1");
        //DelayUtils.waitForPageToLoad(driver, webDriverWait);
        mediaTerminationPage.clickUpdateCable();
        ConfirmationBoxInterface prompt = ConfirmationBox.create(driver, webDriverWait);
        prompt.clickButtonByLabel("Finish");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }*/

    @Test(priority = 21)
    @Description("Create Patchcord on Building A")
    public void createPatchcordOnBuildingA() {
        gisViewPage = GisViewPage.goToGisViewPage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.searchFirstResult(BUILDING_A_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.useContextAction("Show on", "Patchcord wizard");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        PatchcordWizardPage patchcordWizardPage = new PatchcordWizardPage(driver);
        //patchcordWizardPage.getPatchcordConnectorsTableStart().selectRowByAttributeValueWithLabel("Port", "1");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        patchcordWizardPage.setDeviceTerminationB(ODF_A_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        patchcordWizardPage.getPatchcordConnectorsTableEnd().selectRowByAttributeValueWithLabel("Port", "2");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        DelayUtils.sleep(2000);
    }

    public void checkPopup() {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assertions.assertThat(messages).hasSize(1);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getMessageType())
                .isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
    }
}

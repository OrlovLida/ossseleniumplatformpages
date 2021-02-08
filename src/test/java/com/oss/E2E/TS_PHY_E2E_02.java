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
import com.oss.pages.platform.HierarchyViewPage;
import com.oss.pages.platform.HomePage;
import com.oss.pages.radio.CableWizardPage;
import io.qameta.allure.Description;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

public class TS_PHY_E2E_02 extends BaseTestCase {

    private static final String BUILDING_A_NAME = "TS_PHY_E2E_02_Building_A";
    private static final String BUILDING_A_2_NAME = "TS_PHY_E2E_02_Building_A_002";
    private static final String BUILDING_C_NAME = "TS_PHY_E2E_02_Building_C";
    private static final String GEOGRAPHICAL_ADDRESS_BUILDING = "fixedAccessIntegrationTestStreet 87649, fixedAccessIntegrationTestCity";
    private static final String GEOGRAPHICAL_ADDRESS_MANHOLE = "fixedAccessIntegrationTestStreet 659921, fixedAccessIntegrationTestCity";
    private static final String MANHOLE_MODEL = "SK-1";
    private static final String MANHOLE_A_NAME = "TS_PHY_E2E_02_Manhole_A";
    private static final String DUCT_MODEL = "HDPE 100x4,3";
    private static final String DUCT_MODEL2 = "HDPE 320x2,9";
    private static final String DUCT_TYPE_PRIMARY = "Primary Duct";
    private static final String DUCT_TYPE_SUBDUCT = "Subduct";
    private static final String ODF_MODEL = "PS-5/24";
    private static final String ODF_A_NAME = "TS_PHY_E2E_02_ODF_A";
    private static final String ODF_C_NAME = "TS_PHY_E2E_02_ODF_C";
    private static final String CARD_MODEL = "Optomer KS-24";
    private static final String PLUGGABLE_MODULE_MODEL = "Generic SC/PC Front/Back";
    private static final String CABLE_MODEL = "1/4\"\" SCF 14-50J 4.0m";
    private static final String CABLE_FROM_A_TO_C_NAME = "TS_PHY_E2E_02_cable_A_TO_C";
    private static final String CABLE_FROM_A_TO_C_2_NAME = "TS_PHY_E2E_02_cable_A_TO_C_2";
    private static final String DUCT_FROM_A_TO_MANHOLE_NAME = "TS_PHY_E2E_02_duct_A_TO_MANHOLE";
    private static final String DUCT_FROM_C_TO_MANHOLE_NAME = "TS_PHY_E2E_02_duct_C_TO_MANHOLE";
    private static final String DUCT_FROM_A_TO_C_NAME = "TS_PHY_E2E_02_duct_A_TO_C";
    private static final String CENTRAL_LOCATION = "TS_PHY_E2E_02_CENTRAL_LOCATION";

    private GisViewPage gisViewPage;
    private LocationWizardPage locationWizardPage;

    private static final String DRAW_SINGLE_LOCATION_DATA_ATTRIBUTENAME = "activate-location-draw-mode";
    private static final String SHOW_ON_DATA_ATTRIBUTENAME = "viewLinksDropdown-NAVIGATION";
    private static final String CREATE_DATA_ATTRIBUTENAME = "viewLinksDropdown-CREATE";
    private static final String EDIT_DATA_ATTRIBUTENAME = "viewLinksDropdown-EDIT";

    @BeforeClass
    public void openGisView() {
        openGIS();
        gisViewPage = new GisViewPage(driver);
    }

    @Test(priority = 1)
    @Description("Create two buildings")
    public void createBuilding() {
        gisViewPage.searchResult(CENTRAL_LOCATION);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.clickOnMapByCoordinates(2, 4);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.chooseOptionFromDropDownList(DRAW_SINGLE_LOCATION_DATA_ATTRIBUTENAME, "createShape_single_BUILDING");
        gisViewPage.clickOnMapByCoordinates(4, 2);

        locationWizardPage = new LocationWizardPage(driver);
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
        checkMessageText("Locations were created successfully");
    }

    @Test(priority = 2)
    @Description("Change street number")
    public void changeStreetNumber() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.chooseObjectFromList("[Buildings] " + BUILDING_A_2_NAME, 4, 2);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.chooseOptionFromDropDownList(EDIT_DATA_ATTRIBUTENAME, "Edit Location");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        locationWizardPage.setLocationName(BUILDING_C_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        locationWizardPage.clickNext();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        locationWizardPage.setStreetNumber("4");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        locationWizardPage.accept();
        checkPopupType();
    }

    @Test(priority = 3)
    @Description("Change location of building C")
    public void dragAndDropBuildingC() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.useContextActionByLabel("Move Location");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.dragAndDropObject(4, 2, 8, 4);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.useContextActionByLabel("Save Edited Shape");
        checkPopupType();
    }

    @Test(priority = 4)
    @Description("Rename building A")
    public void renameBuildingA() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.clickOnMapByCoordinates(4, 2);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.chooseOptionFromDropDownList(EDIT_DATA_ATTRIBUTENAME, "Edit Location");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        locationWizardPage.setLocationName(BUILDING_A_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        locationWizardPage.clickNext();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        locationWizardPage.accept();
        checkPopupType();
    }

    @Test(priority = 5)
    @Description("Create manhole")
    public void createManhole() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.searchResult(CENTRAL_LOCATION);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        gisViewPage.clickOnMapByCoordinates(2, 4);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.chooseOptionFromDropDownList(DRAW_SINGLE_LOCATION_DATA_ATTRIBUTENAME, "createShape_single_MANHOLE");
        gisViewPage.clickOnMapByCoordinates(8, 2);

        DelayUtils.waitForPageToLoad(driver, webDriverWait);
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
        checkMessageText("Location has been created successfully");
    }

    @Test(priority = 6)
    @Description("Create duct between building A na manhole")
    public void createDuctFromBuildingAToManhole() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.clickOnMapByCoordinates(2, 4);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.useContextActionByLabel("Draw New Duct");
        gisViewPage.clickOnMapByCoordinates(4, 2);
        gisViewPage.clickOnMapByCoordinates(8, 2);
        gisViewPage.doubleClickOnMapByCoordinates(8, 2);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        CreateDuctWizardPage createDuctWizardPage = new CreateDuctWizardPage(driver);
        createDuctWizardPage.setModel(DUCT_MODEL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        createDuctWizardPage.setType(DUCT_TYPE_PRIMARY);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        createDuctWizardPage.setName(DUCT_FROM_A_TO_MANHOLE_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        createDuctWizardPage.create();
        checkMessageText("Duct has been created");
    }

    @Test(priority = 7)
    @Description("Create duct between building C and manhole")
    public void createDuctFromBuildingCToManhole() {
        gisViewPage.clickOnMapByCoordinates(2, 4);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.useContextActionByLabel("Draw New Duct");
        gisViewPage.clickOnMapByCoordinates(8, 4);
        gisViewPage.clickOnMapByCoordinates(8, 2);
        gisViewPage.doubleClickOnMapByCoordinates(8, 2);

        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        CreateDuctWizardPage createDuctWizardPage = new CreateDuctWizardPage(driver);
        createDuctWizardPage.setModel(DUCT_MODEL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        createDuctWizardPage.setType(DUCT_TYPE_PRIMARY);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        createDuctWizardPage.setName(DUCT_FROM_C_TO_MANHOLE_NAME);
        createDuctWizardPage.create();
        checkMessageText("Duct has been created");

        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.enableLayerInTree("Duct");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 8)
    @Description("Create duct between Building A and C")
    public void createDuctFromBuildingAToBuildingC() {
        gisViewPage.clickOnMapByCoordinates(2, 4);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.useContextActionByLabel("Draw New Duct");
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
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        createDuctWizardPage.setName(DUCT_FROM_A_TO_C_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        createDuctWizardPage.create();
        checkMessageText("Duct has been created");
    }

    @Test(priority = 9)
    @Description("Create routing through manhole")
    public void createSubductRouting() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.searchResult(CENTRAL_LOCATION);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.clickOnMapByCoordinates(6, 3);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.chooseOptionFromDropDownList(EDIT_DATA_ATTRIBUTENAME, "Routing");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        RoutingWizardPage routingWizardPage = new RoutingWizardPage(driver);
        routingWizardPage.insertPhysicalLocation(MANHOLE_A_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        routingWizardPage.selectDuctForSegment(0);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        routingWizardPage.selectDuctForSegment(1);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        routingWizardPage.clickOk();
        checkMessageText("Duct has been updated");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 10)
    @Description("Check duct utilization %")
    public void checkDuctUtilizationValue() {
        checkDuctUtilization(DUCT_FROM_A_TO_C_NAME, "119.71");
    }

    @Test(priority = 11)
    @Description("Change model of duct")
    public void changePrimaryDuctModel() {
        gisViewPage.searchResult(DUCT_FROM_C_TO_MANHOLE_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.chooseOptionFromDropDownList(EDIT_DATA_ATTRIBUTENAME, "Edit Duct");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        CreateDuctWizardPage createDuctWizardPage = new CreateDuctWizardPage(driver);
        createDuctWizardPage.setModel(DUCT_MODEL2);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        createDuctWizardPage.create();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        gisViewPage.searchResult(DUCT_FROM_A_TO_MANHOLE_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.chooseOptionFromDropDownList(EDIT_DATA_ATTRIBUTENAME, "Edit Duct");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        createDuctWizardPage.setModel(DUCT_MODEL2);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        createDuctWizardPage.create();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 12)
    @Description("Check duct utilization %")
    public void checkDuctUtilizationAfterModelChange() {
        checkDuctUtilization(DUCT_FROM_A_TO_C_NAME, "10.13");
    }

    @Test(priority = 13)
    @Description("Create copy of ducts")
    public void createCopyOfDucts() {
        gisViewPage.searchResult(CENTRAL_LOCATION);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.clickOnMapByCoordinates(2, 4);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.clickOnMapByCoordinates(6, 2);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.chooseOptionFromDropDownList(EDIT_DATA_ATTRIBUTENAME, "Copy Duct");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        DuctCopyWizardPage ductCopyWizardPage = new DuctCopyWizardPage(driver);
        ductCopyWizardPage.setNumberOfCopies("1");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        ductCopyWizardPage.create();
        checkMessageText("Duct has been copied");

        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.useContextActionByLabel("Refresh");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        gisViewPage.clickOnMapByCoordinates(8, 3);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.chooseOptionFromDropDownList(EDIT_DATA_ATTRIBUTENAME, "Copy Duct");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        ductCopyWizardPage.setNumberOfCopies("1");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        ductCopyWizardPage.create();
        checkMessageText("Duct has been copied");

        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.useContextActionByLabel("Refresh");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 14)
    @Description("Create Cable Between Two Buildings")
    public void createCableFromBuildingAToBuildingC() {
        gisViewPage.searchResult(CENTRAL_LOCATION);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.clickOnMapByCoordinates(2, 4);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.useContextActionByLabel("Draw New Cable");
        gisViewPage.clickOnMapByCoordinates(4, 2);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.clickOnMapByCoordinates(8, 4);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.clickOnMapByCoordinates(8, 4);
        CableWizardPage cableWizardPage = new CableWizardPage(driver);
        cableWizardPage.setModel(CABLE_MODEL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cableWizardPage.setName(CABLE_FROM_A_TO_C_NAME);
        cableWizardPage.accept();
        checkMessageText("Cable has been created successfully");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }


    @Test(priority = 15)
    @Description("Route Cable over Primary Ducts")
    public void routeCableOverPrimaryDucts() {
        gisViewPage.searchResult(CABLE_FROM_A_TO_C_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.chooseOptionFromDropDownList(EDIT_DATA_ATTRIBUTENAME, "Routing");

        CableRoutingViewPage cableRoutingViewPage = new CableRoutingViewPage(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cableRoutingViewPage.clickInsertLocation();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cableRoutingViewPage.insertLocationToRouting(MANHOLE_A_NAME);
        cableRoutingViewPage.clickAddLocationToRouting();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cableRoutingViewPage.selectSegment(1);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cableRoutingViewPage.selectSegment(3);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cableRoutingViewPage.clickSave();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cableRoutingViewPage.clickClose();
        checkPopupType();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 16)
    @Description("Create second Cable between Building A and Building C")
    public void createCable2FromBuildingAToBuildingC() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.searchResult(CENTRAL_LOCATION);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.clickOnMapByCoordinates(2, 4);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.useContextActionByLabel("Draw New Cable");
        gisViewPage.clickOnMapByCoordinates(4, 2);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.clickOnMapByCoordinates(8, 4);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.clickOnMapByCoordinates(8, 4);

        CableWizardPage cableWizardPage = new CableWizardPage(driver);
        cableWizardPage.setModel(CABLE_MODEL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cableWizardPage.setName(CABLE_FROM_A_TO_C_2_NAME);
        cableWizardPage.accept();
        checkMessageText("Cable has been created successfully");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 17)
    @Description("Route second Cable over Subduct")
    public void routeCable2OverSubduct() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.searchResult(CABLE_FROM_A_TO_C_2_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.chooseOptionFromDropDownList(EDIT_DATA_ATTRIBUTENAME, "Routing");

        CableRoutingViewPage cableRoutingViewPage = new CableRoutingViewPage(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cableRoutingViewPage.clickInsertLocation();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cableRoutingViewPage.insertLocationToRouting(MANHOLE_A_NAME);
        cableRoutingViewPage.clickAddLocationToRouting();
        cableRoutingViewPage.selectSegment(1);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cableRoutingViewPage.selectSegment(3);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cableRoutingViewPage.clickSave();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cableRoutingViewPage.clickClose();
        checkPopupType();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.searchResult(CENTRAL_LOCATION);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 18)
    @Description("Create ODF devices on Building A")
    public void createODFDeviceOnBuildingA() {
        createODF(BUILDING_A_NAME, ODF_MODEL, ODF_A_NAME);
    }

    @Test(priority = 19)
    @Description("Add Card to ODF on Building A")
    public void addCardToODFOnBuildingA() {
        addCardToODF(ODF_A_NAME, CARD_MODEL);
    }

    @Test(priority = 20)
    @Description("Add Pluggable Module to ODF on Building A")
    public void addPluggableModuleToODFOnBuildingA() {
        addPluggableModule(PLUGGABLE_MODULE_MODEL);
    }

    @Test(priority = 21)
    @Description("Create ODF devices on Building C")
    public void createODFDeviceOnBuildingC() {
        openGIS();
        createODF(BUILDING_C_NAME, ODF_MODEL, ODF_C_NAME);
    }

    @Test(priority = 22)
    @Description("Add Card to ODF on Building C")
    public void addCardToODFOnBuildingC() {
        addCardToODF(ODF_C_NAME, CARD_MODEL);
    }

    @Test(priority = 23)
    @Description("Add Pluggable Module to ODF on Building C")
    public void addPluggableModuleToODFOnBuildingC() {
        addPluggableModule(PLUGGABLE_MODULE_MODEL);
    }

    @Test(priority = 24)
    @Description("Add Termination to cable")
    public void addTerminationToCable() {
        openGIS();

        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.searchResult(CABLE_FROM_A_TO_C_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.chooseOptionFromDropDownList(EDIT_DATA_ATTRIBUTENAME, "Media Termination");

        MediaTerminationPage mediaTerminationPage = new MediaTerminationPage(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        mediaTerminationPage.setFirstLocation();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        mediaTerminationPage.setDeviceStart(ODF_C_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        mediaTerminationPage.setPortStart("1");

        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        mediaTerminationPage.setDeviceEnd(ODF_A_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        mediaTerminationPage.setPortEnd("2");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        mediaTerminationPage.clickUpdateCable();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        mediaTerminationPage.clickFinish();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 25)
    @Description("Create patchcord on building A")
    public void createPatchcordOnBuildingA() {
        gisViewPage.searchResult(BUILDING_A_NAME);

        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.chooseOptionFromDropDownList(SHOW_ON_DATA_ATTRIBUTENAME, "Patchcord wizard");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        PatchcordWizardPage patchcordWizardPage = new PatchcordWizardPage(driver);
        patchcordWizardPage.setDeviceTerminationA(ODF_A_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
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

    @Test(priority = 26)
    @Description("Delete second cable between building A and C")
    public void deleteCableFromAToC2() {
        deleteCable(CABLE_FROM_A_TO_C_2_NAME);
    }

    @Test(priority = 27)
    @Description("Delete cable between building A and C")
    public void deleteCableFromAToC() {
        deleteCable(CABLE_FROM_A_TO_C_NAME);
    }

    @Test(priority = 28)
    @Description("Delete duct between building A and C")
    public void deleteDuctFrom_A_TO_C() {
        deleteDuct(DUCT_FROM_A_TO_C_NAME);
    }

    @Test(priority = 29)
    @Description("Delete duct between building A and manhole")
    public void deleteDuctFrom_A_TO_Manhole() {
        deleteDuct(DUCT_FROM_A_TO_MANHOLE_NAME);
    }

    @Test(priority = 30)
    @Description("Delete duct between building C and manhole")
    public void deleteDuctFrom_C_TO_Manhole() {
        deleteDuct(DUCT_FROM_C_TO_MANHOLE_NAME);
    }

    @Test(priority = 31)
    @Description("Remove copy of ducts")
    public void deleteCopies() {
        deleteCopy(6, 2);
        deleteCopy(8, 3);
    }

    @Test(priority = 32)
    @Description("Delete ODF device on Building C")
    public void deleteODFOnBuildingC() {
        gisViewPage.searchResult(ODF_C_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.chooseOptionFromDropDownList(SHOW_ON_DATA_ATTRIBUTENAME, "Hierarchy View");

        HierarchyViewPage hierarchyViewPage = new HierarchyViewPage(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        hierarchyViewPage.selectFirstObject();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        hierarchyViewPage.useTreeContextAction("EDIT", "DeleteDeviceWizardAction");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        hierarchyViewPage.clickButtonInConfirmationBox("Yes");
        checkPopupType();
    }

    @Test(priority = 33)
    @Description("Delete ODF device on Building A")
    public void deleteODFOnBuildingA() {
        openGisView();
        gisViewPage.searchResult(ODF_A_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.chooseOptionFromDropDownList(SHOW_ON_DATA_ATTRIBUTENAME, "Hierarchy View");

        HierarchyViewPage hierarchyViewPage = new HierarchyViewPage(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        hierarchyViewPage.selectFirstObject();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        hierarchyViewPage.useTreeContextAction("EDIT", "DeleteDeviceWizardAction");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        hierarchyViewPage.clickButtonInConfirmationBox("Yes");
        checkPopupType();
    }

    @Test(priority = 34)
    @Description("Delete building A")
    public void deleteBuildingA() {
        openGisView();
        deleteLocation(BUILDING_A_NAME);
    }

    @Test(priority = 35)
    @Description("Delete building C")
    public void deleteBuildingC() {
        deleteLocation(BUILDING_C_NAME);
    }

    @Test(priority = 36)
    @Description("Delete manhole")
    public void deleteManhole() {
        deleteLocation(MANHOLE_A_NAME);
    }

    private void openGIS() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        HomePage homePage = new HomePage(driver);
        homePage.chooseFromLeftSideMenu("GIS View", "Views");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    private void createODF(String buildingName, String model, String ODF_name) {
        gisViewPage.searchResult(buildingName);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.chooseOptionFromDropDownList(CREATE_DATA_ATTRIBUTENAME, "Create Device");
        DeviceWizardPage deviceWizardPage = new DeviceWizardPage(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.setModel(model);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.setName(ODF_name);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.next();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.setPreciseLocation(buildingName);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.accept();
        checkMessageText(ODF_name + " has been created successfully");
    }

    private void addCardToODF(String ODF_name, String cardModel) {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.searchResult(ODF_name);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.chooseOptionFromDropDownList(SHOW_ON_DATA_ATTRIBUTENAME, "Hierarchy View");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        HierarchyViewPage hierarchyViewPage = new HierarchyViewPage(driver);
        hierarchyViewPage.selectFirstObject();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        hierarchyViewPage.useTreeContextAction("CREATE", "CreateCardOnDeviceAction");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        CardCreateWizardPage cardCreateWizardPage = new CardCreateWizardPage(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cardCreateWizardPage.setModel(cardModel);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cardCreateWizardPage.setSlots("1");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cardCreateWizardPage.clickAccept();
        checkPopupType();
    }

    private void addPluggableModule(String pluggableModuleModel) {
        HierarchyViewPage hierarchyViewPage = new HierarchyViewPage(driver);
        hierarchyViewPage.useTreeContextAction("CREATE", "CreatePluggableModuleOnDeviceApp");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        CreatePluggableModuleWizardPage createPluggableModuleWizardPage = new CreatePluggableModuleWizardPage(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        createPluggableModuleWizardPage.setModel(pluggableModuleModel);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        createPluggableModuleWizardPage.setPort("1");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        createPluggableModuleWizardPage.accept();
        checkPopupType();
    }

    private void checkPopupType() {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assert.assertEquals(messages.get(0).getMessageType(), SystemMessageContainer.MessageType.SUCCESS);
    }

    private void checkMessageText(String text) {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assert.assertEquals(messages.get(0).getMessageType(), SystemMessageContainer.MessageType.SUCCESS);
        Assert.assertTrue((messages.get(0).getText()).contains(text));
    }

    private void checkDuctUtilization(String ductName, String expectedUtilization) {
        gisViewPage.searchResult(ductName);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.expandDockedPanel("bottom");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.selectTab("6");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        String utilization = gisViewPage.getCellValue(0, "Utilization [%]");
        gisViewPage.hideDockedPanel("bottom");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertEquals(utilization, expectedUtilization);
    }

    private void deleteLocation(String elementName) {
        gisViewPage.searchResult(elementName);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.chooseOptionFromDropDownList(EDIT_DATA_ATTRIBUTENAME, "Remove location");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.clickButtonInPopupByLabel("Delete");
        checkMessageText("Location removed successfully");
    }

    private void deleteCable(String elementName) {
        gisViewPage.searchResult(elementName);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.chooseOptionFromDropDownList(EDIT_DATA_ATTRIBUTENAME, "Remove Connections");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.clickButtonInPopupByLabel("Remove");
        checkMessageText("Connection removed successfully.");
    }

    private void deleteDuct(String ductName) {
        gisViewPage.searchResult(ductName);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.chooseOptionFromDropDownList(EDIT_DATA_ATTRIBUTENAME, "Remove Duct(s)");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.clickButtonInPopupByLabel("Yes");
        checkMessageText("Duct(s) has been removed successfully");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    private void deleteCopy(int x, int y) {
        gisViewPage.searchResult(CENTRAL_LOCATION);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.clickOnMapByCoordinates(2, 4);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.clickOnMapByCoordinates(x, y);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.chooseOptionFromDropDownList(EDIT_DATA_ATTRIBUTENAME, "Remove Duct(s)");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.clickButtonInPopupByLabel("Yes");
        checkMessageText("Duct(s) has been removed successfully");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }
}

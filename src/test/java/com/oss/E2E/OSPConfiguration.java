package com.oss.E2E;

import com.oss.BaseTestCase;
import com.oss.framework.alerts.SystemMessageContainer;
import com.oss.framework.alerts.SystemMessageInterface;
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
    private String BUILDING_A_NAME = "Building A";
    private String BUILDING_C_NAME = "Building C";
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
    private String PLUGGABLE_MODULE_MODEL = "Generic SC/PC Front/Back";

    private LocationOverviewPage locationOverviewPage;
    private DeviceOverviewPage deviceOverviewPage;
    private DuctCopyWizardPage ductCopyWizardPage;


    @BeforeClass
    public void openGisViewByLink() {
        gisViewPage = GisViewPage.goToGisViewPage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.searchFirstResult("gliwice");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
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

    public void checkPopup() {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assertions.assertThat(messages).hasSize(1);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getMessageType())
                .isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
    }
}

package com.oss.ThreeUKProject;

import com.comarch.oss.web.pages.GlobalSearchPage;
import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.alerts.SystemMessageInterface;
import com.oss.framework.widgets.list.CommonList;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.platform.HomePage;
import com.comarch.oss.web.pages.OldInventoryView.OldInventoryViewPage;
import com.oss.pages.radio.Cell5GWizardPage;
import com.oss.pages.radio.CellSiteConfigurationPage;
import com.oss.pages.radio.GNodeBWizardPage;
import com.oss.pages.radio.HostingWizardPage;
import com.oss.repositories.*;
import com.oss.services.ResourceCatalogClient;
import com.oss.untils.Constants;
import com.oss.untils.Environment;
import com.oss.utils.RandomGenerator;
import com.oss.utils.TestListener;
import io.qameta.allure.Description;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners({TestListener.class})
public class Technology5GTests extends BaseTestCase {

    private Environment env = Environment.getInstance();

    private static String locationId;
    private static final String locationName = "LocationSeleniumTests" + (int) (Math.random() * 10000);
    private static Long addressId;
    private static final String countryName = "CountrySeleniumTests";
    private static final String postalCodeName = "PostalCodeSeleniumTests";
    private static final String regionName = "RegionSeleniumTests";
    private static final String districtName = "DistrictSeleniumTests";
    private static final String cityName = "CitySeleniumTests";
    private static Long gNodeBId;
    private static final String gNodeBNameForDelete = "gNodeBForDeleteSeleniumTests" + (int) (Math.random() * 10000);
    private static final String gNodeBNameForEdit = "gNodeBForEditSeleniumTests" + (int) (Math.random() * 10000);
    private static final String gNodeBNameForRemoveHR = "gNodeBForRemoveHRSeleniumTests" + (int) (Math.random() * 10000);
    private static Long cellId;
    private static final String cell5GId1 = RandomGenerator.generateRandomCell5GId();
    private static final String cell5GNameForDelete = "Cell5GForDeleteSeleniumTests" + (int) (Math.random() * 10000);
    private static final String cell5GId2 = RandomGenerator.generateRandomCell5GId();
    private static final String cell5GNameForEdit = "Cell5GForEditSeleniumTests" + (int) (Math.random() * 10000);
    private static final String cell5GId3 = RandomGenerator.generateRandomCell5GId();
    private static final String cell5GNameForRemoveHRDevice = "Cell5GForRemoveHRDeviceSeleniumTests" + (int) (Math.random() * 10000);
    private static final String cell5GId4 = RandomGenerator.generateRandomCell5GId();
    private static final String cell5GNameForRemoveHRArray = "Cell5GForRemoveHRArraySeleniumTests" + (int) (Math.random() * 10000);
    private static Long deviceModelId;
    private static Long cardModelId;
    private static Long deviceId;
    private static final String rruDeviceNameForEdit = "RRUForHostRelationSeleniumTests" + (int) (Math.random() * 10000);
    private static final String bbuDeviceNameForEdit = "BBUWithCardForHostRelationSeleniumTests" + (int) (Math.random() * 10000);
    private static final String aauAAU5614NameForEdit = "AAUForHostRelationSeleniumTests" + (int) (Math.random() * 10000);
    private static final String objectTypeCell5G = "Cell 5G";
    private static final String locationTypeSite = "Site";
    private static final String description = "Selenium Test";
    private static final String carrier5G = "100 MHz - NR-ARFCN 642000";
    private static final String mccMncPrimary = "3UK [mcc: 234, mnc: 20]";
    private static final String MCC = "234";
    private static final String MNC = "20";
    private static final String RRUAPort = "A T01/R01";

    @BeforeClass
    public void createTestData() {
        getOrCreateAddress();
        createPhysicalLocation();
        createGNodeB(gNodeBNameForDelete);
        createGNodeB(gNodeBNameForEdit);
        createCell5G(cell5GNameForDelete, cell5GId1);
        createCell5G(cell5GNameForEdit, cell5GId2);
        createGNodeB(gNodeBNameForRemoveHR);
        createCell5G(cell5GNameForRemoveHRDevice, cell5GId3);
        createDevice(Constants.RRU5501_MODEL, rruDeviceNameForEdit, Constants.DEVICE_MODEL_TYPE);
        createHRToDevice(); //gNB-RRU, Cell-RRU
        createHRToPort(RRUAPort);//gNB-RRU port, Cell-RRU port
        createDeviceWithCard(Constants.BBU5900_MODEL, bbuDeviceNameForEdit, Constants.DEVICE_MODEL_TYPE, Constants.UBBPg3_CARD_MODEL,
                "0", Constants.CARD_MODEL_TYPE);
        createHRToDevice(); //gNB-BBU, Cell-BBU
        createHRToCard("0", "UBBPg3");//gNB-BBU card, Cell-BBU card
        createDevice(Constants.AAU5614ANTENNA_MODEL, aauAAU5614NameForEdit, Constants.ANTENNA_MODEL_TYPE);
        createHRToDevice(); //gNB-AAU, Cell-AAU
        createCell5G(cell5GNameForRemoveHRArray, cell5GId4);
    }

    @BeforeMethod
    public void goToHomePage() {
        HomePage homePage = new HomePage(driver);
        homePage.goToHomePage(driver, BASIC_URL);
    }

    @Test
    @Description("The user creates an gNodeB in Cell Site Configuration and checks the message about successful creation")
    public void tSRAN06CreateGNodeB() {
        String randomGNodeBName = "gNodeBForCreateSeleniumTests" + (int) (Math.random() * 10000);
        String randomGNodeBId = RandomGenerator.generateRandomGNodeBId();

        homePage.setOldObjectType(locationTypeSite);
        new OldInventoryViewPage(driver)
                .filterObject("Name", locationName)
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .clickPlusIconAndSelectOption("Create gNodeB");
        new GNodeBWizardPage(driver)
                .createGNodeB(randomGNodeBName, randomGNodeBId, Constants.HUAWEI_GNODEB_MODEL, mccMncPrimary);
        Assert.assertTrue(SystemMessageContainer.create(driver, webDriverWait)
                .getMessages().get(0).getText().contains("Created gNodeB"));
    }

    @Test
    @Description("The user searches Site in Inventory View, then edits the gNodeB in Cell Site Configuration and checks if the description is updated in Base Stations table")
    public void tSRAN07ModifyGNodeB() {

        homePage.setOldObjectType(locationTypeSite);
        new OldInventoryViewPage(driver)
                .filterObject("Name", locationName)
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .expandTreeToLocation(locationTypeSite, locationName)
                .selectTab("Base Stations")
                .filterObject("Name", gNodeBNameForEdit)
                .clickEditIcon();
        new GNodeBWizardPage(driver)
                .setDescription(description)
                .accept();
        Assert.assertTrue(new CellSiteConfigurationPage(driver).getValueByRowNumber("Description", 0).contains(description));
    }

    @Test
    @Description("The user creates a Cell 5G in Cell Site Configuration and checks the message about successful creation")
    public void tSRAN08CreateCell5G() {
        String randomCell5GName = "Cell5GForCreateSeleniumTests" + (int) (Math.random() * 10000);
        String randomCell5GId = RandomGenerator.generateRandomCell5GId();

        homePage.setOldObjectType(locationTypeSite);
        new OldInventoryViewPage(driver)
                .filterObject("Name", locationName)
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .selectTab("Cells")
                .clickPlusIconAndSelectOption("Create Cell 5G");
        new Cell5GWizardPage(driver)
                .createCell5G(randomCell5GName, gNodeBNameForEdit, randomCell5GId, carrier5G);
        Assert.assertTrue(SystemMessageContainer.create(driver, webDriverWait)
                .getMessages().get(0).getText().contains("Created Cell 5G"));
    }

    @Test
    @Description("The user searches Cell 5G in Inventory View, then edits a Cell 5G in Cell Site Configuration and checks if the description is updated in Cells table")
    public void tSRAN09ModifyCell5G() {

        homePage.setOldObjectType(objectTypeCell5G);
        new OldInventoryViewPage(driver)
                .filterObject("Cell Name", cell5GNameForEdit)
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .expandTreeToBaseStation(locationTypeSite, locationName, gNodeBNameForEdit)
                .selectTab("Cells")
                .filterObject("Name", cell5GNameForEdit)
                .clickEditIcon();
        new Cell5GWizardPage(driver)
                .setDescription(description)
                .accept();
        Assert.assertTrue(new CellSiteConfigurationPage(driver).getValueByRowNumber("Description", 0).contains(description));
    }

    @Test
    @Description("The user creates Host Relation between Cell 5G and RRU, RRU port, BBU and BBU card in Cell Site Configuration and checks if new rows are displayed in Hosting table")
    public void tSRAN31CreateHostRelationBetweenCell5GAndRRUBBUCard() {

        homePage.setOldObjectType(locationTypeSite);
        new OldInventoryViewPage(driver)
                .filterObject("Name", locationName)
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .expandTreeToCell(locationTypeSite, locationName, gNodeBNameForEdit, cell5GNameForEdit)
                .selectTab("Hosting")
                .clickPlusIconAndSelectOption("Host on Device");
        new HostingWizardPage(driver)
                .setDevice(rruDeviceNameForEdit);
        DelayUtils.sleep(2000);
        new HostingWizardPage(driver)
                .setHosting("[" + rruDeviceNameForEdit + "] " + RRUAPort + "");
        DelayUtils.sleep(2000);
        new HostingWizardPage(driver)
                .setDevice(bbuDeviceNameForEdit);
        DelayUtils.sleep(2000);
        new HostingWizardPage(driver)
                .setHosting("[" + bbuDeviceNameForEdit + "][Chassis] " + Constants.UBBPg3_CARD_MODEL + "");
        DelayUtils.sleep(2000);
        new HostingWizardPage(driver).clickAccept();
        SystemMessageInterface systemMessageItem = SystemMessageContainer.create(driver, webDriverWait);
        systemMessageItem.waitForMessageDisappear();
        Assert.assertEquals(new CellSiteConfigurationPage(driver).getRowCount("Hosting Resource"), 4);
    }

    @Test
    @Description("The user removes Host Relation between Cell 5G and RRU, RRU port BBU and BBU card, AAU in Cell Site Configuration and checks if new rows are disappeared in Hosting table")
    public void tSRAN32_36RemoveHostRelationBetweenCell5GAndRRUBBUCardAAU() {

        homePage.setOldObjectType(locationTypeSite);
        new OldInventoryViewPage(driver)
                .filterObject("Name", locationName)
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .expandTreeToCell(locationTypeSite, locationName, gNodeBNameForRemoveHR, cell5GNameForRemoveHRDevice)
                .selectTab("Hosting")
                .clearColumnFilter("Hosting Component")
                .clearColumnFilter("Hosting Resource")
                .selectRowByAttributeValueWithLabel("Hosting Component", Constants.UBBPg3_CARD_MODEL)
                .removeObject();
        new CellSiteConfigurationPage(driver)
                .selectRowByAttributeValueWithLabel("Hosting Resource", bbuDeviceNameForEdit)
                .removeObject();
        new CellSiteConfigurationPage(driver)
                .selectRowByAttributeValueWithLabel("Hosting Component", RRUAPort)
                .removeObject();
        new CellSiteConfigurationPage(driver)
                .selectRowByAttributeValueWithLabel("Hosting Resource", rruDeviceNameForEdit)
                .removeObject();
        new CellSiteConfigurationPage(driver)
                .selectRowByAttributeValueWithLabel("Hosting Resource", aauAAU5614NameForEdit)
                .removeObject();
        SystemMessageInterface systemMessageItem = SystemMessageContainer.create(driver, webDriverWait);
        systemMessageItem.waitForMessageDisappear();
        Assert.assertTrue(new CellSiteConfigurationPage(driver).hasNoData());
    }

    @Test
    @Description("The user creates Host Relation between gNodeB and AAU, RRU, RRU port, BBU and BBU card in Cell Site Configuration and checks if new rows are displayed in Hosting table")
    public void tSRAN33CreateHostRelationBetweenGNodeBAndAAURRUBBUCard() {

        homePage.setOldObjectType(locationTypeSite);
        new OldInventoryViewPage(driver)
                .filterObject("Name", locationName)
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .expandTreeToBaseStation(locationTypeSite, locationName, gNodeBNameForEdit)
                .selectTab("Hosting")
                .useTableContextActionByLabel("Host on Device");
        new HostingWizardPage(driver)
                .setDevice(aauAAU5614NameForEdit);
        DelayUtils.sleep(2000);
        new HostingWizardPage(driver)
                .setDevice(rruDeviceNameForEdit);
        DelayUtils.sleep(2000);
        new HostingWizardPage(driver)
                .setHosting("[" + rruDeviceNameForEdit + "] " + RRUAPort + "");
        DelayUtils.sleep(2000);
        new HostingWizardPage(driver)
                .setDevice(bbuDeviceNameForEdit);
        DelayUtils.sleep(2000);
        new HostingWizardPage(driver)
                .setHosting("[" + bbuDeviceNameForEdit + "][Chassis] " + Constants.UBBPg3_CARD_MODEL + "");
        DelayUtils.sleep(2000);
        new HostingWizardPage(driver).clickAccept();
        SystemMessageInterface systemMessageItem = SystemMessageContainer.create(driver, webDriverWait);
        systemMessageItem.waitForMessageDisappear();
        Assert.assertEquals(new CellSiteConfigurationPage(driver).getRowCount("Hosting Resource"), 5);
    }

    @Test
    @Description("The user removes Host Relation between gNodeB and AAU, RRU, RRU port, BBU and BBU card in Cell Site Configuration and checks if new rows are disappeared in Hosting table")
    public void tSRAN34RemoveHostRelationBetweenGNodeBAndAAURRUBBUCard() {

        homePage.setOldObjectType(locationTypeSite);
        new OldInventoryViewPage(driver)
                .filterObject("Name", locationName)
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .expandTreeToBaseStation(locationTypeSite, locationName, gNodeBNameForRemoveHR)
                .selectTab("Hosting")
                .clearColumnFilter("Hosting Component")
                .clearColumnFilter("Hosting Resource")
                .selectRowByAttributeValueWithLabel("Hosting Component", Constants.UBBPg3_CARD_MODEL)
                .removeObject();
        new CellSiteConfigurationPage(driver)
                .selectRowByAttributeValueWithLabel("Hosting Resource", bbuDeviceNameForEdit)
                .removeObject();
        new CellSiteConfigurationPage(driver)
                .selectRowByAttributeValueWithLabel("Hosting Component", RRUAPort)
                .removeObject();
        new CellSiteConfigurationPage(driver)
                .selectRowByAttributeValueWithLabel("Hosting Resource", rruDeviceNameForEdit)
                .removeObject();
        new CellSiteConfigurationPage(driver)
                .selectRowByAttributeValueWithLabel("Hosting Resource", aauAAU5614NameForEdit)
                .removeObject();
        SystemMessageInterface systemMessageItem = SystemMessageContainer.create(driver, webDriverWait);
        systemMessageItem.waitForMessageDisappear();
        Assert.assertTrue(new CellSiteConfigurationPage(driver).hasNoData());
    }

    @Test
    @Description("The user creates Host Relation between Cell 5G and AAU in Cell Site Configuration and checks if new row is displayed in Hosting table")
    public void tSRAN35CreateHostRelationBetweenCell5GAndAAU() {

        homePage.setOldObjectType(locationTypeSite);
        new OldInventoryViewPage(driver)
                .filterObject("Name", locationName)
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .expandTreeToCell(locationTypeSite, locationName, gNodeBNameForEdit, cell5GNameForEdit)
                .selectTab("Hosting")
                .clickPlusIconAndSelectOption("Host on Device");
        new HostingWizardPage(driver)
                .setDevice(aauAAU5614NameForEdit);
        DelayUtils.sleep(2000);
        new HostingWizardPage(driver).clickAccept();
        SystemMessageInterface systemMessageItem = SystemMessageContainer.create(driver, webDriverWait);
        systemMessageItem.waitForMessageDisappear();
        new CellSiteConfigurationPage(driver)
                .filterObject("Hosting Resource","" + aauAAU5614NameForEdit);
        Assert.assertTrue(new CellSiteConfigurationPage(driver).getValueByRowNumber("Hosting Resource", 0).contains(aauAAU5614NameForEdit));
    }

    @Test
    @Description("The user searches gNodeB in Global Search, then deletes the gNodeB in Cell Site Configuration and checks if search result in Global Search is no data")
    public void tSRAN45RemoveGNodeB() {

        homePage.searchInGlobalSearch(gNodeBNameForDelete)
                .expandShowOnAndChooseView(gNodeBNameForDelete, "NAVIGATION", "Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .expandTreeToLocation(locationTypeSite, locationName)
                .selectTab("Base Stations")
                .filterObject("Name", gNodeBNameForDelete)
                .removeObject();
        SystemMessageInterface systemMessageItem = SystemMessageContainer.create(driver, webDriverWait);
        systemMessageItem.waitForMessageDisappear();
        homePage.searchInGlobalSearch(gNodeBNameForDelete);
        CommonList objectsList = new GlobalSearchPage(driver).getResultsList();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertTrue(objectsList.hasNoData());
    }

    @Test
    @Description("The user searches Cell 5G in Global Search, then deletes the Cell 5G in Cell Site Configuration and checks if search result in Global Search is no data")
    public void tSRAN46RemoveCell5G() {

        homePage.searchInGlobalSearch(cell5GNameForDelete)
                .expandShowOnAndChooseView(cell5GNameForDelete, "NAVIGATION", "Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .expandTreeToBaseStation(locationTypeSite, locationName, gNodeBNameForEdit)
                .selectTab("Cells")
                .filterObject("Name", cell5GNameForDelete)
                .removeObject();
        SystemMessageInterface systemMessageItem = SystemMessageContainer.create(driver, webDriverWait);
        systemMessageItem.waitForMessageDisappear();
        homePage.searchInGlobalSearch(cell5GNameForDelete);
        CommonList objectsList = new GlobalSearchPage(driver).getResultsList();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertTrue(objectsList.hasNoData());
    }

    private void getOrCreateAddress() {
        AddressRepository addressRepository = new AddressRepository(env);
        addressId = addressRepository.updateOrCreateAddress(countryName, postalCodeName, regionName, cityName, districtName);
    }

    private void createPhysicalLocation() {
        LocationInventoryRepository locationInventoryRepository = new LocationInventoryRepository(env);
        locationId = locationInventoryRepository.createLocation(locationName, locationTypeSite, addressId);
    }

    private void createGNodeB(String gNodeBNameForCreate) {
        Radio5gRepository radio5gRepository = new Radio5gRepository(env);
        gNodeBId = radio5gRepository.createGNodeB(gNodeBNameForCreate, Long.valueOf(locationId), MCC, MNC, Constants.HUAWEI_GNODEB_MODEL);
    }

    private void createCell5G(String cell5GNameForCreate, String cell5GIdForCreate) {
        Radio5gRepository radio5gRepository = new Radio5gRepository(env);
        cellId = radio5gRepository.createCell5g(cell5GNameForCreate, Integer.valueOf(cell5GIdForCreate), gNodeBId, MCC, MNC, carrier5G);
    }

    private void createDevice(String deviceModel, String deviceName, String deviceModelType) {
        ResourceCatalogClient resourceCatalogClient = new ResourceCatalogClient(env);
        deviceModelId = resourceCatalogClient.getModelIds(deviceModel);
        PhysicalInventoryRepository physicalInventoryRepository = new PhysicalInventoryRepository(env);
        deviceId = physicalInventoryRepository.createDevice(locationTypeSite, Long.valueOf(locationId), deviceModelId, deviceName, deviceModelType);
    }

    private void createDeviceWithCard(String deviceModel, String deviceName, String deviceModelType, String cardModel, String slotName, String cardModelType) {
        ResourceCatalogClient resourceCatalogClient = new ResourceCatalogClient(env);
        deviceModelId = resourceCatalogClient.getModelIds(deviceModel);
        cardModelId = resourceCatalogClient.getModelIds(cardModel);
        PhysicalInventoryRepository physicalInventoryRepository = new PhysicalInventoryRepository(env);
        deviceId = physicalInventoryRepository.createDeviceWithCard(locationTypeSite, Long.valueOf(locationId), deviceModelId, deviceName, deviceModelType, slotName, cardModelId, cardModelType);
    }

    private void createHRToDevice() {
        Radio5gRepository radio5gRepository = new Radio5gRepository(env);
        radio5gRepository.createHRGNodeBDevice(Long.valueOf(deviceId), gNodeBId);
        radio5gRepository.createHRCellDevice(Long.valueOf(deviceId), gNodeBId, cellId);
    }

    private void createHRToPort(String portName) {
        Radio5gRepository radio5gRepository = new Radio5gRepository(env);
        radio5gRepository.createHRGNodeBDevicePort(Long.valueOf(deviceId), gNodeBId, portName);
        radio5gRepository.createHRCellDevicePort(Long.valueOf(deviceId), gNodeBId, cellId, portName);
    }

    private void createHRToCard(String slotName, String cardName) {
        Radio5gRepository radio5gRepository = new Radio5gRepository(env);
        radio5gRepository.createHRGNodeBDeviceCard(Long.valueOf(deviceId), gNodeBId, slotName, cardName);
        radio5gRepository.createHRCellDeviceCard(Long.valueOf(deviceId), gNodeBId, cellId, slotName, cardName);
    }

}

package com.oss.ThreeUKProject;

import com.oss.BaseTestCase;
import com.oss.framework.alerts.SystemMessageContainer;
import com.oss.framework.alerts.SystemMessageInterface;
import com.oss.framework.listwidget.CommonList;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.platform.GlobalSearchPage;
import com.oss.pages.platform.HomePage;
import com.oss.pages.platform.OldInventoryView.OldInventoryViewPage;
import com.oss.pages.radio.Cell4GWizardPage;
import com.oss.pages.radio.CellSiteConfigurationPage;
import com.oss.pages.radio.ENodeBWizardPage;
import com.oss.pages.radio.HostingWizardPage;
import com.oss.repositories.AddressRepository;
import com.oss.repositories.LocationInventoryRepository;
import com.oss.repositories.PhysicalInventoryRepository;
import com.oss.repositories.Radio4gRepository;
import com.oss.services.PhysicalInventoryClient;
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
public class Technology4GTests extends BaseTestCase {

    private Environment env = Environment.getInstance();

    private static String locationId;
    private static final String locationName = "LocationSeleniumTests4G" + (int) (Math.random() * 10000);
    private static Long addressId;
    private static final String countryName = "CountrySeleniumTests";
    private static final String postalCodeName = "PostalCodeSeleniumTests";
    private static final String regionName = "RegionSeleniumTests";
    private static final String districtName = "DistrictSeleniumTests";
    private static final String cityName = "CitySeleniumTests";
    private static Long eNodeBId;
    private static final String eNodeBNameForDelete = "eNodeBForDeleteSeleniumTests" + (int) (Math.random() * 10000);
    private static final String eNodeBNameForEdit = "eNodeBForEditSeleniumTests" + (int) (Math.random() * 10000);
    private static final String eNodeBNameForRemoveHR = "eNodeBForRemoveHRSeleniumTests" + (int) (Math.random() * 10000);
    private static Long cellId;
    private static final String cell4GId1 = RandomGenerator.generateRandomCell4GId();
    private static final String cell4GNameForDelete = "Cell4GForDeleteSeleniumTests" + (int) (Math.random() * 10000);
    private static final String cell4GId2 = RandomGenerator.generateRandomCell4GId();
    private static final String cell4GNameForEdit = "Cell4GForEditSeleniumTests" + (int) (Math.random() * 10000);
    private static final String cell4GId3 = RandomGenerator.generateRandomCell4GId();
    private static final String cell4GNameForRemoveHRDevice = "Cell4GForRemoveHRDeviceSeleniumTests" + (int) (Math.random() * 10000);
    private static final String cell4GId4 = RandomGenerator.generateRandomCell4GId();
    private static final String cell4GNameForRemoveHRArray = "Cell4GForRemoveHRArraySeleniumTests" + (int) (Math.random() * 10000);
    private static Long deviceModelId;
    private static Long cardModelId;
    private static Long deviceId;
    private static String arrayId;
    private static final String rruDeviceNameForEdit = "RRUForHostRelationSeleniumTests" + (int) (Math.random() * 10000);
    private static final String bbuDeviceNameForEdit = "BBUWithCardForHostRelationSeleniumTests" + (int) (Math.random() * 10000);
    private static final String antennaAHP4517R7v06NameForEdit = "RANAntennaForHostRelationSeleniumTests" + (int) (Math.random() * 10000);
    private static final String objectTypeENodeB = "eNodeB";
    private static final String objectTypeCell4G = "Cell 4G";
    private static final String locationTypeSite = "Site";
    private static final String description = "Selenium Test";
    private static final String carrier4G = "L800-B20-5";
    private static final String mccMncPrimary = "3UK [mcc: 234, mnc: 20]";
    private static final String MCC = "234";
    private static final String MNC = "20";
    private static final String RRUAPort = "A T01/R01";

    @BeforeClass
    public void createTestData() {
        getOrCreateAddress();
        createPhysicalLocation();
        createENodeB(eNodeBNameForDelete);
        createENodeB(eNodeBNameForEdit);
        createCell4G(cell4GNameForDelete, cell4GId1);
        createCell4G(cell4GNameForEdit, cell4GId2);
        createENodeB(eNodeBNameForRemoveHR);
        createCell4G(cell4GNameForRemoveHRDevice, cell4GId3);
        createDevice(Constants.RRU5501_MODEL, rruDeviceNameForEdit, Constants.DEVICE_MODEL_TYPE);
        createHRToDevice(); //eNB-RRU, Cell-RRU
        createHRToPort(RRUAPort);//eNB-RRU port, Cell-RRU port
        createDeviceWithCard(Constants.BBU5900_MODEL, bbuDeviceNameForEdit, Constants.DEVICE_MODEL_TYPE, Constants.UBBPg3_CARD_MODEL,
                "0", Constants.CARD_MODEL_TYPE);
        createHRToDevice(); //eNB-BBU, Cell-BBU
        createHRToCard("0", "UBBPg3");//eNB-BBU card, Cell-BBU card
        createCell4G(cell4GNameForRemoveHRArray, cell4GId4);
        createDevice(Constants.AHP4517R7v06ANTENNA_MODEL, antennaAHP4517R7v06NameForEdit, Constants.ANTENNA_MODEL_TYPE);
        DelayUtils.sleep(5000);
        createHRToArray(Constants.AHP4517R7v06ANTENNA_MODEL + "_Lr1");//Cell-Antenna Array
    }

    @BeforeMethod
    public void goToHomePage() {
        HomePage homePage = new HomePage(driver);
        homePage.goToHomePage(driver, BASIC_URL);
    }

    @Test
    @Description("The user creates an eNodeB in Cell Site Configuration and checks the message about successful creation")
    public void tSRAN02CreateENodeB() {
        String randomENodeBName = "eNodeBForCreateSeleniumTests" + (int) (Math.random() * 10000);
        String randomENodeBId = RandomGenerator.generateRandomENodeBId();

        homePage.setOldObjectType(locationTypeSite);
        new OldInventoryViewPage(driver)
                .filterObject("Name", locationName)
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .clickPlusIconAndSelectOption("Create eNodeB");
        new ENodeBWizardPage(driver)
                .createENodeB(randomENodeBName, randomENodeBId, Constants.HUAWEI_ENODEB_MODEL, mccMncPrimary);
        Assert.assertTrue(SystemMessageContainer.create(driver, webDriverWait)
                .getMessages().get(0).getText().contains("Created eNodeB"));
    }

    @Test
    @Description("The user searches eNodeB in Inventory View, then edits the eNodeB in Cell Site Configuration and checks if the description is updated in Base Stations table")
    public void tSRAN03ModifyENodeB() {

        homePage.setOldObjectType(objectTypeENodeB);
        new OldInventoryViewPage(driver)
                .filterObject("Name", eNodeBNameForEdit)
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .expandTreeToLocation(locationTypeSite, locationName)
                .selectTab("Base Stations")
                .filterObject("Name", eNodeBNameForEdit)
                .clickEditIcon();
        new ENodeBWizardPage(driver)
                .setDescription(description)
                .accept();
        Assert.assertTrue(new CellSiteConfigurationPage(driver).getValueByRowNumber("Description", 0).contains(description));
    }

    @Test
    @Description("The user creates a Cell 4G in Cell Site Configuration and checks the message about successful creation")
    public void tSRAN04CreateCell4G() {
        String randomCell4GName = "Cell4GForCreateSeleniumTests" + (int) (Math.random() * 10000);
        String randomCell4GId = RandomGenerator.generateRandomCell4GId();

        homePage.setOldObjectType(locationTypeSite);
        new OldInventoryViewPage(driver)
                .filterObject("Name", locationName)
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .selectTab("Cells")
                .clickPlusIconAndSelectOption("Create Cell 4G");
        new Cell4GWizardPage(driver)
                .createCell4G(randomCell4GName, eNodeBNameForEdit, randomCell4GId, carrier4G);
        Assert.assertTrue(SystemMessageContainer.create(driver, webDriverWait)
                .getMessages().get(0).getText().contains("Created Cell 4G"));
    }

    @Test
    @Description("The user searches Cell 4G in Inventory View, then edits a Cell 4G in Cell Site Configuration and checks if the description is updated in Cells table")
    public void tSRAN05ModifyCell4G() {

        homePage.setOldObjectType(objectTypeCell4G);
        new OldInventoryViewPage(driver)
                .filterObject("Cell Name", cell4GNameForEdit)
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .expandTreeToBaseStation(locationTypeSite, locationName, eNodeBNameForEdit)
                .selectTab("Cells")
                .filterObject("Name", cell4GNameForEdit)
                .clickEditIcon();
        new Cell4GWizardPage(driver)
                .setDescription(description)
                .accept();
        Assert.assertTrue(new CellSiteConfigurationPage(driver).getValueByRowNumber("Description", 0).contains(description));
    }

    @Test
    @Description("The user creates Host Relation between Cell 4G and RRU, RRU port, BBU and BBU card in Cell Site Configuration and checks if new rows are displayed in Hosting table")
    public void tSRAN25CreateHostRelationBetweenCell4GAndRRUBBUCard() {

        homePage.setOldObjectType(locationTypeSite);
        new OldInventoryViewPage(driver)
                .filterObject("Name", locationName)
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .expandTreeToCell(locationTypeSite, locationName, eNodeBNameForEdit, cell4GNameForEdit)
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
    @Description("The user removes Host Relation between Cell 4G and RRU, RRU port, BBU and BBU card in Cell Site Configuration and checks if new rows are disappeared in Hosting table")
    public void tSRAN26RemoveHostRelationBetweenCell4GAndRRUBBUCard() {

        homePage.setOldObjectType(locationTypeSite);
        new OldInventoryViewPage(driver)
                .filterObject("Name", locationName)
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .expandTreeToCell(locationTypeSite, locationName, eNodeBNameForRemoveHR, cell4GNameForRemoveHRDevice)
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
        SystemMessageInterface systemMessageItem = SystemMessageContainer.create(driver, webDriverWait);
        systemMessageItem.waitForMessageDisappear();
        Assert.assertTrue(new CellSiteConfigurationPage(driver).hasNoData());
    }

    @Test
    @Description("The user creates Host Relation between eNodeB and RRU, RRU port, BBU and BBU card in Cell Site Configuration and checks if new rows are displayed in Hosting table")
    public void tSRAN27CreateHostRelationBetweenENodeBAndRRUBBUCard() {

        homePage.setOldObjectType(locationTypeSite);
        new OldInventoryViewPage(driver)
                .filterObject("Name", locationName)
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .expandTreeToBaseStation(locationTypeSite, locationName, eNodeBNameForEdit)
                .selectTab("Hosting")
                .useTableContextActionByLabel("Host on Device");
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
    @Description("The user removes Host Relation between eNodeB and RRU, RRU port, BBU and BBU card in Cell Site Configuration and checks if new rows are disappeared in Hosting table")
    public void tSRAN28RemoveHostRelationBetweenENodeBAndRRUBBUCard() {

        homePage.setOldObjectType(locationTypeSite);
        new OldInventoryViewPage(driver)
                .filterObject("Name", locationName)
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .expandTreeToBaseStation(locationTypeSite, locationName, eNodeBNameForRemoveHR)
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
        SystemMessageInterface systemMessageItem = SystemMessageContainer.create(driver, webDriverWait);
        systemMessageItem.waitForMessageDisappear();
        Assert.assertTrue(new CellSiteConfigurationPage(driver).hasNoData());
    }

    @Test
    @Description("The user creates Host Relation between Cell 4G and RAN Antenna Array in Cell Site Configuration and checks if new row is displayed in Hosting table")
    public void tSRAN29CreateHostRelationBetweenCell4GAndRANAntennaArray() {

        homePage.setOldObjectType(locationTypeSite);
        new OldInventoryViewPage(driver)
                .filterObject("Name", locationName)
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .expandTreeToCell(locationTypeSite, locationName, eNodeBNameForEdit, cell4GNameForEdit)
                .selectTab("Hosting")
                .clickPlusIconAndSelectOption("Host on Antenna Array");
        new HostingWizardPage(driver)
                .setHostingContains("" + antennaAHP4517R7v06NameForEdit + "/" + Constants.AHP4517R7v06ANTENNA_MODEL + "_Lr1");
        DelayUtils.sleep(2000);
        new HostingWizardPage(driver).clickAccept();
        SystemMessageInterface systemMessageItem = SystemMessageContainer.create(driver, webDriverWait);
        systemMessageItem.waitForMessageDisappear();
        new CellSiteConfigurationPage(driver)
                .filterObject("Hosting Resource","" + Constants.AHP4517R7v06ANTENNA_MODEL + "_Lr1");
        Assert.assertTrue(new CellSiteConfigurationPage(driver).getValueByRowNumber("Hosting Resource", 0).contains("" + Constants.AHP4517R7v06ANTENNA_MODEL + "_Lr1"));
    }

    @Test
    @Description("The user creates Host Relation between Cell 4G and RAN Antenna Array in Cell Site Configuration and checks if new row is displayed in Hosting table")
    public void tSRAN30RemoveHostRelationBetweenCell4GAndRANAntennaArray() {

        homePage.setOldObjectType(locationTypeSite);
        new OldInventoryViewPage(driver)
                .filterObject("Name", locationName)
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .expandTreeToCell(locationTypeSite, locationName, eNodeBNameForRemoveHR, cell4GNameForRemoveHRArray)
                .selectTab("Hosting")
                .clearColumnFilter("Hosting Component")
                .clearColumnFilter("Hosting Resource")
                .selectRowByAttributeValueWithLabel("Hosting Resource", "" + Constants.AHP4517R7v06ANTENNA_MODEL + "_Lr1")
                .removeObject();
        SystemMessageInterface systemMessageItem = SystemMessageContainer.create(driver, webDriverWait);
        systemMessageItem.waitForMessageDisappear();
        Assert.assertTrue(new CellSiteConfigurationPage(driver).hasNoData());
    }

    @Test
    @Description("The user searches eNodeB in Global Search, then deletes the eNodeB in Cell Site Configuration and checks if search result in Global Search is no data")
    public void tSRAN43RemoveENodeB() {

        homePage.searchInGlobalSearch(eNodeBNameForDelete)
                .expandShowOnAndChooseView(eNodeBNameForDelete, "NAVIGATION", "Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .expandTreeToLocation(locationTypeSite, locationName)
                .selectTab("Base Stations")
                .filterObject("Name", eNodeBNameForDelete)
                .removeObject();
        SystemMessageInterface systemMessageItem = SystemMessageContainer.create(driver, webDriverWait);
        systemMessageItem.waitForMessageDisappear();
        homePage.searchInGlobalSearch(eNodeBNameForDelete);
        CommonList objectsList = new GlobalSearchPage(driver).getResultsList();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertTrue(objectsList.isNoData());
    }

    @Test
    @Description("The user searches Cell 4G in Global Search, then deletes the Cell 4G in Cell Site Configuration and checks if search result in Global Search is no data")
    public void tSRAN44RemoveCell4G() {

        homePage.searchInGlobalSearch(cell4GNameForDelete)
                .expandShowOnAndChooseView(cell4GNameForDelete, "NAVIGATION", "Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .expandTreeToBaseStation(locationTypeSite, locationName, eNodeBNameForEdit)
                .selectTab("Cells")
                .filterObject("Name", cell4GNameForDelete)
                .removeObject();
        SystemMessageInterface systemMessageItem = SystemMessageContainer.create(driver, webDriverWait);
        systemMessageItem.waitForMessageDisappear();
        homePage.searchInGlobalSearch(cell4GNameForDelete);
        CommonList objectsList = new GlobalSearchPage(driver).getResultsList();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertTrue(objectsList.isNoData());
    }

    private void getOrCreateAddress() {
        AddressRepository addressRepository = new AddressRepository(env);
        addressId = addressRepository.updateOrCreateAddress(countryName, postalCodeName, regionName, cityName, districtName);
    }

    private void createPhysicalLocation() {
        LocationInventoryRepository locationInventoryRepository = new LocationInventoryRepository(env);
        locationId = locationInventoryRepository.createLocation(locationName, locationTypeSite, addressId);
    }

    private void createENodeB(String eNodeBNameForCreate) {
        Radio4gRepository radio4gRepository = new Radio4gRepository(env);
        eNodeBId = radio4gRepository.createENodeB(eNodeBNameForCreate, Long.valueOf(locationId), MCC, MNC, Constants.HUAWEI_ENODEB_MODEL);
    }

    private void createCell4G(String cell4GNameForCreate, String cell4GIdForCreate) {
        Radio4gRepository radio4gRepository = new Radio4gRepository(env);
        cellId = radio4gRepository.createCell4g(cell4GNameForCreate, Integer.valueOf(cell4GIdForCreate), eNodeBId, MCC, MNC, carrier4G);
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
        Radio4gRepository radio4gRepository = new Radio4gRepository(env);
        radio4gRepository.createHRENodeBDevice(Long.valueOf(deviceId), eNodeBId);
        radio4gRepository.createHRCellDevice(Long.valueOf(deviceId), eNodeBId, cellId);
    }

    private void createHRToArray(String arrayName) {
        Radio4gRepository radio4gRepository = new Radio4gRepository(env);
        PhysicalInventoryClient physicalInventoryClient = new PhysicalInventoryClient(env);
        arrayId = physicalInventoryClient.getAntennaArrayId(deviceId, arrayName);
        radio4gRepository.createHRCellDevice(Long.valueOf(arrayId), eNodeBId, cellId);
    }

    private void createHRToPort(String portName) {
        Radio4gRepository radio4gRepository = new Radio4gRepository(env);
        radio4gRepository.createHRENodeBDevicePort(Long.valueOf(deviceId), eNodeBId, portName);
        radio4gRepository.createHRCellDevicePort(Long.valueOf(deviceId), eNodeBId, cellId, portName);
    }

    private void createHRToCard(String slotName, String cardName) {
        Radio4gRepository radio4gRepository = new Radio4gRepository(env);
        radio4gRepository.createHRENodeBDeviceCard(Long.valueOf(deviceId), eNodeBId, slotName, cardName);
        radio4gRepository.createHRCellDeviceCard(Long.valueOf(deviceId), eNodeBId, cellId, slotName, cardName);
    }

}

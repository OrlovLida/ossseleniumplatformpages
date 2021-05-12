package com.oss.ThreeUKProject;

import com.oss.BaseTestCase;
import com.oss.framework.alerts.SystemMessageContainer;
import com.oss.framework.alerts.SystemMessageInterface;
import com.oss.framework.listwidget.CommonList;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.platform.GlobalSearchPage;
import com.oss.pages.platform.HomePage;
import com.oss.pages.platform.OldInventoryView.OldInventoryViewPage;
import com.oss.pages.radio.Cell3GWizardPage;
import com.oss.pages.radio.CellSiteConfigurationPage;
import com.oss.pages.radio.HostingWizardPage;
import com.oss.pages.radio.NodeBWizardPage;
import com.oss.repositories.*;
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
public class Technology3GTests extends BaseTestCase {

    private Environment env = Environment.getInstance();

    private static String locationId;
    private static final String locationName = "LocationSeleniumTests" + (int) (Math.random() * 10000);
    private static Long addressId;
    private static final String countryName = "CountrySeleniumTests";
    private static final String postalCodeName = "PostalCodeSeleniumTests";
    private static final String regionName = "RegionSeleniumTests";
    private static final String districtName = "DistrictSeleniumTests";
    private static final String cityName = "CitySeleniumTests";
    private static Long rncId;
    private static final String rncNameForDelete = "RNCForDeleteSeleniumTests" + (int) (Math.random() * 10000);
    private static final String rncNameForEdit = "RNCForEditSeleniumTests" + (int) (Math.random() * 10000);
    private static final String rncNameForRemoveHR = "RNCForRemoveHRSeleniumTests" + (int) (Math.random() * 10000);
    private static Long nodeBId;
    private static final String nodeBNameForDelete = "NodeBForDeleteSeleniumTests" + (int) (Math.random() * 10000);
    private static final String nodeBNameForEdit = "NodeBForEditSeleniumTests" + (int) (Math.random() * 10000);
    private static final String nodeBNameForRemoveHR = "NodeBForRemoveHRSeleniumTests" + (int) (Math.random() * 10000);
    private static Long cellId;
    private static final String cell3GId1 = RandomGenerator.generateRandomCell3GId();
    private static final String cell3GNameForDelete = "Cell3GForDeleteSeleniumTests" + (int) (Math.random() * 10000);
    private static final String cell3GId2 = RandomGenerator.generateRandomCell3GId();
    private static final String cell3GNameForEdit = "Cell3GForEditSeleniumTests" + (int) (Math.random() * 10000);
    private static final String cell3GId3 = RandomGenerator.generateRandomCell3GId();
    private static final String cell3GNameForRemoveHRDevice = "Cell3GForRemoveHRDeviceSeleniumTests" + (int) (Math.random() * 10000);
    private static final String cell3GId4 = RandomGenerator.generateRandomCell3GId();
    private static final String cell3GNameForRemoveHRArray = "Cell3GForRemoveHRArraySeleniumTests" + (int) (Math.random() * 10000);
    private static Long deviceModelId;
    private static Long cardModelId;
    private static Long deviceId;
    private static String arrayId;
    private static final String rruDeviceNameForEdit = "RRUForHostRelationSeleniumTests" + (int) (Math.random() * 10000);
    private static final String bbuDeviceNameForEdit = "BBUWithCardForHostRelationSeleniumTests" + (int) (Math.random() * 10000);
    private static final String antennaAHP4517R7v06NameForEdit = "RANAntennaForHostRelationSeleniumTests" + (int) (Math.random() * 10000);
    private static final String objectTypeNodeB = "NodeB";
    private static final String objectTypeCell3G = "Cell 3G";
    private static final String locationTypeSite = "Site";
    private static final String description = "Selenium Test";
    private static final String carrier3G = "10564";
    private static final String MCC = "234";
    private static final String MNC = "20";
    private static final String RRUAPort = "A T01/R01";

    @BeforeClass
    public void createTestData() {
        getOrCreateAddress();
        createPhysicalLocation();
        createNodeB(nodeBNameForDelete, rncNameForDelete);
        createNodeB(nodeBNameForEdit, rncNameForEdit);
        createCell3G(cell3GNameForDelete, cell3GId1);
        createCell3G(cell3GNameForEdit, cell3GId2);
        createNodeB(nodeBNameForRemoveHR, rncNameForRemoveHR);
        createCell3G(cell3GNameForRemoveHRDevice, cell3GId3);
        createDevice(Constants.RRU5501_MODEL, rruDeviceNameForEdit, Constants.DEVICE_MODEL_TYPE);
        createHRToDevice(); //NB-RRU, Cell-RRU
        createHRToPort(RRUAPort);//NB-RRU port, Cell-RRU port
        createDeviceWithCard(Constants.BBU5900_MODEL, bbuDeviceNameForEdit, Constants.DEVICE_MODEL_TYPE, Constants.UBBPg3_CARD_MODEL,
                "0", Constants.CARD_MODEL_TYPE);
        createHRToDevice(); //NB-BBU, Cell-BBU
        createHRToCard("0", "UBBPg3");//NB-BBU card, Cell-BBU card
        createCell3G(cell3GNameForRemoveHRArray, cell3GId4);
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
    @Description("The user creates a NodeB in Cell Site Configuration and checks the message about successful creation")
    public void tSRAN10CreateNodeB() {
        String randomNodeBName = "NodeBForCreateSeleniumTests" + (int) (Math.random() * 10000);
        String randomNodeBId = RandomGenerator.generateRandomENodeBId();

        homePage.setOldObjectType(locationTypeSite);
        new OldInventoryViewPage(driver)
                .filterObject("Name", locationName)
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .clickPlusIconAndSelectOption("Create NodeB");
        new NodeBWizardPage(driver)
                .createNodeB(randomNodeBName, randomNodeBId, Constants.HUAWEI_NODEB_MODEL, rncNameForDelete);
        Assert.assertTrue(SystemMessageContainer.create(driver, webDriverWait)
                .getMessages().get(0).getText().contains("NodeB was created"));
    }

    @Test
    @Description("The user searches NodeB in Inventory View, then edits the NodeB in Cell Site Configuration and checks if the description is updated in Base Stations table")
    public void tSRAN11ModifyNodeB() {

        homePage.setOldObjectType(objectTypeNodeB);
        new OldInventoryViewPage(driver)
                .filterObject("Name", nodeBNameForEdit)
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .expandTreeToLocation(locationTypeSite, locationName)
                .selectTab("Base Stations")
                .filterObject("Name", nodeBNameForEdit)
                .clickEditIcon();
        new NodeBWizardPage(driver)
                .setDescription(description)
                .accept();
        Assert.assertTrue(new CellSiteConfigurationPage(driver).getValueByRowNumber("Description", 0).contains(description));
    }

    @Test
    @Description("The user creates a Cell 3G in Cell Site Configuration and checks the message about successful creation")
    public void tSRAN12CreateCell3G() {
        String randomCell3GName = "Cell3GForCreateSeleniumTests" + (int) (Math.random() * 10000);
        String randomCell3GId = RandomGenerator.generateRandomCell3GId();

        homePage.setOldObjectType(locationTypeSite);
        new OldInventoryViewPage(driver)
                .filterObject("Name", locationName)
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .selectTab("Cells")
                .clickPlusIconAndSelectOption("Create Cell 3G");
        new Cell3GWizardPage(driver)
                .createCell3G(randomCell3GName, nodeBNameForEdit, randomCell3GId, carrier3G);
        Assert.assertTrue(SystemMessageContainer.create(driver, webDriverWait)
                .getMessages().get(0).getText().contains("Cell 3G was created"));
    }

    @Test
    @Description("The user searches Cell 3G in Inventory View, then edits a Cell 3G in Cell Site Configuration and checks if the description is updated in Cells table")
    public void tSRAN13ModifyCell3G() {

        homePage.setOldObjectType(objectTypeCell3G);
        new OldInventoryViewPage(driver)
                .filterObject("Cell Name", cell3GNameForEdit)
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .expandTreeToBaseStation(locationTypeSite, locationName, nodeBNameForEdit)
                .selectTab("Cells")
                .filterObject("Name", cell3GNameForEdit)
                .clickEditIcon();
        new Cell3GWizardPage(driver)
                .setDescription(description)
                .accept();
        Assert.assertTrue(new CellSiteConfigurationPage(driver).getValueByRowNumber("Description", 0).contains(description));
    }

    @Test
    @Description("The user creates Host Relation between Cell 3G and RRU, RRU port BBU and BBU card in Cell Site Configuration and checks if new rows are displayed in Hosting table")
    public void tSRAN37CreateHostRelationBetweenCell3GAndRRUBBUCard() {

        homePage.setOldObjectType(locationTypeSite);
        new OldInventoryViewPage(driver)
                .filterObject("Name", locationName)
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .expandTreeToCell(locationTypeSite, locationName, nodeBNameForEdit, cell3GNameForEdit)
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
    @Description("The user removes Host Relation between Cell 3G and RRU, RRU port, BBU and BBU card in Cell Site Configuration and checks if new rows are disappeared in Hosting table")
    public void tSRAN38RemoveHostRelationBetweenCell3GAndRRUBBUCard() {

        homePage.setOldObjectType(locationTypeSite);
        new OldInventoryViewPage(driver)
                .filterObject("Name", locationName)
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .expandTreeToCell(locationTypeSite, locationName, nodeBNameForRemoveHR, cell3GNameForRemoveHRDevice)
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
    @Description("The user creates Host Relation between NodeB and RRU, RRU port BBU and BBU card in Cell Site Configuration and checks if new rows are displayed in Hosting table")
    public void tSRAN39CreateHostRelationBetweenNodeBAndRRUBBUCard() {

        homePage.setOldObjectType(locationTypeSite);
        new OldInventoryViewPage(driver)
                .filterObject("Name", locationName)
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .expandTreeToBaseStation(locationTypeSite, locationName, nodeBNameForEdit)
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
    @Description("The user removes Host Relation between NodeB and RRU, RRU port, BBU and BBU card in Cell Site Configuration and checks if new rows are disappeared in Hosting table")
    public void tSRAN40RemoveHostRelationBetweenNodeBAndRRUBBUCard() {

        homePage.setOldObjectType(locationTypeSite);
        new OldInventoryViewPage(driver)
                .filterObject("Name", locationName)
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .expandTreeToBaseStation(locationTypeSite, locationName, nodeBNameForRemoveHR)
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
    @Description("The user creates Host Relation between Cell 3G and RAN Antenna Array in Cell Site Configuration and checks if new row is displayed in Hosting table")
    public void tSRAN41CreateHostRelationBetweenCell3GAndRANAntennaArray() {

        homePage.setOldObjectType(locationTypeSite);
        new OldInventoryViewPage(driver)
                .filterObject("Name", locationName)
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .expandTreeToCell(locationTypeSite, locationName, nodeBNameForEdit, cell3GNameForEdit)
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
    @Description("The user creates Host Relation between Cell 3G and RAN Antenna Array in Cell Site Configuration and checks if new row is displayed in Hosting table")
    public void tSRAN42RemoveHostRelationBetweenCell3GAndRANAntennaArray() {

        homePage.setOldObjectType(locationTypeSite);
        new OldInventoryViewPage(driver)
                .filterObject("Name", locationName)
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .expandTreeToCell(locationTypeSite, locationName, nodeBNameForRemoveHR, cell3GNameForRemoveHRArray)
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
    @Description("The user searches NodeB in Global Search, then deletes the NodeB in Cell Site Configuration and checks if search result in Global Search is no data")
    public void tSRAN47RemoveNodeB() {

        homePage.searchInGlobalSearch(nodeBNameForDelete)
                .expandShowOnAndChooseView(nodeBNameForDelete, "NAVIGATION", "Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .expandTreeToLocation(locationTypeSite, locationName)
                .selectTab("Base Stations")
                .filterObject("Name", nodeBNameForDelete)
                .removeObject();
        SystemMessageInterface systemMessageItem = SystemMessageContainer.create(driver, webDriverWait);
        systemMessageItem.waitForMessageDisappear();
        homePage.searchInGlobalSearch(nodeBNameForDelete);
        CommonList objectsList = new GlobalSearchPage(driver).getResultsList();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertTrue(objectsList.isNoData());
    }

    @Test
    @Description("The user searches Cell 3G in Global Search, then deletes the Cell 3G in Cell Site Configuration and checks if search result in Global Search is no data")
    public void tSRAN48RemoveCell3G() {

        homePage.searchInGlobalSearch(cell3GNameForDelete)
                .expandShowOnAndChooseView(cell3GNameForDelete, "NAVIGATION", "Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .expandTreeToBaseStation(locationTypeSite, locationName, nodeBNameForEdit)
                .selectTab("Cells")
                .filterObject("Name", cell3GNameForDelete)
                .removeObject();
        SystemMessageInterface systemMessageItem = SystemMessageContainer.create(driver, webDriverWait);
        systemMessageItem.waitForMessageDisappear();
        homePage.searchInGlobalSearch(cell3GNameForDelete);
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

    private void createNodeB(String nodeBNameForCreate, String rncNameForCreate) {
        Radio3gRepository radio3gRepository = new Radio3gRepository(env);
        rncId = radio3gRepository.createRnc(rncNameForCreate, Long.valueOf(locationId), MCC, MNC);
        nodeBId = radio3gRepository.createNodeB(nodeBNameForCreate, Long.valueOf(locationId), rncId, Constants.HUAWEI_NODEB_MODEL);
    }

    private void createCell3G(String cell3GNameForCreate, String cell3GIdForCreate) {
        Radio3gRepository radio3gRepository = new Radio3gRepository(env);
        cellId = radio3gRepository.createCell3g(cell3GNameForCreate, Integer.valueOf(cell3GIdForCreate), nodeBId, MCC, MNC, carrier3G);
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
        Radio3gRepository radio3gRepository = new Radio3gRepository(env);
        radio3gRepository.createHRNodeBDevice(Long.valueOf(deviceId), nodeBId);
        radio3gRepository.createHRCellDevice(Long.valueOf(deviceId), nodeBId, cellId);
    }

    private void createHRToArray(String arrayName) {
        Radio3gRepository radio3gRepository = new Radio3gRepository(env);
        PhysicalInventoryClient physicalInventoryClient = new PhysicalInventoryClient(env);
        arrayId = physicalInventoryClient.getAntennaArrayId(deviceId, arrayName);
        radio3gRepository.createHRCellDevice(Long.valueOf(arrayId), nodeBId, cellId);
    }

    private void createHRToPort(String portName) {
        Radio3gRepository radio3gRepository = new Radio3gRepository(env);
        radio3gRepository.createHRNodeBDevicePort(Long.valueOf(deviceId), nodeBId, portName);
        radio3gRepository.createHRCellDevicePort(Long.valueOf(deviceId), nodeBId, cellId, portName);
    }

    private void createHRToCard(String slotName, String cardName) {
        Radio3gRepository radio3gRepository = new Radio3gRepository(env);
        radio3gRepository.createHRNodeBDeviceCard(Long.valueOf(deviceId), nodeBId, slotName, cardName);
        radio3gRepository.createHRCellDeviceCard(Long.valueOf(deviceId), nodeBId, cellId, slotName, cardName);
    }

}

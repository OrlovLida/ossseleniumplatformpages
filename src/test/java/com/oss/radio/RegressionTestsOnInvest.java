package com.oss.radio;

import com.oss.BaseTestCase;
import com.oss.framework.alerts.SystemMessageContainer;
import com.oss.framework.alerts.SystemMessageInterface;
import com.oss.framework.listwidget.CommonList;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.physical.DeviceWizardPage;
import com.oss.pages.platform.GlobalSearchPage;
import com.oss.pages.platform.HomePage;
import com.oss.pages.platform.OldInventoryViewPage;
import com.oss.pages.radio.*;
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

/**
 * @author Milena Miętkiewicz
 */

@Listeners({TestListener.class})
public class RegressionTestsOnInvest extends BaseTestCase {

    private Environment env = Environment.getInstance();

    private static String locationId;
    private static final String locationName = RandomGenerator.generateRandomName();
//    private static final String locationName = "SiteSeleniumTests";
    private static Long addressId;
    private static String countryId;
    private static final String countryName = "CountrySeleniumTests";
    private static final String cityName = "CitySeleniumTests";
    private static String regionId;
    private static final String regionName = "RegionSeleniumTests";
    private static final String MCC = "234";
    private static final String MNC = "20";
    private static Long eNodeBId;
    private static final String eNodeBNameForDelete = RandomGenerator.generateRandomName();
    private static final String eNodeBNameForEdit = RandomGenerator.generateRandomName();
    private static final String cell4GId1 = RandomGenerator.generateRandomCell4GId();
    private static final String cell4GNameForDelete = RandomGenerator.generateRandomName();
    private static final String cell4GId2 = RandomGenerator.generateRandomCell4GId();
    private static final String cell4GNameForEdit = RandomGenerator.generateRandomName();
    private static Long gNodeBId;
    private static final String gNodeBNameForDelete = RandomGenerator.generateRandomName();
    private static final String gNodeBNameForEdit = RandomGenerator.generateRandomName();
    private static final String cell5GId1 = RandomGenerator.generateRandomCell5GId();
    private static final String cell5GNameForDelete = RandomGenerator.generateRandomName();
    private static final String cell5GId2 = RandomGenerator.generateRandomCell5GId();
    private static final String cell5GNameForEdit = RandomGenerator.generateRandomName();
    private static Long rncId;
    private static final String rncNameForDelete = RandomGenerator.generateRandomName();
    private static final String rncNameForEdit = RandomGenerator.generateRandomName();
    private static Long nodeBId;
    private static final String nodeBNameForDelete = RandomGenerator.generateRandomName();
    private static final String nodeBNameForEdit = RandomGenerator.generateRandomName();
    private static final String cell3GId1 = RandomGenerator.generateRandomCell3GId();
    private static final String cell3GNameForDelete = RandomGenerator.generateRandomName();
    private static final String cell3GId2 = RandomGenerator.generateRandomCell3GId();
    private static final String cell3GNameForEdit = RandomGenerator.generateRandomName();
    private static Long deviceModelId;
    private static Long cardModelId;
    private static final String rruDeviceNameForDelete = RandomGenerator.generateRandomName();
    private static final String rruDeviceNameForEdit = RandomGenerator.generateRandomName();
    private static final String bbuDeviceNameForDelete = RandomGenerator.generateRandomName();
    private static final String bbuDeviceNameForEdit = RandomGenerator.generateRandomName();
    private static final String antennaAHP4517R7v06NameForDelete = RandomGenerator.generateRandomName();
    private static final String antennaAHP4517R7v06NameForEdit = RandomGenerator.generateRandomName();
    private static final String aauAAU5614NameForDelete = RandomGenerator.generateRandomName();
    private static final String aauAAU5614NameForEdit = RandomGenerator.generateRandomName();
    private static final String objectTypeDevice = "Physical Device";
    private static final String objectTypeNodeB = "NodeB";
    private static final String objectTypeENodeB = "eNodeB";
    private static final String objectTypeCell3G = "Cell 3G";
    private static final String objectTypeCell4G = "Cell 4G";
    private static final String objectTypeCell5G = "Cell 5G";
    private static final String locationTypeSite = "Site";
    private static final String objectTypeRANAntenna = "RAN Antenna";
    private static final String deviceBBUModel = "HUAWEI Technology Co.,Ltd BBU5900";
    private static final String deviceRRUModel = "HUAWEI Technology Co.,Ltd RRU5301";
    private static final String ranAntennaModel = "HUAWEI Technology Co.,Ltd AHP4517R7v06";
    private static final String aauModel = "HUAWEI Technology Co.,Ltd AAU5614";
    private static final String nodeBModel = "HUAWEI Technology Co.,Ltd NodeB";
    private static final String eNodeBModel = "HUAWEI Technology Co.,Ltd eNodeB";
    private static final String gNodeBModel = "HUAWEI Technology Co.,Ltd gNodeB";
    private static final String description = "Selenium Test";
    private static final String mccMncPrimary = "3UK [mcc: 234, mnc: 20]";
    private static final String carrier4G = "L800-B20-5";
    private static final String carrier5G = "NR3600-n78-140";
    private static final String carrier3G = "3GSeleniumCarrier";

    @BeforeClass
    public void createTestData() {
        getOrCreateAddressItemsAndAddress();
        getOrCreatePhysicalLocation();
        createENodeB(eNodeBNameForDelete);
        createENodeB(eNodeBNameForEdit);
        createCell4G(cell4GNameForDelete, cell4GId1);
        createCell4G(cell4GNameForEdit, cell4GId2);
        createGNodeB(gNodeBNameForDelete);
        createGNodeB(gNodeBNameForEdit);
        createCell5G(cell5GNameForDelete, cell5GId1);
        createCell5G(cell5GNameForEdit, cell5GId2);
        createNodeB(nodeBNameForDelete, rncNameForDelete);
        createNodeB(nodeBNameForEdit, rncNameForEdit);
        createCell3G(cell3GNameForDelete, cell3GId1);
        createCell3G(cell3GNameForEdit, cell3GId2);
        createDevice(Constants.RRU5501_MODEL, rruDeviceNameForDelete, Constants.DEVICE_MODEL_TYPE);
        createDevice(Constants.RRU5501_MODEL, rruDeviceNameForEdit, Constants.DEVICE_MODEL_TYPE);
        createDevice(Constants.BBU5900_MODEL, bbuDeviceNameForDelete, Constants.DEVICE_MODEL_TYPE);
        createDeviceWithCard(Constants.BBU5900_MODEL, bbuDeviceNameForEdit, Constants.DEVICE_MODEL_TYPE, Constants.UBBPg3_CARD_MODEL, "0", Constants.CARD_MODEL_TYPE);
        createDevice(Constants.AHP4517R7v06ANTENNA_MODEL, antennaAHP4517R7v06NameForDelete, Constants.ANTENNA_MODEL_TYPE);
        createDevice(Constants.AHP4517R7v06ANTENNA_MODEL, antennaAHP4517R7v06NameForEdit, Constants.ANTENNA_MODEL_TYPE);
        createDevice(Constants.AAU5614ANTENNA_MODEL, aauAAU5614NameForDelete, Constants.ANTENNA_MODEL_TYPE);
        createDevice(Constants.AAU5614ANTENNA_MODEL, aauAAU5614NameForEdit, Constants.ANTENNA_MODEL_TYPE);
    }

    private void getOrCreateAddressItemsAndAddress() {
        AddressRepository addressRepository = new AddressRepository(env);
        countryId = addressRepository.getOrCreateCountryV2(countryName);
        regionId = addressRepository.getOrCreateRegion(countryId, regionName);
        addressRepository.getOrCreateRegion(countryId, regionName);
        addressRepository.getOrCreateCityV2(regionId, cityName);
        addressId = addressRepository.updateOrCreateAddressV2(countryName, countryId, cityName, regionName, regionId);
    }

    private void getOrCreatePhysicalLocation() {
        LocationInventoryRepository locationInventoryRepository = new LocationInventoryRepository(env);
        locationId = locationInventoryRepository.createLocationV2(locationName, locationTypeSite, addressId);
    }

    private void createENodeB(String eNodeBNameForCreate) {
        Radio4gRepository radio4gRepository = new Radio4gRepository(env);
        eNodeBId = radio4gRepository.createENodeB(eNodeBNameForCreate, Long.valueOf(locationId), MCC, MNC, Constants.HUAWEI_ENODEB_MODEL);
    }

    private void createCell4G(String cell4GNameForCreate, String cell4GIdForCreate) {
        Radio4gRepository radio4gRepository = new Radio4gRepository(env);
        radio4gRepository.createCell4g(cell4GNameForCreate, Integer.valueOf(cell4GIdForCreate), eNodeBId, MCC, MNC, carrier4G);
    }

    private void createGNodeB(String gNodeBNameForCreate) {
        Radio5gRepository radio5gRepository = new Radio5gRepository(env);
        gNodeBId = radio5gRepository.createGNodeB(gNodeBNameForCreate, Long.valueOf(locationId), MCC, MNC, Constants.HUAWEI_GNODEB_MODEL);
    }

    private void createCell5G(String cell5GNameForCreate, String cell5GIdForCreate) {
        Radio5gRepository radio5gRepository = new Radio5gRepository(env);
        radio5gRepository.createCell5g(cell5GNameForCreate, Integer.valueOf(cell5GIdForCreate), gNodeBId, MCC, MNC, carrier5G);
    }

    private void createNodeB(String nodeBNameForCreate, String rncNameForCreate) {
        Radio3gRepository radio3gRepository = new Radio3gRepository(env);
        rncId = radio3gRepository.createRnc(rncNameForCreate, Long.valueOf(locationId), MCC, MNC);
        nodeBId = radio3gRepository.createNodeB(nodeBNameForCreate, Long.valueOf(locationId), rncId, Constants.HUAWEI_NODEB_MODEL);
    }

    private void createCell3G(String cell3GNameForCreate, String cell3GIdForCreate) {
        Radio3gRepository radio3gRepository = new Radio3gRepository(env);
        radio3gRepository.createCell3g(cell3GNameForCreate, Integer.valueOf(cell3GIdForCreate), nodeBId, MCC, MNC, carrier3G);
    }

    private void createDevice(String deviceModel, String deviceName, String deviceModelType) {
        ResourceCatalogClient resourceCatalogClient = new ResourceCatalogClient(env);
        deviceModelId = resourceCatalogClient.getModelIds(deviceModel);
        PhysicalInventoryRepository physicalInventoryRepository = new PhysicalInventoryRepository(env);
        physicalInventoryRepository.createDevice(locationTypeSite, Long.valueOf(locationId), deviceModelId, deviceName, deviceModelType);
    }

    private void createDeviceWithCard(String deviceModel, String deviceName, String deviceModelType, String cardModel, String slotName, String cardModelType) {
        ResourceCatalogClient resourceCatalogClient = new ResourceCatalogClient(env);
        deviceModelId = resourceCatalogClient.getModelIds(deviceModel);
        cardModelId = resourceCatalogClient.getModelIds(cardModel);
        PhysicalInventoryRepository physicalInventoryRepository = new PhysicalInventoryRepository(env);
        physicalInventoryRepository.createDeviceWithCard(locationTypeSite, Long.valueOf(locationId), deviceModelId, deviceName, deviceModelType, slotName, cardModelId, cardModelType);
    }

    @BeforeMethod
    public void goToHomePage() {
        HomePage homePage = new HomePage(driver);
        homePage.goToHomePage(driver, BASIC_URL);
    }

    @Test
    @Description("The user creates an eNodeB in Cell Site Configuration and checks the message about successful creation")
    public void tSRAN02CreateENodeB() {
        String randomENodeBName = RandomGenerator.generateRandomName();
        String randomENodeBId = RandomGenerator.generateRandomENodeBId();

        homePage.setOldObjectType(locationTypeSite);
        new OldInventoryViewPage(driver)
                .filterObject("Name", locationName)
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .clickPlusIconAndSelectOption("Create eNodeB");
        new ENodeBWizardPage(driver)
                .createENodeB(randomENodeBName, randomENodeBId, eNodeBModel, mccMncPrimary);
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
        String randomCell4GName = RandomGenerator.generateRandomName();
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
    @Description("The user creates an gNodeB in Cell Site Configuration and checks the message about successful creation")
    public void tSRAN06CreateGNodeB() {
        String randomGNodeBName = RandomGenerator.generateRandomName();
        String randomGNodeBId = RandomGenerator.generateRandomGNodeBId();

        homePage.setOldObjectType(locationTypeSite);
        new OldInventoryViewPage(driver)
                .filterObject("Name", locationName)
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .clickPlusIconAndSelectOption("Create gNodeB");
        new GNodeBWizardPage(driver)
                .createGNodeB(randomGNodeBName, randomGNodeBId, gNodeBModel, mccMncPrimary);
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
        String randomCell5GName = RandomGenerator.generateRandomName();
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
    @Description("The user creates a NodeB in Cell Site Configuration and checks the message about successful creation")
    public void tSRAN10CreateNodeB() {
        String randomNodeBName = RandomGenerator.generateRandomName();
        String randomNodeBId = RandomGenerator.generateRandomENodeBId();

        homePage.setOldObjectType(locationTypeSite);
        new OldInventoryViewPage(driver)
                .filterObject("Name", locationName)
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .clickPlusIconAndSelectOption("Create NodeB");
        new NodeBWizardPage(driver)
                .createNodeB(randomNodeBName, randomNodeBId, nodeBModel, rncNameForDelete);
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
        String randomCell3GName = RandomGenerator.generateRandomName();
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
    @Description("The user creates Radio Remote Unit in Cell Site Configuration and checks the message about successful creation")
    public void tSRAN14CreateRRU() {
        String randomDeviceName = RandomGenerator.generateRandomName();

        homePage.setOldObjectType(locationTypeSite);
        new OldInventoryViewPage(driver)
                .filterObject("Name", locationName)
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .selectTab("Devices")
                .clickPlusIconAndSelectOption("Create Device");
        new DeviceWizardPage(driver)
                .createDevice(deviceRRUModel, randomDeviceName, locationName);
        Assert.assertTrue(SystemMessageContainer.create(driver, webDriverWait)
                .getMessages().get(0).getText().contains("Device " + randomDeviceName + " has been created successfully"));
    }

    @Test
    @Description("The user searches Radio Remote Head in Inventory View, then edits the RRU device in Cell Site Configuration and checks if the description is updated in Device table")
    public void tSRAN15ModifyRRU() {

        homePage.setOldObjectType(objectTypeDevice);
        new OldInventoryViewPage(driver)
                .filterObject("Name", rruDeviceNameForDelete)
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .expandTreeToLocation(locationTypeSite, locationName)
                .selectTab("Devices")
                .filterObject("Name", rruDeviceNameForDelete)
                .clickEditIcon();
        new DeviceWizardPage(driver)
                .setDescription(description);
        new DeviceWizardPage(driver)
                .nextUpdateWizard();
        new DeviceWizardPage(driver)
                .acceptUpdateWizard();
        Assert.assertTrue(new CellSiteConfigurationPage(driver).getValueByRowNumber("Description", 0).contains(description));
    }

    @Test
    @Description("The user creates Base Band Unit in Cell Site Configuration and checks the message about successful creation")
    public void tSRAN16CreateBBU() {
        String randomDeviceName = RandomGenerator.generateRandomName();

        homePage.setOldObjectType(locationTypeSite);
        new OldInventoryViewPage(driver)
                .filterObject("Name", locationName)
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .selectTab("Devices")
                .clickPlusIconAndSelectOption("Create Device");
        new DeviceWizardPage(driver)
                .createDevice(deviceBBUModel, randomDeviceName, locationName);
        Assert.assertTrue(SystemMessageContainer.create(driver, webDriverWait)
                .getMessages().get(0).getText().contains("Device " + randomDeviceName + " has been created successfully"));
    }

    @Test
    @Description("The user searches Base Band Unit in Inventory View, then edits the BBU device in Cell Site Configuration and checks if the description is updated in Device table")
    public void tSRAN17ModifyBBU() {

        homePage.setOldObjectType(objectTypeDevice);
        new OldInventoryViewPage(driver)
                .filterObject("Name", bbuDeviceNameForDelete)
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .expandTreeToLocation(locationTypeSite, locationName)
                .selectTab("Devices")
                .filterObject("Name", bbuDeviceNameForDelete)
                .clickEditIcon();
        new DeviceWizardPage(driver)
                .setDescription(description);
        new DeviceWizardPage(driver)
                .nextUpdateWizard();
        new DeviceWizardPage(driver)
                .acceptUpdateWizard();
        Assert.assertTrue(new CellSiteConfigurationPage(driver).getValueByRowNumber("Description", 0).contains(description));
    }

    @Test
    @Description("The user creates RAN Antenna device in Cell Site Configuration and checks the message about successful creation")
    public void tSRAN19CreateRANAntennaWithArrays() {
        String randomRANAntennaName = RandomGenerator.generateRandomName();

        homePage.setOldObjectType(locationTypeSite);
        new OldInventoryViewPage(driver)
                .filterObject("Name", locationName)
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .selectTab("Devices")
                .clickPlusIconAndSelectOption("Create RAN Antenna");
        new RanAntennaWizardPage(driver)
                .createRANAntenna(ranAntennaModel, randomRANAntennaName, locationName);
        SystemMessageInterface systemMessageItem = SystemMessageContainer.create(driver, webDriverWait);
        systemMessageItem.waitForMessageDisappear();
        new AntennaArrayWizardPage(driver)
                .clickAccept();
        Assert.assertTrue(SystemMessageContainer.create(driver, webDriverWait)
                .getMessages().get(0).getText().contains("Edited Antenna Array"));
    }

    @Test
    @Description("The user searches RAN Antenna in Inventory View, then edits the antenna in Cell Site Configuration and checks if the description is updated in Device table")
    public void tSRAN20ModifyRANAntenna() {

        homePage.setOldObjectType(objectTypeRANAntenna);
        new OldInventoryViewPage(driver)
                .filterObject("Name (Antenna)", antennaAHP4517R7v06NameForDelete)
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .expandTreeToLocation(locationTypeSite, locationName)
                .selectTab("Devices")
                .filterObject("Name", antennaAHP4517R7v06NameForDelete)
                .clickEditIcon();
        new RanAntennaWizardPage(driver)
                .setDescription(description);
        new RanAntennaWizardPage(driver)
                .clickAccept();
        Assert.assertTrue(new CellSiteConfigurationPage(driver).getValueByRowNumber("Description", 0).contains(description));
    }

    @Test
    @Description("The user creates Active Antenna Unit device in Cell Site Configuration and checks the message about successful creation")
    public void tSRAN22CreateAAUWithArrays() {
        String randomRANAntennaName = RandomGenerator.generateRandomName();

        homePage.setOldObjectType(locationTypeSite);
        new OldInventoryViewPage(driver)
                .filterObject("Name", locationName)
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .selectTab("Devices")
                .clickPlusIconAndSelectOption("Create RAN Antenna");
        new RanAntennaWizardPage(driver)
                .createRANAntenna(aauModel, randomRANAntennaName, locationName);
        SystemMessageInterface systemMessageItem = SystemMessageContainer.create(driver, webDriverWait);
        systemMessageItem.waitForMessageDisappear();
        new AntennaArrayWizardPage(driver)
                .clickAccept();
        Assert.assertTrue(SystemMessageContainer.create(driver, webDriverWait)
                .getMessages().get(0).getText().contains("Edited Antenna Array"));
    }

    @Test
    @Description("The user searches Active Antenna Unit in Inventory View, then edits the antenna in Cell Site Configuration and checks if the description is updated in Device table")
    public void tSRAN23ModifyAAU() {

        homePage.setOldObjectType(objectTypeRANAntenna);
        new OldInventoryViewPage(driver)
                .filterObject("Name (Antenna)", aauAAU5614NameForDelete)
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .expandTreeToLocation(locationTypeSite, locationName)
                .selectTab("Devices")
                .filterObject("Name", aauAAU5614NameForDelete)
                .clickEditIcon();
        new RanAntennaWizardPage(driver)
                .setDescription(description);
        new RanAntennaWizardPage(driver)
                .clickAccept();
        Assert.assertTrue(new CellSiteConfigurationPage(driver).getValueByRowNumber("Description", 0).contains(description));
    }

    @Test
    @Description("The user creates Host Relation between Cell 5G and RRU, BBU and BBU card in Cell Site Configuration and checks if new rows are displayed in Hosting table")
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
                .setDevice(bbuDeviceNameForEdit);
        DelayUtils.sleep(2000);
        new HostingWizardPage(driver)
                .setHosting("[" + bbuDeviceNameForEdit + "][Chassis] " + Constants.UBBPg3_CARD_MODEL + "");
        DelayUtils.sleep(2000);
        new HostingWizardPage(driver).clickAccept();
        SystemMessageInterface systemMessageItem = SystemMessageContainer.create(driver, webDriverWait);
        systemMessageItem.waitForMessageDisappear();
        Assert.assertEquals(new CellSiteConfigurationPage(driver).getRowCount("Hosting Resource"), 3);
    }

    //TODO add HR creation by API
    @Test
    @Description("The user removes Host Relation between Cell 4G and RRU, BBU and BBU card in Cell Site Configuration and checks if new rows are disappeared in Hosting table")
    public void tSRAN26RemoveHostRelationBetweenCell4GAndRRUBBUCard() {

        homePage.setOldObjectType(locationTypeSite);
        new OldInventoryViewPage(driver)
                .filterObject("Name", locationName)
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .expandTreeToCell(locationTypeSite, locationName, eNodeBNameForEdit, cell4GNameForEdit)
                .selectTab("Hosting")
                .selectRowByAttributeValueWithLabel("Hosting Component", Constants.UBBPg3_CARD_MODEL)
                .removeObject();
        new CellSiteConfigurationPage(driver)
                .selectRowByAttributeValueWithLabel("Hosting Resource", bbuDeviceNameForEdit)
                .removeObject();
        new CellSiteConfigurationPage(driver)
                .selectRowByAttributeValueWithLabel("Hosting Resource", rruDeviceNameForEdit)
                .removeObject();
        SystemMessageInterface systemMessageItem = SystemMessageContainer.create(driver, webDriverWait);
        systemMessageItem.waitForMessageDisappear();
        Assert.assertTrue(new CellSiteConfigurationPage(driver).hasNoData());
    }

    @Test
    @Description("The user creates Host Relation between eNodeB and RRU, BBU and BBU card in Cell Site Configuration and checks if new rows are displayed in Hosting table")
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
                .setDevice(bbuDeviceNameForEdit);
        DelayUtils.sleep(2000);
        new HostingWizardPage(driver)
                .setHosting("[" + bbuDeviceNameForEdit + "][Chassis] " + Constants.UBBPg3_CARD_MODEL + "");
        DelayUtils.sleep(2000);
        new HostingWizardPage(driver).clickAccept();
        SystemMessageInterface systemMessageItem = SystemMessageContainer.create(driver, webDriverWait);
        systemMessageItem.waitForMessageDisappear();
        Assert.assertEquals(new CellSiteConfigurationPage(driver).getRowCount("Hosting Resource"), 3);
    }

    //TODO add HR creation by API
    @Test
    @Description("The user removes Host Relation between eNodeB and RRU, BBU and BBU card in Cell Site Configuration and checks if new rows are disappeared in Hosting table")
    public void tSRAN28RemoveHostRelationBetweenENodeBAndRRUBBUCard() {

        homePage.setOldObjectType(locationTypeSite);
        new OldInventoryViewPage(driver)
                .filterObject("Name", locationName)
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .expandTreeToBaseStation(locationTypeSite, locationName, eNodeBNameForEdit)
                .selectTab("Hosting")
                .selectRowByAttributeValueWithLabel("Hosting Component", Constants.UBBPg3_CARD_MODEL)
                .removeObject();
        new CellSiteConfigurationPage(driver)
                .selectRowByAttributeValueWithLabel("Hosting Resource", bbuDeviceNameForEdit)
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
        Assert.assertTrue(new CellSiteConfigurationPage(driver).getValueByRowNumber("Hosting Resource", 0).contains("" + Constants.AHP4517R7v06ANTENNA_MODEL + "_Lr1"));
    }

    //TODO add HR creation by API
    @Test
    @Description("The user creates Host Relation between Cell 4G and RAN Antenna Array in Cell Site Configuration and checks if new row is displayed in Hosting table")
    public void tSRAN30RemoveHostRelationBetweenCell4GAndRANAntennaArray() {

        homePage.setOldObjectType(locationTypeSite);
        new OldInventoryViewPage(driver)
                .filterObject("Name", locationName)
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .expandTreeToCell(locationTypeSite, locationName, eNodeBNameForEdit, cell4GNameForEdit)
                .selectTab("Hosting")
                .selectRowByAttributeValueWithLabel("Hosting Resource", "" + Constants.AHP4517R7v06ANTENNA_MODEL + "_Lr1")
                .removeObject();
        SystemMessageInterface systemMessageItem = SystemMessageContainer.create(driver, webDriverWait);
        systemMessageItem.waitForMessageDisappear();
        Assert.assertTrue(new CellSiteConfigurationPage(driver).hasNoData());
    }

    @Test
    @Description("The user creates Host Relation between Cell 5G and RRU, BBU and BBU card in Cell Site Configuration and checks if new rows are displayed in Hosting table")
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
                .setDevice(bbuDeviceNameForEdit);
        DelayUtils.sleep(2000);
        new HostingWizardPage(driver)
                .setHosting("[" + bbuDeviceNameForEdit + "][Chassis] " + Constants.UBBPg3_CARD_MODEL + "");
        DelayUtils.sleep(2000);
        new HostingWizardPage(driver).clickAccept();
        SystemMessageInterface systemMessageItem = SystemMessageContainer.create(driver, webDriverWait);
        systemMessageItem.waitForMessageDisappear();
        Assert.assertEquals(new CellSiteConfigurationPage(driver).getRowCount("Hosting Resource"), 3);
    }

    //TODO add HR creation by API
    @Test
    @Description("The user removes Host Relation between Cell 5G and RRU, BBU and BBU card in Cell Site Configuration and checks if new rows are disappeared in Hosting table")
    public void tSRAN32RemoveHostRelationBetweenCell5GAndRRUBBUCard() {

        homePage.setOldObjectType(locationTypeSite);
        new OldInventoryViewPage(driver)
                .filterObject("Name", locationName)
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .expandTreeToCell(locationTypeSite, locationName, gNodeBNameForEdit, cell5GNameForEdit)
                .selectTab("Hosting")
                .selectRowByAttributeValueWithLabel("Hosting Component", Constants.UBBPg3_CARD_MODEL)
                .removeObject();
        new CellSiteConfigurationPage(driver)
                .selectRowByAttributeValueWithLabel("Hosting Resource", bbuDeviceNameForEdit)
                .removeObject();
        new CellSiteConfigurationPage(driver)
                .selectRowByAttributeValueWithLabel("Hosting Resource", rruDeviceNameForEdit)
                .removeObject();
        SystemMessageInterface systemMessageItem = SystemMessageContainer.create(driver, webDriverWait);
        systemMessageItem.waitForMessageDisappear();
        Assert.assertTrue(new CellSiteConfigurationPage(driver).hasNoData());
    }

    @Test
    @Description("The user creates Host Relation between gNodeB and AAU, RRU, BBU and BBU card in Cell Site Configuration and checks if new rows are displayed in Hosting table")
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

    //TODO add HR creation by API
    @Test
    @Description("The user removes Host Relation between gNodeB and AAU, RRU, BBU and BBU card in Cell Site Configuration and checks if new rows are disappeared in Hosting table")
    public void tSRAN34RemoveHostRelationBetweenGNodeBAndAAURRUBBUCard() {

        homePage.setOldObjectType(locationTypeSite);
        new OldInventoryViewPage(driver)
                .filterObject("Name", locationName)
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .expandTreeToBaseStation(locationTypeSite, locationName, gNodeBNameForEdit)
                .selectTab("Hosting")
                .selectRowByAttributeValueWithLabel("Hosting Component", Constants.UBBPg3_CARD_MODEL)
                .removeObject();
        new CellSiteConfigurationPage(driver)
                .selectRowByAttributeValueWithLabel("Hosting Resource", bbuDeviceNameForEdit)
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
        Assert.assertTrue(new CellSiteConfigurationPage(driver).getValueByRowNumber("Hosting Resource", 0).contains(aauAAU5614NameForEdit));
    }

    //TODO add HR creation by API
    @Test
    @Description("The user creates Host Relation between Cell 5G and AAU in Cell Site Configuration and checks if new row is displayed in Hosting table")
    public void tSRAN36RemoveHostRelationBetweenCell5GAndAAU() {

        homePage.setOldObjectType(locationTypeSite);
        new OldInventoryViewPage(driver)
                .filterObject("Name", locationName)
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .expandTreeToCell(locationTypeSite, locationName, gNodeBNameForEdit, cell5GNameForEdit)
                .selectTab("Hosting")
                .selectRowByAttributeValueWithLabel("Hosting Resource", aauAAU5614NameForEdit)
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
        Assert.assertTrue(objectsList.isNoData());
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
        Assert.assertTrue(objectsList.isNoData());
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

    @Test
    @Description("The user searches Radio Remote Head in Global Search, then removes the RRU device in Cell Site Configuration checks if the description is updated in Device table")
    public void tSRAN49RemoveRRU() {

        homePage.searchInGlobalSearch(rruDeviceNameForEdit)
                .expandShowOnAndChooseView(rruDeviceNameForEdit, "NAVIGATION", "Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .expandTreeToLocation(locationTypeSite, locationName)
                .selectTab("Devices")
                .filterObject("Name", rruDeviceNameForEdit)
                .removeObject();
        SystemMessageInterface systemMessageItem = SystemMessageContainer.create(driver, webDriverWait);
        systemMessageItem.waitForMessageDisappear();
        homePage.searchInGlobalSearch(rruDeviceNameForEdit);
        CommonList objectsList = new GlobalSearchPage(driver).getResultsList();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertTrue(objectsList.isNoData());
    }

    @Test
    @Description("The user searches Base Band Unit in Global Search, then removes the BBU device in Cell Site Configuration checks if the description is updated in Device table")
    public void tSRAN50RemoveBBU() {

        homePage.searchInGlobalSearch(bbuDeviceNameForEdit)
                .expandShowOnAndChooseView(bbuDeviceNameForEdit, "NAVIGATION", "Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .expandTreeToLocation(locationTypeSite, locationName)
                .selectTab("Devices")
                .filterObject("Name", bbuDeviceNameForEdit)
                .removeObject();
        SystemMessageInterface systemMessageItem = SystemMessageContainer.create(driver, webDriverWait);
        systemMessageItem.waitForMessageDisappear();
        homePage.searchInGlobalSearch(bbuDeviceNameForEdit);
        CommonList objectsList = new GlobalSearchPage(driver).getResultsList();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertTrue(objectsList.isNoData());
    }

    @Test
    @Description("The user searches Active Antenna Unit in Global Search, then removes the AAU device in Cell Site Configuration checks if the description is updated in Device table")
    public void tSRAN51RemoveRANAntennaWithArrays() {

        homePage.searchInGlobalSearch(antennaAHP4517R7v06NameForEdit)
                .expandShowOnAndChooseView(antennaAHP4517R7v06NameForEdit, "NAVIGATION", "Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .expandTreeToLocation(locationTypeSite, locationName)
                .selectTab("Devices")
                .filterObject("Name", antennaAHP4517R7v06NameForEdit)
                .removeObject();
        SystemMessageInterface systemMessageItem = SystemMessageContainer.create(driver, webDriverWait);
        systemMessageItem.waitForMessageDisappear();
        homePage.searchInGlobalSearch(antennaAHP4517R7v06NameForEdit);
        CommonList objectsList = new GlobalSearchPage(driver).getResultsList();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertTrue(objectsList.isNoData());
    }

    @Test
    @Description("The user searches Active Antenna Unit in Global Search, then removes the AAU device in Cell Site Configuration checks if the description is updated in Device table")
    public void tSRAN52RemoveAAU() {

        homePage.searchInGlobalSearch(aauAAU5614NameForEdit)
                .expandShowOnAndChooseView(aauAAU5614NameForEdit, "NAVIGATION", "Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .expandTreeToLocation(locationTypeSite, locationName)
                .selectTab("Devices")
                .filterObject("Name", aauAAU5614NameForEdit)
                .removeObject();
        SystemMessageInterface systemMessageItem = SystemMessageContainer.create(driver, webDriverWait);
        systemMessageItem.waitForMessageDisappear();
        homePage.searchInGlobalSearch(aauAAU5614NameForEdit);
        CommonList objectsList = new GlobalSearchPage(driver).getResultsList();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertTrue(objectsList.isNoData());
    }

}

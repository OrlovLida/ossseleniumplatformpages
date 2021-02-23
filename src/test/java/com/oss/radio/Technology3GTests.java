package com.oss.radio;

import com.oss.BaseTestCase;
import com.oss.framework.alerts.SystemMessageContainer;
import com.oss.framework.alerts.SystemMessageInterface;
import com.oss.framework.listwidget.CommonList;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.platform.GlobalSearchPage;
import com.oss.pages.platform.OldInventoryViewPage;
import com.oss.pages.radio.Cell3GWizardPage;
import com.oss.pages.radio.CellSiteConfigurationPage;
import com.oss.pages.radio.NodeBWizardPage;
import com.oss.repositories.AddressRepository;
import com.oss.repositories.LocationInventoryRepository;
import com.oss.repositories.PhysicalInventoryRepository;
import com.oss.repositories.Radio3gRepository;
import com.oss.services.ResourceCatalogClient;
import com.oss.untils.Constants;
import com.oss.untils.Environment;
import com.oss.utils.RandomGenerator;
import com.oss.utils.TestListener;
import io.qameta.allure.Description;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
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
    private static Long nodeBId;
    private static final String nodeBNameForDelete = "NodeBForDeleteSeleniumTests" + (int) (Math.random() * 10000);
    private static final String nodeBNameForEdit = "NodeBForEditSeleniumTests" + (int) (Math.random() * 10000);
    private static final String cell3GId1 = RandomGenerator.generateRandomCell3GId();
    private static final String cell3GNameForDelete = "Cell3GForDeleteSeleniumTests" + (int) (Math.random() * 10000);
    private static final String cell3GId2 = RandomGenerator.generateRandomCell3GId();
    private static final String cell3GNameForEdit = "NodeBForEditSeleniumTests" + (int) (Math.random() * 10000);
    private static Long deviceModelId;
    private static Long cardModelId;
    private static final String rruDeviceNameForEdit = "RRUForHostRelationSeleniumTests" + (int) (Math.random() * 10000);
    private static final String bbuDeviceNameForEdit = "BBUWithCardForHostRelationSeleniumTests" + (int) (Math.random() * 10000);
    private static final String antennaAHP4517R7v06NameForEdit = "RANAntennaForHostRelationSeleniumTests" + (int) (Math.random() * 10000);
    private static final String aauAAU5614NameForEdit = "AAUForHostRelationSeleniumTests" + (int) (Math.random() * 10000);
    private static final String objectTypeNodeB = "NodeB";
    private static final String objectTypeCell3G = "Cell 3G";
    private static final String locationTypeSite = "Site";
    private static final String description = "Selenium Test";
    private static final String carrier3G = "3GSeleniumCarrier";
    private static final String MCC = "234";
    private static final String MNC = "20";

    @BeforeClass
    public void createTestData() {
        getOrCreateAddress();
        createPhysicalLocation();
        createNodeB(nodeBNameForDelete, rncNameForDelete);
        createNodeB(nodeBNameForEdit, rncNameForEdit);
        createCell3G(cell3GNameForDelete, cell3GId1);
        createCell3G(cell3GNameForEdit, cell3GId2);
        createDevice(Constants.RRU5501_MODEL, rruDeviceNameForEdit, Constants.DEVICE_MODEL_TYPE);
        createDeviceWithCard(Constants.BBU5900_MODEL, bbuDeviceNameForEdit, Constants.DEVICE_MODEL_TYPE, Constants.UBBPg3_CARD_MODEL,
                "0", Constants.CARD_MODEL_TYPE);
        createDevice(Constants.AHP4517R7v06ANTENNA_MODEL, antennaAHP4517R7v06NameForEdit, Constants.ANTENNA_MODEL_TYPE);
        createDevice(Constants.AAU5614ANTENNA_MODEL, aauAAU5614NameForEdit, Constants.ANTENNA_MODEL_TYPE);
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
}

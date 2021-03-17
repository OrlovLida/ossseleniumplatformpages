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
import com.oss.pages.radio.AntennaArrayWizardPage;
import com.oss.pages.radio.CellSiteConfigurationPage;
import com.oss.pages.radio.RanAntennaWizardPage;
import com.oss.repositories.AddressRepository;
import com.oss.repositories.LocationInventoryRepository;
import com.oss.repositories.PhysicalInventoryRepository;
import com.oss.services.ResourceCatalogClient;
import com.oss.untils.Constants;
import com.oss.untils.Environment;
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
public class RadioDevicesTests extends BaseTestCase {

    private Environment env = Environment.getInstance();

    private static String locationId;
    private static final String locationName = "LocationSeleniumTests" + (int) (Math.random() * 10000);
    private static Long addressId;
    private static final String countryName = "CountrySeleniumTests";
    private static final String postalCodeName = "PostalCodeSeleniumTests";
    private static final String regionName = "RegionSeleniumTests";
    private static final String districtName = "DistrictSeleniumTests";
    private static final String cityName = "CitySeleniumTests";
    private static Long deviceModelId;
    private static Long cardModelId;
    private static final String rruDeviceNameForDelete = "RRUForDeleteSeleniumTests" + (int) (Math.random() * 10000);
    private static final String rruDeviceNameForEdit = "RRUForEditSeleniumTests" + (int) (Math.random() * 10000);
    private static final String bbuDeviceNameForDelete = "BBUForDeleteSeleniumTests" + (int) (Math.random() * 10000);
    private static final String bbuDeviceNameForEdit = "BBUForEditSeleniumTests" + (int) (Math.random() * 10000);
    private static final String antennaAHP4517R7v06NameForDelete = "RANAntennaForDeleteSeleniumTests" + (int) (Math.random() * 10000);
    private static final String antennaAHP4517R7v06NameForEdit = "RANAntennaForEditSeleniumTests" + (int) (Math.random() * 10000);
    private static final String aauAAU5614NameForDelete = "AAUForDeleteSeleniumTests" + (int) (Math.random() * 10000);
    private static final String aauAAU5614NameForEdit = "AAUForEditSeleniumTests" + (int) (Math.random() * 10000);
    private static final String objectTypeDevice = "Physical Device";
    private static final String locationTypeSite = "Site";
    private static final String objectTypeRANAntenna = "RAN Antenna";
    private static final String description = "Selenium Test";

    @BeforeClass
    public void createTestData() {
        getOrCreateAddress();
        createPhysicalLocation();
        createDevice(Constants.RRU5501_MODEL, rruDeviceNameForDelete, Constants.DEVICE_MODEL_TYPE);
        createDevice(Constants.RRU5501_MODEL, rruDeviceNameForEdit, Constants.DEVICE_MODEL_TYPE);
        createDevice(Constants.BBU5900_MODEL, bbuDeviceNameForDelete, Constants.DEVICE_MODEL_TYPE);
        createDeviceWithCard(Constants.BBU5900_MODEL, bbuDeviceNameForEdit, Constants.DEVICE_MODEL_TYPE, Constants.UBBPg3_CARD_MODEL,
                "0", Constants.CARD_MODEL_TYPE);
        createDevice(Constants.AHP4517R7v06ANTENNA_MODEL, antennaAHP4517R7v06NameForDelete, Constants.ANTENNA_MODEL_TYPE);
        createDevice(Constants.AHP4517R7v06ANTENNA_MODEL, antennaAHP4517R7v06NameForEdit, Constants.ANTENNA_MODEL_TYPE);
        createDevice(Constants.AAU5614ANTENNA_MODEL, aauAAU5614NameForDelete, Constants.ANTENNA_MODEL_TYPE);
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
    @Description("The user creates Radio Remote Unit in Cell Site Configuration and checks the message about successful creation")
    public void tSRAN14CreateRRU() {
        String randomDeviceName = "RRUForCreateSeleniumTests" + (int) (Math.random() * 10000);

        homePage.setOldObjectType(locationTypeSite);
        new OldInventoryViewPage(driver)
                .filterObject("Name", locationName)
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .selectTab("Devices")
                .clickPlusIconAndSelectOption("Create Device");
        new DeviceWizardPage(driver)
                .createDevice(Constants.RRU5301_MODEL, randomDeviceName, locationName);
        Assert.assertTrue(SystemMessageContainer.create(driver, webDriverWait)
                .getMessages().get(0).getText().contains("Device " + randomDeviceName + " has been created successfully"));
    }

    @Test
    @Description("The user searches Radio Remote Head in Inventory View, then edits the RRU device in Cell Site Configuration and checks if the description is updated in Device table")
    public void tSRAN15ModifyRRU() {

        homePage.setOldObjectType(objectTypeDevice);
        new OldInventoryViewPage(driver)
                .filterObject("Name", rruDeviceNameForEdit)
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .expandTreeToLocation(locationTypeSite, locationName)
                .selectTab("Devices")
                .filterObject("Name", rruDeviceNameForEdit)
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
        String randomDeviceName = "BBUForCreateSeleniumTests" + (int) (Math.random() * 10000);

        homePage.setOldObjectType(locationTypeSite);
        new OldInventoryViewPage(driver)
                .filterObject("Name", locationName)
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .selectTab("Devices")
                .clickPlusIconAndSelectOption("Create Device");
        new DeviceWizardPage(driver)
                .createDevice(Constants.BBU5900_MODEL, randomDeviceName, locationName);
        Assert.assertTrue(SystemMessageContainer.create(driver, webDriverWait)
                .getMessages().get(0).getText().contains("Device " + randomDeviceName + " has been created successfully"));
    }

    @Test
    @Description("The user searches Base Band Unit in Inventory View, then edits the BBU device in Cell Site Configuration and checks if the description is updated in Device table")
    public void tSRAN17ModifyBBU() {

        homePage.setOldObjectType(objectTypeDevice);
        new OldInventoryViewPage(driver)
                .filterObject("Name", bbuDeviceNameForEdit)
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .expandTreeToLocation(locationTypeSite, locationName)
                .selectTab("Devices")
                .filterObject("Name", bbuDeviceNameForEdit)
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
        String randomRANAntennaName = "RANAntennaForCreateSeleniumTests" + (int) (Math.random() * 10000);

        homePage.setOldObjectType(locationTypeSite);
        new OldInventoryViewPage(driver)
                .filterObject("Name", locationName)
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .selectTab("Devices")
                .clickPlusIconAndSelectOption("Create RAN Antenna");
        new RanAntennaWizardPage(driver)
                .createRANAntenna(Constants.AHP4517R7v06ANTENNA_MODEL, randomRANAntennaName, locationName);
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
                .filterObject("Name (Antenna)", antennaAHP4517R7v06NameForEdit)
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .expandTreeToLocation(locationTypeSite, locationName)
                .selectTab("Devices")
                .filterObject("Name", antennaAHP4517R7v06NameForEdit)
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
        String randomRANAntennaName = "AAUForCreateSeleniumTests" + (int) (Math.random() * 10000);

        homePage.setOldObjectType(locationTypeSite);
        new OldInventoryViewPage(driver)
                .filterObject("Name", locationName)
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .selectTab("Devices")
                .clickPlusIconAndSelectOption("Create RAN Antenna");
        new RanAntennaWizardPage(driver)
                .createRANAntenna(Constants.AAU5614ANTENNA_MODEL, randomRANAntennaName, locationName);
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
                .filterObject("Name (Antenna)", aauAAU5614NameForEdit)
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .expandTreeToLocation(locationTypeSite, locationName)
                .selectTab("Devices")
                .filterObject("Name", aauAAU5614NameForEdit)
                .clickEditIcon();
        new RanAntennaWizardPage(driver)
                .setDescription(description);
        new RanAntennaWizardPage(driver)
                .clickAccept();
        Assert.assertTrue(new CellSiteConfigurationPage(driver).getValueByRowNumber("Description", 0).contains(description));
    }

    @Test
    @Description("The user searches Radio Remote Head in Global Search, then removes the RRU device in Cell Site Configuration checks if the description is updated in Device table")
    public void tSRAN49RemoveRRU() {

        homePage.searchInGlobalSearch(rruDeviceNameForDelete)
                .expandShowOnAndChooseView(rruDeviceNameForDelete, "NAVIGATION", "Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .expandTreeToLocation(locationTypeSite, locationName)
                .selectTab("Devices")
                .filterObject("Name", rruDeviceNameForDelete)
                .removeObject();
        SystemMessageInterface systemMessageItem = SystemMessageContainer.create(driver, webDriverWait);
        systemMessageItem.waitForMessageDisappear();
        homePage.searchInGlobalSearch(rruDeviceNameForDelete);
        CommonList objectsList = new GlobalSearchPage(driver).getResultsList();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertTrue(objectsList.isNoData());
    }

    @Test
    @Description("The user searches Base Band Unit in Global Search, then removes the BBU device in Cell Site Configuration checks if the description is updated in Device table")
    public void tSRAN50RemoveBBU() {

        homePage.searchInGlobalSearch(bbuDeviceNameForDelete)
                .expandShowOnAndChooseView(bbuDeviceNameForDelete, "NAVIGATION", "Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .expandTreeToLocation(locationTypeSite, locationName)
                .selectTab("Devices")
                .filterObject("Name", bbuDeviceNameForDelete)
                .removeObject();
        SystemMessageInterface systemMessageItem = SystemMessageContainer.create(driver, webDriverWait);
        systemMessageItem.waitForMessageDisappear();
        homePage.searchInGlobalSearch(bbuDeviceNameForDelete);
        CommonList objectsList = new GlobalSearchPage(driver).getResultsList();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertTrue(objectsList.isNoData());
    }

    @Test
    @Description("The user searches Active Antenna Unit in Global Search, then removes the AAU device in Cell Site Configuration checks if the description is updated in Device table")
    public void tSRAN51RemoveRANAntennaWithArrays() {

        homePage.searchInGlobalSearch(antennaAHP4517R7v06NameForDelete)
                .expandShowOnAndChooseView(antennaAHP4517R7v06NameForDelete, "NAVIGATION", "Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .expandTreeToLocation(locationTypeSite, locationName)
                .selectTab("Devices")
                .filterObject("Name", antennaAHP4517R7v06NameForDelete)
                .removeObject();
        SystemMessageInterface systemMessageItem = SystemMessageContainer.create(driver, webDriverWait);
        systemMessageItem.waitForMessageDisappear();
        homePage.searchInGlobalSearch(antennaAHP4517R7v06NameForDelete);
        CommonList objectsList = new GlobalSearchPage(driver).getResultsList();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertTrue(objectsList.isNoData());
    }

    @Test
    @Description("The user searches Active Antenna Unit in Global Search, then removes the AAU device in Cell Site Configuration checks if the description is updated in Device table")
    public void tSRAN52RemoveAAU() {

        homePage.searchInGlobalSearch(aauAAU5614NameForDelete)
                .expandShowOnAndChooseView(aauAAU5614NameForDelete, "NAVIGATION", "Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .expandTreeToLocation(locationTypeSite, locationName)
                .selectTab("Devices")
                .filterObject("Name", aauAAU5614NameForDelete)
                .removeObject();
        SystemMessageInterface systemMessageItem = SystemMessageContainer.create(driver, webDriverWait);
        systemMessageItem.waitForMessageDisappear();
        homePage.searchInGlobalSearch(aauAAU5614NameForDelete);
        CommonList objectsList = new GlobalSearchPage(driver).getResultsList();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertTrue(objectsList.isNoData());
    }

}
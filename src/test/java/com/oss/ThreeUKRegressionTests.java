package com.oss;

import com.oss.pages.radio.*;
import com.oss.repositories.*;
import com.oss.services.ResourceCatalogClient;
import com.oss.untils.Constants;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.oss.framework.alerts.SystemMessageContainer;
import com.oss.framework.alerts.SystemMessageInterface;
import com.oss.framework.listwidget.CommonList;
import com.oss.framework.prompts.ConfirmationBox;
import com.oss.framework.prompts.ConfirmationBoxInterface;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.tablewidget.OldTable;
import com.oss.framework.widgets.tabswidget.TabWindowWidget;
import com.oss.framework.widgets.tabswidget.TabsInterface;
import com.oss.pages.physical.DeviceWizardPage;
import com.oss.pages.physical.LocationOverviewPage;
import com.oss.pages.physical.LocationOverviewPage.TabName;
import com.oss.pages.physical.LocationWizardPage;
import com.oss.pages.platform.GlobalSearchPage;
import com.oss.pages.platform.HomePage;
import com.oss.pages.platform.OldInventoryViewPage;
import com.oss.untils.Environment;
import com.oss.utils.RandomGenerator;
import com.oss.utils.TestListener;

import io.qameta.allure.Description;

/**
 * @author Milena Miętkiewicz
 */

@Listeners({TestListener.class})
public class ThreeUKRegressionTests extends BaseTestCase {

    private Environment env = Environment.getInstance();

    String locationId;
    String locationName = "SiteSeleniumTests";
    Long addressId;
    String countryId;
    String countryName = "CountrySeleniumTests";
    String postalCodeName = "PostalCodeSeleniumTests";
    String cityName = "CitySeleniumTests";
    String subLocationSiteName = RandomGenerator.generateRandomName();
    String MCC = "234";
    String MNC = "20";
    Long eNodeBId;
    String eNodeBNameForCreate1 = RandomGenerator.generateRandomName();
    String eNodeBNameForCreate2 = RandomGenerator.generateRandomName();
    String cell4GIdForCreate1 = RandomGenerator.generateRandomCell4GId();
    String cell4GNameForCreate1 = RandomGenerator.generateRandomName();
    String cell4GIdForCreate2 = RandomGenerator.generateRandomCell4GId();
    String cell4GNameForCreate2 = RandomGenerator.generateRandomName();
    Long gNodeBId;
    String gNodeBNameForCreate1 = RandomGenerator.generateRandomName();
    String gNodeBNameForCreate2 = RandomGenerator.generateRandomName();
    String cell5GIdForCreate1 = RandomGenerator.generateRandomCell5GId();
    String cell5GNameForCreate1 = RandomGenerator.generateRandomName();
    String cell5GIdForCreate2 = RandomGenerator.generateRandomCell5GId();
    String cell5GNameForCreate2 = RandomGenerator.generateRandomName();
    Long rncId;
    String rncNameForCreate1 = RandomGenerator.generateRandomName();
    String rncNameForCreate2 = RandomGenerator.generateRandomName();
    Long nodeBId;
    String nodeBNameForCreate1 = RandomGenerator.generateRandomName();
    String nodeBNameForCreate2 = RandomGenerator.generateRandomName();
    String cell3GIdForCreate1 = RandomGenerator.generateRandomCell3GId();
    String cell3GNameForCreate1 = RandomGenerator.generateRandomName();
    String cell3GIdForCreate2 = RandomGenerator.generateRandomCell3GId();
    String cell3GNameForCreate2 = RandomGenerator.generateRandomName();
    Long deviceModelId;
    String rruDeviceNameForCreate1 = RandomGenerator.generateRandomName();
    String rruDeviceNameForCreate2 = RandomGenerator.generateRandomName();
    String bbuDeviceNameForCreate1 = RandomGenerator.generateRandomName();
    String bbuDeviceNameForCreate2 = RandomGenerator.generateRandomName();
    String antennaAHP4517R7v06NameForCreate1 = RandomGenerator.generateRandomName();
    String antennaAHP4517R7v06NameForCreate2 = RandomGenerator.generateRandomName();
    String aauAAU5614NameForCreate1 = RandomGenerator.generateRandomName();
    String aauAAU5614NameForCreate2 = RandomGenerator.generateRandomName();
    String objectTypeLocation = "Location";
    String objectTypeDevice = "Physical Device";
    String objectTypeNodeB = "NodeB";
    String objectTypeENodeB = "eNodeB";
    String objectTypeCell3G = "Cell 3G";
    String objectTypeCell4G = "Cell 4G";
    String objectTypeCell5G = "Cell 5G";
    String locationTypeSite = "Site";
    String objectTypeRANAntenna = "RAN Antenna";
    String deviceBBUModel = "HUAWEI Technology Co.,Ltd BBU5900";
    String deviceRRUModel = "HUAWEI Technology Co.,Ltd RRU5301";
    String ranAntennaModel = "HUAWEI Technology Co.,Ltd AHP4517R7v06";
    String aauModel = "HUAWEI Technology Co.,Ltd AAU5614";
    String nodeBModel = "HUAWEI Technology Co.,Ltd NodeB";
    String eNodeBModel = "HUAWEI Technology Co.,Ltd eNodeB";
    String gNodeBModel = "HUAWEI Technology Co.,Ltd gNodeB";
    String description = "Selenium Test";
    String mccMncPrimary = "3UK [mcc: 234, mnc: 20]";
    String carrier4G = "L800-B20-5";
    String carrier5G = "NR3600-n78-140";
    String carrier3G = "3GSeleniumCarrier";

    @BeforeClass
    public void createTestData() {
        getOrCreateAddressItemsAndAddress();
        getOrCreatePhysicalLocation();
        createSubLocation();
        createENodeB(eNodeBNameForCreate1);
        createENodeB(eNodeBNameForCreate2);
        createCell4G(cell4GNameForCreate1, cell4GIdForCreate1);
        createCell4G(cell4GNameForCreate2, cell4GIdForCreate2);
        createGNodeB(gNodeBNameForCreate1);
        createGNodeB(gNodeBNameForCreate2);
        createCell5G(cell5GNameForCreate1, cell5GIdForCreate1);
        createCell5G(cell5GNameForCreate2, cell5GIdForCreate2);
        createNodeB(nodeBNameForCreate1, rncNameForCreate1);
        createNodeB(nodeBNameForCreate2, rncNameForCreate2);
        createCell3G(cell3GNameForCreate1, cell3GIdForCreate1);
        createCell3G(cell3GNameForCreate2, cell3GIdForCreate2);
        createDevice(Constants.RRU5501_MODEL, rruDeviceNameForCreate1, Constants.DEVICE_MODEL_TYPE);
        createDevice(Constants.RRU5501_MODEL, rruDeviceNameForCreate2, Constants.DEVICE_MODEL_TYPE);
        createDevice(Constants.BBU5900_MODEL, bbuDeviceNameForCreate1, Constants.DEVICE_MODEL_TYPE);
        createDevice(Constants.BBU5900_MODEL, bbuDeviceNameForCreate2, Constants.DEVICE_MODEL_TYPE);
        createDevice(Constants.AHP4517R7v06ANTENNA_MODEL, antennaAHP4517R7v06NameForCreate1, Constants.ANTENNA_MODEL_TYPE);
        createDevice(Constants.AHP4517R7v06ANTENNA_MODEL, antennaAHP4517R7v06NameForCreate2, Constants.ANTENNA_MODEL_TYPE);
        createDevice(Constants.AAU5614ANTENNA_MODEL, aauAAU5614NameForCreate1, Constants.ANTENNA_MODEL_TYPE);
        createDevice(Constants.AAU5614ANTENNA_MODEL, aauAAU5614NameForCreate2, Constants.ANTENNA_MODEL_TYPE);
    }

    private void getOrCreateAddressItemsAndAddress() {
        AddressRepository addressRepository = new AddressRepository(env);
        countryId = addressRepository.getOrCreateCountry(countryName);
        addressRepository.getOrCreatePostalCode(countryId, postalCodeName);
        addressRepository.getOrCreateCity(countryId, cityName);
        addressId = addressRepository.updateOrCreateAddress(countryName, countryId, postalCodeName, cityName);
    }

    private void getOrCreatePhysicalLocation() {
        LocationInventoryRepository locationInventoryRepository = new LocationInventoryRepository(env);
        locationId = locationInventoryRepository.getOrCreateLocation(locationName, locationTypeSite, addressId);
    }

    private void createSubLocation() {
        LocationInventoryRepository locationInventoryRepository = new LocationInventoryRepository(env);
        locationInventoryRepository.createSubLocation(locationTypeSite, subLocationSiteName, addressId, Long.valueOf(locationId), locationTypeSite);
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

    @BeforeMethod
    public void goToHomePage() {
        HomePage homePage = new HomePage(driver);
        homePage.goToHomePage(driver, BASIC_URL);
    }

    @Test(groups = {"Physical tests"})
    @Description("The user creates location (Site) from left side menu and checks the message about successful creation")
    public void tS01CreateNewSiteSideMenu() {
        String randomLocationName = RandomGenerator.generateRandomName();

        homePage.chooseFromLeftSideMenu("Create Location", "Wizards", "Physical Inventory");
        new LocationWizardPage(driver)
                .createLocation(locationTypeSite, randomLocationName);
        Assert.assertTrue(SystemMessageContainer.create(driver, webDriverWait)
                .getMessages().get(0).getText().contains("Location has been created successfully"));
    }

    @Test(groups = {"Physical tests"})
    @Description("The user filters Site in Inventory View for Location and checks if properties table contains Site name")
    public void tS02BrowseLocationInInventoryView() {

        homePage.setOldObjectType(objectTypeLocation);
        new OldInventoryViewPage(driver)
                .filterObject("Name", locationName, "Location");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        OldTable propertiesTable = OldTable.createByComponentDataAttributeName(driver, webDriverWait, "properties(Location)");
        int rowNumber = propertiesTable.getRowNumber(locationName, "Property Value");
        String rowValue = propertiesTable.getCellValue(rowNumber, "Property Value");
        Assert.assertTrue(rowValue.contains(locationName));
    }

    @Test(groups = {"Physical tests"})
    @Description("The user filters Site in Inventory View for Sites and checks if properties table contains Site name")
    public void tS03BrowseSiteInInventoryView() {

        homePage.setOldObjectType(locationTypeSite);
        new OldInventoryViewPage(driver)
                .filterObject("Name", locationName, "Site");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        OldTable propertiesTable = OldTable.createByComponentDataAttributeName(driver, webDriverWait, "properties(Site)");
        int rowNumber = propertiesTable.getRowNumber(locationName, "Property Value");
        String rowValue = propertiesTable.getCellValue(rowNumber, "Property Value");
        Assert.assertTrue(rowValue.contains(locationName));
    }

    @Test(enabled = false, groups = {"Physical tests"})
    @Description("The user creates Site in Inventory View, searches the Site in Global Search, removes it in IV and checks if the Site is removed in Global Search")
    public void tS04CreateAndDeleteNewSiteInventoryView() {
        String randomLocationName = RandomGenerator.generateRandomName();
    }

    @Test(groups = {"Physical tests"})
    @Description("The user creates sublocation (Site) in Location Overview and checks if a new row is displayed in Locations table")
    public void tS05CreateSubLocationSite() {
        String randomSubLocationName = RandomGenerator.generateRandomName();

        homePage.setOldObjectType(locationTypeSite);
        new OldInventoryViewPage(driver)
                .filterObject("Name", locationName, "Site")
                .expandShowOnAndChooseView("OpenLocationOverviewAction");
        new LocationOverviewPage(driver)
                .clickButton("Create Location");
        new LocationWizardPage(driver)
                .createLocation(locationTypeSite, randomSubLocationName);
        new LocationOverviewPage(driver)
                .selectTab("Locations")
                .filterObjectInSpecificTab(TabName.LOCATIONS, "Name", randomSubLocationName);
        int rowNumber = new LocationOverviewPage(driver).getRowNumber(TabName.LOCATIONS, "Name", randomSubLocationName);
        Assert.assertTrue(new LocationOverviewPage(driver).getValueByRowNumber(TabName.LOCATIONS, "Name", rowNumber).contains(randomSubLocationName));
    }

    @Test(groups = {"Physical tests"})
    @Description("The user edits sublocation (Site) and checks if the description is updated in Locations table")
    public void tS06ModifySubLocationSite() {

        homePage.setOldObjectType(locationTypeSite);
        new OldInventoryViewPage(driver)
                .filterObject("Name", locationName, "Site")
                .expandShowOnAndChooseView("OpenLocationOverviewAction");
        new LocationOverviewPage(driver)
                .selectTab("Locations")
                .filterObjectInSpecificTab(TabName.LOCATIONS, "Name", subLocationSiteName)
                .clickButtonByLabelInSpecificTab(TabName.LOCATIONS, "Edit Location");
        new LocationWizardPage(driver)
                .setDescription(description)
                .accept();
        int rowNumber = new LocationOverviewPage(driver).getRowNumber(TabName.LOCATIONS, "Description", description);
        Assert.assertTrue(new LocationOverviewPage(driver).getValueByRowNumber(TabName.LOCATIONS, "Description", rowNumber).contains(description));
    }

    @Test(groups = {"Physical tests"})
    @Description("The user deletes sublocation (Site) and checks if the row is removed in Locations table")
    public void tS07RemoveSubLocationSite() {

        homePage.setOldObjectType(locationTypeSite);
        new OldInventoryViewPage(driver)
                .filterObject("Name", locationName, "Site")
                .expandShowOnAndChooseView("OpenLocationOverviewAction");
        new LocationOverviewPage(driver)
                .selectTab("Locations")
                .filterObjectInSpecificTab(TabName.LOCATIONS, "Name", subLocationSiteName)
                .clickButtonByLabelInSpecificTab(TabName.LOCATIONS, "Remove Location");
        ConfirmationBoxInterface confirmationBox = ConfirmationBox.create(driver, webDriverWait);
        confirmationBox.clickButtonByLabel("Delete");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        TabsInterface tabTable = TabWindowWidget.create(driver, webDriverWait);
        Assert.assertTrue(tabTable.isNoData("tableAppLocationsId"));
    }

    @Test(groups = {"Radio tests"})
    @Description("The user creates an eNodeB in Cell Site Configuration and checks the message about successful creation")
    public void tSRAN02CreateENodeB() {
        String randomENodeBName = RandomGenerator.generateRandomName();
        String randomENodeBId = RandomGenerator.generateRandomENodeBId();

        homePage.setOldObjectType(locationTypeSite);
        new OldInventoryViewPage(driver)
                .filterObject("Name", locationName, "Site")
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .clickPlusIconAndSelectOption("Create eNodeB");
        new ENodeBWizardPage(driver)
                .createENodeB(randomENodeBName, randomENodeBId, eNodeBModel, mccMncPrimary);
        Assert.assertTrue(SystemMessageContainer.create(driver, webDriverWait)
                .getMessages().get(0).getText().contains("Created eNodeB"));
    }

    @Test(groups = {"Radio tests"})
    @Description("The user searches eNodeB in Inventory View, then edits the eNodeB in Cell Site Configuration and checks if the description is updated in Base Stations table")
    public void tSRAN03ModifyENodeB() {

        homePage.setOldObjectType(objectTypeENodeB);
        new OldInventoryViewPage(driver)
                .filterObject("Name", eNodeBNameForCreate2, "ENodeB")
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .expandTreeToLocation(locationTypeSite, locationName)
                .selectTab("Base Stations")
                .filterObject("Name", eNodeBNameForCreate2)
                .clickEditIcon();
        new ENodeBWizardPage(driver)
                .setDescription(description)
                .accept();
        new CellSiteConfigurationPage(driver).getTabTable();
        OldTable tabTable = new CellSiteConfigurationPage(driver).getTabTable();
        int rowNumber = tabTable.getRowNumber(description, "Description");
        String rowValue = tabTable.getCellValue(rowNumber, "Description");
        Assert.assertTrue(rowValue.contains(description));
    }

    @Test(groups = {"Radio tests"})
    @Description("The user creates a Cell 4G in Cell Site Configuration and checks the message about successful creation")
    public void tSRAN04CreateCell4G() {
        String randomCell4GName = RandomGenerator.generateRandomName();
        String randomCell4GId = RandomGenerator.generateRandomCell4GId();

        homePage.setOldObjectType(locationTypeSite);
        new OldInventoryViewPage(driver)
                .filterObject("Name", locationName, "Site")
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .selectTab("Cells")
                .clickPlusIconAndSelectOption("Create Cell 4G");
        new Cell4GWizardPage(driver)
                .createCell4G(randomCell4GName, eNodeBNameForCreate2, randomCell4GId, carrier4G);
        Assert.assertTrue(SystemMessageContainer.create(driver, webDriverWait)
                .getMessages().get(0).getText().contains("Created Cell 4G"));
    }

    @Test(groups = {"Radio tests"})
    @Description("The user searches Cell 4G in Inventory View, then edits a Cell 4G in Cell Site Configuration and checks if the description is updated in Cells table")
    public void tSRAN05ModifyCell4G() {

        homePage.setOldObjectType(objectTypeCell4G);
        new OldInventoryViewPage(driver)
                .filterObject("Cell Name", cell4GNameForCreate2, "Cell4G")
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .expandTreeToBaseStation(locationTypeSite, locationName, eNodeBNameForCreate2)
                .selectTab("Cells")
                .filterObject("Name", cell4GNameForCreate2)
                .clickEditIcon();
        new Cell4GWizardPage(driver)
                .setDescription(description)
                .accept();
        new CellSiteConfigurationPage(driver).getTabTable();
        OldTable tabTable = new CellSiteConfigurationPage(driver).getTabTable();
        int rowNumber = tabTable.getRowNumber(description, "Description");
        String rowValue = tabTable.getCellValue(rowNumber, "Description");
        Assert.assertTrue(rowValue.contains(description));
    }

    @Test(groups = {"Radio tests"})
    @Description("The user creates an gNodeB in Cell Site Configuration and checks the message about successful creation")
    public void tSRAN06CreateGNodeB() {
        String randomGNodeBName = RandomGenerator.generateRandomName();
        String randomGNodeBId = RandomGenerator.generateRandomGNodeBId();

        homePage.setOldObjectType(locationTypeSite);
        new OldInventoryViewPage(driver)
                .filterObject("Name", locationName, "Site")
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .clickPlusIconAndSelectOption("Create gNodeB");
        new GNodeBWizardPage(driver)
                .createGNodeB(randomGNodeBName, randomGNodeBId, gNodeBModel, mccMncPrimary);
        Assert.assertTrue(SystemMessageContainer.create(driver, webDriverWait)
                .getMessages().get(0).getText().contains("Created gNodeB"));
    }

    @Test(groups = {"Radio tests"})
    @Description("The user searches Site in Inventory View, then edits the gNodeB in Cell Site Configuration and checks if the description is updated in Base Stations table")
    public void tSRAN07ModifyGNodeB() {

        homePage.setOldObjectType(locationTypeSite);
        new OldInventoryViewPage(driver)
                .filterObject("Name", locationName, "Site")
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .expandTreeToLocation(locationTypeSite, locationName)
                .selectTab("Base Stations")
                .filterObject("Name", gNodeBNameForCreate2)
                .clickEditIcon();
        new GNodeBWizardPage(driver)
                .setDescription(description)
                .accept();
        new CellSiteConfigurationPage(driver).getTabTable();
        OldTable tabTable = new CellSiteConfigurationPage(driver).getTabTable();
        int rowNumber = tabTable.getRowNumber(description, "Description");
        String rowValue = tabTable.getCellValue(rowNumber, "Description");
        Assert.assertTrue(rowValue.contains(description));
    }

    @Test(groups = {"Radio tests"})
    @Description("The user creates a Cell 5G in Cell Site Configuration and checks the message about successful creation")
    public void tSRAN08CreateCell5G() {
        String randomCell5GName = RandomGenerator.generateRandomName();
        String randomCell5GId = RandomGenerator.generateRandomCell5GId();

        homePage.setOldObjectType(locationTypeSite);
        new OldInventoryViewPage(driver)
                .filterObject("Name", locationName, "Site")
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .selectTab("Cells")
                .clickPlusIconAndSelectOption("Create Cell 5G");
        new Cell5GWizardPage(driver)
                .createCell5G(randomCell5GName, gNodeBNameForCreate2, randomCell5GId, carrier5G);
        Assert.assertTrue(SystemMessageContainer.create(driver, webDriverWait)
                .getMessages().get(0).getText().contains("Created Cell 5G"));
    }

    @Test(groups = {"Radio tests"})
    @Description("The user searches Cell 5G in Inventory View, then edits a Cell 5G in Cell Site Configuration and checks if the description is updated in Cells table")
    public void tSRAN09ModifyCell5G() {

        homePage.setOldObjectType(objectTypeCell5G);
        new OldInventoryViewPage(driver)
                .filterObject("Cell Name", cell5GNameForCreate2, "Cell5G")
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .expandTreeToBaseStation(locationTypeSite, locationName, gNodeBNameForCreate2)
                .selectTab("Cells")
                .filterObject("Name", cell5GNameForCreate2)
                .clickEditIcon();
        new Cell5GWizardPage(driver)
                .setDescription(description)
                .accept();
        new CellSiteConfigurationPage(driver).getTabTable();
        OldTable tabTable = new CellSiteConfigurationPage(driver).getTabTable();
        int rowNumber = tabTable.getRowNumber(description, "Description");
        String rowValue = tabTable.getCellValue(rowNumber, "Description");
        Assert.assertTrue(rowValue.contains(description));
    }

    @Test(groups = {"Radio tests"})
    @Description("The user creates a NodeB in Cell Site Configuration and checks the message about successful creation")
    public void tSRAN10CreateNodeB() {
        String randomNodeBName = RandomGenerator.generateRandomName();
        String randomNodeBId = RandomGenerator.generateRandomENodeBId();

        homePage.setOldObjectType(locationTypeSite);
        new OldInventoryViewPage(driver)
                .filterObject("Name", locationName, "Site")
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .clickPlusIconAndSelectOption("Create NodeB");
        new NodeBWizardPage(driver)
                .createNodeB(randomNodeBName, randomNodeBId, nodeBModel, rncNameForCreate1);
        Assert.assertTrue(SystemMessageContainer.create(driver, webDriverWait)
                .getMessages().get(0).getText().contains("NodeB was created"));
    }

    @Test(groups = {"Radio tests"})
    @Description("The user searches NodeB in Inventory View, then edits the NodeB in Cell Site Configuration and checks if the description is updated in Base Stations table")
    public void tSRAN11ModifyNodeB() {

        homePage.setOldObjectType(objectTypeNodeB);
        new OldInventoryViewPage(driver)
                .filterObject("Name", nodeBNameForCreate2, "NodeB")
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .expandTreeToLocation(locationTypeSite, locationName)
                .selectTab("Base Stations")
                .filterObject("Name", nodeBNameForCreate2)
                .clickEditIcon();
        new NodeBWizardPage(driver)
                .setDescription(description)
                .accept();
        new CellSiteConfigurationPage(driver).getTabTable();
        OldTable tabTable = new CellSiteConfigurationPage(driver).getTabTable();
        int rowNumber = tabTable.getRowNumber(description, "Description");
        String rowValue = tabTable.getCellValue(rowNumber, "Description");
        Assert.assertTrue(rowValue.contains(description));
    }

    @Test(groups = {"Radio tests"})
    @Description("The user creates a Cell 3G in Cell Site Configuration and checks the message about successful creation")
    public void tSRAN12CreateCell3G() {
        String randomCell3GName = RandomGenerator.generateRandomName();
        String randomCell3GId = RandomGenerator.generateRandomCell3GId();

        homePage.setOldObjectType(locationTypeSite);
        new OldInventoryViewPage(driver)
                .filterObject("Name", locationName, "Site")
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .selectTab("Cells")
                .clickPlusIconAndSelectOption("Create Cell 3G");
        new Cell3GWizardPage(driver)
                .createCell3G(randomCell3GName, nodeBNameForCreate2, randomCell3GId, carrier3G);
        Assert.assertTrue(SystemMessageContainer.create(driver, webDriverWait)
                .getMessages().get(0).getText().contains("Cell 3G was created"));
    }

    @Test(groups = {"Radio tests"})
    @Description("The user searches Cell 3G in Inventory View, then edits a Cell 3G in Cell Site Configuration and checks if the description is updated in Cells table")
    public void tSRAN13ModifyCell3G() {

        homePage.setOldObjectType(objectTypeCell3G);
        new OldInventoryViewPage(driver)
                .filterObject("Cell Name", cell3GNameForCreate2, "Cell3G")
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .expandTreeToBaseStation(locationTypeSite, locationName, nodeBNameForCreate2)
                .selectTab("Cells")
                .filterObject("Name", cell3GNameForCreate2)
                .clickEditIcon();
        new Cell3GWizardPage(driver)
                .setDescription(description)
                .accept();
        new CellSiteConfigurationPage(driver).getTabTable();
        OldTable tabTable = new CellSiteConfigurationPage(driver).getTabTable();
        int rowNumber = tabTable.getRowNumber(description, "Description");
        String rowValue = tabTable.getCellValue(rowNumber, "Description");
        Assert.assertTrue(rowValue.contains(description));
    }

    @Test(groups = {"Radio tests"})
    @Description("The user creates Radio Remote Unit in Cell Site Configuration and checks the message about successful creation")
    public void tSRAN14CreateRRU() {
        String randomDeviceName = RandomGenerator.generateRandomName();

        homePage.setOldObjectType(locationTypeSite);
        new OldInventoryViewPage(driver)
                .filterObject("Name", locationName, "Site")
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .selectTab("Devices")
                .clickPlusIconAndSelectOption("Create Device");
        new DeviceWizardPage(driver)
                .createDevice(deviceRRUModel, randomDeviceName, locationName);
        Assert.assertTrue(SystemMessageContainer.create(driver, webDriverWait)
                .getMessages().get(0).getText().contains("Device " + randomDeviceName + " has been created successfully"));
    }

    @Test(groups = {"Radio tests"})
    @Description("The user searches Radio Remote Head in Inventory View, then edits the RRU device in Cell Site Configuration and checks if the description is updated in Device table")
    public void tSRAN15ModifyRRU() {

        homePage.setOldObjectType(objectTypeDevice);
        new OldInventoryViewPage(driver)
                .filterObject("Name", rruDeviceNameForCreate1, "PhysicalElement")
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .expandTreeToLocation(locationTypeSite, locationName)
                .selectTab("Devices")
                .filterObject("Name", rruDeviceNameForCreate1)
                .clickEditIcon();
        new DeviceWizardPage(driver)
                .setDescription(description);
        new DeviceWizardPage(driver)
                .nextUpdateWizard();
        new DeviceWizardPage(driver)
                .acceptUpdateWizard();
        new CellSiteConfigurationPage(driver).getTabTable();
        OldTable tabTable = new CellSiteConfigurationPage(driver).getTabTable();
        int rowNumber = tabTable.getRowNumber(description, "Description");
        String rowValue = tabTable.getCellValue(rowNumber, "Description");
        Assert.assertTrue(rowValue.contains(description));
    }

    @Test(groups = {"Radio tests"})
    @Description("The user creates Base Band Unit in Cell Site Configuration and checks the message about successful creation")
    public void tSRAN16CreateBBU() {
        String randomDeviceName = RandomGenerator.generateRandomName();

        homePage.setOldObjectType(locationTypeSite);
        new OldInventoryViewPage(driver)
                .filterObject("Name", locationName, "Site")
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .selectTab("Devices")
                .clickPlusIconAndSelectOption("Create Device");
        new DeviceWizardPage(driver)
                .createDevice(deviceBBUModel, randomDeviceName, locationName);
        Assert.assertTrue(SystemMessageContainer.create(driver, webDriverWait)
                .getMessages().get(0).getText().contains("Device " + randomDeviceName + " has been created successfully"));
    }

    @Test(groups = {"Radio tests"})
    @Description("The user searches Base Band Unit in Inventory View, then edits the BBU device in Cell Site Configuration and checks if the description is updated in Device table")
    public void tSRAN17ModifyBBU() {

        homePage.setOldObjectType(objectTypeDevice);
        new OldInventoryViewPage(driver)
                .filterObject("Name", bbuDeviceNameForCreate1, "PhysicalElement")
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .expandTreeToLocation(locationTypeSite, locationName)
                .selectTab("Devices")
                .filterObject("Name", bbuDeviceNameForCreate1)
                .clickEditIcon();
        new DeviceWizardPage(driver)
                .setDescription(description);
        new DeviceWizardPage(driver)
                .nextUpdateWizard();
        new DeviceWizardPage(driver)
                .acceptUpdateWizard();
        new CellSiteConfigurationPage(driver).getTabTable();
        OldTable tabTable = new CellSiteConfigurationPage(driver).getTabTable();
        int rowNumber = tabTable.getRowNumber(description, "Description");
        String rowValue = tabTable.getCellValue(rowNumber, "Description");
        Assert.assertTrue(rowValue.contains(description));
    }

    @Test(groups = {"Radio tests"})
    @Description("The user creates RAN Antenna device in Cell Site Configuration and checks the message about successful creation")
    public void tSRAN19CreateRANAntennaWithArrays() {
        String randomRANAntennaName = RandomGenerator.generateRandomName();

        homePage.setOldObjectType(locationTypeSite);
        new OldInventoryViewPage(driver)
                .filterObject("Name", locationName, "Site")
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .selectTab("Devices")
                .clickPlusIconAndSelectOption("Create RAN Antenna");
        new RanAntennaWizardPage(driver)
                .createRANAntenna(ranAntennaModel, randomRANAntennaName, locationName);
        new AntennaArrayWizardPage(driver)
                .clickAccept();
        Assert.assertTrue(SystemMessageContainer.create(driver, webDriverWait)
                .getMessages().get(0).getText().contains("Edited Antenna Array"));
    }

    @Test(groups = {"Radio tests"})
    @Description("The user searches RAN Antenna in Inventory View, then edits the antenna in Cell Site Configuration and checks if the description is updated in Device table")
    public void tSRAN20ModifyRANAntenna() {

        homePage.setOldObjectType(objectTypeRANAntenna);
        new OldInventoryViewPage(driver)
                .filterObject("Name (Antenna)", antennaAHP4517R7v06NameForCreate1, "RANAntenna")
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .expandTreeToLocation(locationTypeSite, locationName)
                .selectTab("Devices")
                .filterObject("Name", antennaAHP4517R7v06NameForCreate1)
                .clickEditIcon();
        new RanAntennaWizardPage(driver)
                .setDescription(description);
        new RanAntennaWizardPage(driver)
                .clickAccept();
        new CellSiteConfigurationPage(driver).getTabTable();
        OldTable tabTable = new CellSiteConfigurationPage(driver).getTabTable();
        int rowNumber = tabTable.getRowNumber(description, "Description");
        String rowValue = tabTable.getCellValue(rowNumber, "Description");
        Assert.assertTrue(rowValue.contains(description));
    }

    @Test(groups = {"Radio tests"})
    @Description("The user creates Active Antenna Unit device in Cell Site Configuration and checks the message about successful creation")
    public void tSRAN22CreateAAUWithArrays() {
        String randomRANAntennaName = RandomGenerator.generateRandomName();

        homePage.setOldObjectType(locationTypeSite);
        new OldInventoryViewPage(driver)
                .filterObject("Name", locationName, "Site")
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .selectTab("Devices")
                .clickPlusIconAndSelectOption("Create RAN Antenna");
        new RanAntennaWizardPage(driver)
                .createRANAntenna(aauModel, randomRANAntennaName, locationName);
        new AntennaArrayWizardPage(driver)
                .clickAccept();
        Assert.assertTrue(SystemMessageContainer.create(driver, webDriverWait)
                .getMessages().get(0).getText().contains("Edited Antenna Array"));
    }

    @Test(groups = {"Radio tests"})
    @Description("The user searches Active Antenna Unit in Inventory View, then edits the antenna in Cell Site Configuration and checks if the description is updated in Device table")
    public void tSRAN23ModifyAAU() {

        homePage.setOldObjectType(objectTypeRANAntenna);
        new OldInventoryViewPage(driver)
                .filterObject("Name (Antenna)", aauAAU5614NameForCreate1, "RANAntenna")
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .expandTreeToLocation(locationTypeSite, locationName)
                .selectTab("Devices")
                .filterObject("Name", aauAAU5614NameForCreate1)
                .clickEditIcon();
        new RanAntennaWizardPage(driver)
                .setDescription(description);
        new RanAntennaWizardPage(driver)
                .clickAccept();
        new CellSiteConfigurationPage(driver).getTabTable();
        OldTable tabTable = new CellSiteConfigurationPage(driver).getTabTable();
        int rowNumber = tabTable.getRowNumber(description, "Description");
        String rowValue = tabTable.getCellValue(rowNumber, "Description");
        Assert.assertTrue(rowValue.contains(description));
    }

    @Test(groups = {"Radio tests"})
    @Description("The user searches eNodeB in Global Search, then deletes the eNodeB in Cell Site Configuration and checks if search result in Global Search is no data")
    public void tSRAN43RemoveENodeB() {

        homePage.searchInGlobalSearch(eNodeBNameForCreate1)
                .expandShowOnAndChooseView(eNodeBNameForCreate1, "NAVIGATION", "Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .expandTreeToLocation(locationTypeSite, locationName)
                .selectTab("Base Stations")
                .filterObject("Name", eNodeBNameForCreate1)
                .removeObject();
        SystemMessageInterface systemMessageItem = SystemMessageContainer.create(driver, webDriverWait);
        systemMessageItem.waitForMessageDisappear();
        homePage.searchInGlobalSearch(eNodeBNameForCreate1);
        CommonList objectsList = new GlobalSearchPage(driver).getResultsList();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertTrue(objectsList.isNoData());
    }

    @Test(groups = {"Radio tests"})
    @Description("The user searches Cell 4G in Global Search, then deletes the Cell 4G in Cell Site Configuration and checks if search result in Global Search is no data")
    public void tSRAN44RemoveCell4G() {

        homePage.searchInGlobalSearch(cell4GNameForCreate1)
                .expandShowOnAndChooseView(cell4GNameForCreate1, "NAVIGATION", "Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .expandTreeToBaseStation(locationTypeSite, locationName, eNodeBNameForCreate2)
                .selectTab("Cells")
                .filterObject("Name", cell4GNameForCreate1)
                .removeObject();
        SystemMessageInterface systemMessageItem = SystemMessageContainer.create(driver, webDriverWait);
        systemMessageItem.waitForMessageDisappear();
        homePage.searchInGlobalSearch(cell4GNameForCreate1);
        CommonList objectsList = new GlobalSearchPage(driver).getResultsList();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertTrue(objectsList.isNoData());
    }

    @Test(groups = {"Radio tests"})
    @Description("The user searches gNodeB in Global Search, then deletes the gNodeB in Cell Site Configuration and checks if search result in Global Search is no data")
    public void tSRAN45RemoveGNodeB() {

        homePage.searchInGlobalSearch(gNodeBNameForCreate1)
                .expandShowOnAndChooseView(gNodeBNameForCreate1, "NAVIGATION", "Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .expandTreeToLocation(locationTypeSite, locationName)
                .selectTab("Base Stations")
                .filterObject("Name", gNodeBNameForCreate1)
                .removeObject();
        SystemMessageInterface systemMessageItem = SystemMessageContainer.create(driver, webDriverWait);
        systemMessageItem.waitForMessageDisappear();
        homePage.searchInGlobalSearch(gNodeBNameForCreate1);
        CommonList objectsList = new GlobalSearchPage(driver).getResultsList();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertTrue(objectsList.isNoData());
    }

    @Test(groups = {"Radio tests"})
    @Description("The user searches Cell 5G in Global Search, then deletes the Cell 5G in Cell Site Configuration and checks if search result in Global Search is no data")
    public void tSRAN46RemoveCell5G() {

        homePage.searchInGlobalSearch(cell5GNameForCreate1)
                .expandShowOnAndChooseView(cell5GNameForCreate1, "NAVIGATION", "Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .expandTreeToBaseStation(locationTypeSite, locationName, gNodeBNameForCreate2)
                .selectTab("Cells")
                .filterObject("Name", cell5GNameForCreate1)
                .removeObject();
        SystemMessageInterface systemMessageItem = SystemMessageContainer.create(driver, webDriverWait);
        systemMessageItem.waitForMessageDisappear();
        homePage.searchInGlobalSearch(cell5GNameForCreate1);
        CommonList objectsList = new GlobalSearchPage(driver).getResultsList();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertTrue(objectsList.isNoData());
    }

    @Test(groups = {"Radio tests"})
    @Description("The user searches NodeB in Global Search, then deletes the NodeB in Cell Site Configuration and checks if search result in Global Search is no data")
    public void tSRAN47RemoveNodeB() {

        homePage.searchInGlobalSearch(nodeBNameForCreate1)
                .expandShowOnAndChooseView(nodeBNameForCreate1, "NAVIGATION", "Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .expandTreeToLocation(locationTypeSite, locationName)
                .selectTab("Base Stations")
                .filterObject("Name", nodeBNameForCreate1)
                .removeObject();
        SystemMessageInterface systemMessageItem = SystemMessageContainer.create(driver, webDriverWait);
        systemMessageItem.waitForMessageDisappear();
        homePage.searchInGlobalSearch(nodeBNameForCreate1);
        CommonList objectsList = new GlobalSearchPage(driver).getResultsList();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertTrue(objectsList.isNoData());
    }

    @Test(groups = {"Radio tests"})
    @Description("The user searches Cell 3G in Global Search, then deletes the Cell 3G in Cell Site Configuration and checks if search result in Global Search is no data")
    public void tSRAN48RemoveCell3G() {

        homePage.searchInGlobalSearch(cell3GNameForCreate1)
                .expandShowOnAndChooseView(cell3GNameForCreate1, "NAVIGATION", "Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .expandTreeToBaseStation(locationTypeSite, locationName, nodeBNameForCreate2)
                .selectTab("Cells")
                .filterObject("Name", cell3GNameForCreate1)
                .removeObject();
        SystemMessageInterface systemMessageItem = SystemMessageContainer.create(driver, webDriverWait);
        systemMessageItem.waitForMessageDisappear();
        homePage.searchInGlobalSearch(cell3GNameForCreate1);
        CommonList objectsList = new GlobalSearchPage(driver).getResultsList();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertTrue(objectsList.isNoData());
    }

    @Test(groups = {"Radio tests"})
    @Description("The user searches Radio Remote Head in Global Search, then removes the RRU device in Cell Site Configuration checks if the description is updated in Device table")
    public void tSRAN49RemoveRRU() {

        homePage.searchInGlobalSearch(rruDeviceNameForCreate2)
                .expandShowOnAndChooseView(rruDeviceNameForCreate2, "NAVIGATION", "Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .expandTreeToLocation(locationTypeSite, locationName)
                .selectTab("Devices")
                .filterObject("Name", rruDeviceNameForCreate2)
                .removeObject();
        SystemMessageInterface systemMessageItem = SystemMessageContainer.create(driver, webDriverWait);
        systemMessageItem.waitForMessageDisappear();
        homePage.searchInGlobalSearch(rruDeviceNameForCreate2);
        CommonList objectsList = new GlobalSearchPage(driver).getResultsList();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertTrue(objectsList.isNoData());
    }

    @Test(groups = {"Radio tests"})
    @Description("The user searches Base Band Unit in Global Search, then removes the BBU device in Cell Site Configuration checks if the description is updated in Device table")
    public void tSRAN50RemoveBBU() {

        homePage.searchInGlobalSearch(bbuDeviceNameForCreate2)
                .expandShowOnAndChooseView(bbuDeviceNameForCreate2, "NAVIGATION", "Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .expandTreeToLocation(locationTypeSite, locationName)
                .selectTab("Devices")
                .filterObject("Name", bbuDeviceNameForCreate2)
                .removeObject();
        SystemMessageInterface systemMessageItem = SystemMessageContainer.create(driver, webDriverWait);
        systemMessageItem.waitForMessageDisappear();
        homePage.searchInGlobalSearch(bbuDeviceNameForCreate2);
        CommonList objectsList = new GlobalSearchPage(driver).getResultsList();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertTrue(objectsList.isNoData());
    }

    @Test(groups = {"Radio tests"})
    @Description("The user searches Active Antenna Unit in Global Search, then removes the AAU device in Cell Site Configuration checks if the description is updated in Device table")
    public void tSRAN51RemoveRANAntennaWithArrays() {

        homePage.searchInGlobalSearch(antennaAHP4517R7v06NameForCreate2)
                .expandShowOnAndChooseView(antennaAHP4517R7v06NameForCreate2, "NAVIGATION", "Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .expandTreeToLocation(locationTypeSite, locationName)
                .selectTab("Devices")
                .filterObject("Name", antennaAHP4517R7v06NameForCreate2)
                .removeObject();
        SystemMessageInterface systemMessageItem = SystemMessageContainer.create(driver, webDriverWait);
        systemMessageItem.waitForMessageDisappear();
        homePage.searchInGlobalSearch(antennaAHP4517R7v06NameForCreate2);
        CommonList objectsList = new GlobalSearchPage(driver).getResultsList();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertTrue(objectsList.isNoData());
    }

    @Test(groups = {"Radio tests"})
    @Description("The user searches Active Antenna Unit in Global Search, then removes the AAU device in Cell Site Configuration checks if the description is updated in Device table")
    public void tSRAN52RemoveAAU() {

        homePage.searchInGlobalSearch(aauAAU5614NameForCreate2)
                .expandShowOnAndChooseView(aauAAU5614NameForCreate2, "NAVIGATION", "Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .expandTreeToLocation(locationTypeSite, locationName)
                .selectTab("Devices")
                .filterObject("Name", aauAAU5614NameForCreate2)
                .removeObject();
        SystemMessageInterface systemMessageItem = SystemMessageContainer.create(driver, webDriverWait);
        systemMessageItem.waitForMessageDisappear();
        homePage.searchInGlobalSearch(aauAAU5614NameForCreate2);
        CommonList objectsList = new GlobalSearchPage(driver).getResultsList();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertTrue(objectsList.isNoData());
    }

}
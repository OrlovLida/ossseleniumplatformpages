package com.oss;

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
import com.oss.pages.physical.LocationWizardPage;
import com.oss.pages.platform.GlobalSearchPage;
import com.oss.pages.platform.HomePage;
import com.oss.pages.platform.OldInventoryViewPage;
import com.oss.pages.radio.*;
import com.oss.repositories.AddressRepository;
import com.oss.repositories.LocationInventoryRepository;
import com.oss.repositories.Radio4gRepository;
import com.oss.repositories.Radio5gRepository;
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
public class ThreeUKRegressionTests extends BaseTestCase {

    private Environment env = Environment.getInstance();

    String locationId;
    String locationName = "SiteSeleniumTestsNowaTestAPI";
    Long addressId;
    String countryId;
    String countryName = "CountrySeleniumTestsNowaTestAPI";
    String postalCodeName = "PostalCodeSeleniumTestsNowaTestAPI";
    String cityName = "CitySeleniumTestsNowaTestAPI";
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
    String objectTypeLocation = "Location";
    String objectTypeDevice = "Physical Device";
    String objectTypeENodeB = "eNodeB";
    String objectTypeCell4G = "Cell 4G";
    String objectTypeCell5G = "Cell 5G";
    String locationTypeSite = "Site";
    String deviceBBUModel = "HUAWEI Technology Co.,Ltd BBU5900";
    String deviceRRUModel = "HUAWEI Technology Co.,Ltd RRU5301";
    String eNodeBModel = "HUAWEI Technology Co.,Ltd eNodeB";
    String gNodeBModel = "HUAWEI Technology Co.,Ltd gNodeB";
    String description = "Selenium Test";
    String MCCMNCPrimary = "3UK [mcc: 234, mnc: 20]";
    String carrier4G = "L800-B20-5";
    String carrier5G = "NR3600-n78-140";

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
        //to do create device
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
        eNodeBId = radio4gRepository.createENodeB(eNodeBNameForCreate, Long.valueOf(locationId), MCC, MNC);
    }

    private void createCell4G(String cell4GNameForCreate, String cell4GIdForCreate) {
        Radio4gRepository radio4gRepository = new Radio4gRepository(env);
        radio4gRepository.createCell4g(cell4GNameForCreate, Integer.valueOf(cell4GIdForCreate), eNodeBId, MCC, MNC, carrier4G);
    }

    private void createGNodeB(String gNodeBNameForCreate) {
        Radio5gRepository radio5gRepository = new Radio5gRepository(env);
        gNodeBId = radio5gRepository.createGNodeB(gNodeBNameForCreate, Long.valueOf(locationId), MCC, MNC);
    }

    private void createCell5G(String cell5GNameForCreate, String cell5GIdForCreate) {
        Radio5gRepository radio5gRepository = new Radio5gRepository(env);
        radio5gRepository.createCell5g(cell5GNameForCreate, Integer.valueOf(cell5GIdForCreate), gNodeBId, MCC, MNC, carrier5G);
    }

    @BeforeMethod
    public void goToHomePage() {
        HomePage homePage = new HomePage(driver);
        homePage.goToHomePage(driver, BASIC_URL);
    }

    @Test(groups = {"Physical tests"})
    @Description("The user creates a location (Site) from left side menu and checks the message about successful creation")
    public void tS01CreateNewSiteSideMenu() {
        String randomLocationName = RandomGenerator.generateRandomName();

        homePage.chooseFromLeftSideMenu("Create Location", "Wizards", "Physical Inventory");
        new LocationWizardPage(driver)
                .createLocation(locationTypeSite, randomLocationName);
        Assert.assertTrue(SystemMessageContainer.create(driver, webDriverWait)
                .getMessages().get(0).getText().contains("Location has been created successfully"));
    }

    @Test(groups = {"Physical tests"})
    @Description("The user filters a Site in Inventory View for Location and checks if properties table contains Site name")
    public void tS02BrowseLocationInInventoryView() {

        homePage.setAndSelectObjectType(objectTypeLocation);
        new OldInventoryViewPage(driver)
                .filterObject("Name", locationName, "Location");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        OldTable propertiesTable = OldTable.createByComponentDataAttributeName(driver, webDriverWait, "properties(Location)");
        int rowNumber = propertiesTable.getRowNumber(locationName, "Property Value");
        String rowValue = propertiesTable.getValueCell(rowNumber, "Property Value");
        Assert.assertTrue(rowValue.contains(locationName));
    }

    @Test(groups = {"Physical tests"})
    @Description("The user filters a Site in Inventory View for Sites and checks if properties table contains Site name")
    public void tS03BrowseSiteInInventoryView() {

        homePage.setAndSelectObjectType(locationTypeSite);
        new OldInventoryViewPage(driver)
                .filterObject("Name", locationName, "Site");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        OldTable propertiesTable = OldTable.createByComponentDataAttributeName(driver, webDriverWait, "properties(Site)");
        int rowNumber = propertiesTable.getRowNumber(locationName, "Property Value");
        String rowValue = propertiesTable.getValueCell(rowNumber, "Property Value");
        Assert.assertTrue(rowValue.contains(locationName));
    }

    @Test(enabled = false, groups = {"Physical tests"})
    @Description("The user creates a Site in Inventory View, searches for it in Global Search, removes it in IV and checks if the Site is removed in Global Search")
    public void tS04CreateAndDeleteNewSiteInventoryView() {
        String randomLocationName = RandomGenerator.generateRandomName();
    }

    @Test(groups = {"Physical tests"})
    @Description("The user creates a sublocation Site in Location Overview and checks if a new row is displayed in Locations table")
    public void tS05CreateSubLocationSite() {
        String randomSubLocationName = RandomGenerator.generateRandomName();

        homePage.setAndSelectObjectType(locationTypeSite);
        new OldInventoryViewPage(driver)
                .filterObject("Name", locationName, "Site")
                .expandShowOnAndChooseView("OpenLocationOverviewAction");
        new LocationOverviewPage(driver)
                .clickButton("Create Location");
        new LocationWizardPage(driver)
                .createLocation(locationTypeSite, randomSubLocationName);
        new LocationOverviewPage(driver)
                .selectTab("Locations")
                .filterLocationsObject("Name", randomSubLocationName);
//        OldTable tabTable = OldTable.createByComponentDataAttributeName(driver, webDriverWait, "tableAppLocationsId");
        OldTable tabTable = new LocationOverviewPage(driver).getLocationsTabTable();
        int rowNumber = tabTable.getRowNumber(randomSubLocationName, "Name");
        String rowValue = tabTable.getValueCell(rowNumber, "Name");
        Assert.assertTrue(rowValue.contains(randomSubLocationName));
    }

    @Test(groups = {"Physical tests"})
    @Description("The user edits a sublocation Site and checks if the description is updated in Locations table")
    public void tS06ModifySubLocationSite() {

        homePage.setAndSelectObjectType(locationTypeSite);
        new OldInventoryViewPage(driver)
                .filterObject("Name", locationName, "Site")
                .expandShowOnAndChooseView("OpenLocationOverviewAction");
        new LocationOverviewPage(driver)
                .selectTab("Locations")
                .filterLocationsObject("Name", subLocationSiteName)
                .clickEditLocationIcon();
        new LocationWizardPage(driver)
                .setDescription(description)
                .accept();
//        OldTable tabTable = OldTable.createByComponentDataAttributeName(driver, webDriverWait, "tableAppLocationsId");
        OldTable tabTable = new LocationOverviewPage(driver).getLocationsTabTable();
        int rowNumber = tabTable.getRowNumber(description, "Description");
        String rowValue = tabTable.getValueCell(rowNumber, "Description");
        Assert.assertTrue(rowValue.contains(description));
    }

    @Test(groups = {"Physical tests"})
    @Description("The user deletes a sublocation Site and checks if the row is removed in Locations table")
    public void tS07RemoveSubLocationSite() {

        homePage.setAndSelectObjectType(locationTypeSite);
        new OldInventoryViewPage(driver)
                .filterObject("Name", locationName, "Site")
                .expandShowOnAndChooseView("OpenLocationOverviewAction");
        new LocationOverviewPage(driver)
                .selectTab("Locations")
                .filterLocationsObject("Name", subLocationSiteName)
                .clickRemoveLocationIcon();
        ConfirmationBoxInterface confirmationBox = ConfirmationBox.create(driver, webDriverWait);
        confirmationBox.clickButtonByLabel("Delete");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        TabsInterface tabTable = TabWindowWidget.create(driver, webDriverWait);
        Assert.assertTrue(tabTable.isNoData("tableAppLocationsId"));
    }

    @Test(groups = {"Radio tests"})
    @Description("The user creates an eNodeB in Cell Site Configuration and checks the message about successful creation")
    public void tS08CreateENodeB() {
        String randomENodeBName = RandomGenerator.generateRandomName();
        String randomENodeBId = RandomGenerator.generateRandomENodeBId();

        homePage.setAndSelectObjectType(locationTypeSite);
        new OldInventoryViewPage(driver)
                .filterObject("Name", locationName, "Site")
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .clickPlusIconAndSelectOption("Create eNodeB");
        new ENodeBWizardPage(driver)
                .createENodeB(randomENodeBName, randomENodeBId, eNodeBModel, MCCMNCPrimary);
        Assert.assertTrue(SystemMessageContainer.create(driver, webDriverWait)
                .getMessages().get(0).getText().contains("Created eNodeB"));
    }

    @Test(groups = {"Radio tests"})
    @Description("The user edits an eNodeB in Cell Site Configuration and checks if the description is updated in Base Stations table")
    public void tS09ModifyENodeB() {

        homePage.setAndSelectObjectType(objectTypeENodeB);
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
        String rowValue = tabTable.getValueCell(rowNumber, "Description");
        Assert.assertTrue(rowValue.contains(description));
    }

    @Test(groups = {"Radio tests"})
    @Description("The user deletes an eNodeB in Cell Site Configuration and checks if search result in Global Search is no data")
    public void tS10RemoveENodeB() {

        homePage.searchInGlobalSearch(eNodeBNameForCreate1);
        new GlobalSearchPage(driver)
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
    @Description("The user creates a Cell 4G in Cell Site Configuration and checks the message about successful creation")
    public void tS11CreateCell4G() {
        String randomCell4GName = RandomGenerator.generateRandomName();
        String randomCell4GId = RandomGenerator.generateRandomCell4GId();

        homePage.setAndSelectObjectType(locationTypeSite);
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
    @Description("The user edits a Cell 4G in Cell Site Configuration and checks if the description is updated in Cells table")
    public void tS12ModifyCell4G() {

        homePage.setAndSelectObjectType(objectTypeCell4G);
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
        String rowValue = tabTable.getValueCell(rowNumber, "Description");
        Assert.assertTrue(rowValue.contains(description));
    }

    @Test(groups = {"Radio tests"})
    @Description("The user deletes a Cell 4G in Cell Site Configuration and checks if search result in Global Search is no data")
    public void tS13RemoveCell4G() {

        homePage.searchInGlobalSearch(cell4GNameForCreate1);
        new GlobalSearchPage(driver)
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
    @Description("The user creates an gNodeB in Cell Site Configuration and checks the message about successful creation")
    public void tS14CreateGNodeB() {
        String randomGNodeBName = RandomGenerator.generateRandomName();
        String randomGNodeBId = RandomGenerator.generateRandomGNodeBId();

        homePage.setAndSelectObjectType(locationTypeSite);
        new OldInventoryViewPage(driver)
                .filterObject("Name", locationName, "Site")
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .clickPlusIconAndSelectOption("Create gNodeB");
        new GNodeBWizardPage(driver)
                .createGNodeB(randomGNodeBName, randomGNodeBId, gNodeBModel, MCCMNCPrimary);
        Assert.assertTrue(SystemMessageContainer.create(driver, webDriverWait)
                .getMessages().get(0).getText().contains("Created gNodeB"));
    }

    @Test(groups = {"Radio tests"})
    @Description("The user edits an gNodeB in Cell Site Configuration and checks if the description is updated in Base Stations table")
    public void tS15ModifyGNodeB() {

        homePage.setAndSelectObjectType(locationTypeSite);
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
        String rowValue = tabTable.getValueCell(rowNumber, "Description");
        Assert.assertTrue(rowValue.contains(description));
    }

    @Test(groups = {"Radio tests"})
    @Description("The user deletes an gNodeB in Cell Site Configuration and checks if search result in Global Search is no data")
    public void tS16RemoveGNodeB() {

        homePage.searchInGlobalSearch(gNodeBNameForCreate1);
        new GlobalSearchPage(driver)
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
    @Description("The user creates a Cell 5G in Cell Site Configuration and checks the message about successful creation")
    public void tS17CreateCell5G() {
        String randomCell5GName = RandomGenerator.generateRandomName();
        String randomCell5GId = RandomGenerator.generateRandomCell5GId();

        homePage.setAndSelectObjectType(locationTypeSite);
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
    @Description("The user edits a Cell 5G in Cell Site Configuration and checks if the description is updated in Cells table")
    public void tS18ModifyCell5G() {

        homePage.setAndSelectObjectType(objectTypeCell5G);
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
        String rowValue = tabTable.getValueCell(rowNumber, "Description");
        Assert.assertTrue(rowValue.contains(description));
    }

    @Test(groups = {"Radio tests"})
    @Description("The user deletes an Cell 5G in Cell Site Configuration and checks if search result in Global Search is no data")
    public void tS19RemoveCell5G() {

        homePage.searchInGlobalSearch(cell5GNameForCreate1);
        new GlobalSearchPage(driver)
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
    @Description("The user creates Base Band Unit in Cell Site Configuration and checks the message about successful creation")
    public void tS20CreateBBU() {
        String randomDeviceName = RandomGenerator.generateRandomName();

        homePage.setAndSelectObjectType(locationTypeSite);
        new OldInventoryViewPage(driver)
                .filterObject("Name", locationName, "Site")
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .selectTab("Devices")
                .clickPlusIconAndSelectOption("Create Device");
        new DeviceWizardPage(driver)
                .createDevice(deviceBBUModel, randomDeviceName, locationName);
        Assert.assertTrue(SystemMessageContainer.create(driver, webDriverWait)
                .getMessages().get(0).getText().contains("Device has been created successfully"));
    }

    @Test(groups = {"Radio tests"})
    @Description("The user creates Base Band Unit in Cell Site Configuration, searches the device in Inventory View and goes to Cell Site Configuration again, then edits the device and checks if the description is updated in Device table")
    public void tS21CreateAndModifyBBU() {
        String randomDeviceName = RandomGenerator.generateRandomName();

        homePage.setAndSelectObjectType(locationTypeSite);
        new OldInventoryViewPage(driver)
                .filterObject("Name", locationName, "Site")
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .selectTab("Devices")
                .clickPlusIconAndSelectOption("Create Device");
        new DeviceWizardPage(driver)
                .createDevice(deviceRRUModel, randomDeviceName, locationName);
        SystemMessageInterface systemMessageItem = SystemMessageContainer.create(driver, webDriverWait);
        systemMessageItem.waitForMessageDisappear();
        homePage.goToHomePage(driver, BASIC_URL);
        homePage.setAndSelectObjectType(objectTypeDevice);
        new OldInventoryViewPage(driver)
                .filterObject("Name", randomDeviceName, "PhysicalElement")
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .expandTreeToLocation(locationTypeSite, locationName)
                .selectTab("Devices")
                .filterObject("Name", randomDeviceName)
                .clickEditIcon();
        new DeviceWizardPage(driver)
                .setDescription(description);
        new DeviceWizardPage(driver)
                .update();
        new CellSiteConfigurationPage(driver).getTabTable();
        OldTable tabTable = new CellSiteConfigurationPage(driver).getTabTable();
        int rowNumber = tabTable.getRowNumber(description, "Description");
        String rowValue = tabTable.getValueCell(rowNumber, "Description");
        Assert.assertTrue(rowValue.contains(description));
    }

    @Test(groups = {"Radio tests"})
    @Description("The user creates Base Band Unit in Cell Site Configuration, searches the device in Inventory View and goes to Cell Site Configuration again, then edits the device and checks if the description is updated in Device table")
    public void tS22CreateAndRemoveBBU() {
        String randomDeviceName = RandomGenerator.generateRandomName();

        homePage.setAndSelectObjectType(locationTypeSite);
        new OldInventoryViewPage(driver)
                .filterObject("Name", locationName, "Site")
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .selectTab("Devices")
                .clickPlusIconAndSelectOption("Create Device");
        new DeviceWizardPage(driver)
                .createDevice(deviceBBUModel, randomDeviceName, locationName);
        SystemMessageInterface systemMessageItem = SystemMessageContainer.create(driver, webDriverWait);
        systemMessageItem.waitForMessageDisappear();
        homePage.goToHomePage(driver, BASIC_URL);
        homePage.setAndSelectObjectType(objectTypeDevice);
        new OldInventoryViewPage(driver)
                .filterObject("Name", randomDeviceName, "PhysicalElement")
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .expandTreeToLocation(locationTypeSite, locationName)
                .selectTab("Devices")
                .filterObject("Name", randomDeviceName)
                .removeObject();
        systemMessageItem.waitForMessageDisappear();
        homePage.searchInGlobalSearch(randomDeviceName);
        CommonList objectsList = new GlobalSearchPage(driver).getResultsList();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertTrue(objectsList.isNoData());
    }

    @Test(groups = {"Radio tests"})
    @Description("The user creates Radio Remote Unit in Cell Site Configuration and checks the message about successful creation")
    public void tS23CreateRRU() {
        String randomDeviceName = RandomGenerator.generateRandomName();

        homePage.setAndSelectObjectType(locationTypeSite);
        new OldInventoryViewPage(driver)
                .filterObject("Name", locationName, "Site")
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .selectTab("Devices")
                .clickPlusIconAndSelectOption("Create Device");
        new DeviceWizardPage(driver)
                .createDevice(deviceRRUModel, randomDeviceName, locationName);
        Assert.assertTrue(SystemMessageContainer.create(driver, webDriverWait)
                .getMessages().get(0).getText().contains("Device has been created successfully"));
    }

    @Test(groups = {"Radio tests"})
    @Description("The user creates Radio Remote Unit in Cell Site Configuration, searches the device in Inventory View and goes to Cell Site Configuration again, then edits the device and checks if the description is updated in Device table")
    public void tS24CreateAndModifyRRU() {
        String randomDeviceName = RandomGenerator.generateRandomName();

        homePage.setAndSelectObjectType(locationTypeSite);
        new OldInventoryViewPage(driver)
                .filterObject("Name", locationName, "Site")
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .selectTab("Devices")
                .clickPlusIconAndSelectOption("Create Device");
        new DeviceWizardPage(driver)
                .createDevice(deviceRRUModel, randomDeviceName, locationName);
        SystemMessageInterface systemMessageItem = SystemMessageContainer.create(driver, webDriverWait);
        systemMessageItem.waitForMessageDisappear();
        homePage.goToHomePage(driver, BASIC_URL);
        homePage.setAndSelectObjectType(objectTypeDevice);
        new OldInventoryViewPage(driver)
                .filterObject("Name", randomDeviceName, "PhysicalElement")
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .expandTreeToLocation(locationTypeSite, locationName)
                .selectTab("Devices")
                .filterObject("Name", randomDeviceName)
                .clickEditIcon();
        new DeviceWizardPage(driver)
                .setDescription(description);
        new DeviceWizardPage(driver)
                .update();
        new CellSiteConfigurationPage(driver).getTabTable();
        OldTable tabTable = new CellSiteConfigurationPage(driver).getTabTable();
        int rowNumber = tabTable.getRowNumber(description, "Description");
        String rowValue = tabTable.getValueCell(rowNumber, "Description");
        Assert.assertTrue(rowValue.contains(description));
    }

    @Test(groups = {"Radio tests"})
    @Description("The user creates Radio Remote Unit in Cell Site Configuration, searches the device in Inventory View and goes to Cell Site Configuration again, then edits the device and checks if the description is updated in Device table")
    public void tS25CreateAndRemoveRRU() {
        String randomDeviceName = RandomGenerator.generateRandomName();

        homePage.setAndSelectObjectType(locationTypeSite);
        new OldInventoryViewPage(driver)
                .filterObject("Name", locationName, "Site")
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .selectTab("Devices")
                .clickPlusIconAndSelectOption("Create Device");
        new DeviceWizardPage(driver)
                .createDevice(deviceRRUModel, randomDeviceName, locationName);
        SystemMessageInterface systemMessageItem = SystemMessageContainer.create(driver, webDriverWait);
        systemMessageItem.waitForMessageDisappear();
        homePage.goToHomePage(driver, BASIC_URL);
        homePage.setAndSelectObjectType(objectTypeDevice);
        new OldInventoryViewPage(driver)
                .filterObject("Name", randomDeviceName, "PhysicalElement")
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .expandTreeToLocation(locationTypeSite, locationName)
                .selectTab("Devices")
                .filterObject("Name", randomDeviceName)
                .removeObject();
        systemMessageItem.waitForMessageDisappear();
        homePage.searchInGlobalSearch(randomDeviceName);
        CommonList objectsList = new GlobalSearchPage(driver).getResultsList();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertTrue(objectsList.isNoData());
    }

}
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
import com.oss.utils.RandomGenerator;
import com.oss.utils.TestListener;
import io.qameta.allure.Description;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

/**
 * @author Milena Miętkiewicz
 */

@Listeners({TestListener.class})
public class ThreeUKRegressionTests extends BaseTestCase {

    String randomLocationName = RandomGenerator.generateRandomName();
    String locationTypeSite = "Site";
    String objectTypeLocation = "Location";
    String description = "Selenium Test";

    String objectTypeDevice = "Physical Device";
    String randomDeviceName = RandomGenerator.generateRandomName();
    String deviceBBUModel = "HUAWEI Technology Co.,Ltd BBU5900";
    String deviceRRUModel = "HUAWEI Technology Co.,Ltd RRU5301";

    String objectTypeENodeB = "eNodeB";
    String randomENodeBName = RandomGenerator.generateRandomName();
    String randomENodeBId = RandomGenerator.generateRandomENodeBId();
    String eNodeBModel = "HUAWEI Technology Co.,Ltd eNodeB";
    String MCCMNCPrimary = "3UK [mcc: 234, mnc: 20]";

    String objectTypeCell4G = "Cell 4G";
    String randomCell4GName = RandomGenerator.generateRandomName();
    String randomCell4GId = RandomGenerator.generateRandomCell4GId();
    String carrier4G = "L800-B20-5";

    String objectTypeGNodeB = "gNodeB";
    String randomGNodeBName = RandomGenerator.generateRandomName();
    String randomGNodeBId = RandomGenerator.generateRandomGNodeBId();
    String gNodeBModel = "HUAWEI Technology Co.,Ltd gNodeB";

    String objectTypeCell5G = "Cell 5G";
    String randomCell5GName = RandomGenerator.generateRandomName();
    String randomCell5GId = RandomGenerator.generateRandomCell5GId();
    String carrier5G = "NR3600-n78-140";

    @BeforeMethod
    public void goToHomePage() {
        HomePage homePage = new HomePage(driver);
        homePage.goToHomePage(driver, BASIC_URL);
    }

    @Test(groups = {"Physical tests"})
    @Description("The user creates a location (Site) from left side menu and checks the message about successful creation")
    public void tS01CreateNewSiteSideMenu() {

        homePage.chooseFromLeftSideMenu("Create Location", "Wizards", "Physical Inventory");
        new LocationWizardPage(driver)
                .createLocation(locationTypeSite, randomLocationName);
        Assert.assertTrue(SystemMessageContainer.create(driver, webDriverWait)
                .getMessages().get(0).getText().contains("Location has been created successfully"));
    }

    @Test(groups = {"Physical tests"})
    @Description("The user filters the created Site in Inventory View for Location and checks if properties table contains Site name")
    public void tS02BrowseLocationInInventoryView() {
//        String randomLocationName="Milena";

        homePage.setAndSelectObjectType(objectTypeLocation);
        new OldInventoryViewPage(driver)
                .filterObject("Name", randomLocationName, "Location");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        OldTable propertiesTable = OldTable.createByComponentDataAttributeName(driver, webDriverWait, "properties(Location)");
        int rowNumber = propertiesTable.getRowNumber(randomLocationName, "Property Value");
        String rowValue = propertiesTable.getValueCell(rowNumber, "Property Value");
        Assert.assertTrue(rowValue.contains(randomLocationName));
    }

    @Test(groups = {"Physical tests"})
    @Description("The user filters the created Site in Inventory View for Sites and checks if properties table contains Site name")
    public void tS03BrowseSiteInInventoryView() {
//        String randomLocationName="Milena";

        homePage.setAndSelectObjectType(locationTypeSite);
        new OldInventoryViewPage(driver)
                .filterObject("Name", randomLocationName, "Site");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        OldTable propertiesTable = OldTable.createByComponentDataAttributeName(driver, webDriverWait, "properties(Site)");
        int rowNumber = propertiesTable.getRowNumber(randomLocationName, "Property Value");
        String rowValue = propertiesTable.getValueCell(rowNumber, "Property Value");
        Assert.assertTrue(rowValue.contains(randomLocationName));
    }

    @Test(enabled = false, groups = {"Physical tests"}) //not ready
    @Description("The user creates a Site in IV, searches for it in Global Search, removes it in IV and checks if the Site is removed in Global Search")
    public void tS04CreateAndDeleteNewSiteInventoryView() {
    }

    @Test(groups = {"Physical tests"})
    @Description("The user creates a Site in the created Site in Location Overview and checks if a new row is displayed in Locations table")
    public void tS05CreateNewSiteInLocation() {
        String randomLocationNameInLocation = RandomGenerator.generateRandomName();
//        String randomLocationName="Milena";

        homePage.setAndSelectObjectType(locationTypeSite);
        new OldInventoryViewPage(driver)
                .filterObject("Name", randomLocationName, "Site")
                .expandShowOnAndChooseView("OpenLocationOverviewAction");
        new LocationOverviewPage(driver)
                .clickButton("Create Location");
        new LocationWizardPage(driver)
                .createLocation(locationTypeSite, randomLocationNameInLocation);
        new LocationOverviewPage(driver)
                .selectTab("Locations")
                .filterLocationsObject("Name", randomLocationNameInLocation);
//        OldTable tabTable = OldTable.createByComponentDataAttributeName(driver, webDriverWait, "tableAppLocationsId");
        OldTable tabTable = new LocationOverviewPage(driver).getLocationsTabTable();
        int rowNumber = tabTable.getRowNumber(randomLocationNameInLocation, "Name");
        String rowValue = tabTable.getValueCell(rowNumber, "Name");
        Assert.assertTrue(rowValue.contains(randomLocationNameInLocation));
    }

    @Test(groups = {"Physical tests"})
    @Description("The user creates a Site in the created Site in Location Overview, then edits the Site and checks if the description is updated in Locations table")
    public void tS06CreateAndModifySiteInLocation() {
        String randomLocationNameInLocation = RandomGenerator.generateRandomName();
//        String randomLocationName = "Milena";

        homePage.setAndSelectObjectType(locationTypeSite);
        new OldInventoryViewPage(driver)
                .filterObject("Name", randomLocationName, "Site")
                .expandShowOnAndChooseView("OpenLocationOverviewAction");
        new LocationOverviewPage(driver)
                .clickButton("Create Location");
        new LocationWizardPage(driver)
                .createLocation(locationTypeSite, randomLocationNameInLocation);
        new LocationOverviewPage(driver)
                .selectTab("Locations")
                .filterLocationsObject("Name", randomLocationNameInLocation)
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
    @Description("The user creates a Site in the created Site in Location Overview, then deletes the Site and checks if the row is removed in Locations table")
    public void tS07CreateAndRemoveSiteInLocation() {
        String randomLocationNameInLocation = RandomGenerator.generateRandomName();
//        String randomLocationName="Milena";

        homePage.setAndSelectObjectType(locationTypeSite);
        new OldInventoryViewPage(driver)
                .filterObject("Name", randomLocationName, "Site")
                .expandShowOnAndChooseView("OpenLocationOverviewAction");
        new LocationOverviewPage(driver)
                .clickButton("Create Location");
        new LocationWizardPage(driver)
                .createLocation(locationTypeSite, randomLocationNameInLocation);
        new LocationOverviewPage(driver)
                .selectTab("Locations")
                .filterLocationsObject("Name", randomLocationNameInLocation)
                .clickRemoveLocationIcon();
        ConfirmationBoxInterface confirmationBox = ConfirmationBox.create(driver, webDriverWait);
        confirmationBox.clickButtonByLabel("Delete");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        TabsInterface tabTable = TabWindowWidget.create(driver, webDriverWait);
        Assert.assertTrue(tabTable.isNoData("tableAppLocationsId"));
    }

    @Test(groups = {"Radio tests"})
    @Description("The user creates eNodeB in Cell Site Configuration and checks the message about successful creation")
    public void tS08CreateENodeB() {
//        String randomLocationName="Milena";

        homePage.setAndSelectObjectType(locationTypeSite);
        new OldInventoryViewPage(driver)
                .filterObject("Name", randomLocationName, "Site")
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .clickPlusIconAndSelectOption("Create eNodeB");
        new ENodeBWizardPage(driver)
                .createENodeB(randomENodeBName, randomENodeBId, eNodeBModel, MCCMNCPrimary);
        Assert.assertTrue(SystemMessageContainer.create(driver, webDriverWait)
                .getMessages().get(0).getText().contains("Created eNodeB"));
    }

    @Test(groups = {"Radio tests"})
    @Description("The user creates eNodeB in Cell Site Configuration, searches eNodeB in Inventory View and goes to Cell Site Configuration again, then edits the eNodeB and checks if the description is updated in Base Stations table")
    public void tS09CreateAndEditENodeB() {
        String randomENodeBName = RandomGenerator.generateRandomName();
        String randomENodeBId = RandomGenerator.generateRandomENodeBId();
//        String randomLocationName="Milena";

        homePage.setAndSelectObjectType(locationTypeSite);
        new OldInventoryViewPage(driver)
                .filterObject("Name", randomLocationName, "Site")
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .selectTab("Base Stations")
                .clickPlusIconAndSelectOption("Create eNodeB");
        new ENodeBWizardPage(driver)
                .createENodeB(randomENodeBName, randomENodeBId, eNodeBModel, MCCMNCPrimary);
        SystemMessageInterface systemMessageItem = SystemMessageContainer.create(driver, webDriverWait);
        systemMessageItem.waitForMessageDisappear();
        homePage.goToHomePage(driver, BASIC_URL);
        homePage.setAndSelectObjectType(objectTypeENodeB);
        new OldInventoryViewPage(driver)
                .filterObject("Name", randomENodeBName, "ENodeB")
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .expandTreeToLocation(locationTypeSite, randomLocationName)
                .selectTab("Base Stations")
                .filterObject("Name", randomENodeBName)
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
    @Description("The user creates eNodeB in Cell Site Configuration, searches eNodeB name in Global Search and deletes the eNodeB and checks if search result in Global Search is no data")
    public void tS10CreateAndRemoveENodeB() {
        String randomENodeBName = RandomGenerator.generateRandomName();
        String randomENodeBId = RandomGenerator.generateRandomENodeBId();
//        String randomLocationName="Milena";

        homePage.setAndSelectObjectType(locationTypeSite);
        new OldInventoryViewPage(driver)
                .filterObject("Name", randomLocationName, "Site")
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .selectTab("Base Stations")
                .clickPlusIconAndSelectOption("Create eNodeB");
        new ENodeBWizardPage(driver)
                .createENodeB(randomENodeBName, randomENodeBId, eNodeBModel, MCCMNCPrimary);
        SystemMessageInterface systemMessageItem = SystemMessageContainer.create(driver, webDriverWait);
        systemMessageItem.waitForMessageDisappear();
        homePage.searchInGlobalSearch(randomENodeBName);
        new GlobalSearchPage(driver)
                .expandShowOnAndChooseView(randomENodeBName, "Show on", "Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .expandTreeToLocation(locationTypeSite, randomLocationName)
                .selectTab("Base Stations")
                .filterObject("Name", randomENodeBName)
                .clickRemoveIcon();
        systemMessageItem.waitForMessageDisappear();
        homePage.searchInGlobalSearch(randomENodeBName);
        CommonList objectsList = new GlobalSearchPage(driver).getResultsList();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertTrue(objectsList.isNoData());
    }

    @Test(groups = {"Radio tests"})
    @Description("The user creates Cell 4G in Cell Site Configuration and checks the message about successful creation")
    public void tS11CreateCell4G() {
//        String randomLocationName = "Milena";
//        String randomENodeBName = "eNodeBSelenium";

        homePage.setAndSelectObjectType(locationTypeSite);
        new OldInventoryViewPage(driver)
                .filterObject("Name", randomLocationName, "Site")
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .selectTab("Cells")
                .clickPlusIconAndSelectOption("Create Cell 4G");
        new Cell4GWizardPage(driver)
                .createCell4G(randomCell4GName, randomENodeBName, randomCell4GId, carrier4G);
        Assert.assertTrue(SystemMessageContainer.create(driver, webDriverWait)
                .getMessages().get(0).getText().contains("Created Cell 4G"));
    }

    @Test(groups = {"Radio tests"})
    @Description("The user creates Cell 4G in Cell Site Configuration, searches Cell 4G in Inventory View and goes to Cell Site Configuration again, then edits the Cell 4G and checks if the description is updated in Cells table")
    public void tS12CreateAndModifyCell4G() {
        String randomCell4GName = RandomGenerator.generateRandomName();
        String randomCell4GId = RandomGenerator.generateRandomCell4GId();
//        String randomLocationName = "Milena";
//        String randomENodeBName = "eNodeBSelenium";

        homePage.setAndSelectObjectType(locationTypeSite);
        new OldInventoryViewPage(driver)
                .filterObject("Name", randomLocationName, "Site")
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .selectTab("Cells")
                .clickPlusIconAndSelectOption("Create Cell 4G");
        new Cell4GWizardPage(driver)
                .createCell4G(randomCell4GName, randomENodeBName, randomCell4GId, carrier4G);
        SystemMessageInterface systemMessageItem = SystemMessageContainer.create(driver, webDriverWait);
        systemMessageItem.waitForMessageDisappear();
        homePage.goToHomePage(driver, BASIC_URL);
        homePage.setAndSelectObjectType(objectTypeCell4G);
        new OldInventoryViewPage(driver)
                .filterObject("Cell Name", randomCell4GName, "Cell4G")
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .expandTreeToBaseStation(locationTypeSite, randomLocationName, randomENodeBName)
                .selectTab("Cells")
                .filterObject("Name", randomCell4GName)
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
    @Description("The user creates Cell 4G in Cell Site Configuration, searches Cell 4G name in Global Search and deletes the Cell 4G and checks if search result in Global Search is no data")
    public void tS13CreateAndRemoveCell4G() {
        String randomCell4GName = RandomGenerator.generateRandomName();
        String randomCell4GId = RandomGenerator.generateRandomCell4GId();
//        String randomLocationName = "Milena";
//        String randomENodeBName = "eNodeBSelenium";

        homePage.setAndSelectObjectType(locationTypeSite);
        new OldInventoryViewPage(driver)
                .filterObject("Name", randomLocationName, "Site")
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .selectTab("Cells")
                .clickPlusIconAndSelectOption("Create Cell 4G");
        new Cell4GWizardPage(driver)
                .createCell4G(randomCell4GName, randomENodeBName, randomCell4GId, carrier4G);
        SystemMessageInterface systemMessageItem = SystemMessageContainer.create(driver, webDriverWait);
        systemMessageItem.waitForMessageDisappear();
        homePage.searchInGlobalSearch(randomCell4GName);
        new GlobalSearchPage(driver)
                .expandShowOnAndChooseView(randomCell4GName, "Show on", "Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .expandTreeToBaseStation(locationTypeSite, randomLocationName, randomENodeBName)
                .selectTab("Cells")
                .filterObject("Name", randomCell4GName)
                .clickRemoveIcon();
        systemMessageItem.waitForMessageDisappear();
        homePage.searchInGlobalSearch(randomCell4GName);
        CommonList objectsList = new GlobalSearchPage(driver).getResultsList();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertTrue(objectsList.isNoData());
    }

    @Test(groups = {"Radio tests"})
    @Description("The user creates gNodeB in Cell Site Configuration and checks the message about successful creation")
    public void tS14CreateGNodeB() {
//        String randomLocationName = "Milena";

        homePage.setAndSelectObjectType(locationTypeSite);
        new OldInventoryViewPage(driver)
                .filterObject("Name", randomLocationName, "Site")
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .clickPlusIconAndSelectOption("Create gNodeB");
        new GNodeBWizardPage(driver)
                .createGNodeB(randomGNodeBName, randomGNodeBId, gNodeBModel, MCCMNCPrimary);
        Assert.assertTrue(SystemMessageContainer.create(driver, webDriverWait)
                .getMessages().get(0).getText().contains("Created gNodeB"));
    }

    @Test(groups = {"Radio tests"})
    @Description("The user creates gNodeB in Cell Site Configuration, searches gNodeB in Inventory View and goes to Cell Site Configuration again, then edits the gNodeB and checks if the description is updated in Base Stations table")
    public void tS15CreateAndEditGNodeB() {
        String randomGNodeBName = RandomGenerator.generateRandomName();
        String randomGNodeBId = RandomGenerator.generateRandomGNodeBId();
//        String randomLocationName="Milena";

        homePage.setAndSelectObjectType(locationTypeSite);
        new OldInventoryViewPage(driver)
                .filterObject("Name", randomLocationName, "Site")
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .selectTab("Base Stations")
                .clickPlusIconAndSelectOption("Create gNodeB");
        new GNodeBWizardPage(driver)
                .createGNodeB(randomGNodeBName, randomGNodeBId, gNodeBModel, MCCMNCPrimary);
        SystemMessageInterface systemMessageItem = SystemMessageContainer.create(driver, webDriverWait);
        systemMessageItem.waitForMessageDisappear();
        homePage.goToHomePage(driver, BASIC_URL);
        homePage.setAndSelectObjectType(objectTypeGNodeB);
        new OldInventoryViewPage(driver)
                .filterObject("Name", randomGNodeBName, "GNodeB")
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .expandTreeToLocation(locationTypeSite, randomLocationName)
                .selectTab("Base Stations")
                .filterObject("Name", randomGNodeBName)
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
    @Description("The user creates gNodeB in Cell Site Configuration, searches gNodeB name in Global Search and deletes the gNodeB and checks if search result in Global Search is no data")
    public void tS16CreateAndRemoveGNodeB() {
        String randomGNodeBName = RandomGenerator.generateRandomName();
        String randomGNodeBId = RandomGenerator.generateRandomGNodeBId();
//        String randomLocationName="Milena";

        homePage.setAndSelectObjectType(locationTypeSite);
        new OldInventoryViewPage(driver)
                .filterObject("Name", randomLocationName, "Site")
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .selectTab("Base Stations")
                .clickPlusIconAndSelectOption("Create gNodeB");
        new GNodeBWizardPage(driver)
                .createGNodeB(randomGNodeBName, randomGNodeBId, gNodeBModel, MCCMNCPrimary);
        SystemMessageInterface systemMessageItem = SystemMessageContainer.create(driver, webDriverWait);
        systemMessageItem.waitForMessageDisappear();
        homePage.searchInGlobalSearch(randomGNodeBName);
        new GlobalSearchPage(driver)
                .expandShowOnAndChooseView(randomGNodeBName, "Show on", "Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .expandTreeToLocation(locationTypeSite, randomLocationName)
                .selectTab("Base Stations")
                .filterObject("Name", randomGNodeBName)
                .clickRemoveIcon();
        systemMessageItem.waitForMessageDisappear();
        homePage.searchInGlobalSearch(randomGNodeBName);
        CommonList objectsList = new GlobalSearchPage(driver).getResultsList();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertTrue(objectsList.isNoData());
    }

    @Test(groups = {"Radio tests"})
    @Description("The user creates Cell 5G in Cell Site Configuration and checks the message about successful creation")
    public void tS17CreateCell5G() {
//        String randomLocationName = "Milena";
//        String randomGNodeBName = "gNodeBSelenium";

        homePage.setAndSelectObjectType(locationTypeSite);
        new OldInventoryViewPage(driver)
                .filterObject("Name", randomLocationName, "Site")
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .selectTab("Cells")
                .clickPlusIconAndSelectOption("Create Cell 5G");
        new Cell5GWizardPage(driver)
                .createCell5G(randomCell5GName, randomGNodeBName, randomCell5GId, carrier5G);
        Assert.assertTrue(SystemMessageContainer.create(driver, webDriverWait)
                .getMessages().get(0).getText().contains("Created Cell 5G"));
    }

    @Test(groups = {"Radio tests"})
    @Description("The user creates Cell 5G in Cell Site Configuration, searches Cell 5G in Inventory View and goes to Cell Site Configuration again, then edits the Cell 5G and checks if the description is updated in Cells table")
    public void tS18CreateAndModifyCell5G() {
        String randomCell5GName = RandomGenerator.generateRandomName();
        String randomCell5GId = RandomGenerator.generateRandomCell5GId();
//        String randomLocationName = "Milena";
//        String randomGNodeBName = "gNodeBSelenium";

        homePage.setAndSelectObjectType(locationTypeSite);
        new OldInventoryViewPage(driver)
                .filterObject("Name", randomLocationName, "Site")
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .selectTab("Cells")
                .clickPlusIconAndSelectOption("Create Cell 5G");
        new Cell5GWizardPage(driver)
                .createCell5G(randomCell5GName, randomGNodeBName, randomCell5GId, carrier5G);
        SystemMessageInterface systemMessageItem = SystemMessageContainer.create(driver, webDriverWait);
        systemMessageItem.waitForMessageDisappear();
        homePage.goToHomePage(driver, BASIC_URL);
        homePage.setAndSelectObjectType(objectTypeCell5G);
        new OldInventoryViewPage(driver)
                .filterObject("Cell Name", randomCell5GName, "Cell5G")
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .expandTreeToBaseStation(locationTypeSite, randomLocationName, randomGNodeBName)
                .selectTab("Cells")
                .filterObject("Name", randomCell5GName)
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
    @Description("The user creates Cell 5G in Cell Site Configuration, searches Cell 5G name in Global Search and deletes the Cell 5G and checks if search result in Global Search is no data")
    public void tS19CreateAndRemoveCell5G() {
        String randomCell5GName = RandomGenerator.generateRandomName();
        String randomCell5GId = RandomGenerator.generateRandomCell5GId();
//        String randomLocationName = "Milena";
//        String randomGNodeBName = "gNodeBSelenium";

        homePage.setAndSelectObjectType(locationTypeSite);
        new OldInventoryViewPage(driver)
                .filterObject("Name", randomLocationName, "Site")
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .selectTab("Cells")
                .clickPlusIconAndSelectOption("Create Cell 5G");
        new Cell5GWizardPage(driver)
                .createCell5G(randomCell5GName, randomGNodeBName, randomCell5GId, carrier5G);
        SystemMessageInterface systemMessageItem = SystemMessageContainer.create(driver, webDriverWait);
        systemMessageItem.waitForMessageDisappear();
        homePage.searchInGlobalSearch(randomCell5GName);
        new GlobalSearchPage(driver)
                .expandShowOnAndChooseView(randomCell5GName, "Show on", "Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .expandTreeToBaseStation(locationTypeSite, randomLocationName, randomGNodeBName)
                .selectTab("Cells")
                .filterObject("Name", randomCell5GName)
                .clickRemoveIcon();
        systemMessageItem.waitForMessageDisappear();
        homePage.searchInGlobalSearch(randomCell5GName);
        CommonList objectsList = new GlobalSearchPage(driver).getResultsList();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertTrue(objectsList.isNoData());
    }

    @Test(groups = {"Radio tests"})
    @Description("The user creates Base Band Unit in Cell Site Configuration and checks the message about successful creation")
    public void tS20CreateBBU() {
//        String randomLocationName = "Milena";

        homePage.setAndSelectObjectType(locationTypeSite);
        new OldInventoryViewPage(driver)
                .filterObject("Name", randomLocationName, "Site")
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .selectTab("Devices")
                .clickPlusIconAndSelectOption("Create Device");
        new DeviceWizardPage(driver)
                .createDevice(deviceBBUModel, randomDeviceName, randomLocationName);
        Assert.assertTrue(SystemMessageContainer.create(driver, webDriverWait)
                .getMessages().get(0).getText().contains("Device has been created successfully"));
    }

    @Test(groups = {"Radio tests"})
    @Description("The user creates Base Band Unit in Cell Site Configuration, searches the device in Inventory View and goes to Cell Site Configuration again, then edits the device and checks if the description is updated in Device table")
    public void tS21CreateAndModifyBBU() {
        String randomDeviceName = RandomGenerator.generateRandomName();
//        String randomLocationName = "Milena";

        homePage.setAndSelectObjectType(locationTypeSite);
        new OldInventoryViewPage(driver)
                .filterObject("Name", randomLocationName, "Site")
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .selectTab("Devices")
                .clickPlusIconAndSelectOption("Create Device");
        new DeviceWizardPage(driver)
                .createDevice(deviceRRUModel, randomDeviceName, randomLocationName);
        SystemMessageInterface systemMessageItem = SystemMessageContainer.create(driver, webDriverWait);
        systemMessageItem.waitForMessageDisappear();
        homePage.goToHomePage(driver, BASIC_URL);
        homePage.setAndSelectObjectType(objectTypeDevice);
        new OldInventoryViewPage(driver)
                .filterObject("Name", randomDeviceName, "PhysicalElement")
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .expandTreeToLocation(locationTypeSite, randomLocationName)
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
//        String randomLocationName = "Milena";

        homePage.setAndSelectObjectType(locationTypeSite);
        new OldInventoryViewPage(driver)
                .filterObject("Name", randomLocationName, "Site")
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .selectTab("Devices")
                .clickPlusIconAndSelectOption("Create Device");
        new DeviceWizardPage(driver)
                .createDevice(deviceBBUModel, randomDeviceName, randomLocationName);
        SystemMessageInterface systemMessageItem = SystemMessageContainer.create(driver, webDriverWait);
        systemMessageItem.waitForMessageDisappear();
        homePage.goToHomePage(driver, BASIC_URL);
        homePage.setAndSelectObjectType(objectTypeDevice);
        new OldInventoryViewPage(driver)
                .filterObject("Name", randomDeviceName, "PhysicalElement")
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .expandTreeToLocation(locationTypeSite, randomLocationName)
                .selectTab("Devices")
                .filterObject("Name", randomDeviceName)
                .clickRemoveIcon();
        systemMessageItem.waitForMessageDisappear();
        homePage.searchInGlobalSearch(randomDeviceName);
        CommonList objectsList = new GlobalSearchPage(driver).getResultsList();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertTrue(objectsList.isNoData());
    }

    @Test(groups = {"Radio tests"})
    @Description("The user creates Radio Remote Unit in Cell Site Configuration and checks the message about successful creation")
    public void tS23CreateRRU() {
//        String randomLocationName = "Milena";

        homePage.setAndSelectObjectType(locationTypeSite);
        new OldInventoryViewPage(driver)
                .filterObject("Name", randomLocationName, "Site")
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .selectTab("Devices")
                .clickPlusIconAndSelectOption("Create Device");
        new DeviceWizardPage(driver)
                .createDevice(deviceRRUModel, randomDeviceName, randomLocationName);
        Assert.assertTrue(SystemMessageContainer.create(driver, webDriverWait)
                .getMessages().get(0).getText().contains("Device has been created successfully"));
    }

    @Test(groups = {"Radio tests"})
    @Description("The user creates Radio Remote Unit in Cell Site Configuration, searches the device in Inventory View and goes to Cell Site Configuration again, then edits the device and checks if the description is updated in Device table")
    public void tS24CreateAndModifyRRU() {
        String randomDeviceName = RandomGenerator.generateRandomName();
//        String randomLocationName = "Milena";

        homePage.setAndSelectObjectType(locationTypeSite);
        new OldInventoryViewPage(driver)
                .filterObject("Name", randomLocationName, "Site")
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .selectTab("Devices")
                .clickPlusIconAndSelectOption("Create Device");
        new DeviceWizardPage(driver)
                .createDevice(deviceRRUModel, randomDeviceName, randomLocationName);
        SystemMessageInterface systemMessageItem = SystemMessageContainer.create(driver, webDriverWait);
        systemMessageItem.waitForMessageDisappear();
        homePage.goToHomePage(driver, BASIC_URL);
        homePage.setAndSelectObjectType(objectTypeDevice);
        new OldInventoryViewPage(driver)
                .filterObject("Name", randomDeviceName, "PhysicalElement")
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .expandTreeToLocation(locationTypeSite, randomLocationName)
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
//        String randomLocationName = "Milena";

        homePage.setAndSelectObjectType(locationTypeSite);
        new OldInventoryViewPage(driver)
                .filterObject("Name", randomLocationName, "Site")
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .selectTab("Devices")
                .clickPlusIconAndSelectOption("Create Device");
        new DeviceWizardPage(driver)
                .createDevice(deviceRRUModel, randomDeviceName, randomLocationName);
        SystemMessageInterface systemMessageItem = SystemMessageContainer.create(driver, webDriverWait);
        systemMessageItem.waitForMessageDisappear();
        homePage.goToHomePage(driver, BASIC_URL);
        homePage.setAndSelectObjectType(objectTypeDevice);
        new OldInventoryViewPage(driver)
                .filterObject("Name", randomDeviceName, "PhysicalElement")
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .expandTreeToLocation(locationTypeSite, randomLocationName)
                .selectTab("Devices")
                .filterObject("Name", randomDeviceName)
                .clickRemoveIcon();
        systemMessageItem.waitForMessageDisappear();
        homePage.searchInGlobalSearch(randomDeviceName);
        CommonList objectsList = new GlobalSearchPage(driver).getResultsList();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertTrue(objectsList.isNoData());
    }

}

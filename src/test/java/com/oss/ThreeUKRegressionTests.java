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
import com.oss.pages.physical.LocationOverviewPage;
import com.oss.pages.physical.LocationWizardPage;
import com.oss.pages.platform.GlobalSearchPage;
import com.oss.pages.platform.HomePage;
import com.oss.pages.platform.OldInventoryViewPage;
import com.oss.pages.radio.Cell4GWizardPage;
import com.oss.pages.radio.CellSiteConfigurationPage;
import com.oss.pages.radio.ENodeBWizardPage;
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

    String objectTypeENodeB = "eNodeB";
    String randomENodeBName = RandomGenerator.generateRandomName();
    String randomENodeBId = RandomGenerator.generateRandomENodeBId();
    String eNodeBModel = "HUAWEI Technology Co.,Ltd eNodeB";
    String MCCMNCPrimary = "3UK [mcc: 234, mnc: 20]";

    String objectTypeCell4G = "Cell 4G";
    String randomCell4GName = RandomGenerator.generateRandomName();
    String randomCell4GId = RandomGenerator.generateRandomCell4GId();
    String carrier4G = "L800-B20-5";

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
                .filterObject("Name", randomLocationNameInLocation);
        OldTable locationsTable = OldTable.createByComponentDataAttributeName(driver, webDriverWait, "tableAppLocationsId");
        int rowNumber = locationsTable.getRowNumber(randomLocationNameInLocation, "Name");
        String rowValue = locationsTable.getValueCell(rowNumber, "Name");
        Assert.assertTrue(rowValue.contains(randomLocationNameInLocation));
    }

    @Test(groups = {"Physical tests"})
    @Description("The user creates a Site in the created Site in Location Overview, then edits the Site and checks if the description is updated in Locations table")
    public void tS06CreateAndModifySiteInLocation() {
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
                .filterObject("Name", randomLocationNameInLocation)
                .clickEditLocationIcon();
        new LocationWizardPage(driver)
                .setDescription(description)
                .accept();
        OldTable locationsTable = OldTable.createByComponentDataAttributeName(driver, webDriverWait, "tableAppLocationsId");
        int rowNumber = locationsTable.getRowNumber(description, "Description");
        String rowValue = locationsTable.getValueCell(rowNumber, "Description");
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
                .filterObject("Name", randomLocationNameInLocation)
                .clickRemoveLocationIcon();
        ConfirmationBoxInterface confirmationBox = ConfirmationBox.create(driver, webDriverWait);
        confirmationBox.clickButtonByLabel("Delete");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        TabsInterface locationsTable = TabWindowWidget.create(driver, webDriverWait);
        Assert.assertTrue(locationsTable.isNoData("tableAppLocationsId"));
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
        OldTable baseStationsTable = OldTable.createByComponentDataAttributeName(driver, webDriverWait, "BsTableApp");
        int rowNumber = baseStationsTable.getRowNumber(description, "Description");
        String rowValue = baseStationsTable.getValueCell(rowNumber, "Description");
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
        ConfirmationBoxInterface confirmationBox = ConfirmationBox.create(driver, webDriverWait);
        confirmationBox.clickButtonByLabel("Delete");
        systemMessageItem.waitForMessageDisappear();
        homePage.searchInGlobalSearch(randomENodeBName);
        CommonList searchResults = CommonList.create(driver, webDriverWait, "objectsList");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertTrue(searchResults.isNoData());
    }

    @Test(groups = {"Radio tests"})
    @Description("The user creates Cell 4G in Cell Site Configuration and checks the message about successful creation")
    public void tS11CreateCell4G() {
//        String randomLocationName = "Milena";
//        String randomENodeBName = "MilenaeNodeB";

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
//        String randomENodeBName = "MilenaeNodeB";

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
                .expandTreeToENodeB(locationTypeSite, randomLocationName, randomENodeBName)
                .selectTab("Cells")
                .filterObject("Name", randomCell4GName)
                .clickEditIcon();
        new Cell4GWizardPage(driver)
                .setDescription(description)
                .accept();
        OldTable cellsTable = OldTable.createByComponentDataAttributeName(driver, webDriverWait, "cell4gTable");
        int rowNumber = cellsTable.getRowNumber(description, "Description");
        String rowValue = cellsTable.getValueCell(rowNumber, "Description");
        Assert.assertTrue(rowValue.contains(description));
    }

    @Test(groups = {"Radio tests"})
    @Description("The user creates Cell 4G in Cell Site Configuration, searches Cell 4G name in Global Search and deletes the Cell 4G and checks if search result in Global Search is no data")
    public void tS13CreateAndRemoveCell4G() {
        String randomCell4GName = RandomGenerator.generateRandomName();
        String randomCell4GId = RandomGenerator.generateRandomCell4GId();
//        String randomLocationName = "Milena";
//        String randomENodeBName = "MilenaeNodeB";

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
                .expandTreeToENodeB(locationTypeSite, randomLocationName, randomENodeBName)
                .selectTab("Cells")
                .filterObject("Name", randomCell4GName)
                .clickRemoveIcon();
        ConfirmationBoxInterface confirmationBox = ConfirmationBox.create(driver, webDriverWait);
        confirmationBox.clickButtonByLabel("Delete");
        systemMessageItem.waitForMessageDisappear();
        homePage.searchInGlobalSearch(randomCell4GName);
        CommonList searchResults = CommonList.create(driver, webDriverWait, "objectsList");
        Assert.assertTrue(searchResults.isNoData());
    }

    @Test(groups = {"Radio tests"})
    @Description("The user creates eNodeB in Cell Site Configuration and checks the message about successful creation")
    public void tS14CreateGNodeB() {
    }

}
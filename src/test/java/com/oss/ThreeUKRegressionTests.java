package com.oss;

import com.oss.framework.alerts.SystemMessageContainer;
import com.oss.framework.prompts.ConfirmationBox;
import com.oss.framework.prompts.ConfirmationBoxInterface;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.tablewidget.OldTable;
import com.oss.framework.widgets.tabswidget.TabWindowWidget;
import com.oss.framework.widgets.tabswidget.TabsInterface;
import com.oss.pages.physical.LocationWizardPage;
import com.oss.pages.platform.HomePage;
import com.oss.pages.physical.LocationOverviewPage;
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

        HomePage homePage = HomePage.goToHomePage(driver, BASIC_URL);
    }

    @Test(groups = {"Physical tests"})
    @Description("The user creates a location (Site) from left side menu and checks the message about successful creation")
    public void tS01CreateNewSiteSideMenu() {

        new HomePage(driver)
                .chooseFromLeftSideMenu("Create Location", "Wizards", "Physical Inventory");
        new LocationWizardPage(driver)
                .createLocation(locationTypeSite, randomLocationName);
        Assert.assertTrue(SystemMessageContainer.create(driver, webDriverWait)
                .getMessages().get(0).getText().contains("Location has been created successfully"));
    }

    @Test(groups = {"Physical tests"})
    @Description("The user filters the created Site in Inventory View for Location and checks if properties table contains Site name")
    public void tS02BrowseLocationInInventoryView() {
//        String randomLocationName="Milena";

        new HomePage(driver)
                .typeObjectType(objectTypeLocation)
                .confirmObjectType(objectTypeLocation)
                .filterObject("Name", randomLocationName, "Location");
        OldTable propertiesTable = OldTable.createByComponentDataAttributeName(driver, webDriverWait, "properties(Location)");
        int rowNumber = propertiesTable.getRowNumber(randomLocationName, "Property Value");
        String rowValue = propertiesTable.getValueCell(rowNumber, "Property Value");
        Assert.assertTrue(rowValue.contains(randomLocationName));
    }

    @Test(groups = {"Physical tests"})
    @Description("The user filters the created Site in Inventory View for Sites and checks if properties table contains Site name")
    public void tS03BrowseSiteInInventoryView() {
//        String randomLocationName="Milena";

        new HomePage(driver)
                .typeObjectType(locationTypeSite)
                .confirmObjectType(locationTypeSite)
                .filterObject("Name", randomLocationName, "Site");
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

        new HomePage(driver)
                .typeObjectType(locationTypeSite)
                .confirmObjectType(locationTypeSite)
                .filterObject("Name", randomLocationName, "Site");
        new OldInventoryViewPage(driver)
                .expandShowOnAndChooseView("OpenLocationOverviewAction");
        new LocationOverviewPage(driver)
                .clickCreateLocation()
                .createLocation(locationTypeSite, randomLocationNameInLocation);
        new LocationOverviewPage(driver)
                .selectLocationTab()
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

        new HomePage(driver)
                .typeObjectType(locationTypeSite)
                .confirmObjectType(locationTypeSite)
                .filterObject("Name", randomLocationName, "Site");
        new OldInventoryViewPage(driver)
                .expandShowOnAndChooseView("OpenLocationOverviewAction");
        new LocationOverviewPage(driver)
                .clickCreateLocation()
                .createLocation(locationTypeSite, randomLocationNameInLocation);
        new LocationOverviewPage(driver)
                .selectLocationTab()
                .filterObject("Name", randomLocationNameInLocation);
        new LocationOverviewPage(driver)
                .clickEditLocationIcon()
                .typeDescription(description);
        new LocationWizardPage(driver)
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

        new HomePage(driver)
                .typeObjectType(locationTypeSite)
                .confirmObjectType(locationTypeSite)
                .filterObject("Name", randomLocationName, "Site");
        new OldInventoryViewPage(driver)
                .expandShowOnAndChooseView("OpenLocationOverviewAction");
        new LocationOverviewPage(driver)
                .clickCreateLocation()
                .createLocation(locationTypeSite, randomLocationNameInLocation);
        new LocationOverviewPage(driver)
                .selectLocationTab()
                .filterObject("Name", randomLocationNameInLocation);
        new LocationOverviewPage(driver)
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

        new HomePage(driver)
                .typeObjectType(locationTypeSite)
                .confirmObjectType(locationTypeSite)
                .filterObject("Name", randomLocationName, "Site");
        new OldInventoryViewPage(driver)
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .clickPlusIconAndSelectOption("Create eNodeB");
        new ENodeBWizardPage(driver)
                .createENodeB(randomENodeBName, randomENodeBId, eNodeBModel, MCCMNCPrimary);
        new ENodeBWizardPage(driver)
                .accept();
        Assert.assertTrue(SystemMessageContainer.create(driver, webDriverWait)
                .getMessages().get(0).getText().contains("Created eNodeB"));
    }

    @Test(groups = {"Radio tests"})
    @Description("The user creates eNodeB in Cell Site Configuration, searches eNodeB in Inventory View and goes to Cell Site Configuration again, then edits the eNodeB and checks if the description is updated in Base Stations table")
    public void tS09CreateAndEditENodeB() {
        String randomENodeBName = RandomGenerator.generateRandomName();
        String randomENodeBId = RandomGenerator.generateRandomENodeBId();
//        String randomLocationName="Milena";

        new HomePage(driver)
                .typeObjectType(locationTypeSite)
                .confirmObjectType(locationTypeSite)
                .filterObject("Name", randomLocationName, "Site");
        new OldInventoryViewPage(driver)
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .selectTab("Base Stations");
        new CellSiteConfigurationPage(driver)
                .clickPlusIconAndSelectOption("Create eNodeB");
        new ENodeBWizardPage(driver)
                .createENodeB(randomENodeBName, randomENodeBId, eNodeBModel, MCCMNCPrimary);
        new ENodeBWizardPage(driver)
                .accept();
        new HomePage(driver)
                .goToHomePage(driver, BASIC_URL)
                .typeObjectType(objectTypeENodeB)
                .confirmObjectType(objectTypeENodeB)
                .filterObject("Name", randomENodeBName, "ENodeB");
        new OldInventoryViewPage(driver)
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .expandTreeToLocation(locationTypeSite, randomLocationName);
        new CellSiteConfigurationPage(driver)
                .selectTab("Base Stations");
        new CellSiteConfigurationPage(driver)
                .filterObject("Name", randomENodeBName);
        new CellSiteConfigurationPage(driver)
                .clickEditIcon();
        new ENodeBWizardPage(driver)
                .typeDescription(description)
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

        new HomePage(driver)
                .typeObjectType(locationTypeSite)
                .confirmObjectType(locationTypeSite)
                .filterObject("Name", randomLocationName, "Site");
        new OldInventoryViewPage(driver)
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .selectTab("Base Stations");
        new CellSiteConfigurationPage(driver)
                .clickPlusIconAndSelectOption("Create eNodeB");
        new ENodeBWizardPage(driver)
                .createENodeB(randomENodeBName, randomENodeBId, eNodeBModel, MCCMNCPrimary);
        new ENodeBWizardPage(driver)
                .accept();
        new HomePage(driver)
                .searchInGlobalSearch(randomENodeBName)
                .clickThreeDots(randomENodeBName)
                .deleteObject();
        ConfirmationBoxInterface confirmationBox = ConfirmationBox.create(driver, webDriverWait);
        confirmationBox.clickButtonByLabel("Delete");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        OldTable baseStationsTable = OldTable.createByComponentDataAttributeName(driver, webDriverWait, "BsTableApp");
        Assert.assertTrue(baseStationsTable.isNoData());
    }

    @Test(groups = {"Radio tests"})
    @Description("The user creates Cell 4G in Cell Site Configuration and checks the message about successful creation")
    public void tS11CreateCell4G() {
//        String randomLocationName = "Milena";

        new HomePage(driver)
                .typeObjectType(locationTypeSite)
                .confirmObjectType(locationTypeSite)
                .filterObject("Name", randomLocationName, "Site");
        new OldInventoryViewPage(driver)
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .selectTab("Cells");
        new CellSiteConfigurationPage(driver)
                .clickPlusIconAndSelectOption("Create Cell 4G");
        new Cell4GWizardPage(driver)
                .createCell4G(randomCell4GName, randomENodeBName, randomCell4GId, carrier4G);
        new Cell4GWizardPage(driver)
                .accept();
        Assert.assertTrue(SystemMessageContainer.create(driver, webDriverWait)
                .getMessages().get(0).getText().contains("Created Cell 4G"));
    }

    @Test(groups = {"Radio tests"})
    @Description("The user creates Cell 4G in Cell Site Configuration, searches Cell 4G in Inventory View and goes to Cell Site Configuration again, then edits the Cell 4G and checks if the description is updated in Cells table")
    public void tS12CreateAndModifyCell4G() {
        String randomCell4GName = RandomGenerator.generateRandomName();
        String randomCell4GId = RandomGenerator.generateRandomCell4GId();
//        String randomLocationName = "Milena";

        new HomePage(driver)
                .typeObjectType(locationTypeSite)
                .confirmObjectType(locationTypeSite)
                .filterObject("Name", randomLocationName, "Site");
        new OldInventoryViewPage(driver)
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .selectTab("Cells");
        new CellSiteConfigurationPage(driver)
                .clickPlusIconAndSelectOption("Create Cell 4G");
        new Cell4GWizardPage(driver)
                .createCell4G(randomCell4GName, randomENodeBName, randomCell4GId, carrier4G);
        new Cell4GWizardPage(driver)
                .accept();
        new HomePage(driver)
                .goToHomePage(driver, BASIC_URL)
                .typeObjectType(objectTypeCell4G)
                .confirmObjectType(objectTypeCell4G)
                .filterObject("Cell Name", randomCell4GName, "Cell4G");
        new OldInventoryViewPage(driver)
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .expandTreeToENodeB(locationTypeSite, randomLocationName, randomENodeBName);
        new CellSiteConfigurationPage(driver)
                .selectTab("Cells");
        new CellSiteConfigurationPage(driver)
                .filterObject("Name", randomCell4GName);
        new CellSiteConfigurationPage(driver)
                .clickEditIcon();
        new Cell4GWizardPage(driver)
                .typeDescription(description)
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

        new HomePage(driver)
                .typeObjectType(locationTypeSite)
                .confirmObjectType(locationTypeSite)
                .filterObject("Name", randomLocationName, "Site");
        new OldInventoryViewPage(driver)
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .selectTab("Cells");
        new CellSiteConfigurationPage(driver)
                .clickPlusIconAndSelectOption("Create Cell 4G");
        new Cell4GWizardPage(driver)
                .createCell4G(randomCell4GName, randomENodeBName, randomCell4GId, carrier4G);
        new Cell4GWizardPage(driver)
                .accept();
        new HomePage(driver)
                .searchInGlobalSearch(randomCell4GName)
                .clickThreeDots(randomCell4GName)
                .deleteObject();
        ConfirmationBoxInterface confirmationBox = ConfirmationBox.create(driver, webDriverWait);
        confirmationBox.clickButtonByLabel("Delete");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        OldTable objectsList = OldTable.createByComponentDataAttributeName(driver, webDriverWait, "objectsList");
        Assert.assertTrue(objectsList.isNoData());
    }

}
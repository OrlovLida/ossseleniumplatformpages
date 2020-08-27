package com.oss;

import com.oss.framework.alerts.SystemMessageContainer;
import com.oss.framework.prompts.ConfirmationBox;
import com.oss.framework.prompts.ConfirmationBoxInterface;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.tablewidget.OldTable;
import com.oss.framework.widgets.tablewidget.TableInterface;
import com.oss.framework.widgets.tabswidget.TabWindowWidget;
import com.oss.framework.widgets.tabswidget.TabsInterface;
import com.oss.pages.physical.LocationWizardPage;
import com.oss.pages.platform.HomePage;
import com.oss.pages.platform.LocationOverviewPage;
import com.oss.pages.platform.SideMenuPage;
import com.oss.utils.RandomGenerator;
import com.oss.utils.TestListener;
import io.qameta.allure.Description;
import org.openqa.selenium.By;
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

    @BeforeMethod
    public void goToInventoryView() {
        HomePage homePage = HomePage.goToHomePage(driver, BASIC_URL);
    }


    @Test(groups = {"Physical tests"})
    @Description("The user creates a location (Site) from left side menu and checks the message about successful creation")
    public void tS01CreateNewSiteSideMenu() {

        new SideMenuPage(driver)
                .chooseCreateLocation()
                .createLocation(locationTypeSite, randomLocationName);
        Assert.assertTrue(SystemMessageContainer.create(driver, webDriverWait)
                .getMessages().get(0).getText().contains("Location has been created successfully"));
    }

    @Test(groups = {"Physical tests"})
    @Description("The user filters the created Site in Inventory View for Location and checks if properties table contains Site name")
    public void tS02BrowseLocationInInventoryView() {
        new HomePage(driver)
                .typeObjectType(objectTypeLocation)
                .confirmObjectType(objectTypeLocation)
                .filterObjectName(randomLocationName, "Location");
        TableInterface propertiesTable = OldTable.createByComponentDataAttributeName(driver, webDriverWait, "properties(Location)");
        int rowNumber = propertiesTable.getRowNumber(randomLocationName, "Property Value");
        String rowValue = propertiesTable.getValueCell(rowNumber, "Property Value");
        Assert.assertTrue(rowValue.contains(randomLocationName));
    }

    @Test(groups = {"Physical tests"})
    @Description("The user filters the created Site in Inventory View for Sites and checks if properties table contains Site name")
    public void tS03BrowseSiteInInventoryView() {
        new HomePage(driver)
                .typeObjectType(locationTypeSite)
                .confirmObjectType(locationTypeSite)
                .filterObjectName(randomLocationName, "Site");
        TableInterface propertiesTable = OldTable.createByComponentDataAttributeName(driver, webDriverWait, "properties(Site)");
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

        new HomePage(driver)
                .typeObjectType(locationTypeSite)
                .confirmObjectType(locationTypeSite)
                .filterObjectName(randomLocationName, "Site")
                .expandShowOnLocationOverview()
                .clickCreateLocation()
                .createLocation(locationTypeSite, randomLocationNameInLocation);
        new LocationOverviewPage(driver)
                .selectLocationTab()
                .filterObjectName(randomLocationNameInLocation);
        TableInterface locationsTable = OldTable.createByComponentDataAttributeName(driver, webDriverWait, "tableAppLocationsId");
        int rowNumber = locationsTable.getRowNumber(randomLocationNameInLocation, "Name");
        String rowValue = locationsTable.getValueCell(rowNumber, "Name");
        Assert.assertTrue(rowValue.contains(randomLocationNameInLocation));
    }

    @Test(groups = {"Physical tests"})
    @Description("The user creates a Site in the created Site in Location Overview, then edits the Site and checks if the description is updated in Locations table")
    public void tS06CreateAndModifySiteInLocation() {
        String randomLocationNameInLocation = RandomGenerator.generateRandomName();

        new HomePage(driver)
                .typeObjectType(locationTypeSite)
                .confirmObjectType(locationTypeSite)
                .filterObjectName(randomLocationName, "Site")
                .expandShowOnLocationOverview()
                .clickCreateLocation()
                .createLocation(locationTypeSite, randomLocationNameInLocation);
        new LocationOverviewPage(driver)
                .selectLocationTab()
                .filterObjectName(randomLocationNameInLocation)
                .clickEditLocationIcon()
                .typeDescription(description);
        new LocationWizardPage(driver)
                .accept();
        TableInterface locationsTable = OldTable.createByComponentDataAttributeName(driver, webDriverWait, "tableAppLocationsId");
        int rowNumber = locationsTable.getRowNumber(description, "Description");
        String rowValue = locationsTable.getValueCell(rowNumber, "Description");
        Assert.assertTrue(rowValue.contains(description));
    }

    @Test(groups = {"Physical tests"})
    @Description("The user creates a Site in the created Site in Location Overview and checks if the row is removed in Locations table")
    public void tS07CreateAndRemoveSiteInLocation() {
        String randomLocationNameInLocation = RandomGenerator.generateRandomName();

        new HomePage(driver)
                .typeObjectType(locationTypeSite)
                .confirmObjectType(locationTypeSite)
                .filterObjectName(randomLocationName, "Site")
                .expandShowOnLocationOverview()
                .clickCreateLocation()
                .createLocation(locationTypeSite, randomLocationNameInLocation);
        new LocationOverviewPage(driver)
                .selectLocationTab()
                .filterObjectName(randomLocationNameInLocation)
                .clickRemoveLocationIcon();
        ConfirmationBoxInterface confirmationBox = ConfirmationBox.create(driver, webDriverWait);
        confirmationBox.clickButtonByLabel("Delete");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        TabsInterface locationsTable = TabWindowWidget.create(driver, webDriverWait);
        Assert.assertTrue(locationsTable.isNoData("tableAppLocationsId"));
    }

}


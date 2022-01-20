package com.oss.ThreeUKProject;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.alerts.SystemMessageContainer;
import com.oss.framework.prompts.ConfirmationBox;
import com.oss.framework.prompts.ConfirmationBoxInterface;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.tabswidget.TabWindowWidget;
import com.oss.framework.widgets.tabswidget.TabsInterface;
import com.oss.pages.physical.LocationOverviewPage;
import com.oss.pages.physical.LocationOverviewPage.TabName;
import com.oss.pages.physical.LocationWizardPage;
import com.oss.pages.platform.HomePage;
import com.oss.pages.platform.OldInventoryView.OldInventoryViewPage;
import com.oss.repositories.AddressRepository;
import com.oss.repositories.LocationInventoryRepository;
import com.oss.untils.Environment;
import com.oss.utils.RandomGenerator;
import com.oss.utils.TestListener;

import io.qameta.allure.Description;

/**
 * @author Milena Miętkiewicz
 */

@Listeners({TestListener.class})
public class PhysicalRegressionTests extends BaseTestCase {

    private static final String locationName = "LocationSeleniumTests" + (int) (Math.random() * 10000);
    private static final String countryName = "CountrySeleniumTests";
    private static final String postalCodeName = "PostalCodeSeleniumTests";
    private static final String regionName = "RegionSeleniumTests";
    private static final String districtName = "DistrictSeleniumTests";
    private static final String cityName = "CitySeleniumTests";
    private static final String subLocationSiteName = "SubLocationSeleniumTests" + (int) (Math.random() * 10000);
    private static final String objectTypeLocation = "Location";
    private static final String locationTypeSite = "Site";
    private static final String description = "Selenium Test";
    private static String locationId;
    private static Long addressId;
    private Environment env = Environment.getInstance();

    @BeforeClass
    public void createTestData() {
        getOrCreateAddress();
        createPhysicalLocation();
        createSubLocation();
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
                .filterObject("Name", locationName);
        Assert.assertTrue((new OldInventoryViewPage(driver).getProperties("properties(" + objectTypeLocation + ")").get("Name")).contains(locationName));
    }

    @Test(groups = {"Physical tests"})
    @Description("The user filters Site in Inventory View for Sites and checks if properties table contains Site name")
    public void tS03BrowseSiteInInventoryView() {

        homePage.setOldObjectType(locationTypeSite);
        new OldInventoryViewPage(driver)
                .filterObject("Name", locationName);
        Assert.assertTrue((new OldInventoryViewPage(driver).getProperties("properties(" + locationTypeSite + ")").get("Name")).contains(locationName));
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
                .filterObject("Name", locationName)
                .expandShowOnAndChooseView("OpenLocationOverviewAction");
        new LocationOverviewPage(driver)
                .clickButton("Create Location");
        new LocationWizardPage(driver)
                .createLocation(locationTypeSite, randomSubLocationName);
        new LocationOverviewPage(driver)
                .selectTab("Locations")
                .filterObjectInSpecificTab(TabName.LOCATIONS, "Name", randomSubLocationName);
        Assert.assertTrue(new LocationOverviewPage(driver).getValueByRowNumber(TabName.LOCATIONS, "Name", 0).contains(randomSubLocationName));
    }

    @Test(groups = {"Physical tests"})
    @Description("The user edits sublocation (Site) and checks if the description is updated in Locations table")
    public void tS06ModifySubLocationSite() {

        homePage.setOldObjectType(locationTypeSite);
        new OldInventoryViewPage(driver)
                .filterObject("Name", locationName)
                .expandShowOnAndChooseView("OpenLocationOverviewAction");
        new LocationOverviewPage(driver)
                .selectTab("Locations")
                .filterObjectInSpecificTab(TabName.LOCATIONS, "Name", subLocationSiteName)
                .clickButtonByLabelInSpecificTab(TabName.LOCATIONS, "Edit Location");
        new LocationWizardPage(driver)
                .setDescription(description)
                .accept();
        Assert.assertTrue(new LocationOverviewPage(driver).getValueByRowNumber(TabName.LOCATIONS, "Description", 0).contains(description));
    }

    @Test(groups = {"Physical tests"})
    @Description("The user deletes sublocation (Site) and checks if the row is removed in Locations table")
    public void tS07RemoveSubLocationSite() {

        homePage.setOldObjectType(locationTypeSite);
        new OldInventoryViewPage(driver)
                .filterObject("Name", locationName)
                .expandShowOnAndChooseView("OpenLocationOverviewAction");
        new LocationOverviewPage(driver)
                .selectTab("Locations")
                .filterObjectInSpecificTab(TabName.LOCATIONS, "Name", subLocationSiteName)
                .clickButtonByLabelInSpecificTab(TabName.LOCATIONS, "Remove Location");
        ConfirmationBoxInterface confirmationBox = ConfirmationBox.create(driver, webDriverWait);
        confirmationBox.clickButtonByLabel("Delete");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        TabsInterface tabTable = TabWindowWidget.create(driver, webDriverWait);
        Assert.assertTrue(tabTable.hasNoData("tableAppLocationsId"));
    }

    private void getOrCreateAddress() {
        AddressRepository addressRepository = new AddressRepository(env);
        addressId = addressRepository.updateOrCreateAddress(countryName, postalCodeName, regionName, cityName, districtName);
    }

    private void createPhysicalLocation() {
        LocationInventoryRepository locationInventoryRepository = new LocationInventoryRepository(env);
        locationId = locationInventoryRepository.createLocation(locationName, locationTypeSite, addressId);
    }

    private void createSubLocation() {
        LocationInventoryRepository locationInventoryRepository = new LocationInventoryRepository(env);
        locationInventoryRepository.createLocationInLocation(locationTypeSite, subLocationSiteName, addressId, Long.valueOf(locationId), locationTypeSite);
    }

}
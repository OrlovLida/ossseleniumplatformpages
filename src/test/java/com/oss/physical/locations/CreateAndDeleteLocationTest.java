package com.oss.physical.locations;

import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.SystemMessageInterface;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.physical.LocationWizardPage;
import com.oss.pages.platform.HierarchyViewPage;
import com.oss.pages.platform.NewInventoryViewPage;
import com.oss.repositories.AddressRepository;
import com.oss.repositories.LocationInventoryRepository;
import com.oss.untils.Environment;
import io.qameta.allure.Step;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.oss.framework.components.alerts.SystemMessageContainer.*;

public class CreateAndDeleteLocationTest extends BaseTestCase {
    private static final String BUILDING_COMPLEX_TYPE = "Building Complex";
    private static final String BUILDING_COMPLEX_NAME = "selenium test bu_comp_";
    private static final String BUILDING_COMPLEX_ABBREVIATION = "BU COM";
    private static final String BUILDING_COMPLEX_LATITUDE = "51";
    private static final String BUILDING_COMPLEX_LONGITUDE = "17";
    private static final String BUILDING_COMPLEX_DESCRIPTION = "description";
    private static final String BUILDING_COMPLEX_REMARKS = "remarks";
    private final static String PROPERTY_PANEL_ID = "PropertyPanelWidget";
    private SoftAssert softAssert;
    private LocationWizardPage locationWizard;
    private HierarchyViewPage hierarchyViewPage;
    private NewInventoryViewPage newInventoryViewPage;
    private final Environment env = Environment.getInstance();
    private Long geographicalAddressId;
    private static String addressName;
    private AddressRepository addressRepository;
    private LocationInventoryRepository locationInventoryRepository;
    private static final String ID_PATTERN = "id=[\\d]+";
    private static final Pattern PATTERN = Pattern.compile(ID_PATTERN);

    @BeforeClass
    public void init() {
        hierarchyViewPage = HierarchyViewPage.getHierarchyViewPage(driver, webDriverWait);
        softAssert = new SoftAssert();
        addressRepository = new AddressRepository(env);
        locationInventoryRepository = new LocationInventoryRepository(env);
    }

    @Test(priority = 1)
    public void openLocationWizardFromLeftSideMenu() {
        openCreatePhysicalLocationWizardFromLeftSideMenu();
    }

    @Test(priority = 2)
    public void setLocationType() {
        locationWizard = new LocationWizardPage(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        locationWizard.setLocationType(BUILDING_COMPLEX_TYPE);
    }

    @Test(priority = 3)
    public void setLocationSimpleAttributes() {
        locationWizard.setLocationName(BUILDING_COMPLEX_NAME + new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").format(Calendar.getInstance().getTime()));
        locationWizard.setLocationAbbreviation(BUILDING_COMPLEX_ABBREVIATION);
        locationWizard.setLocationLatitude(BUILDING_COMPLEX_LATITUDE);
        locationWizard.setLocationLongitude(BUILDING_COMPLEX_LONGITUDE);
        locationWizard.setDescription(BUILDING_COMPLEX_DESCRIPTION);
        locationWizard.setLocationRemarks(BUILDING_COMPLEX_REMARKS);
        locationWizard.clickNext();
    }

    @Test(priority = 4)
    public void searchAndSetGeographicalAddressName() {
        getFirstGeographicalAddressId();
        locationWizard.setFirstGeographicalAddress(String.valueOf(geographicalAddressId));
        getGeographicalAddressName();
        locationWizard.clickNext();
    }

    @Test(priority = 5)
    public void submitLocationWizard() {
        locationWizard.create();
    }

    @Test(priority = 6)
    public void openHierarchyViewFromViewLink() {
        checkMessageTextForCreatedLocation();
        clickSystemMessageLink();
        closeSystemMessage();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 7)
    public void moveToNewInventoryView() {
        hierarchyViewPage.selectFirstObject();
        hierarchyViewPage.callAction(ActionsContainer.SHOW_ON_GROUP_ID, "InventoryView");
    }

    @Test(priority = 8)
    public void checkLocationAttributes() {
        newInventoryViewPage = new NewInventoryViewPage(driver, webDriverWait);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        newInventoryViewPage.selectFirstRow();
        searchLocationNameByValue();
        searchLocationTypeByValue();
        searchLocationLatitudeByValue();
        searchLocationLongitudeByValue();
        searchLocationAbbreviationByValue();
        searchLocationDescriptionByValue();
        searchLocationRemarksByValue();
        searchLocationAddressByValue();
    }

    @Test(priority = 9, description = "Checking system message summary")
    public void systemMessageSummary() {
        softAssert.assertAll();
    }

    @AfterClass
    public void deleteLocation() {
        Long locationToDelete = getCreatedLocationIdFromURL();
        locationInventoryRepository.deleteLocation(locationToDelete, BUILDING_COMPLEX_TYPE);
    }

    @Step("Open Create Location Wizard from Left Side Menu")
    private void openCreatePhysicalLocationWizardFromLeftSideMenu() {
        homePage.chooseFromLeftSideMenu("Create Physical Location", "Infrastructure Management", "Create Infrastructure");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Step("Get first geographical address Id for created location")
    private void getFirstGeographicalAddressId() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        geographicalAddressId = addressRepository.getFirstGeographicalAddressId();
    }

    @Step("Get name of geographical address by Id for created location")
    private void getGeographicalAddressName() {
        addressName = addressRepository.getGeographicalAddressName(geographicalAddressId);
    }

    @Step("Check message text for created Location")
    private void checkMessageTextForCreatedLocation() {
        SystemMessageInterface systemMessage = create(driver, webDriverWait);
        List<Message> messages = systemMessage.getMessages();
        softAssert.assertEquals(messages.size(), 1, "There is no single message");
        softAssert.assertEquals(messages.get(0).getMessageType(), MessageType.SUCCESS, "There is no successful message");
        softAssert.assertTrue(messages.get(0).getText().contains("Location has been created successfully, click here to open Hierarchy View."), "Returned message contains different content.");
    }

    @Step("Click system message link")
    private void clickSystemMessageLink() {
        create(driver, webDriverWait).clickMessageLink();
    }

    @Step("Close system message")
    private void closeSystemMessage() {
        create(driver, webDriverWait).close();
    }

    @Step("Verification of Location Name in Property Panel")
    public void searchLocationNameByValue() {
        String name = newInventoryViewPage.getPropertyPanel(PROPERTY_PANEL_ID).getPropertyValue("name");
        Assert.assertTrue(name.contains("selenium test bu_comp_"), "Name was not calculated correctly.");
    }

    @Step("Verification of Location Type in Property Panel")
    private void searchLocationTypeByValue() {
        String name = newInventoryViewPage.getPropertyPanel(PROPERTY_PANEL_ID).getPropertyValue("type");
        Assert.assertEquals(name, "BuildingComplex", "There is a different type of location.");
    }

    @Step("Verification of Location Latitude in Property Panel")
    private void searchLocationLatitudeByValue() {
        String name = newInventoryViewPage.getPropertyPanel(PROPERTY_PANEL_ID).getPropertyValue(("latitude"));
        Assert.assertEquals(name, "51", "There is a different value of latitude.");
    }

    @Step("Verification of Location Longitude in Property Panel")
    private void searchLocationLongitudeByValue() {
        String name = newInventoryViewPage.getPropertyPanel(PROPERTY_PANEL_ID).getPropertyValue(("longitude"));
        Assert.assertEquals(name, "17", "There is a different value of longitude.");
    }

    @Step("Verification of Location Remarks in Property Panel")
    private void searchLocationRemarksByValue() {
        String name = newInventoryViewPage.getPropertyPanel(PROPERTY_PANEL_ID).getPropertyValue(("remarks"));
        Assert.assertEquals(name, "remarks", "There is a different value of remarks.");
    }

    @Step("Verification of Location Description in Property Panel")
    private void searchLocationDescriptionByValue() {
        String name = newInventoryViewPage.getPropertyPanel(PROPERTY_PANEL_ID).getPropertyValue(("description"));
        Assert.assertEquals(name, "description", "There is a different value of description.");
    }

    @Step("Verification of Location Address in Property Panel")
    private void searchLocationAddressByValue() {
        String name = newInventoryViewPage.getPropertyPanel(PROPERTY_PANEL_ID).getPropertyValue(("address.name"));
        Assert.assertEquals(name, addressName, "There is a different address than one specified in wizard.");
    }

    @Step("Verification of Location Abbreviation in Property Panel")
    private void searchLocationAbbreviationByValue() {
        String name = newInventoryViewPage.getPropertyPanel(PROPERTY_PANEL_ID).getPropertyValue(("abbreviation"));
        Assert.assertEquals(name, "BU COM", "There is a different value of abbreviation.");
    }

    @Step
    private static Set<Long> getIdFromString(String string) {
        Set<Long> ids = new HashSet<>();
        Matcher matcher = PATTERN.matcher(string);
        while (matcher.find()) {
            String stringId = matcher.group();
            Long id = Long.parseLong(stringId.split("=")[1]);
            ids.add(id);
        }
        return ids;
    }

    @Step
    private Long getCreatedLocationIdFromURL() {
        return getIdFromString(driver.getCurrentUrl()).stream()
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Couldn't extract object id from URL"));
    }
}



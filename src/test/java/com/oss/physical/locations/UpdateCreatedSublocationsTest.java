package com.oss.physical.locations;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.NoSuchElementException;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.comarch.oss.web.pages.HierarchyViewPage;
import com.comarch.oss.web.pages.NewInventoryViewPage;
import com.comarch.oss.web.pages.SearchObjectTypePage;
import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.alerts.SystemMessageInterface;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.mainheader.PerspectiveChooser;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.advancedsearch.AdvancedSearchWidget;
import com.oss.framework.widgets.propertypanel.PropertyPanel;
import com.oss.pages.physical.SublocationWizardPage;
import com.oss.repositories.AddressRepository;
import com.oss.repositories.LocationInventoryRepository;
import com.oss.repositories.PlanningRepository;
import com.oss.repositories.entities.Sublocation;
import com.oss.untils.Environment;

import io.qameta.allure.Description;
import io.qameta.allure.Step;

import static com.oss.framework.components.alerts.SystemMessageContainer.create;

public class UpdateCreatedSublocationsTest extends BaseTestCase {
    private static final String dateAndTime = new SimpleDateFormat("dd-MM-yyyy_HH:mm").format(Calendar.getInstance().getTime());
    private static final String time = dateAndTime.substring(11);

    private static final String COUNTRY_NAME = "country_kk" + time;
    private static final String REGION_NAME = "region_kk";
    private static final String DISTRICT_NAME = "district_kk";
    private static final String CITY_NAME = "city_kk" + dateAndTime;
    private static final String POSTAL_CODE_NAME = "12-345";
    private static final String LOCATION_NAME = "selenium_test_bu_" + dateAndTime;
    private static final String INDOOR_CABINET_NAME = "selenium_test_indoor_cabinet_";
    private static final String DUCT_ENTRY_NAME = "selenium_test_duct_entry_";
    private static final String RACK_NAME = "selenium_test_rack_";
    private static final String SUBLOCATION_NAME = "edited_name!";
    private static final String RACK_MODEL_MANUFACTURER_MODEL_NAME = "Generic";

    private static final String LOCATION_TYPE = "Building";
    private static final String INDOOR_CABINET_TYPE = "IndoorCabinet";
    private static final String DUCT_ENTRY_TYPE = "DuctEntry";
    private static final String RACK_TYPE = "Rack";
    private static final String RACK_MODEL_TYPE = "RackModel";
    private static final String RACK_MODEL_NAME = "LiSA FA 600";
    private static final String INDOOR_CABINET_MODEL_TYPE = "IndoorCabinetModel";
    private static final String OBJECT_TYPE = "Physical Location";

    private static final String DESCRIPTION_VALUE = "description";
    private static final String REMARKS_VALUE = "remarks";
    private static final String DIMENSION_VALUE = "2.0447";
    private static final String RACK_MODEL_VALUE = "19\" 46U 1000x1000 (Bottom-Up)";
    private static final String RACK_MODEL_HEIGHT_VALUE = "2.0447";
    private static final String RACK_MODEL_WIDTH_VALUE = "1";
    private static final String RACK_MODEL_DEPTH_VALUE = "1";

    private static final String ADVANCED_SEARCH_ID = "advancedSearch";
    private static final String INVENTORY_VIEW_ID = "InventoryView";
    private static final String HIERARCHY_VIEW_ID = "Hierarchy View";
    private static final String RESOURCE_INVENTORY_ID = "Resource Inventory";
    private static final String EDIT_SUBLOCATION_ID = "UpdateSublocationWizardAction";
    private static final String HEIGHT_ID = "height";
    private static final String WIDTH_ID = "width";
    private static final String DEPTH_ID = "depth";
    private static final String NAME_ID = "name";
    private static final String DESCRIPTION_ID = "description";
    private static final String REMARKS_ID = "remarks";
    private static final String PROPERTY_PANEL_WIDGET_ID = "PropertyPanelWidget";
    private static final String MODEL_MANUFACTURER_ID = "model.manufacturer";
    private static final String MODEL_NAME_ID = "model.name";
    private static final String ID_DATA_COL_ID = "id";

    private static final String DUCT_ENTRY_PATH = "%d.locations.duct entry.%d";
    private static final String INDOOR_CABINET_PATH = "%d.locations.indoor cabinet.%d";
    private static final String RACK_PATH = "%d.locations.rack.%d";

    private static final String SUBLOCATION_UPDATED_MESSAGE = "Sublocation has been updated successfully, click here to open Hierarchy View.";
    private static final String DIFFERENT_DESCRIPTION_MESSAGE = "There is a different value of description than edited value: ";
    private static final String DIFFERENT_REMARKS_MESSAGE = "There is a different value of remarks than edited value: ";
    private static final String INCORRECT_HEIGHT_VALUES_MESSAGE = "Compared height values are equal.";
    private static final String INCORRECT_DEPTH_VALUES_MESSAGE = "Compared depht values are equal.";
    private static final String INCORRECT_WIDTH_VALUES_MESSAGE = "Compared width values are equal.";
    private static final String DIFFERENT_HEIGHT_MESSAGE = "There is a different value of height than edited value: ";
    private static final String DIFFERENT_DEPTH_MESSAGE = "There is a different value of depth than edited value: ";
    private static final String DIFFERENT_WIDTH_MESSAGE = "There is a different value of width than edited value: ";
    private static final String DIFFERENT_NAME_MESSAGE = "There is a different name of sublocation than edited value: ";
    private static final String DIFFERENT_MANUFACTURER_MODEL_NAME_MESSAGE = "There is a different value of Rack Manufacturer Model Name than edited value: ";
    private static final String DIFFERENT_MODEL_NAME_MESSAGE = "There is a different componentValue of Rack Model Name than edited value: ";
    private static final String HIERARCHY_VIEW_HAS_SOME_DATA_MESSAGE = "There are some data on View.";
    private static final String NO_SINGLE_MESSAGE = "There is no single message";
    private static final String NO_SUCCESSFUL_MESSAGE = "There is no successful message";
    private static final String DIFFERENT_CONTENT_MESSAGE = "Returned message contains different content.";
    private static final String SUBLOCATION_NODE_NOT_VISIBLE_ON_HIERARCHY_VIEW_MESSAGE = "Sublocation node is not visible on Hierarchy View";
    private static final String SUBLOCATION_MODEL_NOT_FOUND_MESSAGE = "Model was not found: %s";

    private Long geographicalAddressId;
    private Long locationId;
    private Long indoorCabinetModelId;
    private Long rackModelId;
    private Long indoorCabinetId;
    private Long ductEntryId;
    private Long rackId;

    private AddressRepository addressRepository;
    private LocationInventoryRepository locationInventoryRepository;
    private PlanningRepository planningRepository;

    private NewInventoryViewPage newInventoryViewPage;
    private HierarchyViewPage hierarchyViewPage;

    private SoftAssert softAssert;

    private final Environment env = Environment.getInstance();

    @BeforeClass
    public void init() {
        setLivePerspective();
        initRepositories();
        initPages();
        createGeographicalAddress();
        createLocation();
        getIndoorCabinetModelId();
        getRackModelId();
        createIndoorCabinet();
        createRack();
        createDuctEntry();
    }

    @Test(priority = 1)
    @Description("Open Search Object field for Hierarchy View")
    public void openSearchFieldOfHierarchyViewFromLeftSideMenu() {
        homePage.chooseFromLeftSideMenu(HIERARCHY_VIEW_ID, RESOURCE_INVENTORY_ID);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 2, dependsOnMethods = {"openSearchFieldOfHierarchyViewFromLeftSideMenu"})
    @Description("Search for Physical Location in search object type and go to Advanced Search of HV")
    public void searchForPhysicalLocationInSearchObjectTypePageAndClickOpenToMoveOnAdvancedSearchOfHV() {
        SearchObjectTypePage searchObjectTypePage = new SearchObjectTypePage(driver, webDriverWait);
        searchObjectTypePage.searchType(OBJECT_TYPE);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 3, dependsOnMethods = {"searchForPhysicalLocationInSearchObjectTypePageAndClickOpenToMoveOnAdvancedSearchOfHV"})
    @Description("Find location by Id in advanced search and load Hierarchy View if it is possible")
    public void findLocationByIdAndLoadHierarchyViewForItIfThereIsVisibleData() {
        AdvancedSearchWidget advancedSearchWidget = AdvancedSearchWidget.createById(driver, webDriverWait, ADVANCED_SEARCH_ID);
        advancedSearchWidget.setFilter(ID_DATA_COL_ID, locationId.toString());
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        advancedSearchWidget.getTableComponent().selectRow(0);
        advancedSearchWidget.clickAdd();

        Assert.assertFalse(hierarchyViewPage.hasNoData(), HIERARCHY_VIEW_HAS_SOME_DATA_MESSAGE);

    }

    @Test(priority = 4, dependsOnMethods = {"findLocationByIdAndLoadHierarchyViewForItIfThereIsVisibleData"})
    @Description("Check if sublocation nodes are present, then open Edit Sublocation Wizard, update some attributes of each Sublocations and check them on Property Panel of HV")
    public void ifSublocationNodesArePresentThenUpdateSublocationsAttributesInWizardsOnHierarchyView() {
        updateSublocationsAttributesByWizard();
    }

    @Test(priority = 5, dependsOnMethods = {"ifSublocationNodesArePresentThenUpdateSublocationsAttributesInWizardsOnHierarchyView"})
    @Description("Check updated attributes of each sublocation in Property Panel on Hierarchy View")
    public void checkUpdatedAttributesOfSublocationsInPropertyPanelOnHierarchyView() {
        checkUpdatedAttributesOfSublocationInPropertyPanelOnHierarchyView();
    }

    @Test(priority = 6, dependsOnMethods = {"checkUpdatedAttributesOfSublocationsInPropertyPanelOnHierarchyView"})
    @Description("Open Inventory View for selected Sublocations If Inventory View is not empty")
    public void selectAllSublocationNodesAndOpenThemInInventoryViewIfViewIsNotEmpty() {
        selectNodesOfEachSublocationAndOpenThemInInventoryView();
    }

    @Test(priority = 7, dependsOnMethods = {"selectAllSublocationNodesAndOpenThemInInventoryViewIfViewIsNotEmpty"})
    @Description("Check Edited Attributes of Indoor Cabinet on IV")
    public void checkUpdatedAttributesOfSublocationsOnInventoryView() {
        checkUpdatedAttributesOfSublocationOnPropertyPanelInInventoryView();
    }

    @AfterClass
    public void deleteCreatedObjects() {
        deleteCreatedSublocations();
        deletePhysicalLocation();
        deleteGeographicalAddress();
    }

    @Step("Set perspective to LIVE")
    private void setLivePerspective() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        PerspectiveChooser.create(driver, webDriverWait).setLivePerspective();
    }

    @Step("Initialize repositories")
    private void initRepositories() {
        addressRepository = new AddressRepository(env);
        locationInventoryRepository = new LocationInventoryRepository(env);
        planningRepository = new PlanningRepository(Environment.getInstance());
    }

    @Step("Initialize pages")
    public void initPages() {
        hierarchyViewPage = HierarchyViewPage.getHierarchyViewPage(driver, webDriverWait);
        newInventoryViewPage = NewInventoryViewPage.getInventoryViewPage(driver, webDriverWait);
    }

    @Step("Create Geographical Address by API")
    private void createGeographicalAddress() {
        geographicalAddressId = addressRepository.updateOrCreateAddress(COUNTRY_NAME, POSTAL_CODE_NAME, REGION_NAME, CITY_NAME, DISTRICT_NAME);
    }

    @Step("Create Physical Location by API")
    private void createLocation() {
        locationId = Long.valueOf(locationInventoryRepository.createLocation(LOCATION_NAME, LOCATION_TYPE, geographicalAddressId));
    }

    @Step("Get First Id of Indoor Cabinet Model")
    private void getIndoorCabinetModelId() {
        indoorCabinetModelId = planningRepository.getFirstObjectWithType(INDOOR_CABINET_MODEL_TYPE);
    }

    @Step("Get First Id of Rack Model")
    private void getRackModelId() {
        rackModelId = planningRepository.getObjectIdByTypeAndName(RACK_MODEL_TYPE, RACK_MODEL_NAME)
                .orElseThrow(() -> new NoSuchElementException(String.format(SUBLOCATION_MODEL_NOT_FOUND_MESSAGE, RACK_MODEL_NAME)));
    }

    @Step("Create IndoorCabinet by API")
    private void createIndoorCabinet() {
        Sublocation sublocation = Sublocation.builder()
                .withSubLocationType(INDOOR_CABINET_TYPE)
                .withSubLocationName(INDOOR_CABINET_NAME + dateAndTime)
                .withPreciseLocation(locationId)
                .withPreciseLocationType(LOCATION_TYPE)
                .withParentLocationId(locationId)
                .withParentLocationType(LOCATION_TYPE)
                .withSublocationModelId(indoorCabinetModelId)
                .withSublocationModelType(INDOOR_CABINET_MODEL_TYPE)
                .build();

        indoorCabinetId = locationInventoryRepository.createSubLocation(sublocation);
    }

    @Step("Create Rack by API")
    private void createRack() {
        Sublocation sublocation = Sublocation.builder()
                .withSubLocationType(RACK_TYPE)
                .withSubLocationName(RACK_NAME + dateAndTime)
                .withPreciseLocation(locationId)
                .withPreciseLocationType(LOCATION_TYPE)
                .withParentLocationId(locationId)
                .withParentLocationType(LOCATION_TYPE)
                .withSublocationModelId(rackModelId)
                .withSublocationModelType(RACK_MODEL_TYPE)
                .build();

        rackId = locationInventoryRepository.createSubLocation(sublocation);
    }

    @Step("Create DuctEntry by API")
    private void createDuctEntry() {
        Sublocation sublocation = Sublocation.builder()
                .withSubLocationType(DUCT_ENTRY_TYPE)
                .withSubLocationName(DUCT_ENTRY_NAME + dateAndTime)
                .withPreciseLocation(locationId)
                .withPreciseLocationType(LOCATION_TYPE)
                .withParentLocationId(locationId)
                .withParentLocationType(LOCATION_TYPE)
                .build();

        ductEntryId = locationInventoryRepository.createSubLocation(sublocation);
    }

    @Step("Create paths for each Sublocation under Building and return Ids of them")
    private String getSublocationsPath(Long sublocationId) {
        if (sublocationId.equals(ductEntryId)) {
            return String.format(DUCT_ENTRY_PATH, locationId, ductEntryId);
        } else if (sublocationId.equals(indoorCabinetId)) {
            return String.format(INDOOR_CABINET_PATH, locationId, indoorCabinetId);
        } else if (sublocationId.equals(rackId)) {
            return String.format(RACK_PATH, locationId, rackId);
        }
        return String.valueOf(sublocationId);
    }

    @Step("Open Edit Sublocation Wizard and update some attributes of them")
    private void updateSublocationsAttributesByWizard() {
        softAssert = new SoftAssert();

        Long[] sublocationIds = {ductEntryId, indoorCabinetId, rackId};

        for (Long id : sublocationIds) {
            Assert.assertTrue(hierarchyViewPage.isNodePresentByPath(getSublocationsPath(id)), SUBLOCATION_NODE_NOT_VISIBLE_ON_HIERARCHY_VIEW_MESSAGE);

            hierarchyViewPage.selectNodeByPath(getSublocationsPath(id));
            hierarchyViewPage.callAction(ActionsContainer.EDIT_GROUP_ID, EDIT_SUBLOCATION_ID);
            DelayUtils.waitForPageToLoad(driver, webDriverWait);

            SublocationWizardPage sublocation = new SublocationWizardPage(driver);

            sublocation.setDescription(DESCRIPTION_VALUE);
            sublocation.setRemarks(REMARKS_VALUE);

            if (!id.equals(rackId)) {
                sublocation.setWidth(DIMENSION_VALUE);
                sublocation.setDepth(DIMENSION_VALUE);
                sublocation.setHeight(DIMENSION_VALUE);
            }

            if (id.equals(rackId)) {
                String heightValue = sublocation.getHeight();
                String widthValue = sublocation.getWidth();
                String depthValue = sublocation.getDepth();
                sublocation.setSublocationName(SUBLOCATION_NAME + DIMENSION_VALUE);
                sublocation.setSublocationModel(RACK_MODEL_VALUE);
                DelayUtils.waitForPageToLoad(driver, webDriverWait);
                String editedHeightValue = sublocation.getHeight();
                String editedWidthValue = sublocation.getWidth();
                String editedDepthValue = sublocation.getDepth();

                softAssert.assertNotEquals(heightValue, editedHeightValue, INCORRECT_HEIGHT_VALUES_MESSAGE);
                softAssert.assertNotEquals(widthValue, editedWidthValue, INCORRECT_WIDTH_VALUES_MESSAGE);
                softAssert.assertNotEquals(depthValue, editedDepthValue, INCORRECT_DEPTH_VALUES_MESSAGE);
            }
            sublocation.clickAccept();

            checkMessageTextForUpdatedSublocation();

            hierarchyViewPage.unselectNodeByPath(getSublocationsPath(id));
        }

        softAssert.assertAll();
    }

    @Step("Check Attributes of edited Sublocation on Property Panel of HV")
    private void checkUpdatedAttributesOfSublocationInPropertyPanelOnHierarchyView() {
        Long[] sublocationIds = {ductEntryId, indoorCabinetId, rackId};

        softAssert = new SoftAssert();

        for (Long id : sublocationIds) {
            hierarchyViewPage.selectNodeByPath(getSublocationsPath(id));

            checkSublocationAttributeByValue(DESCRIPTION_VALUE, DESCRIPTION_ID, DIFFERENT_DESCRIPTION_MESSAGE);
            checkSublocationAttributeByValue(REMARKS_VALUE, REMARKS_ID, DIFFERENT_REMARKS_MESSAGE);

            if (!id.equals(rackId)) {
                checkSublocationAttributeByValue(DIMENSION_VALUE, HEIGHT_ID, DIFFERENT_HEIGHT_MESSAGE);
                checkSublocationAttributeByValue(DIMENSION_VALUE, WIDTH_ID, DIFFERENT_WIDTH_MESSAGE);
                checkSublocationAttributeByValue(DIMENSION_VALUE, DEPTH_ID, DIFFERENT_DEPTH_MESSAGE);
            }


            if (id.equals(rackId)) {
                checkSublocationAttributeByValue(RACK_MODEL_HEIGHT_VALUE, HEIGHT_ID, DIFFERENT_HEIGHT_MESSAGE);
                checkSublocationAttributeByValue(RACK_MODEL_WIDTH_VALUE, WIDTH_ID, DIFFERENT_WIDTH_MESSAGE);
                checkSublocationAttributeByValue(RACK_MODEL_DEPTH_VALUE, DEPTH_ID, DIFFERENT_DEPTH_MESSAGE);
                checkSublocationAttributeByValue(RACK_MODEL_MANUFACTURER_MODEL_NAME, MODEL_MANUFACTURER_ID, DIFFERENT_MANUFACTURER_MODEL_NAME_MESSAGE);
                checkSublocationAttributeByValue(RACK_MODEL_VALUE, MODEL_NAME_ID, DIFFERENT_MODEL_NAME_MESSAGE);
                checkSublocationAttributeByValue(SUBLOCATION_NAME + DIMENSION_VALUE, NAME_ID, DIFFERENT_NAME_MESSAGE);
            }

            hierarchyViewPage.unselectNodeByPath(getSublocationsPath(id));
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
        }
        softAssert.assertAll();
    }

    private void selectNodesOfEachSublocationAndOpenThemInInventoryView() {
        hierarchyViewPage.selectNodeByPath(getSublocationsPath(ductEntryId));
        hierarchyViewPage.selectNodeByPath(getSublocationsPath(indoorCabinetId));
        hierarchyViewPage.selectNodeByPath(getSublocationsPath(rackId));
        hierarchyViewPage.callAction(ActionsContainer.SHOW_ON_GROUP_ID, INVENTORY_VIEW_ID);

        Assert.assertFalse(newInventoryViewPage.checkIfTableIsEmpty(), HIERARCHY_VIEW_HAS_SOME_DATA_MESSAGE);
    }

    @Step("Check Attributes of edited Sublocation on Property Panel in IV")
    private void checkUpdatedAttributesOfSublocationOnPropertyPanelInInventoryView() {
        Long[] sublocationIds = {ductEntryId, indoorCabinetId, rackId};

        softAssert = new SoftAssert();

        for (Long id : sublocationIds) {
            selectOnlyOneRow(String.valueOf(id));

            checkSublocationAttributeByValue(DESCRIPTION_VALUE, DESCRIPTION_ID, DIFFERENT_DESCRIPTION_MESSAGE);
            checkSublocationAttributeByValue(REMARKS_VALUE, REMARKS_ID, DIFFERENT_REMARKS_MESSAGE);

            if (!id.equals(rackId)) {
                checkSublocationAttributeByValue(DIMENSION_VALUE, HEIGHT_ID, DIFFERENT_HEIGHT_MESSAGE);
                checkSublocationAttributeByValue(DIMENSION_VALUE, WIDTH_ID, DIFFERENT_WIDTH_MESSAGE);
                checkSublocationAttributeByValue(DIMENSION_VALUE, DEPTH_ID, DIFFERENT_DEPTH_MESSAGE);
            }

            if (id.equals(rackId)) {
                checkSublocationAttributeByValue(RACK_MODEL_HEIGHT_VALUE, HEIGHT_ID, DIFFERENT_HEIGHT_MESSAGE);
                checkSublocationAttributeByValue(RACK_MODEL_WIDTH_VALUE, WIDTH_ID, DIFFERENT_WIDTH_MESSAGE);
                checkSublocationAttributeByValue(RACK_MODEL_DEPTH_VALUE, DEPTH_ID, DIFFERENT_DEPTH_MESSAGE);
                checkSublocationAttributeByValue(RACK_MODEL_MANUFACTURER_MODEL_NAME, MODEL_MANUFACTURER_ID, DIFFERENT_MANUFACTURER_MODEL_NAME_MESSAGE);
                checkSublocationAttributeByValue(RACK_MODEL_VALUE, MODEL_NAME_ID, DIFFERENT_MODEL_NAME_MESSAGE);
                checkSublocationAttributeByValue(SUBLOCATION_NAME + DIMENSION_VALUE, NAME_ID, DIFFERENT_NAME_MESSAGE);
            }

            selectOnlyOneRow(String.valueOf(id));
        }
        softAssert.assertAll();
    }

    @Step("Check message text for updated Sublocations")
    private void checkMessageTextForUpdatedSublocation() {
        SystemMessageInterface systemMessage = create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();

        softAssert = new SoftAssert();
        softAssert.assertEquals(messages.size(), 1, NO_SINGLE_MESSAGE);
        softAssert.assertTrue(SystemMessageContainer.MessageType.SUCCESS.equals(messages.get(0).getMessageType()), NO_SUCCESSFUL_MESSAGE);
        softAssert.assertTrue(messages.get(0).getText().contains(SUBLOCATION_UPDATED_MESSAGE), DIFFERENT_CONTENT_MESSAGE);

        softAssert.assertAll();

        create(driver, webDriverWait).close();
    }

    @Step("Verification of Sublocation Attributes By Value in Hierarchy View")
    public void checkSublocationAttributeByValue(String expectedValue, String attributeId, String validationMessage) {
        PropertyPanel propertyPanel = PropertyPanel.createById(driver, webDriverWait, PROPERTY_PANEL_WIDGET_ID);
        String actualValue = propertyPanel.getPropertyValue(attributeId);
        softAssert.assertEquals(actualValue, expectedValue, validationMessage + actualValue);
    }

    private void selectOnlyOneRow(String value) {
        if (newInventoryViewPage.getMainTable().getSelectedRows().isEmpty()) {
            newInventoryViewPage.selectRow(ID_DATA_COL_ID, value);
        } else {
            newInventoryViewPage.getMainTable().unselectAllRows();
        }
    }

    @Step("Delete updated Sublocations by API")
    private void deleteCreatedSublocations() {
        locationInventoryRepository.deleteSubLocation(indoorCabinetId.toString());
        locationInventoryRepository.deleteSubLocation(rackId.toString());
        locationInventoryRepository.deleteSubLocation(ductEntryId.toString());
    }

    @Step("Delete Physical Location by API")
    private void deletePhysicalLocation() {
        locationInventoryRepository.deleteLocation(locationId, LOCATION_TYPE);
    }

    @Step("Delete Geographical Address by API")
    private void deleteGeographicalAddress() {
        addressRepository.deleteGeographicalAddress(geographicalAddressId);
    }
}

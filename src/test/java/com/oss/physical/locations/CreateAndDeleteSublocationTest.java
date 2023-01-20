package com.oss.physical.locations;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
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
import com.oss.framework.components.prompts.ConfirmationBox;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.propertypanel.PropertyPanel;
import com.oss.framework.widgets.table.TableWidget;
import com.oss.framework.widgets.tabs.TabsWidget;
import com.oss.pages.physical.SublocationWizardPage;
import com.oss.repositories.AddressRepository;
import com.oss.repositories.LocationInventoryRepository;
import com.oss.untils.Environment;

import io.qameta.allure.Description;
import io.qameta.allure.Step;

import static com.oss.framework.components.alerts.SystemMessageContainer.create;

public class CreateAndDeleteSublocationTest extends BaseTestCase {
    private static final String LIVE_PERSPECTIVE_TEXT = "Live";

    private static final String COUNTRY_NAME = "country_kk";
    private static final String POSTAL_CODE_NAME = "23-456";
    private static final String REGION_NAME = "region_kk";
    private static final String CITY_NAME = "city_kk";
    private static final String DISTRICT_NAME = "district_kk";
    private static final String LOCATION_NAME = "selenium test bu_kk";
    private static final String RACK_MODEL_MANUFACTURER_MODEL_NAME = "Generic";
    private static final String RACK_MODEL_PARTLY_NAME = "19";

    private static final String LOCATION_TYPE = "Physical Location";
    private static final String LOCATION_TYPE_API = "BuildingComplex";
    private static final String FLOOR_TYPE = "Floor";
    private static final String ROOM_TYPE = "Room";
    private static final String RACK_TYPE = "Rack";

    private static final String PRECISE_LOCATION = "B";
    private static final String RACK_MODEL = "Generic 19";
    private static final String DESCRIPTION = "description";
    private static final String REMARKS = "remarks";
    private static final String HEIGHT_FIELD = "height";
    private static final String WIDTH_FIELD = "width";
    private static final String DEPTH_FIELD = "depth";
    private static final String RACK_HEIGHT = "0";
    private static final String RACK_WIDTH = "0";
    private static final String RACK_DEPTH = "0";
    private static final String TAB_PATTERN = "tab_%s";

    private static final String TABLE_SUBLOCATIONS_ID = "SublocationsWidget";
    private static final String CREATE_SUBLOCATION_WIZARD_ID = "CreateSublocationInLocationWizardAction";
    private static final String NAME_COL_ID = "name";
    private static final String TYPE_COL_ID = "type";
    private static final String LOCATION_COL_ID = "location.name";
    private static final String MODEL_MANUFACTURER_ID = "model.manufacturer";
    private static final String MODEL_NAME_ID = "model.name";
    private static final String PRECISE_LOCATION_ID = "preciseLocation.name";
    private final static String PROPERTY_PANEL_WIDGET_ID = "PropertyPanelWidget";
    private static final String DELETE_SUBLOCATION_ACTION_ID = "RemoveSublocationWizardAction";
    private static final String HIERARCHY_VIEW_ACTION_ID = "HierarchyView";
    private static final String INVENTORY_VIEW_ACTION_ID = "InventoryView";

    private static final String CREATE_SUBLOCATION_MESSAGE = "I'm starting to create Sublocation: ";
    private static final String VERIFICATION_SUBLOCATION_ATTRIBUTES_HV = "Verification of Sublocation attributes on HV = ";
    private static final String VERIFICATION_SUBLOCATION_ATTRIBUTES_IV = "Verification of Sublocation attributes on IV = ";
    private static final String SUBLOCATION_CREATED_MESSAGE = "Sublocation created successfully, click here to open Hierarchy View.";
    private static final String SUBLOCATION_REMOVED_MESSAGE = "Sublocation has been removed.";
    private static final String NO_SUCCESSFUL_MESSAGE = "There is no successful message";
    private static final String DIFFERENT_CONTENT_MESSAGE = "Returned message contains different content.";
    private static final String DIFFERENT_NAME_MESSAGE = "Name was not calculated correctly: ";
    private static final String DIFFERENT_TYPE_MESSAGE = "There is a different type of sublocation: ";
    private static final String DIFFERENT_PRECISE_LOCATION_MESSAGE = "There is a different value of Precise Location ";
    private static final String DIFFERENT_DESCRIPTION_MESSAGE = "There is a different value of description: ";
    private static final String DIFFERENT_REMARKS_MESSAGE = "There is a different value of remarks: ";
    private static final String DIFFERENT_HEIGHT_MESSAGE = "There is empty field with height!";
    private static final String DIFFERENT_DEPTH_MESSAGE = "There is empty field with depth!";
    private static final String DIFFERENT_WIDTH_MESSAGE = "There is empty field with width!";
    private static final String DIFFERENT_MANUFACTURER_MODEL_NAME_MESSAGE = "There is a different value of Rack Manufacturer Model Name";
    private static final String DIFFERENT_MODEL_NAME_MESSAGE = "There is a different componentValue of Rack Model Name";
    private static final String NO_SINGLE_MESSAGE = "There is no single message";

    private static final String INVENTORY_VIEW_ACTION_LABEL = "Inventory View";
    private static final String DELETE_BUTTON_LABEL = "Delete";

    private static final String RESOURCE_INVENTORY_PATH = "Resource Inventory";
    private static final String RACK_PATH = ".Locations.Rack." + RACK_TYPE;

    private Long geographicalAddressId;
    private Long locationId;
    private AddressRepository addressRepository;
    private LocationInventoryRepository locationInventoryRepository;
    private NewInventoryViewPage newInventoryViewPage;
    private SoftAssert softAssert;
    private HierarchyViewPage hierarchyViewPage;
    private TableWidget tableWidget;

    private final String dateAndTime = new SimpleDateFormat("dd-MM-yyyy_HH:mm").format(Calendar.getInstance().getTime());
    private final String time = dateAndTime.substring(11);

    private final Environment env = Environment.getInstance();
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateAndDeleteSublocationTest.class);

    @BeforeClass
    public void init() {
        setLivePerspective();
        addressRepository = new AddressRepository(env);
        locationInventoryRepository = new LocationInventoryRepository(env);
        hierarchyViewPage = HierarchyViewPage.getHierarchyViewPage(driver, webDriverWait);
        newInventoryViewPage = NewInventoryViewPage.getInventoryViewPage(driver, webDriverWait);
    }

    @Test(priority = 1)
    @Description("Create prerequisites needed to update sublocations")
    public void preparePrerequisites() {
        createGeographicalAddress();
        createLocation();
    }

    @Test(priority = 2, dependsOnMethods = {"preparePrerequisites"})
    @Description("Open Inventory View and Search Object by Type")
    public void openInventoryViewFromLeftSideMenu() {
        openSearchObjectOfInventoryViewFromLeftSideMenu();
    }

    @Test(priority = 3, dependsOnMethods = {"openInventoryViewFromLeftSideMenu"})
    @Description("Search for Building Complex, display them in IV and find location by name")
    public void searchLocationAndOpenNewInventoryView() {
        SearchObjectTypePage searchObjectTypePage = new SearchObjectTypePage(driver, webDriverWait);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        searchObjectTypePage.searchType(LOCATION_TYPE);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        newInventoryViewPage.searchObject(LOCATION_NAME);
        newInventoryViewPage.selectFirstRow();
    }

    @Test(priority = 4, dependsOnMethods = {"searchLocationAndOpenNewInventoryView"})
    @Description("Open wizard to create Sublocations (Floor,Rack, Room) With Full Attributes and check confirmation system message")
    public void createSublocations() {
        String[] sublocationTypes = {FLOOR_TYPE, ROOM_TYPE, RACK_TYPE};
        for (String type : sublocationTypes) {
            LOGGER.info(CREATE_SUBLOCATION_MESSAGE + type);
            openWizardToCreateSublocation();
            SublocationWizardPage sublocationWizardPage = new SublocationWizardPage(driver);
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            sublocationWizardPage.setSublocationType(type);
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            if (!type.equals(FLOOR_TYPE)) {
                sublocationWizardPage.setPreciseLocation(PRECISE_LOCATION);
            }
            sublocationWizardPage.setSublocationName(type + dateAndTime);
            if (type.equals(RACK_TYPE)) {
                DelayUtils.waitForPageToLoad(driver, webDriverWait);
                sublocationWizardPage.setSublocationModel(RACK_MODEL);
                DelayUtils.waitForPageToLoad(driver, webDriverWait);
                checkIfRackDimensionsFieldsAreNotEmpty(sublocationWizardPage);
            }
            sublocationWizardPage.setDescription(DESCRIPTION);
            sublocationWizardPage.setRemarks(REMARKS);
            sublocationWizardPage.clickNext();
            sublocationWizardPage.create();
            checkMessageTextForCreatedAndRemovedSublocation(SUBLOCATION_CREATED_MESSAGE);
            closeSystemMessage();
        }
    }

    @Test(priority = 5, dependsOnMethods = {"createSublocations"})
    @Description("Open Hierarchy View for Physical Location of created Sublocations")
    public void moveToHierarchyView() {
        newInventoryViewPage.callAction(ActionsContainer.SHOW_ON_GROUP_ID, HIERARCHY_VIEW_ACTION_ID);
        hierarchyViewPage.selectFirstObject();
    }

    @Test(priority = 6, dependsOnMethods = {"moveToHierarchyView"})
    @Description("Check Attributes of Sublocations in Sublocations tab on HV")
    public void checkSublocationsAttributesOnHierarchyView() {
        tableWidget = getTableWidget();
        tableWidget.fullTextSearch(time);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        String[] sublocationTypes = {FLOOR_TYPE, ROOM_TYPE, RACK_TYPE};
        softAssert = new SoftAssert();
        for (String type : sublocationTypes) {
            LOGGER.info(VERIFICATION_SUBLOCATION_ATTRIBUTES_HV + type);
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            tableWidget.selectRowByAttributeValue(TYPE_COL_ID, type);
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            int rowIndex = tableWidget.getRowNumber(type, TYPE_COL_ID);

            String componentValue = tableWidget.getCellValue(rowIndex, NAME_COL_ID);
            softAssert.assertTrue(componentValue.contains(dateAndTime), DIFFERENT_NAME_MESSAGE + componentValue);

            componentValue = tableWidget.getCellValue(rowIndex, TYPE_COL_ID);
            softAssert.assertTrue(componentValue.equals(type), DIFFERENT_TYPE_MESSAGE + componentValue);

            componentValue = tableWidget.getCellValue(rowIndex, LOCATION_COL_ID);
            softAssert.assertTrue(componentValue.contains(LOCATION_NAME), DIFFERENT_PRECISE_LOCATION_MESSAGE + componentValue);

            componentValue = tableWidget.getCellValue(rowIndex, DESCRIPTION);
            softAssert.assertEquals(componentValue, DESCRIPTION, DIFFERENT_DESCRIPTION_MESSAGE + componentValue);

            componentValue = tableWidget.getCellValue(rowIndex, REMARKS);
            softAssert.assertEquals(componentValue, REMARKS, DIFFERENT_REMARKS_MESSAGE + componentValue);

            if (type.equals(RACK_TYPE)) {
                checkRackAttributesInPropertiesTab();
                DelayUtils.waitForPageToLoad(driver, webDriverWait);
            } else {
                tableWidget.unselectRow(rowIndex);
            }
        }
        softAssert.assertAll();
    }

    @Test(priority = 7, dependsOnMethods = {"checkSublocationsAttributesOnHierarchyView"})
    @Description("Open Inventory View for selected Sublocations")
    public void moveToInventoryView() {
        hierarchyViewPage.selectFirstObject();
        tableWidget = getTableWidget();
        tableWidget.fullTextSearch(time);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        tableWidget.selectAllRows();
        tableWidget.callAction(ActionsContainer.SHOW_ON_GROUP_ID, INVENTORY_VIEW_ACTION_ID);
    }

    @Test(priority = 8, dependsOnMethods = {"moveToInventoryView"})
    @Description("Check Attributes of Sublocations on IV")
    public void checkSublocationsAttributesOnInventoryView() {
        String[] sublocationTypes = {FLOOR_TYPE, ROOM_TYPE, RACK_TYPE};
        softAssert = new SoftAssert();
        int i = 0;
        for (String type : sublocationTypes) {
            LOGGER.info(VERIFICATION_SUBLOCATION_ATTRIBUTES_IV + type);
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            newInventoryViewPage.selectRow(TYPE_COL_ID, type);
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            checkNameByValueOnIV();
            checkDescriptionByValueOnIV();
            checkRemarksByValueOnIV();
            String componentValue = newInventoryViewPage.getPropertyPanel(PROPERTY_PANEL_WIDGET_ID).getPropertyValue(TYPE_COL_ID);
            softAssert.assertEquals(componentValue, type, DIFFERENT_TYPE_MESSAGE + componentValue);
            if (type.equals(RACK_TYPE)) {
                checkPreciseLocationByName();
                checkRackManufacturerModelByModelName();
                checkRackModelByModelName();
                checkRackHeightByName();
                checkRackWidthByName();
                checkRackDepthByName();
            }
            newInventoryViewPage.unselectObjectByRowId(i);
            i++;
        }
        softAssert.assertAll();
    }

    @Test(priority = 9, dependsOnMethods = {"checkSublocationsAttributesOnInventoryView"})
    @Description("Delete all created Sublocations and check confirmation system messages")
    public void deleteSublocations() {
        String[] sublocationTypes = {FLOOR_TYPE, ROOM_TYPE, RACK_TYPE,};
        for (String type : sublocationTypes) {
            selectOnlyOneRow(type);
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            newInventoryViewPage.callAction(ActionsContainer.EDIT_GROUP_ID, DELETE_SUBLOCATION_ACTION_ID);
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            ConfirmationBox.create(driver, webDriverWait).clickButtonByLabel(DELETE_BUTTON_LABEL);
            checkMessageTextForCreatedAndRemovedSublocation(SUBLOCATION_REMOVED_MESSAGE);
            closeSystemMessage();
        }
    }

    @Test(priority = 10, dependsOnMethods = {"deleteSublocations"})
    @Description("Delete prerequisites")
    public void deletePrerequisites() {
        deleteLocation();
        deleteGeographicalAddress();
    }

    @Step("Create Geographical Address by API")
    public void createGeographicalAddress() {
        geographicalAddressId = addressRepository.updateOrCreateAddress(COUNTRY_NAME, POSTAL_CODE_NAME, REGION_NAME, CITY_NAME, DISTRICT_NAME);
    }

    @Step("Create Physical Location by API")
    public void createLocation() {
        locationId = Long.valueOf(locationInventoryRepository.createLocation(LOCATION_NAME, LOCATION_TYPE_API, geographicalAddressId));
    }

    @Step("Open Create Sublocation Wizard from Physical Location context")
    public void openWizardToCreateSublocation() {
        newInventoryViewPage.callAction(ActionsContainer.CREATE_GROUP_ID, CREATE_SUBLOCATION_WIZARD_ID);
    }

    @Step("Check that attributes are autocompleted for selected Rack model and fields are not empty")
    private void checkIfRackDimensionsFieldsAreNotEmpty(SublocationWizardPage sublocationWizardPage) {
        softAssert = new SoftAssert();

        String heightValue = sublocationWizardPage.getHeight();
        softAssert.assertFalse(heightValue.isEmpty(), DIFFERENT_HEIGHT_MESSAGE + heightValue);

        String widthValue = sublocationWizardPage.getWidth();
        softAssert.assertFalse(widthValue.isEmpty(), DIFFERENT_WIDTH_MESSAGE + widthValue);

        String depthValue = sublocationWizardPage.getDepth();
        softAssert.assertFalse(depthValue.isEmpty(), DIFFERENT_DEPTH_MESSAGE + depthValue);

        softAssert.assertAll();
    }

    @Step("Verification of Sublocation Name in Property Panel")
    public void checkNameByValueOnIV() {
        String nameValue = newInventoryViewPage.getPropertyPanel(PROPERTY_PANEL_WIDGET_ID).getPropertyValue(NAME_COL_ID);
        Assert.assertTrue(nameValue.contains(dateAndTime), DIFFERENT_NAME_MESSAGE + nameValue);
    }

    @Step("Verification of Sublocation Description in Property Panel")
    public void checkDescriptionByValueOnIV() {
        String descriptionValue = newInventoryViewPage.getPropertyPanel(PROPERTY_PANEL_WIDGET_ID).getPropertyValue(DESCRIPTION);
        Assert.assertEquals(descriptionValue, DESCRIPTION, DIFFERENT_DESCRIPTION_MESSAGE + descriptionValue);
    }

    @Step("Verification of Sublocation Name in Property Panel")
    public void checkRemarksByValueOnIV() {
        String remarksValue = newInventoryViewPage.getPropertyPanel(PROPERTY_PANEL_WIDGET_ID).getPropertyValue(REMARKS);
        Assert.assertEquals(remarksValue, REMARKS, DIFFERENT_REMARKS_MESSAGE + remarksValue);
    }

    @Step
    public void checkRackAttributesInPropertiesTab() {
        softAssert = new SoftAssert();
        PropertyPanel propertyPanel = PropertyPanel.createById(driver, webDriverWait, PROPERTY_PANEL_WIDGET_ID);
        String locationValue = propertyPanel.getPropertyValue(NAME_COL_ID);
        hierarchyViewPage.selectNodeByLabelsPath(locationValue + RACK_PATH + dateAndTime);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        hierarchyViewPage.unselectFirstObject();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        propertyPanel = PropertyPanel.createById(driver, webDriverWait, PROPERTY_PANEL_WIDGET_ID);

        String componentValue = propertyPanel.getPropertyValue(MODEL_MANUFACTURER_ID);
        softAssert.assertEquals(componentValue, RACK_MODEL_MANUFACTURER_MODEL_NAME, DIFFERENT_MANUFACTURER_MODEL_NAME_MESSAGE + componentValue);

        componentValue = propertyPanel.getPropertyValue(MODEL_NAME_ID);
        softAssert.assertTrue(componentValue.contains(RACK_MODEL_PARTLY_NAME), DIFFERENT_MODEL_NAME_MESSAGE + componentValue);

        componentValue = propertyPanel.getPropertyValue(HEIGHT_FIELD);
        softAssert.assertNotEquals(componentValue, RACK_HEIGHT, DIFFERENT_HEIGHT_MESSAGE + componentValue);

        componentValue = propertyPanel.getPropertyValue(WIDTH_FIELD);
        softAssert.assertNotEquals(componentValue, RACK_WIDTH, DIFFERENT_WIDTH_MESSAGE + componentValue);

        componentValue = propertyPanel.getPropertyValue(DEPTH_FIELD);
        softAssert.assertNotEquals(componentValue, RACK_DEPTH, DIFFERENT_DEPTH_MESSAGE + componentValue);

        hierarchyViewPage.unselectNodeByLabelsPath(locationValue + RACK_PATH + dateAndTime);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        hierarchyViewPage.selectFirstObject();
        softAssert.assertAll();
    }

    @Step("Verification of Sublocation Precise Location in Property Panel")
    public void checkPreciseLocationByName() {
        String locationValue = newInventoryViewPage.getPropertyPanel(PROPERTY_PANEL_WIDGET_ID).getPropertyValue(PRECISE_LOCATION_ID).toLowerCase();
        Assert.assertTrue(locationValue.contains(LOCATION_NAME), DIFFERENT_PRECISE_LOCATION_MESSAGE + locationValue);
    }

    @Step("Verification of Rack Manufacturer Model Name in Property Panel")
    private void checkRackManufacturerModelByModelName() {
        String manufacturerValue = newInventoryViewPage.getPropertyPanel(PROPERTY_PANEL_WIDGET_ID).getPropertyValue(MODEL_MANUFACTURER_ID);
        Assert.assertEquals(manufacturerValue, RACK_MODEL_MANUFACTURER_MODEL_NAME, DIFFERENT_MANUFACTURER_MODEL_NAME_MESSAGE + manufacturerValue);
    }

    @Step("Verification of Rack Model Name in Property Panel")
    private void checkRackModelByModelName() {
        String modelValue = newInventoryViewPage.getPropertyPanel(PROPERTY_PANEL_WIDGET_ID).getPropertyValue(MODEL_NAME_ID);
        Assert.assertTrue(modelValue.contains(RACK_MODEL_PARTLY_NAME), DIFFERENT_MODEL_NAME_MESSAGE + modelValue);
    }

    @Step("Verification of Rack Height in Property Panel")
    private void checkRackHeightByName() {
        String heihgtValue = newInventoryViewPage.getPropertyPanel(PROPERTY_PANEL_WIDGET_ID).getPropertyValue(HEIGHT_FIELD);
        Assert.assertNotEquals(heihgtValue, RACK_HEIGHT);
    }

    @Step("Verification of Rack Width in Property Panel")
    private void checkRackWidthByName() {
        String widthValue = newInventoryViewPage.getPropertyPanel(PROPERTY_PANEL_WIDGET_ID).getPropertyValue(WIDTH_FIELD);
        Assert.assertNotEquals(widthValue, RACK_WIDTH);
    }

    @Step("Verification of Rack Depth in Property Panel")
    private void checkRackDepthByName() {
        String depthValue = newInventoryViewPage.getPropertyPanel(PROPERTY_PANEL_WIDGET_ID).getPropertyValue(DEPTH_FIELD);
        Assert.assertNotEquals(depthValue, RACK_DEPTH);
    }

    @Step("Get Table Widget")
    private TableWidget getTableWidget() {
        openTab();
        return TableWidget.createById(driver, CreateAndDeleteSublocationTest.TABLE_SUBLOCATIONS_ID, webDriverWait);
    }

    @Step("Open tab by Id")
    private void openTab() {
        TabsWidget tabsWidget = hierarchyViewPage.getBottomTabsWidget();
        tabsWidget.selectTabById(String.format(TAB_PATTERN, CreateAndDeleteSublocationTest.TABLE_SUBLOCATIONS_ID));
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Step("Set perspective to LIVE")
    private void setLivePerspective() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        PerspectiveChooser perspectiveChooser = PerspectiveChooser.create(driver, webDriverWait);
        if (!perspectiveChooser.getCurrentPerspective().equals(LIVE_PERSPECTIVE_TEXT)) {
            perspectiveChooser.setLivePerspective();
        }
    }

    @Step("Open Inventory View with Search Object Field from Left Side Menu")
    private void openSearchObjectOfInventoryViewFromLeftSideMenu() {
        homePage.chooseFromLeftSideMenu(INVENTORY_VIEW_ACTION_LABEL, RESOURCE_INVENTORY_PATH);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Step("Check message text for created Sublocation")
    private void checkMessageTextForCreatedAndRemovedSublocation(String text) {
        SystemMessageInterface systemMessage = create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        softAssert = new SoftAssert();
        softAssert.assertEquals(messages.size(), 1, NO_SINGLE_MESSAGE);
        softAssert.assertTrue(SystemMessageContainer.MessageType.SUCCESS.equals(messages.get(0).getMessageType()), NO_SUCCESSFUL_MESSAGE);
        softAssert.assertTrue(messages.get(0).getText().contains(text), DIFFERENT_CONTENT_MESSAGE);
        softAssert.assertAll();
    }

    @Step("Close system message")
    private void closeSystemMessage() {
        create(driver, webDriverWait).close();
    }

    @Step("Select only one row")
    private void selectOnlyOneRow(String value) {
        newInventoryViewPage.getMainTable().unselectAllRows();
        newInventoryViewPage.selectRow(TYPE_COL_ID, value);
    }

    @Step("Delete Building Complex")
    private void deleteLocation() {
        locationInventoryRepository.deleteLocation(locationId, LOCATION_TYPE_API);
    }

    @Step("Delete geographical address")
    private void deleteGeographicalAddress() {
        addressRepository.deleteGeographicalAddress(geographicalAddressId);
    }

}
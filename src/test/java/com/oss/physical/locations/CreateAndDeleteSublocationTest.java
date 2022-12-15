package com.oss.physical.locations;

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
import com.oss.pages.platform.HierarchyViewPage;
import com.oss.pages.platform.NewInventoryViewPage;
import com.oss.pages.platform.SearchObjectTypePage;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import static com.oss.framework.components.alerts.SystemMessageContainer.create;

public class CreateAndDeleteSublocationTest extends BaseTestCase {
    private static final String LIVE_PERSPECTIVE_TEXT = "Live";
    private static final String LOCATION_TYPE = "Building Complex";
    private static final String LOCATION_NAME = "selenium test bu";
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
    private NewInventoryViewPage newInventoryViewPage;
    private SoftAssert softAssert;
    private HierarchyViewPage hierarchyViewPage;
    private TableWidget tableWidget;
    private static final String TABLE_SUBLOCATIONS_ID = "SublocationsWidget";
    private static final String TAB_PATTERN = "tab_%s";
    private static final String NAME_COL_ID = "name";
    private static final String TYPE_COL_ID = "type";
    private static final String LOCATION_COL_ID = "location.name";
    private static final String MODEL_MANUFACTURER_ID = "model.manufacturer";
    private static final String MODEL_NAME_ID = "model.name";
    private static final String PRECISE_LOCATION_ID = "preciseLocation.name";
    private final String dateAndTime = new SimpleDateFormat("dd-MM-yyyy_HH:mm").format(Calendar.getInstance().getTime());
    private final String time = dateAndTime.substring(11);
    private final static String PROPERTY_PANEL_WIDGET_ID = "PropertyPanelWidget";
    private static final String DELETE_SUBLOCATION_ACTION_ID = "RemoveSublocationWizardAction";
    private static final String RACK_PATH = ".Locations.Rack." + RACK_TYPE;
    private static final String SUBLOCATION_CREATED_MESSAGE = "Sublocation created successfully, click here to open Hierarchy View.";
    private static final String SUBLOCATION_REMOVED_MESSAGE = "Sublocation has been removed.";
    private static final String DELETE_BUTTON_LABEL = "Delete";
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateAndDeleteSublocationTest.class);

    @BeforeClass
    public void init() {
        setLivePerspective();
        hierarchyViewPage = HierarchyViewPage.getHierarchyViewPage(driver, webDriverWait);
        newInventoryViewPage = NewInventoryViewPage.getInventoryViewPage(driver, webDriverWait);
    }

    @Test(priority = 1)
    @Description("Open Inventory View and Search Object by Type")
    public void openInventoryViewFromLeftSideMenu() {
        openSearchObjectOfInventoryViewFromLeftSideMenu();
    }

    @Test(priority = 2, dependsOnMethods = {"openInventoryViewFromLeftSideMenu"})
    @Description("Search for Building Complex, display them in IV and find location by name")
    public void searchLocationAndOpenNewInventoryView() {
        SearchObjectTypePage searchObjectTypePage = new SearchObjectTypePage(driver, webDriverWait);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        searchObjectTypePage.searchType(LOCATION_TYPE);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        newInventoryViewPage.searchObject(LOCATION_NAME);
        newInventoryViewPage.selectFirstRow();
    }

    @Test(priority = 3, dependsOnMethods = {"searchLocationAndOpenNewInventoryView"})
    @Description("Open wizard to create Sublocations (Floor,Rack, Room) With Full Attributes and check confirmation system message")
    public void createSublocations() {
        String[] sublocationTypes = {FLOOR_TYPE, ROOM_TYPE, RACK_TYPE};
        for (String type : sublocationTypes) {
            LOGGER.info("I'm starting to create Sublocation: " + type + "...");
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

    @Test(priority = 4, dependsOnMethods = {"createSublocations"})
    @Description("Open Hierarchy View for Physical Location of created Sublocations")
    public void moveToHierarchyView() {
        newInventoryViewPage.callAction(ActionsContainer.SHOW_ON_GROUP_ID, "HierarchyView");
        hierarchyViewPage.selectFirstObject();
    }

    @Test(priority = 5, dependsOnMethods = {"moveToHierarchyView"})
    @Description("Check Attributes of Sublocations in Sublocations tab on HV")
    public void checkSublocationsAttributesOnHierarchyView() {
        tableWidget = getTableWidget();
        tableWidget.fullTextSearch(time);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        String[] sublocationTypes = {FLOOR_TYPE, ROOM_TYPE, RACK_TYPE};
        softAssert = new SoftAssert();
        for (String type : sublocationTypes) {
            LOGGER.info("Verification of Sublocation attributes on HV = " + type);
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            tableWidget.selectRowByAttributeValue(TYPE_COL_ID, type);
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            int rowIndex = tableWidget.getRowNumber(type, TYPE_COL_ID);
            String componentValue = tableWidget.getCellValue(rowIndex, NAME_COL_ID);
            softAssert.assertTrue(componentValue.contains(dateAndTime), "Name was not calculated correctly: " + componentValue);
            componentValue = tableWidget.getCellValue(rowIndex, TYPE_COL_ID);
            softAssert.assertTrue(componentValue.equals(type), "There is a different type of sublocation: " + componentValue);
            componentValue = tableWidget.getCellValue(rowIndex, LOCATION_COL_ID);
            softAssert.assertTrue(componentValue.contains(LOCATION_NAME), "There is a different componentValue of Precise Location " + componentValue);
            componentValue = tableWidget.getCellValue(rowIndex, DESCRIPTION);
            softAssert.assertEquals(componentValue, DESCRIPTION, "There is a different componentValue of description: " + componentValue);
            componentValue = tableWidget.getCellValue(rowIndex, REMARKS);
            softAssert.assertEquals(componentValue, REMARKS, "There is a different componentValue of remarks: " + componentValue);
            if (type.equals(RACK_TYPE)) {
                searchRackAttributesInPropertiesTab();
                DelayUtils.waitForPageToLoad(driver, webDriverWait);
            } else {
                tableWidget.unselectRow(rowIndex);
            }
        }
        softAssert.assertAll();
    }

    @Test(priority = 6, dependsOnMethods = {"checkSublocationsAttributesOnHierarchyView"})
    @Description("Open Inventory View for selected Sublocations")
    public void moveToInventoryView() {
        hierarchyViewPage.selectFirstObject();
        tableWidget = getTableWidget();
        tableWidget.fullTextSearch(time);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        tableWidget.selectAllRows();
        tableWidget.callAction(ActionsContainer.SHOW_ON_GROUP_ID, "InventoryView");
    }

    @Test(priority = 7, dependsOnMethods = {"moveToInventoryView"})
    @Description("Check Attributes of Sublocations on IV")
    public void checkSublocationsAttributesOnInventoryView() {
        String[] sublocationTypes = {FLOOR_TYPE, ROOM_TYPE, RACK_TYPE};
        softAssert = new SoftAssert();
        int i = 0;
        for (String type : sublocationTypes) {
            LOGGER.info("Verification of Sublocation attributes = " + type);
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            newInventoryViewPage.selectRow(TYPE_COL_ID, type);
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            searchNameByValueOnIV();
            searchDescriptionByValueOnIV();
            searchRemarksByValueOnIV();
            String componentValue = newInventoryViewPage.getPropertyPanel(PROPERTY_PANEL_WIDGET_ID).getPropertyValue(TYPE_COL_ID);
            softAssert.assertEquals(componentValue, type, "There is a different type of location: " + componentValue);
            if (type.equals(RACK_TYPE)) {
                searchPreciseLocationByName();
                searchRackManufacturerModelByModelName();
                searchRackModelByModelName();
                searchRackHeightByName();
                searchRackWidthByName();
                searchRackDepthByName();
            }
            newInventoryViewPage.unselectObjectByRowId(i);
            i++;
            softAssert.assertAll();
        }
    }

    @Test(priority = 8, dependsOnMethods = {"checkSublocationsAttributesOnInventoryView"})
    @Description("Delete all created Sublocations and check confirmation system messages")
    public void deleteSublocations() {
        String[] sublocationTypes = {FLOOR_TYPE, ROOM_TYPE, RACK_TYPE,};
        for (String type : sublocationTypes) {
            newInventoryViewPage.selectRow(TYPE_COL_ID, type);
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            newInventoryViewPage.callAction(ActionsContainer.EDIT_GROUP_ID, DELETE_SUBLOCATION_ACTION_ID);
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            ConfirmationBox.create(driver, webDriverWait).clickButtonByLabel(DELETE_BUTTON_LABEL);
            checkMessageTextForCreatedAndRemovedSublocation(SUBLOCATION_REMOVED_MESSAGE);
            closeSystemMessage();
            newInventoryViewPage.unselectObjectByRowId(0);
            newInventoryViewPage.refreshMainTable();
        }
    }

    @Step("Open Create Sublocation Wizard from Physical Location context")
    public void openWizardToCreateSublocation() {
        newInventoryViewPage.callAction(ActionsContainer.CREATE_GROUP_ID, "CreateSublocationInLocationWizardAction");
    }

    @Step("Check that attributes are autocompleted for selected Rack model and fields are not empty")
    private void checkIfRackDimensionsFieldsAreNotEmpty(SublocationWizardPage sublocationWizardPage) {
        softAssert = new SoftAssert();
        String heightValue = sublocationWizardPage.getHeight();
        softAssert.assertFalse(heightValue.isEmpty(), "There is empty field with height!" + heightValue);
        String widthValue = sublocationWizardPage.getWidth();
        softAssert.assertFalse(widthValue.isEmpty(), "There is empty field with width!" + widthValue);
        String depthValue = sublocationWizardPage.getDepth();
        softAssert.assertFalse(depthValue.isEmpty(), "There is empty field with depth!" + depthValue);
        softAssert.assertAll();
    }

    @Step("Verification of Sublocation Name in Property Panel")
    public void searchNameByValueOnIV() {
        softAssert = new SoftAssert();
        String nameValue = newInventoryViewPage.getPropertyPanel(PROPERTY_PANEL_WIDGET_ID).getPropertyValue(NAME_COL_ID);
        softAssert.assertTrue(nameValue.contains(dateAndTime), "Name was not calculated correctly: " + nameValue);
        softAssert.assertAll();
    }

    @Step("Verification of Sublocation Description in Property Panel")
    public void searchDescriptionByValueOnIV() {
        softAssert = new SoftAssert();
        String descriptionValue = newInventoryViewPage.getPropertyPanel(PROPERTY_PANEL_WIDGET_ID).getPropertyValue(DESCRIPTION);
        softAssert.assertEquals(descriptionValue, DESCRIPTION, "There is a different value of description: " + descriptionValue);
        softAssert.assertAll();
    }

    @Step("Verification of Sublocation Name in Property Panel")
    public void searchRemarksByValueOnIV() {
        softAssert = new SoftAssert();
        String remarksValue = newInventoryViewPage.getPropertyPanel(PROPERTY_PANEL_WIDGET_ID).getPropertyValue(REMARKS);
        softAssert.assertEquals(remarksValue, REMARKS, "There is a different value of remarks: " + remarksValue);
        softAssert.assertAll();
    }

    @Step
    public void searchRackAttributesInPropertiesTab() {
        softAssert = new SoftAssert();
        PropertyPanel propertyPanel = PropertyPanel.createById(driver, webDriverWait, PROPERTY_PANEL_WIDGET_ID);
        String locationValue = propertyPanel.getPropertyValue(NAME_COL_ID);
        hierarchyViewPage.selectNodeByLabelsPath(locationValue + RACK_PATH + dateAndTime);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        hierarchyViewPage.unselectFirstObject();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        propertyPanel = PropertyPanel.createById(driver, webDriverWait, PROPERTY_PANEL_WIDGET_ID);
        String componentValue = propertyPanel.getPropertyValue(MODEL_MANUFACTURER_ID);
        softAssert.assertEquals(componentValue, "Generic", "There is a different componentValue of Rack Manufacturer Model Name: " + componentValue);
        componentValue = propertyPanel.getPropertyValue(MODEL_NAME_ID);
        softAssert.assertTrue(componentValue.contains("19"), "There is a different componentValue of Rack Model Name: " + componentValue);
        componentValue = propertyPanel.getPropertyValue(HEIGHT_FIELD);
        softAssert.assertNotEquals(componentValue, RACK_HEIGHT, "There is a different componentValue of height" + componentValue);
        componentValue = propertyPanel.getPropertyValue(WIDTH_FIELD);
        softAssert.assertNotEquals(componentValue, RACK_WIDTH, "There is a different componentValue of width" + componentValue);
        componentValue = propertyPanel.getPropertyValue(DEPTH_FIELD);
        softAssert.assertNotEquals(componentValue, RACK_DEPTH, "There is a different componentValue of depth" + componentValue);
        softAssert.assertAll();
        hierarchyViewPage.unselectNodeByLabelsPath(locationValue + RACK_PATH + dateAndTime);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        hierarchyViewPage.selectFirstObject();
    }

    @Step("Verification of Sublocation Precise Location in Property Panel")
    public void searchPreciseLocationByName() {
        softAssert = new SoftAssert();
        String locationValue = newInventoryViewPage.getPropertyPanel(PROPERTY_PANEL_WIDGET_ID).getPropertyValue(PRECISE_LOCATION_ID).toLowerCase();
        softAssert.assertTrue(locationValue.contains(LOCATION_NAME), "There is a different value of Precise Location: " + locationValue);
        softAssert.assertAll();
    }

    @Step("Verification of Rack Manufacturer Model Name in Property Panel")
    private void searchRackManufacturerModelByModelName() {
        softAssert = new SoftAssert();
        String manufacturerValue = newInventoryViewPage.getPropertyPanel(PROPERTY_PANEL_WIDGET_ID).getPropertyValue(MODEL_MANUFACTURER_ID);
        softAssert.assertEquals(manufacturerValue, "Generic", "There is a different value of Rack Manufacturer Model Name: " + manufacturerValue);
        softAssert.assertAll();
    }

    @Step("Verification of Rack Model Name in Property Panel")
    private void searchRackModelByModelName() {
        softAssert = new SoftAssert();
        String modelValue = newInventoryViewPage.getPropertyPanel(PROPERTY_PANEL_WIDGET_ID).getPropertyValue(MODEL_NAME_ID);
        softAssert.assertTrue(modelValue.contains("19"), "There is a different value of Rack Model Name: " + modelValue);
        softAssert.assertAll();
    }

    @Step("Verification of Rack Height in Property Panel")
    private void searchRackHeightByName() {
        softAssert = new SoftAssert();
        String heihgtValue = newInventoryViewPage.getPropertyPanel(PROPERTY_PANEL_WIDGET_ID).getPropertyValue(HEIGHT_FIELD);
        softAssert.assertNotEquals(heihgtValue, RACK_HEIGHT);
        softAssert.assertAll();
    }

    @Step("Verification of Rack Width in Property Panel")
    private void searchRackWidthByName() {
        softAssert = new SoftAssert();
        String widthValue = newInventoryViewPage.getPropertyPanel(PROPERTY_PANEL_WIDGET_ID).getPropertyValue(WIDTH_FIELD);
        softAssert.assertNotEquals(widthValue, RACK_WIDTH);
        softAssert.assertAll();
    }

    @Step("Verification of Rack Depth in Property Panel")
    private void searchRackDepthByName() {
        softAssert = new SoftAssert();
        String depthValue = newInventoryViewPage.getPropertyPanel(PROPERTY_PANEL_WIDGET_ID).getPropertyValue(DEPTH_FIELD);
        softAssert.assertNotEquals(depthValue, RACK_DEPTH);
        softAssert.assertAll();
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
        homePage.chooseFromLeftSideMenu("Inventory View", "Resource Inventory");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Step("Check message text for created Sublocation")
    private void checkMessageTextForCreatedAndRemovedSublocation(String text) {
        SystemMessageInterface systemMessage = create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        softAssert = new SoftAssert();
        softAssert.assertEquals(messages.size(), 1, "There is no single message");
        softAssert.assertTrue(SystemMessageContainer.MessageType.SUCCESS.equals(messages.get(0).getMessageType()), "There is no successful message");
        softAssert.assertTrue(messages.get(0).getText().contains(text), "Returned message contains different content.");
        softAssert.assertAll();
    }

    @Step("Close system message")
    private void closeSystemMessage() {
        create(driver, webDriverWait).close();
    }

}
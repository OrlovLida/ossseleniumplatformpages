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
                checkAutoCompleteRackAttributes(sublocationWizardPage);
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
            LOGGER.info("Verification of Sublocation" + " attributes on HV = " + type);
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            tableWidget.selectRowByAttributeValue(TYPE_COL_ID, type);
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            int rowIndex = tableWidget.getRowNumber(type, TYPE_COL_ID);
            String name = tableWidget.getCellValue(rowIndex, NAME_COL_ID);
            softAssert.assertTrue(name.contains(dateAndTime), "Name was not calculated correctly: " + name);
            name = tableWidget.getCellValue(rowIndex, TYPE_COL_ID);
            softAssert.assertTrue(name.equals(type), "There is a different type of sublocation: " + name);
            name = tableWidget.getCellValue(rowIndex, LOCATION_COL_ID);
            softAssert.assertTrue(name.contains(LOCATION_NAME), "There is a different value of Precise Location " + name);
            name = tableWidget.getCellValue(rowIndex, DESCRIPTION);
            softAssert.assertEquals(name, DESCRIPTION, "There is a different value of description: " + name);
            name = tableWidget.getCellValue(rowIndex, REMARKS);
            softAssert.assertEquals(name, REMARKS, "There is a different value of remarks: " + name);
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
        for (String type : sublocationTypes) {
            LOGGER.info("Verification of Sublocation " + " attributes = " + type);
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            newInventoryViewPage.selectRow(TYPE_COL_ID, type);
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            searchNameByValueOnIV();
            searchDescriptionByValueOnIV();
            searchRemarksByValueOnIV();
            if (type.equals(FLOOR_TYPE)) {
                String name = newInventoryViewPage.getPropertyPanel(PROPERTY_PANEL_WIDGET_ID).getPropertyValue(TYPE_COL_ID);
                softAssert.assertEquals(name, type, "There is a different type of location: " + name);
                newInventoryViewPage.unselectObjectByRowId(0);
            } else if (type.equals(ROOM_TYPE)) {
                String name = newInventoryViewPage.getPropertyPanel(PROPERTY_PANEL_WIDGET_ID).getPropertyValue(TYPE_COL_ID);
                softAssert.assertEquals(name, type, "There is a different type of location: " + name);
                newInventoryViewPage.unselectObjectByRowId(1);
            } else {
                String name = newInventoryViewPage.getPropertyPanel(PROPERTY_PANEL_WIDGET_ID).getPropertyValue(TYPE_COL_ID);
                softAssert.assertEquals(name, type, "There is a different type of location: " + name);
                searchPreciseLocationByName();
                searchRackManufacturerModelByModelName();
                searchRackModelByModelName();
                searchRackHeightByName();
                searchRackWidthByName();
                searchRackDepthByName();
                newInventoryViewPage.unselectObjectByRowId(2);
            }
        }
        softAssert.assertAll();
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
    public void checkAutoCompleteRackAttributes(SublocationWizardPage sublocationWizardPage) {
        softAssert = new SoftAssert();
        String componentValue = sublocationWizardPage.getComponentValue(HEIGHT_FIELD);
        softAssert.assertFalse(componentValue.isEmpty(), "There is empty field with height!" + componentValue);
        componentValue = sublocationWizardPage.getComponentValue(WIDTH_FIELD);
        softAssert.assertFalse(componentValue.isEmpty(), "There is empty field with width!" + componentValue);
        componentValue = sublocationWizardPage.getComponentValue(DEPTH_FIELD);
        softAssert.assertFalse(componentValue.isEmpty(), "There is empty field with depth!" + componentValue);
        softAssert.assertAll();
    }

    @Step("Verification of Sublocation Name in Property Panel")
    public void searchNameByValueOnIV() {
        softAssert = new SoftAssert();
        String name = newInventoryViewPage.getPropertyPanel(PROPERTY_PANEL_WIDGET_ID).getPropertyValue(NAME_COL_ID);
        softAssert.assertTrue(name.contains(dateAndTime), "Name was not calculated correctly: " + name);
        softAssert.assertAll();
    }

    @Step("Verification of Sublocation Description in Property Panel")
    public void searchDescriptionByValueOnIV() {
        softAssert = new SoftAssert();
        String name = newInventoryViewPage.getPropertyPanel(PROPERTY_PANEL_WIDGET_ID).getPropertyValue(DESCRIPTION);
        softAssert.assertEquals(name, DESCRIPTION, "There is a different value of description: " + name);
        softAssert.assertAll();
    }

    @Step("Verification of Sublocation Name in Property Panel")
    public void searchRemarksByValueOnIV() {
        softAssert = new SoftAssert();
        String name = newInventoryViewPage.getPropertyPanel(PROPERTY_PANEL_WIDGET_ID).getPropertyValue(REMARKS);
        softAssert.assertEquals(name, REMARKS, "There is a different value of remarks: " + name);
        softAssert.assertAll();
    }

    @Step
    public void searchRackAttributesInPropertiesTab() {
        softAssert = new SoftAssert();
        PropertyPanel propertyPanel = PropertyPanel.createById(driver, webDriverWait, PROPERTY_PANEL_WIDGET_ID);
        String bcname = propertyPanel.getPropertyValue(NAME_COL_ID);
        hierarchyViewPage.selectNodeByLabelsPath(bcname + RACK_PATH + dateAndTime);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        hierarchyViewPage.unselectFirstObject();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        propertyPanel = PropertyPanel.createById(driver, webDriverWait, PROPERTY_PANEL_WIDGET_ID);
        String name = propertyPanel.getPropertyValue(MODEL_MANUFACTURER_ID);
        softAssert.assertEquals(name, "Generic", "There is a different value of Rack Manufacturer Model Name: " + name);
        name = propertyPanel.getPropertyValue(MODEL_NAME_ID);
        softAssert.assertTrue(name.contains("19"), "There is a different value of Rack Model Name: " + name);
        name = propertyPanel.getPropertyValue(HEIGHT_FIELD);
        softAssert.assertNotEquals(name, RACK_HEIGHT);
        name = propertyPanel.getPropertyValue(WIDTH_FIELD);
        softAssert.assertNotEquals(name, RACK_WIDTH);
        name = propertyPanel.getPropertyValue(DEPTH_FIELD);
        softAssert.assertNotEquals(name, RACK_DEPTH);
        softAssert.assertAll();
        hierarchyViewPage.unselectNodeByLabelsPath(bcname + RACK_PATH + dateAndTime);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        hierarchyViewPage.selectFirstObject();
    }

    @Step("Verification of Sublocation Precise Location in Property Panel")
    public void searchPreciseLocationByName() {
        softAssert = new SoftAssert();
        String name = newInventoryViewPage.getPropertyPanel(PROPERTY_PANEL_WIDGET_ID).getPropertyValue(PRECISE_LOCATION_ID).toLowerCase();
        softAssert.assertTrue(name.contains(LOCATION_NAME), "There is a different value of Precise Location: " + name);
        softAssert.assertAll();
    }

    @Step("Verification of Rack Manufacturer Model Name in Property Panel")
    private void searchRackManufacturerModelByModelName() {
        softAssert = new SoftAssert();
        String name = newInventoryViewPage.getPropertyPanel(PROPERTY_PANEL_WIDGET_ID).getPropertyValue(MODEL_MANUFACTURER_ID);
        softAssert.assertEquals(name, "Generic", "There is a different value of Rack Manufacturer Model Name: " + name);
        softAssert.assertAll();
    }

    @Step("Verification of Rack Model Name in Property Panel")
    private void searchRackModelByModelName() {
        softAssert = new SoftAssert();
        String name = newInventoryViewPage.getPropertyPanel(PROPERTY_PANEL_WIDGET_ID).getPropertyValue(MODEL_NAME_ID);
        softAssert.assertTrue(name.contains("19"), "There is a different value of Rack Model Name: " + name);
        softAssert.assertAll();
    }

    @Step("Verification of Rack Height in Property Panel")
    private void searchRackHeightByName() {
        softAssert = new SoftAssert();
        String name = newInventoryViewPage.getPropertyPanel(PROPERTY_PANEL_WIDGET_ID).getPropertyValue(HEIGHT_FIELD);
        softAssert.assertNotEquals(name, RACK_HEIGHT);
        softAssert.assertAll();
    }

    @Step("Verification of Rack Width in Property Panel")
    private void searchRackWidthByName() {
        softAssert = new SoftAssert();
        String name = newInventoryViewPage.getPropertyPanel(PROPERTY_PANEL_WIDGET_ID).getPropertyValue(WIDTH_FIELD);
        softAssert.assertNotEquals(name, RACK_WIDTH);
        softAssert.assertAll();
    }

    @Step("Verification of Rack Depth in Property Panel")
    private void searchRackDepthByName() {
        softAssert = new SoftAssert();
        String name = newInventoryViewPage.getPropertyPanel(PROPERTY_PANEL_WIDGET_ID).getPropertyValue(DEPTH_FIELD);
        softAssert.assertNotEquals(name, RACK_DEPTH);
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
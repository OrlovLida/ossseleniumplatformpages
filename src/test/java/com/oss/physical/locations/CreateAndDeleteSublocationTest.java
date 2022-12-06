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
import org.testng.Assert;
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
    private static final String FLOOR_NAME = "selenium test floor_";
    private static final String ROOM_NAME = "selenium test room_";
    private static final String RACK_NAME = "selenium test rack_";
    private static final String PRECISE_LOCATION = "B";
    private static final String RACK_MODEL = "Generic 19";
    private static final String DESCRIPTION = "description";
    private static final String REMARKS = "remarks";
    private static final String RACK_HEIGHT = "0";
    private static final String HEIGHT_FIELD = "height";
    private static final String WIDTH_FIELD = "width";
    private static final String DEPTH_FIELD = "depth";
    private static final String RACK_WIDTH = "0";
    private static final String RACK_DEPTH = "0";
    private NewInventoryViewPage newInventoryViewPage;
    private SoftAssert softAssert;
    private HierarchyViewPage hierarchyViewPage;
    private TableWidget tableWidget;
    private static final String TABLE_SUBLOCATIONS = "SublocationsWidget";
    private static final String TAB_PATTERN = "tab_%s";
    private static final String NAME_COL_ID = "name";
    private static final String TYPE_COL_ID = "type";
    private static final String DESCRIPTION_COL_ID = "description";
    private static final String REMARKS_COL_ID = "remarks";
    private static final String LOCATION_COL_ID = "location.name";
    private final String dateAndTime = new SimpleDateFormat("dd-MM-yyyy+HH:mm").format(Calendar.getInstance().getTime());
    private final static String PROPERTY_PANEL_WIDGET_ID = "PropertyPanelWidget";
    private static final String DELETE_SUBLOCATION_ACTION_ID = "RemoveSublocationWizardAction";
    private static final String RACK_PATH = ".Locations.Rack." + RACK_NAME;

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
        searchObjectTypePage.searchType(LOCATION_TYPE);
        newInventoryViewPage.searchObject(LOCATION_NAME);
        newInventoryViewPage.selectFirstRow();
    }

    @Test(priority = 3, dependsOnMethods = {"searchLocationAndOpenNewInventoryView"})
    @Description("Open wizard to create Sublocations (Floor,Rack, Room) With Full Attributes and check confirmation system message")
    public void createSublocations() {
        String[] sublocationType = {FLOOR_TYPE, ROOM_TYPE, RACK_TYPE};
        int numberOfType = sublocationType.length;
        String[] sublocationName = {FLOOR_NAME, ROOM_NAME, RACK_NAME};
        for (int i = 0; i < numberOfType; i++) {
            System.out.println("I'm starting to create Sublocation: " + sublocationType[i] + "...");
            openWizardToCreateSublocation();
            SublocationWizardPage sublocationWizardPage = new SublocationWizardPage(driver);
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            sublocationWizardPage.setSublocationType(sublocationType[i]);
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            if (i >= 1) {
                sublocationWizardPage.setPreciseLocation(PRECISE_LOCATION);
            }
            sublocationWizardPage.setSublocationName(sublocationName[i] + dateAndTime);
            if (i == 2) {
                sublocationWizardPage.setSublocationModel(RACK_MODEL);
                DelayUtils.waitForPageToLoad(driver, webDriverWait);
                checkAutoCompleteRackAttributes(sublocationWizardPage);
            }
            sublocationWizardPage.setDescription(DESCRIPTION);
            sublocationWizardPage.setRemarks(REMARKS);
            sublocationWizardPage.clickNext();
            sublocationWizardPage.create();
            checkMessageTextForCreatedSublocation();
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
        tableWidget.fullTextSearch(dateAndTime);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        String[] sublocationType = {FLOOR_TYPE, ROOM_TYPE, RACK_TYPE};
        int numberOfType = sublocationType.length;
        softAssert = new SoftAssert();
        for (int i = 0; i < numberOfType; i++) {
            System.out.println("Verification of Sublocation" + " attributes on HV = " + sublocationType[i]);
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            tableWidget.selectRow(i);
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            String rowValue = tableWidget.getCellValue(i, NAME_COL_ID);
            softAssert.assertTrue(rowValue.contains(dateAndTime), "Name was not calculated correctly: " + rowValue);
            rowValue = tableWidget.getCellValue(i, LOCATION_COL_ID).toLowerCase();
            softAssert.assertTrue(rowValue.contains(LOCATION_NAME), "There is a different value of Precise Location " + rowValue);
            rowValue = tableWidget.getCellValue(i, DESCRIPTION_COL_ID);
            Assert.assertEquals(rowValue, "description", "There is a different value of description: " + rowValue);
            rowValue = tableWidget.getCellValue(i, REMARKS_COL_ID);
            Assert.assertEquals(rowValue, "remarks", "There is a different value of remarks: " + rowValue);
            if (i == 0) {
                rowValue = tableWidget.getCellValue(i, TYPE_COL_ID);
                softAssert.assertEquals(rowValue, sublocationType[i], "There is a different type of sublocation: " + rowValue);
                tableWidget.unselectRow(i);
            } else if (i == 1) {
                rowValue = tableWidget.getCellValue(i, TYPE_COL_ID);
                softAssert.assertEquals(rowValue, sublocationType[i], "There is a different type of sublocation: " + rowValue);
                tableWidget.unselectRow(i);
            } else {
                rowValue = tableWidget.getCellValue(i, TYPE_COL_ID);
                softAssert.assertEquals(rowValue, sublocationType[i], "There is a different type of sublocation: " + rowValue);
                searchRackAttributesInPropertiesTab();
                DelayUtils.waitForPageToLoad(driver, webDriverWait);
            }
        }
        softAssert.assertAll();
    }

    @Test(priority = 6, dependsOnMethods = {"checkSublocationsAttributesOnHierarchyView"})
    @Description("Open Inventory View for selected Sublocations")
    public void moveToInventoryView() {
        tableWidget = getTableWidget();
        tableWidget.fullTextSearch(dateAndTime);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        tableWidget.selectAllRows();
        tableWidget.callAction(ActionsContainer.SHOW_ON_GROUP_ID, "InventoryView");
    }

    @Test(priority = 7, dependsOnMethods = {"moveToInventoryView"})
    @Description("Check Attributes of Sublocations on IV")
    public void checkSublocationsAttributesOnInventoryView() {
        String[] sublocationType = {FLOOR_TYPE, ROOM_TYPE, RACK_TYPE};
        int numberOfType = sublocationType.length;
        softAssert = new SoftAssert();
        for (int i = 0; i < numberOfType; i++) {
            System.out.println("Verification of Sublocation " + " attributes = " + sublocationType[i]);
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            newInventoryViewPage.selectRow(TYPE_COL_ID, sublocationType[i]);
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            searchNameByValueOnIV();
            searchDescriptionByValueOnIV();
            searchRemarksByValueOnIV();
            if (i == 0) {
                String name = newInventoryViewPage.getPropertyPanel(PROPERTY_PANEL_WIDGET_ID).getPropertyValue("type");
                Assert.assertEquals(name, sublocationType[i], "There is a different type of location: " + name);
                newInventoryViewPage.unselectObjectByRowId(i);
            } else if (i == 1) {
                String name = newInventoryViewPage.getPropertyPanel(PROPERTY_PANEL_WIDGET_ID).getPropertyValue("type");
                Assert.assertEquals(name, sublocationType[i], "There is a different type of location: " + name);
                newInventoryViewPage.unselectObjectByRowId(i);
            } else {
                String name = newInventoryViewPage.getPropertyPanel(PROPERTY_PANEL_WIDGET_ID).getPropertyValue("type");
                Assert.assertEquals(name, sublocationType[i], "There is a different type of location: " + name);
                searchPreciseLocationByName();
                searchRackManufacturerModelByModelName();
                searchRackModelByModelName();
                searchRackHeightByName();
                searchRackWidthByName();
                searchRackDepthByName();
                newInventoryViewPage.unselectObjectByRowId(i);
            }
        }
        softAssert.assertAll();
    }

    @Test(priority = 8, dependsOnMethods = {"checkSublocationsAttributesOnInventoryView"})
    @Description("Delete all created Sublocations and check confirmation system messages")
    public void deleteSublocations() {
        String[] sublocationType = {FLOOR_TYPE, ROOM_TYPE, RACK_TYPE,};
        int numberOfType = sublocationType.length;
        for (int i = 0; i < numberOfType; i++) {
            newInventoryViewPage.selectRow(TYPE_COL_ID, sublocationType[i]);
            newInventoryViewPage.callAction(ActionsContainer.EDIT_GROUP_ID, DELETE_SUBLOCATION_ACTION_ID);
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            ConfirmationBox.create(driver, webDriverWait).clickButtonByLabel("Delete");
            checkMessageTextForRemovedSublocation();
            closeSystemMessage();
            newInventoryViewPage.unselectObjectByRowId(i);
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
        }
    }

    @Test(priority = 9, dependsOnMethods = {"deleteSublocations"})
    @Description("Check if Inventory View is empty from objects")
    public void refreshInventoryView() {
        newInventoryViewPage.refreshMainTable();
        softAssert = new SoftAssert();
        Assert.assertTrue(newInventoryViewPage.getMainTable().hasNoData(), "Some objects are still visible on view.");
        softAssert.assertAll();
    }

    @Step("Open Create Sublocation Wizard from Physical Location context")
    public void openWizardToCreateSublocation() {
        newInventoryViewPage.callAction(ActionsContainer.CREATE_GROUP_ID, "CreateSublocationInLocationWizardAction");
    }

    public void checkAutoCompleteRackAttributes(SublocationWizardPage sublocationWizardPage) {
        softAssert = new SoftAssert();
        String componentValue = sublocationWizardPage.getComponentValue(HEIGHT_FIELD);
        Assert.assertFalse(componentValue.isEmpty(), "There is empty field with height!" + componentValue);
        componentValue = sublocationWizardPage.getComponentValue(WIDTH_FIELD);
        Assert.assertFalse(componentValue.isEmpty(), "There is empty field with width!" + componentValue);
        componentValue = sublocationWizardPage.getComponentValue(DEPTH_FIELD);
        Assert.assertFalse(componentValue.isEmpty(), "There is empty field with depth!" + componentValue);
        softAssert.assertAll();
    }

    @Step("Verification of Sublocation Name in Property Panel")
    public void searchNameByValueOnIV() {
        softAssert = new SoftAssert();
        String name = newInventoryViewPage.getPropertyPanel(PROPERTY_PANEL_WIDGET_ID).getPropertyValue("name");
        Assert.assertTrue(name.contains(dateAndTime), "Name was not calculated correctly: " + name);
        softAssert.assertAll();
    }

    @Step("Verification of Sublocation Description in Property Panel")
    public void searchDescriptionByValueOnIV() {
        softAssert = new SoftAssert();
        String name = newInventoryViewPage.getPropertyPanel(PROPERTY_PANEL_WIDGET_ID).getPropertyValue("description");
        Assert.assertEquals(name, "description", "There is a different value of description: " + name);
        softAssert.assertAll();
    }

    @Step("Verification of Sublocation Name in Property Panel")
    public void searchRemarksByValueOnIV() {
        softAssert = new SoftAssert();
        String name = newInventoryViewPage.getPropertyPanel(PROPERTY_PANEL_WIDGET_ID).getPropertyValue("remarks");
        Assert.assertEquals(name, "remarks", "There is a different value of remarks: " + name);
        softAssert.assertAll();
    }

    @Step
    public void searchRackAttributesInPropertiesTab() {
        softAssert = new SoftAssert();
        PropertyPanel propertyPanel = PropertyPanel.createById(driver, webDriverWait, PROPERTY_PANEL_WIDGET_ID);
        String bcname = propertyPanel.getPropertyValue("name");
        hierarchyViewPage.selectNodeByLabelsPath(bcname + RACK_PATH + dateAndTime);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        hierarchyViewPage.unselectFirstObject();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        propertyPanel = PropertyPanel.createById(driver, webDriverWait, PROPERTY_PANEL_WIDGET_ID);
        String name = propertyPanel.getPropertyValue("model.manufacturer");
        Assert.assertEquals(name, "Generic", "There is a different value of Rack Manufacturer Model Name: " + name);
        name = propertyPanel.getPropertyValue("model.name");
        Assert.assertTrue(name.contains("19"), "There is a different value of Rack Model Name: " + name);
        name = propertyPanel.getPropertyValue("height");
        Assert.assertNotEquals(name, RACK_HEIGHT);
        name = propertyPanel.getPropertyValue("width");
        Assert.assertNotEquals(name, RACK_WIDTH);
        name = propertyPanel.getPropertyValue("depth");
        Assert.assertNotEquals(name, RACK_DEPTH);
        softAssert.assertAll();
        hierarchyViewPage.unselectNodeByLabelsPath(bcname + RACK_PATH + dateAndTime);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        hierarchyViewPage.selectFirstObject();
    }


    @Step("Verification of Sublocation Precise Location in Property Panel")
    public void searchPreciseLocationByName() {
        softAssert = new SoftAssert();
        String name = newInventoryViewPage.getPropertyPanel(PROPERTY_PANEL_WIDGET_ID).getPropertyValue("preciseLocation.name").toLowerCase();
        Assert.assertTrue(name.contains(LOCATION_NAME), "There is a different value of Precise Location: " + name);
        softAssert.assertAll();
    }

    @Step("Verification of Rack Manufacturer Model Name in Property Panel")
    private void searchRackManufacturerModelByModelName() {
        softAssert = new SoftAssert();
        String name = newInventoryViewPage.getPropertyPanel(PROPERTY_PANEL_WIDGET_ID).getPropertyValue("model.manufacturer");
        Assert.assertEquals(name, "Generic", "There is a different value of Rack Manufacturer Model Name: " + name);
        softAssert.assertAll();
    }

    @Step("Verification of Rack Model Name in Property Panel")
    private void searchRackModelByModelName() {
        softAssert = new SoftAssert();
        String name = newInventoryViewPage.getPropertyPanel(PROPERTY_PANEL_WIDGET_ID).getPropertyValue("model.name");
        Assert.assertTrue(name.contains("19"), "There is a different value of Rack Model Name: " + name);
        softAssert.assertAll();
    }

    @Step("Verification of Rack Height in Property Panel")
    private void searchRackHeightByName() {
        softAssert = new SoftAssert();
        String name = newInventoryViewPage.getPropertyPanel(PROPERTY_PANEL_WIDGET_ID).getPropertyValue("height");
        Assert.assertNotEquals(name, RACK_HEIGHT);
        softAssert.assertAll();
    }

    @Step("Verification of Rack Width in Property Panel")
    private void searchRackWidthByName() {
        softAssert = new SoftAssert();
        String name = newInventoryViewPage.getPropertyPanel(PROPERTY_PANEL_WIDGET_ID).getPropertyValue("width");
        Assert.assertNotEquals(name, RACK_WIDTH);
        softAssert.assertAll();
    }

    @Step("Verification of Rack Depth in Property Panel")
    private void searchRackDepthByName() {
        softAssert = new SoftAssert();
        String name = newInventoryViewPage.getPropertyPanel(PROPERTY_PANEL_WIDGET_ID).getPropertyValue("depth");
        Assert.assertNotEquals(name, RACK_DEPTH);
        softAssert.assertAll();
    }

    @Step("Get Table Widget")
    private TableWidget getTableWidget() {
        openTab();
        return TableWidget.createById(driver, CreateAndDeleteSublocationTest.TABLE_SUBLOCATIONS, webDriverWait);
    }

    @Step("Open tab by Id")
    private void openTab() {
        TabsWidget tabsWidget = hierarchyViewPage.getBottomTabsWidget();
        tabsWidget.selectTabById(String.format(TAB_PATTERN, CreateAndDeleteSublocationTest.TABLE_SUBLOCATIONS));
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
    private void checkMessageTextForCreatedSublocation() {
        softAssert = new SoftAssert();
        SystemMessageInterface systemMessage = create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        softAssert.assertEquals(messages.size(), 1, "There is no single message");
        softAssert.assertEquals(messages.get(0).getMessageType(), SystemMessageContainer.MessageType.SUCCESS, "There is no successful message");
        softAssert.assertTrue(messages.get(0).getText().contains("Sublocation created successfully, click here to open Hierarchy View."), "Returned message contains different content.");
        softAssert.assertAll();
    }

    @Step("Check message text for removed Sublocation")
    private void checkMessageTextForRemovedSublocation() {
        softAssert = new SoftAssert();
        SystemMessageInterface systemMessage = create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        softAssert.assertEquals(messages.size(), 1, "There is no single message");
        softAssert.assertEquals(messages.get(0).getMessageType(), SystemMessageContainer.MessageType.SUCCESS, "There is no successful message");
        softAssert.assertTrue(messages.get(0).getText().contains("Sublocation has been removed."), "Returned message contains different content.");
        softAssert.assertAll();
    }

    @Step("Close system message")
    private void closeSystemMessage() {
        create(driver, webDriverWait).close();
    }

}